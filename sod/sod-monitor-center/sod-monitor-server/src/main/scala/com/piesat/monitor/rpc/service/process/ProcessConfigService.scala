package com.piesat.monitor.rpc.service.process

import java.util.Date

import com.github.pagehelper.PageInfo
import com.piesat.common.utils.IdUtils
import com.piesat.monitor.dao.es.processConfig.ProcessConfigMapper
import com.piesat.monitor.dao.es.ssh.SshMapper
import com.piesat.monitor.entity.process.ProcessConfig
import com.piesat.monitor.repository.process.ProcessConfigDao
import com.piesat.monitor.repository.ssh.SshDao
import com.piesat.util.page.{PageBean, PageForm}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.util.PageUtil
import java.util.{List => JavaList}


/**
  * Created by zzj on 2020/3/23.
  */
@Service
class ProcessConfigService  @Autowired()(processConfigDao: ProcessConfigDao, processConfigMapper:ProcessConfigMapper)
{
  def findById(id:String):ProcessConfig={
    processConfigDao.findById(id).get()
  }
  def save(processConfig:ProcessConfig):ProcessConfig={
    processConfig.setId(
     IdUtils.simpleUUID
    )
    processConfig.setCreateTime(new Date())
    processConfigDao.save(processConfig)

  }
  def update(processConfig:ProcessConfig):ProcessConfig={
    processConfigDao.save(processConfig)
  }
  def list(processConfig:ProcessConfig): JavaList[ProcessConfig] = {
    var list = processConfigMapper.list(processConfig)
    list
  }

  def page(pageForm: PageForm[ProcessConfig]): PageBean[ProcessConfig] = {
    PageUtil.startPage(pageForm.getCurrentPage, pageForm.getPageSize)
    var list = processConfigMapper.list(pageForm.getT)
    val pageInfo: PageInfo[ProcessConfig] = new PageInfo[ProcessConfig](list)
    val pageBean: PageBean[ProcessConfig] = new PageBean[ProcessConfig](pageInfo.getTotal, pageInfo.getPages, list)
    pageBean
  }
  def delete(id:String):Unit={
    processConfigDao.deleteById(id)
  }
}
