package com.piesat.sod.system.rpc.dto;

import com.piesat.util.BaseDto;

import lombok.Data;
/**
 *  sql 模板管理dto
 * @author adminis
 *
 */
@Data
public class SqlTemplateDto extends BaseDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3883656248790827717L;
	/**
	 * 数据库类型
	 */
	private String databaseServer;
	/**
	 * 模板信息
	 */
	private String template;
	/**
	 *  数据库名称
	 */
	private String databaseName;

}
