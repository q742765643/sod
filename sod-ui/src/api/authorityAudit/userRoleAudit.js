import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_UCENTER_API

// 查询列表
export function gatAllBiz(query) {
  return request({
    url: baseUrl + '/system/user/gatAllBiz',
    method: 'get',
    params: query
  })
}

// 根据用户id查询
export function findByUserId(query) {
  return request({
    url: baseUrl + '/dm/databaseSpecial/findByUserId',
    method: 'get',
    params: query
  })
}
