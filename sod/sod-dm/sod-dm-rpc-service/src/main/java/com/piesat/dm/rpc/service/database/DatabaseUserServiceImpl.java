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

import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
}
