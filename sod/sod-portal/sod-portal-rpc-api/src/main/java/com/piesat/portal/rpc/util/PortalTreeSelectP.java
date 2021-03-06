package com.piesat.portal.rpc.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.piesat.portal.rpc.dto.DepartManageDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Treeselect树结构实体类
 *
 * @author ruoyi
 */
@Data
public class PortalTreeSelectP implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private String id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<PortalTreeSelectP> children;

    public PortalTreeSelectP()
    {

    }

    public PortalTreeSelectP(DepartManageDto dept)
    {
        this.id = dept.getDeptcode();
        this.label = dept.getDeptname();
        this.children = dept.getChildren().stream().map(PortalTreeSelectP::new).collect(Collectors.toList());
    }

}
