package com.piesat.schedule.mapper;

import com.piesat.schedule.entity.DatabaseOperationVo;
import com.piesat.schedule.entity.IndexVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zzj on 2020/2/14.
 */
@Component
public interface DatabaseOperationMapper {
    public int selectTableCount(DatabaseOperationVo databaseOperationVo);

    public Date selectKtableMaxTime(DatabaseOperationVo databaseOperationVo);

    public int delteKtable(DatabaseOperationVo databaseOperationVo);

    public int deleteVtable(DatabaseOperationVo databaseOperationVo);

    public List<Map<String,Object>> selectXuguPartition(@Param("schemaName") String schemaName, @Param("tableName") String tableName);

    public int deletePartition(@Param("tableName") String tableName,@Param("partName") String partName);

    public List<Map<String,Object>> selectByKCondition(DatabaseOperationVo databaseOperationVo);

    public List<Map<String,Object>> selectByVCondition(DatabaseOperationVo databaseOperationVo);

    public long updateIndex(IndexVo indexVo);

    public long deleteIndex(IndexVo indexVo);

}
