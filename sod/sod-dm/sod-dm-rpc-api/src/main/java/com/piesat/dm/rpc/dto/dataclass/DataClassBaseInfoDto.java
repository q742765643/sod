package com.piesat.dm.rpc.dto.dataclass;

import com.piesat.util.BaseDto;
import lombok.Data;

import javax.persistence.Column;

/**
 * 资料基础信息
 *
 * @author cwh
 * @date 2020年 04月01日 15:45:12
 */
@Data
public class DataClassBaseInfoDto extends BaseDto {
    private String dataClassId;

    private String cCoremetaId;

    private String cIdabs;

    private String cMainfreq;

    private String cDatascal;

    private String cWestbl;

    private String cEastbl;

    private String cSouthbl;

    private String cNorthbl;

    private String cGeodesc;

    private String cSource;

    private String cNetname;

    private String cNettype;

    private String cObsfreq;

    private String cBegin;

    private String cEnd;

    private String cCreatName;

    private String cChecker;

    private String cRpindname;

    private String useBaseInfo;
}
