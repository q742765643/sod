package com.piesat.dm.rpc.dto.database;

import lombok.Data;

import java.util.Date;

/**
 * 数据库节点信息
 *
 * @author cwh
 * @date 2019年 11月21日 19:00:28
 */
@Data
public class DatabaseNodesDto {
    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 数据库id
     * database_id
     */
    private String databaseId;

    /**
     * 节点ip
     * database_node
     */
    private String databaseNode;

    /**
     * 节点状态
     * node_state
     */
    private String nodeState;

    /**
     * 节点角色
     * node_role
     */
    private String nodeRole;

    private Date createTime;

    private Date updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private Integer version;

}
