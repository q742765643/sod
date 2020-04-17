package com.piesat.dm.rpc.service.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.dao.dataclass.LogicDefineDao;
import com.piesat.dm.dao.datatable.*;
import com.piesat.dm.entity.database.DatabaseEntity;
import com.piesat.dm.entity.datatable.ShardingEntity;
import com.piesat.dm.entity.datatable.TableColumnEntity;
import com.piesat.dm.entity.datatable.TableIndexEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.datatable.TableExportService;
import jnr.ffi.Struct;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
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
            typeList.addAll(mybatisQueryMapper.getDataTypeList(dataClassIdParam));
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
            String nowtime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String outFileName = databaseName+"_"+nowtime;
            if(sqlList!=null&&sqlList.size()>0){
                //判断导出一个文件还是多个文件
                if(exportType==1){
                    StringBuilder sb = new StringBuilder();
                    for(Map<String,Object> sqlMap : sqlList){
                        String sql = "--"+sqlMap.getOrDefault("DATA_SERVICE_ID","")
                                +"_"+sqlMap.getOrDefault("DATA_SERVICE_NAME","")
                                +"_"+sqlMap.getOrDefault("TABLE_NAME","")
                                +"\n"
                                +sqlMap.getOrDefault("TABLE_SQL","")
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
                        String sql = "--"+sqlMap.getOrDefault("DATA_SERVICE_ID","")
                                +"_"+sqlMap.getOrDefault("DATA_SERVICE_NAME","")
                                +"_"+sqlMap.getOrDefault("TABLE_NAME","")
                                +"\n"
                                +sqlMap.getOrDefault("TABLE_SQL","")
                                +"\n\n";
                        String fileName = sqlMap.getOrDefault("DATA_SERVICE_ID","")
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

    @Override
    public Map<String, Object> getExportMapSimple(String database_id, String data_class_ids) {
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
