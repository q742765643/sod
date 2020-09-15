package com.piesat.schedule.rpc.service.nas;

import com.piesat.schedule.rpc.service.nas.quota.BaseQuota;
import com.piesat.schedule.rpc.service.nas.quota.HauweiQuota;

/**
 * @ClassName : QuotaEnum
 * @Description :
 * @Author : zzj
 * @Date: 2020-09-14 18:01
 */
public enum QuotaEnum {
    HUAWEI("HUAWEI", new HauweiQuota());
    private String title;
    private BaseQuota baseQuota;
    QuotaEnum(String title, BaseQuota baseQuota) {
        this.title = title;
        this.baseQuota = baseQuota;
    }
    public static QuotaEnum match(String name, QuotaEnum defaultItem) {
        if (name != null) {
            for (QuotaEnum item : QuotaEnum.values()) {
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

    public void setTitle(String title) {
        this.title = title;
    }

    public BaseQuota getBaseQuota() {
        return baseQuota;
    }

    public void setBaseQuota(BaseQuota baseQuota) {
        this.baseQuota = baseQuota;
    }
}

