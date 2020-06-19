import request from '@/utils/request'
const baseUrl =process.env.VUE_APP_UCENTER_API

// 获取路由
export const getRouters = () => {
  return request({
    url: baseUrl+'/getRouters',
    method: 'get'
  })
}
