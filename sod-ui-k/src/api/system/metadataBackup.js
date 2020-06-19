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
// 立即执行
export function execute(id) {
  return request({
    url: baseUrl + '/schedule/job/execute',
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
// 查询数据库元数据
export function findMeta(query) {
  return request({
    url: baseUrl + '/schedule/metaBackup/findMeta',
    method: 'get',
    params: query
  })
}
// 添加

export function addMetaBackup(data) {
  return request({
    url: baseUrl + '/schedule/metaBackup',
    method: 'post',
    data: data
  })
}

// 修改
export function editMetaBackup(data) {
  return request({
    url: baseUrl + '/schedule/metaBackup',
    method: 'put',
    data: data
  })
}
// 手工执行元数据备份
export function handExecute(query) {
  return request({
    url: baseUrl + '/schedule/metaBackup/handExecute',
    method: 'get',
    params: query
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

export function listLogDatail(id) {
  return request({
    url: baseUrl + '/schedule/metaBackupLog/' + id,
    method: 'get',
  })
}

// 删除

export function deleteLogById(id) {
  return request({
    url: baseUrl + '/schedule/metaBackupLog/' + id,
    method: 'delete',
  })
}
// 下载

export function downFile(query) {
  return request({
    url: baseUrl + '/api/schedule/uploadDown/downFile',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}

export function exportTableLog(query) {
  return request({
    url: baseUrl + '/schedule/metaBackupLog/export',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}

export function exportTable(query) {
  return request({
    url: baseUrl + '/schedule/metaBackup/export',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}
