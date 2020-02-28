package com.piesat.schedule.client.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-27 15:06
 **/
@Data
public class MetadataVo {
    private List<String> schema = new ArrayList<>();
    private List<String> table = new ArrayList<>();
    private List<String> view = new ArrayList<>();
    private List<String> sequence = new ArrayList<>();
    private List<String> procedure = new ArrayList<>();
    private List<String> trigger = new ArrayList<>();
    private List<String> users = new ArrayList<>();
    private List<String> roles = new ArrayList<>();

    private String indexPath="";
    private String parentPath="";
    private String zipPath="";
    private boolean isExpData=true;

    private long startTime;

    private long endTime;
}

