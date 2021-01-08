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
// 执行
export function executeNew(data) {
  return request({
    url: baseUrl + '/dm/onlineTime/executeNew',
    method: 'put',
    data: data
  })
}

export function getByClassId(query) {
  return request({
    url: baseUrl + '/dm/onlineTime/getByClassId',
    method: 'get',
    params: query
  })
}
// 立即执行
export function execute(id) {
  return request({
    url: baseUrl + '/schedule/job/execute',
    method: 'get',
    params: {
      "id": id
    }
  })
}
