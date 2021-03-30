package com.piesat.dm.rpc.service.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.common.constants.ConstantsMsg;
import com.piesat.dm.common.enums.StatusEnum;
import com.piesat.dm.dao.dataclass.DataClassInfoDao;
import com.piesat.dm.entity.dataclass.DataClassInfoEntity;
import com.piesat.dm.rpc.api.ReviewLogService;
import com.piesat.dm.rpc.api.database.SchemaService;
import com.piesat.dm.rpc.api.dataclass.DataClassInfoService;
import com.piesat.dm.rpc.api.dataclass.DataClassInfoService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.api.dataclass.DataLogicService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.api.datatable.ShardingService;
import com.piesat.dm.rpc.api.datatable.TableColumnService;
import com.piesat.dm.rpc.api.datatable.TableIndexService;
import com.piesat.dm.rpc.dto.ReviewLogDto;
import com.piesat.dm.rpc.dto.database.SchemaDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassInfoDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassLogicDto;
import com.piesat.dm.rpc.dto.datatable.DataTableInfoDto;
import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import com.piesat.dm.rpc.dto.datatable.TableIndexDto;
import com.piesat.dm.rpc.dto.datatable.TablePartDto;
import com.piesat.dm.rpc.mapper.dataclass.DataClassInfoMapper;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 资料申请
 *
 * @author cwh
 * @date 2019年 11月22日 16:31:15
 */
@Service
public class DataClassInfoServiceImpl extends BaseService<DataClassInfoEntity> implements DataClassInfoService {
    @Autowired
    private DataClassInfoDao dataClassInfoDao;
    @Autowired
    private DataClassService dataClassService;
    @Autowired
    private DataClassInfoMapper dataClassInfoMapper;
    @Autowired
    private TableColumnService tableColumnService;
    @Autowired
    private TableIndexService tableIndexService;
    @Autowired
    private ShardingService shardingService;
    @Autowired
    private DataLogicService dataLogicService;
    @Autowired
    private DataTableService dataTableService;
    @Autowired
    private SchemaService schemaService;


    @Override
    public BaseDao<DataClassInfoEntity> getBaseDao() {
        return dataClassInfoDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultT saveDto(DataClassInfoDto dataClassInfoDto) {
        this.saveNotNull(this.dataClassInfoMapper.toEntity(dataClassInfoDto));
        return ResultT.success();
    }

    @Override
    public ResultT apply(DataClassInfoDto dataClassInfoDto) {
        return null;
    }


    @Override
    public ResultT list(PageForm<DataClassInfoDto> pageForm) {
        DataClassInfoDto t = pageForm.getT();
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        specificationBuilder.add("dataClassId", SpecificationOperator.Operator.likeAll.name(), t.getDataClassId());
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime", "reviewTime");
        PageBean pageBean = this.getPage(specificationBuilder.generateSpecification(), pageForm, sort);
        List<DataClassInfoEntity> list = (List<DataClassInfoEntity>) pageBean.getPageData();
        List<DataClassInfoDto> dataClassInfoDtos = this.dataClassInfoMapper.toDto(list);
        pageBean.setPageData(dataClassInfoDtos);
        return ResultT.success(pageBean);
    }

    @Override
    public DataClassInfoDto getDotById(String id) {
        DataClassInfoDto dataClassInfoDto = this.dataClassInfoMapper.toDto(this.getById(id));
        return dataClassInfoDto;
    }

    @Override
    public List<DataClassInfoDto> all() {
        return this.dataClassInfoMapper.toDto(this.getAll());
    }

}
