package com.piesat.monitor.rpc.service.flow

import java.util.{Date, List => JavaList}

import com.github.pagehelper.PageInfo
import com.piesat.common.utils.IdUtils
import com.piesat.monitor.dao.es.flow.StationLevelMapper
import com.piesat.monitor.entity.flow.StationLevel
import com.piesat.util.page.{PageBean, PageForm}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.util.PageUtil


/**
  * Created by zzj on 2020/3/23.
  */
@Service
class StationLevelService  @Autowired()(stationLevelMapper:StationLevelMapper)
{
  def findById(id:String):StationLevel={
    stationLevelMapper.findById(id)
  }

  def list(stationLevel:StationLevel): JavaList[StationLevel] = {
    var list = stationLevelMapper.list(stationLevel)
    list
  }

  def page(pageForm: PageForm[StationLevel]): PageBean[StationLevel] = {
    PageUtil.startPage(pageForm.getCurrentPage, pageForm.getPageSize)
    var list = stationLevelMapper.list(pageForm.getT)
    val pageInfo: PageInfo[StationLevel] = new PageInfo[StationLevel](list)
    val pageBean: PageBean[StationLevel] = new PageBean[StationLevel](pageInfo.getTotal, pageInfo.getPages, list)
    pageBean
  }

}
