package com.piesat.portal.rpc.api;

import com.piesat.portal.rpc.dto.RoleManageDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;

/**
 * 角色管理
 */
public interface RoleManageService {
    public PageBean selectPageList(PageForm<RoleManageDto> pageForm);

    RoleManageDto saveDto(RoleManageDto roleManageDto);

    RoleManageDto getDotById(String id);

    RoleManageDto updateDto(RoleManageDto roleManageDto);

    void deleteRoleByIds(List<String> list);

    RoleManageDto updateRoleStatus(RoleManageDto roleManageDto);

    List<RoleManageDto> findAllRole();
}
