package com.piesat.sod.system.rpc.dto;

import lombok.Data;

import javax.persistence.Column;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/13 15:11
 */
@Data
public class ServiceCodeDto {

    /**
     * 服务代码
     */
    private String userEleCode;

    /**
     * 字段编码
     */
    private String dbEleCode;

    /**
     * 要素中文名称
     */
    private String eleName;

    /**
     * 描述
     */
    private String description;

    /**
     *
     */
    private String hasSod;

    /**
     *  单位
     */
    private String eleUnit;

    /**
     *  是否有标识代码表
     */
    private String isCodeParam;

    /**
     *  标识代码表
     */
    private String codeTableId;
}
