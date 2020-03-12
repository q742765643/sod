package com.piesat.dm.rpc.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.DateUtils;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.core.parser.DatabaseType;
import com.piesat.dm.dao.DataAuthorityApplyDao;
import com.piesat.dm.entity.DataAuthorityApplyEntity;
import com.piesat.dm.entity.DataAuthorityRecordEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.DataAuthorityApplyService;
import com.piesat.dm.rpc.dto.DataAuthorityApplyDto;
import com.piesat.dm.rpc.dto.DataAuthorityRecordDto;
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

        List<DataAuthorityApplyDto> dataAuthorityApplyDtos = new ArrayList<DataAuthorityApplyDto>();
        //循环遍历
        for(DataAuthorityApplyEntity authorityApply : dataAuthorityApplyEntities){

            //若所申请资料全部审核了，将这条申请记录修改为“已审核”
            Set<DataAuthorityRecordEntity> dataAuthorityRecordList = authorityApply.getDataAuthorityRecordList();
            int num = 0;
            for(DataAuthorityRecordEntity authorityRecord:dataAuthorityRecordList){
                if(authorityRecord.getAuthorize() != null){
                    num++;
                    if (num == dataAuthorityRecordList.size()) {
                        authorityApply.setAuditStatus("02");
                        authorityApply = this.saveNotNull(authorityApply);
                    }
                }
            }

            DataAuthorityApplyDto dataAuthorityApplyDto1 = dataAuthorityApplyMapper.toDto(authorityApply);
            List<DataAuthorityRecordDto> dataAuthorityRecordDtos = dataAuthorityRecordMapper.toDto(new ArrayList<DataAuthorityRecordEntity>(dataAuthorityRecordList));
            dataAuthorityApplyDto1.setDataAuthorityRecordList(new HashSet<DataAuthorityRecordDto>(dataAuthorityRecordDtos));


            //遍历所有用户信息找到每条记录对应的用户信息
            dataAuthorityApplyDto1.setUserName("申请人");
            dataAuthorityApplyDto1.setTelephone("12388888888");
            dataAuthorityApplyDto1.setDepartment("部门");
            dataAuthorityApplyDtos.add(dataAuthorityApplyDto1);
        }

        pageBean.setPageData(dataAuthorityApplyDtos);
        return pageBean;
    }



    /*@Override
    public PageBean selectPageList(PageForm<DataAuthorityApplyDto> pageForm) {
        DataAuthorityApplyEntity sataAuthorityApplyEntity=dataAuthorityApplyMapper.toEntity(pageForm.getT());
        DataAuthorityApplyDto dataAuthorityApplyDto = pageForm.getT();
        PageHelper.startPage(pageForm.getCurrentPage(),pageForm.getPageSize());

        String userId="";
        if(StringUtils.isNotNullString(dataAuthorityApplyDto.getUserName())){
            //调用接口 根据用户名查询用户id
             userId="";
        }
        String applyTime = "";
        if(dataAuthorityApplyDto.getApplyTime() != null){
            applyTime = DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss",dataAuthorityApplyDto.getApplyTime());
        }
        String endTime = "";
        if(dataAuthorityApplyDto.getEndTime() != null){
            endTime = DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss",dataAuthorityApplyDto.getEndTime());
        }

        //根据条件查询当前分页所有
        List<Map<String,Object>> lists = mybatisQueryMapper.selectDataAuthorityApplyPageList(dataAuthorityApplyDto.getAuditStatus(),userId,applyTime,endTime);
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(lists);
        //获取当前页数据
        PageBean pageBean=new PageBean(pageInfo.getTotal(),pageInfo.getPages(),lists);
        return pageBean;
    }*/

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
        Set<DataAuthorityRecordDto> dataAuthorityRecordList = dataAuthorityApplyDto.getDataAuthorityRecordList();
        for(DataAuthorityRecordDto dataAuthorityRecordDto:dataAuthorityRecordList){

            mybatisQueryMapper.updateDataAuthorityRecord(dataAuthorityRecordDto.getId(),dataAuthorityRecordDto.getAuthorize(),dataAuthorityRecordDto.getCause());
        }

        map.put("returnCode",0);

        return map;
    }
}
