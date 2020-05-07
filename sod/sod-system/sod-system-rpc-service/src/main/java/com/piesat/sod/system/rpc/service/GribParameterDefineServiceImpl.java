package com.piesat.sod.system.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.common.utils.poi.ExcelUtil;
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
        GribParameterDefineDto cribParameterDefineDto = pageForm.getT();
//        GribParameterDefineEntity gribParameterDefineEntity=gribParameterDefineMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(cribParameterDefineDto.getGribVersion()!=null&&cribParameterDefineDto.getGribVersion().toString().length()>0){
            specificationBuilder.add("gribVersion", SpecificationOperator.Operator.eq.name(),cribParameterDefineDto.getGribVersion());
        }
        if(cribParameterDefineDto.getParameterId()!=null&&cribParameterDefineDto.getParameterId().toString().length()>0){
            specificationBuilder.add("parameterId", SpecificationOperator.Operator.likeAll.name(),cribParameterDefineDto.getParameterId());
        }
        if(StringUtils.isNotNullString(cribParameterDefineDto.getEleCodeShort())){
            specificationBuilder.add("eleCodeShort", SpecificationOperator.Operator.likeAll.name(),cribParameterDefineDto.getEleCodeShort());
        }
        if(cribParameterDefineDto.getSubjectId() != null){
            specificationBuilder.add("subjectId", SpecificationOperator.Operator.likeAll.name(),cribParameterDefineDto.getSubjectId());
        }
        if(cribParameterDefineDto.getClassify() != null){
            specificationBuilder.add("classify", SpecificationOperator.Operator.likeAll.name(),cribParameterDefineDto.getClassify());
        }
        if(StringUtils.isNotNullString(cribParameterDefineDto.getElementCn())){
            specificationBuilder.add("elementCn", SpecificationOperator.Operator.likeAll.name(),cribParameterDefineDto.getElementCn());
        }
        if(StringUtils.isNotNullString(cribParameterDefineDto.getPublicConfig())){
            specificationBuilder.add("publicConfig", SpecificationOperator.Operator.likeAll.name(),cribParameterDefineDto.getPublicConfig());
        }
        if(StringUtils.isNotNullString(cribParameterDefineDto.getTemplateId())){
            specificationBuilder.add("templateId", SpecificationOperator.Operator.likeAll.name(),cribParameterDefineDto.getTemplateId());
        }
        if(StringUtils.isNotNullString(cribParameterDefineDto.getTemplateDesc())){
            specificationBuilder.add("templateDesc", SpecificationOperator.Operator.likeAll.name(),cribParameterDefineDto.getTemplateDesc());
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

    @Override
    public void exportExcel(GribParameterDefineDto gribParameterDefineDto) {
        List<GribParameterDefineDto> gribParameterDefineDtos = this.findByParam(gribParameterDefineDto);
        List<GribParameterDefineEntity> gribParameterDefineEntities = gribParameterDefineMapstruct.toEntity(gribParameterDefineDtos);
        ExcelUtil<GribParameterDefineEntity> util=new ExcelUtil(GribParameterDefineEntity.class);
        util.exportExcel(gribParameterDefineEntities,"GRIB参数定义");
    }

    @Override
    public List<GribParameterDefineDto> findByParam(GribParameterDefineDto gribParameterDefineDto) {

        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(com.piesat.common.utils.StringUtils.isNotNullString(gribParameterDefineDto.getGribVersion())){
            specificationBuilder.add("gribVersion", SpecificationOperator.Operator.eq.name(),gribParameterDefineDto.getGribVersion());
        }
        if(com.piesat.common.utils.StringUtils.isNotNullString(gribParameterDefineDto.getParameterId())){
            specificationBuilder.add("parameterId", SpecificationOperator.Operator.eq.name(),gribParameterDefineDto.getParameterId());
        }
        if(com.piesat.common.utils.StringUtils.isNotNullString(gribParameterDefineDto.getEleCodeShort())){
            specificationBuilder.add("eleCodeShort", SpecificationOperator.Operator.eq.name(),gribParameterDefineDto.getEleCodeShort());
        }
        List<GribParameterDefineEntity> gribParameterDefineEntities = this.getAll(specificationBuilder.generateSpecification());
        List<GribParameterDefineDto> gribParameterDefineDtos = this.gribParameterDefineMapstruct.toDto(gribParameterDefineEntities);
        return gribParameterDefineDtos;
    }
}
