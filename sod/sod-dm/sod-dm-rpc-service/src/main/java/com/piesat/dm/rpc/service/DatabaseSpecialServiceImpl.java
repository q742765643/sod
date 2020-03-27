package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.entity.UUIDEntity;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.common.tree.Tree;
import com.piesat.dm.common.tree.Ztree;
import com.piesat.dm.core.api.DatabaseDcl;
import com.piesat.dm.core.api.impl.Cassandra;
import com.piesat.dm.core.api.impl.Gbase8a;
import com.piesat.dm.core.api.impl.Xugu;
import com.piesat.dm.core.parser.DatabaseInfo;
import com.piesat.dm.dao.*;
import com.piesat.dm.entity.*;
import com.piesat.dm.rpc.api.DatabaseSpecialService;
import com.piesat.dm.rpc.api.DatabaseUserService;
import com.piesat.dm.rpc.dto.*;
import com.piesat.dm.rpc.mapper.*;
import org.bouncycastle.asn1.dvcs.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public DatabaseSpecialDto getDotById(String id) {
        DatabaseSpecialEntity databaseSpecialEntity = this.getById(id);
        return this.databaseSpecialMapper.toDto(databaseSpecialEntity);
    }

    @Override
    public DatabaseSpecialDto saveDto(DatabaseSpecialDto databaseSpecialDto) {
        DatabaseSpecialEntity databaseSpecialEntity = this.databaseSpecialMapper.toEntity(databaseSpecialDto);
        this.save(databaseSpecialEntity);
        return this.databaseSpecialMapper.toDto(databaseSpecialEntity);
    }

    /**
     * 数据库授权
     * @param databaseDto
     */
    @Override
    public void empowerDatabaseSperial(DatabaseDto databaseDto) {
        try {
            String userId = databaseDto.getUserId();
            DatabaseDefineDto databaseDefineDto = databaseDto.getDatabaseDefine();
            //判断用户是否申请过数据库账户
            List<DatabaseUserEntity> databaseUserEntityList = databaseUserDao.findByUserId(userId);
            if(databaseUserEntityList==null || databaseUserEntityList.size()==0){
                return;
            }
            //删除历史授权记录，重新添加
            String sdbId = databaseDto.getTdbId();
            databaseSpecialAuthorityDao.deleteBySdbId(sdbId);
            //需要授权的数据库列表
            List<DatabaseSpecialAuthorityDto> databaseSpecialAuthorityList = databaseDto.getDatabaseSpecialAuthorityList();
            for(int i=0; i<databaseSpecialAuthorityList.size();i++){
                DatabaseSpecialAuthorityDto databaseSpecialAuthorityDto = databaseSpecialAuthorityList.get(i);
                databaseSpecialAuthorityDao.save(databaseSpecialAuthorityMapper.toEntity(databaseSpecialAuthorityDto));
                databaseDto.setId(databaseSpecialAuthorityDto.getDatabaseId());
                //获取物理库历史配置信息
                DatabaseEntity oldDatabaseEntity = databaseDao.findByDatabaseDefine(databaseSpecialAuthorityDto.getDatabaseId());
                DatabaseDefineEntity oldDatabaseDefineEntity = oldDatabaseEntity.getDatabaseDefine();
                if(oldDatabaseEntity==null){
                    return ;
                }
                //判断数据库类型
                if(oldDatabaseDefineEntity.getDatabaseType().equals(databaseInfo.getXugu())){
                    databaseDefineDto.setDatabaseInstance(oldDatabaseDefineEntity.getDatabaseInstance());
                }else if(oldDatabaseDefineEntity.getDatabaseType().equals(databaseInfo.getGbase8a())
                    || oldDatabaseDefineEntity.getDatabaseType().equals(databaseInfo.getCassandra())){
                    databaseDefineDto.setDatabaseInstance(databaseDefineDto.getDatabaseInstance());
                }else{
                    return;
                }
                databaseDefineDto.setDatabaseType(oldDatabaseDefineEntity.getDatabaseType());
                databaseDefineDto.setCreateTime(new Date());
                databaseDefineDto.setUserDisplayControl(1);
                //申请专题库物理库
                List<DatabaseEntity> databaseEntityList = databaseDao.findByTdbId(databaseDto.getTdbId());
                if(databaseEntityList==null&&databaseEntityList.size()==0){
                    databaseDto.setId(UUID.randomUUID().toString());
                    databaseDao.save(databaseMapper.toEntity(databaseDto));
                }
                //申请创建模式
                Set<DatabaseAdministratorEntity> databaseAdministratorSet = oldDatabaseDefineEntity.getDatabaseAdministratorList();
                //访问路径、账号、密码
                String url = oldDatabaseDefineEntity.getDatabaseUrl();
                if(databaseAdministratorSet!=null){
                    //获取任意登录账号
                    DatabaseAdministratorEntity databaseAdministratorEntity = databaseAdministratorSet.iterator().next();
                    String username = databaseAdministratorEntity.getUserName();
                    String password = databaseAdministratorEntity.getPassWord();
                    DatabaseDcl databaseVO = null;
                    //
                    String schemaName = databaseDto.getSchemaName();
                    DatabaseUserEntity databaseUserEntity = databaseUserEntityList.get(0);
                    String databaseUpPassword = databaseUserEntity.getDatabaseUpPassword();
                    String databaseUpId = databaseUserEntity.getDatabaseUpId();
                    String[] upIpArr = databaseUserEntity.getDatabaseUpIp().split(";");
                    List<String> databaseUpIpList = Arrays.asList(upIpArr);
                    //表数据增删改查权限
                    boolean dataAuthor = databaseSpecialAuthorityDto.getTableDataAccess() == 2;
                    //创建表权限
                    boolean creatAuthor = databaseSpecialAuthorityDto.getCreateTable() == 2;
                    //删除表权限
                    boolean dropAuthor = databaseSpecialAuthorityDto.getDeleteTable() == 2;
                    //判断是什么数据库
                    if(oldDatabaseDefineEntity.getDatabaseType().equals(databaseInfo.getXugu())){
                        databaseVO = new Xugu(url,username,password);
                        databaseVO.createSchemas(schemaName,databaseUpId,null,dataAuthor,creatAuthor,dropAuthor,null);
                    }else if(oldDatabaseDefineEntity.getDatabaseType().equals(databaseInfo.getGbase8a())){
                        databaseVO = new Gbase8a(url,username,password);
                        databaseVO.createSchemas(schemaName,databaseUpId,databaseUpPassword,dataAuthor,creatAuthor,dropAuthor,databaseUpIpList);
                    }else if(oldDatabaseDefineEntity.getDatabaseType().equals(databaseInfo.getCassandra())){
                        databaseVO = new Cassandra(oldDatabaseDefineEntity.getDatabaseIp(),
                                Integer.parseInt(oldDatabaseDefineEntity.getDatabasePort()),
                                username,password,oldDatabaseEntity.getSchemaName());
                        databaseVO.createSchemas(schemaName,databaseUpId,databaseUpPassword,dataAuthor,creatAuthor,dropAuthor,databaseUpIpList);
                    }else{
                        return;
                    }
                    if(databaseVO!=null){
                        databaseVO.closeConnect();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void empowerDataOne(DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto) {
        try {
            //更新权限
            DatabaseSpecialReadWriteEntity databaseSpecialReadWriteEntity = databaseSpecialReadWriteMapper
                    .toEntity(databaseSpecialReadWriteDto);
            databaseSpecialReadWriteDao.save(databaseSpecialReadWriteEntity);

            if(databaseSpecialReadWriteDto.getDatabaseId()!="RADB"){
                String userId = databaseSpecialReadWriteDto.getUserId();
                String databaseId = databaseSpecialReadWriteDto.getDatabaseId();
                String dataClassId = databaseSpecialReadWriteDto.getDataClassId();
                Integer applyAuthority = databaseSpecialReadWriteDto.getApplyAuthority();
                Map<String,Object> paramMap = new HashMap<>();

                if(databaseSpecialReadWriteDto.getExamineStatus()==1){
                    //授权
                    empowerAuthority(userId,databaseId,dataClassId,applyAuthority);
                }else if(databaseSpecialReadWriteDto.getExamineStatus()==2){
                    //撤销授权
                    cancelAuthority(userId,databaseId,dataClassId,1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void empowerDataBatch(List<DatabaseSpecialReadWriteDto> databaseSpecialReadWriteDtoList) {
        try {
            for(DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto : databaseSpecialReadWriteDtoList){
                String sdbId = databaseSpecialReadWriteDto.getSdbId();//专题库ID
                String dataClassId = databaseSpecialReadWriteDto.getDataClassId();//存储编码
                String databaseId = databaseSpecialReadWriteDto.getDatabaseId();//数据库ID
                Integer applyAuthority = databaseSpecialReadWriteDto.getApplyAuthority();//申请权限
                Integer examineStatus = databaseSpecialReadWriteDto.getExamineStatus();//授权状态
                String userId = databaseSpecialReadWriteDto.getUserId();//用户ID
                List<DataTableEntity> dataTableList = dataTableDao.findByDataServiceIdAndClassLogicId(dataClassId,databaseId);

                if(!databaseId.equals("RADB")){
                    if(databaseSpecialReadWriteDto.getExamineStatus()==1){//授权
                        empowerAuthority(userId,databaseId,dataClassId,applyAuthority);
                    }else if(databaseSpecialReadWriteDto.getExamineStatus()==2){//撤销权限
                        cancelAuthority(userId,databaseId,dataClassId,1);
                    }
                }
                //更新数据库状态
                databaseSpecialReadWriteDao.save(databaseSpecialReadWriteMapper.toEntity(databaseSpecialReadWriteDto));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Ztree> getTreeBySdbId(String sdbId) {

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
        //List<DminSpecialDbTree> dataList = dminSpecialDbTreeDao.getRecordByTdbId(tdbId);

        return null;
    }

    /**
     * 授权
     */
    private void empowerAuthority(String userId,String databaseId,String dataClassId,Integer applyAuthority){
        DatabaseDcl databaseVO = null;
        try {
            //判断用户是否申请过数据库账户
            List<DatabaseUserEntity> databaseUserEntityList = databaseUserDao.findByUserId(userId);
            if(databaseUserEntityList!=null&&databaseUserEntityList.size()>0){
                DatabaseUserEntity databaseUserEntity = databaseUserEntityList.get(0);
                //up账户IP
                String databaseUPIp = null;
                if(StringUtils.isNotBlank(databaseUserEntity.getDatabaseUpIp())){
                    databaseUPIp = databaseUserEntity.getDatabaseUpIp();
                }else if(StringUtils.isNotBlank(databaseUserEntity.getDatabaseUpIpSegment())){
                    databaseUPIp = databaseUserEntity.getDatabaseUpIpSegment();
                }
                //获取用户可用的物理库ID
                String[] databaseIdArray = databaseUserEntity.getDatabaseUpId().split(",");
                //获取申请ID对应的物理库
                DatabaseEntity databaseEntity = databaseDao.findById(databaseId).get();
                String databaseDefineId = databaseEntity.getDatabaseDefine().getId();
                String[] xuguDatabaseArray = {"HADB"};
                String[] gbaseDatabaseArray = {"STDB","BFDB","FIDB"};
                //判断待授权物理库是否是用户可用
                if(Arrays.asList(databaseIdArray).contains(databaseDefineId) &&
                        (Arrays.asList(xuguDatabaseArray).contains(databaseDefineId)
                        || Arrays.asList(gbaseDatabaseArray).contains(databaseDefineId))){
                    DatabaseDefineEntity databaseDefineEntity = databaseEntity.getDatabaseDefine();
                    Set<DatabaseAdministratorEntity> databaseAdministratorSet = databaseDefineEntity.getDatabaseAdministratorList();
                    //访问路径、账号、密码
                    String url = databaseDefineEntity.getDatabaseUrl();
                    if(databaseAdministratorSet!=null) {
                        //获取任意登录账号
                        DatabaseAdministratorEntity databaseAdministratorEntity = databaseAdministratorSet.iterator().next();
                        String username = databaseAdministratorEntity.getUserName();
                        String password = databaseAdministratorEntity.getPassWord();

                        //虚谷
                        if(Arrays.asList(xuguDatabaseArray).contains(databaseDefineId)){
                            databaseVO = new Xugu(url,username,password);
                        }else if(Arrays.asList(gbaseDatabaseArray).contains(databaseDefineId)){//南大
                            databaseVO = new Gbase8a(url,username,password);
                        }
                        List<DataTableEntity> dataTableList = dataTableDao.findByDataServiceIdAndClassLogicId(dataClassId,databaseId);
                        for(DataTableEntity dataTableEntity : dataTableList){
                            String tableName = dataTableEntity.getTableName();
                            //默认读权限
                            boolean select = true;
                            if(applyAuthority==2){//读写权限
                                select = false;
                            }
                            databaseVO.addPermissions(select,databaseEntity.getSchemaName(),
                                     tableName,databaseUserEntity.getDatabaseUpId(),null,null);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            databaseVO.closeConnect();
        }
    }

    /**
     * 撤销权限
     */
    private void cancelAuthority(String userId,String databaseId,String dataClassId,Integer mark){
        DatabaseDcl databaseVO = null;
        try {
            //判断用户是否申请过数据库账户
            List<DatabaseUserEntity> databaseUserEntityList = databaseUserDao.findByUserId(userId);
            if(databaseUserEntityList!=null&&databaseUserEntityList.size()>0){
                DatabaseUserEntity databaseUserEntity = databaseUserEntityList.get(0);
                //up账户IP
                String databaseUPIp = null;
                if(StringUtils.isNotBlank(databaseUserEntity.getDatabaseUpIp())){
                    databaseUPIp = databaseUserEntity.getDatabaseUpIp();
                }else if(StringUtils.isNotBlank(databaseUserEntity.getDatabaseUpIpSegment())){
                    databaseUPIp = databaseUserEntity.getDatabaseUpIpSegment();
                }
                //获取用户可用的物理库ID
                String[] databaseIdArray = databaseUserEntity.getDatabaseUpId().split(",");
                //获取申请ID对应的物理库
                DatabaseEntity databaseEntity = databaseDao.findById(databaseId).get();
                String databaseDefineId = databaseEntity.getDatabaseDefine().getId();
                String[] xuguDatabaseArray = {"HADB"};
                String[] gbaseDatabaseArray = {"STDB","BFDB","FIDB"};
                //判断待授权物理库是否是用户可用
                if(Arrays.asList(databaseIdArray).contains(databaseDefineId) &&
                        (Arrays.asList(xuguDatabaseArray).contains(databaseDefineId)
                                || Arrays.asList(gbaseDatabaseArray).contains(databaseDefineId))){
                    DatabaseDefineEntity databaseDefineEntity = databaseEntity.getDatabaseDefine();
                    Set<DatabaseAdministratorEntity> databaseAdministratorSet = databaseDefineEntity.getDatabaseAdministratorList();
                    //访问路径、账号、密码
                    String url = databaseDefineEntity.getDatabaseUrl();
                    if(databaseAdministratorSet!=null) {
                        //获取任意登录账号
                        DatabaseAdministratorEntity databaseAdministratorEntity = databaseAdministratorSet.iterator().next();
                        String username = databaseAdministratorEntity.getUserName();
                        String password = databaseAdministratorEntity.getPassWord();

                        //虚谷
                        if(Arrays.asList(xuguDatabaseArray).contains(databaseDefineId)){
                            databaseVO = new Xugu(url,username,password);
                        }else if(Arrays.asList(gbaseDatabaseArray).contains(databaseDefineId)){//南大
                            databaseVO = new Gbase8a(url,username,password);
                        }
                        List<DataTableEntity> dataTableList = dataTableDao.findByDataServiceIdAndClassLogicId(dataClassId,databaseId);
                        String[] permissions = {"SELECT","UPDATE","INSERT","DELETE"};
                        for(DataTableEntity dataTableEntity : dataTableList){
                            String tableName = dataTableEntity.getTableName();
                            databaseVO.deletePermissions(permissions,databaseEntity.getSchemaName(),
                                    dataTableEntity.getTableName(),databaseUserEntity.getDatabaseUpId(),null,null);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            databaseVO.closeConnect();
        }
    }


}
