import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_WLEI;
// 新增表格
export function addTable(data) {
  return request({
    url: baseUrl + '/dm/databaseUser/add',
    method: 'post',
    data: data
  })
}
