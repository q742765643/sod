package com.piesat.schedule.mapper;

import com.piesat.schedule.entity.JobInfoEntity;
import org.apache.ibatis.annotations.Param;
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

    public String findTypeById(String id);

    public JobInfoEntity findById(String id);

    public List<String> selectBackupDataClassId();

    public List<String> selectClearDataClassId();

    public List<String> selectMoveDataClassId();

    public void updateTriggerStatus(@Param("triggerStatus")int triggerStatus, @Param("id")String id);


}

