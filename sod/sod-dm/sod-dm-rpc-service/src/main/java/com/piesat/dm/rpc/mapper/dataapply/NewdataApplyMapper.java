package com.piesat.dm.rpc.mapper.dataapply;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.dataapply.NewdataApplyEntity;
import com.piesat.dm.rpc.dto.dataapply.NewdataApplyDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/5 19:15
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewdataApplyMapper extends BaseMapper<NewdataApplyDto, NewdataApplyEntity> {
}
