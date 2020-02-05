package com.piesat.schedule.rpc.dto.sync;

import com.piesat.util.BaseDto;
import lombok.Data;

/**
 * @author yaya
 * @description TODO
 * @date 2020/1/17 10:18
 */
@Data
public class SyncFilterDto{

    /**
     * id
     */
    private Integer id;
    /**
     * 列名
     */
    private String columnName;

    /**
     * 列名对应的过滤值
     */
    private String filterValues;

    /**
     * 列的操作符
     */
    private String columnOper;
}
