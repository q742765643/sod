package com.piesat.dm.rpc.dto.dataclass;

import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import lombok.Data;

import java.util.List;

/**
 * @author cwh
 * @version 1.0.0
 * @ClassName DataClassServiceCodeList.java
 * @Description TODO
 * @createTime 2021年04月16日 14:04:00
 */
@Data
public class DataClassServiceCodeList {

    private static final long serialVersionUID = 1L;

    private List<DataClassServiceCodeDto> serviceCodeList;
}
