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

// 删除
export function deleteColumnValue(query) {
  return request({
    url: baseUrl + '/dm/storageConfiguration/deleteColumnValue',
    method: 'delete',
    params: query
  })
}

// 配置
export function updateColumnValue(data) {
  return request({
    url: baseUrl + '/dm/storageConfiguration/updateColumnValue',
    method: 'post',
    data: data
  })
}
