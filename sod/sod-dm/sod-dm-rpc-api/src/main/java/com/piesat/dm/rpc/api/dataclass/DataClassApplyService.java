package com.piesat.dm.rpc.api.dataclass;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.dataclass.DataClassApplyDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 资料申请
 *
 * @author cwh
 * @date 2019年 11月22日 15:33:10
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DataClassApplyService {
    /**
     * 保存
     * @param dataClassApplyDto
     * @return
     */
    DataClassApplyDto saveDto(DataClassApplyDto dataClassApplyDto);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    DataClassApplyDto getDotById(String id);

    /**
     * 根据id删除
     * @param id
     */
    void delete(String id);

    /**
     * 查询所有
     * @return
     */
    List<DataClassApplyDto> all();

}
