package com.piesat.dm.rpc.service.newdata;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.dm.rpc.api.*;
import com.piesat.dm.rpc.dto.LogicDefineDto;
import com.piesat.dm.rpc.dto.LogicStorageTypesDto;
import com.piesat.ucenter.rpc.api.system.DictDataService;
import com.piesat.ucenter.rpc.dto.system.DictDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/5 19:03
 */
@Service
public class NewdataApplyGrpcService {

    @Autowired
    private LogicDefineService logicDefineService;

    @Autowired
    private DataLogicService dataLogicService;

    @GrpcHthtClient
    private DictDataService dictDataService;


    public List<Map<String, Object>> getLogicInfo() {
        List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();

        // 获取逻辑库类型
        List<LogicDefineDto> logiclist = logicDefineService.all();
        //查询物理库-逻辑库
        List<Map<String, Object>> distinctDatabaseAndLogic = dataLogicService.getDistinctDatabaseAndLogic();
        //查询所有的表类型
        Map<String,String> dictMap = new HashMap<String,String>();
        List<DictDataDto> dictDataDtos = dictDataService.selectDictDataByType("table_type");
        if(dictDataDtos != null){
            for(DictDataDto dictDataDto:dictDataDtos){
                dictMap.put(dictDataDto.getDictValue(),dictDataDto.getDictLabel());//E_table 要素表
            }
        }

        if (logiclist != null && logiclist.size() > 0){
            for (int i = logiclist.size()-1; i >=0; i--) {
                LogicDefineDto logicDefineDto = logiclist.get(i);
                Map<String,Object> logicDefineMap = JSONObject.parseObject(JSON.toJSONString(logicDefineDto));
                //逻辑库对应的表类型
                List<LogicStorageTypesDto> logicStorageTypesEntityList = logicDefineDto.getLogicStorageTypesEntityList();
                List<Map<String, Object>> tableTypeList = new ArrayList<Map<String, Object>>();
                if(logicStorageTypesEntityList != null){
                    for(LogicStorageTypesDto logicStorageTypesDto : logicStorageTypesEntityList){
                        Map<String, Object> map = new HashMap<String, Object>();
                        String storageType = logicStorageTypesDto.getStorageType();
                        map.put("key",storageType);
                        map.put("name",dictMap.get(storageType));
                        tableTypeList.add(map);
                    }
                }
                logicDefineMap.put("tableType", tableTypeList);

                //逻辑库对应的物理库
                List<Map<String, Object>> databaseList = new ArrayList<Map<String, Object>>();
                for(Map<String, Object> databaseLogic : distinctDatabaseAndLogic){
                    if(logicDefineDto.getLogicFlag().toString().equals(String.valueOf(databaseLogic.get("logic_flag")))){
                        databaseList.add(databaseLogic);
                    }
                }
                if (databaseList.size() != 0) {
                    logicDefineMap.put("physics", databaseList);
                    result.add(logicDefineMap);
                }
            }
        }

        return result;
    }
}
