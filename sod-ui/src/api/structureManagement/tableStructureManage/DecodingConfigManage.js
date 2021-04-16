import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;

// 条件分页查询
export function griddecodingList(query) {
  return request({
    url: baseUrl + '/dm/griddecoding/list',
    method: 'get',
    params: query
  })
}
// 新增
export function griddecodingSave(data) {
  return request({
    url: baseUrl + '/dm/griddecoding/save',
    method: 'post',
    data: data
  })
}
// 新增多个
export function saveList(data) {
  return request({
    url: baseUrl + '/dm/griddecoding/saveList',
    method: 'post',
    data: data
  })
}
// 查详情
export function getById(query) {
  return request({
    url: baseUrl + '/dm/griddecoding/get',
    method: 'get',
    params: query
  })
}
// 删除

export function deleteList(query) {
  return request({
    url: baseUrl + '/dm/griddecoding/delByIds',
    method: 'delete',
    params: query
  })
}
