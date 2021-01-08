import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_PORTAL;

// 查询表格
export function queryDataPage(query) {
  return request({
    url: baseUrl + '/portal/userManage/list',
    method: 'get',
    params: query
  })
}

// 删除
export function delById(query) {
  return request({
    url: baseUrl + '/portal/userManage/del',
    method: 'DELETE',
    params: query
  })
}

// 详情
export function getById(query) {
  return request({
    url: baseUrl + '/portal/userManage/getById',
    method: 'get',
    params: query
  })
}

// 修改审核状态
export function editById(data) {
  return request({
    url: baseUrl + '/portal/userManage/editById',
    method: 'put',
    data: data
  })
}
// 重置密码
export function resetPwd(data) {
  return request({
    url: baseUrl + '/portal/userManage/resetPwd',
    method: 'put',
    data: data
  })
}

