import request from '@/utils/request'
const baseUrl = window.serverConfig.VUE_APP_DM

export function showImg(query) {
  return request({
    url: baseUrl + '/dm/fileUpDown/showImg',
    method: 'get',
    params: query
  })
}
