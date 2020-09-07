package com.piesat.schedule.rpc.service.clear;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.common.utils.poi.ExcelUtil;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.schedule.client.api.ExecutorBiz;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.dao.clear.MetaClearDao;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.backup.MetaBackupEntity;
import com.piesat.schedule.entity.clear.MetaClearEntity;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.api.clear.MetaClearService;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.rpc.dto.backup.MetaBackupDto;
import com.piesat.schedule.rpc.dto.clear.MetaClearDto;
import com.piesat.schedule.rpc.mapstruct.clear.MetaClearMapstruct;
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
 * @create: 2020-03-09 11:12
 **/
@Service
public class MetaClearServiceImpl extends BaseService<MetaClearEntity> implements MetaClearService{
    @Autowired
    private MetaClearDao metaClearDao;
    @Autowired
    private MetaClearMapstruct metaClearMapstruct;
    @Autowired
    private JobInfoService jobInfoService;
    @GrpcHthtClient
    private ExecutorBiz executorBiz;
    @GrpcHthtClient
    private DatabaseService databaseService;
    @GrpcHthtClient
    private DictDataService dictDataService;
    @Override
    public BaseDao<MetaClearEntity> getBaseDao() {
        return metaClearDao;
    }
    @Override
    public PageBean selectMetaClearList(PageForm<MetaClearDto> pageForm){
        MetaClearEntity metaClearEntity=metaClearMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(metaClearEntity.getDatabaseName())){
            specificationBuilder.add("databaseName", SpecificationOperator.Operator.likeAll.name(),metaClearEntity.getDatabaseName());
        }
        if(StringUtils.isNotNullString(metaClearEntity.getTaskName())){
            specificationBuilder.add("taskName", SpecificationOperator.Operator.likeAll.name(),metaClearEntity.getTaskName());
        }
        if(null!=metaClearEntity.getTriggerStatus()){
            specificationBuilder.add("triggerStatus",SpecificationOperator.Operator.eq.name(),metaClearEntity.getTriggerStatus());
        }
        if(StringUtils.isNotNullString((String) metaClearEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) metaClearEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) metaClearEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) metaClearEntity.getParamt().get("endTime"));
        }
        Sort sort=Sort.by(Sort.Direction.DESC,"createTime");
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<MetaClearEntity> metaClearEntities= (List<MetaClearEntity>) pageBean.getPageData();
        pageBean.setPageData(metaClearMapstruct.toDto(metaClearEntities));
        return pageBean;

    }
    @Override
    public MetaClearDto findMetaClearById(String metaClearId){
        MetaClearEntity metaClearEntity=this.getById(metaClearId);
        return metaClearMapstruct.toDto(metaClearEntity);

    }
    @Override
    public void saveMetaClear(MetaClearDto metaClearDto){
        MetaClearEntity metaClearEntity=metaClearMapstruct.toEntity(metaClearDto);
        this.getDataBase(metaClearEntity);
        metaClearEntity=this.saveNotNull(metaClearEntity);
        jobInfoService.start(metaClearMapstruct.toDto(metaClearEntity));

    }
    @Override
    public void updateMetaClear(MetaClearDto metaClearDto){
        MetaClearEntity metaClearEntity=metaClearMapstruct.toEntity(metaClearDto);
        this.getDataBase(metaClearEntity);
        this.saveNotNull(metaClearEntity);
        jobInfoService.start(metaClearDto);
    }
    @Override
    public void deleteMeteClearByIds(String[] metaClearIds){
        this.deleteByIds(Arrays.asList(metaClearIds));
        jobInfoService.stopByIds(Arrays.asList(metaClearIds));
    }

    @Override
    public List<TreeVo> findAllTableByIp(String databaseId){
        MetaClearEntity metaClearEntity=new MetaClearEntity();
        metaClearEntity.setDatabaseId(databaseId);
        this.getDataBase(metaClearEntity);
        return executorBiz.findAllTableByIp(metaClearEntity.getParentId(),metaClearEntity.getDatabaseType());
    }
    @Override
    public List<Map<String,String>> findDataBase(){
        List<Map<String,String>> maps=new ArrayList<>();
        List<String> vaules=new ArrayList<>();
        List<DictDataDto> dictDataDtos=dictDataService.selectDictDataByType("database_metaclear");
        List<DatabaseDto> databaseDtos=databaseService.findByLevel(1);
        for(DictDataDto dictDataDto:dictDataDtos){
            for(DatabaseDto databaseDto:databaseDtos){
                if(databaseDto.getDatabaseDefine().getDatabaseIp().indexOf(dictDataDto.getDictValue())!=-1){
                    Map<String,String> map=new HashMap<>();
                    String adress=databaseDto.getDatabaseDefine().getDatabaseIp()+":"+databaseDto.getDatabaseDefine().getDatabasePort()+":"+databaseDto.getDatabaseDefine().getDatabaseInstance();
                    if(!vaules.contains(adress)){
                        vaules.add(adress);
                        map.put("KEY",databaseDto.getId());
                        map.put("VAULE",databaseDto.getDatabaseDefine().getDatabaseIp()+":"+databaseDto.getDatabaseDefine().getDatabasePort()+":"+databaseDto.getDatabaseDefine().getDatabaseInstance());
                        map.put("parentId",databaseDto.getDatabaseDefine().getId());
                        maps.add(map);
                    }
                }
            }
        }
        return maps;
    }

    public void getDataBase(MetaClearEntity metaClearEntity){
        DatabaseDto databaseDto= databaseService.getDotById(metaClearEntity.getDatabaseId());
        String parentId=databaseDto.getDatabaseDefine().getId();
        String databaseName=databaseDto.getDatabaseDefine().getDatabaseName()+"_"+databaseDto.getDatabaseName();
        metaClearEntity.setDatabaseName(databaseName);
        metaClearEntity.setParentId(parentId);
        metaClearEntity.setDatabaseType(databaseDto.getDatabaseDefine().getDatabaseType());
    }

    public List<MetaClearEntity> selectMetaClearList(MetaClearDto metaClearDto){
        MetaClearEntity metaClearEntity=metaClearMapstruct.toEntity(metaClearDto);
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(metaClearEntity.getDatabaseName())){
            specificationBuilder.add("databaseName", SpecificationOperator.Operator.likeAll.name(),metaClearEntity.getDatabaseName());
        }
        if(StringUtils.isNotNullString(metaClearEntity.getTaskName())){
            specificationBuilder.add("taskName", SpecificationOperator.Operator.likeAll.name(),metaClearEntity.getTaskName());
        }
        if(null!=metaClearEntity.getTriggerStatus()){
            specificationBuilder.add("triggerStatus",SpecificationOperator.Operator.eq.name(),metaClearEntity.getTriggerStatus());
        }
        if(StringUtils.isNotNullString((String) metaClearEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) metaClearEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) metaClearEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) metaClearEntity.getParamt().get("endTime"));
        }
        Sort sort=Sort.by(Sort.Direction.DESC,"createTime");
        List<MetaClearEntity> metaClearEntities= this.getAll(specificationBuilder.generateSpecification(),sort);
        return metaClearEntities;

    }

    @Override
    public void exportExcel(MetaClearDto metaClearDto){
        List<MetaClearEntity> entities=this.selectMetaClearList(metaClearDto);
        ExcelUtil<MetaClearEntity> util=new ExcelUtil(MetaClearEntity.class);
        util.exportExcel(entities,"元数据清除配置");
    }
}

