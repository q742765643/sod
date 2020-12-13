package com.piesat.portal.rpc.api;

import com.piesat.portal.rpc.dto.MenuManageDto;
import com.piesat.portal.rpc.util.PortalTreeSelect;

import java.util.List;

/**
 * 菜单管理
 */
public interface MenuManageService {
    List<MenuManageDto> selectMenuList(MenuManageDto menu);

    List<PortalTreeSelect> treeSelect(MenuManageDto menu);

    public MenuManageDto insertMenu(MenuManageDto menu);

    public MenuManageDto updateMenu(MenuManageDto menu);

    public MenuManageDto selectMenuById(String menuId);

    public void deleteMenuById(String menuId);
}
