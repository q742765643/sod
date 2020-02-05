package com.piesat.sod.system.web.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piesat.sod.system.rpc.api.SqlTemplateService;
import com.piesat.sod.system.rpc.dto.SqlTemplateDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *  sql 模板管理
 * @author adminis
 *
 */

@RestController
@RequestMapping(value="/restApi/sqlTemplate")
@Api(value="SQL模板管理Controller",tags= {"sql模板管理相关接口"})
public class SqlTemplateController {

	@Autowired
	private SqlTemplateService sqlTemplateService;
	
	/**
	 *  新增sql模板
	 * @param sqlTemplateDto
	 * @return
	 */
	@ApiOperation(value="新增sql模板",notes="新增sql模板")
	@PostMapping(value="/add")
	@RequiresPermissions("restApi:sqlTemplate:add")
	@Log(title = "SQL模板管理", businessType = BusinessType.INSERT)
	public ResultT addSqlTemplate(SqlTemplateDto sqlTemplateDto) {
		try {
			sqlTemplateService.add(sqlTemplateDto);
			return ResultT.success("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	/**
	 *  删除sql模板
	 * @param id
	 * @return
	 */
	@ApiOperation(value="删除sql模板",notes="删除sql模板")
	@DeleteMapping(value="/del")
	@RequiresPermissions("restApi:sqlTemplate:del")
	@Log(title = "SQL模板管理", businessType = BusinessType.INSERT)
	public ResultT delSqlTemplate(String id) {
		try {
			sqlTemplateService.del(id);
			return ResultT.success("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
}
