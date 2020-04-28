package com.piesat.common.utils;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * test
 *
 * @author cwh
 * @date 2020年 04月26日 10:53:33
 */
public class Test {
    public static void main(String[] args) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //加密所需的salt(盐)
        textEncryptor.setPassword("HT95279527");
        //要加密的数据（数据库的用户名或密码）
        String username = textEncryptor.encrypt("USR_SOD");
        String password = textEncryptor.encrypt("Pnmic_qwe123");
        System.out.println("username:"+username);
        System.out.println("password:"+password);
    }
}
