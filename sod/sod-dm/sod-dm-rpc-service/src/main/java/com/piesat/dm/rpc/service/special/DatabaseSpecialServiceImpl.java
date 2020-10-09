package com.piesat.dm.rpc.service.special;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.common.utils.poi.ExcelUtil;
import com.piesat.dm.common.tree.Ztree;
import com.piesat.dm.core.api.DatabaseDcl;
import com.piesat.dm.core.api.impl.Cassandra;
import com.piesat.dm.core.api.impl.Gbase8a;
import com.piesat.dm.core.api.impl.Xugu;
import com.piesat.dm.core.parser.DatabaseInfo;
import com.piesat.dm.dao.ReadAuthorityDao;
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.dao.database.DatabaseDefineDao;
import com.piesat.dm.dao.database.DatabaseUserDao;
import com.piesat.dm.dao.datatable.DataTableDao;
import com.piesat.dm.dao.special.*;
import com.piesat.dm.entity.ReadAuthorityEntity;
import com.piesat.dm.entity.database.DatabaseAdministratorEntity;
import com.piesat.dm.entity.database.DatabaseDefineEntity;
import com.piesat.dm.entity.database.DatabaseEntity;
import com.piesat.dm.entity.database.DatabaseUserEntity;
import com.piesat.dm.entity.datatable.DataTableEntity;
import com.piesat.dm.entity.special.*;
import com.piesat.dm.mapper.MybatisModifyMapper;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.dataapply.DataAuthorityApplyService;
import com.piesat.dm.rpc.api.special.DatabaseSpecialAuthorityService;
import com.piesat.dm.rpc.api.special.DatabaseSpecialService;
import com.piesat.dm.rpc.dto.dataapply.CloudDatabaseApplyDto;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityApplyDto;
import com.piesat.dm.rpc.dto.database.DatabaseDefineDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialAccessDto;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialAuthorityDto;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialDto;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialReadWriteDto;
import com.piesat.dm.rpc.mapper.database.DatabaseDefineMapper;
import com.piesat.dm.rpc.mapper.database.DatabaseMapper;
import com.piesat.dm.rpc.mapper.special.DatabaseSpecialAccessMapper;
import com.piesat.dm.rpc.mapper.special.DatabaseSpecialAuthorityMapper;
import com.piesat.dm.rpc.mapper.special.DatabaseSpecialMapper;
import com.piesat.dm.rpc.mapper.special.DatabaseSpecialReadWriteMapper;
import com.piesat.dm.rpc.util.DatabaseUtil;
import com.piesat.ucenter.dao.system.UserDao;
import com.piesat.ucenter.entity.system.UserEntity;
import com.piesat.util.GetAllUserInfo;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 专题库管理
 */
@Service
public class DatabaseSpecialServiceImpl extends BaseService<DatabaseSpecialEntity> implements DatabaseSpecialService {
    @Autowired
    private DatabaseSpecialDao databaseSpecialDao;
    @Autowired
    private DatabaseSpecialMapper databaseSpecialMapper;
    @Autowired
    private DatabaseUserDao databaseUserDao;
    @Autowired
    private DatabaseSpecialAuthorityDao databaseSpecialAuthorityDao;
    @Autowired
    private DatabaseSpecialAuthorityMapper databaseSpecialAuthorityMapper;
    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    private DatabaseMapper databaseMapper;
    @Autowired
    private DatabaseSpecialReadWriteDao databaseSpecialReadWriteDao;
    @Autowired
    private DatabaseSpecialReadWriteMapper databaseSpecialReadWriteMapper;
    @Autowired
    private DataTableDao dataTableDao;
    @Autowired
    private DatabaseInfo databaseInfo;
    @Autowired
    private DatabaseDefineDao databaseDefineDao;
    @Autowired
    private DatabaseDefineMapper databaseDefineMapper;
    @Autowired
    private DatabaseSpecialTreeDao databaseSpecialTreeDao;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private DataAuthorityApplyService dataAuthorityApplyService;
    @Autowired
    private DatabaseSpecialAccessDao databaseSpecialAccessDao;
    @Autowired
    private MybatisModifyMapper mybatisModifyMapper;

    @Autowired
    private DatabaseSpecialAccessMapper databaseSpecialAccessMapper;

    @Autowired
    private ReadAuthorityDao readAuthorityDao;

    @GrpcHthtClient
    private UserDao userDao;

    @Autowired
    private DatabaseSpecialAuthorityService databaseSpecialAuthorityService;

    @Override
    public BaseDao<DatabaseSpecialEntity> getBaseDao() {
        return databaseSpecialDao;
    }

    @Override
    public List<DatabaseSpecialDto> all() {
        List<DatabaseSpecialEntity> all = this.getAll();
        return this.databaseSpecialMapper.toDto(all);
    }

    @Override
    public PageBean getPage(PageForm<DatabaseSpecialDto> pageForm) {
        DatabaseSpecialEntity databaseSpecialEntity = this.databaseSpecialMapper.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(databaseSpecialEntity.getExamineStatus())) {
            specificationBuilder.add("examineStatus", SpecificationOperator.Operator.eq.name(), databaseSpecialEntity.getExamineStatus());
        }
        if (StringUtils.isNotNullString(databaseSpecialEntity.getSdbName())) {
            specificationBuilder.add("sdbName", SpecificationOperator.Operator.likeAll.name(), databaseSpecialEntity.getSdbName());
        }
        if (StringUtils.isNotNullString(pageForm.getT().getUserName())) {
            //调用接口 根据用户名查询用户id
            List<String> userId = new ArrayList<String>();
            userId.add("noUseId");
            List<UserEntity> userEntities = userDao.findByWebUsernameLike("%" + pageForm.getT().getUserName() + "%");
            if (userEntities != null && userEntities.size() > 0) {
                for (UserEntity userEntity : userEntities) {
                    userId.add(userEntity.getUserName());
                }
            }
            specificationBuilder.add("userId", SpecificationOperator.Operator.in.name(), userId);
        }
        List<String> userId = new ArrayList<String>();
        userId.add("noUseId");
        List<UserEntity> users = userDao.findByCheckedNot("0");
        if (users != null && users.size() > 0) {
            for (UserEntity userEntity : users) {
                userId.add(userEntity.getUserName());
            }
        }
        specificationBuilder.add("userId", SpecificationOperator.Operator.in.name(), userId);
        Sort sort = Sort.by(Sort.Direction.ASC, "examineStatus").and(Sort.by(Sort.Direction.DESC, "createTime"));
        PageBean pageBean = this.getPage(specificationBuilder.generateSpecification(), pageForm, sort);
        List<DatabaseSpecialEntity> databaseSpecialEntities = (List<DatabaseSpecialEntity>) pageBean.getPageData();

        List<DatabaseSpecialDto> databaseSpecialDtos = databaseSpecialMapper.toDto(databaseSpecialEntities);

        //调用接口获取所有的用户信息
        List<UserEntity> userEntities = userDao.findByUserType("11");
        //循环遍历
        for (DatabaseSpecialDto databaseSpecialDto : databaseSpecialDtos) {
            //遍历所有用户信息找到每条记录对应的用户信息
            for (UserEntity userEntity : userEntities) {
                if (userEntity.getUserName().equals(databaseSpecialDto.getUserId())) {
                    databaseSpecialDto.setUserName(userEntity.getWebUsername());
                    databaseSpecialDto.setUserPhone(userEntity.getPhonenumber());
                    databaseSpecialDto.setDepartment(userEntity.getDeptName());
                }
            }
        }

        pageBean.setPageData(databaseSpecialDtos);
        return pageBean;
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        if (StringUtils.isNotEmpty(id)) {
            this.databaseSpecialAuthorityDao.deleteBySdbId(id);
            this.databaseSpecialReadWriteDao.deleteBySdbId(id);
            databaseDao.deleteByTdbId(id);
            this.delete(id);
        }
    }

    @Transactional
    @Override
    public void deleteAccessBySdbIdAndUserId(String sdbId, String userId) {
        this.databaseSpecialAccessDao.deleteBySdbIdAndUserId(sdbId, userId);
    }

    @Override
    public List<DatabaseSpecialDto> findByUserId(String userId) {
        List<DatabaseSpecialEntity> byUserId = this.databaseSpecialDao.findByUserId(userId);
        return this.databaseSpecialMapper.toDto(byUserId);
    }

    @Override
    public DatabaseSpecialDto getDotById(String id) {
        DatabaseSpecialEntity databaseSpecialEntity = this.getById(id);
        DatabaseSpecialDto databaseSpecialDto = this.databaseSpecialMapper.toDto(databaseSpecialEntity);
        //为数据库中午名称赋值
        if (databaseSpecialDto != null) {
            String[] databaseArray = databaseSpecialDto.getDatabaseId().split(",");
            //查询数据库信息列表
            List<DatabaseDefineEntity> databaseDefineEntityList = databaseDefineDao.findAll();
            String databaseName = "";
            for (String databaseId : databaseArray) {
                for (DatabaseDefineEntity databaseDefineEntity : databaseDefineEntityList) {
                    //根据数据库ID判断数据库名称
                    if (databaseId.equals(databaseDefineEntity.getId())) {
                        databaseName += databaseDefineEntity.getDatabaseName() + ",";
                    }
                }
            }
            if (databaseName.length() > 0) {
                databaseName = databaseName.substring(0, databaseName.length() - 1);
            }
            databaseSpecialDto.setDatabaseName(databaseName);

            //调接口查申请人详情
            UserEntity userEntity = userDao.findByUserName(databaseSpecialDto.getUserId());
            if (userEntity != null) {
                databaseSpecialDto.setUserName(userEntity.getWebUsername());
                databaseSpecialDto.setUserPhone(userEntity.getTutorPhone());
                databaseSpecialDto.setDepartment(userEntity.getDeptName());
            }
        }
        return databaseSpecialDto;
    }

    @Override
    public DatabaseSpecialDto saveDto(DatabaseSpecialDto databaseSpecialDto) {
        DatabaseSpecialEntity databaseSpecialEntity = this.databaseSpecialMapper.toEntity(databaseSpecialDto);
        this.saveNotNull(databaseSpecialEntity);
        return this.databaseSpecialMapper.toDto(databaseSpecialEntity);
    }

    @Override
    @Transactional
    public DatabaseSpecialDto addOrUpdate(Map<String, String> map, String filePath) {

        String tdb_name = map.get("TDB_NAME");
        String tdb_id = map.get("TDB_ID");
        String user_id = map.get("USER_ID");
        String uses = map.get("USES");
        String tdb_img = map.get("TDB_IMG");
        String database_id = map.get("DATABASE_ID");
        String database_schema_id = map.get("DATABASE_SCHEMA_ID");
        String data = map.get("data");
        String databaseSpecialReadWriteList = map.get("databaseSpecialReadWriteList");

        DatabaseSpecialEntity databaseSpecialEntity = new DatabaseSpecialEntity();
        if (StringUtils.isNotNullString(tdb_name)) {
            databaseSpecialEntity.setSdbName(tdb_name);
        }
        if (StringUtils.isNotNullString(tdb_id)) {
            databaseSpecialEntity.setId(tdb_id);
        }
        if (StringUtils.isNotNullString(uses)) {
            databaseSpecialEntity.setUses(uses);
        }
        if (StringUtils.isNotNullString(tdb_img)) {
            databaseSpecialEntity.setSdbImg(tdb_img);
        }
        if (StringUtils.isNotNullString(database_id)) {
            databaseSpecialEntity.setDatabaseId(database_id);
        }
        if (StringUtils.isNotNullString(database_schema_id)) {
            databaseSpecialEntity.setDatabaseSchema(database_schema_id);
        }
        if (StringUtils.isNotNullString(filePath)) {
            databaseSpecialEntity.setApplyMaterial(filePath);
        }
        if (StringUtils.isNotEmpty(data)) {
            JSONObject object = JSONObject.parseObject(data);
            String userId = (String) object.get("userId");
            databaseSpecialEntity.setUserId(userId);
        }

        //待审核
        databaseSpecialEntity.setExamineStatus("1");
        databaseSpecialEntity.setUseStatus("2");
        databaseSpecialEntity = this.saveNotNull(databaseSpecialEntity);

        /*if (StringUtils.isNotEmpty(database_id)) {
            String[] split = database_id.split(",");
            for (String databaseId : split) {
                DatabaseSpecialAuthorityEntity dsa = new DatabaseSpecialAuthorityEntity();
                dsa.setDatabaseId(databaseId);
                dsa.setCreateTable(1);
                dsa.setDeleteTable(1);
                dsa.setSdbId(databaseSpecialEntity.getId());
                dsa.setTableDataAccess(1);
                dsa.setCreateTime(new Date());
                this.databaseSpecialAuthorityDao.saveNotNull(dsa);
            }
        }*/


        if (StringUtils.isNotEmpty(databaseSpecialReadWriteList)) {
            JSONArray objects = JSONArray.parseArray(databaseSpecialReadWriteList);
            if (objects != null && objects.size() > 0) {
                for (int i = 0; i < objects.size(); i++) {
                    Map<String, String> mapReadWrite = (Map<String, String>) objects.get(i);
                    DatabaseSpecialReadWriteEntity readWriteEntity = new DatabaseSpecialReadWriteEntity();
                    readWriteEntity.setSdbId(databaseSpecialEntity.getId());
                    readWriteEntity.setDataClassId(mapReadWrite.get("dataClassId"));
                    readWriteEntity.setDatabaseId(mapReadWrite.get("databaseId"));
                    readWriteEntity.setApplyAuthority(Integer.valueOf(mapReadWrite.get("applyAuthority")));
                    //3 待审核
                    readWriteEntity.setExamineStatus(3);
                    //默认分类
                    readWriteEntity.setTypeId("9999");
                    //专题库创建 申请资料默认为引用
                    readWriteEntity.setDataType(2);
                    this.databaseSpecialReadWriteDao.saveNotNull(readWriteEntity);
                }
            }
        }

        return this.databaseSpecialMapper.toDto(databaseSpecialEntity);
    }

    /**
     * 数据库授权
     *
     * @param databaseDto
     */
    @Override
    @Transactional
    public void empowerDatabaseSpecial(DatabaseDto databaseDto) {
//        System.out.println(databaseDto);
        try {
            String userId = databaseDto.getUserId();
            //判断用户是否申请过数据库账户
            List<DatabaseUserEntity> databaseUserEntityList = databaseUserDao.findByUserId(userId);
            if (databaseUserEntityList != null && databaseUserEntityList.size() > 0) {
                //删除历史授权记录，重新添加
                String sdbId = databaseDto.getTdbId();
                databaseSpecialAuthorityDao.deleteBySdbId(sdbId);
                List<DatabaseEntity> databaseEntitys = databaseDao.findByTdbId(sdbId);
                List<String> databaseIds = databaseEntitys.stream().map(d -> d.getDatabaseDefine().getId()).collect(Collectors.toList());
                //需要授权的数据库列表
                List<DatabaseSpecialAuthorityDto> databaseSpecialAuthorityList = databaseDto.getDatabaseSpecialAuthorityList();
                for (int i = 0; i < databaseSpecialAuthorityList.size(); i++) {
                    DatabaseSpecialAuthorityDto databaseSpecialAuthorityDto = databaseSpecialAuthorityList.get(i);
                    //保存申请列表
                    DatabaseSpecialAuthorityEntity databaseSpecialAuthorityEntity = databaseSpecialAuthorityMapper.toEntity(databaseSpecialAuthorityDto);
                    databaseSpecialAuthorityDao.save(databaseSpecialAuthorityEntity);
                    //需要处理的数据库ID
                    String databaseId = databaseSpecialAuthorityDto.getDatabaseId();
                    DatabaseDefineEntity databaseDefineEntity = databaseDefineDao.findById(databaseId).get();
                    if (databaseDefineEntity != null && !databaseIds.contains(databaseId)) {
                        DatabaseEntity databaseEntity = new DatabaseEntity();
                        databaseEntity.setDatabaseClassify("专题库");
                        databaseEntity.setDatabaseName(databaseDto.getDatabaseName());
                        databaseEntity.setSchemaName(databaseDto.getSchemaName());
                        databaseEntity.setStopUse(false);
                        databaseEntity.setTdbId(databaseDto.getTdbId());
                        databaseEntity.setDatabaseDefine(databaseDefineEntity);
                        databaseDao.save(databaseEntity);
                    }
                }
                //授权
                for (int i = 0; i < databaseSpecialAuthorityList.size(); i++) {
                    DatabaseSpecialAuthorityDto databaseSpecialAuthorityDto = databaseSpecialAuthorityList.get(i);
                    String databaseId = databaseSpecialAuthorityDto.getDatabaseId();
                    DatabaseDefineEntity databaseDefineEntity = databaseDefineDao.findById(databaseId).get();
                    DatabaseDefineDto databaseDefineDto = this.databaseDefineMapper.toDto(databaseDefineEntity);
                    DatabaseDcl databaseVO = DatabaseUtil.getDatabaseDefine(databaseDefineDto, databaseInfo);
                    try {
                        //申请创建模式
                        Set<DatabaseAdministratorEntity> databaseAdministratorSet = databaseDefineEntity.getDatabaseAdministratorList();
                        //访问路径、账号、密码
                        String url = databaseDefineEntity.getDatabaseUrl();
                        if (databaseAdministratorSet != null) {
                            //获取任意登录账号
                            String schemaName = databaseDto.getSchemaName();
                            DatabaseUserEntity databaseUserEntity = databaseUserEntityList.get(0);
                            String databaseUpId = databaseUserEntity.getDatabaseUpId();

                            //表数据增删改查权限
                            boolean dataAuthor = databaseSpecialAuthorityDto.getTableDataAccess() == 2;
                            //创建表权限
                            boolean creatAuthor = databaseSpecialAuthorityDto.getCreateTable() == 2;
                            //删除表权限
                            boolean dropAuthor = databaseSpecialAuthorityDto.getDeleteTable() == 2;

                            try {
                                databaseVO.createSchemas(schemaName, databaseUpId, null, dataAuthor, creatAuthor, dropAuthor, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                                logger.error("[E4006] 同名用户或模式已存在 ");
                            }
                            if (databaseVO != null) {
                                databaseVO.closeConnect();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (databaseVO != null) {
                            databaseVO.closeConnect();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public PageBean selectPageList(PageForm<DatabaseSpecialDto> pageForm) {
        DatabaseSpecialEntity databaseSpecialEntity = databaseSpecialMapper.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotBlank(databaseSpecialEntity.getExamineStatus())) {
            specificationBuilder.add("examineStatus", SpecificationOperator.Operator.eq.name(), databaseSpecialEntity.getExamineStatus());
        }
        if (StringUtils.isNotBlank(databaseSpecialEntity.getSdbName())) {
            specificationBuilder.add("sdbName", SpecificationOperator.Operator.likeAll.name(), databaseSpecialEntity.getSdbName());
        }
        if (StringUtils.isNotBlank(databaseSpecialEntity.getUserId())) {
            specificationBuilder.add("userId", SpecificationOperator.Operator.likeAll.name(), databaseSpecialEntity.getUserId());
        }
        PageBean pageBean = this.getPage(specificationBuilder.generateSpecification(), pageForm, null);
        List<DatabaseSpecialEntity> databaseSpecialList = (List<DatabaseSpecialEntity>) pageBean.getPageData();
        pageBean.setPageData(databaseSpecialMapper.toDto(databaseSpecialList));
        return pageBean;
    }

    @Override
    @Transactional
    public void empowerDataOne(DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto) {
        try {
            //更新权限
            DatabaseSpecialReadWriteEntity databaseSpecialReadWriteEntity = databaseSpecialReadWriteMapper
                    .toEntity(databaseSpecialReadWriteDto);
            mybatisModifyMapper.modifyDatabaseSpecialReadWrite(databaseSpecialReadWriteEntity);

            if (databaseSpecialReadWriteDto.getDatabaseId() != "RADB") {
                String userId = databaseSpecialReadWriteDto.getUserId();
                String databaseId = databaseSpecialReadWriteDto.getDatabaseId();
                String dataClassId = databaseSpecialReadWriteDto.getDataClassId();
                Integer applyAuthority = databaseSpecialReadWriteDto.getApplyAuthority();
                Map<String, Object> paramMap = new HashMap<>();

                if (databaseSpecialReadWriteDto.getExamineStatus() == 1) {
                    //授权
                    empowerAuthority(userId, databaseId, dataClassId, applyAuthority);
                } else if (databaseSpecialReadWriteDto.getExamineStatus() == 2) {
                    //撤销授权
                    cancelAuthority(userId, databaseId, dataClassId, 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void empowerDataBatch(List<DatabaseSpecialReadWriteDto> databaseSpecialReadWriteDtoList) {
        try {
            for (DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto : databaseSpecialReadWriteDtoList) {
                String sdbId = databaseSpecialReadWriteDto.getSdbId();//专题库ID
                String dataClassId = databaseSpecialReadWriteDto.getDataClassId();//存储编码
                String databaseId = databaseSpecialReadWriteDto.getDatabaseId();//数据库ID
                Integer applyAuthority = databaseSpecialReadWriteDto.getApplyAuthority();//申请权限
                Integer examineStatus = databaseSpecialReadWriteDto.getExamineStatus();//授权状态
                String userId = databaseSpecialReadWriteDto.getUserId();//用户ID
                String failureReason = databaseSpecialReadWriteDto.getFailureReason();
                if (examineStatus == 1) {
                    failureReason = "-";
                }
                List<DataTableEntity> dataTableList = dataTableDao.findByDataServiceIdAndClassLogicId(dataClassId, databaseId);

                if (!"RADB".equals(databaseId)) {
                    if (databaseSpecialReadWriteDto.getExamineStatus() == 1) {//授权
                        empowerAuthority(userId, databaseId, dataClassId, applyAuthority);
                    } else if (databaseSpecialReadWriteDto.getExamineStatus() == 2) {//撤销权限
                        cancelAuthority(userId, databaseId, dataClassId, 1);
                    }
                }

                //更新授权状态
                List<DatabaseSpecialReadWriteEntity> dataList = databaseSpecialReadWriteDao.findBySdbIdAndDataClassId(sdbId, dataClassId);
                System.out.println(dataList.size());
                if (dataList != null && dataList.size() > 0) {
                    for (DatabaseSpecialReadWriteEntity databaseSpecialReadWriteEntity : dataList) {
                        databaseSpecialReadWriteEntity.setExamineStatus(examineStatus);
                        databaseSpecialReadWriteEntity.setFailureReason(failureReason);
                        mybatisModifyMapper.modifyDatabaseSpecialReadWrite(databaseSpecialReadWriteEntity);
                    }
                }
//                mybatisModifyMapper.modifyDatabaseSpecialReadWrite(databaseSpecialReadWriteMapper.toEntity(databaseSpecialReadWriteDto));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> getDataTreeBySdbId(String sdbId) {

        Map<String, Object> resultMap = new HashMap<String, Object>();

        List<Ztree> data = new ArrayList<Ztree>();

        // 添加默认分类
        Ztree mainTree = new Ztree("-1", "-2", "资料分类树", true, 0);
        mainTree.setOpen(true);
        Ztree autoTree = new Ztree("9999", "-1", "未归类资料", false);
        Ztree readyTree = new Ztree("0", "-1", "已归类资料", true);
        data.add(mainTree);
        data.add(autoTree);
        data.add(readyTree);

        // 根据专题库ID获取对应树信息。
        int maxTypeId = 1;
        List<DatabaseSpecialTreeEntity> databaseSpecialTreeEntities = databaseSpecialTreeDao.findBySdbId(sdbId);
        if (databaseSpecialTreeEntities != null && databaseSpecialTreeEntities.size() > 0) {
            for (DatabaseSpecialTreeEntity databaseSpecialTree : databaseSpecialTreeEntities) {

                //获取最大类型id
                if (Integer.parseInt(databaseSpecialTree.getTypeId()) > maxTypeId) {
                    maxTypeId = Integer.parseInt(databaseSpecialTree.getTypeId());
                }

                Ztree tree = new Ztree();
                tree.setId(databaseSpecialTree.getTypeId());
                tree.setpId(databaseSpecialTree.getParentId());
                tree.setName(databaseSpecialTree.getTypeName());
                tree.setIsParent(true);
                data.add(tree);
            }
        }

        List<Map<String, Object>> readWriteBySdbId = mybatisQueryMapper.querySpecialReadWriteBySdbId(sdbId);
        if (readWriteBySdbId != null && readWriteBySdbId.size() > 0) {
            for (Map<String, Object> datum : readWriteBySdbId) {
                Ztree tree = new Ztree();
                tree.setId(datum.get("ID").toString());
                tree.setName(datum.get("NAME").toString());
                tree.setpId(datum.get("PID").toString());
                tree.setIsParent(false);
                data.add(tree);
            }
        }
        resultMap.put("maxTypeId", maxTypeId);
        resultMap.put("data", data);
        return resultMap;
    }

    @Override
    public Map<String, Object> getTreeBySdbId(String sdbId) {

        Map<String, Object> resultMap = new HashMap<String, Object>();

        List<Ztree> data = new ArrayList<Ztree>();

        // 添加默认分类
        Ztree mainTree = new Ztree("-1", "-2", "资料分类树", true, 0);
        mainTree.setOpen(true);
        Ztree autoTree = new Ztree("9999", "-1", "未归类资料", false);
        Ztree readyTree = new Ztree("0", "-1", "已归类资料", true);
        data.add(mainTree);
        data.add(autoTree);
        data.add(readyTree);

        // 根据专题库ID获取对应树信息。
        int maxTypeId = 1;
        List<DatabaseSpecialTreeEntity> databaseSpecialTreeEntities = databaseSpecialTreeDao.findBySdbId(sdbId);
        if (databaseSpecialTreeEntities != null && databaseSpecialTreeEntities.size() > 0) {
            for (DatabaseSpecialTreeEntity databaseSpecialTree : databaseSpecialTreeEntities) {

                //获取最大类型id
                if (Integer.parseInt(databaseSpecialTree.getTypeId()) > maxTypeId) {
                    maxTypeId = Integer.parseInt(databaseSpecialTree.getTypeId());
                }

                Ztree tree = new Ztree();
                tree.setId(databaseSpecialTree.getTypeId());
                tree.setpId(databaseSpecialTree.getParentId());
                tree.setName(databaseSpecialTree.getTypeName());
                tree.setIsParent(true);
                data.add(tree);
            }
        }
        resultMap.put("maxTypeId", maxTypeId);
        resultMap.put("data", data);
        return resultMap;
    }

    @Override
    public List<DatabaseSpecialDto> getByExamineStatus(String examineStatus) {
        List<DatabaseSpecialEntity> databaseSpecialEntities = databaseSpecialDao.findByExamineStatusOrderBySortNoAscCreateTimeDesc(examineStatus);
        return this.databaseSpecialMapper.toDto(databaseSpecialEntities);
    }

    @Override
    public DatabaseSpecialDto updateUseStatusById(String sdbId, String useStatus) {
        DatabaseSpecialDto databaseSpecialDto = this.getDotById(sdbId);
        databaseSpecialDto.setUseStatus(useStatus);
        DatabaseSpecialEntity databaseSpecialEntity = databaseSpecialMapper.toEntity(databaseSpecialDto);
        databaseSpecialEntity = this.saveNotNull(databaseSpecialEntity);
        return databaseSpecialMapper.toDto(databaseSpecialEntity);
    }

    @Override
    public List<Map<String, Object>> getAllOtherRecordByUserId(String userId, String useStatus) {
        List<Map<String, Object>> allOtherRecordByUserId = mybatisQueryMapper.getAllOtherRecordByUserId(userId, useStatus);
        return allOtherRecordByUserId;
    }

    @Override
    public List<Map<String, Object>> getByUserIdAndUseStatus(String userId, String useStatus) {
        List<Map<String, Object>> mapList = mybatisQueryMapper.querySpecialByUserIdAndUseStatus(userId, useStatus);
        return mapList;
    }

    @Override
    public List<DatabaseSpecialDto> getByUseStatus(String useStatus) {
        List<DatabaseSpecialEntity> databaseSpecialEntities = databaseSpecialDao.findByUseStatus(useStatus);
        List<DatabaseSpecialDto> databaseSpecialDtos = databaseSpecialMapper.toDto(databaseSpecialEntities);
        //调用接口获取所有的用户信息
        List<UserEntity> userEntities = userDao.findByUserType("11");
        if (databaseSpecialDtos != null && databaseSpecialDtos.size() > 0) {
            for (DatabaseSpecialDto databaseSpecialDto : databaseSpecialDtos) {
                //遍历所有用户信息找到每条记录对应的用户信息
                for (UserEntity userEntity : userEntities) {
                    if (userEntity.getUserName().equals(databaseSpecialDto.getUserId())) {
                        databaseSpecialDto.setUserName(userEntity.getWebUsername());
                        databaseSpecialDto.setUserPhone(userEntity.getPhonenumber());
                        databaseSpecialDto.setDepartment(userEntity.getDeptName());
                    }
                }
            }
        }

        return databaseSpecialDtos;
    }


    /**
     * 授权
     */
    private void empowerAuthority(String userId, String databaseId, String dataClassId, Integer applyAuthority) {
        DatabaseDcl databaseVO = null;
        try {
            //判断用户是否申请过数据库账户
            List<DatabaseUserEntity> databaseUserEntityList = databaseUserDao.findByUserId(userId);
            if (databaseUserEntityList != null && databaseUserEntityList.size() > 0) {
                DatabaseUserEntity databaseUserEntity = databaseUserEntityList.get(0);
                //up账户IP
                String databaseUPIp = null;
                if (StringUtils.isNotBlank(databaseUserEntity.getDatabaseUpIp())) {
                    databaseUPIp = databaseUserEntity.getDatabaseUpIp();
                } else if (StringUtils.isNotBlank(databaseUserEntity.getDatabaseUpIpSegment())) {
                    databaseUPIp = databaseUserEntity.getDatabaseUpIpSegment();
                }
                //获取用户可用的物理库ID
                String[] databaseIdArray = databaseUserEntity.getDatabaseUpId().split(",");
                //获取申请ID对应的物理库
                DatabaseEntity databaseEntity = databaseDao.findById(databaseId).get();
                String databaseDefineId = databaseEntity.getDatabaseDefine().getId();
                String[] xuguDatabaseArray = {"HADB"};
                String[] gbaseDatabaseArray = {"STDB", "BFDB", "FIDB"};
                //判断待授权物理库是否是用户可用
                if (Arrays.asList(databaseIdArray).contains(databaseDefineId) &&
                        (Arrays.asList(xuguDatabaseArray).contains(databaseDefineId)
                                || Arrays.asList(gbaseDatabaseArray).contains(databaseDefineId))) {
                    DatabaseDefineEntity databaseDefineEntity = databaseEntity.getDatabaseDefine();
                    Set<DatabaseAdministratorEntity> databaseAdministratorSet = databaseDefineEntity.getDatabaseAdministratorList();
                    //访问路径、账号、密码
                    String url = databaseDefineEntity.getDatabaseUrl();
                    if (databaseAdministratorSet != null) {
                        //获取任意登录账号
                        DatabaseAdministratorEntity databaseAdministratorEntity = databaseAdministratorSet.iterator().next();
                        String username = databaseAdministratorEntity.getUserName();
                        String password = databaseAdministratorEntity.getPassWord();

                        //虚谷
                        if (Arrays.asList(xuguDatabaseArray).contains(databaseDefineId)) {
                            databaseVO = new Xugu(url, username, password);
                        } else if (Arrays.asList(gbaseDatabaseArray).contains(databaseDefineId)) {//南大
                            databaseVO = new Gbase8a(url, username, password);
                        }
                        List<DataTableEntity> dataTableList = dataTableDao.findByDataServiceIdAndClassLogicId(dataClassId, databaseId);
                        for (DataTableEntity dataTableEntity : dataTableList) {
                            String tableName = dataTableEntity.getTableName();
                            //默认读权限
                            boolean select = true;
                            if (applyAuthority == 2) {//读写权限
                                select = false;
                            }
                            databaseVO.addPermissions(select, databaseEntity.getSchemaName(),
                                    tableName, databaseUserEntity.getDatabaseUpId(), null, null);
                        }
                        databaseVO.closeConnect();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (databaseVO != null) {
                databaseVO.closeConnect();
            }
        }
    }

    /**
     * 撤销权限
     */
    public void cancelAuthority(String userId, String databaseId, String dataClassId, Integer mark) {
        DatabaseDcl databaseVO = null;
        try {
            //判断用户是否申请过数据库账户
            List<DatabaseUserEntity> databaseUserEntityList = databaseUserDao.findByUserId(userId);
            if (databaseUserEntityList != null && databaseUserEntityList.size() > 0) {
                DatabaseUserEntity databaseUserEntity = databaseUserEntityList.get(0);
                //up账户IP
                String databaseUPIp = null;
                if (StringUtils.isNotBlank(databaseUserEntity.getDatabaseUpIp())) {
                    databaseUPIp = databaseUserEntity.getDatabaseUpIp();
                } else if (StringUtils.isNotBlank(databaseUserEntity.getDatabaseUpIpSegment())) {
                    databaseUPIp = databaseUserEntity.getDatabaseUpIpSegment();
                }
                //获取用户可用的物理库ID
                String[] databaseIdArray = databaseUserEntity.getDatabaseUpId().split(",");
                //获取申请ID对应的物理库
                DatabaseEntity databaseEntity = databaseDao.findById(databaseId).get();
                String databaseDefineId = databaseEntity.getDatabaseDefine().getId();
                String[] xuguDatabaseArray = {"HADB"};
                String[] gbaseDatabaseArray = {"STDB", "BFDB", "FIDB"};
                //判断待授权物理库是否是用户可用
                if (Arrays.asList(databaseIdArray).contains(databaseDefineId) &&
                        (Arrays.asList(xuguDatabaseArray).contains(databaseDefineId)
                                || Arrays.asList(gbaseDatabaseArray).contains(databaseDefineId))) {
                    DatabaseDefineEntity databaseDefineEntity = databaseEntity.getDatabaseDefine();
                    Set<DatabaseAdministratorEntity> databaseAdministratorSet = databaseDefineEntity.getDatabaseAdministratorList();
                    //访问路径、账号、密码
                    String url = databaseDefineEntity.getDatabaseUrl();
                    if (databaseAdministratorSet != null) {
                        //获取任意登录账号
                        DatabaseAdministratorEntity databaseAdministratorEntity = databaseAdministratorSet.iterator().next();
                        String username = databaseAdministratorEntity.getUserName();
                        String password = databaseAdministratorEntity.getPassWord();

                        //虚谷
                        if (Arrays.asList(xuguDatabaseArray).contains(databaseDefineId)) {
                            databaseVO = new Xugu(url, username, password);
                        } else if (Arrays.asList(gbaseDatabaseArray).contains(databaseDefineId)) {//南大
                            databaseVO = new Gbase8a(url, username, password);
                        }
                        List<DataTableEntity> dataTableList = dataTableDao.findByDataServiceIdAndClassLogicId(dataClassId, databaseId);
                        String[] permissions = {"SELECT", "UPDATE", "INSERT", "DELETE"};
                        for (DataTableEntity dataTableEntity : dataTableList) {
                            String tableName = dataTableEntity.getTableName();
                            databaseVO.deletePermissions(permissions, databaseEntity.getSchemaName(),
                                    tableName, databaseUserEntity.getDatabaseUpId(), null, null);
                        }
                        databaseVO.closeConnect();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (databaseVO != null) {
                databaseVO.closeConnect();
            }
        }
    }

    @Override
    @Transactional
    public DatabaseSpecialDto saveMultilRecord(DatabaseSpecialDto databaseSpecialDto) {
        //读申请是否自动授权
        boolean readAuthorityFlag = false;
        List<ReadAuthorityEntity> readAuthorityEntities = readAuthorityDao.findAll();
        if (readAuthorityEntities != null && readAuthorityEntities.size() > 0) {
            if ("1".equals(readAuthorityEntities.get(0).getValue())) {
                readAuthorityFlag = true;
            }
        }

        List<DatabaseSpecialReadWriteDto> databaseSpecialReadWriteList = databaseSpecialDto.getDatabaseSpecialReadWriteList();
        if (databaseSpecialReadWriteList != null && databaseSpecialReadWriteList.size() > 0) {
            for (DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto : databaseSpecialReadWriteList) {
                // 如果是读权限且允许默认通过，通过；否则，待审核
                if (databaseSpecialReadWriteDto.getApplyAuthority() == 1 && readAuthorityFlag) {
                    databaseSpecialReadWriteDto.setExamineStatus(1);
                } else {
                    databaseSpecialReadWriteDto.setExamineStatus(3);//待授权
                }

                // 默认分类
                databaseSpecialReadWriteDto.setTypeId("9999");
                //申请资料（非自建）
                databaseSpecialReadWriteDto.setDataType(2);
                //保存
                databaseSpecialReadWriteDao.save(databaseSpecialReadWriteMapper.toEntity(databaseSpecialReadWriteDto));

                //到实际物理库授权
                if (databaseSpecialReadWriteDto.getExamineStatus() == 1) {
                    this.empowerDataOne(databaseSpecialReadWriteDto);
                }
            }
        }
        return databaseSpecialDto;
    }

    @Override
    public DatabaseSpecialAccessDto getSpecialAccess(String tdbId, String userId) {
        DatabaseSpecialAccessEntity special = databaseSpecialAccessDao.findBySdbIdAndUserId(tdbId, userId);
        return databaseSpecialAccessMapper.toDto(special);
    }

    @Override
    public DatabaseSpecialAccessDto specialAccessApply(DatabaseSpecialAccessDto databaseSpecialAccessDto) {
        //设置访问权限为完整访问权限
        databaseSpecialAccessDto.setAccessAuthority(2);
        //待审核
        databaseSpecialAccessDto.setExamineStatus("1");
        databaseSpecialAccessDto.setUseStatus("1");
        DatabaseSpecialAccessEntity databaseSpecialAccessEntity = databaseSpecialAccessMapper.toEntity(databaseSpecialAccessDto);
        databaseSpecialAccessEntity = databaseSpecialAccessDao.saveNotNull(databaseSpecialAccessEntity);
        return databaseSpecialAccessMapper.toDto(databaseSpecialAccessEntity);
    }

    @Override
    @Transactional
    public DatabaseSpecialAccessDto specialAccessAutho(DatabaseSpecialAccessDto databaseSpecialAccessDto) {
        DatabaseSpecialAccessEntity databaseSpecialAccessEntity = databaseSpecialAccessMapper.toEntity(databaseSpecialAccessDto);
        //审核通过，设置使用状态为2使用中
        if ("2".equals(databaseSpecialAccessEntity.getExamineStatus())) {
            databaseSpecialAccessEntity.setUseStatus("2");
        }
        databaseSpecialAccessEntity.setExamineTime(new Date());
        //更新申请记录
        mybatisModifyMapper.updateSpecialAccess(databaseSpecialAccessEntity);

        //给专题库下的资料授权
        if ("2".equals(databaseSpecialAccessEntity.getExamineStatus())) {
            List<DatabaseSpecialReadWriteEntity> databaseSpecialReadWriteEntities = this.databaseSpecialReadWriteDao.findBySdbId(databaseSpecialAccessDto.getSdbId());
            if (databaseSpecialReadWriteEntities != null && databaseSpecialReadWriteEntities.size() > 0) {
                for (int i = 0; i < databaseSpecialReadWriteEntities.size(); i++) {
                    DatabaseSpecialReadWriteEntity databaseSpecialReadWriteEntity = databaseSpecialReadWriteEntities.get(i);
                    empowerAuthority(databaseSpecialAccessEntity.getUserId(), databaseSpecialReadWriteEntity.getDatabaseId(),
                            databaseSpecialReadWriteEntity.getDataClassId(), databaseSpecialReadWriteEntity.getApplyAuthority());
                }
            }
        }
        return databaseSpecialAccessDto;
    }

    @Override
    @Transactional
    public Map<String, Object> updateBySql(String tdbId, String userId, String cause, String examineStatus) {
        DatabaseSpecialDto specialdb = new DatabaseSpecialDto();
        Map<String, Object> map = new HashMap<>();
		/*String examineStatus = request.getParameter("examineStatus");
		String cause = request.getParameter("cause");
		String tdbId = request.getParameter("tdbId");
		String userId = request.getParameter("userId");*/
        Date now = new Date();
        specialdb.setExamineTime(now);
        specialdb.setExaminer("0");
        specialdb.setFailureReason(cause);
        specialdb.setExamineStatus(examineStatus);
        specialdb.setId(tdbId);
        specialdb.setUserId(userId);
        if ("1".equals(examineStatus) || "3".equals(examineStatus)) {
            specialdb.setUseStatus("1");
        } else if ("2".equals(examineStatus)) {
            specialdb.setUseStatus("2");
        }//else if(examineStatus.equals("2")&&)
        databaseSpecialDao.save(databaseSpecialMapper.toEntity(specialdb));
        /**
         * 给资料授权
         */
        try {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            Map<String, Object> parammap = new HashMap<String, Object>();
            //根据专题库ID获取到专题库资料列表
            getAllRecordByTdbId(tdbId, dataMap);
            if ("0".equals(dataMap.get("returnCode").toString())) {
                JSONArray data = (JSONArray) dataMap.get("data");
                if (data != null && data.size() > 0) {
                    //遍历单条授权
                    for (int i = 0; i < data.size(); i++) {
                        JSONObject obj = (JSONObject) data.get(i);
                        String data_class_id = obj.getString("DATA_CLASS_ID");//存储编码
                        int apply_authority = Integer.parseInt(obj.getString("APPLY_AUTHORITY"));//授权权限
                        String logic_id = obj.getString("LOGIC_ID");//物理库id
                        empowerAuthority(userId, logic_id, data_class_id, apply_authority);
                    }
                }
                map.put("returnCode", "0");
                map.put("returnMessage", "操作成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("returnCode", "1");
            map.put("returnMessage", "操作失败 : " + e.getMessage());
        }
        return map;
    }

    @Override
    public Map<String, Object> getRecordByTdbId(String tdbId, String typeId, String cause) {
        Map<String, Object> map = new HashMap<>();

        //下面定义一个JSONObject对象，用来存储对应专题库信息。
        JSONObject createApplyData = new JSONObject();
        //下面定义JSONArray，来存储数据表信息。

        //下面根据专题库ID号获取对应专题库信息。
        DatabaseSpecialDto oneRecord = this.databaseSpecialMapper.toDto(getById(tdbId));
        if (oneRecord != null) {
            //下面取得专题库ID号。
            createApplyData.put("TDB_ID", oneRecord.getId());
            //下面取得专题库名称。
            createApplyData.put("TDB_NAME", oneRecord.getSdbName());
            //下面取得专题库图标路径。
            createApplyData.put("TDB_IMG", oneRecord.getSdbImg());
            //下面取得申请用户ID。
            createApplyData.put("USER_ID", oneRecord.getUserId());
            //下面取得申请时间。
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date applyTime = oneRecord.getCreateTime();
            String strApplyTime = sdf.format(applyTime);
            createApplyData.put("APPLY_TIME", strApplyTime);
            //下面取得用途。
            createApplyData.put("USES", oneRecord.getUses());
            //下面取得申请材料路径。
            createApplyData.put("APPLY_FILE_PATH", oneRecord.getApplyMaterial());
            //下面取得审核人。
            createApplyData.put("EXAMINER", oneRecord.getExaminer());
            //下面取得审核状态。
            createApplyData.put("EXAMINE_STATUS", oneRecord.getExamineStatus());
            //下面取得审核时间。
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date examineTime = oneRecord.getExamineTime();
            String strexamineTime = sdf1.format(examineTime);
            createApplyData.put("EXAMINE_TIME", strexamineTime);
            //下面取得拒绝原因。
            createApplyData.put("CAUSE", oneRecord.getFailureReason());
            //下面取得专题库使用状态。
            createApplyData.put("USE_STATUS", oneRecord.getUseStatus());
            //下面取得物理库id。
            createApplyData.put("DATABASE_ID", oneRecord.getDatabaseId());
            createApplyData.put("SCHEMA_ID", oneRecord.getDatabaseSchema());

            //下面根据专题库ID号获取对应授权允许的数据表信息。
            List<Map<String, Object>> dataList = this.mybatisQueryMapper.getRecordByTdbId(tdbId, typeId, cause);
            map.put("data", dataList);
        }

        //下面给result赋值。
        map.put("specialDb", createApplyData);
        return map;
    }

    @Override
    public Map<String, Object> getOneRecordByTdbId(String tdbId, String typeId, String status) {
        Map<String, Object> map = new HashMap<>();

        //下面定义一个JSONObject对象，用来存储对应专题库信息。
        JSONObject createApplyData = new JSONObject();
        //下面根据专题库ID号获取对应专题库信息。
        DatabaseSpecialDto oneRecord = this.databaseSpecialMapper.toDto(getById(tdbId));
        if (oneRecord != null) {
            //下面取得专题库ID号。
            createApplyData.put("TDB_ID", oneRecord.getId());
            //下面取得专题库名称。
            createApplyData.put("TDB_NAME", oneRecord.getSdbName());
            //下面取得专题库图标路径。
            createApplyData.put("TDB_IMG", oneRecord.getSdbImg());
            //下面取得申请用户ID。
            createApplyData.put("USER_ID", oneRecord.getUserId());
            //下面取得申请时间。
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date applyTime = oneRecord.getCreateTime();
            String strApplyTime = sdf.format(applyTime);
            createApplyData.put("APPLY_TIME", strApplyTime);
            //下面取得用途。
            createApplyData.put("USES", oneRecord.getUses());
            //下面取得申请材料路径。
            createApplyData.put("APPLY_FILE_PATH", oneRecord.getApplyMaterial());
            //下面取得审核人。
            createApplyData.put("EXAMINER", oneRecord.getExaminer());
            //下面取得审核状态。
            createApplyData.put("EXAMINE_STATUS", oneRecord.getExamineStatus());
            //下面取得审核时间。
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date examineTime = oneRecord.getExamineTime();
            String strexamineTime = sdf1.format(examineTime);
            createApplyData.put("EXAMINE_TIME", strexamineTime);
            //下面取得拒绝原因。
            createApplyData.put("CAUSE", oneRecord.getFailureReason());
            //下面取得专题库使用状态。
            createApplyData.put("USE_STATUS", oneRecord.getUseStatus());

            //下面根据专题库ID号获取对应授权允许的数据表信息。
            List<Map<String, Object>> dataList = mybatisQueryMapper.getAuthorizeRecordByTdbId(tdbId, typeId, status);
            map.put("dataList", dataList);
        }

        //下面给result赋值。
        map.put("specialDb", createApplyData);
        return map;
    }

    @Override
    public Map<String, Object> getAllSpecial(String userId) {
        Map<String, Object> map = new HashMap<>();
        /*try {
            //下面根据传入的机构ID取得对应机构下的所有专题库信息。
            List<DatabaseSpecialEntity> dataList=this.findBy;
            //获取当前用户申请过的专题库
            List<DatabaseSpecialAccessEntity> applyList = mybatisQueryMapper.getAllRecordByUserId(userId);

            //对查询结果进行奇葩排序 :用户自建的指定,其他按创建时间排序;2019-6-5 10:11:20 wlg
            //结果
            List<DatabaseSpecialEntity> result = new LinkedList<>();
            //用户自建专题库
            List<DatabaseSpecialEntity> userList = new LinkedList<>();
            //其他专题库
            List<DatabaseSpecialEntity> otherList = new LinkedList<>();


            //更改奇葩逻辑2019-6-4 10:19:03 wlg
            if(dataList!=null&&dataList.size()>0 ){
                for(DatabaseSpecialEntity obj1 : dataList){
                    //根据用户id获取用户信息
                    String userInfo =  new GetAllUserInfo().getUserInfo((String) obj1.getUserId());
                    JSONObject jsonobject = JSON.parseObject(userInfo);
                    if(applyList!=null&&applyList.size()>0){
                        for(DatabaseSpecialAccessEntity obj2 : applyList){
                            if(obj1.getId().equals(obj2.getSdbId())){
                                obj1.setExaminer("1");
                                obj1.setExamineStatus(obj2.getExamineStatus());
                                obj1.setUseStatus(obj2.getUseStatus());
                                obj1.setFailureReason(obj2.getFailureReason());
                                break;
                            }
                        }
                    }

                    //奇葩排序
                    if(obj1.getUserId().equals(userId)){
                        userList.add(obj1);
                    }else{
                        otherList.add(obj1);
                    }
                }
                //奇葩排序完成
                result.addAll(userList);
                result.addAll(otherList);
            }
            map.put("data", result);
            map.put("returnCode", 0);
            map.put("returnMessage", "专题库数据获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("returnCode", 1);
            map.put("returnMessage", "专题库数据获取失败");
        }*/
        return map;
    }

    @Override
    public Map<String, Object> saveOneRecord(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            //下面通过request取得传入的JSONObject对象对应字符串。
            StringBuilder responseStrBuilder = new StringBuilder();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }

            //下面定义DminSpecialDbAccessapply对象。
            DatabaseSpecialAccessEntity oneRecord = new DatabaseSpecialAccessEntity();
            //下面从responseStrBuilder中获取专题库引用申请信息。
            JSONObject oneTableInfo = JSONObject.parseObject(responseStrBuilder.toString());
            //下面遍历JSONObject对象。
            Iterator iterator = oneTableInfo.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = oneTableInfo.getString(key);

                //下面循环读取每个数据。
                if ("TDB_ID".equals(key)) {
                    oneRecord.setSdbId(value);
                } else if ("USER_ID".equals(key)) {
                    oneRecord.setUserId(value);
                } else if ("USES".equals(key)) {
                    oneRecord.setUses(value);
                }
            }
            //下面为oneRecord赋值缺省信息。
            //下面设置访问权限。
            oneRecord.setAccessAuthority(2);//默认设置为2-完整访问权限。
            //下面设置申请时间。
            Date now = new Date();
            oneRecord.setCreateTime(now);
            //下面设置审核状态。
            oneRecord.setExamineStatus("1");
            //下面这只专题库使用状态。
            oneRecord.setUseStatus("1");
            //下面将该条申请信息保存到数据表。
            mybatisQueryMapper.saveOneRecord(oneRecord);

            //下面生成返回信息。
            map.put("returnCode", 0);
            map.put("returnMessage", "保存数据成功");
            streamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            //下面生成返回信息。
            map.put("returnCode", 1);
            map.put("returnMessage", "保存数据失败：" + e.getMessage());
        }
        //下面返回值。
        return map;
    }

    @Override
    public void exportExcel(DatabaseSpecialDto databaseSpecialDto) {
        List<DatabaseSpecialDto> databaseSpecialDtos = this.findByParam(databaseSpecialDto);
        List<DatabaseSpecialEntity> entities = databaseSpecialMapper.toEntity(databaseSpecialDtos);
        List<DatabaseSpecialEntity> newEn = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            DatabaseSpecialEntity databaseSpecialEntity = entities.get(i);
            databaseSpecialEntity.setNum(i + 1);
            List<DatabaseSpecialAuthorityDto> authorityList = this.databaseSpecialAuthorityService.getAuthorityBySdbId(databaseSpecialEntity.getId());
            if (authorityList.size() > 0) {
                String vv = "☑";
                String xx = "☒";
                for (int j = 0; j < authorityList.size(); j++) {
                    DatabaseSpecialEntity newD = new DatabaseSpecialEntity();
                    BeanUtils.copyProperties(databaseSpecialEntity, newD);
                    DatabaseSpecialAuthorityDto databaseSpecialAuthorityDto = authorityList.get(j);
                    String position15 = String.format("%-15s", databaseSpecialAuthorityDto.getDatabaseId());
                    if (2 == databaseSpecialAuthorityDto.getCreateTable()) {
                        position15 += vv + "创建   ";
                    } else {
                        position15 += xx + "创建   ";
                    }
                    if (2 == databaseSpecialAuthorityDto.getDeleteTable()) {
                        position15 += vv + "删除   ";
                    } else {
                        position15 += xx + "删除   ";
                    }
                    if (2 == databaseSpecialAuthorityDto.getTableDataAccess()) {
                        position15 += vv + "读写   ";
                    } else {
                        position15 += xx + "读写   ";
                    }
                    newD.setAuthorizationStatus(position15);
                    newEn.add(newD);
                }
            } else {
                DatabaseSpecialEntity newD = new DatabaseSpecialEntity();
                BeanUtils.copyProperties(databaseSpecialEntity, newD);
                newEn.add(newD);
            }

        }
        ExcelUtil<DatabaseSpecialEntity> util = new ExcelUtil(DatabaseSpecialEntity.class, true);
        util.exportExcel(newEn, "专题库审核");
    }

    @Override
    public List<DatabaseSpecialDto> findByParam(DatabaseSpecialDto databaseSpecialDto) {
        DatabaseSpecialEntity databaseSpecialEntity = databaseSpecialMapper.toEntity(databaseSpecialDto);
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(databaseSpecialEntity.getExamineStatus())) {
            specificationBuilder.add("examineStatus", SpecificationOperator.Operator.eq.name(), databaseSpecialEntity.getExamineStatus());
        }
        if (StringUtils.isNotNullString(databaseSpecialEntity.getSdbName())) {
            specificationBuilder.add("sdbName", SpecificationOperator.Operator.likeAll.name(), databaseSpecialEntity.getSdbName());
        }
        if (StringUtils.isNotNullString(databaseSpecialDto.getUserName())) {
            //调用接口 根据用户名查询用户id
            List<String> userId = new ArrayList<String>();
            userId.add("noUseId");
            List<UserEntity> userEntities = userDao.findByWebUsernameLike("%" + databaseSpecialDto.getUserName() + "%");
            if (userEntities != null && userEntities.size() > 0) {
                for (UserEntity userEntity : userEntities) {
                    userId.add(userEntity.getUserName());
                }
            }
            specificationBuilder.add("userId", SpecificationOperator.Operator.in.name(), userId);
        }
        List<String> userId = new ArrayList<String>();
        userId.add("noUseId");
        List<UserEntity> users = userDao.findByCheckedNot("0");
        if (users != null && users.size() > 0) {
            for (UserEntity userEntity : users) {
                userId.add(userEntity.getUserName());
            }
        }
        specificationBuilder.add("userId", SpecificationOperator.Operator.in.name(), userId);
        List<DatabaseSpecialEntity> databaseSpecialEntities = this.getAll(specificationBuilder.generateSpecification());
        List<DatabaseSpecialDto> databaseSpecialDtos = this.databaseSpecialMapper.toDto(databaseSpecialEntities);
        return databaseSpecialDtos;
    }

    /**
     * 根据专题库id获取所有对应资料
     *
     * @param tdbId
     * @param map
     */
    private void getAllRecordByTdbId(String tdbId, Map<String, Object> map) {
        // 下面定义变量。
        JSONArray data = null;

        try {
            // 下面根据传入的专题库ID取得所有对应资料。
            List<DatabaseSpecialReadWriteEntity> dataList = databaseSpecialReadWriteDao.findBySdbId(tdbId);
            // 下面判断获取的记录数据个数。
            int dataNum = dataList.size();
            if (dataNum >= 1) {
                // 程序执行到这里说明查到了资料。
                // 下面创建JSONArray对象，来存储查出的所有记录数据。
                data = new JSONArray();
                for (int i = 1; i <= dataNum; i = i + 1) {
                    // 下面记录一个专题库信息。
                    JSONObject oneData = new JSONObject();
                    // 下面取得专题库ID号。
                    oneData.put("TDB_ID", dataList.get(i - 1).getSdbId());
                    // 下面取得存储编码。
                    oneData.put("DATA_CLASS_ID", dataList.get(i - 1).getDataClassId());
                    // 下面取得逻辑库ID。
                    oneData.put("LOGIC_ID", dataList.get(i - 1).getDatabaseId());
                    // 下面取得申请权限。
                    oneData.put("APPLY_AUTHORITY", dataList.get(i - 1).getApplyAuthority());
                    // 下面取得授权。
                    oneData.put("AUTHORIZE", dataList.get(i - 1).getEmpowerAuthority());
                    // 下面取得拒绝原因。
                    oneData.put("CAUSE", dataList.get(i - 1).getFailureReason());
                    // 下面取得分类ID。
                    oneData.put("TYPE_ID", dataList.get(i - 1).getTypeId());
                    // 下面取得申请时间。
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date applyTime = dataList.get(i - 1).getCreateTime();
                    String strApplyTime = sdf.format(applyTime);
                    oneData.put("APPLY_TIME", strApplyTime);

                    // 下面将一个专题库信息存入data。
                    data.add(oneData);
                }
            }

            // 下面给result赋值。
            map.put("data", data);
            map.put("returnCode", 0);
            map.put("returnMessage", "获取数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            // 下面给result赋值。
            map.put("returnCode", 1);
            map.put("returnMessage", "获取数据失败：" + e.getMessage());
        }

        // 下面返回值。
        return;
    }

    public static void main(String[] args) {
        List<DatabaseEntity> databaseEntitys = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            DatabaseDefineEntity dd = new DatabaseDefineEntity();
            dd.setId("www"+i);
            DatabaseEntity d = new DatabaseEntity();
                    d.setDatabaseDefine(dd);
            databaseEntitys.add(d);
        }

                List<String> databaseIds = databaseEntitys.stream().map(d -> d.getDatabaseDefine().getId()).collect(Collectors.toList());
        databaseIds.stream().forEach(System.out::println);
    }

}
