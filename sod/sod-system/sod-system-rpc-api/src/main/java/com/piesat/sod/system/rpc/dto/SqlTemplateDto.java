package com.piesat.sod.system.rpc.dto;

import java.io.Serializable;

import lombok.Data;
/**
 *  sql 模板管理dto
 * @author adminis
 *
 */
@Data
public class SqlTemplateDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3883656248790827717L;
	
	/**
	 * id
	 */
	private String id;
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
