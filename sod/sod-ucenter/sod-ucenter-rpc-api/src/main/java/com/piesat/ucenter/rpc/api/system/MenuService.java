package com.piesat.ucenter.rpc.api.system;

import com.piesat.ucenter.rpc.dto.system.MenuDto;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.ucenter.rpc.util.RouterVo;

import java.util.List;
import java.util.Set;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/28 17:07
 */
public interface MenuService {
    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    List<MenuDto> selectMenuList(MenuDto menu);
    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
   Set<String> selectMenuPermsByUserId(String userId);

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
   Set<String> getMenuPermission(UserDto user);
    /**
     * 获取路由列表
     *
     * @param userId 用户id
     * @return 获取路由
     */
   List<RouterVo> getRouters(String userId);
}
