package com.piesat.schedule.client.service.databse;

import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.mapper.database.XuguOperationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-24 14:27
 **/
@Service
public class XuguService {
    @Autowired
    private XuguOperationMapper xuguOperationMapper;

    public List<TreeVo> findMeta(){
        List<TreeVo> treeVos=new ArrayList<>();
        TreeVo pUserTreeVo=new TreeVo("","用户","用户",true);
        treeVos.add(pUserTreeVo);
        List<String> users=xuguOperationMapper.findXuguUsers();
        if(!users.isEmpty()){
            for(String user:users){
                TreeVo treeUser=new TreeVo();
                treeUser.setId(user);
                treeUser.setPId("用户");
                treeUser.setName(user);
                treeVos.add(treeUser);
            }
        }

        TreeVo pRoleTreeVo=new TreeVo("","角色","角色",true);
        treeVos.add(pRoleTreeVo);
        List<String> roles=xuguOperationMapper.findXuguRoles();
        if(!roles.isEmpty()){
            for(String role:roles){
                TreeVo treeRole=new TreeVo();
                treeRole.setId(role);
                treeRole.setPId("角色");
                treeRole.setName(role);
                treeVos.add(treeRole);
            }
        }
        TreeVo pInstanceTreeVo=new TreeVo("","数据库","数据库",true);
        treeVos.add(pInstanceTreeVo);
        List<String> instances=xuguOperationMapper.findXuguInstance();
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
        List<String> tables=xuguOperationMapper.findXuguTables(instance);
        if(!tables.isEmpty()){
           for(String table:tables){
               TreeVo treeTable=new TreeVo();
               treeTable.setId(instance+"."+table);
               treeTable.setPId("表"+instance);
               treeTable.setName(instance+"."+table);
               treeVos.add(treeTable);
           }
        }
        TreeVo pViewTreeVo=new TreeVo(instance,"视图"+instance,"视图",true);
        treeVos.add(pViewTreeVo);
        List<String> views=xuguOperationMapper.findXuguViews(instance);
        if(!views.isEmpty()){
            for(String view:views){
                TreeVo treeView=new TreeVo();
                treeView.setId(instance+"."+view);
                treeView.setPId("视图"+instance);
                treeView.setName(instance+"."+view);
                treeVos.add(treeView);
            }
        }
        TreeVo pSeqTreeVo=new TreeVo(instance,"序列"+instance,"序列",true);
        treeVos.add(pSeqTreeVo);
        List<String> seqs=xuguOperationMapper.findXuguSeqs(instance);
        if(!seqs.isEmpty()){
            for(String seq:seqs){
                TreeVo treeSeq=new TreeVo();
                treeSeq.setId(instance+"."+seq);
                treeSeq.setPId("序列"+instance);
                treeSeq.setName(instance+"."+seq);
                treeVos.add(treeSeq);
            }
        }

        TreeVo pTrigTreeVo=new TreeVo(instance,"触发器"+instance,"触发器",true);
        treeVos.add(pTrigTreeVo);
        List<String> trigs=xuguOperationMapper.findXuguTrigs(instance);
        if(!trigs.isEmpty()){
            for(String trig:trigs){
                TreeVo treeTrig=new TreeVo();
                treeTrig.setId(instance+"."+trig);
                treeTrig.setPId("触发器"+instance);
                treeTrig.setName(instance+"."+trig);
                treeVos.add(treeTrig);
            }
        }
        TreeVo pProcTreeVo=new TreeVo(instance,"存储过程"+instance,"存储过程",true);
        treeVos.add(pProcTreeVo);
        List<String> procs=xuguOperationMapper.findXuguProcs(instance);
        if(!procs.isEmpty()){
            for(String proc:procs){
                TreeVo treeProc=new TreeVo();
                treeProc.setId(instance+"."+proc);
                treeProc.setPId("存储过程"+instance);
                treeProc.setName(instance+"."+proc);
                treeVos.add(treeProc);
            }
        }


    }
}

