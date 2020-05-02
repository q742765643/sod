package com.piesat.schedule.entity.mmd;

import javax.persistence.*;

import com.piesat.schedule.entity.JobInfoEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 公共元数据同步配置实体
*@description
*@author wlg
*@date 2020年2月6日下午2:53:25
*
*/
@Data
@Table(name="T_SOD_SYNC_COMMETADATA_CFG")
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("MMD")
public class ComMetadataSyncCfgEntity extends JobInfoEntity{

	/**
	 *
	 */
	private static final long serialVersionUID = 2711537570477549135L;
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
	@Column(name="START_TIME",length=50)
	private String startTime;
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

	/**
	 * 来源
	 */
	@Column(name="SOURCE",length=200)
	private String source;



	/**
	 * 描述
	 */
	@Transient
	private String description;


}
