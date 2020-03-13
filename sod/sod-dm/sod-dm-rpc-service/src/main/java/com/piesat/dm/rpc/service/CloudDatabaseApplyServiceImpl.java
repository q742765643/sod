package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.dao.CloudDatabaseApplyDao;
import com.piesat.dm.entity.CloudDatabaseApplyEntity;
import com.piesat.dm.rpc.api.CloudDatabaseApplyService;
import com.piesat.dm.rpc.dto.CloudDatabaseApplyDto;
import com.piesat.dm.rpc.mapper.CloudDatabaseApplyMapper;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public BaseDao<CloudDatabaseApplyEntity> getBaseDao() {
        return this.cloudDatabaseApplyDao;
    }

    @Override
    public PageBean selectPageList(PageForm<CloudDatabaseApplyDto> pageForm) {
        CloudDatabaseApplyEntity cloudDatabaseApplyEntity = cloudDatabaseApplyMapper.toEntity(pageForm.getT());

        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(cloudDatabaseApplyEntity.getExamineStatus())){
            specificationBuilder.add("examineStatus", SpecificationOperator.Operator.eq.name(),cloudDatabaseApplyEntity.getExamineStatus());
        }
        if(StringUtils.isNotNullString(pageForm.getT().getUserName())){
            //调用接口 根据用户名查询用户id
            String userId="";
            specificationBuilder.add("userId", SpecificationOperator.Operator.eq.name(),userId);
        }
        if(StringUtils.isNotNullString((String) cloudDatabaseApplyEntity.getDatabaseName())){
            specificationBuilder.add("databaseName",SpecificationOperator.Operator.likeAll.name(),cloudDatabaseApplyEntity.getDatabaseName());
        }
        if(StringUtils.isNotNullString((String) cloudDatabaseApplyEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) cloudDatabaseApplyEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) cloudDatabaseApplyEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) cloudDatabaseApplyEntity.getParamt().get("endTime"));
        }
        Sort sort = Sort.by(Sort.Direction.ASC,"examineStatus").and(Sort.by(Sort.Direction.DESC,"createTime"));
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<CloudDatabaseApplyEntity> cloudDatabaseApplyEntities = (List<CloudDatabaseApplyEntity>) pageBean.getPageData();

        List<CloudDatabaseApplyDto> cloudDatabaseApplyDtos = cloudDatabaseApplyMapper.toDto(cloudDatabaseApplyEntities);


        //调用接口获取所有的用户信息

        //循环遍历
        for(CloudDatabaseApplyDto cloudDatabaseApplyDto : cloudDatabaseApplyDtos){

            //遍历所有用户信息找到每条记录对应的用户信息
            cloudDatabaseApplyDto.setUserName("申请人");
            cloudDatabaseApplyDto.setTelephone("12388888888");
            cloudDatabaseApplyDto.setDepartment("部门");
        }
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
    public void deleteById(String id) {
        this.delete(id);
    }
}
