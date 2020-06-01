package com.piesat.dm.web.controller.datatable;

import com.piesat.dm.rpc.api.datatable.GridDecodingService;
import com.piesat.dm.rpc.dto.datatable.GridDecodingDto;
import com.piesat.dm.rpc.dto.datatable.GridDecodingList;
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
 * 编码配置
 *
 * @author cwh
 * @date 2020年 02月12日 14:51:55
 */
@Api(tags = "编码配置")
@RequestMapping("/dm/griddecoding")
@RestController
public class GridDecodingController {
    @Autowired
    private GridDecodingService gridDecodingService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:griddecoding:add")
    @Log(title = "编码配置管理（新增）", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody GridDecodingDto gridDecodingDto) {
        try {
            GridDecodingDto save = this.gridDecodingService.saveDto(gridDecodingDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "新增多个")
    @RequiresPermissions("dm:griddecoding:saveList")
    @Log(title = "编码配置管理（批量新增）", businessType = BusinessType.INSERT)
    @PostMapping(value = "/saveList")
    public ResultT saveList(@RequestBody GridDecodingList gridDecodingList) {
        try {
            List<GridDecodingDto> save = this.gridDecodingService.saveList(gridDecodingList);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:griddecoding:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            GridDecodingDto gridDecodingDto = this.gridDecodingService.getDotById(id);
            return ResultT.success(gridDecodingDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:griddecoding:del")
    @Log(title = "编码配置管理（删除）", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.gridDecodingService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "根据id删除多个")
    @RequiresPermissions("dm:griddecoding:delByIds")
    @Log(title = "编码配置管理（批量删除）", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/delByIds")
    public ResultT delByIds(String ids) {
        try {
            this.gridDecodingService.delByIds(ids);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:griddecoding:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<GridDecodingDto> all = this.gridDecodingService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    @RequiresPermissions("dm:griddecoding:list")
    public ResultT<PageBean> list(String dataServiceId,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Map<String,String> map = new HashMap<String,String>();
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<Map<String,String>> pageForm = new PageForm<>(pageNum, pageSize, map);
        PageBean pageBean = gridDecodingService.list(pageForm,dataServiceId);
        resultT.setData(pageBean);
        return resultT;
    }
}
