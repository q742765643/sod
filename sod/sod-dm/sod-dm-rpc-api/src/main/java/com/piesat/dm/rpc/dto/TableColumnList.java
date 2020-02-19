package com.piesat.dm.rpc.dto;

import lombok.Data;

import javax.swing.table.TableColumn;
import java.util.ArrayList;
import java.util.List;

/**
 * 字段数组
 *
 * @author cwh
 * @date 2020年 02月19日 11:02:19
 */
@Data
public class TableColumnList {
    private List<TableColumnDto> tableColumnList = new ArrayList<>();
}
