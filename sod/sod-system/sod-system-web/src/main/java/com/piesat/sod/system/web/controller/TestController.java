package com.piesat.sod.system.web.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.util.StringUtil;
import com.piesat.sod.system.rpc.api.DictionaryService;
import com.piesat.sod.system.rpc.dto.DictionaryDto;
import com.piesat.util.ResultT;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/** 测试
*@description
*@author wlg
*@date 2020年1月16日下午3:03:04
*
*/
@RestController
@RequestMapping(value="/dictionary")
@Api(value="测试Controller",tags = {"测试接口"})
public class TestController {
	
	@Autowired
	private DictionaryService dictionaryService;
	/**
	 *  测试
	 * @description 
	 * @author wlg
	 * @date 2020年1月16日下午3:05:07
	 * @return
	 */
	@RequiresPermissions("test:getApi")
	@ApiOperation(value="字段与索引类型查询接口",notes="字段与索引类型查询接口")
	@GetMapping(value="/getApi")
	public ResultT getApi() {
		return ResultT.success("success");
	}
	
	
	
	@RequiresPermissions("dictionary:findById")
//  @Log(title = "字段与索引类型管理", businessType = BusinessType.INSERT)
	@ApiOperation(value="主键查询",notes="根据id查询接口")
	@GetMapping(value="/findById")
	public ResultT findById(String id) {
		try {
			if(StringUtil.isEmpty(id)) return ResultT.failed("参数不能为空");
			
			DictionaryDto data = dictionaryService.findById(id);
			return ResultT.success(data);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}

}
