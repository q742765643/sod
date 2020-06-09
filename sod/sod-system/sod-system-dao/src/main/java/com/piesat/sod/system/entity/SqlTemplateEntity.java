package com.piesat.sod.system.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.piesat.common.jpa.entity.BaseEntity;

import lombok.Data;

/**
 *  sql 模板实体
 * @author adminis
 *
 */
@Data
@Table(name="T_SOD_SQL_TEMPLATE")
@Entity
public class SqlTemplateEntity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4779126125185117401L;
	/**
	 * 数据库类型
	 */
	@Column(name="DATABASE_SERVER",length=36)
	private String databaseServer;
	/**
	 * 模板信息
	 */
	@Column(name="TEMPLATE",length=4000)
	private String template;
	/**
	 *  数据库名称
	 */
	@Transient
	private String databaseName;

}
