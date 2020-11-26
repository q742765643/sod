package com.piesat.schedule.rpc.service.syncdar;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.dao.syncdar.SyncDarLogDao;
import com.piesat.schedule.entity.syncdar.SyncDarLogEntity;
import com.piesat.schedule.rpc.api.syncdar.SyncDarLogService;
import com.piesat.schedule.rpc.dto.syncdar.SyncDarLogDto;
import com.piesat.schedule.rpc.mapstruct.syncdar.SyncDarLogMapstruct;
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
public class SyncDarLogServiceImpl extends BaseService<SyncDarLogEntity> implements SyncDarLogService {
    @Autowired
    private SyncDarLogDao syncDarLogDao;
    @Autowired
    private SyncDarLogMapstruct syncDarLogMapstruct;

    @Override
    public BaseDao<SyncDarLogEntity> getBaseDao() {
        return this.syncDarLogDao;
    }

    @Override
    public PageBean selectSyncDarLogList(PageForm<SyncDarLogDto> pageForm) {
        SyncDarLogEntity syncDarLogEntity = syncDarLogMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(syncDarLogEntity.getDatabaseId())) {
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(), syncDarLogEntity.getDatabaseId());
        }
        SimpleSpecificationBuilder specificationBuilderOr = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(syncDarLogEntity.getDataClassId())) {
            specificationBuilderOr.add("dataClassId", SpecificationOperator.Operator.likeAll.name(), syncDarLogEntity.getDataClassId());
            specificationBuilderOr.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(), syncDarLogEntity.getDataClassId());
        }
        if (StringUtils.isNotNullString(syncDarLogEntity.getProfileName())) {
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(), syncDarLogEntity.getProfileName());
        }
        if (null != syncDarLogEntity.getHandleCode()) {
            specificationBuilder.add("handleCode", SpecificationOperator.Operator.eq.name(), syncDarLogEntity.getHandleCode());
        }
        if (StringUtils.isNotNullString(syncDarLogEntity.getTableName())) {
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(), syncDarLogEntity.getTableName());
        }
        if (StringUtils.isNotNullString((String) syncDarLogEntity.getParamt().get("beginTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.ges.name(), (String) syncDarLogEntity.getParamt().get("beginTime"));
        }
        if (StringUtils.isNotNullString((String) syncDarLogEntity.getParamt().get("endTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.les.name(), (String) syncDarLogEntity.getParamt().get("endTime"));
        }
        Specification specification = specificationBuilder.generateSpecification().and(specificationBuilderOr.generateSpecification());
        Sort sort = Sort.by(Sort.Direction.DESC, "lastTime");
        PageBean pageBean = this.getPage(specification, pageForm, sort);
        List<SyncDarLogEntity> syncDarLogEntitys = (List<SyncDarLogEntity>) pageBean.getPageData();
        pageBean.setPageData(syncDarLogMapstruct.toDto(syncDarLogEntitys));
        return pageBean;
    }

    @Override
    public SyncDarLogDto selectSyncDarLogByJobId(String jobId) {
        PageForm pageForm = new PageForm(1, 1);
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(jobId)) {
            specificationBuilder.add("jobId", SpecificationOperator.Operator.eq.name(), jobId);
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "lastTime");
        PageBean pageBean = this.getPage(specificationBuilder.generateSpecification(), pageForm, sort);
        List<SyncDarLogEntity> syncDarLogEntitys = (List<SyncDarLogEntity>) pageBean.getPageData();
        if (null != syncDarLogEntitys && !syncDarLogEntitys.isEmpty()) {
            return syncDarLogMapstruct.toDto(syncDarLogEntitys.get(0));
        }
        return null;
    }

    @Override
    public SyncDarLogDto findSyncDarLogById(String syncDarLogId) {
        SyncDarLogEntity syncDarLogEntity=this.getById(syncDarLogId);
        return syncDarLogMapstruct.toDto(syncDarLogEntity);
    }

    @Override
    public void deleteSyncDarLogByIds(String[] syncDarLogIds) {
        this.deleteByIds(Arrays.asList(syncDarLogIds));
    }
}
