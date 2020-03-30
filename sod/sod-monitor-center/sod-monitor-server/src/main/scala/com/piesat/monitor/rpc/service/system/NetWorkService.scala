package com.piesat.monitor.rpc.service.system

import java.util.{List => JavaList}

import com.piesat.monitor.dao.es.system.{CpuMapper, NetWorkMapper}
import com.piesat.monitor.entity.system.{CpuEntity, NetWorkEntity}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import scala.collection.JavaConversions._

/**
  * Created by zzj on 2020/3/24.
  */
@Service
class NetWorkService @Autowired()(netWorkMapper: NetWorkMapper){

  def list(netWorkEntity: NetWorkEntity):JavaList[NetWorkEntity]={
      netWorkEntity.setDataset("system.network")
      var netList=netWorkMapper.list(netWorkEntity)
      var i=0;
      for(ne:NetWorkEntity<- netList){
        if(i<netList.size()-1){
          var lastTime= netList.get(i+1).getTimestamp
          var lastInBytes=netList.get(i+1).getInBytes
          var lastOutBytes=netList.get(i+1).getOutBytes
          var lassumInBytes= netList.get(i+1).getSumInBytes
          var lassumOutBytes= netList.get(i+1).getSumOutBytes

          var inSpeed=(ne.getInBytes-lastInBytes)/(ne.getTimestamp.getTime/1000-lastTime.getTime/1000)
          ne.setInSpeed(inSpeed)
          var outSpeed=(ne.getOutBytes-lastOutBytes)/(ne.getTimestamp.getTime/1000-lastTime.getTime/1000)
          ne.setOutSpeed(outSpeed)
          var transferredIn=(ne.getSumInBytes-lassumInBytes)
          ne.setTransferredIn(transferredIn)
          var transferredOut=(ne.getSumOutBytes-lassumOutBytes)
          ne.setTransferredOut(transferredOut)
        }
        i+=1
      }
      netList
  }

}
