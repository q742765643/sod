package com.piesat.ucenter.rpc.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/13 18:05
 */
@Data
public class User implements Serializable {
    String name;
    int age;
    List<String> list;
    Map<String,String> map;
    Date date=new Date();

}
