package com.piesat.schedule.client.handler.move;

import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.OwnException;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.client.datasource.DataSourceContextHolder;
import com.piesat.schedule.client.handler.base.BaseHandler;
import com.piesat.schedule.client.service.DatabaseOperationService;
import com.piesat.schedule.client.service.move.MoveLogService;
import com.piesat.schedule.client.util.DiSendUtil;
import com.piesat.schedule.client.util.EiSendUtil;
import com.piesat.schedule.client.util.ExtractMessage;
import com.piesat.schedule.client.util.FileUtil;
import com.piesat.schedule.client.vo.*;
import com.piesat.schedule.entity.IndexVo;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.clear.ClearEntity;
import com.piesat.schedule.entity.clear.ClearLogEntity;
import com.piesat.schedule.entity.move.MoveEntity;
import com.piesat.schedule.entity.move.MoveLogEntity;
import com.piesat.util.ResultT;
import com.piesat.util.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
    @Autowired
    private MoveLogService moveLogService;
    @Override
    public void execute(JobInfoEntity jobInfoEntity) {
        MoveEntity moveEntity = (MoveEntity) jobInfoEntity;
        ResultT<String> resultT = new ResultT<>();
        this.preParam(moveEntity, resultT);
        log.info("迁移调用成功");
    }


    public void preParam(MoveEntity moveEntity, ResultT<String> resultT) {
        MoveVo moveVo=new MoveVo();
        moveVo.setStartTime(System.currentTimeMillis());
        MoveLogEntity moveLogEntity=new MoveLogEntity();
        BeanUtils.copyProperties(moveEntity,moveLogEntity);
        moveLogEntity.setId(null);

        DiSendVo diSendVo = new DiSendVo();
        long occurTime = System.currentTimeMillis();
        diSendVo.setStartTimeS(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(moveEntity.getTriggerLastTime()));
        diSendVo.setSendPhys(moveEntity.getParentId());
        diSendVo.setTaskId(moveEntity.getId());
        diSendVo.setDataType(moveEntity.getDdataId());
        diSendVo.setTaskName(moveEntity.getProfileName());
        diSendVo.setStartTimeA(System.currentTimeMillis());

        try {
           this.moveAndClearExecute(moveVo,moveEntity,moveLogEntity,resultT);
        } catch (Exception e) {
            resultT.setErrorMessage("迁移异常:{}",OwnException.get(e));

        } finally {
            Map mapDetail = new HashMap(10);
            mapDetail.put("COMPLETEDATA", moveVo.getMoveCount());
            mapDetail.put("DETAIL", resultT.getProcessMsg());
            diSendVo.setTaskDetail(mapDetail);
            diSendVo.setEndTimeA(System.currentTimeMillis());
            if (resultT.isSuccess()) {
                diSendVo.setTaskState("成功");
            } else {
                diSendVo.setTaskState("失败");
                diSendVo.setTaskErrorReason(resultT.getMsg());
                diSendVo.setTaskErrorTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(System.currentTimeMillis())));
            }
            diSendVo.setRecordTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(System.currentTimeMillis())));
            DiSendUtil.send(moveEntity.getTriggerLastTime(), "迁移任务", diSendVo);

            if(!resultT.isSuccess()){
                EiSendVo eiSendVo=new EiSendVo();
                EiSendUtil.send(eiSendVo,1,moveEntity.getProfileName(),occurTime,resultT);
            }
        }

    }
    public void moveAndClearExecute( MoveVo moveVo,MoveEntity moveEntity,MoveLogEntity moveLogEntity,ResultT<String> resultT){
        try {
            this.calculateTime(moveVo,moveEntity,resultT);
            this.checkMoveLogEntity(moveLogEntity,resultT);
            if(!resultT.isSuccess()){
                return;
            }
            moveLogEntity=this.insertMoveLog(moveLogEntity,moveEntity,resultT);
            if(!resultT.isSuccess()){
                return;
            }
            long clearcount= databaseOperationService.selectTableCount(moveLogEntity.getParentId(),moveLogEntity.getTableName(),moveVo.getClearConditions(),resultT);
            moveVo.setClearCount(clearcount);
            resultT.setSuccessMessage("开始nas迁移");
            this.moveExecute(moveVo,moveLogEntity,resultT);
            if("1".equals(moveEntity.getIsClear())&&clearcount>0){

                this.clearExecute(moveVo,moveLogEntity,resultT);
            }
        } catch (Exception e) {
            resultT.setErrorMessage("迁移异常:{}",OwnException.get(e));

        }finally {
            moveVo.setEndTime(System.currentTimeMillis());
            this.updateMoveLog(moveVo,moveLogEntity,resultT);
        }
    }
    public MoveLogEntity insertMoveLog(MoveLogEntity moveLogEntity,MoveEntity moveEntity,ResultT<String> resultT){
        try {
            moveLogEntity.setJobId(moveEntity.getId());
            moveLogEntity.setHandleCode("0");
            moveLogEntity.setTriggerCode(1);
            moveLogEntity.setHandleTime(new Date());
            moveLogEntity=moveLogService.saveNotNull(moveLogEntity);
            return moveLogEntity;
        }  catch (Exception e) {
            resultT.setErrorMessage("插入日志失败:{}", OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_13_ERROR.getKey());
            EiSendUtil.executeSqlException(resultT);
        }
        return null;


    }
    public void updateMoveLog(MoveVo moveVo, MoveLogEntity moveLogEntity, ResultT<String> resultT){
        try {
            moveLogEntity.setHandleCode("1");
            moveLogEntity.setElapsedTime((moveVo.getEndTime()-moveVo.getStartTime())/1000);
            if(!resultT.isSuccess()){
                moveLogEntity.setHandleCode("2");
            }
            moveLogEntity.setMoveCount(moveVo.getMoveCount());
            moveLogEntity.setClearCount(moveVo.getClearCount());
            moveLogEntity.setHandleMsg(resultT.getProcessMsg().toString());
            moveLogService.saveNotNull(moveLogEntity);
        } catch (Exception e){
            resultT.setErrorMessage("修改日志出错{}",OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_13_ERROR.getKey());
            EiSendUtil.executeSqlException(resultT);
        }

    }
    public void checkMoveLogEntity(MoveLogEntity moveLogEntity, ResultT<String> resultT){
        if(!StringUtils.isNotNullString(moveLogEntity.getParentId())){
            resultT.setErrorMessage("物理库基础库ID不能为空");
            return;
        }
        if(!StringUtils.isNotNullString(moveLogEntity.getDataClassId())){
            resultT.setErrorMessage("存储编码不能为空");
            return;
        }
        if(!StringUtils.isNotNullString(moveLogEntity.getDdataId())){
            resultT.setErrorMessage("四级编码不能为空");
            return;
        }
        if(!StringUtils.isNotNullString(moveLogEntity.getTableName())){
            resultT.setErrorMessage("表名不能为空");

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
            moveVo.setClearConditions(replaceVo.getMsg());
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
        DataSourceContextHolder.setDataSource(moveLogEntity.getParentId());

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
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
                }
            }else{
                compensateList.add(conditions);
            }
            resultT.setSuccessMessage("按迁移频率开始切割");
        } catch (Exception e) {
            resultT.setErrorMessage("时间切割失败{}",OwnException.get(e));
        }finally {
            DataSourceContextHolder.clearDataSource();
        }
    }
    public void moveExecute(MoveVo moveVo, MoveLogEntity moveLogEntity, ResultT<String> resultT){

        List<String> compensateList=new ArrayList<>();
        long movecount= databaseOperationService.selectTableCount(moveLogEntity.getParentId(),moveLogEntity.getTableName(),moveVo.getMoveConditions(),resultT);
        moveVo.setMoveCount(movecount);

        this.calculateCompensateList(compensateList,moveLogEntity,moveVo.getMoveConditions(),moveVo.getMoveTime(),resultT);
        for(String conditions:compensateList){
            resultT.setSuccessMessage("条件为{}",conditions);
            this.moveAndClearLogic(0,conditions,moveVo,moveLogEntity,resultT);
            if(!resultT.isSuccess()){
                return;
            }
        }
    }
    public void clearExecute(MoveVo moveVo, MoveLogEntity moveLogEntity, ResultT<String> resultT){

        List<String> compensateList=new ArrayList<>();
        this.calculateCompensateList(compensateList,moveLogEntity,moveVo.getClearConditions(),moveVo.getClearTime(),resultT);
        resultT.setSuccessMessage("开始nas清除");
        for(String conditions:compensateList){
            resultT.setSuccessMessage("条件为{}",conditions);
            this.moveAndClearLogic(1,conditions,moveVo,moveLogEntity,resultT);
            if(!resultT.isSuccess()){
                return;
            }
        }
    }

    public void moveAndClearLogic(int type,String conditions,MoveVo moveVo,MoveLogEntity moveLogEntity,ResultT<String> resultT){
        FileUtil.checkFile(moveLogEntity.getSourceDirectory(),resultT);
        if(!resultT.isSuccess()){
            return;
        }

        List<Map<String,Object>> mapKList=databaseOperationService.selectByKCondition(moveLogEntity.getParentId(),moveLogEntity.getTableName(),conditions,resultT);
        if(!resultT.isSuccess()){
            return;
        }
        if(null==mapKList||mapKList.isEmpty()){
            return;
        }
        for(Map<String,Object> mapK:mapKList){
            if(type==0){
                this.moveLogic(mapK,moveLogEntity,resultT);
                if(!resultT.isSuccess()){
                    return;
                }
            }
            if(type==1){
                this.clearLogic(mapK,moveLogEntity,resultT);
                if(!resultT.isSuccess()){
                    return;
                }
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
        databaseOperationService.updateIndex(moveLogEntity.getParentId(),kIndexVo,resultT);
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
        databaseOperationService.deleteIndex(moveLogEntity.getParentId(),kIndexVo,resultT);
    }

    public void deleteVIndex(MoveLogEntity moveLogEntity,String vconditions ,ResultT<String> resultT){
        List<Map<String,Object>> mapVList=databaseOperationService.selectByVCondition(moveLogEntity,vconditions,resultT);
        if(!resultT.isSuccess()){
            return;
        }
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
        databaseOperationService.deleteIndex(moveLogEntity.getParentId(),vIndexVo,resultT);
    }
}