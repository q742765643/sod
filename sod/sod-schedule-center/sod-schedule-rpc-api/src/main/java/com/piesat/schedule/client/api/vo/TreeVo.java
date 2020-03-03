package com.piesat.schedule.client.api.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-24 14:44
 **/
@Data
public class TreeVo {
    @JsonProperty
    private String pId;
    private String id;
    private String name;
    private  boolean checked;
    @JsonProperty
    private boolean isParent;

    public TreeVo(String pId, String id, String name){
        this.pId=pId;
        this.id=id;
        this.name=name;

    }

    public TreeVo(){

    }
}

