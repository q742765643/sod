package com.piesat.schedule.rpc.service.backup;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.common.utils.poi.ExcelUtil;
import com.piesat.schedule.client.api.ExecutorBiz;
import com.piesat.schedule.dao.backup.BackupLogDao;
import com.piesat.schedule.dao.backup.MetaBackupDao;
import com.piesat.schedule.dao.backup.MetaBackupLogDao;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.entity.backup.MetaBackupEntity;
import com.piesat.schedule.entity.backup.MetaBackupLogEntity;
import com.piesat.schedule.rpc.api.backup.BackupLogService;
import com.piesat.schedule.rpc.api.backup.MetaBackupLogService;
import com.piesat.schedule.rpc.api.backup.MetaBackupService;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.rpc.dto.backup.BackupLogDto;
import com.piesat.schedule.rpc.dto.backup.MetaBackupLogDto;
import com.piesat.schedule.rpc.mapstruct.backup.BackupLogMapstruct;
import com.piesat.schedule.rpc.mapstruct.backup.MetaBackupLogMapstruct;
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
public class MetaBackupLogServiceImpl extends BaseService<MetaBackupLogEntity> implements MetaBackupLogService {
    @Autowired
    private MetaBackupLogDao metaBackupLogDao;
    @Autowired
    private MetaBackupLogMapstruct metaBackupLogMapstruct;
    @GrpcHthtClient
    private ExecutorBiz executorBiz;

    @Override
    public BaseDao<MetaBackupLogEntity> getBaseDao() {
        return metaBackupLogDao;
    }
    @Override
    public PageBean selectMetaBackupLogList(PageForm<MetaBackupLogDto> pageForm){
        MetaBackupLogEntity metaBackupLogEntity=metaBackupLogMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();

        if(StringUtils.isNotNullString(metaBackupLogEntity.getDatabaseName())){
            specificationBuilder.add("databaseName", SpecificationOperator.Operator.likeAll.name(),metaBackupLogEntity.getDatabaseName());
        }
        if(StringUtils.isNotNullString(metaBackupLogEntity.getTaskName())){
            specificationBuilder.add("taskName", SpecificationOperator.Operator.likeAll.name(),metaBackupLogEntity.getTaskName());
        }
        if(null!=metaBackupLogEntity.getHandleCode()){
            specificationBuilder.add("handleCode",SpecificationOperator.Operator.eq.name(),metaBackupLogEntity.getHandleCode());
        }
        if(StringUtils.isNotNullString((String) metaBackupLogEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) metaBackupLogEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) metaBackupLogEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) metaBackupLogEntity.getParamt().get("endTime"));
        }
        if(StringUtils.isNotNullString(metaBackupLogEntity.getDatabaseId())){
            specificationBuilder.add("databaseId",SpecificationOperator.Operator.eq.name(),metaBackupLogEntity.getDatabaseId());
        }
        Sort sort=Sort.by(Sort.Direction.DESC,"createTime");
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<MetaBackupLogEntity> metaBackupLogEntities= (List<MetaBackupLogEntity>) pageBean.getPageData();
        pageBean.setPageData(metaBackupLogMapstruct.toDto(metaBackupLogEntities));
        return pageBean;

    }
    @Override
    public MetaBackupLogDto findMetaBackupLogById(String metaBackupLogId){
        MetaBackupLogEntity metaBackupLogEntity=this.getById(metaBackupLogId);
        return metaBackupLogMapstruct.toDto(metaBackupLogEntity);

    }
    @Override
    public void deleteMetaBackupLogByIds(String[] metaBackupLogIds){
        this.deleteByIds(Arrays.asList(metaBackupLogIds));
    }
    public List<MetaBackupLogEntity> selectMetaBackupLogList(MetaBackupLogDto metaBackupLogDto){
        MetaBackupLogEntity metaBackupLogEntity=metaBackupLogMapstruct.toEntity(metaBackupLogDto);
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();

        if(StringUtils.isNotNullString(metaBackupLogEntity.getDatabaseName())){
            specificationBuilder.add("databaseName", SpecificationOperator.Operator.likeAll.name(),metaBackupLogEntity.getDatabaseName());
        }
        if(StringUtils.isNotNullString(metaBackupLogEntity.getTaskName())){
            specificationBuilder.add("taskName", SpecificationOperator.Operator.likeAll.name(),metaBackupLogEntity.getTaskName());
        }
        if(null!=metaBackupLogEntity.getHandleCode()){
            specificationBuilder.add("handleCode",SpecificationOperator.Operator.eq.name(),metaBackupLogEntity.getHandleCode());
        }
        if(StringUtils.isNotNullString((String) metaBackupLogEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) metaBackupLogEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) metaBackupLogEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) metaBackupLogEntity.getParamt().get("endTime"));
        }
        if(StringUtils.isNotNullString(metaBackupLogEntity.getDatabaseId())){
            specificationBuilder.add("databaseId",SpecificationOperator.Operator.eq.name(),metaBackupLogEntity.getDatabaseId());
        }
        Sort sort=Sort.by(Sort.Direction.DESC,"createTime");
        List<MetaBackupLogEntity> metaBackupLogEntities=this.getAll(specificationBuilder.generateSpecification(),sort);
        return metaBackupLogEntities;

    }

    @Override
    public void exportExcel(MetaBackupLogDto metaBackupLogDto){
        List<MetaBackupLogEntity> entities=this.selectMetaBackupLogList(metaBackupLogDto);
        ExcelUtil<MetaBackupLogEntity> util=new ExcelUtil(MetaBackupLogEntity.class);
        util.exportExcel(entities,"数据备份日志");
    }



}

