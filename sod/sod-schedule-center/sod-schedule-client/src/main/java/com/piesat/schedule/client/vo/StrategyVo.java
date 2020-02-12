package com.piesat.schedule.client.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-07 17:13
 **/
@Data
public class StrategyVo {

    private String tempPtah;
    private String vfileName;
    private String kfilePath;
    private String vfilePath;
    private String indexPath;
    private String tempZipPath;
    private String realFileName;
    private long startTime;
    private long endTime;
    private StringBuilder handMsg;
    private Integer sort;
    private List<String> deleteFileList=new ArrayList<>();


}

