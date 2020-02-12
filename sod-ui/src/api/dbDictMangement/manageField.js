import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DB_API
//管理字段管理
export function findAllManageGroup() {
  return request({
    url: baseUrl + '/managefield/findAllManageGroup',
    method: 'get',
  })
}
export function addManageGroup(query) {
  return request({
    url: baseUrl + "/managefield/addManageGroup",
    method: "post",
    params: query
  });
}
export function editManageGroup(query) {
  return request({
    url: baseUrl + "/managefield/editManageGroup",
    method: "PUT",
    params: query
  });
}
export function findManageFieldByPk(query) {
  return request({
    url: baseUrl + '/managefield/findManageFieldByPk',
    method: 'get',
    data: query
  })
}
export function delManageGroup(data) {
  return request({
    url: baseUrl + '/managefield/delManageGroup',
    method: 'DELETE',
    data: data
  })
}
export function addManageField(query) {
  return request({
    url: baseUrl + "/managefield/addManageField",
    method: "post",
    params: query
  });
}
