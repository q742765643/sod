package com.piesat.dm.rpc.service.database;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.common.constants.Constants;
import com.piesat.dm.core.action.build.DataBuild;
import com.piesat.dm.core.enums.DatabaseTypesEnum;
import com.piesat.dm.core.model.ConnectVo;
import com.piesat.dm.core.model.SelectVo;
import com.piesat.dm.dao.database.SchemaDao;
import com.piesat.dm.dao.datatable.DataTableDao;
import com.piesat.dm.entity.database.SchemaEntity;
import com.piesat.dm.entity.datatable.DataTableInfoEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.database.SchemaService;
import com.piesat.dm.rpc.dto.database.SchemaDto;
import com.piesat.dm.rpc.mapper.database.DatabaseMapper;
import com.piesat.util.ResultT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据库基础库专题库
 *
 * @author cwh
 * @date 2019年 11月22日 16:27:41
 */
@Service
public class SchemaServiceImpl extends BaseService<SchemaEntity> implements SchemaService {
    @Autowired
    private SchemaDao schemaDao;
    @Autowired
    private DatabaseMapper databaseMapper;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private DataTableDao dataTableDao;

    @Override
    public BaseDao<SchemaEntity> getBaseDao() {
        return schemaDao;
    }

    @Override
    public SchemaDto saveDto(SchemaDto schemaDto) {
        if ("基础库".equals(schemaDto.getSchemaNameCn())) {
            schemaDto.setLevel(1);
        }
        SchemaEntity schemaEntity = this.databaseMapper.toEntity(schemaDto);
        schemaEntity = this.saveNotNull(schemaEntity);
        return this.databaseMapper.toDto(schemaEntity);
    }

    @Override
    public List<SchemaDto> all() {
        List<SchemaEntity> all = this.getAll();
        return this.databaseMapper.toDto(all);
    }

    @Override
    public List<Map<String, Object>> getDatabaseName() {
        List<Map<String, Object>> list = mybatisQueryMapper.getDatabaseName();
        return list;
    }

    @Override
    public List<Map<String, Object>> getByDatabaseDefineId(String id) {
        //String sql = "select *  from T_SOD_DATABASE t where t.DATABASE_DEFINE_ID = '"+id+"'";
        List<Map<String, Object>> list = mybatisQueryMapper.getByDatabaseDefineId(id);
        return list;
    }

    @Override
    public List<SchemaDto> findByLevel(int level) {
        List<SchemaEntity> all = this.schemaDao.findByLevel(level);
        return this.databaseMapper.toDto(all);
    }

    @Override
    public List<SchemaDto> findByDatabaseClassifyAndIdIn(String databaseClassify, List<String> ids) {
        List<SchemaEntity> schemaEntityList = this.schemaDao.findByDatabaseClassifyAndIdIn(databaseClassify, ids);
        return this.databaseMapper.toDto(schemaEntityList);
    }

    @Override
    public List<SchemaDto> findByDatabaseClassifyAndDatabaseDefineIdIn(String databaseClassify, List<String> databaseDefineIds) {
        List<SchemaEntity> schemaEntityList = this.schemaDao.findByDatabaseClassifyAndDatabaseIdIn(databaseClassify, databaseDefineIds);
        return this.databaseMapper.toDto(schemaEntityList);
    }

    @Override
    public List<SchemaDto> findByDatabaseDefineIdIn(List<String> databaseDefineIds) {
        List<SchemaEntity> schemaEntityList = this.schemaDao.findByDatabaseIdIn(databaseDefineIds);
        return this.databaseMapper.toDto(schemaEntityList);
    }

    @Override
    public List<SchemaDto> findByDatabaseDefineId(String id) {
        List<SchemaEntity> schemaEntityList = this.schemaDao.findByDatabase_Id(id);
        return this.databaseMapper.toDto(schemaEntityList);
    }

    @Override
    public List<SchemaDto> findByDatabaseClassify(String databaseClassify) {
        List<SchemaEntity> databaseEntities = this.schemaDao.findByDatabaseClassify(databaseClassify);
        //闲时优化
        if (databaseEntities != null && databaseEntities.size() > 0) {
            for (int i = databaseEntities.size() - 1; i > -1; i--) {
                if (databaseEntities.get(i).getDatabase().getUserDisplayControl().intValue() != 1) {
                    databaseEntities.remove(databaseEntities.get(i));
                }
            }
        }
        return this.databaseMapper.toDto(databaseEntities);
    }

    @Override
    public List<Map<String, Object>> findByUserIdAndDatabaseDefineId(String userId, String databaseDefineId) {
        //由于框架bug导致带union的sql报错，暂时分开写
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> byUserIdAndDatabaseDefineId1 = this.mybatisQueryMapper.findByUserIdAndDatabaseDefineId1(userId, databaseDefineId);
        if (byUserIdAndDatabaseDefineId1 != null) {
            resultList.addAll(byUserIdAndDatabaseDefineId1);
        }
        List<Map<String, Object>> byUserIdAndDatabaseDefineId2 = this.mybatisQueryMapper.findByUserIdAndDatabaseDefineId2(userId, databaseDefineId);
        if (byUserIdAndDatabaseDefineId2 != null) {
            resultList.addAll(byUserIdAndDatabaseDefineId2);
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> getDatabaseList(String ifDisplay) {
        String sql = "select t.id ID,concat(concat(d.database_name,'_'),t.database_name) DATABASE_NAME  from T_SOD_DATABASE t, T_SOD_DATABASE_DEFINE d WHERE t.DATABASE_DEFINE_ID = d.id AND d.user_display_control in(" + ifDisplay + ")";
        List<Map<String, Object>> list = this.queryByNativeSQL(sql);
        return list;
    }

    @Override
    public List<SchemaDto> findByDatabaseName(String databaseName) {
        List<SchemaEntity> databaseEntityList = this.schemaDao.findBySchemaNameCn(databaseName);
        return this.databaseMapper.toDto(databaseEntityList);
    }

    @Override
    public ResultT statisticalSpace(String id) {
        ResultT r = new ResultT();
        SchemaDto schemaDto = this.getDotById(id);
        ConnectVo connectVo = schemaDto.getConnectVo();
        if (connectVo.getDatabaseType().equals(DatabaseTypesEnum.XUGU)) {
            String url = connectVo.getUrl();
            int i = url.indexOf(Constants.QUESTION_MARK);
            int i1 = url.indexOf(Constants.BACKSLASH, url.lastIndexOf(Constants.BACKSLASH_DUBLE) + 2);
            String databaseName = url.substring(i1 + 1, i);
            url.replace(databaseName, Constants.SYSTEM);
            connectVo.setUrl(url);
            connectVo.setUserName(Constants.SYSDBA);
            connectVo.setPassWord(Constants.SYSDBA);
            SelectVo s = new SelectVo();
            s.setSchema(schemaDto.getSchemaName());
            s.setDatabaseName(databaseName);
            new DataBuild()
                    .init(connectVo, r)
                    .statisticalSpace(s, r)
                    .close();
        } else {
            r.setData("未知");
        }
        return r;
    }

    @Override
    public SchemaDto getDotById(String id) {
        SchemaEntity schemaEntity = this.getById(id);
        return this.databaseMapper.toDto(schemaEntity);
    }

    @Override
    public ResultT deleteById(String id) {
        List<DataTableInfoEntity> logicList = this.dataTableDao.findByDatabaseId(id);
        if (logicList.size() > 0) {
            return ResultT.failed("数据库存在资料，请先删除相关资料，如表名为：" + logicList.get(0).getTableName());
        }
        this.delete(id);
        return ResultT.success();
    }

}
