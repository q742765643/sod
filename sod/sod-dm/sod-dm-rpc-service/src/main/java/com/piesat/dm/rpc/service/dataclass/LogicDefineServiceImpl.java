package com.piesat.dm.rpc.service.dataclass;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.common.utils.poi.ExcelUtil;
import com.piesat.dm.dao.database.DatabaseDefineDao;
import com.piesat.dm.dao.dataclass.LogicDefineDao;
import com.piesat.dm.entity.dataclass.LogicDatabaseEntity;
import com.piesat.dm.entity.dataclass.LogicDefineEntity;
import com.piesat.dm.entity.dataclass.LogicStorageTypesEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.database.DatabaseDefineService;
import com.piesat.dm.rpc.api.dataclass.LogicDefineService;
import com.piesat.dm.rpc.dto.database.DatabaseDefineDto;
import com.piesat.dm.rpc.dto.dataclass.LogicDatabaseDto;
import com.piesat.dm.rpc.dto.dataclass.LogicDefineDto;
import com.piesat.dm.rpc.mapper.dataclass.LogicDefineMapper;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.rpc.dto.backup.BackupLogDto;
import com.piesat.ucenter.rpc.api.system.DictDataService;
import com.piesat.ucenter.rpc.dto.system.DictDataDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 数据用途定义
 *
 * @author cwh
 * @date 2019年 11月22日 16:36:19
 */
@Service
public class LogicDefineServiceImpl extends BaseService<LogicDefineEntity> implements LogicDefineService {
    @Autowired
    private LogicDefineDao logicDefineDao;
    @Autowired
    private LogicDefineMapper logicDefineMapper;
    @Autowired
    private DatabaseDefineDao databaseDefineDao;
    @Autowired
    private DatabaseDefineService databaseDefineService;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @GrpcHthtClient
    private DictDataService dictDataService;
    @Autowired
    private LogicDefineService logicDefineService;


    @Override
    public BaseDao<LogicDefineEntity> getBaseDao() {
        return logicDefineDao;
    }

    @Override
    public LogicDefineDto saveDto(LogicDefineDto logicDefineDto) {
        logicDefineDto.setCreateTime(new Date());
        LogicDefineEntity logicDefineEntity = this.logicDefineMapper.toEntity(logicDefineDto);
        logicDefineEntity = this.logicDefineDao.saveNotNull(logicDefineEntity);
        return this.logicDefineMapper.toDto(logicDefineEntity);
    }

    @Override
    public List<LogicDefineDto> all() {
        List<LogicDefineEntity> all = this.getAll();
        return this.logicDefineMapper.toDto(all);
    }

    @Override
    public List<LogicDefineDto> findByParam(LogicDefineDto logicDefineDto) {
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(logicDefineDto.getLogicFlag())){
            specificationBuilder.add("logicFlag", SpecificationOperator.Operator.likeAll.name(),logicDefineDto.getLogicFlag());
        }
        if(StringUtils.isNotNullString(logicDefineDto.getLogicName())){
            specificationBuilder.add("logicName", SpecificationOperator.Operator.likeAll.name(),logicDefineDto.getLogicName());
        }
        List<LogicDefineEntity> logicDefineEntities = this.getAll(specificationBuilder.generateSpecification());
        List<DatabaseDefineDto> all = this.databaseDefineService.all();
        List<LogicDefineDto> logicDefineDtos = this.logicDefineMapper.toDto(logicDefineEntities);
        for (LogicDefineDto logicDefineDto1 : logicDefineDtos) {
            List<LogicDatabaseDto> logicDatabaseEntityList = logicDefineDto1.getLogicDatabaseEntityList();
            for (LogicDatabaseDto logicDatabaseDto : logicDatabaseEntityList) {
                for (com.piesat.dm.rpc.dto.database.DatabaseDefineDto DatabaseDefineDto : all) {
                    if (DatabaseDefineDto.getId().equals(logicDatabaseDto.getDatabaseId())) {
                        logicDatabaseDto.setDatabaseName(DatabaseDefineDto.getDatabaseName());
                    }
                }
            }
        }
        return logicDefineDtos;
    }

    @Override
    public LogicDefineDto getDotById(String id) {
        LogicDefineEntity logicDefineEntity = this.getById(id);
        return this.logicDefineMapper.toDto(logicDefineEntity);
    }

    @Override
    public PageBean selectPageList(PageForm<LogicDefineDto> pageForm) {
        LogicDefineEntity logicDefineEntity=logicDefineMapper.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(logicDefineEntity.getLogicFlag())){
            specificationBuilder.add("logicFlag", SpecificationOperator.Operator.likeAll.name(),logicDefineEntity.getLogicFlag());
        }
        if(StringUtils.isNotNullString(logicDefineEntity.getLogicName())){
            specificationBuilder.add("logicName", SpecificationOperator.Operator.likeAll.name(),logicDefineEntity.getLogicName());
        }
        Sort sort=Sort.by(Sort.Direction.ASC,"serialNumber");
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<LogicDefineEntity> logicDefineEntities= (List<LogicDefineEntity>) pageBean.getPageData();
        List<LogicDefineDto> logicDefineDtos = logicDefineMapper.toDto(logicDefineEntities);
        pageBean.setPageData(logicDefineDtos);
        return pageBean;
    }

    @Override
    public List<LogicDefineDto> getAllLogicDefine() {
        List<LogicDefineEntity> logicDefineDaoAll = this.logicDefineDao.findAll();
        return this.logicDefineMapper.toDto(logicDefineDaoAll);
    }

    @Override
    public List<LogicDefineDto> findByLogicFlag(String logicFlag) {
        List<LogicDefineEntity> logicDefineEntities = this.logicDefineDao.findByLogicFlag(logicFlag);
        return this.logicDefineMapper.toDto(logicDefineEntities);
    }

    @Override
    public LogicDefineDto updateDto(LogicDefineDto logicDefineDto) {
        LogicDefineEntity logicDefineEntity=logicDefineMapper.toEntity(logicDefineDto);
        logicDefineEntity = this.saveNotNull(logicDefineEntity);
        return logicDefineMapper.toDto(logicDefineEntity);
    }

    @Override
    public List<Map<String, Object>> getLogicByUserId(String databaseIds) {
       return mybatisQueryMapper.getLogicByUserId(databaseIds.split(","));
    }

    @Override
    public void exportExcel(LogicDefineDto logicDefineDto) {
        Map<String,String> dataMap=new HashMap<>();
        Map<String,String> tableMap=new HashMap<>();

        List<DatabaseDefineDto> all = this.databaseDefineService.all();
        for(int i=0;i<all.size();i++){
            dataMap.put(all.get(i).getId(),all.get(i).getDatabaseName());
        }
        List<DictDataDto> dictDataDtos = dictDataService.selectDictDataByType("sys_storage_type");
        for(int i=0;i<dictDataDtos.size();i++){
            tableMap.put(dictDataDtos.get(i).getDictValue(),dictDataDtos.get(i).getDictLabel());
        }
        List<LogicDefineDto> dtoList = this.findByParam(logicDefineDto);
        List<LogicDefineEntity> entities=logicDefineMapper.toEntity(dtoList);
        for(int i=0;i<entities.size();i++){
            entities.get(i).setNum(i+1);
            LogicDefineEntity logicDefineEntity=entities.get(i);
            String xx="";
            String yy="";
            for(LogicStorageTypesEntity table:logicDefineEntity.getLogicStorageTypesEntityList()){
                xx+=tableMap.get(table.getStorageType())+";";
            }
            for(LogicDatabaseEntity database:logicDefineEntity.getLogicDatabaseEntityList()){
                yy+=dataMap.get(database.getDatabaseId())+";";
            }
            entities.get(i).setTableType(xx);
            entities.get(i).setDatabaseType(yy);
        }
        ExcelUtil<LogicDefineEntity> util=new ExcelUtil(LogicDefineEntity.class);
        util.exportExcel(entities,"数据库用途");
    }
}
