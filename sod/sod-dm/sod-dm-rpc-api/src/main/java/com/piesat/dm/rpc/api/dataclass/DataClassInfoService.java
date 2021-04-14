package com.piesat.dm.rpc.api.dataclass;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.dataclass.DataClassApplyDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassInfoDto;
import com.piesat.util.ResultT;
import com.piesat.util.constant.GrpcConstant;
import com.piesat.util.page.PageForm;

import java.util.List;

/**
 * @author cuiwenhui
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DataClassInfoService {
    /**
     * 保存
     * @param dataClassInfoDto
     * @return
     */
    ResultT saveDto(DataClassInfoDto dataClassInfoDto);

    /**
     * 申请
     * @param dataClassInfoDto
     * @return
     */
    ResultT apply(DataClassInfoDto dataClassInfoDto);

    /**
     * 查询列表
     * @param pageForm
     * @return
     */
    ResultT list(PageForm<DataClassInfoDto> pageForm);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    DataClassInfoDto getDotById(String id);

    /**
     * 根据id删除
     * @param id
     */
    void delete(String id);

    /**
     * 查询所有
     * @return
     */
    List<DataClassInfoDto> all();

    /**
     * 根据存储编码查询
     * @param id
     * @return
     */
    List<DataClassInfoDto> getDotByClassId(String id);
}
