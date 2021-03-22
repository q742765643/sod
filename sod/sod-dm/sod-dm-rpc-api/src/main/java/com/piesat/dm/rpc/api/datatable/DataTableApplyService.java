package com.piesat.dm.rpc.api.datatable;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.dataclass.DataClassLabelDefDto;
import com.piesat.dm.rpc.dto.datatable.DataTableApplyDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 表申请
 *
 * @author cwh
 * @date 2020年 07月31日 16:10:25
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DataTableApplyService {
    /**
     * 保存
     * @param dataTableApplyDto
     * @return
     */
    DataTableApplyDto saveDto(DataTableApplyDto dataTableApplyDto);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    DataTableApplyDto getDotById(String id);

    /**
     * 根据id删除
     * @param id
     */
    void delete(String id);

    /**
     * 查下所有
     * @return
     */
    List<DataTableApplyDto> all();
}
