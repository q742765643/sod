package com.piesat.schedule.web.controller.move;

import com.piesat.schedule.rpc.api.move.MoveService;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.rpc.dto.move.MoveDto;
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

import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-24 17:19
 **/
@RestController
@Api(value="数据迁移接口",tags = {"数据迁移接口"})
@RequestMapping("/schedule/move")
public class MoveController {
    @Autowired
    private MoveService moveService;

    @RequiresPermissions("schedule:move:list")
    @ApiOperation(value = "分页查询迁移任务", notes = "分页查询迁移任务")
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
    @ApiOperation(value = "根据ID查询迁移任务", notes = "根据ID查询迁移任务")
    @GetMapping(value = "/{moveId}")
    public ResultT<MoveDto> getInfo(@PathVariable String moveId)
    {
        ResultT<MoveDto> resultT=new ResultT<>();
        MoveDto moveDto= moveService.findMoveById(moveId);
        resultT.setData(moveDto);
        return resultT;
    }
    @RequiresPermissions("schedule:move:add")
    @ApiOperation(value = "添加迁移任务", notes = "添加迁移任务")
    @Log(title = "迁移任务管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultT<String> add(@RequestBody MoveDto move)
    {
        if(null==move.getIsAlarm()||"".equals(move.getIsAlarm())){
            move.setIsAlarm("1");
        }
        ResultT<String> resultT=new ResultT<>();
        moveService.saveMove(move);
        return resultT;
    }

    @RequiresPermissions("schedule:move:edit")
    @ApiOperation(value = "修改迁移任务", notes = "修改迁移任务")
    @Log(title = "迁移任务管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultT<String> edit(@RequestBody MoveDto move)
    {
        if(null==move.getIsAlarm()||"".equals(move.getIsAlarm())){
            move.setIsAlarm("1");
        }
        ResultT<String> resultT=new ResultT<>();
        moveService.updateMove(move);
        return resultT;
    }
    /**
     * 删除用户
     */
    @RequiresPermissions("schedule:move:remove")
    @ApiOperation(value = "删除迁移任务", notes = "删除迁移任务")
    @Log(title = "迁移任务管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{moveIds}")
    public  ResultT<String> remove(@PathVariable String[] moveIds)
    {
        ResultT<String> resultT=new ResultT<>();
        moveService.deleteMoveByIds(moveIds);
        return resultT;
    }
    @ApiOperation(value = "查询迁移任务物理库", notes = "查询迁移任务物理库")
    @GetMapping("/findDatabase")
    public ResultT findDatabase(){
        ResultT resultT=new ResultT<>();
        List<Map<String,Object>> mapList=moveService.findDatabase();
        resultT.setData(mapList);
        return resultT;
    }
    @ApiOperation(value = "查询需要迁移资料列表", notes = "查询需要迁移资料列表")
    @GetMapping("/findDataClassId")
    public ResultT findDataClassId(String databaseId,String dataClassId){
        ResultT resultT=new ResultT<>();
        List<Map<String,Object>> mapList=moveService.findDataClassId(databaseId,dataClassId);
        resultT.setData(mapList);
        return resultT;
    }

    @ApiOperation(value = "数据迁移配置导出", notes = "数据迁移配置导出")
    @RequiresPermissions("schedule:move:export")
    @GetMapping("/export")
    public void exportExcel(MoveDto moveDto){
        moveService.exportExcel(moveDto);

    }

    @ApiOperation(value = "根据资料存储编码查询", notes = "根据资料存储编码查询")
    @GetMapping("/findByDataClassId")
    public ResultT findByDataClassId(String dataClassId){
        ResultT resultT=new ResultT<>();
        List<MoveDto> mapList=moveService.findByDataClassId(dataClassId);
        resultT.setData(mapList);
        return resultT;
    }

}

