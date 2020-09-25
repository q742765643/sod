package com.piesat.schedule.client.service.recover;

import com.alibaba.fastjson.JSON;
import com.piesat.common.utils.OwnException;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.client.business.BaseBusiness;
import com.piesat.schedule.client.enums.BusinessEnum;
import com.piesat.schedule.client.util.FileUtil;
import com.piesat.schedule.client.util.ZipUtils;
import com.piesat.schedule.client.util.fetl.type.Type;
import com.piesat.schedule.client.vo.MetadataVo;
import com.piesat.schedule.client.vo.RecoverMetaVo;
import com.piesat.schedule.entity.backup.MetaBackupEntity;
import com.piesat.schedule.entity.backup.MetaBackupLogEntity;
import com.piesat.schedule.entity.recover.MetaRecoverLogEntity;
import com.piesat.schedule.entity.recover.RecoverLogEntity;
import com.piesat.util.ResultT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.ResultSet;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-02 13:04
 **/
@Slf4j
@Service
public class DataBaseRecoverService {
    @Autowired
    private MetaRecoverLogService metaRecoverLogService;
    @Value("${backup.temp-path}")
    private String backupTempPath;

    public void recover(MetaRecoverLogEntity recoverLogEntity){
        ResultT<String> resultT=new ResultT();
        RecoverMetaVo recoverMetaVo=new RecoverMetaVo();
        recoverMetaVo.setStartTime(System.currentTimeMillis());
        String copyPath="";
        try {
            String unzipParent="";
            recoverLogEntity=this.insertMetaBackupLog(recoverLogEntity,resultT);
            if(!resultT.isSuccess()){
                return;
            }
            resultT.setSuccessMessage("开始解析备份内容");
            this.parsingMetadata(recoverMetaVo,recoverLogEntity,resultT);
            Map<Type, Set<String>> impInfo= this.toMap(recoverMetaVo,recoverLogEntity,resultT);
            File file=new File(recoverLogEntity.getStorageDirectory());
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
            FileUtil.copyFile(recoverLogEntity.getStorageDirectory(),copyPath,resultT);
            String unzipPath=unzipParent+fileName;
            recoverMetaVo.setUnzipPath(unzipPath);
            recoverMetaVo.setIndexPath(unzipPath+"/index.sql");
            resultT.setSuccessMessage("开始解压缩文件{}",recoverLogEntity.getStorageDirectory());
            ZipUtils.unZip(new File(recoverLogEntity.getStorageDirectory()),unzipPath);
            if(!resultT.isSuccess()){
                return;
            }
            BusinessEnum businessEnum = BusinessEnum.match(recoverLogEntity.getDatabaseType(), null);
            BaseBusiness baseBusiness = businessEnum.getBaseBusiness();
            baseBusiness.recoverMeta(recoverMetaVo,impInfo,recoverLogEntity,resultT);
        } catch (Exception e) {
            resultT.setErrorMessage(OwnException.get(e));
            log.error(OwnException.get(e));
        } finally {
            recoverMetaVo.setEndTime(System.currentTimeMillis());
            FileUtil.delFile(new File(copyPath),resultT);
            FileUtil.delFile(new File(recoverMetaVo.getUnzipPath()),resultT);
            this.updateMetaRecoverLogEntity(recoverLogEntity,recoverMetaVo,resultT);
        }

    }
    public void parsingMetadata(RecoverMetaVo recoverMetaVo, MetaRecoverLogEntity recoverLogEntity, ResultT<String> resultT){
        List<TreeVo> treeVos= JSON.parseArray(recoverLogEntity.getRecoverContent(),TreeVo.class);
        for(TreeVo treeVo:treeVos) {
            if (treeVo.isParent() && treeVo.getPId().indexOf("数据库") < 0) {
                continue;
            }
            if (treeVo.getPId().indexOf("数据库") != -1) {
                recoverMetaVo.getSchema().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("用户") != -1) {
                recoverMetaVo.getUsers().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("角色") != -1) {
                recoverMetaVo.getRoles().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("表") != -1) {
                recoverMetaVo.getTable().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("视图") != -1) {
                recoverMetaVo.getView().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("序列") != -1) {
                recoverMetaVo.getSequence().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("触发器") != -1) {
                recoverMetaVo.getTrigger().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("存储过程") != -1) {
                recoverMetaVo.getProcedure().add(treeVo.getId());
            }

        }
    }
    public  Map<Type, Set<String>>  toMap(RecoverMetaVo recoverMetaVo, MetaRecoverLogEntity recoverLogEntity, ResultT<String> resultT){
        Map<Type, Set<String>> impInfo = new HashMap<>();
        if(!recoverMetaVo.getSchema().isEmpty()){
            impInfo.put(Type.SCHEMA, recoverMetaVo.getSchema());
        }
        if(!recoverMetaVo.getUsers().isEmpty()){
            impInfo.put(Type.USER, recoverMetaVo.getUsers());
        }
        if(!recoverMetaVo.getRoles().isEmpty()){
            impInfo.put(Type.ROLE,recoverMetaVo.getRoles());
        }
        if(!recoverMetaVo.getView().isEmpty()){
            impInfo.put(Type.VIEW,recoverMetaVo.getView());
        }
        if(!recoverMetaVo.getProcedure().isEmpty()){
            impInfo.put(Type.PROCEDURE,recoverMetaVo.getProcedure());
        }
        if(!recoverMetaVo.getSequence().isEmpty()){
            impInfo.put(Type.SEQUENCE,recoverMetaVo.getSequence());
        }
        if(!recoverMetaVo.getTrigger().isEmpty()){
            impInfo.put(Type.TRIGGER,recoverMetaVo.getTrigger());
        }
        if(!recoverMetaVo.getTable().isEmpty()){
            if(recoverLogEntity.getIsStructure().indexOf("0")!=-1){
                impInfo.put(Type.TABLE,recoverMetaVo.getTable());
            }
            if(recoverLogEntity.getIsStructure().indexOf("1")!=-1){
                impInfo.put(Type.DATA,recoverMetaVo.getTable());
            }
        }
        return impInfo;
    }

    public MetaRecoverLogEntity insertMetaBackupLog(MetaRecoverLogEntity metaRecoverLogEntity, ResultT<String> resultT){
        try {

            metaRecoverLogEntity.setHandleCode("0");
            metaRecoverLogEntity.setTriggerCode(1);
            metaRecoverLogEntity.setHandleTime(new Date());
            metaRecoverLogEntity=metaRecoverLogService.saveNotNull(metaRecoverLogEntity);
            return metaRecoverLogEntity;
        }  catch (Exception e) {
            resultT.setErrorMessage("插入日志失败:{}", OwnException.get(e));
            log.error("插入日志失败:{}", OwnException.get(e));
        }
        return null;


    }

    public void updateMetaRecoverLogEntity(MetaRecoverLogEntity metaRecoverLogEntity, RecoverMetaVo recoverMetaVo, ResultT<String> resultT){
        try {
            metaRecoverLogEntity.setHandleCode("1");
            metaRecoverLogEntity.setElapsedTime((recoverMetaVo.getEndTime()-recoverMetaVo.getStartTime())/1000);
            if(!resultT.isSuccess()){
                metaRecoverLogEntity.setHandleCode("2");
            }
            metaRecoverLogEntity.setHandleMsg(resultT.getProcessMsg().toString());
            metaRecoverLogService.saveNotNull(metaRecoverLogEntity);
        } catch (Exception e){
            resultT.setErrorMessage("修改日志出错{}",OwnException.get(e));
            log.error("修改日志出错{}",OwnException.get(e));
        }

    }
    public  List<TreeVo> getFileList(String parentId,String storageDirectory){
        List<TreeVo> list=new ArrayList<>();

        String path = storageDirectory+"/"+parentId;
        TreeVo treeVo=new TreeVo();
        treeVo.setId(path);
        treeVo.setName(path);
        treeVo.setPId("");
        treeVo.setParent(true);
        list.add(treeVo);
        try {
            this.getFileFistChidren(path,list);
        } catch (Exception e) {
            log.error(OwnException.get(e));
        }
        return list;

    }
    public  List<TreeVo> getDataFileList(RecoverLogEntity recoverLogEntity){
        List<TreeVo> list=new ArrayList<>();
        String path = recoverLogEntity.getStorageDirectory()+"/"+recoverLogEntity.getParentId()+"/"+recoverLogEntity.getDataClassId();
        TreeVo treeVo=new TreeVo();
        treeVo.setId(path);
        treeVo.setName(path);
        treeVo.setPId("");
        treeVo.setParent(true);
        list.add(treeVo);
        try {
            this.getFileFistChidren(path,list);
        } catch (Exception e) {
            log.error(OwnException.get(e));
        }
        return list;

    }
    public  void getFileFistChidren(String childrenPath,List<TreeVo> list){
        File childrenFile=new File(childrenPath);
        File [] fileList= childrenFile.listFiles();
        if(fileList.length==0){
            return ;
        }
        List<File> children = Arrays.asList(fileList);
        Collections.sort(children, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile()){
                    return -1;
                }
                if (o1.isFile() && o2.isDirectory()){
                    return 1;
                }
                return o1.getName().compareTo(o2.getName());
            }
        });
        /*Arrays.sort(children, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0){
                    return -1;
                }

                else if (diff == 0){
                    return 0;
                }

                else {
                    return 1;
                }
            }

            @Override
            public boolean equals(Object obj) {
                return true;
            }

        });*/

        int i=0;
        for(File file:children){
            if(file.isHidden()){
                continue;
            }
            TreeVo treeVo=new TreeVo();
            treeVo.setId(file.getPath().replace("\\","/"));
            treeVo.setName(file.getName());
            treeVo.setPId(childrenPath);
            if(file.isDirectory()){
                treeVo.setParent(true);
                list.add(treeVo);
                if(i==0){
                  this.getFileFistChidren(treeVo.getId(),list);
                }
                i++;
            }
            if(file.isFile()){
                treeVo.setParent(false);
                list.add(treeVo);
            }

        }

    }

    public List<TreeVo> getFileChidren(String childrenPath){
        List<TreeVo> list=new ArrayList<>();
        try {
            File childrenFile=new File(childrenPath);
            File [] fileList = childrenFile.listFiles();
            if(fileList.length==0){
                return list;
            }
            List<File> children = Arrays.asList(fileList);
            Collections.sort(children, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile()){
                        return -1;
                    }
                    if (o1.isFile() && o2.isDirectory()){
                        return 1;
                    }
                    return o1.getName().compareTo(o2.getName());
                }
            });
            for(File file:children){
                if(file.isHidden()){
                    continue;
                }
                TreeVo treeVo=new TreeVo();
                treeVo.setId(file.getPath().replace("\\","/"));
                treeVo.setName(file.getName());
                treeVo.setPId(childrenPath);
                if(file.isDirectory()){
                    treeVo.setParent(true);
                }
                if(file.isFile()){
                    treeVo.setParent(false);
                }
                list.add(treeVo);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }





    public  Map<String,Object> parsingPath(String path){
        Map<String,Object> map=new HashMap<>();
        StringBuilder stringBuilder=new StringBuilder();
        StringBuilder structure=new StringBuilder();
        ZipFile zipFile=null;
        InputStream in=null;
        FileInputStream fileInputStream=null;
        ZipInputStream zin=null;
        try {
            zipFile=new ZipFile(path);
            Enumeration zipEnum = zipFile.entries();
            fileInputStream=new FileInputStream(path);
            in = new BufferedInputStream(fileInputStream);
            zin = new ZipInputStream(in);
            while(zipEnum.hasMoreElements ()) {
                ZipEntry ze = (ZipEntry) zipEnum.nextElement();
                if(ze.getName().indexOf("tree.txt")!=-1){
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(zipFile.getInputStream(ze)));
                    String str;
                    while ((str = br.readLine()) != null) {
                        stringBuilder.append(str);
                    }
                    br.close();

                }
                if(ze.getName().indexOf("structure.txt")!=-1){
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(zipFile.getInputStream(ze)));
                    String str;
                    while ((str = br.readLine()) != null) {
                        structure.append(str);
                    }
                    br.close();
                }

            }
            List<TreeVo> treeVos=JSON.parseArray(stringBuilder.toString(),TreeVo.class);
            map.put("tree",treeVos);
            map.put("isStructure",structure.toString());
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null!=fileInputStream){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                if(in!=null){
                    in.close();
                }
                if(zin!=null){
                    zin.closeEntry();
                    zin.close();
                }
                if(zipFile!=null){
                    zipFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return map;
    }
}

