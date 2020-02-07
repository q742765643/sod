package com.piesat.schedule.enums;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 17:33
 */
public enum  ExecutorBlockStrategyEnum {
    SINGLE_SERIAL("SINGLE_SERIAL"),
    TASK_SERIAL("TASK_SERIAL"),
    SINGLE_PARALLEL("SINGLE_PARALLEL"),
    CLUSTER_SERIAL("CLUSTER_SERIAL");

    private String title;
    private ExecutorBlockStrategyEnum (String title) {
        this.title = title;
    }
    public static ExecutorBlockStrategyEnum match(String name, ExecutorBlockStrategyEnum defaultItem) {
        if (name != null) {
            for (ExecutorBlockStrategyEnum item:ExecutorBlockStrategyEnum.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
        }
        return defaultItem;
    }

}
