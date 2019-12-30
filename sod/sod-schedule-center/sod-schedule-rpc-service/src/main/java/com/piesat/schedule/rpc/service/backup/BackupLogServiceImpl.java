package com.piesat.schedule.rpc.service.backup;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.dao.backup.BackupLogDao;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.rpc.api.backup.BackupLogService;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.rpc.dto.backup.BackupLogDto;
import com.piesat.schedule.rpc.mapstruct.backup.BackupLogMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-29 19:27
 **/
@Service
public class BackupLogServiceImpl extends BaseService<BackupLogEntity> implements BackupLogService{
    @Autowired
    private BackupLogDao backupLogDao;
    @Autowired
    private BackupLogMapstruct backupLogMapstruct;
    @Override
    public BaseDao<BackupLogEntity> getBaseDao() {
        return backupLogDao;
    }
    @Override
    public PageBean selectBackupLogList(PageForm<BackupLogDto> pageForm){
        BackupLogEntity backupLogEntity=backupLogMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(backupLogEntity.getDatabaseId())){
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(),backupLogEntity.getDatabaseId());
        }
        if(StringUtils.isNotNullString(backupLogEntity.getDataClassId())){
            specificationBuilder.add("dataClassId", SpecificationOperator.Operator.likeAll.name(),backupLogEntity.getDataClassId());
            specificationBuilder.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(),backupLogEntity.getDataClassId());
        }
        if(StringUtils.isNotNullString(backupLogEntity.getProfileName())){
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(),backupLogEntity.getProfileName());
        }
        if(null!=backupLogEntity.getHandleCode()){
            specificationBuilder.add("handleCode",SpecificationOperator.Operator.eq.name(),backupLogEntity.getHandleCode());
        }
        if(StringUtils.isNotNullString(backupLogEntity.getTableName())){
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(),backupLogEntity.getTableName());
        }
        if(StringUtils.isNotNullString((String) backupLogEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) backupLogEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) backupLogEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) backupLogEntity.getParamt().get("endTime"));
        }
        Sort sort=Sort.by(Sort.Direction.ASC,"createTime");
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<BackupLogEntity> backupLogEntities= (List<BackupLogEntity>) pageBean.getPageData();
        pageBean.setPageData(backupLogMapstruct.toDto(backupLogEntities));
        return pageBean;

    }

    @Override
    public BackupLogDto findBackupLogById(String backupLogId){
        BackupLogEntity backupLogEntity=this.getById(backupLogId);
        return backupLogMapstruct.toDto(backupLogEntity);

    }
    @Override
    public void deleteBackupLogByIds(String[] backupLogIds){
        this.deleteByIds(Arrays.asList(backupLogIds));
    }
}

