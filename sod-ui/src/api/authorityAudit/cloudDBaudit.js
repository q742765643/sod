import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SERVER_API;
// 查询表格
export function cldbApplicationAll(query) {
  return request({
    url: baseUrl + '/system/api/cldbApplication/all',
    method: 'get',
    params: query
  })
}
// 删除表格
export function deleteExamine(data) {
  return request({
    url: baseUrl + '/system/api/cldbApplication/deleteExamine',
    method: 'get',
    params: data
  })
}
// 添加表格
export function addInfo(data) {
  return request({
    url: baseUrl + '/system/api/cldbApplication/addInfo',
    method: 'post',
    data: data
  })
}
// 获取数据库类型
export function getYunDbLogic(data) {
  return request({
    url: baseUrl + '/system/api/cldbApplication/getYunDbLogic',
    method: 'get',
  })
}
// 获取文件存储类型存储空间大小
export function getFileLogic(data) {
  return request({
    url: baseUrl + '/system/api/cldbApplication/getFileLogic',
    method: 'get',
  })
}
// 获取mysql类型存储空间大小
export function getMysqlLogic(data) {
  return request({
    url: baseUrl + '/system/api/cldbApplication/getMysqlLogic',
    method: 'get',
  })
}
//获取redis类型存储空间大小
export function getRedisLogic(data) {
  return request({
    url: baseUrl + '/system/api/cldbApplication/getRedisLogic',
    method: 'get',
  })
}
// 获取cpu内存类型
export function databaseLogic(data) {
  return request({
    url: baseUrl + '/system/api/cldbApplication/databaseLogic',
    method: 'get',
  })
}
