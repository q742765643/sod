package com.piesat.dm.rpc.dto;

import com.piesat.util.BaseDto;
import lombok.Data;

/**
 * @author yaya
 * @description TODO
 * @date 2020/4/16 11:04
 */
@Data
public class ConsistencyCheckHistoryDto extends BaseDto {
    /**
     * 与数据库关联id
     * database_id
     */
    private String databaseId;

    /**
     * 文件路径
     */
    private String fileDirectory;

    /**
     * 文件名称
     */
    private String fileName;
}
