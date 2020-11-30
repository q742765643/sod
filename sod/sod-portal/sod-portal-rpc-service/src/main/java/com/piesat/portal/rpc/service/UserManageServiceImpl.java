package com.piesat.portal.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.MD5Util;
import com.piesat.common.utils.StringUtils;
import com.piesat.portal.dao.UserManageDao;
import com.piesat.portal.entity.UserManageEntity;
import com.piesat.portal.rpc.api.UserManageService;
import com.piesat.portal.rpc.dto.UserManageDto;
import com.piesat.portal.rpc.mapstruct.UserManageMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userManageService")
public class UserManageServiceImpl extends BaseService<UserManageEntity> implements UserManageService {

    @Autowired
    private UserManageDao userManageDao;

    @Autowired
    private UserManageMapstruct userManageMapstruct;

    @Override
    public BaseDao<UserManageEntity> getBaseDao() {
        return userManageDao;
    }

    @Override
    public PageBean selectPageList(PageForm<UserManageDto> pageForm) {
        UserManageDto userManageDto = pageForm.getT();
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotEmpty(userManageDto.getUserName())){
            specificationBuilder.add("userName", SpecificationOperator.Operator.likeAll.name(),userManageDto.getUserName());
        }
        if(StringUtils.isNotEmpty(userManageDto.getLoginName())){
            specificationBuilder.add("loginName", SpecificationOperator.Operator.eq.name(),userManageDto.getLoginName());
        }
        if(StringUtils.isNotEmpty(userManageDto.getIscheck())){
            specificationBuilder.add("ischeck", SpecificationOperator.Operator.eq.name(),userManageDto.getIscheck());
        }

        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,null);
        List<UserManageEntity> userManageEntities = (List<UserManageEntity>) pageBean.getPageData();
        List<UserManageDto> userManageDtos = userManageMapstruct.toDto(userManageEntities);
        pageBean.setPageData(userManageDtos);
        return pageBean;
    }

    @Override
    public UserManageDto getDotById(String id) {
        UserManageEntity userManageEntity = this.getById(id);
        return this.userManageMapstruct.toDto(userManageEntity);
    }

    @Override
    public UserManageDto updateDto(UserManageDto userManageDto) {
        UserManageEntity userManageEntity = userManageMapstruct.toEntity(userManageDto);
        userManageEntity = this.saveNotNull(userManageEntity);
        return userManageMapstruct.toDto(userManageEntity);
    }

    @Override
    public UserManageDto resetPwd(UserManageDto userManageDto) {
        UserManageEntity userManageEntity = userManageMapstruct.toEntity(userManageDto);
        String password = userManageEntity.getPassword();
        password = MD5Util.MD5Encode(password);
        userManageEntity.setPassword(password);
        userManageEntity = this.saveNotNull(userManageEntity);
        return userManageMapstruct.toDto(userManageEntity);
    }
}
