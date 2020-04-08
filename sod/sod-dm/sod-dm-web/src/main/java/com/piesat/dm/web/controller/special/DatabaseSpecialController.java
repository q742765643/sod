package com.piesat.dm.web.controller.special;

import com.alibaba.fastjson.JSONObject;
import com.piesat.dm.rpc.api.special.DatabaseSpecialAuthorityService;
import com.piesat.dm.rpc.api.special.DatabaseSpecialReadWriteService;
import com.piesat.dm.rpc.api.special.DatabaseSpecialService;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialAuthorityDto;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialDto;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialReadWriteDto;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 专题库管理
 * @Description
 * @ClassName DatabaseSpecialController
 * @author wulei
 * @date 2020/2/12 15:56
 */
@Api(tags = "专题库管理")
@RequestMapping("/dm/databaseSpecial")
@RestController
public class DatabaseSpecialController {

    @Autowired
    private DatabaseSpecialService databaseSpecialService;
    @Autowired
    private DatabaseSpecialReadWriteService databaseSpecialReadWriteService;
    @Autowired
    private DatabaseSpecialAuthorityService databaseSpecialAuthorityService;


    @PostMapping(value = "/save")
    @ApiOperation(value = "添加", notes = "添加")
    public ResultT save(@RequestBody DatabaseSpecialDto databaseSpecialDto) {
        try {
            DatabaseSpecialDto save = this.databaseSpecialService.saveDto(databaseSpecialDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "获取专题库列表")
    @RequiresPermissions("dm:databaseSpecial:specialList")
    @GetMapping(value = "/specialList")
    public ResultT all() {
        try {
            List<DatabaseSpecialDto> all = this.databaseSpecialService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:databaseSpecial:getById")
    @GetMapping(value = "/getById")
    public ResultT get(String id) {
        try {
            DatabaseSpecialDto databaseSpecialDto = this.databaseSpecialService.getDotById(id);
            return ResultT.success(databaseSpecialDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "获取专题库资料列表")
    @RequiresPermissions("dm:databaseSpecial:getSpecialDataList")
    @GetMapping(value = "/getSpecialDataList")
    public ResultT getSpecialDataList(String sdbId,String dataType) {
        try {
            List<DatabaseSpecialReadWriteDto> dataList = this.databaseSpecialReadWriteService.getDotList(sdbId,dataType);
            return ResultT.success(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:databaseSpecial:del")
    @DeleteMapping(value = "/delete")
    public ResultT delete(String id) {
        try {
            this.databaseSpecialService.deleteById(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据专题库id删除专题库权限表中记录")
    @RequiresPermissions("dm:databaseSpecial:deleteAuthorityBySdbId")
    @DeleteMapping(value = "/deleteAuthorityBySdbId")
    public ResultT deleteAuthorityBySdbId(String sdbId) {
        try {
            this.databaseSpecialService.deleteAuthorityBySdbId(sdbId);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "修改专题库基本信息")
    @RequiresPermissions("dm:databaseSpecial:update")
    @PostMapping(value = "/update")
    public ResultT update(@RequestBody DatabaseSpecialDto databaseSpecialDto) {
        try {
            DatabaseSpecialDto save = this.databaseSpecialService.saveDto(databaseSpecialDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据专题库id修改专题库使用状态")
    @RequiresPermissions("dm:databaseSpecial:updateUseStatusById")
    @PostMapping(value = "/updateUseStatusById")
    public ResultT updateUseStatusById(String sdbId,String useStatus) {
        try {
            DatabaseSpecialDto save = this.databaseSpecialService.updateUseStatusById(sdbId, useStatus);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据获取专题库对应数据库权限")
    @RequiresPermissions("dm:databaseSpecial:getAuthorityBySdbId")
    @GetMapping(value = "/getAuthorityBySdbId")
    public ResultT getAuthorityBySdbId(String sdbId) {
        try {
            List<DatabaseSpecialAuthorityDto> authorityList = this.databaseSpecialAuthorityService.getAuthorityBySdbId(sdbId);
            return ResultT.success(authorityList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "数据库授权")
    @RequiresPermissions("dm:databaseSpecial:empowerDatabaseSperial")
    @PostMapping(value = "/empowerDatabaseSperial")
    public ResultT empowerDatabaseSperial(@RequestBody DatabaseDto DatabaseDto) {
        try {
            this.databaseSpecialService.empowerDatabaseSperial(DatabaseDto);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "资料授权-单独")
    @RequiresPermissions("dm:databaseSpecial:empowerDataOne")
    @PostMapping(value = "/empowerDataOne")
    public ResultT empowerDataOne(@RequestBody DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto) {
        try {
            this.databaseSpecialService.empowerDataOne(databaseSpecialReadWriteDto);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "资料授权-批量")
    @RequiresPermissions("dm:databaseSpecial:empowerDataOne")
    @PostMapping(value = "/empowerDataBatch")
    public ResultT empowerDataBatch(@RequestBody String listString) {
        try {
            List<DatabaseSpecialReadWriteDto> databaseSpecialReadWriteDtoList = JSONObject.parseArray(listString,
                    DatabaseSpecialReadWriteDto.class);
            this.databaseSpecialService.empowerDataBatch(databaseSpecialReadWriteDtoList);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据专题库ID获取对应树和资料信息")
    @RequiresPermissions("dm:databaseSpecial:getDataTreeBySdbId")
    @GetMapping(value = "/getDataTreeBySdbId")
    public ResultT getDataTreeBySdbId(String sdbId) {
        try {
            Map<String, Object> treeBySdbId = this.databaseSpecialService.getDataTreeBySdbId(sdbId);
            return ResultT.success(treeBySdbId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据专题库ID获取对应树信息")
    @RequiresPermissions("dm:databaseSpecial:getTreeBySdbId")
    @GetMapping(value = "/getTreeBySdbId")
    public ResultT getTreeBySdbId(String sdbId) {
        try {
            Map<String, Object> treeBySdbId = this.databaseSpecialService.getTreeBySdbId(sdbId);
            return ResultT.success(treeBySdbId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据审核状态查询")
    @RequiresPermissions("dm:databaseSpecial:getByExamineStatus")
    @GetMapping(value = "/getByExamineStatus")
    public ResultT getByExamineStatus(String examineStatus) {
        try {
            List<DatabaseSpecialDto> databaseSpecialDtos = this.databaseSpecialService.getByExamineStatus(examineStatus);
            return ResultT.success(databaseSpecialDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据用户id查询其他用户创建并且没有被该用户引用的所有专题库信息")
    @RequiresPermissions("dm:databaseSpecial:getAllOtherRecordByUserId")
    @GetMapping(value = "/getAllOtherRecordByUserId")
    public ResultT getAllOtherRecordByUserId(String userId,String useStatus) {
        try {
            List<Map<String,Object>> databaseSpecialDtos = this.databaseSpecialService.getAllOtherRecordByUserId(userId,useStatus);
            return ResultT.success(databaseSpecialDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据专题库id查询专题库申请资料（去除该用户申请的资料授权）")
    @RequiresPermissions("dm:databaseSpecial:getRecordSpecialByTdbId")
    @GetMapping(value = "/getRecordSpecialByTdbId")
    public ResultT getRecordSpecialByTdbId(String sdbId,String userId) {
        try {
            List<Map<String,Object>> recordSpecial = this.databaseSpecialReadWriteService.getRecordSpecialByTdbId(sdbId,userId);
            return ResultT.success(recordSpecial);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "根据用户id和使用状态查询")
    @RequiresPermissions("dm:databaseSpecial:getByUserIdAndUseStatus")
    @GetMapping(value = "/getByUserIdAndUseStatus")
    public ResultT getByUserIdAndUseStatus(String userId,String useStatus) {
        try {
            List<DatabaseSpecialDto> databaseSpecialDtos = this.databaseSpecialService.getByUserIdAndUseStatus(userId,useStatus);
            return ResultT.success(databaseSpecialDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }



}
