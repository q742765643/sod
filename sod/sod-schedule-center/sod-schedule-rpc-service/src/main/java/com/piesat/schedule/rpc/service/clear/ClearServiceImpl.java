package com.piesat.schedule.rpc.service.clear;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.dao.clear.ClearDao;
import com.piesat.schedule.entity.clear.ClearEntity;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.api.clear.ClearService;
import com.piesat.schedule.rpc.dto.clear.ClearDto;
import com.piesat.schedule.rpc.mapstruct.clear.ClearMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-24 11:11
 **/
@Service
public class ClearServiceImpl extends BaseService<ClearEntity> implements ClearService{
    @Autowired
    private ClearMapstruct clearMapstruct;
    @Autowired
    private ClearDao clearDao;
    @Autowired
    private JobInfoService jobInfoService;
    @Override
    public BaseDao<ClearEntity> getBaseDao() {
        return clearDao;
    }
    @Override
    public PageBean selectClearList(PageForm<ClearDto> pageForm){
        ClearEntity clearEntity=clearMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(clearEntity.getDatabaseId())){
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(),clearEntity.getDatabaseId());
        }
        if(StringUtils.isNotNullString(clearEntity.getDataClassId())){
            specificationBuilder.add("dataClassId", SpecificationOperator.Operator.likeAll.name(),clearEntity.getDataClassId());
            specificationBuilder.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(),clearEntity.getDataClassId());
        }
        if(StringUtils.isNotNullString(clearEntity.getProfileName())){
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(),clearEntity.getProfileName());
        }
        if(null!=clearEntity.getTriggerStatus()){
            specificationBuilder.add("triggerStatus",SpecificationOperator.Operator.eq.name(),clearEntity.getTriggerStatus());
        }
        if(StringUtils.isNotNullString(clearEntity.getTableName())){
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(),clearEntity.getTableName());
        }
        if(StringUtils.isNotNullString((String) clearEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) clearEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) clearEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) clearEntity.getParamt().get("endTime"));
        }
        Sort sort=Sort.by(Sort.Direction.ASC,"createTime");
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<ClearEntity> clearEntities= (List<ClearEntity>) pageBean.getPageData();
        pageBean.setPageData(clearMapstruct.toDto(clearEntities));
        return pageBean;

    }
    @Override
    public ClearDto findClearById(String clearId){
        ClearEntity clearEntity=this.getById(clearId);
        return clearMapstruct.toDto(clearEntity);

    }
    @Override
    public void saveClear(ClearDto clearDto){
        ClearEntity clearEntity=clearMapstruct.toEntity(clearDto);
        clearEntity=this.saveNotNull(clearEntity);
        jobInfoService.start(clearMapstruct.toDto(clearEntity));
    }
    @Override
    public void updateClear(ClearDto clearDto){
        ClearEntity clearEntity=clearMapstruct.toEntity(clearDto);
        this.saveNotNull(clearEntity);
        jobInfoService.start(clearDto);

    }
    @Override
    public void deleteClearByIds(String[] clearIds){
        this.deleteByIds(Arrays.asList(clearIds));
        jobInfoService.stopByIds(Arrays.asList(clearIds));

    }
}

