package com.piesat.schedule.client.api.vo;

import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-24 14:44
 **/
@Data
public class TreeVo {

    private String pId;
    private String id;
    private String name;
    private  boolean checked;
    private boolean isParent;

    public TreeVo(String pId,String id,String name,boolean isParent){
        this.pId=pId;
        this.id=id;
        this.name=name;
        this.isParent=isParent;

    }

    public TreeVo(){

    }
}

