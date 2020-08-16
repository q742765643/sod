package com.piesat.dm.web.controller.datatable;

import com.piesat.dm.entity.datatable.CmccElementEntity;
import com.piesat.dm.entity.datatable.DatumTableEntity;
import com.piesat.dm.rpc.api.dataclass.DataLogicService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.api.datatable.TableColumnService;
import com.piesat.dm.rpc.dto.dataclass.DataLogicDto;
import com.piesat.dm.rpc.dto.datatable.DataTableDto;
import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import com.piesat.dm.rpc.dto.datatable.TableColumnList;
import com.piesat.dm.rpc.service.GrpcService;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 表字段信息
 *
 * @author cwh
 * @date 2019年 11月29日 10:27:31
 */
@Api(tags = "表字段管理")
@RequestMapping("/dm/tableColumn")
@RestController
public class TableColumnController {
    @Autowired
    private TableColumnService tableColumnService;
    @Autowired
    private DataTableService dataTableService;
    @Autowired
    private DataLogicService dataLogicService;
    @Autowired
    private GrpcService grpcService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:tableColumn:add")
    @Log(title = "表字段管理（新增字段信息）", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody TableColumnDto tableColumnDto) {
        try {
            TableColumnDto save = this.tableColumnService.saveDto(tableColumnDto);
            DataTableDto dataTable = dataTableService.getDotById(save.getTableId());
            DataLogicDto dataLogic = dataTable.getClassLogic();
            if (dataLogic.getIsComplete() == null || !dataLogic.getIsComplete()) {
                dataLogic.setIsComplete(true);
                dataLogicService.saveDto(dataLogic);
            }
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "批量添加")
    @PostMapping(value = "/saveColumns")
    public ResultT saveColumns(@RequestBody DataTableDto dataTableDto) {
        try {
            LinkedHashSet<TableColumnDto> columns = dataTableDto.getColumns();
            if (columns != null) {
                for (TableColumnDto tableColumnDto : columns) {
                    this.tableColumnService.saveDto(tableColumnDto);
                }
            }
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


    @PutMapping("/edit")
    @ApiOperation(value = "编辑", notes = "编辑")
    public ResultT<TableColumnDto> edit(@RequestBody TableColumnDto tableColumnDto) {
        ResultT<TableColumnDto> resultT = new ResultT<>();
        tableColumnDto = this.tableColumnService.updateDto(tableColumnDto);
        resultT.setData(tableColumnDto);
        return resultT;
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:tableColumn:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            TableColumnDto tableColumnDto = this.tableColumnService.getDotById(id);
            return ResultT.success(tableColumnDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:tableColumn:del")
    @Log(title = "表字段管理（删除字段信息）", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.tableColumnService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据多个id删除")
    @RequiresPermissions("dm:tableColumn:delIds")
    @Log(title = "表字段管理（批量删除字段信息）", businessType = BusinessType.DELETE)
    @PostMapping(value = "/delIds")
    public ResultT delByIds(String ids) {
        try {
            int i = this.tableColumnService.deleteByIdIn(Arrays.asList(ids.split(",")));
            return ResultT.success(i);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:tableColumn:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<TableColumnDto> all = this.tableColumnService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "批量添加")
    @RequiresPermissions("dm:tableColumn:saveList")
    @Log(title = "表字段管理（批量添加字段信息）", businessType = BusinessType.INSERT)
    @PostMapping(value = "/saveList")
    public ResultT saveDtoList(@RequestBody TableColumnList tableColumnList) {
        try {
            List<TableColumnDto> save = this.tableColumnService.saveDtoList(tableColumnList.getTableColumnList());
            DataTableDto dataTable = dataTableService.getDotById(save.get(0).getTableId());
            DataLogicDto dataLogic = dataTable.getClassLogic();
            if (dataLogic.getIsComplete() == null || !dataLogic.getIsComplete()) {
                dataLogic.setIsComplete(true);
                dataLogicService.saveDto(dataLogic);
            }
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据tableId查询")
    @RequiresPermissions("dm:tableColumn:findByTableId")
    @GetMapping(value = "/findByTableId")
    public ResultT findByTableId(String tableId) {
        try {
            List<TableColumnDto> all = this.tableColumnService.findByTableId(tableId);
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据tableId查询主键")
    @RequiresPermissions("dm:tableColumn:getPrimaryKey")
    @GetMapping(value = "/getPrimaryKey")
    public ResultT getPrimaryKey(String tableId) {
        try {
            List<TableColumnDto> all = this.tableColumnService.getPrimaryKey(tableId);
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "导出")
    @RequiresPermissions("dm:tableColumn:export")
    @GetMapping(value = "/export")
    public ResultT export(HttpServletResponse response, String tableId) {
        try {
            List<TableColumnDto> all = this.tableColumnService.getPrimaryKey(tableId);
            // 创建一个webbook，对应一个Excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            // 在webbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet sheet = wb.createSheet("数据信息表");

            String[] strArr = new String[]{"TRUE", "FALSE"};
            // 设置第一列的1-10行为下拉列表
            CellRangeAddressList regions = new CellRangeAddressList(1, 100, 8, 12);
            // 创建下拉列表数据
            DVConstraint constraint = DVConstraint.createExplicitListConstraint(strArr);
            // 绑定
            HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
            sheet.addValidationData(dataValidation);

            // 在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = sheet.createRow((int) 0);
            // 创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式

            HSSFCell cell = row.createCell((short) 0);
            cell.setCellValue("公共元数据字段");
            cell.setCellStyle(style);
            cell = row.createCell((short) 1);
            cell.setCellValue("字段名称");
            cell.setCellStyle(style);
            cell = row.createCell((short) 2);
            cell.setCellValue("服务名称");
            cell.setCellStyle(style);
            cell = row.createCell((short) 3);
            cell.setCellValue("中文简称");
            cell.setCellStyle(style);
            cell = row.createCell((short) 4);
            cell.setCellValue("数据类型");
            cell.setCellStyle(style);
            cell = row.createCell((short) 5);
            cell.setCellValue("数据精度");
            cell.setCellStyle(style);
            cell = row.createCell((short) 6);
            cell.setCellValue("要素单位（英文）");
            cell.setCellStyle(style);
            cell = row.createCell((short) 7);
            cell.setCellValue("要素单位（中文）");
            cell.setCellStyle(style);
            cell = row.createCell((short) 8);
            cell.setCellValue("是否可空");
            cell.setCellStyle(style);
            cell = row.createCell((short) 9);
            cell.setCellValue("是否可改");
            cell.setCellStyle(style);
            cell = row.createCell((short) 10);
            cell.setCellValue("是否显示");
            cell.setCellStyle(style);
            cell = row.createCell((short) 11);
            cell.setCellValue("是否主键");
            cell.setCellStyle(style);
            cell = row.createCell((short) 12);
            cell.setCellValue("中文描述");
            cell.setCellStyle(style);
            cell = row.createCell((short) 13);
            cell.setCellValue("是否管理字段");
            cell.setCellStyle(style);

            HSSFCellStyle cellStyle = wb.createCellStyle();
            HSSFDataFormat format = wb.createDataFormat();
            cellStyle.setDataFormat(format.getFormat("@"));
            for (int i = 0; i < all.size(); i++) {
                row = sheet.createRow((int) i + 1);
                TableColumnDto dataStructure = all.get(i);
                // 创建单元格，并设置值
                row.createCell((short) 0).setCellValue(dataStructure.getDbEleCode());
                row.createCell((short) 1).setCellValue(dataStructure.getCElementCode());
                row.createCell((short) 2).setCellValue(dataStructure.getUserEleCode());
                row.createCell((short) 3).setCellValue(dataStructure.getEleName());
                row.createCell((short) 4).setCellValue(dataStructure.getType());
                HSSFCell hc = row.createCell((short) 5);
                hc.setCellStyle(cellStyle);
                hc.setCellValue(dataStructure.getAccuracy());
                row.createCell((short) 6).setCellValue(dataStructure.getUnit());
                row.createCell((short) 7).setCellValue(dataStructure.getUnitCn());
                HSSFCell hc8 = row.createCell((short) 8);
                hc8.setCellStyle(cellStyle);
                hc8.setCellValue(dataStructure.getIsNull());
                row.createCell((short) 9).setCellValue(dataStructure.getIsUpdate());
                row.createCell((short) 10).setCellValue(dataStructure.getIsShow());
                row.createCell((short) 11).setCellValue(dataStructure.getIsPrimaryKey());
                row.createCell((short) 12).setCellValue(dataStructure.getNameCn());
                row.createCell((short) 13).setCellValue(dataStructure.getIsManager());
            }

            // 将文件存到指定位置
            try {
                response.setContentType("application/x-msdownload;");
                response.setHeader("Content-disposition",
                        "attachment;filename=" + new String("cimiss.xlsx".getBytes("utf-8"), "ISO8859-1"));
                wb.write(response.getOutputStream());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "同步服务编码")
    @RequiresPermissions("dm:tableColumn:syncSCode")
    @Log(title = "同步服务编码", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/syncSCode")
    public ResultT syncSCode(@RequestBody TableColumnList tableColumnList) {
        try {
            int un = this.grpcService.syncServiceName(tableColumnList.getTableColumnList());
            ResultT r = new ResultT();
            r.setData(un);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "查询公共元数据字段")
    @RequiresPermissions("dm:tableColumn:queryCmccElements")
    @GetMapping(value = "/queryCmccElements")
    public ResultT queryCmccElements(DatumTableEntity datumTableEntity) {
        try {
            List<CmccElementEntity> all = this.grpcService.queryCmccElements(datumTableEntity);
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询最大编号")
    @RequiresPermissions("dm:tableColumn:findMaxNum")
    @GetMapping(value = "/findMaxNum")
    public Integer findMaxNum(String tableId) {
        return this.tableColumnService.findMaxNum(tableId);
    }

}
