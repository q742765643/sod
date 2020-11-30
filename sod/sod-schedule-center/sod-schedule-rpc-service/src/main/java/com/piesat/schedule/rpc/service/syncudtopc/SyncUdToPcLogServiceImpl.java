package com.piesat.schedule.rpc.service.syncudtopc;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.dao.syncudtopc.SyncUdToPcLogDao;
import com.piesat.schedule.entity.syncudtopc.SyncUdToPcLogEntity;
import com.piesat.schedule.rpc.api.syncudtopc.SyncUdToPcLogService;
import com.piesat.schedule.rpc.dto.syncudtopc.SyncUdToPcLogDto;
import com.piesat.schedule.rpc.mapstruct.syncudtopc.SyncUdToPcLogMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author cwh
 * @date 2020年 10月28日 16:42:52
 */
@Service
public class SyncUdToPcLogServiceImpl extends BaseService<SyncUdToPcLogEntity> implements SyncUdToPcLogService {
    @Autowired
    private SyncUdToPcLogDao syncUdToPcLogDao;
    @Autowired
    private SyncUdToPcLogMapstruct syncUdToPcLogMapstruct;

    @Override
    public BaseDao<SyncUdToPcLogEntity> getBaseDao() {
        return this.syncUdToPcLogDao;
    }

    @Override
    public PageBean selectSyncUdToPcLogList(PageForm<SyncUdToPcLogDto> pageForm) {
        SyncUdToPcLogEntity syncUdToPcLogEntity = syncUdToPcLogMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(syncUdToPcLogEntity.getDatabaseId())) {
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(), syncUdToPcLogEntity.getDatabaseId());
        }
        SimpleSpecificationBuilder specificationBuilderOr = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(syncUdToPcLogEntity.getDataClassId())) {
            specificationBuilderOr.add("dataClassId", SpecificationOperator.Operator.likeAll.name(), syncUdToPcLogEntity.getDataClassId());
            specificationBuilderOr.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(), syncUdToPcLogEntity.getDataClassId());
        }
        if (StringUtils.isNotNullString(syncUdToPcLogEntity.getProfileName())) {
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(), syncUdToPcLogEntity.getProfileName());
        }
        if (null != syncUdToPcLogEntity.getHandleCode()) {
            specificationBuilder.add("handleCode", SpecificationOperator.Operator.eq.name(), syncUdToPcLogEntity.getHandleCode());
        }
        if (StringUtils.isNotNullString(syncUdToPcLogEntity.getTableName())) {
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(), syncUdToPcLogEntity.getTableName());
        }
        if (StringUtils.isNotNullString((String) syncUdToPcLogEntity.getParamt().get("beginTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.ges.name(), (String) syncUdToPcLogEntity.getParamt().get("beginTime"));
        }
        if (StringUtils.isNotNullString((String) syncUdToPcLogEntity.getParamt().get("endTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.les.name(), (String) syncUdToPcLogEntity.getParamt().get("endTime"));
        }
        Specification specification = specificationBuilder.generateSpecification().and(specificationBuilderOr.generateSpecification());
        Sort sort = Sort.by(Sort.Direction.DESC, "lastTime");
        PageBean pageBean = this.getPage(specification, pageForm, sort);
        List<SyncUdToPcLogEntity> syncUdToPcLogEntitys = (List<SyncUdToPcLogEntity>) pageBean.getPageData();
        pageBean.setPageData(syncUdToPcLogMapstruct.toDto(syncUdToPcLogEntitys));
        return pageBean;
    }

    @Override
    public SyncUdToPcLogDto selectSyncUdToPcLogByJobId(String jobId) {
        PageForm pageForm = new PageForm(1, 1);
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(jobId)) {
            specificationBuilder.add("jobId", SpecificationOperator.Operator.eq.name(), jobId);
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "lastTime");
        PageBean pageBean = this.getPage(specificationBuilder.generateSpecification(), pageForm, sort);
        List<SyncUdToPcLogEntity> syncUdToPcLogEntitys = (List<SyncUdToPcLogEntity>) pageBean.getPageData();
        if (null != syncUdToPcLogEntitys && !syncUdToPcLogEntitys.isEmpty()) {
            return syncUdToPcLogMapstruct.toDto(syncUdToPcLogEntitys.get(0));
        }
        return null;
    }

    @Override
    public SyncUdToPcLogDto findSyncUdToPcLogById(String syncUdToPcLogId) {
        SyncUdToPcLogEntity syncUdToPcLogEntity=this.getById(syncUdToPcLogId);
        return syncUdToPcLogMapstruct.toDto(syncUdToPcLogEntity);
    }

    @Override
    public void deleteSyncUdToPcLogByIds(String[] syncUdToPcLogIds) {
        this.deleteByIds(Arrays.asList(syncUdToPcLogIds));
    }
}
