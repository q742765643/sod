package com.piesat.sod.system.rpc.mapstruct;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.sod.system.entity.GribParameterDefineEntity;
import com.piesat.sod.system.rpc.dto.GribParameterDefineDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/9 17:47
 */
@Service
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GribParameterDefineMapstruct extends BaseMapper<GribParameterDefineDto, GribParameterDefineEntity> {
}
