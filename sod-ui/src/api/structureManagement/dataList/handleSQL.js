import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;

// 查询表信息 生成SQL
export function getSql(query) {
  return request({
    url: baseUrl + '/dm/dataTable/getSql',
    method: 'get',
    params: query
  })
}

// 保存SQL
export function saveSql(data) {
  return request({
    url: baseUrl + '/dm/tablesql/save',
    method: 'post',
    data: data
  })
}
// 创建物理库
export function createTable(data) {
  return request({
    url: baseUrl + '/dm/dataTable/createTable',
    method: 'post',
    data: data
  })
}

//表结构是否存在
export function existTable(query) {
  return request({
    url: baseUrl + '/dm/dataTable/existTable',
    method: 'get',
    params: query
  })
}
