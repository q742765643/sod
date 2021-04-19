package com.piesat.dm.rpc.dto.dataclass;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 资料分类
 *
 * @author cwh
 * @date 2019年 11月20日 17:02:58
 */
@Data
public class DataClassDto {
    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * data_class_id
     */
    private String dataClassId;

    /**
     * class_name
     */
    private String className;

    /**
     * parent_class_id
     */
    private String parentId;

    /**
     * serial_no
     */
    private Integer serialNo;

    /**
     * type
     * 1为目录，2为资料
     */
    private Integer type;

    /**
     * d_data_id
     */
    private String dDataId;

    /**
     * meta_data_name
     */
    private String metaDataName;

    /**
     * access_control
     */
    private Integer isAccess;

    /**
     * if_stop_use
     */
    private Boolean ifStopUse;

    /**
     * frequency_type
     */
    private Integer frequencyType;

    /**
     * is_all_line
     */
    private Integer isAllLine;

    /**
     * use_base_info
     */
    private Integer useBaseInfo;

    private Date createTime;

    private Date updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private Integer version;

    private String createBy;

    private List<DataClassLogicDto> dataLogicList;

    private List<DataClassLabelDto> dataClassLabelList;

    private List<DataClassUserDto> dataClassUserList;

    private List<DataClassServiceCodeDto> serviceCodeList;

    //portal申请id
    private String applyId;

    private String userId;

    private String remark;

    public DataClassInfoDto getInfo(){
        DataClassInfoDto d = new DataClassInfoDto();
        d.setDataClassId(this.dataClassId);
        d.setDataName(this.className);
        d.setIsArchive(this.isAccess);
        d.setSn(this.serialNo);
        d.setUserId(this.userId);
        return d;
    }

}
