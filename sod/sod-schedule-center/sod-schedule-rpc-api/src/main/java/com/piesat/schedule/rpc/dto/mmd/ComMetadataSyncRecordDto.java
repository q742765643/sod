package com.piesat.schedule.rpc.dto.mmd;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.piesat.util.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 公共元数据同步记录
*@description
*@author wlg
*@date 2020年2月6日下午5:05:58
*
*/
@Data
@EqualsAndHashCode(callSuper = true)
public class ComMetadataSyncRecordDto extends BaseDto{

	/**
	 *
	 */
	private static final long serialVersionUID = -3286392842561056997L;
	/**
	 * 开始时间
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	/**
	 * 结束时间
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
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
