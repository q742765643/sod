package com.piesat.dm.rpc.dto.dataclass;

import com.piesat.util.BaseDto;
import lombok.Data;

/**
 * 资料用户权限
 *
 * @author cwh
 * @date 2020年 07月29日 10:59:09
 */
@Data
public class DataClassUserDto extends BaseDto {
    private static final long serialVersionUID = 1L;

    private String dataClassId;

    private String userName;

}
