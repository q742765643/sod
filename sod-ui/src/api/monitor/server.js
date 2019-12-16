import request from '@/utils/request'
const baseUrl =process.env.VUE_APP_UCENTER_API

// 查询服务器详细
export function getServer() {
  return request({
    url: baseUrl+'/monitor/server',
    method: 'get'
  })
}
