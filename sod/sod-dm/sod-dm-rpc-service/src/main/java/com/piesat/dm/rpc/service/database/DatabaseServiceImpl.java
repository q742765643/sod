package com.piesat.dm.rpc.service.database;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.poi.ExcelUtil;
import com.piesat.dm.core.api.DatabaseDcl;
import com.piesat.dm.core.parser.DatabaseInfo;
import com.piesat.dm.dao.database.SchemaDao;
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.dao.dataclass.LogicDatabaseDao;
import com.piesat.dm.dao.datatable.DataTableDao;
import com.piesat.dm.entity.database.DatabaseEntity;
import com.piesat.dm.entity.database.SchemaEntity;
import com.piesat.dm.entity.datatable.DataTableInfoEntity;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.dto.database.DatabaseDefineDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.mapper.database.DatabaseDefineMapper;
import com.piesat.dm.rpc.mapper.database.DatabaseMapper;
import com.piesat.dm.rpc.util.DatabaseUtil;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 数据库类型定义
 *
 * @author cwh
 * @date 2019年 11月22日 15:59:29
 */
@Service
public class DatabaseServiceImpl extends BaseService<DatabaseEntity> implements DatabaseService {
    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    private SchemaDao schemaDao;
    @Autowired
    private DatabaseMapper databaseMapper;
    @Autowired
    private DatabaseInfo databaseInfo;
    @Autowired
    private DatabaseDefineMapper databaseDefineMapper;
    @Autowired
    private LogicDatabaseDao logicDatabaseDao;
    @Autowired
    private DataTableDao dataTableDao;

    @Override
    public BaseDao<DatabaseEntity> getBaseDao() {
        return databaseDao;
    }

    @Override
    @Transactional
    public DatabaseDefineDto saveDto(DatabaseDefineDto databaseDefineDto) {
        DatabaseDto databaseDto = databaseDefineDto.getDatabaseDto();
        if (StringUtils.isEmpty(databaseDto.getId())) {
            databaseDto.setCreateTime(new Date());
            databaseDto.setStopUse(false);
        }
        if (StringUtils.isEmpty(databaseDefineDto.getId())) {
            databaseDefineDto.setCreateTime(new Date());
        }
        DatabaseEntity databaseEntity = this.databaseDefineMapper.toEntity(databaseDefineDto);
        DatabaseEntity save = this.saveNotNull(databaseEntity);
        SchemaEntity schemaEntity = this.databaseMapper.toEntity(databaseDto);
        schemaEntity.setDatabaseDefine(save);
        schemaEntity = this.schemaDao.saveNotNull(schemaEntity);
        DatabaseDefineDto databaseDefineDto1 = this.databaseDefineMapper.toDto(save);
        databaseDefineDto1.setDatabaseDto(this.databaseMapper.toDto(schemaEntity));
        return databaseDefineDto1;
    }

    @Override
    public List<DatabaseDefineDto> all() {
        List<DatabaseEntity> all = this.getAll();
        return this.databaseDefineMapper.toDto(all);
    }

    @Override
    public List<DatabaseDefineDto> export(String id, String databaseName) {
        SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder();
        if (StringUtils.isNotBlank(id)) {
            ssb.add("id", SpecificationOperator.Operator.likeAll.name(), id);
        }
        if (StringUtils.isNotBlank(databaseName)) {
            ssb.add("databaseName", SpecificationOperator.Operator.likeAll.name(), databaseName);
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        List<DatabaseEntity> all = this.getAll(ssb.generateSpecification(), sort);
        return this.databaseDefineMapper.toDto(all);
    }

    @Override
    public List<DatabaseDefineDto> findByType(String databaseType) {
        return this.databaseDefineMapper.toDto(this.databaseDao.findByDatabaseType(databaseType));
    }

    @Override
    public PageBean getPage(DatabaseDefineDto databaseDefineDto, int pageNum, int pageSize) {
        SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder();
        if (StringUtils.isNotBlank(databaseDefineDto.getId())) {
            ssb.add("id", SpecificationOperator.Operator.likeAll.name(), databaseDefineDto.getId());
        }
        if (StringUtils.isNotBlank(databaseDefineDto.getDatabaseName())) {
            ssb.add("databaseName", SpecificationOperator.Operator.likeAll.name(), databaseDefineDto.getDatabaseName());
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "serialNumber");
        PageBean page = this.getPage(ssb.generateSpecification(), new PageForm(pageNum, pageSize), sort);
        List<DatabaseEntity> pageData = (List<DatabaseEntity>) page.getPageData();
        page.setPageData(this.databaseDefineMapper.toDto(pageData));
        return page;
    }

    @Override
    public DatabaseDefineDto conStatus(String id) {
        DatabaseDefineDto dotById = this.getDotById(id);
        DatabaseDto database = new DatabaseDto();
        database.setDatabaseDefine(dotById);
        DatabaseDcl db = null;
        try {
            db = DatabaseUtil.getPubDatabase(database, databaseInfo);
            if (db != null) {
                db.closeConnect();
                dotById.setCheckConn(1);
            } else {
                dotById.setCheckConn(2);
            }
        } catch (Exception e) {
            dotById.setCheckConn(2);
//            e.printStackTrace();
        } finally {
            if (db != null) {
                db.closeConnect();
            }
        }
        this.saveDto(dotById);
        return dotById;
    }

    @Override
    public ResultT connStatus(DatabaseDefineDto databaseDefineDto) {
        DatabaseDcl db = null;
        try {
            db = DatabaseUtil.getDatabaseDefine(databaseDefineDto, databaseInfo);
            if (db != null) {
                db.closeConnect();
                return ResultT.success();
            } else {
                return ResultT.failed();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        } finally {
            if (db != null) {
                db.closeConnect();
            }
        }
    }

    @Override
    public List<DatabaseDefineDto> findByIdIn(List<String> ids) {
        List<DatabaseEntity> databaseDefineEntities = this.databaseDao.findByIdIn(ids);
        return this.databaseDefineMapper.toDto(databaseDefineEntities);
    }

    @Override
    public void exportExcel(String id, String databaseName) {
        List<DatabaseDefineDto> dtoList = this.export(id, databaseName);
        List<DatabaseEntity> entities = databaseDefineMapper.toEntity(dtoList);
        ExcelUtil<DatabaseEntity> util = new ExcelUtil(DatabaseEntity.class);
        util.exportExcel(entities, "数据库");
    }

    @Override
    public List<DatabaseDefineDto> getDatabaseDefineList() {
        List<DatabaseEntity> list = databaseDao.findByUserDisplayControlNot(2);
        return databaseDefineMapper.toDto(list);
    }

    @Override
    public DatabaseDefineDto getDotById(String id) {
        DatabaseEntity databaseEntity = this.getById(id);
        DatabaseDefineDto databaseDefineDto = this.databaseDefineMapper.toDto(databaseEntity);
        List<SchemaEntity> databaseEntities = this.schemaDao.findByDatabaseDefine_IdAndDatabaseName(id, "基础库");
        if (databaseEntities.size() > 0) {
            DatabaseDto databaseDto = this.databaseMapper.toDto(databaseEntities).get(0);
            databaseDefineDto.setDatabaseDto(databaseDto);
        }
        return databaseDefineDto;
    }

    @Transactional
    @Override
    public ResultT delByIds(String ids) {
        String[] split = ids.split(",");
        for (int i = 0; i < split.length; i++) {
            String id = split[i];
            List<SchemaEntity> databases = this.schemaDao.findByDatabaseDefine_Id(id);
            for (int j = 0; j < databases.size(); j++) {
                SchemaEntity database = databases.get(j);
                List<DataTableInfoEntity> logicList = this.dataTableDao.findByDatabaseId(database.getId());
                if (logicList.size() > 0) {
                    return ResultT.failed("数据库存在资料，请先删除相关资料，如表名为：" + logicList.get(0).getTableName());
                }
            }
        }

        for (String id : split) {
            this.schemaDao.deleteByDatabaseDefine_Id(id);
            this.logicDatabaseDao.deleteByDatabaseId(id);
        }
        this.deleteByIds(Arrays.asList(split));
        return ResultT.success();
    }
}
