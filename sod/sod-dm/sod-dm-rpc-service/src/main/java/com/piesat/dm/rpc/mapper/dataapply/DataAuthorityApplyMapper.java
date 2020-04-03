package com.piesat.dm.rpc.mapper.dataapply;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.dataapply.DataAuthorityApplyEntity;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityApplyDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/10 11:49
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataAuthorityApplyMapper  extends BaseMapper<DataAuthorityApplyDto, DataAuthorityApplyEntity> {
}
