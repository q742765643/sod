package com.piesat.schedule.rpc.service.synces;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.dao.synces.SyncEsLogDao;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.entity.synces.SyncEsLogEntity;
import com.piesat.schedule.rpc.api.synces.SyncEsLogService;
import com.piesat.schedule.rpc.dto.synces.SyncEsLogDto;
import com.piesat.schedule.rpc.mapstruct.synces.SyncEsLogMapstruct;
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
public class SyncEsLogServiceImpl extends BaseService<SyncEsLogEntity> implements SyncEsLogService {
    @Autowired
    private SyncEsLogDao syncEsLogDao;
    @Autowired
    private SyncEsLogMapstruct syncEsLogMapstruct;

    @Override
    public BaseDao<SyncEsLogEntity> getBaseDao() {
        return this.syncEsLogDao;
    }

    @Override
    public PageBean selectSyncEsLogList(PageForm<SyncEsLogDto> pageForm) {
        SyncEsLogEntity syncEsLogEntity = syncEsLogMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(syncEsLogEntity.getDatabaseId())) {
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(), syncEsLogEntity.getDatabaseId());
        }
        SimpleSpecificationBuilder specificationBuilderOr = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(syncEsLogEntity.getDataClassId())) {
            specificationBuilderOr.add("dataClassId", SpecificationOperator.Operator.likeAll.name(), syncEsLogEntity.getDataClassId());
            specificationBuilderOr.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(), syncEsLogEntity.getDataClassId());
        }
        if (StringUtils.isNotNullString(syncEsLogEntity.getProfileName())) {
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(), syncEsLogEntity.getProfileName());
        }
        if (null != syncEsLogEntity.getHandleCode()) {
            specificationBuilder.add("handleCode", SpecificationOperator.Operator.eq.name(), syncEsLogEntity.getHandleCode());
        }
        if (StringUtils.isNotNullString(syncEsLogEntity.getTableName())) {
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(), syncEsLogEntity.getTableName());
        }
        if (StringUtils.isNotNullString((String) syncEsLogEntity.getParamt().get("beginTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.ges.name(), (String) syncEsLogEntity.getParamt().get("beginTime"));
        }
        if (StringUtils.isNotNullString((String) syncEsLogEntity.getParamt().get("endTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.les.name(), (String) syncEsLogEntity.getParamt().get("endTime"));
        }
        Specification specification = specificationBuilder.generateSpecification().and(specificationBuilderOr.generateSpecification());
        Sort sort = Sort.by(Sort.Direction.DESC, "lastTime");
        PageBean pageBean = this.getPage(specification, pageForm, sort);
        List<SyncEsLogEntity> syncEsLogEntitys = (List<SyncEsLogEntity>) pageBean.getPageData();
        pageBean.setPageData(syncEsLogMapstruct.toDto(syncEsLogEntitys));
        return pageBean;
    }

    @Override
    public SyncEsLogDto selectSyncEsLogByJobId(String jobId) {
        PageForm pageForm = new PageForm(1, 1);
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(jobId)) {
            specificationBuilder.add("jobId", SpecificationOperator.Operator.eq.name(), jobId);
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "lastTime");
        PageBean pageBean = this.getPage(specificationBuilder.generateSpecification(), pageForm, sort);
        List<SyncEsLogEntity> syncEsLogEntitys = (List<SyncEsLogEntity>) pageBean.getPageData();
        if (null != syncEsLogEntitys && !syncEsLogEntitys.isEmpty()) {
            return syncEsLogMapstruct.toDto(syncEsLogEntitys.get(0));
        }
        return null;
    }

    @Override
    public SyncEsLogDto findSyncEsLogById(String syncEsLogId) {
        SyncEsLogEntity syncEsLogEntity=this.getById(syncEsLogId);
        return syncEsLogMapstruct.toDto(syncEsLogEntity);
    }

    @Override
    public void deleteSyncEsLogByIds(String[] syncEsLogIds) {
        this.deleteByIds(Arrays.asList(syncEsLogIds));
    }
}
