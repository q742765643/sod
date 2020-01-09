import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SERVER_API;
// 查询表格
export function gridEleServiceDefineAll(query) {
  return request({
    url: baseUrl + '/system/api/gridEleServiceDefine/all',
    method: 'get',
    params: query
  })
}
// 添加
export function gridEleServiceDefineAdd(data) {
  return request({
    url: baseUrl + '/system/api/gridEleServiceDefine/add',
    method: 'post',
    data: data
  })
}
// 编辑
export function gridEleServiceDefineEdit(data) {
  return request({
    url: baseUrl + '/system/api/gridEleServiceDefine/edit',
    method: 'post',
    data: data
  })
}
// 删除
export function gridEleServiceDefineDelete(ids) {
  return request({
    url: baseUrl + '/system/api/gridEleServiceDefine/deleteByIds',
    method: 'get',
    params: ids
  })
}
