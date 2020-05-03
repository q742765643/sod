import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SCHEDULE_CENTER_API

// 查询列表
export function pageList(query) {
  return request({
    url: baseUrl + '/comMetaData/page',
    method: 'get',
    params: query
  })
}

// 查询记录据列表
export function recordPage(query) {
  return request({
    url: baseUrl + '/comMetaData/recordPage',
    method: 'get',
    params: query
  })
}


// 新增
export function addConfig(query) {
  return request({
    url: baseUrl + '/comMetaData/addConfig',
    method: 'post',
    data: query
  })
}
// 编辑
export function editConfig(query) {
  return request({
    url: baseUrl + '/comMetaData/editConfig',
    method: 'put',
    data: query
  })
}

// 删除任务
export function delConfig(query) {
  return request({
    url: baseUrl + '/comMetaData/delConfig',
    method: 'delete',
    params: query
  })
}
// 删除记录
export function delRecord(query) {
  return request({
    url: baseUrl + '/comMetaData/delRecord',
    method: 'delete',
    params: query
  })
}
// 立即同步公共元数据
export function syncDataNow(query) {
  return request({
    url: baseUrl + '/comMetaData/syncDataNow',
    method: 'get',
    params: query,
    timeout:1000 * 60 * 20
  })
}

// 任务详情
export function findCfgByPk(query) {
  return request({
    url: baseUrl + '/comMetaData/findCfgByPk',
    method: 'get',
    params: query
  })
}
