package com.piesat.schedule.rpc.service.clear;

import com.alibaba.fastjson.JSON;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.common.utils.poi.ExcelUtil;
import com.piesat.dm.rpc.api.StorageConfigurationService;
import com.piesat.dm.rpc.api.dataclass.DataLogicService;
import com.piesat.dm.rpc.dto.StorageConfigurationDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.dataclass.DataLogicDto;
import com.piesat.dm.rpc.dto.datatable.TableForeignKeyDto;
import com.piesat.schedule.dao.clear.ClearDao;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.clear.ClearEntity;
import com.piesat.schedule.entity.clear.ClearEntity;
import com.piesat.schedule.entity.clear.ClearLogEntity;
import com.piesat.schedule.mapper.JobInfoMapper;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.api.clear.ClearService;
import com.piesat.schedule.rpc.dto.clear.ClearDto;
import com.piesat.schedule.rpc.dto.clear.ClearLogDto;
import com.piesat.schedule.rpc.mapstruct.clear.ClearMapstruct;
import com.piesat.schedule.rpc.service.DataBaseService;
import com.piesat.schedule.rpc.service.DiSendService;
import com.piesat.schedule.rpc.vo.DataRetrieval;
import com.piesat.ucenter.rpc.api.system.DictDataService;
import com.piesat.ucenter.rpc.dto.system.DictDataDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-24 11:11
 **/
@Service
public class ClearServiceImpl extends BaseService<ClearEntity> implements ClearService{
    @Autowired
    private ClearMapstruct clearMapstruct;
    @Autowired
    private ClearDao clearDao;
    @Autowired
    private JobInfoService jobInfoService;
    @Autowired
    private DataBaseService dataBaseService;
    @Autowired
    private JobInfoMapper jobInfoMapper;
    @Autowired
    private DiSendService diSendService;
    @GrpcHthtClient
    private DictDataService dictDataService;
    @GrpcHthtClient
    private DataLogicService dataLogicService;
    @GrpcHthtClient
    private StorageConfigurationService storageConfigurationService;
    @Override
    public BaseDao<ClearEntity> getBaseDao() {
        return clearDao;
    }
    @Override
    public PageBean selectClearList(PageForm<ClearDto> pageForm){
        ClearEntity clearEntity=clearMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(clearEntity.getDatabaseId())){
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(),clearEntity.getDatabaseId());
        }
        SimpleSpecificationBuilder specificationBuilderOr=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(clearEntity.getDataClassId())){
            specificationBuilderOr.add("dataClassId", SpecificationOperator.Operator.likeAll.name(),clearEntity.getDataClassId());
            specificationBuilderOr.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(),clearEntity.getDataClassId());
        }
        if(StringUtils.isNotNullString(clearEntity.getProfileName())){
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(),clearEntity.getProfileName());
        }
        if(null!=clearEntity.getTriggerStatus()){
            specificationBuilder.add("triggerStatus",SpecificationOperator.Operator.eq.name(),clearEntity.getTriggerStatus());
        }
        if(StringUtils.isNotNullString(clearEntity.getTableName())){
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(),clearEntity.getTableName());
        }
        if(StringUtils.isNotNullString((String) clearEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) clearEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) clearEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) clearEntity.getParamt().get("endTime"));
        }
        Specification specification=specificationBuilder.generateSpecification().and(specificationBuilderOr.generateSpecification());
        Sort sort=Sort.by(Sort.Direction.DESC,"createTime");
        PageBean pageBean=this.getPage(specification,pageForm,sort);
        List<ClearEntity> clearEntities= (List<ClearEntity>) pageBean.getPageData();
        pageBean.setPageData(clearMapstruct.toDto(clearEntities));
        return pageBean;

    }

    @Override
    public ClearDto selectClearByParam(String databaseId, String dataClassId){
        PageForm pageForm=new PageForm(1,1);
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(databaseId)){
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(),databaseId);
        }
        if(StringUtils.isNotNullString(dataClassId)){
            specificationBuilder.add("dataClassId", SpecificationOperator.Operator.likeAll.name(),dataClassId);
            specificationBuilder.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(),dataClassId);
        }
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,Sort.unsorted());
        List<ClearEntity> clearEntities= (List<ClearEntity>) pageBean.getPageData();
        if(null!=clearEntities&&!clearEntities.isEmpty()){
            return clearMapstruct.toDto(clearEntities.get(0));
        }
        return null;
    }
    @Override
    public ClearDto findClearById(String clearId){
        ClearEntity clearEntity=this.getById(clearId);
        return clearMapstruct.toDto(clearEntity);

    }
    @Override
    @Transactional
    public void saveClear(ClearDto clearDto){
        ClearEntity clearEntity=clearMapstruct.toEntity(clearDto);
        this.getDataBaseAndClassId(clearEntity);
        clearEntity=this.saveNotNull(clearEntity);
        diSendService.sendClear(clearEntity);
        jobInfoService.start(clearMapstruct.toDto(clearEntity));
        List<DataLogicDto> dataLogic = this.dataLogicService.getDataLogic(clearEntity.getDataClassId(), clearEntity.getDatabaseId(), clearEntity.getTableName());
        for (DataLogicDto dl : dataLogic) {
            StorageConfigurationDto scd = new StorageConfigurationDto();
            scd.setClassLogicId(dl.getId());
            scd.setBackupIdentifier(1);
            scd.setBackupId(clearEntity.getId());
            this.storageConfigurationService.updateDataAuthorityConfig(scd);
        }
    }
    @Override
    @Transactional
    public void updateClear(ClearDto clearDto){
        ClearEntity clearEntity=clearMapstruct.toEntity(clearDto);
        this.getDataBaseAndClassId(clearEntity);
        clearEntity=this.saveNotNull(clearEntity);
        diSendService.sendClear(clearEntity);
        jobInfoService.start(clearDto);

    }
    @Override
    public void deleteClearByIds(String[] clearIds){
        this.deleteByIds(Arrays.asList(clearIds));
        for(int i=0;i<clearIds.length;i++){
            diSendService.sendDeleteDi(clearIds[i]);
        }
        jobInfoService.stopByIds(Arrays.asList(clearIds));

    }

    @Override
    public List<Map<String,Object>> findDatabase(){
        List<DictDataDto> dictDataDtos=dictDataService.selectDictDataByType("clear_database");
        List<String> dicts=new ArrayList<>();
        for(DictDataDto dictDataDto:dictDataDtos) {
            dicts.add(dictDataDto.getDictValue());
        }

        List<Map<String,Object>> databaseDtos=new ArrayList<>();
        List<DatabaseDto> databaseListAll= dataBaseService.findAllDataBase();
        for(DatabaseDto databaseDto:databaseListAll){
            String databaseName=databaseDto.getDatabaseDefine().getDatabaseName()+"_"+databaseDto.getDatabaseName();
            String parentId=databaseDto.getDatabaseDefine().getId();
            if(dicts.contains(parentId.toUpperCase())) {
                LinkedHashMap<String,Object> map=new LinkedHashMap<>();
                map.put("KEY",databaseDto.getId());
                map.put("VALUE",databaseName);
                databaseDtos.add(map);
            }

        }

        return databaseDtos;

    }

    @Override
    public List<Map<String,Object>> findDataClassId(String dataBaseId, String dataClassId){

        List<Map<String,Object>> dataClassIds=new ArrayList<>();

        List<Map<String, Object>> mapList=dataBaseService.getByDatabaseId(dataBaseId);
        List<String> isHave=jobInfoMapper.selectClearDataClassId(dataBaseId);
        for(Map<String, Object> map:mapList){
            String dataClass= (String) map.get("DATA_CLASS_ID");
            String className= (String) map.get("CLASS_NAME");
            String ddataId=(String)map.get("D_DATA_ID");
            if(!isHave.contains(dataClass)||dataClass.equals(dataClassId)){
                LinkedHashMap<String,Object> dataClassIdMap=new LinkedHashMap<>();
                dataClassIdMap.put("KEY",dataClass);
                dataClassIdMap.put("VALUE",className);
                dataClassIdMap.put("D_DATA_ID",ddataId);
                dataClassIds.add(dataClassIdMap);
            }
        }

        return dataClassIds;

    }
    public void getDataBaseAndClassId(ClearEntity clearEntity){
        DataRetrieval dataRetrieval= dataBaseService.getByDatabaseIdAndClassId(clearEntity.getDatabaseId(),clearEntity.getDataClassId());
        clearEntity.setDdataId(dataRetrieval.getDdataId());
        clearEntity.setTableName(dataRetrieval.getTableName());
        clearEntity.setVTableName(dataRetrieval.getVTableName());
        clearEntity.setDatabaseType(dataRetrieval.getDatabaseType());
        clearEntity.setParentId(dataRetrieval.getParentId());
        clearEntity.setProfileName(dataRetrieval.getProfileName());
        if(null==dataRetrieval.getForeignKey()||!StringUtils.isNotNullString(dataRetrieval.getVTableName())){
            TableForeignKeyDto tableForeignKeyDto=new TableForeignKeyDto();
            tableForeignKeyDto.setEleColumn("D_RECORD_ID");
            tableForeignKeyDto.setKeyColumn("D_RECORD_ID");
            List<TableForeignKeyDto> tableForeignKeyDtos=new ArrayList<>();
            tableForeignKeyDtos.add(tableForeignKeyDto);
            dataRetrieval.setForeignKey(JSON.toJSONString(tableForeignKeyDtos));
        }

        clearEntity.setForeignKey(dataRetrieval.getForeignKey());
    }

    public List<ClearEntity> selectClearList(ClearDto clearDto){
        ClearEntity clearEntity=clearMapstruct.toEntity(clearDto);
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(clearEntity.getDatabaseId())){
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(),clearEntity.getDatabaseId());
        }
        SimpleSpecificationBuilder specificationBuilderOr=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(clearEntity.getDataClassId())){
            specificationBuilderOr.add("dataClassId", SpecificationOperator.Operator.likeAll.name(),clearEntity.getDataClassId());
            specificationBuilderOr.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(),clearEntity.getDataClassId());
        }
        if(StringUtils.isNotNullString(clearEntity.getProfileName())){
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(),clearEntity.getProfileName());
        }
        if(null!=clearEntity.getTriggerStatus()){
            specificationBuilder.add("triggerStatus",SpecificationOperator.Operator.eq.name(),clearEntity.getTriggerStatus());
        }
        if(StringUtils.isNotNullString(clearEntity.getTableName())){
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(),clearEntity.getTableName());
        }
        if(StringUtils.isNotNullString((String) clearEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) clearEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) clearEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) clearEntity.getParamt().get("endTime"));
        }
        Specification specification=specificationBuilder.generateSpecification().and(specificationBuilderOr.generateSpecification());
        Sort sort=Sort.by(Sort.Direction.DESC,"createTime");
        List<ClearEntity> clearEntities=this.getAll(specification,sort);
        return clearEntities;

    }

    @Override
    public void exportExcel(ClearDto clearDto){
        List<ClearEntity> entities=this.selectClearList(clearDto);
        ExcelUtil<ClearEntity> util=new ExcelUtil(ClearEntity.class);
        util.exportExcel(entities,"数据清除配置");
    }
}

