package com.piesat.dm.web.controller.dataclass;

import com.alibaba.fastjson.JSONArray;
import com.piesat.common.config.DatabseType;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.rpc.api.database.SchemaService;
import com.piesat.dm.rpc.api.dataclass.DataClassLabelService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.api.dataclass.DataClassUserService;
import com.piesat.dm.rpc.api.dataclass.DataLogicService;
import com.piesat.dm.rpc.dto.ConsistencyCheckDto;
import com.piesat.dm.rpc.dto.dataclass.*;
import com.piesat.dm.rpc.service.GrpcService;
import com.piesat.dm.rpc.vo.DataclassVo;
import com.piesat.dm.rpc.vo.TableInfoVo;
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

import java.util.*;

/**
 * 资料分类
 *
 * @author cwh
 * @date 2019年 11月29日 09:53:19
 */
@Api(tags = "资料分类管理")
@RequestMapping("/dm/dataClass")
@RestController
public class DataClassController {
    @Autowired
    private DataClassService dataClassService;
    @Autowired
    private DataLogicService dataLogicService;
    @Autowired
    private DataClassLabelService dataClassLabelService;
    @Autowired
    private DataClassUserService dataClassUserService;
    @Autowired
    private SchemaService schemaService;
    @Autowired
    private GrpcService grpcService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:dataClass:add")
    @Log(title = "资料分类管理", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody DataClassDto dataClassDto) {
        String metaDataName = dataClassDto.getMetaDataName();
        if (StringUtils.isBlank(metaDataName)) {
            dataClassDto.setMetaDataName(dataClassDto.getClassName());
        }
        DataClassDto byDataClassId = this.dataClassService.findByDataClassId(dataClassDto.getDataClassId());
        if (StringUtils.isEmpty(dataClassDto.getId()) && byDataClassId != null) {
            return ResultT.failed("存储编码已经存在！");
        }
        try {
            DataClassDto save = this.dataClassService.saveDto(dataClassDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:dataClass:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DataClassDto dataClassDto = this.dataClassService.getDotById(id);
            List<DataClassLabelDto> dataClassLabels = this.dataClassLabelService.findByDataClassId(dataClassDto.getDataClassId());
            dataClassDto.setDataClassLabelList(dataClassLabels);
            List<DataClassUserDto> dataClassUsers = this.dataClassUserService.findByDataClassId(dataClassDto.getDataClassId());
            dataClassDto.setDataClassUserList(dataClassUsers);
            return ResultT.success(dataClassDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询归档时间")
    @RequiresPermissions("dm:dataClass:getArchive")
    @GetMapping(value = "/getArchive")
    public ResultT getArchive(String ddataid) {
        try {
            Map map = this.dataClassService.getArchive(ddataid);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据DataClassId查询")
    @RequiresPermissions("dm:dataClass:findByClassId")
    @GetMapping(value = "/findByClassId")
    public ResultT findByClassId(String id) {
        try {
            DataClassDto dataClassDto = this.dataClassService.findByDataClassId(id);
            List<DataClassLogicDto> byDataClassId = this.dataLogicService.findByDataClassId(id);
            dataClassDto.setDataLogicList(byDataClassId);
            List<DataClassLabelDto> dataClassLabels = this.dataClassLabelService.findByDataClassId(dataClassDto.getDataClassId());
            dataClassDto.setDataClassLabelList(dataClassLabels);
            List<DataClassUserDto> dataClassUsers = this.dataClassUserService.findByDataClassId(dataClassDto.getDataClassId());
            dataClassDto.setDataClassUserList(dataClassUsers);
            return ResultT.success(dataClassDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据dDataId查询")
    @RequiresPermissions("dm:dataClass:findByDDataId")
    @GetMapping(value = "/findByDDataId")
    public ResultT findByDDataId(String dDataId) {
        try {
            List<DataClassDto> dataClassDtos = this.dataClassService.findByDDataId(dDataId);
            return ResultT.success(dataClassDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:dataClass:del")
    @Log(title = "资料分类管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.dataClassService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据dataClassId删除")
    @RequiresPermissions("dm:dataClass:delByClass")
    @Log(title = "资料分类管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/delByClass")
    public ResultT deleteByClassName(String dataClassId) {
        try {
            this.dataClassService.deleteByDataClassId(dataClassId);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询资料分类(parentId=0)")
//    @RequiresPermissions("dm:dataClass:findAllCategory")
    @GetMapping(value = "/findAllCategory")
    public ResultT findAllCategory() {
        try {
            List<DataClassDto> dataClassDtos = this.dataClassService.findAllCategory();
            return ResultT.success(dataClassDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:dataClass:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<DataClassDto> all = this.dataClassService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "按数据用途查询资料分类")
    @RequiresPermissions("dm:dataClass:logicClass")
    @GetMapping(value = "/logicClass")
    public ResultT getLogicClass() {
        try {
            JSONArray all = this.dataClassService.getLogicClass();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "按数据库查询资料分类")
    @RequiresPermissions("dm:dataClass:databaseClass")
    @GetMapping(value = "/databaseClass")
    public ResultT getDatabaseClass() {
        try {
            JSONArray all = null;
            if ("mysql".equals(DatabseType.type.toLowerCase())) {
                all = this.dataClassService.getDatabaseClassMysql();
            } else if ("postgresql".equals(DatabseType.type.toLowerCase())) {
                all = this.dataClassService.getDatabaseClassPostgresql();
            } else {
                all = this.dataClassService.getDatabaseClass();
            }

            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询资料分类树")
    @RequiresPermissions("dm:dataClass:getTree")
    @GetMapping(value = "/getTree")
    public ResultT getTree() {
        try {
            JSONArray tree = this.dataClassService.getTree();
            return ResultT.success(tree);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据目录查询资料")
    @RequiresPermissions("dm:dataClass:getListBYIn")
    @GetMapping(value = "/getListBYIn")
    public ResultT getListBYIn(String stringList, String className, String dDataId, String dataclassId) {
        try {
            List<String> l = new ArrayList<>();
            if (StringUtils.isNotEmpty(stringList)) {
                String[] split = stringList.split(",");
                l = Arrays.asList(split);
            }
            List<Map<String, Object>> all = this.dataClassService.getListBYIn(l, className, dDataId, dataclassId);
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询资料概览")
    @RequiresPermissions("dm:dataClass:getBaseData")
    @GetMapping(value = "/getBaseData")
    public ResultT getBaseData(DataClassDto dataClassDto,
                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        try {
            PageBean all = this.dataClassService.getBaseData(new PageForm(pageNum, pageSize), dataClassDto);
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "导出资料概览")
    @RequiresPermissions("dm:dataClass:exportBaseData")
    @GetMapping(value = "/exportBaseData")
    public void exportBaseData(DataClassDto dataClassDto) {
        this.dataClassService.exportBaseData(dataClassDto);
    }


    @ApiOperation(value = "获取新增存储编码")
    @RequiresPermissions("dm:dataClass:getNewDataClassId")
    @GetMapping(value = "/getNewDataClassId")
    public ResultT getNewDataClassId(String parentId) {
        try {
            String all = this.dataClassService.findByParentId(parentId);
            ResultT r = new ResultT();
            r.isSuccess();
            r.setData(all);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据四级编码获取数据用途")
    @GetMapping(value = "/getLogicByDdataId")
    public ResultT getLogicByDdataId(String dDataId) {
        try {
            List<Map<String, Object>> logicByDdataId = this.dataClassService.getLogicByDdataId(dDataId);
            ResultT r = new ResultT();
            r.isSuccess();
            r.setData(logicByDdataId);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据存储编码编码获取资料详情")
    @GetMapping(value = "/getDataClassCoreInfo")
    public ResultT getDataClassCoreInfo(String dDataId) {
        try {
            Map<String, Object> map = this.dataClassService.getDataClassCoreInfo(dDataId);
            ResultT r = new ResultT();
            r.isSuccess();
            r.setData(map);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PutMapping("/updateIsAllLine")
    @ApiOperation(value = "修改近线服务", notes = "修改近线服务")
    public ResultT<DataClassDto> updateIsAllLine(DataClassDto dataClassDto) {
        ResultT<DataClassDto> resultT = new ResultT<>();
        dataClassDto = this.dataClassService.updateIsAllLine(dataClassDto);
        resultT.setData(dataClassDto);
        return resultT;
    }

    @GetMapping("/haveClassByDataId")
    @ApiOperation(value = "是否存在四级编码对应资料", notes = "是否存在四级编码对应资料")
    public ResultT<Boolean> haveClassByDataId(String dataId) {
        return this.dataClassService.haveClassByDataId(dataId);
    }

    @GetMapping("/getClassByTableId")
    @ApiOperation(value = "根据tableId查询相关资料", notes = "根据tableId查询相关资料")
    public ResultT getClassByTableId(String tableId) {
        return this.dataClassService.getClassByTableId(tableId);
    }

    @GetMapping("/getTableInfo")
    @ApiOperation(value = "根据存储编码查询关联表", notes = "根据存储编码查询关联表")
    public ResultT getTableInfo(String dataclassId) {
        ResultT resultT = new ResultT<>();
        List<Map<String, Object>> tableInfo = this.dataClassService.getTableInfo(dataclassId);
        resultT.setData(tableInfo);
        return resultT;
    }


    @GetMapping("/getPageDataclassInfo")
    @ApiOperation(value = "条件分页查询")
    @RequiresPermissions("dm:dataClass:getPageDataclassInfo")
    public ResultT<List> getPageDataclassInfo(DataclassVo dataclassVo,
                                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Map<String, Object> map = dataclassVo.getMap();
        PageForm<Map<String, Object>> pageForm = new PageForm<>(pageNum, pageSize, map);
        PageBean pageBean = this.dataClassService.getPageDataclassInfo(pageForm);
        return ResultT.success(pageBean.getPageData());
    }

    @ApiOperation(value = "逻辑删除（2）/恢复（0）")
    @RequiresPermissions("dm:dataClass:tombstone")
    @Log(title = "资料分类管理", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/tombstone")
    public ResultT tombstone(String dataClassId, String delFlag) {
        try {
            this.dataClassService.tombstone(dataClassId, delFlag);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @GetMapping("/fileDirsList")
    @ApiOperation(value = "索引库目录条件分页查询", notes = "索引库目录条件分页查询")
    public ResultT<PageBean> getFileDirsInfo(@RequestParam(value = "className", defaultValue = "") String className,
                                             @RequestParam(value = "dataId", defaultValue = "") String dataId,
                                             @RequestParam(value = "pId", defaultValue = "") String pId,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ResultT<PageBean> resultT = new ResultT<>();
        Map<String,String> m = new HashMap<>();
        m.put("className",className);
        m.put("dataId",dataId);
        m.put("pId",pId);
        PageForm<Map<String,String>> pageForm = new PageForm<>(pageNum, pageSize, m);
        PageBean pageBean = this.dataClassService.getFileDirsInfo(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }
}
