package com.piesat.dm.rpc.dto.datatable;

import lombok.Data;

import java.util.List;

/**
 * 分库分表建数组
 *
 * @author cwh
 * @date 2020年 02月19日 11:00:49
 */
@Data
public class ShardingList {

    private static final long serialVersionUID = 1L;

    private List<ShardingDto> shardingList;
}
