package com.piesat.dm.web.controller.datatable;

import com.piesat.dm.rpc.api.dataapply.NewdataApplyService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.api.dataclass.DataLogicService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.dto.dataapply.NewdataApplyDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.dm.rpc.dto.dataclass.DataLogicDto;
import com.piesat.dm.rpc.dto.datatable.DataTableDto;
import com.piesat.dm.rpc.dto.datatable.SampleData;
import com.piesat.dm.rpc.dto.datatable.TableSqlDto;
import com.piesat.dm.rpc.service.GrpcService;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.ucenter.rpc.dto.system.DictDataDto;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 表信息
 *
 * @author cwh
 * @date 2019年 11月29日 09:56:25
 */
@Api(tags = "表信息管理")
@RequestMapping("/dm/dataTable")
@RestController
public class DataTableController {
    @Autowired
    private DataTableService dataTableService;
    @Autowired
    private DataClassService dataClassService;
    @Autowired
    private DataLogicService dataLogicService;
    @Autowired
    private NewdataApplyService newdataApplyService;
    @Autowired
    private GrpcService grpcService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:dataTable:add")
    @Log(title = "表信息管理(新增表信息)", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody DataTableDto dataTableDto) {
        try {
            String id = dataTableDto.getId();
            if (StringUtils.isNotBlank(id)) {
                DataTableDto dot = this.dataTableService.getDotById(id);
                dataTableDto.setColumns(dot.getColumns());
                dataTableDto.setTableIndexList(dot.getTableIndexList());
            }
            DataLogicDto dataLogicDto = dataLogicService.getDotById(dataTableDto.getClassLogic().getId());
            dataTableDto.setClassLogic(dataLogicDto);
            DataTableDto save = this.dataTableService.saveDto(dataTableDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "新增申请表")
    @RequiresPermissions("dm:dataTable:addApply")
    @Log(title = "表信息管理（新增申请表信息）", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addApply")
    public ResultT addApply(String classLogicIds, String applyId) {
        try {
            NewdataApplyDto newdataApplyDto = this.newdataApplyService.getDotById(applyId);
            String[] ids = classLogicIds.split(",");
            List<DataTableDto> list = new ArrayList<>();
            for (String id : ids) {
                DataLogicDto dataLogicDto = this.dataLogicService.getDotById(id);
                DataClassDto dataClassDto = this.dataClassService.findByDataClassId(dataLogicDto.getDataClassId());
                DataTableDto dataTableDto = new DataTableDto();
                dataTableDto.setClassLogic(dataLogicDto);
                dataTableDto.setColumns(newdataApplyDto.toTableColumn());
                dataTableDto.setCreateTime(new Date());
                dataTableDto.setDataServiceId(dataClassDto.getDataClassId());
                dataTableDto.setDbTableType("E");
                dataTableDto.setUserId(newdataApplyDto.getUserId());
                dataTableDto.setNameCn(dataClassDto.getClassName());
                dataTableDto.setDataServiceName(dataClassDto.getClassName());
                dataTableDto.setTableName(newdataApplyDto.getTableName());
                DataTableDto save = this.dataTableService.saveDto(dataTableDto);
                list.add(save);
            }
            return ResultT.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:dataTable:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DataTableDto dataTableDto = this.dataTableService.getDotById(id);
            return ResultT.success(dataTableDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:dataTable:del")
    @Log(title = "表信息管理（删除表信息）", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.dataTableService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有(禁用)")
    @RequiresPermissions("dm:dataTable:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
//            List<DataTableDto> all = this.dataTableService.all();
            return ResultT.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据物理库id和存储编码查询")
    @RequiresPermissions("dm:dataTable:dc")
    @GetMapping(value = "/dc")
    public ResultT getByDatabaseIdAndClassId(String databaseId, String dataClassId) {
        try {
            List<DataTableDto> r = this.dataTableService.getByDatabaseIdAndClassId(databaseId, dataClassId);
            return ResultT.success(r);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据数据用途id查询")
    @RequiresPermissions("dm:dataTable:gcl")
    @GetMapping(value = "/gcl")
    public ResultT getByClassLogicId(String classLogic) {
        try {
            List<DataTableDto> r = this.dataTableService.getByClassLogicId(classLogic);
            return ResultT.success(r);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据物理库id查询")
    @RequiresPermissions("dm:dataTable:db")
    @GetMapping(value = "/db")
    public ResultT getByDatabaseId(String databaseId) {
        try {
            List<Map<String, Object>> r = this.dataTableService.getByDatabaseId(databaseId);
            return ResultT.success(r);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据用户id查询资料及父级")
    //@RequiresPermissions("dm:dataTable:findByUserId")
    @GetMapping(value = "/findByUserId")
    public ResultT findByUserId(String userId) {
        try {
            List<Map<String, Object>> r = this.dataTableService.findByUserId(userId);
            return ResultT.success(r);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询样例数据")
    @RequiresPermissions("dm:dataTable:sample")
    @PostMapping(value = "/sample")
    public ResultT getSampleData(@RequestBody SampleData sampleData) {
        try {
            return this.dataTableService.getSampleData(sampleData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "粘贴表结构")
    @RequiresPermissions("dm:dataTable:paste")
    @Log(title = "表信息管理（粘贴表结构信息）", businessType = BusinessType.INSERT)
    @PostMapping(value = "/paste")
    public ResultT paste(String copyId, String pasteId) {
        try {
            return this.dataTableService.paste(copyId, pasteId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询概览信息")
//    @RequiresPermissions("dm:dataTable:getOverview")
    @GetMapping(value = "/getOverview")
    public ResultT getOverview(String databaseId, String dataClassId) {
        try {
            return this.dataTableService.getOverview(databaseId, dataClassId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询sql信息")
    @RequiresPermissions("dm:dataTable:getSql")
    @GetMapping(value = "/getSql")
    public ResultT getSql(String tableId, String databaseId) {
        try {
            return this.grpcService.getSql(tableId, databaseId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据类型查询字典信息")
    @RequiresPermissions("dm:dataTable:getDictByType")
    @GetMapping(value = "/getDictByType")
    public ResultT getDictByType(String dictType) {
        List<DictDataDto> dictByType = this.grpcService.getDictByType(dictType);
        return ResultT.success(dictByType);
    }

    @ApiOperation(value = "根据资料编码查询")
    //@RequiresPermissions("dm:dataTable:getByClassId")
    @GetMapping(value = "/getByClassId")
    public ResultT getByClassId(String dataClassId) {
        try {
            List<Map<String, Object>> r = this.dataTableService.getByClassId(dataClassId);
            return ResultT.success(r);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "根据资料存储编码取得对应表的相关存储结构详细信息")
    //@RequiresPermissions("dm:dataTable:getMultiDataInfoByClassId")
    @GetMapping(value = "/getMultiDataInfoByClassId")
    public ResultT getMultiDataInfoByClassId(String dataClassId) {
        try {
            List<Map<String, Object>> r = this.dataTableService.getMultiDataInfoByClassId(dataClassId);
            return ResultT.success(r);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "创建表结构")
    @RequiresPermissions("dm:dataTable:createTable")
    @Log(title = "表信息管理（创建数据库表结构）", businessType = BusinessType.INSERT)
    @PostMapping(value = "/createTable")
    public ResultT createTable(@RequestBody TableSqlDto tableSqlDto) {
        try {
            ResultT resultT = this.dataTableService.existTable(tableSqlDto);
            if (resultT.getCode()==1||resultT.getData().equals(true)){
                return ResultT.failed("数据库已经存在表结构!");
            }
            return this.dataTableService.createTable(tableSqlDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "表结构是否存在")
    @RequiresPermissions("dm:dataTable:existTable")
    @GetMapping(value = "/existTable")
    public ResultT existTable(TableSqlDto tableSqlDto) {
        try {
            return this.dataTableService.existTable(tableSqlDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
