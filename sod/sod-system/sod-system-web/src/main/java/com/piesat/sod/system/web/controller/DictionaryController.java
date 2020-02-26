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

import com.github.pagehelper.util.StringUtil;
import com.piesat.sod.system.rpc.api.DictionaryService;
import com.piesat.sod.system.rpc.dto.DictionaryDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/** 字段与索引类型管理
*@description
*@author wlg
*@date 2020年1月14日下午6:58:57
*
*/
@RestController
@RequestMapping(value="/restApi/dicmgn")
@Api(value="字段与索引类型管理Controller",tags = {"字段与索引类型管理接口"})
@Slf4j
public class DictionaryController {
	
	@Autowired
	private DictionaryService dictionaryService;
	
	/**
	 *  字段与索引类型查询
	 * @description 
	 * @author wlg
	 * @date 2020年1月14日下午7:03:18
	 * @param dictionaryDto
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequiresPermissions("restApi:dicmgn:page")
	@ApiOperation(value="字段与索引类型查询接口",notes="字段与索引类型查询接口")
	@GetMapping(value="/page")
	public ResultT getPage(DictionaryDto dictionaryDto,int pageNum,int pageSize) {
		try {
			PageForm<DictionaryDto> pageForm = new PageForm<>(pageNum,pageSize,dictionaryDto);
			PageBean page = dictionaryService.findPageData(pageForm);
			return ResultT.success(page);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	
	/**
	 *  获取字典分组
	 * @description 
	 * @author wlg
	 * @date 2020年1月15日上午8:53:02
	 * @param menu
	 * @return
	 */
	@RequiresPermissions("restApi:dicmgn:findMenu")
	@ApiOperation(value="获取字段与索引类型分组接口",notes="获取字段与索引类型分组接口")
	@GetMapping(value="/findMenu")
	public ResultT findMenu(String menu) {
		try {
			if(StringUtil.isEmpty(menu)) return ResultT.failed("参数不能为空");
			
			List<DictionaryDto> data = dictionaryService.findMenu(menu);
			return ResultT.success(data);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	
	/**
	 *  新增字典分组
	 * @description 
	 * @author wlg
	 * @date 2020年1月15日上午10:00:55
	 * @param dictionaryDto
	 * @return
	 */
	@ApiOperation(value="新增字段与索引类型分组",notes="新增字段与索引类型分组")
	@PostMapping(value="/addType")
	@RequiresPermissions("restApi:dicmgn:addType")
    @Log(title = "字段与索引类型管理", businessType = BusinessType.INSERT)
	public ResultT addType(DictionaryDto dictionaryDto) {
		try {
			dictionaryService.addType(dictionaryDto);
			return ResultT.success("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}

	/**
	 *  主键查询
	 * @description 
	 * @author wlg
	 * @date 2020年1月15日上午10:29:25
	 * @param id
	 * @return
	 */
	@ApiOperation(value="根据id查询接口",notes="根据id查询接口")
	@GetMapping(value="/findById")
	@RequiresPermissions("restApi:dicmgn:findById")
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
	
	/**
	 * 根据id编辑数据
	 * @description 
	 * @author wlg
	 * @date 2020年1月15日上午10:55:34
	 * @param dictionaryDto
	 * @return
	 */
	@ApiOperation(value="编辑接口",notes="编辑接口")
	@PutMapping(value="/updateById")
	@RequiresPermissions("restApi:dicmgn:updateById")
    @Log(title = "字段与索引类型管理", businessType = BusinessType.UPDATE)
	public ResultT updateById(DictionaryDto dictionaryDto) {
		try {
			DictionaryDto old = dictionaryService.findById(dictionaryDto.getId());
			if(null == old) return ResultT.failed("当前数据不存在或者已删除");
			
			dictionaryService.updateDictionary(dictionaryDto);
			return ResultT.success("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	/**
	 *  删除接口
	 * @description 
	 * @author wlg
	 * @date 2020年1月15日上午11:47:35
	 * @param ids
	 * @return
	 */
	@ApiOperation(value="删除接口",notes="删除接口,id逗号拼接")
	@DeleteMapping(value="/deleteByIds")
	@RequiresPermissions("restApi:dicmgn:deleteByIds")
    @Log(title = "字段与索引类型管理", businessType = BusinessType.DELETE)
	public ResultT deleteByIds(String ids) {
		try {
			
			if(StringUtil.isEmpty(ids)) return ResultT.failed("参数不能为空");
			
			dictionaryService.deleteByIds(ids);
			return ResultT.success("操作成功");
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	
	/**
	 *  根据type 查询
	 * @description 
	 * @author wlg
	 * @date 2020年2月6日下午5:59:59
	 * @param type
	 * @return
	 */
	@ApiOperation(value="根据type查询字段与索引类型数据",notes="根据type查询字段与索引类型数据")
	@GetMapping(value="/findByType")
	@RequiresPermissions("restApi:dicmgn:findByType")
	public ResultT findByType(String type) {
		log.info(">>>>>>>>根据type查询字典数据");
		if(StringUtil.isEmpty(type)) return ResultT.failed("参数不能为空");
		try {
			List<DictionaryDto> data = dictionaryService.findByType(type);
			return ResultT.success(data);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	/**
	 *  获取所有数据库类型
	 * @description 
	 * @author wlg
	 * @date 2020年2月7日下午5:18:52
	 * @return
	 */
	@ApiOperation(value="获取数据库类型",notes="获取数据库类型")
	@GetMapping(value="/queryAllByTypeAndFlag")
	@RequiresPermissions("restApi:dicmgn:findById")
	public ResultT queryAllByTypeAndFlag() {
		log.info(">>>>>>>获取数据库类型");
		try {
			List<DictionaryDto> data = dictionaryService.queryAllByTypeAndFlag();
			return ResultT.success(data);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	
}
