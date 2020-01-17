import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SYNC

export function syncList(query) {
  return request({
    url: baseUrl + '/schedule/sync/list',
    method: 'get',
    params: query
  })
}
