package com.piesat.schedule.client.enums;

import com.piesat.schedule.client.business.*;

/**
 * @program: SyncMaster
 * @description:
 * @author: zzj
 * @create: 2018-12-25 16:25
 **/
public enum BusinessEnum {
    //ADS备份逻辑
    //ALI_ADS("ads", ""),
    //虚谷备份逻辑
    XUGU("xugu", new XuguBusiness()),
    //DRDS备份逻辑
    //ALI_DRDS("stdb", ""),
    //GBASE备份逻辑
    GBASE8A("gbase8a", new GbaseBusiness()),
    CASSANDRA("cassandra",new CassandraBusiness()),
    //postgresql逻辑
    POSTGRESQL("postgresql",new PostgresqlBusiness());

    private String title;
    private BaseBusiness baseBusiness;

    BusinessEnum(String title, BaseBusiness baseBusiness) {
        this.title = title;
        this.baseBusiness = baseBusiness;
    }

    public static BusinessEnum match(String name, BusinessEnum defaultItem) {
        if (name != null) {
            for (BusinessEnum item : BusinessEnum.values()) {
                if (item.name().toUpperCase().equals(name.toUpperCase())) {
                    return item;
                }
            }
        }
        return defaultItem;
    }

    public String getTitle() {
        return title;
    }

    public BaseBusiness getBaseBusiness() {
        return baseBusiness;
    }
}

