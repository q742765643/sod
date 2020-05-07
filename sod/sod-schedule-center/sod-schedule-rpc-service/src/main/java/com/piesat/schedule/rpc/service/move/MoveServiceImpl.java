package com.piesat.schedule.rpc.service.move;

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
import com.piesat.schedule.dao.move.MoveDao;
import com.piesat.schedule.entity.clear.ClearEntity;
import com.piesat.schedule.entity.move.MoveEntity;
import com.piesat.schedule.entity.move.MoveLogEntity;
import com.piesat.schedule.mapper.JobInfoMapper;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.api.move.MoveService;
import com.piesat.schedule.rpc.dto.move.MoveDto;
import com.piesat.schedule.rpc.dto.move.MoveLogDto;
import com.piesat.schedule.rpc.mapstruct.move.MoveMapstruct;
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

import java.util.*;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-24 16:27
 **/
@Service
public class MoveServiceImpl extends BaseService<MoveEntity> implements MoveService{
    @Autowired
    private MoveDao moveDao;
    @Autowired
    private MoveMapstruct moveMapstruct;
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
    public BaseDao<MoveEntity> getBaseDao() {
        return moveDao;
    }
    @Override
    public PageBean selectMoveList(PageForm<MoveDto> pageForm){
        MoveEntity moveEntity=moveMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(moveEntity.getDatabaseId())){
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(),moveEntity.getDatabaseId());
        }
        SimpleSpecificationBuilder specificationBuilderOr=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(moveEntity.getDataClassId())){
            specificationBuilderOr.add("dataClassId", SpecificationOperator.Operator.likeAll.name(),moveEntity.getDataClassId());
            specificationBuilderOr.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(),moveEntity.getDataClassId());
        }
        if(StringUtils.isNotNullString(moveEntity.getProfileName())){
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(),moveEntity.getProfileName());
        }
        if(null!=moveEntity.getTriggerStatus()){
            specificationBuilder.add("triggerStatus",SpecificationOperator.Operator.eq.name(),moveEntity.getTriggerStatus());
        }
        if(StringUtils.isNotNullString(moveEntity.getTableName())){
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(),moveEntity.getTableName());
        }
        if(StringUtils.isNotNullString((String) moveEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) moveEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) moveEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) moveEntity.getParamt().get("endTime"));
        }
        Specification specification=specificationBuilder.generateSpecification().and(specificationBuilderOr.generateSpecification());
        Sort sort=Sort.by(Sort.Direction.DESC,"createTime");
        PageBean pageBean=this.getPage(specification,pageForm,sort);
        List<MoveEntity> moveEntities= (List<MoveEntity>) pageBean.getPageData();
        pageBean.setPageData(moveMapstruct.toDto(moveEntities));
        return pageBean;

    }
    @Override
    public MoveDto selectmoveByParam(String databaseId, String dataClassId){
        PageForm pageForm=new PageForm(1,1);
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(databaseId)){
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(),databaseId);
        }
        SimpleSpecificationBuilder specificationBuilderOr=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(dataClassId)){
            specificationBuilderOr.add("dataClassId", SpecificationOperator.Operator.likeAll.name(),dataClassId);
            specificationBuilderOr.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(),dataClassId);
        }
        Specification specification=specificationBuilder.generateSpecification().and(specificationBuilderOr.generateSpecification());
        PageBean pageBean=this.getPage(specification,pageForm,Sort.unsorted());
        List<MoveEntity> moveEntities= (List<MoveEntity>) pageBean.getPageData();
        if(null!=moveEntities&&!moveEntities.isEmpty()){
            return moveMapstruct.toDto(moveEntities.get(0));
        }
        return null;
    }
    @Override
    public MoveDto findMoveById(String moveId){
        MoveEntity moveEntity=this.getById(moveId);
        return moveMapstruct.toDto(moveEntity);

    }
    @Override
    public void saveMove(MoveDto moveDto){
        MoveEntity moveEntity=moveMapstruct.toEntity(moveDto);
        this.getDataBaseAndClassId(moveEntity);
        moveEntity=this.saveNotNull(moveEntity);
        diSendService.sendMove(moveEntity);
        jobInfoService.start(moveMapstruct.toDto(moveEntity));
        String tableName = moveEntity.getTableName();
        List<DataLogicDto> dataLogic = this.dataLogicService.getDataLogic(moveEntity.getDataClassId(), moveEntity.getDatabaseId(), tableName.contains(".")?tableName.substring(tableName.indexOf(".")+1):tableName);
        for (DataLogicDto dl : dataLogic) {
            StorageConfigurationDto scd = new StorageConfigurationDto();
            scd.setClassLogicId(dl.getId());
            scd.setMoveIdentifier(1);
            scd.setMoveId(moveEntity.getId());
            this.storageConfigurationService.updateDataAuthorityConfig(scd);
        }
    }
    @Override
    public void updateMove(MoveDto moveDto){
        MoveEntity moveEntity=moveMapstruct.toEntity(moveDto);
        this.getDataBaseAndClassId(moveEntity);
        moveEntity=this.saveNotNull(moveEntity);
        diSendService.sendMove(moveEntity);
        jobInfoService.start(moveDto);
    }
    @Override
    public void deleteMoveByIds(String[] moveIds){
        this.deleteByIds(Arrays.asList(moveIds));
        for(int i=0;i<moveIds.length;i++){
            diSendService.sendDeleteDi(moveIds[i]);
        }
        jobInfoService.stopByIds(Arrays.asList(moveIds));
    }

    @Override
    public List<Map<String,Object>> findDatabase(){
        List<DictDataDto> dictDataDtos=dictDataService.selectDictDataByType("move_database");
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
        List<String> isHave=jobInfoMapper.selectMoveDataClassId(dataBaseId);
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

    public void getDataBaseAndClassId(MoveEntity moveEntity){
        DataRetrieval dataRetrieval= dataBaseService.getByDatabaseIdAndClassId(moveEntity.getDatabaseId(),moveEntity.getDataClassId());
        moveEntity.setDdataId(dataRetrieval.getDdataId());
        moveEntity.setTableName(dataRetrieval.getTableName());
        moveEntity.setVTableName(dataRetrieval.getVTableName());
        moveEntity.setDatabaseType(dataRetrieval.getDatabaseType());
        moveEntity.setParentId(dataRetrieval.getParentId());
        moveEntity.setProfileName(dataRetrieval.getProfileName());
        if(null==dataRetrieval.getForeignKey()||!StringUtils.isNotNullString(dataRetrieval.getVTableName())){
            TableForeignKeyDto tableForeignKeyDto=new TableForeignKeyDto();
            tableForeignKeyDto.setEleColumn("D_FILE_ID");
            tableForeignKeyDto.setKeyColumn("D_FILE_ID");
            List<TableForeignKeyDto> tableForeignKeyDtos=new ArrayList<>();
            tableForeignKeyDtos.add(tableForeignKeyDto);
            dataRetrieval.setForeignKey(JSON.toJSONString(tableForeignKeyDtos));
        }
        if(!StringUtils.isNotNullString(dataRetrieval.getPrimaryKey())){
            dataRetrieval.setPrimaryKey("D_FILE_ID");
        }
        moveEntity.setPrimaryKey(dataRetrieval.getPrimaryKey());
        moveEntity.setForeignKey(dataRetrieval.getForeignKey());
    }
    public List<MoveEntity> selectMoveList(MoveDto moveDto){
        MoveEntity moveEntity=moveMapstruct.toEntity(moveDto);
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(moveEntity.getDatabaseId())){
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(),moveEntity.getDatabaseId());
        }
        SimpleSpecificationBuilder specificationBuilderOr=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(moveEntity.getDataClassId())){
            specificationBuilderOr.add("dataClassId", SpecificationOperator.Operator.likeAll.name(),moveEntity.getDataClassId());
            specificationBuilderOr.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(),moveEntity.getDataClassId());
        }
        if(StringUtils.isNotNullString(moveEntity.getProfileName())){
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(),moveEntity.getProfileName());
        }
        if(null!=moveEntity.getTriggerStatus()){
            specificationBuilder.add("triggerStatus",SpecificationOperator.Operator.eq.name(),moveEntity.getTriggerStatus());
        }
        if(StringUtils.isNotNullString(moveEntity.getTableName())){
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(),moveEntity.getTableName());
        }
        if(StringUtils.isNotNullString((String) moveEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) moveEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) moveEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) moveEntity.getParamt().get("endTime"));
        }
        Specification specification=specificationBuilder.generateSpecification().and(specificationBuilderOr.generateSpecification());
        Sort sort=Sort.by(Sort.Direction.DESC,"createTime");
        List<MoveEntity> moveEntities=this.getAll(specification,sort);
        return moveEntities;

    }

    @Override
    public void exportExcel(MoveDto moveDto){
        List<MoveEntity> entities=this.selectMoveList(moveDto);
        ExcelUtil<MoveEntity> util=new ExcelUtil(MoveEntity.class);
        util.exportExcel(entities,"数据迁移配置");
    }

    @Override
    public List<MoveDto> findByDataClassId(String dataClassId) {
        List<MoveEntity> moveEntities = moveDao.findByDataClassId(dataClassId);
        return moveMapstruct.toDto(moveEntities);
    }
}

