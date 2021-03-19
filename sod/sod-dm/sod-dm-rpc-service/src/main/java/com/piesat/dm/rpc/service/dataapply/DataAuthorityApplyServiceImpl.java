package com.piesat.dm.rpc.service.dataapply;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.config.DatabseType;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.common.constants.ConstantsMsg;
import com.piesat.dm.core.constants.Constants;
import com.piesat.dm.core.enums.DbaEnum;
import com.piesat.dm.core.model.AuthorityVo;
import com.piesat.dm.core.model.ConnectVo;
import com.piesat.dm.core.parser.DatabaseInfo;
import com.piesat.dm.dao.ReadAuthorityDao;
import com.piesat.dm.dao.dataapply.DataAuthorityApplyDao;
import com.piesat.dm.dao.dataapply.DataAuthorityRecordDao;
import com.piesat.dm.dao.datatable.DataTableDao;
import com.piesat.dm.entity.ReadAuthorityEntity;
import com.piesat.dm.entity.dataapply.DataAuthorityApplyEntity;
import com.piesat.dm.entity.dataapply.DataAuthorityRecordEntity;
import com.piesat.dm.entity.datatable.DataTableInfoEntity;
import com.piesat.dm.mapper.MybatisModifyMapper;
import com.piesat.dm.mapper.MybatisPageMapper;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.dataapply.DataAuthorityApplyService;
import com.piesat.dm.rpc.api.database.DatabaseUserService;
import com.piesat.dm.rpc.api.database.SchemaService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.api.dataclass.DataLogicService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.api.special.DatabaseSpecialReadWriteService;
import com.piesat.dm.rpc.api.special.DatabaseSpecialService;
import com.piesat.dm.rpc.dto.ReadAuthorityDto;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityApplyDto;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityRecordDto;
import com.piesat.dm.rpc.dto.database.DatabaseUserDto;
import com.piesat.dm.rpc.dto.database.SchemaDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialReadWriteDto;
import com.piesat.dm.rpc.mapper.ReadAuthorityMapper;
import com.piesat.dm.rpc.mapper.dataapply.DataAuthorityApplyMapper;
import com.piesat.dm.rpc.mapper.dataapply.DataAuthorityRecordMapper;
import com.piesat.ucenter.dao.system.UserDao;
import com.piesat.ucenter.entity.system.UserEntity;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.CmadaasApiUtil;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * @author yaya
 * @description TODO
 * @date 2020/3/10 13:01
 */
@Service
public class DataAuthorityApplyServiceImpl extends BaseService<DataAuthorityApplyEntity> implements DataAuthorityApplyService {

    @Autowired
    private DataAuthorityApplyDao dataAuthorityApplyDao;
    @Autowired
    private DataAuthorityApplyMapper dataAuthorityApplyMapper;
    @Autowired
    private DataAuthorityRecordMapper dataAuthorityRecordMapper;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private MybatisModifyMapper mybatisModifyMapper;
    @Autowired
    private MybatisPageMapper mybatisPageMapper;
    @Autowired
    private SchemaService schemaService;
    @Autowired
    private DataAuthorityRecordDao dataAuthorityRecordDao;
    @Autowired
    private DatabaseSpecialService databaseSpecialService;
    @Autowired
    private DatabaseSpecialReadWriteService databaseSpecialReadWriteService;
    @Autowired
    private DatabaseUserService databaseUserService;
    @Autowired
    private DataTableService dataTableService;
    @Autowired
    private DataClassService dataClassService;
    @Autowired
    private DataTableDao dataTableDao;
    @Autowired
    private ReadAuthorityDao readAuthorityDao;
    @Autowired
    private ReadAuthorityMapper readAuthorityMapper;
    @Autowired
    private DatabaseInfo databaseInfo;
    @Autowired
    private DataLogicService dataLogicService;

    @GrpcHthtClient
    private UserDao userDao;

    @Override
    public BaseDao<DataAuthorityApplyEntity> getBaseDao() {
        return this.dataAuthorityApplyDao;
    }

    @Override
    public PageBean selectPageList(PageForm<Map<String, String>> pageForm) {
        PageHelper.startPage(pageForm.getCurrentPage(),pageForm.getPageSize());
        List<Map<String,Object>> lists = this.mybatisPageMapper.getPageAuthorityDataApplyFlow(pageForm.getT());
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(lists);
        PageBean pageBean=new PageBean(pageInfo.getTotal(),pageInfo.getPages(),lists);
        return pageBean;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataAuthorityApplyDto saveDto(DataAuthorityApplyDto dataAuthorityApplyDto) {
        DataAuthorityApplyEntity dataAuthorityApplyEntity = dataAuthorityApplyMapper.toEntity(dataAuthorityApplyDto);
        //新增申请设置成待审
        dataAuthorityApplyEntity.setAuditStatus("01");

        //保存
        dataAuthorityApplyEntity = this.saveNotNull(dataAuthorityApplyEntity);

        //读申请是否自动授权
        List<ReadAuthorityEntity> readAuthorityEntities = readAuthorityDao.findAll();
        if (readAuthorityEntities != null && readAuthorityEntities.size() > 0) {
            //读申请自动授权
            if ("1".equals(readAuthorityEntities.get(0).getValue())) {
                Set<DataAuthorityRecordEntity> dataAuthorityRecordList = dataAuthorityApplyEntity.getDataAuthorityRecordList();
                if (dataAuthorityRecordList != null && dataAuthorityRecordList.size() > 0) {
                    for (DataAuthorityRecordEntity dataAuthorityRecordEntity : dataAuthorityRecordList) {
                        if (dataAuthorityRecordEntity.getApplyAuthority().intValue() == 1) {
                            DataAuthorityRecordDto dataAuthorityRecordDto = dataAuthorityRecordMapper.toDto(dataAuthorityRecordEntity);
                            //物理库授权
                            updateOneRecordCheck(dataAuthorityApplyEntity.getUserId(), dataAuthorityRecordDto);
                        }
                    }
                }
            }
        }
        return dataAuthorityApplyMapper.toDto(dataAuthorityApplyEntity);
    }

    @Override
    public Map<String, Object> getObjectById(String id) {
        Map<String, Object> objectMap = mybatisQueryMapper.queryDataAuthorityApplyById(id);
        if (objectMap != null) {
            if (objectMap.get("USER_ID") != null) {
                //调接口查申请人详情
                UserEntity userEntity = userDao.findByUserName((String) objectMap.get("USER_ID"));
                if (userEntity != null) {
                    objectMap.put("USERREALNAME", userEntity.getWebUsername());
                    objectMap.put("USERPHONE", userEntity.getTutorPhone());
                    objectMap.put("DEPARTMENT", userEntity.getDeptName());
                }
            }
        }
        return objectMap;
    }

    @Override
    public List<Map<String, Object>> getRecordByApplyId(Map<String, String> map) {
        if ("mysql".equals(DatabseType.type.toLowerCase())) {
            return mybatisQueryMapper.getRecordByApplyIdMysql(map);
        } else {
            return mybatisQueryMapper.getRecordByApplyId(map);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultT updateRecordCheck(DataAuthorityApplyDto dataAuthorityApplyDto) {
        List<DataAuthorityRecordDto> dataAuthorityRecordList = dataAuthorityApplyDto.getDataAuthorityRecordList();
        ResultT r = new ResultT();
        //获取用户up账户
        DatabaseUserDto databaseUserDto = databaseUserService.findByUserIdAndExamineStatus(dataAuthorityApplyDto.getUserId(), "1");
        if (databaseUserDto == null) {
            return ResultT.failed(ConstantsMsg.MSG2);
        }
        //用户up账户对应的可用物理库
        String examineDatabaseId = databaseUserDto.getExamineDatabaseId();
        if (StringUtils.isEmpty(examineDatabaseId)) {
            return ResultT.failed(ConstantsMsg.MSG2);
        }
        List<String> databaseIds = Arrays.asList(examineDatabaseId.split(Constants.COMMA));
        //为每个数据表进行授权
        for (DataAuthorityRecordDto dataAuthorityRecordDto : dataAuthorityRecordList) {
            DataTableInfoEntity dataTableInfo = this.dataTableDao.findById(dataAuthorityRecordDto.getTableId()).orElse(null);
            SchemaDto schemaDto = schemaService.getDotById(dataTableInfo.getDatabaseId());
            if (!databaseIds.contains(schemaDto.getDatabase().getId())) {
                r.setErrorMessage(String.format(ConstantsMsg.MSG3, schemaDto.getDatabase().getDatabaseName()));
                continue;
            }
            if (StringUtils.isEmpty(dataAuthorityRecordDto.getTableName())) {
                continue;
            }
            ConnectVo coreInfo = schemaDto.getConnectVo();
            DbaEnum dbaEnum = dataAuthorityRecordDto.getApplyAuthority() == 1 ? DbaEnum.READ : DbaEnum.WRITE;
            AuthorityVo a = new AuthorityVo(schemaDto.getSchemaName(), dataTableInfo.getTableName(), databaseUserDto.getDatabaseUpId(), dbaEnum);

            coreInfo.build(r)
                    .grantTable(a, r)
                    .close();

            mybatisQueryMapper.updateDataAuthorityRecord(dataAuthorityRecordDto.getId(), dataAuthorityRecordDto.getAuthorize(), dataAuthorityRecordDto.getCause());
        }
        return r;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultT updateOneRecordCheck(String userId, DataAuthorityRecordDto dataAuthorityRecordDto) {
        ResultT r = new ResultT();
        //获取用户up账户
        DatabaseUserDto databaseUserDto = databaseUserService.findByUserIdAndExamineStatus(userId, "1");
        if (databaseUserDto == null) {
            return ResultT.failed(ConstantsMsg.MSG2);
        }

        List<String> databaseIds = Arrays.asList(databaseUserDto.getExamineDatabaseId().split(","));

        DataTableInfoEntity dataTableInfo = this.dataTableDao.findById(dataAuthorityRecordDto.getTableId()).orElse(null);

        SchemaDto schemaDto = schemaService.getDotById(dataTableInfo.getDatabaseId());

        if (!databaseIds.contains(schemaDto.getDatabase().getId())) {
            return ResultT.failed(String.format(ConstantsMsg.MSG3, schemaDto.getDatabase().getDatabaseName()));
        }

        if (!databaseIds.contains(schemaDto.getDatabase().getId())) {
            r.setErrorMessage(String.format(ConstantsMsg.MSG3, schemaDto.getDatabase().getDatabaseName()));
        }
        ConnectVo coreInfo = schemaDto.getConnectVo();
        DbaEnum dbaEnum = dataAuthorityRecordDto.getApplyAuthority() == 1 ? DbaEnum.READ : DbaEnum.WRITE;
        AuthorityVo a = new AuthorityVo(schemaDto.getSchemaName(), dataTableInfo.getTableName(), databaseUserDto.getDatabaseUpId(), dbaEnum);

        coreInfo.build(r)
                .grantTable(a, r)
                .close();

        mybatisQueryMapper.updateDataAuthorityRecord(dataAuthorityRecordDto.getId(), dataAuthorityRecordDto.getAuthorize(), dataAuthorityRecordDto.getCause());
        return r;
    }

    @Override
    public ResultT updateRecordCheckCancel(DataAuthorityApplyDto dataAuthorityApplyDto) {
        ResultT r = new ResultT();
        List<DataAuthorityRecordDto> dataAuthorityRecordList = dataAuthorityApplyDto.getDataAuthorityRecordList();
        //获取用户up账户
        DatabaseUserDto databaseUserDto = databaseUserService.findByUserIdAndExamineStatus(dataAuthorityApplyDto.getUserId(), "1");
        if (databaseUserDto == null) {
            return ResultT.failed(ConstantsMsg.MSG2);
        }

        StringBuffer buffer = new StringBuffer();
        boolean flag = true;
        //循环
        for (DataAuthorityRecordDto dataAuthorityRecordDto : dataAuthorityRecordList) {

            if (dataAuthorityRecordDto.getAuthorize() != null && dataAuthorityRecordDto.getAuthorize().intValue() == 1) {//已授权资料，撤销
                // 授权
                DataTableInfoEntity dataTableInfo = this.dataTableDao.findById(dataAuthorityRecordDto.getTableId()).orElse(null);
                //获取物理库信息
                SchemaDto schemaDto = schemaService.getDotById(dataTableInfo.getDatabaseId());
                if (StringUtils.isEmpty(dataAuthorityRecordDto.getTableName())) {
                    continue;
                }
                ConnectVo coreInfo = schemaDto.getConnectVo();
                DbaEnum dbaEnum = dataAuthorityRecordDto.getApplyAuthority() == 1 ? DbaEnum.READ : DbaEnum.WRITE;
                AuthorityVo a = new AuthorityVo(schemaDto.getSchemaName(), dataTableInfo.getTableName(), databaseUserDto.getDatabaseUpId(), dbaEnum);
                coreInfo.build(r)
                        .revokeTable(a, r)
                        .close();
            }
            mybatisQueryMapper.updateDataAuthorityRecord(dataAuthorityRecordDto.getId(), dataAuthorityRecordDto.getAuthorize(), dataAuthorityRecordDto.getCause());
        }
        if (flag) {
            return ResultT.success("拒绝成功");
        } else {
            return ResultT.failed(buffer.toString());
        }
    }

    @Override
    public List<Map<String, Object>> getRecordListByUserId(String userId) {
        if ("mysql".equals(DatabseType.type.toLowerCase())) {
            return mybatisQueryMapper.getRecordListByUserIdMysql(userId);
        } else {
            return mybatisQueryMapper.getRecordListByUserId(userId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRecordByApplyIdAndClassId(String apply_id, String data_class_id, Integer authorize, String cause) {
        List<DataAuthorityRecordEntity> dataAuthorityRecordEntities = null;
//                dataAuthorityRecordDao.findByApplyIdAndDataClassId(apply_id, data_class_id);
        if (dataAuthorityRecordEntities != null && dataAuthorityRecordEntities.size() > 0) {
            for (DataAuthorityRecordEntity dataAuthorityRecordEntity : dataAuthorityRecordEntities) {
                //如果资料是专题库引用资料，审核专题库引用资料
//                if (StringUtils.isNotNullString(dataAuthorityRecordEntity.getQtdbId())) {
//                    List<DatabaseSpecialReadWriteDto> databaseSpecialReadWriteDtos = databaseSpecialReadWriteService.findBySdbIdAndDataClassId(dataAuthorityRecordEntity.getQtdbId(), data_class_id);
//                    if (databaseSpecialReadWriteDtos != null && databaseSpecialReadWriteDtos.size() > 0) {
//                        for (DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto : databaseSpecialReadWriteDtos) {
//                            if (StringUtils.isNotNullString(cause)) {
//                                databaseSpecialReadWriteDto.setFailureReason(cause);
//                            }
//                            databaseSpecialReadWriteDto.setExamineStatus(authorize);
//                            databaseSpecialReadWriteService.saveDto(databaseSpecialReadWriteDto);
//                        }
//                    }
//                }

                //资料授权审核
                mybatisQueryMapper.updateDataAuthorityRecord(dataAuthorityRecordEntity.getId(), authorize, cause);

                dataAuthorityRecordMapper.toDto(dataAuthorityRecordEntity);
                //到物理库授权
                //updateRecordCheck(dataAuthorityRecordEntity);
            }
        }
    }

    @Override
    public Map<String, Object> getAuthorDataByClassId(String dataClassId) {
        HashMap<String, Object> map = new HashMap<>();
        DataClassDto dataClassDto = dataClassService.findByDataClassId(dataClassId);
        //List<DataAuthorityRecordEntity> dataAuthorityRecordEntities = dataAuthorityRecordDao.findByDataClassIdAndAuthorize(dataClassId, 1);
        map.put("access_control", dataClassDto.getIsAccess());
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRecordByApplyIdAndClassIdAndDatabaseId(String apply_id, String data_class_id, String
            database_id, Integer authorize, String cause) {
        List<DataAuthorityRecordEntity> dataAuthorityRecordEntities = null;
//                dataAuthorityRecordDao.findByApplyIdAndDataClassIdAndDatabaseId(apply_id, data_class_id, database_id);
        if (dataAuthorityRecordEntities != null && dataAuthorityRecordEntities.size() > 0) {
            for (DataAuthorityRecordEntity dataAuthorityRecordEntity : dataAuthorityRecordEntities) {
                mybatisQueryMapper.updateDataAuthorityRecord(dataAuthorityRecordEntity.getId(), authorize, cause);
            }
        }

    }

    @Override
    public Map<String, Object> getDataAuthorityList(String userId, String applyAuthority, String logicId, String
            dataName, String category, String schemaId) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("userId", userId);

        if (null == userId || "".equals(userId)) {
            result.put("returnMessage", "请登录");
            result.put("returnCode", "-1002");
            result.put("DS", null);
            return result;
        }
        paraMap.put("applyAuthority", applyAuthority);
        paraMap.put("logicId", logicId);
        paraMap.put("dataName", dataName);
        paraMap.put("category", category);
        paraMap.put("schemaId", schemaId);
        try {
            List<Map<String, Object>> daList = mybatisQueryMapper.getDataAuthorityList(paraMap);
            result.put("returnCode", "0");
            result.put("returnMessage", "查询成功");
            result.put("DS", daList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("returnCode", "1");
            result.put("returnMessage", "查询失败");
        }
        return result;
    }

    @Override
    public Map<String, Object> getDataCreator(String dataClassId) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            DataTableInfoEntity dataTableEntity = dataTableDao.findById(dataClassId).get();
            String userId = dataTableEntity.getUserId();
            if (!"admin".equals(userId)) {
                com.alibaba.fastjson.JSONObject obj = CmadaasApiUtil.getUserInfo(userId);
                result.put("DS", obj);
            } else {
                result.put("DS", "admin");
            }
            result.put("returnCode", "0");

        } catch (Exception e) {
            e.printStackTrace();
            result.put("returnCode", "1");
            result.put("returnMessage", "查询失败 : " + e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> deleteDataAuthorityById(String applyId, String dataBaseId, String dataClassId) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("applyId", applyId);
        paraMap.put("dataBaseId", dataBaseId);
        paraMap.put("dataClassId", dataClassId);
        //删除前查询是否存在
        try {
            mybatisModifyMapper.delDataAuthorityByApplyId(paraMap);
            mybatisModifyMapper.clearUselessApply();
            result.put("returnCode", "0");
            result.put("returnMessage", "删除成功。");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("returnCode", "1");
            result.put("returnMessage", "删除失败 : " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> getDataCategory() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> categoryList = mybatisQueryMapper.getDataCategory();
            result.put("returnCode", "0");
            result.put("DS", categoryList);
            result.put("returnMessage", "操作成功 。");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("returnCode", "1");
            result.put("returnMessage", "操作失败 ：" + e.getMessage());
        }
        return result;
    }

    /**
     * @param userId
     * @return
     * @throws Exception
     * @description
     * @author wlg
     * @date 2020-04-22 17:06
     */
    @Override
    public List<Map<String, Object>> getApplyDataInfo(String userId) throws Exception {
        //获取userId的可用物理库
        DatabaseUserDto databaseUserDto = databaseUserService.findByUserIdAndExamineStatus(userId, "1");
        if (databaseUserDto == null || !StringUtils.isNotNullString(databaseUserDto.getExamineDatabaseId())) {
            return null;
        }
        //查询所有不属于该用户的资料
        List<Map<String, Object>> result = mybatisQueryMapper.getApplyDataInfo(userId);

        //查询该用户已经申请使用的资料
        List<Map<String, Object>> recordListByUserId = mybatisQueryMapper.getApplyRecordListByUserId(userId);

        //查询可用物理库里已建的表
        Map<String, List<String>> databaseTables = dataLogicService.getDatabaseTables(databaseUserDto.getExamineDatabaseId());


        //剔除还没在物理库建表的资料
        List<String> databaseIds = Arrays.asList(databaseUserDto.getExamineDatabaseId().split(","));
        for (int i = result.size() - 1; i > -1; i--) {
            Map<String, Object> map = result.get(i);
            String databaseId = (String) map.get("DATABASEID");
            String dataClassId = (String) map.get("DATACLASSID");
            String tableName = (String) map.get("TABLENAME");

            //资料所在的库没有权限的移除
            if (!databaseIds.contains(databaseId)) {
                result.remove(map);
                continue;
            }

            //没建表的移除
            List<String> tableList = databaseTables.get(databaseId);
            if (tableList == null || tableList.size() == 0) {
                result.remove(map);
                continue;
            }
            if ("HADB".equalsIgnoreCase(databaseId) && !tableList.contains(tableName.toLowerCase())) {
                result.remove(map);
                continue;
            }
            if (!"HADB".equalsIgnoreCase(databaseId) && !tableList.contains(tableName.toUpperCase())) {
                result.remove(map);
                continue;
            }

            //已经申请过的移除
            if (recordListByUserId != null && recordListByUserId.size() > 0) {
                for (Map<String, Object> record : recordListByUserId) {
                    if (databaseId.equals((String) record.get("DATABASE_DEFINE_ID")) && dataClassId.equals((String) record.get("DATA_CLASS_ID"))) {
                        result.remove(map);
                        break;
                    }
                }
            }

        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getApplyedFileDataInfo(String userId) {
        List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> recodeFileDataInfo = mybatisQueryMapper.getApplyedRecodeFileDataInfo(userId);
        List<Map<String, Object>> specialReadWriteFileDataInfo = mybatisQueryMapper.getSpecialReadWriteFileDataInfo(userId);
        if (recodeFileDataInfo != null && recodeFileDataInfo.size() > 0) {
            arrayList.addAll(recodeFileDataInfo);
        }
        for (int i = 0; i < arrayList.size(); i++) {
            Map<String, Object> recordOne = arrayList.get(i);
            if (specialReadWriteFileDataInfo != null && specialReadWriteFileDataInfo.size() > 0) {
                for (int j = specialReadWriteFileDataInfo.size() - 1; j > -1; j--) {
                    Map<String, Object> readWriteOne = specialReadWriteFileDataInfo.get(j);
                    if (recordOne.get("DATABASE_ID").equals(readWriteOne.get("DATABASE_ID")) &&
                            recordOne.get("DATA_CLASS_ID").equals(readWriteOne.get("DATA_CLASS_ID")) &&
                            recordOne.get("TABLE_NAME").equals(readWriteOne.get("TABLE_NAME"))
                    ) {
                        specialReadWriteFileDataInfo.remove(readWriteOne);
                        break;
                    }
                }
            }
        }
        arrayList.addAll(specialReadWriteFileDataInfo);

        //分页
        /*PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(arrayList);
        int pages = arrayList.size() % pageForm.getPageSize() == 0 ? arrayList.size() / pageForm.getPageSize() : arrayList.size() / pageForm.getPageSize() + 1;
        pageInfo.setPages(pages);
        int fromIndex = (pageForm.getCurrentPage() - 1) * pageForm.getPageSize();
        if (pageForm.getCurrentPage() < pageInfo.getPages()) {
            arrayList = arrayList.subList(fromIndex, fromIndex + pageForm.getPageSize());

        } else if (Integer.valueOf(pageForm.getCurrentPage()).equals(Integer.valueOf(pageInfo.getPages()))) {
            arrayList = arrayList.subList(fromIndex, arrayList.size());
        }
        PageBean pageBean = new PageBean(pageInfo.getTotal(), pageInfo.getPages(), arrayList);
        return pageBean;*/
        return arrayList;
    }

    @Override
    public ReadAuthorityDto updateReadAuthority(ReadAuthorityDto readAuthorityDto) {
        ReadAuthorityEntity readAuthorityEntity = readAuthorityMapper.toEntity(readAuthorityDto);
        readAuthorityEntity = readAuthorityDao.saveNotNull(readAuthorityEntity);
        return readAuthorityMapper.toDto(readAuthorityEntity);
    }

    @Override
    public List<ReadAuthorityDto> getReadAuthority() {
        List<ReadAuthorityEntity> readAuthorityEntities = readAuthorityDao.findAll();
        return readAuthorityMapper.toDto(readAuthorityEntities);
    }

    @Override
    public List<DataAuthorityApplyDto> findByUserId(String userId) {
        List<DataAuthorityApplyEntity> dalList = this.dataAuthorityApplyDao.findByUserId(userId);
        return this.dataAuthorityApplyMapper.toDto(dalList);
    }

    @Override
    public void deleteByDataClassId(String dataclassId) {
//        this.dataAuthorityRecordDao.deleteByDataClassId(dataclassId);
    }

    @Override
    public void deleteByUserId(String userId) {
        List<DataAuthorityApplyEntity> dalList = this.dataAuthorityApplyDao.findByUserId(userId);
        if (dalList != null && dalList.size() > 0) {
            for (int i = 0; i < dalList.size(); i++) {
                DataAuthorityApplyEntity dataAuthorityApplyEntity = dalList.get(i);
                Set<DataAuthorityRecordEntity> dataAuthorityRecordList = dataAuthorityApplyEntity.getDataAuthorityRecordList();
                if (dataAuthorityRecordList != null) {
                    for (Iterator<DataAuthorityRecordEntity> iterator = dataAuthorityRecordList.iterator(); iterator.hasNext(); ) {
                        DataAuthorityRecordEntity t = (DataAuthorityRecordEntity) iterator.next();
                        this.dataAuthorityRecordDao.delete(t);
                    }
                }
                this.dataAuthorityApplyDao.delete(dataAuthorityApplyEntity);
            }
        }
    }

}
