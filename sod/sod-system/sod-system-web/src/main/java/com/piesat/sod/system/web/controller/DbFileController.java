package com.piesat.sod.system.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.piesat.sod.system.rpc.api.DbFileService;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/** 数据库文档 控制器
*@description
*@author wlg
*@date 2019年11月20日下午2:30:17
*
*/
@RestController
@Api(value="数据库文档Controller",tags = {"数据库文档操作接口"})
public class DbFileController {
	
	
	@Autowired
	private DbFileService dbFileService;
	
	/**
	 * 获取数据库文件分页数据接口
	 * @description 
	 * @author wlg
	 * @date 2019年11月20日下午3:25:52
	 * @param request
	 * @param pageForm
	 * @return
	 */
	@ApiOperation(value="获取数据库文件分页数据接口",notes="获取数据库文件分页数据接口")
	@GetMapping(value="/api/dbfile/getpage")
	public ResultT getPageData(HttpServletRequest request,PageForm pageForm) {
		try {
			Map<String,Object> param = new HashMap<>();
			param.put("fileType", request.getParameter("fileType"));
			param.put("fileName", request.getParameter("fileName"));
			param.put("fileSuffix", request.getParameter("fileSuffix"));
			param.put("fileStartDate", request.getParameter("fileStartDate"));
			param.put("fileEndDate", request.getParameter("fileEndDate"));
			param.put("field", request.getParameter("field"));
			param.put("order", request.getParameter("order"));
			PageBean page = dbFileService.findPageData(param, pageForm);
			return ResultT.success(page);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	
	/**
	 *  上传文件
	 * @description 
	 * @author wlg
	 * @date 2019年11月21日上午11:12:41
	 * @param request
	 * @return
	 */
	@ApiOperation(value="数据库文档上传文件接口",notes="数据库文档上传文件接口")
	@PostMapping(value="/api/dbfile/upload")
	public ResultT uploadFile(MultipartHttpServletRequest request) {
		try {
			dbFileService.save(request);
			return ResultT.success();
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	/**
	 *  数据库文档删除
	 * @description 
	 * @author wlg
	 * @date 2019年12月23日上午8:28:15
	 * @param request
	 * @return
	 */
	@ApiOperation(value="数据库文档删除接口",notes="数据库文档删除接口")
	@GetMapping(value="/api/dbfile/deleteByIds")
	public ResultT deleteByIds(HttpServletRequest request){
		String ids = request.getParameter("ids");
		try {
			dbFileService.deleteByIds(ids);
			return ResultT.success();
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
		
	}

}
