import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SYSTEM;
// 查询表格
export function gridEleDecodeDefineAll(query) {
  return request({
    url: baseUrl + '/system/gribParameterDefine/list',
    method: 'get',
    params: query
  })
}
// 添加
export function gridEleDecodeDefineAdd(data) {
  return request({
    url: baseUrl + '/system/gribParameterDefine/save',
    method: 'post',
    data: data
  })
}
export function gridEleDecodeDefineEdit(data) {
  return request({
    url: baseUrl + '/system/gribParameterDefine/edit',
    method: 'put',
    data: data
  })
}
// 删除
export function gridEleDecodeDefineDelete(ids) {
  return request({
    url: baseUrl + '/system/gribParameterDefine/deleteByIds/' + ids,
    method: 'delete',
  })
}
// 导出
export function exportJSON(query) {
  return request({
    url: baseUrl + '/system/gribParameterDefine/exportTable',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}
// 导入
export function importJSON() {
  return request({
    url: baseUrl + '/system/api/gridEleDecodeDefine/importJSON',
    method: 'post',
    data: data
  })
}
