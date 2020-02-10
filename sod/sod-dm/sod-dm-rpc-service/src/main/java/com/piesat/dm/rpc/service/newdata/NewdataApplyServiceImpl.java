package com.piesat.dm.rpc.service.newdata;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.newdata.NewdataApplyDao;
import com.piesat.dm.entity.newdata.NewdataApplyEntity;
import com.piesat.dm.rpc.api.*;
import com.piesat.dm.rpc.api.newdata.NewdataApplyService;
import com.piesat.dm.rpc.dto.*;
import com.piesat.dm.rpc.dto.newdata.NewdataApplyDto;
import com.piesat.dm.rpc.mapper.newdata.NewdataApplyMapper;
import com.piesat.ucenter.rpc.api.system.DictDataService;
import com.piesat.ucenter.rpc.dto.system.DictDataDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GrpcHthtClient
    private DictDataService dictDataService;

    @Autowired
    private StorageConfigurationService storageConfigurationService;

    @Autowired
    private DataTableService dataTableService;

    @Autowired
    private TableColumnService tableColumnService;


    @Override
    public BaseDao<NewdataApplyEntity> getBaseDao() {
        return newdataApplyDao;
    }

    /*@Override
    public PageBean selectPageList(NewdataApplyDto newdataApplyDto, int pageNum, int pageSize) {
        SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder();
        if (newdataApplyDto.getExamineStatus() != null) {
            ssb.add("examineStatus", SpecificationOperator.Operator.eq.name(), newdataApplyDto.getExamineStatus());
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "applyTime");
        PageBean page = this.getPage(ssb.generateSpecification(), new PageForm(pageNum, pageSize), sort);
        List<NewdataApplyEntity> pageData = (List<NewdataApplyEntity>)page.getPageData();
        page.setPageData(this.newdataApplyMapper.toDto(pageData));
        return page;
    }*/

    @Override
    public PageBean selectPageList(NewdataApplyDto newdataApplyDto, int pageNum, int pageSize) {
        String sql = "select a.*, b.c_datumtype as type_name, s.*, f.database_name from t_sod_newdata_apply a " +
                "left join t_sod_data_datumtypeinfo b on a.d_data_id = b.c_datum_code " +
                "left join t_sod_storage_configuration s on a.logic_id = s.logic_id and a.database_id = s.database_id and a.data_class_id = s.data_class_id" +
                "left join t_sod_database_define f on a.database_id = f.id ";
        PageBean page = this.queryByNativeSQLPageMap(sql,null, new PageForm(pageNum, pageSize));
        return page;
    }

    @Override
    public int updateStatus(NewdataApplyDto newdataApplyDto) {
        return newdataApplyDao.updateStatus(newdataApplyDto.getExamineStatus(),newdataApplyDto.getRemark(),newdataApplyDto.getExaminer(),newdataApplyDto.getId(),newdataApplyDto.getDDataId(),newdataApplyDto.getTableName());
    }

    @Override
    public List<Map<String, Object>> getLogicInfo() {
        List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();

        // 获取逻辑库类型
        List<LogicDefineDto> logiclist = logicDefineService.all();
        //查询物理库-逻辑库
        List<Map<String, Object>> distinctDatabaseAndLogic = dataLogicService.getDistinctDatabaseAndLogic();
        //查询所有的表类型
        Map<String,String> dictMap = new HashMap<String,String>();
        List<DictDataDto> dictDataDtos = dictDataService.selectDictDataByType("table_type");
        if(dictDataDtos != null){
            for(DictDataDto dictDataDto:dictDataDtos){
                dictMap.put(dictDataDto.getDictValue(),dictDataDto.getDictLabel());//E_table 要素表
            }
        }

        if (logiclist != null && logiclist.size() > 0){
            for (int i = logiclist.size()-1; i >=0; i--) {
                LogicDefineDto logicDefineDto = logiclist.get(i);
                Map<String,Object> logicDefineMap = JSONObject.parseObject(JSON.toJSONString(logicDefineDto));
                //逻辑库对应的表类型
                List<LogicStorageTypesDto> logicStorageTypesEntityList = logicDefineDto.getLogicStorageTypesEntityList();
                List<Map<String, Object>> tableTypeList = new ArrayList<Map<String, Object>>();
                if(logicStorageTypesEntityList != null){
                    for(LogicStorageTypesDto logicStorageTypesDto : logicStorageTypesEntityList){
                        Map<String, Object> map = new HashMap<String, Object>();
                        String storageType = logicStorageTypesDto.getStorageType();
                        map.put("key",storageType);
                        map.put("name",dictMap.get(storageType));
                        tableTypeList.add(map);
                    }
                }
                logicDefineMap.put("tableType", tableTypeList);

                //逻辑库对应的物理库
                List<Map<String, Object>> databaseList = new ArrayList<Map<String, Object>>();
                for(Map<String, Object> databaseLogic : distinctDatabaseAndLogic){
                    if(logicDefineDto.getLogicFlag().toString().equals(String.valueOf(databaseLogic.get("logic_flag")))){
                        databaseList.add(databaseLogic);
                    }
                }
                if (databaseList.size() != 0) {
                    logicDefineMap.put("physics", databaseList);
                    result.add(logicDefineMap);
                }
            }
        }

        return result;
    }

    @Override
    public Map<String, Object> queryCheckByApplyId(NewdataApplyDto newdataApplyDto) {
        String sql = "SELECT a.*, b.c_datum_code,b.c_datumtype,c.c_datum_code type_code,c.c_datumtype type_name "
                + " FROM dmin_data_newdata_apply a,t_sod_data_datumtypeinfo b,t_sod_data_datumtypeinfo c "
                + " WHERE a.id = '" + newdataApplyDto.getId() + "' AND a.d_data_id = b.c_datum_code"
                + " AND SUBSTR(a.D_DATA_ID,0,1) = c.c_datum_code" + " AND a.d_data_id ='" + newdataApplyDto.getDDataId() + "' and a.table_name = '"+newdataApplyDto.getTableName() +"'";
        List<Map<String, Object>> list = this.queryByNativeSQL(sql);
        return list == null ? null : list.get(0);
    }

    @Override
    public ResultT<String> addGroup(DataClassDto dataClassDto, NewdataApplyDto newdataApplyDto) {
        //判断存储编码是否重复
        DataClassDto dataClassResult = dataClassService.findByDataClassId(dataClassDto.getDataClassId());
        if(dataClassResult != null){
            return ResultT.failed("存储编码重复，请检查重新添加！");
        }
        //添加资料分类
        dataClassDto = dataClassService.saveDto(dataClassDto);

        if(dataClassDto != null && dataClassDto.getType() == 2){
            List<DataLogicDto> dataLogicDtoList = newdataApplyDto.getDataLogicDtoList();
            for(DataLogicDto dataLogicDto : dataLogicDtoList){
                //添加资料用途分类
                dataLogicService.saveDto(dataLogicDto);
                //添加存储策略
                StorageConfigurationDto storageConfigurationDto = new StorageConfigurationDto();
                storageConfigurationDto.setLogicId(dataLogicDto.getLogicFlag());
                storageConfigurationDto.setDatabaseId(dataLogicDto.getDatabaseId());
                storageConfigurationDto.setDataClassId(dataLogicDto.getDataClassId());
                storageConfigurationDto.setStorageDefineIdentifier(2);
                storageConfigurationDto.setSyncIdentifier(2);
                storageConfigurationDto.setMovecleanIdentifier(2);
                storageConfigurationDto.setBackupIdentifier(2);
                storageConfigurationDto.setArchivingIdentifier(2);
                storageConfigurationService.saveDto(storageConfigurationDto);
            }
        }
        //修改申请表的d_data_id和data_class_id
        newdataApplyDao.updateDDateIdAndDataClassId(newdataApplyDto.getDDataId(), newdataApplyDto.getDataClassId(), newdataApplyDto.getId());
        return ResultT.success();
    }

    @Override
    public ResultT<String> updateGroup(DataClassDto dataClassDto, NewdataApplyDto newdataApplyDto, String old_data_class_id) {
        //判断存储编码是否有改动
        if(!dataClassDto.getDataClassId().equals(old_data_class_id)){
            DataClassDto dataClassResult = dataClassService.findByDataClassId(dataClassDto.getDataClassId());
            if(dataClassResult != null){
                return ResultT.failed("存储编码重复，请检查重新添加！");
            }
            //删除修改前的资料分类,会同时删掉表结构相关数据
            dataClassService.deleteByDataClassId(old_data_class_id);
            //添加新的资料分类
            dataClassDto = dataClassService.saveDto(dataClassDto);
        }
        //删除修改前的资料用途分类
        dataLogicService.deleteByDataClassId(old_data_class_id);
        //添加修改后的资料用途分类
        List<DataLogicDto> dataLogicDtoList = newdataApplyDto.getDataLogicDtoList();
        for(DataLogicDto dataLogicDto : dataLogicDtoList){
            //添加资料用途分类
            dataLogicService.saveDto(dataLogicDto);

            //删除修改前的存储策略,如果一条策略的logic_id  database_id  dataclass_id均为改动，则不删除
            //storageConfigurationService.
            //添加修改后的存储策略
            StorageConfigurationDto storageConfigurationDto = new StorageConfigurationDto();
            storageConfigurationDto.setLogicId(dataLogicDto.getLogicFlag());
            storageConfigurationDto.setDatabaseId(dataLogicDto.getDatabaseId());
            storageConfigurationDto.setDataClassId(dataLogicDto.getDataClassId());
            storageConfigurationDto.setStorageDefineIdentifier(2);
            storageConfigurationDto.setSyncIdentifier(2);
            storageConfigurationDto.setMovecleanIdentifier(2);
            storageConfigurationDto.setBackupIdentifier(2);
            storageConfigurationDto.setArchivingIdentifier(2);
            storageConfigurationService.saveDto(storageConfigurationDto);
        }

        //修改申请表的d_data_id和data_class_id
        newdataApplyDao.updateDDateIdAndDataClassId(newdataApplyDto.getDDataId(), newdataApplyDto.getDataClassId(), newdataApplyDto.getId());
        return ResultT.success();
    }

    @Override
    public ResultT<String> addOrUpdateDataTable(DataTableDto dataTableDto, HttpServletRequest request) {
        dataTableDto.setTableName(dataTableDto.getTableName().toUpperCase());
        String databaseId = request.getParameter("databaseId");
        String dataClassId = request.getParameter("dataClassId");
        //最后返回一条记录
        List<DataTableDto> dataTableDtos = dataTableService.getByDatabaseIdAndClassId(databaseId, dataClassId);
        if (dataTableDtos != null && dataTableDtos.size() > 0) {
            //修改
            dataTableService.updateById(dataTableDtos.get(0));
        }else{
            //添加
        }

        return null;
    }

    @Override
    public List<Map<String, Object>> getNewdataTableField(String id) {
        return null;
    }

    @Override
    public ResultT<String> addDataStructure(TableColumnDto tableColumnDto) {
        TableColumnDto tableColumnDto1 = tableColumnService.saveDto(tableColumnDto);
        return ResultT.success();
    }

    @Override
    public ResultT<String> updateDataStructure(TableColumnDto tableColumnDto) {
        tableColumnService.updateDto(tableColumnDto);
        return ResultT.success();
    }

}
