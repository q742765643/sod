package com.piesat.dm.web.controller.special;

import com.alibaba.fastjson.JSONObject;
import com.piesat.dm.rpc.api.special.DatabaseSpecialAuthorityService;
import com.piesat.dm.rpc.api.special.DatabaseSpecialReadWriteService;
import com.piesat.dm.rpc.api.special.DatabaseSpecialService;
import com.piesat.dm.rpc.dto.database.DatabaseDefineDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialAuthorityDto;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialDto;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialReadWriteDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 专题库管理
 * @Description
 * @ClassName DatabaseSpecialController
 * @author wulei
 * @date 2020/2/12 15:56
 */
@Api(tags = "专题库管理")
@RequestMapping("/dm/databaseSpecial")
@RestController
public class DatabaseSpecialController {

    @Autowired
    private DatabaseSpecialService databaseSpecialService;
    @Autowired
    private DatabaseSpecialReadWriteService databaseSpecialReadWriteService;
    @Autowired
    private DatabaseSpecialAuthorityService databaseSpecialAuthorityService;
    @Autowired
    private DatabaseSpecialTreeServiceImpl databaseSpecialTreeService;


    @ApiOperation(value = "分页查询")
    @RequiresPermissions("dm:databaseSpecial:page")
    @GetMapping(value = "/page")
    public ResultT<PageBean<DatabaseSpecialDto>> getPage(DatabaseSpecialDto databaseSpecialDto,
                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ResultT<PageBean<DatabaseSpecialDto>> resultT=new ResultT<>();
        PageForm<DatabaseSpecialDto> pageForm=new PageForm<>(pageNum,pageSize,databaseSpecialDto);
        PageBean pageBean=databaseSpecialService.getPage(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @PostMapping(value = "/save")
    @ApiOperation(value = "添加", notes = "添加")
    public ResultT save(@RequestBody DatabaseSpecialDto databaseSpecialDto) {
        try {
            DatabaseSpecialDto save = this.databaseSpecialService.saveDto(databaseSpecialDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "获取专题库列表")
    @RequiresPermissions("dm:databaseSpecial:specialList")
    @GetMapping(value = "/specialList")
    public ResultT<PageBean> list(DatabaseSpecialDto databaseSpecialDto, int pageNum, int pageSize) {
        try{
            ResultT<PageBean> resultT = new ResultT<>();
            PageForm<DatabaseSpecialDto> pageForm = new PageForm<>(pageNum, pageSize, databaseSpecialDto);
            PageBean pageBean = databaseSpecialService.selectPageList(pageForm);
            resultT.setData(pageBean);
            return resultT;
        }catch(Exception e){
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:databaseSpecial:getById")
    @GetMapping(value = "/getById")
    public ResultT get(String id) {
        try {
            DatabaseSpecialDto databaseSpecialDto = this.databaseSpecialService.getDotById(id);
            return ResultT.success(databaseSpecialDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "获取专题库资料列表")
    @RequiresPermissions("dm:databaseSpecial:getSpecialDataList")
    @GetMapping(value = "/getSpecialDataList")
    public ResultT getSpecialDataList(DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto) {
        try {
            List<DatabaseSpecialReadWriteDto> dataList = this.databaseSpecialReadWriteService.getDotList(databaseSpecialReadWriteDto);
            return ResultT.success(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:databaseSpecial:del")
    @DeleteMapping(value = "/delete")
    public ResultT delete(String id) {
        try {
            this.databaseSpecialService.deleteById(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据专题库id删除专题库权限表中记录")
    @RequiresPermissions("dm:databaseSpecial:deleteAuthorityBySdbId")
    @DeleteMapping(value = "/deleteAuthorityBySdbId")
    public ResultT deleteAuthorityBySdbId(String sdbId) {
        try {
            this.databaseSpecialService.deleteAuthorityBySdbId(sdbId);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "修改专题库基本信息")
    @RequiresPermissions("dm:databaseSpecial:update")
    @PostMapping(value = "/update")
    public ResultT update(@RequestBody DatabaseSpecialDto databaseSpecialDto) {
        try {
            DatabaseSpecialDto save = this.databaseSpecialService.saveDto(databaseSpecialDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据专题库id修改专题库使用状态")
    @RequiresPermissions("dm:databaseSpecial:updateUseStatusById")
    @PostMapping(value = "/updateUseStatusById")
    public ResultT updateUseStatusById(String sdbId,String useStatus) {
        try {
            DatabaseSpecialDto save = this.databaseSpecialService.updateUseStatusById(sdbId, useStatus);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据获取专题库对应数据库权限")
    @RequiresPermissions("dm:databaseSpecial:getAuthorityBySdbId")
    @GetMapping(value = "/getAuthorityBySdbId")
    public ResultT getAuthorityBySdbId(String sdbId) {
        try {
            List<DatabaseSpecialAuthorityDto> authorityList = this.databaseSpecialAuthorityService.getAuthorityBySdbId(sdbId);
            return ResultT.success(authorityList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "数据库授权")
    @RequiresPermissions("dm:databaseSpecial:empowerDatabaseSpecial")
    @PostMapping(value = "/empowerDatabaseSpecial")
    public ResultT empowerDatabaseSpecial(@RequestBody SpecialParamVO specialParamVO) {
        try {
            DatabaseDto databaseDto = new DatabaseDto();
            databaseDto.setUserId(specialParamVO.getUserId());
            DatabaseDefineDto databaseDefineDto = new DatabaseDefineDto();
            databaseDefineDto.setDatabaseInstance(specialParamVO.getSimpleName());
            databaseDto.setDatabaseDefine(databaseDefineDto);
            databaseDto.setTdbId(specialParamVO.getSdbId());
            databaseDto.setDatabaseSpecialAuthorityList(specialParamVO.getDatabaseSpecialAuthorityList());
            databaseDto.setSchemaName(specialParamVO.getSimpleName());
            this.databaseSpecialService.empowerDatabaseSpecial(databaseDto);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

	@Data
    private static class SpecialParamVO{
        //专题库ID
        String sdbId;
        //专题库名称
        String sdbName;
        //专题库简称
        String simpleName;
        //数据库ID
        String databaseId;
        //账户ID
        String userId;
        //权限列表
        List<DatabaseSpecialAuthorityDto> DatabaseSpecialAuthorityList;
    }

    @ApiOperation(value = "资料授权-单独")
    @RequiresPermissions("dm:databaseSpecial:empowerDataOne")
    @PostMapping(value = "/empowerDataOne")
    public ResultT empowerDataOne(@RequestBody DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto) {
        try {
            this.databaseSpecialService.empowerDataOne(databaseSpecialReadWriteDto);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "资料授权-批量")
    @RequiresPermissions("dm:databaseSpecial:empowerDataOne")
    @PostMapping(value = "/empowerDataBatch")
    public ResultT empowerDataBatch(@RequestBody String listString) {
        try {
            List<DatabaseSpecialReadWriteDto> databaseSpecialReadWriteDtoList = JSONObject.parseArray(listString,
                    DatabaseSpecialReadWriteDto.class);
            this.databaseSpecialService.empowerDataBatch(databaseSpecialReadWriteDtoList);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据专题库ID获取对应树和资料信息")
    @RequiresPermissions("dm:databaseSpecial:getDataTreeBySdbId")
    @GetMapping(value = "/getDataTreeBySdbId")
    public ResultT getDataTreeBySdbId(String sdbId) {
        try {
            Map<String, Object> treeBySdbId = this.databaseSpecialService.getDataTreeBySdbId(sdbId);
            return ResultT.success(treeBySdbId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据专题库ID获取对应树信息")
    @RequiresPermissions("dm:databaseSpecial:getTreeBySdbId")
    @GetMapping(value = "/getTreeBySdbId")
    public ResultT getTreeBySdbId(String sdbId) {
        try {
            Map<String, Object> treeBySdbId = this.databaseSpecialService.getTreeBySdbId(sdbId);
            return ResultT.success(treeBySdbId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据审核状态查询")
    @RequiresPermissions("dm:databaseSpecial:getByExamineStatus")
    @GetMapping(value = "/getByExamineStatus")
    public ResultT getByExamineStatus(String examineStatus) {
        try {
            List<DatabaseSpecialDto> databaseSpecialDtos = this.databaseSpecialService.getByExamineStatus(examineStatus);
            return ResultT.success(databaseSpecialDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据用户id查询其他用户创建并且没有被该用户引用的所有专题库信息")
    @RequiresPermissions("dm:databaseSpecial:getAllOtherRecordByUserId")
    @GetMapping(value = "/getAllOtherRecordByUserId")
    public ResultT getAllOtherRecordByUserId(String userId,String useStatus) {
        try {
            List<Map<String,Object>> databaseSpecialDtos = this.databaseSpecialService.getAllOtherRecordByUserId(userId,useStatus);
            return ResultT.success(databaseSpecialDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据专题库id查询专题库申请资料（去除该用户申请的资料授权）")
    @RequiresPermissions("dm:databaseSpecial:getRecordSpecialByTdbId")
    @GetMapping(value = "/getRecordSpecialByTdbId")
    public ResultT getRecordSpecialByTdbId(String sdbId,String userId) {
        try {
            List<Map<String,Object>> recordSpecial = this.databaseSpecialReadWriteService.getRecordSpecialByTdbId(sdbId,userId);
            return ResultT.success(recordSpecial);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "根据用户id和使用状态查询")
    @RequiresPermissions("dm:databaseSpecial:getByUserIdAndUseStatus")
    @GetMapping(value = "/getByUserIdAndUseStatus")
    public ResultT getByUserIdAndUseStatus(String userId,String useStatus) {
        try {
            List<DatabaseSpecialDto> databaseSpecialDtos = this.databaseSpecialService.getByUserIdAndUseStatus(userId,useStatus);
            return ResultT.success(databaseSpecialDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
    @ApiOperation(value = "插入同一专题库下的多条记录")
    @RequiresPermissions("dm:databaseSpecial:saveMultilRecord")
    @GetMapping(value = "/saveMultilRecord")
    @Log(title = "插入同一专题库下的多条记录", businessType = BusinessType.INSERT)
    public  ResultT saveMultilRecord(HttpServletRequest request){
        try {
            Map<String, Object> map = this.databaseSpecialService.saveMultilRecord(request);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
    @ApiOperation(value = "根据用户id和专题库id查询")
    @RequiresPermissions("dm:databaseSpecial:getdefeataudit")
    @GetMapping(value = "/getdefeataudit")
    public  ResultT getdefeataudit(String tdbId, String userId){
        try {
            DatabaseSpecialDto databaseSpecialDto =  this.databaseSpecialService.getdefeataudit(tdbId,userId);
            return ResultT.success(databaseSpecialDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
    @ApiOperation(value = "根据用户id和专题库状态修改")
    @RequiresPermissions("dm:databaseSpecial:updateBySql")
    @GetMapping(value = "/updateBySql")
    public  ResultT updateBySql(HttpServletRequest request){
        try {
            String tdbId = request.getParameter("tdbId");
            String userId = request.getParameter("userId");
            String cause = request.getParameter("cause");
            String examineStatus = request.getParameter("examineStatus");
            Map<String,Object> map =  this.databaseSpecialService.updateBySql(tdbId,userId,cause,examineStatus);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
    @ApiOperation(value = "下根据专题库ID号获取对应专题库信息和该专题库中对应数据信息")
    @RequiresPermissions("dm:databaseSpecial:getRecordByTdbId")
    @GetMapping(value = "/getRecordByTdbId")
    public  ResultT getRecordByTdbId(String tdbId,String typeId, String cause){
        try {
            Map<String,Object> map =  this.databaseSpecialService.getRecordByTdbId(tdbId,typeId,cause);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
    @ApiOperation(value = "根据专题库ID号获取对应专题库信息和对应数据信息")
    @RequiresPermissions("dm:databaseSpecial:getOneRecordByTdbId")
    @GetMapping(value = "/getOneRecordByTdbId")
    public  ResultT getOneRecordByTdbId(String tdbId,String typeId, String cause){
        try {
            Map<String,Object> map =  this.databaseSpecialService.getOneRecordByTdbId(tdbId,typeId,cause);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
    @ApiOperation(value = "修改资料申请状态")
    @RequiresPermissions("dm:databaseSpecial:changeDataStatus")
    @GetMapping(value = "/changeDataStatus")
    public  ResultT changeDataStatus(String tdbId, String physical,String data_class_id){
        try {
            Map<String,Object> map =  this.databaseSpecialReadWriteService.changeDataStatus(tdbId,physical,data_class_id);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
    @ApiOperation(value = "专题库资料清单删除")
    @RequiresPermissions("dm:databaseSpecial:deleteRecords")
    @GetMapping(value = "/deleteRecords")
    public  ResultT deleteRecords(HttpServletRequest request){
        try {
            Map<String,Object> map =  this.databaseSpecialReadWriteService.deleteRecords(request);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
    @ApiOperation(value = "保存专题库资料树")
    @RequiresPermissions("dm:databaseSpecial:saveTreeData")
    @GetMapping(value = "/saveTreeData")
    public  ResultT saveTreeData(String tdbId, HttpServletRequest request){
        try {
            Map<String,Object> map =  this.databaseSpecialTreeService.saveTreeData(tdbId,request);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
    @ApiOperation(value = "根据专题库ID号和分类ID修改对应分类名称、上级分类、排序")
    @RequiresPermissions("dm:databaseSpecial:updateOneRecordByTdbId")
    @GetMapping(value = "/updateOneRecordByTdbId")
    public  ResultT updateOneRecordByTdbId(HttpServletRequest request){
        try {
            Map<String,Object> map =  this.databaseSpecialTreeService.updateOneRecordByTdbId(request);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
    @ApiOperation(value = "根据专题库ID号和分类ID删除对应树信息")
    @RequiresPermissions("dm:databaseSpecial:deleteRecordByTdbId")
    @GetMapping(value = "/deleteRecordByTdbId")
    public  ResultT deleteRecordByTdbId(String tdbId, String typeId){
        try {
            Map<String,Object> map =  this.databaseSpecialTreeService.deleteRecordByTdbId(tdbId,typeId);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
    @ApiOperation(value = "根据专题库ID和存储编码来修改分类ID")
    @RequiresPermissions("dm:databaseSpecial:updateTypeIdByTdbId")
    @GetMapping(value = "/updateTypeIdByTdbId")
    public  ResultT updateTypeIdByTdbId(String tdbId, String dataClassId, String typeId){
        try {
            Map<String,Object> map =  this.databaseSpecialTreeService.updateTypeIdByTdbId(tdbId, dataClassId, typeId);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
    @ApiOperation(value = "获取平台中所有专题库")
    @RequiresPermissions("dm:databaseSpecial:getAllSpecial")
    @GetMapping(value = "/getAllSpecial")
    public  ResultT getAllSpecial(String userId){
        try {
            Map<String,Object> map =  this.databaseSpecialService.getAllSpecial(userId);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
    @ApiOperation(value = "提交专题库引用申请并将一条记录插入数据表")
    @RequiresPermissions("dm:databaseSpecial:saveOneRecord")
    @GetMapping(value = "/saveOneRecord")
    public  ResultT saveOneRecord(HttpServletRequest request){
        try {
            Map<String,Object> map =  this.databaseSpecialService.saveOneRecord(request);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
    @ApiOperation(value = "获取平台中废弃的专题库")
    @RequiresPermissions("dm:databaseSpecial:getDiscardSpecial")
    @GetMapping(value = "/getDiscardSpecial")
    public  ResultT saveOneRecord(String userId){
        try {
            Map<String,Object> map =  this.databaseSpecialService.getDiscardSpecial(userId);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
    @ApiOperation(value = "专题库创建申请")
    @RequiresPermissions("dm:databaseSpecial:saveCreateapply")
    @GetMapping(value = "/saveCreateapply")
    public  ResultT saveCreateapply(HttpServletRequest request){
        try {
            Map<String,Object> map =  this.databaseSpecialService.saveCreateapply(request);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


}
