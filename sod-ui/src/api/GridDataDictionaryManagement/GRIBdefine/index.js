import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SERVER_API;
// 查询表格
export function gridEleDecodeDefineAll(query) {
  return request({
    url: baseUrl + '/system/api/gridEleDecodeDefine/all',
    method: 'get',
    params: query
  })
}
// 添加
export function gridEleDecodeDefineAdd(data) {
  return request({
    url: baseUrl + '/system/api/gridEleDecodeDefine/add',
    method: 'post',
    data: data
  })
}
export function gridEleDecodeDefineEdit(data) {
  return request({
    url: baseUrl + '/system/api/gridEleDecodeDefine/add',
    method: 'post',
    data: data
  })
}
// 删除
export function gridEleDecodeDefineDelete(ids) {
  return request({
    url: baseUrl + '/system/api/gridEleDecodeDefine/deleteByIds/' + ids,
    method: 'get',
  })
}
// 导出 
export function exportJSON() {
  return request({
    url: baseUrl + '/system/api/gridEleDecodeDefine/exportJSON',
    method: 'get',
  })
}
// 导入
export function importJSON(data) {
  return request({
    url: baseUrl + '/system/api/gridEleDecodeDefine/importJSON',
    method: 'post',
    data: data
  })
}
