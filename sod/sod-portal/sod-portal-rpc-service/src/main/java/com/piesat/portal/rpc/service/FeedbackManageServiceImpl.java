package com.piesat.portal.rpc.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.MyBeanUtils;
import com.piesat.common.utils.StringUtils;
import com.piesat.portal.dao.FeedbackManageDao;
import com.piesat.portal.entity.FeedbackManageEntity;
import com.piesat.portal.rpc.api.FeedbackManageService;
import com.piesat.portal.rpc.dto.FeedbackManageDto;
import com.piesat.portal.rpc.mapstruct.FeedbackManageMapstruct;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("feedbackManageService")
public class FeedbackManageServiceImpl extends BaseService<FeedbackManageEntity> implements FeedbackManageService {


    @Autowired
    private FeedbackManageDao feedbackManageDao;

    @Autowired
    private FeedbackManageMapstruct feedbackManageMapstruct;

    @Override
    public BaseDao<FeedbackManageEntity> getBaseDao() {
        return feedbackManageDao;
    }

    @Override
    public PageBean selectPageList(PageForm<FeedbackManageDto> pageForm) {
        FeedbackManageDto feedbackManageDto = pageForm.getT();
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotEmpty(feedbackManageDto.getFeedbackType())){
            specificationBuilder.add("feedbackType", SpecificationOperator.Operator.likeAll.name(),feedbackManageDto.getFeedbackType());
        }
        if(StringUtils.isNotEmpty(feedbackManageDto.getStatus())){
            specificationBuilder.add("status", SpecificationOperator.Operator.eq.toString(),feedbackManageDto.getStatus());
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "status").and(Sort.by(Sort.Direction.DESC, "createTime"));
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<FeedbackManageEntity> feedbackManageEntities = (List<FeedbackManageEntity>) pageBean.getPageData();
        List<FeedbackManageDto> feedbackManageDtos = feedbackManageMapstruct.toDto(feedbackManageEntities);
        pageBean.setPageData(feedbackManageDtos);
        return pageBean;
    }

    @Override
    public FeedbackManageDto getDotById(String id) {
        FeedbackManageEntity feedbackManageEntity = this.getById(id);
        return this.feedbackManageMapstruct.toDto(feedbackManageEntity);
    }

    @Override
    public FeedbackManageDto updateDto(FeedbackManageDto feedbackManageDto) {
        FeedbackManageEntity feedbackManageEntity = feedbackManageMapstruct.toEntity(feedbackManageDto);
        feedbackManageEntity = this.saveNotNull(feedbackManageEntity);
        return feedbackManageMapstruct.toDto(feedbackManageEntity);
    }

    @Override
    public void deleteById(String id){
        this.delete(id);
    }

    @Override
    public void deleteRecordByIds(List<String> ids) {
        this.deleteByIds(ids);
    }

    @Override
    public FeedbackManageDto addFeedback(Map<String, String[]> parameterMap) {
        FeedbackManageEntity feedbackManageEntity = new FeedbackManageEntity();
        MyBeanUtils.getObject(feedbackManageEntity,parameterMap);
//        String[] data = parameterMap.get("data");
//        if (data != null && data.length > 0) {
//            if (StringUtils.isNotEmpty(data[0])) {
//                JSONObject object = JSONObject.parseObject(data[0]);
//                String userId = (String) object.get("userId");
//                feedbackManageEntity.setUserId(userId);
//                String id = (String) object.get("id");
//                if(StringUtils.isNotNullString(id)){
//                    feedbackManageEntity.setId(id);
//                }
//            }
//        }

        feedbackManageEntity = this.saveNotNull(feedbackManageEntity);
        return feedbackManageMapstruct.toDto(feedbackManageEntity);
    }

    @Override
    public  List<FeedbackManageDto> getFeedList(String userName, String isShow, String status){
        List<FeedbackManageEntity> feedbackManageEntitys = feedbackManageDao.findByUserNameAndIsShowLikeAndStatusLike(userName,isShow,status);
        return  feedbackManageMapstruct.toDto(feedbackManageEntitys);
    }
    public  List<FeedbackManageDto> getIsShow(String isShow){
        List<FeedbackManageEntity> feedbackManageEntitys = feedbackManageDao.findByIsShow(isShow);
        return  feedbackManageMapstruct.toDto(feedbackManageEntitys);
    }
}
