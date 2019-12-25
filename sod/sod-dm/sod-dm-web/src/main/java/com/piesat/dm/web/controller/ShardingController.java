package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.ShardingService;
import com.piesat.dm.rpc.dto.ShardingDto;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分库分表键
 *
 * @author cwh
 * @date 2019年 12月09日 14:14:12
 */
@RestController
@Api(value="表分库分表键controller",tags={"表分库分表键接口"})
public class ShardingController {
    @Autowired
    private ShardingService shardingService;

    @PostMapping(value = "/api/Sharding/save")
    @ApiOperation(value = "添加分库分表键接口", notes = "添加分库分表键接口")
    public ResultT save(@RequestBody ShardingDto shardingDto) {
        try {
            ShardingDto save = this.shardingService.saveDto(shardingDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/Sharding/get")
    @ApiOperation(value = "获取分库分表键接口", notes = "获取分库分表键接口")
    public ResultT get(String id) {
        try {
            ShardingDto shardingDto = this.shardingService.getDotById(id);
            return ResultT.success(shardingDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/Sharding/del")
    @ApiOperation(value = "删除分库分表键接口", notes = "删除分库分表键接口")
    public ResultT del(String id) {
        try {
            this.shardingService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/Sharding/all")
    @ApiOperation(value = "获取所有分库分表键接口", notes = "获取所有分库分表键接口")
    public ResultT all() {
        try {
            List<ShardingDto> all = this.shardingService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PostMapping(value = "/api/Sharding/getByTableId")
    @ApiOperation(value = "根据表id查询分库分表键接口", notes = "根据表id查询分库分表键接口")
    public ResultT getDotByTableId(String id) {
        try {
            List<ShardingDto> shardingDtos = this.shardingService.getDotByTableId(id);
            return ResultT.success(shardingDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

}
