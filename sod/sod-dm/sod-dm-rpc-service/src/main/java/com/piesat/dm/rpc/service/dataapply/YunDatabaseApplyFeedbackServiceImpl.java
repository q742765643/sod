package com.piesat.dm.rpc.service.dataapply;

import com.alibaba.fastjson.JSONObject;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.MyBeanUtils;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.dao.dataapply.YunDatabaseApplyFeedbackDao;
import com.piesat.dm.entity.dataapply.YunDatabaseApplyFeedbackEntity;
import com.piesat.dm.entity.dataapply.YunDatabaseApplyLogEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.dataapply.YunDatabaseApplyFeedbackService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.dto.dataapply.YunDatabaseApplyFeedbackDto;
import com.piesat.dm.rpc.mapper.dataapply.YunDatabaseApplyFeedbackMapper;
import com.piesat.ucenter.dao.system.UserDao;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public  class YunDatabaseApplyFeedbackServiceImpl extends BaseService<YunDatabaseApplyFeedbackEntity> implements YunDatabaseApplyFeedbackService {


    @Autowired
    private YunDatabaseApplyFeedbackDao yunDatabaseApplyFeedbackDao;

    @Autowired
    private YunDatabaseApplyFeedbackMapper yunDatabaseApplyFeedbackMapper;

    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private DataClassService dataClassService;
    @GrpcHthtClient
    private UserDao userDao;

    @Override
    public PageBean selectPageList(PageForm<YunDatabaseApplyFeedbackDto> pageForm) {
        YunDatabaseApplyFeedbackEntity yunDatabaseApplyFeedbackEntity = yunDatabaseApplyFeedbackMapper.toEntity(pageForm.getT());

        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();

        if (StringUtils.isNotNullString((String) yunDatabaseApplyFeedbackEntity.getDisplayname())) {
            specificationBuilder.add("displayname", SpecificationOperator.Operator.likeAll.name(), yunDatabaseApplyFeedbackEntity.getDisplayname());
        }
        if (StringUtils.isNotNullString((String) yunDatabaseApplyFeedbackEntity.getFeedbackTitle())) {
            specificationBuilder.add("feedbackTitle", SpecificationOperator.Operator.likeAll.name(), yunDatabaseApplyFeedbackEntity.getFeedbackTitle());
        }
        if (StringUtils.isNotNullString((String) yunDatabaseApplyFeedbackEntity.getFeedbackStatus())) {
            specificationBuilder.add("feedbackStatus", SpecificationOperator.Operator.likeAll.name(), yunDatabaseApplyFeedbackEntity.getFeedbackStatus());
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "feedbackStatus").and(Sort.by(Sort.Direction.DESC, "createTime"));
        PageBean pageBean = this.getPage(specificationBuilder.generateSpecification(), pageForm, sort);

        List<YunDatabaseApplyFeedbackEntity> yunDatabaseApplyFeedbackEntities = (List<YunDatabaseApplyFeedbackEntity>) pageBean.getPageData();
        List<YunDatabaseApplyFeedbackDto> yunDatabaseApplyFeedbackDtos = yunDatabaseApplyFeedbackMapper.toDto(yunDatabaseApplyFeedbackEntities);

        pageBean.setPageData(yunDatabaseApplyFeedbackDtos);
        return pageBean;
    }

    @Override
    public YunDatabaseApplyFeedbackDto saveDto(YunDatabaseApplyFeedbackDto yunDatabaseApplyFeedbackDto) {
        YunDatabaseApplyFeedbackEntity yunDatabaseApplyFeedbackEntity = yunDatabaseApplyFeedbackMapper.toEntity(yunDatabaseApplyFeedbackDto);
        yunDatabaseApplyFeedbackEntity = this.saveNotNull(yunDatabaseApplyFeedbackEntity);
        return yunDatabaseApplyFeedbackMapper.toDto(yunDatabaseApplyFeedbackEntity);
    }

    public YunDatabaseApplyFeedbackDto updateDto(YunDatabaseApplyFeedbackDto yunDatabaseApplyFeedbackDto) {
        YunDatabaseApplyFeedbackEntity yunDatabaseApplyFeedbackEntity = yunDatabaseApplyFeedbackMapper.toEntity(yunDatabaseApplyFeedbackDto);
        yunDatabaseApplyFeedbackEntity = this.saveNotNull(yunDatabaseApplyFeedbackEntity);
        return yunDatabaseApplyFeedbackMapper.toDto(yunDatabaseApplyFeedbackEntity);
    }
    @Override
    public YunDatabaseApplyFeedbackDto addFeedback(Map<String, String[]> parameterMap) {
        YunDatabaseApplyFeedbackEntity yunDatabaseApplyFeedbackEntity = new YunDatabaseApplyFeedbackEntity();
        MyBeanUtils.getObject(yunDatabaseApplyFeedbackEntity,parameterMap);
        String[] data = parameterMap.get("data");
        if (data != null && data.length > 0) {
            if (StringUtils.isNotEmpty(data[0])) {
                JSONObject object = JSONObject.parseObject(data[0]);
                String userId = (String) object.get("userId");
                yunDatabaseApplyFeedbackEntity.setUserId(userId);
                String id = (String) object.get("id");
                if(StringUtils.isNotNullString(id)){
                    yunDatabaseApplyFeedbackEntity.setId(id);
                }

            }
        }

        yunDatabaseApplyFeedbackEntity = this.saveNotNull(yunDatabaseApplyFeedbackEntity);
        return yunDatabaseApplyFeedbackMapper.toDto(yunDatabaseApplyFeedbackEntity);
    }
    @Override
    public  List<YunDatabaseApplyFeedbackDto> getByItserviceId(String itserviceId, String feedbackStatus){
        List<YunDatabaseApplyFeedbackEntity> yunDatabaseApplyFeedbackEntities = yunDatabaseApplyFeedbackDao.findByItserviceIdAndFeedbackStatusLike(itserviceId,feedbackStatus);
        return  yunDatabaseApplyFeedbackMapper.toDto(yunDatabaseApplyFeedbackEntities);
    }
    @Override
    public YunDatabaseApplyFeedbackDto getFeeById(String id) {
        YunDatabaseApplyFeedbackEntity yunDatabaseApplyFeedbackEntity = this.getById(id);
        YunDatabaseApplyFeedbackDto yunDatabaseApplyFeedbackDto = yunDatabaseApplyFeedbackMapper.toDto(yunDatabaseApplyFeedbackEntity);

        return yunDatabaseApplyFeedbackDto;
    }
    @Override
    public YunDatabaseApplyFeedbackDto updateFeedbackStatus(String id, String feedbackStatus) {
        YunDatabaseApplyFeedbackEntity yunDatabaseApplyFeedbackEntity = this.getById(id);
        yunDatabaseApplyFeedbackEntity.setFeedbackStatus(feedbackStatus);
        yunDatabaseApplyFeedbackEntity = this.saveNotNull(yunDatabaseApplyFeedbackEntity);
        return yunDatabaseApplyFeedbackMapper.toDto(yunDatabaseApplyFeedbackEntity);
    }
    @Override
    public void deleteByFeedbackId(String itserviceId, String feedbackStatus) {
        List<YunDatabaseApplyFeedbackDto> yunDatabaseApplyFeedbackDtos = this.getByItserviceId(itserviceId,feedbackStatus);
        for(int i = 0;i<yunDatabaseApplyFeedbackDtos.size();i++) {
            YunDatabaseApplyFeedbackDto filePathN = (YunDatabaseApplyFeedbackDto)yunDatabaseApplyFeedbackDtos.get(i);
            String id = filePathN.getId();
            this.delete(id);
        }
    }
    @Override
    public void deleteById(String id) {
        this.delete(id);
    }
    @Override
    public BaseDao<YunDatabaseApplyFeedbackEntity> getBaseDao() {
        return this.yunDatabaseApplyFeedbackDao;
    }
}
