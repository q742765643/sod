package com.piesat.monitor.util

import java.text.SimpleDateFormat
import java.util.Date

import org.elasticsearch.search.DocValueFormat.DateTime

/**
  * Created by zzj on 2020/3/26.
  */
object IndexUtil {
  def getDateStr: String ={
    val now: Date = new Date()
    val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd")
    val date = dateFormat.format(now)
    date
  }

}
