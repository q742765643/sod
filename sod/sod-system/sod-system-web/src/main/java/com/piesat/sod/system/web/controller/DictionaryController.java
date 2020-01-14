package com.piesat.sod.system.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piesat.sod.system.rpc.api.DictionaryService;
import com.piesat.sod.system.rpc.dto.DictionaryDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
*@description
*@author wlg
*@date 2020年1月14日下午6:58:57
*
*/
@RestController
@RequestMapping(value="/api/dictionary")
@Api(value="字典表管理Controller",tags = {"字典表管理接口"})
public class DictionaryController {
	
	@Autowired
	private DictionaryService dictionaryService;
	
	/**
	 *  字典表查询
	 * @description 
	 * @author wlg
	 * @date 2020年1月14日下午7:03:18
	 * @param dictionaryDto
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
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

}
