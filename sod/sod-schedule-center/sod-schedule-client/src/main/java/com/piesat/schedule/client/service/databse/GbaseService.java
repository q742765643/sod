package com.piesat.schedule.client.service.databse;

import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.mapper.database.GbaseOperationMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-24 17:21
 **/
@Service
public class GbaseService {
    private GbaseOperationMapper gbaseOperationMapper;
    public List<TreeVo> findMeta(){
        List<TreeVo> treeVos=new ArrayList<>();
        TreeVo pUserTreeVo=new TreeVo("","用户","用户",true);
        treeVos.add(pUserTreeVo);
        List<Map<String,Object>> users=gbaseOperationMapper.findGbaseUsers();
        if(!users.isEmpty()){
            for(Map<String,Object> user:users){
                TreeVo treeUser=new TreeVo();
                treeUser.setId(user.get("USER")+"--"+user.get("UUID"));
                treeUser.setPId("用户");
                treeUser.setName(user.get("USER")+"--"+user.get("UUID"));
                treeVos.add(treeUser);
            }
        }

        TreeVo pInstanceTreeVo=new TreeVo("","数据库","数据库",true);
        treeVos.add(pInstanceTreeVo);
        List<String> instances=gbaseOperationMapper.findGbaseInstance();
        if(!instances.isEmpty()){
            for(String instance:instances){
                TreeVo treeInstance=new TreeVo();
                treeInstance.setId(instance);
                treeInstance.setPId("数据库");
                treeInstance.setName(instance);
                treeVos.add(treeInstance);
                this.getInstanceMeta(instance,treeVos);
            }
        }

        return treeVos;
    }

    public void getInstanceMeta(String instance,List<TreeVo> treeVos){
        TreeVo pTableTreeVo=new TreeVo(instance,"表"+instance,"表",true);
        treeVos.add(pTableTreeVo);
        List<String> tables=gbaseOperationMapper.findGbaseTables(instance);
        if(!tables.isEmpty()){
            for(String table:tables){
                TreeVo treeTable=new TreeVo();
                treeTable.setId(instance+"."+table);
                treeTable.setPId("表"+instance);
                treeTable.setName(instance+"."+table);
                treeVos.add(treeTable);
            }
        }


    }
}

