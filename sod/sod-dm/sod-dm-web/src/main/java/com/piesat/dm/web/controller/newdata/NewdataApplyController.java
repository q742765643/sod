package com.piesat.dm.web.controller.newdata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.piesat.dm.rpc.api.DataClassService;
import com.piesat.dm.rpc.api.DatabaseService;
import com.piesat.dm.rpc.api.DatumTypeInfoService;
import com.piesat.dm.rpc.api.newdata.NewdataApplyService;
import com.piesat.dm.rpc.dto.DataClassDto;
import com.piesat.dm.rpc.dto.DataLogicDto;
import com.piesat.dm.rpc.dto.DataTableDto;
import com.piesat.dm.rpc.dto.TableColumnDto;
import com.piesat.dm.rpc.dto.newdata.NewdataApplyDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/5 17:09
 */
@RestController
@RequestMapping("/dm/newdataApply")
@Api(value = "新增注册资料申请controller", tags = {"新增注册资料"})
public class NewdataApplyController {
    @Autowired
    private NewdataApplyService newdataApplyService;
    @Autowired
    private DataClassService dataClassService;
    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private DatumTypeInfoService datumTypeInfoService;


    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean> list(NewdataApplyDto newdataApplyDto,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ResultT<PageBean> resultT=new ResultT<>();
        PageBean pageBean = this.newdataApplyService.selectPageList(newdataApplyDto, pageNum, pageSize);
        resultT.setData(pageBean);
        return resultT;
    }

   @GetMapping("/getDataTypeList")
   @ApiOperation(value = "查询数据分类", notes = "查询数据分类")
   public ResultT<List<Map<String,Object>>> getDataTypeList(){
       List<Map<String,Object>> dataTypeList = this.dataClassService.getDataTypeList();
       return ResultT.success(dataTypeList);
   }

    @PostMapping(value = "/queryCheckByApplyId")
    @ApiOperation(value = "根据申请id、四级编码和表名查待审核记录", notes = "根据申请id、四级编码和表名查待审核记录")
    public ResultT<Map<String,Object>> queryCheckByApplyId(@RequestBody NewdataApplyDto newdataApplyDto){
        Map<String, Object> list = newdataApplyService.queryCheckByApplyId(newdataApplyDto);
        return ResultT.success(list);
    }

    /**
     * 获取全部物理库和专题库
     *
     * @return
     */
    @GetMapping("/queryDatabaseDetail")
    @ApiOperation(value = "获取全部物理库和专题库", notes = "获取全部物理库和专题库")
    public ResultT<List<Map<String, Object>>> queryDatabaseDetail() {
        ResultT<List<Map<String, Object>>> resultT = new ResultT<>();
        List<Map<String, Object>> databaseNames = databaseService.getDatabaseName();
        resultT.setData(databaseNames);
        return resultT;
    }

    @PostMapping(value = "/updateStatus")
    @ApiOperation(value = "存储资料审核接口", notes = "存储资料审核接口")
    public ResultT<String> updateStatus(@RequestBody NewdataApplyDto newdataApplyDto){
        int result = newdataApplyService.updateStatus(newdataApplyDto);
        if(result > 0){
            return ResultT.success();
        }
        return ResultT.failed();
    }

    @GetMapping("/getDataClassLogic/{classLogicIds}")
    @ApiOperation(value = "根据存储编码获取资料信息", notes = "根据存储编码获取资料信息")
    public  ResultT<List<Map<String,Object>>> getDataClassLogic(@PathVariable String[] classLogicIds){
        List<String> stringList = Arrays.asList(classLogicIds);
        List<Map<String, Object>> listBYIn = dataClassService.getListBYIn(stringList, null, null);
        return ResultT.success(listBYIn);
    }

    /**
     *
     * @return
     */
    @ApiOperation(value = "查询公共元数据资料分类树")
    @GetMapping(value = "/getTree")
    public ResultT getTree() {
        try {
            JSONArray tree = this.datumTypeInfoService.getTree();
            return ResultT.success(tree);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @GetMapping("/getDataGroup")
    @ApiOperation(value = "查询父节点目录", notes = "查询父节点目录")
    public ResultT<List<Map<String,Object>>> getDataGroup(){
        List<Map<String,Object>> dataGroupList = this.dataClassService.getDataGroup();
        return ResultT.success(dataGroupList);
    }

    /**
     * 根据逻辑库查物理库？
     * @return
     */
    @GetMapping("/getLogicInfo")
    @ApiOperation(value = "查询逻辑库物理库信息", notes = "查询逻辑库物理库信息")
    public ResultT<List<Map<String,Object>>> getLogicInfo(){
        List<Map<String,Object>> logicInfoList = this.newdataApplyService.getLogicInfo();
        return ResultT.success(logicInfoList);
    }


    @GetMapping("/getDataClassIdNum")
    @ApiOperation(value = "查询存储编码最大后位编码", notes = "查询存储编码最大后位编码")
    public ResultT<String> getDataClassIdNum(){
        String num = dataClassService.getDataClassIdNum();
        return ResultT.success(num);
    }


    @GetMapping("/getDbDataByPId/{id}")
    @ApiOperation(value = "根据物理库ID查专题库", notes = "根据物理库ID查专题库")
    public  ResultT<List<Map<String,Object>>> getDbDataByPId(@PathVariable String id){
        List<Map<String, Object>> databaseList = databaseService.getByDatabaseDefineId(id);
        return ResultT.success(databaseList);
    }

    /**
     * ???
     * @param dDataId
     * @return
     */
    @GetMapping("/getDataClassLogicBydDataId/{dDataId}")
    @ApiOperation(value = "根据四级编码获取资料信息", notes = "根据四级编码获取资料信息")
    public  ResultT<List<Map<String,Object>>> getDataClassLogicBydDataId(@PathVariable String dDataId){
        List<Map<String, Object>> listBYIn = dataClassService.getListBYIn(null, null, dDataId);
        return ResultT.success(listBYIn);
    }

    @ApiOperation(value="保存申请资料配置接口",notes="保存申请资料配置接口")
    @PostMapping(value="/addGroup")
    public ResultT<String> addGroup(@RequestBody DataClassDto dataClassDto, HttpServletRequest request) {
       /* DataClassEntity
                access_control--isAccess
                class_name
                d_data_id
                data_class_id
                if_stop_use
                meta_data_name
                meta_data_stor_type
                parent_class_id
                serial_no
                use_base_info
        DataLogicEntity
                logic_id
                database_id
                storage_type*/
        NewdataApplyDto newdataApplyDto = new NewdataApplyDto();
        newdataApplyDto.setId(request.getParameter("applyId"));
        newdataApplyDto.setDDataId(request.getParameter("dDataId"));
        newdataApplyDto.setDataClassId(request.getParameter("dataClassId"));

        String[] dataclasslogics = request.getParameterValues("dataclasslogic");
        for(String dataclasslogic : dataclasslogics){
            JSONObject parse = JSONObject.parseObject(dataclasslogic);
            DataLogicDto dataLogicDto = new DataLogicDto();
            dataLogicDto.setLogicFlag((String)parse.get("logicId"));
            dataLogicDto.setDatabaseId((String)parse.get("databaseId"));
            dataLogicDto.setStorageType((String)parse.get("storageType"));
            dataLogicDto.setDataClassId((String)parse.get("dataClassId"));
            newdataApplyDto.getDataLogicDtoList().add(dataLogicDto);
        }
        return newdataApplyService.addGroup(dataClassDto, newdataApplyDto);
    }

    @ApiOperation(value="更新申请资料配置接口",notes="更新申请资料配置接口")
    @PostMapping(value="/updateGroup")
    public ResultT<String> updateGroup(@RequestBody DataClassDto dataClassDto, HttpServletRequest request) {
       /* DataClassEntity
                access_control--isAccess
                class_name
                d_data_id
                data_class_id
                if_stop_use
                meta_data_name
                meta_data_stor_type
                parent_class_id
                serial_no
                use_base_info
        DataLogicEntity
                logic_id
                database_id
                storage_type
         old_data_class_id
        */

        NewdataApplyDto newdataApplyDto = new NewdataApplyDto();
        newdataApplyDto.setId(request.getParameter("applyId"));
        newdataApplyDto.setDDataId(request.getParameter("dDataId"));
        newdataApplyDto.setDataClassId(request.getParameter("dataClassId"));

        String[] dataclasslogics = request.getParameterValues("dataclasslogic");
        for(String dataclasslogic : dataclasslogics){
            JSONObject parse = JSONObject.parseObject(dataclasslogic);
            DataLogicDto dataLogicDto = new DataLogicDto();
            dataLogicDto.setLogicFlag((String)parse.get("logicId"));
            dataLogicDto.setDatabaseId((String)parse.get("databaseId"));
            dataLogicDto.setStorageType((String)parse.get("storageType"));
            dataLogicDto.setDataClassId((String)parse.get("dataClassId"));
            newdataApplyDto.getDataLogicDtoList().add(dataLogicDto);
        }

        String  old_data_class_id = request.getParameter("old_data_class_id");
        return newdataApplyService.updateGroup(dataClassDto, newdataApplyDto, old_data_class_id);
    }


    @ApiOperation(value="更新或添加数据表接口",notes="更新或添加数据表接口")
    @PostMapping(value="/addOrUpdateDataTable")
    public ResultT<String> addOrUpdateDataTable(@RequestBody DataTableDto dataTableDto, HttpServletRequest request){
       return newdataApplyService.addOrUpdateDataTable(dataTableDto);
    }


    //
    @GetMapping("/getNewdataTableField/{id}")
    @ApiOperation(value = "根据任务id获取表字段信息", notes = "根据任务id获取表字段信息")
    public  ResultT<String> getNewdataTableField(@PathVariable String id){
        List<TableColumnDto> newdataTableField = newdataApplyService.getNewdataTableField(id);
        return ResultT.success();
    }

    //addDataStructure
    @ApiOperation(value="添加表结构接口",notes="添加表结构接口")
    @PostMapping("addDataStructure")
    public ResultT<String> addDataStructure(@RequestBody TableColumnDto tableColumnDto) {
        return this.newdataApplyService.addDataStructure(tableColumnDto);
    }

    @ApiOperation(value="修改表结构接口",notes="添加表结构接口")
    @PostMapping("updateDataStructure")
    public ResultT<String> updateDataStructure(@RequestBody TableColumnDto tableColumnDto) {
        return this.newdataApplyService.updateDataStructure(tableColumnDto);
    }

    /*@ApiOperation(value="根据物理库id获取表信息",notes="根据物理库id获取表信息")
    @PostMapping("getTableByPhysicsId/{databaseId}")
    public ResultT<String> getTableByPhysicsId(@PathVariable String databaseId) {
        return this.newdataApplyService.getTableByPhysicsId(databaseId);
    }*/

}
