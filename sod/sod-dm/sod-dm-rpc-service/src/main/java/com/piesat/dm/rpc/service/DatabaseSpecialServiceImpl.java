package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.entity.UUIDEntity;
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
import com.piesat.dm.rpc.mapper.DatabaseMapper;
import com.piesat.dm.rpc.mapper.DatabaseSpecialAuthorityMapper;
import com.piesat.dm.rpc.mapper.DatabaseSpecialMapper;
import com.piesat.dm.rpc.mapper.DatabaseUserMapper;
import org.bouncycastle.asn1.dvcs.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.plugin.util.UIUtil;

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
}
