import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;

// 查询表信息 生成SQL
export function getSql(query) {
  return request({
    url: baseUrl + '/dm/dataTable/getSql',
    method: 'get',
    params: query
  })
}
