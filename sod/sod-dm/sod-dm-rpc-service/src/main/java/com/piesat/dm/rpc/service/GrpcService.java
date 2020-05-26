package com.piesat.dm.rpc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.core.parser.DatabaseInfo;
import com.piesat.dm.dao.*;
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.dao.database.DatabaseDefineDao;
import com.piesat.dm.dao.datatable.DataTableDao;
import com.piesat.dm.dao.datatable.ShardingDao;
import com.piesat.dm.dao.datatable.TableColumnDao;
import com.piesat.dm.entity.*;
import com.piesat.dm.entity.database.DatabaseEntity;
import com.piesat.dm.entity.dataclass.LogicDefineEntity;
import com.piesat.dm.entity.datatable.CmccElementEntity;
import com.piesat.dm.entity.datatable.DataTableEntity;
import com.piesat.dm.entity.datatable.DatumTableEntity;
import com.piesat.dm.entity.datatable.ShardingEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.database.DatabaseDefineService;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.api.dataclass.DataLogicService;
import com.piesat.dm.rpc.api.dataclass.LogicDefineService;
import com.piesat.dm.rpc.dto.database.DatabaseDefineDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.dataclass.DataLogicDto;
import com.piesat.dm.rpc.dto.dataclass.LogicDatabaseDto;
import com.piesat.dm.rpc.dto.dataclass.LogicDefineDto;
import com.piesat.dm.rpc.dto.dataclass.LogicStorageTypesDto;
import com.piesat.dm.rpc.dto.datatable.DataTableDto;
import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import com.piesat.dm.rpc.dto.datatable.TableIndexDto;
import com.piesat.dm.rpc.mapper.*;
import com.piesat.dm.rpc.mapper.dataclass.LogicDefineMapper;
import com.piesat.dm.rpc.mapper.datatable.DataTableMapper;
import com.piesat.dm.rpc.mapper.datatable.ShardingMapper;
import com.piesat.dm.rpc.mapper.datatable.TableColumnMapper;
import com.piesat.dm.rpc.service.dataclass.LogicDefineServiceImpl;
import com.piesat.dm.rpc.service.datatable.DatabaseSqlService;
import com.piesat.schedule.rpc.api.backup.BackupService;
import com.piesat.schedule.rpc.api.clear.ClearService;
import com.piesat.schedule.rpc.api.move.MoveService;
import com.piesat.schedule.rpc.api.recover.MetaRecoverLogService;
import com.piesat.schedule.rpc.api.sync.SyncTaskService;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.rpc.dto.clear.ClearDto;
import com.piesat.schedule.rpc.dto.sync.SyncTaskDto;
import com.piesat.sod.system.rpc.api.ServiceCodeService;
import com.piesat.sod.system.rpc.api.SqlTemplateService;
import com.piesat.sod.system.rpc.dto.ServiceCodeDto;
import com.piesat.sod.system.rpc.dto.SqlTemplateDto;
import com.piesat.ucenter.rpc.api.system.DictDataService;
import com.piesat.ucenter.rpc.dto.system.DictDataDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/9 15:28
 */
@Service
public class GrpcService {
    @Autowired
    private StorageConfigurationDao storageConfigurationDao;
    @Autowired
    private StorageConfigurationMapper storageConfigurationMapper;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private DataClassService dataClassService;
    @Autowired
    private LogicDefineService logicDefineService;
    @Autowired
    private DatabaseDefineDao databaseDefineDao;
    @Autowired
    private DatabaseDefineService databaseDefineService;
    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    private DataLogicService dataLogicService;
    @Autowired
    private LogicDefineServiceImpl logicDefineServiceImpl;
    @Autowired
    private LogicDefineMapper logicDefineMapper;
    @Autowired
    private DataTableDao dataTableDao;
    @Autowired
    private DataTableMapper dataTableMapper;
    @Autowired
    private ShardingDao shardingDao;
    @Autowired
    private ShardingMapper shardingMapper;
    @Autowired
    private DatabaseInfo databaseInfo;
    @Autowired
    private DatabaseSqlService databaseSqlService;
    @Autowired
    private TableColumnDao tableColumnDao;
    @Autowired
    private TableColumnMapper tableColumnMapper;
    @Autowired
    private DatabaseService databaseService;

    @GrpcHthtClient
    private DictDataService dictDataService;
    @GrpcHthtClient
    private ClearService clearService;
    @GrpcHthtClient
    private MoveService moveService;
    @GrpcHthtClient
    private SyncTaskService syncTaskService;
    @GrpcHthtClient
    private BackupService backupService;
    @GrpcHthtClient
    private MetaRecoverLogService metaRecoverLogService;
    @GrpcHthtClient
    private SqlTemplateService sqlTemplateService;
    @GrpcHthtClient
    private ServiceCodeService serviceCodeService;

    @Transactional
    public ResultT updateColumnValue(String id, String column, String value) {
        //删除配置
        if ("3".equals(value)) {
            //获取策略配置
            StorageConfigurationEntity storage = this.storageConfigurationDao.findById(id).get();
            if (column.equals("storage_define_identifier")) {
                //删除存储结构
                dataLogicService.deleteById(storage.getClassLogicId());
            } else if (column.equals("sync_identifier")) {
                //删除同步配置
                String task_id = storage.getSyncId();
                if (StringUtils.isNotNullString(task_id)) {
                    SyncTaskDto syncTaskDto = syncTaskService.getDtoById(task_id);
                    if (syncTaskDto != null) {
                        syncTaskService.deleteSync(task_id);
                    }
                }
            } else if (column.equals("move_identifier")) {
                //删除迁移清楚
                String taskId = storage.getMoveId();
                if (StringUtils.isNotNullString(taskId)) {
                    moveService.deleteMoveByIds(new String[]{taskId});
                }
            } else if (column.equals("clean_identifier")) {
                //删除迁移清楚
                String taskId = storage.getClearId();
                if (StringUtils.isNotNullString(taskId)) {
                    clearService.deleteClearByIds(new String[]{taskId});
                }
            } else if (column.equals("backup_identifier")) {
                //删除备份
                String taskId = storage.getBackupId();
                if (StringUtils.isNotNullString(taskId)) {
                    backupService.deleteBackupByIds(new String[]{taskId});
                }
            } else if (column.equals("archiving_identifier")) {
                //恢复
//                String taskId = storage.getClearId();
//                if (StringUtils.isNotNullString(taskId)) {
//                    metaRecoverLogService.deleteMetaRecoverLogByIds(new String[]{taskId});
//                }
            }
        }
        //更新状态
        mybatisQueryMapper.updateStorageConfigurationStatus(id, column, value);

        return ResultT.success();
    }

    @Transactional
    public ResultT deleteById(String id) {
        StorageConfigurationEntity storage = this.storageConfigurationDao.findById(id).orElse(null);
        if (storage == null) {
            return ResultT.failed("概览信息未配置");
        }
        //删除同步配置
        if (StringUtils.isNotNullString(storage.getSyncId())) {
            SyncTaskDto syncTaskDto = syncTaskService.getDtoById(storage.getSyncId());
            if (syncTaskDto != null) {
                syncTaskService.deleteSync(storage.getSyncId());
            }
        }
        //删除存储结构
        if (StringUtils.isNotEmpty(storage.getClassLogicId())) {
            DataLogicDto dotById = dataLogicService.getDotById(storage.getClassLogicId());
            if (dotById != null) {
                dataLogicService.deleteById(storage.getClassLogicId());
            }
        }
        //删除迁移清楚
        if (StringUtils.isNotNullString(storage.getClearId())) {
            ClearDto clearById = clearService.findClearById(storage.getClearId());
            if (clearById != null) {
                clearService.deleteClearByIds(new String[]{storage.getClearId()});
            }
        }
        //删除备份
        if (StringUtils.isNotNullString(storage.getBackupId())) {
            BackUpDto backupById = backupService.findBackupById(storage.getBackupId());
            if (backupById != null) {
                backupService.deleteBackupByIds(new String[]{storage.getBackupId()});
            }
        }
        //删除配置信息
        storageConfigurationDao.deleteById(id);
        return ResultT.success();
    }

    public List<Map<String, Object>> getLogicInfo() {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        // 获取逻辑库类型
        List<LogicDefineDto> logiclist = logicDefineService.all();
        //查询物理库-逻辑库
        List<Map<String, Object>> distinctDatabaseAndLogic = dataLogicService.getDistinctDatabaseAndLogic();
        //查询所有的表类型
        Map<String, String> dictMap = new HashMap<String, String>();
        List<DictDataDto> dictDataDtos = dictDataService.selectDictDataByType("table_type");
        if (dictDataDtos != null) {
            for (DictDataDto dictDataDto : dictDataDtos) {
                dictMap.put(dictDataDto.getDictValue(), dictDataDto.getDictLabel());//E_table 要素表
            }
        }

        if (logiclist != null && logiclist.size() > 0) {
            for (int i = logiclist.size() - 1; i >= 0; i--) {
                LogicDefineDto logicDefineDto = logiclist.get(i);
                Map<String, Object> logicDefineMap = JSONObject.parseObject(JSON.toJSONString(logicDefineDto));
                //逻辑库对应的表类型
                List<LogicStorageTypesDto> logicStorageTypesEntityList = logicDefineDto.getLogicStorageTypesEntityList();
                List<Map<String, Object>> tableTypeList = new ArrayList<Map<String, Object>>();
                if (logicStorageTypesEntityList != null) {
                    for (LogicStorageTypesDto logicStorageTypesDto : logicStorageTypesEntityList) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        String storageType = logicStorageTypesDto.getStorageType();
                        map.put("key", storageType);
                        map.put("name", dictMap.get(storageType));
                        tableTypeList.add(map);
                    }
                }
                logicDefineMap.put("tableType", tableTypeList);

                //逻辑库对应的物理库
                List<Map<String, Object>> databaseList = new ArrayList<Map<String, Object>>();
                for (Map<String, Object> databaseLogic : distinctDatabaseAndLogic) {
                    if (logicDefineDto.getLogicFlag().toString().equals(String.valueOf(databaseLogic.get("logic_flag")))) {
                        databaseList.add(databaseLogic);
                    }
                }
                if (databaseList.size() != 0) {
                    logicDefineMap.put("physics", databaseList);
                    result.add(logicDefineMap);
                }
            }
        }

        return result;
    }


    public List<LogicDefineDto> getAllLogicDefine() {
        List<LogicDefineDto> logicDefineDtos = this.logicDefineService.all();
        List<DictDataDto> DictDataDtos = this.dictDataService.selectDictDataByType("sys_storage_type");
        List<DatabaseDefineDto> all = this.databaseDefineService.all();
        for (LogicDefineDto logicDefineDto : logicDefineDtos) {
            List<LogicStorageTypesDto> logicStorageTypesEntityList = logicDefineDto.getLogicStorageTypesEntityList();
            for (LogicStorageTypesDto logicStorageTypesDto : logicStorageTypesEntityList) {
                for (DictDataDto dictDataDto : DictDataDtos) {
                    if (dictDataDto.getDictValue().equals(logicStorageTypesDto.getStorageType())) {
                        logicStorageTypesDto.setStorageName(dictDataDto.getDictLabel());
                    }
                }
            }
            List<LogicDatabaseDto> logicDatabaseEntityList = logicDefineDto.getLogicDatabaseEntityList();
            for (LogicDatabaseDto logicDatabaseDto : logicDatabaseEntityList) {
                for (DatabaseDefineDto DatabaseDefineDto : all) {
                    if (DatabaseDefineDto.getId().equals(logicDatabaseDto.getDatabaseId())) {
                        logicDatabaseDto.setDatabaseName(DatabaseDefineDto.getDatabaseName());
                    }
                }
            }
        }
        return logicDefineDtos;
    }


    public PageBean selectLogicDefinePageList(PageForm<LogicDefineDto> pageForm) {
        LogicDefineEntity logicDefineEntity = logicDefineMapper.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(logicDefineEntity.getLogicFlag())) {
            specificationBuilder.add("logicFlag", SpecificationOperator.Operator.likeAll.name(), logicDefineEntity.getLogicFlag());
        }
        if (StringUtils.isNotNullString(logicDefineEntity.getLogicName())) {
            specificationBuilder.add("logicName", SpecificationOperator.Operator.likeAll.name(), logicDefineEntity.getLogicName());
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "serialNumber");
        PageBean pageBean = this.logicDefineServiceImpl.getPage(specificationBuilder.generateSpecification(), pageForm, sort);
        List<LogicDefineEntity> logicDefineEntities = (List<LogicDefineEntity>) pageBean.getPageData();
        List<LogicDefineDto> logicDefineDtos = logicDefineMapper.toDto(logicDefineEntities);
        List<DictDataDto> DictDataDtos = this.dictDataService.selectDictDataByType("sys_storage_type");
        List<DatabaseDefineDto> all = this.databaseDefineService.all();
        List<DatabaseDto> databaseList = this.databaseService.all();
        for (LogicDefineDto logicDefineDto : logicDefineDtos) {
            List<LogicStorageTypesDto> logicStorageTypesEntityList = logicDefineDto.getLogicStorageTypesEntityList();
            for (LogicStorageTypesDto logicStorageTypesDto : logicStorageTypesEntityList) {
                for (DictDataDto dictDataDto : DictDataDtos) {
                    if (dictDataDto.getDictValue().equals(logicStorageTypesDto.getStorageType())) {
                        logicStorageTypesDto.setStorageName(dictDataDto.getDictLabel());
                    }
                }
            }
            List<LogicDatabaseDto> logicDatabaseEntityList = logicDefineDto.getLogicDatabaseEntityList();
            for (LogicDatabaseDto logicDatabaseDto : logicDatabaseEntityList) {
                for (DatabaseDefineDto databaseDto : all) {
                    if (databaseDto.getId().equals(logicDatabaseDto.getDatabaseId())) {
                        logicDatabaseDto.setDatabaseName(databaseDto.getDatabaseName());
                    }
                }
            }
        }

        pageBean.setPageData(logicDefineDtos);
        return pageBean;
    }

    public ResultT getSql(String tableId, String databaseId) {
        List<ShardingEntity> shardingEntities = this.shardingDao.findByTableId(tableId);
        DataTableEntity dataTableEntity = this.dataTableDao.findById(tableId).get();
        DataTableDto dataTableDto = this.dataTableMapper.toDto(dataTableEntity);
        List<TableColumnDto> TableColumnDtos = new ArrayList<TableColumnDto>(dataTableDto.getColumns());
        List<TableIndexDto> TableIndexDtos = new ArrayList<TableIndexDto>(dataTableDto.getTableIndexList());
        Optional<DatabaseEntity> databaseEntity = this.databaseDao.findById(databaseId);
        DatabaseEntity database = databaseEntity.get();
        String databaseType = database.getDatabaseDefine().getDatabaseType();
        Map<String, String> map = new HashMap<>();
        try {
            List<SqlTemplateDto> sqlTemplateDtos = this.sqlTemplateService.checkSqlTemplate(databaseType);
            if (sqlTemplateDtos == null || sqlTemplateDtos.size() == 0) {
                return ResultT.failed("模板不存在！");
            } else {
                String createSql = "", insertSql = "", querySql = "";
                if (this.databaseInfo.getXugu().equals(databaseType.toLowerCase())) {
                    createSql = this.databaseSqlService.getXuGuCreateSql(sqlTemplateDtos.get(0), dataTableDto, TableColumnDtos, TableIndexDtos, this.shardingMapper.toDto(shardingEntities), database.getSchemaName());
                    insertSql = this.databaseSqlService.getXuGuInsertSql(dataTableDto, TableColumnDtos, database.getSchemaName());
                    querySql = this.databaseSqlService.getXuGuQuerySql(dataTableDto, TableColumnDtos, database.getSchemaName());
                } else if (this.databaseInfo.getGbase8a().equals(databaseType.toLowerCase())) {
                    createSql = this.databaseSqlService.getGbaseCreateSql(sqlTemplateDtos.get(0), dataTableDto, TableColumnDtos, TableIndexDtos, this.shardingMapper.toDto(shardingEntities), database.getSchemaName());
                    insertSql = this.databaseSqlService.getGbaseInsertSql(dataTableDto, TableColumnDtos, database.getSchemaName());
                    querySql = this.databaseSqlService.getGbaseQuerySql(dataTableDto, TableColumnDtos, database.getSchemaName());
                } else if (this.databaseInfo.getCassandra().equals(databaseType.toLowerCase())) {
                    createSql = this.databaseSqlService.getCassandraCreateSql(sqlTemplateDtos.get(0), dataTableDto, TableColumnDtos, TableIndexDtos, this.shardingMapper.toDto(shardingEntities), database.getSchemaName());
                    insertSql = this.databaseSqlService.getCassandraInsertSql(dataTableDto, TableColumnDtos, database.getSchemaName());
                    querySql = this.databaseSqlService.getCassandraQuerySql(dataTableDto, TableColumnDtos, database.getSchemaName());
                } else if (this.databaseInfo.getPostgresql().equals(databaseType.toLowerCase())) {
                    createSql = this.databaseSqlService.getPostgreSqlCreateSql(sqlTemplateDtos.get(0), dataTableDto, TableColumnDtos, TableIndexDtos, this.shardingMapper.toDto(shardingEntities), database.getSchemaName());
                    insertSql = this.databaseSqlService.getPostgreSqlInsertSql(dataTableDto, TableColumnDtos, database.getSchemaName());
                    querySql = this.databaseSqlService.getPostgreSqlQuerySql(dataTableDto, TableColumnDtos, database.getSchemaName());
                }
                map.put("createSql", createSql);
                map.put("insertSql", insertSql);
                map.put("selectSql", querySql);
                return ResultT.success(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    public int syncServiceName(List<TableColumnDto> tableColumnDtoList) {
        List<ServiceCodeDto> serviceCodeDtos = this.serviceCodeService.queryAll();
        int un = 0;
        for (ServiceCodeDto sc : serviceCodeDtos) {
            for (TableColumnDto tc : tableColumnDtoList) {
                if (sc.getDbEleCode().equals(tc.getCElementCode()) && !sc.getUserEleCode().equals(tc.getUserEleCode())) {
                    tc.setUserEleCode(sc.getUserEleCode());
                    this.tableColumnDao.save(this.tableColumnMapper.toEntity(tc));
                    un++;
                }
            }
        }

        return un;
    }

    public List<DictDataDto> getDictByType(String dictType) {
        List<DictDataDto> DictDataDtos = this.dictDataService.selectDictDataByType(dictType);
        return DictDataDtos;
    }


    public List<CmccElementEntity> queryCmccElements(DatumTableEntity datumTableEntity) {
        String c_datum_code = datumTableEntity.getC_datum_code();
        c_datum_code = c_datum_code.substring(0, 11);
        datumTableEntity.setC_datum_code(c_datum_code);
        return this.mybatisQueryMapper.queryCmccElements(datumTableEntity);
    }
}

