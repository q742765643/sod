package com.piesat.ucenter.rpc.service.monitor;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.ucenter.dao.monitor.OperLogDao;
import com.piesat.ucenter.entity.monitor.OperLogEntity;
import com.piesat.ucenter.rpc.api.monitor.OperLogService;
import com.piesat.ucenter.rpc.dto.monitor.OperLogDto;
import com.piesat.ucenter.rpc.mapstruct.monitor.OperLogMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-16 15:07
 **/
@Service
public class OperLogServiceImpl extends BaseService<OperLogEntity> implements OperLogService{
    @Autowired
    private OperLogMapstruct operLogMapstruct;
    @Autowired
    private OperLogDao operLogDao;
    @Override
    public BaseDao<OperLogEntity> getBaseDao() {
        return operLogDao;
    }
    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    @Override
    public void insertOperlog(OperLogDto operLog)
    {
        OperLogEntity operLogEntity=operLogMapstruct.toEntity(operLog);
        this.save(operLogEntity);
    }

    /**
     * 查询系统操作日志集合
     *
     * @param pageForm 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public PageBean selectOperLogList(PageForm<OperLogDto> pageForm)
    {
        OperLogDto operLog=pageForm.getT();
        SimpleSpecificationBuilder<OperLogEntity> specificationBuilder=new SimpleSpecificationBuilder<>();
        if(null!=operLog.getTitle()&&!"".equals(operLog.getTitle())){
            specificationBuilder.add("title", SpecificationOperator.Operator.likeAll.name(),operLog.getTitle());
        }
        if(null!=operLog.getBusinessType()){
            specificationBuilder.add("businessType",SpecificationOperator.Operator.eq.name(),operLog.getBusinessTypes());
        }
        if(null!=operLog.getBusinessTypes()&&operLog.getBusinessTypes().length>0){
            specificationBuilder.add("businessType",SpecificationOperator.Operator.in.name(),operLog.getBusinessTypes());
        }
        if(null!=operLog.getStatus()){
            specificationBuilder.add("status",SpecificationOperator.Operator.eq.name(),operLog.getBusinessTypes());
        }
        if(null!=operLog.getOperName()&&!"".equals(operLog.getOperName())){
            specificationBuilder.add("operName",SpecificationOperator.Operator.likeAll.name(),operLog.getBusinessTypes());
        }
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,null);
        List<OperLogEntity> logEntities= (List<OperLogEntity>) pageBean.getPageData();
        pageBean.setPageData(operLogMapstruct.toDto(logEntities));
        return pageBean;
    }

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public void deleteOperLogByIds(String[] operIds)
    {
        this.deleteByIds(Arrays.asList(operIds));
    }

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public OperLogDto selectOperLogById(String operId)
    {
        OperLogEntity operLogEntity=this.getById(operId);
        return operLogMapstruct.toDto(operLogEntity);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog()
    {
       this.deleteAll();
    }


}

