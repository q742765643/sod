package com.piesat.monitor.entity.flow

import lombok.Data
import org.springframework.data.elasticsearch.annotations.Document

/**
  * Created by zzj on 2020/3/26.
  */
@Data
@Document(indexName = "ssh",`type` = "ssh")
class PlayBill {

}
