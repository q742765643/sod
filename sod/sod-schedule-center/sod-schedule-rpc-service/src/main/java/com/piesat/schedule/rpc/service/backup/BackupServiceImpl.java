package com.piesat.schedule.rpc.service.backup;

import com.alibaba.fastjson.JSON;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.common.utils.poi.ExcelUtil;
import com.piesat.dm.rpc.api.StorageConfigurationService;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.api.dataclass.DataLogicService;
import com.piesat.dm.rpc.api.datatable.TableForeignKeyService;
import com.piesat.dm.rpc.dto.StorageConfigurationDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.dataclass.DataLogicDto;
import com.piesat.dm.rpc.dto.datatable.TableForeignKeyDto;
import com.piesat.schedule.dao.backup.BackupDao;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.mapper.JobInfoMapper;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.api.backup.BackupService;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.rpc.mapstruct.backup.BackupMapstruct;
import com.piesat.schedule.rpc.service.DataBaseService;
import com.piesat.schedule.rpc.service.DiSendService;
import com.piesat.schedule.rpc.vo.DataRetrieval;
import com.piesat.ucenter.rpc.api.system.DictDataService;
import com.piesat.ucenter.rpc.dto.system.DictDataDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-23 09:17
 **/
@Service
public class BackupServiceImpl extends BaseService<BackupEntity> implements BackupService {
    @Autowired
    private BackupDao backupDao;
    @Autowired
    private BackupMapstruct backupMapstruct;
    @Autowired
    private JobInfoService jobInfoService;
    @Autowired
    private DataBaseService dataBaseService;
    @GrpcHthtClient
    private DictDataService dictDataService;
    @Autowired
    private JobInfoMapper jobInfoMapper;
    @Autowired
    private DiSendService diSendService;
    @GrpcHthtClient
    private DatabaseService databaseService;
    @GrpcHthtClient
    private TableForeignKeyService tableForeignKeyService;
    @GrpcHthtClient
    private DataLogicService dataLogicService;
    @GrpcHthtClient
    private StorageConfigurationService storageConfigurationService;

    @Override
    public BaseDao<BackupEntity> getBaseDao() {
        return backupDao;
    }

    @Override
    public PageBean selectBackupList(PageForm<BackUpDto> pageForm) {
        BackupEntity backupEntity = backupMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(backupEntity.getDatabaseId())) {
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(), backupEntity.getDatabaseId());
        }
        SimpleSpecificationBuilder specificationBuilderOr = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(backupEntity.getDataClassId())) {
            specificationBuilderOr.add("dataClassId", SpecificationOperator.Operator.likeAll.name(), backupEntity.getDataClassId());
            specificationBuilderOr.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(), backupEntity.getDataClassId());
        }
        if (StringUtils.isNotNullString(backupEntity.getProfileName())) {
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(), backupEntity.getProfileName());
        }
        if (null != backupEntity.getTriggerStatus()) {
            specificationBuilder.add("triggerStatus", SpecificationOperator.Operator.eq.name(), backupEntity.getTriggerStatus());
        }
        if (StringUtils.isNotNullString(backupEntity.getTableName())) {
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(), backupEntity.getTableName());
        }
        if (StringUtils.isNotNullString((String) backupEntity.getParamt().get("beginTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.ges.name(), (String) backupEntity.getParamt().get("beginTime"));
        }
        if (StringUtils.isNotNullString((String) backupEntity.getParamt().get("endTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.les.name(), (String) backupEntity.getParamt().get("endTime"));
        }
        Specification specification = specificationBuilder.generateSpecification().and(specificationBuilderOr.generateSpecification());
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        PageBean pageBean = this.getPage(specification, pageForm, sort);
        List<BackupEntity> backupEntities = (List<BackupEntity>) pageBean.getPageData();
        pageBean.setPageData(backupMapstruct.toDto(backupEntities));
        return pageBean;

    }

    @Override
    public BackUpDto selectBackupByParam(String databaseId, String dataClassId) {
        PageForm pageForm = new PageForm(1, 1);
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(databaseId)) {
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(), databaseId);
        }
        SimpleSpecificationBuilder specificationBuilderOr = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(dataClassId)) {
            specificationBuilderOr.add("dataClassId", SpecificationOperator.Operator.likeAll.name(), dataClassId);
            specificationBuilderOr.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(), dataClassId);
        }
        Specification specification = specificationBuilder.generateSpecification().and(specificationBuilderOr.generateSpecification());
        PageBean pageBean = this.getPage(specification, pageForm, Sort.unsorted());
        List<BackupEntity> backupEntities = (List<BackupEntity>) pageBean.getPageData();
        if (null != backupEntities && !backupEntities.isEmpty()) {
            return backupMapstruct.toDto(backupEntities.get(0));
        }
        return null;
    }

    @Override
    public BackUpDto findBackupById(String backupId) {
        BackupEntity backupEntity = this.getById(backupId);
        return backupMapstruct.toDto(backupEntity);

    }

    @Override
    public void saveBackup(BackUpDto backUpDto) {
        BackupEntity backupEntity = backupMapstruct.toEntity(backUpDto);
        this.getDataBaseAndClassId(backupEntity);
        backupEntity = this.saveNotNull(backupEntity);
        diSendService.sendBackup(backupEntity);
        jobInfoService.start(backupMapstruct.toDto(backupEntity));
        List<DataLogicDto> dataLogic = this.dataLogicService.getDataLogic(backupEntity.getDataClassId(), backupEntity.getDatabaseId(), backupEntity.getTableName());
        for (DataLogicDto dl : dataLogic) {
            StorageConfigurationDto scd = new StorageConfigurationDto();
            scd.setClassLogicId(dl.getId());
            scd.setBackupIdentifier(1);
            scd.setBackupId(backupEntity.getId());
            this.storageConfigurationService.updateDataAuthorityConfig(scd);
        }
    }

    @Override
    public void updateBackup(BackUpDto backUpDto) {
        BackupEntity backupEntity = backupMapstruct.toEntity(backUpDto);
        this.getDataBaseAndClassId(backupEntity);
        backupEntity = this.saveNotNull(backupEntity);
        diSendService.sendBackup(backupEntity);
        jobInfoService.start(backUpDto);
    }

    @Override
    public void deleteBackupByIds(String[] backupIds) {
        try{
            this.deleteByIds(Arrays.asList(backupIds));
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        for (int i = 0; i < backupIds.length; i++) {
            diSendService.sendDeleteDi(backupIds[i]);
        }
        jobInfoService.stopByIds(Arrays.asList(backupIds));
    }

    @Override
    public List<Map<String, Object>> findDatabase() {
        List<DictDataDto> dictDataDtos = dictDataService.selectDictDataByType("backup_database");
        List<String> dicts = new ArrayList<>();
        for (DictDataDto dictDataDto : dictDataDtos) {
            dicts.add(dictDataDto.getDictValue());
        }

        List<Map<String, Object>> databaseDtos = new ArrayList<>();
        List<DatabaseDto> databaseListAll = dataBaseService.findAllDataBase();
        for (DatabaseDto databaseDto : databaseListAll) {
            String databaseName = databaseDto.getDatabaseDefine().getDatabaseName() + "_" + databaseDto.getDatabaseName();
            String parentId = databaseDto.getDatabaseDefine().getId();
            if (dicts.contains(parentId.toUpperCase())) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("KEY", databaseDto.getId());
                map.put("VALUE", databaseName);
                map.put("parentId", databaseDto.getDatabaseDefine().getId());
                databaseDtos.add(map);
            }

        }

        return databaseDtos;

    }

    @Override
    public List<Map<String, Object>> findDataClassId(String dataBaseId, String dataClassId) {

        List<Map<String, Object>> dataClassIds = new ArrayList<>();

        List<Map<String, Object>> mapList = dataBaseService.getByDatabaseId(dataBaseId);
        List<String> isHave = jobInfoMapper.selectBackupDataClassId(dataBaseId);
        for (Map<String, Object> map : mapList) {
            String dataClass = (String) map.get("DATA_CLASS_ID");
            String className = (String) map.get("CLASS_NAME");
            if (!isHave.contains(dataClass) || dataClass.equals(dataClassId)) {
                LinkedHashMap<String, Object> dataClassIdMap = new LinkedHashMap<>();
                dataClassIdMap.put("KEY", dataClass);
                dataClassIdMap.put("VALUE", className);
                dataClassIds.add(dataClassIdMap);
            }
        }

        return dataClassIds;

    }

    public void getDataBaseAndClassId(BackupEntity backupEntity) {
        DataRetrieval dataRetrieval = dataBaseService.getByDatabaseIdAndClassId(backupEntity.getDatabaseId(), backupEntity.getDataClassId());
        backupEntity.setDdataId(dataRetrieval.getDdataId());
        backupEntity.setTableName(dataRetrieval.getTableName());
        backupEntity.setVTableName(dataRetrieval.getVTableName());
        backupEntity.setDatabaseType(dataRetrieval.getDatabaseType());
        backupEntity.setParentId(dataRetrieval.getParentId());
        backupEntity.setProfileName(dataRetrieval.getProfileName());
        if(null==dataRetrieval.getForeignKey()||!StringUtils.isNotNullString(dataRetrieval.getVTableName())){
            if(dataRetrieval.getParentId().toUpperCase().indexOf("FIDB")!=-1){
                TableForeignKeyDto tableForeignKeyDto=new TableForeignKeyDto();
                tableForeignKeyDto.setEleColumn("D_FILE_ID");
                tableForeignKeyDto.setKeyColumn("D_FILE_ID");
                List<TableForeignKeyDto> tableForeignKeyDtos=new ArrayList<>();
                tableForeignKeyDtos.add(tableForeignKeyDto);
                dataRetrieval.setForeignKey(JSON.toJSONString(tableForeignKeyDtos));
            }else{
                TableForeignKeyDto tableForeignKeyDto=new TableForeignKeyDto();
                tableForeignKeyDto.setEleColumn("D_RECORD_ID");
                tableForeignKeyDto.setKeyColumn("D_RECORD_ID");
                List<TableForeignKeyDto> tableForeignKeyDtos=new ArrayList<>();
                tableForeignKeyDtos.add(tableForeignKeyDto);
                dataRetrieval.setForeignKey(JSON.toJSONString(tableForeignKeyDtos));
            }

        }

        backupEntity.setForeignKey(dataRetrieval.getForeignKey());
    }

    public List<BackupEntity> selectBackupList(BackUpDto backUpDto) {
        BackupEntity backupEntity = backupMapstruct.toEntity(backUpDto);
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(backupEntity.getDatabaseId())) {
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(), backupEntity.getDatabaseId());
        }
        SimpleSpecificationBuilder specificationBuilderOr = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(backupEntity.getDataClassId())) {
            specificationBuilderOr.add("dataClassId", SpecificationOperator.Operator.likeAll.name(), backupEntity.getDataClassId());
            specificationBuilderOr.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(), backupEntity.getDataClassId());
        }
        if (StringUtils.isNotNullString(backupEntity.getProfileName())) {
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(), backupEntity.getProfileName());
        }
        if (null != backupEntity.getTriggerStatus()) {
            specificationBuilder.add("triggerStatus", SpecificationOperator.Operator.eq.name(), backupEntity.getTriggerStatus());
        }
        if (StringUtils.isNotNullString(backupEntity.getTableName())) {
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(), backupEntity.getTableName());
        }
        if (StringUtils.isNotNullString((String) backupEntity.getParamt().get("beginTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.ges.name(), (String) backupEntity.getParamt().get("beginTime"));
        }
        if (StringUtils.isNotNullString((String) backupEntity.getParamt().get("endTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.les.name(), (String) backupEntity.getParamt().get("endTime"));
        }
        Specification specification = specificationBuilder.generateSpecification().and(specificationBuilderOr.generateSpecification());
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        List<BackupEntity> backupEntities = this.getAll(specification, sort);
        return backupEntities;
    }

    @Override
    public void exportExcel(BackUpDto backUpDto) {
        List<BackupEntity> entities = this.selectBackupList(backUpDto);
        ExcelUtil<BackupEntity> util = new ExcelUtil(BackupEntity.class);
        util.exportExcel(entities, "数据备份配置");
    }
}

