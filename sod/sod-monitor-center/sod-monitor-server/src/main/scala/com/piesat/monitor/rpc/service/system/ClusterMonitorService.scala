package com.piesat.monitor.rpc.service.system

import java.util
import java.util.function.Function
import java.util.{Date, List => JavaList}

import com.piesat.monitor.dao.es.system.{ClusterMonitorMapper, CpuMapper}
import com.piesat.monitor.entity.ssh.SshEntity
import com.piesat.monitor.entity.system.{ClusterMonitor, CpuEntity}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.stream.{Collectors, Stream}

import scala.collection.JavaConversions._
import com.piesat.monitor.dao.es.ssh.SshMapper
import com.piesat.monitor.vo.cluster.{CLusterMaster, CluserNode}
import org.util.DateUtil

/**
  * Created by zzj on 2020/3/24.
  */
@Service
class ClusterMonitorService @Autowired()(clusterMonitorMapper: ClusterMonitorMapper,
                                         sshMapper: SshMapper,
                                         processService:ProcessService){

  def list(sshEntity: SshEntity):CLusterMaster={
          var list:JavaList[SshEntity]=sshMapper.list(sshEntity)
          if(list==null||list.isEmpty){
            return null
          }
          val ff = new Function[SshEntity, String]() {
            override def apply(sshEntity: SshEntity): String = sshEntity.getIp
          }
          val ips = list.stream.map[String](ff).collect(Collectors.toList[String])
          var clusterMonitor=new ClusterMonitor
          clusterMonitor.setIps(ips)
          var map:util.Map[String,Date]=DateUtil.getStartAndEnd()
          clusterMonitor.startTime=map.get("startTime")
          clusterMonitor.endTime=map.get("endTime")
          val  clusterMonitors=clusterMonitorMapper.list(clusterMonitor)
          var clusterNodes:JavaList[CluserNode]=new util.ArrayList[CluserNode]()
          var masterNomalNum:Int=0
          var masterDiskNomalNum:Int=0

           var masterDiskPct:Float=0
          for(ssh:SshEntity<-list){
              var cluserNode:CluserNode=new CluserNode
              cluserNode.ip=ssh.ip
              for(clu:ClusterMonitor<-clusterMonitors){
                  if(clu.ip.equals(ssh.ip)){
                    cluserNode.cpuPct=clu.totalCpuPct
                    cluserNode.memoryPct=clu.usedPct
                    cluserNode.diskPct=clu.filePct
                    cluserNode.diskIsNormal=1
                    masterDiskNomalNum+=1
                    masterDiskPct+= cluserNode.diskPct
                    cluserNode.netWorkIsNormal=1
                    var flag=processService.checkProcess(ssh.ip)
                    if(flag){
                      cluserNode.processIsNormal=1
                    }

                  }
              }
              if((cluserNode.processIsNormal+cluserNode.diskIsNormal+cluserNode.netWorkIsNormal)==3){
                cluserNode.setIsNormal(1)
                masterNomalNum+=1
              }
              clusterNodes.add(cluserNode)
          }
          var cLusterMaster:CLusterMaster=new CLusterMaster
           cLusterMaster.setCluserNode(clusterNodes)
           cLusterMaster.setNormalNum(masterNomalNum)
           cLusterMaster.setAbnormalNum(list.size()-masterNomalNum)
           if(masterDiskNomalNum>0){
             cLusterMaster.setDiskPct(masterDiskPct/masterDiskNomalNum)
           }
           cLusterMaster
  }

}
