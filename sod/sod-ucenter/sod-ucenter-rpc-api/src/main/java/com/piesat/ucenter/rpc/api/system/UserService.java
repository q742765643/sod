package com.piesat.ucenter.rpc.api.system;

import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/21 17:10
 */
public interface UserService {
    public UserDto saveUserDto(UserDto userDto);


    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    public UserDto selectUserByUserName(String userName);

    /**
     * 根据条件分页查询用户列表
     *
     * @param
     * @return 用户信息集合信息
     */
    PageBean selectUserList(PageForm<UserDto> pageForm);
}
