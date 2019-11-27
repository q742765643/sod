package com.piesat.ucenter.entity.system;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/25 18:28
 */
@Entity
@Data
@Table(name="T_SOD_GRPC_LOG")
public class GrpcLogEntity {
    @Id
    @Column(length = 32)
    private String id;

    private String userId;

    private String serverId;

    private String serverIp;

    private String serviceName;

    private String callMethod;

    @Column(nullable = false)
    private Date createTime;

    private Date updateTime;


}
