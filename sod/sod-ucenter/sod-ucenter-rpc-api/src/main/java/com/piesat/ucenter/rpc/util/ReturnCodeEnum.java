package com.piesat.ucenter.rpc.util;

/**
 * @program: SyncMaster
 * @description:返回工具类
 * @author: zzj
 * @create: 2018-12-18 14:45
 **/
public enum ReturnCodeEnum {
    //成功
    SUCCESS(0, "成功"),
    //失败
    FIAL(1, " 失败"),

    /**
     * ==========101 100===============
     **/
    ReturnCodeEnum_101_ERROR(101, "获取任务详情失败#1#1"),

    ReturnCodeEnum_12_ERROR(12, "删除分区异常#检查虚谷数据库是否产生异常#1.虚谷数据库产生错误");








    private int key;
    private String value;

    ReturnCodeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValue(int key) {
        for (ReturnCodeEnum st : ReturnCodeEnum.values()) {
            if (key == st.key) {
                return st.value;
            }
        }
        return "";
    }
    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
