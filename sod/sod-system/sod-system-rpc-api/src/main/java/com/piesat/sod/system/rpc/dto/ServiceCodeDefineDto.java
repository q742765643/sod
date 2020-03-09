package com.piesat.sod.system.rpc.dto;

import com.piesat.util.BaseDto;
import lombok.Data;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/9 14:04
 */
@Data
public class ServiceCodeDefineDto extends BaseDto {

    /**
     * 要素服务代码
     */
    private String userFcstEle;

    /**
     * 要素存储短名
     */
    private String dbFcstEle;

    /**
     * 要素名（要素长名）
     */
    private String elePropertyName;

    /**
     * 要素单位
     */
    private String eleUnit;

    /**
     * 中文名称
     */
    private String eleName;

}
