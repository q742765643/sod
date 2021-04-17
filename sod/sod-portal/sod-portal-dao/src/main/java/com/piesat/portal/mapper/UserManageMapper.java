package com.piesat.portal.mapper;

import com.piesat.portal.entity.UserManageEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserManageMapper {
    void updateUser(UserManageEntity userManageEntity);

    List<UserManageEntity> getSubUsersC(String id);

    List<UserManageEntity> getSubUsersP(String id);

    List<UserManageEntity> getBusinessUserP(List<String> deptunicodes);

    List<UserManageEntity> getBusinessUserC(List<String> orgCodePaths);
}
