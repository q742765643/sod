import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;
// 查询表格
export function getBaseData(query) {
  return request({
    url: baseUrl + '/dm/dataClass/getBaseData',
    method: 'get',
    params: query
  })
}
export function exportTable(query) {
  return request({
    url: baseUrl + '/dm/dataClass/exportBaseData',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}
