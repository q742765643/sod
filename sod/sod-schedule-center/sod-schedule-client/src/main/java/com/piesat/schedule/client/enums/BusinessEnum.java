package com.piesat.schedule.client.enums;

import com.piesat.schedule.client.business.BaseBusiness;
import com.piesat.schedule.client.business.GbaseBusiness;
import com.piesat.schedule.client.business.XuguBusiness;

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
    GBASE8A("gabse8a", new GbaseBusiness());
    private String title;
    private BaseBusiness baseBusiness;

    BusinessEnum(String title, BaseBusiness baseBusiness) {
        this.title = title;
        this.baseBusiness = baseBusiness;
    }

    public static BusinessEnum match(String name, BusinessEnum defaultItem) {
        if (name != null) {
            for (BusinessEnum item : BusinessEnum.values()) {
                if (item.name().equals(name)) {
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

