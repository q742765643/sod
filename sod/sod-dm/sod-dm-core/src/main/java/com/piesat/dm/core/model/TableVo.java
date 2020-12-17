package com.piesat.dm.core.model;

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
    private String partiColumn;
    private List<ColumnVo> columnVos;
    private List<IndexVo> indexVos;
}
