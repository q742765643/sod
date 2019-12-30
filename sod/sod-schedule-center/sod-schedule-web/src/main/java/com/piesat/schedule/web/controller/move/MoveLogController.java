package com.piesat.schedule.web.controller.move;

import com.piesat.schedule.rpc.api.move.MoveLogService;
import com.piesat.schedule.rpc.dto.move.MoveDto;
import com.piesat.schedule.rpc.dto.move.MoveLogDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.soap.Addressing;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-29 20:18
 **/
@RestController
@RequestMapping("/schedule/moveLog")
public class MoveLogController {
    @Autowired
    private MoveLogService moveLogService;

    @RequiresPermissions("schedule:moveLog:list")
    @GetMapping("/list")
    public ResultT<PageBean> list(MoveLogDto moveLog, int pageNum, int pageSize)
    {
        ResultT<PageBean> resultT=new ResultT<>();
        PageForm<MoveLogDto> pageForm=new PageForm<>(pageNum,pageSize,moveLog);
        PageBean pageBean=moveLogService.selectMoveLogList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }


    @RequiresPermissions("schedule:moveLog:query")
    @GetMapping(value = "/{moveLogId}")
    public ResultT<MoveLogDto> getInfo(@PathVariable String moveLogId)
    {
        ResultT<MoveLogDto> resultT=new ResultT<>();
        MoveLogDto moveLogDto= moveLogService.findMoveLogById(moveLogId);
        resultT.setData(moveLogDto);
        return resultT;
    }

    @RequiresPermissions("schedule:moveLog:remove")
    @Log(title = "迁移任务日志管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{moveLogIds}")
    public  ResultT<String> remove(@PathVariable String[] moveLogIds)
    {
        ResultT<String> resultT=new ResultT<>();
        moveLogService.deleteMoveLogByIds(moveLogIds);
        return resultT;
    }

}

