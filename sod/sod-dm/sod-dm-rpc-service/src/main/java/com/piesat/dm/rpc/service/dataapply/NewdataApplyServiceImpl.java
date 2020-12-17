package com.piesat.dm.rpc.service.dataapply;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.utils.StringUtils;
import com.piesat.common.utils.UUID;
import com.piesat.dm.dao.dataapply.NewdataApplyDao;
import com.piesat.dm.dao.dataapply.NewdataTableColumnDao;
import com.piesat.dm.entity.dataapply.NewdataApplyEntity;
import com.piesat.dm.entity.dataapply.NewdataTableColumnEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.*;
import com.piesat.dm.rpc.api.dataapply.NewdataApplyService;
import com.piesat.dm.rpc.api.dataapply.NewdataTableColumnService;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.api.dataclass.DataLogicService;
import com.piesat.dm.rpc.api.dataclass.LogicDefineService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.api.datatable.ShardingService;
import com.piesat.dm.rpc.api.datatable.TableColumnService;
import com.piesat.dm.rpc.dto.*;
import com.piesat.dm.rpc.dto.dataapply.NewdataApplyDto;
import com.piesat.dm.rpc.dto.dataapply.NewdataTableColumnDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassLogicDto;
import com.piesat.dm.rpc.dto.dataclass.DataLogicDto;
import com.piesat.dm.rpc.dto.datatable.DataTableDto;
import com.piesat.dm.rpc.dto.datatable.DataTableInfoDto;
import com.piesat.dm.rpc.dto.datatable.ShardingDto;
import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import com.piesat.dm.rpc.mapper.dataapply.NewdataApplyMapper;
import com.piesat.dm.rpc.mapper.dataapply.NewdataTableColumnMapper;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/5 19:03
 */
@Service
public class NewdataApplyServiceImpl extends BaseService<NewdataApplyEntity> implements NewdataApplyService {
    @Autowired
    private NewdataApplyDao newdataApplyDao;

    @Autowired
    private NewdataApplyMapper newdataApplyMapper;

    @Autowired
    private DataClassService dataClassService;

    @Autowired
    private LogicDefineService logicDefineService;

    @Autowired
    private DataLogicService dataLogicService;

    @Autowired
    private StorageConfigurationService storageConfigurationService;

    @Autowired
    private DataTableService dataTableService;

    @Autowired
    private TableColumnService tableColumnService;
    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private ShardingService shardingService;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private NewdataTableColumnDao newdataTableColumnDao;
    @Autowired
    private NewdataTableColumnMapper newdataTableColumnMapper;
    @Autowired
    private NewdataTableColumnService newdataTableColumnService;


    @Override
    public BaseDao<NewdataApplyEntity> getBaseDao() {
        return newdataApplyDao;
    }

    @Override
    public PageBean selectPageList(PageForm<Map<String,Object>> pageForm) {
        PageHelper.startPage(pageForm.getCurrentPage(),pageForm.getPageSize());
        Object status = pageForm.getT().get("status");
        List<Map<String,Object>> lists = null;
        if (status.equals(1)){
            lists = mybatisQueryMapper.selectNewdataApplyPageList(pageForm.getT());//自定义的接口
        }else {
            lists = mybatisQueryMapper.selectDataApplyPageList(pageForm.getT());//自定义的接口
            for (Map<String,Object> m:lists ) {
                m.put("LOGIC_ID",m.get("R_LOGIC_ID"));
            }
        }

        PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(lists);
        //获取当前页数据
        PageBean pageBean=new PageBean(pageInfo.getTotal(),pageInfo.getPages(),lists);
        return pageBean;
    }

    @Override
    public NewdataApplyDto getDotById(String id) {
        NewdataApplyEntity newdataApplyEntity = this.getById(id);
        List<NewdataTableColumnEntity> byApplyId = newdataTableColumnDao.findByApplyId(id);
        List<NewdataTableColumnDto> newdataTableColumnDtos = this.newdataTableColumnMapper.toDto(byApplyId);
        NewdataApplyDto newdataApplyDto = newdataApplyMapper.toDto(newdataApplyEntity);
        newdataApplyDto.setColumnList(newdataTableColumnDtos);
        return newdataApplyDto;
    }

    @Override
    public NewdataApplyDto saveDto(NewdataApplyDto newdataApplyDto) {
        NewdataApplyEntity newdataApplyEntity = newdataApplyMapper.toEntity(newdataApplyDto);
        if(!StringUtils.isNotNullString(newdataApplyEntity.getId())){
            newdataApplyEntity.setId(UUID.randomUUID().toString());
        }
        newdataApplyEntity = this.saveNotNull(newdataApplyEntity);
        return newdataApplyMapper.toDto(newdataApplyEntity);
    }


    @Override
    public NewdataApplyDto updateDto(NewdataApplyDto newdataApplyDto) {
        NewdataApplyEntity newdataApplyEntity = newdataApplyMapper.toEntity(newdataApplyDto);
        newdataApplyEntity = this.saveNotNull(newdataApplyEntity);
        return newdataApplyMapper.toDto(newdataApplyEntity);
    }

    @Transactional
    @Override
    public NewdataApplyDto updateStatus(NewdataApplyDto newdataApplyDto) {
        NewdataApplyEntity newdataApplyEntity = newdataApplyMapper.toEntity(newdataApplyDto);
        //newdataApplyEntity = this.saveNotNull(newdataApplyEntity);
        if(newdataApplyDto.getExamineStatus() != null){
            if(newdataApplyDto.getExamineStatus().intValue() == 2 || newdataApplyDto.getExamineStatus().intValue() == 3){
                newdataApplyEntity.setExamineTime(new Date());
                UserDto loginUser = (UserDto) SecurityUtils.getSubject().getPrincipal();
                newdataApplyEntity.setExaminer(loginUser.getNickName());
            }
        }
        mybatisQueryMapper.updateNewdataApplyStatus(newdataApplyEntity);
        return newdataApplyMapper.toDto(newdataApplyEntity);
    }

    @Override
    public List<Map<String, Object>> getLogicInfo() {
        return null;
    }

    @Override
    public Map<String, Object> queryCheckByApplyId(String id) {
        List<Map<String, Object>> maps = mybatisQueryMapper.queryNewdataApplyByApplyId(id);
        return maps == null ? null : maps.get(0);
    }


    @Override
    public List<DataTableInfoDto> getDataTableByType(DataClassLogicDto dataLogicDto) {
        List<DataTableInfoDto> byDatabaseIdAndClassId = dataTableService.getByDatabaseIdAndClassId(dataLogicDto.getDatabaseId(), dataLogicDto.getDataClassId());
        return byDatabaseIdAndClassId;
    }

    @Override
    public ResultT<String> addDataStructure(TableColumnDto tableColumnDto) {
        try {
            TableColumnDto tableColumnDto1 = tableColumnService.saveDto(tableColumnDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
        return ResultT.success();
    }

    @Override
    public ResultT<String> updateDataStructure(TableColumnDto tableColumnDto) {
        //tableColumnService.updateDto(tableColumnDto);
        return ResultT.success();
    }

    @Override
    public PageBean getTableDataInfo(String tableId, String databaseId, int pageNum, int pageSize) {
        String sql = "select *  from t_sod_table_collect_info where table_id='" + tableId + "' and database_id = '" +databaseId+ "'";
        PageBean page = this.queryByNativeSQLPageMap(sql,null, new PageForm(pageNum, pageSize));
        return page;
    }

    @Override
    public Map<String, Object> getArchiveInfo(String c_datum_code) {
        String sql = "SELECT C.* FROM (SELECT A.C_DATUM_CODE,A.C_SOURSDATUM_CODE,LEVEL L FROM TAB_OMIN_CM_CC_DATUMTYPEINFO A " +
                "START WITH A.C_DATUM_CODE = '" + c_datum_code + "'" +
                "CONNECT BY PRIOR A.C_SOURSDATUM_CODE = A.C_DATUM_CODE) B,T_SOD_ARCHIVE_INFO C  " +
                "WHERE L = 2 AND B.C_SOURSDATUM_CODE = C.PRODUCTIONCODE";
        List<Map<String, Object>> list = this.queryByNativeSQL(sql);
        return list == null ? null:list.get(0);
    }

    @Override
    public List<Map<String, Object>> getByUserIdAndApplyId(NewdataApplyDto newdataApplyDto) {
        NewdataApplyEntity newdataApplyEntity = newdataApplyMapper.toEntity(newdataApplyDto);
        return mybatisQueryMapper.getByUserIdAndApplyId(newdataApplyEntity);
    }

    @Override
    public List<Map<String, Object>> getColumnByIdAndDDataId(NewdataApplyDto newdataApplyDto) {
        NewdataApplyEntity newdataApplyEntity = newdataApplyMapper.toEntity(newdataApplyDto);
        newdataApplyDto = this.getDotById(newdataApplyEntity.getId());

        List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();
        if(newdataApplyDto != null && StringUtils.isNotNullString(newdataApplyDto.getDataClassId())){
            resultMap = mybatisQueryMapper.getColumnByIdAndDDataId(newdataApplyEntity);
        }else{
            List<NewdataTableColumnEntity> tableColumnEntities = newdataTableColumnDao.findByApplyId(newdataApplyDto.getId());
            if(tableColumnEntities != null && tableColumnEntities.size() > 0){
                for (NewdataTableColumnEntity newdataTableColumnEntity: tableColumnEntities){
                    Map<String, Object> map = new HashMap<>();
                    map.put("C_ELEMENT_CODE",newdataTableColumnEntity.getCElementCode());
                    map.put("ELE_NAME",newdataTableColumnEntity.getEleName());
                    map.put("TYPE",newdataTableColumnEntity.getType());
                    map.put("ACCURACY",newdataTableColumnEntity.getAccuracy());
                    map.put("UNIT",newdataTableColumnEntity.getUnit());
                    map.put("SERIAL_NUMBER",newdataTableColumnEntity.getSerialNumber());
                    map.put("IS_NULL",newdataTableColumnEntity.getIsNull());
                    map.put("IS_PRIMARY_KEY",newdataTableColumnEntity.getIsPrimaryKey());
                    resultMap.add(map);
                }
            }
        }
        return resultMap;
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        NewdataApplyEntity newdataApplyEntity = this.getById(id);

        if(StringUtils.isNotNullString(newdataApplyEntity.getDataClassId())){
            //删除存储策略
            storageConfigurationService.deleteByDataClassId(newdataApplyEntity.getDataClassId());
            //删除表结构
            dataClassService.deleteByDataClassId(newdataApplyEntity.getDataClassId());
        }
        //删除portal申请时携带的字段信息
        newdataTableColumnService.deleteByApplyId(id);
        //删除申请信息
        if(this.exists(id)){
            this.delete(id);
        }
    }
    @Override
    public void delApply(String applyId, String dDataId) {
        NewdataApplyDto newdataApplyDto = this.getDotById(applyId);
        newdataApplyDto.setExamineStatus(4);
        NewdataApplyEntity newdataApplyEntity = newdataApplyMapper.toEntity(newdataApplyDto);
        newdataApplyDao.saveNotNull(newdataApplyEntity);
    }

    @Override
    public Map<String, Object> getSchemaInfo(String physicId, String userId) {
        Map<String,Object> result = new HashMap<String,Object>();
        try {
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("physicId", physicId);
            param.put("userId", userId);
            List<Map<String,String>> dataList = mybatisQueryMapper.getSchemaByPhysic(param);

            result.put("returnCode", "0");
            result.put("DS", dataList);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("returnCode", "1");
            result.put("returnMessage", "查询失败 : "+e.getMessage());
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getDataInfoByUserId(String userId, String dataClassId) {
        List<Map<String, Object>> dataInfoByUserId = mybatisQueryMapper.getDataInfoByUserId(userId, dataClassId);
        return dataInfoByUserId;
    }

    @Override
    public List<Map<String, Object>> getSpecialDBData(String sdbId) {
        List<Map<String, Object>> specialDBData = mybatisQueryMapper.getSpecialDBData(sdbId);
        return specialDBData;
    }

    @Override
    public List<NewdataApplyDto> findByDataClassIdAndUserId(String dataClassId, String userId) {
        List<NewdataApplyEntity> newdataApplyEntitys = this.newdataApplyDao.findByDataClassIdAndUserId(dataClassId, userId);
        return this.newdataApplyMapper.toDto(newdataApplyEntitys);
    }
}
