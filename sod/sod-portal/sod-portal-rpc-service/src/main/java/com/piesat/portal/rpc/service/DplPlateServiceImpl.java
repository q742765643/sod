package com.piesat.portal.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.FileUtil;
import com.piesat.common.utils.StringUtils;
import com.piesat.portal.dao.DplPlateDao;
import com.piesat.portal.entity.DplPlateEntity;
import com.piesat.portal.rpc.api.DplPlateService;
import com.piesat.portal.rpc.dto.DplPlateDto;
import com.piesat.portal.rpc.mapstruct.DplPlateMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Base64;
import java.util.List;

@Service("dplPlateService")
public class DplPlateServiceImpl extends BaseService<DplPlateEntity> implements DplPlateService {

    @Autowired
    private DplPlateDao dplPlateDao;

    @Autowired
    private DplPlateMapstruct dplPlateMapstruct;

    @Override
    public BaseDao<DplPlateEntity> getBaseDao() {
        return dplPlateDao;
    }

    @Override
    public PageBean selectPageList(PageForm<DplPlateDto> pageForm) {
        DplPlateDto dplPlateDto = pageForm.getT();
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotEmpty(dplPlateDto.getAlgName())){
            specificationBuilder.add("algName", SpecificationOperator.Operator.likeAll.name(),dplPlateDto.getAlgName());
        }
        if(StringUtils.isNotEmpty(dplPlateDto.getIsshow())){
            specificationBuilder.add("isshow", SpecificationOperator.Operator.eq.name(),dplPlateDto.getIsshow());
        }
        PageBean pageBean = this.getPage(specificationBuilder.generateSpecification(),pageForm,null);
        List<DplPlateEntity> dplPlateEntities = (List<DplPlateEntity>) pageBean.getPageData();
        List<DplPlateDto> dataPlateDtos = dplPlateMapstruct.toDto(dplPlateEntities);
        pageBean.setPageData(dataPlateDtos);
        return pageBean;
    }

    @Override
    public DplPlateDto getDotById(String id) {
        DplPlateEntity dplPlateEntity = this.getById(id);
        return this.dplPlateMapstruct.toDto(dplPlateEntity);
    }

    @Override
    public DplPlateDto saveDto(DplPlateDto dplPlateDto) {
        DplPlateEntity dplPlateEntity = dplPlateMapstruct.toEntity(dplPlateDto);
        File file = new File(dplPlateDto.getIcon());
        if(file != null && file.exists()){
            byte[] bytes = FileUtil.File2byte(file);
            dplPlateEntity.setIcon(Base64.getEncoder().encodeToString(bytes));
        }
        dplPlateEntity = this.saveNotNull(dplPlateEntity);
        file.delete();
        return dplPlateMapstruct.toDto(dplPlateEntity);
    }

    @Override
    public DplPlateDto updateDto(DplPlateDto dplPlateDto) {
        DplPlateEntity dplPlateEntity = dplPlateMapstruct.toEntity(dplPlateDto);
        File file = new File(dplPlateDto.getIcon());
        if(file != null && file.exists()){
            byte[] bytes = FileUtil.File2byte(file);
            dplPlateEntity.setIcon(Base64.getEncoder().encodeToString(bytes));
        }
        dplPlateEntity = this.saveNotNull(dplPlateEntity);
        file.delete();
        return dplPlateMapstruct.toDto(dplPlateEntity);
    }
}
