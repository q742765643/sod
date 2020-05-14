package com.piesat.schedule.entity.index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.piesat.common.jpa.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**数据库使用情况统计
*@description
*@author wlg
*@date 2020年3月30日下午6:30:23
*
*/
@Data
@Table(name="T_SOD_DATABASE_CAPACITY_INFO")
@Entity
@EqualsAndHashCode(callSuper = true)
public class DbCapacityEntity extends BaseEntity{/**
	 * 
	 */
	private static final long serialVersionUID = 6197706960611049416L;
	
	@Column(name="DATABASE_ID",length=50)
	private String databaseId;
	
	@Column(name="DATABASE_NAME",length=50)
	private String databaseName;
	
	@Column(name="ALIAS_NAME",length=50)
	private String aliasName;
	
	@Column(name="DATABASE_LOGIC",length=50)
	private String databaseLogic;
	
	@Column(name="TOTAL_CAPACITY",length=10)
	private long totalCapacity;
	
	@Column(name="USED_CAPACITY",length=10)
	private long usedCapacity;

}
