import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DMWLEI

export function showImg(query) {
  return request({
    url: baseUrl + '/dm/fileUpDown/showImg',
    method: 'get',
    params: query
  })
}
