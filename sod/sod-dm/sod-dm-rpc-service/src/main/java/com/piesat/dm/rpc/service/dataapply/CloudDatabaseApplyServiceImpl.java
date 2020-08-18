package com.piesat.dm.rpc.service.dataapply;

import com.alibaba.fastjson.JSONObject;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.MyBeanUtils;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.dao.dataapply.CloudDatabaseApplyDao;
import com.piesat.dm.entity.dataapply.CloudDatabaseApplyEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.dataapply.CloudDatabaseApplyService;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.dto.dataapply.CloudDatabaseApplyDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.dm.rpc.mapper.dataapply.CloudDatabaseApplyMapper;
import com.piesat.ucenter.dao.system.UserDao;
import com.piesat.ucenter.entity.system.UserEntity;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/12 16:14
 */
@Service
public class CloudDatabaseApplyServiceImpl extends BaseService<CloudDatabaseApplyEntity> implements CloudDatabaseApplyService {

    @Autowired
    private CloudDatabaseApplyDao cloudDatabaseApplyDao;

    @Autowired
    private CloudDatabaseApplyMapper cloudDatabaseApplyMapper;

    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private DataClassService dataClassService;

    @GrpcHthtClient
    private UserDao userDao;

    @Override
    public BaseDao<CloudDatabaseApplyEntity> getBaseDao() {
        return this.cloudDatabaseApplyDao;
    }

    @Override
    public PageBean selectPageList(PageForm<CloudDatabaseApplyDto> pageForm) {
        CloudDatabaseApplyEntity cloudDatabaseApplyEntity = cloudDatabaseApplyMapper.toEntity(pageForm.getT());

        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(cloudDatabaseApplyEntity.getExamineStatus())) {
            specificationBuilder.add("examineStatus", SpecificationOperator.Operator.eq.name(), cloudDatabaseApplyEntity.getExamineStatus());
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
        if (StringUtils.isNotNullString((String) cloudDatabaseApplyEntity.getDatabaseName())) {
            specificationBuilder.add("databaseName", SpecificationOperator.Operator.likeAll.name(), cloudDatabaseApplyEntity.getDatabaseName());
        }
        if (StringUtils.isNotNullString((String) cloudDatabaseApplyEntity.getParamt().get("beginTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.ges.name(), (String) cloudDatabaseApplyEntity.getParamt().get("beginTime"));
        }
        if (StringUtils.isNotNullString((String) cloudDatabaseApplyEntity.getParamt().get("endTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.les.name(), (String) cloudDatabaseApplyEntity.getParamt().get("endTime"));
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "examineStatus").and(Sort.by(Sort.Direction.DESC, "createTime"));
        PageBean pageBean = this.getPage(specificationBuilder.generateSpecification(), pageForm, sort);
        List<CloudDatabaseApplyEntity> cloudDatabaseApplyEntities = (List<CloudDatabaseApplyEntity>) pageBean.getPageData();

        List<CloudDatabaseApplyDto> cloudDatabaseApplyDtos = cloudDatabaseApplyMapper.toDto(cloudDatabaseApplyEntities);


        //调用接口获取所有的用户信息
        List<UserEntity> userEntities = userDao.findByUserType("11");

        cloudDatabaseApplyDtos = cloudDatabaseApplyDtos.parallelStream().map(c -> {
            UserEntity userEntity = userEntities.stream().filter(d -> c.getUserId().equals(d.getUserName())).findFirst().orElse(null);
            if (userEntity != null) {
                c.setUserName(userEntity.getWebUsername());
                c.setTelephone(userEntity.getTutorPhone());
                c.setDepartment(userEntity.getDeptName());
            }
            return c;
        }).collect(Collectors.toList());

        pageBean.setPageData(cloudDatabaseApplyDtos);
        return pageBean;
    }

    @Override
    public CloudDatabaseApplyDto saveDto(CloudDatabaseApplyDto cloudDatabaseApplyDto) {
        CloudDatabaseApplyEntity cloudDatabaseApplyEntity = cloudDatabaseApplyMapper.toEntity(cloudDatabaseApplyDto);
        cloudDatabaseApplyEntity = this.saveNotNull(cloudDatabaseApplyEntity);
        return cloudDatabaseApplyMapper.toDto(cloudDatabaseApplyEntity);
    }

    @Override
    public CloudDatabaseApplyDto updateDto(CloudDatabaseApplyDto cloudDatabaseApplyDto) {
        CloudDatabaseApplyEntity cloudDatabaseApplyEntity = cloudDatabaseApplyMapper.toEntity(cloudDatabaseApplyDto);
        cloudDatabaseApplyEntity = this.saveNotNull(cloudDatabaseApplyEntity);
        return cloudDatabaseApplyMapper.toDto(cloudDatabaseApplyEntity);
    }

    @Override
    public CloudDatabaseApplyDto addOrUpdate(Map<String, String[]> parameterMap, String filePath) {
        CloudDatabaseApplyEntity cloudDatabaseApplyEntity = new CloudDatabaseApplyEntity();
        MyBeanUtils.getObject(cloudDatabaseApplyEntity, parameterMap);
        String[] data = parameterMap.get("data");
        if (data != null && data.length > 0) {
            if (StringUtils.isNotEmpty(data[0])) {
                JSONObject object = JSONObject.parseObject(data[0]);
                String userId = (String) object.get("userId");
                cloudDatabaseApplyEntity.setUserId(userId);
                String id = (String) object.get("id");
                if(StringUtils.isNotNullString(id)){
                    cloudDatabaseApplyEntity.setId(id);
                }
            }
        }
        if (StringUtils.isNotNullString(filePath)) {
            cloudDatabaseApplyEntity.setExamineMaterial(filePath);
        }
        cloudDatabaseApplyEntity.setExamineStatus("01");
        cloudDatabaseApplyEntity = this.saveNotNull(cloudDatabaseApplyEntity);
        return cloudDatabaseApplyMapper.toDto(cloudDatabaseApplyEntity);
    }

    @Override
    public CloudDatabaseApplyDto getDotById(String id) {
        CloudDatabaseApplyEntity cloudDatabaseApplyEntity = this.getById(id);
        CloudDatabaseApplyDto cloudDatabaseApplyDto = cloudDatabaseApplyMapper.toDto(cloudDatabaseApplyEntity);
        //调接口查申请人详情
        UserEntity userEntity = userDao.findByUserName(cloudDatabaseApplyDto.getUserId());
        if (userEntity != null) {
            cloudDatabaseApplyDto.setUserName(userEntity.getWebUsername());
            cloudDatabaseApplyDto.setTelephone(userEntity.getTutorPhone());
            cloudDatabaseApplyDto.setDepartment(userEntity.getDeptName());
        }
        return cloudDatabaseApplyDto;
    }

    @Override
    public void deleteById(String id) {
        this.delete(id);
    }

    @Override
    public List<CloudDatabaseApplyDto> getByUserId(String userId) {
        List<CloudDatabaseApplyEntity> cloudDatabaseApplyEntities = cloudDatabaseApplyDao.findByUserIdOrderByExamineStatusAscCreateTimeDesc(userId);
        return cloudDatabaseApplyMapper.toDto(cloudDatabaseApplyEntities);
    }

    @Override
    public CloudDatabaseApplyDto updateExamineStatus(String id, String examineStatus) {
        CloudDatabaseApplyEntity cloudDatabaseApplyEntity = this.getById(id);
        cloudDatabaseApplyEntity.setExamineStatus(examineStatus);
        cloudDatabaseApplyEntity.setExamineTime(new Date());
        cloudDatabaseApplyEntity = this.saveNotNull(cloudDatabaseApplyEntity);
        return cloudDatabaseApplyMapper.toDto(cloudDatabaseApplyEntity);
    }

    @Override
    public Map<String, Object> getRecentTime(String classDataId, String ctsCode) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            //获取近线时间
            List<Map<String, Object>> recentOnlineList = mybatisQueryMapper.getRecentOnlineTime(ctsCode);
            if (null != recentOnlineList && recentOnlineList.size() > 0) {
                result.put("recentOnline", recentOnlineList.get(0));
            }
            DataClassDto dataClassDto = this.dataClassService.findByDataClassId(classDataId);
            if (dataClassDto != null) {
                int is_all_line = dataClassDto.getIsAllLine();
                result.put("is_all_line", is_all_line);
            } else {
                result.put("is_all_line", 1);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
