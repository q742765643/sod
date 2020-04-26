package com.piesat.sod.system.rpc.dto;
 /** 管理字段dto
*@description
*@author wlg
*@date 2020年1月15日下午5:01:10
*
*/

import com.piesat.util.BaseDto;

import lombok.Data;

import javax.persistence.Version;

@Data
public class ManageFieldDto extends BaseDto{

	/**
	 *
	 */
	private static final long serialVersionUID = 3016029307502490989L;
	/**
	 * 存储编码
	 */
	private String dbEleCode;
	/**
	 * 服务编码
	 */
	private String userEleCode;
	/**
	 * 中文名
	 */
	private String dbEleName;
	/**
	 *  中文简称
	 */
	private String eleName;
	/**
	 * 数据类型
	 */
	private String type;
	/**
	 * 精度
	 */
	private Integer dataPrecision1;
	/**
	 * 长度
	 */
	private Integer length;
	/**
	 * 是否可空
	 */
	private Boolean nullAble;
	/**
	 * 是否可修改
	 */
	private Boolean updateAble;
	/**
	 * 数据精度
	 */
	private Integer dataPrecision;
	/**
	 *  分组id
	 */
	private String groupId;

	private Integer version;

}
