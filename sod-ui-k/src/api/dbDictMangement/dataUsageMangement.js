import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;
// 查询表格
export function queryDataBaseLogicAll(query) {
  return request({
    url: baseUrl + '/dm/logicDefine/list',
    method: 'get',
    params: query
  })
}
// 删除
export function delLogic(query) {
  return request({
    url: baseUrl + '/dm/logicDefine/del',
    method: 'DELETE',
    params: query
  })
}
// 详情
export function getById(query) {
  return request({
    url: baseUrl + '/dm/logicDefine/get',
    method: 'get',
    params: query
  })
}
// 表类型
export function getAllStorageType() {
  return request({
    url: baseUrl + '/dm/logicDefine/getAllStorageType',
    method: 'get',
  })
}
// 数据库名称
export function getDatabaseName() {
  return request({
    url: baseUrl + '/dm/databaseDefine/all',
    method: 'get',
  })
}
// 新增
export function saveLogic(data) {
  return request({
    url: baseUrl + '/dm/logicDefine/save',
    method: 'post',
    data: data
  })
}
// 编辑
export function editLogic(data) {
  return request({
    url: baseUrl + '/dm/logicDefine/edit',
    method: 'put',
    data: data
  })
}
// 下载

export function exportTable(query) {
  return request({
    url: baseUrl + '/dm/logicDefine/exportTable',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}
