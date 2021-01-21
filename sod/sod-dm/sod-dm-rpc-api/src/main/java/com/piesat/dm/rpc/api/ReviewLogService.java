package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.entity.ReviewLogEntity;
import com.piesat.dm.rpc.dto.ReviewLogDto;
import com.piesat.dm.rpc.dto.datatable.TableSqlDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 表sql
 *
 * @author cwh
 * @date 2020年 04月02日 14:06:59
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface ReviewLogService {
    /**
     * 保存
     * @param reviewLogDto
     * @return
     */
    ReviewLogDto saveDto(ReviewLogDto reviewLogDto);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    ReviewLogDto getDotById(String id);

    /**
     * 删除
     * @param id
     */
    void delete(String id);

    /**
     * 查询所有
     * @return
     */
    @Deprecated
    List<ReviewLogDto> all();

    /**
     * 根据绑定id查询
     * @param bindId
     * @return
     */
    List<ReviewLogEntity> findByBindId(String bindId);
}
