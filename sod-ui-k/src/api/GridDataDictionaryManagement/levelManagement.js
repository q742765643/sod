import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_UCENTER_API;
// 查询表格
export function levelList(query) {
  return request({
    url: baseUrl + '/dictionary/level/list',
    method: 'get',
    params: query
  })
}
// 查所有
export function getAllLevel() {
  return request({
    url: baseUrl + '/dictionary/level/getAllLevel',
    method: 'get',
  })
}
// 添加
export function levelSave(data) {
  return request({
    url: baseUrl + '/dictionary/level/save',
    method: 'post',
    data: data
  })
}
// 编辑
export function levelEdit(data) {
  return request({
    url: baseUrl + '/dictionary/level/edit',
    method: 'put',
    data: data
  })
}
// 删除
export function levelDelete(ids) {
  return request({
    url: baseUrl + '/dictionary/level/' + ids,
    method: 'delete',
  })
}
