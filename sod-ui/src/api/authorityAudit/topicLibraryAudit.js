import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_WLEI;
// 查询表格
export function specialList(query) {
  return request({
    url: baseUrl + '/dm/databaseSpecial/specialList',
    method: 'get',
    params: query
  })
}

// 根据ID查询基本信息
export function getById(query) {
  return request({
    url: baseUrl + '/dm/databaseSpecial/getById',
    method: 'get',
    params: query
  })
}

// 数据库授权信息
export function getAuthorityBySdbId(query) {
  return request({
    url: baseUrl + '/dm/databaseSpecial/getAuthorityBySdbId',
    method: 'get',
    params: query
  })
}

// 获取专题库资料列表
export function getSpecialDataList(query) {
  return request({
    url: baseUrl + '/dm/databaseSpecial/getSpecialDataList',
    method: 'get',
    params: query
  })
}

/* 数据库授权 */
export function empowerDatabaseSperial(data) {
  return request({
    url: baseUrl + '/dm/databaseSpecial/empowerDatabaseSperial',
    method: 'post',
    data: data
  })
}
export function deleteList(query) {
  return request({
    url: baseUrl + '/dm/databaseSpecial/delete',
    method: 'delete',
    params: query
  })
}

// 批量授权
export function empowerDataBatch(data) {
  return request({
    url: baseUrl + '/dm/databaseSpecial/empowerDataBatch',
    method: 'post',
    data: data
  })
}

// 单个授权
export function empowerDataOne(data) {
  return request({
    url: baseUrl + '/dm/databaseSpecial/empowerDataOne',
    method: 'post',
    data: data
  })
}
