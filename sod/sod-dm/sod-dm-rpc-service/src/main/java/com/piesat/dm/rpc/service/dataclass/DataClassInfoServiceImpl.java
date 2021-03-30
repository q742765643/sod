package com.piesat.dm.rpc.service.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.DateUtils;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.common.constants.ConstantsMsg;
import com.piesat.dm.common.enums.StatusEnum;
import com.piesat.dm.dao.dataclass.DataClassInfoDao;
import com.piesat.dm.entity.dataclass.DataClassInfoEntity;
import com.piesat.dm.rpc.api.ReviewLogService;
import com.piesat.dm.rpc.api.database.SchemaService;
import com.piesat.dm.rpc.api.dataclass.*;
import com.piesat.dm.rpc.api.dataclass.DataClassInfoService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.api.datatable.ShardingService;
import com.piesat.dm.rpc.api.datatable.TableColumnService;
import com.piesat.dm.rpc.api.datatable.TableIndexService;
import com.piesat.dm.rpc.dto.ReviewLogDto;
import com.piesat.dm.rpc.dto.database.SchemaDto;
import com.piesat.dm.rpc.dto.dataclass.*;
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

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 资料申请
 *
 * @author cwh
 * @date 2019年 11月22日 16:31:15
 */
@Service
public class DataClassInfoServiceImpl extends BaseService<DataClassInfoEntity> implements DataClassInfoService {

    private final String MLDT = "MLDT.";
    private final String FORMAT = "yyyy.MMdd";

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
    @Autowired
    private DataClassLabelDefService dataClassLabelDefService;
    @Autowired
    private DataClassLabelService dataClassLabelService;


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
    @Transactional(rollbackFor = Exception.class)
    public ResultT apply(DataClassInfoDto dataClassInfoDto) {
        try {
            String dataClassId = dataClassInfoDto.getDataClassId();
            if (StringUtils.isEmpty(dataClassId)) {
                String sf = MLDT + DateUtils.dateTimeNow(FORMAT);
                List<DataClassInfoEntity> list = this.dataClassInfoDao.findByDataTypeOrderByDataClassIdDesc(1);
                if (list.size() > 0) {
                    String dataClassId1 = list.get(0).getDataClassId();
                    String su = dataClassId1.substring(0, 14);
                    String s1 = dataClassId1.substring(15, 19);
                    if (sf.equals(su)){
                        int i = Integer.valueOf(s1) + 1;
                        DecimalFormat df = new DecimalFormat("0000");
                        dataClassId = sf + "." + df.format(i);
                    }else {
                        dataClassId = sf + ".0001";
                    }
                }
                dataClassInfoDto.setDataClassId(dataClassId);
            }
            List<DataClassLabelDto> dataClassLabelList = dataClassInfoDto.getDataClassLabelList();
            for (DataClassLabelDto dataClassLabelDto : dataClassLabelList) {
                String labelKey = dataClassLabelDto.getLabelKey();
                if (StringUtils.isEmpty(labelKey)){
                    DataClassLabelDefDto dl = new DataClassLabelDefDto();
                    dl.setLabelName(dataClassLabelDto.getLabelName());
                    dl.setRemark(dataClassLabelDto.getRemark());
                    dl.setStatus(1);
                    dl.setUserId(dataClassInfoDto.getUserId());
                    DataClassLabelDefDto dataClassLabelDefDto = this.dataClassLabelDefService.saveDto(dl);
                    dataClassLabelDto.setLabelKey(dataClassLabelDefDto.getId());
                }
                if (StringUtils.isEmpty(dataClassLabelDto.getDataClassId())){
                    dataClassLabelDto.setDataClassId(dataClassId);
                }
                this.dataClassLabelService.saveDto(dataClassLabelDto);
            }
            List<DataClassLogicDto> dataClassLogicList = dataClassInfoDto.getDataClassLogicList();
            for (DataClassLogicDto dataClassLogicDto : dataClassLogicList) {
                if (StringUtils.isEmpty(dataClassLogicDto.getDataClassId())){
                    dataClassLogicDto.setDataClassId(dataClassId);
                }
            }
            this.dataLogicService.saveList(dataClassLogicList);
            dataClassInfoDto.setStatus(1);
            this.saveDto(dataClassInfoDto);
        }catch (Exception e){
            return ResultT.failed(e.getMessage());
        }
        return ResultT.success();
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
        List<DataClassLabelDto> dataClassLabelDtoList = this.dataClassLabelService.findByDataClassId(dataClassInfoDto.getDataClassId());
        List<DataClassLogicDto> dataClassLogicDtoList = this.dataLogicService.findByDataClassId(dataClassInfoDto.getDataClassId());
        dataClassInfoDto.setDataClassLabelList(dataClassLabelDtoList);
        dataClassInfoDto.setDataClassLogicList(dataClassLogicDtoList);
        return dataClassInfoDto;
    }

    @Override
    public List<DataClassInfoDto> all() {
        return this.dataClassInfoMapper.toDto(this.getAll());
    }

}
