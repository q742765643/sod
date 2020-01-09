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
export function delContainerTableById(data) {
  return request({
    url: baseUrl + '/dm/dataClass/del',
    method: 'delete',
    params: data
  })
}
