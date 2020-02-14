package com.piesat.schedule.rpc.service;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.dm.rpc.api.DataLogicService;
import com.piesat.dm.rpc.api.DataTableService;
import com.piesat.dm.rpc.api.DatabaseService;
import com.piesat.dm.rpc.dto.DataTableDto;
import com.piesat.dm.rpc.dto.DatabaseDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-14 10:29
 **/
@Service
public class DataBaseService
{
    @GrpcHthtClient
    private DatabaseService databaseService;
    @GrpcHthtClient
    private DataTableService dataTableService;
    @GrpcHthtClient
    private DataLogicService dataLogicService;

    public List<DatabaseDto> findAllDataBase(){
        List<DatabaseDto> databaseDtos=databaseService.all();
        return databaseDtos;
    }

    public List<Map<String, Object>> getByDatabaseId(String databaseId){
        List<Map<String, Object>> mapList=dataLogicService.getByDatabaseId(databaseId);
        return mapList;
    }
    public Map<String,String> getByDatabaseIdAndClassId(String databaseId,String dataClassId){
        List<DataTableDto>  dataTableDtos=dataTableService.getByDatabaseIdAndClassId(databaseId,dataClassId);
        Map<String,String> map=new HashMap<>();
        if(dataTableDtos.size()>1){
            for(DataTableDto dataTableDto:dataTableDtos){
                if("K".equals(dataTableDto.getDbTableType())){
                    map.put("tableName",dataTableDto.getTableName());
                }else{
                    map.put("vTableName",dataTableDto.getTableName());
                }
            }
        }
        if(dataTableDtos.size()==1)
        {
            map.put("tableName",dataTableDtos.get(0).getTableName());
            map.put("vTableName","");
        }
        return map;

    }
}

