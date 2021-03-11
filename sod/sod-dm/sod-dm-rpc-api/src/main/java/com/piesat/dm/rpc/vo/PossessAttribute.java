package com.piesat.dm.rpc.vo;

import lombok.Data;

/**
 * @author cwh
 * @program: sod
 * @description:
 * @date 2020年 12月29日 14:14:34
 */
@Data
public class PossessAttribute {
    private Boolean haveLength;
    private Boolean haveAccuracy;
    private Boolean haveIsNull;

    public PossessAttribute(Boolean haveLength, Boolean haveAccuracy, Boolean haveIsNull) {
        this.haveAccuracy = haveAccuracy;
        this.haveLength = haveLength;
        this.haveIsNull = haveIsNull;
    }
}
