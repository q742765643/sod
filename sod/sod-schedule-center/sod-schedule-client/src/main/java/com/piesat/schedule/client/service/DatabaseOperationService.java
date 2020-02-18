package com.piesat.schedule.client.service;

import com.piesat.schedule.entity.DatabaseOperationVo;
import com.piesat.schedule.entity.clear.ClearLogEntity;
import com.piesat.schedule.mapper.DatabaseOperationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-14 15:15
 **/
@Service
public class DatabaseOperationService {
    @Autowired
    private DatabaseOperationMapper databaseOperationMapper;


    public Date selectKtableMaxTime(String ktable, String conditions){
        DatabaseOperationVo databaseOperationVo=new DatabaseOperationVo();
        databaseOperationVo.setKtable(ktable);
        databaseOperationVo.setConditions(conditions);
       return databaseOperationMapper.selectKtableMaxTime( databaseOperationVo);
    }

    public int delteKtable(ClearLogEntity clearLogEntity, String conditions){
        DatabaseOperationVo databaseOperationVo=new DatabaseOperationVo();
        databaseOperationVo.setKtable(clearLogEntity.getTableName());
        databaseOperationVo.setConditions(conditions);
        return databaseOperationMapper.delteKtable(databaseOperationVo);
    }

    public int deleteVtable(ClearLogEntity clearLogEntity, String conditions){
        DatabaseOperationVo databaseOperationVo=new DatabaseOperationVo();
        databaseOperationVo.setKtable(clearLogEntity.getTableName());
        databaseOperationVo.setConditions(conditions);
        databaseOperationVo.setVtable(clearLogEntity.getVTableName());
        databaseOperationVo.setFk(clearLogEntity.getForeignKey());
        return databaseOperationMapper.deleteVtable(databaseOperationVo);
    }

    public List<Map<String,Object>> selectXuguPartition(String schemaName, String tableName){
        return databaseOperationMapper.selectXuguPartition(schemaName,tableName);
    }

    public int deletePartition(String tableName,String partName){
        return databaseOperationMapper.deletePartition(tableName,partName);
    }
}

