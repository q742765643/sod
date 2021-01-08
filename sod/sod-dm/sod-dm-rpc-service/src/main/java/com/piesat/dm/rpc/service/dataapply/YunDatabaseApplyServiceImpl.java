package com.piesat.dm.rpc.service.dataapply;

import com.alibaba.fastjson.JSONObject;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.MyBeanUtils;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.dao.dataapply.YunDatabaseApplyDao;
import com.piesat.dm.entity.dataapply.YunDatabaseApplyEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.dataapply.YunDatabaseApplyService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.dto.dataapply.YunDatabaseApplyDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.dm.rpc.mapper.dataapply.YunDatabaseApplyMapper;
import com.piesat.ucenter.dao.system.UserDao;
import com.piesat.ucenter.entity.system.UserEntity;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public  class YunDatabaseApplyServiceImpl extends BaseService<YunDatabaseApplyEntity> implements YunDatabaseApplyService {

    @Autowired
    private YunDatabaseApplyDao yunDatabaseApplyDao;

    @Autowired
    private YunDatabaseApplyMapper yunDatabaseApplyMapper;

    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private DataClassService dataClassService;

    @GrpcHthtClient
    private UserDao userDao;

    @Override
    public PageBean selectPageList(PageForm<YunDatabaseApplyDto> pageForm) {
        YunDatabaseApplyEntity yunDatabaseApplyEntity = yunDatabaseApplyMapper.toEntity(pageForm.getT());

        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(yunDatabaseApplyEntity.getExamineStatus())) {
            specificationBuilder.add("examineStatus", SpecificationOperator.Operator.eq.name(), yunDatabaseApplyEntity.getExamineStatus());
        }
        if (StringUtils.isNotNullString(pageForm.getT().getWebuserName())) {
            //调用接口 根据用户名查询用户id
            List<String> userId = new ArrayList<String>();
            userId.add("noUseId");
            List<UserEntity> userEntities = userDao.findByWebUsernameLike("%" + pageForm.getT().getWebuserName() + "%");
            if (userEntities != null && userEntities.size() > 0) {
                for (UserEntity userEntity : userEntities) {
                    userId.add(userEntity.getUserName());
                }
            }
            specificationBuilder.add("userId", SpecificationOperator.Operator.in.name(), userId);
        }
        if(StringUtils.isNotNullString((String) yunDatabaseApplyEntity.getUserId())){
            specificationBuilder.add("userId",SpecificationOperator.Operator.likeAll.name(),yunDatabaseApplyEntity.getUserId());
        }
        if (StringUtils.isNotNullString((String) yunDatabaseApplyEntity.getDisplayname())) {
            specificationBuilder.add("displayname", SpecificationOperator.Operator.likeAll.name(), yunDatabaseApplyEntity.getDisplayname());
        }
        if (StringUtils.isNotNullString((String) yunDatabaseApplyEntity.getStorageLogic())) {
            specificationBuilder.add("storageLogic", SpecificationOperator.Operator.likeAll.name(), yunDatabaseApplyEntity.getStorageLogic());
        }
        if (StringUtils.isNotNullString((String) yunDatabaseApplyEntity.getParamt().get("beginTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.ges.name(), (String) yunDatabaseApplyEntity.getParamt().get("beginTime"));
        }
        if (StringUtils.isNotNullString((String) yunDatabaseApplyEntity.getParamt().get("endTime"))) {
            specificationBuilder.add("createTime", SpecificationOperator.Operator.les.name(), (String) yunDatabaseApplyEntity.getParamt().get("endTime"));
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "examineStatus").and(Sort.by(Sort.Direction.DESC, "createTime"));
        PageBean pageBean = this.getPage(specificationBuilder.generateSpecification(), pageForm, sort);
        List<YunDatabaseApplyEntity> yunDatabaseApplyEntities = (List<YunDatabaseApplyEntity>) pageBean.getPageData();

        List<YunDatabaseApplyDto> yunDatabaseApplyDtos = yunDatabaseApplyMapper.toDto(yunDatabaseApplyEntities);


        //调用接口获取所有的用户信息
        List<UserEntity> userEntities = userDao.findByUserType("11");

        yunDatabaseApplyDtos = yunDatabaseApplyDtos.parallelStream().map(c -> {
            UserEntity userEntity = userEntities.stream().filter(d -> c.getUserId().equals(d.getUserName())).findFirst().orElse(null);
            if (userEntity != null) {
                c.setWebuserName(userEntity.getWebUsername());
                c.setTelephone(userEntity.getTutorPhone());
                c.setDepartment(userEntity.getDeptName());
            }
            return c;
        }).collect(Collectors.toList());

        pageBean.setPageData(yunDatabaseApplyDtos);
        return pageBean;
    }

    @Override
    public YunDatabaseApplyDto saveDto(YunDatabaseApplyDto yunDatabaseApplyDto) {
        YunDatabaseApplyEntity yunDatabaseApplyEntity = yunDatabaseApplyMapper.toEntity(yunDatabaseApplyDto);
        yunDatabaseApplyEntity = this.saveNotNull(yunDatabaseApplyEntity);

        return yunDatabaseApplyMapper.toDto(yunDatabaseApplyEntity);
    }

    @Override
    public YunDatabaseApplyDto updateDto(YunDatabaseApplyDto yunDatabaseApplyDto) {
        YunDatabaseApplyEntity yunDatabaseApplyEntity = yunDatabaseApplyMapper.toEntity(yunDatabaseApplyDto);
        yunDatabaseApplyEntity = this.saveNotNull(yunDatabaseApplyEntity);
        return yunDatabaseApplyMapper.toDto(yunDatabaseApplyEntity);
    }

//新增
    @Override
    public YunDatabaseApplyDto addorUpdate(Map<String, String[]> parameterMap, String filePath) {
        YunDatabaseApplyEntity yunDatabaseApplyEntity = new YunDatabaseApplyEntity();
        MyBeanUtils.getObject(yunDatabaseApplyEntity,parameterMap);
        String[] data = parameterMap.get("data");
        if (data != null && data.length > 0) {
//            if (StringUtils.isNotEmpty(data[0])) {
                JSONObject object = JSONObject.parseObject(data[0]);
                String userId = (String) object.get("userId");
                yunDatabaseApplyEntity.setUserId(userId);
                String id = (String) object.get("id");
                if(StringUtils.isNotNullString(id)){
                    yunDatabaseApplyEntity.setId(id);
                }
//            }
        }
        if (StringUtils.isNotNullString(filePath)) {
            yunDatabaseApplyEntity.setExamineMaterial(filePath);
        }
        yunDatabaseApplyEntity = this.saveNotNull(yunDatabaseApplyEntity);
        return yunDatabaseApplyMapper.toDto(yunDatabaseApplyEntity);
    }
    //编辑
    @Override
    public YunDatabaseApplyDto addorUpdate2(Map<String, String[]> parameterMap, String filePath) {
        YunDatabaseApplyEntity yunDatabaseApplyEntity = new YunDatabaseApplyEntity();
        MyBeanUtils.getObject(yunDatabaseApplyEntity,parameterMap);
        String[] data = parameterMap.get("data");
        if (data != null && data.length > 0) {
            JSONObject object = JSONObject.parseObject(data[0]);
            String userId = (String) object.get("userId");
            yunDatabaseApplyEntity.setUserId(userId);
            String id = (String) object.get("id");
            if(StringUtils.isNotNullString(id)){
                yunDatabaseApplyEntity.setId(id);
            }

        }
        if (StringUtils.isNotNullString(filePath)) {
            yunDatabaseApplyEntity.setExamineMaterial(filePath);
        }
        yunDatabaseApplyEntity = this.saveNotNull(yunDatabaseApplyEntity);
        return yunDatabaseApplyMapper.toDto(yunDatabaseApplyEntity);
    }
    @Override
    public YunDatabaseApplyDto getDotById(String id) {
        YunDatabaseApplyEntity yunDatabaseApplyEntity = this.getById(id);
        YunDatabaseApplyDto yunDatabaseApplyDto = yunDatabaseApplyMapper.toDto(yunDatabaseApplyEntity);
        //调接口查申请人详情
        UserEntity userEntity = userDao.findByUserName(yunDatabaseApplyDto.getUserId());
        if (userEntity != null) {
            yunDatabaseApplyDto.setWebuserName(userEntity.getWebUsername());
            yunDatabaseApplyDto.setTelephone(userEntity.getTutorPhone());
            yunDatabaseApplyDto.setDepartment(userEntity.getDeptName());
        }
        return yunDatabaseApplyDto;
    }
    @Override
    public YunDatabaseApplyDto getById1(String id) {
        YunDatabaseApplyEntity yunDatabaseApplyEntity = this.getById(id);
        YunDatabaseApplyDto yunDatabaseApplyDto = yunDatabaseApplyMapper.toDto(yunDatabaseApplyEntity);
//        System.out.println("+++++++"+yunDatabaseApplyDto+"--------------------");
        return yunDatabaseApplyDto;
    }
    @Override
    public void deleteById(String id) {
        this.delete(id);
    }

    @Override
    public List<YunDatabaseApplyDto> getByUserId(String userId) {
        List<YunDatabaseApplyEntity> yunDatabaseApplyEntities = yunDatabaseApplyDao.findByUserIdOrderByExamineStatusAscCreateTimeDesc(userId);

        return yunDatabaseApplyMapper.toDto(yunDatabaseApplyEntities);
    }


    @Override
    public  List<YunDatabaseApplyDto> getByDNES(String userId,String storageLogic,String examineStatus,String displayname){
        List<YunDatabaseApplyEntity> yunDatabaseApplyEntities = yunDatabaseApplyDao.findByUserIdAndStorageLogicAndExamineStatusLikeAndDisplaynameLike(userId,storageLogic,examineStatus,displayname);
        return  yunDatabaseApplyMapper.toDto(yunDatabaseApplyEntities);
    }

    @Override
    public List<YunDatabaseApplyDto> getByUserIdStorageLogic(String userId,String storageLogic) {
        List<YunDatabaseApplyEntity> yunDatabaseApplyEntities = yunDatabaseApplyDao.findByUserIdAndStorageLogic(userId,storageLogic);

        return yunDatabaseApplyMapper.toDto(yunDatabaseApplyEntities);
    }

    @Override
    public YunDatabaseApplyDto updateExamineStatus(String id, String examineStatus) {
        YunDatabaseApplyEntity yunDatabaseApplyEntity = this.getById(id);
        yunDatabaseApplyEntity.setExamineStatus(examineStatus);
        yunDatabaseApplyEntity.setExamineTime(new Date());
        yunDatabaseApplyEntity = this.saveNotNull(yunDatabaseApplyEntity);
        return yunDatabaseApplyMapper.toDto(yunDatabaseApplyEntity);
    }

    @Override
    public Map<String, Object> getRecentTime(String classDataId, String ctsCode) {
        Map<String ,Object> result = new HashMap<String, Object>();
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

    @Override
    public BaseDao<YunDatabaseApplyEntity> getBaseDao() {
        return this.yunDatabaseApplyDao;
    }
}
