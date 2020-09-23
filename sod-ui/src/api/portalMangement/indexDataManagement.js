import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_PORTAL;

// 查询表格
export function queryDataPage(query) {
  return request({
    url: baseUrl + '/portal/homeData/list',
    method: 'get',
    params: query
  })
}

// 新增
export function homeDataSave(data) {
  return request({
    url: baseUrl + '/portal/homeData/save',
    method: 'post',
    data: data
  })
}
// 详情
export function getById(query) {
  return request({
    url: baseUrl + '/portal/homeData/getById',
    method: 'get',
    params: query
  })
}

// 编辑
export function editById(data) {
  return request({
    url: baseUrl + '/portal/homeData/edit',
    method: 'put',
    data: data
  })
}

// 删除
export function delById(query) {
  return request({
    url: baseUrl + '/portal/homeData/del',
    method: 'DELETE',
    params: query
  })
}
