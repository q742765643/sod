import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DICT;
// 查询表格
export function defineList(query) {
  return request({
    url: baseUrl + '/dictionary/define/list',
    method: 'get',
    params: query
  })
}
// 添加
export function defineSave(data) {
  return request({
    url: baseUrl + '/dictionary/define/save',
    method: 'post',
    data: data
  })
}
// 编辑
export function defineEdit(data) {
  return request({
    url: baseUrl + '/dictionary/define/edit',
    method: 'put',
    data: data
  })
}
// 删除
export function defineDelete(id) {
  return request({
    url: baseUrl + '/dictionary/define/' + id,
    method: 'delete',
  })
}
