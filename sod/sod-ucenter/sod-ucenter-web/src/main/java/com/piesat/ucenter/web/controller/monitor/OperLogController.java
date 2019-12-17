package com.piesat.ucenter.web.controller.monitor;

import com.piesat.common.annotation.HtParam;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.ucenter.rpc.api.monitor.OperLogService;
import com.piesat.ucenter.rpc.dto.monitor.OperLogDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-16 15:44
 **/
@RestController
@RequestMapping("/monitor/operlog")
public class OperLogController {
    @Autowired
    private OperLogService operLogService;

    @RequiresPermissions("monitor:operlog:list")
    @GetMapping("/list")
    public ResultT<PageBean> list(OperLogDto operLog, @HtParam(value="pageNum",defaultValue="1") int pageNum,
                                  @HtParam(value="pageSize",defaultValue="10") int pageSize)
    {
        ResultT<PageBean> resultT=new ResultT<>();
        PageForm<OperLogDto> pageForm=new PageForm(pageNum,pageSize,operLog);
        PageBean pageBean=operLogService.selectOperLogList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }
    @RequiresPermissions("monitor:operlog:remove")
    @DeleteMapping("/{operIds}")
    public ResultT<String> remove(@PathVariable String[] operIds)
    {
        ResultT<String> resultT=new ResultT<>();
        operLogService.deleteOperLogByIds(operIds);
        return resultT;
    }

    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @RequiresPermissions("monitor:operlog:remove")
    @DeleteMapping("/clean")
    public ResultT<String> clean()
    {
        ResultT<String> resultT=new ResultT<>();
        operLogService.cleanOperLog();
        return resultT;
    }
}

