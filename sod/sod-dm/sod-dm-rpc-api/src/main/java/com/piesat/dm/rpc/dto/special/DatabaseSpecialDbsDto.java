package com.piesat.dm.rpc.dto.special;

import com.piesat.util.BaseDto;
import lombok.Data;

import javax.persistence.Column;

/**
 * @author cwh
 * @version 1.0.0
 * @ClassName DatabaseSpecialDbsDto.java
 * @Description TODO
 * @createTime 2021年04月04日 14:17:00
 */
@Data
public class DatabaseSpecialDbsDto  extends BaseDto {
    private static final long serialVersionUID = 1L;

    /**
     * 申请空间(GB)
     */
    private Integer sizeOfSpace;

    /**
     * 已用空间(GB)
     */
    private Integer usedSpace;

    /**
     * 专题库类型(DB,NAS两种)
     */
    private String dbType;

    private String databaseId;
}
