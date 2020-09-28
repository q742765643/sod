package com.piesat.schedule.client.service.databse;

import com.alibaba.druid.pool.DruidDataSource;
import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.OwnException;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.client.datasource.DataSourceContextHolder;
import com.piesat.schedule.client.datasource.DynamicDataSource;
import com.piesat.schedule.client.util.CmdUtil;
import com.piesat.schedule.client.util.EiSendUtil;
import com.piesat.schedule.client.util.ZipUtils;
import com.piesat.schedule.client.util.fetl.type.Type;
import com.piesat.schedule.client.vo.ConnectVo;
import com.piesat.schedule.client.vo.MetadataVo;
import com.piesat.schedule.client.vo.RecoverMetaVo;
import com.piesat.schedule.entity.backup.MetaBackupEntity;
import com.piesat.schedule.entity.recover.MetaRecoverLogEntity;
import com.piesat.schedule.entity.recover.RecoverLogEntity;
import com.piesat.schedule.mapper.database.GbaseOperationMapper;
import com.piesat.util.ResultT;
import com.piesat.util.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-24 17:21
 **/
@Slf4j
@Service
public class GbaseService {
    @Value("${gbaseserver.ip}")
    private String serverIp;
    @Value("${gbaseserver.user}")
    private String serverUser;
    @Value("${gbaseserver.pass}")
    private String serverPass;
    @Autowired
    private GbaseOperationMapper gbaseOperationMapper;

    public List<TreeVo> findMeta() {
        List<TreeVo> treeVos = new ArrayList<>();
        TreeVo pUserTreeVo = new TreeVo("", "用户", "用户", true);
        treeVos.add(pUserTreeVo);
        List<Map<String, Object>> users = gbaseOperationMapper.findGbaseUsers();
        if (!users.isEmpty()) {
            for (Map<String, Object> user : users) {
                TreeVo treeUser = new TreeVo();
                treeUser.setId(String.valueOf(user.get("USER")).trim() + "--" + String.valueOf(user.get("UUID")).trim());
                treeUser.setPId("用户");
                treeUser.setName(String.valueOf(user.get("USER")).trim() + "--" + String.valueOf(user.get("UUID")).trim());
                treeVos.add(treeUser);
            }
        }

        TreeVo pInstanceTreeVo = new TreeVo("", "数据库", "数据库", true);
        treeVos.add(pInstanceTreeVo);
        List<String> instances = gbaseOperationMapper.findGbaseInstance();
        if (!instances.isEmpty()) {
            for (String instance : instances) {
                TreeVo treeInstance = new TreeVo();
                treeInstance.setId(instance);
                treeInstance.setPId("数据库");
                treeInstance.setName(instance);
                treeInstance.setParent(true);
                treeVos.add(treeInstance);
                this.getInstanceMeta(instance, treeVos);
            }
        }

        return treeVos;
    }

    public void getInstanceMeta(String instance, List<TreeVo> treeVos) {
        TreeVo pTableTreeVo = new TreeVo(instance, "表" + instance, "表", true);
        treeVos.add(pTableTreeVo);
        List<String> tables = gbaseOperationMapper.findGbaseTables(instance);
        if (!tables.isEmpty()) {
            for (String table : tables) {
                TreeVo treeTable = new TreeVo();
                treeTable.setId(instance + "." + table);
                treeTable.setPId("表" + instance);
                treeTable.setName(instance + "." + table);
                treeVos.add(treeTable);
            }
        }


    }


    public void metaBack(MetaBackupEntity metaBackupEntity, MetadataVo metadataVo, ResultT<String> resultT) {
        if (!metadataVo.getUsers().isEmpty()) {
            for (String user : metadataVo.getUsers()) {
                this.expUser(user, metadataVo, metaBackupEntity, resultT);
            }
        }
        if (!metadataVo.getTable().isEmpty()) {
            for (String table : metadataVo.getTable()) {
                if (metaBackupEntity.getIsStructure().indexOf("0") != -1) {
                    this.expTable(table, metadataVo, metaBackupEntity, resultT);
                }
                if (metadataVo.isExpData()) {
                    this.expData(table, metadataVo, metaBackupEntity, resultT);
                }
            }


        }
    }

    public void expUser(String userAndUuid, MetadataVo metadataVo, MetaBackupEntity metaBackupEntity, ResultT<String> resultT) {
        DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);
        ConnectVo connectVo=dynamicDataSource.getConnectVo(metaBackupEntity.getParentId());
        if (connectVo != null) {
            try {
                log.info("开始备份用户{}", userAndUuid);
                String user = userAndUuid.split("--")[0];
                String path = metadataVo.getParentPath() + "/USER_" + userAndUuid + ".sql";
                StringBuilder sql = new StringBuilder();
                sql.append("gccli -h ").append(connectVo.getIp())
                        .append(" -u").append(connectVo.getUserName()).append(" -p").append(connectVo.getPassWord())
                        .append(" -N -s ").append(" -e ").append("\"");
                sql.append("show grants for " + user + "").append("\"").append(">>" + path);
                String[] commands = new String[]{"/bin/sh", "-c", sql.toString()};
                int exit = CmdUtil.expCmd(commands, resultT);
                if (exit == 0) {
                    StringBuilder writePath = new StringBuilder();
                    writePath.append("---user ").append("USER_" + userAndUuid + ".sql").append("---\r\n");
                    writePath.append("USER_" + userAndUuid + ".sql").append("\r\n");
                    writePath.append("---end user---\r\n");
                    ZipUtils.writetxt(metadataVo.getIndexPath(), writePath.toString(), resultT);
                } else {
                    resultT.setErrorMessage("用户{}备份失败", userAndUuid);
                    log.error("用户{}备份失败", userAndUuid);

                }
            } catch (Exception e) {
                resultT.setErrorMessage("用户{}备份失败", userAndUuid);
                log.error(OwnException.get(e));
            }

        }
    }

    public void expTable(String tableInfo, MetadataVo metadataVo, MetaBackupEntity metaBackupEntity, ResultT<String> resultT) {
        DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);
        ConnectVo connectVo=dynamicDataSource.getConnectVo(metaBackupEntity.getParentId());

        if (connectVo != null) {
            try {
                log.info("开始备份表{}结构", tableInfo);
                String path = metadataVo.getParentPath() + "/TABLE_" + tableInfo + ".sql";
                String instance = tableInfo.split("\\.")[0];
                String tableName = tableInfo.split("\\.")[1];
                StringBuilder sql = new StringBuilder();
                sql.append("gcdump  -h ").append(connectVo.getIp())
                        .append(" -u").append(connectVo.getUserName()).append(" -p").append(connectVo.getPassWord())
                        .append(" " + instance + " ").append(tableName);
                sql.append(">>" + path + "");
                String[] commands = new String[]{"/bin/sh", "-c", sql.toString()};
                int exit = CmdUtil.expCmd(commands, resultT);
                if (exit == 0) {
                    StringBuilder write = new StringBuilder();
                    ZipUtils.readFile(path, write, resultT);
                    ZipUtils.writeFile(path, write, resultT);
                    StringBuilder writePath = new StringBuilder();
                    writePath.append("---table ").append(instance).append(".").append(tableName).append("---\r\n");
                    writePath.append("TABLE_" + tableInfo + ".sql").append("\r\n");
                    writePath.append("---end table---\r\n");
                    ZipUtils.writetxt(metadataVo.getIndexPath(), writePath.toString(), resultT);
                } else {
                    resultT.setErrorMessage("表结构{}备份失败", tableInfo);
                    log.error("表结构{}备份失败", tableInfo);
                }
            } catch (Exception e) {
                resultT.setErrorMessage("表结构{}备份失败", tableInfo);
                log.error(OwnException.get(e));
            }

        }
    }

    public void expData(String table, MetadataVo metadataVo, MetaBackupEntity metaBackupEntity, ResultT<String> resultT) {
        DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);
        ConnectVo connectVo=dynamicDataSource.getConnectVo(metaBackupEntity.getParentId());
        if (connectVo != null) {
            try {
                String path = metadataVo.getParentPath() + "/DATA_" + table + ".txt";
                StringBuilder sql = new StringBuilder();
                sql.append("gccli -h ").append(connectVo.getIp())
                        .append(" -u")
                        .append(connectVo.getUserName())
                        .append(" -p").append(connectVo.getPassWord())
                        .append(" -e ")
                        .append("\"").append("rmt:select * from ").append(table);
                        if(StringUtils.isNotNullString(metaBackupEntity.getConditions())){
                           sql.append(" where ").append(metaBackupEntity.getConditions());
                        }
                        sql.append(" INTO OUTFILE '").append(path).append("'")
                        .append(" WITH HEAD FIELDS TERMINATED BY ','")
                        .append("\"");
                String[] commands = new String[]{"/bin/sh", "-c", sql.toString()};
                int exit = CmdUtil.expCmd(commands, resultT);
                if (exit == 0) {
                    StringBuilder writePath = new StringBuilder();
                    writePath.append("---data " + table + "---").append("\r\n");
                    writePath.append("DATA_" + table + ".txt").append("\r\n");
                    writePath.append("---end data---").append("\r\n");
                    ZipUtils.writetxt(metadataVo.getIndexPath(), writePath.toString(), resultT);
                } else {
                    resultT.setErrorMessage("表数据{}备份失败", table);
                    log.error("表数据{}备份失败", table);
                }
            } catch (Exception e) {
                resultT.setErrorMessage("表数据{}备份失败", table);
                log.error(OwnException.get(e));
            }

        }
    }

    public void recoverMeta(RecoverMetaVo recoverMetaVo, Map<Type, Set<String>> impInfo, MetaRecoverLogEntity recoverLogEntity, ResultT<String> resultT) {
        Set<String> schemas=impInfo.get(Type.SCHEMA);
        Map<String, String> map = ZipUtils.readFile(recoverMetaVo.getIndexPath(), resultT);
        List<String> schemaList=gbaseOperationMapper.findGbaseInstance();
        if(null!=schemas&&!schemas.isEmpty()){
            for(String schema:schemas){
                if(!schemaList.contains(schema)){
                    this.recoverGbaseSchema(schema,resultT);
                }
            }
        }
        Set<String> users=impInfo.get(Type.USER);
        if(null!=users&&!users.isEmpty()){
            users.forEach(user->{
                String realPath = recoverMetaVo.getUnzipPath() + "/" + map.get("---user " + user + "---");
                this.recoverGbaseUser(user,realPath,resultT);
            });
        }
        Set<String> tables=impInfo.get(Type.TABLE);
        if(null!=tables&&!tables.isEmpty()){
            tables.forEach(table->{
                String realPath = recoverMetaVo.getUnzipPath() + "/" + map.get("---table " + table + "---");
                this.recoverGbaseTable(recoverLogEntity.getParentId(),table,realPath,resultT);
            });
        }
        Set<String> datas=impInfo.get(Type.DATA);
        if(null!=datas&&!datas.isEmpty()){
            for(String data:datas){
                String realPath = recoverMetaVo.getUnzipPath() + "/" + map.get("---data " + data + "---");
                this.recoverGbaseData(data,realPath,resultT);
            }
        }


    }
    public void recoverGbaseSchema(String instance,ResultT<String> resultT){
        try {
            gbaseOperationMapper.createGbaseSchema(instance);
        } catch (Exception e) {
            log.error(OwnException.get(e));
           resultT.setErrorMessage("数据库{}创建失败",instance);
        }

    }
    public void recoverGbaseUser(String user,String path, ResultT<String> resultT) {
        try (FileReader reader = new FileReader(path);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                if(!"".equals(line)){
                    gbaseOperationMapper.createGbaseUser(line);
                }

            }
        } catch (Exception e) {
            log.error(OwnException.get(e));
            resultT.setErrorMessage("用户{}创建失败",user);
        }

    }
    public void recoverGbaseTable(String parentId,String table,String path, ResultT<String> resultT) {
        DynamicDataSource dynamicDataSource= SpringUtil.getBean(DynamicDataSource.class);
        int exit=-1;
        try {
            ConnectVo connectVo=dynamicDataSource.getConnectVo(parentId);
            if(null==connectVo){
                log.error("{}数据为空",parentId);
                resultT.setErrorMessage("表{}结构恢复失败",table);
                return;
            }
            StringBuilder sql = new StringBuilder();
            sql.append("gccli -h ").append(connectVo.getIp())
                    .append(" -u").append(connectVo.getUserName()).append(" -p").append(connectVo.getPassWord())
                    .append(" -N -s ").append(" -f");
            sql.append("<" + path + "");
            String[] commands = new String[]{"/bin/sh", "-c", sql.toString()};
            exit=CmdUtil.expCmd(commands,resultT);
        } catch (Exception e) {
            resultT.setErrorMessage("表结构{}恢复失败",table);
            log.error(OwnException.get(e));
        }
        if(exit!=0){
            resultT.setErrorMessage("表结构{}恢复失败",table);
            log.error("表结构{}备份失败",table);
        }


    }
    public void recoverGbaseData( String tableName,String path, ResultT<String> resultT) {
        DynamicDataSource dynamicDataSource= SpringUtil.getBean(DynamicDataSource.class);
        Connection conn = null;
        try {
            conn= dynamicDataSource.getConnection();
            Statement statement = conn.createStatement();

            StringBuilder sql = new StringBuilder();
            sql.append("load data infile ");
            sql.append("'sftp://").append(serverUser).append(":").append(serverPass);
            sql.append("@").append(serverIp).append(path).append("'");
            sql.append(" into table ").append(tableName);
            sql.append(" NULL_VALUE '\\\\N' DATETIME FORMAT '%Y-%m-%d %H:%i:%s.%f'   FIELDS TERMINATED BY ',' ");
            log.info("开始执行gbase恢复:{}",sql);
            //gbaseOperationMapper.createGbaseUser(sql.toString());
            resultT.setSuccessMessage("恢复语句为:"+sql.toString());
            statement.execute(sql.toString());
            log.info("开始执行gbase结束:{}","=======================");
        } catch (Exception e) {
            resultT.setErrorMessage("表{}数据恢复失败",tableName);
           log.error(OwnException.get(e));
        }finally {
            if(null!=conn){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void recoverStructedData(RecoverMetaVo recoverMetaVo,RecoverLogEntity recoverLogEntity,ResultT<String> resultT){
        Map<String, String> map = ZipUtils.readFile(recoverMetaVo.getIndexPath(), resultT);

        try {
            DataSourceContextHolder.setDataSource(recoverLogEntity.getParentId());
            map.forEach((key, value) -> {

                if (key.indexOf("---data ") != -1) {
                    String data = key.replace("---data ", "").replace("---", "");
                    String realPath = recoverMetaVo.getUnzipPath() + "/" + value;
                    this.recoverGbaseData(data, realPath, resultT);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataSourceContextHolder.clearDataSource();
        }


    }

    public List<TreeVo> findAllTableByIp(){
        List<TreeVo> treeVos = new ArrayList<>();
        List<String> instances =gbaseOperationMapper.findGbaseInstance();
        if (!instances.isEmpty()) {
            for (String instance : instances) {
                TreeVo treeInstance = new TreeVo();
                treeInstance.setId(instance);
                treeInstance.setPId("");
                treeInstance.setName(instance);
                treeInstance.setParent(true);
                treeVos.add(treeInstance);
                List<String> tables = gbaseOperationMapper.findGbaseTables(instance);
                if (!tables.isEmpty()) {
                    for (String table : tables) {
                        TreeVo treeTable = new TreeVo();
                        treeTable.setId(instance + "." + table);
                        treeTable.setPId(instance);
                        treeTable.setName(instance + "." + table);
                        treeVos.add(treeTable);
                    }
                }
            }
        }
        return treeVos;
    }
}
