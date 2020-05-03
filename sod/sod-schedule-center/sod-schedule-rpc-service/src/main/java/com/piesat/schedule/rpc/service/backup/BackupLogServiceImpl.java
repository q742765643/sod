package com.piesat.schedule.rpc.service.backup;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.common.utils.poi.ExcelUtil;
import com.piesat.schedule.dao.backup.BackupLogDao;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.rpc.api.backup.BackupLogService;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.rpc.dto.backup.BackupLogDto;
import com.piesat.schedule.rpc.mapstruct.backup.BackupLogMapstruct;
import com.piesat.ucenter.rpc.api.system.DictDataService;
import com.piesat.ucenter.rpc.dto.system.DictDataDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        SimpleSpecificationBuilder specificationBuilderOr=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(backupLogEntity.getDataClassId())){
            specificationBuilderOr.add("dataClassId", SpecificationOperator.Operator.likeAll.name(),backupLogEntity.getDataClassId());
            specificationBuilderOr.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(),backupLogEntity.getDataClassId());
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
        Specification specification=specificationBuilder.generateSpecification().and(specificationBuilderOr.generateSpecification());
        Sort sort=Sort.by(Sort.Direction.DESC,"backupTime");
        PageBean pageBean=this.getPage(specification,pageForm,sort);
        List<BackupLogEntity> backupLogEntities= (List<BackupLogEntity>) pageBean.getPageData();
        pageBean.setPageData(backupLogMapstruct.toDto(backupLogEntities));
        return pageBean;

    }
    @Override
    public BackupLogDto selectBackupLoByJobId(String jobId){
        PageForm pageForm=new PageForm(1,1);
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(jobId)){
            specificationBuilder.add("jobId", SpecificationOperator.Operator.eq.name(),jobId);
        }
        Sort sort=Sort.by(Sort.Direction.DESC,"backupTime");
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<BackupLogEntity> backupLogEntities= (List<BackupLogEntity>) pageBean.getPageData();
        if(null!=backupLogEntities&&!backupLogEntities.isEmpty()){
            return backupLogMapstruct.toDto(backupLogEntities.get(0));
        }
        return null;

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

    public List<BackupLogEntity> selectBackupLogList(BackupLogDto backupLogDto){
        BackupLogEntity backupLogEntity=backupLogMapstruct.toEntity(backupLogDto);
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(backupLogEntity.getDatabaseId())){
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(),backupLogEntity.getDatabaseId());
        }
        SimpleSpecificationBuilder specificationBuilderOr=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(backupLogEntity.getDataClassId())){
            specificationBuilderOr.add("dataClassId", SpecificationOperator.Operator.likeAll.name(),backupLogEntity.getDataClassId());
            specificationBuilderOr.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(),backupLogEntity.getDataClassId());
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
        Specification specification=specificationBuilder.generateSpecification().and(specificationBuilderOr.generateSpecification());
        Sort sort=Sort.by(Sort.Direction.DESC,"backupTime");
        List<BackupLogEntity> backupLogEntities=this.getAll(specification,sort);
        return backupLogEntities;

    }
    @Override
    public void exportExcel(BackupLogDto backupLogDto){
        List<BackupLogEntity> entities=this.selectBackupLogList(backupLogDto);
        ExcelUtil<BackupLogEntity> util=new ExcelUtil(BackupLogEntity.class);
        util.exportExcel(entities,"数据备份日志");
    }


}

