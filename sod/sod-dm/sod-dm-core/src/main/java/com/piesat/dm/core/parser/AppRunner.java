package com.piesat.dm.core.parser;

import cn.hutool.core.util.ArrayUtil;
import com.piesat.dm.core.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

/**
 * 初始化执行
 *
 * @author cwh
 * @date 2020年 02月17日 11:34:14
 */
@Order(1) // @Order注解可以改变执行顺序，越小越先执行
@Component
public class AppRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ ArrayUtil.containsIgnoreCase(ManagerUser.sysUser.split(Constants.COMMA),"USR_SOD"));
    }
}

