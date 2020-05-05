package com.piesat.schedule.rpc.service.recover;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.schedule.client.api.ExecutorBiz;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.dao.recover.MetaRecoverLogDao;
import com.piesat.schedule.dao.recover.RecoverLogDao;
import com.piesat.schedule.entity.recover.MetaRecoverLogEntity;
import com.piesat.schedule.entity.recover.RecoverLogEntity;
import com.piesat.schedule.rpc.api.recover.MetaRecoverLogService;
import com.piesat.schedule.rpc.api.recover.RecoverLogService;
import com.piesat.schedule.rpc.dto.recover.MetaRecoverLogDto;
import com.piesat.schedule.rpc.dto.recover.RecoverLogDto;
import com.piesat.schedule.rpc.mapstruct.recover.MetaRecoverLogMapstruct;
import com.piesat.schedule.rpc.mapstruct.recover.RecoverLogMapstruct;
import com.piesat.schedule.rpc.service.DataBaseService;
import com.piesat.schedule.rpc.vo.DataRetrieval;
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
public class RecoverLogServiceImpl extends BaseService<RecoverLogEntity> implements RecoverLogService {
    @Autowired
    private RecoverLogDao recoverLogDao;
    @Autowired
    private RecoverLogMapstruct recoverLogMapstruct;
    @Autowired
    private DataBaseService dataBaseService;
    @GrpcHthtClient
    private ExecutorBiz executorBiz;
    @GrpcHthtClient
    private DatabaseService databaseService;

    @Override
    public BaseDao<RecoverLogEntity> getBaseDao() {
        return recoverLogDao;
    }

    @Override
    public PageBean selectRecoverLogList(PageForm<RecoverLogDto> pageForm){
        RecoverLogEntity recoverLogEntity=recoverLogMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(recoverLogEntity.getDatabaseId())){
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(),recoverLogEntity.getDatabaseId());
        }
        SimpleSpecificationBuilder specificationBuilderOr=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(recoverLogEntity.getDataClassId())){
            specificationBuilderOr.add("dataClassId", SpecificationOperator.Operator.likeAll.name(),recoverLogEntity.getDataClassId());
            specificationBuilderOr.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(),recoverLogEntity.getDataClassId());
        }
        if(StringUtils.isNotNullString(recoverLogEntity.getProfileName())){
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(),recoverLogEntity.getProfileName());
        }
        if(null!=recoverLogEntity.getHandleCode()){
            specificationBuilder.add("handleCode",SpecificationOperator.Operator.eq.name(),recoverLogEntity.getHandleCode());
        }
        if(StringUtils.isNotNullString(recoverLogEntity.getTableName())){
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(),recoverLogEntity.getTableName());
        }
        if(StringUtils.isNotNullString((String) recoverLogEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) recoverLogEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) recoverLogEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) recoverLogEntity.getParamt().get("endTime"));
        }
        Sort sort=Sort.by(Sort.Direction.DESC,"createTime");
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<RecoverLogEntity> recoverLogEntities= (List<RecoverLogEntity>) pageBean.getPageData();
        pageBean.setPageData(recoverLogMapstruct.toDto(recoverLogEntities));
        return pageBean;

    }
    @Override
    public  RecoverLogDto findRecoverLogById(String recoverLogId){
        RecoverLogEntity recoverLogEntity=this.getById(recoverLogId);
        return recoverLogMapstruct.toDto(recoverLogEntity);

    }
    @Override
    public void deleteRecoverLogByIds(String[] recoverLogIds){
        this.deleteByIds(Arrays.asList(recoverLogIds));
    }


    @Override
    public  List<TreeVo> getDataFileList(String databaseId, String dataClassId, String path){
        RecoverLogEntity recoverLogEntity=new RecoverLogEntity();
        recoverLogEntity.setDatabaseId(databaseId);
        recoverLogEntity.setDataClassId(dataClassId);
        recoverLogEntity.setStorageDirectory(path);
        this.getDataBase(recoverLogEntity);
        return executorBiz.getDataFileList(recoverLogEntity);
    }

    @Override
    public List<TreeVo> getFileChidren(String childrenPath){
        return executorBiz.getFileChidren(childrenPath);
    }
    @Override
    public void recoverStructedData(RecoverLogDto recoverLogDto){
        RecoverLogEntity recoverLogEntity=recoverLogMapstruct.toEntity(recoverLogDto);
        this.getDataBase(recoverLogEntity);
        executorBiz.recoverStructedData(recoverLogEntity);
    }
    @Override
    public List<Map<String,Object>> md5Check(List<String> paths){
        return executorBiz.md5Check(paths);
    }
    public void getDataBase(RecoverLogEntity recoverLogEntity){
        DataRetrieval dataRetrieval= dataBaseService.getByDatabaseIdAndClassId(recoverLogEntity.getDatabaseId(),recoverLogEntity.getDataClassId());
        recoverLogEntity.setDdataId(dataRetrieval.getDdataId());
        recoverLogEntity.setTableName(dataRetrieval.getTableName());
        recoverLogEntity.setVTableName(dataRetrieval.getVTableName());
        recoverLogEntity.setDatabaseType(dataRetrieval.getDatabaseType());
        recoverLogEntity.setParentId(dataRetrieval.getParentId());
        recoverLogEntity.setProfileName(dataRetrieval.getProfileName());
    }
}

