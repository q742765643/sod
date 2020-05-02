package com.piesat.schedule.entity.index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.piesat.common.jpa.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 资料统计表
*@description
*@author wlg
*@date 2020年3月30日下午3:00:40
*
*/
@Data
@Table(name="T_SOD_DATA_CLASS_COUNT")
@Entity
@EqualsAndHashCode(callSuper = true)
public class ClassCountEntity extends BaseEntity{/**
	 * 
	 */
	private static final long serialVersionUID = -1537071249889118475L;
	/**
	 * 年
	 */
	@Column(name="YEAR",length=10)
	private String year;
	/**
	 * 月
	 */
	@Column(name="MONTH",length=10)
	private String month;
	/**
	 *  数量
	 */
	@Column(name="NUM",length=10)
	private Integer num;

}
