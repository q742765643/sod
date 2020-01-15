package com.piesat.dm.rpc.service;

import com.alibaba.fastjson.JSONArray;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.common.codedom.CodeDOM;
import com.piesat.dm.common.tree.BaseParser;
import com.piesat.dm.common.tree.TreeLevel;
import com.piesat.dm.dao.*;
import com.piesat.dm.entity.DataClassEntity;
import com.piesat.dm.entity.DataLogicEntity;
import com.piesat.dm.entity.DataTableEntity;
import com.piesat.dm.entity.DatabaseEntity;
import com.piesat.dm.rpc.api.DataClassService;
import com.piesat.dm.rpc.dto.DataClassDto;
import com.piesat.dm.rpc.mapper.DataClassMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 资料分类
 *
 * @author cwh
 * @date 2019年 11月22日 16:31:15
 */
@Service
public class DataClassServiceImpl extends BaseService<DataClassEntity> implements DataClassService {
    @Autowired
    private DataClassDao dataClassDao;
    @Autowired
    private DataClassMapper dataClassMapper;
    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    private DataLogicDao dataLogicDao;
    @Autowired
    private DataTableDao dataTableDao;
    @Autowired
    private ShardingDao shardingDao;


    @Override
    public BaseDao<DataClassEntity> getBaseDao() {
        return dataClassDao;
    }

    @Override
    public DataClassDto saveDto(DataClassDto dataClassDto) {
        DataClassEntity dataClassEntity = this.dataClassMapper.toEntity(dataClassDto);
        dataClassEntity = this.save(dataClassEntity);
        return this.dataClassMapper.toDto(dataClassEntity);
    }

    @Override
    public List<DataClassDto> all() {
        List<DataClassEntity> all = this.getAll();
        return this.dataClassMapper.toDto(all);
    }


    @Override
    public JSONArray getLogicClass() {
        String sql = "SELECT LOGIC_NAME \"name\",CONCAT('0-',LOGIC_FLAG) \"id\",'0' \"pId\",'1' \"type\",'0' \"metaDataId\" FROM  T_SOD_LOGIC_DEFINE " +
                " UNION " +
                "SELECT DISTINCT CLASS_NAME \"name\", CONCAT(DATA_CLASS_ID, CONCAT('-', LOGIC_FLAG)) \"id\",CONCAT(PARENT_ID,  CONCAT('-', LOGIC_FLAG)) \"pId\", '1' \"type\", '0' \"metaDataId\" " +
                "FROM T_SOD_DATA_CLASS A INNER JOIN T_SOD_DATA_LOGIC B ON  A.DATA_CLASS_ID =  SUBSTR(B.DATA_CLASS_ID,0,1) " +
                " UNION " +
                "SELECT D.CLASS_NAME \"name\", CONCAT(D.DATA_CLASS_ID, CONCAT('-', C.LOGIC_FLAG)) \"id\", CONCAT(D.PARENT_ID,  CONCAT('-', C.LOGIC_FLAG)) \"pId\",'1' \"type\",'0' \"metaDataId\" " +
                "FROM (SELECT CLASS_NAME,DATA_CLASS_ID,LOGIC_FLAG,PARENT_ID FROM T_SOD_DATA_CLASS A " +
                " INNER JOIN T_SOD_DATA_LOGIC B ON  A.DATA_CLASS_ID = B.DATA_CLASS_ID) C,T_SOD_DATA_CLASS D where C.PARENT_ID = D.DATA_CLASS_ID " +
                " UNION " +
                "SELECT F.CLASS_NAME \"name\", CONCAT(F.DATA_CLASS_ID, CONCAT('-', E.LOGIC_FLAG)) \"id\", CONCAT(F.PARENT_ID,  CONCAT('-', E.LOGIC_FLAG)) \"pId\",'1' \"type\",'0' \"metaDataId\"  FROM " +
                "(SELECT D.CLASS_NAME,D.DATA_CLASS_ID,D.PARENT_ID,C.LOGIC_FLAG FROM " +
                "(SELECT CLASS_NAME,DATA_CLASS_ID,LOGIC_FLAG,PARENT_ID FROM T_SOD_DATA_CLASS A INNER JOIN T_SOD_DATA_LOGIC B  ON  A.DATA_CLASS_ID = B.DATA_CLASS_ID) C,T_SOD_DATA_CLASS D " +
                "where C.PARENT_ID = D.DATA_CLASS_ID) E,T_SOD_DATA_CLASS F  where E.PARENT_ID = F.DATA_CLASS_ID " +
                " UNION " +
                "SELECT CLASS_NAME \"name\",DATA_CLASS_ID \"id\",CONCAT(PARENT_ID,CONCAT('-',LOGIC_FLAG)) \"pId\"," +
                "'2' \"type\",'0' \"metaDataId\" FROM T_SOD_DATA_CLASS A INNER JOIN T_SOD_DATA_LOGIC B ON A.DATA_CLASS_ID = B.DATA_CLASS_ID WHERE TYPE = 2";
        List<Map<String, Object>> maps = this.queryByNativeSQL(sql);
        List l = new ArrayList();
        for (Map<String, Object> m : maps) {
            TreeLevel tl = new TreeLevel();
            tl.setId(m.get("id").toString());
            tl.setParentId(m.get("pId").toString());
            tl.setName(m.get("name").toString());
            tl.setType(m.get("type").toString());
            l.add(tl);
        }
        return JSONArray.parseArray(BaseParser.parserListToLevelTree(l));
    }

    @Override
    public DataClassDto findByDataClassId(String dataClassId) {
        DataClassEntity dataClassEntity = this.dataClassDao.findByDataClassId(dataClassId);
        return this.dataClassMapper.toDto(dataClassEntity);
    }

    @Override
    public JSONArray getDatabaseClass() {
        List<DatabaseEntity> databaseList = this.databaseDao.findAll();
        String sql =
                "SELECT * FROM (SELECT CONCAT(ID,'-P') id,'999' pId,DATABASE_NAME name,'' DATA_CLASS_ID,1 type,true isParent FROM " +
                        "T_SOD_DATABASE_DEFINE WHERE USER_DISPLAY_CONTROL = 1 ORDER BY ID)" +
                        "UNION " +
                        "SELECT * FROM (SELECT ID id,CONCAT(DATABASE_DEFINE_ID,'-P') pId," +
                        "CONCAT(CONCAT(CONCAT(DATABASE_NAME,'('),SCHEMA_NAME),')') name,'' DATA_CLASS_ID,1 type,true isParent FROM " +
                        "T_SOD_DATABASE WHERE STOP_USE = false AND DATABASE_DEFINE_ID IN (SELECT ID FROM T_SOD_DATABASE_DEFINE WHERE USER_DISPLAY_CONTROL = 1) ORDER BY ID)";
        List<Map<String, Object>> list = this.queryByNativeSQL(sql);
        for (DatabaseEntity db : databaseList) {
            if (!db.getStopUse()) {
                sql = "SELECT DISTINCT CASE TYPE WHEN 2 THEN DATA_CLASS_ID ELSE CONCAT(DATA_CLASS_ID, '" + db.getId() + "') END id,CLASS_NAME name," +
                        "CONCAT(CASE PARENT_ID WHEN 0 THEN '' ELSE PARENT_ID END, '" + db.getId() + "') pId," +
                        "DATA_CLASS_ID,TYPE  type, " +
                        "CASE TYPE WHEN 1 THEN TRUE ELSE FALSE END isParent " +
                        "FROM T_SOD_DATA_CLASS " +
                        "START WITH DATA_CLASS_ID IN (SELECT DISTINCT DATA_CLASS_ID FROM T_SOD_DATA_LOGIC WHERE DATABASE_ID = '" + db.getId() + "') " +
                        "CONNECT BY PRIOR PARENT_ID = DATA_CLASS_ID ORDER BY id ";
                List<Map<String, Object>> dataList = this.queryByNativeSQL(sql);
                list.addAll(dataList);
            }
        }
        List l = new ArrayList();
        for (Map<String, Object> m : list) {
            TreeLevel tl = new TreeLevel();
            tl.setId(m.get("ID").toString());
            tl.setParentId(m.get("PID").toString());
            tl.setName(m.get("NAME").toString());
            tl.setType(m.get("TYPE").toString());
            l.add(tl);
        }
        return JSONArray.parseArray(BaseParser.parserListToLevelTree(l));
    }

    @Override
    public JSONArray getTree() {
        List<DataClassEntity> all = this.getAll();
        List l = new ArrayList();
        for (DataClassEntity d : all) {
            TreeLevel tl = new TreeLevel();
            tl.setId(d.getDataClassId());
            tl.setParentId(d.getParentId());
            tl.setName(d.getClassName());
            tl.setType(Integer.toString(d.getType()));
            l.add(tl);
        }
        return JSONArray.parseArray(BaseParser.parserListToLevelTree(l));
    }

    @Override
    public List<Map<String, Object>> getListBYIn(List<String> classIds, String className, String dDataId) {
        return this.dataClassDao.getListBYIn(classIds, StringUtils.isNotBlank(className) ? "%" + className + "%" : null, StringUtils.isNotBlank(dDataId) ? "%" + dDataId + "%" : null);
    }

    @Override
    public DataClassDto getDotById(String id) {
        DataClassEntity dataClassEntity = this.getById(id);
        return this.dataClassMapper.toDto(dataClassEntity);
    }

    @Override
    public void deleteByDataClassId(String dataClassId) {
        List<DataLogicEntity> dll = this.dataLogicDao.findByDataClassId(dataClassId);
        for (DataLogicEntity dl : dll) {
            List<DataTableEntity> dts = this.dataTableDao.findByClassLogic_Id(dl.getId());
            for (DataTableEntity dt:dts ) {
                this.shardingDao.deleteByTableId(dt.getId());
            }
            this.dataTableDao.deleteByClassLogic_Id(dl.getId());
        }
        this.dataLogicDao.deleteByDataClassId(dataClassId);
        this.dataClassDao.deleteByDataClassId(dataClassId);
    }

    public void importData() {
        String sql = "select * from DMIN_DATA_CLASS_TABLE";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            DataClassEntity dc = new DataClassEntity();
            dc.setClassName(m.get("CLASS_NAME").toString());
            dc.setDataClassId(m.get("DATA_CLASS_ID").toString());
            dc.setDDataId(m.get("D_DATA_ID").toString());
            dc.setFrequencyType(0);
            dc.setIfStopUse(false);
            dc.setIsAccess(1);
            dc.setIsAllLine(1);
            dc.setMetaDataName(m.get("META_DATA_NAME").toString());
            dc.setParentId(m.get("PARENT_CLASS_ID").toString());
            Object serial_no = m.get("SERIAL_NO");
            int n = 0;
            if (serial_no != null) {
                if (StringUtils.isNotEmpty(serial_no.toString())) {
                    n = Integer.parseInt(serial_no.toString());
                }
            }
            dc.setSerialNo(n);
            String meta_data_stor_type = m.get("META_DATA_STOR_TYPE").toString();
            if ("目录".equals(meta_data_stor_type)) dc.setType(1);
            else dc.setType(2);
            dc.setUseBaseInfo(1);
            dc.setDelFlag("0");
            dc.setCreateTime(new Date());
            save(dc);
        }
    }
}
