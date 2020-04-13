import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;

// 查询表信息
export function storageConfigurationList(query) {
  return request({
    url: baseUrl + '/dm/storageConfiguration/list',
    method: 'get',
    params: query
  })
}
