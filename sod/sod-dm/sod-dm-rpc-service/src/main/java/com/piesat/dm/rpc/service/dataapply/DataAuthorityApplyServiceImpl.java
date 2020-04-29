package com.piesat.dm.rpc.service.dataapply;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.core.api.impl.Gbase8a;
import com.piesat.dm.core.api.impl.Xugu;
import com.piesat.dm.core.parser.DatabaseType;
import com.piesat.dm.dao.ReadAuthorityDao;
import com.piesat.dm.dao.dataapply.DataAuthorityApplyDao;
import com.piesat.dm.dao.dataapply.DataAuthorityRecordDao;
import com.piesat.dm.dao.datatable.DataTableDao;
import com.piesat.dm.entity.ReadAuthorityEntity;
import com.piesat.dm.entity.dataapply.DataAuthorityApplyEntity;
import com.piesat.dm.entity.dataapply.DataAuthorityRecordEntity;
import com.piesat.dm.entity.datatable.DataTableEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.dataapply.DataAuthorityApplyService;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.api.database.DatabaseUserService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.api.special.DatabaseSpecialReadWriteService;
import com.piesat.dm.rpc.api.special.DatabaseSpecialService;
import com.piesat.dm.rpc.dto.ReadAuthorityDto;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityApplyDto;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityRecordDto;
import com.piesat.dm.rpc.dto.database.DatabaseAdministratorDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.database.DatabaseUserDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialReadWriteDto;
import com.piesat.dm.rpc.mapper.ReadAuthorityMapper;
import com.piesat.dm.rpc.mapper.dataapply.DataAuthorityApplyMapper;
import com.piesat.dm.rpc.mapper.dataapply.DataAuthorityRecordMapper;
import com.piesat.util.CmadaasApiUtil;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
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
    private DatabaseService databaseService;
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
    private DataAuthorityRecordDao dataAuthorityRecordService;
    @Autowired
    private DataClassService dataClassService;
    @Autowired
    private DataTableDao dataTableDao;
    @Autowired
    private ReadAuthorityDao readAuthorityDao;
    @Autowired
    private ReadAuthorityMapper readAuthorityMapper;
    @Override
    public BaseDao<DataAuthorityApplyEntity> getBaseDao() {
        return this.dataAuthorityApplyDao;
    }

    @Override
    public PageBean selectPageList(PageForm<DataAuthorityApplyDto> pageForm) {
        DataAuthorityApplyEntity dataAuthorityApplyEntity = dataAuthorityApplyMapper.toEntity(pageForm.getT());

        DataAuthorityApplyDto dataAuthorityApplyDto = pageForm.getT();
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(dataAuthorityApplyDto.getAuditStatus())){
            specificationBuilder.add("auditStatus", SpecificationOperator.Operator.eq.name(),dataAuthorityApplyDto.getAuditStatus());
        }
        if(StringUtils.isNotNullString(dataAuthorityApplyDto.getUserName())){
            //调用接口 根据用户名查询用户id
            String userId="";
            specificationBuilder.add("userId", SpecificationOperator.Operator.eq.name(),userId);
        }
        if(StringUtils.isNotNullString((String) dataAuthorityApplyEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) dataAuthorityApplyEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) dataAuthorityApplyEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) dataAuthorityApplyEntity.getParamt().get("endTime"));
        }
        Sort sort = Sort.by(Sort.Direction.DESC,"auditStatus","createTime");
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<DataAuthorityApplyEntity> dataAuthorityApplyEntities = (List<DataAuthorityApplyEntity>) pageBean.getPageData();

        //调用接口获取所有的用户信息

        List<DataAuthorityApplyDto> dataAuthorityApplyDtos = dataAuthorityApplyMapper.toDto(dataAuthorityApplyEntities);

        //循环遍历
        for(DataAuthorityApplyDto authorityApply : dataAuthorityApplyDtos){

            //若所申请资料全部审核了，将这条申请记录修改为“已审核”
            List<DataAuthorityRecordDto> dataAuthorityRecordList = authorityApply.getDataAuthorityRecordList();
            int num = 0;
            if(StringUtils.isNotNullString(authorityApply.getAuditStatus()) && !"02".equals(authorityApply.getAuditStatus())){
                for(DataAuthorityRecordDto authorityRecord:dataAuthorityRecordList){
                    if(authorityRecord.getAuthorize() != null){
                        num++;
                        if (num == dataAuthorityRecordList.size()) {
                            //authorityApply.setAuditStatus("02");
                            //authorityApply = this.saveNotNull(authorityApply);
                            this.mybatisQueryMapper.updateDataAuthorityApply(authorityApply.getId(),"02");
                        }
                    }
                }
            }

            //遍历所有用户信息找到每条记录对应的用户信息
           /* authorityApply.setUserName("申请人");
            authorityApply.setTelephone("12388888888");
            authorityApply.setDepartment("部门");*/
        }

        pageBean.setPageData(dataAuthorityApplyDtos);
        return pageBean;
    }

    @Override
    public DataAuthorityApplyDto saveDto(DataAuthorityApplyDto dataAuthorityApplyDto) {
        DataAuthorityApplyEntity dataAuthorityApplyEntity = dataAuthorityApplyMapper.toEntity(dataAuthorityApplyDto);
        //新增申请设置成待审
        dataAuthorityApplyEntity.setAuditStatus("01");

        //读申请自动授权
        Set<DataAuthorityRecordEntity> dataAuthorityRecordList = dataAuthorityApplyEntity.getDataAuthorityRecordList();
        if(dataAuthorityRecordList != null && dataAuthorityRecordList.size()>0){
            for(DataAuthorityRecordEntity dataAuthorityRecordEntity : dataAuthorityRecordList){
                if(dataAuthorityRecordEntity.getApplyAuthority().intValue() == 1){
                    dataAuthorityRecordEntity.setAuthorize(1);
                }
            }
        }
        //保存
        dataAuthorityApplyEntity = this.saveNotNull(dataAuthorityApplyEntity);
        return dataAuthorityApplyMapper.toDto(dataAuthorityApplyEntity);
    }

    @Override
    public Map<String, Object> getObjectById(String id) {
        Map<String, Object> objectMap = mybatisQueryMapper.queryDataAuthorityApplyById(id);
        //根据用户id获取用户信息
        objectMap.put("USERREALNAME", "申请人");
        objectMap.put("USERPHONE", "12388888888");
        objectMap.put("DEPARTMENT", "部门");

        return objectMap;
    }

    @Override
    public List<Map<String,Object>> getRecordByApplyId(Map<String,String> map) {
        if(DatabaseType.databaseType.toLowerCase().equals("mysql")){
            return mybatisQueryMapper.getRecordByApplyIdMysql(map);
        }else{
            return mybatisQueryMapper.getRecordByApplyId(map);
        }
    }

    @Override
    public Map<String, Object> updateRecordCheck(DataAuthorityApplyDto dataAuthorityApplyDto) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<DataAuthorityRecordDto> dataAuthorityRecordList = dataAuthorityApplyDto.getDataAuthorityRecordList();

        //获取用户up账户
        DatabaseUserDto databaseUserDto = databaseUserService.findByUserIdAndExamineStatus(dataAuthorityApplyDto.getUserId(), "1");
        if(databaseUserDto == null){
            map.put("msg","授权失败,未找到用户对应数据库账户信息");
            return map;
        }
        String ip = "";
        if(StringUtils.isNotNullString(databaseUserDto.getDatabaseUpIp())){
            ip = databaseUserDto.getDatabaseUpIp();
        }else if(StringUtils.isNotNullString(databaseUserDto.getDatabaseUpIpSegment())){
            ip = databaseUserDto.getDatabaseUpIpSegment();
        }

        //用户up账户对应的可用物理库
        List<String> databaseIds = Arrays.asList(databaseUserDto.getExamineDatabaseId().split(","));


        StringBuffer buffer = new StringBuffer();
        //为每个数据表进行授权
        for(DataAuthorityRecordDto dataAuthorityRecordDto:dataAuthorityRecordList){

            boolean flag = true;
            if(!databaseIds.contains(dataAuthorityRecordDto.getDatabaseId())){
                buffer.append("不具备对物理库：" + dataAuthorityRecordDto.getDatabaseId()+"的访问权限" + "<br/>");
                continue;
            }

            //1 根据编码获取表(可能有多个表，需要遍历对每个表授权)，前端传来多个相同编码记录时不要重复操作   2 前端传表
            //获取资料对应的表信息
            //List<DataTableDto> dataTableDtos = dataTableService.getByDatabaseIdAndClassId(dataAuthorityRecordDto.getDatabaseId(), dataAuthorityRecordDto.getDataClassId());

            DatabaseDto databaseDto = databaseService.getDotById(dataAuthorityRecordDto.getDatabaseId());

            //获取数据库管理账户
            DatabaseAdministratorDto databaseAdministratorDto = null;
            Set<DatabaseAdministratorDto> databaseAdministratorList = databaseDto.getDatabaseDefine().getDatabaseAdministratorList();
            for(DatabaseAdministratorDto databaseAdministratorDto1 : databaseAdministratorList){
                if(databaseAdministratorDto1.getIsManager()){
                    databaseAdministratorDto = databaseAdministratorDto1;
                    break;
                }
            }


            if(databaseDto.getDatabaseDefine().getDatabaseType().equalsIgnoreCase("xugu")){//xugu
                if(databaseAdministratorDto == null){
                    buffer.append("物理库：" + dataAuthorityRecordDto.getDatabaseId()+"没有管理员账户" + "<br/>");
                    continue;
                }
                try {
                    Xugu xugu = new Xugu(databaseDto.getDatabaseDefine().getDatabaseUrl(),databaseAdministratorDto.getUserName(),databaseAdministratorDto.getPassWord());
                    if("1".equals(dataAuthorityRecordDto.getAuthorize())){//授权读
                        xugu.addPermissions(true,databaseDto.getSchemaName(),dataAuthorityRecordDto.getTableName(),databaseUserDto.getDatabaseUpId(),"",null);
                    }else if("2".equals(dataAuthorityRecordDto.getAuthorize())){//授权写
                        xugu.addPermissions(false,databaseDto.getSchemaName(),dataAuthorityRecordDto.getTableName(),databaseUserDto.getDatabaseUpId(),"",null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    flag = false;
                    buffer.append("表："+dataAuthorityRecordDto.getTableName()+"物理库：" + dataAuthorityRecordDto.getDatabaseId()+"授权失败" + "<br/>");
                }



            }else if(databaseDto.getDatabaseDefine().getDatabaseType().equalsIgnoreCase("Gbase8a")){//gbase8a
                if(databaseAdministratorDto == null){
                    buffer.append("物理库：" + dataAuthorityRecordDto.getDatabaseId()+"没有管理员账户" + "<br/>");
                    continue;
                }
                try {
                    Gbase8a gbase8a = new Gbase8a(databaseDto.getDatabaseDefine().getDatabaseUrl(),databaseAdministratorDto.getUserName(),databaseAdministratorDto.getPassWord());
                    List<String> ips = new ArrayList<String>();
                    ips.add(ip);
                    if("1".equals(dataAuthorityRecordDto.getAuthorize())){//授权读
                        gbase8a.addPermissions(true,databaseDto.getSchemaName(),dataAuthorityRecordDto.getTableName(),databaseUserDto.getDatabaseUpId(),databaseUserDto.getDatabaseUpPassword(),ips);
                    }else if("2".equals(dataAuthorityRecordDto.getAuthorize())){//授权写
                        gbase8a.addPermissions(false,databaseDto.getSchemaName(),dataAuthorityRecordDto.getTableName(),databaseUserDto.getDatabaseUpId(),databaseUserDto.getDatabaseUpPassword(),ips);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    flag = false;
                    buffer.append("表："+dataAuthorityRecordDto.getTableName()+"物理库：" + dataAuthorityRecordDto.getDatabaseId()+"授权失败" +"<br/>");
                }

            }/*else if(databaseDto.getDatabaseDefine().getDatabaseType().equalsIgnoreCase("Cassandra")){
                //Cassandra
            }*/
            if(flag){
                mybatisQueryMapper.updateDataAuthorityRecord(dataAuthorityRecordDto.getId(),dataAuthorityRecordDto.getAuthorize(),dataAuthorityRecordDto.getCause());
                buffer.append("表："+dataAuthorityRecordDto.getTableName()+"物理库：" + dataAuthorityRecordDto.getDatabaseId()+"授权成功" + "<br/>");
            }
        }

        map.put("msg",buffer.toString());
        return map;
    }

    @Override
    public Map<String, Object> updateRecordCheckCancel(DataAuthorityApplyDto dataAuthorityApplyDto) {
        return null;
    }

    @Override
    public List<Map<String,Object>> getRecordListByUserId(String userId) {
        if(DatabaseType.databaseType.toLowerCase().equals("mysql")){
            return mybatisQueryMapper.getRecordListByUserIdMysql(userId);
        }else{
            return mybatisQueryMapper.getRecordListByUserId(userId);
        }
    }

    @Override
    public void updateRecordByApplyIdAndClassId(String apply_id,String data_class_id,Integer authorize, String cause) {
        List<DataAuthorityRecordEntity> dataAuthorityRecordEntities = dataAuthorityRecordDao.findByApplyIdAndDataClassId(apply_id, data_class_id);

        if(dataAuthorityRecordEntities != null && dataAuthorityRecordEntities.size() > 0){
            for(DataAuthorityRecordEntity dataAuthorityRecordEntity : dataAuthorityRecordEntities){
                //如果资料是专题库引用资料，审核专题库引用资料
                if(StringUtils.isNotNullString(dataAuthorityRecordEntity.getQtdbId())){
                    List<DatabaseSpecialReadWriteDto> databaseSpecialReadWriteDtos = databaseSpecialReadWriteService.findBySdbIdAndDataClassId(dataAuthorityRecordEntity.getQtdbId(), data_class_id);
                    if(databaseSpecialReadWriteDtos != null && databaseSpecialReadWriteDtos.size() > 0){
                        for(DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto : databaseSpecialReadWriteDtos){
                            if(StringUtils.isNotNullString(cause)){
                                databaseSpecialReadWriteDto.setFailureReason(cause);
                            }
                            databaseSpecialReadWriteDto.setExamineStatus(authorize);
                            databaseSpecialReadWriteService.saveDto(databaseSpecialReadWriteDto);
                        }
                    }
                }

                //资料授权审核
                mybatisQueryMapper.updateDataAuthorityRecord(dataAuthorityRecordEntity.getId(),authorize,cause);

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
    public void updateRecordByApplyIdAndClassIdAndDatabaseId(String apply_id, String data_class_id, String database_id, Integer authorize, String cause) {
        List<DataAuthorityRecordEntity> dataAuthorityRecordEntities = dataAuthorityRecordDao.findByApplyIdAndDataClassIdAndDatabaseId(apply_id, data_class_id,database_id);
        if(dataAuthorityRecordEntities != null && dataAuthorityRecordEntities.size() > 0){
            for(DataAuthorityRecordEntity dataAuthorityRecordEntity : dataAuthorityRecordEntities){
                mybatisQueryMapper.updateDataAuthorityRecord(dataAuthorityRecordEntity.getId(),authorize,cause);
            }
        }

    }
    @Override
    public Map<String, Object> getDataAuthorityList(String userId, String applyAuthority, String logicId, String dataName, String category, String schemaId) {
        Map<String,Object> result = new HashMap<String,Object>();
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("userId", userId);

        if(null == userId || "".equals(userId)) {
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
            List<Map<String,Object>> daList = mybatisQueryMapper.getDataAuthorityList(paraMap);
            result.put("returnCode", "0");
            result.put("returnMessage", "查询成功");
            result.put("DS", daList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("returnCode","1");
            result.put("returnMessage", "查询失败");
        }
        return result;
    }

    @Override
    public Map<String, Object> getDataCreator(String dataClassId) {
        Map<String,Object> result = new HashMap<String,Object>();
        try {
            DataTableEntity dataTableEntity = dataTableDao.findById(dataClassId).get();
            String userId = dataTableEntity.getUserId();
            if(!"admin".equals(userId)){
                com.alibaba.fastjson.JSONObject obj = CmadaasApiUtil.getUserInfo(userId);
                result.put("DS", obj);
            }else{
                result.put("DS", "admin");
            }
            result.put("returnCode", "0");

        } catch (Exception e) {
            e.printStackTrace();
            result.put("returnCode", "1");
            result.put("returnMessage", "查询失败 : "+e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> deleteDataAuthorityById(String applyId, String dataBaseId, String dataClassId) {
        Map<String,Object> result = new HashMap<String,Object>();
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("applyId", applyId);
        paraMap.put("dataBaseId", dataBaseId);
        paraMap.put("dataClassId",dataClassId);
        //删除前查询是否存在
        try {
            mybatisQueryMapper.delDataAuthorityByApplyId(paraMap);
//            mybatisQueryMapper.clearUselessApply();
            result.put("returnCode", "0");
            result.put("returnMessage", "删除成功。");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("returnCode", "1");
            result.put("returnMessage", "删除失败 : "+e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> getDataCategory() {
        Map<String,Object> result = new HashMap<String,Object>();
        try {
            List<Map<String,Object>> categoryList = mybatisQueryMapper.getDataCategory();
            result.put("returnCode", "0");
            result.put("DS", categoryList);
            result.put("returnMessage", "操作成功 。");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("returnCode", "1");
            result.put("returnMessage", "操作失败 ："+e.getMessage());
        }
        return result;
    }

    /**
     *
     * @description
     * @author wlg
     * @date 2020-04-22 17:06
     * @param userId
     * @return
     * @throws Exception
     */
	@Override
	public List<Map<String, Object>> getApplyDataInfo(String userId) throws Exception {
		List<Map<String,Object>> result = mybatisQueryMapper.getApplyDataInfo(userId);
		return result;
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

}
