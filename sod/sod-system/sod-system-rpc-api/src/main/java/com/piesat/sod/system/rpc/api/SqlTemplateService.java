package com.piesat.sod.system.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.sod.system.rpc.dto.SqlTemplateDto;
/**
 *  sql 模板管理
 * @author adminis
 *
 */
@GrpcHthtService(server = "sqlTemplate",serialization = SerializeType.PROTOSTUFF)
public interface SqlTemplateService {

	/**
	 *  新增sql模板
	 * @param sqlTemplateDto
	 * @throws Exception
	 */
	void add(SqlTemplateDto sqlTemplateDto) throws Exception;
	/**
	 *  删除sql模板
	 * @param id
	 * @throws Exception
	 */
	void del(String id) throws Exception;
}
