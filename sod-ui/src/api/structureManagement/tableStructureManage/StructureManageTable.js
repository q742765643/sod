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
export function tableColumnDel(data) {
  return request({
    url: baseUrl + '/dm/tableColumn/delIds',
    method: 'delete',
    data: data
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

// 要素表/键表
export function dataTableSavle(data) {
  return request({
    url: baseUrl + '/dm/dataTable/save',
    method: 'post',
    data: data
  })
}
// /字段新增
export function tableColumnSave(data) {
  return request({
    url: baseUrl + '/dm/tableColumn/save',
    method: 'post',
    data: data
  })
}

// 字段批量新增
export function tableColumnSaveList(data) {
  return request({
    url: baseUrl + '/dm/tableColumn/saveList',
    method: 'post',
    data: data
  })
}

// 查索引
export function findTableIndex(query) {
  return request({
    url: baseUrl + '/dm/tableIndex/findByTableId',
    method: 'get',
    params: query
  })
}

// 删除索引
export function delTableIndex(data) {
  return request({
    url: baseUrl + '/dm/tableIndex/del',
    method: 'delete',
    data: data
  })
}

// 资料类型统计
export function datastatisticsList(query) {
  return request({
    url: baseUrl + '/dm/datastatistics/list',
    method: 'get',
    params: query
  })
}

// 样例数据查询
export function sampleList(data) {
  return request({
    url: baseUrl + '/dm/dataTable/sample',
    method: 'get',
    data: data
  })
}

// 区域信息新增
export function gridareaSave(data) {
  return request({
    url: baseUrl + '/dm/gridarea/save',
    method: 'post',
    data: data
  })
}
// 区域信息查询
export function gridareaList(query) {
  return request({
    url: baseUrl + '/dm/gridarea/list',
    method: 'get',
    params: query
  })
}

// 服务配置
export function dataserverbaseinfoGet(query) {
  return request({
    url: baseUrl + '/dm/dataserverbaseinfo/get',
    method: 'get',
    params: query
  })
}

// 根据存储编码查询默认配置详情
export function findByDataCLassId(query) {
  return request({
    url: baseUrl + '/dm/dataserverbaseinfo/findByDataCLassId',
    method: 'get',
    params: query
  })
}
//保存
export function saveBase(data) {
  return request({
    url: baseUrl + '/dm/dataserverbaseinfo/save',
    method: 'post',
    data: data
  })
}
