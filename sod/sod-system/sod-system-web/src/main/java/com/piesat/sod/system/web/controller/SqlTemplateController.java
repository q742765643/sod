package com.piesat.sod.system.web.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.util.StringUtil;
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
@RequestMapping(value="/sqlTemplate")
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
	@Log(title = "SQL模板管理", businessType = BusinessType.DELETE)
	public ResultT delSqlTemplate(String ids) {
		try {
			if(StringUtils.isEmpty(ids)) return ResultT.failed("参数不能为空");
			sqlTemplateService.delByIds(ids);
			return ResultT.success("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	
	/**
	 *   根据id查询
	 * @param id
	 * @return
	 */
	@ApiOperation(value="根据Id查询sql模板",notes="根据Id查询sql模板")
	@GetMapping(value="/findByPk")
	@RequiresPermissions("restApi:sqlTemplate:findByPk")
	public ResultT findByPk(String id) {
		try {
			if(StringUtils.isEmpty(id)) return ResultT.failed("参数不能为空");
			SqlTemplateDto std =  sqlTemplateService.findByPk(id);
			return ResultT.success(std);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	
	/**
	 *  编辑sql模板
	 * @param sqlTemplateDto
	 * @return
	 */
	@ApiOperation(value="编辑sql模板",notes="编辑sql模板")
	@PutMapping(value="/edit")
	@RequiresPermissions("restApi:sqlTemplate:edit")
	@Log(title = "SQL模板管理", businessType = BusinessType.UPDATE)
	public ResultT edit(SqlTemplateDto sqlTemplateDto) {
		try {
			sqlTemplateService.edit(sqlTemplateDto);
			return ResultT.success("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	/**
	 * 查询所有sql模板
	 * @return
	 */
	
	@ApiOperation(value="查询所有sql模板",notes="查询所有sql模板")
	@GetMapping(value="/findAll")
	@RequiresPermissions("restApi:sqlTemplate:findAll")
	public ResultT findAll() {
		try {
			List<SqlTemplateDto> dtoList = sqlTemplateService.findAll();
			return ResultT.success(dtoList);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	/**
	 * checkSqlTemplate
	 * @param databaseServer
	 * @return
	 */
	@ApiOperation(value="checkSqlTemplate",notes="checkSqlTemplate")
	@GetMapping(value="/checkSqlTemplate")
	@RequiresPermissions("restApi:sqlTemplate:checkSqlTemplate")
	public ResultT checkSqlTemplate(String databaseServer) {
		try {
			if(StringUtil.isEmpty(databaseServer)) return ResultT.failed("参数不允许为空");
			List<SqlTemplateDto> dtoList = sqlTemplateService.checkSqlTemplate(databaseServer);
			return ResultT.success(dtoList);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	
	
}
