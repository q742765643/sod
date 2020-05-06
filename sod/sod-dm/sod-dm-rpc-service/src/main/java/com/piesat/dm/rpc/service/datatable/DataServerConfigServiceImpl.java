package com.piesat.dm.rpc.service.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.dm.dao.datatable.DataServerConfigDao;
import com.piesat.dm.entity.datatable.DataServerConfigEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.datatable.DataServerConfigService;
import com.piesat.dm.rpc.dto.datatable.DataServerConfigDto;
import com.piesat.dm.rpc.mapper.datatable.DataServerConfigMapper;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 服务信息配置
 *
 * @author cwh
 * @date 2020年 02月12日 15:57:27
 */
@Service
public class DataServerConfigServiceImpl extends BaseService<DataServerConfigEntity> implements DataServerConfigService {
    @Autowired
    private DataServerConfigDao dataServerConfigDao;
    @Autowired
    private DataServerConfigMapper dataServerConfigMapper;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;

    @Override
    public BaseDao<DataServerConfigEntity> getBaseDao() {
        return this.dataServerConfigDao;
    }

    @Override
    public DataServerConfigDto saveDto(DataServerConfigDto dataServerConfigDto) {
        DataServerConfigEntity dataServerConfigEntity = this.dataServerConfigMapper.toEntity(dataServerConfigDto);
        dataServerConfigEntity = this.saveNotNull(dataServerConfigEntity);
        return this.dataServerConfigMapper.toDto(dataServerConfigEntity);
    }

    @Override
    @Transactional
    public List<DataServerConfigDto> saveDtoList(List<DataServerConfigDto> dataServerConfigDtos) {
        List<DataServerConfigEntity> dataServerConfigEntity = this.dataServerConfigMapper.toEntity(dataServerConfigDtos);
        dataServerConfigEntity = this.dataServerConfigDao.saveAll(dataServerConfigEntity);
        return this.dataServerConfigMapper.toDto(dataServerConfigEntity);
    }

    @Override
    public DataServerConfigDto getDotById(String id) {
        DataServerConfigEntity dataServerConfigEntity = this.getById(id);
        return this.dataServerConfigMapper.toDto(dataServerConfigEntity);
    }

    @Override
    @Transactional
    public void delByIds(String ids) {
        String[] split = ids.split(",");
        this.deleteByIds(Arrays.asList(split));
    }

    @Override
    public List<DataServerConfigDto> all() {
        List<DataServerConfigEntity> all = this.getAll();
        return this.dataServerConfigMapper.toDto(all);
    }

    @Override
    public PageBean list(PageForm pageForm, String dataServiceId) {
        SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder();
        if (StringUtils.isNotBlank(dataServiceId)) {
            ssb.add("dataServiceId", SpecificationOperator.Operator.eq.name(), dataServiceId);
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        PageBean page = this.getPage(ssb.generateSpecification(), pageForm, sort);
        List<DataServerConfigEntity> pageData = (List<DataServerConfigEntity>) page.getPageData();
        page.setPageData(this.dataServerConfigMapper.toDto(pageData));
        return page;
    }

    @Override
    public int getDataServiceMaxNum(String data_service_id) {
        return this.mybatisQueryMapper.getDataServiceMaxNum(data_service_id);
    }
}
