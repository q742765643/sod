import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SCHEDULE_CENTER_API


// 数据库ip
export function findDataBase() {
  return request({
    url: baseUrl + '/schedule/metaBackup/findDataBase',
    method: 'get',
  })
}
/* 元数据备份配置 */
// 查询列表
export function metaBackupList(query) {
  return request({
    url: baseUrl + '/schedule/metaBackup/list',
    method: 'get',
    params: query
  })
}
// 删除列表
export function metaBackupDel(metaBackupIds) {
  return request({
    url: baseUrl + '/schedule/metaBackup/' + metaBackupIds,
    method: 'DELETE',
  })
}
// 启动
export function startTask(id) {
  return request({
    url: baseUrl + '/schedule/job/startById',
    method: 'get',
    params: {
      "id": id
    }
  })
}
// /停止
export function stopTask(id) {
  return request({
    url: baseUrl + '/schedule/job/stop',
    method: 'get',
    params: {
      "id": id
    }
  })
}


/* 元数据备份监控 */
// 查询列表
export function listLog(query) {
  return request({
    url: baseUrl + '/schedule/metaBackupLog/list',
    method: 'get',
    params: query
  })
}
