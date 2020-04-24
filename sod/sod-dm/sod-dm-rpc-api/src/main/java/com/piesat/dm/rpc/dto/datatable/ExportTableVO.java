package com.piesat.dm.rpc.dto.datatable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @Description
 * @ClassName ExportTableVO
 * @author wulei
 * @date 2020/4/23 14:30
 */
@Api("表结构导出VO")
@Data
public class ExportTableVO {
    @ApiParam("数据库用途ID")
    private String use_id;
    @ApiParam("数据库ID")
    private String database_id;
    @ApiParam("存储编码字符串，逗号分隔")
    private String data_class_ids;
    @ApiParam("SQL导出类型，1单文件，2多文件")
    private String exportType;
    @ApiParam("字段，逗号分隔")
    private String columns;
    @ApiParam("是否导出索引，导出传0，不导出传1")
    private String index;
    @ApiParam("是否导出分库分表，导出传0，不导出传1")
    private String shard;
}
