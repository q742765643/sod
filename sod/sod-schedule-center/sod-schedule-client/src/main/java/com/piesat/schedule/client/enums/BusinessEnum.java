package com.piesat.schedule.client.enums;
/**
 * @program: SyncMaster
 * @description:
 * @author: zzj
 * @create: 2018-12-25 16:25
 **/
public enum BusinessEnum {
    //ADS备份逻辑
    ALI_ADS("ads", ""),
    //虚谷备份逻辑
    XUGU("xugu", ""),
    //DRDS备份逻辑
    ALI_DRDS("stdb", ""),
    //GBASE备份逻辑
    GBASE8A("gabse8a", "");
    private String title;
    private String baseBusiness;

    BusinessEnum(String title, String baseBusiness) {
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

    public String getBaseBusiness() {
        return baseBusiness;
    }
}

