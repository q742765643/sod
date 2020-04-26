package com.piesat.sod.system.web.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.util.StringUtil;
import com.piesat.sod.system.rpc.api.ManageFieldService;
import com.piesat.sod.system.rpc.dto.ManageFieldDto;
import com.piesat.sod.system.rpc.dto.ManageGroupDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/** 管理字段管理
*@description
*@author wlg
*@date 2020年1月16日下午5:38:05
*
*/
@RestController
@RequestMapping(value="/managefield")
@Api(value="管理字段管理Controller",tags = {"管理字段管理页面相关接口"})
@Slf4j
public class ManageFiledController {

	@Autowired
	private ManageFieldService manageFieldService;

	/**
	 * 获取管理字段分组
	 * @description
	 * @author wlg
	 * @date 2020年1月16日下午5:42:27
	 * @return
	 */
	@RequiresPermissions("restApi:manageField:findAllManageGroup")
	@ApiOperation(value="获取所有管理字段分组",notes="管理字段分组获取接口")
	@GetMapping(value="/findAllManageGroup")
	public ResultT findAllManageGroup() {
		log.info(">>>>>>>>>>>>>开始获取管理字段分组");
		try {
			List<ManageGroupDto> data = manageFieldService.findAllManageGroup();
			return ResultT.success(data);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	/**
	 *  管理字段分组新增接口
	 * @description
	 * @author wlg
	 * @date 2020年1月17日下午3:27:59
	 * @param manageGroupDto
	 * @return
	 */
	@RequiresPermissions("restApi:manageField:addManageGroup")
	@Log(title = "管理字段管理", businessType = BusinessType.INSERT)
	@PostMapping(value="/addManageGroup")
	@ApiOperation(value="新增管理字段分组",notes="管理字段分组新增接口")
	public ResultT addManageGroup(ManageGroupDto manageGroupDto) {
		log.info(">>>>>>>>>>>>>>新增管理字段分组");
		try {
			manageFieldService.addManageGroup(manageGroupDto);
			return ResultT.success("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	/**
	 * 管理字段删除接口
	 * @description
	 * @author wlg
	 * @date 2020年1月17日下午3:32:45
	 * @param groupId
	 * @return
	 */
	@RequiresPermissions("restApi:manageField:delManageGroup")
	@Log(title = "管理字段管理", businessType = BusinessType.DELETE)
	@DeleteMapping(value="/delManageGroup")
	@ApiOperation(value="删除管理字段分组",notes="管理字段分组删除接口")
	public ResultT delManageGroup(String groupId) {
		log.info(">>>>>>>>>>>>>>管理字段分组删除");
		try {
			manageFieldService.delManageGroup(groupId);
			return ResultT.success("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}

	/**
	 *  主键查询管理字段分组
	 * @description
	 * @author wlg
	 * @date 2020年2月6日上午10:39:16
	 * @param groupId
	 * @return
	 */
	@RequiresPermissions("restApi:manageField:findManageGroupByPk")
	@GetMapping(value="/findManageGroupByPk")
	@ApiOperation(value="根据groupId查询管理字段分组",notes="查询单条管理字段分组接口")
	public ResultT findManageGroupByPk(String groupId) {
		log.info(">>>>>>>>主键查询管理字段分组");
		try {
			ManageGroupDto mgd= manageFieldService.findManageGroupByPk(groupId);
			return ResultT.success(mgd);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	/**
	 *  编辑管理字段分组
	 * @description
	 * @author wlg
	 * @date 2020年2月6日上午11:29:30
	 * @param manageGroupDto
	 * @return
	 */
	@RequiresPermissions("restApi:manageField:editManageGroup")
	@Log(title = "管理字段管理", businessType = BusinessType.UPDATE)
	@PutMapping(value="/editManageGroup")
	@ApiOperation(value="编辑管理字段分组",notes="编辑管理字段分组")
	public ResultT editManageGroup(ManageGroupDto manageGroupDto) {
		log.info(">>>>>>>> 编辑管理字段分组");
		try {
			manageFieldService.editManageGroup(manageGroupDto);
			return ResultT.success("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}


	/**
	 * 获取管理字段分页数据
	 * @description
	 * @author wlg
	 * @date 2020年1月19日上午9:21:40
	 * @param manageFieldDto
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequiresPermissions("restApi:manageField:page")
	@ApiOperation(value="管理字段分页查询接口",notes="管理字段查询接口")
	@GetMapping(value="/page")
	public ResultT findPageData(ManageFieldDto manageFieldDto,int pageNum,int pageSize) {
		log.info(">>>>>>>>>>>>获取管理字段分页数据");
		try {
			PageForm<ManageFieldDto> pageForm = new PageForm<>(pageNum,pageSize,manageFieldDto);
			PageBean page = manageFieldService.findPageData(pageForm);
			return ResultT.success(page);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}

	@RequiresPermissions("restApi:manageField:findData")
	@ApiOperation(value="管理字段查询接口",notes="管理字段接口")
	@GetMapping(value="/findData")
	public ResultT findData(ManageFieldDto manageFieldDto) {
		log.info(">>>>>>>>>>>>获取管理字段分页数据");
		try {
			List<ManageFieldDto> page = manageFieldService.findData(manageFieldDto);
			return ResultT.success(page);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}

	/**
	 *  添加管理字段
	 * @description
	 * @author wlg
	 * @date 2020年2月6日上午10:04:17
	 * @param manageFieldDto
	 * @return
	 */
	@RequiresPermissions("restApi:manageField:addManageField")
	@Log(title = "管理字段管理", businessType = BusinessType.INSERT)
	@PostMapping(value="/addManageField")
	@ApiOperation(value="新增管理字段",notes="新增管理字段")
	public ResultT addManageField(ManageFieldDto manageFieldDto) {
		log.info(">>>>>>>>>>>>>管理字段新增");
		try {
			manageFieldService.addManageField(manageFieldDto);
			return ResultT.success("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	/**
	 *  主键查询单条管理字段
	 * @description
	 * @author wlg
	 * @date 2020年2月6日上午10:54:47
	 * @param id
	 * @return
	 */
	@RequiresPermissions("restApi:manageField:findManageFieldByPk")
	@GetMapping(value="/findManageFieldByPk")
	@ApiOperation(value="根据ID查询管理字段",notes="查询单条管理字段分组接口")
	public ResultT findManageFieldByPk(String id) {
		log.info(">>>>>>>>主键查询单条管理字段");
		try {
			if(StringUtil.isEmpty(id)) return ResultT.failed("参数不能为空");
			ManageFieldDto mfd= manageFieldService.findManageFieldByPk(id);
			return ResultT.success(mfd);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}

	/**
	 *  编辑管理字段
	 * @description
	 * @author wlg
	 * @date 2020年2月6日上午11:27:53
	 * @param manageFieldDto
	 * @return
	 */
	@RequiresPermissions("restApi:manageField:editManageField")
	@Log(title = "管理字段管理", businessType = BusinessType.UPDATE)
	@PostMapping(value="/editManageField")
	@ApiOperation(value="编辑管理字段",notes="编辑管理字段")
	public ResultT editManageField(@RequestBody ManageFieldDto manageFieldDto) {
		log.info(">>>>>>>>编辑管理字段");
		try {
			manageFieldService.editManageField(manageFieldDto);
			return ResultT.success("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}

	/**
	 *  批量删除关联字段
	 * @description
	 * @author wlg
	 * @date 2020年2月6日上午11:47:38
	 * @param ids
	 * @return
	 */
	@RequiresPermissions("restApi:manageField:delManageField")
	@Log(title = "管理字段管理", businessType = BusinessType.DELETE)
	@DeleteMapping(value="/delManageField")
	@ApiOperation(value="删除管理字段",notes="删除管理字段")
	public ResultT delManageField(String ids) {
		log.info(">>>>>>>>批量删除管理字段");
		try {
			manageFieldService.delManageField(ids);
			return ResultT.success("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}

	/**
	 *  管理字段导出
	 * @description
	 * @author wlg
	 * @date 2020年3月19日下午4:32:44
	 * @param dto
	 */
	@ApiOperation(value = "管理字段导出", notes = "管理字段导出")
    @RequiresPermissions("restApi:manageField:export")
    @GetMapping("/export")
	public void exportExcel(ManageFieldDto dto) {
		manageFieldService.exportExcel(dto);
	}


}
