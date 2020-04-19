package com.piesat.dm.rpc.mapper.dataapply;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.dataapply.NewdataTableColumnEntity;
import com.piesat.dm.rpc.dto.dataapply.NewdataTableColumnDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/4/18 21:37
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewdataTableColumnMapper extends BaseMapper<NewdataTableColumnDto, NewdataTableColumnEntity> {
}
