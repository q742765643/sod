package com.piesat.portal.rpc.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.portal.dao.FileManageDao;
import com.piesat.portal.entity.FileManageEntity;
import com.piesat.portal.mapper.FileManageMapper;
import com.piesat.portal.rpc.api.FileManageService;
import com.piesat.portal.rpc.dto.FileManageDto;
import com.piesat.portal.rpc.mapstruct.FileManageMapstruct;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("fileManageService")
public class FileManageServiceImpl extends BaseService<FileManageEntity> implements FileManageService {

    @Autowired
    private FileManageDao fileManageDao;

    @Autowired
    private FileManageMapstruct fileManageMapstruct;

    @Autowired
    private FileManageMapper fileManageMapper;

    @Override
    public BaseDao<FileManageEntity> getBaseDao() {
        return fileManageDao;
    }

    @Override
    public PageBean selectPageList(PageForm<FileManageDto> pageForm) {
        FileManageEntity fileManageEntity = fileManageMapstruct.toEntity(pageForm.getT());
        PageHelper.startPage(pageForm.getCurrentPage(),pageForm.getPageSize());

        if(StringUtils.isNotBlank(fileManageEntity.getFileName())){
            fileManageEntity.setFileName("%"+fileManageEntity.getFileName()+"%");
        }

        List<FileManageEntity> fileManageEntities = fileManageMapper.selectPageList(fileManageEntity);
        PageInfo<FileManageEntity> pageInfo = new PageInfo<>(fileManageEntities);
        //获取当前页数据
        List<FileManageDto> fileManageDtos= fileManageMapstruct.toDto(pageInfo.getList());
        PageBean pageBean=new PageBean(pageInfo.getTotal(),pageInfo.getPages(),fileManageDtos);
        return pageBean;
    }

    @Override
    public FileManageDto saveDto(FileManageDto fileManageDto) {
        UserDto loginUser = (UserDto) SecurityUtils.getSubject().getPrincipal();
        FileManageEntity fileManageEntity = fileManageMapstruct.toEntity(fileManageDto);
        fileManageEntity.setCreateBy(loginUser.getNickName());
        fileManageEntity = this.saveNotNull(fileManageEntity);
        return fileManageMapstruct.toDto(fileManageEntity);
    }

    @Override
    public FileManageDto getDotById(String id) {
        FileManageEntity fileManageEntity = this.getById(id);
        return this.fileManageMapstruct.toDto(fileManageEntity);
    }

    @Override
    public FileManageDto updateDto(FileManageDto fileManageDto) {
        UserDto loginUser = (UserDto) SecurityUtils.getSubject().getPrincipal();
        FileManageEntity fileManageEntity = fileManageMapstruct.toEntity(fileManageDto);
        fileManageEntity.setCreateBy(loginUser.getNickName());
        fileManageEntity = this.saveNotNull(fileManageEntity);
        return fileManageMapstruct.toDto(fileManageEntity);
    }

}
