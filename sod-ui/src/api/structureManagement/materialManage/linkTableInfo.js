import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;
// 查询表格
export function getTableInfo(query) {
  return request({
    url: baseUrl + '/dm/dataClass/getTableInfo',
    method: 'get',
    params: query
  })
}

