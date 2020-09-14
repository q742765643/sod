import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_PORTAL;

// 查询表格
export function queryDataPage(query) {
  return request({
    url: baseUrl + '/portal/feedbackManage/list',
    method: 'get',
    params: query
  })
}


// 详情
export function getById(query) {
  return request({
    url: baseUrl + '/portal/feedbackManage/getById',
    method: 'get',
    params: query
  })
}
