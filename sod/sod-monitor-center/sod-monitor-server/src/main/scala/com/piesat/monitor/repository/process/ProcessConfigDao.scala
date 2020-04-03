package com.piesat.monitor.repository.process

import com.piesat.monitor.entity.process.ProcessConfig
import com.piesat.monitor.entity.system.ProcessEntity
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
  * Created by zzj on 2020/4/1.
  */
trait ProcessConfigDao extends ElasticsearchRepository[ProcessConfig,String]{

}
