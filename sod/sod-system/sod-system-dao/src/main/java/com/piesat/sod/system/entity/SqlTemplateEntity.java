package com.piesat.sod.system.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 *  sql 模板实体
 * @author adminis
 *
 */
@Data
@Table(name="T_SOD_SQL_TEMPLATE")
@Entity
public class SqlTemplateEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4779126125185117401L;
	/**
	 * id
	 */
	@Id
    @Column(name="ID",length=36)
    @GeneratedValue(generator = "htht.uuid")
    @GenericGenerator(name = "htht.uuid", strategy = "com.piesat.common.jpa.generator.UUIDStringGenerator")
	private String id;
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

}
