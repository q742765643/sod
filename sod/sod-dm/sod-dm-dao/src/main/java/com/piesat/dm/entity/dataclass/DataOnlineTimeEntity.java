package com.piesat.dm.entity.dataclass;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/17 18:45
 */
@Data
@Entity
@Table(name = "T_SOD_DATA_ONLINE_TIME")
public class DataOnlineTimeEntity extends BaseEntity {

    /**
     * 存储编码
     * data_class_id
     */
    @Column(name = "data_class_id", length = 255 )
    private String dataClassId;

    /**
     * 资料最早时间
     */
    @Column(name = "begin_time")
    private Date beginTime;

    /**
     * 资料最新时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 日期类型,today和end_time
     */
    @Column(name = "end_time_flag", length = 50)
    private String endTimeFlag;

    /**
     * 0:未使用，1：使用中
     */
    @Column(name = "is_use", length = 1)
    private Integer isUse;
}
