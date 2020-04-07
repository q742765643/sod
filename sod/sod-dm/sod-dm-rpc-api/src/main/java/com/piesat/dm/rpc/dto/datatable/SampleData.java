package com.piesat.dm.rpc.dto.datatable;

import lombok.Data;

import java.util.List;

/**
 * 样例数据
 *
 * @author cwh
 * @date 2020年 02月13日 16:28:41
 */
@Data
public class SampleData {
    private String tableName;
    private String databaseId;
    private List<String> column;
}
