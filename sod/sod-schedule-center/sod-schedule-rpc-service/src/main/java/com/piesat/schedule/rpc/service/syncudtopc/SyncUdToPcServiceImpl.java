package com.piesat.schedule.rpc.service.syncudtopc;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.common.utils.poi.ExcelUtil;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.database.SchemaDto;
import com.piesat.schedule.dao.syncudtopc.SyncUdToPcDao;
import com.piesat.schedule.entity.syncudtopc.SyncUdToPcEntity;
import com.piesat.schedule.mapper.JobInfoMapper;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.api.syncudtopc.SyncUdToPcService;
import com.piesat.schedule.rpc.dto.syncudtopc.SyncUdToPcDto;
import com.piesat.schedule.rpc.mapstruct.syncudtopc.SyncUdToPcMapstruct;
import com.piesat.schedule.rpc.service.DataBaseService;
import com.piesat.schedule.rpc.vo.DataRetrieval;
import com.piesat.ucenter.rpc.api.system.DictDataService;
import com.piesat.ucenter.rpc.dto.system.DictDataDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author cwh
 * @date 2020年 10月28日 16:18:00
 */
@Service
@Slf4j
public class SyncUdToPcServiceImpl extends BaseService<SyncUdToPcEntity> implements SyncUdToPcService {
    @Autowired
    private SyncUdToPcDao syncUdToPcDao;
    @Autowired
    private SyncUdToPcMapstruct syncUdToPcMapstruct;
    @Autowired
    private JobInfoService jobInfoService;
    @Autowired
    private DataBaseService dataBaseService;
    @Autowired
    private JobInfoMapper jobInfoMapper;
    @GrpcHthtClient
    private DictDataService dictDataService;

    @Override
    public BaseDao<SyncUdToPcEntity> getBaseDao() {
        return this.syncUdToPcDao;
    }

    @Override
    public PageBean selectSyncUdToPcList(PageForm<SyncUdToPcDto> pageForm) {
        SyncUdToPcEntity syncUdToPcEntity = syncUdToPcMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(syncUdToPcEntity.getDatabaseId())) {
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(), syncUdToPcEntity.getDatabaseId());
        }
        SimpleSpecificationBuilder specificationBuilderOr = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(syncUdToPcEntity.getDataClassId())) {
            specificationBuilderOr.add("dataClassId", SpecificationOperator.Operator.likeAll.name(), syncUdToPcEntity.getDataClassId());
            specificationBuilderOr.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(), syncUdToPcEntity.getDataClassId());
        }
        if (StringUtils.isNotNullString(syncUdToPcEntity.getProfileName())) {
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(), syncUdToPcEntity.getProfileName());
        }
        if (null != syncUdToPcEntity.getTriggerStatus()) {
            specificationBuilder.add("triggerStatus", SpecificationOperator.Operator.eq.name(), syncUdToPcEntity.getTriggerStatus());
        }
        if (StringUtils.isNotNullString(syncUdToPcEntity.getTableName())) {
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(), syncUdToPcEntity.getTableName());
        }
        if (StringUtils.isNotNullString((String) syncUdToPcEntity.getParamt().get("beginTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.ges.name(), (String) syncUdToPcEntity.getParamt().get("beginTime"));
        }
        if (StringUtils.isNotNullString((String) syncUdToPcEntity.getParamt().get("endTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.les.name(), (String) syncUdToPcEntity.getParamt().get("endTime"));
        }
        Specification specification = specificationBuilder.generateSpecification().and(specificationBuilderOr.generateSpecification());
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        PageBean pageBean = this.getPage(specification, pageForm, sort);
        List<SyncUdToPcEntity> syncUdToPcEntitys = (List<SyncUdToPcEntity>) pageBean.getPageData();
        pageBean.setPageData(syncUdToPcMapstruct.toDto(syncUdToPcEntitys));
        return pageBean;
    }

    @Override
    public SyncUdToPcDto findSyncUdToPcById(String syncUdToPcId) {
        SyncUdToPcEntity syncUdToPcEntity = this.getById(syncUdToPcId);
        return syncUdToPcMapstruct.toDto(syncUdToPcEntity);
    }

    @Override
    public void saveSyncUdToPc(SyncUdToPcDto syncUdToPcDto) {
        SyncUdToPcEntity syncUdToPcEntity = syncUdToPcMapstruct.toEntity(syncUdToPcDto);
        this.getDataBaseAndClassId(syncUdToPcEntity);
        syncUdToPcEntity = this.saveNotNull(syncUdToPcEntity);
        jobInfoService.start(syncUdToPcMapstruct.toDto(syncUdToPcEntity));
    }

    @Override
    public void updateSyncUdToPc(SyncUdToPcDto syncUdToPcDto) {
        SyncUdToPcEntity syncUdToPcEntity = syncUdToPcMapstruct.toEntity(syncUdToPcDto);
        this.getDataBaseAndClassId(syncUdToPcEntity);
        syncUdToPcEntity = this.saveNotNull(syncUdToPcEntity);
        jobInfoService.start(syncUdToPcMapstruct.toDto(syncUdToPcEntity));
    }

    public void getDataBaseAndClassId(SyncUdToPcEntity syncUdToPcEntity) {
        DataRetrieval dataRetrieval = dataBaseService.getByDatabaseIdAndClassId(syncUdToPcEntity.getDatabaseId(), syncUdToPcEntity.getDataClassId());
        syncUdToPcEntity.setDdataId(dataRetrieval.getDdataId());
        syncUdToPcEntity.setTableName(dataRetrieval.getTableName());
        syncUdToPcEntity.setVTableName(dataRetrieval.getVTableName());
        syncUdToPcEntity.setDatabaseType(dataRetrieval.getDatabaseType());
        syncUdToPcEntity.setParentId(dataRetrieval.getParentId());
        syncUdToPcEntity.setProfileName(dataRetrieval.getProfileName());
    }

    @Override
    public void deleteSyncUdToPcIds(String[] syncUdToPcIds) {
        try{
            this.deleteByIds(Arrays.asList(syncUdToPcIds));
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        jobInfoService.stopByIds(Arrays.asList(syncUdToPcIds));
    }

    @Override
    public List<Map<String, Object>> findDatabase() {
        List<DictDataDto> dictDataDtos = dictDataService.selectDictDataByType("backup_database");
        List<String> dicts = new ArrayList<>();
        for (DictDataDto dictDataDto : dictDataDtos) {
            dicts.add(dictDataDto.getDictValue());
        }

        List<Map<String, Object>> databaseDtos = new ArrayList<>();
        List<SchemaDto> databaseListAll = dataBaseService.findAllDataBase();
        for (SchemaDto databaseDto : databaseListAll) {
            String databaseName = databaseDto.getDatabase().getDatabaseName() + "_" + databaseDto.getDatabaseName();
            String parentId = databaseDto.getDatabase().getId();
            if (dicts.contains(parentId.toUpperCase())) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("KEY", databaseDto.getId());
                map.put("VALUE", databaseName);
                map.put("parentId", databaseDto.getDatabase().getId());
                databaseDtos.add(map);
            }

        }

        return databaseDtos;

    }
    @Override
    public List<Map<String, Object>> findDataClassId(String dataBaseId, String dataClassId) {
        List<Map<String, Object>> dataClassIds = new ArrayList<>();

        List<Map<String, Object>> mapList = dataBaseService.getByDatabaseId(dataBaseId);
        List<String> isHave = jobInfoMapper.selectUdToPcDataClassId(dataBaseId);
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

    public List<SyncUdToPcEntity> selectSyncToFileList(SyncUdToPcDto syncUdToPcDto) {
        SyncUdToPcEntity syncUdToPcEntity = syncUdToPcMapstruct.toEntity(syncUdToPcDto);
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(syncUdToPcEntity.getDatabaseId())) {
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(), syncUdToPcEntity.getDatabaseId());
        }
        SimpleSpecificationBuilder specificationBuilderOr = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(syncUdToPcEntity.getDataClassId())) {
            specificationBuilderOr.add("dataClassId", SpecificationOperator.Operator.likeAll.name(), syncUdToPcEntity.getDataClassId());
            specificationBuilderOr.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(), syncUdToPcEntity.getDataClassId());
        }
        if (StringUtils.isNotNullString(syncUdToPcEntity.getProfileName())) {
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(), syncUdToPcEntity.getProfileName());
        }
        if (null != syncUdToPcEntity.getTriggerStatus()) {
            specificationBuilder.add("triggerStatus", SpecificationOperator.Operator.eq.name(), syncUdToPcEntity.getTriggerStatus());
        }
        if (StringUtils.isNotNullString(syncUdToPcEntity.getTableName())) {
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(), syncUdToPcEntity.getTableName());
        }
        if (StringUtils.isNotNullString((String) syncUdToPcEntity.getParamt().get("beginTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.ges.name(), (String) syncUdToPcEntity.getParamt().get("beginTime"));
        }
        if (StringUtils.isNotNullString((String) syncUdToPcEntity.getParamt().get("endTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.les.name(), (String) syncUdToPcEntity.getParamt().get("endTime"));
        }
        Specification specification = specificationBuilder.generateSpecification().and(specificationBuilderOr.generateSpecification());
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        List<SyncUdToPcEntity> syncUdToPcEntitys = this.getAll(specification, sort);
        return syncUdToPcEntitys;
    }
    @Override
    public void exportExcel(SyncUdToPcDto syncUdToPcDto) {
        List<SyncUdToPcEntity> entities = this.selectSyncToFileList(syncUdToPcDto);
        ExcelUtil<SyncUdToPcEntity> util = new ExcelUtil(SyncUdToPcEntity.class);
        util.exportExcel(entities, "数据同步配置");
    }
}
