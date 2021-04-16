import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;

// 查询表信息
export function storageFieldList(query) {
  return request({
    url: baseUrl + '/dm/storageConfiguration/storageFieldList',
    method: 'get',
    params: query
  })
}
