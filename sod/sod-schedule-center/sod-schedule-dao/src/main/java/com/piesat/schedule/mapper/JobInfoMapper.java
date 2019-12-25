package com.piesat.schedule.mapper;

import com.piesat.schedule.entity.JobInfoEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-25 10:56
 **/
@Component
public interface JobInfoMapper {

    public List<JobInfoEntity> findJobList();
}

