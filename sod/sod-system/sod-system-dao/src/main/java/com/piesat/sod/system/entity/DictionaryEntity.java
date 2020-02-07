package com.piesat.sod.system.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/** 字典表实体
*@description
*@author wlg
*@date 2020年1月14日下午4:45:14
*
*/
@Data
@Entity
@Table(name = "T_SOD_SYSTEM_DICTIONARY")
public class DictionaryEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4397669300055893445L;
	
	/**
	 *  id
	 *  
	 */
	@Id
    @Column(name="ID",length=36)
    @GeneratedValue(generator = "htht.uuid")
    @GenericGenerator(name = "htht.uuid", strategy = "com.piesat.common.jpa.generator.UUIDStringGenerator")
	private String id;
	/**
	 * 关键字
	 */
	@Column(name="KEY_COL",length=64)
	private String keyCol;
	/**
	 * 字典类型
	 */
	@Column(name="TYPE",length=11)
	private Integer type;
	/**
	 * 中文名
	 */
	@Column(name="NAME_CN",length=64)
	private String nameCn;
	/**
	 * 字典描述
	 */
	@Column(name="DESCRIPTION",length=255)
	private String description;
	/**
	 * 区分是字典数据还是字典类型
	 */
	@Column(name="FLAG",length=255)
	private String flag;
	/**
	 * 是否可删除
	 */
	@Column(name="CAN_DELETE",length=255)
	private String canDelete;
	/**
	 * 目录id
	 */
	@Column(name="MENU")
	private Integer menu;
	/**
	 * 排序编号
	 */
	@Column(name="SERIAL_NUMBER",length=10)
	private String serialNumber;
	
	/**
	 *  字典类型
	 */
	@Transient
	private String dataType;

}
