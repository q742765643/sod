import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_UCENTER_API;
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
// 详情
export function detailById(id) {
  return request({
    url: baseUrl + '/dictionary/define/' + id,
    method: 'get',
  })
}
// 下载
export function exportTable(query) {
  return request({
    url: baseUrl + '/dictionary/define/exportTable',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}
