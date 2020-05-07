package com.piesat.schedule.entity.mmd;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.piesat.common.jpa.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 公共元数据同步记录实体
*@description
*@author wlg
*@date 2020年2月6日下午4:12:08
*
*/
@Data
@Entity
@Table(name="T_SOD_SYNC_COMMETADATA_RECORD")
@EqualsAndHashCode(callSuper = true)
public class ComMetadataSyncRecordEntity extends BaseEntity{

	/**
	 *
	 */
	private static final long serialVersionUID = 4080326249096299646L;
	/**
	 * 开始时间
	 */
	@Column(name="START_TIME")
	private Date startTime;
	/**
	 * 结束时间
	 */
	@Column(name="STOP_TIME")
	private Date stopTime;
	/**
	 * 同步表名
	 */
	@Column(name="SYNC_TABLE_NAME",length=50)
	private String syncTableName;
	/**
	 * 同步记录数
	 */
	@Column(name="SYNC_RECORD_NUM",length=10)
	private Integer syncRecordNum;
	/**
	 * 同步类型:全量同步,增量同步
	 */
	@Column(name="SYNC_TYPE",length=50)
	private String syncType;
	/**
	 * 同步方式:自动同步,手动同步
	 */
	@Column(name="SYNC_MODEL",length=50)
	private String syncModel;
	/**
	 * 运行状态:同步成功,同步失败
	 */
	@Column(name="RUN_STATE",length=50)
	private String runState;
	/**
	 * 失败原因
	 */
	@Column(name="FAIL_REASON",length=500)
	private String failReason;


}
