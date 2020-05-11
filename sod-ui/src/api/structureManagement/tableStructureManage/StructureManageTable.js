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
    method: 'post',
    params: data
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
// 删除
export function delByIdsKey(data) {
  return request({
    url: baseUrl + '/dm/foreignKey/delByIds',
    method: 'delete',
    params: data
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
    method: 'post',
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

// 字段新增 里的 数据类型
// 根据类型查询字典信息
export function getDictByType(query) {
  return request({
    url: baseUrl + '/dm/dataTable/getDictByType',
    method: 'get',
    params: query
  })
}
// 根据ID查询表字段详情
export function getColumDatailById(query) {
  return request({
    url: baseUrl + '/dm/tableColumn/get',
    method: 'get',
    params: query
  })
}
// 索引新增
export function tableIndexSave(data) {
  return request({
    url: baseUrl + '/dm/tableIndex/save',
    method: 'post',
    data: data
  })
}

// 查所有区域
export function allArea(query) {
  return request({
    url: baseUrl + '/dictionary/define/all',
    method: 'get',
  })
}
// 区域信息 删除


export function gridareaDel(query) {
  return request({
    url: baseUrl + '/dm/gridarea/delByIds',
    method: 'delete',
    params: query
  })
}
// 根据存储编码查询区域信息
export function findByDataServiceId(query) {
  return request({
    url: baseUrl + '/dm/gridarea/findByDataServiceId',
    method: 'get',
    params: query
  })
}

// 要素存储代码
export function serviceCodeQueryAll(query) {
  return request({
    url: baseUrl + '/system/gribParameterDefine/queryAll',
    method: 'get',
    async: false,
    params: query
  })
}
// 根据服务代码查询
export function findByDbFcstEle(query) {
  return request({
    url: baseUrl + '/system/serviceCodeDefine/findByDbFcstEle',
    method: 'get',
    params: query
  })
}
// 新增数据服务信息
export function dataserverconfigSave(data) {
  return request({
    url: baseUrl + '/dm/dataserverconfig/save',
    method: 'post',
    data: data
  })
}

export function dataserverconfiglist(query) {
  return request({
    url: baseUrl + '/dm/dataserverconfig/list',
    method: 'get',
    params: query
  })
}

export function dataserverconfigget(query) {
  return request({
    url: baseUrl + '/dm/dataserverconfig/get',
    method: 'get',
    params: query
  })
}

export function addList(data) {
  return request({
    url: baseUrl + '/dm/dataserverconfig/addList',
    method: 'post',
    data: data
  })
}

// 服务信息删除

export function delByIds(query) {
  return request({
    url: baseUrl + '/dm/dataserverconfig/delByIds',
    method: 'delete',
    params: query
  })
}
export function syncSCode(data) {
  return request({
    url: baseUrl + '/dm/tableColumn/syncSCode',
    method: 'post',
    data: data
  })
}


// 查询公共元数据字段
export function queryCmccElements(query) {
  return request({
    url: baseUrl + '/dm/tableColumn/queryCmccElements',
    method: 'get',
    params: query
  })
}

export function managefieldPage(query) {
  return request({
    url: baseUrl + '/managefield/findData',
    method: 'get',
    params: query
  })
}

// 基本信息查询
export function getDataClassBaseInfo(query) {
  return request({
    url: baseUrl + '/dm/classbaseinfo/getAllDataClassBaseInfo',
    method: 'get',
    params: query
  })
}
// 基本信息编辑
export function saveDataClassBaseInfo(query) {
  return request({
    url: baseUrl + '/dm/classbaseinfo/saveDataClassBaseInfo',
    method: 'post',
    data: query
  })
}

// 资料类型统计 单选

export function updateIsAllLine(query) {
  return request({
    url: baseUrl + '/dm/dataClass/updateIsAllLine',
    method: 'put',
    params: query
  })
}

// 查询归档时间
export function getArchive(query) {
  return request({
    url: baseUrl + '/dm/dataClass/getArchive',
    method: 'get',
    params: query
  })
}
