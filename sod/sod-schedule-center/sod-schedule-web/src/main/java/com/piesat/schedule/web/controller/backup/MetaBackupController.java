package com.piesat.schedule.web.controller.backup;

import com.piesat.schedule.rpc.api.backup.MetaBackupService;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.rpc.dto.backup.MetaBackupDto;
import com.piesat.schedule.rpc.service.backup.MetaBackService;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-26 16:54
 **/
@RestController
@RequestMapping("/schedule/metaBackup")
public class MetaBackupController {
    @Autowired
    private MetaBackupService metaBackupService;
    @RequiresPermissions("schedule:metaBackup:list")
    @ApiOperation(value = "分页查询元数据备份任务", notes = "分页查询元数据备份任务")
    @GetMapping("/list")
    public ResultT<PageBean> list(MetaBackupDto metaBackupDto, int pageNum, int pageSize)
    {
        ResultT<PageBean> resultT=new ResultT<>();
        PageForm<MetaBackupDto> pageForm=new PageForm<>(pageNum,pageSize,metaBackupDto);
        PageBean pageBean=metaBackupService.selectBackupList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }
    @RequiresPermissions("schedule:metaBackup:query")
    @ApiOperation(value = "根据ID查询元数据备份任务", notes = "根据ID查询元数据备份任务")
    @GetMapping(value = "/{metaBackupId}")
    public ResultT<MetaBackupDto> getInfo(@PathVariable String metaBackupId)
    {
        ResultT<MetaBackupDto> resultT=new ResultT<>();
        MetaBackupDto metaBackupDto= metaBackupService.findBackupById(metaBackupId);
        resultT.setData(metaBackupDto);
        return resultT;
    }
}

