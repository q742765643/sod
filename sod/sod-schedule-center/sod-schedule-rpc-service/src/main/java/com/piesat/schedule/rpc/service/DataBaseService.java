package com.piesat.schedule.rpc.service;

import com.alibaba.fastjson.JSON;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.api.dataclass.DataLogicService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.datatable.TableForeignKeyDto;
import com.piesat.schedule.rpc.vo.DataRetrieval;
import com.piesat.util.ResultT;
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

    public DatabaseDto findDataBaseById(String dataBaseId){
        DatabaseDto databaseDto=databaseService.getDotById(dataBaseId);
        return databaseDto;
    }

    public List<Map<String, Object>> getByDatabaseId(String databaseId){
        List<Map<String, Object>> mapList=dataLogicService.getByDatabaseId(databaseId);
        return mapList;
    }
    public DataRetrieval getByDatabaseIdAndClassId(String databaseId,String dataClassId){
     /*   List<DataTableDto>  dataTableDtos=dataTableService.getByDatabaseIdAndClassId(databaseId,dataClassId);
        Map<String,String> map=new HashMap<>();
        if(dataTableDtos.size()>1){
            for(DataTableDto dataTableDto:dataTableDtos){
                if("K".equals(dataTableDto.getDbTableType())){
                    map.put("tableName",dataTableDto.getTableName());
                    map.put("logicId",dataTableDto.getClassLogic().getId());
                    map.put("tableId",dataTableDto.getId());
                }else{
                    map.put("vTableName",dataTableDto.getTableName());
                }
            }
        }
        if(dataTableDtos.size()==1)
        {
            map.put("tableName",dataTableDtos.get(0).getTableName());
            map.put("logicId",dataTableDtos.get(0).getClassLogic().getId());
            map.put("tableId",dataTableDtos.get(0).getId());
            map.put("vTableName","");
        }*/
        DataRetrieval dataRetrieval=new DataRetrieval();
        Map<String,String> map=new HashMap<>();
        ResultT resultT=dataTableService.getOverview(databaseId,dataClassId);
        Map<String,Object> mapResultT= (Map<String, Object>) resultT.getData();
        DatabaseDto databaseDto= (DatabaseDto) mapResultT.get("database");
        String schemaName=databaseDto.getSchemaName();
        String parentId=databaseDto.getDatabaseDefine().getId();
        String databaseName=databaseDto.getDatabaseDefine().getDatabaseName()+"_"+databaseDto.getDatabaseName();
        String tableName= (String) mapResultT.get("K");
        String vTableName= (String)mapResultT.get("E");
        String databaseType=databaseDto.getDatabaseDefine().getDatabaseType();
        String ddataId= (String) mapResultT.get("D_DATA_ID");
        String primaryKey= (String) mapResultT.get("primaryKey");
        String dataName= (String) mapResultT.get("CLASSNAME");
        List<TableForeignKeyDto> tableForeignKeyDtos= (List<TableForeignKeyDto>) mapResultT.get("foreignKey");
        dataRetrieval.setDatabaseType(databaseType);
        dataRetrieval.setDdataId(ddataId);
        dataRetrieval.setTableName(schemaName+"."+tableName);
        if(StringUtils.isNotNullString(vTableName)){
            dataRetrieval.setVTableName(schemaName+"."+vTableName);
        }
        dataRetrieval.setProfileName(databaseName+"_"+dataName);
        dataRetrieval.setParentId(parentId);
        dataRetrieval.setPrimaryKey(primaryKey);
        //dataRetrieval.setVTableName("");
        if(null!=tableForeignKeyDtos&&tableForeignKeyDtos.size()>0){
            dataRetrieval.setForeignKey(JSON.toJSONString(tableForeignKeyDtos));
        }

        return dataRetrieval;

    }



}

