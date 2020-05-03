package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.service.DatabaseVisibleService;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库可视化
 *
 * @author cwh
 * @date 2020年 04月27日 13:41:05
 */
@RestController
@RequestMapping("/dm/databasevisible")
@Api(value = "数据库可视化", tags = {"数据库可视化"})
public class DatabaseVisibleController {

    @Autowired
    private DatabaseVisibleService databaseVisibleService;

    @Value("${fidbDownloadUrl}")
    private String fidbDownloadUrl;

    @GetMapping("/getDatabaseByBizUserId")
    @ApiOperation(value = "根据业务用户id查询数据库", notes = "根据业务用户id查询数据库")
    public Map<String,Object> getDatabaseByBizUserId(String bizUserId) {
        ResultT result = this.databaseVisibleService.getDatabaseByBizUserId(bizUserId);
        Map<String,Object> map = new HashMap<String, Object>();
        if (result.getCode()==200){
            map.put("returnCode",0);
            map.put("data",result.getData());
        }else {
            map.put("returnCode",1);
            map.put("data",null);
        }
        return map;
    }
    @GetMapping("/getDataclassTree")
    @ApiOperation(value = "查询资料树", notes = "查询资料树")
    public Map<String,Object> getDataclassTree(String bizUserId,String dataBaseId) {
        ResultT result =  this.databaseVisibleService.getDataclassTree(bizUserId,dataBaseId);
        Map<String,Object> map = new HashMap<String, Object>();
        if (result.getCode()==200){
            map.put("returnCode",0);
            map.put("data",result.getData());
        }else {
            map.put("returnCode",1);
            map.put("data",null);
        }
        return map;
    }
    @GetMapping("/getDatabaseInfo")
    @ApiOperation(value = "查询数据库信息", notes = "查询数据库信息")
    public Map<String,Object> getDatabaseInfo(String databaseId, String bizUserId) {
        ResultT result = this.databaseVisibleService.getDatabaseInfo(databaseId,bizUserId);
        Map<String,Object> map = new HashMap<String, Object>();
        if (result.getCode()==200){
            map.put("returnCode",0);
            map.put("data",result.getData());
        }else {
            map.put("returnCode",1);
            map.put("data",null);
        }
        return map;
    }
    @GetMapping("/getTable")
    @ApiOperation(value = "查询表信息", notes = "查询表信息")
    public Map<String,Object> getTable(String bizUserId, String dataclassId) {
        ResultT result =  this.databaseVisibleService.getTable(bizUserId,dataclassId);
        Map<String,Object> map = new HashMap<String, Object>();
        if (result.getCode()==200){
            map.put("returnCode",0);
            map.put("data",result.getData());
        }else {
            map.put("returnCode",1);
            map.put("data",null);
        }
        return map;
    }


    @GetMapping("/getTableInfo")
    @ApiOperation(value = "查询表信息", notes = "查询表信息")
    public Map<String,Object> getTableInfo(String tableName) {
        ResultT result =   this.databaseVisibleService.getTableInfo(tableName);
        Map<String,Object> map = new HashMap<String, Object>();
        if (result.getCode()==200){
            map.put("returnCode",0);
            map.put("data",result.getData());
        }else {
            map.put("returnCode",1);
            map.put("data",null);
        }
        return map;
    }

    @GetMapping("/getSpecial")
    @ApiOperation(value = "查询专题库", notes = "查询专题库")
    public Map<String,Object> getSpecial(String bizUserId) {
        ResultT result = this.databaseVisibleService.getSpecial(bizUserId);
        Map<String,Object> map = new HashMap<String, Object>();
        if (result.getCode()==200){
            map.put("returnCode",0);
            map.put("data",result.getData());
        }else {
            map.put("returnCode",1);
            map.put("data",null);
        }
        return map;
    }

    @GetMapping("/getSpecialInfo")
    @ApiOperation(value = "查询专题库详情", notes = "查询专题库详情")
    public Object getSpecialInfo(String specialId) {
        ResultT result = this.databaseVisibleService.getSpecialInfo(specialId);
        return  result.getData();
    }

    @GetMapping("/getAccountInfo")
    @ApiOperation(value = "查询数据库用户", notes = "查询数据库用户")
    public Object getAccountInfo(String bizUserId) {
        ResultT result = this.databaseVisibleService.getAccountInfo(bizUserId);
       return  result.getData();
    }

    @GetMapping("/getDownloadUrl")
    @ApiOperation(value = "查询下载url", notes = "查询下载url")
    public String getDownloadUrl() {
       return fidbDownloadUrl;
    }

}
