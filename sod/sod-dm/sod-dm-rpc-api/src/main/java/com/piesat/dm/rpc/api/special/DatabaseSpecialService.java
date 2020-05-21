package com.piesat.dm.rpc.api.special;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.entity.special.DatabaseSpecialAccessEntity;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialAccessDto;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialDto;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialReadWriteDto;
import com.piesat.util.constant.GrpcConstant;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 专题库管理
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DatabaseSpecialService {

    /**
     * 查询所有
     * @return
     */
    List<DatabaseSpecialDto> all();

    /**
     * 根据ID删除
     * @param id
     */
    void deleteById(String id);

    void deleteAccessBySdbIdAndUserId(String sdbId,String userId);

    List<DatabaseSpecialDto> findByUserId(String userId);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    DatabaseSpecialDto getDotById(String id);

    /**
     * 更新
     * @param databaseSpecialDto
     * @return
     */
    DatabaseSpecialDto saveDto(DatabaseSpecialDto databaseSpecialDto);

    DatabaseSpecialDto addOrUpdate(Map<String, String> map,String filePath);

    /**
     * 数据库授权
     * @param databaseDto
     */
    void empowerDatabaseSpecial(DatabaseDto databaseDto);

    /**
     * 单独资料授权
     * @param databaseSpecialReadWriteDto
     */
    void empowerDataOne(DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto);

    /**
     * 批量资料授权
     * @param databaseSpecialReadWriteDtoList
     */
    void empowerDataBatch(List<DatabaseSpecialReadWriteDto> databaseSpecialReadWriteDtoList);

    Map<String,Object> getDataTreeBySdbId(String sdbId);

    Map<String,Object> getTreeBySdbId(String sdbId);

    List<DatabaseSpecialDto> getByExamineStatus(String examineStatus);

    DatabaseSpecialDto updateUseStatusById(String sdbId,String useStatus);

    List<Map<String,Object>> getAllOtherRecordByUserId(String userId,String useStatus);

    List<Map<String, Object>>getByUserIdAndUseStatus(String userId,String useStatus);

    List<DatabaseSpecialDto> getByUseStatus(String useStatus);

    public PageBean getPage(PageForm<DatabaseSpecialDto> pageForm);

	/**
     * 分页查询
     * @param pageForm
     * @return
     */
    PageBean selectPageList(PageForm<DatabaseSpecialDto> pageForm);

    DatabaseSpecialDto saveMultilRecord(DatabaseSpecialDto databaseSpecialDto);

    DatabaseSpecialAccessDto getSpecialAccess(String tdbId, String userId);

    DatabaseSpecialAccessDto specialAccessApply(DatabaseSpecialAccessDto databaseSpecialAccessDto);

    DatabaseSpecialAccessDto specialAccessAutho(DatabaseSpecialAccessDto databaseSpecialAccessDto);

    Map<String, Object> updateBySql(String tdbId, String userId, String cause, String examineStatus);

    Map<String, Object> getRecordByTdbId(String tdbId, String userId, String cause);

    Map<String, Object> getOneRecordByTdbId(String tdbId, String userId, String cause);

    Map<String, Object> getAllSpecial(String userId);

    Map<String, Object> saveOneRecord(HttpServletRequest request);

    void exportExcel(DatabaseSpecialDto databaseSpecialDto);

    List<DatabaseSpecialDto> findByParam(DatabaseSpecialDto databaseSpecialDto);

}
