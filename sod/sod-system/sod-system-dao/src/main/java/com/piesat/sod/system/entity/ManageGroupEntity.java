package com.piesat.sod.system.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/** 管理字段分组
*@description
*@author wlg
*@date 2020年1月15日下午4:15:30
*
*/
@Data
@Entity
@Table(name = "T_SOD_DB_MANAGER_FIELDGROUP")
public class ManageGroupEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8958617789331657656L;
	
	/**
	 *  分组id
	 */
	@Id
    @Column(name="GROUP_ID")
    @GeneratedValue(generator = "htht.uuid")
    @GenericGenerator(name = "htht.uuid", strategy = "com.piesat.common.jpa.generator.UUIDStringGenerator")
	private String groupId;
	
	/**
	 *  分组名称
	 */
	@Column(name="GROUP_NAME")
	private String groupName;

}
