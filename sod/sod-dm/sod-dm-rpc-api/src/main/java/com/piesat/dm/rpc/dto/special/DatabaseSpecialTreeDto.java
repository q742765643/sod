package com.piesat.dm.rpc.dto.special;

import com.piesat.util.BaseDto;
import lombok.Data;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/27 18:15
 */
@Data
public class DatabaseSpecialTreeDto extends BaseDto {
    /**
     * 专题库id
     */
    private String sdbId;

    /**
     * 分类id
     */
    private String typeId;

    /**
     * 分类名称
     */
    private String typeName;

    /**
     * 上级分类
     */
    private String parentId;

    /**
     * 排序
     */
    private Integer sort;

}
