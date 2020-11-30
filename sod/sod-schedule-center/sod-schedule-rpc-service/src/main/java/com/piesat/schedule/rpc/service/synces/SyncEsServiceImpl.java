package com.piesat.schedule.rpc.service.synces;

import com.alibaba.fastjson.JSON;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.rpc.dto.datatable.TableForeignKeyDto;
import com.piesat.schedule.dao.backup.BackupDao;
import com.piesat.schedule.dao.synces.SyncEsDao;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.synces.SyncEsEntity;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.api.synces.SyncEsService;
import com.piesat.schedule.rpc.dto.backup.BackupLogDto;
import com.piesat.schedule.rpc.dto.synces.SyncEsDto;
import com.piesat.schedule.rpc.mapstruct.backup.BackupMapstruct;
import com.piesat.schedule.rpc.mapstruct.synces.SyncEsMapstruct;
import com.piesat.schedule.rpc.service.DataBaseService;
import com.piesat.schedule.rpc.vo.BackupVo;
import com.piesat.schedule.rpc.vo.DataRetrieval;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author cwh
 * @date 2020年 10月28日 16:18:00
 */
@Service
@Slf4j
public class SyncEsServiceImpl extends BaseService<SyncEsEntity> implements SyncEsService {
    @Autowired
    private SyncEsDao syncEsDao;
    @Autowired
    private SyncEsMapstruct syncEsMapstruct;
    @Autowired
    private JobInfoService jobInfoService;
    @Autowired
    private DataBaseService dataBaseService;

    @Override
    public BaseDao<SyncEsEntity> getBaseDao() {
        return this.syncEsDao;
    }

    @Override
    public PageBean selectSyncEsList(PageForm<SyncEsDto> pageForm) {
        SyncEsEntity syncEsEntity = syncEsMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(syncEsEntity.getDatabaseId())) {
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(), syncEsEntity.getDatabaseId());
        }
        SimpleSpecificationBuilder specificationBuilderOr = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(syncEsEntity.getDataClassId())) {
            specificationBuilderOr.add("dataClassId", SpecificationOperator.Operator.likeAll.name(), syncEsEntity.getDataClassId());
            specificationBuilderOr.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(), syncEsEntity.getDataClassId());
        }
        if (StringUtils.isNotNullString(syncEsEntity.getProfileName())) {
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(), syncEsEntity.getProfileName());
        }
        if (null != syncEsEntity.getTriggerStatus()) {
            specificationBuilder.add("triggerStatus", SpecificationOperator.Operator.eq.name(), syncEsEntity.getTriggerStatus());
        }
        if (StringUtils.isNotNullString(syncEsEntity.getTableName())) {
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(), syncEsEntity.getTableName());
        }
        if (StringUtils.isNotNullString((String) syncEsEntity.getParamt().get("beginTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.ges.name(), (String) syncEsEntity.getParamt().get("beginTime"));
        }
        if (StringUtils.isNotNullString((String) syncEsEntity.getParamt().get("endTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.les.name(), (String) syncEsEntity.getParamt().get("endTime"));
        }
        Specification specification = specificationBuilder.generateSpecification().and(specificationBuilderOr.generateSpecification());
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        PageBean pageBean = this.getPage(specification, pageForm, sort);
        List<SyncEsEntity> syncEsEntitys = (List<SyncEsEntity>) pageBean.getPageData();
        pageBean.setPageData(syncEsMapstruct.toDto(syncEsEntitys));
        return pageBean;
    }

    @Override
    public SyncEsDto findSyncEsById(String syncEsId) {
        SyncEsEntity syncEsEntity = this.getById(syncEsId);
        return syncEsMapstruct.toDto(syncEsEntity);
    }

    @Override
    public void saveSyncEs(SyncEsDto syncEsDto) {
        SyncEsEntity syncEsEntity = syncEsMapstruct.toEntity(syncEsDto);
        this.getDataBaseAndClassId(syncEsEntity);
        syncEsEntity = this.saveNotNull(syncEsEntity);
        jobInfoService.start(syncEsMapstruct.toDto(syncEsEntity));
    }

    @Override
    public void updateSyncEs(SyncEsDto syncEsDto) {
        SyncEsEntity syncEsEntity = syncEsMapstruct.toEntity(syncEsDto);
        this.getDataBaseAndClassId(syncEsEntity);
        syncEsEntity = this.saveNotNull(syncEsEntity);
        jobInfoService.start(syncEsMapstruct.toDto(syncEsEntity));
    }

    public void getDataBaseAndClassId(SyncEsEntity syncEsEntity) {
        DataRetrieval dataRetrieval = dataBaseService.getByDatabaseIdAndClassId(syncEsEntity.getDatabaseId(), syncEsEntity.getDataClassId());
        syncEsEntity.setDdataId(dataRetrieval.getDdataId());
        syncEsEntity.setTableName(dataRetrieval.getTableName());
        syncEsEntity.setVTableName(dataRetrieval.getVTableName());
        syncEsEntity.setDatabaseType(dataRetrieval.getDatabaseType());
        syncEsEntity.setParentId(dataRetrieval.getParentId());
        syncEsEntity.setProfileName(dataRetrieval.getProfileName());
    }

    @Override
    public void deleteSyncEsIds(String[] syncEsIds) {
        try{
            this.deleteByIds(Arrays.asList(syncEsIds));
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        jobInfoService.stopByIds(Arrays.asList(syncEsIds));
    }

}
