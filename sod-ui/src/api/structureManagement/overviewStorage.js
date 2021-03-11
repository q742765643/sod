import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;

// 查询表信息
export function storageConfigurationList(query) {
  return request({
    url: baseUrl + '/dm/storageConfiguration/list',
    method: 'get',
    params: query
  })
}

// 删除
export function deleteColumnValue(query) {
  return request({
    url: baseUrl + '/dm/storageConfiguration/deleteColumnValue',
    method: 'delete',
    params: query
  })
}

// 配置
export function updateColumnValue(data) {
  return request({
    url: baseUrl + '/dm/storageConfiguration/updateColumnValue',
    method: 'post',
    params: data
  })
}

export function exportTable(query) {
  return request({
    url: baseUrl + '/dm/storageConfiguration/exportTable',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}

// 根据子表类型查询
export function existTable(query) {
  return request({
    url: baseUrl + '/dm/dataTable/existTable',
    method: 'get',
    params: query
  })
}
// 根据子表类型查询
export function findBySubType(query) {
  return request({
    url: baseUrl + '/dm/dataTable/findBySubType',
    method: 'get',
    params: query
  })
}
// 根据tableId查询相关资料
export function getClassByTableId(query) {
  return request({
    url: baseUrl + '/dm/dataClass/getClassByTableId',
    method: 'get',
    params: query
  })
}

// 查询规范
export function getNorm(query) {
  return request({
    url: baseUrl + '/dm/dataclassnorm/get',
    method: 'get',
    params: query
  })
}
// 新增规范
export function saveNorm(data) {
  return request({
    url: baseUrl + '/dm/dataclassnorm/save',
    method: 'post',
    data: data
  })
}