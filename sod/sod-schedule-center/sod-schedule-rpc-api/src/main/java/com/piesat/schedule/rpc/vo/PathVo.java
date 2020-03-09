package com.piesat.schedule.rpc.vo;

import lombok.Data;

import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-06 12:16
 **/
@Data
public class PathVo {
    private String databaseId;
    private List<String> paths;
}

