package com.piesat.sod.system.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piesat.sod.system.rpc.api.IndexService;
import com.piesat.util.ResultT;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/** 首页
*@description
*@author wlg
*@date 2020年2月19日下午3:22:50
*
*/
@RestController
@RequestMapping(value="/index")
@Api(value="首页查询Controller",tags= {"首页数据查询接口"})
@Slf4j
public class IndexController {

	@Autowired
	private IndexService indexService;
	
	/**
	 *  获取已办待办
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午4:44:36
	 * @return
	 */
	@RequiresPermissions("restApi:index:findUndoCount")
	@ApiOperation(value="获取已办待办",notes="获取已办待办")
	@GetMapping(value="/findUndoCount")
	public ResultT findUndoCount() {
		log.info(">>>>>>>获取首页已办待办");
		try {
			Map<String,Object> data = new HashMap<>();
			//新增资料
			Map<String,Object> xzzl = indexService.findNewDataCount();
			data.put("xzzl", xzzl);
			//数据授权
			Map<String,Object> sjsq = indexService.findDataAuthorCount();
			data.put("sjsq", sjsq);
			//数据库账户
			Map<String,Object> sjkzh = indexService.findUpAccCount();
			data.put("sjkzh", sjkzh);
			//业务专题库
			Map<String,Object> ywztk = indexService.findSpecialDBCount();
			data.put("ywztk", ywztk);
			//云数据库
			Map<String,Object> ysjk = indexService.findCloudDBCount();
			data.put("ysjk", ysjk);
			
			return ResultT.success(data);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	/**
	 *  获取资料种数统计
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午5:11:12
	 * @return
	 */
	@RequiresPermissions("restApi:index:findDataMonthCount")
	@ApiOperation(value="获取最近12个月的资料数量统计",notes="获取最近12个月的资料数量统计")
	@GetMapping(value="/findDataMonthCount")
	public ResultT findDataMonthCount() {
		log.info(">>>>> 获取首页最近12个月的资料数量统计");
		try {
			Map<String,Object> data = indexService.findDataMonthCount();
			return ResultT.success(data);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	/**
	 *  获取文件列表
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午5:58:06
	 * @return
	 */
	@RequiresPermissions("restApi:index:findFileList")
	@ApiOperation(value="获取文件列表",notes="获取文件列表")
	@GetMapping(value="/findFileList")
	public ResultT findFileList() {
		log.info(">>>>>> 获取首页文件列表");
		try {
			List<Map<String,Object>> data = indexService.findFileList();
			return ResultT.success(data);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	/**
	 *  按资料分类统计资料数量
	 * @description 
	 * @author wlg
	 * @date 2020年2月20日上午9:23:20
	 * @return
	 */
	@RequiresPermissions("restApi:index:findDataCount")
	@ApiOperation(value="获取资料分类统计数量",notes="获取资料分类统计数量")
	@GetMapping(value="/findDataCount")
	public ResultT findDataCount() {
		log.info(">>>>>> 获取首页资料分类统计数量");
		try {
			List<Map<String,Object>> data = indexService.findDataCount();
			return ResultT.success(data);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	/**
	 *  获取逻辑库资料数
	 * @description 
	 * @author wlg
	 * @date 2020年2月20日上午10:36:02
	 * @return
	 */
	@RequiresPermissions("restApi:index:findLogicCountData")
	@ApiOperation(value="统计逻辑库资料数",notes="统计逻辑库资料数")
	@GetMapping(value="/findLogicCountData")
	public ResultT findLogicCountData() {
		log.info(">>>>>> 统计首页逻辑库资料数");
		try {
			List<Map<String,Object>> data = indexService.findLogicCountData();
			return ResultT.success(data);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	/**
	 *  获取所有已审主题
	 * @description 
	 * @author wlg
	 * @date 2020年2月20日上午10:55:37
	 * @return
	 */
	@RequiresPermissions("restApi:index:findSpecialDbList")
	@ApiOperation(value="获取所有已审核专题库信息",notes="获取所有已审核的专题库信息")
	@GetMapping(value="/findSpecialDbList")
	public ResultT findSpecialDbList() {
		log.info("获取首页专题库信息");
		try {
			List<Map<String,Object>> data = indexService.findSpecialDbList();
			return ResultT.success(data);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	/**
	 *  获取逻辑库信息
	 * @description 
	 * @author wlg
	 * @date 2020年2月20日上午11:19:17
	 * @return
	 */
	@RequiresPermissions("restApi:index:findLogicInfo")
	@ApiOperation(value="获取数据库用途信息",notes="获取数据库用途信息信息")
	@GetMapping(value="/findLogicInfo")
	public ResultT findLogicInfo() {
		log.info(">>>>>>获取首页数据用途");
		try {
			List<Map<String,Object>> data = indexService.findLogicInfo();
			return ResultT.success(data);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	
}
