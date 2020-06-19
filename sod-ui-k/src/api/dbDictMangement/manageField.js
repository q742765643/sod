import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SYSTEM
//管理字段管理
export function findAllManageGroup() {
  return request({
    url: baseUrl + '/managefield/findAllManageGroup',
    method: 'get',
  })
}
export function addManageGroup(data) {
  return request({
    url: baseUrl + "/managefield/addManageGroup",
    method: "post",
    params: data
  });
}
export function editManageGroup(data) {
  return request({
    url: baseUrl + "/managefield/editManageGroup",
    method: "PUT",
    params: data
  });
}
export function findManageFieldByPk(query) {
  return request({
    url: baseUrl + '/managefield/findManageFieldByPk',
    method: 'get',
    params: query
  })
}
export function pageField(query) {
  return request({
    url: baseUrl + '/managefield/page',
    method: 'get',
    params: query
  })
}
export function delManageGroup(query) {
  return request({
    url: baseUrl + '/managefield/delManageGroup',
    method: 'DELETE',
    params: query
  })
}
export function addManageField(data) {
  return request({
    url: baseUrl + "/managefield/addManageField",
    method: "post",
    params: data
  });
}
export function findByType() {
  return request({
    url: baseUrl + "/system/dict/data//dictType/table_column_type",
    method: "get",
  });
}
export function delManageField(data) {
  return request({
    url: baseUrl + "/managefield/delManageField",
    method: "DELETE",
    params: data
  });
}

export function editManageField(data) {
  return request({
    url: baseUrl + "/managefield/editManageField",
    method: "post",
    data: data
  });
}
export function exportTable(query) {
  return request({
    url: baseUrl + '/managefield/export',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}
