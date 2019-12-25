package com.piesat.schedule.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
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
    @Override
    public BaseDao<JobInfoEntity> getBaseDao() {
        return jobInfoDao;
    }
    @Override
    public List<JobInfoDto> findJobList(){
        List<JobInfoEntity> jobInfoEntities=jobInfoMapper.findJobList();
        return jobInfoMapstruct.toDto(jobInfoEntities);
    }
}

