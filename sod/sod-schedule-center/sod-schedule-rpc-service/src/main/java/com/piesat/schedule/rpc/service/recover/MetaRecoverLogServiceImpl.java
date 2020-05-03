package com.piesat.schedule.rpc.service.recover;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.schedule.client.api.ExecutorBiz;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.dao.recover.MetaRecoverLogDao;
import com.piesat.schedule.entity.backup.MetaBackupEntity;
import com.piesat.schedule.entity.backup.MetaBackupLogEntity;
import com.piesat.schedule.entity.recover.MetaRecoverLogEntity;
import com.piesat.schedule.rpc.api.recover.MetaRecoverLogService;
import com.piesat.schedule.rpc.dto.backup.MetaBackupLogDto;
import com.piesat.schedule.rpc.dto.recover.MetaRecoverLogDto;
import com.piesat.schedule.rpc.mapstruct.recover.MetaRecoverLogMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-03 10:34
 **/
@Service
public class MetaRecoverLogServiceImpl extends BaseService<MetaRecoverLogEntity> implements MetaRecoverLogService{
    @Autowired
    private MetaRecoverLogDao metaRecoverLogDao;
    @Autowired
    private MetaRecoverLogMapstruct metaRecoverLogMapstruct;
    @GrpcHthtClient
    private ExecutorBiz executorBiz;
    @GrpcHthtClient
    private DatabaseService databaseService;

    @Override
    public BaseDao<MetaRecoverLogEntity> getBaseDao() {
        return metaRecoverLogDao;
    }

    @Override
    public PageBean selectMetaRecoverLogList(PageForm<MetaRecoverLogDto> pageForm){
        MetaRecoverLogEntity metaRecoverLogEntity=metaRecoverLogMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();

        if(StringUtils.isNotNullString(metaRecoverLogEntity.getDatabaseName())){
            specificationBuilder.add("databaseName", SpecificationOperator.Operator.likeAll.name(),metaRecoverLogEntity.getDatabaseName());
        }
        if(StringUtils.isNotNullString(metaRecoverLogEntity.getTaskName())){
            specificationBuilder.add("taskName", SpecificationOperator.Operator.likeAll.name(),metaRecoverLogEntity.getTaskName());
        }
        if(null!=metaRecoverLogEntity.getHandleCode()){
            specificationBuilder.add("handleCode",SpecificationOperator.Operator.eq.name(),metaRecoverLogEntity.getHandleCode());
        }
        if(StringUtils.isNotNullString((String) metaRecoverLogEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) metaRecoverLogEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) metaRecoverLogEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) metaRecoverLogEntity.getParamt().get("endTime"));
        }
        Sort sort=Sort.by(Sort.Direction.DESC,"createTime");
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<MetaRecoverLogEntity> metaRecoverLogEntities= (List<MetaRecoverLogEntity>) pageBean.getPageData();
        pageBean.setPageData(metaRecoverLogMapstruct.toDto(metaRecoverLogEntities));
        return pageBean;

    }
    @Override
    public  MetaRecoverLogDto findMetaRecoverLogById(String metaRecoverLogId){
        MetaRecoverLogEntity metaRecoverLogEntity=this.getById(metaRecoverLogId);
        return metaRecoverLogMapstruct.toDto(metaRecoverLogEntity);

    }
    @Override
    public void deleteMetaRecoverLogByIds(String[] MetaRecoverLogIds){
        this.deleteByIds(Arrays.asList(MetaRecoverLogIds));
    }

    @Override
    public void recover(MetaRecoverLogDto metaRecoverLogDto){
        MetaRecoverLogEntity metaRecoverLogEntity= metaRecoverLogMapstruct.toEntity(metaRecoverLogDto);
        this.getDataBase(metaRecoverLogEntity);
        executorBiz.recover(metaRecoverLogEntity);
    }
    @Override
    public  List<TreeVo> getFileList(String databaseId, String storageDirectory){
        DatabaseDto databaseDto= databaseService.getDotById(databaseId);
        return executorBiz.getFileList(databaseDto.getDatabaseDefine().getId(),storageDirectory);
    }

    public void getDataBase(MetaRecoverLogEntity metaRecoverLogEntity){
        DatabaseDto databaseDto= databaseService.getDotById(metaRecoverLogEntity.getDatabaseId());
        String parentId=databaseDto.getDatabaseDefine().getId();
        String databaseName=databaseDto.getDatabaseDefine().getDatabaseName()+"_"+databaseDto.getDatabaseName();
        metaRecoverLogEntity.setDatabaseName(databaseName);
        metaRecoverLogEntity.setParentId(parentId);
        metaRecoverLogEntity.setDatabaseType(databaseDto.getDatabaseDefine().getDatabaseType());
    }
    @Override
    public List<TreeVo> getFileChidren(String childrenPath){
        return executorBiz.getFileChidren(childrenPath);
    }
    @Override
    public Map<String,Object> parsingPath(String path){
        return executorBiz.parsingPath(path);
    }

}

