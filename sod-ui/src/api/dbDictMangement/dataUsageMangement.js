import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SERVER_API;
// 查询表格
export function queryDataBaseLogicAll(query) {
  return request({
    url: baseUrl + '/system/api/apiDataEleDefine/queryDataBaseLogicAll',
    method: 'get',
    params: query
  })
}
