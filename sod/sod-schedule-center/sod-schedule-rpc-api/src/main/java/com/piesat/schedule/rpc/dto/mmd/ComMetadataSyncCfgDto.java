package com.piesat.schedule.rpc.dto.mmd;
 /** 公共元数据同步配置dto
  * 
*@description
*@author wlg
*@date 2020年2月6日下午4:51:44
*
*/

import java.io.Serializable;

import lombok.Data;

@Data
public class ComMetadataSyncCfgDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3281009586986033266L;
	
	/**
	 *  id
	 */
	private String id;
	/**
	 * 任务名
	 */
	private String taskName;
	/**
	 * 表名
	 */
	private String tableName;
	/**
	 *  接口url
	 */
	private String apiUrl;
	/**
	 * 接口类型 
	 * 1:全量同步
	 * 2:增量同步
	 */
	private String apiType;
	/**
	 *  接口数据关键字
	 */
	private String apiDataKey;
	/**
	 *  启动时间
	 */
	private Integer startTime;
	/**
	 *  启动时间单位
	 */
	private String startTimeUnit;
	/**
	 *  同步主键
	 */
	private String primaryKey;
	/**
	 *  描述
	 */
	private String description;

}
