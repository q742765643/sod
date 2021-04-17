package com.piesat.portal.rpc.api;

import com.piesat.portal.rpc.dto.UserManageDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;

/**
 * 用户管理
 */
public interface UserManageService {
    PageBean selectPageList(PageForm<UserManageDto> pageForm);

    void delete(String id);

    UserManageDto getDotById(String id);

    UserManageDto updateDto(UserManageDto userManageDto);

    UserManageDto saveDto(UserManageDto userManageDto);

    UserManageDto resetPwd(UserManageDto userManageDto);

    UserManageDto editUserRole(UserManageDto userManageDto);

    List<UserManageDto> getSubUsers(String id);

    List<UserManageDto> getBusinessUser(String id);

}
