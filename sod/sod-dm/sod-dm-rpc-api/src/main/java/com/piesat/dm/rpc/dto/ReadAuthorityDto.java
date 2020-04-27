package com.piesat.dm.rpc.dto;

import com.piesat.util.BaseDto;
import lombok.Data;

/**
 * @author yaya
 * @description TODO
 * @date 2020/4/27 10:46
 */
@Data
public class ReadAuthorityDto  extends BaseDto {
    private String name;

    private String value;

    private String description;
}
