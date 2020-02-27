package com.piesat.dm.web.controller.newdata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.rpc.api.*;
import com.piesat.dm.rpc.api.newdata.NewdataApplyService;
import com.piesat.dm.rpc.dto.*;
import com.piesat.dm.rpc.dto.newdata.NewdataApplyDto;
import com.piesat.dm.rpc.service.GrpcService;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    private GrpcService grpcService;
    @Autowired
    private DataClassService dataClassService;
    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private DatabaseDefineService databaseDefineService;
    @Autowired
    private DatumTypeInfoService datumTypeInfoService;
    @Autowired
    private TableColumnService tableColumnService;
    @Autowired
    private TableForeignKeyService tableForeignKeyService;
    @Autowired
    private DataLogicService dataLogicService;
    @Autowired
    private TableIndexService tableIndexService;
    @Autowired
    private ShardingService shardingService;

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean> list(NewdataApplyDto newdataApplyDto,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Map<String,Object> map = new HashMap<String,Object>();
        if(StringUtils.isNotNullString(String.valueOf(newdataApplyDto.getExamineStatus()))){
            map.put("status",newdataApplyDto.getExamineStatus());
        }
        if(StringUtils.isNotNullString(newdataApplyDto.getDDataId())){
            map.put("dataType",newdataApplyDto.getDDataId());
        }
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<Map<String,Object>> pageForm = new PageForm<>(pageNum, pageSize, map);
        PageBean pageBean = newdataApplyService.selectPageList(pageForm);
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
    @ApiOperation(value = "根据申请id查询", notes = "根据申请id查询")
    public ResultT<Map<String,Object>> queryCheckByApplyId(String id){
        Map<String, Object> list = newdataApplyService.queryCheckByApplyId(id);
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
    public ResultT updateStatus(NewdataApplyDto newdataApplyDto){
        try {
            NewdataApplyDto save = newdataApplyService.updateStatus(newdataApplyDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
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
        List<Map<String,Object>> logicInfoList = this.grpcService.getLogicInfo();
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


    @GetMapping("/getDbDataById/{id}")
    @ApiOperation(value = "根据物理库id获取物理库信息", notes = "根据物理库id获取物理库信息")
    public  ResultT<DatabaseDefineDto> getDbDataById(@PathVariable String id){
        DatabaseDefineDto databaseDefineDto = databaseDefineService.getDotById(id);
        return ResultT.success(databaseDefineDto);
    }


    /**
     * 一个表应该对应多个classLogic？
     * @param dataTableDto
     * @param request
     * @return
     */
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
    @PostMapping("/addDataStructure")
    public ResultT<String> addDataStructure(@RequestBody TableColumnDto tableColumnDto) {
        return this.newdataApplyService.addDataStructure(tableColumnDto);
    }

    @ApiOperation(value="修改表结构接口",notes="添加表结构接口")
    @PostMapping("/updateDataStructure")
    public ResultT<String> updateDataStructure(@RequestBody TableColumnDto tableColumnDto) {
        return this.newdataApplyService.updateDataStructure(tableColumnDto);
    }

    /*@ApiOperation(value="根据物理库id获取表信息",notes="根据物理库id获取表信息")
    @PostMapping("getTableByPhysicsId/{databaseId}")
    public ResultT<String> getTableByPhysicsId(@PathVariable String databaseId) {
        return this.newdataApplyService.getTableByPhysicsId(databaseId);
    }*/

    @ApiOperation(value="根据存储编码、物理库获取表信息",notes="根据存储编码、物理库获取表信息")
    @PostMapping("/getDataTableByType")
    public ResultT<List<DataTableDto>> getDataTableByType(@RequestBody DataLogicDto dataLogicDto){
        List<DataTableDto> dataTableByType = newdataApplyService.getDataTableByType(dataLogicDto);
        return ResultT.success(dataTableByType);
    }

    @GetMapping("/getDataStructure/{id}")
    @ApiOperation(value = "根据表id获取表字段信息", notes = "根据表id获取表字段信息")
    public ResultT<List<TableColumnDto>> getDataStructure(@PathVariable String id){
        List<TableColumnDto> tableColumnDtos = tableColumnService.findByTableId(id);
        return ResultT.success(tableColumnDtos);
    }

    @ApiOperation(value="根据表id获取表索引信息",notes="根据表id获取表索引信息")
    @PostMapping("/getTableIndex/{id}")
    public ResultT<List<TableIndexDto>> getTableIndex(@PathVariable String id){
        List<TableIndexDto> tableIndexDtos = tableIndexService.findByTableId(id);
        return ResultT.success(tableIndexDtos);
    }

    /**
     * 外键可以对应多个datalogic,
     * 不同的物理库，相同的编码和存储类型，他们的表结构和外键是一样的，分库分表键不一定一样？
     * @param dataLogicDto
     * @return
     */
    @ApiOperation(value="根据存储编码、逻辑库、存储类型获取键值表的外键",notes="根据存储编码、物理库获取键值表的外键")
    @PostMapping("/getForeignKey")
    public ResultT<List<Map<String,Object>>> getForeignKey(@RequestBody DataLogicDto dataLogicDto){
        List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
        List<DataLogicDto> dataLogicDtos = dataLogicService.findByDataClassIdAndLogicFlagAndStorageType(dataLogicDto);
        if(dataLogicDtos != null && dataLogicDtos.size() > 0){
            for(DataLogicDto logicDto : dataLogicDtos){
                List<TableForeignKeyDto> byClassLogicId = tableForeignKeyService.findByClassLogicId(logicDto.getId());
                if(byClassLogicId != null && byClassLogicId.size() > 0){
                    for(TableForeignKeyDto tableForeignKeyDto : byClassLogicId){
                        Map<String,Object> result = new HashMap<String,Object>();
                        result.put("K",byClassLogicId.get(0).getKeyColumn());
                        result.put("V",byClassLogicId.get(0).getEleColumn());
                        results.add(result);
                    }
                    break;
                }
            }

        }
        return ResultT.success(results);
    }

    @ApiOperation(value="根据表id获取表分库分表信息",notes="根据表id获取表分库分表信息")
    @PostMapping("/findSharding/{id}")
    public ResultT<List<ShardingDto>> findSharding(@PathVariable String id){
        List<ShardingDto> shardingDtos = shardingService.getDotByTableId(id);
        return ResultT.success(shardingDtos);
    }

    /**
     * 在线时间？
     * @param id
     * @return
     */
    @ApiOperation(value="根据资料id获取资料在线时间段",notes="根据资料id获取资料在线时间段")
    @PostMapping("/getArchiveInfo/{id}")
    public ResultT<Map<String,Object>> getArchiveInfo(@PathVariable String id){
        Map<String,Object> archiveInfo = newdataApplyService.getArchiveInfo(id);
        return ResultT.success(archiveInfo);
    }

    @GetMapping("/getTableDataInfo")
    @ApiOperation(value = "分页查询在线时间数据", notes = "分页查询在线时间数据")
    public ResultT<PageBean> getTableDataInfo(HttpServletRequest request,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ResultT<PageBean> resultT=new ResultT<>();
        String tableId = request.getParameter("tableId");
        String databaseId = request.getParameter("databaseId");
        PageBean pageBean = this.newdataApplyService.getTableDataInfo(tableId, databaseId, pageNum, pageSize);
        resultT.setData(pageBean);
        return resultT;
    }

    //selectYang样例数据查询


}
