package com.piesat.dm.entity;

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
@Table(name = "T_SOD_DATABASE_NODE")
@Entity
//@Proxy(lazy = false)
public class DatabaseNodesEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 数据库id
     * database_id
     */
//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "database_id1")
//    private DatabaseDefineEntity databaseId;
    @Column(name = "database_id")
    private String databaseId;

    /**
     * 节点ip
     * database_node
     */
    @Column(name = "database_node", length = 255)
    private String databaseNode;

    /**
     * 节点状态
     * node_state
     */
    @Column(name = "node_state", length = 36)
    private String nodeState;

    /**
     * 节点角色
     * node_role
     */
    @Column(name = "node_role", length = 36)
    private String nodeRole;

}
