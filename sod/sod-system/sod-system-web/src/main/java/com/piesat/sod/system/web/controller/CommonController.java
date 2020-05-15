package com.piesat.sod.system.web.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/** 通用接口
*@description
*@author wlg
*@date 2019年12月20日下午4:33:22
*
*/
@Controller
@Api(value="通用接口Controller",tags = {"通用接口"})
@Slf4j
public class CommonController {

	/**
	 * 根据文件路径下载文件
	 * @description
	 * @author wlg
	 * @date 2019年12月20日下午4:45:44
	 * @param request
	 * @param response
	 */
	@ApiOperation(value="根据文件路径下载文件",notes="根据文件路径下载文件")
    //@RequiresPermissions("api:com:downloadByPath")//不要加权限
	@GetMapping(value="/api/com/downloadByPath")
	public void downloadByPath(HttpServletRequest request, HttpServletResponse response) {
		String fullPath = request.getParameter("filePath");
		File file = new File(fullPath);
		String fileName = file.getName();

		if(file.exists()){
            try {

                String userAgent = request.getHeader("User-Agent");
                if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                    //IE浏览器处理
                    fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
                } else {
                    fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
                    // 非IE浏览器的处理：
//                    fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
                }
                // 以流的形式下载文件。
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                // 清空response
                //response.reset();
                // 设置response的Header
                response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName));
                response.addHeader("Content-Length", "" + file.length());
                OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                outputStream.write(buffer);
                outputStream.flush();
                outputStream.close();
            }catch (Exception ex) {
            	log.error("下载文件出错:" + ex);
            	ex.printStackTrace();
            }
        }else{
            try {
                response.setContentType("text/html; charset=UTF-8"); //转码
                PrintWriter out = response.getWriter();
                out.flush();
                out.println("<script defer='defer' type='text/javascript'>");
                out.println("alert('文件不存在或已经被删除！');");
//                out.println("window.location='/AnnualStatistics/downloadList';");
                out.println("</script>");
            } catch (Exception e) {
            	log.error("下载文件出错:" + e);
            	e.printStackTrace();
            }
        }
	}
}
