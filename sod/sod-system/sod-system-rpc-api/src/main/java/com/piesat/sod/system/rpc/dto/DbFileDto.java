package com.piesat.sod.system.rpc.dto;

import com.piesat.util.BaseDto;

import lombok.Data;

/** 数据库文件表信息DTO
*@description
*@author wlg
*@date 2019年11月20日上午11:39:26
*
*/
@Data
public class DbFileDto extends BaseDto{
	/**
	 * 
	 */
	private static final long serialVersionUID = 707151325787729909L;
	/**
	 * 文件类型
	 */
	private String fileType;
	/**
	 * 文件名称
	 */
	private String fileName;
	/**
	 * 存储名称
	 */
	private String fileStorName;
	/**
	 * 存储路径
	 */
	private String fileStorPath;
	/**
	 * 是否是图片
	 */
	private String filePictrue;
	/**
	 * 文档后缀
	 */
	private String fileSuffix;
	
	/**
	 *  开始时间
	 */
	private String startDate;
	/**
	 *  结束时间
	 */
	private String endDate;
	/**
	 *  排序字段
	 */
	private String field;
	/**
	 *  顺序
	 */
	private String order;
}
