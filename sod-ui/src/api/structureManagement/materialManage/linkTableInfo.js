import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;
// 查询表格
export function getRelatedTables(query) {
  return request({
    url: baseUrl + '/dm/dataTable/getRelatedTables',
    method: 'get',
    params: query
  })
}

