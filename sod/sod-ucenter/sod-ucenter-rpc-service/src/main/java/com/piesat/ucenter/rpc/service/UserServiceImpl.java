package com.piesat.ucenter.rpc.service;

import com.piesat.ucenter.rpc.api.UserService;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/12 17:24
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public String test() {
        return "sddddd";
    }
}
