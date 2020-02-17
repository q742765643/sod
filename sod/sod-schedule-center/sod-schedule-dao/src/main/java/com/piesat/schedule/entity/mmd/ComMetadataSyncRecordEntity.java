package com.piesat.schedule.entity.mmd;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/** 公共元数据同步记录实体
*@description
*@author wlg
*@date 2020年2月6日下午4:12:08
*
*/
@Data
@Entity
@Table(name="t_sod_sync_commetadata_record")
public class ComMetadataSyncRecordEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4080326249096299646L;
	/**
	 * id
	 */
	@Id
    @Column(name="ID",length=36)
    @GeneratedValue(generator = "htht.uuid")
    @GenericGenerator(name = "htht.uuid", strategy = "com.piesat.common.jpa.generator.UUIDStringGenerator")
	private String id;
	/**
	 * 开始时间
	 */
	@Column(name="START_TIME",length=36)
	private Date startTime;
	/**
	 * 结束时间
	 */
	@Column(name="STOP_TIME",length=36)
	private Date stopTime;
	/**
	 * 同步表名
	 */
	@Column(name="SYNC_TABLE_NAME",length=36)
	private String syncTableName;
	/**
	 * 同步记录数
	 */
	@Column(name="SYNC_RECORD_NUM",length=36)
	private Integer syncRecordNum;
	/**
	 * 同步类型:全量同步,增量同步
	 */
	@Column(name="SYNC_TYPE",length=36)
	private String syncType;
	/**
	 * 同步方式:自动同步,手动同步
	 */
	@Column(name="SYNC_MODEL",length=36)
	private String syncModel;
	/**
	 * 运行状态:同步成功,同步失败
	 */
	@Column(name="RUN_STATE",length=36)
	private String runState;
	/**
	 * 失败原因
	 */
	@Column(name="FAIL_REASON",length=36)
	private String failReason;
	

}
