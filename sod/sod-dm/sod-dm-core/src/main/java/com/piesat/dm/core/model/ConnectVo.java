package com.piesat.dm.core.model;

import com.piesat.dm.core.enums.DatabaseTypesEnum;
import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-26 13:53
 **/
@Data
public class ConnectVo {
    private String pid;
    private DatabaseTypesEnum databaseType;
    private String ip;
    private String className;
    private int port;
    private String userName;
    private String passWord;
    private String url;

}

