package com.piesat.portal.rpc.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.piesat.portal.rpc.dto.DepartManageDto;
import com.piesat.portal.rpc.dto.MenuManageDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Treeselect树结构实体类
 *
 * @author ruoyi
 */
@Data
public class PortalTreeSelect implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private String id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<PortalTreeSelect> children;

    public PortalTreeSelect()
    {

    }

    public PortalTreeSelect(DepartManageDto dept)
    {
        this.id = dept.getDeptunicode();
        this.label = dept.getDeptname();
        this.children = dept.getChildren().stream().map(PortalTreeSelect::new).collect(Collectors.toList());
    }

    public PortalTreeSelect(MenuManageDto menuManageDto)
    {
        this.id = menuManageDto.getId();
        this.label = menuManageDto.getMenuName();
        this.children = menuManageDto.getChildren().stream().map(PortalTreeSelect::new).collect(Collectors.toList());
    }

}
