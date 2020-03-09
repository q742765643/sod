package com.piesat.schedule.client.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-02 13:21
 **/
@Data
public class RecoverMetaVo {
    private Set<String> schema = new HashSet<>();
    private Set<String> table = new HashSet<>();
    private Set<String> view = new HashSet<>();
    private Set<String> sequence = new HashSet<>();
    private Set<String> procedure = new HashSet<>();
    private Set<String> trigger = new HashSet<>();
    private Set<String> users = new HashSet<>();
    private Set<String> roles = new HashSet<>();
    private String unzipPath="";
    private String indexPath="";
    private long startTime;

    private long endTime;
}

