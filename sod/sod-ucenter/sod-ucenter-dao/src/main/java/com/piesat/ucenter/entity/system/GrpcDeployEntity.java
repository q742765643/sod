package com.piesat.ucenter.entity.system;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Date;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/25 10:43
 */
@Entity
@Data
@Table(name="T_SOD_GRPC_DEPLOY")
public class GrpcDeployEntity {
    @Id
    @Column(length = 32)
    private String id;
    private String jarName;
    private String serverId;
    private Date deployTime;

    @Column(nullable = false)
    private Date createTime;

    private Date updateTime;

}
