import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SCHEDULE_CENTER_API

// 获取资料名称
export function getByDatabaseId(id) {
  return request({
    url: baseUrl + '/schedule/job/getByDatabaseId/' + id,
    method: 'get',
  })
}
// 查询结构化数据恢复文件树
export function getDataFileList(query) {
  return request({
    url: baseUrl + '/recoverLog/getDataFileList',
    method: 'get',
    params: query
  })
}

// 查询结构化数据恢复文件树子节点
export function getFileChidren(query) {
  return request({
    url: baseUrl + '/recoverLog/getFileChidren',
    method: 'get',
    params: query
  })
}
// md5校验
export function md5Check(data) {
  return request({
    url: baseUrl + '/recoverLog/md5Check',
    method: 'post',
    data: data
  })
}
// 执行结构化数据恢复
export function recoverStructedData(data) {
  return request({
    url: baseUrl + '/recoverLog/recoverStructedData',
    method: 'post',
    data: data
  })
}

/* 日志 */
// 列表
export function recoverLogList(query) {
  return request({
    url: baseUrl + '/recoverLog/list',
    method: 'get',
    params: query
  })
}
// 删除
export function deleteLogById(id) {
  return request({
    url: baseUrl + '/recoverLog/' + id,
    method: 'delete',
  })
}
// 详情
export function detailLogById(id) {
  return request({
    url: baseUrl + '/recoverLog/' + id,
    method: 'get',
  })
}
