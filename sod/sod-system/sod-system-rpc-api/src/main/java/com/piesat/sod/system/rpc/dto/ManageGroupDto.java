package com.piesat.sod.system.rpc.dto;

import java.io.Serializable;

import lombok.Data;

/** 管理字段分组
*@description
*@author wlg
*@date 2020年1月15日下午5:07:14
*
*/
@Data
public class ManageGroupDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2138692409890552965L;
	
	/**
	 *  分组id
	 */
	private String groupId;
	
	/**
	 *  分组名称
	 */
	private String groupName;
	
	

}
