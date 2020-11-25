package com.piesat.schedule.sync.client.vo;

import lombok.Data;

/**
 * @author cwh
 * @date 2020年 11月16日 11:58:54
 */

@Data
public class SftpConfig {

    public SftpConfig(String hostname, int port, String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
    }


    /**
     * 连接地址
     */
    private String hostname;
    /**
     * 端口号
     */
    private Integer port;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 协议， 值写sftp
     */
    private String protocol = "sftp";
    /**
     * sftp服务器根路径
     */
    private String root;
    /**
     * session连接超时时间
     */
    private Integer sessionConnectTimeout = 30000;
    /**
     * channel连接超时时间
     */
    private Integer channelConnectedTimeout = 30000;
}
