package com.piesat.sod.system.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.piesat.sod.system.rpc.api.DbFileService;
import com.piesat.sod.system.rpc.dto.DbFileDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
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
	//@RequiresPermissions("api:dbfile:getpage")
	public ResultT getPageData(DbFileDto dbFileDto,int pageNum, int pageSize) {
		try {
			PageForm<DbFileDto> pageForm = new PageForm<>(pageNum,pageSize,dbFileDto);
			PageBean page = dbFileService.findPageData(pageForm);
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
	//@RequiresPermissions("api:dbfile:upload")
    @Log(title = "数据库文档管理", businessType = BusinessType.INSERT)
	public ResultT uploadFile(MultipartHttpServletRequest request) {
		try {
			ResultT save = dbFileService.save(request);
			return save;
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
	@DeleteMapping(value="/api/dbfile/deleteByIds")
	//@RequiresPermissions("api:dbfile:deleteByIds")
    @Log(title = "数据库文档管理", businessType = BusinessType.DELETE)
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

	@ApiOperation(value="静态文件下载接口",notes="静态文件下载接口")
	//@RequiresPermissions("api:dbfile:downloadFile")
	@GetMapping("/api/dbfile/downloadFile")
	public void downloadFile(HttpServletResponse response, String name){
		this.dbFileService.downloadFile(response,name);
	}

}
