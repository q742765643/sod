package com.piesat.dm.web.controller.datatable;

import com.piesat.dm.rpc.api.datatable.TableDataStatisticsService;
import com.piesat.dm.rpc.dto.datatable.TableDataStatisticsDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
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
 * 表数据统计
 *
 * @author cwh
 * @date 2020年 02月13日 14:56:37
 */
@Api(tags = "表数据统计")
@RequestMapping("/dm/datastatistics")
@RestController
public class TableDataStatisticsController {

    @Autowired
    private TableDataStatisticsService tableDataStatisticsService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:datastatistics:add")
    @Log(title = "表数据统计", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody TableDataStatisticsDto tableDataStatisticsDto) {
        try {
            TableDataStatisticsDto save = this.tableDataStatisticsService.saveDto(tableDataStatisticsDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:datastatistics:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            TableDataStatisticsDto tableDataStatisticsDto = this.tableDataStatisticsService.getDotById(id);
            return ResultT.success(tableDataStatisticsDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:datastatistics:del")
    @Log(title = "表数据统计", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.tableDataStatisticsService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:datastatistics:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<TableDataStatisticsDto> all = this.tableDataStatisticsService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    @RequiresPermissions("dm:datastatistics:list")
    public ResultT<PageBean> list(TableDataStatisticsDto tableDataStatisticsDto,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<TableDataStatisticsDto> pageForm = new PageForm<>(pageNum, pageSize, tableDataStatisticsDto);
        PageBean pageBean = tableDataStatisticsService.list(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @ApiOperation(value = "获取在线时间(根据存储编码和统计日期查询)")
    @GetMapping(value = "/getOnlineTime")
    public ResultT getOnlineTime(String classDataId,String statisticDate) {
        try {
            Map<String, Object> onlineTime = this.tableDataStatisticsService.getOnlineTime(classDataId, statisticDate);
            return ResultT.success(onlineTime);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

}
