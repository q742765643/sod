import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;
// 资料分类树
export function dataClassAll() {
  return request({
    url: baseUrl + '/dm/dataClass/getTree',
    method: 'get',
  })
}
// 按数据库查询资料分类
export function databaseClass() {
  return request({
    url: baseUrl + '/dm/dataClass/databaseClass',
    method: 'get',
  })
}
// 按数据用途查询资料分类
export function logicClass() {
  return request({
    url: baseUrl + '/dm/dataClass/logicClass',
    method: 'get',
  })
}
// 公共元数据结构树
export function datumTypeGetTree() {
  return request({
    url: baseUrl + '/dm/datumType/getTree',
    method: 'get',
  })
}
// 根据ID删除
export function delBackup(id) {
  return request({
    url: baseUrl + '/dm/dataClass/del/' + id,
    method: 'delete'
  })
}
// 根据ID查询
export function dataClassById(id) {
  return request({
    url: baseUrl + '/dm/dataClass/get/' + id,
    method: 'get',
  })
}
// 新增/编辑
export function dataClassSave(data) {
  return request({
    url: baseUrl + '/dm/dataClass/save',
    method: 'post',
    data: data
  })
}
