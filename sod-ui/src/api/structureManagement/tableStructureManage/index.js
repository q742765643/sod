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
    url: baseUrl + '/dm/logicDefine/all',
    method: 'get',
    params: query
  })
}

// 粘贴
export function pasteTable(data) {
  return request({
    url: baseUrl + '/dm/dataTable/paste',
    method: 'post',
    data: data
  })
}
//粘贴前查询
export function dataLogicGet(data) {
  return request({
    url: baseUrl + '/dm/dataLogic/get',
    method: 'get',
    data: data
  })
}
