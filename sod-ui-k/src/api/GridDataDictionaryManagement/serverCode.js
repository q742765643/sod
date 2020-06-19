import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SYSTEM;
// 查询表格
export function gridEleServiceDefineAll(query) {
  return request({
    url: baseUrl + '/system/serviceCodeDefine/list',
    method: 'get',
    params: query
  })
}
// 添加
export function gridEleServiceDefineAdd(data) {
  return request({
    url: baseUrl + '/system/serviceCodeDefine/save',
    method: 'post',
    data: data
  })
}
// 编辑
export function gridEleServiceDefineEdit(data) {
  return request({
    url: baseUrl + '/system/serviceCodeDefine/edit',
    method: 'put',
    data: data
  })
}
// 删除
export function gridEleServiceDefineDelete(ids) {
  return request({
    url: baseUrl + '/system/serviceCodeDefine/deleteByIds/' + ids,
    method: 'delete'
  })


}
