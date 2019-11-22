package com.piesat.ucenter.rpc.api.system;

import com.piesat.ucenter.rpc.dto.system.UserDto;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/21 17:10
 */
public interface UserService {
    public UserDto saveUserDto(UserDto userDto);
}
