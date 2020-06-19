import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SCHEDULE_CENTER_API


// 数据库ip
export function findDataBase() {
  return request({
    url: baseUrl + '/schedule/metaClear/findDataBase',
    method: 'get',
  })
}
/* 元数据清除配置 */
// 查询列表
export function metaClearList(query) {
  return request({
    url: baseUrl + '/schedule/metaClear/list',
    method: 'get',
    params: query
  })
}
// 删除列表
export function metaClearDel(id) {
  return request({
    url: baseUrl + '/schedule/metaClear/' + id,
    method: 'DELETE',
  })
}

// 查询数据库元数据
export function findMeta(query) {
  return request({
    url: baseUrl + '/schedule/metaClear/findMeta',
    method: 'get',
    params: query
  })
}
// 添加

export function addMetaClear(data) {
  return request({
    url: baseUrl + '/schedule/metaClear',
    method: 'post',
    data: data
  })
}

// 修改
export function editMetaBackup(data) {
  return request({
    url: baseUrl + '/schedule/metaClear',
    method: 'put',
    data: data
  })
}

// 根据ID查询元数据清除任务
export function getDetailByID(id) {
  return request({
    url: baseUrl + '/schedule/metaClear/' + id,
    method: 'get',
  })
}
/* 元数据清除日志 */
// 查询列表
export function listLog(query) {
  return request({
    url: baseUrl + '/schedule/metaClearLog/list',
    method: 'get',
    params: query
  })
}

// 根据ID查询元数据清除任务日志
export function listLogDatail(id) {
  return request({
    url: baseUrl + '/schedule/metaClearLog/' + id,
    method: 'get',
  })
}

// 删除

export function deleteLogById(id) {
  return request({
    url: baseUrl + '/schedule/metaClearLog/' + id,
    method: 'delete',
  })
}
// 下载

export function metaClearLogExport(query) {
  return request({
    url: baseUrl + '/schedule/metaClearLog/export',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}
// 导出
export function metaClearExport(query) {
  return request({
    url: baseUrl + '/schedule/metaClear/export',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}
