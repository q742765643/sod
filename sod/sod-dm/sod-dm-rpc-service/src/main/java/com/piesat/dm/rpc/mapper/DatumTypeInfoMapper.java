package com.piesat.dm.rpc.mapper;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.DatumTypeInfoEntity;
import com.piesat.dm.rpc.dto.DatumTypeInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * 公共元数据
 *
 * @author cwh
 * @date 2019年 11月27日 16:24:08
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DatumTypeInfoMapper extends BaseMapper<DatumTypeInfoDto, DatumTypeInfoEntity> {
}
