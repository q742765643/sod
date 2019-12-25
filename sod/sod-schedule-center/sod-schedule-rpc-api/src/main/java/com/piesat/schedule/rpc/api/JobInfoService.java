package com.piesat.schedule.rpc.api;

import com.piesat.schedule.rpc.dto.JobInfoDto;

import java.util.List;

/**
 * Created by zzj on 2019/12/20.
 */
public interface JobInfoService {
    public List<JobInfoDto> findJobList();
}
