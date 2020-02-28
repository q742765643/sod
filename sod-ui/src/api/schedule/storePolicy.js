import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SCHEDULE_CENTER_API

// 存储策略树
export function strategyTree(query) {
  return request({
    url: baseUrl + '/strategyPolicy/strategyTree',
    method: 'get',
    params: query
  })
}
