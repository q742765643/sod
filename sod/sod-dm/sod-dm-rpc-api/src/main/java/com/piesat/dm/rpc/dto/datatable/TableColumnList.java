package com.piesat.dm.rpc.dto.datatable;

import lombok.Data;

import java.util.List;

/**
 * 字段数组
 *
 * @author cwh
 * @date 2020年 02月19日 11:02:19
 */
@Data
public class TableColumnList {

    private static final long serialVersionUID = 1L;

    private List<TableColumnDto> tableColumnList;
}
