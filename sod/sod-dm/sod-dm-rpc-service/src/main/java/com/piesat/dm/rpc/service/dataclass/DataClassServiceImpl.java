package com.piesat.dm.rpc.service.dataclass;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSONArray;
import com.piesat.common.config.DatabseType;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.poi.ExcelUtil;
import com.piesat.dm.common.tree.BaseParser;
import com.piesat.dm.common.tree.TreeLevel;
import com.piesat.dm.dao.database.SchemaDao;
import com.piesat.dm.dao.dataclass.DataClassDao;
import com.piesat.dm.dao.dataclass.DataLogicDao;
import com.piesat.dm.dao.datatable.DataTableDao;
import com.piesat.dm.dao.datatable.ShardingDao;
import com.piesat.dm.dao.datatable.TableColumnDao;
import com.piesat.dm.dao.datatable.TableIndexDao;
import com.piesat.dm.entity.database.SchemaEntity;
import com.piesat.dm.entity.dataclass.DataClassBaseInfoEntity;
import com.piesat.dm.entity.dataclass.DataClassEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.AdvancedConfigService;
import com.piesat.dm.rpc.api.dataapply.DataAuthorityApplyService;
import com.piesat.dm.rpc.api.dataapply.NewdataApplyService;
import com.piesat.dm.rpc.api.dataapply.NewdataTableColumnService;
import com.piesat.dm.rpc.api.database.DatabaseUserService;
import com.piesat.dm.rpc.api.dataclass.*;
import com.piesat.dm.rpc.api.special.DatabaseSpecialReadWriteService;
import com.piesat.dm.rpc.api.special.DatabaseSpecialService;
import com.piesat.dm.rpc.dto.AdvancedConfigDto;
import com.piesat.dm.rpc.dto.dataapply.NewdataApplyDto;
import com.piesat.dm.rpc.dto.database.DatabaseUserDto;
import com.piesat.dm.rpc.dto.dataclass.*;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialDto;
import com.piesat.dm.rpc.mapper.dataclass.DataClassBaseInfoMapper;
import com.piesat.dm.rpc.mapper.dataclass.DataClassMapper;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    private SchemaDao schemaDao;
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
    @Autowired
    private DataClassLabelService dataClassLabelService;
    @Autowired
    private DataClassUserService dataClassUserService;
    @Autowired
    private DatabaseSpecialReadWriteService databaseSpecialReadWriteService;
    @Autowired
    private DataAuthorityApplyService dataAuthorityApplyService;
    @Autowired
    private NewdataTableColumnService newdataTableColumnService;
    @Autowired
    private AdvancedConfigService advancedConfigService;
    @Autowired
    private DatabaseSpecialService databaseSpecialService;
    @Autowired
    private DatabaseUserService databaseUserService;

    @Override
    public BaseDao<DataClassEntity> getBaseDao() {
        return dataClassDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataClassDto saveDto(DataClassDto dataClassDto) {

        UserDto loginUser = (UserDto) SecurityUtils.getSubject().getPrincipal();
        dataClassDto.setCreateBy(loginUser.getUserName());

        DataClassEntity dataClassEntity = this.dataClassMapper.toEntity(dataClassDto);
        List<DataClassLogicDto> byDataClassId = this.dataLogicService.findByDataClassId(dataClassDto.getDataClassId());

        List<DataClassLogicDto> dataLogicList = dataClassDto.getDataLogicList();

        List<String> all = byDataClassId.stream().map(DataClassLogicDto::getId).collect(Collectors.toList());
        List<String> nnn = new ArrayList<>();
        boolean haveTable = dataLogicList != null && StringUtils.isNotEmpty(dataLogicList.get(0).getDataClassId());
        if (haveTable) {
            nnn = dataLogicList.stream().map(DataClassLogicDto::getId).collect(Collectors.toList());
        }
        all.removeAll(nnn);
        for (String d : all) {
            dataLogicService.onlyDeleteById(d);
        }
        dataClassEntity = this.saveNotNull(dataClassEntity);
        List<DataClassLogicDto> dataLogicDtos = null;
        if (haveTable) {
            dataLogicDtos = this.dataLogicService.saveList(dataLogicList);
            for (DataClassLogicDto d : dataLogicDtos) {
                List<AdvancedConfigDto> sc = this.advancedConfigService.findByTableId(d.getTableId());
                if (sc != null && sc.size() > 0) {
                    AdvancedConfigDto advancedConfigDto = sc.get(0);
                    advancedConfigDto.setStorageDefineIdentifier(1);
                    this.advancedConfigService.updateDataAuthorityConfig(advancedConfigDto);
                } else {
                    AdvancedConfigDto advancedConfigDto = new AdvancedConfigDto();
                    advancedConfigDto.setTableId(d.getTableId());
                    advancedConfigDto.setStorageDefineIdentifier(1);
                    advancedConfigDto.setSyncIdentifier(2);
                    advancedConfigDto.setCleanIdentifier(2);
                    advancedConfigDto.setMoveIdentifier(2);
                    advancedConfigDto.setBackupIdentifier(2);
                    advancedConfigDto.setArchivingIdentifier(2);
                    this.advancedConfigService.saveDto(advancedConfigDto);
                }
            }
        }

        this.dataClassLabelService.deleteByDataClassId(dataClassDto.getDataClassId());
        List<DataClassLabelDto> dataClassLabelList = dataClassDto.getDataClassLabelList();

        if (dataClassLabelList != null && dataClassLabelList.size() > 0) {
            dataClassLabelList = dataClassLabelList.stream().map(e -> {
                DataClassLabelDto d = new DataClassLabelDto();
                d.setCreateTime(new DateTime());
                d.setLabelKey(e.getLabelKey());
                d.setDataClassId(dataClassDto.getDataClassId());
                return d;
            }).collect(Collectors.toList());
            this.dataClassLabelService.saveList(dataClassLabelList);
        }

        this.dataClassUserService.deleteByDataClassId(dataClassDto.getDataClassId());
        List<DataClassUserDto> dataClassUserList = dataClassDto.getDataClassUserList();

        if (dataClassUserList != null && dataClassUserList.size() > 0) {
            dataClassUserList = dataClassUserList.stream().map(e -> {
                DataClassUserDto d = new DataClassUserDto();
                d.setCreateTime(new DateTime());
                d.setUserName(e.getUserName());
                d.setDataClassId(dataClassDto.getDataClassId());
                return d;
            }).collect(Collectors.toList());
            this.dataClassUserService.saveList(dataClassUserList);
        }

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
        List<Map<String, Object>> maps = new ArrayList<>();
        if ("postgresql".equals(DatabseType.type)) {
            maps = this.mybatisQueryMapper.getLogicClassTreePostgresql();
        } else {
            maps = this.mybatisQueryMapper.getLogicClassTree();
        }

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
        List<SchemaEntity> databaseList = this.schemaDao.findByDatabase_UserDisplayControl(1);
        List<Map<String, Object>> list = this.mybatisQueryMapper.getDatabaseTree();
        for (SchemaEntity db : databaseList) {
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
        List<SchemaEntity> databaseList = this.schemaDao.findAll();
        List<Map<String, Object>> list = this.mybatisQueryMapper.getDatabaseTree();
        for (SchemaEntity db : databaseList) {
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

    @Override
    public JSONArray getDatabaseClassPostgresql() {
        List<SchemaEntity> databaseList = this.schemaDao.findByDatabase_UserDisplayControl(1);
        List<Map<String, Object>> list = this.mybatisQueryMapper.getDatabaseTreePostgresql();
        for (SchemaEntity db : databaseList) {
            if (!db.getStopUse()) {
                List<Map<String, Object>> dataList = this.mybatisQueryMapper.getDatabaseClassTreePostgresql(db.getId());
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
        List l = new ArrayList();
        Sort sort = Sort.by(Sort.Direction.ASC, "dataClassId");
        UserDto loginUser = (UserDto) SecurityUtils.getSubject().getPrincipal();
        List<DataClassEntity> all;
        if ("11".equals(loginUser.getUserType())) {
            List<DataClassUserDto> users = this.dataClassUserService.findByUserName(loginUser.getUserName());
            List<String> classIds = users.stream().map(DataClassUserDto::getDataClassId).collect(Collectors.toList());
            all = this.dataClassDao.findByDataClassIdInOrTypeOrderByDataClassIdAsc(classIds, 1);
        } else {
            all = this.getAll(sort);
        }
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
    public JSONArray getSimpleTree(String databaseId) {
        List<DataClassEntity> dataClassList = new ArrayList<>();
        if ("postgresql".equals(DatabseType.type)) {
            dataClassList = mybatisQueryMapper.getDataClassTreePostgresql(databaseId);
        } else {
            dataClassList = mybatisQueryMapper.getDataClassTree(databaseId);
        }
        List l = new ArrayList();
        for (DataClassEntity d : dataClassList) {
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
    public List<Map<String, Object>> getListBYIn(List<String> classIds, String className, String dDataId, String dataclassId) {
        if (StringUtils.isNotBlank(className) || StringUtils.isNotBlank(dDataId) || StringUtils.isNotBlank(dataclassId)) {
            classIds = null;
        }
        return this.mybatisQueryMapper.getDataClassListBYIn(classIds, StringUtils.isNotBlank(className) ? "%" + className + "%" : null, StringUtils.isNotBlank(dDataId) ? "%" + dDataId + "%" : null, StringUtils.isNotBlank(dataclassId) ? "%" + dataclassId + "%" : null);
    }

    @Override
    public DataClassDto getDotById(String id) {
        DataClassEntity dataClassEntity = this.getById(id);
        return this.dataClassMapper.toDto(dataClassEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByDataClassId(String dataClassId) {
        logger.info("根据dataClassId删除资料:" + dataClassId);
        DataClassDto dataClass = this.findByDataClassId(dataClassId);
        if (dataClass == null) {
            logger.info("根据dataClassId删除资料,查出资料为null:" + dataClassId);
        }
        this.dataClassLabelService.deleteByDataClassId(dataClassId);
        this.dataLogicDao.deleteByDataClassId(dataClassId);
        this.dataClassDao.deleteByDataClassId(dataClassId);
        this.databaseSpecialReadWriteService.deleteByDataClassId(dataClassId);
        this.dataAuthorityApplyService.deleteByDataClassId(dataClassId);
        if (dataClass != null) {
            List<NewdataApplyDto> newDataApplyDtoList = this.newdataApplyService.findByDataClassIdAndUserId(dataClassId, dataClass.getCreateBy());
            if (newDataApplyDtoList != null) {
                newDataApplyDtoList.forEach(e -> {
                    this.newdataApplyService.deleteById(e.getId());
                    this.newdataTableColumnService.deleteByApplyId(e.getId());
                });
            }
        }

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
        if (dataClassDto.getType() != null) {
            ssb.add("type", SpecificationOperator.Operator.eq.name(), dataClassDto.getType());
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
        ssb.add("type", SpecificationOperator.Operator.eq.name(), 2);
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        List<DataClassEntity> all = this.getAll(ssb.generateSpecification());
        ExcelUtil<DataClassEntity> util = new ExcelUtil(DataClassEntity.class);
        util.exportExcel(all, "资料概览信息");
    }

    @Override
    public String findByParentId(String parentId) {
        if (parentId.contains("S") || parentId.contains("Z.9999.9999")) {
            String m = "";
            if (parentId.contains("S")) {
                String classId = parentId.replaceAll("S", "M");
                m = classId.substring(0, classId.indexOf(".M") + 2);
            } else {
                m = "Z.9999.9999.M";
            }

            List<DataClassEntity> cs = this.dataClassDao.findByTypeAndDataClassIdLikeOrderByDataClassIdDesc(2, m + "%");
            if (cs.size() > 0) {
                String dataClassId = cs.get(0).getDataClassId();
                int no;
                try {
                    int l = dataClassId.length() > 13 ? 3 : 4;
                    no = Integer.parseInt(dataClassId.substring(dataClassId.length() - l));
                } catch (Exception e) {
                    return m;
                }
                no++;
                DecimalFormat df = new DecimalFormat("000");
                String str = df.format(no);
                return m + str;
            } else {
                return m + "001";
            }
        } else {
            return parentId;
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
        if (dataclasslist.size() == 0) {
            map.put("returnCode", 1);
            map.put("returnMessage", "存储编码不存在");
            return map;
        }
        String use_base_info = dataclasslist.get(0).get("USE_BASE_INFO").toString();
        DataClassBaseInfoDto dataClassCoreInfos = new DataClassBaseInfoDto();

        if ("1".equals(use_base_info)) {
            dataClassCoreInfos = dataClassBaseInfoService.getDataClassBaseInfo(dataClassId);
        } else {
            DataClassBaseInfoEntity dataClassBaseInfo = mybatisQueryMapper.getDataClassBaseInfo(dataClassId);
            dataClassCoreInfos = this.dataClassBaseInfoMapper.toDto(dataClassBaseInfo);
        }
        String C_COREMETA_ID = dataClassCoreInfos.getCCoremetaId();
        List<LinkedHashMap<String, Object>> tempele = mybatisQueryMapper.selectTabOmincmccTempele(C_COREMETA_ID);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < tempele.size(); i++) {
            Map<String, Object> m = new HashMap<>();
            m.put("C_BEGIN", tempele.get(i).get("C_BEGIN"));
            m.put("C_END", tempele.get(i).get("C_END"));
            m.put("C_OBSFREQ", tempele.get(i).get("C_OBSFREQ"));
            if (m.get("C_OBSFREQ") != null && !"".equals(m.get("C_OBSFREQ"))) {
                dataClassCoreInfos.setCBegin(m.get("C_BEGIN").toString());
                dataClassCoreInfos.setCEnd(m.get("C_END").toString());
                dataClassCoreInfos.setCObsfreq(m.get("C_OBSFREQ").toString());
            }
        }
        DataClassBaseInfoDto dataClassCoreInfo = dataClassCoreInfos;
        if (c_datum_code.startsWith("F") && "1".equals(use_base_info)) {
            List<LinkedHashMap<String, Object>> select = mybatisQueryMapper.selectGridAreaDefine(dataClassId);
            if (select.size() > 0) {
                LinkedHashMap<String, Object> gad = select.get(0);
                String AREA_REGION_DESC = gad.get("AREA_REGION_DESC").toString();
                String[] split = AREA_REGION_DESC.split(";");
                if (split.length > 1) {
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
        }
        dataClassCoreInfo.setCDatascal(dataClassCoreInfo.getCDatascal());
        dataClassCoreInfo.setCSource(dataClassCoreInfo.getCSource());
        dataClassCoreInfo.setCIdabs(dataClassCoreInfo.getCIdabs());
        map.put("data", dataClassCoreInfo);
        map.put("returnCode", 0);
        map.put("returnMessage", "获取数据成功");
        return map;
    }

    @Override
    public DataClassDto updateIsAllLine(DataClassDto dataClassDto) {
        DataClassDto d = this.findByDataClassId(dataClassDto.getDataClassId());
        d.setIsAllLine(dataClassDto.getIsAllLine());
        return dataClassMapper.toDto(this.saveNotNull(dataClassMapper.toEntity(d)));
    }

    @Override
    public Map<String, Object> getArchive(String ddataid) {
        List<Map<String, Object>> archive = this.mybatisQueryMapper.getArchive(ddataid);
        Date beginTime = null;
        Date endTime = null;
        for (Map<String, Object> m : archive) {
            Object databegintime = m.get("DATABEGINTIME");
            Object dataendtime = m.get("DATAENDTIME");
            if (databegintime instanceof Date) {
                Date begin = (Date) databegintime;
                if (beginTime == null || beginTime.after(begin)) {
                    beginTime = begin;
                }
            }
            if (dataendtime instanceof Date) {
                Date end = (Date) dataendtime;
                if (endTime == null || endTime.before(end)) {
                    endTime = end;
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        return map;
    }

    @Override
    public ResultT haveClassByDataId(String dataId) {
        List<DataClassEntity> dataClassEntities = this.dataClassDao.findByDDataId(dataId);
        if (dataClassEntities.size() > 0) {
            List<DataClassLogicDto> dataLogicDto = this.dataLogicService.findByDataClassId(dataClassEntities.get(0).getDataClassId());
            if (dataLogicDto.size() > 0) {
                return ResultT.success(true);
            }
        }
        return ResultT.success(false);
    }

    @Override
    public List<DataClassDto> findByDataClassIdAndCreateBy(String dataclassId, String userId) {
        List<DataClassEntity> byDataClassIdAndCreateBy = this.dataClassDao.findByDataClassIdAndCreateBy(dataclassId, userId);
        return this.dataClassMapper.toDto(byDataClassIdAndCreateBy);
    }

    @Override
    public ResultT<List<Map<String, Object>>> getClassByTableId(String tableId) {
        return ResultT.success(this.dataClassDao.getClassByTableId(tableId));
    }

    @Override
    public void deleteByBizUserId(String bizUserid) {
        //删除业务用户注册资料
        List<NewdataApplyDto> newdataApplyDtos = this.newdataApplyService.findByUserId(bizUserid);
        if (newdataApplyDtos != null && newdataApplyDtos.size() > 0) {
            for (int i = 0; i < newdataApplyDtos.size(); i++) {
                newdataApplyService.deleteById(newdataApplyDtos.get(i).getId());
            }
        }
        //专题库
        List<DatabaseSpecialDto> databaseSpecialDtos = this.databaseSpecialService.findByUserId(bizUserid);
        if (databaseSpecialDtos != null && databaseSpecialDtos.size() > 0) {
            for (int i = 0; i < databaseSpecialDtos.size(); i++) {
                databaseSpecialService.deleteById(databaseSpecialDtos.get(i).getId());
            }
        }
        //数据库访问账户
        List<DatabaseUserDto> databaseUserDtos = this.databaseUserService.getByUserId(bizUserid);
        if (databaseUserDtos != null && databaseUserDtos.size() > 0) {
            for (int i = 0; i < databaseUserDtos.size(); i++) {
                this.databaseUserService.deleteById(databaseUserDtos.get(i).getId());
            }
        }
        //资料访问权限
        dataAuthorityApplyService.deleteByUserId(bizUserid);
    }

    @Override
    public List<Map<String, Object>> getTableInfo(String dataclassId) {
        return this.dataClassDao.getTableInfo(dataclassId);
    }
}
