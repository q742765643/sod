package com.piesat.schedule.rpc.service.backup;

import com.alibaba.fastjson.JSON;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.OwnException;
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
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.mapper.JobInfoMapper;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.api.backup.BackupLogService;
import com.piesat.schedule.rpc.api.backup.BackupService;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.rpc.dto.backup.BackupLogDto;
import com.piesat.schedule.rpc.mapstruct.backup.BackupMapstruct;
import com.piesat.schedule.rpc.rule.ExtractMessage;
import com.piesat.schedule.rpc.service.DataBaseService;
import com.piesat.schedule.rpc.service.DiSendService;
import com.piesat.schedule.rpc.vo.BackupVo;
import com.piesat.schedule.rpc.vo.DataRetrieval;
import com.piesat.schedule.rpc.vo.ReplaceVo;
import com.piesat.ucenter.rpc.api.system.DictDataService;
import com.piesat.ucenter.rpc.dto.system.DictDataDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    @Autowired
    private BackupLogService backupLogService;
    private static final String BACKUP_TIME="backupTime";

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

    public ResultT<String> execute(String id){
        ResultT resultT=new ResultT();
        BackupEntity backupEntity=this.getById(id);
        BackupVo backupVo = this.calculateBackupTime(backupEntity,System.currentTimeMillis(), resultT);
        BackupLogDto backupLogDto=backupLogService.selectBackupLoByJobId(id);
        if(null!=backupVo&&null!=backupLogDto){
            if(backupVo.getBackupTime()==backupLogDto.getBackupTime()
            &&"1".equals(backupLogDto.getHandleCode())){
                resultT.setMessage("已有备份文件,请删除日志重新提交任务!");
                return resultT;
            }
        }
        try {
            jobInfoService.execute(id);
        } catch (Exception e) {
            resultT.setErrorMessage("立即执行失败");
        }
        return resultT;
    }
    public BackupVo calculateBackupTime(BackupEntity backupEntity, long backupTime, ResultT<String> resultT) {
        BackupVo backupVo = new BackupVo();
        ReplaceVo replaceVo = new ReplaceVo();
        replaceVo.setMsg(backupEntity.getConditions());
        replaceVo.setDatabaseId(backupEntity.getParentId());
        replaceVo.setDataClassId(backupEntity.getDataClassId());
        replaceVo.setDdataId(backupEntity.getDdataId());
        replaceVo.setBackupTime(backupTime);
        ExtractMessage.getIndexOf(replaceVo, resultT);
        Map<String, Long> map = this.calculateMistiming(replaceVo.getTimeSet(), backupTime, resultT);
        backupVo.setBackupTime(map.get(BACKUP_TIME));
        backupVo.setMistiming(map.get("mistiming"));
        backupVo.setConditions(replaceVo.getMsg());
        replaceVo.setMsg(backupEntity.getSecondConditions());
        ExtractMessage.getIndexOf(replaceVo, resultT);
        if (backupVo.getMistiming() > 0) {
            Map<String, Long> mapHis = this.calculateMistiming(replaceVo.getTimeSet(), backupTime, resultT);
            backupVo.setBackupTimeHis(mapHis.get(BACKUP_TIME));
            backupVo.setSecondConditions(replaceVo.getMsg());
        }


        return backupVo;
    }
    public Map<String, Long> calculateMistiming(Set<Long> timeSet, long deafulBackupTime, ResultT<String> resultT) {
        Map<String, Long> map = null;
        try {
            map = new HashMap<>();
            long mistiming = 0;
            long backupTime = 0;
            if (timeSet.size() == 2) {
                List<Long> timeList = new ArrayList<>();
                for (long time : timeSet) {
                    timeList.add(time);
                }
                long time1 = timeList.get(0);
                long time2 = timeList.get(1);
                if (time1 < time2) {
                    backupTime = time1;
                } else {
                    backupTime = time2;
                }
                mistiming = Math.abs(time2 - time1);


            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(deafulBackupTime));
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                backupTime = calendar.getTime().getTime();
            }
            map.put("mistiming", mistiming);
            map.put(BACKUP_TIME, backupTime);
        } catch (Exception e) {
            resultT.setErrorMessage("计算历史时次出错{}", OwnException.get(e));
            log.error("计算历史时次出错{}",OwnException.get(e));
            log.error(OwnException.get(e));

        }
        return map;

    }
}

