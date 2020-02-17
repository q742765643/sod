package com.piesat.schedule.client.handler.move;

import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.OwnException;
import com.piesat.schedule.client.handler.base.BaseHandler;
import com.piesat.schedule.client.service.DatabaseOperationService;
import com.piesat.schedule.client.util.ExtractMessage;
import com.piesat.schedule.client.util.FileUtil;
import com.piesat.schedule.client.vo.ClearVo;
import com.piesat.schedule.client.vo.MoveVo;
import com.piesat.schedule.client.vo.ReplaceVo;
import com.piesat.schedule.entity.IndexVo;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.clear.ClearEntity;
import com.piesat.schedule.entity.move.MoveEntity;
import com.piesat.schedule.entity.move.MoveLogEntity;
import com.piesat.util.ResultT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 18:24
 */
@Slf4j
@Service("moveHandler")
public class MoveHandler implements BaseHandler {
    @Autowired
    private DatabaseOperationService databaseOperationService;
    @Override
    public void execute(JobInfoEntity jobInfoEntity) {
        MoveEntity moveEntity = (MoveEntity) jobInfoEntity;
        ResultT<String> resultT = new ResultT<>();
        this.preParam(moveEntity, resultT);
        log.info("迁移调用成功");
    }


    public void preParam(MoveEntity moveEntity, ResultT<String> resultT) {
        MoveVo moveVo=new MoveVo();
        MoveLogEntity moveLogEntity=new MoveLogEntity();
        this.calculateTime(moveVo,moveEntity,resultT);
        this.moveExecute(moveVo,moveLogEntity,resultT);
        if("1".equals(moveEntity.getIsClear())){
           this.clearExecute(moveVo,moveLogEntity,resultT);
        }

    }
    public void calculateTime(MoveVo moveVo,MoveEntity moveEntity, ResultT<String> resultT){
        try {
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ReplaceVo replaceVo = new ReplaceVo();
            replaceVo.setMsg(moveEntity.getConditions());
            replaceVo.setDatabaseId(moveEntity.getParentId());
            replaceVo.setDataClassId(moveEntity.getDataClassId());
            replaceVo.setDdataId(moveEntity.getDdataId());
            replaceVo.setBackupTime(moveEntity.getTriggerLastTime());
            ExtractMessage.getIndexOf(replaceVo, resultT);
            moveVo.setMoveConditions(replaceVo.getMsg());
            Set<Long> timeSet=replaceVo.getTimeSet();
            if(timeSet.size()==1){
                for (long time : timeSet) {
                    moveVo.setMoveTime(time);
                    resultT.setSuccessMessage("迁移删除时间为小于{}",format.format(time));
                }
            }
            replaceVo.setMsg(moveEntity.getClearConditions());
            ExtractMessage.getIndexOf(replaceVo, resultT);
            Set<Long> clearTimeSet=replaceVo.getTimeSet();
            if(timeSet.size()==1){
                for (long time : clearTimeSet) {
                    moveVo.setClearTime(time);
                    resultT.setSuccessMessage("清除删除时间为小于{}",format.format(time));
                }
            }
            replaceVo.setMsg(moveEntity.getArchiveConditions());
            ExtractMessage.getIndexOf(replaceVo, resultT);
            Set<Long> archiveTimeSet=replaceVo.getTimeSet();
            if(timeSet.size()==1){
                for (long time : archiveTimeSet) {
                    moveVo.setArchiveTime(time);
                    resultT.setSuccessMessage("归档清除删除时间为小于{}",format.format(time));
                }
            }
        } catch (Exception e) {
            resultT.setErrorMessage("计算时间失败,{}", OwnException.get(e));
        }

    }
    public void calculateCompensateList(List<String> compensateList, MoveLogEntity moveLogEntity, String conditions, long time, ResultT<String> resultT){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date minDate=databaseOperationService.selectKtableMaxTime(moveLogEntity.getTableName(),conditions);
        long startTime=minDate.getTime();
        if(time>0){
            String ytime=format.format(time);
            while (startTime<time){
                startTime=startTime+moveLogEntity.getMoveLimit()*1000;
                if(startTime>time){
                    startTime=time;
                }
                String ddateTime=format.format(startTime);
                conditions=conditions.replaceAll(ytime,ddateTime);
                compensateList.add(conditions);
                if(!resultT.isSuccess()){
                    return;
                }
            }
        }else{
            compensateList.add(conditions);
        }
    }
    public void moveExecute(MoveVo moveVo, MoveLogEntity moveLogEntity, ResultT<String> resultT){

        List<String> compensateList=new ArrayList<>();
        this.calculateCompensateList(compensateList,moveLogEntity,moveVo.getMoveConditions(),moveVo.getMoveTime(),resultT);
        for(String conditions:compensateList){
            this.moveAndClearLogic(0,conditions,moveVo,moveLogEntity,resultT);

        }
    }
    public void clearExecute(MoveVo moveVo, MoveLogEntity moveLogEntity, ResultT<String> resultT){

        List<String> compensateList=new ArrayList<>();
        this.calculateCompensateList(compensateList,moveLogEntity,moveVo.getClearConditions(),moveVo.getClearTime(),resultT);
        for(String conditions:compensateList){
            this.moveAndClearLogic(1,conditions,moveVo,moveLogEntity,resultT);

        }
    }

    public void moveAndClearLogic(int type,String conditions,MoveVo moveVo,MoveLogEntity moveLogEntity,ResultT<String> resultT){
        FileUtil.checkFile(moveLogEntity.getSourceDirectory(),resultT);
        if(!resultT.isSuccess()){
            return;
        }
        List<Map<String,Object>> mapKList=databaseOperationService.selectByKCondition(moveLogEntity.getTableName(),conditions);
        if(null==mapKList||mapKList.isEmpty()){
            return;
        }
        for(Map<String,Object> mapK:mapKList){
            if(type==0){
                this.moveLogic(mapK,moveLogEntity,resultT);
            }
            if(type==1){
                this.clearLogic(mapK,moveLogEntity,resultT);
            }
        }

    }
    public void moveLogic(Map<String,Object> mapK,MoveLogEntity moveLogEntity,ResultT<String> resultT){
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fkValue = (String) mapK.get(moveLogEntity.getForeignKey());
        Date ddateTimed = (Date) mapK.get("d_datetime");
        String ddateTime=format.format(ddateTimed);
        String storageSite = (String) mapK.get("d_storage_site");
        if(!storageSite.startsWith(moveLogEntity.getSourceDirectory())){
            resultT.setErrorMessage("{}源目录错误",moveLogEntity.getSourceDirectory());
            return;
        }
        String vconditions=moveLogEntity.getForeignKey()+"='"+fkValue+"' and d_datetime='"+ddateTime+"'";
        if(null!=moveLogEntity.getVTableName()){
            this.deleteVIndex(moveLogEntity,vconditions,resultT);
            if(!resultT.isSuccess()){
                return;
            }
        }
        String targetPath=storageSite.replace(moveLogEntity.getSourceDirectory(),moveLogEntity.getTargetDirectory());
        FileUtil.copyFile(storageSite,targetPath,resultT);
        if(!resultT.isSuccess()){
            return;
        }
        IndexVo kIndexVo=new IndexVo();
        kIndexVo.setNewPath(targetPath);
        kIndexVo.setTable(moveLogEntity.getTableName());
        kIndexVo.setConditions(vconditions);
        databaseOperationService.updateIndex(kIndexVo);
    }
    public void clearLogic(Map<String,Object> mapK,MoveLogEntity moveLogEntity,ResultT<String> resultT){
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fkValue = (String) mapK.get(moveLogEntity.getForeignKey());
        Date ddateTimed = (Date) mapK.get("d_datetime");
        String ddateTime=format.format(ddateTimed);
        String storageSite = (String) mapK.get("d_storage_site");
        String vconditions=moveLogEntity.getForeignKey()+"='"+fkValue+"' and d_datetime='"+ddateTime+"'";
        if(null!=moveLogEntity.getVTableName()){
            this.deleteVIndex(moveLogEntity,vconditions,resultT);
            if(!resultT.isSuccess()){
                return;
            }
        }
        FileUtil.delete(storageSite,resultT);
        if(!resultT.isSuccess()){
            return;
        }
        IndexVo kIndexVo=new IndexVo();
        kIndexVo.setTable(moveLogEntity.getTableName());
        kIndexVo.setConditions(vconditions);
        databaseOperationService.deleteIndex(kIndexVo);
    }

    public void deleteVIndex(MoveLogEntity moveLogEntity,String vconditions ,ResultT<String> resultT){
        List<Map<String,Object>> mapVList=databaseOperationService.selectByVCondition(moveLogEntity,vconditions);
        if(null==mapVList||mapVList.isEmpty()){
            return;
        }
        for(Map<String,Object> mapV:mapVList){
            String vFileNameSource = (String) mapV.get("v_field_file_name_source");
            FileUtil.delete(vFileNameSource,resultT);
            if(!resultT.isSuccess()){
                return;
            }

        }
        IndexVo vIndexVo=new IndexVo();
        vIndexVo.setTable(moveLogEntity.getVTableName());
        vIndexVo.setConditions(vconditions);
        databaseOperationService.deleteIndex(vIndexVo);
    }
}