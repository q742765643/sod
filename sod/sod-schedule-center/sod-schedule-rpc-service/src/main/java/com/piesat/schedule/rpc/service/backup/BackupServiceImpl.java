package com.piesat.schedule.rpc.service.backup;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.dao.backup.BackupDao;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.rpc.api.backup.BackupService;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.rpc.mapstruct.backup.BackupMapstruct;
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
 * @create: 2019-12-23 09:17
 **/
@Service
public class BackupServiceImpl extends BaseService<BackupEntity> implements BackupService{
    @Autowired
    private BackupDao backupDao;
    @Autowired
    private BackupMapstruct backupMapstruct;
    @Override
    public BaseDao<BackupEntity> getBaseDao() {
        return backupDao;
    }
    @Override
    public PageBean selectBackupList(PageForm<BackUpDto> pageForm){
        BackupEntity backupEntity=backupMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(backupEntity.getDatabaseId())){
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(),backupEntity.getDatabaseId());
        }
        if(StringUtils.isNotNullString(backupEntity.getDataClassId())){
            specificationBuilder.add("dataClassId", SpecificationOperator.Operator.likeAll.name(),backupEntity.getDataClassId());
            specificationBuilder.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(),backupEntity.getDataClassId());
        }
        if(StringUtils.isNotNullString(backupEntity.getProfileName())){
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(),backupEntity.getProfileName());
        }
        if(null!=backupEntity.getTriggerStatus()){
            specificationBuilder.add("triggerStatus",SpecificationOperator.Operator.eq.name(),backupEntity.getTriggerStatus());
        }
        if(StringUtils.isNotNullString(backupEntity.getTableName())){
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(),backupEntity.getTableName());
        }
        if(StringUtils.isNotNullString((String) backupEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) backupEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) backupEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) backupEntity.getParamt().get("endTime"));
        }
        Sort sort=Sort.by(Sort.Direction.ASC,"createTime");
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<BackupEntity> backupEntities= (List<BackupEntity>) pageBean.getPageData();
        pageBean.setPageData(backupMapstruct.toDto(backupEntities));
        return pageBean;

    }
    @Override
    public BackUpDto findBackupById(String backupId){
        BackupEntity backupEntity=this.getById(backupId);
        return backupMapstruct.toDto(backupEntity);

    }
    @Override
    public void saveBackup(BackUpDto backUpDto){
        BackupEntity backupEntity=backupMapstruct.toEntity(backUpDto);
        this.save(backupEntity);
    }
    @Override
    public void updateBackup(BackUpDto backUpDto){
        BackupEntity backupEntity=backupMapstruct.toEntity(backUpDto);
        this.save(backupEntity);
    }
    @Override
    public void deleteBackupByIds(String[] backupIds){
        this.deleteByIds(Arrays.asList(backupIds));
    }
}

