package com.piesat.sod.system.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.sod.system.rpc.dto.SqlTemplateDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 *  sql 模板管理
 * @author adminis
 *
 */
@GrpcHthtService(server = GrpcConstant.SYSTEM_SERVER,serialization = SerializeType.PROTOSTUFF)
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
	void delByIds(String ids) throws Exception;
	/**
	 *  主键查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	SqlTemplateDto findByPk(String id) throws Exception;
	/**
	 *  编辑
	 * @param sqlTemplateDto
	 * @throws Exception
	 */
	void edit(SqlTemplateDto sqlTemplateDto) throws Exception;
	
	/**
	 *  查询全部
	 * @return
	 * @throws Exception
	 */
	List<SqlTemplateDto> findAll() throws Exception;
	/**
	 *  check
	 * @param databaseServer
	 * @return
	 * @throws Exception
	 */
	List<SqlTemplateDto> checkSqlTemplate(String databaseServer) throws Exception;
}
