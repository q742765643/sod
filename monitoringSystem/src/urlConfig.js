const adress = process.env.VUE_APP_BASE_API;
const interfaceObj = {
  cpuUsage: adress + '/system/cpuUsage', //查询时间范围内cpu使用率
  fileUsage: adress + '/system/fileUsage', //查询磁盘使用信息
  groupbyTaskDuty: adress + '/diTaskExecute/groupbyTaskDuty', //任务执行情况
  clusterMonitor: adress + '/system/clusterMonitor', //查询集群各节点状态
  kafkaMonitorLag: adress + '/kafkaMonitor/lag', //查询kafka积压情况
  kafkaAlias: adress + '/kafkaConfig/selectAlias', //查询kafka别名
  rabbitAlias: adress + '/rabbitMqConfig/selectAlias', //查询rabbit别名
  rabbitMonitorLag: adress + '/rabbitMqMonitor/lag', //查询rabbit积压情况
  flowMonitorList: adress + '/flowMonitor/list', //查询数据全流程监控列表
  memoryUsage: adress + '/system/memoryUsage', //查询内存使用率
  netWorkUsage: adress + '/system/netWorkUsage', //查询网络流量
  processUsage: adress + '/system/processUsage' //查询进程信息
};
export {
  interfaceObj
};