package com.piesat.schedule.rpc.vo;

import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-31 09:47
 **/
@Data
public class Server {
    private String host;
    private Integer httpPort;
    private Integer grpcPort;
    private Integer limit=100;
    private Integer use;
}

