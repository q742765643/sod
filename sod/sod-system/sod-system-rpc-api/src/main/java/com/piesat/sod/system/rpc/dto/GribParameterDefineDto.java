package com.piesat.sod.system.rpc.dto;

import com.piesat.util.BaseDto;
import lombok.Data;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/9 17:42
 */
@Data
public class GribParameterDefineDto extends BaseDto {
    /**
     * 要素存储短名
     */
    private String eleCodeShort;

    /**
     * 学科
     */
    private String subjectId;

    /**
     * 参数种类
     */
    private String classify;

    /**
     * 参数编码
     */
    private String parameterId;

    /**
     * GRIB版本
     */
    private String gribVersion;

    /**
     * 中文描述
     */
    private String elementCn;

    /**
     * 是否为共有配置
     */
    private String publicConfig;

    /**
     * 模板编号
     */
    private String templateId;

    /**
     * 模板说明
     */
    private String templateDesc;
}
