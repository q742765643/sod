package com.piesat.dm.rpc.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.StorageConfigurationDao;
import com.piesat.dm.entity.StorageConfigurationEntity;
import com.piesat.dm.mapper.MybatisModifyMapper;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.api.StorageConfigurationService;
import com.piesat.dm.rpc.dto.StorageConfigurationDto;
import com.piesat.dm.rpc.mapper.StorageConfigurationMapper;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/9 15:28
 */
@Service
public class StorageConfigurationServiceImpl extends BaseService<StorageConfigurationEntity> implements StorageConfigurationService {
    @Autowired
    private StorageConfigurationDao storageConfigurationDao;
    @Autowired
    private StorageConfigurationMapper storageConfigurationMapper;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private MybatisModifyMapper mybatisModifyMapper;
    @Autowired
    private DataClassService dataClassService;

    @Override
    public BaseDao<StorageConfigurationEntity> getBaseDao() {
        return storageConfigurationDao;
    }

    @Override
    public PageBean selectPageList(PageForm<Map<String,String>> pageForm) {
        PageHelper.startPage(pageForm.getCurrentPage(),pageForm.getPageSize());
        List<Map<String,Object>> lists = mybatisQueryMapper.selectStorageConfigurationPageList(pageForm.getT());//自定义的接口
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(lists);
        //获取当前页数据
        PageBean pageBean=new PageBean(pageInfo.getTotal(),pageInfo.getPages(),lists);
        return pageBean;
    }

    @Override
    public PageBean storageFieldList(PageForm<Map<String,String>> pageForm) {
        PageHelper.startPage(pageForm.getCurrentPage(),pageForm.getPageSize());
        List<Map<String,Object>> lists = mybatisQueryMapper.storageFieldList(pageForm.getT());//自定义的接口
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(lists);
        //获取当前页数据
        PageBean pageBean=new PageBean(pageInfo.getTotal(),pageInfo.getPages(),lists);
        return pageBean;
    }

    @Override
    public StorageConfigurationDto saveDto(StorageConfigurationDto storageConfigurationDto) {
        StorageConfigurationEntity storageConfigurationEntity = this.storageConfigurationMapper.toEntity(storageConfigurationDto);
        storageConfigurationEntity = this.storageConfigurationDao.saveNotNull(storageConfigurationEntity);
        return this.storageConfigurationMapper.toDto(storageConfigurationEntity);
    }

    @Override
    public Map<String, Object> exportTable(Map<String, String> pMap) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String,Object>> list = mybatisQueryMapper.selectStorageConfigurationPageList(pMap);
        ArrayList<String> headList = new ArrayList<>();
        headList.add("资料名称");
        headList.add("四级编码");
        headList.add("存储编码");
        headList.add("表名称");
        headList.add("数据用途");
        headList.add("数据库");
        headList.add("专题名");
        ArrayList<List<String>> lists = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            List<String> strings = new ArrayList<>();
            Map<String, Object> map = list.get(i);
            strings.add(map.get("CLASS_NAME") == null ? "" : map.get("CLASS_NAME").toString());
            strings.add(map.get("D_DATA_ID") == null ? "" : map.get("D_DATA_ID").toString());
            strings.add(map.get("DATA_CLASS_ID") == null ? "" : map.get("DATA_CLASS_ID").toString());
            strings.add(map.get("TABLE_NAME") == null ? "" : map.get("TABLE_NAME").toString());
            strings.add(map.get("LOGIC_NAME") == null ? "" : map.get("LOGIC_NAME").toString());
            strings.add(map.get("DATABASE_NAME") == null ? "" : map.get("DATABASE_NAME").toString());
            strings.add(map.get("SPECIAL_DATABASE_NAME") == null ? "" : map.get("SPECIAL_DATABASE_NAME").toString());
            lists.add(strings);
        }
        resultMap.put("headList",headList);
        resultMap.put("lists",lists);
        return resultMap;
    }

    @Override
    public List<Map<String,Object>> findByDataClassId(String dataClassId) {
        return mybatisQueryMapper.findStorageConfigurationByDataClassId(dataClassId);
    }

    @Override
    public void deleteByDataClassId(String dataClassId) {
        this.mybatisQueryMapper.deleteStorageConfigurationByDataClassId(dataClassId);
    }

    @Override
    public void updateDataAuthorityConfig(StorageConfigurationDto storageConfigurationDto) {
        StorageConfigurationEntity storageConfigurationEntity = this.storageConfigurationMapper.toEntity(storageConfigurationDto);
        this.mybatisModifyMapper.updateDataAuthorityConfig(storageConfigurationEntity);
    }

    @Override
    public List<StorageConfigurationDto> findByClassLogicId(String id) {
        return this.storageConfigurationMapper.toDto(this.storageConfigurationDao.findByClassLogicId(id));
    }
}
