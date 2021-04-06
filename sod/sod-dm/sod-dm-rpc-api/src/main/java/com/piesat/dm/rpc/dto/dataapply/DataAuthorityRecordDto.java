package com.piesat.dm.rpc.dto.dataapply;

import com.piesat.util.BaseDto;
import lombok.Data;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/10 15:40
 */
@Data
public class DataAuthorityRecordDto extends BaseDto {
    /**
     * 申请id
     */
    private String applyId;

    /**
     * 表id
     */
    private String tableId;

    /**
     * 模式
     */
    private String schema;

    /**
     * 申请权限
     * 1:读权限 2：写权限
     */
    private Integer applyAuthority;

    /**
     * 授权
     * 1:允许 2：禁止
     */
    private Integer authorize;

    /**
     * 拒绝原因
     * 当授权为2时需填写拒绝原因
     */
    private String cause;


    private String typeName;
    private String className;
    private String tableName;
    private String databaseName;
    private String specialDatabaseName;
}
