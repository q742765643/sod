package com.piesat.schedule.rpc.service.move;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.dao.move.MoveLogDao;
import com.piesat.schedule.entity.move.MoveLogEntity;
import com.piesat.schedule.entity.move.MoveLogEntity;
import com.piesat.schedule.rpc.api.move.MoveLogService;
import com.piesat.schedule.rpc.dto.move.MoveLogDto;
import com.piesat.schedule.rpc.mapstruct.move.MoveLogMapstruct;
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
 * @create: 2019-12-29 19:28
 **/
@Service
public class MoveLogServiceImpl extends BaseService<MoveLogEntity> implements MoveLogService{
    @Autowired
    private MoveLogDao moveLogDao;
    @Autowired
    private MoveLogMapstruct moveLogMapstruct;
    @Override
    public BaseDao<MoveLogEntity> getBaseDao() {
        return moveLogDao;
    }
    @Override
    public PageBean selectMoveLogList(PageForm<MoveLogDto> pageForm){
        MoveLogEntity moveLogEntity=moveLogMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(moveLogEntity.getDatabaseId())){
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(),moveLogEntity.getDatabaseId());
        }
        if(StringUtils.isNotNullString(moveLogEntity.getDataClassId())){
            specificationBuilder.add("dataClassId", SpecificationOperator.Operator.likeAll.name(),moveLogEntity.getDataClassId());
            specificationBuilder.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(),moveLogEntity.getDataClassId());
        }
        if(StringUtils.isNotNullString(moveLogEntity.getProfileName())){
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(),moveLogEntity.getProfileName());
        }
        if(null!=moveLogEntity.getHandleCode()){
            specificationBuilder.add("handleCode",SpecificationOperator.Operator.eq.name(),moveLogEntity.getHandleCode());
        }
        if(StringUtils.isNotNullString(moveLogEntity.getTableName())){
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(),moveLogEntity.getTableName());
        }
        if(StringUtils.isNotNullString((String) moveLogEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) moveLogEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) moveLogEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) moveLogEntity.getParamt().get("endTime"));
        }
        Sort sort=Sort.by(Sort.Direction.ASC,"createTime");
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<MoveLogEntity> moveEntities= (List<MoveLogEntity>) pageBean.getPageData();
        pageBean.setPageData(moveLogMapstruct.toDto(moveEntities));
        return pageBean;

    }
    @Override
    public MoveLogDto findMoveLogById(String moveLogId){
        MoveLogEntity moveLogEntity=this.getById(moveLogId);
        return moveLogMapstruct.toDto(moveLogEntity);

    }

    @Override
    public void deleteMoveLogByIds(String[] moveLogIds){
        this.deleteByIds(Arrays.asList(moveLogIds));
    }
}

