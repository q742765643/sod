import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_PORTAL;

// 查询表格
export function queryDataPage(query) {
  return request({
    url: baseUrl + '/portal/dataPlate/list',
    method: 'get',
    params: query
  })
}

// 新增
export function homeDataSave(data) {
  return request({
    url: baseUrl + '/portal/dataPlate/save',
    method: 'post',
    data: data
  })
}
// 详情
export function getById(query) {
  return request({
    url: baseUrl + '/portal/dataPlate/getById',
    method: 'get',
    params: query
  })
}

// 编辑
 export function editById(data) {
  return request({
    url: baseUrl + '/portal/dataPlate/edit',
    method: 'put',
    data: data
  })
}

// 删除
export function delById(query) {
  return request({
    url: baseUrl + '/portal/dataPlate/del',
    method: 'DELETE',
    params: query
  })
}
