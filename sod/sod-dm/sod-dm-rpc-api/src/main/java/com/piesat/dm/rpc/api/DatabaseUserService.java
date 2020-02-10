package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.DataLogicDto;
import com.piesat.dm.rpc.dto.DatabaseUserDto;
import com.piesat.util.constant.GrpcConstant;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Map;

/**
 * 数据库账户管理
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DatabaseUserService {

    /**
     * 查询所有
     * @return
     */
    List<DatabaseUserDto> all();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    DatabaseUserDto getDotById(String id);

    /**
     * 根据ID删除
     * @param id
     */
    void delete(String id);

}
