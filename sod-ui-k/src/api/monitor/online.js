import request from '@/utils/request'
const baseUrl =process.env.VUE_APP_UCENTER_API

// 查询在线用户列表
export function list(query) {
  return request({
    url: baseUrl+'/monitor/online/list',
    method: 'get',
    params: query
  })
}

// 强退用户
export function forceLogout(tokenId) {
  return request({
    url: baseUrl+'/monitor/online/' + tokenId,
    method: 'delete'
  })
}
