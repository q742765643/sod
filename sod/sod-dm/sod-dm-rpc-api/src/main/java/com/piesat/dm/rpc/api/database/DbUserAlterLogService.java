package com.piesat.dm.rpc.api.database;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.database.DbUserAlterLogDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * @author cwh
 * @date 2020年 12月16日 15:19:53
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DbUserAlterLogService {
    /**
     * 保存
     * @param dbUserAlterLogDto
     * @return
     */
    DbUserAlterLogDto saveDto(DbUserAlterLogDto dbUserAlterLogDto);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    DbUserAlterLogDto getDotById(String id);

    /**
     * 根据id删除
     * @param id
     */
    void delete(String id);

    /**
     * 查询所有
     * @return
     */
    List<DbUserAlterLogDto> all();

    /**
     * 删除
     * @param id
     */
    void deleteByAuthorizeId(String id);
}
