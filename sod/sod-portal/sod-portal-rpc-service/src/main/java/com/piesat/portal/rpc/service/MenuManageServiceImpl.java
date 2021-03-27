package com.piesat.portal.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.portal.dao.MenuManageDao;
import com.piesat.portal.entity.MenuManageEntity;
import com.piesat.portal.mapper.MenuManageMapper;
import com.piesat.portal.rpc.api.MenuManageService;
import com.piesat.portal.rpc.dto.MenuManageDto;
import com.piesat.portal.rpc.mapstruct.MenuManageMapstruct;
import com.piesat.portal.rpc.util.PortalTreeSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service("menuManageService")
public class MenuManageServiceImpl extends BaseService<MenuManageEntity> implements MenuManageService {

    @Autowired
    private MenuManageDao menuManageDao;

    @Autowired
    private MenuManageMapper menuManageMapper;

    @Autowired
    private MenuManageMapstruct menuManageMapstruct;
    @Override
    public BaseDao<MenuManageEntity> getBaseDao() {
        return menuManageDao;
    }

    @Override
    public List<MenuManageDto> selectMenuList(MenuManageDto menu) {
        List<MenuManageEntity> menuManageEntities = menuManageMapper.selectMenuList(menuManageMapstruct.toEntity(menu));
        return this.buildMenuTree(menuManageMapstruct.toDto(menuManageEntities));
    }

    @Override
    public List<PortalTreeSelect> treeSelect(MenuManageDto menu) {
        List<MenuManageEntity> menuList = menuManageMapper.selectMenuList(menuManageMapstruct.toEntity(menu));
        return this.buildMenuTreeSelect(menuManageMapstruct.toDto(menuList));
    }

    @Override
    public MenuManageDto insertMenu(MenuManageDto menu) {
        MenuManageEntity menuManageEntity = menuManageMapstruct.toEntity(menu);
        return menuManageMapstruct.toDto(this.saveNotNull(menuManageEntity));
    }

    @Override
    public MenuManageDto updateMenu(MenuManageDto menu) {
        MenuManageEntity menuManageEntity = menuManageMapstruct.toEntity(menu);
        return menuManageMapstruct.toDto(this.saveNotNull(menuManageEntity));
    }

    @Override
    public MenuManageDto selectMenuById(String menuId) {
        MenuManageEntity menuManageEntity = this.getById(menuId);
        return menuManageMapstruct.toDto(menuManageEntity);
    }

    @Override
    public void deleteMenuById(String menuId) {
        this.delete(menuId);
    }

    @Override
    public List<String> selectMenuListByRoleId(String roleId) {
        return menuManageMapper.selectMenuListByRoleId(roleId);
    }

    public List<PortalTreeSelect> buildMenuTreeSelect(List<MenuManageDto> menus)
    {
        List<MenuManageDto> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(PortalTreeSelect::new).collect(Collectors.toList());
    }

    public List<MenuManageDto> buildMenuTree(List<MenuManageDto> menus)
    {
        List<MenuManageDto> returnList = new ArrayList<MenuManageDto>();
        for (Iterator<MenuManageDto> iterator = menus.iterator(); iterator.hasNext();)
        {
            MenuManageDto t = (MenuManageDto) iterator.next();
            // 根据传入的某个父节点ID,遍历该父节点的所有子节点
            if ("0".equals(t.getParentId()))
            {
                recursionFn(menus, t);
                returnList.add(t);
            }
        }
        removeMenuTree(menus,returnList);
        if (!menus.isEmpty())
        {
            returnList.addAll(menus);
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<MenuManageDto> list, MenuManageDto t)
    {
        // 得到子节点列表
        List<MenuManageDto> childList = getChildList(list, t);
        t.setChildren(childList);
        for (MenuManageDto tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                // 判断是否有子节点
                Iterator<MenuManageDto> it = childList.iterator();
                while (it.hasNext())
                {
                    MenuManageDto n = (MenuManageDto) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<MenuManageDto> getChildList(List<MenuManageDto> list, MenuManageDto t)
    {
        List<MenuManageDto> tlist = new ArrayList<MenuManageDto>();
        Iterator<MenuManageDto> it = list.iterator();
        while (it.hasNext())
        {
            MenuManageDto n = (MenuManageDto) it.next();
            if (n.getParentId().equals(t.getId()))
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<MenuManageDto> list, MenuManageDto t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    /**
     * 菜单列表移除树结构列表中的节点
     * @param menus  菜单列表
     * @param menuTree 树结构列表
     */
    public void removeMenuTree(List<MenuManageDto> menus,List<MenuManageDto> menuTree){
        for (int i=0;i<menuTree.size();i++){
            MenuManageDto t = menuTree.get(i);
            for(int j=0;j<menus.size();j++){
                if(t.getId().equals(menus.get(j).getId())){
                    menus.remove(menus.get(j));
                    break;
                }
            }
            List<MenuManageDto> children = t.getChildren();
            removeMenuTree(menus,children);
        }
    }


}
