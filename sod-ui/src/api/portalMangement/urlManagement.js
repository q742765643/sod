import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_PORTAL;

// 查询表格
export function queryDataPage(query) {
  return request({
    url: baseUrl + '/portal/apiManage/list',
    method: 'get',
    params: query
  })
}

// 新增
export function apiManageSave(data) {
  return request({
    url: baseUrl + '/portal/apiManage/save',
    method: 'post',
    data: data
  })
}
// 详情
export function getById(query) {
  return request({
    url: baseUrl + '/portal/apiManage/getById',
    method: 'get',
    params: query
  })
}

// 编辑
export function editById(data) {
  return request({
    url: baseUrl + '/portal/apiManage/edit',
    method: 'put',
    data: data
  })
}

// 删除
export function delById(query) {
  return request({
    url: baseUrl + '/portal/apiManage/del',
    method: 'DELETE',
    params: query
  })
}

// 批量导入
export function uploadApi(data) {
  return request({
    url: baseUrl + '/portal/apiManage/upload',
    method: 'post',
    data: data
  })
}
