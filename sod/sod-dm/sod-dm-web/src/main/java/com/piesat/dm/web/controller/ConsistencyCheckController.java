package com.piesat.dm.web.controller;

import com.piesat.common.utils.DateUtils;
import com.piesat.common.utils.FileUploadUtils;
import com.piesat.dm.entity.ConsistencyCheckHistoryEntity;
import com.piesat.dm.rpc.api.ConsistencyCheckHistoryService;
import com.piesat.dm.rpc.api.ConsistencyCheckService;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.dto.ConsistencyCheckDto;
import com.piesat.dm.rpc.dto.ConsistencyCheckHistoryDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

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

    @Autowired
    private ConsistencyCheckHistoryService consistencyCheckHistoryService;

    @Autowired
    private DatabaseService databaseService;

    @Value("${serverfile.dfcheck}")
    private String fileAddress;

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean> list(@RequestParam(value = "database_name", defaultValue = "") String databaseName,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ResultT<PageBean> resultT = new ResultT<>();
        ConsistencyCheckDto consistencyCheckDto = new ConsistencyCheckDto();
        consistencyCheckDto.setDatabaseName(databaseName);
        PageForm<ConsistencyCheckDto> pageForm = new PageForm<>(pageNum, pageSize, consistencyCheckDto);
        PageBean pageBean = consistencyCheckService.selectPageList(pageForm);
        resultT.setData(pageBean);

        return resultT;
    }

    @GetMapping("/historyList")
    @ApiOperation(value = "历史报告列表", notes = "历史报告列表")
    public ResultT<PageBean> historyList(ConsistencyCheckHistoryDto consistencyCheckHistoryDto,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<ConsistencyCheckHistoryDto> pageForm = new PageForm<>(pageNum, pageSize, consistencyCheckHistoryDto);
        PageBean pageBean = consistencyCheckHistoryService.selectPageList(pageForm);
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

    @DeleteMapping("/deleteByIds/{ids}")
    @ApiOperation(value = "批量删除", notes = "批量删除")
    public ResultT<String> deleteByIds(@PathVariable String[] ids)
    {
        ResultT<String> resultT=new ResultT<>();
        List<String> list=new ArrayList();
        if(ids.length>0){
            list= Arrays.asList(ids);
            this.consistencyCheckService.deleteRecordByIds(list);
        }
        return resultT;
    }

    @DeleteMapping("/deleteHistoryById/{id}")
    @ApiOperation(value = "根据id删除历史记录", notes = "根据id删除历史记录")
    public ResultT<String> deleteHistoryById(@PathVariable String id)
    {
        ResultT<String> resultT=new ResultT<>();
        this.consistencyCheckHistoryService.deleteById(id);
        return resultT;
    }

    @PostMapping(value = "/downHistoryDfcheckFile")
    @ApiOperation(value = "历史报告下载", notes = "历史报告下载")
    public void downHistoryDfcheckFile(HttpServletResponse response, String filename,String file_directory){
        try {
            filename = URLDecoder.decode(filename,"UTF-8");
            file_directory = URLDecoder.decode(file_directory,"UTF-8");
            FileInputStream fis = new FileInputStream(file_directory+"/"+filename);
            response.reset();
            response.setContentType("bin");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename,"UTF-8"));
            // 循环取出流中的数据
            byte[] b = new byte[200];
            int len;

            while ((len = fis.read(b)) > 0){
                response.getOutputStream().write(b, 0, len);
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @PostMapping(value = "/downloadDfcheckFile")
    @ApiOperation(value = "生成差异报告", notes = "生成差异报告")
    public void downloadDfcheckFile(@RequestBody DatabaseDto databaseDto, HttpServletResponse response){
         databaseDto = databaseService.getDotById(databaseDto.getId());

        String fileName = databaseDto.getDatabaseDefine().getDatabaseName()+"_"+databaseDto.getDatabaseName()+"_"+databaseDto.getSchemaName()+"_"
                +"元数据差异"+"_"+ DateUtils.dateTimeNow("yyyyMMddhhMMss")+".xls";


        Map<String, List<List<String>>> compileResults = this.consistencyCheckService.downloadDfcheckFile(databaseDto.getId());

        // 创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();

        // 创建单元格，并设置值表头 设置表头左对齐
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);// 创建一个左对齐格式
        style.setWrapText(true);//自动换行

        sheetOne(wb,style,compileResults.get("columnResult"));

        sheetTwo(wb,style,compileResults.get("indexResult"));

        sheetThree(wb,style,compileResults.get("shardingResult"));

        // 将文件存到指定位置
        String filePath = fileAddress +"/"+ fileName;
        FileUploadUtils.uploadFileExl(wb,filePath);

        //保存差异检查历史表
        //判断同一时刻的文件是否存在
        List<ConsistencyCheckHistoryDto> historys = consistencyCheckHistoryService.findHistoryByDatabaseIdAndFileName(databaseDto.getId(), fileName);
        if(historys != null && historys.size()>0){
            ConsistencyCheckHistoryDto consistencyCheckHistoryDto = historys.get(0);
            //更新时间
            consistencyCheckHistoryService.saveDto(consistencyCheckHistoryDto);
        }else{
            ConsistencyCheckHistoryDto consistencyCheckHistoryDto = new ConsistencyCheckHistoryDto();
            consistencyCheckHistoryDto.setDatabaseId(databaseDto.getId());
            consistencyCheckHistoryDto.setFileName(fileName);
            consistencyCheckHistoryDto.setFileDirectory(fileAddress);
            consistencyCheckHistoryService.saveDto(consistencyCheckHistoryDto);
        }

        //下载
        try {
            response.setContentType("application/octet-stream;charset=UTF-8" );
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Access-Control-Expose-Headers","content-disposition");
            response.addHeader("content-disposition","attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @PostMapping(value = "/updateEleInfo")
    @ApiOperation(value = "同步字段信息", notes = "同步字段信息")
    public ResultT updateEleInfo(@RequestBody DatabaseDto databaseDto){
        try {
            this.consistencyCheckService.updateEleInfo(databaseDto.getId());
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

}
