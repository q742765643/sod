import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;

// 查询表信息
export function onLineList(query) {
  return request({
    url: baseUrl + '/dm/onlineTime/onLineList',
    method: 'get',
    params: query
  })
}
// 修改资料在线时间
export function update(data) {
  return request({
    url: baseUrl + '/dm/onlineTime/update',
    method: 'put',
    data: data
  })
}
