package com.piesat.dm.web.controller.dataapply;

import com.piesat.common.utils.StringUtils;
import com.piesat.dm.rpc.api.dataapply.DataAuthorityApplyService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.dto.ReadAuthorityDto;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityApplyDto;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityRecordDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
    @RequiresPermissions("dm:dataAuthorityApply:list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean> list(DataAuthorityApplyDto dataAuthorityApplyDto, int pageNum, int pageSize) {
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<DataAuthorityApplyDto> pageForm = new PageForm<>(pageNum, pageSize, dataAuthorityApplyDto);
        PageBean pageBean = dataAuthorityApplyService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @PostMapping(value = "/save")
    @ApiOperation(value = "添加", notes = "添加")
    public ResultT save(@RequestBody DataAuthorityApplyDto dataAuthorityApplyDto) {
        try {
            DataAuthorityApplyDto save = this.dataAuthorityApplyService.saveDto(dataAuthorityApplyDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @GetMapping(value = "/getApplyInfoById")
    //@RequiresPermissions("dm:dataAuthorityApply:getApplyInfoById")
    @ApiOperation(value = "根据id查询申请信息", notes = "根据id查询申请信息")
    public ResultT<Map<String,Object>> getApplyInfoById(String id)
    {
        ResultT<Map<String,Object>> resultT=new ResultT<>();
        Map<String,Object> dataAuthorityApply= this.dataAuthorityApplyService.getObjectById(id);
        resultT.setData(dataAuthorityApply);
        return resultT;
    }

    @GetMapping(value = "/getRecordByByUserId")
    //@RequiresPermissions("dm:dataAuthorityApply:getRecordByByUserId")
    @ApiOperation(value = "根据用户id查询申请资料信息", notes = "根据用户id查询申请资料信息")
    public ResultT getRecordByByUserId(String bizUserId)
    {
        ResultT<List<Map<String,Object>>> resultT=new ResultT<>();
        List<DataAuthorityApplyDto> dataAuthorityApplyList = this.dataAuthorityApplyService.findByUserId(bizUserId);
        if (dataAuthorityApplyList!=null&&dataAuthorityApplyList.size()>0){
            String id = dataAuthorityApplyList.get(dataAuthorityApplyList.size()-1).getId();
            Map<String,String> map = new HashMap<String,String>();
            map.put("applyId",id);
            List<Map<String,Object>> lists = this.dataAuthorityApplyService.getRecordByApplyId(map);
            resultT.setData(lists);
            return resultT;
        }else {
            return resultT;
        }
    }


    @GetMapping(value = "/getRecordByApplyId")
    //@RequiresPermissions("dm:dataAuthorityApply:getRecordByApplyId")
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
    //@RequiresPermissions("dm:dataAuthorityApply:updateRecordCheck")
    @ApiOperation(value = "授权资料", notes = "授权资料")
    public ResultT updateRecordCheck(@RequestBody DataAuthorityApplyDto dataAuthorityApplyDto){
       return  dataAuthorityApplyService.updateRecordCheck(dataAuthorityApplyDto);
    }

    @PutMapping(value = "/updateRecordCheckCancel")
    @ApiOperation(value = "拒绝授权/撤销已授权资料", notes = "拒绝授权/撤销已授权资料")
    public ResultT updateRecordCheckCancel(@RequestBody DataAuthorityApplyDto dataAuthorityApplyDto){
        return dataAuthorityApplyService.updateRecordCheckCancel(dataAuthorityApplyDto);
    }

    @PostMapping(value = "/updateRecordCheckPortal")
    //@RequiresPermissions("dm:dataAuthorityApply:updateRecordCheck")
    @ApiOperation(value = "授权资料Portal", notes = "授权资料Portal")
    public ResultT updateRecordCheckPortal(@RequestBody DataAuthorityApplyDto dataAuthorityApplyDto){
        return  dataAuthorityApplyService.updateRecordCheck(dataAuthorityApplyDto);
    }

    @PostMapping(value = "/updateRecordCheckCancelPortal")
    @ApiOperation(value = "拒绝授权/撤销已授权资料Portal", notes = "拒绝授权/撤销已授权资料Portal")
    public ResultT updateRecordCheckCancelPortal(@RequestBody DataAuthorityApplyDto dataAuthorityApplyDto){
        return dataAuthorityApplyService.updateRecordCheckCancel(dataAuthorityApplyDto);
    }

    @GetMapping(value = "/getRecordListByUserId")
    @ApiOperation(value = "根据用户id查询申请资料(别人申请该用户创建得资料)", notes = "根据用户id查询申请资料(别人申请该用户创建得资料)")
    public ResultT<List<Map<String,Object>>> getRecordListByUserId(String userId)
    {
        ResultT<List<Map<String,Object>>> resultT=new ResultT<>();
        List<Map<String,Object>> recordListByUserId= this.dataAuthorityApplyService.getRecordListByUserId(userId);
        resultT.setData(recordListByUserId);
        return resultT;
    }


    @GetMapping(value = "/updateRecordByApplyIdAndClassId")
    @ApiOperation(value = "根据申请id和存储编码修改数据授权资料信息(授权/拒绝)", notes = "根据申请id和存储编码修改数据授权资料信息(授权/拒绝)")
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

    @GetMapping(value = "/getAuthorDataByClassId")
    @ApiOperation(value = "根据资料编码获取授权信息", notes = "根据资料编码获取授权信息")
    public ResultT getAuthorDataByClassId(String dataClassId)
    {
        ResultT resultT=new ResultT<>();
        Map<String,Object> resultMap = this.dataAuthorityApplyService.getAuthorDataByClassId(dataClassId);
        resultT.setData(resultMap);
        return resultT;
    }

    @ApiOperation(value="获取用户资料申请列表")
//    @RequiresPermissions("api:dataAuthorityApply:getDataAuthorityList")
    @GetMapping(value="/api/dataAuthorityApply/getDataAuthorityList")
    public ResultT getDataAuthorityList(String userId, String applyAuthority, String logicId,String dataName,String category,String schemaId){
        try {
            Map<String, Object> map = dataAuthorityApplyService.getDataAuthorityList(userId, applyAuthority, logicId,dataName,category,schemaId);
            return ResultT.success(map);
        }catch (Exception e){
            return ResultT.failed(e.getMessage());
        }
    }
    @ApiOperation(value="根据四级编码获取用户信息")
    @RequiresPermissions("api:dataAuthorityApply:getDataCreator")
    @PostMapping(value="/api/dataAuthorityApply/getDataCreator")
    public ResultT getDataCreator(String dataClassId){
        try {
            Map<String, Object> map = dataAuthorityApplyService.getDataCreator(dataClassId);
            return ResultT.success();
        }catch (Exception e){
            return ResultT.failed(e.getMessage());
        }
    }
    @ApiOperation(value="根据id删除资料记录")
//    @RequiresPermissions("api:dataAuthorityApply:deleteDataAuthorityById")
    @GetMapping(value="/deleteDataAuthorityById")
    public ResultT deleteDataAuthorityById(String applyId,String dataBaseId,String dataClassId){
        try {
            Map<String, Object> map = dataAuthorityApplyService.deleteDataAuthorityById(applyId,dataBaseId,dataClassId);
            return ResultT.success();
        }catch (Exception e){
            return ResultT.failed(e.getMessage());
        }
    }
    @ApiOperation(value="查看所有资料分类")
    @RequiresPermissions("api:dataAuthorityApply:getDataCategory")
    @PostMapping(value="/api/dataAuthorityApply/getDataCategory")
    public ResultT getDataCategory(){
        try {
            Map<String, Object> map = dataAuthorityApplyService.getDataCategory();
            return ResultT.success(map);
        }catch (Exception e){
            return ResultT.failed(e.getMessage());
        }
    }
    /**
     *  获取可申请资料清单
     * @description
     * @author wlg
     * @date 2020年4月22日下午4:12:44
     * @return
     */
    @ApiOperation(value="获取可申请资料清单")
    //@RequiresPermissions("dm:dataAuthorityApply:getApplyDataInfo")
    @GetMapping(value = "/getApplyDataInfo")
    public ResultT getApplyDataInfo(String userId) {
    	try {
    		List<Map<String,Object>> data = dataAuthorityApplyService.getApplyDataInfo(userId);
    		return ResultT.success(data);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
    }
    @PostMapping(value = "/updateReadAuthority")
    //@RequiresPermissions("dm:dataAuthorityApply:updateReadAuthority")
    @ApiOperation(value = "读权限默认通过修改", notes = "读权限默认通过修改")
    public ResultT updateReadAuthority(@RequestBody ReadAuthorityDto readAuthorityDto) {
        try {
            readAuthorityDto = this.dataAuthorityApplyService.updateReadAuthority(readAuthorityDto);
            return ResultT.success(readAuthorityDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @GetMapping(value = "/getReadAuthority")
    //@RequiresPermissions("dm:dataAuthorityApply:getReadAuthority")
    @ApiOperation(value = "读权限默认通过，查询", notes = "读权限默认通过，查询")
    public ResultT getReadAuthority() {
        try {
            List<ReadAuthorityDto> readAuthorityDtoList = this.dataAuthorityApplyService.getReadAuthority();
            return ResultT.success(readAuthorityDtoList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

}
