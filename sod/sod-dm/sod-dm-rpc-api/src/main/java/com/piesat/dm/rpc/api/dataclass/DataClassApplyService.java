package com.piesat.dm.rpc.api.dataclass;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.dataclass.DataClassApplyDto;
import com.piesat.util.ResultT;
import com.piesat.util.constant.GrpcConstant;
import com.piesat.util.page.PageForm;

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
    ResultT saveDto(DataClassApplyDto dataClassApplyDto);

    /**
     * 审核
     * @param dataClassApplyDto
     * @return
     */
    ResultT review(DataClassApplyDto dataClassApplyDto);

    /**
     * 查询列表
     * @param pageForm
     * @return
     */
    ResultT list(PageForm<DataClassApplyDto> pageForm);

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
