package com.piesat.sod.system.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/** 管理字段分组关联实体
*@description
*@author wlg
*@date 2020年1月15日下午4:19:56
*
*/
@Data
@Entity
@Table(name = "DMIN_DB_MANAGER_FIELD_FIELDGROUP")
public class ManageFieldGroupEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2784345878526165734L;
	/**
	 * 分组id
	 */
	@Id
	@Column(name="GROUP_ID")
	private String groupId;
	/**
	 *  关联id
	 */
	@Id
	@Column(name="FIELD_ID")
	private String fieldId;

}
