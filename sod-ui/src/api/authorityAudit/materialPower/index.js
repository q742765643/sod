import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM

export function queryList(query) {
  return request({
    url: baseUrl + '/dm/dataAuthorityApply/list',
    method: 'get',
    params: query
  })
}
