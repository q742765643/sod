import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;
// 查询表格
export function getListBYIn(query) {
  return request({
    url: baseUrl + '/dm/dataClass/getListBYIn',
    method: 'get',
    params: query
  })
}
// 根据公共元数据树查询
export function getLogicByDdataId(query) {
  return request({
    url: baseUrl + '/dm/dataClass/getLogicByDdataId',
    method: 'get',
    params: query
  })
}
// 根据公共元数据树查询表格
export function dataTableGcl(query) {
  return request({
    url: baseUrl + '/dm/dataTable/gcl',
    method: 'get',
    params: query
  })
}

// 删除资料/目录
export function delByClass(data) {
  return request({
    url: baseUrl + '/dm/dataClass/delByClass',
    method: 'delete',
    params: data
  })
}

// 新增资料或者目录 type = 1 资料，2 目录
export function save(data) {
  return request({
    url: baseUrl + '/dm/dataClass/save',
    method: 'post',
    data: data
  })
}

// 查询资料/目录
export function getDetailById(query) {
  return request({
    url: baseUrl + '/dm/dataClass/findByClassId',
    method: 'get',
    params: query
  })
}

// 数据用途选择
export function logicDefineAll(query) {
  return request({
    url: baseUrl + '/dm/logicDefine/getAllLd',
    method: 'get',
    params: query
  })
}

// 粘贴
export function pasteTable(data) {
  return request({
    url: baseUrl + '/dm/dataTable/paste',
    method: 'post',
    params: data
  })
}
//粘贴前查询
export function dataLogicGet(query) {
  return request({
    url: baseUrl + '/dm/dataLogic/get',
    method: 'get',
    params: query
  })
}

// 产品配置启用
export function enable() {
  return request({
    url: baseUrl + '/dm/product/enable',
    method: 'get',
  })
}
// 产品获取新增存储编码
export function getNewDataClassId(query) {
  return request({
    url: baseUrl + '/dm/dataClass/getNewDataClassId',
    method: 'get',
    params: query
  })
}

//根据父ID查询专题库
export function findByDatabaseDefineId(query) {
  return request({
    url: baseUrl + '/dm/database/findByDatabaseDefineId',
    method: 'get',
    params: query
  })
}
export function exportTable() {
  return request({
    url: baseUrl + '/api/dbfile/downloadFile',
    method: 'get',
    params: {
      name: "add-column"
    },
    responseType: "arraybuffer"
  })
}


export function getBizUserByName(query) {
  return request({
    url: baseUrl + '/system/user/getBizUserByName',
    method: 'get',
    params: query
  })
}

// 根据id查询关联表信息
export function dataTableGet(query) {
  return request({
    url: baseUrl + '/dm/dataTable/get',
    method: 'get',
    params: query
  })
}

// 公共元数据是否必须
export function mmdneed() {
  return request({
    url: baseUrl + '/dm/product/mmdneed',
    method: 'get'
  })
}


// 关联表信息 查询可以展示的数据库列表
export function getCanShowDatabaseDefineList() {
  return request({
    url: baseUrl + '/dm/databaseDefine/getCanShowDatabaseDefineList',
    method: 'get'
  })
}

// 关联表信息 根据物理库id查询
export function findETable(query) {
  return request({
    url: baseUrl + '/dm/dataTable/findETable',
    method: 'get',
    params: query
  })
}

// 关联表信息 根据tableId查询相关的键表要素表
export function findTables(query) {
  return request({
    url: baseUrl + '/dm/dataTable/findTables',
    method: 'get',
    params: query
  })
}

// 关联表信息 
export function findTablesByTableName(query) {
  return request({
    url: baseUrl + '/dm/dataTable/findTablesByTableName',
    method: 'get',
    params: query
  })
}


// 查询所有要素表 
export function findAllETables() {
  return request({
    url: baseUrl + '/dm/dataTable/findAllETables',
    method: 'get'
  })
}


