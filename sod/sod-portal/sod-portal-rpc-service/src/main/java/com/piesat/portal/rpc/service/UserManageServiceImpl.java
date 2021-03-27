package com.piesat.portal.rpc.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.*;
import com.piesat.common.utils.http.HttpClientUtil;
import com.piesat.common.utils.http.HttpUtils;
import com.piesat.portal.dao.UserManageDao;
import com.piesat.portal.dao.UserRoleManageDao;
import com.piesat.portal.entity.UserManageEntity;
import com.piesat.portal.entity.UserRoleManageEntity;
import com.piesat.portal.mapper.RoleManageMapper;
import com.piesat.portal.mapper.UserManageMapper;
import com.piesat.portal.rpc.api.DepartManageService;
import com.piesat.portal.rpc.api.UserManageService;
import com.piesat.portal.rpc.dto.DepartManageDto;
import com.piesat.portal.rpc.dto.UserManageDto;
import com.piesat.portal.rpc.mapstruct.UserManageMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service("userManageService")
public class UserManageServiceImpl extends BaseService<UserManageEntity> implements UserManageService {

    @Value("${sysLevel.value:P}")
    private String sysLevel;
    @Value("${unifyAuthorize.url}")
    private String uaUrl;
    @Value("${unifyAuthorize.password}")
    private String uaPassword;

    @Autowired
    private UserManageDao userManageDao;

    @Autowired
    private UserManageMapstruct userManageMapstruct;

    @Autowired
    private DepartManageService departManageService;

    @Autowired
    private UserManageMapper userManageMapper;

    @Autowired
    private RoleManageMapper roleManageMapper;

    @Autowired
    private UserRoleManageDao userRoleManageDao;

    @Override
    public BaseDao<UserManageEntity> getBaseDao() {
        return userManageDao;
    }

    @Override
    public PageBean selectPageList(PageForm<UserManageDto> pageForm) {
        UserManageDto userManageDto = pageForm.getT();
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotEmpty(userManageDto.getUserName())){
            specificationBuilder.add("userName", SpecificationOperator.Operator.likeAll.name(),userManageDto.getUserName());
        }
        if(StringUtils.isNotEmpty(userManageDto.getLoginName())){
            specificationBuilder.add("loginName", SpecificationOperator.Operator.eq.name(),userManageDto.getLoginName());
        }
        if(StringUtils.isNotEmpty(userManageDto.getIscheck())){
            specificationBuilder.add("ischeck", SpecificationOperator.Operator.eq.name(),userManageDto.getIscheck());
        }
        if(StringUtils.isNotEmpty(userManageDto.getUserLevel())){
            specificationBuilder.add("userLevel", SpecificationOperator.Operator.eq.name(),userManageDto.getUserLevel());
        }
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,null);
        List<UserManageEntity> userManageEntities = (List<UserManageEntity>) pageBean.getPageData();
        List<UserManageDto> userManageDtos = userManageMapstruct.toDto(userManageEntities);
        if("P".equals(sysLevel)){
            //查询部门信息
            List<DepartManageDto> departManageDtos = departManageService.findAllDept();
            if(userManageDtos != null && userManageDtos.size()>0 && departManageDtos != null && departManageDtos.size()>0){
                for(UserManageDto userManage : userManageDtos){
                    for(DepartManageDto departManageDto : departManageDtos){
                        if(departManageDto.getDeptunicode().equals(userManage.getDeptunicode())){
                            userManage.setDeptName(departManageDto.getDeptname());
                            break;
                        }
                    }
                }
            }
        }else{
            //查询用户详细信息
            if(userManageDtos != null && userManageDtos.size()>0){
                for(UserManageDto userManage : userManageDtos){
                    if (StringUtils.isNotEmpty(uaUrl) && StringUtils.isNotEmpty(uaPassword)) {
                        JSONObject jsonObject = getPortalUserInfo(userManage.getId());
                        userManage.setPhone("");
                        if(jsonObject != null && jsonObject.size()>0){
                            userManage.setPhone((String) jsonObject.get("mobile"));
                            userManage.setLoginName((String) jsonObject.get("Username"));
                            userManage.setEmail((String) jsonObject.get("Email"));
                            userManage.setPost((String) jsonObject.get("positionLevel"));
                            userManage.setFixedphone((String) jsonObject.get("officePhone"));
                            String namepath = (String) jsonObject.get("namepath");
                            if(namepath != null && namepath.lastIndexOf("/") == namepath.length()-1){
                                //eg:中国气象局/减灾司/
                                namepath = namepath.substring(0,namepath.length()-1);
                                //namepath = namepath.substring(namepath.lastIndexOf("/")+1);
                                userManage.setDeptName(namepath);
                            }
                        }
                    }
                }
            }
        }

        pageBean.setPageData(userManageDtos);
        return pageBean;
    }


    public JSONObject getPortalUserInfo(String userId){
        JSONObject resultJsonObject = new JSONObject();
        try{
            String url = uaUrl + "/userservice/getUserInfo/getUserByOAID";

            String message = "client_id=CMADAAS&uid="+userId;
            //生成签名
            String hash = SM3Utils.Encode(message);
            //组装json
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("client_id", "CMADAAS");
            jsonObject.put("uid", userId);
            jsonObject.put("signature", hash);
            String jsonStr = jsonObject.toString();
            //生成请求报文
            String s1 = SM4Utils.encrypt(jsonStr,uaPassword);

            HashMap<String, String> headers= new HashMap<String, String>();
            headers.put("client_id","CMADAAS");
            String result = HttpClientUtil.doPost(url,s1,headers);
            if(!"Index: 0, Size: 0".equals(result)){
                String userInfo = SM4Utils.decrypt(result,uaPassword);
                resultJsonObject = JSONObject.parseObject(userInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultJsonObject;
    }


    @Override
    public UserManageDto getDotById(String id) {
        UserManageEntity userManageEntity = this.getById(id);
        UserManageDto userManageDto = this.userManageMapstruct.toDto(userManageEntity);
        if("P".equals(sysLevel)){
            String deptunicode = userManageDto.getDeptunicode();
            List<DepartManageDto> departManageDtos = departManageService.findByDeptunicode(deptunicode);
            if(departManageDtos != null && departManageDtos.size()>0){
                String deptName = "";
                deptName = getAllDeptName(deptName,departManageDtos.get(0));
                userManageDto.setDeptName(deptName);
            }
            //获取用户角色
            List<String> roles = roleManageMapper.selectRoleListByUserId(id);
            userManageDto.setRoleIds(roles.toArray(new String[roles.size()]));
        }else{
            if (StringUtils.isNotEmpty(uaUrl) && StringUtils.isNotEmpty(uaPassword)) {
                JSONObject jsonObject = getPortalUserInfo(id);
                userManageDto.setPhone("");
                if(jsonObject != null && jsonObject.size()>0){
                    userManageDto.setPhone((String) jsonObject.get("mobile"));
                    userManageDto.setLoginName((String) jsonObject.get("Username"));
                    userManageDto.setEmail((String) jsonObject.get("Email"));
                    userManageDto.setPost((String) jsonObject.get("positionLevel"));
                    userManageDto.setFixedphone((String) jsonObject.get("officePhone"));
                    String namepath = (String) jsonObject.get("namepath");

                    if(namepath != null && namepath.lastIndexOf("/") == namepath.length()-1){
                        //eg:中国气象局/减灾司/
                        namepath = namepath.substring(0,namepath.length()-1);
                        //namepath = namepath.substring(namepath.lastIndexOf("/")+1);
                        userManageDto.setDeptName(namepath);
                    }
                }
            }
        }
        return userManageDto;
    }

    private String  getAllDeptName(String deptName,DepartManageDto dept){
        if(dept.getDeptcode().equals(dept.getParentCode())){
            return dept.getDeptname()+"-"+deptName;
        }else{
            if(StringUtil.isEmpty(deptName)){
                deptName = dept.getDeptname();
            }else{
                deptName = dept.getDeptname() + "-"+deptName;
            }
            List<DepartManageDto> parent = departManageService.findByDeptcode(dept.getParentCode());
            if(null != parent && parent.size()>0){
                return getAllDeptName(deptName,parent.get(0));
            }else{
                return deptName;
            }
        }
    }

    @Override
    public UserManageDto updateDto(UserManageDto userManageDto) {
        UserManageEntity userManageEntity = this.getById(userManageDto.getId());
        if(StringUtils.isNotEmpty(userManageDto.getIscheck())){
            userManageEntity.setIscheck(userManageDto.getIscheck());
        }
        if(userManageEntity.getVersion()==null){
            userManageEntity.setVersion(0);
        }else{
            userManageEntity.setVersion(userManageEntity.getVersion()+1);
        }
        //userManageEntity = this.saveNotNull(userManageEntity);
        userManageMapper.updateUser(userManageEntity);
        return userManageMapstruct.toDto(userManageEntity);
    }

    @Override
    public UserManageDto resetPwd(UserManageDto userManageDto) {
        UserManageEntity userManageEntity = userManageMapstruct.toEntity(userManageDto);
        String password = userManageEntity.getPassword();
        password = MD5Util.MD5Encode(password).toUpperCase();
        userManageEntity.setPassword(password);
        //userManageEntity = this.saveNotNull(userManageEntity);
        userManageMapper.updateUser(userManageEntity);
        return userManageMapstruct.toDto(userManageEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserManageDto editUserRole(UserManageDto userManageDto) {
        // 删除用户与角色关联
        userRoleManageDao.deleteByUserId(userManageDto.getId());
        // 新增用户与角色管理
        insertUserRole(userManageDto);
        return null;
    }

    public void insertUserRole(UserManageDto user) {
        String[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles)) {
            // 新增用户与角色管理
            List<UserRoleManageEntity> list = new ArrayList<>();
            for (String roleId : roles) {
                UserRoleManageEntity ur = new UserRoleManageEntity();
                ur.setUserId(user.getId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleManageDao.saveNotNullAll(list);
            }
        }
    }

    @Override
    public UserManageDto saveDto(UserManageDto userManageDto) {
        UserManageEntity userManageEntity = userManageMapstruct.toEntity(userManageDto);
        if(StringUtils.isEmpty(userManageEntity.getId())){
            userManageEntity.setId(IdUtils.simpleUUID());
        }
        userManageEntity.setUpdateTime(new Date());
        userManageEntity = this.saveNotNull(userManageEntity);
        return userManageMapstruct.toDto(userManageEntity);
    }

}
