package com.piesat.dm.rpc.service.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.common.constants.ConstantsMsg;
import com.piesat.dm.common.enums.StatusEnum;
import com.piesat.dm.dao.dataclass.DataClassApplyDao;
import com.piesat.dm.entity.dataclass.DataClassApplyEntity;
import com.piesat.dm.rpc.api.ReviewLogService;
import com.piesat.dm.rpc.api.database.SchemaService;
import com.piesat.dm.rpc.api.dataclass.DataClassApplyService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.api.dataclass.DataLogicService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.api.datatable.ShardingService;
import com.piesat.dm.rpc.api.datatable.TableColumnService;
import com.piesat.dm.rpc.api.datatable.TableIndexService;
import com.piesat.dm.rpc.dto.ReviewLogDto;
import com.piesat.dm.rpc.dto.database.SchemaDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassApplyDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassLogicDto;
import com.piesat.dm.rpc.dto.datatable.DataTableInfoDto;
import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import com.piesat.dm.rpc.dto.datatable.TableIndexDto;
import com.piesat.dm.rpc.dto.datatable.TablePartDto;
import com.piesat.dm.rpc.mapper.dataclass.DataClassApplyMapper;
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
public class DataClassApplyServiceImpl extends BaseService<DataClassApplyEntity> implements DataClassApplyService {
    @Autowired
    private DataClassApplyDao dataClassApplyDao;
    @Autowired
    private DataClassService dataClassService;
    @Autowired
    private DataClassApplyMapper dataClassApplyMapper;
    @Autowired
    private TableColumnService tableColumnService;
    @Autowired
    private TableIndexService tableIndexService;
    @Autowired
    private ShardingService shardingService;
    @Autowired
    private ReviewLogService reviewLogService;
    @Autowired
    private DataLogicService dataLogicService;
    @Autowired
    private DataTableService dataTableService;
    @Autowired
    private SchemaService schemaService;


    @Override
    public BaseDao<DataClassApplyEntity> getBaseDao() {
        return dataClassApplyDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultT saveDto(DataClassApplyDto dataClassApplyDto) {
        DataClassDto dataClassDto = this.dataClassService.findByDataClassId(dataClassApplyDto.getDataClassId());
        if (dataClassDto != null) {
            return ResultT.failed(ConstantsMsg.MSG15);
        }
        List<DataClassLogicDto> dataClassLogicList = dataClassApplyDto.getDataLogicList();
        List<TableColumnDto> tableColumns = dataClassApplyDto.getColumns();
        List<TableIndexDto> tableIndexList = dataClassApplyDto.getTableIndexList();
        TablePartDto tablePart = dataClassApplyDto.getTablePart();
        DataClassApplyEntity save = this.save(this.dataClassApplyMapper.toEntity(dataClassApplyDto));
        save.setStatus(StatusEnum.待审核.getCode());
        DataClassApplyDto dataClassApply = this.dataClassApplyMapper.toDto(save);
        if (dataClassLogicList != null && !dataClassLogicList.isEmpty()) {
            dataClassLogicList.forEach(e -> {
                this.dataLogicService.saveDto(e);
            });
        } else {
            Optional.ofNullable(tableColumns).ifPresent(e -> {
                e.forEach(e1 -> {
                    e1.setTableId(save.getId());
                });
                List<TableColumnDto> tableColumnDtos = this.tableColumnService.saveDtoList(e);
                dataClassApply.setColumns(tableColumnDtos);
            });
            Optional.ofNullable(tableIndexList).ifPresent(e -> {
                e.forEach(e1 -> {
                    e1.setTableId(save.getId());
                    this.tableIndexService.saveDto(e1);
                });
            });
            Optional.ofNullable(tablePart).ifPresent(e -> {
                this.shardingService.saveDto(e);
            });
        }

        ReviewLogDto rl = new ReviewLogDto();
        rl.setBindId(save.getId());
        rl.setUserId(save.getUserId());
        rl.setStatusInfo(ConstantsMsg.STATUS1);
        this.reviewLogService.saveDto(rl);
        return ResultT.success(dataClassApply);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultT review(DataClassApplyDto dca) {
        DataClassApplyEntity dca_ = this.dataClassApplyMapper.toEntity(dca);
        if (StatusEnum.match(dca_.getStatus()) == StatusEnum.审核未通过) {
            dca_ = this.save(dca_);
        } else if (StatusEnum.match(dca_.getStatus()) == StatusEnum.审核通过) {
            DataClassDto dataClass = dca.getDataClass();
            DataClassDto dataClassDto = this.dataClassService.saveDto(dataClass);
            List<DataClassLogicDto> dataLogicList = dca.getDataLogicList();
            if (dataLogicList != null && !dataLogicList.isEmpty()) {
                this.dataLogicService.saveList(dataLogicList);
            } else {
                DataTableInfoDto tableInfo = dca.getTableInfo();
                DataTableInfoDto dataTableInfoDto = this.dataTableService.saveDto(tableInfo);
                DataClassLogicDto d = new DataClassLogicDto();
                d.setTableId(dataTableInfoDto.getId());
                d.setDataClassId(dataClassDto.getDataClassId());
                this.dataLogicService.saveDto(d);
            }
        }
        return ResultT.success(dca_);
    }

    @Override
    public ResultT list(PageForm<DataClassApplyDto> pageForm) {
        DataClassApplyDto t = pageForm.getT();
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isEmpty(t.getUserId())) {
            return ResultT.failed(ConstantsMsg.MSG16);
        }
        specificationBuilder.add("userId", SpecificationOperator.Operator.eq.name(), t.getUserId());
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime", "reviewTime");
        PageBean pageBean = this.getPage(specificationBuilder.generateSpecification(), pageForm, sort);
        List<DataClassApplyEntity> list = (List<DataClassApplyEntity>) pageBean.getPageData();
        List<DataClassApplyDto> dataClassApplyDtos = this.dataClassApplyMapper.toDto(list);
        dataClassApplyDtos.forEach(e -> {
            if (StringUtils.isNotEmpty(e.getDatabaseId())) {
                SchemaDto dotById = this.schemaService.getDotById(e.getDatabaseId());
                if (dotById != null) {
                    e.setDatabaseName(dotById.getDatabase().getDatabaseName());
                }
            }
        });
        dataClassApplyDtos.forEach(e -> {
            if (e.getSubType().equals(1)) {
                List<DataClassLogicDto> byDataClassId = this.dataLogicService.findByDataClassId(e.getDataClassId());
                e.setDataLogicList(byDataClassId);
            }
        });
        pageBean.setPageData(dataClassApplyDtos);
        return ResultT.success(pageBean);
    }

    @Override
    public DataClassApplyDto getDotById(String id) {
        DataClassApplyDto dataClassApplyDto = this.dataClassApplyMapper.toDto(this.getById(id));
        List<TableColumnDto> tableColumn = this.tableColumnService.findByTableId(dataClassApplyDto.getId());
        dataClassApplyDto.setColumns(tableColumn);
        List<TableIndexDto> tableIndex = this.tableIndexService.findByTableId(dataClassApplyDto.getId());
        dataClassApplyDto.setTableIndexList(tableIndex);
        TablePartDto tablePartDto = this.shardingService.getDotByTableId(dataClassApplyDto.getId());
        dataClassApplyDto.setTablePart(tablePartDto);
        return dataClassApplyDto;
    }

    @Override
    public List<DataClassApplyDto> all() {
        return this.dataClassApplyMapper.toDto(this.getAll());
    }

}
