package com.piesat.dm.web.controller.datatable;

import com.piesat.dm.rpc.api.datatable.DataServerConfigService;
import com.piesat.dm.rpc.dto.datatable.DataServerConfigDto;
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
 * 服务信息配置
 *
 * @author cwh
 * @date 2020年 02月12日 16:01:07
 */
@Api(tags = "服务信息配置")
@RequestMapping("/dm/dataserverconfig")
@RestController
public class DataServerConfigController {
    @Autowired
    private DataServerConfigService dataServerConfigService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:dataserverconfig:add")
    @Log(title = "服务基础信息管理（新增）", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody DataServerConfigDto dataServerConfigDto) {
        try {
            DataServerConfigDto save = this.dataServerConfigService.saveDto(dataServerConfigDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "新增多个")
    @RequiresPermissions("dm:dataserverconfig:addList")
    @Log(title = "服务基础信息管理（批量添加）", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addList")
    public ResultT addList(@RequestBody List<DataServerConfigDto> dataServerConfigDto) {
        try {
            List<DataServerConfigDto> save = this.dataServerConfigService.saveDtoList(dataServerConfigDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:dataserverconfig:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DataServerConfigDto dataServerConfigDto = this.dataServerConfigService.getDotById(id);
            return ResultT.success(dataServerConfigDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:dataserverconfig:del")
    @Log(title = "服务基础信息管理（删除）", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.dataServerConfigService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据多个id删除")
    @RequiresPermissions("dm:dataserverconfig:delByIds")
    @Log(title = "服务基础信息管理（批量删除）", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/delByIds")
    public ResultT delByIds(String ids) {
        try {
            this.dataServerConfigService.delByIds(ids);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:dataserverconfig:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<DataServerConfigDto> all = this.dataServerConfigService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询")
    @RequiresPermissions("dm:dataserverconfig:list")
    public ResultT<PageBean> list(String dataServiceId,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Map<String, String> map = new HashMap<String, String>();
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<Map<String, String>> pageForm = new PageForm<>(pageNum, pageSize, map);
        PageBean pageBean = dataServerConfigService.list(pageForm, dataServiceId);
        resultT.setData(pageBean);
        return resultT;
    }

    @GetMapping("/getMaxNum")
    @ApiOperation(value = "查询数据服务的最大编号")
    @RequiresPermissions("dm:dataserverconfig:getMaxNum")
    public ResultT getMaxNum(String data_service_id) {
        int dataServiceMaxNum = dataServerConfigService.getDataServiceMaxNum(data_service_id);
        ResultT r = new ResultT();
        r.setData(dataServiceMaxNum);
        return r;
    }

}
