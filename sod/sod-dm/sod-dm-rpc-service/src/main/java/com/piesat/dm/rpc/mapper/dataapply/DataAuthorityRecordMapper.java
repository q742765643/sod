package com.piesat.dm.rpc.mapper.dataapply;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.dataapply.DataAuthorityRecordEntity;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityRecordDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/10 16:49
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataAuthorityRecordMapper extends BaseMapper<DataAuthorityRecordDto, DataAuthorityRecordEntity> {

}
