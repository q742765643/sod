import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SCHEDULE_CENTER_API


// 数据库ip
export function findDataBase() {
  return request({
    url: baseUrl + '/schedule/metaBackup/findDataBase',
    method: 'get',
  })
}
/* 元数据恢复 */
// 查询树列表
export function getFileList(data) {
  return request({
    url: baseUrl + '/metaRecoverLog/getFileList',
    method: 'post',
    params: data
  })
}
// 查询树节点
export function getFileChidren(data) {
  return request({
    url: baseUrl + '/metaRecoverLog/getFileChidren',
    method: 'post',
    params: data
  })
}
// 执行
export function parsingPath(data) {
  return request({
    url: baseUrl + '/metaRecoverLog/parsingPath',
    method: 'post',
    params: data
  })
}
// 恢复
export function recover(data) {
  return request({
    url: baseUrl + '/metaRecoverLog/recover',
    method: 'post',
    data: data
  })
}
/* 元数据恢复日志 */
// list
export function listLog(data) {
  return request({
    url: baseUrl + '/metaRecoverLog/list',
    method: 'get',
    params: data
  })
}

// 查看详情
export function logDetail(id) {
  return request({
    url: baseUrl + '/metaRecoverLog/' + id,
    method: 'get',
  })
}

//删除
export function deleteLogById(id) {
  return request({
    url: baseUrl + '/metaRecoverLog/' + id,
    method: 'delete',
  })
}
