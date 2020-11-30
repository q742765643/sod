package com.piesat.schedule.sync.client.vo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

/**
 * @author cuiwenhui
 */
@Data
public class FtpConfig {

    public FtpConfig(String url, int port, String username, String password) {
        this.url = url;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * 上传文件名
     */
    private String fileName;
    /**
     * ftp服务器地址
     */
    private String url;

    /**
     * ftp服务器端口
     */
    private int port;

    /**
     * ftp服务器用户名
     */
    private String username;

    /**
     * ftp服务器密码
     */
    private String password;

    /**
     * ftp服务器存放文件的路径
     */
    private String remotePath;

    /**
     * 本地需要上传的文件的路径
     */
    private String localDir;

    /**
     * 下载文件时，存放在本地的路径
     */
    private String downDir;

}
