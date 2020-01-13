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
// 删除表格
export function delByClass(data) {
  return request({
    url: baseUrl + '/dm/dataClass/delByClass',
    method: 'delete',
    params: data
  })
}
