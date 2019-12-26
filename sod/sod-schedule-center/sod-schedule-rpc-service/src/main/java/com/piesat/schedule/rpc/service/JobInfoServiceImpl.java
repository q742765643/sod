package com.piesat.schedule.rpc.service;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.rpc.api.DataTableService;
import com.piesat.dm.rpc.api.DatabaseService;
import com.piesat.dm.rpc.dto.DataTableDto;
import com.piesat.dm.rpc.dto.DatabaseDto;
import com.piesat.schedule.dao.JobInfoDao;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.mapper.JobInfoMapper;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.dto.JobInfoDto;
import com.piesat.schedule.rpc.mapstruct.JobInfoMapstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-20 16:29
 **/
@Service
public class JobInfoServiceImpl extends BaseService<JobInfoEntity> implements JobInfoService{
    @Autowired
    private JobInfoDao jobInfoDao;
    @Autowired
    private JobInfoMapstruct jobInfoMapstruct;
    @Autowired
    private JobInfoMapper jobInfoMapper;
    @GrpcHthtClient
    private DatabaseService databaseService;
    @GrpcHthtClient
    private DataTableService dataTableService;
    @Override
    public BaseDao<JobInfoEntity> getBaseDao() {
        return jobInfoDao;
    }
    @Override
    public List<JobInfoDto> findJobList(){
        List<JobInfoEntity> jobInfoEntities=jobInfoMapper.findJobList();
        return jobInfoMapstruct.toDto(jobInfoEntities);
    }
    @Override
    public List<DatabaseDto> findAllDataBase(){
        List<DatabaseDto> databaseDtos=databaseService.all();
        return databaseDtos;
    }
    @Override
    public List<DataTableDto> getByDatabaseId(String databaseId){
        List<DataTableDto> dataTableDtos=dataTableService.getByDatabaseId(databaseId);
        return dataTableDtos;
    }
}

