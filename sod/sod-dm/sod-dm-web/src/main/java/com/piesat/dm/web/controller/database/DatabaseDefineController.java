package com.piesat.dm.web.controller.database;

import com.piesat.common.utils.StringUtils;
import com.piesat.dm.common.util.ExportTableUtil;
import com.piesat.dm.rpc.api.database.DatabaseDefineService;
import com.piesat.dm.rpc.api.database.DatabaseUserService;
import com.piesat.dm.rpc.api.dataclass.LogicDefineService;
import com.piesat.dm.rpc.dto.database.DatabaseDefineDto;
import com.piesat.dm.rpc.dto.database.DatabaseUserDto;
import com.piesat.dm.rpc.dto.dataclass.LogicDatabaseDto;
import com.piesat.dm.rpc.dto.dataclass.LogicDefineDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 数据库类型定义
 *
 * @author cwh
 * @date 2019年 11月29日 09:41:21
 */
@Api(tags = "数据库类型管理")
@RequestMapping("/dm/databaseDefine")
@RestController
public class DatabaseDefineController {
    @Autowired
    private DatabaseDefineService databaseDefineService;
    @Autowired
    private DatabaseUserService databaseUserService;
    @Autowired
    private LogicDefineService logicDefineService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:databaseDefine:add")
    @Log(title = "数据库类型管理", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody DatabaseDefineDto databaseDefineDto) {
        try {
            DatabaseDefineDto save = this.databaseDefineService.saveDto(databaseDefineDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:databaseDefine:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DatabaseDefineDto DatabaseDefineDto = this.databaseDefineService.getDotById(id);
            return ResultT.success(DatabaseDefineDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:databaseDefine:del")
    @Log(title = "数据库类型管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.databaseDefineService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据多个id删除(逗号分隔)")
    @RequiresPermissions("dm:databaseDefine:delByIds")
    @Log(title = "数据库类型管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/delByIds")
    public ResultT delByIds(String ids) {
        try {
            this.databaseDefineService.delByIds(ids);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:databaseDefine:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<DatabaseDefineDto> all = this.databaseDefineService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询可以展示的数据库列表")
    @RequiresPermissions("dm:databaseDefine:getCanShowDatabaseDefineList")
    @GetMapping(value = "/getCanShowDatabaseDefineList")
    public ResultT getCanShowDatabaseDefineList() {
        try {
            List<DatabaseDefineDto> all = this.databaseDefineService.getDatabaseDefineList();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

//    @ApiOperation(value = "导出")
//    @RequiresPermissions("dm:databaseDefine:export")
//    @GetMapping(value = "/export")
    public void export(String id, String databaseName, HttpServletRequest request, HttpServletResponse response) {

        List<DatabaseDefineDto> all = this.databaseDefineService.export(id, databaseName);
        ArrayList<String> headList = new ArrayList<>();
        headList.add("物理库ID");
        headList.add("物理库名称");
        headList.add("物理库实例");
        headList.add("物理库类型id");
        headList.add("IP地址");
        headList.add("主备类型");
        headList.add("存储容量");
        headList.add("显示控制");
        headList.add("运行状态");

        List<List<String>> lists = new ArrayList<>();
        for (DatabaseDefineDto ddd : all) {
            ArrayList<String> strings = new ArrayList<>();
            strings.add(ddd.getId());
            strings.add(ddd.getDatabaseName());
            strings.add(ddd.getDatabaseInstance());
            strings.add(ddd.getDatabaseType());
            strings.add(ddd.getDatabaseIp());
            strings.add(ddd.getMainBakType() == 1 ? "主" : "备");
            Integer databaseCapacity = ddd.getDatabaseCapacity();
            strings.add(databaseCapacity == null ? "" : databaseCapacity.toString());
            if (ddd.getUserDisplayControl() == 1) {
                strings.add("显示");
            } else {
                strings.add("不显示");
            }
            if (ddd.getCheckConn() == 1) {
                strings.add("连接正常");
            } else {
                strings.add("连接异常");
            }
            lists.add(strings);
        }
        ExportTableUtil.exportTable(request, response, headList, lists, "物理库定义导出");

    }

    @ApiOperation(value = "导出")
//    @RequiresPermissions("dm:databaseDefine:export")
    @GetMapping(value = "/export")
    public void exportExcel(String id, String databaseName){
        databaseDefineService.exportExcel(id,databaseName);
    }

    @ApiOperation(value = "分页查询(支持id和databaseName查询)")
    @RequiresPermissions("dm:databaseDefine:page")
    @GetMapping(value = "/page")
    public ResultT<PageBean> getPage(DatabaseDefineDto databaseDefineDto,
                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        try {
            PageBean page = this.databaseDefineService.getPage(databaseDefineDto, pageNum, pageSize);
            return ResultT.success(page);
        } catch (Exception e) {
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询数据库连接")
    @RequiresPermissions("dm:databaseDefine:conStatus")
    @GetMapping(value = "/conStatus")
    public ResultT conStatus(String id) {
        try {
            DatabaseDefineDto all = this.databaseDefineService.conStatus(id);
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询数据库连接(新增)")
    @RequiresPermissions("dm:databaseDefine:connStatus")
    @PostMapping(value = "/connStatus")
    public ResultT connStatus(DatabaseDefineDto databaseDefineDto) {
        try {
            return this.databaseDefineService.connStatus(databaseDefineDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据用户ID和逻辑库ID查询物理库的信息")
    //@RequiresPermissions("dm:databaseDefine:findByUserIdAndLogicId")
    @GetMapping(value = "/findByUserIdAndLogicId")
    public ResultT findByUserIdAndLogicId(String userId,String logicId) {
        try {
            //查询用户申请的up账户
            DatabaseUserDto databaseUserDto = this.databaseUserService.findByUserIdAndExamineStatus(userId,"1");
            if(databaseUserDto == null || !StringUtils.isNotNullString(databaseUserDto.getExamineDatabaseId())){
                return ResultT.failed("请先创建存储账户！！！");
            }
            List<String> targetDatabaseIdList = new ArrayList<String>();
            //用户可用的databaseId（父级databaseId）
            List<String> list = Arrays.asList(databaseUserDto.getExamineDatabaseId().split(","));
            //逻辑库对应的databaseId（父级databaseId）
            List<LogicDefineDto> logicDefineDtos = logicDefineService.findByLogicFlag(logicId);
            if(logicDefineDtos != null && logicDefineDtos.size() >0){
                if(logicDefineDtos.get(0).getLogicDatabaseEntityList() != null && logicDefineDtos.get(0).getLogicDatabaseEntityList().size() > 0) {
                    for (LogicDatabaseDto logicDatabaseDto : logicDefineDtos.get(0).getLogicDatabaseEntityList()) {
                        if(list.contains(logicDatabaseDto.getDatabaseId())){
                            targetDatabaseIdList.add(logicDatabaseDto.getDatabaseId());
                        }
                    }
                }
            }
            List<DatabaseDefineDto> databaseDefineDtos = this.databaseDefineService.findByIdIn(targetDatabaseIdList);
            return ResultT.success(databaseDefineDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

}
