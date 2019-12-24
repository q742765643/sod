package com.piesat.schedule.web.controller.move;

import com.piesat.schedule.rpc.api.move.MoveService;
import com.piesat.schedule.rpc.dto.move.MoveDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
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
 * @create: 2019-12-24 17:19
 **/
@RestController
@RequestMapping("/schedule/move")
public class MoveController {
    @Autowired
    private MoveService moveService;

    @RequiresPermissions("schedule:move:list")
    @GetMapping("/list")
    public ResultT<PageBean> list(MoveDto move, int pageNum, int pageSize)
    {
        ResultT<PageBean> resultT=new ResultT<>();
        PageForm<MoveDto> pageForm=new PageForm<>(pageNum,pageSize,move);
        PageBean pageBean=moveService.selectMoveList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @RequiresPermissions("schedule:move:query")
    @GetMapping(value = "/{moveId}")
    public ResultT<MoveDto> getInfo(@PathVariable String moveId)
    {
        ResultT<MoveDto> resultT=new ResultT<>();
        MoveDto moveDto= moveService.findMoveById(moveId);
        resultT.setData(moveDto);
        return resultT;
    }
    @RequiresPermissions("schedule:move:add")
    @Log(title = "迁移任务管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultT<String> add(@RequestBody MoveDto move)
    {
        ResultT<String> resultT=new ResultT<>();
        moveService.saveMove(move);
        return resultT;
    }

    @RequiresPermissions("schedule:move:edit")
    @Log(title = "迁移任务管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultT<String> edit(@RequestBody MoveDto move)
    {
        ResultT<String> resultT=new ResultT<>();
        moveService.updateMove(move);
        return resultT;
    }
    /**
     * 删除用户
     */
    @RequiresPermissions("schedule:move:remove")
    @Log(title = "迁移任务管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{moveIds}")
    public  ResultT<String> remove(@PathVariable String[] moveIds)
    {
        ResultT<String> resultT=new ResultT<>();
        moveService.deleteMoveByIds(moveIds);
        return resultT;
    }
}

