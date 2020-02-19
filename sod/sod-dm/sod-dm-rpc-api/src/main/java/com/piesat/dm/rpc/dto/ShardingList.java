package com.piesat.dm.rpc.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 分库分表建数组
 *
 * @author cwh
 * @date 2020年 02月19日 11:00:49
 */
@Data
public class ShardingList {
    private List<ShardingDto> shardingList = new ArrayList<>();
}
