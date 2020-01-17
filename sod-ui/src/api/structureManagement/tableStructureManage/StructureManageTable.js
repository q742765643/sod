import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;

// 查询表信息
export function gcl(query) {
  return request({
    url: baseUrl + '/dm/dataTable/gcl',
    method: 'get',
    params: query
  })
}
// 根据表格id查询字段列表
export function findByTableId(query) {
  return request({
    url: baseUrl + '/dm/dataClass/findByTableId',
    method: 'get',
    params: query
  })
}
