package com.piesat.schedule.client.service.recover;

import com.piesat.common.utils.OwnException;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.client.business.BaseBusiness;
import com.piesat.schedule.client.enums.BusinessEnum;
import com.piesat.schedule.client.util.FileUtil;
import com.piesat.schedule.client.util.Md5Utils;
import com.piesat.schedule.client.util.ZipUtils;
import com.piesat.schedule.client.vo.RecoverMetaVo;
import com.piesat.schedule.entity.recover.MetaRecoverLogEntity;
import com.piesat.schedule.entity.recover.RecoverLogEntity;
import com.piesat.util.ResultT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-05 11:21
 **/
@Slf4j
@Service
public class DataRecoverService {
    @Autowired
    private RecoverLogService recoverLogService;
    @Value("${backup.temp-path}")
    private String backupTempPath;

    public void recoverStructedData(RecoverLogEntity recoverLogEntity){

        ResultT<String> resultT=new ResultT();
        long startTime=System.currentTimeMillis();
        String[] filePaths=recoverLogEntity.getStorageDirectory().split(",");
        List<String>  fileList= Arrays.asList(filePaths);
        try {
            recoverLogEntity=this.insertRecoverLog(recoverLogEntity,resultT);
            for(String filePath:fileList){
                log.info("gbase恢复文件{}",filePath);
                resultT.setSuccessMessage("开始恢复文件{}",filePath);
                this.toRecover(filePath,recoverLogEntity,resultT);
            }
        } catch (Exception e) {
            resultT.setErrorMessage(OwnException.get(e));
        }finally {
            this.updateRecoverLog(recoverLogEntity,startTime,resultT);
        }

    }

    public void toRecover(String filePath,RecoverLogEntity recoverLogEntity,ResultT<String> resultT){
        RecoverMetaVo recoverMetaVo=new RecoverMetaVo();
        String copyPath="";
        try {
            String unzipParent="";
            File file=new File(filePath);
            String fileName=file.getName().substring(0,file.getName().lastIndexOf("."));

            if(backupTempPath.endsWith("/")){
                unzipParent=backupTempPath+"recover/";
            }else {
                unzipParent=backupTempPath+"/recover/";
            }
            if(!new File(unzipParent).exists()){
                new File(unzipParent).mkdirs();
            }
            copyPath=unzipParent+file.getName();
            FileUtil.copyFile(filePath,copyPath,resultT);
            String unzipPath=unzipParent+fileName;
            recoverMetaVo.setUnzipPath(unzipPath);
            recoverMetaVo.setIndexPath(unzipPath+"/index.sql");
            ZipUtils.unZip(new File(filePath),unzipPath);
            log.info("恢复文件判断数据库");
            BusinessEnum businessEnum = BusinessEnum.match(recoverLogEntity.getDatabaseType(), null);
            BaseBusiness baseBusiness = businessEnum.getBaseBusiness();
            baseBusiness.recoverStructedData(recoverMetaVo,recoverLogEntity,resultT);
        } catch (Exception e) {
            resultT.setErrorMessage(OwnException.get(e));
        }finally {
            FileUtil.delFile(new File(copyPath),resultT);
            FileUtil.delFile(new File(recoverMetaVo.getUnzipPath()),resultT);
        }
    }

    public RecoverLogEntity insertRecoverLog(RecoverLogEntity recoverLogEntity, ResultT<String> resultT){
        try {

            recoverLogEntity.setHandleCode("0");
            recoverLogEntity.setTriggerCode(1);
            recoverLogEntity.setHandleTime(new Date());
            recoverLogEntity=recoverLogService.saveNotNull(recoverLogEntity);
            return recoverLogEntity;
        }  catch (Exception e) {
            resultT.setErrorMessage("插入日志失败:{}", OwnException.get(e));
            log.error("插入日志失败:{}", OwnException.get(e));
        }
        return null;


    }

    public void updateRecoverLog(RecoverLogEntity recoverLogEntity, long startTime, ResultT<String> resultT){
        try {
            recoverLogEntity.setHandleCode("1");
            recoverLogEntity.setElapsedTime((System.currentTimeMillis()-startTime)/1000);
            if(!resultT.isSuccess()){
                recoverLogEntity.setHandleCode("2");
            }
            recoverLogEntity.setHandleMsg(resultT.getProcessMsg().toString());
            recoverLogService.saveNotNull(recoverLogEntity);
        } catch (Exception e){
            resultT.setErrorMessage("修改日志出错{}",OwnException.get(e));
            log.error("修改日志出错{}",OwnException.get(e));
        }

    }

    public List<Map<String,Object>> md5Check(List<String> paths){
        List<Map<String,Object>> mapList=new ArrayList<>();
        paths.forEach(path->{
            Map<String,Object> map=new HashMap<>();
            ResultT<String> resultT=new ResultT<>();
            String md5= Md5Utils.getFileMD5String(path,resultT);
            map.put("path",path);
            if(StringUtils.isNotNullString(md5)&&path.indexOf(md5)!=-1){
                map.put("isUpdate",true);
            }else{
                map.put("isUpdate",false);
            }
            mapList.add(map);

        });
        return mapList;
    }
}

