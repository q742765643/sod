package com.piesat.dm.rpc.service.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.dm.dao.datatable.GridDecodingDao;
import com.piesat.dm.entity.datatable.GridDecodingEntity;
import com.piesat.dm.rpc.api.datatable.GridDecodingService;
import com.piesat.dm.rpc.dto.datatable.GridDecodingDto;
import com.piesat.dm.rpc.dto.datatable.GridDecodingList;
import com.piesat.dm.rpc.mapper.datatable.GridDecodingMapper;
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
 * 编码配置
 *
 * @author cwh
 * @date 2020年 02月12日 14:47:50
 */
@Service
public class GridDecodingServiceImpl extends BaseService<GridDecodingEntity> implements GridDecodingService {

    @Autowired
    private GridDecodingDao gridDecodingDao;
    @Autowired
    private GridDecodingMapper gridDecodingMapper;


    @Override
    public BaseDao<GridDecodingEntity> getBaseDao() {
        return this.gridDecodingDao;
    }

    @Override
    public GridDecodingDto saveDto(GridDecodingDto gridDecodingDto) {
        GridDecodingEntity gridDecodingEntity = this.gridDecodingMapper.toEntity(gridDecodingDto);
        gridDecodingEntity = this.saveNotNull(gridDecodingEntity);
        return this.gridDecodingMapper.toDto(gridDecodingEntity);
    }

    @Override
    @Transactional
    public List<GridDecodingDto> saveList(GridDecodingList gridDecodingList) {
        List<GridDecodingEntity> gridDecodingEntity = this.gridDecodingMapper.toEntity(gridDecodingList.getGridDecodingList());
        gridDecodingEntity = this.saveNotNull(gridDecodingEntity);
        return this.gridDecodingMapper.toDto(gridDecodingEntity);
    }

    @Override
    public GridDecodingDto getDotById(String id) {
        GridDecodingEntity gridDecodingEntity = this.getById(id);
        return this.gridDecodingMapper.toDto(gridDecodingEntity);
    }

    @Override
    @Transactional
    public void delByIds(String ids) {
        String[] split = ids.split(",");
        this.deleteByIds(Arrays.asList(split));
    }

    @Override
    public List<GridDecodingDto> all() {
        List<GridDecodingEntity> all = this.getAll();
        return this.gridDecodingMapper.toDto(all);
    }

    @Override
    public PageBean list(PageForm pageForm,String dataServiceId) {
        SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder();
        if (StringUtils.isNotBlank(dataServiceId)) {
            ssb.add("dataServiceId", SpecificationOperator.Operator.eq.name(), dataServiceId);
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        PageBean page = this.getPage(ssb.generateSpecification(), pageForm, sort);
        List<GridDecodingEntity> pageData = (List<GridDecodingEntity>)page.getPageData();
        page.setPageData(this.gridDecodingMapper.toDto(pageData));
        return page;
    }
}
