package com.piesat.monitor.rpc.service.flow

import java.util.{Date, List => JavaList}

import com.github.pagehelper.PageInfo
import com.piesat.common.utils.IdUtils
import com.piesat.monitor.dao.es.flow.PlayBillMapper
import com.piesat.monitor.entity.flow.PlayBill
import com.piesat.monitor.repository.flow.PlayBillDao
import com.piesat.util.page.{PageBean, PageForm}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.util.PageUtil


/**
  * Created by zzj on 2020/3/23.
  */
@Service
class PlayBillService  @Autowired()(playBillDao: PlayBillDao, playBillMapper:PlayBillMapper)
{
  def findById(id:String):PlayBill={
    playBillDao.findById(id).get()
  }
  def save(playBill:PlayBill):PlayBill={
    playBill.setId(
     IdUtils.simpleUUID
    )
    playBill.setCreateTime(new Date())
    playBillDao.save(playBill)

  }
  def update(playBill:PlayBill):PlayBill={
    playBillDao.save(playBill)
  }
  def list(playBill:PlayBill): JavaList[PlayBill] = {
    var list = playBillMapper.list(playBill)
    list
  }

  def page(pageForm: PageForm[PlayBill]): PageBean[PlayBill] = {
    PageUtil.startPage(pageForm.getCurrentPage, pageForm.getPageSize)
    var list = playBillMapper.list(pageForm.getT)
    val pageInfo: PageInfo[PlayBill] = new PageInfo[PlayBill](list)
    val pageBean: PageBean[PlayBill] = new PageBean[PlayBill](pageInfo.getTotal, pageInfo.getPages, list)
    pageBean
  }
  def delete(id:String):Unit={
    playBillDao.deleteById(id)
  }
}
