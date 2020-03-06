package com.piesat.schedule.client.vo;

import com.piesat.util.ResultT;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-07 17:13
 **/
@Slf4j
@Data
public class StrategyVo {

    private String tempPtah="";
    private String vfileName="";
    private String kfilePath="";
    private String vfilePath="";
    private String indexPath="";
    private String tempZipPath="";
    private String realFileName="";
    private long startTime;
    private long endTime;
    private StringBuilder handMsg=new StringBuilder();
    private Integer sort;
    private List<String> deleteFileList=new ArrayList<>();

    public void appendSuccess(String msg){
        this.handMsg.append(msg+"</br>");
        log.info(msg);
    }
    public void appendErro(String msg,ResultT<String> resultT){
        this.handMsg.append(msg+"</br>");
        resultT.setErrorMessage(msg);
        log.error(msg);
    }


}

