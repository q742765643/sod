package com.piesat.dm.rpc.service.dataclass;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.piesat.common.config.DatabseType;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.poi.ExcelUtil;
import com.piesat.dm.common.tree.BaseParser;
import com.piesat.dm.common.tree.TreeLevel;
import com.piesat.dm.dao.database.SchemaDao;
import com.piesat.dm.dao.dataclass.DataClassDao;
import com.piesat.dm.dao.dataclass.DataClassServiceCodeDao;
import com.piesat.dm.dao.dataclass.DataLogicDao;
import com.piesat.dm.dao.datatable.DataTableDao;
import com.piesat.dm.dao.datatable.ShardingDao;
import com.piesat.dm.dao.datatable.TableColumnDao;
import com.piesat.dm.dao.datatable.TableIndexDao;
import com.piesat.dm.entity.database.SchemaEntity;
import com.piesat.dm.entity.dataclass.DataClassBaseInfoEntity;
import com.piesat.dm.entity.dataclass.DataClassEntity;
import com.piesat.dm.entity.dataclass.DataClassServiceCodeEntity;
import com.piesat.dm.mapper.MybatisPageMapper;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.AdvancedConfigService;
import com.piesat.dm.rpc.api.dataapply.DataAuthorityApplyService;
import com.piesat.dm.rpc.api.dataapply.NewdataApplyService;
import com.piesat.dm.rpc.api.dataapply.NewdataTableColumnService;
import com.piesat.dm.rpc.api.database.DatabaseUserService;
import com.piesat.dm.rpc.api.dataclass.*;
import com.piesat.dm.rpc.api.special.DatabaseSpecialReadWriteService;
import com.piesat.dm.rpc.api.special.DatabaseSpecialService;
import com.piesat.dm.rpc.dto.AdvancedConfigDto;
import com.piesat.dm.rpc.dto.dataapply.NewdataApplyDto;
import com.piesat.dm.rpc.dto.database.DatabaseUserDto;
import com.piesat.dm.rpc.dto.dataclass.*;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialDto;
import com.piesat.dm.rpc.mapper.dataclass.DataClassBaseInfoMapper;
import com.piesat.dm.rpc.mapper.dataclass.DataClassMapper;
import com.piesat.dm.rpc.mapper.dataclass.DataClassServiceCodeMapper;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 资料服务编码信息
 *
 * @author cwh
 * @date 2019年 11月22日 16:31:15
 */
@Service
public class DataClassServiceCodeServiceImpl extends BaseService<DataClassServiceCodeEntity> implements DataClassServiceCodeService {
    @Autowired
    private DataClassServiceCodeDao dataClassServiceCodeDao;
    @Autowired
    private DataClassServiceCodeMapper dataClassServiceCodeMapper;

    @Override
    public BaseDao<DataClassServiceCodeEntity> getBaseDao() {
        return dataClassServiceCodeDao;
    }


    @Override
    public DataClassServiceCodeDto saveDto(DataClassServiceCodeDto dataClassServiceCodeDto) {
        DataClassServiceCodeEntity dataClassServiceCodeEntity = this.dataClassServiceCodeMapper.toEntity(dataClassServiceCodeDto);
        dataClassServiceCodeEntity = this.saveNotNull(dataClassServiceCodeEntity);
        return this.dataClassServiceCodeMapper.toDto(dataClassServiceCodeEntity);
    }

    @Override
    public DataClassServiceCodeDto getDotById(String id) {
        DataClassServiceCodeEntity dataClassServiceCodeEntity = this.getById(id);
        return this.dataClassServiceCodeMapper.toDto(dataClassServiceCodeEntity);
    }

    @Override
    public void deleteById(String id) {
        this.delete(id);
    }

    @Override
    public List<DataClassServiceCodeDto> all() {
        List<DataClassServiceCodeEntity> all = this.getAll();
        return this.dataClassServiceCodeMapper.toDto(all);
    }
}
