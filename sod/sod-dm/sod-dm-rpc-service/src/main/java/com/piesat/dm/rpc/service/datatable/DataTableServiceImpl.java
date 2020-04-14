package com.piesat.dm.rpc.service.datatable;

import com.alibaba.fastjson.JSONObject;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.core.api.DatabaseDcl;
import com.piesat.dm.core.parser.DatabaseInfo;
import com.piesat.dm.core.parser.DatabaseType;
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.dao.dataclass.DataClassDao;
import com.piesat.dm.dao.dataclass.DataLogicDao;
import com.piesat.dm.dao.datatable.*;
import com.piesat.dm.entity.database.DatabaseEntity;
import com.piesat.dm.entity.dataclass.DataClassEntity;
import com.piesat.dm.entity.dataclass.DataLogicEntity;
import com.piesat.dm.entity.datatable.*;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.datatable.DataTableDto;
import com.piesat.dm.rpc.dto.datatable.SampleData;
import com.piesat.dm.rpc.dto.datatable.TableIndexDto;
import com.piesat.dm.rpc.dto.datatable.TableSqlDto;
import com.piesat.dm.rpc.mapper.datatable.DataTableMapper;
import com.piesat.dm.rpc.mapper.database.DatabaseMapper;
import com.piesat.dm.rpc.mapper.datatable.TableForeignKeyMapper;
import com.piesat.dm.rpc.util.DatabaseUtil;
import com.piesat.util.ResultT;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 表信息
 *
 * @author cwh
 * @date 2019年 11月22日 16:34:17
 */
@Service
public class DataTableServiceImpl extends BaseService<DataTableEntity> implements DataTableService {
    @Autowired
    private DataTableDao dataTableDao;
    @Autowired
    private DataTableMapper dataTableMapper;
    @Autowired
    private ShardingDao shardingDao;
    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    private DatabaseMapper databaseMapper;
    @Autowired
    private DatabaseSqlService databaseSqlService;
    @Autowired
    private DatabaseInfo databaseInfo;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private DataLogicDao dataLogicDao;
    @Autowired
    private DataClassDao dataClassDao;
    @Autowired
    private TableColumnDao tableColumnDao;
    @Autowired
    private TableIndexDao tableIndexDao;
    @Autowired
    private TableForeignKeyDao tableForeignKeyDao;
    @Autowired
    private TableForeignKeyMapper tableForeignKeyMapper;

    @Override
    public BaseDao<DataTableEntity> getBaseDao() {
        return dataTableDao;
    }

    @Override
    public DataTableDto saveDto(DataTableDto dataTableDto) {
        DataTableEntity dataTableEntity = this.dataTableMapper.toEntity(dataTableDto);
        dataTableEntity = this.save(dataTableEntity);
        return this.dataTableMapper.toDto(dataTableEntity);
    }

    @Override
    public List<DataTableDto> all() {
        List<DataTableEntity> all = this.getAll();
        return this.dataTableMapper.toDto(all);
    }

    @Override
    public DataTableDto getDotById(String id) {
        DataTableEntity dataTableEntity = this.getById(id);
        return this.dataTableMapper.toDto(dataTableEntity);
    }

    @Override
    public void deleteByClassLogicId(String classLogicId) {
        this.dataTableDao.deleteByClassLogic_Id(classLogicId);
    }

    @Override
    public List<DataTableDto> getByDatabaseIdAndClassId(String databaseId, String dataClassId) {
        List<DataTableEntity> tableEntities = this.dataTableDao.getByDatabaseIdAndClassId(databaseId, dataClassId);
        return this.dataTableMapper.toDto(tableEntities);
    }

    @Override
    public List<Map<String, Object>> getByDatabaseId(String databaseId) {
        //List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(this.dataTableDao.getByDatabaseId(databaseId));
        String sql = "select A.* ,B.data_class_id,B.storage_type from T_SOD_DATA_TABLE A,T_SOD_DATA_LOGIC B where A.class_logic_id=B.id and B.database_id ='" + databaseId + "'";
        List<Map<String, Object>> list = this.queryByNativeSQL(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> findByUserId(String userId) {
        if (DatabaseType.databaseType.toLowerCase().equals("mysql")) {
            return mybatisQueryMapper.getInfoByUserIdMysql(userId);
        } else {
            return mybatisQueryMapper.getInfoByUserId(userId);
        }
    }

    @Override
    public List<Map<String, Object>> getByClassId(String dataClassId) {
        return mybatisQueryMapper.getTableInfoByClassId(dataClassId);
    }

    @Override
    public List<Map<String, Object>> getMultiDataInfoByClassId(String dataClassId) {
        //根据存储编码查询表信息
        List<Map<String, Object>> tableInfoLists = mybatisQueryMapper.getTableInfoByClassId(dataClassId);
        if (tableInfoLists != null && tableInfoLists.size() > 0) {
            for (int i = 0; i < tableInfoLists.size(); i++) {
                Map<String, Object> tableInfo = tableInfoLists.get(i);

                //根据表ID查询表实体
                DataTableDto dataTableDto = this.getDotById(String.valueOf(tableInfo.get("ID")));

                //索引涉及到的字段
                List<String> indexField = new ArrayList<String>();
                JSONObject indexFieldJson = new JSONObject();
                LinkedHashSet<TableIndexDto> tableIndexList = dataTableDto.getTableIndexList();
                if (tableIndexList != null && tableIndexList.size() > 0) {
                    for (TableIndexDto tableIndexDto : tableIndexList) {
                        String indexColumn = tableIndexDto.getIndexColumn();
                        for (String column : indexColumn.split(",")) {
                            if (!indexField.contains(column)) {
                                indexField.add(column);
                                indexFieldJson.put(String.format("index_field%d", indexField.size()), column);
                            }
                        }
                    }
                }

                tableInfo.put("table_structure", dataTableDto.getColumns());//表字段信息
                tableInfo.put("table_index1", tableIndexList);//索引信息
                tableInfo.put("table_index2", indexFieldJson);
                tableInfo.put("table_index3", indexField);
            }
        }
        return tableInfoLists;
    }

    @Override
    public List<DataTableDto> getByClassLogicId(String classLogic) {
        List<DataTableEntity> tableEntities = this.dataTableDao.findByClassLogicId(classLogic);
        return this.dataTableMapper.toDto(tableEntities);
    }

    @Override
    public int updateById(DataTableDto dataTableDto) {
        return dataTableDao.updateById(dataTableDto.getTableName(), dataTableDto.getId());
    }


    @Override
    public ResultT getOverview(String databaseId, String dataClassId) {
        List<DataTableEntity> tableEntities = this.dataTableDao.getByDatabaseIdAndClassId(databaseId, dataClassId);
        if (tableEntities == null || tableEntities.size() == 0) {
            return ResultT.failed("没有适应表");
        } else {
            Map<String, Object> map = new HashMap<>();
            DatabaseEntity databaseEntity = this.databaseDao.findById(databaseId).get();
            DataTableEntity keyTable = null;
            DataTableEntity eleTable = null;
            if (tableEntities.size() == 1) {
                keyTable = tableEntities.get(0);
            } else {
                if (tableEntities.get(0).getDbTableType().toUpperCase().equals("K")) {
                    keyTable = tableEntities.get(0);
                    eleTable = tableEntities.get(1);
                } else {
                    keyTable = tableEntities.get(1);
                    eleTable = tableEntities.get(0);
                }
            }
            map.put("K", keyTable == null ? "" : keyTable.getTableName());
            map.put("E", eleTable == null ? "" : eleTable.getTableName());
            List<TableForeignKeyEntity> foreignKeyEntities = this.tableForeignKeyDao.findByClassLogicId(keyTable.getClassLogic().getId());
            if (foreignKeyEntities.size() > 0) {
                map.put("foreignKey", this.tableForeignKeyMapper.toDto(foreignKeyEntities));
            }
            List<TableColumnEntity> primaryKey = this.tableColumnDao.findByTableIdAndIsPrimaryKeyTrue(keyTable.getId());
            if (primaryKey.size() > 0) {
                map.put("primaryKey", primaryKey.get(0).getDbEleCode());
            }
            map.put("database", this.databaseMapper.toDto(databaseEntity));
            DataClassEntity dataClass = this.dataClassDao.findByDataClassId(keyTable.getClassLogic().getDataClassId());
            map.put("D_DATA_ID", dataClass.getDDataId());
            map.put("CLASSNAME", dataClass.getClassName());
            return ResultT.success(map);
        }
    }

    @Override
    public ResultT getSampleData(SampleData sampleData) throws Exception {
        DatabaseEntity databaseEntity = this.databaseDao.findById(sampleData.getDatabaseId()).get();
        DatabaseDto databaseDto = databaseMapper.toDto(databaseEntity);
        DatabaseDcl database = DatabaseUtil.getDatabase(databaseDto, databaseInfo);
        return database.queryData(databaseDto.getSchemaName(), sampleData.getTableName(), sampleData.getColumn(), 10);
    }

    @Override
    public List<Map<String, Object>> getByDatabaseIdAndTableName(String databaseId, String tableName) {
        return mybatisQueryMapper.getByDatabaseIdAndTableName(databaseId, tableName);
    }

    @Override
    @Transactional
    public ResultT paste(String copyId, String pasteId) {
        List<DataTableEntity> copys = this.dataTableDao.findByClassLogicId(copyId);
        DataLogicEntity paste = this.dataLogicDao.getOne(pasteId);
        List<DataTableEntity> pDataTableEntitys = this.dataTableDao.findByClassLogicId(pasteId);
        for (DataTableEntity pd : pDataTableEntitys) {
            this.shardingDao.deleteByTableId(pd.getId());
            this.tableColumnDao.deleteByTableId(pd.getId());
            this.tableIndexDao.deleteByTableId(pd.getId());
            this.delete(pd.getId());
        }

        DataClassEntity dataClassEntity = this.dataClassDao.findByDataClassId(paste.getDataClassId());
        for (DataTableEntity copy : copys) {
            DataTableEntity dte = new DataTableEntity();
            BeanUtils.copyProperties(copy, dte);
            List<ShardingEntity> shardingEntities = this.shardingDao.findByTableId(dte.getId());
            dte.setClassLogic(paste);
            dte.setDataServiceId(dataClassEntity.getDataClassId());
            dte.setDataServiceName(dataClassEntity.getClassName());
            dte.setNameCn(dataClassEntity.getClassName());
            dte.setCreateTime(new Date());
            dte.setId(null);
            dte.setCreateTime(new Date());
            dte.setVersion(0);
            Set<TableColumnEntity> columns = dte.getColumns();
            Set<TableColumnEntity> cl = new LinkedHashSet();
            for (TableColumnEntity c : columns) {
                TableColumnEntity cc = new TableColumnEntity();
                BeanUtils.copyProperties(c, cc);
                cc.setVersion(0);
                String id = UUID.randomUUID().toString();
                cc.setId(null);
                cc.setCreateTime(new Date());
                cl.add(cc);
            }
            dte.setColumns(cl);
            Set<TableIndexEntity> tableIndexList = dte.getTableIndexList();
            Set<TableIndexEntity> til = new LinkedHashSet();
            for (TableIndexEntity index : tableIndexList) {
                TableIndexEntity te = new TableIndexEntity();
                BeanUtils.copyProperties(index, te);
                String id = UUID.randomUUID().toString();
                te.setId(null);
                te.setCreateTime(new Date());
                te.setVersion(0);
                til.add(te);
            }
            dte.setTableIndexList(til);
            DataTableEntity save = this.dataTableDao.save(dte);
            for (ShardingEntity se : shardingEntities) {
                ShardingEntity sde = new ShardingEntity();
                BeanUtils.copyProperties(se, sde);
                sde.setTableId(save.getId());
                String id = UUID.randomUUID().toString();
                sde.setId(null);
                sde.setCreateTime(new Date());
                sde.setVersion(0);
                this.shardingDao.save(sde);
            }

        }
        return ResultT.success();
    }

    @Override
    public ResultT createTable(TableSqlDto tableSqlDto) {
        DatabaseEntity databaseEntity = this.databaseDao.findById(tableSqlDto.getDatabaseId()).get();
        DatabaseDto databaseDto = databaseMapper.toDto(databaseEntity);
        try {
            DatabaseDcl database = DatabaseUtil.getDatabase(databaseDto, databaseInfo);
            database.createTable(tableSqlDto.getTableSql(), tableSqlDto.getTableName(), tableSqlDto.getDelOld());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
        return ResultT.success();
    }

    @Override
    public List<DataTableDto> findByTableNameAndDatabaseIdAndDataclassId(String tableName, String databaseId,String dataclassId) {
        List<DataTableEntity> dataTables = this.dataTableDao.findByTableNameAndClassLogic_DatabaseIdAndClassLogic_DataClassId(tableName, databaseId,dataclassId);
        return this.dataTableMapper.toDto(dataTables);
    }
}
