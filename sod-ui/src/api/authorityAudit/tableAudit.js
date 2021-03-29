import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;
// 查询表格
export function tableapplyList(query) {
  return request({
    url: baseUrl + '/dm/tableapply/list',
    method: 'get',
    params: query
  })
}
//根据用户名查询业务用户
export function getBizUserByName(query) {
    return request({
      url: baseUrl + '/system/user/getBizUserByName',
      method: 'get',
      params: query
    })
}
  
//根据绑定id查询审核日志
export function reviewlogenable(query) {
    return request({
      url: baseUrl + '/dm/reviewlog/enable',
      method: 'get',
      params: query
    })
}
  
//审核
export function tableapplyReview(data) {
  return request({
    url: baseUrl + '/dm/tableapply/review',
    method: 'post',
    data: data
  })
}