package com.piesat.sod.system.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.sod.system.dao.GribParameterDefineDao;
import com.piesat.sod.system.entity.GribParameterDefineEntity;
import com.piesat.sod.system.rpc.api.GribParameterDefineService;
import com.piesat.sod.system.rpc.dto.GribParameterDefineDto;
import com.piesat.sod.system.rpc.mapstruct.GribParameterDefineMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/9 17:51
 */
@Service
public class GribParameterDefineServiceImpl extends BaseService<GribParameterDefineEntity> implements GribParameterDefineService {

    @Autowired
    private GribParameterDefineDao gribParameterDefineDao;

    @Autowired
    private GribParameterDefineMapstruct gribParameterDefineMapstruct;

    @Override
    public BaseDao<GribParameterDefineEntity> getBaseDao() {
        return this.gribParameterDefineDao;
    }

    @Override
    public PageBean selectPageList(PageForm<GribParameterDefineDto> pageForm) {
        GribParameterDefineEntity gribParameterDefineEntity=gribParameterDefineMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(gribParameterDefineEntity.getGribVersion() != null){
            specificationBuilder.add("gribVersion", SpecificationOperator.Operator.eq.name(),gribParameterDefineEntity.getGribVersion());
        }
        if(gribParameterDefineEntity.getParameterId() != null){
            specificationBuilder.add("parameterId", SpecificationOperator.Operator.likeAll.name(),gribParameterDefineEntity.getParameterId());
        }
        if(StringUtils.isNotNullString(gribParameterDefineEntity.getEleCodeShort())){
            specificationBuilder.add("eleCodeShort", SpecificationOperator.Operator.likeAll.name(),gribParameterDefineEntity.getEleCodeShort());
        }
        if(gribParameterDefineEntity.getSubjectId() != null){
            specificationBuilder.add("subjectId", SpecificationOperator.Operator.likeAll.name(),gribParameterDefineEntity.getSubjectId());
        }
        if(gribParameterDefineEntity.getClassify() != null){
            specificationBuilder.add("classify", SpecificationOperator.Operator.likeAll.name(),gribParameterDefineEntity.getClassify());
        }
        if(StringUtils.isNotNullString(gribParameterDefineEntity.getElementCn())){
            specificationBuilder.add("elementCn", SpecificationOperator.Operator.likeAll.name(),gribParameterDefineEntity.getElementCn());
        }
        if(StringUtils.isNotNullString(gribParameterDefineEntity.getPublicConfig())){
            specificationBuilder.add("publicConfig", SpecificationOperator.Operator.likeAll.name(),gribParameterDefineEntity.getPublicConfig());
        }
        if(StringUtils.isNotNullString(gribParameterDefineEntity.getTemplateId())){
            specificationBuilder.add("templateId", SpecificationOperator.Operator.likeAll.name(),gribParameterDefineEntity.getTemplateId());
        }
        if(StringUtils.isNotNullString(gribParameterDefineEntity.getTemplateDesc())){
            specificationBuilder.add("templateDesc", SpecificationOperator.Operator.likeAll.name(),gribParameterDefineEntity.getTemplateDesc());
        }
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,null);
        List<GribParameterDefineEntity> GribParameterDefineEntities = (List<GribParameterDefineEntity>) pageBean.getPageData();
        List<GribParameterDefineDto> gribParameterDefineDtos = gribParameterDefineMapstruct.toDto(GribParameterDefineEntities);
        pageBean.setPageData(gribParameterDefineDtos);
        return pageBean;
    }

    @Override
    public GribParameterDefineDto saveDto(GribParameterDefineDto gribParameterDefineDto) {
        GribParameterDefineEntity gribParameterDefineEntity=gribParameterDefineMapstruct.toEntity(gribParameterDefineDto);
        gribParameterDefineEntity = this.saveNotNull(gribParameterDefineEntity);
        return gribParameterDefineMapstruct.toDto(gribParameterDefineEntity);
    }

    @Override
    public GribParameterDefineDto updateDto(GribParameterDefineDto gribParameterDefineDto) {
        GribParameterDefineEntity gribParameterDefineEntity=gribParameterDefineMapstruct.toEntity(gribParameterDefineDto);
        gribParameterDefineEntity = this.saveNotNull(gribParameterDefineEntity);
        return gribParameterDefineMapstruct.toDto(gribParameterDefineEntity);
    }

    @Override
    public void deleteRecordByIds(List<String> ids) {
        this.deleteByIds(ids);
    }

    @Override
    public List<GribParameterDefineDto> all() {
        List<GribParameterDefineEntity> all = this.getAll();
        return this.gribParameterDefineMapstruct.toDto(all);
    }
}
