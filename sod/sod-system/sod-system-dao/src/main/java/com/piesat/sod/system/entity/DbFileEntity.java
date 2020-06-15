package com.piesat.sod.system.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.piesat.common.jpa.entity.BaseEntity;

import lombok.Data;

/** 数据库文档实体
*@description
*@author wlg
*@date 2019年11月20日上午11:11:42
*
*/
@Data
@Table(name = "T_SOD_DB_FILE")
@Entity
//@MappedSuperclass
public class DbFileEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5919028096424404350L;
	/**
	 * 文件类型
	 */
	@Column(name="FILE_TYPE")
	private String fileType;
	/**
	 * 文件名称
	 */
	@Column(name="FILE_NAME")
	private String fileName;
	/**
	 * 存储名称
	 */
	@Column(name="FILE_STOR_NAME")
	private String fileStorName;
	/**
	 * 存储路径
	 */
	@Column(name="FILE_STOR_PATH")
	private String fileStorPath;
	/**
	 * 是否是图片
	 */
	@Column(name="FILE_PICTURE")
	private String filePictrue;
	/**
	 * 文档后缀
	 */
	@Column(name="FILE_SUFFIX")
	private String fileSuffix;
	
	/**
	 * 开始时间
	 */
	@Transient
	private String startDate;
	/**
	 *  结束时间
	 */
	@Transient
	private String endDate;
	
	/**
	 * 排序字段
	 */
	@Transient
	private String field;
	/**
	 *  asc desc
	 */
	@Transient
	private String order;

}
