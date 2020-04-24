package com.piesat.dm.rpc.service.dataclass;

import com.alibaba.fastjson.JSONArray;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.poi.ExcelUtil;
import com.piesat.dm.common.tree.BaseParser;
import com.piesat.dm.common.tree.TreeLevel;
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.dao.dataclass.DataClassDao;
import com.piesat.dm.dao.dataclass.DataLogicDao;
import com.piesat.dm.dao.datatable.DataTableDao;
import com.piesat.dm.dao.datatable.ShardingDao;
import com.piesat.dm.dao.datatable.TableColumnDao;
import com.piesat.dm.dao.datatable.TableIndexDao;
import com.piesat.dm.entity.dataclass.DataClassBaseInfoEntity;
import com.piesat.dm.entity.dataclass.DataClassEntity;
import com.piesat.dm.entity.dataclass.DataLogicEntity;
import com.piesat.dm.entity.datatable.DataTableEntity;
import com.piesat.dm.entity.database.DatabaseEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.dataapply.NewdataApplyService;
import com.piesat.dm.rpc.api.dataclass.DataClassBaseInfoService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.api.dataclass.DataLogicService;
import com.piesat.dm.rpc.dto.dataapply.NewdataApplyDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassBaseInfoDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.dm.rpc.dto.dataclass.DataLogicDto;
import com.piesat.dm.rpc.mapper.dataclass.DataClassBaseInfoMapper;
import com.piesat.dm.rpc.mapper.dataclass.DataClassMapper;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import com.xugu.cloudjdbc.Clob;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.*;

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
    @Autowired
    private DataLogicService dataLogicService;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private TableColumnDao tableColumnDao;
    @Autowired
    private TableIndexDao tableIndexDao;
    @Autowired
    private NewdataApplyService newdataApplyService;
    @Autowired
    private DataClassBaseInfoService dataClassBaseInfoService;
    @Autowired
    private DataClassBaseInfoMapper dataClassBaseInfoMapper;
    @Override
    public BaseDao<DataClassEntity> getBaseDao() {
        return dataClassDao;
    }

    @Override
    public DataClassDto saveDto(DataClassDto dataClassDto) {
        NewdataApplyDto newdataApplyDto = null;
        if (StringUtils.isNotBlank(dataClassDto.getApplyId())){
            newdataApplyDto = this.newdataApplyService.getDotById(dataClassDto.getApplyId());
            dataClassDto.setCreateBy(newdataApplyDto.getUserId());
        }else {
            UserDto loginUser =(UserDto) SecurityUtils.getSubject().getPrincipal();
            dataClassDto.setCreateBy(loginUser.getUserName());
        }
        DataClassEntity dataClassEntity = this.dataClassMapper.toEntity(dataClassDto);
        dataClassEntity = this.saveNotNull(dataClassEntity);
        if (newdataApplyDto!=null){
            newdataApplyDto.setDataClassId(dataClassEntity.getDataClassId());
            this.newdataApplyService.saveDto(newdataApplyDto);
        }
        List<DataLogicDto> byDataClassId = this.dataLogicService.findByDataClassId(dataClassDto.getDataClassId());
        byDataClassId.removeAll(dataClassDto.getDataLogicList());
        for (DataLogicDto d:byDataClassId ) {
            dataLogicService.deleteById(d.getId());
        }
        List<DataLogicDto> dataLogicDtos = this.dataLogicService.saveList(dataClassDto.getDataLogicList());
        DataClassDto dataClassDto1 = this.dataClassMapper.toDto(dataClassEntity);
        dataClassDto1.setDataLogicList(dataLogicDtos);
        return dataClassDto1;
    }

    @Override
    public List<DataClassDto> all() {
        List<DataClassEntity> all = this.getAll();
        return this.dataClassMapper.toDto(all);
    }


    @Override
    public JSONArray getLogicClass() {
        List<Map<String, Object>> maps = this.mybatisQueryMapper.getLogicClassTree();
        List l = new ArrayList();
        for (Map<String, Object> m : maps) {
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
    public DataClassDto findByDataClassId(String dataClassId) {
        DataClassEntity dataClassEntity = this.dataClassDao.findByDataClassId(dataClassId);
        return this.dataClassMapper.toDto(dataClassEntity);
    }

    @Override
    public List<DataClassDto> findByDDataId(String dDataId) {
        List<DataClassEntity> dataClassEntities = this.dataClassDao.findByDDataId(dDataId);
        return this.dataClassMapper.toDto(dataClassEntities);
    }

    @Override
    public JSONArray getDatabaseClass() {
        List<DatabaseEntity> databaseList = this.databaseDao.findAll();
        List<Map<String, Object>> list = this.mybatisQueryMapper.getDatabaseTree();
        for (DatabaseEntity db : databaseList) {
            if (!db.getStopUse()) {
                List<Map<String, Object>> dataList = this.mybatisQueryMapper.getDatabaseClassTree(db.getId());
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
    public JSONArray getDatabaseClassMysql() {
        List<DatabaseEntity> databaseList = this.databaseDao.findAll();
        List<Map<String, Object>> list = this.mybatisQueryMapper.getDatabaseTree();
        for (DatabaseEntity db : databaseList) {
            if (!db.getStopUse()) {
                List<Map<String, Object>> dataList = this.mybatisQueryMapper.getDatabaseClassTreeMysql(db.getId());
                list.addAll(dataList);
                List<String> l = new ArrayList<>();
                for (Map map : dataList) {
                    l.add(map.get("PARENT_ID").toString());
                }
                if (l.size() > 0) {
                    getParents(list, l, db.getId());
                }
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

    public void getParents(List<Map<String, Object>> list, List<String> classIds, String id) {
        List<Map<String, Object>> databaseClassTreePMysql = this.mybatisQueryMapper.getDatabaseClassTreePMysql(classIds, id);
        list.addAll(databaseClassTreePMysql);
        List<String> l = new ArrayList<>();
        for (Map<String, Object> map : databaseClassTreePMysql) {
            l.add(map.get("PARENT_ID").toString());
        }
        if (l.size() > 0) {
            getParents(list, l, id);
        } else {
            return;
        }
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
    public JSONArray getSimpleTree(){
        List<DataClassEntity> all = this.getAll();
        List l = new ArrayList();
        for (DataClassEntity d : all) {
            TreeLevel tl = new TreeLevel();
            tl.setId(d.getDataClassId());
            tl.setParentId(d.getParentId());
            tl.setName(d.getClassName());
            tl.setType("1");
            l.add(tl);
        }
        return JSONArray.parseArray(BaseParser.parserListToLevelTree(l));
    }

    @Override
    public List<Map<String, Object>> getListBYIn(List<String> classIds, String className, String dDataId) {
        return this.mybatisQueryMapper.getDataClassListBYIn(classIds, StringUtils.isNotBlank(className) ? "%" + className + "%" : null, StringUtils.isNotBlank(dDataId) ? "%" + dDataId + "%" : null);
    }

    @Override
    public DataClassDto getDotById(String id) {
        DataClassEntity dataClassEntity = this.getById(id);
        return this.dataClassMapper.toDto(dataClassEntity);
    }

    @Override
    @Transactional
    public void deleteByDataClassId(String dataClassId) {
        List<DataLogicEntity> dll = this.dataLogicDao.findByDataClassId(dataClassId);
        for (DataLogicEntity dl : dll) {
            List<DataTableEntity> dts = this.dataTableDao.findByClassLogic_Id(dl.getId());
            for (DataTableEntity dt : dts) {
                this.shardingDao.deleteByTableId(dt.getId());
                this.tableColumnDao.deleteByTableId(dt.getId());
                this.tableIndexDao.deleteByTableId(dt.getId());
            }
            this.dataTableDao.deleteByClassLogic_Id(dl.getId());
        }
        this.dataLogicDao.deleteByDataClassId(dataClassId);
        this.dataClassDao.deleteByDataClassId(dataClassId);
    }

    @Override
    public List<Map<String, Object>> getDataTypeList() {
        String sql = "select data_class_id,class_name from t_sod_data_class where length(data_class_id)=1";
        List<Map<String, Object>> list = this.queryByNativeSQL(sql);
        return list;
    }

    /**
     * 获取所有目录
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getDataGroup() {
        String sql = "select * from t_sod_data_class where type=1 order by data_class_id";
        List<Map<String, Object>> list = this.queryByNativeSQL(sql);
        return list;
    }

    @Override
    public String getDataClassIdNum() {
        String xuguSql = "select" +
                "   case" +
                "      when not exists(select 1 from (SELECT atol(substr(DATA_CLASS_ID,14,3)) id FROM DMIN_DATA_CLASS_TABLE WHERE META_DATA_STOR_TYPE = '资料' AND D_DATA_ID = 'Z.9999.9999'" +
                "AND substr(DATA_CLASS_ID,13,1)='M') where id=1)" +
                "      then 1" +
                "      else (" +
                "         select min(a.id+1)" +
                "           from (SELECT atol(substr(DATA_CLASS_ID,14,3)) id FROM DMIN_DATA_CLASS_TABLE WHERE META_DATA_STOR_TYPE = '资料' AND D_DATA_ID = 'Z.9999.9999'" +
                "AND substr(DATA_CLASS_ID,13,1)='M') as a" +
                "        where not exists" +
                "        (" +
                "          select 1" +
                "            from (SELECT atol(substr(DATA_CLASS_ID,14,3)) id FROM DMIN_DATA_CLASS_TABLE WHERE META_DATA_STOR_TYPE = '资料' AND D_DATA_ID = 'Z.9999.9999'" +
                "AND substr(DATA_CLASS_ID,13,1)='M') as b" +
                "         where b.id=a.id+1" +
                "        )" +
                "     )" +
                "  end as NUM;";

        String mysqlSql = "select" +
                "   case" +
                "      when not exists(select 1 from (SELECT cast(substr(DATA_CLASS_ID,14,3) as SIGNED) id FROM DMIN_DATA_CLASS_TABLE WHERE META_DATA_STOR_TYPE = '资料' AND D_DATA_ID = 'Z.9999.9999'" +
                "AND substr(DATA_CLASS_ID,13,1)='M') where id=1)" +
                "      then 1" +
                "      else (" +
                "         select min(a.id+1)" +
                "           from (SELECT cast(substr(DATA_CLASS_ID,14,3) as SIGNED) id FROM DMIN_DATA_CLASS_TABLE WHERE META_DATA_STOR_TYPE = '资料' AND D_DATA_ID = 'Z.9999.9999'" +
                "AND substr(DATA_CLASS_ID,13,1)='M') as a" +
                "        where not exists" +
                "        (" +
                "          select 1" +
                "            from (SELECT cast(substr(DATA_CLASS_ID,14,3) as SIGNED) id FROM DMIN_DATA_CLASS_TABLE WHERE META_DATA_STOR_TYPE = '资料' AND D_DATA_ID = 'Z.9999.9999'" +
                "AND substr(DATA_CLASS_ID,13,1)='M') as b" +
                "         where b.id=a.id+1" +
                "        )" +
                "     )" +
                "  end as NUM;";
        List<Map<String, Object>> list = this.queryByNativeSQL(mysqlSql);
        String num = list.get(0).get("NUM").toString();
        if (num.length() < 3) {
            String ling = "00";
            int a = 3 - num.length();
            num = ling.substring(0, a) + num;
        }
        return num;
    }

    @Override
    public PageBean getBaseData(PageForm<Map<String, String>> pageForm, DataClassDto dataClassDto) {
        SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder();
        if (StringUtils.isNotBlank(dataClassDto.getClassName())) {
            ssb.add("className", SpecificationOperator.Operator.likeAll.name(), dataClassDto.getClassName());
        }
        if (dataClassDto.getIsAccess() != null) {
            ssb.add("isAccess", SpecificationOperator.Operator.eq.name(), dataClassDto.getIsAccess());
        }
        if (dataClassDto.getIfStopUse() != null) {
            ssb.add("ifStopUse", SpecificationOperator.Operator.eq.name(), dataClassDto.getIfStopUse());
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        PageBean page = this.getPage(ssb.generateSpecification(), pageForm, sort);
        return page;
    }

    @Override
    public List<DataClassDto> findAllCategory() {
        List<DataClassEntity> dataClassIdAsc = this.dataClassDao.findByParentIdOrderByDataClassIdAsc("0");
        return this.dataClassMapper.toDto(dataClassIdAsc);
    }

    @Override
    public void exportBaseData(DataClassDto dataClassDto) {
        SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder();
        if (StringUtils.isNotBlank(dataClassDto.getClassName())) {
            ssb.add("className", SpecificationOperator.Operator.likeAll.name(), dataClassDto.getClassName());
        }
        if (dataClassDto.getIsAccess() != null) {
            ssb.add("isAccess", SpecificationOperator.Operator.eq.name(), dataClassDto.getIsAccess());
        }
        if (dataClassDto.getIfStopUse() != null) {
            ssb.add("ifStopUse", SpecificationOperator.Operator.eq.name(), dataClassDto.getIfStopUse());
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        List<DataClassEntity> all = this.getAll(ssb.generateSpecification());
        ExcelUtil<DataClassEntity> util=new ExcelUtil(DataClassEntity.class);
        util.exportExcel(all,"资料概览信息");
    }

    @Override
    public String findByParentId(String parentId) {
        List<DataClassEntity> dataClassIdAsc = this.dataClassDao.findByParentIdAndTypeOrderByDataClassIdDesc(parentId,2);
        List<DataClassDto> dataClassDtos = this.dataClassMapper.toDto(dataClassIdAsc);
        if (parentId.length() > 8) {
            if (dataClassDtos.size() > 0) {
                String dataClassId = dataClassDtos.get(0).getDataClassId();
                String newId = dataClassId.substring(0,dataClassId.length()-5);
                int no;
                try {
                    int l = dataClassId.length() > 13 ? 3 : 4;
                    no = Integer.parseInt(dataClassId.substring(dataClassId.length() - l));
                } catch (Exception e) {
                    return newId + ".M";
                }
                no++;
                DecimalFormat df = new DecimalFormat("000");
                String str = df.format(no);
                return newId + ".M" + str;
            } else {
                return parentId + ".M001";
            }
        } else {
            if (dataClassDtos.size() > 0) {
                String dataClassId = dataClassDtos.get(0).getDataClassId();
                String newId = dataClassId.substring(0,dataClassId.length()-5);
                int no;
                try {
                    int l = dataClassId.length() > 13 ? 3 : 4;
                    no = Integer.parseInt(dataClassId.substring(dataClassId.length() - l));
                } catch (Exception e) {
                    return newId + ".";
                }
                no++;
                DecimalFormat df = new DecimalFormat("0000");
                String str = df.format(no);
                return newId + "." + str;
            } else {
                return parentId + ".0001";
            }
        }
    }

    @Override
    public List<Map<String, Object>> getLogicByDdataId(String dDataId) {
        return this.mybatisQueryMapper.getLogicByDdataId(dDataId);
    }
    @Override
    public Map<String, Object> getDataClassCoreInfo(String c_datum_code) {
        String dataClassId = c_datum_code;
        Map<String, Object> map = new HashMap<>();
        List<LinkedHashMap<String, Object>> dataclasslist = mybatisQueryMapper.getDataClassInfo(dataClassId);
        if (dataclasslist.size()==0){
            map.put("returnCode", 1);
            map.put("returnMessage", "存储编码不存在");
            return map;
        }
        String use_base_info = dataclasslist.get(0).get("USE_BASE_INFO").toString();
        DataClassBaseInfoDto dataClassCoreInfos = new DataClassBaseInfoDto();

        if ("1".equals(use_base_info)){
            dataClassCoreInfos=dataClassBaseInfoService.getDataClassBaseInfo(dataClassId);
        }else{
            DataClassBaseInfoEntity dataClassBaseInfo = mybatisQueryMapper.getDataClassBaseInfo(dataClassId);
            dataClassCoreInfos = this.dataClassBaseInfoMapper.toDto(dataClassBaseInfo);
        }
        String C_COREMETA_ID=dataClassCoreInfos.getCCoremetaId();
        List<LinkedHashMap<String, Object>> tempele = mybatisQueryMapper.selectTabOmincmccTempele(C_COREMETA_ID);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < tempele.size(); i++) {
            Map<String, Object> m = new HashMap<>();
            m.put("C_BEGIN",tempele.get(i).get("C_BEGIN"));
            m.put("C_END",tempele.get(i).get("C_END"));
            m.put("C_OBSFREQ",tempele.get(i).get("C_OBSFREQ"));
            if (m.get("C_OBSFREQ")!=null&&!"".equals(m.get("C_OBSFREQ"))){
                dataClassCoreInfos.setCBegin(m.get("C_BEGIN").toString());
                dataClassCoreInfos.setCEnd(m.get("C_END").toString());
                dataClassCoreInfos.setCObsfreq(m.get("C_OBSFREQ").toString());
            }
        }
        DataClassBaseInfoDto dataClassCoreInfo = dataClassCoreInfos;
        if (c_datum_code.startsWith("F")&&"1".equals(use_base_info)) {
            List<LinkedHashMap<String, Object>> select = mybatisQueryMapper.selectGridAreaDefine(dataClassId);
            if (select.size() > 0) {
                LinkedHashMap<String, Object> gad = select.get(0);
                String AREA_REGION_DESC = gad.get("AREA_REGION_DESC").toString();
                String[] split = AREA_REGION_DESC.split(";");
                int flagbegin = split[0].indexOf("[");
                int flagend = split[0].indexOf("]");
                int flagbegin1 = split[1].indexOf("[");
                int flagend1 = split[1].indexOf("]");
                String lat = split[0].substring(flagbegin + 1, flagend);
                String lon = split[1].substring(flagbegin1 + 1, flagend1);
                dataClassCoreInfo.setCWestbl(lon.split(",")[0]);
                dataClassCoreInfo.setCEastbl(lon.split(",")[1]);
                dataClassCoreInfo.setCSouthbl(lat.split(",")[1]);
                dataClassCoreInfo.setCNorthbl(lat.split(",")[0]);
            }
        }
        dataClassCoreInfo.setCDatascal(dataClassCoreInfo.getCDatascal());
        dataClassCoreInfo.setCSource(dataClassCoreInfo.getCSource());
        dataClassCoreInfo.setCIdabs(dataClassCoreInfo.getCIdabs());
        map.put("data", dataClassCoreInfo);
        map.put("returnCode", 0);
        map.put("returnMessage", "获取数据成功");
        return  map;
    }
}
