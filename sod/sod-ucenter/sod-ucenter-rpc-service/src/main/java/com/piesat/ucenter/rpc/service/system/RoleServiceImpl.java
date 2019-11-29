package com.piesat.ucenter.rpc.service.system;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.utils.StringUtils;
import com.piesat.ucenter.dao.system.RoleDao;
import com.piesat.ucenter.entity.system.RoleEntity;
import com.piesat.ucenter.mapper.system.RoleMapper;
import com.piesat.ucenter.rpc.api.system.RoleService;
import com.piesat.ucenter.rpc.dto.system.RoleDto;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.ucenter.rpc.mapstruct.system.RoleMapstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/28 16:03
 */
@Service
public class RoleServiceImpl extends BaseService<RoleEntity> implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMapstruct roleMapstruct;
    @Override
    public BaseDao<RoleEntity> getBaseDao() {
        return roleDao;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(String userId)
    {
        List<RoleEntity> perms = roleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (RoleEntity perm : perms)
        {
            if (StringUtils.isNotNull(perm))
            {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }
    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    @Override
    public Set<String> getRolePermission(UserDto user)
    {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.getId().equals("1"))
        {
            roles.add("admin");
        }
        else
        {
            roles.addAll(this.selectRolePermissionByUserId(user.getId()));
        }
        return roles;
    }

}
