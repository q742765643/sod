package com.piesat.sod.system.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/** 管理字段实体
*@description
*@author wlg
*@date 2020年1月15日下午3:54:12
*
*/
@Data
@Entity
@Table(name="DMIN_DB_MANAGER_FIELD")
public class ManageFieldEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7295902358572895742L;
	
	/**
	 *  id
	 */
	@Id
    @Column(name="ID")
    @GeneratedValue(generator = "htht.uuid")
    @GenericGenerator(name = "htht.uuid", strategy = "com.piesat.common.jpa.generator.UUIDStringGenerator")
	private String id;
	/**
	 * 存储编码
	 */
	@Column(name="DB_ELE_CODE")
	private String dbEleCode;
	/**
	 * 服务编码
	 */
	@Column(name="USER_ELE_CODE")
	private String userEleCode;
	/**
	 * 中文名
	 */
	@Column(name="DB_ELE_NAME")
	private String dbEleName;
	/**
	 *  中文简称
	 */
	@Column(name="ELE_NAME")
	private String eleName;
	/**
	 * 数据类型
	 */
	@Column(name="TYPE")
	private String type;
	/**
	 * 精度
	 */
	@Column(name="DATA_PRECISION1")
	private Integer dataPrecision1;
	/**
	 * 长度
	 */
	@Column(name="LENGTH")
	private Integer length;
	/**
	 * 是否可空
	 */
	@Column(name="IS_NULL")
	private Boolean nullAble;
	/**
	 * 是否可修改
	 */
	@Column(name="IS_UPDATE")
	private Boolean updateAble;
	/**
	 * 数据精度
	 */
	@Column(name="DATA_PRECISION")
	private Integer dataPrecision;

}
