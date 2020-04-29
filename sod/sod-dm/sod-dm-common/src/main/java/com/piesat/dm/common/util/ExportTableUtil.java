package com.piesat.dm.common.util;

import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/12 16:05
 */
public class ExportTableUtil {

    public static void exportTable(HttpServletRequest request, HttpServletResponse response, List<String> headList, List<List<String>> list, String name){
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
//            response.setContentType("application/x-download");
            response.setContentType("application/octet-stream;charset=UTF-8");

            String fileName = name + ".xlsx";
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            // 第一步：定义一个新的工作簿
            System.setProperty("javax.xml.parsers.DocumentBuilderFactory","com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
            XSSFWorkbook wb = new XSSFWorkbook();
            // 第二步：创建一个Sheet页
            XSSFSheet sheet = wb.createSheet(fileName);
            sheet.setDefaultRowHeight((short) (2 * 256));//设置行高
            sheet.setColumnWidth(0, 5500);//设置列宽
            sheet.setColumnWidth(1,5500);
            sheet.setColumnWidth(2,5500);
            sheet.setColumnWidth(3,5500);
            sheet.setColumnWidth(4,5500);
            sheet.setColumnWidth(5,5500);
            sheet.setColumnWidth(6,5500);
            sheet.setColumnWidth(7,5500);
            sheet.setColumnWidth(8,5500);
            sheet.setColumnWidth(9,5500);
            sheet.setColumnWidth(10,5500);
            sheet.setColumnWidth(11,5500);
            XSSFFont font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 16);
            XSSFRow row = sheet.createRow(0);
            XSSFCell cell = row.createCell(0);
            for (int i = 0; i < headList.size(); i++) {
                cell = row.createCell(i);
                cell.setCellValue(headList.get(i));
            }
            XSSFRow row1;
            XSSFCell cells;
            for (int i = 0; i < list.size(); i++) {
                // 第三步：在这个sheet页里创建一行
                row1 = sheet.createRow(i+1);
                // 第四步：在该行创建一个单元格

                for (int i1 = 0; i1 < headList.size(); i1++) {
                    cells = row1.createCell(i1);
                    cells.setCellValue(list.get(i).get(i1));
                }
            }
            OutputStream out = response.getOutputStream();
            wb.write(out);
            out.close();
            wb.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static byte[] exportTableNew(HttpServletRequest request, HttpServletResponse response, List<String> headList, List<List<String>> list, String name){
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
//            response.setContentType("application/x-download");
            response.setContentType("application/octet-stream;charset=UTF-8");

            String fileName = name + ".xlsx";
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            // 第一步：定义一个新的工作簿
            System.setProperty("javax.xml.parsers.DocumentBuilderFactory","com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
            XSSFWorkbook wb = new XSSFWorkbook();
            // 第二步：创建一个Sheet页
            XSSFSheet sheet = wb.createSheet(fileName);
            sheet.setDefaultRowHeight((short) (2 * 256));//设置行高
            sheet.setColumnWidth(0, 5500);//设置列宽
            sheet.setColumnWidth(1,5500);
            sheet.setColumnWidth(2,5500);
            sheet.setColumnWidth(3,5500);
            sheet.setColumnWidth(4,5500);
            sheet.setColumnWidth(5,5500);
            sheet.setColumnWidth(6,5500);
            sheet.setColumnWidth(7,5500);
            sheet.setColumnWidth(8,5500);
            sheet.setColumnWidth(9,5500);
            sheet.setColumnWidth(10,5500);
            sheet.setColumnWidth(11,5500);
            XSSFFont font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 16);
            XSSFRow row = sheet.createRow(0);
            XSSFCell cell = row.createCell(0);
            for (int i = 0; i < headList.size(); i++) {
                cell = row.createCell(i);
                cell.setCellValue(headList.get(i));
            }
            XSSFRow row1;
            XSSFCell cells;
            for (int i = 0; i < list.size(); i++) {
                // 第三步：在这个sheet页里创建一行
                row1 = sheet.createRow(i+1);
                // 第四步：在该行创建一个单元格

                for (int i1 = 0; i1 < headList.size(); i1++) {
                    cells = row1.createCell(i1);
                    cells.setCellValue(list.get(i).get(i1));
                }
            }

//            OutputStream out = response.getOutputStream();
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            wb.write(byteOut);
            byte[] result = byteOut.toByteArray();
            response.addHeader("Content-Length", "" + result.length);
            byteOut.close();
            wb.close();

            return result;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
