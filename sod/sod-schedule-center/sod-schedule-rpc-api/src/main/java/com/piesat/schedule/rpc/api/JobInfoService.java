package com.piesat.schedule.rpc.api;

import com.piesat.dm.rpc.dto.DataLogicDto;
import com.piesat.dm.rpc.dto.DataTableDto;
import com.piesat.dm.rpc.dto.DatabaseDto;
import com.piesat.schedule.rpc.dto.JobInfoDto;

import java.util.List;
import java.util.Map;

/**
 * Created by zzj on 2019/12/20.
 */
public interface JobInfoService {
    public List<JobInfoDto> findJobList();

    public List<DatabaseDto> findAllDataBase();

    public List<Map<String, Object>>  getByDatabaseId(String databaseId);

    public Map<String,String> getByDatabaseIdAndClassId(String databaseId,String dataClassId);

    public void init();

    public void start(JobInfoDto jobInfoDto);

    public void stop(String id);

    public void stopByIds(List<String> ids);
}
