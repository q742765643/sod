package com.piesat.dm.rpc.dto.datatable;

import com.piesat.dm.entity.datatable.TableColumnEntity;
import com.piesat.dm.entity.datatable.TableIndexEntity;
import com.piesat.util.BaseDto;
import lombok.Data;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author cwh
 * @date 2020年 12月01日 15:19:19
 */
@Data
public class DataTableInfoDto extends BaseDto {
    /**
     * 数据库id
     */
    private String databaseId;

    /**
     * 表存储类型
     */
    private String storageType;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表子类型（键表、要素表）
     */
    private String tableType;

    /**
     * 表描述
     */
    private String tableDesc;

    /**
     * 中文名
     */
    private String nameCn;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 申请人
     */
    private String userId;


    private LinkedHashSet<TableColumnDto> columns;

    private LinkedHashSet<TableIndexDto> tableIndexList;
}
