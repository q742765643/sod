package com.piesat.dm.rpc.service.database;

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
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.dao.database.DatabaseDefineDao;
import com.piesat.dm.dao.database.DatabaseUserDao;
import com.piesat.dm.entity.database.DatabaseAdministratorEntity;
import com.piesat.dm.entity.database.DatabaseDefineEntity;
import com.piesat.dm.entity.database.DatabaseEntity;
import com.piesat.dm.entity.database.DatabaseUserEntity;
import com.piesat.dm.rpc.api.database.DatabaseUserService;
import com.piesat.dm.rpc.dto.database.DatabaseUserDto;
import com.piesat.dm.rpc.mapper.database.DatabaseUserMapper;
import com.piesat.ucenter.entity.system.DictTypeEntity;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Paths;
import java.util.*;

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
    private DatabaseDefineDao databaseDefineDao;
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


    @Override
    public BaseDao<DatabaseUserEntity> getBaseDao() {
        return databaseUserDao;
    }

    @Override
    public PageBean selectPageList(PageForm<DatabaseUserDto> pageForm) {
        DatabaseUserEntity databaseUserEntity=databaseUserMapper.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotBlank(databaseUserEntity.getExamineStatus())){
            specificationBuilder.add("examineStatus", SpecificationOperator.Operator.eq.name(),databaseUserEntity.getExamineStatus());
        }
        Sort sort = Sort.by(Sort.Direction.ASC,"examineStatus").and(Sort.by(Sort.Direction.DESC,"createTime"));
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<DatabaseUserEntity> databaseUserEntityList= (List<DatabaseUserEntity>) pageBean.getPageData();
        List<DatabaseUserDto> databaseUserDtoList = databaseUserMapper.toDto(databaseUserEntityList);
        //获取数据库列表，查询展示数据库中文名称
        List<DatabaseEntity> databaseEntityList = databaseDao.findAll();
        if(databaseUserDtoList!=null&&databaseUserDtoList.size()>0&&databaseEntityList!=null&&databaseEntityList.size()>0){
            for(DatabaseUserDto dto : databaseUserDtoList){
                String[] applyDatabaseIdArray = dto.getApplyDatabaseId().split(",");
                String applyDatabaseName = "";
                for(String applyDatabaseId : applyDatabaseIdArray){
                    for(DatabaseEntity databaseEntity : databaseEntityList){
                        if(applyDatabaseId.equals(databaseEntity.getId())){
                            applyDatabaseName += databaseEntity.getDatabaseDefine().getDatabaseName()+",";
                        }
                    }
                }
                if(applyDatabaseName.length()>0){
                    applyDatabaseName = applyDatabaseName.substring(0,applyDatabaseName.length()-1);
                }
                dto.setApplyDatabaseName(applyDatabaseName);
            }
        }
        pageBean.setPageData(databaseUserDtoList);
        return pageBean;
    }

	@Override
    public DatabaseUserDto mergeDto(DatabaseUserDto databaseUserDto) {
        this.delete(databaseUserDto.getId());
        DatabaseUserEntity databaseUserEntity = this.databaseUserMapper.toEntity(databaseUserDto);
        this.save(databaseUserEntity);
        return this.databaseUserMapper.toDto(databaseUserEntity);
    }

    @Override
    public void exportData(String examineStatus) {
        List<DatabaseUserEntity> byExamineStatus = this.databaseUserDao.findByExamineStatus(examineStatus);
        ExcelUtil<DatabaseUserEntity> util=new ExcelUtil(DatabaseUserEntity.class);
        util.exportExcel(byExamineStatus,"数据库访问账户信息");
    }

    @Override
    public List<DatabaseUserDto> all() {
        List<DatabaseUserEntity> all = this.getAll();
        return this.databaseUserMapper.toDto(all);
    }

    @Override
    public DatabaseUserDto getDotById(String id) {
        DatabaseUserEntity databaseUserEntity = this.getById(id);
        return this.databaseUserMapper.toDto(databaseUserEntity);
    }

    @Override
    public DatabaseUserDto getDotByUPID(String databaseUPId) {
        DatabaseUserEntity databaseUserEntity = databaseUserDao.findByDatabaseUpId(databaseUPId);
        return this.databaseUserMapper.toDto(databaseUserEntity);
    }

    @Override
    public DatabaseUserDto getDotByUserId(String userId) {
        DatabaseUserEntity databaseUserEntity = databaseUserDao.findByUserIdAndExamineStatusNot(userId,"2");
        return this.databaseUserMapper.toDto(databaseUserEntity);
    }

    @Override
    public DatabaseUserDto findByUserIdAndExamineStatus(String userId,String examineStatus) {
        DatabaseUserEntity databaseUserEntity = databaseUserDao.findByUserIdAndExamineStatus(userId,examineStatus);
        return this.databaseUserMapper.toDto(databaseUserEntity);
    }

    @Override
    public DatabaseUserDto saveDto(DatabaseUserDto databaseUserDto) {
        DatabaseUserEntity databaseUserEntity = this.databaseUserMapper.toEntity(databaseUserDto);
        this.save(databaseUserEntity);
        return this.databaseUserMapper.toDto(databaseUserEntity);
    }

    @Override
    public boolean empower(DatabaseUserDto databaseUserDto) {
        try{
            //根据ID获取旧的申请信息
            DatabaseUserEntity oldDatabaseUserEntity = this.getById(databaseUserDto.getId());
            //待授权Id
            String[] needEmpowerIdArr = databaseUserDto.getDatabaseUpId().split(",");
            List<String> needEmpowerIdist = Arrays.asList(needEmpowerIdArr);
            String[] haveEmpowerIdArr = oldDatabaseUserEntity.getDatabaseUpId().split(",");
            List<String> haveEmpowerIdist = Arrays.asList(haveEmpowerIdArr);
            //非首次审核通过，授权的id中去掉以前的id
            if(oldDatabaseUserEntity.getExamineStatus().equals("1")){
                needEmpowerIdist.removeAll(haveEmpowerIdist);
            }

            /**为申请的IP授权**/
            //待授权IP
            String[] needEmpowerIpArr = databaseUserDto.getDatabaseUpIp().split(";");
            for(String databaseId : needEmpowerIdist){
                DatabaseDcl databaseVO = getDatabase(databaseId);
                if(databaseVO!=null){
                    databaseVO.addUser(databaseUserDto.getDatabaseUpId(),databaseUserDto.getDatabaseUpPassword(),needEmpowerIpArr);
                    databaseVO.closeConnect();
                }
            }

            /**为已有账号修改密码**/
            if(oldDatabaseUserEntity.getExamineStatus().equals("1")){
                needEmpowerIdist.addAll(haveEmpowerIdist);
            }
            for(String databaseId : needEmpowerIdist){
                DatabaseDcl databaseVO = getDatabase(databaseId);
                if(databaseVO!=null){
                    databaseVO.updateAccount(databaseUserDto.getDatabaseUpId(),databaseUserDto.getDatabaseUpPassword());
                    databaseVO.closeConnect();
                }
            }

            /**删除被撤销的数据库**/
            haveEmpowerIdist.removeAll(needEmpowerIdist);
            for(String databaseId : haveEmpowerIdist){
                DatabaseDcl databaseVO = getDatabase(databaseId);
                if(databaseVO!=null){
                    for(String ip : needEmpowerIpArr){
                        databaseVO.deleteUser(databaseUserDto.getDatabaseUpId(),ip);
                        databaseVO.closeConnect();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param databaseId
     * @return
     */
    private DatabaseDcl getDatabase(String databaseId){
        DatabaseDcl databaseVO = null;
        try{
            DatabaseEntity databaseEntity = databaseDao.findById(databaseId).get();
            DatabaseDefineEntity databaseDefineEntity = databaseEntity.getDatabaseDefine();
            Set<DatabaseAdministratorEntity> databaseAdministratorSet = databaseDefineEntity.getDatabaseAdministratorList();
            //访问路径、账号、密码
            String url = databaseDefineEntity.getDatabaseUrl();

            if(databaseAdministratorSet!=null){
                //获取任意登录账号
                DatabaseAdministratorEntity databaseAdministratorEntity = databaseAdministratorSet.iterator().next();
                String username = databaseAdministratorEntity.getUserName();
                String password = databaseAdministratorEntity.getPassWord();

                //判断是什么数据库
                if(databaseDefineEntity.getDatabaseType().equals(databaseInfo.getXugu())){
                    databaseVO = new Xugu(url,username,password);
                }else if(databaseDefineEntity.getDatabaseType().equals(databaseInfo.getGbase8a())){
                    databaseVO = new Gbase8a(url,username,password);
                }else if(databaseDefineEntity.getDatabaseType().equals(databaseInfo.getCassandra())){
                    databaseVO = new Cassandra(databaseDefineEntity.getDatabaseIp(),
                            Integer.parseInt(databaseDefineEntity.getDatabasePort()),
                            username,password,databaseEntity.getSchemaName());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return databaseVO;
    }
    @Override
    public DatabaseUserDto applyDatabaseUser(HttpServletRequest request){
        DatabaseUserDto databases = new DatabaseUserDto();
        try{
            //下面定义存储存储上传信息的Map。
            Map<String,Object> upLoadData= new HashMap<String,Object>();
            HttpSession session = request.getSession();
            String path = session.getServletContext().getRealPath("");
            //下面取得文件目录的路径。
            String upload_file_path = path + File.separator+"tupian";
            //设置工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //设置文件存储位置
            if(!Paths.get(upload_file_path).toFile().exists())
            {
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
            for (FileItem item : list)
            {
                if (item.isFormField())
                {
                    //程序执行到这里说明获取的是一般字段信息。
                    String name = item.getFieldName();
                    String value = item.getString("utf-8");
                    //下面将数据存入upLoadData中。
                    if(!StringUtils.isEmpty(name) && !StringUtils.isEmpty(value)){//代码审查修改的
                        upLoadData.put(name, value);
                    }
                }
                else
                {
                    //程序执行到这里说明获取的是文件信息。
                    String name = item.getFieldName();
                    String value = item.getName();
                    if(!StringUtils.isEmpty(Paths.get(value).getFileName().toString())){//代码审查修改的
                        fileName =Paths.get(value).getFileName().toString();
                    }
                    // 写文件到path目录，文件名问filename
                    if(!StringUtils.isEmpty(fileName) && !StringUtils.isEmpty(name)){//代码审查修改的
                        item.write(new File(upload_file_path, fileName));
                        //下面将数据存入upLoadData中。
                        upLoadData.put(name, upload_file_path+fileName);
                    }
                }
            }
            for (Map.Entry<String,Object> item : upLoadData.entrySet())
            {
                //下面取得upLoadData中的每个值。
                String key = item.getKey();
                Object val = item.getValue();
                if(key.equals("databaseup_id")==true)
                    databases.setDatabaseUpId((String) val);
                else if(key.equals("databaseup_password")==true)
                {
                    databases.setDatabaseUpPassword((String)val);
                }
                else if(key.equals("database_id")==true)
                {
                    databases.setApplyDatabaseId((String)val);
                }
                else if(key.equals("databaseup_desc")==true)
                {
                    databases.setDatabaseUpDesc((String)val);
                }
                else if(key.equals("user_id")==true)
                {
                    databases.setUserId((String)val);
                }
                else if(key.equals("database_id")==true)
                {
                    databases.setApplyDatabaseId((String)val);
                }
                else if(key.equals("DATABASEUP_IP")==true)
                {
                    databases.setDatabaseUpIp((String)val);
                }
                else if(key.equals("DATABASEUP_IP_SEGMENT")==true)
                {
                    databases.setDatabaseUpIpSegment((String)val);
                }
                else if(key.equals("databaseup_desc")==true)
                {
                    databases.setDatabaseUpDesc((String)val);
                }
                else if(key.equals("apply_material")==true){
                    databases.setApplyMaterial(fileName);
                }
            }
            String st = "0";
            databases.setExamineStatus(st);
            DatabaseUserEntity databaseUserEntity = this.databaseUserMapper.toEntity(databases);
            this.save(databaseUserEntity);
            return this.databaseUserMapper.toDto(databaseUserEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databases;
    }
    @Override
    public Map<String, Object> dataAuthorityCancel(String user_id, String database_id, String data_class_id, Integer apply_authoritys, String mark) {
        String ip = "";
        String [] permission={"SELECT","UPDATE","INSERT","DELETE"};
        //下面根据用户ID取得审核通过的用户数据库访问账户信息。
        //获取用户up账户
        Map<String, Object> map = new HashMap<>();
        DatabaseUserDto dbaccount = databaseUserService.findByUserIdAndExamineStatus(user_id, "1");
        DataAuthorityApplyDto dataAuthorityApplyDto = new DataAuthorityApplyDto();
        dataAuthorityApplyDto.setUserId(user_id);
        List<DataAuthorityRecordDto> dataAuthorityRecordList = dataAuthorityApplyDto.getDataAuthorityRecordList();
        if(dbaccount!=null)
        {
            //判断用户申请绑定的ip类型,并取得对应IP地址或IP地址段。
            if(!StringUtils.isEmpty(dbaccount.getDatabaseUpIp()))
            {
                ip = dbaccount.getDatabaseUpIp();
            }
            else if (!StringUtils.isEmpty(dbaccount.getDatabaseUpIpSegment()))
            {
                ip = dbaccount.getDatabaseUpIpSegment();
            }

            //下面定义变量。
            Map<String, String> hashmap = new HashMap<String, String>();
            //下面取得用户可用数据库ID。
            String a[] = dbaccount.getExamineDatabaseId().split(",");
            try
            {
                //下面判断是否为可分配权限的数据库ID。
                Boolean Flag = false;
                //下面取得database_id对应的父物理库ID。
                DatabaseEntity databaseEntity = databaseDao.findById(database_id).get();
                String parentDatabase_id=databaseEntity.getDatabaseDefine().getId();
                //下面判断已授权的物理库对应父物理库是否是南大或虚谷数据，并判断是否是用户可用的物理库。
                if(parentDatabase_id.equals("HADB") || parentDatabase_id.equals("STDB") || parentDatabase_id.equals("BFDB") || parentDatabase_id.equals("FIDB"))
                {
                    for (int i = 0; i < a.length; i++)
                    {
                        if (a[i].equals(parentDatabase_id))
                        {
                            Flag =true;
                            break;
                        }
                    }
                }
                if(Flag==true)
                {
                    //程序执行到这里说明该数据库ID为可分配权限的数据库。
                    //下面根据资料存储编码和物理库ID取得该资料的详细信息。
                    //下面取得资料信息。
                    List<DataTableEntity> dataTableList = dataTableDao.findByDataServiceIdAndClassLogicId(data_class_id,database_id);
                    //下面根据物理库ID取得物理库对应详细信息。
                    //DataBasePhysics databasephysics = dataBasePhysicsDao.queryDataBasePhysicsByDbIds(database_id);
                    //获取数据库管理账户
                    DatabaseAdministratorEntity databaseAdministratorEntity = null;
                    Set<DatabaseAdministratorEntity>  databaseAdministratorList=databaseEntity.getDatabaseDefine().getDatabaseAdministratorList();
                    for(DatabaseAdministratorEntity databasephysics : databaseAdministratorList){
                        if(databasephysics.getIsManager()){
                            databaseAdministratorEntity = databasephysics;
                            break;
                        }
                    }
                    //下面循环处理li中的每张表。
                    for (DataTableEntity dataTableEntity : dataTableList) {
                        //下面取得表名。
                        String table_name = dataTableEntity.getTableName();
                        //下面通过表名取得对应资料存储编码。
                        //List<DminDataIdTable> lis = dminDataIdTableDao.getDataclassIdByTableName(table_name);
                        if (databaseEntity.getDatabaseDefine().getDatabaseType().equalsIgnoreCase("xugu")) {//xugu
                            //进行权限撤销
                            if (databaseAdministratorEntity == null) {
                                map.put("returnCode", 1);
                                map.put("returnMessage", "没有xugu管理员账户！");
                                continue;
                            }
                            try {
                                Xugu xugu = new Xugu(databaseEntity.getDatabaseDefine().getDatabaseUrl(), databaseAdministratorEntity.getUserName(), databaseAdministratorEntity.getPassWord());
                                xugu.deletePermissions(permission, databaseEntity.getSchemaName(), table_name, dbaccount.getDatabaseUpId(), "", null);
                            } catch (Exception e) {
                                e.printStackTrace();
                                map.put("returnCode", 2);
                                map.put("returnMessage", "撤销权限失败");
                            }

                        } else if (databaseEntity.getDatabaseDefine().getDatabaseType().equalsIgnoreCase("Gbase8a")) {//gbase8a
                            if (databaseAdministratorEntity == null) {
                                map.put("returnCode", 1);
                                map.put("returnMessage", "没有Gbase8a管理员账户！");
                                continue;
                            }
                            try {
                                Gbase8a gbase8a = new Gbase8a(databaseEntity.getDatabaseDefine().getDatabaseUrl(), databaseAdministratorEntity.getUserName(), databaseAdministratorEntity.getPassWord());
                                List<String> ips = new ArrayList<String>();
                                ips.add(ip);
                                gbase8a.deletePermissions(permission, databaseEntity.getSchemaName(), table_name, dbaccount.getDatabaseUpId(), dbaccount.getDatabaseUpPassword(), ips);

                            } catch (Exception e) {
                                e.printStackTrace();
                                map.put("returnCode", 2);
                                map.put("returnMessage", "撤销权限失败");
                            }

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
                        if (mark.equals("3"))//这里是针对用户注册资料权限的撤销。
                        {
                            //这里不用处理引用资料和专题库中资料。
                        }
                    }
                    map.put("returnCode", 0);
                    map.put("returnMessage", "撤销权限成功");
                }
                else if(Flag==false)
                {
                    map.put("returnCode", 3);
                    map.put("returnMessage", "不具备该物理库访问权限！");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                map.put("returnCode", 2);
                map.put("returnMessage", "撤销权限失败");
            }
        }
        else if(dbaccount==null)
        {
            map.put("returnCode", 4);
            map.put("returnMessage", "该用户未创建数据库访问账户");
        }

        //下面返回。
        return map;
    }
}
