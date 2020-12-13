package com.piesat.portal.rpc.dto;

import com.piesat.util.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单管理
 */
@Data
public class MenuManageDto extends BaseDto {

    /** 菜单名称 */
    private String menuName;

    /** 父菜单名称 */
    private String parentName;

    /** 父菜单ID */
    private String parentId;

    /** 显示顺序 */
    private int orderNum;

    /** 路由地址 */
    private String path;

    /** 组件路径 */
    private String component;

    /** 是否为外链（0是 1否） */
    private int isFrame=1;

    /** 类型（M目录 C菜单 F按钮） */
    private String menuType;

    /** 菜单状态:0显示,1隐藏 */
    private String visible="0";

    /** 权限字符串 */
    private String perms;

    /** 菜单图标 */
    private String icon;


    /**
     * 备注
     */
    private String remark;

    @ApiModelProperty(hidden = true)
    private List<MenuManageDto> children=new ArrayList<>();
}
