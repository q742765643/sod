package com.piesat.dm.rpc.service.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.dm.dao.datatable.TableDataStatisticsDao;
import com.piesat.dm.entity.datatable.TableDataStatisticsEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.dataclass.DataOnlineTimeService;
import com.piesat.dm.rpc.api.datatable.TableDataStatisticsService;
import com.piesat.dm.rpc.dto.dataclass.DataOnlineTimeDto;
import com.piesat.dm.rpc.dto.datatable.TableDataStatisticsDto;
import com.piesat.dm.rpc.mapper.datatable.TableDataStatisticsMapper;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表数据统计
 *
 * @author cwh
 * @date 2020年 02月13日 14:52:57
 */
@Service
public class TableDataStatisticsServiceImpl extends BaseService<TableDataStatisticsEntity> implements TableDataStatisticsService {
    @Autowired
    private TableDataStatisticsDao tableDataStatisticsDao;
    @Autowired
    private TableDataStatisticsMapper tableDataStatisticsMapper;

    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;

    @Autowired
    private DataOnlineTimeService dataOnlineTimeService;

    @Override
    public BaseDao<TableDataStatisticsEntity> getBaseDao() {
        return this.tableDataStatisticsDao;
    }

    @Override
    public TableDataStatisticsDto saveDto(TableDataStatisticsDto tableDataStatisticsDto) {
        TableDataStatisticsEntity tableDataStatisticsEntity = this.tableDataStatisticsMapper.toEntity(tableDataStatisticsDto);
        tableDataStatisticsEntity = this.save(tableDataStatisticsEntity);
        return this.tableDataStatisticsMapper.toDto(tableDataStatisticsEntity);
    }

    @Override
    public TableDataStatisticsDto getDotById(String id) {
        TableDataStatisticsEntity tableDataStatisticsEntity = this.getById(id);
        return this.tableDataStatisticsMapper.toDto(tableDataStatisticsEntity);
    }

    @Override
    public List<TableDataStatisticsDto> all() {
        List<TableDataStatisticsEntity> all = this.getAll();
        return this.tableDataStatisticsMapper.toDto(all);
    }

    @Override
    public PageBean list(PageForm pageForm,String tableId) {
        SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder();
        if (StringUtils.isNotBlank(tableId)) {
            ssb.add("tableId", SpecificationOperator.Operator.eq.name(), tableId);
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        PageBean page = this.getPage(ssb.generateSpecification(), pageForm, sort);
        List<TableDataStatisticsEntity> pageData = (List<TableDataStatisticsEntity>)page.getPageData();
        page.setPageData(this.tableDataStatisticsMapper.toDto(pageData));
        return page;
    }

    @Override
    public Map<String, Object> getOnlineTime(String classDataId, String statisticDate) {

        Map<String,Object> map = new HashMap<String,Object>();

        List<Map<String,Object>> onlineList = mybatisQueryMapper.getOnlineTime(classDataId,statisticDate);
        DataOnlineTimeDto dataOnlineTimeDto = dataOnlineTimeService.findByDataClassId(classDataId);

        if (dataOnlineTimeDto!=null && !"0".equals(dataOnlineTimeDto.getUsing())){
            if ("today".equals(dataOnlineTimeDto.getEndTimeFlag())) {
                onlineList.get(0).put("END_TIME",new Date());
            }else if (dataOnlineTimeDto.getEndTime()!=null){
                onlineList.get(0).put("END_TIME", dataOnlineTimeDto.getEndTime());
            }
            if (dataOnlineTimeDto.getBeginTime()!=null){
                onlineList.get(0).put("BEGIN_TIME",dataOnlineTimeDto.getBeginTime());
            }
        }
        if(null != onlineList && onlineList.size()>0) {
            map.put("online", onlineList.get(0));
        }
        return map;
    }

}
