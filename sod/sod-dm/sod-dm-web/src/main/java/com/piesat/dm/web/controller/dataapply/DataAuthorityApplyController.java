package com.piesat.dm.web.controller.dataapply;

import com.piesat.common.utils.StringUtils;
import com.piesat.dm.rpc.api.dataapply.DataAuthorityApplyService;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityApplyDto;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityRecordDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/10 13:08
 */
@RestController
@RequestMapping("/dm/dataAuthorityApply")
@Api(value="资料访问权限审核",tags= {"资料访问权限审核"})
public class DataAuthorityApplyController {

    @Autowired
    private DataAuthorityApplyService dataAuthorityApplyService;

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean> list(DataAuthorityApplyDto dataAuthorityApplyDto, int pageNum, int pageSize) {
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<DataAuthorityApplyDto> pageForm = new PageForm<>(pageNum, pageSize, dataAuthorityApplyDto);
        PageBean pageBean = dataAuthorityApplyService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @GetMapping(value = "/getApplyInfoById")
    @ApiOperation(value = "根据id查询申请信息", notes = "根据id查询申请信息")
    public ResultT<Map<String,Object>> getApplyInfoById(String id)
    {
        ResultT<Map<String,Object>> resultT=new ResultT<>();
        Map<String,Object> dataAuthorityApply= this.dataAuthorityApplyService.getObjectById(id);
        resultT.setData(dataAuthorityApply);
        return resultT;
    }

    @GetMapping(value = "/getRecordByApplyId")
    @ApiOperation(value = "根据申请id查询申请资料信息", notes = "根据申请id查询申请资料信息")
    public ResultT<List<Map<String,Object>>> getRecordByApplyId(DataAuthorityRecordDto dataAuthorityRecordDto)
    {
        ResultT<List<Map<String,Object>>> resultT=new ResultT<>();
        Map<String,String> map = new HashMap<String,String>();
        if(StringUtils.isNotNullString(dataAuthorityRecordDto.getApplyId())){
            map.put("applyId",dataAuthorityRecordDto.getApplyId());
        }
        if(StringUtils.isNotNullString(dataAuthorityRecordDto.getTypeName())){
            map.put("typeName",dataAuthorityRecordDto.getTypeName());
        }
        if(StringUtils.isNotNullString(dataAuthorityRecordDto.getClassName())){
            map.put("className",dataAuthorityRecordDto.getClassName());
        }
        if(StringUtils.isNotNullString(dataAuthorityRecordDto.getTableName())){
            map.put("tableName",dataAuthorityRecordDto.getTableName());
        }
        if(StringUtils.isNotNullString(dataAuthorityRecordDto.getDatabaseName())){
            map.put("databaseName",dataAuthorityRecordDto.getDatabaseName());
        }
        if(StringUtils.isNotNullString(dataAuthorityRecordDto.getSpecialDatabaseName())){
            map.put("specialDatabaseName",dataAuthorityRecordDto.getSpecialDatabaseName());
        }
        if(dataAuthorityRecordDto.getApplyAuthority() != null){
            map.put("applyAuthority",String.valueOf(dataAuthorityRecordDto.getApplyAuthority()));
        }
        if(dataAuthorityRecordDto.getAuthorize() != null){
            map.put("authorize",String.valueOf(dataAuthorityRecordDto.getAuthorize()));
        }
        List<Map<String,Object>> lists = this.dataAuthorityApplyService.getRecordByApplyId(map);
        resultT.setData(lists);
        return resultT;
    }

    @PutMapping(value = "/updateRecordCheck")
    @ApiOperation(value = "授权资料", notes = "授权资料")
    public ResultT updateRecordCheck(@RequestBody DataAuthorityApplyDto dataAuthorityApplyDto){
       Map<String,Object> result = dataAuthorityApplyService.updateRecordCheck(dataAuthorityApplyDto);
       return ResultT.success(result.get("msg"));
    }

    @PutMapping(value = "/updateRecordCheckCancel")
    @ApiOperation(value = "撤销已授权资料", notes = "撤销已授权资料")
    public ResultT updateRecordCheckCancel(@RequestBody DataAuthorityApplyDto dataAuthorityApplyDto){
        Map<String,Object> result = dataAuthorityApplyService.updateRecordCheckCancel(dataAuthorityApplyDto);
        return ResultT.success(result.get("msg"));
    }

    @GetMapping(value = "/getRecordListByUserId")
    @ApiOperation(value = "根据用户id查询申请资料", notes = "根据用户id查询申请资料")
    public ResultT<List<Map<String,Object>>> getRecordListByUserId(String userId)
    {
        ResultT<List<Map<String,Object>>> resultT=new ResultT<>();
        List<Map<String,Object>> recordListByUserId= this.dataAuthorityApplyService.getRecordListByUserId(userId);
        resultT.setData(recordListByUserId);
        return resultT;
    }


    @PutMapping(value = "/updateRecordByApplyIdAndClassId")
    @ApiOperation(value = "根据申请id和存储编码修改数据授权资料信息", notes = "根据申请id和存储编码修改数据授权资料信息")
    public ResultT<String> updateRecordByApplyIdAndClassId(String apply_id,String data_class_id,Integer authorize, String cause){
        try {
            dataAuthorityApplyService.updateRecordByApplyIdAndClassId(apply_id,data_class_id,authorize,cause);
            return ResultT.success();
        }catch (Exception e){
            return ResultT.failed(e.getMessage());
        }
    }
    @PutMapping(value = "/updateRecordByApplyIdAndClassIdAndDatabaseId")
    @ApiOperation(value = "根据申请id、存储编码和数据库id修改数据授权资料信息", notes = "根据申请id、存储编码和数据库id修改数据授权资料信息")
    public ResultT<String> updateRecordByApplyIdAndClassIdAndDatabaseId(String apply_id,String data_class_id,String database_id,Integer authorize, String cause){
        try {
            dataAuthorityApplyService.updateRecordByApplyIdAndClassIdAndDatabaseId(apply_id,data_class_id,database_id,authorize,cause);
            return ResultT.success();
        }catch (Exception e){
            return ResultT.failed(e.getMessage());
        }
    }

}
