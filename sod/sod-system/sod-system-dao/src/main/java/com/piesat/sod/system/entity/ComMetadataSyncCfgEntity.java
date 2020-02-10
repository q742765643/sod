package com.piesat.sod.system.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/** 公共元数据同步配置实体
*@description
*@author wlg
*@date 2020年2月6日下午2:53:25
*
*/
@Data
@Table(name="t_sod_sync_commetadata_cfg")
@Entity
public class ComMetadataSyncCfgEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2114752285073022365L;
	
	/**
	 *  id
	 */
	@Id
    @Column(name="ID",length=36)
    @GeneratedValue(generator = "htht.uuid")
    @GenericGenerator(name = "htht.uuid", strategy = "com.piesat.common.jpa.generator.UUIDStringGenerator")
	private String id;
	/**
	 * 任务名
	 */
	@Column(name="TASK_NAME",length=50)
	private String taskName;
	/**
	 * 表名
	 */
	@Column(name="TABLE_NAME",length=50)
	private String tableName;
	/**
	 *  接口url
	 */
	@Column(name="API_URL",length=200)
	private String apiUrl;
	/**
	 * 接口类型 
	 * 1:全量同步
	 * 2:增量同步
	 */
	@Column(name="API_TYPE",length=1)
	private String apiType;
	/**
	 *  接口数据关键字
	 */
	@Column(name="API_DATAKEY",length=50)
	private String apiDataKey;
	/**
	 *  启动时间
	 */
	@Column(name="START_TIME",length=5)
	private Integer startTime;
	/**
	 *  启动时间单位
	 */
	@Column(name="START_TIME_UNIT",length=10)
	private String startTimeUnit;
	/**
	 *  同步主键
	 */
	@Column(name="PRIMARY_KEY",length=200)
	private String primaryKey;
	

}
