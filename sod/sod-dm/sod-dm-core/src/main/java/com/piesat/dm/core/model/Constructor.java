package com.piesat.dm.core.model;

import com.piesat.dm.core.enums.DagTypeEnum;
import lombok.Data;

/**
 * @author cwh
 * @date 2020年 12月03日 18:35:36
 */
@Data
public class Constructor {
    private DagTypeEnum dagType;
    private ConnectVo connectVo;
    /**
     * 数据库唯一标识
     */
    private String pid;
    private String schema;
    private String tableName;
    private AuthorityVo authorityVo;
    private UserInfo userInfo;
    private ColumnVo column;
}
