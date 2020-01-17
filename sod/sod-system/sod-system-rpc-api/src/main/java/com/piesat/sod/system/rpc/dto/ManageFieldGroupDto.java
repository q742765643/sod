package com.piesat.sod.system.rpc.dto;

import java.io.Serializable;

import lombok.Data;

/**
*@description
*@author wlg
*@date 2020年1月15日下午5:10:21
*
*/
@Data
public class ManageFieldGroupDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7367616459801149136L;
	
	/**
	 *  分组id
	 */
	private String groupId;
	/**
	 *  字段id
	 */
	private String fieldId;
	
	

}
