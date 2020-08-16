package com.piesat.dm.web.controller.datatable;

import com.piesat.dm.rpc.api.datatable.TableIndexService;
import com.piesat.dm.rpc.dto.datatable.TableIndexDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 表索引
 *
 * @author cwh
 * @date 2019年 11月29日 10:30:01
 */
@Api(tags = "表索引")
@RequestMapping("/dm/tableIndex")
@RestController
public class TableIndexController {
    @Autowired
    private TableIndexService tableIndexService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:tableIndex:add")
    @Log(title = "表索引（新增）", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody TableIndexDto tableIndexDto) {
        return this.tableIndexService.saveDto(tableIndexDto);
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:tableIndex:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            TableIndexDto tableIndexDto = this.tableIndexService.getDotById(id);
            return ResultT.success(tableIndexDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:tableIndex:del")
    @Log(title = "表索引（删除）", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(@RequestBody TableIndexDto tableIndexDto) {
        try {
            this.tableIndexService.delete(tableIndexDto.getId());
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:tableIndex:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<TableIndexDto> all = this.tableIndexService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据tableId查询")
    @RequiresPermissions("dm:tableIndex:findByTableId")
    @GetMapping(value = "/findByTableId")
    public ResultT findByTableId(String tableId) {
        try {
            List<TableIndexDto> all = this.tableIndexService.findByTableId(tableId);
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
