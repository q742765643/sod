package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.dm.dao.DataServerConfigDao;
import com.piesat.dm.entity.DataServerConfigEntity;
import com.piesat.dm.entity.GridAreaEntity;
import com.piesat.dm.rpc.api.DataServerConfigService;
import com.piesat.dm.rpc.dto.DataServerConfigDto;
import com.piesat.dm.rpc.mapper.DataServerConfigMapper;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    @Override
    public BaseDao<DataServerConfigEntity> getBaseDao() {
        return this.dataServerConfigDao;
    }

    @Override
    public DataServerConfigDto saveDto(DataServerConfigDto dataServerConfigDto) {
        DataServerConfigEntity dataServerConfigEntity = this.dataServerConfigMapper.toEntity(dataServerConfigDto);
        dataServerConfigEntity = this.save(dataServerConfigEntity);
        return this.dataServerConfigMapper.toDto(dataServerConfigEntity);
    }

    @Override
    public DataServerConfigDto getDotById(String id) {
        DataServerConfigEntity dataServerConfigEntity = this.getById(id);
        return this.dataServerConfigMapper.toDto(dataServerConfigEntity);
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
}
