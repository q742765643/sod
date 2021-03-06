package com.piesat.schedule.entity.synces;

import com.piesat.common.annotation.Excel;
import com.piesat.schedule.entity.JobInfoEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 非结构化数据交换系统同步
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-20 15:14
 **/
@Data
@Entity
@Table(name="T_SOD_JOB_SYNCES_INFO")
@DiscriminatorValue("SYNCES")
public class SyncEsEntity extends JobInfoEntity{
    @Excel(name = "物理库ID")
    @Column(name="database_id", length=50)
    private String databaseId;
    @Column(name="parent_id", length=50)
    private String parentId;
    @Excel(name = "存储编码")
    @Column(name="data_class_id", length=50)
    private String dataClassId;
    @Excel(name = "资料名称")
    @Column(name="profile_name", length=255)
    private String profileName;
    @Excel(name = "四级编码")
    @Column(name="d_data_id", length=50)
    private String ddataId;
    @Excel(name = "存储目录")
    @Column(name="save_directory", length=255)
    private String saveDirectory;
    @Excel(name = "数据库类型")
    @Column(name="database_type", length=50)
    private String databaseType;
    @Excel(name = "键表名")
    @Column(name="table_name", length=50)
    private String tableName;
    @Excel(name = "要素表名")
    @Column(name="v_table_name", length=50)
    private String vTableName;
    @Column(name="last_time")
    private Date lastTime;
    @Column(name="start_time")
    private Date startTime;
    @Column(name="buffer_time")
    private long bufferTime;
    /**
     * 文件地址字段
     */
    @Column(name = "site_column")
    private String siteColumn;
    /**
     * 时间字段
     */
    @Column(name = "time_column")
    private String timeColumn;
    @Column(name="transfer_type")
    private String transferType;
    @Column(name="ftp_ip")
    private String ftpIp;
    @Column(name="ftp_port")
    private int ftpPort;
    @Column(name="ftp_user")
    private String ftpUser;
    @Column(name="ftp_pwd")
    private String ftpPwd;
}

