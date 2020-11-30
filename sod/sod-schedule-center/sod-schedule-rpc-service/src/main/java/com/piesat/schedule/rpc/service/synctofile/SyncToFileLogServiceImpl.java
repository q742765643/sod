package com.piesat.schedule.rpc.service.synctofile;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.dao.synctofile.SyncToFileLogDao;
import com.piesat.schedule.entity.synctofile.SyncToFileLogEntity;
import com.piesat.schedule.rpc.api.synctofile.SyncToFileLogService;
import com.piesat.schedule.rpc.dto.synctofile.SyncToFileLogDto;
import com.piesat.schedule.rpc.mapstruct.synctofile.SyncToFileLogMapstruct;
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
public class SyncToFileLogServiceImpl extends BaseService<SyncToFileLogEntity> implements SyncToFileLogService {
    @Autowired
    private SyncToFileLogDao syncToFileLogDao;
    @Autowired
    private SyncToFileLogMapstruct syncToFileLogMapstruct;

    @Override
    public BaseDao<SyncToFileLogEntity> getBaseDao() {
        return this.syncToFileLogDao;
    }

    @Override
    public PageBean selectSyncToFileLogList(PageForm<SyncToFileLogDto> pageForm) {
        SyncToFileLogEntity syncToFileLogEntity = syncToFileLogMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(syncToFileLogEntity.getDatabaseId())) {
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(), syncToFileLogEntity.getDatabaseId());
        }
        SimpleSpecificationBuilder specificationBuilderOr = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(syncToFileLogEntity.getDataClassId())) {
            specificationBuilderOr.add("dataClassId", SpecificationOperator.Operator.likeAll.name(), syncToFileLogEntity.getDataClassId());
            specificationBuilderOr.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(), syncToFileLogEntity.getDataClassId());
        }
        if (StringUtils.isNotNullString(syncToFileLogEntity.getProfileName())) {
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(), syncToFileLogEntity.getProfileName());
        }
        if (null != syncToFileLogEntity.getHandleCode()) {
            specificationBuilder.add("handleCode", SpecificationOperator.Operator.eq.name(), syncToFileLogEntity.getHandleCode());
        }
        if (StringUtils.isNotNullString(syncToFileLogEntity.getTableName())) {
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(), syncToFileLogEntity.getTableName());
        }
        if (StringUtils.isNotNullString((String) syncToFileLogEntity.getParamt().get("beginTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.ges.name(), (String) syncToFileLogEntity.getParamt().get("beginTime"));
        }
        if (StringUtils.isNotNullString((String) syncToFileLogEntity.getParamt().get("endTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.les.name(), (String) syncToFileLogEntity.getParamt().get("endTime"));
        }
        Specification specification = specificationBuilder.generateSpecification().and(specificationBuilderOr.generateSpecification());
        Sort sort = Sort.by(Sort.Direction.DESC, "lastTime");
        PageBean pageBean = this.getPage(specification, pageForm, sort);
        List<SyncToFileLogEntity> syncToFileLogEntitys = (List<SyncToFileLogEntity>) pageBean.getPageData();
        pageBean.setPageData(syncToFileLogMapstruct.toDto(syncToFileLogEntitys));
        return pageBean;
    }

    @Override
    public SyncToFileLogDto selectSyncToFileLogByJobId(String jobId) {
        PageForm pageForm = new PageForm(1, 1);
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(jobId)) {
            specificationBuilder.add("jobId", SpecificationOperator.Operator.eq.name(), jobId);
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "lastTime");
        PageBean pageBean = this.getPage(specificationBuilder.generateSpecification(), pageForm, sort);
        List<SyncToFileLogEntity> syncToFileLogEntitys = (List<SyncToFileLogEntity>) pageBean.getPageData();
        if (null != syncToFileLogEntitys && !syncToFileLogEntitys.isEmpty()) {
            return syncToFileLogMapstruct.toDto(syncToFileLogEntitys.get(0));
        }
        return null;
    }

    @Override
    public SyncToFileLogDto findSyncToFileLogById(String syncToFileLogId) {
        SyncToFileLogEntity syncToFileLogEntity=this.getById(syncToFileLogId);
        return syncToFileLogMapstruct.toDto(syncToFileLogEntity);
    }

    @Override
    public void deleteSyncToFileLogByIds(String[] syncToFileLogIds) {
        this.deleteByIds(Arrays.asList(syncToFileLogIds));
    }
}
