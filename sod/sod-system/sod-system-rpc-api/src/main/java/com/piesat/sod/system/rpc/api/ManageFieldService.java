package com.piesat.sod.system.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.sod.system.rpc.dto.ManageFieldDto;
import com.piesat.sod.system.rpc.dto.ManageGroupDto;
import com.piesat.util.constant.GrpcConstant;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;

/**
*@description
*@author wlg
*@date 2020年1月16日下午5:12:11
*
*/
@GrpcHthtService(server = GrpcConstant.SYSTEM_SERVER,serialization = SerializeType.PROTOSTUFF)
public interface ManageFieldService {

	/**
	 *  获取所有管理字段分组
	 * @description
	 * @author wlg
	 * @date 2020年1月16日下午5:26:57
	 * @return
	 * @throws Exception
	 */
	List<ManageGroupDto> findAllManageGroup() throws Exception;

	/**
	 *  新增管理字段分组
	 * @description
	 * @author wlg
	 * @date 2020年1月16日下午5:55:09
	 * @param manageGroupDto
	 * @throws Exception
	 */
	void addManageGroup(ManageGroupDto manageGroupDto) throws Exception;
	/**
	 *  删除管理字段分组
	 * @description
	 * @author wlg
	 * @date 2020年1月16日下午5:57:26
	 * @param groupId
	 * @throws Exception
	 */
	void delManageGroup(String groupId) throws Exception;
	/**
	 *  管理字段分页查询
	 * @description
	 * @author wlg
	 * @date 2020年1月17日下午3:56:27
	 * @param manageGroupDto
	 * @return
	 * @throws Exception
	 */
	PageBean findPageData(PageForm<ManageFieldDto> pageForm) throws Exception;

	/**
	 * 新增管理字段
	 * @description
	 * @author wlg
	 * @date 2020年2月6日上午10:06:29
	 * @param manageFieldDto
	 * @throws Exception
	 */
	void addManageField(ManageFieldDto manageFieldDto) throws Exception;

	/**
	 *  主键查询管理字段分组
	 * @description
	 * @author wlg
	 * @date 2020年2月6日上午10:34:28
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	ManageGroupDto findManageGroupByPk(String groupId) throws Exception;
	/***
	 * 主键查询管理字段
	 * @description
	 * @author wlg
	 * @date 2020年2月6日上午10:47:36
	 * @param id
	 * @return
	 * @throws Exception
	 */
	ManageFieldDto findManageFieldByPk(String id)throws Exception;
	/**
	 *  编辑管理字段
	 * @description
	 * @author wlg
	 * @date 2020年2月6日上午11:23:21
	 * @param manageFieldDto
	 * @throws Exception
	 */
	void editManageField(ManageFieldDto manageFieldDto) throws Exception;
	/**
	 *  编辑管理字段分组
	 * @description
	 * @author wlg
	 * @date 2020年2月6日上午11:31:02
	 * @param manageGroupDto
	 * @throws Exception
	 */
	void editManageGroup(ManageGroupDto manageGroupDto) throws Exception;

	/**
	 *  删除关联字段分组
	 * @description
	 * @author wlg
	 * @date 2020年2月6日上午11:37:28
	 * @param ids
	 * @throws Exception
	 */
	void delManageField(String ids) throws Exception;
	/**
	 *  管理字段导出
	 * @description
	 * @author wlg
	 * @date 2020年3月19日下午4:34:04
	 * @param dto
	 */
	void exportExcel(ManageFieldDto dto);

	List<ManageFieldDto> findData(ManageFieldDto dto);
}
