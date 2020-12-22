package com.piesat.dm.core.parser;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author cwh
 * @date 2020年 12月18日 17:56:32
 */
@Component
@PropertySource(value = {"classpath:config.yml"}, factory = YamlConfigFactory.class)
@ConfigurationProperties(prefix = "database")
public class ManagerUser {
    public static String sysUser;

    /**
     * 注入
     * @param sys_users
     */
    @Autowired(required = false)
    public void setUploadPath(@Value("${database.sys_users}")String sys_users) {
        ManagerUser.sysUser = sys_users;
    }
}
