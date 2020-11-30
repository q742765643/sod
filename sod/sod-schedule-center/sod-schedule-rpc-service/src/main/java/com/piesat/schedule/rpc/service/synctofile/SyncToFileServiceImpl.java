package com.piesat.schedule.rpc.service.synctofile;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.dao.synctofile.SyncToFileDao;
import com.piesat.schedule.entity.synctofile.SyncToFileEntity;
import com.piesat.schedule.mapper.JobInfoMapper;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.api.synctofile.SyncToFileService;
import com.piesat.schedule.rpc.dto.synctofile.SyncToFileDto;
import com.piesat.schedule.rpc.mapstruct.synctofile.SyncToFileMapstruct;
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
public class SyncToFileServiceImpl extends BaseService<SyncToFileEntity> implements SyncToFileService {
    @Autowired
    private SyncToFileDao syncToFileDao;
    @Autowired
    private SyncToFileMapstruct syncToFileMapstruct;
    @Autowired
    private JobInfoService jobInfoService;
    @Autowired
    private DataBaseService dataBaseService;
    @Autowired
    private JobInfoMapper jobInfoMapper;

    @Override
    public BaseDao<SyncToFileEntity> getBaseDao() {
        return this.syncToFileDao;
    }

    @Override
    public PageBean selectSyncToFileList(PageForm<SyncToFileDto> pageForm) {
        SyncToFileEntity syncToFileEntity = syncToFileMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(syncToFileEntity.getDatabaseId())) {
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(), syncToFileEntity.getDatabaseId());
        }
        SimpleSpecificationBuilder specificationBuilderOr = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(syncToFileEntity.getDataClassId())) {
            specificationBuilderOr.add("dataClassId", SpecificationOperator.Operator.likeAll.name(), syncToFileEntity.getDataClassId());
            specificationBuilderOr.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(), syncToFileEntity.getDataClassId());
        }
        if (StringUtils.isNotNullString(syncToFileEntity.getProfileName())) {
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(), syncToFileEntity.getProfileName());
        }
        if (null != syncToFileEntity.getTriggerStatus()) {
            specificationBuilder.add("triggerStatus", SpecificationOperator.Operator.eq.name(), syncToFileEntity.getTriggerStatus());
        }
        if (StringUtils.isNotNullString(syncToFileEntity.getTableName())) {
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(), syncToFileEntity.getTableName());
        }
        if (StringUtils.isNotNullString((String) syncToFileEntity.getParamt().get("beginTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.ges.name(), (String) syncToFileEntity.getParamt().get("beginTime"));
        }
        if (StringUtils.isNotNullString((String) syncToFileEntity.getParamt().get("endTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.les.name(), (String) syncToFileEntity.getParamt().get("endTime"));
        }
        Specification specification = specificationBuilder.generateSpecification().and(specificationBuilderOr.generateSpecification());
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        PageBean pageBean = this.getPage(specification, pageForm, sort);
        List<SyncToFileEntity> syncToFileEntitys = (List<SyncToFileEntity>) pageBean.getPageData();
        pageBean.setPageData(syncToFileMapstruct.toDto(syncToFileEntitys));
        return pageBean;
    }

    @Override
    public SyncToFileDto findSyncToFileById(String syncToFileId) {
        SyncToFileEntity syncToFileEntity = this.getById(syncToFileId);
        return syncToFileMapstruct.toDto(syncToFileEntity);
    }

    @Override
    public void saveSyncToFile(SyncToFileDto syncToFileDto) {
        SyncToFileEntity syncToFileEntity = syncToFileMapstruct.toEntity(syncToFileDto);
        this.getDataBaseAndClassId(syncToFileEntity);
        syncToFileEntity = this.saveNotNull(syncToFileEntity);
        jobInfoService.start(syncToFileMapstruct.toDto(syncToFileEntity));
    }

    @Override
    public void updateSyncToFile(SyncToFileDto syncToFileDto) {
        SyncToFileEntity syncToFileEntity = syncToFileMapstruct.toEntity(syncToFileDto);
        this.getDataBaseAndClassId(syncToFileEntity);
        syncToFileEntity = this.saveNotNull(syncToFileEntity);
        jobInfoService.start(syncToFileMapstruct.toDto(syncToFileEntity));
    }

    public void getDataBaseAndClassId(SyncToFileEntity syncToFileEntity) {
        DataRetrieval dataRetrieval = dataBaseService.getByDatabaseIdAndClassId(syncToFileEntity.getDatabaseId(), syncToFileEntity.getDataClassId());
        syncToFileEntity.setDdataId(dataRetrieval.getDdataId());
        syncToFileEntity.setTableName(dataRetrieval.getTableName());
        syncToFileEntity.setVTableName(dataRetrieval.getVTableName());
        syncToFileEntity.setDatabaseType(dataRetrieval.getDatabaseType());
        syncToFileEntity.setParentId(dataRetrieval.getParentId());
        syncToFileEntity.setProfileName(dataRetrieval.getProfileName());
    }

    @Override
    public void deleteSyncToFileIds(String[] syncToFileIds) {
        try{
            this.deleteByIds(Arrays.asList(syncToFileIds));
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        jobInfoService.stopByIds(Arrays.asList(syncToFileIds));
    }

    @Override
    public List<Map<String, Object>> findDataClassId(String dataBaseId, String dataClassId) {
        List<Map<String, Object>> dataClassIds = new ArrayList<>();

        List<Map<String, Object>> mapList = dataBaseService.getByDatabaseId(dataBaseId);
        List<String> isHave = jobInfoMapper.selectToFileDataClassId(dataBaseId);
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
