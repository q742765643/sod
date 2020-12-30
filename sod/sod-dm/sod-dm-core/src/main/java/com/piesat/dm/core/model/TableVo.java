package com.piesat.dm.core.model;

import com.piesat.dm.core.constants.Constants;
import jnr.ffi.annotations.In;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

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
    private String primaryKeyStr;
    private List<IndexVo> indexVos;

    public void setColumnVos(List<ColumnVo> columnVos) {
        this.columnVos = columnVos;
        if (columnVos!=null){
            primaryKeyStr = columnVos.stream()
                    .filter(ColumnVo::getIsPrimaryKey)
                    .map(ColumnVo::getColumnName)
                    .collect(Collectors.joining(Constants.COMMA));
        }
    }
}