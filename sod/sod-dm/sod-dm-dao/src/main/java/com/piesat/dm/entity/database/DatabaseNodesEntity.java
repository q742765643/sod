package com.piesat.dm.entity.database;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 数据库节点信息
 *
 * @author cwh
 * @date 2019年 11月21日 19:00:28
 */
@Data
@Entity
@Table(name = "T_SOD_DATABASE_NODE")
//@org.hibernate.annotations.Table(appliesTo = "t_sod_database_node",comment = "数据库节点信息表")
public class DatabaseNodesEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 数据库id
     * database_id
     */
    @Column(name = "database_id", columnDefinition = "varchar(225) default ''")
    private String databaseId;

    /**
     * 节点ip
     * database_node
     */
    @Column(name = "database_node", columnDefinition = "varchar(225) default ''")
    private String databaseNode;

    /**
     * 节点状态
     * node_state
     */
    @Column(name = "node_state", columnDefinition = "varchar(36) default ''")
    private String nodeState;

    /**
     * 节点角色
     * node_role
     */
    @Column(name = "node_role", columnDefinition = "varchar(36) default ''")
    private String nodeRole;

}
