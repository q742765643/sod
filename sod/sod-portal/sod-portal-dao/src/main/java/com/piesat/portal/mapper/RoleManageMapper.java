package com.piesat.portal.mapper;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RoleManageMapper {
    public List<String> selectRoleListByUserId(String userId);
}
