package com.piesat.dm.rpc.service.database;

import com.alibaba.fastjson.JSONObject;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.poi.ExcelUtil;
import com.piesat.dm.core.api.DatabaseDcl;
import com.piesat.dm.core.api.impl.Cassandra;
import com.piesat.dm.core.api.impl.Gbase8a;
import com.piesat.dm.core.api.impl.Xugu;
import com.piesat.dm.core.parser.DatabaseInfo;
import com.piesat.dm.dao.database.DatabaseAdministratorDao;
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.dao.database.DatabaseDefineDao;
import com.piesat.dm.dao.database.DatabaseUserDao;
import com.piesat.dm.dao.datatable.DataTableDao;
import com.piesat.dm.entity.database.DatabaseAdministratorEntity;
import com.piesat.dm.entity.database.DatabaseDefineEntity;
import com.piesat.dm.entity.database.DatabaseEntity;
import com.piesat.dm.entity.database.DatabaseUserEntity;
import com.piesat.dm.entity.datatable.DataTableEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.dataapply.DataAuthorityApplyService;
import com.piesat.dm.rpc.api.database.DatabaseDefineService;
import com.piesat.dm.rpc.api.database.DatabaseUserService;
import com.piesat.dm.rpc.api.special.DatabaseSpecialReadWriteService;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityApplyDto;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityRecordDto;
import com.piesat.dm.rpc.dto.database.DatabaseAdministratorDto;
import com.piesat.dm.rpc.dto.database.DatabaseDefineDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.database.DatabaseUserDto;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialReadWriteDto;
import com.piesat.dm.rpc.mapper.database.DatabaseMapper;
import com.piesat.dm.rpc.mapper.database.DatabaseUserMapper;
import com.piesat.dm.rpc.util.DatabaseUtil;
import com.piesat.ucenter.dao.system.UserDao;
import com.piesat.ucenter.entity.system.DictTypeEntity;
import com.piesat.ucenter.entity.system.UserEntity;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据库访问账户管理
 */
@Service
public class DatabaseUserServiceImpl extends BaseService<DatabaseUserEntity> implements DatabaseUserService {
    @Autowired
    private DatabaseUserDao databaseUserDao;
    @Autowired
    private DatabaseUserMapper databaseUserMapper;
    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    private DatabaseDefineService databaseDefineService;
    @Autowired
    private DatabaseInfo databaseInfo;
    @Autowired
    private DatabaseUserService databaseUserService;
    @Autowired
    private DataTableDao dataTableDao;
    @Autowired
    private DatabaseAdministratorDao databaseAdministratorDao;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private DatabaseSpecialReadWriteService databaseSpecialReadWriteService;
    @Autowired
    private DataAuthorityApplyService dataAuthorityApplyService;
    @Autowired
    private DatabaseMapper databaseMapper;
    @GrpcHthtClient
    private UserDao userDao;
    @Autowired
    private DatabaseDefineDao databaseDefineDao;
    @Value("${mng.ip}")
    private String mngIp;
    @Value("${database.sys-users}")
    private String sysUsers;

    @Override
    public BaseDao<DatabaseUserEntity> getBaseDao() {
        return databaseUserDao;
    }

    @Override
    public PageBean selectPageList(PageForm<DatabaseUserDto> pageForm) {
        DatabaseUserEntity databaseUserEntity = databaseUserMapper.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();

        if (StringUtils.isNotBlank(databaseUserEntity.getExamineStatus())) {
            specificationBuilder.add("examineStatus", SpecificationOperator.Operator.eq.name(), databaseUserEntity.getExamineStatus());
        }
        if (StringUtils.isNotBlank(databaseUserEntity.getUserId())) {
            specificationBuilder.add("userId", SpecificationOperator.Operator.eq.name(), databaseUserEntity.getUserId());
        }
        if (StringUtils.isNotBlank(databaseUserEntity.getDatabaseUpId())) {
            specificationBuilder.add("databaseUpId", SpecificationOperator.Operator.likeAll.name(), databaseUserEntity.getDatabaseUpId());
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "examineStatus").and(Sort.by(Sort.Direction.DESC, "createTime"));
        PageBean pageBean = this.getPage(specificationBuilder.generateSpecification(), pageForm, sort);
        List<DatabaseUserEntity> databaseUserEntityList = (List<DatabaseUserEntity>) pageBean.getPageData();
        List<DatabaseUserDto> databaseUserDtoList = databaseUserMapper.toDto(databaseUserEntityList);
        //获取数据库列表，查询展示数据库中文名称
        List<DatabaseDefineEntity> databaseDefineEntities = databaseDefineDao.findAll();
        if (databaseUserDtoList != null && databaseUserDtoList.size() > 0 && databaseDefineEntities != null && databaseDefineEntities.size() > 0) {
            for (DatabaseUserDto dto : databaseUserDtoList) {
                dto.setDatabaseUpPassword("");
                //获取数据库中文名称
                if (StringUtils.isNotEmpty(dto.getExamineDatabaseId())) {
                    String[] examineDatabaseIdArray = dto.getExamineDatabaseId().split(",");
                    String applyDatabaseName = "";
                    for (String examineDatabaseId : examineDatabaseIdArray) {
                        for (DatabaseDefineEntity databaseDefineEntity : databaseDefineEntities) {
                            if (examineDatabaseId.equals(databaseDefineEntity.getId())) {
                                applyDatabaseName += databaseDefineEntity.getDatabaseName() + ",";
                                break;
                            }
                        }
                    }
                    if (applyDatabaseName.length() > 0) {
                        applyDatabaseName = applyDatabaseName.substring(0, applyDatabaseName.length() - 1);
                    }
                    dto.setApplyDatabaseName(applyDatabaseName);
                }

                //获取申请用户信息
                UserEntity userEntity = userDao.findByUserNameAndUserType(dto.getUserId(), "11");
                if (userEntity != null) {
                    dto.setUserName(userEntity.getWebUsername());
                    dto.setDeptName(userEntity.getDeptName());
                    dto.setPhonenumber(userEntity.getPhonenumber());
                }
            }
        }

        pageBean.setPageData(databaseUserDtoList);
        return pageBean;
    }

    @Override
    public DatabaseUserDto mergeDto(DatabaseUserDto databaseUserDto) {
        DatabaseUserEntity databaseUserEntity = this.databaseUserMapper.toEntity(databaseUserDto);
        this.saveNotNull(databaseUserEntity);
        return this.databaseUserMapper.toDto(databaseUserEntity);
    }

    @Override
    public void exportData(String examineStatus) {
        List<DatabaseUserEntity> byExamineStatus = null;
        if (StringUtils.isNotBlank(examineStatus)) {
            byExamineStatus = this.databaseUserDao.findByExamineStatus(examineStatus);
        } else {
            byExamineStatus = this.databaseUserDao.findAll();
        }
        ExcelUtil<DatabaseUserEntity> util = new ExcelUtil(DatabaseUserEntity.class);
        util.exportExcel(byExamineStatus, "数据库访问账户信息");
    }

    @Override
    public List<DatabaseUserDto> all() {
        List<DatabaseUserEntity> all = this.getAll();
        return this.databaseUserMapper.toDto(all);
    }

    @Override
    public DatabaseUserDto getDotById(String id) {
        DatabaseUserEntity databaseUserEntity = this.getById(id);
        DatabaseUserDto databaseUserDto = this.databaseUserMapper.toDto(databaseUserEntity);
        //调接口查申请人详情
        UserEntity userEntity = userDao.findByUserName(databaseUserDto.getUserId());
        if (userEntity != null) {
            databaseUserDto.setUserName(userEntity.getWebUsername());
            databaseUserDto.setPhonenumber(userEntity.getPhonenumber());
            databaseUserDto.setDeptName(userEntity.getDeptName());
        }
        return databaseUserDto;
    }

    @Override
    public void deleteById(String id) {
        DatabaseUserDto dotById1 = this.getDotById(id);
        List<String> sysIdList = Arrays.asList(this.sysUsers.toLowerCase().split(","));
        String upId = dotById1.getDatabaseUpId().toLowerCase();
        if (!sysIdList.contains(upId) && StringUtils.isNotBlank(dotById1.getExamineDatabaseId())) {
            String[] needEmpowerIdArr = dotById1.getExamineDatabaseId().split(",");
            Arrays.stream(needEmpowerIdArr).filter(e -> !StringUtils.isEmpty(e)).forEach(e -> {
                DatabaseDefineDto dotById = this.databaseDefineService.getDotById(e);
                DatabaseDcl databaseVO = null;
                try {
                    databaseVO = DatabaseUtil.getDatabaseDefine(dotById, databaseInfo);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Optional.ofNullable(databaseVO).ifPresent(DatabaseDcl::closeConnect);
                }
                Optional.ofNullable(databaseVO).ifPresent(d -> {
                    try {
                        d.deleteUser(dotById1.getDatabaseUpId());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        d.closeConnect();
                    } finally {
                        d.closeConnect();
                    }
                });
            });
        }
        this.delete(id);
    }

    @Override
    public DatabaseUserDto getDotByUPID(String databaseUPId) {
        DatabaseUserEntity databaseUserEntity = databaseUserDao.findByDatabaseUpId(databaseUPId);
        return this.databaseUserMapper.toDto(databaseUserEntity);
    }

    @Override
    public DatabaseUserDto getDotByUserId(String userId) {
        DatabaseUserEntity databaseUserEntity = databaseUserDao.findByUserIdAndExamineStatusNot(userId, "2");
        return this.databaseUserMapper.toDto(databaseUserEntity);
    }

    @Override
    public DatabaseUserDto findByUserIdAndExamineStatus(String userId, String examineStatus) {
        DatabaseUserEntity databaseUserEntity = databaseUserDao.findByUserIdAndExamineStatus(userId, examineStatus);
        return this.databaseUserMapper.toDto(databaseUserEntity);
    }

    @Override
    public DatabaseUserDto findByUserIdAndDatabaseUpId(String userId, String upId) {
        List<DatabaseUserEntity> byUserId = this.databaseUserDao.findByUserIdAndDatabaseUpId(userId, upId);
        DatabaseUserEntity d = null;
        if (byUserId != null && byUserId.size() > 0) {
            d = byUserId.get(0);
        }
        return this.databaseUserMapper.toDto(d);
    }

    @Override
    public DatabaseUserDto saveDto(DatabaseUserDto databaseUserDto) {
        DatabaseUserEntity databaseUserEntity = this.databaseUserMapper.toEntity(databaseUserDto);
        DatabaseUserEntity databaseUserEntity1 = this.saveNotNull(databaseUserEntity);
        return this.databaseUserMapper.toDto(databaseUserEntity1);
    }

    @Override
    public boolean databaseUserExi(DatabaseUserDto databaseUserDto) {
        String[] needEmpowerIdArr = databaseUserDto.getApplyDatabaseId().split(",");
        return Optional.ofNullable(needEmpowerIdArr).map(id -> Arrays.stream(id).filter(e -> !StringUtils.isEmpty(e)).anyMatch(d -> {
            DatabaseDefineDto dotById = this.databaseDefineService.getDotById(d);
            DatabaseDcl databaseVO = null;
            try {
                databaseVO = DatabaseUtil.getDatabaseDefine(dotById, databaseInfo);
            } catch (Exception e) {
                e.printStackTrace();
                Optional.ofNullable(databaseVO).ifPresent(DatabaseDcl::closeConnect);
            }
            return Optional.ofNullable(databaseVO).map(o -> {
                try {
                    return o.getUserNum(databaseUserDto.getDatabaseUpId());
                } catch (Exception e) {
                    e.printStackTrace();
                    o.closeConnect();
                } finally {
                    o.closeConnect();
                }
                return 1;
            }).equals(0);
        })).orElse(false);
    }

    @Override
    public DatabaseUserDto addOrUpdate(Map<String, String[]> parameterMap, String filePath) {
        Map<String, String> map = new LinkedHashMap<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (entry.getValue().length > 0) {
                map.put(entry.getKey(), entry.getValue()[0]);
            }
        }

        String databaseup_id = map.get("databaseup_id");
        String databaseUP_IP = map.get("databaseUP_IP");
        String id = map.get("id");
        String databaseup_desc = map.get("databaseup_desc");
        String database_ids = map.get("database_ids");
        String databaseup_password = map.get("databaseup_password");
        String data = map.get("data");
        String picurl2 = map.get("picurl2");//修改前的申请材料，是否删除
        String pdfPath = map.get("pdfPath");

        DatabaseUserEntity databaseUserEntity = new DatabaseUserEntity();
        Optional.ofNullable(id).ifPresent(databaseUserEntity::setId);
        Optional.ofNullable(filePath).ifPresent(databaseUserEntity::setApplyMaterial);
        Optional.ofNullable(pdfPath).ifPresent(databaseUserEntity::setPdfPath);
        Optional.ofNullable(data).map(JSONObject::parseObject).map(e -> e.getString("userId")).ifPresent(databaseUserEntity::setUserId);
        Optional.ofNullable(databaseup_password).ifPresent(databaseUserEntity::setDatabaseUpPassword);

        databaseUserEntity.setApplyDatabaseId(database_ids);

        databaseUserEntity.setDatabaseUpDesc(databaseup_desc);
        databaseUserEntity.setDatabaseUpId(databaseup_id);
        databaseUserEntity.setDatabaseUpIp(databaseUP_IP);
        databaseUserEntity.setExamineStatus("0");
        databaseUserEntity = this.saveNotNull(databaseUserEntity);
        return this.databaseUserMapper.toDto(databaseUserEntity);
    }

    public static void main(String[] args) {

    }

    @Override
    public ResultT empower(DatabaseUserDto databaseUserDto) {
        String[] sysIds = this.sysUsers.toLowerCase().split(",");
        List<String> sysIdList = Arrays.asList(sysIds);
        String upId = databaseUserDto.getDatabaseUpId().toLowerCase();
        if (sysIdList.contains(upId)) {
            return ResultT.failed(databaseUserDto.getDatabaseUpId() + "为数据库内部用户，禁止创建！");
        }
        //根据ID获取旧的申请信息
        DatabaseUserEntity oldDatabaseUserEntity = this.getById(databaseUserDto.getId());
        //待授权Id
        String[] needEmpowerIds = databaseUserDto.getApplyDatabaseId().split(",");
        List<String> needEmpowerList = Arrays.stream(needEmpowerIds).collect(Collectors.toList());
        String[] haveEmpowerIdArr = Optional.ofNullable(oldDatabaseUserEntity).map(DatabaseUserEntity::getExamineDatabaseId).map(s -> s.split(",")).orElse(new String[0]);
        List<String> haveEmpowerIdist = new ArrayList<>(Arrays.asList(haveEmpowerIdArr));
        List<String> thisHaveIds = new ArrayList<>(haveEmpowerIdist);

        StringBuilder sbff = new StringBuilder();

        //非首次审核通过，授权的id中去掉以前的id
        if (oldDatabaseUserEntity.getExamineStatus().equals("1")) {
            needEmpowerList.removeAll(haveEmpowerIdist);
        }
        //过滤空字符串
        needEmpowerList = needEmpowerList.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
        /**为申请的IP授权**/
        //待授权IP
        String databaseUpIp = databaseUserDto.getDatabaseUpIp();
        String[] needEmpowerIpArr = Optional.ofNullable(databaseUpIp).map(e -> {
            e += "," + mngIp;
            return e.split(",");
        }).orElse(mngIp.split(","));
        List<DatabaseDefineDto> DatabaseDefineList = needEmpowerList.stream().map(this.databaseDefineService::getDotById).collect(Collectors.toList());
        for (DatabaseDefineDto d : DatabaseDefineList) {
            Set<DatabaseAdministratorDto> databaseAdministratorList = d.getDatabaseAdministratorList();
            boolean b = databaseAdministratorList.stream().anyMatch(DatabaseAdministratorDto::getIsManager);
            if (!b) {
                return ResultT.failed(String.format("%s 数据库管理账户缺失!", d.getDatabaseName()));
            }
        }
        DatabaseDefineList.forEach(d -> {
            DatabaseDcl databaseVO = null;
            String databaseId = d.getId();
            try {
                databaseVO = DatabaseUtil.getDatabaseDefine(d, databaseInfo);
                if (databaseVO != null) {
                    databaseVO.addUser(databaseUserDto.getDatabaseUpId(), databaseUserDto.getDatabaseUpPassword(), needEmpowerIpArr);
                    databaseVO.closeConnect();
                    if (!thisHaveIds.contains(databaseId)) {
                        thisHaveIds.add(databaseId);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                String message = e.getMessage();
                Boolean pass = StringUtils.isNotBlank(message) && (message.contains("已经存在") || message.contains("已存在") || message.contains("already exists"));
                if (pass) {
                    if (!thisHaveIds.contains(databaseId)){
                        thisHaveIds.add(databaseId);
                    }
                } else {
                    sbff.append(databaseId + "数据库账户创建失败，msg:" + e.getMessage() + "\n");
                }
            } finally {
                Optional.ofNullable(databaseVO).ifPresent(DatabaseDcl::closeConnect);
            }
        });

        /**删除被撤销的数据库**/
        Arrays.stream(haveEmpowerIdArr).filter(e -> {
            List<String> stringList = Arrays.stream(needEmpowerIds).collect(Collectors.toList());
            return !stringList.contains(e);
        }).filter(StringUtils::isNotEmpty).forEach(s -> {
            DatabaseDefineDto dotById = this.databaseDefineService.getDotById(s);
            DatabaseDcl databaseVO = null;
            try {
                databaseVO = DatabaseUtil.getDatabaseDefine(dotById, databaseInfo);
                if (databaseVO != null) {
                    databaseVO.deleteUser(databaseUserDto.getDatabaseUpId());
                    databaseVO.closeConnect();
                    thisHaveIds.remove(s);
                }
            } catch (Exception e) {
                e.printStackTrace();
                sbff.append(s + "数据库用户删除失败，msg:" + e.getMessage() + "\n");
            } finally {
                if (databaseVO != null) {
                    databaseVO.closeConnect();
                }
            }
        });


        Arrays.stream(needEmpowerIds).filter(StringUtils::isNotEmpty).forEach(s -> {
            DatabaseDefineDto dotById = this.databaseDefineService.getDotById(s);
            DatabaseDcl databaseVO = null;
            try {
                databaseVO = DatabaseUtil.getDatabaseDefine(dotById, databaseInfo);
                if (databaseVO != null) {
                    databaseVO.updateAccount(databaseUserDto.getDatabaseUpId(), databaseUserDto.getDatabaseUpPassword());
                    databaseVO.closeConnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
                sbff.append(s + "数据库账户修改失败，msg:" + e.getMessage() + "\n");
            } finally {
                if (databaseVO != null) {
                    databaseVO.closeConnect();
                }
            }
        });


        databaseUserDto.setExamineDatabaseId(StringUtils.join(thisHaveIds, ","));

        //修改绑定ip
        thisHaveIds.forEach(s -> {
            DatabaseDefineDto dotById = this.databaseDefineService.getDotById(s);
            DatabaseDcl databaseVO = null;
            try {
                databaseVO = DatabaseUtil.getDatabaseDefine(dotById, databaseInfo);
                if (databaseVO != null) {
                    databaseVO.bindIp(databaseUserDto.getDatabaseUpId(), needEmpowerIpArr);
                    databaseVO.closeConnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (databaseVO != null) {
                    databaseVO.closeConnect();
                }
            }
        });


        String msg = sbff.toString();
        if (StringUtils.isNotBlank(msg)) {
            System.out.println(msg);
            //前端不需要返回这么详细的错误信息
            return ResultT.failed(msg);
        } else {
            return ResultT.success();
        }

    }

    @Override
    public DatabaseUserDto applyDatabaseUser(HttpServletRequest request) {
        DatabaseUserDto databases = new DatabaseUserDto();
        try {
            //下面定义存储存储上传信息的Map。
            Map<String, Object> upLoadData = new HashMap<String, Object>();
            HttpSession session = request.getSession();
            String path = session.getServletContext().getRealPath("");
            //下面取得文件目录的路径。
            String upload_file_path = path + File.separator + "tupian";
            //设置工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //设置文件存储位置
            if (!Paths.get(upload_file_path).toFile().exists()) {
                Paths.get(upload_file_path).toFile().mkdirs();
            }
            factory.setRepository(Paths.get(upload_file_path).toFile());
            //设置大小，如果文件小于设置大小的话，放入内存中，如果大于的话则放入磁盘中,单位是byte
            factory.setSizeThreshold(0);
            ServletFileUpload upload = new ServletFileUpload(factory);
            //这里就是中文文件名处理的代码，其实只有一行
            upload.setHeaderEncoding("utf-8");
            String fileName = null;
            List<FileItem> list = upload.parseRequest((RequestContext) request);
            for (FileItem item : list) {
                if (item.isFormField()) {
                    //程序执行到这里说明获取的是一般字段信息。
                    String name = item.getFieldName();
                    String value = item.getString("utf-8");
                    //下面将数据存入upLoadData中。
                    if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(value)) {//代码审查修改的
                        upLoadData.put(name, value);
                    }
                } else {
                    //程序执行到这里说明获取的是文件信息。
                    String name = item.getFieldName();
                    String value = item.getName();
                    if (!StringUtils.isEmpty(Paths.get(value).getFileName().toString())) {//代码审查修改的
                        fileName = Paths.get(value).getFileName().toString();
                    }
                    // 写文件到path目录，文件名问filename
                    if (!StringUtils.isEmpty(fileName) && !StringUtils.isEmpty(name)) {//代码审查修改的
                        item.write(new File(upload_file_path, fileName));
                        //下面将数据存入upLoadData中。
                        upLoadData.put(name, upload_file_path + fileName);

                    }
                }
            }
            databases.setApplyMaterial(fileName);
            upLoadData.forEach((k, v) -> {
                switch (k) {
                    case "databaseup_id":
                        databases.setDatabaseUpId(v.toString());
                    case "databaseup_password":
                        databases.setDatabaseUpPassword(v.toString());
                    case "database_id":
                        databases.setApplyDatabaseId(v.toString());
                    case "databaseup_desc":
                        databases.setDatabaseUpDesc(v.toString());
                    case "user_id":
                        databases.setUserId(v.toString());
                    case "DATABASEUP_IP":
                        databases.setDatabaseUpIp(v.toString());
                    case "DATABASEUP_IP_SEGMENT":
                        databases.setDatabaseUpIpSegment(v.toString());
                }
            });
            String st = "0";
            databases.setExamineStatus(st);
            DatabaseUserEntity databaseUserEntity = this.databaseUserMapper.toEntity(databases);
            this.saveNotNull(databaseUserEntity);
            return this.databaseUserMapper.toDto(databaseUserEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databases;
    }

    @Override
    public Map<String, Object> dataAuthorityCancel(String user_id, String database_id, String data_class_id, Integer apply_authoritys, String mark) {

        String[] permission = {"SELECT", "UPDATE", "INSERT", "DELETE"};
        //下面根据用户ID取得审核通过的用户数据库访问账户信息。
        //获取用户up账户
        Map<String, Object> map = new HashMap<>();
        DatabaseUserDto dbaccount = databaseUserService.findByUserIdAndExamineStatus(user_id, "1");
        DataAuthorityApplyDto dataAuthorityApplyDto = new DataAuthorityApplyDto();
        dataAuthorityApplyDto.setUserId(user_id);

        if (dbaccount != null) {


            //下面取得用户可用数据库ID。
            String a[] = dbaccount.getExamineDatabaseId().split(",");
            try {
                //下面判断是否为可分配权限的数据库ID。
                Boolean Flag = false;
                //下面取得database_id对应的父物理库ID。
                DatabaseEntity databaseEntity = databaseDao.findById(database_id).get();
                DatabaseDto databaseDto = this.databaseMapper.toDto(databaseEntity);

                //下面取得资料信息。
                List<DataTableEntity> dataTableList = dataTableDao.findByDataServiceIdAndClassLogicId(data_class_id, database_id);
                //下面根据物理库ID取得物理库对应详细信息。
                //DataBasePhysics databasephysics = dataBasePhysicsDao.queryDataBasePhysicsByDbIds(database_id);
                //获取数据库管理账户
                DatabaseAdministratorEntity databaseAdministratorEntity = null;
                Set<DatabaseAdministratorEntity> databaseAdministratorList = databaseEntity.getDatabaseDefine().getDatabaseAdministratorList();
                for (DatabaseAdministratorEntity databasephysics : databaseAdministratorList) {
                    if (databasephysics.getIsManager()) {
                        databaseAdministratorEntity = databasephysics;
                        break;
                    }
                }
                //下面循环处理li中的每张表。
                for (DataTableEntity dataTableEntity : dataTableList) {
                    //下面取得表名。
                    String table_name = dataTableEntity.getTableName();
                    DatabaseDcl databaseDcl = null;
                    try {
                        databaseDcl = DatabaseUtil.getDatabase(databaseDto, databaseInfo);
                    } catch (Exception e) {
                        if (e.getMessage().contains("用户不存在")) {
                            map.put("returnCode", 1);
                            map.put("returnMessage", databaseDto.getDatabaseDefine().getDatabaseName() + "用户不存在！");
                            Optional.ofNullable(databaseDcl).ifPresent(DatabaseDcl::closeConnect);
                            continue;
                        }
                    }
                    try {
                        if (databaseDcl != null) {
                            databaseDcl.deletePermissions(permission, databaseEntity.getSchemaName(), table_name, dbaccount.getDatabaseUpId(), null, null);
                            databaseDcl.closeConnect();
                        }
                    } catch (Exception e) {
                        map.put("returnCode", 2);
                        map.put("returnMessage", "撤销权限失败！");
                        continue;
                    }


                    //下面根据数据库类型删除表的读写权限。
                    //下面修改资料权限状态。
                    if (mark.equals("1"))//这里是针对专题库权限的撤销。
                    {
                        //下面修改所有用户自建专题库中表名相同的资料状态为禁用。
                        //根据用户id，查询该用户所有自建专题库下申请访问的资料(既包含申请访问资料，又包含私有资料)
                        List<Map<String, Object>> list10 = mybatisQueryMapper.getSpecialAuthorizeList(user_id);
                        if (list10 != null) {
                            for (Map<String, Object> map10 : list10) {
                                //下面取得表名，并判断是否table_name相同。
                                //下面要处理table_name为空的特殊情况。
                                if (map10.get("table_name").toString().equals("") == false) {
                                    List<String> tableName10 = (ArrayList<String>) map10.get("table_name");
                                    for (int i = 1; i <= tableName10.size(); i = i + 1) {
                                        if (tableName10.get(i - 1).equals(table_name) == true) {
                                            //程序执行到这里说明找到了表名相同的资料。
                                            //业务专题库中对于目前已经撤销权限的物理库进行授权状态更改
                                            String TDB_ID = map10.get("TDB_ID").toString();
                                            String DATABASE_ID = map10.get("LOGIC_ID").toString();
                                            String DATA_CLASS_ID = map10.get("DATA_CLASS_ID").toString();
                                            //下面判断表名相同资料是与撤销资料为同一物理库ID。
                                            if (DATABASE_ID.equals(database_id) == true) {
                                                DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto = new DatabaseSpecialReadWriteDto();
                                                databaseSpecialReadWriteDto.setSdbId(TDB_ID);
                                                databaseSpecialReadWriteDto.setDatabaseId(DATABASE_ID);
                                                databaseSpecialReadWriteDto.setDataClassId(DATA_CLASS_ID);
                                                databaseSpecialReadWriteService.saveDto(databaseSpecialReadWriteDto);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //下面修改用户所有引用资料中表名相同的资料状态为禁用。
                        //根据用户id，查询该用户已申请访问的资料(资料使用权限申请数据)。
                        List<Map<String, Object>> list20 = dataAuthorityApplyService.getRecordListByUserId(user_id);
                        if (list20 != null) {
                            for (Map<String, Object> map10 : list20) {
                                //下面取得表名，并判断是否table_name相同。
                                //下面要处理table_name为空的特殊情况。
                                if (map10.get("table_name").toString().equals("") == false) {
                                    List<String> tableName10 = (ArrayList<String>) map10.get("table_name");
                                    for (int i = 1; i <= tableName10.size(); i = i + 1) {
                                        if (tableName10.get(i - 1).equals(table_name) == true) {
                                            //数据授权中对于目前已经撤销权限的物理库进行授权状态更改
                                            String APPLY_ID = map10.get("APPLY_ID").toString();
                                            String DATABASE_ID = map10.get("DATABASE_ID").toString();
                                            String DATA_CLASS_ID = map10.get("DATA_CLASS_ID").toString();
                                            //下面判断表名相同资料是与撤销资料为同一物理库ID。
                                            if (DATABASE_ID.equals(database_id) == true) {
                                                Integer AUTHORIZE = 2;
                                                mybatisQueryMapper.updateDataAuthorityStatus(APPLY_ID, DATABASE_ID, DATA_CLASS_ID, AUTHORIZE);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (mark.equals("2"))//这里是针对引用资料权限的撤销。
                    {
                        //下面修改所有用户自建专题库中表名相同的资料状态为禁用。
                        //根据用户id，查询该用户所有自建专题库下申请访问的资料(既包含申请访问资料，又包含私有资料)
                        List<Map<String, Object>> list10 = mybatisQueryMapper.getSpecialAuthorizeList(user_id);
                        if (list10 != null) {
                            for (Map<String, Object> map10 : list10) {
                                //下面取得表名，并判断是否table_name相同。
                                //下面要处理table_name为空的特殊情况。
                                if (map10.get("table_name").toString().equals("") == false) {
                                    List<String> tableName10 = (ArrayList<String>) map10.get("table_name");
                                    for (int i = 1; i <= tableName10.size(); i = i + 1) {
                                        if (tableName10.get(i - 1).equals(table_name) == true) {
                                            //程序执行到这里说明找到了表名相同的资料。
                                            //业务专题库中对于目前已经撤销权限的物理库进行授权状态更改
                                            String TDB_ID = map10.get("TDB_ID").toString();
                                            String DATABASE_ID = map10.get("LOGIC_ID").toString();
                                            String DATA_CLASS_ID = map10.get("DATA_CLASS_ID").toString();
                                            //下面判断表名相同资料是与撤销资料为同一物理库ID。
                                            if (DATABASE_ID.equals(database_id) == true) {
                                                DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto = new DatabaseSpecialReadWriteDto();
                                                databaseSpecialReadWriteDto.setSdbId(TDB_ID);
                                                databaseSpecialReadWriteDto.setDatabaseId(DATABASE_ID);
                                                databaseSpecialReadWriteDto.setDataClassId(DATA_CLASS_ID);
                                                databaseSpecialReadWriteService.saveDto(databaseSpecialReadWriteDto);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //下面修改所有用户引用资料中表名相同的资料状态为禁用。
                        //根据用户id，查询该用户已申请访问的资料(资料使用权限申请数据)。
                        List<Map<String, Object>> list20 = dataAuthorityApplyService.getRecordListByUserId(user_id);
                        if (list20 != null) {
                            for (Map<String, Object> map10 : list20) {
                                //下面取得表名，并判断是否table_name相同。
                                //下面要处理table_name为空的特殊情况。
                                if (map10.get("table_name").toString().equals("") == false) {
                                    List<String> tableName10 = (ArrayList<String>) map10.get("table_name");
                                    for (int i = 1; i <= tableName10.size(); i = i + 1) {
                                        if (tableName10.get(i - 1).equals(table_name) == true) {
                                            //数据授权中对于目前已经撤销权限的物理库进行授权状态更改
                                            String APPLY_ID = map10.get("APPLY_ID").toString();
                                            String DATABASE_ID = map10.get("DATABASE_ID").toString();
                                            String DATA_CLASS_ID = map10.get("DATA_CLASS_ID").toString();
                                            //下面判断表名相同资料是与撤销资料为同一物理库ID。
                                            if (DATABASE_ID.equals(database_id) == true) {
                                                Integer AUTHORIZE = 2;
                                                mybatisQueryMapper.updateDataAuthorityStatus(APPLY_ID, DATABASE_ID, DATA_CLASS_ID, AUTHORIZE);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
                map.put("returnCode", 0);
                map.put("returnMessage", "撤销权限成功");

            } catch (Exception e) {
                e.printStackTrace();
                map.put("returnCode", 2);
                map.put("returnMessage", "撤销权限失败");
            }
        } else {
            map.put("returnCode", 4);
            map.put("returnMessage", "该用户未创建数据库访问账户");
        }

        return map;
    }

    @Override
    public ResultT changePassword(String id, String oldPwd, String newPwd) {
        DatabaseUserDto databaseUserDto = this.getDotById(id);
        if (!databaseUserDto.getDatabaseUpPassword().equals(oldPwd)) {
            return ResultT.failed("输入的旧密码不正确");
        }

        StringBuffer buffer = new StringBuffer();
        boolean flag = true;
        String[] databaseIds = databaseUserDto.getExamineDatabaseId().split(",");
        for (String databaseId : databaseIds) {
            DatabaseDefineDto databaseDefineDto = databaseDefineService.getDotById(databaseId);

            DatabaseDcl databaseDefine = null;
            try {
                databaseDefine = DatabaseUtil.getDatabaseDefine(databaseDefineDto, databaseInfo);
            } catch (Exception e) {
                buffer.append("物理库：" + databaseDefineDto.getDatabaseName() + ",没有管理员账户" + "<br/>");
                continue;
            }
            try {
                databaseDefine.updateAccount(databaseUserDto.getDatabaseUpId(), newPwd);
            } catch (Exception e) {
                buffer.append("物理库：" + databaseDefineDto.getDatabaseName() + ",密码修改失败" + "<br/>");
            }
            Optional.ofNullable(databaseDefine).ifPresent(DatabaseDcl::closeConnect);
            //获取数据库管理账户
            DatabaseAdministratorDto databaseAdministratorDto = null;
            Set<DatabaseAdministratorDto> databaseAdministratorList = databaseDefineDto.getDatabaseAdministratorList();
            for (DatabaseAdministratorDto databaseAdministratorDto1 : databaseAdministratorList) {
                if (databaseAdministratorDto1.getIsManager()) {
                    databaseAdministratorDto = databaseAdministratorDto1;
                    break;
                }
            }
            if (databaseAdministratorDto == null) {
                buffer.append("物理库：" + databaseDefineDto.getDatabaseName() + ",没有管理员账户" + "<br/>");
                continue;
            }

        }
        //数据库全部更新成功，修改记录密码
        if (flag) {

        }
        return null;
    }

    @Override
    public ResultT updateBizPwd(String bizUserId, String ids, String newPwd) {
        for (String id : ids.split(",")) {
            DatabaseDefineDto databaseDefineDto = databaseDefineService.getDotById(id);
            DatabaseDcl databaseDcl = null;
            try {
                databaseDcl = DatabaseUtil.getDatabaseDefine(databaseDefineDto, databaseInfo);
                databaseDcl.updateAccount(bizUserId, newPwd);
                databaseDcl.closeConnect();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                databaseDcl.closeConnect();
            }
        }
        return ResultT.success();
    }


}
