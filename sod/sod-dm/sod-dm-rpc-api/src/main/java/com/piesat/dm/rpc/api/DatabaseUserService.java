package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.DataLogicDto;
import com.piesat.dm.rpc.dto.DatabaseUserDto;
import com.piesat.util.constant.GrpcConstant;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
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

    /**
     * 根据UP账户查询
     * @param databaseUPId
     * @return
     */
    DatabaseUserDto getDotByUPID(String databaseUPId);

    /**
     * 根据userId查询
     * @param userId
     * @return
     */
    DatabaseUserDto getDotByUserId(String userId);

    /**
     * 根据userId和审核状态查询
     * @param userId
     * @param examineStatus
     * @return
     */
    DatabaseUserDto findByUserIdAndExamineStatus(String userId,String examineStatus);

    /**
     * 新增
     * @param atabaseUserDto
     * @return
     */
    DatabaseUserDto saveDto(DatabaseUserDto atabaseUserDto);

    /**
     * 数据库授权
     * @param databaseUserDto
     * @return
     */
    boolean empower(DatabaseUserDto databaseUserDto);

    /**
     * 分页查询
     * @param pageForm
     * @return
     */
    PageBean selectPageList(PageForm<DatabaseUserDto> pageForm);
}
