package com.piesat.dm.rpc.service.database;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.dm.core.api.DatabaseDcl;
import com.piesat.dm.core.parser.DatabaseInfo;
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.dao.database.DatabaseDefineDao;
import com.piesat.dm.entity.database.DatabaseDefineEntity;
import com.piesat.dm.entity.database.DatabaseEntity;
import com.piesat.dm.rpc.api.database.DatabaseDefineService;
import com.piesat.dm.rpc.dto.database.DatabaseDefineDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.mapper.database.DatabaseDefineMapper;
import com.piesat.dm.rpc.mapper.database.DatabaseMapper;
import com.piesat.dm.rpc.util.DatabaseUtil;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
public class DatabaseDefineServiceImpl extends BaseService<DatabaseDefineEntity> implements DatabaseDefineService {
    @Autowired
    private DatabaseDefineDao databaseDefineDao;
    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    private DatabaseMapper databaseMapper;
    @Autowired
    private DatabaseInfo databaseInfo;
    @Autowired
    private DatabaseDefineMapper databaseDefineMapper;

    @Override
    public BaseDao<DatabaseDefineEntity> getBaseDao() {
        return databaseDefineDao;
    }

    @Override
    public DatabaseDefineDto saveDto(DatabaseDefineDto databaseDefineDto) {
        DatabaseDto databaseDto = databaseDefineDto.getDatabaseDto();
        if (StringUtils.isEmpty(databaseDto.getId())){
            databaseDto.setCreateTime(new Date());
        }
        if (StringUtils.isEmpty(databaseDefineDto.getId())){
            databaseDefineDto.setCreateTime(new Date());
        }
        DatabaseDefineEntity databaseDefineEntity = this.databaseDefineMapper.toEntity(databaseDefineDto);
        DatabaseDefineEntity save = this.save(databaseDefineEntity);
        DatabaseEntity databaseEntity = this.databaseMapper.toEntity(databaseDto);
        databaseEntity.setDatabaseDefine(save);
        databaseEntity = this.databaseDao.saveNotNull(databaseEntity);
        DatabaseDefineDto databaseDefineDto1 = this.databaseDefineMapper.toDto(save);
        databaseDefineDto1.setDatabaseDto(this.databaseMapper.toDto(databaseEntity));
        return databaseDefineDto1;
    }

    @Override
    public List<DatabaseDefineDto> all() {
        List<DatabaseDefineEntity> all = this.getAll();
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
        List<DatabaseDefineEntity> all = this.getAll(ssb.generateSpecification(), sort);
        return this.databaseDefineMapper.toDto(all);
    }

    @Override
    public List<DatabaseDefineDto> findByType(String databaseType) {
        return this.databaseDefineMapper.toDto(this.databaseDefineDao.findByDatabaseType(databaseType));
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
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        PageBean page = this.getPage(ssb.generateSpecification(), new PageForm(pageNum, pageSize), sort);
        List<DatabaseDefineEntity> pageData = (List<DatabaseDefineEntity>)page.getPageData();
        page.setPageData(this.databaseDefineMapper.toDto(pageData));
        return page;
    }

    @Override
    public DatabaseDefineDto conStatus(String id) {
        DatabaseDefineDto dotById = this.getDotById(id);
        DatabaseDto database = new DatabaseDto();
        database.setDatabaseDefine(dotById);
        try {
            DatabaseDcl db = DatabaseUtil.getPubDatabase(database, databaseInfo);
            if (db!=null){
                db.closeConnect();
                dotById.setCheckConn(1);
            }else {
                dotById.setCheckConn(2);
            }
        } catch (Exception e) {
            dotById.setCheckConn(2);
            e.printStackTrace();
        }
        this.saveDto(dotById);
        return dotById;
    }

    @Override
    public DatabaseDefineDto getDotById(String id) {
        DatabaseDefineEntity databaseDefineEntity = this.getById(id);
        DatabaseDefineDto databaseDefineDto = this.databaseDefineMapper.toDto(databaseDefineEntity);
        List<DatabaseEntity> databaseEntities = this.databaseDao.findByDatabaseDefine_IdAndDatabaseName(id, "基础库");
        if (databaseEntities.size()>0){
            DatabaseDto databaseDto = this.databaseMapper.toDto(databaseEntities).get(0);
            databaseDefineDto.setDatabaseDto(databaseDto);
        }
        return databaseDefineDto;
    }

    @Override
    public void delByIds(String ids) {
        String[] split = ids.split(",");
        this.deleteByIds(Arrays.asList(split));
        for (String id:split) {
            this.databaseDao.deleteByDatabaseDefine_Id(id);
        }
    }
}
