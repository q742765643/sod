package com.piesat.dm.rpc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.common.constants.ConstantsMsg;
import com.piesat.dm.core.factory.AuzFactory;
import com.piesat.dm.core.model.ColumnVo;
import com.piesat.dm.core.model.IndexVo;
import com.piesat.dm.core.model.TableVo;
import com.piesat.dm.core.parser.DatabaseInfo;
import com.piesat.dm.dao.*;
import com.piesat.dm.dao.database.SchemaDao;
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.dao.datatable.ShardingDao;
import com.piesat.dm.dao.datatable.TableColumnDao;
import com.piesat.dm.entity.*;
import com.piesat.dm.entity.dataclass.LogicDefineEntity;
import com.piesat.dm.entity.datatable.*;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.api.database.SchemaService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.api.dataclass.DataLogicService;
import com.piesat.dm.rpc.api.dataclass.LogicDefineService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.database.SchemaDto;
import com.piesat.dm.rpc.dto.dataclass.LogicDatabaseDto;
import com.piesat.dm.rpc.dto.dataclass.LogicDefineDto;
import com.piesat.dm.rpc.dto.dataclass.LogicStorageTypesDto;
import com.piesat.dm.rpc.dto.datatable.DataTableInfoDto;
import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import com.piesat.dm.rpc.dto.datatable.TableSqlDto;
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
    private AdvancedConfigDao advancedConfigDao;
    @Autowired
    private AdvancedConfigMapper advancedConfigMapper;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private DataClassService dataClassService;
    @Autowired
    private LogicDefineService logicDefineService;
    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private SchemaDao schemaDao;
    @Autowired
    private DataLogicService dataLogicService;
    @Autowired
    private LogicDefineServiceImpl logicDefineServiceImpl;
    @Autowired
    private LogicDefineMapper logicDefineMapper;
    @Autowired
    private DataTableService dataTableService;
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
    private SchemaService schemaService;

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
            AdvancedConfigEntity storage = this.advancedConfigDao.findById(id).get();
            if (column.equals("sync_identifier")) {
                //删除同步配置
                String task_id = storage.getSyncId();
                if (StringUtils.isNotEmpty(task_id) && syncTaskService.getDtoById(task_id) != null) {
                    syncTaskService.deleteSync(task_id);
                }
            } else if (column.equals("move_identifier")) {
                //删除迁移清楚
                String taskId = storage.getMoveId();
                if (StringUtils.isNotEmpty(taskId)) {
                    moveService.deleteMoveByIds(new String[]{taskId});
                }
            } else if (column.equals("clean_identifier")) {
                //删除迁移清楚
                String taskId = storage.getClearId();
                if (StringUtils.isNotEmpty(taskId)) {
                    clearService.deleteClearByIds(new String[]{taskId});
                }
            } else if (column.equals("backup_identifier")) {
                //删除备份
                String taskId = storage.getBackupId();
                if (StringUtils.isNotEmpty(taskId)) {
                    backupService.deleteBackupByIds(new String[]{taskId});
                }
            }
        }
        //更新状态
        mybatisQueryMapper.updateStorageConfigurationStatus(id, column, value);

        return ResultT.success();
    }

    @Transactional
    public ResultT deleteById(String id) {
        try {
            List<AdvancedConfigEntity> acs = this.advancedConfigDao.findByTableId(id);
            if (acs.size() > 0) {
                AdvancedConfigEntity ac = acs.get(0);
                //删除同步配置
                if (StringUtils.isNotEmpty(ac.getSyncId()) && syncTaskService.getDtoById(ac.getSyncId()) != null) {
                    syncTaskService.deleteSync(ac.getSyncId());
                }
                //删除迁移清楚
                if (StringUtils.isNotEmpty(ac.getClearId()) && clearService.findClearById(ac.getClearId()) != null) {
                    clearService.deleteClearByIds(new String[]{ac.getClearId()});
                }
                //删除备份
                if (StringUtils.isNotEmpty(ac.getBackupId()) && backupService.findBackupById(ac.getBackupId()) != null) {
                    backupService.deleteBackupByIds(new String[]{ac.getBackupId()});
                }
                //删除配置信息
                advancedConfigDao.deleteById(id);
            }
            this.dataTableService.delete(id);
        } catch (Exception e) {
            return ResultT.failed(e.getMessage());
        }
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
        List<DatabaseDto> all = this.databaseService.all();
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
                for (DatabaseDto DatabaseDto : all) {
                    if (DatabaseDto.getId().equals(logicDatabaseDto.getDatabaseId())) {
                        logicDatabaseDto.setDatabaseName(DatabaseDto.getDatabaseName());
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
        List<DatabaseDto> all = this.databaseService.all();
        List<SchemaDto> databaseList = this.schemaService.all();
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
                for (DatabaseDto databaseDto : all) {
                    if (databaseDto.getId().equals(logicDatabaseDto.getDatabaseId())) {
                        logicDatabaseDto.setDatabaseName(databaseDto.getDatabaseName());
                    }
                }
            }
        }

        pageBean.setPageData(logicDefineDtos);
        return pageBean;
    }

    public int syncServiceName(List<TableColumnDto> tableColumnDtoList) {
        List<ServiceCodeDto> serviceCodeDtos = this.serviceCodeService.queryAll();
        int un = 0;
        for (ServiceCodeDto sc : serviceCodeDtos) {
            for (TableColumnDto tc : tableColumnDtoList) {
                if (sc.getDbEleCode().equals(tc.getCElementCode()) && !sc.getUserEleCode().equalsIgnoreCase(tc.getUserEleCode())) {
                    tc.setUserEleCode(sc.getUserEleCode());
                    this.tableColumnDao.saveNotNull(this.tableColumnMapper.toEntity(tc));
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

    public ResultT getSql(String tableId) {
        DataTableInfoDto dataTableInfoDto = this.dataTableService.getDotById(tableId);
        SchemaDto schemaDto = this.schemaService.getDotById(dataTableInfoDto.getDatabaseId());
        if (schemaDto == null) {
            return ResultT.failed(String.format(ConstantsMsg.MSG9, dataTableInfoDto.getTableName()));
        }

        List<ColumnVo> columnVo = dataTableInfoDto.getColumnVo();
        List<IndexVo> indexVo = dataTableInfoDto.getIndexVo();
        TableVo t = new TableVo();
        try {
            List<SqlTemplateDto> sqlTemplate = this.sqlTemplateService.checkSqlTemplate(schemaDto.getDatabase().getDatabaseType());
            if (sqlTemplate != null && sqlTemplate.size() > 0) {
                t.setTemplate(sqlTemplate.get(0).getTemplate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        t.setSchema(schemaDto.getSchemaName());
        t.setTableName(dataTableInfoDto.getTableName());
        t.setTableDesc(dataTableInfoDto.getNameCn());
        t.setColumnVos(columnVo);
        t.setIndexVos(indexVo);
        PartingEntity partingEntity = this.shardingDao.findById(tableId).orElse(null);
        t.setPartColumn(partingEntity.getPartitions());
        t.setPartDimension(partingEntity.getPartDimension());
        t.setPartUnit(partingEntity.getPartUnit());
        TableSqlDto ts = new TableSqlDto();
        ResultT r = new ResultT();
        AuzFactory.createTableSql(t, r);
        String create = String.valueOf(r.getData());
        ts.setCreateSql(create);
        AuzFactory.queryTableSql(t, r);
        String query = String.valueOf(r.getData());
        ts.setQuerySql(query);
        AuzFactory.insertTableSql(t, r);
        String insert = String.valueOf(r.getData());
        ts.setInsertSql(insert);
        r.setData(ts);
        return r;
    }
}

