package com.piesat.sod.system.rpc.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/** 公共元数据同步记录
*@description
*@author wlg
*@date 2020年2月6日下午5:05:58
*
*/
@Data
public class ComMetadataSyncRecordDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3286392842561056997L;
	
	/**
	 * id
	 */
	private String id;
	/**
	 * 开始时间
	 */
	private Date startTime;
	/**
	 * 结束时间
	 */
	private Date stopTime;
	/**
	 * 同步表名
	 */
	private String syncTableName;
	/**
	 * 同步记录数
	 */
	private Integer syncRecordNum;
	/**
	 * 同步类型:全量同步,增量同步
	 */
	private String syncType;
	/**
	 * 同步方式:自动同步,手动同步
	 */
	private String syncModel;
	/**
	 * 运行状态:同步成功,同步失败
	 */
	private String runState;
	/**
	 * 失败原因
	 */
	private String failReason;

}
