package com.piesat.dm.rpc.mapper;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.DataOnlineTimeEntity;
import com.piesat.dm.rpc.dto.DataOnlineTimeDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/17 19:21
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataOnlineTimeMapper extends BaseMapper<DataOnlineTimeDto, DataOnlineTimeEntity> {
}
