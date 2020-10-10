package com.piesat.dm.rpc.dto.special;

import com.piesat.common.jpa.entity.BaseEntity;
import com.piesat.util.BaseDto;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 专题库管理资料
 *
 * @author wulei
 * @date 2020年 2月12日 17:12:47
 */
@Data
public class DatabaseSpecialReadWriteDto extends BaseDto {
    private static final long serialVersionUID = 1L;

    /**
     * 专题库id
     */
    private String sdbId;

    /**
     * 存储编码
     */
    private String dataClassId;

    /**
     * 物理库Id
     */
    private String databaseId;

    /**
     * 申请权限
     */
    private Integer applyAuthority;

    /**
     * 授权权限
     */
    private Integer empowerAuthority;

    /**
     * 审核人
     */
    private String examiner;

    /**
     * 审核状态
     * 1：允许 2：禁止 3:待授权
     */
    private Integer examineStatus;

    /**
     * 审核时间
     */
    private Date examineTime;

    /**
     * 拒绝原因
     */
    private String failureReason;

    /**
     * 区分自建还是申请
     */
    private Integer dataType;

    /**
     * 分类id
     * 资料归属哪个分类下面
     */
    private String typeId;

    private String userId;

	@ApiParam("资料分类")
    private String typeName;

    @ApiParam("资料名称")
    private String dataName;

    @ApiParam("表名称")
    private String tableName;

    @ApiParam("数据库名称")
    private String databaseName;

    @ApiParam("专题库名称")
    private String sdbName;

    @ApiParam("四级编码")
    private String dDataId;

    //接受前端条件查询参数 通过/拒绝
    private String applyAuthorityString;
    private String examineStatusString;
}
