package com.piesat.schedule.client.handler.move;

import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.OwnException;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.client.datasource.DataSourceContextHolder;
import com.piesat.schedule.client.handler.base.BaseHandler;
import com.piesat.schedule.client.service.DatabaseOperationService;
import com.piesat.schedule.client.service.move.MoveLogService;
import com.piesat.schedule.client.util.*;
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
    public void execute(JobInfoEntity jobInfoEntity,ResultT<String> resultT) {
        MoveEntity moveEntity = (MoveEntity) jobInfoEntity;
        if(null!=moveEntity.getForeignKey()){
            moveEntity.setForeignKey(moveEntity.getForeignKey().toUpperCase());
        }
        this.preParam(moveEntity, resultT);
        log.info("迁移调用成功");
    }


    public void preParam(MoveEntity moveEntity, ResultT<String> resultT) {
        MoveVo moveVo=new MoveVo();
        moveVo.setStartTime(System.currentTimeMillis());
        MoveLogEntity moveLogEntity=new MoveLogEntity();
        BeanUtils.copyProperties(moveEntity,moveLogEntity);
        moveLogEntity.setTriggerTime(moveEntity.getTriggerLastTime());
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
            log.error("迁移异常:{}",OwnException.get(e));

        } finally {
            Map mapDetail = new HashMap(10);
            mapDetail.put("COMPLETEDATA", moveVo.getMoveCount());
            mapDetail.put("DETAIL", resultT.getProcessMsg());
            diSendVo.setTaskDetail(mapDetail);
            diSendVo.setEndTimeA(System.currentTimeMillis());
            if (resultT.isSuccess()) {
                diSendVo.setTaskState("成功");
            } else {
                log.error("迁移任务ID{},任务名{},错误{}",moveEntity.getId(),moveEntity.getProfileName(),resultT.getMsg());
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
            moveLogEntity.setConditions(moveVo.getMoveConditions());
            moveLogEntity.setClearConditions(moveVo.getClearConditions());
            moveLogEntity.setMoveTime(moveVo.getMoveTime());
            moveLogEntity=this.insertMoveLog(moveLogEntity,moveEntity,resultT);
            if(!resultT.isSuccess()){
                return;
            }

            resultT.setSuccessMessage("开始nas迁移");
            log.info("开始nas迁移");
            this.moveExecute(moveVo,moveLogEntity,resultT);


            if("1".equals(moveEntity.getIsClear())){
                long clearcount= databaseOperationService.selectTableCount(moveLogEntity.getParentId(),moveLogEntity.getTableName(),moveVo.getClearConditions(),resultT);
                moveVo.setClearCount(clearcount);
                if(clearcount==0){
                    resultT.setSuccessMessage("需要清除数为0条");
                    log.info("需要清除数为0条");
                }
                if(clearcount>0){
                    this.clearExecute(moveVo,moveLogEntity,resultT);

                }
            }
        } catch (Exception e) {
            resultT.setErrorMessage("迁移异常:{}",OwnException.get(e));
            log.error("迁移异常:{}",OwnException.get(e));

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
            log.error("插入日志失败:{}", OwnException.get(e));
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
            log.error("修改日志出错{}",OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_13_ERROR.getKey());
            EiSendUtil.executeSqlException(resultT);
        }

    }
    public void checkMoveLogEntity(MoveLogEntity moveLogEntity, ResultT<String> resultT){
        if(!StringUtils.isNotNullString(moveLogEntity.getParentId())){
            resultT.setErrorMessage("物理库基础库ID不能为空");
            log.error("物理库基础库ID不能为空");
            return;
        }
        if(!StringUtils.isNotNullString(moveLogEntity.getDataClassId())){
            resultT.setErrorMessage("存储编码不能为空");
            log.error("存储编码不能为空");
            return;
        }
        if(!StringUtils.isNotNullString(moveLogEntity.getDdataId())){
            resultT.setErrorMessage("四级编码不能为空");
            log.error("四级编码不能为空");

            return;
        }
        if(!StringUtils.isNotNullString(moveLogEntity.getTableName())){
            resultT.setErrorMessage("表名不能为空");
            log.error("表名不能为空");

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
                    log.info("迁移删除时间为小于{}",format.format(time));
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
                    log.info("清除删除时间为小于{}",format.format(time));

                }
            }
        } catch (Exception e) {
            resultT.setErrorMessage("计算时间失败,{}", OwnException.get(e));
            log.error("计算时间失败,{}", OwnException.get(e));
        }

    }
    public void calculateCompensateList(List<String> compensateList, MoveLogEntity moveLogEntity, String conditions, long time, ResultT<String> resultT){
        DataSourceContextHolder.setDataSource(moveLogEntity.getParentId());

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date minDate=databaseOperationService.selectKtableMaxTime(moveLogEntity.getTableName(),conditions);
            long startTime=0;
            if(null!=minDate){
                startTime=minDate.getTime();
            }
            if(time!=0&&startTime!=0){
                resultT.setSuccessMessage("按迁移频率开始切割");
                log.info("按迁移频率开始切割");
                String ytime=format.format(time);
                while (startTime<time){
                    startTime=startTime+moveLogEntity.getMoveLimit()*1000;
                    if(startTime>time){
                        startTime=time;
                    }
                    String ddateTime=format.format(startTime);
                    String newconditions=conditions.replaceAll(ytime,ddateTime);
                    compensateList.add(newconditions);
                }
            }else{
                compensateList.add(conditions);
            }
        } catch (Exception e) {
            resultT.setErrorMessage("时间切割失败{}",OwnException.get(e));
            log.error("时间切割失败{}",OwnException.get(e));
        }finally {
            DataSourceContextHolder.clearDataSource();
        }
    }
    public void moveExecute(MoveVo moveVo, MoveLogEntity moveLogEntity, ResultT<String> resultT){

        List<String> compensateList=new ArrayList<>();
        long movecount= databaseOperationService.selectTableCount(moveLogEntity.getParentId(),moveLogEntity.getTableName(),moveVo.getMoveConditions(),resultT);
        moveVo.setMoveCount(movecount);
        if(movecount==0){
            resultT.setSuccessMessage("需要迁移为0条");
            log.info("需要迁移为0条");
            return;
        }
        this.calculateCompensateList(compensateList,moveLogEntity,moveVo.getMoveConditions(),moveVo.getMoveTime(),resultT);
        resultT.setSuccessMessage("开始nas迁移条件为{}",compensateList.get(0));
        resultT.setSuccessMessage("结束nas迁移条件为{}",compensateList.get(compensateList.size()-1));
        resultT.setSuccessMessage("一共需要执行次数{}",compensateList.size());
        log.info("开始nas迁移条件为{}",compensateList.get(0));
        log.info("结束nas迁移条件为{}",compensateList.get(compensateList.size()-1));
        log.info("一共需要执行次数{}",compensateList.size());

        for(String conditions:compensateList){

            this.moveAndClearLogic(0,conditions,moveVo,moveLogEntity,resultT);
            if(!resultT.isSuccess()){
                resultT.setSuccessMessage("条件为{}时执行失败",conditions);
                log.error("条件为{}时执行失败",conditions);
                return;
            }
        }
    }
    public void clearExecute(MoveVo moveVo, MoveLogEntity moveLogEntity, ResultT<String> resultT){

        List<String> compensateList=new ArrayList<>();
        this.calculateCompensateList(compensateList,moveLogEntity,moveVo.getClearConditions(),moveVo.getClearTime(),resultT);
        resultT.setSuccessMessage("开始nas清除");
        resultT.setSuccessMessage("开始nas清除条件为{}",compensateList.get(0));
        resultT.setSuccessMessage("结束nas清除条件为{}",compensateList.get(compensateList.size()-1));
        resultT.setSuccessMessage("一共需要执行次数{}",compensateList.size());

        log.info("开始nas清除条件为{}",compensateList.get(0));
        log.info("结束nas清除条件为{}",compensateList.get(compensateList.size()-1));
        log.info("一共需要执行次数{}",compensateList.size());
        for(String conditions:compensateList){
            this.moveAndClearLogic(1,conditions,moveVo,moveLogEntity,resultT);
            if(!resultT.isSuccess()){
                resultT.setSuccessMessage("条件为{}时执行失败",conditions);
                log.error("条件为{}时执行失败",conditions);
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
        //String fkValue = (String) mapK.get(moveLogEntity.getForeignKey());
        Date ddateTimed = (Date) mapK.get("D_DATETIME");
        String ddateTime=format.format(ddateTimed);
        String storageSite = (String) mapK.get("D_STORAGE_SITE");
        if(!storageSite.startsWith(moveLogEntity.getSourceDirectory())){
            resultT.setErrorMessage("源目录错误{}",moveLogEntity.getSourceDirectory());
            log.error("{}源目录错误",moveLogEntity.getSourceDirectory());
            return;
        }

        if(null!=moveLogEntity.getVTableName()&&StringUtils.isNotNullString(moveLogEntity.getVTableName())){
            String fkconditionsV=TableForeignKeyUtil.getMoveVsql(moveLogEntity.getForeignKey(),mapK,resultT);
            if(!resultT.isSuccess()){
                return;
            }
            String vconditions=  fkconditionsV+" and d_datetime='"+ddateTime+"'";
            this.deleteVIndex(moveLogEntity,vconditions,resultT);
            if(!resultT.isSuccess()){
                return;
            }
        }
        String targetPath=storageSite.replace(moveLogEntity.getSourceDirectory(),moveLogEntity.getTargetDirectory());
        FileUtil.copyMoveFile(storageSite,targetPath,resultT);
        if(!resultT.isSuccess()){
            return;
        }
        FileUtil.delete(storageSite,resultT);
        IndexVo kIndexVo=new IndexVo();
        kIndexVo.setNewPath(targetPath);
        kIndexVo.setTable(moveLogEntity.getTableName());
        String p=TableForeignKeyUtil.getMoveKsql(moveLogEntity.getPrimaryKey(),mapK,resultT);
        if(!resultT.isSuccess()){
            return;
        }
        String kconditions=p+" and d_datetime='"+ddateTime+"'";
        kIndexVo.setConditions(kconditions);
        databaseOperationService.updateIndex(moveLogEntity.getParentId(),kIndexVo,resultT);
    }
    public void clearLogic(Map<String,Object> mapK,MoveLogEntity moveLogEntity,ResultT<String> resultT){
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date ddateTimed = (Date) mapK.get("D_DATETIME");
        String ddateTime=format.format(ddateTimed);
        String storageSite = (String) mapK.get("D_STORAGE_SITE");

        if(null!=moveLogEntity.getVTableName()&&StringUtils.isNotNullString(moveLogEntity.getVTableName())){
            String fkconditionsV=TableForeignKeyUtil.getMoveVsql(moveLogEntity.getForeignKey(),mapK,resultT);
            if(!resultT.isSuccess()){
                return;
            }
            String vconditions= fkconditionsV+"and  d_datetime='"+ddateTime+"'";
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
        String p=TableForeignKeyUtil.getMoveKsql(moveLogEntity.getPrimaryKey(),mapK,resultT);
        if(!resultT.isSuccess()){
            return;
        }
        String kconditions=p+" and d_datetime='"+ddateTime+"'";
        kIndexVo.setConditions(kconditions);
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
            String vFileNameSource = (String) mapV.get("V_FIELD_FILE_NAME_SOURCE");
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