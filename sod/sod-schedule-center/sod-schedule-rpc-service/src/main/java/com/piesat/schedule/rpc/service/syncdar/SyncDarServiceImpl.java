package com.piesat.schedule.rpc.service.syncdar;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.dao.syncdar.SyncDarDao;
import com.piesat.schedule.entity.syncdar.SyncDarEntity;
import com.piesat.schedule.mapper.JobInfoMapper;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.api.syncdar.SyncDarService;
import com.piesat.schedule.rpc.dto.syncdar.SyncDarDto;
import com.piesat.schedule.rpc.mapstruct.syncdar.SyncDarMapstruct;
import com.piesat.schedule.rpc.service.DataBaseService;
import com.piesat.schedule.rpc.vo.DataRetrieval;
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
public class SyncDarServiceImpl extends BaseService<SyncDarEntity> implements SyncDarService {
    @Autowired
    private SyncDarDao syncDarDao;
    @Autowired
    private SyncDarMapstruct syncDarMapstruct;
    @Autowired
    private JobInfoService jobInfoService;
    @Autowired
    private DataBaseService dataBaseService;
    @Autowired
    private JobInfoMapper jobInfoMapper;

    @Override
    public BaseDao<SyncDarEntity> getBaseDao() {
        return this.syncDarDao;
    }

    @Override
    public PageBean selectSyncDarList(PageForm<SyncDarDto> pageForm) {
        SyncDarEntity syncDarEntity = syncDarMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(syncDarEntity.getDatabaseId())) {
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(), syncDarEntity.getDatabaseId());
        }
        SimpleSpecificationBuilder specificationBuilderOr = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(syncDarEntity.getDataClassId())) {
            specificationBuilderOr.add("dataClassId", SpecificationOperator.Operator.likeAll.name(), syncDarEntity.getDataClassId());
            specificationBuilderOr.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(), syncDarEntity.getDataClassId());
        }
        if (StringUtils.isNotNullString(syncDarEntity.getProfileName())) {
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(), syncDarEntity.getProfileName());
        }
        if (null != syncDarEntity.getTriggerStatus()) {
            specificationBuilder.add("triggerStatus", SpecificationOperator.Operator.eq.name(), syncDarEntity.getTriggerStatus());
        }
        if (StringUtils.isNotNullString(syncDarEntity.getTableName())) {
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(), syncDarEntity.getTableName());
        }
        if (StringUtils.isNotNullString((String) syncDarEntity.getParamt().get("beginTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.ges.name(), (String) syncDarEntity.getParamt().get("beginTime"));
        }
        if (StringUtils.isNotNullString((String) syncDarEntity.getParamt().get("endTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.les.name(), (String) syncDarEntity.getParamt().get("endTime"));
        }
        Specification specification = specificationBuilder.generateSpecification().and(specificationBuilderOr.generateSpecification());
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        PageBean pageBean = this.getPage(specification, pageForm, sort);
        List<SyncDarEntity> syncDarEntitys = (List<SyncDarEntity>) pageBean.getPageData();
        pageBean.setPageData(syncDarMapstruct.toDto(syncDarEntitys));
        return pageBean;
    }

    @Override
    public SyncDarDto findSyncDarById(String syncDarId) {
        SyncDarEntity syncDarEntity = this.getById(syncDarId);
        return syncDarMapstruct.toDto(syncDarEntity);
    }

    @Override
    public void saveSyncDar(SyncDarDto syncDarDto) {
        SyncDarEntity syncDarEntity = syncDarMapstruct.toEntity(syncDarDto);
        this.getDataBaseAndClassId(syncDarEntity);
        syncDarEntity = this.saveNotNull(syncDarEntity);
        jobInfoService.start(syncDarMapstruct.toDto(syncDarEntity));
    }

    @Override
    public void updateSyncDar(SyncDarDto syncDarDto) {
        SyncDarEntity syncDarEntity = syncDarMapstruct.toEntity(syncDarDto);
        this.getDataBaseAndClassId(syncDarEntity);
        syncDarEntity = this.saveNotNull(syncDarEntity);
        jobInfoService.start(syncDarMapstruct.toDto(syncDarEntity));
    }

    public void getDataBaseAndClassId(SyncDarEntity syncDarEntity) {
        DataRetrieval dataRetrieval = dataBaseService.getByDatabaseIdAndClassId(syncDarEntity.getDatabaseId(), syncDarEntity.getDataClassId());
        syncDarEntity.setDdataId(dataRetrieval.getDdataId());
        syncDarEntity.setTableName(dataRetrieval.getTableName());
        syncDarEntity.setVTableName(dataRetrieval.getVTableName());
        syncDarEntity.setDatabaseType(dataRetrieval.getDatabaseType());
        syncDarEntity.setParentId(dataRetrieval.getParentId());
        syncDarEntity.setProfileName(dataRetrieval.getProfileName());
    }

    @Override
    public void deleteSyncDarIds(String[] syncDarIds) {
        try{
            this.deleteByIds(Arrays.asList(syncDarIds));
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        jobInfoService.stopByIds(Arrays.asList(syncDarIds));
    }

    @Override
    public List<Map<String, Object>> findDataClassId(String dataBaseId, String dataClassId) {
        List<Map<String, Object>> dataClassIds = new ArrayList<>();

        List<Map<String, Object>> mapList = dataBaseService.getByDatabaseId(dataBaseId);
        List<String> isHave = jobInfoMapper.selectDarDataClassId(dataBaseId);
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

}
