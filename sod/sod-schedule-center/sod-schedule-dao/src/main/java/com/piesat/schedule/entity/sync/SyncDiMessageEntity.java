package com.piesat.schedule.entity.sync;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * @author yaya
 * @description TODO
 * @date 2020/5/9 10:33
 */
@Data
@Entity
@Table(name = "T_SOD_JOB_SYNCDI_MESSAGE")
public class SyncDiMessageEntity{
    /**
     * 对应同步任务表中的id
     */
    @Id
    @Column(name = "id", nullable = false,length = 32)
    private String id;

    /**
     * 资料四级编码
     */
    @Column(name="d_data_id", length = 20)
    private String dDataId;


    /**
     * 消息队列名
     */
    @Column(name="message_queue_name", length = 100)
    private String messageQueueName;

    /**
     * 主键组成
     */
    @Column(name="primary_composition", length = 100)
    private String primaryComposition;
}
