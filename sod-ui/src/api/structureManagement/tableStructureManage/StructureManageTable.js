import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;

// 查询表信息
export function gcl(query) {
  return request({
    url: baseUrl + '/dm/dataTable/gcl',
    method: 'get',
    params: query
  })
}
// 根据表格id查询字段列表
export function findByTableId(query) {
  return request({
    url: baseUrl + '/dm/tableColumn/findByTableId',
    method: 'get',
    params: query
  })
}
// 删除字段
export function tableColumnDel(id) {
  return request({
    url: baseUrl + '/dm/tableColumn/delIds?ids=' + id,
    method: 'delete',
  })
}
// 根据表id查询分库分表键

export function getByTableId(query) {
  return request({
    url: baseUrl + '/dm/sharding/getByTableId',
    method: 'get',
    params: query
  })
}
// 新增分库分表
export function shardingSaves(data) {
  return request({
    url: baseUrl + '/dm/sharding/saves',
    method: 'post',
    data: data
  })
}
// 根据logicid查询外键关联
export function foreignKeyList(query) {
  return request({
    url: baseUrl + '/dm/foreignKey/findByClassLogicId',
    method: 'get',
    params: query
  })
}
// 新增外键关联
export function foreignKeySave(data) {
  return request({
    url: baseUrl + '/dm/foreignKey/save',
    method: 'post',
    data: data
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

// 要素表/键表新增
export function dataTableSavle(data) {
  return request({
    url: baseUrl + '/dm/dataTable/save',
    method: 'post',
    data: data
  })
}
// 删除外键关联
// export function tableColumnDel(id) {
//   return request({
//     url: baseUrl + '/dm/tableColumn/delIds?ids=' + id,
//     method: 'delete',
//   })
// }
