import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;
// 查询表格
export function pageList(query) {
  return request({
    url: baseUrl + '/dm/databaseSpecial/page',
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

/* 删除 */
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

// 数据库授权
export function empowerDatabaseSpecial(data) {
  return request({
    url: baseUrl + '/dm/databaseSpecial/empowerDatabaseSpecial',
    method: 'post',
    data: data
  })
}

// 修改专题库基本信息
export function saveBase(data) {
  return request({
    url: baseUrl + '/dm/databaseSpecial/save',
    method: 'post',
    data: data
  })
}

export function exportTable(query) {
  return request({
    url: baseUrl + '/dm/fileUpDown/download',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}


// 拒绝
export function updateExamineStatus(query) {
  return request({
    url: baseUrl + '/dm/databaseSpecial/updateExamineStatus',
    method: 'get',
    params: query,
  })
}

export function exportTables(query) {
  return request({
    url: baseUrl + '/dm/databaseSpecial/exportTable',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}
