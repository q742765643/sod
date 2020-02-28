import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SCHEDULE_CENTER_API

export function strategyTree(query) {
  return request({
    url: baseUrl + '/strategyPolicy/strategyTree',
    method: 'get',
    params: query
  })
}
