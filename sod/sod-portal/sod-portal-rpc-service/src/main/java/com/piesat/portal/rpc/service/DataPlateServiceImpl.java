package com.piesat.portal.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.FileUtil;
import com.piesat.common.utils.StringUtils;
import com.piesat.portal.dao.DataPlateDao;
import com.piesat.portal.entity.DataPlateEntity;
import com.piesat.portal.rpc.api.DataPlateService;
import com.piesat.portal.rpc.dto.DataPlateDto;
import com.piesat.portal.rpc.mapstruct.DataPlateMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;
import java.util.List;

@Service("dataPlateService")
public class DataPlateServiceImpl extends BaseService<DataPlateEntity> implements DataPlateService {

    @Autowired
    private DataPlateDao dataPlateDao;

    @Autowired
    private DataPlateMapstruct dataPlateMapstruct;

    @Override
    public BaseDao<DataPlateEntity> getBaseDao() {
        return dataPlateDao;
    }

    @Override
    public PageBean selectPageList(PageForm<DataPlateDto> pageForm) {
        DataPlateDto dataPlateDto = pageForm.getT();
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotEmpty(dataPlateDto.getModule())){
            specificationBuilder.add("module", SpecificationOperator.Operator.eq.name(),dataPlateDto.getModule());
        }
        if(StringUtils.isNotEmpty(dataPlateDto.getDataName())){
            specificationBuilder.add("dataName", SpecificationOperator.Operator.likeAll.name(),dataPlateDto.getDataName());
        }
        if(StringUtils.isNotEmpty(dataPlateDto.getIsshow())){
            specificationBuilder.add("isshow", SpecificationOperator.Operator.eq.name(),dataPlateDto.getIsshow());
        }
        PageBean pageBean = this.getPage(specificationBuilder.generateSpecification(),pageForm,null);
        List<DataPlateEntity> dataPlateEntities = (List<DataPlateEntity>) pageBean.getPageData();
        List<DataPlateDto> dataPlateDtos = dataPlateMapstruct.toDto(dataPlateEntities);
        pageBean.setPageData(dataPlateDtos);
        return pageBean;
    }

    @Override
    public DataPlateDto getDotById(String id) {
        DataPlateEntity dataPlateEntity = this.getById(id);
        return this.dataPlateMapstruct.toDto(dataPlateEntity);
    }

    @Override
    public DataPlateDto saveDto(DataPlateDto dataPlateDto) {
        DataPlateEntity dataPlateEntity = dataPlateMapstruct.toEntity(dataPlateDto);
        File file = new File(dataPlateEntity.getIcon());
        if(file != null && file.exists()){
            byte[] bytes = FileUtil.File2byte(file);
            dataPlateEntity.setIcon(Base64.getEncoder().encodeToString(bytes));
        }
        dataPlateEntity = this.saveNotNull(dataPlateEntity);
        file.delete();
        return dataPlateMapstruct.toDto(dataPlateEntity);
    }

    @Override
    public DataPlateDto updateDto(DataPlateDto dataPlateDto) {
        DataPlateEntity dataPlateEntity = dataPlateMapstruct.toEntity(dataPlateDto);
        File file = new File(dataPlateEntity.getIcon());
        if(file != null && file.exists()){
            byte[] bytes = FileUtil.File2byte(file);
            dataPlateEntity.setIcon(Base64.getEncoder().encodeToString(bytes));
        }
        dataPlateEntity = this.saveNotNull(dataPlateEntity);
        file.delete();
        return dataPlateMapstruct.toDto(dataPlateEntity);
    }


}
