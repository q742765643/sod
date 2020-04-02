package com.piesat.dm.rpc.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.DateUtils;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.core.api.impl.Gbase8a;
import com.piesat.dm.core.api.impl.Xugu;
import com.piesat.dm.core.parser.DatabaseType;
import com.piesat.dm.dao.DataAuthorityApplyDao;
import com.piesat.dm.dao.DataAuthorityRecordDao;
import com.piesat.dm.entity.DataAuthorityApplyEntity;
import com.piesat.dm.entity.DataAuthorityRecordEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.*;
import com.piesat.dm.rpc.dto.*;
import com.piesat.dm.rpc.mapper.DataAuthorityApplyMapper;
import com.piesat.dm.rpc.mapper.DataAuthorityRecordMapper;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
            specificationBuilder.add("applyTime",SpecificationOperator.Operator.ges.name(),(String) dataAuthorityApplyEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) dataAuthorityApplyEntity.getParamt().get("endTime"))){
            specificationBuilder.add("applyTime",SpecificationOperator.Operator.les.name(),(String) dataAuthorityApplyEntity.getParamt().get("endTime"));
        }
        Sort sort = Sort.by(Sort.Direction.DESC,"auditStatus","applyTime");
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
            authorityApply.setUserName("申请人");
            authorityApply.setTelephone("12388888888");
            authorityApply.setDepartment("部门");
        }

        pageBean.setPageData(dataAuthorityApplyDtos);
        return pageBean;
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
            }
        }
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

}
