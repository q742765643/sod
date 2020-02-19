package com.piesat.schedule.rpc.service.move;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.dao.move.MoveDao;
import com.piesat.schedule.entity.move.MoveEntity;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.api.move.MoveService;
import com.piesat.schedule.rpc.dto.move.MoveDto;
import com.piesat.schedule.rpc.mapstruct.move.MoveMapstruct;
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
 * @create: 2019-12-24 16:27
 **/
@Service
public class MoveServiceImpl extends BaseService<MoveEntity> implements MoveService{
    @Autowired
    private MoveDao moveDao;
    @Autowired
    private MoveMapstruct moveMapstruct;
    @Autowired
    private JobInfoService jobInfoService;
    @Override
    public BaseDao<MoveEntity> getBaseDao() {
        return moveDao;
    }
    @Override
    public PageBean selectMoveList(PageForm<MoveDto> pageForm){
        MoveEntity moveEntity=moveMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(moveEntity.getDatabaseId())){
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(),moveEntity.getDatabaseId());
        }
        if(StringUtils.isNotNullString(moveEntity.getDataClassId())){
            specificationBuilder.add("dataClassId", SpecificationOperator.Operator.likeAll.name(),moveEntity.getDataClassId());
            specificationBuilder.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(),moveEntity.getDataClassId());
        }
        if(StringUtils.isNotNullString(moveEntity.getProfileName())){
            specificationBuilder.add("profileName", SpecificationOperator.Operator.likeAll.name(),moveEntity.getProfileName());
        }
        if(null!=moveEntity.getTriggerStatus()){
            specificationBuilder.add("triggerStatus",SpecificationOperator.Operator.eq.name(),moveEntity.getTriggerStatus());
        }
        if(StringUtils.isNotNullString(moveEntity.getTableName())){
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(),moveEntity.getTableName());
        }
        if(StringUtils.isNotNullString((String) moveEntity.getParamt().get("beginTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.ges.name(),(String) moveEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) moveEntity.getParamt().get("endTime"))){
            specificationBuilder.add("createTime",SpecificationOperator.Operator.les.name(),(String) moveEntity.getParamt().get("endTime"));
        }
        Sort sort=Sort.by(Sort.Direction.ASC,"createTime");
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<MoveEntity> moveEntities= (List<MoveEntity>) pageBean.getPageData();
        pageBean.setPageData(moveMapstruct.toDto(moveEntities));
        return pageBean;

    }
    public MoveDto selectmoveByParam(String databaseId, String dataClassId){
        PageForm pageForm=new PageForm(1,1);
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(databaseId)){
            specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(),databaseId);
        }
        if(StringUtils.isNotNullString(dataClassId)){
            specificationBuilder.add("dataClassId", SpecificationOperator.Operator.likeAll.name(),dataClassId);
            specificationBuilder.addOr("ddataId", SpecificationOperator.Operator.likeAll.name(),dataClassId);
        }
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,Sort.unsorted());
        List<MoveEntity> moveEntities= (List<MoveEntity>) pageBean.getPageData();
        if(null!=moveEntities&&!moveEntities.isEmpty()){
            return moveMapstruct.toDto(moveEntities.get(0));
        }
        return null;
    }
    @Override
    public MoveDto findMoveById(String moveId){
        MoveEntity moveEntity=this.getById(moveId);
        return moveMapstruct.toDto(moveEntity);

    }
    @Override
    public void saveMove(MoveDto moveDto){
        MoveEntity moveEntity=moveMapstruct.toEntity(moveDto);
        moveEntity=this.saveNotNull(moveEntity);
        jobInfoService.start(moveMapstruct.toDto(moveEntity));
    }
    @Override
    public void updateMove(MoveDto moveDto){
        MoveEntity moveEntity=moveMapstruct.toEntity(moveDto);
        this.saveNotNull(moveEntity);
        jobInfoService.start(moveDto);
    }
    @Override
    public void deleteMoveByIds(String[] moveIds){
        this.deleteByIds(Arrays.asList(moveIds));
        jobInfoService.stopByIds(Arrays.asList(moveIds));
    }
}

