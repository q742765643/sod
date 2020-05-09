package com.piesat.schedule.client.vo;

import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-26 13:53
 **/
@Data
public class ConnectVo {
    private String ip;
    private int port;
    private String userName;
    private String passWord;
    private String url;

}

