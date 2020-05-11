package com.piesat.schedule.rpc.api;

import com.piesat.schedule.rpc.dto.JobInfoDto;

import java.util.List;
import java.util.Map;

/**
 * Created by zzj on 2019/12/20.
 */
public interface JobInfoService {

    JobInfoDto saveDto(JobInfoDto jobInfoDto);

    public List<JobInfoDto> findJobList();

    public Object getJobById(String id);

    public void init();

    public void start(JobInfoDto jobInfoDto);

    public void stop(String id);

    public void stopByIds(List<String> ids);

    public void execute(String id);

    public void executeB(String id,String time);

    public void startById(String id);

    public void stopById(String id);
}
