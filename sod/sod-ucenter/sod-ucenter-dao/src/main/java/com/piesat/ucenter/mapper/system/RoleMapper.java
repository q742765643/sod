package com.piesat.ucenter.mapper.system;

import com.piesat.ucenter.entity.system.RoleEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/28 16:00
 */
@Component
public interface RoleMapper {
    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<RoleEntity> selectRolePermissionByUserId(String userId);
}
