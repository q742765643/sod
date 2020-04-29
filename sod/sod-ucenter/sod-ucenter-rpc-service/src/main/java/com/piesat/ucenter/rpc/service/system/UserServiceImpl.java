package com.piesat.ucenter.rpc.service.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.AESUtil;
import com.piesat.common.utils.DateUtils;
import com.piesat.common.utils.StringUtils;
import com.piesat.common.utils.poi.ExcelUtil;
import com.piesat.dm.entity.dataclass.LogicDefineEntity;
import com.piesat.dm.rpc.api.dataapply.DataAuthorityApplyService;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityApplyDto;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityRecordDto;
import com.piesat.dm.rpc.dto.dataclass.LogicDefineDto;
import com.piesat.ucenter.dao.system.UserDao;
import com.piesat.ucenter.dao.system.UserRoleDao;
import com.piesat.ucenter.entity.system.BizUserEntity;
import com.piesat.ucenter.entity.system.RoleEntity;
import com.piesat.ucenter.entity.system.UserEntity;
import com.piesat.ucenter.entity.system.UserRoleEntity;
import com.piesat.ucenter.mapper.system.RoleMapper;
import com.piesat.ucenter.mapper.system.UserMapper;
import com.piesat.ucenter.rpc.api.system.UserService;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.ucenter.rpc.mapstruct.system.UserMapstruct;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/21 17:13
 */
@Service
public class UserServiceImpl extends BaseService<UserEntity> implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserMapstruct userMapstruct;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private RoleMapper roleMapper;
    @GrpcHthtClient
    private DataAuthorityApplyService dataAuthorityApplyService;

    @Override
    public BaseDao<UserEntity> getBaseDao() {
        return userDao;
    }

    @Override
    public UserDto saveUserDto(UserDto userDto) {
        UserEntity userEntity = userMapstruct.toEntity(userDto);
        userEntity = this.saveNotNull(userEntity);
        return userMapstruct.toDto(userEntity);

    }

    /**
     * @描述 根据用户名查找用户
     * @参数 [userName]
     * @返回值 com.piesat.ucenter.rpc.dto.system.UserDto
     * @author zzj
     * @创建时间 2019/11/28 16:38
     **/
    @Override
    public UserDto selectUserByUserName(String userName) {
        UserEntity userEntity = userDao.findByUserName(userName);
        return userMapstruct.toDto(userEntity);
    }

    /**
     * @描述 根据appId查找用户
     * @参数 [appId]
     * @返回值 com.piesat.ucenter.rpc.dto.system.UserDto
     * @author zzj
     * @创建时间 2019/11/28 16:38
     **/
    @Override
    public UserDto selectUserByAppId(String appId) {
        UserEntity userEntity = userDao.findByAppId(appId);
        return userMapstruct.toDto(userEntity);
    }

    /**
     * 根据条件分页查询用户列表
     *
     * @param
     * @return 用户信息集合信息
     */
    @Override
    public PageBean selectUserList(PageForm<UserDto> pageForm) {
        UserEntity userEntity = userMapstruct.toEntity(pageForm.getT());
        PageHelper.startPage(pageForm.getCurrentPage(), pageForm.getPageSize());
        List<UserEntity> userEntities = userMapper.selectUserList(userEntity);
        PageInfo<UserEntity> pageInfo = new PageInfo<>(userEntities);
        List<UserDto> userDtos = userMapstruct.toDto(pageInfo.getList());
        PageBean pageBean = new PageBean(pageInfo.getTotal(), pageInfo.getPages(), userDtos);
        return pageBean;
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public void insertUser(UserDto user) {
        UserEntity userEntity = userMapstruct.toEntity(user);
        String password = new Md5Hash(userEntity.getPassword(), user.getUserName(), 2).toString();
        userEntity.setPassword(password);
        userEntity = this.saveNotNull(userEntity);
        // 新增用户岗位关联
        //insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(userEntity);
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(UserEntity user) {
        String[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles)) {
            // 新增用户与角色管理
            List<UserRoleEntity> list = new ArrayList<>();
            for (String roleId : roles) {
                UserRoleEntity ur = new UserRoleEntity();
                ur.setUserId(user.getId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleDao.saveNotNullAll(list);
            }
        }
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public void updateUser(UserDto user) {
        UserEntity userEntity = userMapstruct.toEntity(user);
        userEntity = this.saveNotNull(userEntity);
        String userId = user.getId();
        // 删除用户与角色关联
        userRoleDao.deleteByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(userEntity);
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public void updateUserStatus(UserDto user) {
        UserEntity userEntity = userMapstruct.toEntity(user);
        userEntity.setStatus(user.getStatus());
        this.saveNotNull(userEntity);
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public UserDto updateProfile(UserDto user) {
        UserEntity userEntity = userMapstruct.toEntity(user);
        return userMapstruct.toDto(this.saveNotNull(userEntity));
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    public void deleteUserByIds(List<String> userIds) {
        for (String userId : userIds) {
            userRoleDao.deleteByUserId(userId);
        }
        this.deleteByIds(userIds);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public UserDto selectUserById(String userId) {
        UserEntity userEntity = this.getById(userId);
        List<String> roles = roleMapper.selectRoleListByUserId(userId);
        userEntity.setRoleIds(roles.toArray(new String[roles.size()]));
        return userMapstruct.toDto(userEntity);
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<RoleEntity> list = roleMapper.selectRolesByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (RoleEntity role : list) {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    @Override
    public void exportExcel(UserDto userDto) {
        UserEntity userEntity = userMapstruct.toEntity(userDto);
        List<UserEntity> entities = userMapper.selectUserList(userEntity);
        ExcelUtil<UserEntity> util = new ExcelUtil(UserEntity.class);
        util.exportExcel(entities, "用户");
    }

    @Override
    public ResultT addBizUser(Map<String, String[]> parameterMap, String applyPaper) {

        Map<String, String> map = new LinkedHashMap<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (entry.getValue().length > 0) {
                map.put(entry.getKey(), entry.getValue()[0]);
            }
        }
        String appNames = map.get("appName");
        String bizUserid = map.get("bizUserid");
        String applyAuthority = map.get("applyAuthority");
        JSONObject jsonObject = JSONObject.parseObject(applyAuthority);
        JSONObject sod = jsonObject.getJSONObject("sod");
        String sodApi = sod.getString("sodApi");
        String sodApp = sod.getString("sodApp");
        JSONObject sodData = sod.getJSONObject("sodData");
        String dbIds = sodData.get("dbIds").toString();
        String dbCreate = sodData.get("dbCreate").toString();
        JSONArray applyData = sodData.getJSONArray("applyData");
        DataAuthorityApplyDto daa = new DataAuthorityApplyDto();
        daa.setUserId(bizUserid);
        daa.setCreateTime(new Date());
        List<DataAuthorityRecordDto> list = new ArrayList<>();
        for (int i = 0; i < applyData.size(); i++) {
            DataAuthorityRecordDto dar = new DataAuthorityRecordDto();
            JSONObject jsonObject1 = applyData.getJSONObject(i);
            String databaseId = jsonObject1.getString("databaseId");
            String dataClassId = jsonObject1.getString("dataClassId");
            dar.setDatabaseId(databaseId);
            dar.setDataClassId(dataClassId);
            dar.setApplyAuthority(1);
            dar.setCreateTime(new Date());
            list.add(dar);
        }
        daa.setDataAuthorityRecordList(list);
        this.dataAuthorityApplyService.saveDto(daa);

        UserEntity byBizUserId = this.userDao.findByUserName(bizUserid);
        if (byBizUserId != null) {
            return ResultT.failed("业务用户注册id已存在！");
        }
        String password = map.get("password");
        try {
            password = AESUtil.aesEncrypt(password).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String bizType = map.get("bizType");
        String bizIp = map.get("bizIp");
        String validTime = map.get("validTime");
        String remark = map.get("remark");
        String webUsername = map.get("webUsername");
        String legalUnits = map.get("legalUnits");
        String deptName = map.get("deptName");
        String phone = map.get("phone");
        String tutorName = map.get("tutorName");
        String tutorPhone = map.get("tutorPhone");
        String webUserid = map.get("webUserid");
//        String appCoin = map.get("appCoin");
//        String applyAuthority = map.get("applyAuthority");
//        String deptId = map.get("deptId");
        String loginName = map.get("loginName");
//        String userId = map.get("userId");
//        String interfaceId = map.get("interfaceId");
//        String nonce = map.get("nonce");
//        String timestamp = map.get("timestamp");
        UserEntity UserEntity = new UserEntity();
        UserEntity.setUserType("11");
        UserEntity.setNickName(webUsername);
        UserEntity.setApplyPaper(applyPaper);
        UserEntity.setApplyTime(new Date());
        UserEntity.setAppName(appNames);
        UserEntity.setBizIp(bizIp);
        UserEntity.setBizType(bizType);
        UserEntity.setUserName(bizUserid);
        UserEntity.setChecked("0");
        UserEntity.setDeptName(deptName);
        UserEntity.setLastEditTime(new Date());
        UserEntity.setLegalUnits(legalUnits);
        UserEntity.setPassword(password);
        UserEntity.setPhonenumber(phone);
        UserEntity.setRemark(remark);
        UserEntity.setTutorName(tutorName);
        UserEntity.setTutorPhone(tutorPhone);
        Date date = DateUtils.dateTime("yyyy-MM-dd", validTime);
        UserEntity.setValidTime(date);
        UserEntity.setWebUserId(webUserid);
        UserEntity.setWebUsername(webUsername);
        UserEntity.setDbIds(dbIds);
        UserEntity.setSodApi(sodApi);
        UserEntity.setDbCreate(dbCreate);
        UserEntity.setStatus("1".equals(sodApp) ? "0" : "1");
        UserEntity userEntity = this.userDao.saveNotNull(UserEntity);
        return ResultT.success(userEntity);
    }

    @Override
    public PageBean findAllBizUser(PageForm<UserDto> pageForm) {
        UserEntity userEntity=this.userMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(userEntity.getUserName())){
            specificationBuilder.add("userName", SpecificationOperator.Operator.likeAll.name(),userEntity.getUserName());
        }
        if(StringUtils.isNotNullString(userEntity.getNickName())){
            specificationBuilder.add("nickName", SpecificationOperator.Operator.likeAll.name(),userEntity.getNickName());
        }
        if(StringUtils.isNotNullString(userEntity.getChecked())){
            specificationBuilder.add("checked", SpecificationOperator.Operator.eq.name(),userEntity.getChecked());
        }
        if(StringUtils.isNotNullString(userEntity.getDeptName())){
            specificationBuilder.add("deptName", SpecificationOperator.Operator.likeAll.name(),userEntity.getDeptName());
        }
        Sort sort=Sort.by(Sort.Direction.DESC,"createTime");
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<UserEntity> userEntitys= (List<UserEntity>) pageBean.getPageData();
        List<UserDto> userDtos = this.userMapstruct.toDto(userEntitys);
        pageBean.setPageData(userDtos);
        return pageBean;
    }

    @Override
    public void editBase(UserDto user) {
        UserDto userDto = this.selectUserByUserName(user.getUserName());
        user.setId(userDto.getId());
        this.saveNotNull(this.userMapstruct.toEntity(user));
    }

    @Override
    public List<UserDto> findByUserType(String userType) {
        List<UserEntity> userEntities = this.userDao.findByUserType(userType);
        return userMapstruct.toDto(userEntities);
    }
}
