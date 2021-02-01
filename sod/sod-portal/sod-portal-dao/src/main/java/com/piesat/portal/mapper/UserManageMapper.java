package com.piesat.portal.mapper;

import com.piesat.portal.entity.UserManageEntity;
import org.springframework.stereotype.Component;

@Component
public interface UserManageMapper {
    void updateUser(UserManageEntity userManageEntity);
}
