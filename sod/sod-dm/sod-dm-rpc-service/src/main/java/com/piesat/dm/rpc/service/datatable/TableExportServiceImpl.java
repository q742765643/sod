package com.piesat.dm.rpc.service.datatable;

import com.piesat.common.config.DatabseType;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.dao.dataclass.LogicDefineDao;
import com.piesat.dm.dao.datatable.*;
import com.piesat.dm.entity.datatable.DataTableEntity;
import com.piesat.dm.entity.datatable.ShardingEntity;
import com.piesat.dm.entity.datatable.TableColumnEntity;
import com.piesat.dm.entity.datatable.TableIndexEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.datatable.TableExportService;
import com.piesat.dm.rpc.service.GrpcService;
import com.piesat.util.ResultT;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 表结构导出
 *
 * @author wulei
 * @date 2020年 2月 5日 10:40:38
 */
@Service
public class TableExportServiceImpl extends BaseService<TableIndexEntity> implements TableExportService {
    @Autowired
    private TableExportDao tableExportDao;
    @Autowired
    private DataTableDao dataTableDao;
    @Autowired
    private TableColumnDao tableColumnDao;
    @Autowired
    private TableIndexDao tableIndexDao;
    @Autowired
    private ShardingDao shardingDao;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private LogicDefineDao logicDefineDao;
    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    private GrpcService grpcService;

    @Override
    public BaseDao<TableIndexEntity> getBaseDao() {
        return null;
    }

    @Override
    public Map<String, Object> getExportMap(String physicalDBParam, String dataClassIdParam) {
        try {
            dataClassIdParam = "'"+dataClassIdParam.replaceAll(",", "','")+"'";
            System.out.println(dataClassIdParam);
            //获取导出的资料集合
            List<Map<String,Object>> dataList = mybatisQueryMapper.findByDataServiceId(dataClassIdParam,physicalDBParam);
            //循环获取table_id
            String tableId = "";
            if(dataList!=null&&dataList.size()>0){
                StringBuilder tableIdSB = new StringBuilder();
                for(Map<String,Object> map : dataList){
                    tableIdSB.append(map.get("ID")).append(",");
                }
                tableId = tableIdSB.toString();
                if(tableId.length()>0){
                    tableId = tableId.substring(0,tableId.length()-1);
                }
            }
            if(tableId.length()>0){
                tableId = "'"+tableId.replaceAll(",", "','")+"'";
            }else{
                return null;
            }
            //获取字段信息
            List<Map<String,Object>> columnList = new ArrayList<Map<String,Object>>();
            columnList.addAll(mybatisQueryMapper.findColumnByTableId(tableId));
            //获取资料索引信息
            List<Map<String,Object>> indexList = new ArrayList<Map<String,Object>>();
            indexList.addAll(mybatisQueryMapper.findIndexByTableId(tableId));
            //获取资料分库分表信息
            List<Map<String,Object>> sharedList = new ArrayList<Map<String,Object>>();
            sharedList.addAll(mybatisQueryMapper.findShardByTableId(tableId));
            for(int i=0; i<dataList.size(); i++){
                Map<String,Object> dataMap = dataList.get(i);
                String table_id = dataMap.get("ID").toString();
                //处理字段信息
                int columnNum = 0;
                List<Map<String,Object>> subColumnList = new ArrayList<Map<String,Object>>();
                for(int j=0; j<columnList.size(); j++){
                    Map<String,Object> columnMap = new HashMap<>();
                    Map<String,Object> column = columnList.get(j);
                    String id = column.get("TABLE_ID")+"";
                    if(table_id.equals(id)){
                        columnNum++;
                        columnMap.put("column_num", columnNum);
                        String ele_name = column.get("ELE_NAME")+"";
                        ele_name = ele_name.replaceAll("<", "");
                        ele_name = ele_name.replaceAll(">", "");
                        ele_name = ele_name.replaceAll("≥", "");
                        columnMap.put("ele_name", ele_name);
                        columnMap.put("db_ele_code", column.get("DB_ELE_CODE"));
                        String type = getStr(column.get("TYPE")+"");
                        columnMap.put("type", type);
                        //map.put("accuracy", map.get("ACCURACY"));
                        String accuracy = column.getOrDefault("ACCURACY","")+"";
                        if(StringUtils.isNotBlank(accuracy)){
                            accuracy = accuracy.replace(".", ",");
                            columnMap.put("type", column.get("TYPE")+"" + "("+accuracy+")");
                        }
                        String constraint = "";
                        if(Boolean.parseBoolean(column.get("IS_NULL").toString())){
                            constraint = "可为空";
                        }else{
                            constraint = "不为空";
                        }
                        if(Boolean.parseBoolean(column.get("IS_PRIMARY_KEY").toString())){
                            constraint = constraint+",主键";
                        }
                        columnMap.put("constraint", constraint);
                        columnMap.put("description", column.getOrDefault("NAME_CN",""));
                        subColumnList.add(columnMap);
                    }
                }
                dataMap.put("columnList", subColumnList);
                //处理索引信息
                List<Map<String,Object>> subIndexList = new ArrayList<Map<String,Object>>();
                int indexNum = 0;
                for(int j=0; j<indexList.size(); j++){
                    Map<String,Object> indexMap = new HashMap<>();
                    Map<String,Object> tableIndex = indexList.get(j);
                    String id = tableIndex.get("TABLE_ID")+"";
                    if(table_id.equals(id)){
                        indexNum++;
                        indexMap.put("index_num", indexNum);
                        indexMap.put("index_type", tableIndex.get("INDEX_TYPE"));
                        indexMap.put("index_name", tableIndex.get("INDEX_NAME"));
                        indexMap.put("index_column", tableIndex.get("INDEX_COLUMN"));
                        subIndexList.add(indexMap);
                    }
                }
                dataMap.put("indexList", subIndexList);
                if (subIndexList.size()==0){
                    dataMap.put("haveIndex","N");
                }else {
                    dataMap.put("haveIndex","Y");
                }				//处理分区信息
                int sharedNum = 0;
                List<Map<String,Object>> subSharedList = new ArrayList<Map<String,Object>>();
                for(int j=0; j<sharedList.size(); j++){
                    Map<String,Object> shardingMap = new HashMap<>();
                    Map<String,Object> sharding = sharedList.get(j);
                    String id = sharding.get("TABLE_ID")+"";
                    if(table_id.equals(id)){
                        sharedNum++;
                        shardingMap.put("shared_num", sharedNum);
                        String type = Integer.parseInt(sharding.get("SHARDING_TYPE")+"")==0?"分库键":"分表键";
                        shardingMap.put("shared_type", type);
                        shardingMap.put("shared_column", sharding.get("COLUMN_NAME"));
                        subSharedList.add(shardingMap);
                    }
                }
                dataMap.put("sharedList", subSharedList);
                if (subSharedList.size()==0){
                    dataMap.put("haveShared","N");
                }else {
                    dataMap.put("haveShared","Y");
                }
            }

            //获取资料分类集合
            List<Map<String,Object>> typeList = new ArrayList<Map<String,Object>>();
            if("postgresql".equals(DatabseType.type)){
                typeList.addAll(mybatisQueryMapper.getDataTypeListPostgresql(dataClassIdParam));
            }else{
                typeList.addAll(mybatisQueryMapper.getDataTypeList(dataClassIdParam));
            }
            for(int i=0; i<typeList.size(); i++){
                Map<String,Object> typemap = typeList.get(i);
                String typeCode = typemap.get("DATA_CLASS_ID").toString();
                typemap.put("menu_type_name", (i+1)+"."+typemap.get("CLASS_NAME"));
                List<Map<String,Object>> typeDataList = new ArrayList<Map<String,Object>>();
                int num = 0;

                for(int j=0; j<dataList.size(); j++){
                    int num_min = 0;
                    Map<String,Object> dataMap = dataList.get(j);
                    /*ModeArea m = new ModeArea();
                    m.setArea_id(dataMap.get("DATA_CLASS_ID").toString());
                    ModeArea modearea = null;
                    if ("FIDB".equals(physicalDBParam))	{
                        modearea = this.modeEleDefDao.getAreaById(m);
                        num_min = 1;
                    }*/


                    if(dataMap.get("DATA_SERVICE_ID").toString().startsWith(typeCode)){
                        num++;
                        //标题赋值
                        dataMap.put("menu_data_name", (i+1)+"."+num+"."+getStr(dataMap.get("NAME_CN").toString()));
                        /*if (num_min == 1){
                            dataMap.put("menu_sa_description_name", (i+1)+"."+num+"."+(num_min)+".存储目录");
                            dataMap.put("directory_specification", modearea == null ? "" : modearea.getArea_name());
                            dataMap.put("haveDS","Y");
                        }else {
                            dataMap.put("haveDS","N");
                        }*/
                        dataMap.put("menu_sub_description", (i+1)+"."+num+"."+(num_min+1)+".表内容概述");
                        dataMap.put("menu_sub_table", (i+1)+"."+num+"."+(num_min+2)+".表结构信息");
                        dataMap.put("menu_sub_index", (i+1)+"."+num+"."+(num_min+3)+".索引信息");
                        dataMap.put("menu_sub_shared", (i+1)+"."+num+"."+(num_min+4)+".分库分表信息");
                        //表内容表格
                        dataMap.put("data_name", getStr(dataMap.get("NAME_CN").toString()));
                        dataMap.put("table_name", dataMap.get("TABLE_NAME").toString().toUpperCase());
                        dataMap.put("d_data_id", dataMap.get("DATA_SERVICE_ID").toString().toUpperCase());
                        dataMap.put("primary_key","");
                        typeDataList.add(dataMap);
                    }
                }
                typemap.put("dataInfoList", typeDataList);
            }
            Map<String,Object> infoMap = new HashMap<String, Object>();
            infoMap.put("typeInfoList", typeList);
            infoMap.put("word_date", new SimpleDateFormat("yyyy年M月d日").format(new Date()));
            return infoMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getLogicName(String database_logic) {
        return null;
    }

    @Override
    public String getDatabaseName(String database_id) {
        Map<String, Object> databaseMap = mybatisQueryMapper.findByDatabaseDefine(database_id);
        return databaseMap.getOrDefault("DATABASE_NAME","").toString();
    }

    @Override
    public Map<String, Object> exportSqlFile(String databaseId, String dataClassIds, Integer exportType,String outFilePath) {
        Map<String,Object> resultMap = new HashMap<>();
        try{
            dataClassIds = "'"+dataClassIds.replaceAll(",", "','")+"'";
            System.out.println(dataClassIds);
            //获取数据列表
            List<Map<String,Object>> sqlList = mybatisQueryMapper.getSqlList(databaseId,dataClassIds);
            String databaseName = getDatabaseName(databaseId);
//            String nowtime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String outFileName = databaseName+"_"+uuid;
            if(sqlList!=null&&sqlList.size()>0){
                //判断导出一个文件还是多个文件
                if(exportType==1){
                    StringBuilder sb = new StringBuilder();
                    for(Map<String,Object> sqlMap : sqlList){
                        String sqlContent = "";
                        if(sqlMap.get("TABLE_SQL")!=null){
                            Clob sqlClob = (Clob) sqlMap.get("TABLE_SQL");
                            sqlContent = ClobToString(sqlClob);
                        }else {
                            String tableId = sqlMap.get("ID").toString();
                            ResultT sql = this.grpcService.getSql(tableId, databaseId);
                            if (sql.getCode() == 200){
                                Object data = sql.getData();
                                if (data instanceof HashMap){
                                    String createSql = ((HashMap) data).get("createSql").toString();
                                    if (StringUtils.isNotEmpty(createSql)){
                                        sqlContent = createSql;
                                    }
                                }
                            }else {
                                sqlContent = "--"+sql.getMsg()+"--\n\n";
                            }
                        }
                        String sql = "--"+sqlMap.getOrDefault("DATA_SERVICE_ID","")
                                +"_"+sqlMap.getOrDefault("DATA_SERVICE_NAME","")
                                +"_"+sqlMap.getOrDefault("TABLE_NAME","")
                                +"\n"
                                +sqlContent
                                +"\n\n";
                        sb.append(sql);
                    }
                    File file = new File(outFilePath+"/"+outFileName+".sql");
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fileWriter);
                    bw.write(sb.toString());
                    bw.close();
                    resultMap.put("fileName",file.getName());
                    resultMap.put("filePath",file.getAbsolutePath());
                }else if(exportType==2){
                    List<File> fileList = new ArrayList<>();
                    for(Map<String,Object> sqlMap : sqlList){
                        String sqlContent = "";
                        if(sqlMap.get("TABLE_SQL")!=null){
                            Clob sqlClob = (Clob) sqlMap.get("TABLE_SQL");
                            sqlContent = ClobToString(sqlClob);
                        }else {
                            String tableId = sqlMap.get("ID").toString();
                            ResultT sql = this.grpcService.getSql(tableId, databaseId);
                            if (sql.getCode() == 200){
                                Object data = sql.getData();
                                if (data instanceof HashMap){
                                    String createSql = ((HashMap) data).get("createSql").toString();
                                    if (StringUtils.isNotEmpty(createSql)){
                                        sqlContent = createSql;
                                    }
                                }
                            }else {
                                sqlContent = "--"+sql.getMsg()+"--\n\n";
                            }
                        }

                        String sql = "--"+sqlMap.getOrDefault("DATA_SERVICE_ID","")
                                +"_"+sqlMap.getOrDefault("DATA_SERVICE_NAME","")
                                +"_"+sqlMap.getOrDefault("TABLE_NAME","")
                                +"\n"
                                +sqlContent
                                +"\n\n";
                        String fileName = sqlMap.getOrDefault("ID","")
                                +"_"+sqlMap.getOrDefault("DATA_SERVICE_ID","")
                                +"_"+sqlMap.getOrDefault("DATA_SERVICE_NAME","")
                                +"_"+sqlMap.getOrDefault("TABLE_NAME","")+".sql";
                        File file = new File(outFilePath+"/"+fileName);
                        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
                        BufferedWriter bw = new BufferedWriter(fileWriter);
                        bw.write(sql);
                        bw.close();
                        fileList.add(file);
                    }
                    FileOutputStream fos = new FileOutputStream(new File(outFilePath+"/"+outFileName+".zip"));
                    toZip(fileList,fos);
                    resultMap.put("fileName",outFileName+".zip");
                    resultMap.put("filePath",outFilePath+"/"+outFileName+".zip");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

    public String ClobToString(Clob clob) throws SQLException, IOException {
        String ret = "";
        Reader read= clob.getCharacterStream();
        BufferedReader br = new BufferedReader(read);
        String s = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (s != null) {
            sb.append(s);
            s = br.readLine();
        }
        ret = sb.toString();
        if(br != null){
            br.close();
        }
        if(read != null){
            read.close();
        }
        return ret;
    }

    @Override
    public Map<String, Object> getExportMapSimple(String database_id, String data_class_ids, String columns, String index, String shard) {
        try{
            String[] data_class_id_Array = data_class_ids.split(",");
            // 工具类所用map读取
            Map<String, Object> dataMapss = new HashMap<String, Object>();
            // 最外层用于存放map的list集合
            List<Map<String, Object>> arra = new ArrayList<Map<String, Object>>();
            int titles = 1;
            // 根据传过来的四级编码循环多表导出
            for (String data_class_id : data_class_id_Array) {
                Map<String, Object> dataMaps = new HashMap<String, Object>();

                List<DataTableEntity> dataTableList = dataTableDao.findByDataServiceIdAndClassLogicId(data_class_id, database_id);

                List<Map<String, Object>> KV = new ArrayList<Map<String, Object>>();

                for (DataTableEntity dataTable : dataTableList) {
                    Map<String, Object> dataMap = new HashMap<String, Object>();

                    String table_id = dataTable.getId();
                    String table_name = dataTable.getTableName();
                    String data_service_name = dataTable.getDataServiceName();
                    List<TableColumnEntity> tableColumnList = tableColumnDao.findByTableId(table_id);

                    dataMap.put("titles", titles++);
                    dataMap.put("cnName", data_service_name);
                    if(table_name==null){
                        dataMap.put("tableName", " ");
                    }else{
                        dataMap.put("tableName", table_name);
                    }

                    // 表结构部分
                    List<Map<String, Object>> listInfo = new ArrayList<Map<String, Object>>();
                    if (StringUtils.isBlank(columns)) {
                        for (TableColumnEntity tableColumn : tableColumnList) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("id", " ");
                            map.put("db_ele_code", " ");
                            map.put("c_element_code", " ");
                            map.put("user_ele_code", " ");
                            map.put("ele_name", " ");
                            map.put("type", " ");
                            map.put("accuracy", " ");
                            map.put("unit", " ");
                            map.put("unit_cn", " ");
                            map.put("is_null", " ");
                            map.put("is_update", " ");
                            map.put("is_show", " ");
                            map.put("is_premary_key", " ");
                            map.put("name_cn", " ");
                            map.put("is_manager", " ");
                            map.put("serial_number", " ");
                            listInfo.add(map);
                        }
                    } else if (columns.length() >= 1) {
                        List<String> exportColumn = Arrays.asList(columns.split(","));
                        int num = 1;
                        for (TableColumnEntity tableColumn : tableColumnList) {
                            Map<String, Object> map = new HashMap<String, Object>();

                            map.put("id", num++);
                            if (exportColumn.contains("db_ele_code")) {
                                String db_ele_code = tableColumn.getDbEleCode();
                                if(db_ele_code==null){
                                    map.put("db_ele_code", " ");
                                }else{
                                    map.put("db_ele_code", db_ele_code);
                                }
                            } else {
                                map.put("db_ele_code", " ");
                            }
                            if (exportColumn.contains("c_element_code")) {
                                String c_element_code = tableColumn.getCElementCode();
                                if(c_element_code==null){
                                    map.put("c_element_code", " ");
                                }else{
                                    map.put("c_element_code", c_element_code);
                                }
                            } else {
                                map.put("c_element_code", " ");
                            }
                            if (exportColumn.contains("user_ele_code")) {
                                String user_ele_code = tableColumn.getUserEleCode();
                                if(user_ele_code==null){
                                    map.put("user_ele_code", " ");
                                }else{
                                    map.put("user_ele_code", user_ele_code);
                                }
                            } else {
                                map.put("user_ele_code", " ");
                            }
                            if (exportColumn.contains("ele_name")) {
                                String ele_name = tableColumn.getEleName();
                                if(ele_name==null){
                                    map.put("ele_name", " ");
                                }else{
                                    if (ele_name.contains("<")) {
                                        map.put("ele_name", ele_name.replace("<", "&lt;"));
                                    } else {
                                        map.put("ele_name", ele_name);
                                    }
                                }
                            } else {
                                map.put("ele_name", " ");
                            }
                            if (exportColumn.contains("type")) {
                                String type = tableColumn.getType();
                                if(type==null){
                                    map.put("type", " ");
                                }else{
                                    map.put("type", type);
                                }
                            } else {
                                map.put("type", " ");
                            }
                            if (exportColumn.contains("accuracy")) {
                                String accuracy = tableColumn.getAccuracy();
                                if(accuracy==null){
                                    map.put("accuracy", " ");
                                }else{
                                    map.put("accuracy", accuracy);
                                }
                            } else {
                                map.put("accuracy", " ");
                            }
                            if (exportColumn.contains("unit")) {
                                String unit = tableColumn.getUnit();
                                if(unit==null){
                                    map.put("unit", " ");
                                }else{
                                    map.put("unit", unit);
                                }
                                map.put("unit", unit);
                            } else {
                                map.put("unit", " ");
                            }
                            if (exportColumn.contains("unit_cn")) {
                                String unit_cn = tableColumn.getUnitCn();
                                if(unit_cn==null){
                                    map.put("unit_cn", " ");
                                }else{
                                    map.put("unit_cn", unit_cn);
                                }
                            } else {
                                map.put("unit_cn", " ");
                            }
                            if (exportColumn.contains("is_null")) {
                                String Is_null = String.valueOf(tableColumn.getIsNull());
                                if (Is_null.equals("true")) {
                                    map.put("is_null", "是");
                                } else {
                                    map.put("is_null", "否");
                                }
                            } else {
                                map.put("is_null", " ");
                            }
                            if (exportColumn.contains("is_update")) {
                                String Is_update = String.valueOf(tableColumn.getIsUpdate());
                                if (Is_update.equals("true")) {
                                    map.put("is_update", "是");
                                } else {
                                    map.put("is_update", "否");
                                }
                            } else {
                                map.put("is_update", " ");
                            }
                            if (exportColumn.contains("is_show")) {
                                String Is_show = String.valueOf(tableColumn.getIsShow());
                                if (Is_show.equals("true")) {
                                    map.put("is_show", "是");
                                } else {
                                    map.put("is_show", "否");
                                }
                            } else {
                                map.put("is_show", " ");
                            }
                            if (exportColumn.contains("is_premary_key")) {
                                String Is_premary_key = String.valueOf(tableColumn.getIsPrimaryKey());
                                if (Is_premary_key.equals("true")) {
                                    map.put("is_premary_key", "是");
                                } else {
                                    map.put("is_premary_key", "否");
                                }
                            } else {
                                map.put("is_premary_key", " ");
                            }
                            if (exportColumn.contains("name_cn")) {
                                String name_cn = tableColumn.getNameCn();
                                if(name_cn==null){
                                    map.put("name_cn", " ");
                                }else{
                                    map.put("name_cn", name_cn);
                                }
                            } else {
                                map.put("name_cn", " ");
                            }
                            if (exportColumn.contains("is_manager")) {
                                String Is_manager = String.valueOf(tableColumn.getIsManager());
                                if (Is_manager.equals("true")) {
                                    map.put("is_manager", "是");
                                } else {
                                    map.put("is_manager", "否");
                                }
                            } else {
                                map.put("is_manager", " ");
                            }
                            if (exportColumn.contains("serial_number")) {
                                map.put("serial_number", tableColumn.getSerialNumber());
                            } else {
                                map.put("serial_number", " ");
                            }
                            listInfo.add(map);
                        }
                    }
                    // 索引部分
                    List<Map<String, Object>> listIndex = new ArrayList<Map<String, Object>>();
                    List<TableIndexEntity> tableIndexList = tableIndexDao.findByTableId(table_id);
                    int indexNum = 1;

                    if (StringUtils.isBlank(index)) {
                        // List<String> exportColumnIndex =
                        // Arrays.asList(exportColumnIndexs);

                        for (TableIndexEntity tableIndex : tableIndexList) {
                            Map<String, Object> mapIndex = new HashMap<String, Object>();

                            mapIndex.put("indexId", " ");
                            mapIndex.put("indexType", " ");
                            mapIndex.put("indexName", " ");
                            mapIndex.put("indexColumn", " ");
                            listIndex.add(mapIndex);
                        }
                    } else if (index.equals("0")) {
                        for (TableIndexEntity tableIndex : tableIndexList) {
                            Map<String, Object> mapIndex = new HashMap<String, Object>();
                            mapIndex.put("indexId", indexNum++);
                            mapIndex.put("indexType", tableIndex.getIndexType());
                            mapIndex.put("indexName", tableIndex.getIndexName());
                            mapIndex.put("indexColumn", tableIndex.getIndexColumn());
                            listIndex.add(mapIndex);
                        }
                    }
                    // 分库分表部分
                    List<Map<String, Object>> listSharding = new ArrayList<Map<String, Object>>();
                    List<ShardingEntity> shardingList = shardingDao.findByTableId(table_id);
                    int shardNum = 1;
                    if (StringUtils.isBlank(shard)) {
                        for (ShardingEntity sharding : shardingList) {
                            Map<String, Object> mapSharding = new HashMap<String, Object>();

                            mapSharding.put("shardId", " ");
                            Integer sharding_type = sharding.getShardingType();
                            if (sharding_type==1) {
                                mapSharding.put("shardingType", "分表键");
                            } else {
                                mapSharding.put("shardingType", "分库键");
                            }
                            mapSharding.put("field", " ");
                            listSharding.add(mapSharding);
                        }
                    } else if (columns.equals("0")) {
                        for (ShardingEntity sharding : shardingList) {
                            Map<String, Object> mapSharding = new HashMap<String, Object>();
                            mapSharding.put("shardId", shardNum++);
                            Integer sharding_type = sharding.getShardingType();
                            if (sharding_type==1) {
                                mapSharding.put("shardingType", "分表键");
                            } else {
                                mapSharding.put("shardingType", "分库键");
                            }
                            mapSharding.put("field", sharding.getColumnName());
                            listSharding.add(mapSharding);
                        }
                    }
                    dataMap.put("listInfo", listInfo);
                    dataMap.put("listIndex", listIndex);
                    dataMap.put("listSharding", listSharding);
                    KV.add(dataMap);
                }
                dataMaps.put("KV", KV);
                arra.add(dataMaps);
            }
            dataMapss.put("arra", arra);
            return dataMapss;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void toZip(List<File> srcFiles , OutputStream out)throws RuntimeException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null ;
        try {
            zos = new ZipOutputStream(out);
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[512];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1){
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) +" ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils",e);
        }finally{
            if(zos != null){
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getStr(String str){
        return str.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
}
