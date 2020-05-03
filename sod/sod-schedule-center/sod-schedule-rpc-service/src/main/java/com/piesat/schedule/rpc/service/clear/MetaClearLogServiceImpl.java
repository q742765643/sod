package com.piesat.schedule.rpc.service.clear;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.common.utils.poi.ExcelUtil;
import com.piesat.schedule.dao.clear.MetaClearLogDao;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.backup.MetaBackupLogEntity;
import com.piesat.schedule.entity.clear.MetaClearLogEntity;
import com.piesat.schedule.rpc.api.clear.MetaClearLogService;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.rpc.dto.backup.MetaBackupLogDto;
import com.piesat.schedule.rpc.dto.clear.MetaClearDto;
import com.piesat.schedule.rpc.dto.clear.MetaClearLogDto;
import com.piesat.schedule.rpc.mapstruct.clear.MetaClearLogMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-09 17:29
 **/
@Service
public class MetaClearLogServiceImpl extends BaseService<MetaClearLogEntity> implements MetaClearLogService {
    @Autowired
    private MetaClearLogMapstruct metaClearLogMapstruct;
    @Autowired
    private MetaClearLogDao metaClearLogDao;
    @Override
    public BaseDao<MetaClearLogEntity> getBaseDao() {
        return metaClearLogDao;
    }

    @Override
    public PageBean selectMetaClearLogList(PageForm<MetaClearLogDto> pageForm){
        MetaClearLogEntity metaClearLogEntity=metaClearLogMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();

        if(StringUtils.isNotNullString(metaClearLogEntity.getDatabaseName())){
            specificationBuilder.add("databaseName", SpecificationOperator.Operator.likeAll.name(),metaClearLogEntity.getDatabaseName());
        }
        if(StringUtils.isNotNullString(metaClearLogEntity.getTaskName())){
            specificationBuilder.add("taskName", SpecificationOperator.Operator.likeAll.name(),metaClearLogEntity.getTaskName());
        }
        if(null!=metaClearLogEntity.getHandleCode()){
            specificationBuilder.add("handleCode",SpecificationOperator.Operator.eq.name(),metaClearLogEntity.getHandleCode());
        }
        if(StringUtils.isNotNullString((String) metaClearLogEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) metaClearLogEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) metaClearLogEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) metaClearLogEntity.getParamt().get("endTime"));
        }
        Sort sort=Sort.by(Sort.Direction.DESC,"createTime");
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<MetaClearLogEntity> metaClearLogEntities= (List<MetaClearLogEntity>) pageBean.getPageData();
        pageBean.setPageData(metaClearLogMapstruct.toDto(metaClearLogEntities));
        return pageBean;

    }
    @Override
    public MetaClearLogDto findMetaClearLogById(String metaClearLogId){
        MetaClearLogEntity metaClearLogEntity=this.getById(metaClearLogId);
        return metaClearLogMapstruct.toDto(metaClearLogEntity);

    }
    @Override
    public void deleteMetaClearLogByIds(String[] metaClearLogIds){
        this.deleteByIds(Arrays.asList(metaClearLogIds));
    }

    public List<MetaClearLogEntity> selectMetaClearLogList(MetaClearLogDto metaClearLogDto){
        MetaClearLogEntity metaClearLogEntity=metaClearLogMapstruct.toEntity(metaClearLogDto);
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();

        if(StringUtils.isNotNullString(metaClearLogEntity.getDatabaseName())){
            specificationBuilder.add("databaseName", SpecificationOperator.Operator.likeAll.name(),metaClearLogEntity.getDatabaseName());
        }
        if(StringUtils.isNotNullString(metaClearLogEntity.getTaskName())){
            specificationBuilder.add("taskName", SpecificationOperator.Operator.likeAll.name(),metaClearLogEntity.getTaskName());
        }
        if(null!=metaClearLogEntity.getHandleCode()){
            specificationBuilder.add("handleCode",SpecificationOperator.Operator.eq.name(),metaClearLogEntity.getHandleCode());
        }
        if(StringUtils.isNotNullString((String) metaClearLogEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) metaClearLogEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) metaClearLogEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) metaClearLogEntity.getParamt().get("endTime"));
        }
        Sort sort=Sort.by(Sort.Direction.DESC,"createTime");
        List<MetaClearLogEntity> metaClearLogEntities=this.getAll(specificationBuilder.generateSpecification(),sort);
        return metaClearLogEntities;

    }

    @Override
    public void exportExcel(MetaClearLogDto metaClearLogDto){
        List<MetaClearLogEntity> entities=this.selectMetaClearLogList(metaClearLogDto);
        ExcelUtil<MetaClearLogEntity> util=new ExcelUtil(MetaClearLogEntity.class);
        util.exportExcel(entities,"元数据清除日志");
    }

}

