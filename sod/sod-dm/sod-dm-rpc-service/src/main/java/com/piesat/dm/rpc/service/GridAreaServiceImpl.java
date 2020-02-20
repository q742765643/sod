package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.dm.dao.GridAreaDao;
import com.piesat.dm.entity.GridAreaEntity;
import com.piesat.dm.rpc.api.GridAreaService;
import com.piesat.dm.rpc.dto.GridAreaDto;
import com.piesat.dm.rpc.mapper.GridAreaMapper;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 区域信息
 *
 * @author cwh
 * @date 2020年 02月10日 14:36:52
 */
@Service
public class GridAreaServiceImpl extends BaseService<GridAreaEntity> implements GridAreaService {

    @Autowired
    private GridAreaDao gridAreaDao;
    @Autowired
    private GridAreaMapper gridAreaMapper;

    @Override
    public BaseDao<GridAreaEntity> getBaseDao() {
        return this.gridAreaDao;
    }

    @Override
    public GridAreaDto saveDto(GridAreaDto gridAreaDto) {
        GridAreaEntity gridAreaEntity = this.gridAreaMapper.toEntity(gridAreaDto);
        gridAreaEntity = this.save(gridAreaEntity);
        return this.gridAreaMapper.toDto(gridAreaEntity);
    }

    @Override
    public GridAreaDto getDotById(String id) {
        GridAreaEntity gridAreaEntity = this.getById(id);
        return this.gridAreaMapper.toDto(gridAreaEntity);
    }

    @Override
    public List<GridAreaDto> all() {
        List<GridAreaEntity> all = this.getAll();
        return this.gridAreaMapper.toDto(all);
    }

    @Override
    public PageBean list(PageForm pageForm, String dataServiceId) {
        SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder();
        if (StringUtils.isNotBlank(dataServiceId)) {
            ssb.add("dataServiceId", SpecificationOperator.Operator.eq.name(), dataServiceId);
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        PageBean page = this.getPage(ssb.generateSpecification(), pageForm, sort);
        List<GridAreaEntity> pageData = (List<GridAreaEntity>) page.getPageData();
        page.setPageData(this.gridAreaMapper.toDto(pageData));
        return page;
    }

    @Override
    public List<GridAreaDto> findByDataServiceId(String dataServiceId) {
        List<GridAreaEntity> all = this.gridAreaDao.findByDataServiceId(dataServiceId);
        return this.gridAreaMapper.toDto(all);
    }
}
