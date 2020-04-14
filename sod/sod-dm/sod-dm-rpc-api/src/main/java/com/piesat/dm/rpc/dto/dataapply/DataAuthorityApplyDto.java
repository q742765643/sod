package com.piesat.dm.rpc.dto.dataapply;

import com.piesat.util.BaseDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/10 11:46
 */
@Data
public class DataAuthorityApplyDto extends BaseDto {

    /**
     * 申请用户id
     */
    private String userId;

    /**
     * 申请用户名
     */
    private String userName;

    /**
     * 机构
     */
    private String department;

    /**
     * 联系方式
     */
    private String telephone;

    /**
     * 审核状态
     * 01：待审、02：已审
     */
    private String auditStatus;

    /**
     * 审核人
     */
    private String examiner;

    /**
     * 审核时间
     */
    private Date examineTime;

    private List<DataAuthorityRecordDto> dataAuthorityRecordList;
}
