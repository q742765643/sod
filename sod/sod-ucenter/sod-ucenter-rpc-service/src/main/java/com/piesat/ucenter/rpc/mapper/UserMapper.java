package com.piesat.ucenter.rpc.mapper;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.ucenter.dao.UserDao;
import com.piesat.ucenter.entity.UserEntity;
import com.piesat.ucenter.rpc.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/18 16:43
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface  UserMapper extends BaseMapper<UserDto,UserEntity> {
}
