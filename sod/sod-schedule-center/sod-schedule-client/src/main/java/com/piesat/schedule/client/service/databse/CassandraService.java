package com.piesat.schedule.client.service.databse;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.TableMetadata;
import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.client.datasource.DynamicDataSource;
import com.piesat.schedule.client.vo.CassandraConVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-25 10:34
 **/
@Service
public class CassandraService {
    public List<TreeVo> findMeta(String parenId){
        List<TreeVo> treeVos=new ArrayList<>();
        DynamicDataSource dynamicDataSource= SpringUtil.getBean(DynamicDataSource.class);
        dynamicDataSource.selectDataSource(parenId);
        CassandraConVo cassandraConVo= (CassandraConVo) DynamicDataSource._targetDataSources.get(parenId);
        Cluster cluster =
                Cluster.builder().addContactPoint(cassandraConVo.getIp())
                        .withPort(cassandraConVo.getPort())
                        .withCredentials(cassandraConVo.getUserName(),cassandraConVo.getPassWord())
                        .build();

        try {
            Metadata metadata = cluster.getMetadata();
            TreeVo pInstanceTreeVo=new TreeVo("","数据库","数据库",true);
            treeVos.add(pInstanceTreeVo);
            List<KeyspaceMetadata> keyspaceMetadatas=metadata.getKeyspaces();
            if(!keyspaceMetadatas.isEmpty()){
                for(KeyspaceMetadata keyspaceMetadata:keyspaceMetadatas){
                    TreeVo treeInstance=new TreeVo();
                    treeInstance.setId(keyspaceMetadata.getName());
                    treeInstance.setPId("数据库");
                    treeInstance.setName(keyspaceMetadata.getName());
                    treeVos.add(treeInstance);
                    this.getInstanceMeta(keyspaceMetadata,treeVos);
                }

            }
        } finally {
            cluster.close();
        }
        return treeVos;
    }
    public void getInstanceMeta( KeyspaceMetadata keyspaceMetadata,List<TreeVo> treeVos){
        Collection<TableMetadata> tables= keyspaceMetadata.getTables();
        String instance=keyspaceMetadata.getName();
        TreeVo pTableTreeVo=new TreeVo(instance,"表"+instance,"表",true);
        treeVos.add(pTableTreeVo);
        if(!tables.isEmpty()){
            for(TableMetadata table:tables){
                TreeVo treeTable=new TreeVo();
                treeTable.setId(instance+"."+table.getName());
                treeTable.setPId("表"+instance);
                treeTable.setName(instance+"."+table.getName());
                treeVos.add(treeTable);
            }
        }


    }
}

