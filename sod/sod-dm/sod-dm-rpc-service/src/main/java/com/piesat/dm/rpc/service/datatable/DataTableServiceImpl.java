package com.piesat.dm.rpc.service.datatable;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.MapUtil;
import com.piesat.common.config.DatabseType;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.common.constants.Constants;
import com.piesat.dm.common.constants.ConstantsMsg;
import com.piesat.dm.core.action.build.DataBuild;
import com.piesat.dm.core.action.exc.abs.ExcAbs;
import com.piesat.dm.core.enums.CountEnum;
import com.piesat.dm.core.model.AuthorityVo;
import com.piesat.dm.core.model.ConnectVo;
import com.piesat.dm.core.model.SelectVo;
import com.piesat.dm.core.parser.DatabaseInfo;
import com.piesat.dm.dao.dataclass.DataClassDao;
import com.piesat.dm.dao.dataclass.DataLogicDao;
import com.piesat.dm.dao.datatable.*;
import com.piesat.dm.entity.dataclass.DataClassAndTableEntity;
import com.piesat.dm.entity.dataclass.DataClassEntity;
import com.piesat.dm.entity.datatable.*;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.dataapply.NewdataApplyService;
import com.piesat.dm.rpc.api.database.SchemaService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.dto.dataapply.NewdataApplyDto;
import com.piesat.dm.rpc.dto.database.SchemaDto;
import com.piesat.dm.rpc.dto.datatable.DataTableInfoDto;
import com.piesat.dm.rpc.dto.datatable.SampleData;
import com.piesat.dm.rpc.dto.datatable.TableIndexDto;
import com.piesat.dm.rpc.dto.datatable.TableSqlDto;
import com.piesat.dm.rpc.mapper.database.DatabaseMapper;
import com.piesat.dm.rpc.mapper.datatable.DataTableMapper;
import com.piesat.dm.rpc.mapper.datatable.TableForeignKeyMapper;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.ResultT;
import com.piesat.util.ReturnCodeEnum;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.shiro.SecurityUtils;
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
public class DataTableServiceImpl extends BaseService<DataTableInfoEntity> implements DataTableService {
    @Autowired
    private DataTableDao dataTableDao;
    @Autowired
    private DataTableMapper dataTableMapper;
    @Autowired
    private ShardingDao shardingDao;
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
    private NewdataApplyService newdataApplyService;
    @Autowired
    private SchemaService schemaService;
    @Autowired
    private TableForeignKeyMapper tableForeignKeyMapper;

    @Override
    public BaseDao<DataTableInfoEntity> getBaseDao() {
        return dataTableDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataTableInfoDto saveDto(DataTableInfoDto dataTableDto) {
        if (dataTableDto.getId() != null) {
            DataTableInfoDto dotById = this.getDotById(dataTableDto.getId());
            List<DataClassAndTableEntity> dataClassLogic = this.dataLogicDao.findByTableId(dataTableDto.getId());
            if (dataClassLogic != null && !dataClassLogic.isEmpty()) {
                String dataClassId = dataClassLogic.get(0).getDataClassId();
                List<NewdataApplyDto> NewdataApplyDtos = this.newdataApplyService
                        .findByDataClassIdAndUserId(dataClassId, dotById.getUserId());
                if (NewdataApplyDtos != null && !NewdataApplyDtos.isEmpty()) {
                    NewdataApplyDto newdataApplyDto = NewdataApplyDtos.get(0);
                    newdataApplyDto.setTableName(dataTableDto.getTableName());
                    this.newdataApplyService.saveDto(newdataApplyDto);
                }
            }
        }

        DataTableInfoEntity dataTableEntity = this.dataTableMapper.toEntity(dataTableDto);
        UserDto loginUser = (UserDto) SecurityUtils.getSubject().getPrincipal();
        dataTableEntity.setCreator(loginUser.getUserName());
        dataTableEntity = this.saveNotNull(dataTableEntity);
        return this.dataTableMapper.toDto(dataTableEntity);
    }

    @Override
    public List<DataTableInfoDto> all() {
        List<DataTableInfoEntity> all = this.getAll();
        return this.dataTableMapper.toDto(all);
    }

    @Override
    public DataTableInfoDto getDotById(String id) {
        DataTableInfoDto dataTableInfoDto = this.dataTableMapper.toDto(this.getById(id));
        if (dataTableInfoDto == null) {
            return null;
        }
        SchemaDto schemaDto = this.schemaService.getDotById(dataTableInfoDto.getDatabaseId());
        if (schemaDto != null) {
            dataTableInfoDto.setDatabaseType(schemaDto.getDatabase().getDatabaseType());
            dataTableInfoDto.setDatabaseName(schemaDto.getDatabase().getDatabaseName());
        }
        return dataTableInfoDto;
    }

    @Override
    public void deleteById(String id) {
        this.shardingDao.deleteById(id);
        this.tableColumnDao.deleteByTableId(id);
        this.tableIndexDao.deleteByTableId(id);
        this.delete(id);
    }

    @Override
    public List<DataTableInfoDto> getByDatabaseIdAndClassId(String databaseId, String dataClassId) {
        List<DataTableInfoEntity> tableEntities = this.dataTableDao.getByDatabaseIdAndClassId(databaseId, dataClassId);
        return this.dataTableMapper.toDto(tableEntities);
    }

    @Override
    public List<Map<String, Object>> getByDatabaseId(String databaseId) {
        String sql = "select A.* from T_SOD_DATA_TABLE_INFO A,T_SOD_DATACLASS_TABLE B where ( A.id=B.table_id or A.id = B.SUB_TABLE_ID ) and A.database_id ='" + databaseId + "'";
        List<Map<String, Object>> list = this.queryByNativeSQL(sql);
        List<Map<String, Object>> maps = MapUtil.transformMapList(list);
        return maps;
    }

    @Override
    public List<Map<String, Object>> findByUserId(String userId) {
        List<Map<String, Object>> resultList;
        if ("mysql".equals(DatabseType.type.toLowerCase())) {
            resultList = mybatisQueryMapper.getInfoByUserIdMysql(userId);
        } else {
            resultList = mybatisQueryMapper.getInfoByUserId(userId);
        }
        for (Map<String, Object> map : resultList) {
            map.put("id", map.get("ID"));
            map.put("pId", map.get("PID"));
            map.put("name", map.get("NAME"));
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> getByClassId(String dataClassId) {
        return mybatisQueryMapper.getTableInfoByClassId(dataClassId);
    }

    @Override
    public List<Map<String, Object>> getMultiDataInfoByClassId(String dataClassId) {
        //根据存储编码查询表信息
        List<Map<String, Object>> tableInfoLists = mybatisQueryMapper.getTableInfoByClassId(dataClassId);
        if (tableInfoLists != null && !tableInfoLists.isEmpty()) {
            for (int i = 0; i < tableInfoLists.size(); i++) {
                Map<String, Object> tableInfo = tableInfoLists.get(i);

                //根据表ID查询表实体
                DataTableInfoDto dataTableDto = this.getDotById(String.valueOf(tableInfo.get("ID")));

                //索引涉及到的字段
                List<String> indexField = new ArrayList<String>();
                JSONObject indexFieldJson = new JSONObject();
                LinkedHashSet<TableIndexDto> tableIndexList = dataTableDto.getTableIndexList();
                if (tableIndexList != null && !tableIndexList.isEmpty()) {
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
    public List<DataTableInfoDto> getByClassLogicId(String classLogic) {
        List<DataTableInfoEntity> tableEntities = this.dataTableDao.getByClassLogicId(classLogic);
        return this.dataTableMapper.toDto(tableEntities);
    }


    @Override
    public ResultT getOverview(String databaseId, String dataClassId) {
        List<DataTableInfoEntity> tableEntities = this.dataTableDao.getByDatabaseIdAndClassId(databaseId, dataClassId);
        if (tableEntities == null || tableEntities.size() == 0) {
            return ResultT.failed("没有适应表");
        } else {
            Map<String, Object> map = new HashMap<>();
            SchemaDto schemaDto = this.schemaService.getDotById(databaseId);
            DataTableInfoEntity keyTable = null;
            DataTableInfoEntity eleTable = null;
            if (tableEntities.size() == 1) {
                keyTable = tableEntities.get(0);
            } else {
                if ("K".equals(tableEntities.get(0).getTableType().toUpperCase())) {
                    keyTable = tableEntities.get(0);
                    eleTable = tableEntities.get(1);
                } else {
                    keyTable = tableEntities.get(1);
                    eleTable = tableEntities.get(0);
                }
            }
            map.put("K", keyTable == null ? "" : keyTable.getTableName());
            map.put("E", eleTable == null ? "" : eleTable.getTableName());
            List<TableForeignKeyEntity> foreignKeyEntities = this.tableForeignKeyDao.findByTableId(keyTable.getId());
            if (foreignKeyEntities != null && !foreignKeyEntities.isEmpty()) {
                map.put("foreignKey", this.tableForeignKeyMapper.toDto(foreignKeyEntities));
            }
            List<TableColumnEntity> primaryKey = this.tableColumnDao.findByTableIdAndIsPrimaryKeyTrue(keyTable.getId());
            if (primaryKey != null && !primaryKey.isEmpty()) {
                map.put("primaryKey", primaryKey.get(0).getDbEleCode());
            }
            map.put("database", schemaDto);
            List<DataClassAndTableEntity> dataClassTable = this.dataLogicDao.findByTableId(keyTable.getId());
            if (dataClassTable.size() < 1) {
                return ResultT.failed("没有对应资料");
            }
            DataClassEntity dataClass = this.dataClassDao.findByDataClassId(dataClassTable.get(0).getDataClassId());
            map.put("D_DATA_ID", dataClass.getDDataId());
            map.put("CLASSNAME", dataClass.getClassName());
            return ResultT.success(map);
        }
    }

    @Override
    public ResultT getSampleData(SampleData sampleData) {
        SchemaDto schemaDto = this.schemaService.getDotById(sampleData.getDatabaseId());
        ResultT r = new ResultT();
        r.setTitle(ConstantsMsg.TITLE1);
        if (schemaDto == null) {
            r.setErrorMessage(ConstantsMsg.MSG10, sampleData.getTableName());
            return r;
        }
        ConnectVo coreInfo = schemaDto.getConnectVo();

        SelectVo s = new SelectVo();
        s.setSchema(schemaDto.getSchemaName());
        s.setTableName(sampleData.getTableName());

        new DataBuild()
                .init(coreInfo,r)
                .sampleData(s,r)
                .close();

        r.setMessage(String.valueOf(r.getProcessMsg()));
        return r;
    }

    @Override
    public List<Map<String, Object>> getByDatabaseIdAndTableName(String databaseId, String tableName) {
        return mybatisQueryMapper.getByDatabaseIdAndTableName(databaseId, tableName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultT paste(String copyId, String pasteId) {
        List<DataTableInfoEntity> copys = this.dataTableDao.getByClassLogicId(copyId);
        DataClassAndTableEntity paste = this.dataLogicDao.getOne(pasteId);
        List<DataTableInfoEntity> pDataTableEntitys = this.dataTableDao.getByClassLogicId(pasteId);
        for (DataTableInfoEntity pd : pDataTableEntitys) {
            this.shardingDao.deleteById(pd.getId());
            this.tableColumnDao.deleteByTableId(pd.getId());
            this.tableIndexDao.deleteByTableId(pd.getId());
            this.delete(pd.getId());
        }

        DataClassEntity dataClassEntity = this.dataClassDao.findByDataClassId(paste.getDataClassId());
        for (DataTableInfoEntity copy : copys) {
            DataTableInfoEntity dte = new DataTableInfoEntity();
            BeanUtils.copyProperties(copy, dte);
            TablePartEntity parting = this.shardingDao.findById(dte.getId()).orElse(null);
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
            dte.setUserId(dataClassEntity.getCreateBy());
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
            DataTableInfoEntity save = this.dataTableDao.saveNotNull(dte);

            TablePartEntity sde = new TablePartEntity();
            BeanUtils.copyProperties(parting, sde);
            sde.setId(save.getId());
            sde.setCreateTime(new Date());
            sde.setVersion(0);
            this.shardingDao.saveNotNull(sde);
        }
        List<NewdataApplyDto> NewdataApplyDtos = this.newdataApplyService
                .findByDataClassIdAndUserId(dataClassEntity.getDataClassId(), dataClassEntity.getCreateBy());
        if (NewdataApplyDtos !=null && !NewdataApplyDtos.isEmpty() && copys!=null && !copys.isEmpty()) {
            NewdataApplyDto newdataApplyDto = NewdataApplyDtos.get(0);
            newdataApplyDto.setTableName(copys.get(0).getTableName());
            this.newdataApplyService.saveDto(newdataApplyDto);
        }
        return ResultT.success();
    }

    @Override
    public ResultT createTable(TableSqlDto tableSqlDto) {
        ResultT r = new ResultT();
        SchemaDto schemaDto = this.schemaService.getDotById(tableSqlDto.getDatabaseId());
        ConnectVo coreInfo = schemaDto.getConnectVo();

        coreInfo.build(r)
                .exc(tableSqlDto.getCreateSql(), r)
                .close();

        return r;
    }

    @Override
    public ResultT existTable(TableSqlDto tableSqlDto) {
        ResultT r = new ResultT();
        r.setTitle(ConstantsMsg.TITLE2);
        SchemaDto schemaDto = this.schemaService.getDotById(tableSqlDto.getDatabaseId());
        ConnectVo coreInfo = schemaDto.getConnectVo();
        AuthorityVo a = new AuthorityVo(schemaDto.getSchemaName(), tableSqlDto.getTableName());
        ExcAbs exc = coreInfo.build(r);
        if (!r.isSuccess()) {
            return r;
        }
        r.setData(exc.existTable(a, r));
        exc.close();
        r.setMessage(String.valueOf(r.getProcessMsg()));
        return r;
    }

    @Override
    public List<Map<String, Object>> findETable(String databaseId) {
        return this.dataTableDao.findETables(databaseId, Constants.TABLE_TYPE_E);
    }

    @Override
    public List<Map<String, Object>> findTables(String tableId) {
        List<Map<String, Object>> tables = this.dataTableDao.getTables(tableId);
        return tables;
    }

    @Override
    public List<Map<String, Object>> getRelatedTables(String tableId) {
        List<Map<String, Object>> tables = this.dataTableDao.getRelatedTables(tableId);
        return tables;
    }

    @Override
    public void contrastColumns(DataTableInfoDto dataTableInfoDto, ResultT resultT) {
        resultT.setData(dataTableInfoDto);
        if (StringUtils.isEmpty(dataTableInfoDto.getDatabaseId())) {
            resultT.setErrorMessage(String.format(ConstantsMsg.MSG9, dataTableInfoDto.getTableName()));
            return;
        }
        SchemaDto schemaDto = schemaService.getDotById(dataTableInfoDto.getDatabaseId());
        if (schemaDto == null) {
            resultT.setErrorMessage(String.format(ConstantsMsg.MSG10, dataTableInfoDto.getTableName()));
            return;
        }
        dataTableInfoDto.setDatabasePid(schemaDto.getDatabase().getId());
        ConnectVo coreInfo = schemaDto.getConnectVo();
        AuthorityVo a = new AuthorityVo(schemaDto.getSchemaName(), dataTableInfoDto.getTableName());
        ExcAbs exc = coreInfo.build(resultT);
        if (!resultT.isSuccess()) {
            resultT.setMessage(ReturnCodeEnum.SUCCESS, resultT.getMsg());
            return;
        }

        List<Map<String, Object>> c = null;
        List<Map<String, Object>> i = null;
        ResultT<List<Map<String, Object>>> r = new ResultT<>();

        exc.columnInfo(a, r);
        if (r.isSuccess()) {
            c = r.getData();
        }
        r = new ResultT<>();
        exc.indexInfo(a, r);
        exc.close();
        if (r.isSuccess()) {
            i = r.getData();
        }
        dataTableInfoDto.contrast(c, i, null);
    }

    @Override
    public PageBean getPageTableInfo(PageForm<Map<String, String>> pageForm) {
        PageHelper.startPage(pageForm.getCurrentPage(), pageForm.getPageSize());
        List<Map<String, Object>> lists = mybatisQueryMapper.getPageTableInfo(pageForm.getT());
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(lists);
        PageBean pageBean = new PageBean(pageInfo.getTotal(), pageInfo.getPages(), lists);
        return pageBean;
    }

    @Override
    public long countTable(TableSqlDto tableSqlDto) {
        ResultT<Map<CountEnum, String>> r = new ResultT();
        SchemaDto schemaDto = this.schemaService.getDotById(tableSqlDto.getDatabaseId());
        ConnectVo coreInfo = schemaDto.getConnectVo();

        SelectVo s = new SelectVo();
        s.setSchema(schemaDto.getSchemaName());
        s.setTableName(tableSqlDto.getTableName());

        new DataBuild()
                .init(coreInfo,r)
                .countData(s,true,r)
                .close();

        return Integer.valueOf(r.getData().get(CountEnum.ALL_COUNT));
    }

    @Override
    public List<Map<String, Object>> findBySubType(String tableType, String storageType) {
        return this.dataTableDao.findBySubType(tableType, storageType);
    }

    @Override
    public List<Map<String, Object>> findTablesByTableName(String tableName) {
        return this.dataTableDao.findTables(tableName);
    }

    @Override
    public List<Map<String, Object>> findAllETables() {
        return this.dataTableDao.findAllETables();
    }

}
