package com.piesat.monitor.rpc.service.flow

import java.util.{List => JavaList}

import com.github.pagehelper.PageInfo
import com.piesat.monitor.dao.es.flow.FlowMonitorMapper
import com.piesat.monitor.entity.flow.FlowMonitor
import com.piesat.util.page.{PageBean, PageForm}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.util.PageUtil


/**
  * Created by zzj on 2020/3/23.
  */
@Service
class FlowMonitorService  @Autowired()(flowMonitorMapper:FlowMonitorMapper)
{
  def findById(id:String):FlowMonitor={
    flowMonitorMapper.findById(id)
  }

  def list(flowMonitor:FlowMonitor): JavaList[FlowMonitor] = {
    var list = flowMonitorMapper.list(flowMonitor)
    list
  }

  def page(pageForm: PageForm[FlowMonitor]): PageBean[FlowMonitor] = {
    PageUtil.startPage(pageForm.getCurrentPage, pageForm.getPageSize)
    var list = flowMonitorMapper.list(pageForm.getT)
    val pageInfo: PageInfo[FlowMonitor] = new PageInfo[FlowMonitor](list)
    val pageBean: PageBean[FlowMonitor] = new PageBean[FlowMonitor](pageInfo.getTotal, pageInfo.getPages, list)
    pageBean
  }

}
