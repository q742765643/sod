package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.ConsistencyCheckService;
import com.piesat.dm.rpc.dto.ConsistencyCheckDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/14 10:14
 */
@RestController
@RequestMapping("/dm/consistencyCheck")
@Api(value = "一致性检查controller", tags = {"一致性检查"})
public class ConsistencyCheckController {

    @Autowired
    private ConsistencyCheckService consistencyCheckService;

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean> list(ConsistencyCheckDto consistencyCheckDto, int pageNum, int pageSize) {
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<ConsistencyCheckDto> pageForm = new PageForm<>(pageNum, pageSize, consistencyCheckDto);
        PageBean pageBean = consistencyCheckService.selectPageList(pageForm);
        resultT.setData(pageBean);

        return resultT;
    }


    /**
     *  添加
     * @param consistencyCheckDto
     * @return
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "添加", notes = "添加")
    public ResultT save(@RequestBody ConsistencyCheckDto consistencyCheckDto) {
        try {
            ConsistencyCheckDto save = this.consistencyCheckService.saveDto(consistencyCheckDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @DeleteMapping("/deleteById")
    @ApiOperation(value = "根据id删除", notes = "根据id删除")
    public ResultT<String> remove(String id)
    {
        ResultT<String> resultT=new ResultT<>();
        this.consistencyCheckService.deleteById(id);
        return resultT;
    }

    @PostMapping(value = "/downloadDfcheckFile")
    @ApiOperation(value = "生成差异报告", notes = "生成差异报告")
    public void downloadDfcheckFile(HttpServletRequest request, HttpServletResponse response){
       String databaseId =  request.getParameter("databaseId");
       Map<String, List<List<String>>> compileResults = this.consistencyCheckService.downloadDfcheckFile(databaseId);

        // 创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();

        // 创建单元格，并设置值表头 设置表头左对齐
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);// 创建一个左对齐格式
        style.setWrapText(true);//自动换行

        sheetOne(wb,style,compileResults.get("columnResult"));

        sheetTwo(wb,style,compileResults.get("indexResult"));

        sheetThree(wb,style,compileResults.get("shardingResult"));

    }

    public void sheetOne(HSSFWorkbook wb,HSSFCellStyle style,List<List<String>> compileResult){
        // 在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("元数据字段检查表");

        HSSFRow row = sheet.createRow((int) 0);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("物理库数据表名");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)0,(short)(10000));

        cell = row.createCell((short) 1);
        cell.setCellValue("存储编码");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)1,(short)(5000));

        cell = row.createCell((short) 2);
        cell.setCellValue("资料名称");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)2,(short)(10000));

        cell = row.createCell((short) 3);
        cell.setCellValue("存储元数据中该表是否存在");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)3,(short)(5000));

        cell = row.createCell((short) 4);
        cell.setCellValue("存储元数据多余的字段");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)4,(short)(5000));

        cell = row.createCell((short) 5);
        cell.setCellValue("存储元数据缺失的字段");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)5,(short)(5000));

        cell = row.createCell((short) 6);
        cell.setCellValue("存储元数据类型需要修改的字段");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)6,(short)(10000));

        cell = row.createCell((short) 7);
        cell.setCellValue("存储元数据精度需要修改的字段");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)7,(short)(10000));

        cell = row.createCell((short) 8);
        cell.setCellValue("存储元数据和物理库非空设置不一致的字段");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)8,(short)(10000));

        cell = row.createCell((short) 9);
        cell.setCellValue("存储元数据和物理库主键设置不一致的字段");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)9,(short)(10000));

        int num = 1;
        for(int i = 0; i < compileResult.size(); i++){
            List<String> compileResultOne = compileResult.get(i);
            row = sheet.createRow(num);
            num++;
            for(int j = 0;j < compileResultOne.size();j++){

                String aa = compileResultOne.get(j);
                if(aa.indexOf(";") != -1){
                    String[] lums = aa.split(";");
                    aa = "";
                    for(int jj = 0 ;jj<lums.length;jj++){
                        aa += lums[jj] + "\r\n";
                    }
                }

                cell = row.createCell((short)j);
                cell.setCellValue(aa);
                cell.setCellStyle(style);
            }
        }
    }
    public void sheetTwo(HSSFWorkbook wb,HSSFCellStyle style,List<List<String>> compileResult){
        // 在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("元数据索引检查表");

        HSSFRow row = sheet.createRow((int) 0);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("物理库数据表名");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)0,(short)(10000));

        cell = row.createCell((short) 1);
        cell.setCellValue("存储编码");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)1,(short)(5000));

        cell = row.createCell((short) 2);
        cell.setCellValue("资料名称");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)2,(short)(10000));

        cell = row.createCell((short) 3);
        cell.setCellValue("存储元数据中该表是否存在");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)3,(short)(5000));

        cell = row.createCell((short) 4);
        cell.setCellValue("存储元数据多余的索引");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)4,(short)(5000));

        cell = row.createCell((short) 5);
        cell.setCellValue("存储元数据缺失的索引");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)5,(short)(5000));

        cell = row.createCell((short) 6);
        cell.setCellValue("存储元数据需要修改的索引");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)6,(short)(5000));

        int num = 1;
        for(int i = 0; i < compileResult.size(); i++){
            List<String> compileResultOne = compileResult.get(i);
            row = sheet.createRow(num);
            num++;
            for(int j = 0;j < compileResultOne.size();j++){

                String aa = compileResultOne.get(j);
                if(aa.indexOf(";") != -1){
                    String[] lums = aa.split(";");
                    aa = "";
                    for(int jj = 0 ;jj<lums.length;jj++){
                        aa += lums[jj] + "\r\n";
                    }
                }

                cell = row.createCell((short)j);
                cell.setCellValue(aa);
                cell.setCellStyle(style);
            }
        }
    }

    public void sheetThree(HSSFWorkbook wb,HSSFCellStyle style,List<List<String>> compileResult){
        // 在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("元数据分库分表检查表");

        HSSFRow row = sheet.createRow((int) 0);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("物理库数据表名");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)0,(short)(10000));

        cell = row.createCell((short) 1);
        cell.setCellValue("存储编码");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)1,(short)(5000));

        cell = row.createCell((short) 2);
        cell.setCellValue("资料名称");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)2,(short)(10000));

        cell = row.createCell((short) 3);
        cell.setCellValue("存储元数据中该表是否存在");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)3,(short)(5000));

        cell = row.createCell((short) 4);
        cell.setCellValue("存储元数据多余的分库分表键");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)4,(short)(5000));

        cell = row.createCell((short) 5);
        cell.setCellValue("存储元数据缺失的分库分表键");
        cell.setCellStyle(style);
        sheet.setColumnWidth((short)5,(short)(5000));

        int num = 1;
        for(int i = 0;i<compileResult.size(); i++){
            List<String> compileResultOne = compileResult.get(i);
            row = sheet.createRow(num);
            num++;
            for(int j = 0;j < compileResultOne.size();j++){

                String aa = compileResultOne.get(j);
                if(aa.indexOf(";") != -1){
                    String[] lums = aa.split(";");
                    aa = "";
                    for(int jj = 0 ;jj<lums.length;jj++){
                        aa += lums[jj];
                    }
                }

                cell = row.createCell((short)j);
                cell.setCellValue(aa);
                cell.setCellStyle(style);
            }
        }
    }

}
