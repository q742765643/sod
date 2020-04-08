package com.piesat.dm.rpc.dto.datatable;

import lombok.Data;

import java.util.List;

/**
 * @author cwh
 * @date 2020年 04月03日 16:22:17
 */
@Data
public class GridDecodingList {
    private static final long serialVersionUID = 1L;

    private List<GridDecodingDto> gridDecodingList;
}
