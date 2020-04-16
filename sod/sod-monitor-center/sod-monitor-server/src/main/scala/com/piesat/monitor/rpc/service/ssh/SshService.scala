package com.piesat.monitor.rpc.service.ssh

import java.util.{Date, List => JavaList}

import com.github.pagehelper.PageInfo
import com.piesat.common.utils.IdUtils
import com.piesat.monitor.dao.es.ssh.SshMapper
import com.piesat.monitor.entity.ssh.SshEntity
import com.piesat.monitor.repository.ssh.SshDao
import com.piesat.util.page.{PageBean, PageForm}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.util.PageUtil


/**
  * Created by zzj on 2020/3/23.
  */
@Service
class SshService  @Autowired()(sshDao: SshDao, sshMapper:SshMapper)
{
  def findById(id:String):SshEntity={
    sshDao.findById(id).get()
  }
  def save(ssh:SshEntity):SshEntity={
    ssh.setId(
     IdUtils.simpleUUID
    )
    ssh.setCreateTime(new Date())
    sshDao.save(ssh)

  }
  def update(ssh:SshEntity):SshEntity={
    sshDao.save(ssh)
  }
  def list(ssh:SshEntity): JavaList[SshEntity] = {
    var list =sshMapper.list(ssh)
    list
  }


  def page(pageForm: PageForm[SshEntity]): PageBean[SshEntity] = {
    PageUtil.startPage(pageForm.getCurrentPage, pageForm.getPageSize)
    var list = sshMapper.list(pageForm.getT)
    val pageInfo: PageInfo[SshEntity] = new PageInfo[SshEntity](list)
    val pageBean: PageBean[SshEntity] = new PageBean[SshEntity](pageInfo.getTotal, pageInfo.getPages, list)
    pageBean
  }
  def delete(id:String):Unit={
    sshDao.deleteById(id)
  }
  def selectAlias:JavaList[String]={
    sshMapper.selectAlias
  }

}
