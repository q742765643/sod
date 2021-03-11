package com.piesat.dm.web.controller.datatable;

import com.piesat.dm.rpc.api.datatable.ShardingService;
import com.piesat.dm.rpc.dto.datatable.TablePartDto;
import com.piesat.dm.rpc.dto.datatable.ShardingList;
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
 * 分库分表键
 *
 * @author cwh
 * @date 2019年 12月09日 14:14:12
 */
@Api(tags = "表分库分表键")
@RequestMapping("/dm/sharding")
@RestController
public class ShardingController {
    @Autowired
    private ShardingService shardingService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:sharding:add")
    @Log(title = "表分库分表键（新增）", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody TablePartDto tablePartDto) {
        try {
            TablePartDto save = this.shardingService.saveDto(tablePartDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "新增多个")
    @RequiresPermissions("dm:sharding:adds")
    @Log(title = "表分库分表键（批量添加）", businessType = BusinessType.INSERT)
    @PostMapping(value = "/saves")
    public ResultT saves(@RequestBody ShardingList shardingList) {
        try {
            List<TablePartDto> save = this.shardingService.saveDto(shardingList.getShardingList());
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:sharding:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            TablePartDto tablePartDto = this.shardingService.getDotById(id);
            return ResultT.success(tablePartDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:sharding:del")
    @Log(title = "表分库分表键（删除）", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.shardingService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:sharding:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<TablePartDto> all = this.shardingService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据表id查询分库分表键")
    @RequiresPermissions("dm:sharding:tableId")
    @GetMapping(value = "/getByTableId")
    public ResultT getDotByTableId(String id) {
        try {
            TablePartDto tablePartDtos = this.shardingService.getDotByTableId(id);
            return ResultT.success(tablePartDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

}
