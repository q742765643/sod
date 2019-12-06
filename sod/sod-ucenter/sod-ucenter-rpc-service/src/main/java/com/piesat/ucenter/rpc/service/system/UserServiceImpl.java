package com.piesat.ucenter.rpc.service.system;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.utils.StringUtils;
import com.piesat.ucenter.dao.system.UserDao;
import com.piesat.ucenter.dao.system.UserRoleDao;
import com.piesat.ucenter.entity.system.UserEntity;
import com.piesat.ucenter.entity.system.UserRoleEntity;
import com.piesat.ucenter.mapper.system.RoleMapper;
import com.piesat.ucenter.mapper.system.UserMapper;
import com.piesat.ucenter.rpc.api.system.UserService;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.ucenter.rpc.mapstruct.system.UserMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/21 17:13
 */
@Service
public class UserServiceImpl extends BaseService<UserEntity> implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserMapstruct userMapstruct;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public BaseDao<UserEntity> getBaseDao() {
        return userDao;
    }
    @Override
    public UserDto saveUserDto(UserDto userDto){
        UserEntity userEntity= userMapstruct.toEntity(userDto);
        userEntity=this.save(userEntity);
        userEntity= userMapper.selectByPrimaryKey("1");
        PageHelper.startPage(1,10);
        userMapper.selectByPrimaryKey("1");
        return userMapstruct.toDto(userEntity);

    }
    /**
     *@描述 根据用户名查找用户
     *@参数 [userName]
     *@返回值 com.piesat.ucenter.rpc.dto.system.UserDto
     *@author zzj
     *@创建时间 2019/11/28 16:38 
     **/
    @Override
    public UserDto selectUserByUserName(String userName){
        UserEntity userEntity=userDao.findByUserName(userName);
        return userMapstruct.toDto(userEntity);
    }

    /**
     * 根据条件分页查询用户列表
     *
     * @param
     * @return 用户信息集合信息
     */
    @Override
    public PageBean selectUserList(PageForm<UserDto> pageForm)
    {
        UserEntity userEntity=userMapstruct.toEntity(pageForm.getT());
        PageHelper.startPage(pageForm.getCurrentPage(),pageForm.getPageSize());
        List<UserEntity> userEntities=userMapper.selectUserList(userEntity);
        PageInfo<UserEntity> pageInfo = new PageInfo<>(userEntities);
        List<UserDto> userDtos= userMapstruct.toDto(pageInfo.getList());
        PageBean pageBean=new PageBean(pageInfo.getTotal(),pageInfo.getPageSize(),userDtos);
        return pageBean;
    }
    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public void insertUser(UserDto user)
    {
        UserEntity userEntity=userMapstruct.toEntity(user);
        String password = new Md5Hash(userEntity.getPassword(),user.getUserName(),2).toString();
        userEntity.setPassword(password);
        userEntity=this.save(userEntity);
        // 新增用户岗位关联
        //insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(userEntity);
    }
    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(UserEntity user)
    {
        String[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles))
        {
            // 新增用户与角色管理
            List<UserRoleEntity> list = new ArrayList<>();
            for (String roleId : roles)
            {
                UserRoleEntity ur = new UserRoleEntity();
                ur.setUserId(user.getId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0)
            {
                userRoleDao.saveAll(list);
            }
        }
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public void updateUser(UserDto user)
    {
        UserEntity userEntity=userMapstruct.toEntity(user);
        userEntity=this.save(userEntity);
        String userId = user.getId();
        // 删除用户与角色关联
        userRoleDao.deleteByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(userEntity);
    }
    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public void updateUserStatus(UserDto user)
    {
        UserEntity userEntity=this.getById(user.getId());
        userEntity.setStatus(user.getStatus());
        this.save(userEntity);
    }
    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    public void deleteUserByIds(List<String> userIds)
    {
     /*   for (Long userId : userIds)
        {
            checkUserAllowed(new SysUser(userId));
        }*/
        this.deleteByIds(userIds);
    }
    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public UserDto selectUserById(String userId)
    {
        UserEntity userEntity=this.getById(userId);
        List<String> roles=roleMapper.selectRoleListByUserId(userId);
        userEntity.setRoleIds( roles.toArray(new String[roles.size()]));
        return userMapstruct.toDto(userEntity);
    }
}
