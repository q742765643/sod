import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;
// 查询表格
export function list(query) {
  return request({
    url: baseUrl + '/dm/newdataApply/list',
    method: 'get',
    params: query
  })
}
