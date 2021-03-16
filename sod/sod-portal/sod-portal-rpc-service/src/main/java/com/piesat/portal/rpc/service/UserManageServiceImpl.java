package com.piesat.portal.rpc.service;

import com.github.pagehelper.util.StringUtil;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.IdUtils;
import com.piesat.common.utils.MD5Util;
import com.piesat.common.utils.StringUtils;
import com.piesat.portal.dao.UserManageDao;
import com.piesat.portal.dao.UserRoleManageDao;
import com.piesat.portal.entity.UserManageEntity;
import com.piesat.portal.entity.UserRoleManageEntity;
import com.piesat.portal.mapper.RoleManageMapper;
import com.piesat.portal.mapper.UserManageMapper;
import com.piesat.portal.rpc.api.DepartManageService;
import com.piesat.portal.rpc.api.UserManageService;
import com.piesat.portal.rpc.dto.DepartManageDto;
import com.piesat.portal.rpc.dto.UserManageDto;
import com.piesat.portal.rpc.mapstruct.UserManageMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("userManageService")
public class UserManageServiceImpl extends BaseService<UserManageEntity> implements UserManageService {

    @Autowired
    private UserManageDao userManageDao;

    @Autowired
    private UserManageMapstruct userManageMapstruct;

    @Autowired
    private DepartManageService departManageService;

    @Autowired
    private UserManageMapper userManageMapper;

    @Autowired
    private RoleManageMapper roleManageMapper;

    @Autowired
    private UserRoleManageDao userRoleManageDao;

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
        if(StringUtils.isNotEmpty(userManageDto.getUserLevel())){
            specificationBuilder.add("userLevel", SpecificationOperator.Operator.eq.name(),userManageDto.getUserLevel());
        }
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,null);
        List<UserManageEntity> userManageEntities = (List<UserManageEntity>) pageBean.getPageData();
        List<UserManageDto> userManageDtos = userManageMapstruct.toDto(userManageEntities);
        //查询部门信息
        List<DepartManageDto> departManageDtos = departManageService.findAllDept();

        if(userManageDtos != null && userManageDtos.size()>0 && departManageDtos != null && departManageDtos.size()>0){
            for(UserManageDto userManage : userManageDtos){
                for(DepartManageDto departManageDto : departManageDtos){
                    if(departManageDto.getDeptunicode().equals(userManage.getDeptunicode())){
                        userManage.setDeptName(departManageDto.getDeptname());
                        break;
                    }
                }
            }
        }
        pageBean.setPageData(userManageDtos);
        return pageBean;
    }

    @Override
    public UserManageDto getDotById(String id) {
        UserManageEntity userManageEntity = this.getById(id);
        UserManageDto userManageDto = this.userManageMapstruct.toDto(userManageEntity);
        String deptunicode = userManageDto.getDeptunicode();
        List<DepartManageDto> departManageDtos = departManageService.findByDeptunicode(deptunicode);
        if(departManageDtos != null && departManageDtos.size()>0){
            String deptName = "";
            deptName = getAllDeptName(deptName,departManageDtos.get(0));
            userManageDto.setDeptName(deptName);
        }
        //获取用户角色
        List<String> roles = roleManageMapper.selectRoleListByUserId(id);
        userManageDto.setRoleIds(roles.toArray(new String[roles.size()]));
        return userManageDto;
    }

    private String  getAllDeptName(String deptName,DepartManageDto dept){
        if(dept.getDeptcode().equals(dept.getParentCode())){
            return dept.getDeptname()+"-"+deptName;
        }else{
            if(StringUtil.isEmpty(deptName)){
                deptName = dept.getDeptname();
            }else{
                deptName = dept.getDeptname() + "-"+deptName;
            }
            List<DepartManageDto> parent = departManageService.findByDeptcode(dept.getParentCode());
            if(null != parent && parent.size()>0){
                return getAllDeptName(deptName,parent.get(0));
            }else{
                return deptName;
            }
        }
    }

    @Override
    public UserManageDto updateDto(UserManageDto userManageDto) {
        UserManageEntity userManageEntity = this.getById(userManageDto.getId());
        if(StringUtils.isNotEmpty(userManageDto.getIscheck())){
            userManageEntity.setIscheck(userManageDto.getIscheck());
        }
        if(userManageEntity.getVersion()==null){
            userManageEntity.setVersion(0);
        }else{
            userManageEntity.setVersion(userManageEntity.getVersion()+1);
        }
        //userManageEntity = this.saveNotNull(userManageEntity);
        userManageMapper.updateUser(userManageEntity);
        return userManageMapstruct.toDto(userManageEntity);
    }

    @Override
    public UserManageDto resetPwd(UserManageDto userManageDto) {
        UserManageEntity userManageEntity = userManageMapstruct.toEntity(userManageDto);
        String password = userManageEntity.getPassword();
        password = MD5Util.MD5Encode(password).toUpperCase();
        userManageEntity.setPassword(password);
        //userManageEntity = this.saveNotNull(userManageEntity);
        userManageMapper.updateUser(userManageEntity);
        return userManageMapstruct.toDto(userManageEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserManageDto editUserRole(UserManageDto userManageDto) {
        // 删除用户与角色关联
        userRoleManageDao.deleteByUserId(userManageDto.getId());
        // 新增用户与角色管理
        insertUserRole(userManageDto);
        return null;
    }

    public void insertUserRole(UserManageDto user) {
        String[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles)) {
            // 新增用户与角色管理
            List<UserRoleManageEntity> list = new ArrayList<>();
            for (String roleId : roles) {
                UserRoleManageEntity ur = new UserRoleManageEntity();
                ur.setUserId(user.getId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleManageDao.saveNotNullAll(list);
            }
        }
    }

    @Override
    public UserManageDto saveDto(UserManageDto userManageDto) {
        UserManageEntity userManageEntity = userManageMapstruct.toEntity(userManageDto);
        if(StringUtils.isEmpty(userManageEntity.getId())){
            userManageEntity.setId(IdUtils.simpleUUID());
        }
        userManageEntity.setUpdateTime(new Date());
        userManageEntity = this.saveNotNull(userManageEntity);
        return userManageMapstruct.toDto(userManageEntity);
    }

}
