package com.piesat.portal.mapper;

import com.piesat.portal.entity.MenuManageEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MenuManageMapper {

    public List<MenuManageEntity> selectMenuList(MenuManageEntity menu);

    List<String> selectMenuListByRoleId(String roleId);
}
