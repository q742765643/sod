package com.piesat.dm.rpc.service.dataapply;

import com.piesat.common.config.DatabseType;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.core.api.DatabaseDcl;
import com.piesat.dm.core.api.impl.Gbase8a;
import com.piesat.dm.core.api.impl.Xugu;
import com.piesat.dm.core.parser.DatabaseInfo;
import com.piesat.dm.dao.ReadAuthorityDao;
import com.piesat.dm.dao.dataapply.DataAuthorityApplyDao;
import com.piesat.dm.dao.dataapply.DataAuthorityRecordDao;
import com.piesat.dm.dao.datatable.DataTableDao;
import com.piesat.dm.entity.ReadAuthorityEntity;
import com.piesat.dm.entity.dataapply.DataAuthorityApplyEntity;
import com.piesat.dm.entity.dataapply.DataAuthorityRecordEntity;
import com.piesat.dm.entity.datatable.DataTableEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.dataapply.DataAuthorityApplyService;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.api.database.DatabaseUserService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.api.special.DatabaseSpecialReadWriteService;
import com.piesat.dm.rpc.api.special.DatabaseSpecialService;
import com.piesat.dm.rpc.dto.ReadAuthorityDto;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityApplyDto;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityRecordDto;
import com.piesat.dm.rpc.dto.database.DatabaseAdministratorDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.database.DatabaseUserDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialReadWriteDto;
import com.piesat.dm.rpc.mapper.ReadAuthorityMapper;
import com.piesat.dm.rpc.mapper.dataapply.DataAuthorityApplyMapper;
import com.piesat.dm.rpc.mapper.dataapply.DataAuthorityRecordMapper;
import com.piesat.dm.rpc.util.DatabaseUtil;
import com.piesat.ucenter.dao.system.UserDao;
import com.piesat.ucenter.entity.system.UserEntity;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.CmadaasApiUtil;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.hibernate.dialect.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/10 13:01
 */
@Service
public class DataAuthorityApplyServiceImpl extends BaseService<DataAuthorityApplyEntity> implements DataAuthorityApplyService {

    @Autowired
    private DataAuthorityApplyDao dataAuthorityApplyDao;
    @Autowired
    private DataAuthorityApplyMapper dataAuthorityApplyMapper;
    @Autowired
    private DataAuthorityRecordMapper dataAuthorityRecordMapper;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private DataAuthorityRecordDao dataAuthorityRecordDao;
    @Autowired
    private DatabaseSpecialService databaseSpecialService;
    @Autowired
    private DatabaseSpecialReadWriteService databaseSpecialReadWriteService;
    @Autowired
    private DatabaseUserService databaseUserService;
    @Autowired
    private DataTableService dataTableService;
    @Autowired
    private DataClassService dataClassService;
    @Autowired
    private DataTableDao dataTableDao;
    @Autowired
    private ReadAuthorityDao readAuthorityDao;
    @Autowired
    private ReadAuthorityMapper readAuthorityMapper;
    @Autowired
    private DatabaseInfo databaseInfo;
    @GrpcHthtClient
    private UserDao userDao;
    @Override
    public BaseDao<DataAuthorityApplyEntity> getBaseDao() {
        return this.dataAuthorityApplyDao;
    }

    @Override
    public PageBean selectPageList(PageForm<DataAuthorityApplyDto> pageForm) {
        UserDto loginUser =(UserDto) SecurityUtils.getSubject().getPrincipal();
        DataAuthorityApplyEntity dataAuthorityApplyEntity = dataAuthorityApplyMapper.toEntity(pageForm.getT());

        DataAuthorityApplyDto dataAuthorityApplyDto = pageForm.getT();
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(dataAuthorityApplyDto.getAuditStatus())){
            specificationBuilder.add("auditStatus", SpecificationOperator.Operator.eq.name(),dataAuthorityApplyDto.getAuditStatus());
        }
        if(StringUtils.isNotNullString(dataAuthorityApplyDto.getUserName())){
            //调用接口 根据用户名查询用户id
            List<String> userId= new ArrayList<String>();
            userId.add("noUseId");
            List<UserEntity> userEntities = userDao.findByWebUsernameLike("%"+pageForm.getT().getUserName()+"%");
            if(userEntities != null && userEntities.size()>0){
                for(UserEntity userEntity : userEntities){
                    userId.add(userEntity.getUserName());
                }
            }
            specificationBuilder.add("userId", SpecificationOperator.Operator.in.name(),userId);
        }
        if(StringUtils.isNotNullString((String) dataAuthorityApplyEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) dataAuthorityApplyEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) dataAuthorityApplyEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) dataAuthorityApplyEntity.getParamt().get("endTime"));
        }
        List<String> allApplyId = this.dataAuthorityRecordDao.findAllApplyId();
        if(allApplyId==null||allApplyId.size()==0){
            return null;
        }
        specificationBuilder.add("id", SpecificationOperator.Operator.in.name(),allApplyId);
        Sort sort = Sort.by(Sort.Direction.DESC,"auditStatus","createTime");
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<DataAuthorityApplyEntity> dataAuthorityApplyEntities = (List<DataAuthorityApplyEntity>) pageBean.getPageData();

        //调用接口获取所有的用户信息

        List<DataAuthorityApplyDto> dataAuthorityApplyDtos = dataAuthorityApplyMapper.toDto(dataAuthorityApplyEntities);

        //调用接口获取所有的用户信息
        List<UserEntity> userEntities = userDao.findByUserType("11");
        //循环遍历
        for(DataAuthorityApplyDto authorityApply : dataAuthorityApplyDtos){

            //若所申请资料全部审核了，将这条申请记录修改为“已审核”
            List<DataAuthorityRecordDto> dataAuthorityRecordList = authorityApply.getDataAuthorityRecordList();
            int num = 0;
            if(StringUtils.isNotNullString(authorityApply.getAuditStatus()) && !"02".equals(authorityApply.getAuditStatus())){
                for(DataAuthorityRecordDto authorityRecord:dataAuthorityRecordList){
                    if(authorityRecord.getAuthorize() != null){
                        num++;
                        if (num == dataAuthorityRecordList.size()) {
                            //authorityApply.setAuditStatus("02");
                            //authorityApply = this.saveNotNull(authorityApply);
                            this.mybatisQueryMapper.updateDataAuthorityApply(authorityApply.getId(),"02",loginUser.getUserName(),new Date());
                        }
                    }
                }
            }

            //遍历所有用户信息找到每条记录对应的用户信息
            for(UserEntity userEntity:userEntities){
                if(userEntity.getUserName().equals(authorityApply.getUserId())){
                    authorityApply.setUserName(userEntity.getWebUsername());
                    authorityApply.setTelephone(userEntity.getPhonenumber());
                    authorityApply.setDepartment(userEntity.getDeptName());
                }
            }
        }

        pageBean.setPageData(dataAuthorityApplyDtos);
        return pageBean;
    }

    @Override
    @Transactional
    public DataAuthorityApplyDto saveDto(DataAuthorityApplyDto dataAuthorityApplyDto) {
        DataAuthorityApplyEntity dataAuthorityApplyEntity = dataAuthorityApplyMapper.toEntity(dataAuthorityApplyDto);
        //新增申请设置成待审
        dataAuthorityApplyEntity.setAuditStatus("01");

        //保存
        dataAuthorityApplyEntity = this.saveNotNull(dataAuthorityApplyEntity);

        //读申请是否自动授权
        List<ReadAuthorityEntity> readAuthorityEntities = readAuthorityDao.findAll();
        if(readAuthorityEntities != null && readAuthorityEntities.size()>0){
            //读申请自动授权
            if("1".equals(readAuthorityEntities.get(0).getValue())){
                Set<DataAuthorityRecordEntity> dataAuthorityRecordList = dataAuthorityApplyEntity.getDataAuthorityRecordList();
                if(dataAuthorityRecordList != null && dataAuthorityRecordList.size()>0){
                    for(DataAuthorityRecordEntity dataAuthorityRecordEntity : dataAuthorityRecordList){
                        if(dataAuthorityRecordEntity.getApplyAuthority().intValue() == 1){
                            DataAuthorityRecordDto dataAuthorityRecordDto = dataAuthorityRecordMapper.toDto(dataAuthorityRecordEntity);
                            //物理库授权
                            updateOneRecordCheck(dataAuthorityApplyEntity.getUserId(),dataAuthorityRecordDto);
                        }
                    }
                }
            }
        }
        return dataAuthorityApplyMapper.toDto(dataAuthorityApplyEntity);
    }

    @Override
    public Map<String, Object> getObjectById(String id) {
        Map<String, Object> objectMap = mybatisQueryMapper.queryDataAuthorityApplyById(id);
        if(objectMap != null){
            if(objectMap.get("USER_ID") != null){
                //调接口查申请人详情
                UserEntity userEntity = userDao.findByUserName((String) objectMap.get("USER_ID"));
                if(userEntity != null){
                    objectMap.put("USERREALNAME", userEntity.getWebUsername());
                    objectMap.put("USERPHONE", userEntity.getTutorPhone());
                    objectMap.put("DEPARTMENT", userEntity.getDeptName());
                }
            }
        }
        return objectMap;
    }

    @Override
    public List<Map<String,Object>> getRecordByApplyId(Map<String,String> map) {
        if("mysql".equals(DatabseType.type.toLowerCase())){
            return mybatisQueryMapper.getRecordByApplyIdMysql(map);
        }else{
            return mybatisQueryMapper.getRecordByApplyId(map);
        }
    }

    @Override
    @Transactional
    public ResultT updateRecordCheck(DataAuthorityApplyDto dataAuthorityApplyDto) {
        List<DataAuthorityRecordDto> dataAuthorityRecordList = dataAuthorityApplyDto.getDataAuthorityRecordList();

        //获取用户up账户
        DatabaseUserDto databaseUserDto = databaseUserService.findByUserIdAndExamineStatus(dataAuthorityApplyDto.getUserId(), "1");
        if(databaseUserDto == null){
            return ResultT.failed("授权失败,未找到用户对应数据库账户信息");
        }

        //用户up账户对应的可用物理库
        String examineDatabaseId = databaseUserDto.getExamineDatabaseId();
        if(!StringUtils.isNotNullString(examineDatabaseId)){
            return ResultT.failed("授权失败,用户对应数据库账户未审核");
        }
        List<String> databaseIds = Arrays.asList(examineDatabaseId.split(","));


        StringBuffer buffer = new StringBuffer();
        boolean flag = true;
        //为每个数据表进行授权
        for(DataAuthorityRecordDto dataAuthorityRecordDto:dataAuthorityRecordList){
            DatabaseDto databaseDto = databaseService.getDotById(dataAuthorityRecordDto.getDatabaseId());

            if(!databaseIds.contains(databaseDto.getDatabaseDefine().getId())){
                buffer.append("不具备对物理库：" + databaseDto.getDatabaseDefine().getDatabaseName()+"_"+databaseDto.getDatabaseName()+"的访问权限" + "<br/>");
                flag = false;
                continue;
            }

            //1 根据编码获取表(可能有多个表，需要遍历对每个表授权)，前端传来多个相同编码记录时不要重复操作   2 前端传表
            //获取资料对应的表信息
            //List<DataTableDto> dataTableDtos = dataTableService.getByDatabaseIdAndClassId(dataAuthorityRecordDto.getDatabaseId(), dataAuthorityRecordDto.getDataClassId());


            DatabaseDcl databaseDcl = null;
            try {
                databaseDcl = DatabaseUtil.getDatabase(databaseDto, databaseInfo);
            }catch (Exception e){
                if (e.getMessage().contains("用户不存在")){
                    buffer.append("物理库：" + databaseDto.getDatabaseDefine().getDatabaseName()+"_"+databaseDto.getDatabaseName()+"没有管理员账户" + "<br/>");
                    flag = false;
                    continue;
                }
            }
            if(!StringUtils.isNotNullString(dataAuthorityRecordDto.getTableName())){
                buffer.append("表名为空，"+"授权失败"+"<br/>");
                flag = false;
                continue;
            }
            try{
                if(dataAuthorityRecordDto.getApplyAuthority()==1){//授权读
                    databaseDcl.addPermissions(true,databaseDto.getSchemaName(),dataAuthorityRecordDto.getTableName(),databaseUserDto.getDatabaseUpId(),"",null);
                }else if(dataAuthorityRecordDto.getApplyAuthority()==2){//授权写
                    databaseDcl.addPermissions(false,databaseDto.getSchemaName(),dataAuthorityRecordDto.getTableName(),databaseUserDto.getDatabaseUpId(),"",null);
                }
                Optional.ofNullable(databaseDcl).ifPresent(DatabaseDcl::closeConnect);
            }catch (Exception e){
                buffer.append("表"+dataAuthorityRecordDto.getTableName()+"授权失败"+e.getMessage()+"<br/>");
                flag = false;
                continue;
            }finally {
                if (databaseDcl!=null){
                    databaseDcl.closeConnect();
                }
            }


            mybatisQueryMapper.updateDataAuthorityRecord(dataAuthorityRecordDto.getId(),dataAuthorityRecordDto.getAuthorize(),dataAuthorityRecordDto.getCause());
        }
        if(flag){
            return  ResultT.success("授权成功");
        }else {
            return ResultT.failed(buffer.toString());
        }
    }

    @Override
    @Transactional
    public ResultT updateOneRecordCheck(String userId,DataAuthorityRecordDto dataAuthorityRecordDto) {

        //获取用户up账户
        DatabaseUserDto databaseUserDto = databaseUserService.findByUserIdAndExamineStatus(userId, "1");
        if(databaseUserDto == null){
            return ResultT.failed("授权失败,未找到用户对应数据库账户信息");
        }

        //用户up账户对应的可用物理库
        List<String> databaseIds = Arrays.asList(databaseUserDto.getExamineDatabaseId().split(","));

        DatabaseDto databaseDto = databaseService.getDotById(dataAuthorityRecordDto.getDatabaseId());

        if(!databaseIds.contains(databaseDto.getDatabaseDefine().getId())){
            return ResultT.failed("不具备对物理库：" + databaseDto.getDatabaseDefine().getDatabaseName()+"_"+databaseDto.getDatabaseName()+"的访问权限" + "<br/>");
        }

        //1 根据编码获取表(可能有多个表，需要遍历对每个表授权)，前端传来多个相同编码记录时不要重复操作   2 前端传表
        //获取资料对应的表信息
        //List<DataTableDto> dataTableDtos = dataTableService.getByDatabaseIdAndClassId(dataAuthorityRecordDto.getDatabaseId(), dataAuthorityRecordDto.getDataClassId());

        DatabaseDcl databaseDcl = null;
        try {
            databaseDcl = DatabaseUtil.getDatabase(databaseDto, databaseInfo);
        }catch (Exception e){
            if (e.getMessage().contains("用户不存在")){
                Optional.ofNullable(databaseDcl).ifPresent(DatabaseDcl::closeConnect);
                return ResultT.failed("物理库：" + databaseDto.getDatabaseDefine().getDatabaseName()+"_"+databaseDto.getDatabaseName()+"没有管理员账户" + "<br/>");
            }
        }

        try{
            if(dataAuthorityRecordDto.getApplyAuthority()==1){//授权读
                databaseDcl.addPermissions(true,databaseDto.getSchemaName(),dataAuthorityRecordDto.getTableName(),databaseUserDto.getDatabaseUpId(),"",null);
            }else if(dataAuthorityRecordDto.getApplyAuthority()==2){//授权写
                databaseDcl.addPermissions(false,databaseDto.getSchemaName(),dataAuthorityRecordDto.getTableName(),databaseUserDto.getDatabaseUpId(),"",null);
            }
            dataAuthorityRecordDto.setAuthorize(1);
        }catch (Exception e){
            Optional.ofNullable(databaseDcl).ifPresent(DatabaseDcl::closeConnect);
            return ResultT.failed("表"+dataAuthorityRecordDto.getTableName()+"授权失败"+e.getMessage()+"<br/>");
        }
        Optional.ofNullable(databaseDcl).ifPresent(DatabaseDcl::closeConnect);
        mybatisQueryMapper.updateDataAuthorityRecord(dataAuthorityRecordDto.getId(),dataAuthorityRecordDto.getAuthorize(),dataAuthorityRecordDto.getCause());
        return ResultT.success("授权成功");
    }

    @Override
    public ResultT updateRecordCheckCancel(DataAuthorityApplyDto dataAuthorityApplyDto) {
        List<DataAuthorityRecordDto> dataAuthorityRecordList = dataAuthorityApplyDto.getDataAuthorityRecordList();

        //获取用户up账户
        DatabaseUserDto databaseUserDto = databaseUserService.findByUserIdAndExamineStatus(dataAuthorityApplyDto.getUserId(), "1");
        if(databaseUserDto == null){
            return ResultT.failed("授权失败,未找到用户对应数据库账户信息");
        }

        StringBuffer buffer = new StringBuffer();
        boolean flag = true;
        //循环
        for(DataAuthorityRecordDto dataAuthorityRecordDto:dataAuthorityRecordList){

            if(dataAuthorityRecordDto.getAuthorize() != null && dataAuthorityRecordDto.getAuthorize().intValue() == 1){//已授权资料，撤销授权
                //获取物理库信息
                DatabaseDto databaseDto = databaseService.getDotById(dataAuthorityRecordDto.getDatabaseId());
                DatabaseDcl databaseDcl = null;
                try {
                    databaseDcl = DatabaseUtil.getDatabase(databaseDto, databaseInfo);
                }catch (Exception e){
                    if (e.getMessage().contains("用户不存在")){
                        buffer.append("物理库：" + databaseDto.getDatabaseDefine().getDatabaseName()+"_"+databaseDto.getDatabaseName()+"没有管理员账户" + "<br/>");
                        flag = false;
                        Optional.ofNullable(databaseDcl).ifPresent(DatabaseDcl::closeConnect);
                        continue;
                    }
                }
                try{
                    if(dataAuthorityRecordDto.getApplyAuthority()==1){//撤销读权限
                        databaseDcl.deletePermissions("SELECT".split(","),databaseDto.getSchemaName(),dataAuthorityRecordDto.getTableName(),databaseUserDto.getDatabaseUpId(),"",null);
                    }else if(dataAuthorityRecordDto.getApplyAuthority()==2){//撤销写权限
                        databaseDcl.deletePermissions("SELECT,UPDATE,INSERT,DELETE".split(","),databaseDto.getSchemaName(),dataAuthorityRecordDto.getTableName(),databaseUserDto.getDatabaseUpId(),"",null);
                    }
                }catch (Exception e){
                    buffer.append("表"+dataAuthorityRecordDto.getTableName()+"授权失败"+e.getMessage()+"<br/>");
                    flag = false;
                    Optional.ofNullable(databaseDcl).ifPresent(DatabaseDcl::closeConnect);
                    continue;
                }
                Optional.ofNullable(databaseDcl).ifPresent(DatabaseDcl::closeConnect);
            }

            mybatisQueryMapper.updateDataAuthorityRecord(dataAuthorityRecordDto.getId(),dataAuthorityRecordDto.getAuthorize(),dataAuthorityRecordDto.getCause());
        }
        if(flag){
            return  ResultT.success("拒绝成功");
        }else {
            return ResultT.failed(buffer.toString());
        }
    }

    @Override
    public List<Map<String,Object>> getRecordListByUserId(String userId) {
        if("mysql".equals(DatabseType.type.toLowerCase())){
            return mybatisQueryMapper.getRecordListByUserIdMysql(userId);
        }else{
            return mybatisQueryMapper.getRecordListByUserId(userId);
        }
    }

    @Override
    @Transactional
    public void updateRecordByApplyIdAndClassId(String apply_id,String data_class_id,Integer authorize, String cause) {
        List<DataAuthorityRecordEntity> dataAuthorityRecordEntities = dataAuthorityRecordDao.findByApplyIdAndDataClassId(apply_id, data_class_id);

        if(dataAuthorityRecordEntities != null && dataAuthorityRecordEntities.size() > 0){
            for(DataAuthorityRecordEntity dataAuthorityRecordEntity : dataAuthorityRecordEntities){
                //如果资料是专题库引用资料，审核专题库引用资料
                if(StringUtils.isNotNullString(dataAuthorityRecordEntity.getQtdbId())){
                    List<DatabaseSpecialReadWriteDto> databaseSpecialReadWriteDtos = databaseSpecialReadWriteService.findBySdbIdAndDataClassId(dataAuthorityRecordEntity.getQtdbId(), data_class_id);
                    if(databaseSpecialReadWriteDtos != null && databaseSpecialReadWriteDtos.size() > 0){
                        for(DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto : databaseSpecialReadWriteDtos){
                            if(StringUtils.isNotNullString(cause)){
                                databaseSpecialReadWriteDto.setFailureReason(cause);
                            }
                            databaseSpecialReadWriteDto.setExamineStatus(authorize);
                            databaseSpecialReadWriteService.saveDto(databaseSpecialReadWriteDto);
                        }
                    }
                }

                //资料授权审核
                mybatisQueryMapper.updateDataAuthorityRecord(dataAuthorityRecordEntity.getId(),authorize,cause);

                dataAuthorityRecordMapper.toDto(dataAuthorityRecordEntity);
                //到物理库授权
                //updateRecordCheck(dataAuthorityRecordEntity);
            }
        }
    }

    @Override
    public Map<String, Object> getAuthorDataByClassId(String dataClassId) {
        HashMap<String, Object> map = new HashMap<>();
        DataClassDto dataClassDto = dataClassService.findByDataClassId(dataClassId);
        //List<DataAuthorityRecordEntity> dataAuthorityRecordEntities = dataAuthorityRecordDao.findByDataClassIdAndAuthorize(dataClassId, 1);
        map.put("access_control", dataClassDto.getIsAccess());
        return map;
    }

    @Override
    @Transactional
    public void updateRecordByApplyIdAndClassIdAndDatabaseId(String apply_id, String data_class_id, String database_id, Integer authorize, String cause) {
        List<DataAuthorityRecordEntity> dataAuthorityRecordEntities = dataAuthorityRecordDao.findByApplyIdAndDataClassIdAndDatabaseId(apply_id, data_class_id,database_id);
        if(dataAuthorityRecordEntities != null && dataAuthorityRecordEntities.size() > 0){
            for(DataAuthorityRecordEntity dataAuthorityRecordEntity : dataAuthorityRecordEntities){
                mybatisQueryMapper.updateDataAuthorityRecord(dataAuthorityRecordEntity.getId(),authorize,cause);
            }
        }

    }
    @Override
    public Map<String, Object> getDataAuthorityList(String userId, String applyAuthority, String logicId, String dataName, String category, String schemaId) {
        Map<String,Object> result = new HashMap<String,Object>();
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("userId", userId);

        if(null == userId || "".equals(userId)) {
            result.put("returnMessage", "请登录");
            result.put("returnCode", "-1002");
            result.put("DS", null);
            return result;
        }
        paraMap.put("applyAuthority", applyAuthority);
        paraMap.put("logicId", logicId);
        paraMap.put("dataName", dataName);
        paraMap.put("category", category);
        paraMap.put("schemaId", schemaId);
        try {
            List<Map<String,Object>> daList = mybatisQueryMapper.getDataAuthorityList(paraMap);
            result.put("returnCode", "0");
            result.put("returnMessage", "查询成功");
            result.put("DS", daList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("returnCode","1");
            result.put("returnMessage", "查询失败");
        }
        return result;
    }

    @Override
    public Map<String, Object> getDataCreator(String dataClassId) {
        Map<String,Object> result = new HashMap<String,Object>();
        try {
            DataTableEntity dataTableEntity = dataTableDao.findById(dataClassId).get();
            String userId = dataTableEntity.getUserId();
            if(!"admin".equals(userId)){
                com.alibaba.fastjson.JSONObject obj = CmadaasApiUtil.getUserInfo(userId);
                result.put("DS", obj);
            }else{
                result.put("DS", "admin");
            }
            result.put("returnCode", "0");

        } catch (Exception e) {
            e.printStackTrace();
            result.put("returnCode", "1");
            result.put("returnMessage", "查询失败 : "+e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> deleteDataAuthorityById(String applyId, String dataBaseId, String dataClassId) {
        Map<String,Object> result = new HashMap<String,Object>();
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("applyId", applyId);
        paraMap.put("dataBaseId", dataBaseId);
        paraMap.put("dataClassId",dataClassId);
        //删除前查询是否存在
        try {
            mybatisQueryMapper.delDataAuthorityByApplyId(paraMap);
            mybatisQueryMapper.clearUselessApply();
            result.put("returnCode", "0");
            result.put("returnMessage", "删除成功。");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("returnCode", "1");
            result.put("returnMessage", "删除失败 : "+e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> getDataCategory() {
        Map<String,Object> result = new HashMap<String,Object>();
        try {
            List<Map<String,Object>> categoryList = mybatisQueryMapper.getDataCategory();
            result.put("returnCode", "0");
            result.put("DS", categoryList);
            result.put("returnMessage", "操作成功 。");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("returnCode", "1");
            result.put("returnMessage", "操作失败 ："+e.getMessage());
        }
        return result;
    }

    /**
     *
     * @description
     * @author wlg
     * @date 2020-04-22 17:06
     * @param userId
     * @return
     * @throws Exception
     */
	@Override
	public List<Map<String, Object>> getApplyDataInfo(String userId) throws Exception {
	    //获取userId的可用物理库
        DatabaseUserDto databaseUserDto = databaseUserService.findByUserIdAndExamineStatus(userId, "1");
        List<Map<String,Object>> result = mybatisQueryMapper.getApplyDataInfo(userId);
        if(databaseUserDto == null || !StringUtils.isNotNullString(databaseUserDto.getExamineDatabaseId())){
            return  null;
        }
        List<String> databaseIds = Arrays.asList(databaseUserDto.getExamineDatabaseId().split(","));
        for(int i=result.size()-1;i>-1;i--){
            Map<String, Object> map = result.get(i);
            if(!databaseIds.contains((String)map.get("DATABASEID"))){
                result.remove(map);
            }
        }
		return result;
	}

    @Override
    public ReadAuthorityDto updateReadAuthority(ReadAuthorityDto readAuthorityDto) {
        ReadAuthorityEntity readAuthorityEntity = readAuthorityMapper.toEntity(readAuthorityDto);
        readAuthorityEntity = readAuthorityDao.saveNotNull(readAuthorityEntity);
        return readAuthorityMapper.toDto(readAuthorityEntity);
    }

    @Override
    public List<ReadAuthorityDto> getReadAuthority() {
        List<ReadAuthorityEntity> readAuthorityEntities = readAuthorityDao.findAll();
        return readAuthorityMapper.toDto(readAuthorityEntities);
    }

    @Override
    public List<DataAuthorityApplyDto> findByUserId(String userId) {
        List<DataAuthorityApplyEntity> dalList = this.dataAuthorityApplyDao.findByUserId(userId);
       return this.dataAuthorityApplyMapper.toDto(dalList);
    }

    @Override
    public void deleteByDataClassId(String dataclassId) {
        this.dataAuthorityRecordDao.deleteByDataClassId(dataclassId);
    }

}
