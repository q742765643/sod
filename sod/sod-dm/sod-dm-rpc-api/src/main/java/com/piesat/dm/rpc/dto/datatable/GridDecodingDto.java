package com.piesat.dm.rpc.dto.datatable;

import lombok.Data;

import java.util.Date;

/**
 * 解码配置
 *
 * @author cwh
 * @date 2020年 02月12日 11:19:06
 */
@Data
public class GridDecodingDto {
    private static final long serialVersionUID = 1L;

    private String id;

    private String gridDecodeId;

    private String dataServiceId;

    private String dataDpcId;

    private String eleCodeShort;

    private int subjectId;

    private int classify;

    private int parameterId;

    private int gribVersion;

    private String elementCn;

    private String publicConfig;

    private String templateId;

    private Date createTime;

    private Date updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private Integer version;
}
