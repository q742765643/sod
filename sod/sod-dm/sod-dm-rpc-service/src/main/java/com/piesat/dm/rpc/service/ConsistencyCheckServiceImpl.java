package com.piesat.dm.rpc.service;

import com.piesat.common.MapUtil;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.core.action.exc.abs.ExcAbs;
import com.piesat.dm.core.model.AuthorityVo;
import com.piesat.dm.core.model.ConnectVo;
import com.piesat.dm.core.parser.DatabaseInfo;
import com.piesat.dm.dao.ConsistencyCheckDao;
import com.piesat.dm.dao.dataclass.DataLogicDao;
import com.piesat.dm.dao.datatable.ShardingDao;
import com.piesat.dm.entity.ConsistencyCheckEntity;
import com.piesat.dm.entity.dataclass.DataClassAndTableEntity;
import com.piesat.dm.rpc.api.ConsistencyCheckService;
import com.piesat.dm.rpc.api.database.SchemaService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.api.datatable.TableColumnService;
import com.piesat.dm.rpc.dto.ConsistencyCheckDto;
import com.piesat.dm.rpc.dto.database.SchemaDto;
import com.piesat.dm.rpc.dto.datatable.DataTableInfoDto;
import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import com.piesat.dm.rpc.dto.datatable.TableIndexDto;
import com.piesat.dm.rpc.mapper.ConsistencyCheckMapper;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/14 10:43
 */
@Service
public class ConsistencyCheckServiceImpl extends BaseService<ConsistencyCheckEntity> implements ConsistencyCheckService {

    @Autowired
    private ConsistencyCheckDao consistencyCheckDao;
    @Autowired
    private ConsistencyCheckMapper consistencyCheckMapper;
    @Autowired
    private SchemaService schemaService;
    @Autowired
    private DataTableService dataTableService;
    @Autowired
    private DataLogicDao dataLogicDao;
    @Autowired
    private ShardingDao shardingDao;
    @Autowired
    private DatabaseInfo databaseInfo;
    @Autowired
    private TableColumnService tableColumnService;

    private String table_name = "table_name";
    private String column_name = "column_name";
    private String index_name = "index_name";
    private String index_column = "index_column";
    private String indexs = "indexs";

    @Override
    public BaseDao<ConsistencyCheckEntity> getBaseDao() {
        return this.consistencyCheckDao;
    }

    @Override
    public PageBean selectPageList(PageForm<ConsistencyCheckDto> pageForm) {
        //这里的逻辑十八弯
        ConsistencyCheckEntity consistencyCheckEntity = consistencyCheckMapper.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotBlank(consistencyCheckEntity.getDatabaseName())) {
            specificationBuilder.add("databaseName", SpecificationOperator.Operator.likeAll.name(), consistencyCheckEntity.getDatabaseName());
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        PageBean pageBean = this.getPage(specificationBuilder.generateSpecification(), pageForm, sort);
        List<ConsistencyCheckEntity> consistencyCheckEntities = (List<ConsistencyCheckEntity>) pageBean.getPageData();
        List<ConsistencyCheckDto> consistencyCheckDtos = consistencyCheckMapper.toDto(consistencyCheckEntities);
        if (consistencyCheckDtos != null && !consistencyCheckDtos.isEmpty()) {
            for (ConsistencyCheckDto consistencyCheckDto : consistencyCheckDtos) {
                SchemaDto schemaDto = schemaService.getDotById(consistencyCheckDto.getDatabaseId());
                consistencyCheckDto.setSchemaDto(schemaDto);
            }
        }
        pageBean.setPageData(consistencyCheckDtos);
        return pageBean;
    }

    @Override
    public ConsistencyCheckDto saveDto(ConsistencyCheckDto consistencyCheckDto) {
        ConsistencyCheckEntity consistencyCheckEntity = consistencyCheckMapper.toEntity(consistencyCheckDto);
        consistencyCheckEntity = this.saveNotNull(consistencyCheckEntity);
        return consistencyCheckMapper.toDto(consistencyCheckEntity);
    }

    @Override
    public void deleteById(String id) {
        this.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRecordByIds(List<String> ids) {
        this.deleteByIds(ids);
    }

    @Override
    public Map<String, List<List<String>>> downloadDfcheckFile(String databaseId) {
        ResultT<List<Map<String, Object>>> r = new ResultT();
        //获取数据库详细信息
        SchemaDto schemaDto = schemaService.getDotById(databaseId);
        Map<String, List<List<String>>> compileResult = new HashMap<>();
        compileResult.put("columnResult", new ArrayList<>());
        compileResult.put("indexResult", new ArrayList<>());
        compileResult.put("shardingResult", new ArrayList<>());
        ConnectVo coreInfo = schemaDto.getConnectVo();
        AuthorityVo a = new AuthorityVo(schemaDto.getSchemaName());
        ExcAbs exc = coreInfo.build(r);
        exc.allTables(a,r);
        if (r.isSuccess()){
            List<Map<String, Object>> maps = MapUtil.transformMapList( r.getData());
            ResultT<List<Map<String, Object>>> column = new ResultT();
            ResultT<List<Map<String, Object>>> index = new ResultT();
            maps.stream().map(e->String.valueOf(e.get(table_name))).forEach(tableName->{
                exc.columnInfo(a,column);
                exc.indexInfo(a,index);
                //物理库表字段信息
                Map<String, Map<String, Object>> columnInfos = null;
                if (column.isSuccess()){
                    columnInfos = column.getData().stream().collect(Collectors.toMap(k -> k.get(column_name).toString(), t -> t));
                }
                //物理库表索引和分库分表信息
                Map<String, Map<String, String>> indexAndShardings = new HashMap<>();
                if (index.isSuccess()){
                    Map<String, String> m = index.getData().stream().collect(Collectors.toMap(k -> k.get(index_name).toString(), t -> t.get(index_column).toString()));
                    indexAndShardings.put(indexs,m);
                }
                compareDifferences(databaseId, tableName.toUpperCase(), columnInfos, indexAndShardings, compileResult);
            });
        }
        exc.close();
        return compileResult;
    }


    public void compareDifferences(String databaseId, String tableName, Map<String, Map<String, Object>> columnInfos, Map<String, Map<String, String>> indexAndShardings, Map<String, List<List<String>>> compileResult) {
        //物理库数据表名	  存储编码	资料名称	  存储元数据中该表是否存在	存储元数据多余的字段   存储元数据缺失的字段  存储元数据类型需要修改的字段   存储元数据精度需要修改的字段   存储元数据和物理库非空设置不一致的字段   存储元数据和物理库主键设置不一致的字段
        //物理库数据表名   存储编码  资料名称  存储元数据中该表是否存在   存储元数据多余的索引   存储元数据缺失的索引  存储元数据需要修改的索引
        List<List<String>> columnResults = compileResult.get("columnResult");
        List<List<String>> indexResults = compileResult.get("indexResult");
        List<List<String>> shardingResults = compileResult.get("shardingResult");
        List<String> columnResult = null;
        List<String> indexResult = null;
        List<String> shardingResult = null;
        //元数据表信息
        List<Map<String, Object>> dataTableList = dataTableService.getByDatabaseIdAndTableName(databaseId, tableName);
        if (dataTableList == null || dataTableList.size() == 0) {
            //元数据表缺失
            columnResult = Arrays.asList("", "", "", "", "", "", "", "", "", "");
            columnResult.set(0, tableName);
            columnResult.set(3, "不存在");
            columnResults.add(columnResult);
            indexResult = Arrays.asList("", "", "", "", "", "", "");
            indexResult.set(0, tableName);
            indexResult.set(3, "不存在");
            indexResults.add(indexResult);
            shardingResult = Arrays.asList("", "", "", "", "", "", "", "", "", "");
            shardingResult.set(0, tableName);
            shardingResult.set(3, "不存在");
            shardingResults.add(shardingResult);
            return;
        }
        for (Map<String, Object> dataTable : dataTableList) {
            String tableId = (String) dataTable.get("ID");
            List<DataClassAndTableEntity> dataClassLogics = dataLogicDao.findByTableIdOrSubTableId(tableId, tableId);
            String dataClassId = "";
            if (dataClassLogics.size() > 0) {
                dataClassId = dataClassLogics.get(0).getDataClassId();
            }
            DataTableInfoDto dataTableDto = dataTableService.getDotById(tableId);
            columnResult = Arrays.asList("", "", "", "", "", "", "", "", "", "");
            columnResult.set(0, tableName);
            columnResult.set(1, dataClassId);
            columnResult.set(2, dataTableDto.getNameCn());
            columnResult.set(3, "存在");
            //以元数据库字段为准，遍历元数据字段
            Iterator<TableColumnDto> columnIterator = dataTableDto.getColumns().iterator();
            List<TableColumnDto> columnList = IteratorUtils.toList(columnIterator);
            for (TableColumnDto columnDto : columnList) {
                //TableColumnDto columnDto = columnIterator.next();
                Map<String, Object> columnOneInfo = columnInfos.get(columnDto.getDbEleCode().toUpperCase());
                //存储元数据多余字段
                if (columnOneInfo == null) {
                    if (StringUtils.isNotNullString(columnResult.get(4))) {
                        columnResult.set(4, columnResult.get(4) + ";" + columnDto.getDbEleCode());
                    } else {
                        columnResult.set(4, columnDto.getDbEleCode());
                    }
                    continue;
                }
                //判断类型和长度
                String dbColumnType = ((String) columnOneInfo.get("column_type")).replace(",", ".");// decimal(4,0)  varchar(200)  date
                String dbType = "";
                String dbAcc = "";
                if (dbColumnType.indexOf("(") != -1) {
                    dbType = dbColumnType.substring(0, dbColumnType.indexOf("("));
                    dbAcc = dbColumnType.substring(dbColumnType.indexOf("(") + 1, dbColumnType.length() - 1);
                } else {
                    dbType = dbColumnType;
                }
                if (!dbType.equalsIgnoreCase(columnDto.getType())) {
                    if (!("decimal".equalsIgnoreCase(columnDto.getType()) && "NUMERIC".equalsIgnoreCase(dbType)) &&
                            !("int".equalsIgnoreCase(columnDto.getType()) && "INTEGER".equalsIgnoreCase(dbType)) &&
                            !("timestamp".equalsIgnoreCase(columnDto.getType()) && "DATETIME".equalsIgnoreCase(dbType)) &&
                            !("NUMBER".equalsIgnoreCase(columnDto.getType()) && "NUMERIC".equalsIgnoreCase(dbType))) {
                        if (StringUtils.isNotNullString(columnResult.get(6))) {
                            columnResult.set(6, columnResult.get(6) + ";" + columnDto.getDbEleCode() + ":" + columnDto.getType() + "=>" + dbType);
                        } else {
                            columnResult.set(6, columnDto.getDbEleCode() + ":" + columnDto.getType() + "=>" + dbType);
                        }
                    }
                }
                if (!dbAcc.equalsIgnoreCase(columnDto.getAccuracy())) {
                    if (!((null == columnDto.getAccuracy()) && "".equalsIgnoreCase(dbAcc))) {
                        if (StringUtils.isNotNullString(columnResult.get(7))) {
                            columnResult.set(7, columnResult.get(7) + ";" + columnDto.getDbEleCode() + ":" + columnDto.getAccuracy() + "=>" + dbAcc);
                        } else {
                            columnResult.set(7, columnDto.getDbEleCode() + ":" + columnDto.getAccuracy() + "=>" + dbAcc);
                        }
                    }
                }
                //判断为空设置是否一致
                if ((columnDto.getIsNull() && (Integer) columnOneInfo.get("is_nullable") == 0) || (!columnDto.getIsNull() && (Integer) columnOneInfo.get("is_nullable") == 1)) {
                    if (StringUtils.isNotNullString(columnResult.get(8))) {
                        columnResult.set(8, columnResult.get(8) + ";" + columnDto.getDbEleCode());
                    } else {
                        columnResult.set(8, columnDto.getDbEleCode());
                    }
                }
            }

            //以物理库为基础，查找元数据库缺失的字段
            for (String codeName : columnInfos.keySet()) {
                boolean flag = false;
                for (TableColumnDto columnDto1 : columnList) {
                    //TableColumnDto columnDto1 = columnIterator.next();
                    if (codeName.equalsIgnoreCase(columnDto1.getDbEleCode())) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    if (StringUtils.isNotNullString(columnResult.get(5))) {
                        columnResult.set(5, columnResult.get(5) + ";" + codeName);
                    } else {
                        columnResult.set(5, codeName);
                    }
                }
            }
            columnResults.add(columnResult);


            indexResult = Arrays.asList("", "", "", "", "", "", "");
            indexResult.set(0, tableName);
            indexResult.set(1, dataClassId);
            indexResult.set(2, dataTableDto.getNameCn());
            indexResult.set(3, "存在");
            //以元数据库索引为准，遍历元数据索引
            Iterator<TableIndexDto> indexIterator = dataTableDto.getTableIndexList().iterator();
            List<TableIndexDto> indexList = IteratorUtils.toList(indexIterator);
            Map<String, String> dbIndexs = indexAndShardings.get("indexs");
            for (TableIndexDto indexDto : indexList) {
                //TableIndexDto indexDto = indexIterator.next();
                String indexColumn = dbIndexs.get(indexDto.getIndexName());
                //元数据多余的索引
                if (!StringUtils.isNotNullString(indexColumn)) {
                    if (StringUtils.isNotNullString(indexResult.get(4))) {
                        indexResult.set(4, indexResult.get(4) + ";" + indexDto.getIndexName());
                    } else {
                        indexResult.set(4, indexDto.getIndexName());
                    }
                    continue;
                }
                //判断索引字段
                if (!indexColumn.equalsIgnoreCase(indexDto.getIndexColumn())) {
                    if (StringUtils.isNotNullString(indexResult.get(6))) {
                        indexResult.set(6, indexResult.get(6) + ";" + indexDto.getIndexName());
                    } else {
                        indexResult.set(6, indexDto.getIndexName());
                    }
                }
            }
            //以物理库为基础，查找元数据库缺失的索引
            for (String indexName : dbIndexs.keySet()) {
                boolean flag = false;
                for (TableIndexDto indexDto1 : indexList) {
                    //TableIndexDto indexDto1 = indexIterator.next();
                    if (indexName.equalsIgnoreCase(indexDto1.getIndexName())) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    if (StringUtils.isNotNullString(indexResult.get(5))) {
                        indexResult.set(5, indexResult.get(5) + ";" + indexName);
                    } else {
                        indexResult.set(5, indexName);
                    }
                }
            }
            indexResults.add(indexResult);

            shardingResult = Arrays.asList("", "", "", "", "", "", "");
            shardingResult.set(0, tableName);
            shardingResult.set(1, dataClassId);
            shardingResult.set(2, dataTableDto.getNameCn());
            shardingResult.set(3, "存在");

        }
    }

    @Override
    public void updateEleInfo(String databaseId) {
        ResultT<List<Map<String, Object>>> r = new ResultT();
        //获取数据库详细信息
        SchemaDto schemaDto = schemaService.getDotById(databaseId);
        ConnectVo coreInfo = schemaDto.getConnectVo();
        AuthorityVo a = new AuthorityVo(schemaDto.getSchemaName());
        ExcAbs exc = coreInfo.build(r);
        exc.allTables(a,r);
        if (r.isSuccess()){
            List<Map<String, Object>> maps = MapUtil.transformMapList(r.getData());
            ResultT<List<Map<String, Object>>> column = new ResultT();
            maps.stream().map(e->String.valueOf(e.get(table_name))).forEach(tableName->{
                exc.columnInfo(a,column);
                //物理库表字段信息
                Map<String, Map<String, Object>> columnInfos = null;
                if (column.isSuccess()){
                    columnInfos = column.getData().stream().collect(Collectors.toMap(k -> k.get(column_name).toString(), t -> t));
                }
                updateEleSubInfo(databaseId, tableName.toUpperCase(), columnInfos);
            });
        }
        exc.close();
    }

    public void updateEleSubInfo(String databaseId, String tableName, Map<String, Map<String, Object>> columnInfos) {
        //元数据表信息
        List<Map<String, Object>> dataTableList = dataTableService.getByDatabaseIdAndTableName(databaseId, tableName);
        if (dataTableList == null || dataTableList.size() == 0) {
            //元数据表缺失
            return;
        }
        for (Map<String, Object> dataTable : dataTableList) {
            String tableId = (String) dataTable.get("ID");
            DataTableInfoDto dataTableDto = dataTableService.getDotById(tableId);
            //以元数据库字段为准，遍历元数据字段
            Iterator<TableColumnDto> columnIterator = dataTableDto.getColumns().iterator();
            List<TableColumnDto> columnList = IteratorUtils.toList(columnIterator);
            for (TableColumnDto columnDto : columnList) {
                boolean flag = false;
                Map<String, Object> columnOneInfo = columnInfos.get(columnDto.getDbEleCode().toUpperCase());
                //存储元数据多余字段
                if (columnOneInfo == null) {
                    continue;
                }
                //判断类型和长度
                String dbColumnType = ((String) columnOneInfo.get("column_type")).replace(",", ".");// decimal(4,0)  varchar(200)  date
                String dbType = "";
                String dbAcc = "";
                if (dbColumnType.indexOf("(") != -1) {
                    dbType = dbColumnType.substring(0, dbColumnType.indexOf("("));
                    dbAcc = dbColumnType.substring(dbColumnType.indexOf("(") + 1, dbColumnType.length() - 1);
                } else {
                    dbType = dbColumnType;
                }
                if (!dbType.equalsIgnoreCase(columnDto.getType())) {
                    if (!("decimal".equalsIgnoreCase(columnDto.getType()) && "NUMERIC".equalsIgnoreCase(dbType)) &&
                            !("int".equalsIgnoreCase(columnDto.getType()) && "INTEGER".equalsIgnoreCase(dbType)) &&
                            !("timestamp".equalsIgnoreCase(columnDto.getType()) && "DATETIME".equalsIgnoreCase(dbType)) &&
                            !("NUMBER".equalsIgnoreCase(columnDto.getType()) && "NUMERIC".equalsIgnoreCase(dbType))) {
                        //字段类型不一致
                        continue;
                    }
                }
                if (!dbAcc.equalsIgnoreCase(columnDto.getAccuracy())) {
                    if (!((null == columnDto.getAccuracy()) && "".equalsIgnoreCase(dbAcc))) {
                        //System.out.println("精度修改（"+databaseId+":"+tableName+")原始字段"+columnDto.getAccuracy()+"应该修改为"+dbAcc );
                        //根据数据库真实字段精度更新元数据库
                        columnDto.setAccuracy(dbAcc);
                        flag = true;
                    }
                }
                //判断为空设置是否一致
                if ((columnDto.getIsNull() && (Integer) columnOneInfo.get("is_nullable") == 0) || (!columnDto.getIsNull() && (Integer) columnOneInfo.get("is_nullable") == 1)) {
                    //System.out.println("可空修改（"+databaseId+":"+tableName+")原始为"+columnDto.getIsNull()+"应该修改为相反的" );
                    //根据数据库真实字段可空更新元数据库
                    columnDto.setIsNull(!columnDto.getIsNull());
                    flag = true;

                }
                if (flag) {
                    //更新
                    try {
                        tableColumnService.saveDto(columnDto);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
