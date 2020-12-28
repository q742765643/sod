package com.piesat.dm.core.model;

import jnr.ffi.annotations.In;
import lombok.Data;

import java.util.List;

/**
 * @author cwh
 * @date 2020年 12月10日 18:19:55
 */
@Data
public class TableVo {
    private String template;
    private String schema;
    private String tableName;
    private String tableDesc;
    private String partColumn;
    private Integer partDimension;
    private String partUnit;
    private List<ColumnVo> columnVos;
    private List<IndexVo> indexVos;
}
