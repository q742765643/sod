package com.piesat.schedule.web.controller.mmd;
 /** 公共元数据同步Controller
*@description
*@author wlg
*@date 2020年2月6日下午5:24:31
*
*/

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.util.StringUtil;
import com.piesat.schedule.rpc.api.mmd.ComMetadataSyncService;
import com.piesat.schedule.rpc.dto.mmd.ComMetadataSyncCfgDto;
import com.piesat.schedule.rpc.dto.mmd.ComMetadataSyncRecordDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value="/comMetaData")
@Api(value="公共元数据同步Controller",tags = {"公共元数据同步接口"})
@Slf4j
public class ComMetadataSyncController {

	@Autowired
	private ComMetadataSyncService comMetadataSyncService;

	/**
	 *  分页查询
	 * @description
	 * @author wlg
	 * @date 2020年2月17日下午4:03:33
	 * @param comMetadataSyncCfgDto
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequiresPermissions("restApi:comMetaData:page")
	@ApiOperation(value="公共元数据同步分页信息查询",notes="公共元数据同步任务分页查询接口")
	@GetMapping(value="/page")
	public ResultT findPage(ComMetadataSyncCfgDto comMetadataSyncCfgDto,int pageNum,int pageSize) {
		log.info(">>>>>>>公共元数据同步任务查询");
		try {
			PageForm<ComMetadataSyncCfgDto> pageForm = new PageForm<>(pageNum,pageSize,comMetadataSyncCfgDto);
			PageBean page = comMetadataSyncService.findPageData(pageForm);
			return ResultT.success(page);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}

	/**
	 *  新增同步任务配置
	 * @description
	 * @author wlg
	 * @date 2020年2月17日下午4:31:16
	 * @param comMetadataSyncCfgDto
	 * @return
	 */
	@ApiOperation(value="新增同步任务配置",notes="新增同步任务配置接口")
	@PostMapping(value="/addConfig")
	@RequiresPermissions("restApi:comMetaData:addConfig")
    @Log(title = "新增同步任务配置接口", businessType = BusinessType.INSERT)
	public ResultT addConfig(@RequestBody ComMetadataSyncCfgDto comMetadataSyncCfgDto) {
		log.info(">>>>>>新增同步任务配置");
		try {
			comMetadataSyncService.addConfig(comMetadataSyncCfgDto);
			return ResultT.success("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	/**
	 *  公共元数据同步任务主键查询
	 * @description
	 * @author wlg
	 * @date 2020年2月17日下午4:46:52
	 * @param id
	 * @return
	 */
	@RequiresPermissions("restApi:comMetaData:findCfgByPk")
	@ApiOperation(value="公共元数据主键查询",notes="公共元数据主键查询接口")
	@GetMapping(value="/findCfgByPk")
	public ResultT findCfgByPk(String id) {
		log.info(">>>>>>>公共元数据同步 主键查询");
		try {
			if(StringUtil.isEmpty(id)) return ResultT.failed("参数不能为空");
			ComMetadataSyncCfgDto data = comMetadataSyncService.findByPk(id);
			return ResultT.success(data);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	/**
	 *  编辑同步任务配置
	 * @description
	 * @author wlg
	 * @date 2020年2月17日下午5:08:07
	 * @param comMetadataSyncCfgDto
	 * @return
	 */
	@ApiOperation(value="编辑同步任务配置",notes="编辑同步任务配置接口")
	@PutMapping(value="/editConfig")
	@RequiresPermissions("restApi:comMetaData:editConfig")
    @Log(title = "新增同步任务配置接口", businessType = BusinessType.UPDATE)
	public ResultT editCfg(@RequestBody ComMetadataSyncCfgDto comMetadataSyncCfgDto) {
		log.info(">>>>>>>>公共元数据同步任务修改");
		try {
			comMetadataSyncService.editConfig(comMetadataSyncCfgDto);
			return ResultT.success("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}

	/**
	 *  批量删除公共元数据同步任务
	 * @description
	 * @author wlg
	 * @date 2020年2月17日下午5:22:13
	 * @param ids
	 * @return
	 */
	@ApiOperation(value="删除公共元数据同步任务配置",notes="删除公共元数据同步任务配置接口")
	@DeleteMapping(value="/delConfig")
	@RequiresPermissions("restApi:comMetaData:delConfig")
    @Log(title = "删除公共元数据同步任务配置接口", businessType = BusinessType.DELETE)
	public ResultT delCfg(String ids) {
		log.info(">>>>>>>>删除公共元数据同步任务");
		try {
			if(StringUtil.isEmpty(ids)) return ResultT.failed("参数不能为空");
			comMetadataSyncService.delConfig(ids);
			return ResultT.success("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}

	/**
	 *  获取公共元数据同步记录分页信息
	 * @description
	 * @author wlg
	 * @date 2020年2月17日下午5:58:22
	 * @param comMetadataSyncRecordDto
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequiresPermissions("restApi:comMetaData:recordPage")
	@ApiOperation(value="公共元数据同步记录分页信息查询",notes="公共元数据同步记录分页查询接口")
	@GetMapping(value="/recordPage")
	public ResultT findRecordPage(ComMetadataSyncRecordDto comMetadataSyncRecordDto,int pageNum,int pageSize) {
		log.info(">>>>>>>>获取公共元数据同步记录分页信息");
		try {
			PageForm<ComMetadataSyncRecordDto> pageForm = new PageForm<>(pageNum,pageSize,comMetadataSyncRecordDto);
			PageBean page = comMetadataSyncService.findRecordPage(pageForm);
			return ResultT.success(page);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	/**
	 *  删除公共元数据同步记录
	 * @description
	 * @author wlg
	 * @date 2020年2月17日下午6:02:48
	 * @param ids
	 * @return
	 */
	@ApiOperation(value="删除公共元数据同步记录",notes="删除公共元数据同步记录接口")
	@DeleteMapping(value="/delRecord")
	@RequiresPermissions("restApi:comMetaData:delRecord")
    @Log(title = "删除公共元数据同步记录接口", businessType = BusinessType.DELETE)
	public ResultT delRecord(String ids) {
		log.info(">>>>>>>删除公共元数据同步记录接口");
		try {
			if(StringUtil.isEmpty(ids)) return ResultT.failed("参数不能为空");
			comMetadataSyncService.delRecord(ids);
			return ResultT.success("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}
	/**
	 *  立即同步公共元数据
	 *  两个入口 : 1 公共元数据同步 立即同步按钮,传参数是ids
	 *  2.新增数据注册 增量同步按钮 传参数是apiType
	 * @description
	 * @author wlg
	 * @date 2020年2月18日上午11:34:16
	 * @param ids
	 * @return
	 */
	@ApiOperation(value="立即同步公共元数据",notes="立即同步公共元数据接口")
	@GetMapping(value="/syncDataNow")
	@RequiresPermissions("restApi:comMetaData:syncDataNow")
    @Log(title = "立即同步公共元数据接口", businessType = BusinessType.OTHER)
	public ResultT syncDataNow(String ids,String apiType) {
		log.info(">>>>>>立即执行公共元数据同步");
		try {
			//oprType 同步模式1 :自动同步 , 2 : 手动同步
			return comMetadataSyncService.syncDataNow(ids, apiType,2);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultT.failed(e.getMessage());
		}
	}

}
