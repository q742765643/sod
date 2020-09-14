package com.piesat.portal.rpc.service;

import com.github.pagehelper.PageHelper;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
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
import org.springframework.stereotype.Service;

import java.util.List;

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
        if(StringUtils.isNotEmpty(feedbackManageDto.getUserId())){
            specificationBuilder.add("userId", SpecificationOperator.Operator.eq.toString(),feedbackManageDto.getUserId());
        }
        if(StringUtils.isNotEmpty(feedbackManageDto.getStatus())){
            specificationBuilder.add("status", SpecificationOperator.Operator.eq.toString(),feedbackManageDto.getStatus());
        }
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,null);
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
        UserDto loginUser = (UserDto) SecurityUtils.getSubject().getPrincipal();
        FeedbackManageEntity feedbackManageEntity=feedbackManageMapstruct.toEntity(feedbackManageDto);
        feedbackManageEntity.setUpdateBy(loginUser.getNickName());
        feedbackManageEntity = this.saveNotNull(feedbackManageEntity);
        return feedbackManageMapstruct.toDto(feedbackManageEntity);
    }

    @Override
    public void deleteRecordByIds(List<String> ids) {
        this.deleteByIds(ids);
    }
}
