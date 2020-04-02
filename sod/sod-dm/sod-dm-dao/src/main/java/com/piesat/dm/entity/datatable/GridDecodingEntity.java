package com.piesat.dm.entity.datatable;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 解码配置
 *
 * @author cwh
 * @date 2020年 02月12日 11:19:06
 */
@Data
@Table(name = "T_SOD_GRID_DECODING")
@Entity
public class GridDecodingEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "grid_decode_id", length = 255)
    private String gridDecodeId;

    @Column(name = "data_service_id", length = 36)
    private String dataServiceId;

    @Column(name = "data_dpc_id", length = 36)
    private String dataDpcId;

    @Column(name = "ele_code_short", length = 36)
    private String eleCodeShort;

    @Column(name = "subject_id")
    private int subjectId;

    @Column(name = "classify")
    private int classify;

    @Column(name = "parameter_id")
    private int parameterId;

    @Column(name = "grib_version")
    private int gribVersion;

    @Column(name = "element_cn", length = 255)
    private String elementCn;

    @Column(name = "public_config", length = 255)
    private String publicConfig;

    @Column(name = "template_id", length = 255)
    private String templateId;

}
