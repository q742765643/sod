package com.piesat.schedule.rpc.service.backup;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.common.utils.poi.ExcelUtil;
import com.piesat.dm.rpc.api.database.SchemaService;
import com.piesat.dm.rpc.dto.database.SchemaDto;
import com.piesat.schedule.client.api.ExecutorBiz;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.dao.backup.MetaBackupDao;
import com.piesat.schedule.entity.backup.MetaBackupEntity;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.api.backup.MetaBackupService;
import com.piesat.schedule.rpc.dto.backup.MetaBackupDto;
import com.piesat.schedule.rpc.mapstruct.backup.MetaBackupMapstruct;
import com.piesat.schedule.rpc.thread.ScheduleThread;
import com.piesat.ucenter.rpc.api.system.DictDataService;
import com.piesat.ucenter.rpc.dto.system.DictDataDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-26 15:59
 **/
@Service
public class MetaBackupServiceImpl extends BaseService<MetaBackupEntity> implements MetaBackupService{
    @Autowired
    private MetaBackupDao metaBackupDao;
    @Autowired
    private MetaBackupMapstruct metaBackupMapstruct;
    @Autowired
    private JobInfoService jobInfoService;
    @Autowired
    private ScheduleThread scheduleThread;
    @GrpcHthtClient
    private ExecutorBiz executorBiz;
    @GrpcHthtClient
    private SchemaService schemaService;
    @GrpcHthtClient
    private DictDataService dictDataService;

    @Override
    public BaseDao<MetaBackupEntity> getBaseDao() {
        return metaBackupDao;
    }
    @Override
    public PageBean selectBackupList(PageForm<MetaBackupDto> pageForm){
        MetaBackupEntity metaBackupEntity=metaBackupMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(metaBackupEntity.getDatabaseName())){
            specificationBuilder.add("databaseName", SpecificationOperator.Operator.likeAll.name(),metaBackupEntity.getDatabaseName());
        }
        if(StringUtils.isNotNullString(metaBackupEntity.getTaskName())){
            specificationBuilder.add("taskName", SpecificationOperator.Operator.likeAll.name(),metaBackupEntity.getTaskName());
        }
        if(null!=metaBackupEntity.getTriggerStatus()){
            specificationBuilder.add("triggerStatus",SpecificationOperator.Operator.eq.name(),metaBackupEntity.getTriggerStatus());
        }
        if(StringUtils.isNotNullString((String) metaBackupEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) metaBackupEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) metaBackupEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) metaBackupEntity.getParamt().get("endTime"));
        }
        if(StringUtils.isNotNullString(metaBackupEntity.getDatabaseId())){
            specificationBuilder.add("databaseId",SpecificationOperator.Operator.eq.name(),metaBackupEntity.getDatabaseId());
        }
        Sort sort=Sort.by(Sort.Direction.DESC,"createTime");
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<MetaBackupEntity> metaBackupEntities= (List<MetaBackupEntity>) pageBean.getPageData();
        pageBean.setPageData(metaBackupMapstruct.toDto(metaBackupEntities));
        return pageBean;

    }
    @Override
    public MetaBackupDto findBackupById(String metaBackupId){
        MetaBackupEntity metaBackupEntity=this.getById(metaBackupId);
        return metaBackupMapstruct.toDto(metaBackupEntity);

    }
    @Override
    public void saveBackup(MetaBackupDto metaBackupDto){
        MetaBackupEntity metaBackupEntity=metaBackupMapstruct.toEntity(metaBackupDto);
        this.getDataBase(metaBackupEntity);
        metaBackupEntity=this.saveNotNull(metaBackupEntity);
        jobInfoService.start(metaBackupMapstruct.toDto(metaBackupEntity));

    }
    @Override
    public void updateBackup(MetaBackupDto metaBackupDto){
        MetaBackupEntity metaBackupEntity=metaBackupMapstruct.toEntity(metaBackupDto);
        this.getDataBase(metaBackupEntity);
        this.saveNotNull(metaBackupEntity);
        jobInfoService.start(metaBackupDto);
    }
    @Override
    public void deleteBackupByIds(String[] metaBackupIds){
        this.deleteByIds(Arrays.asList(metaBackupIds));
        jobInfoService.stopByIds(Arrays.asList(metaBackupIds));
    }

    @Override
    public List<TreeVo> findMeta(String databaseId){
        SchemaDto schemaDto = schemaService.getDotById(databaseId);
        String parentId= schemaDto.getDatabase().getId();
        String databaseType= schemaDto.getDatabase().getDatabaseType();
        return executorBiz.findMeta(parentId,databaseType);
    }

    @Override
    public List<Map<String,String>> findDataBase(){
        List<Map<String,String>> maps=new ArrayList<>();
        List<String> vaules=new ArrayList<>();
        List<DictDataDto> dictDataDtos=dictDataService.selectDictDataByType("database_metadata");
        List<SchemaDto> schemaDtos = schemaService.findByLevel(1);
        for(DictDataDto dictDataDto:dictDataDtos){
            for(SchemaDto schemaDto : schemaDtos){
                if(schemaDto.getDatabase().getDatabaseIp().indexOf(dictDataDto.getDictValue())!=-1){
                    Map<String,String> map=new HashMap<>();
                    String adress= schemaDto.getDatabase().getDatabaseIp()+":"+ schemaDto.getDatabase().getDatabasePort()+":"+ schemaDto.getDatabase().getDatabaseInstance();
                    if(!vaules.contains(adress)){
                        vaules.add(adress);
                        map.put("KEY", schemaDto.getId());
                        map.put("VAULE", schemaDto.getDatabase().getDatabaseIp()+":"+ schemaDto.getDatabase().getDatabasePort()+":"+ schemaDto.getDatabase().getDatabaseInstance());
                        map.put("parentId", schemaDto.getDatabase().getId());
                        maps.add(map);
                    }

                }
            }
        }
        return maps;
    }
    public void getDataBase(MetaBackupEntity metaBackupEntity){
        SchemaDto schemaDto = schemaService.getDotById(metaBackupEntity.getDatabaseId());
        String parentId= schemaDto.getDatabase().getId();
        String databaseName= schemaDto.getDatabase().getDatabaseName()+"_"+ schemaDto.getDatabaseName();
        metaBackupEntity.setDatabaseName(databaseName);
        metaBackupEntity.setParentId(parentId);
        metaBackupEntity.setDatabaseType(schemaDto.getDatabase().getDatabaseType());
    }

    @Override
    public void handExecute(MetaBackupDto metaBackupDto){

        MetaBackupEntity metaBackupEntity=metaBackupMapstruct.toEntity(metaBackupDto);
        metaBackupEntity.setType("METABACKUP");
        metaBackupEntity.setTriggerLastTime(System.currentTimeMillis());
        this.getDataBase(metaBackupEntity);
        executorBiz.handMetaBack(metaBackupEntity);

    }

    public List<MetaBackupEntity> selectBackupList(MetaBackupDto metaBackupDto){
        MetaBackupEntity metaBackupEntity=metaBackupMapstruct.toEntity(metaBackupDto);
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(metaBackupEntity.getDatabaseName())){
            specificationBuilder.add("databaseName", SpecificationOperator.Operator.likeAll.name(),metaBackupEntity.getDatabaseName());
        }
        if(StringUtils.isNotNullString(metaBackupEntity.getTaskName())){
            specificationBuilder.add("taskName", SpecificationOperator.Operator.likeAll.name(),metaBackupEntity.getTaskName());
        }
        if(null!=metaBackupEntity.getTriggerStatus()){
            specificationBuilder.add("triggerStatus",SpecificationOperator.Operator.eq.name(),metaBackupEntity.getTriggerStatus());
        }
        if(StringUtils.isNotNullString((String) metaBackupEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) metaBackupEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) metaBackupEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) metaBackupEntity.getParamt().get("endTime"));
        }
        if(StringUtils.isNotNullString(metaBackupEntity.getDatabaseId())){
            specificationBuilder.add("databaseId",SpecificationOperator.Operator.eq.name(),metaBackupEntity.getDatabaseId());
        }
        Sort sort=Sort.by(Sort.Direction.DESC,"createTime");
        List<MetaBackupEntity> metaBackupEntities=this.getAll(specificationBuilder.generateSpecification(),sort);
        return metaBackupEntities;

    }
    @Override
    public void exportExcel(MetaBackupDto metaBackupDto){
        List<MetaBackupEntity> entities=this.selectBackupList(metaBackupDto);
        ExcelUtil<MetaBackupEntity> util=new ExcelUtil(MetaBackupEntity.class);
        util.exportExcel(entities,"元数据备份配置");
    }
}

