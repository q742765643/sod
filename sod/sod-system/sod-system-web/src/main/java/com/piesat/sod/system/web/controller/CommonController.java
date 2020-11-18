package com.piesat.sod.system.web.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.piesat.common.constant.FileTypesConstant;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

    @ApiOperation(value="文件预览",notes="文件预览")
    @GetMapping(value="/api/com/previewPdf")
    public ResponseEntity<InputStreamResource> previewPDF(HttpServletRequest request){
        try {
            String filePath = request.getParameter("filePath");
            FileSystemResource file = new FileSystemResource(filePath);


            File resultFile = file.getFile();
            String suffix = filePath.substring(filePath.indexOf(".")+1);

            boolean b = Arrays.stream(FileTypesConstant.PORTAL_OFFICE_TYPES).anyMatch(e -> e.equalsIgnoreCase(suffix));
            if(b){
                //resultFile = office2pdf(file.getFile());
            }

            FileSystemResource previewFile = new FileSystemResource(resultFile);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            if(Arrays.stream(FileTypesConstant.PORTAL_IMG_TYPES).anyMatch(e -> e.equalsIgnoreCase(suffix))){
                headers.add("Content-Type", "image/"+suffix.substring(1));
            }
            headers.add("Content-Disposition", String.format("inline; filename=\"%s\"", new String( previewFile.getFilename().getBytes("UTF8"), "ISO8859-1")));
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(previewFile.contentLength())
//	                .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new InputStreamResource(previewFile.getInputStream()));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
