package com.piesat.schedule.web.controller.nas;

import com.piesat.schedule.entity.nas.NasManageEntity;
import com.piesat.schedule.rpc.api.nas.NasManageService;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.rpc.dto.nas.NasManageDto;
import com.piesat.schedule.rpc.dto.nas.NasManageFileDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName : NasManageController
 * @Description : nas申请
 * @Author : zzj
 * @Date: 2020-09-07 14:32
 */
@RestController
@Api(value="nas存储申请接口",tags = {"nas存储申请接口"})
@RequestMapping("/schedule/nasmanage")
public class NasManageController {
    @Autowired
    private NasManageService nasManageService;

    @ApiOperation(value = "分页查询nas存储申请信息", notes = "分页查询nas存储申请信息")
    @GetMapping("/list")
    public ResultT<PageBean<NasManageDto>> selectPageList(NasManageDto nasManageDto, int pageNum, int pageSize)
    {
        ResultT<PageBean<NasManageDto>> resultT=new ResultT<>();
        PageForm<NasManageDto> pageForm=new PageForm<>(pageNum,pageSize,nasManageDto);
        PageBean pageBean=nasManageService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @ApiOperation(value = "添加nas存储申请", notes = "添加nas存储申请")
    @Log(title = "nas存储申请管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultT<String> add(NasManageFileDto nasManageDto)
    {
        ResultT<String> resultT=new ResultT<>();
        nasManageService.add(nasManageDto,resultT);
        return resultT;
    }
    @ApiOperation(value = "审核nas存储申请", notes = "审核nas存储申请")
    @Log(title = "nas存储申请管理", businessType = BusinessType.INSERT)
    @PostMapping("/audit")
    public ResultT<String> audit(@RequestBody NasManageDto nasManageDto)
    {
        ResultT<String> resultT=new ResultT<>();
        nasManageService.audit(nasManageDto,resultT);
        return resultT;
    }

    @ApiOperation(value = "删除nas存储申请管理", notes = "删除nas存储申请管理")
    @Log(title = "nas存储申请管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public  ResultT<String> remove(@PathVariable String[] ids)
    {
        ResultT<String> resultT=new ResultT<>();
        nasManageService.deleteIds(ids);
        return resultT;
    }
}

