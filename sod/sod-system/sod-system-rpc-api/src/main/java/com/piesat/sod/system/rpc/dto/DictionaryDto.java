package com.piesat.sod.system.rpc.dto;

import java.io.Serializable;

import lombok.Data;

/** 字典表DTO
*@description
*@author wlg
*@date 2020年1月14日下午5:00:02
*
*/
@Data
public class DictionaryDto implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 8090317147605913793L;
	
	/**
	 * id
	 */
	private String id;
	/**
	 * 关键字
	 */
	private String keyCol;
	/**
	 * 字典类型
	 */
	private Integer type;
	/**
	 * 中文名
	 */
	private String nameCn;
	/**
	 * 字典描述
	 */
	private String description;
	/**
	 * 区分是字典数据还是字典类型
	 */
	private String flag;
	/**
	 * 是否可删除
	 */
	private String canDelete;
	/**
	 * 目录id
	 */
	private Integer menu;
	/**
	 * 排序编号
	 */
	private String serialNumber;
	/**
	 *  数据类型
	 */
	private String dataType;

}
