import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;
// 新增表格
export function addTable(data) {
  return request({
    url: baseUrl + '/dm/databaseUser/add',
    method: 'post',
    data: data
  })
}

// 数据库列表
export function databaseList(query) {
  return request({
    url: baseUrl + '/dm/databaseUser/databaseList',
    method: 'get',
    params: query
  })
}

// 表格列表
export function databaseUserAll(query) {
  return request({
    url: baseUrl + '/dm/databaseUser/all',
    method: 'get',
    params: query
  })
}
