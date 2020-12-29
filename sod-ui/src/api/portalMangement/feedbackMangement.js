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

// 编辑
export function editById(data) {
  return request({
    url: baseUrl + '/portal/feedbackManage/edit',
    method: 'put',
    data: data
  })
}
//删除
export function deleteId(data) {
  return request({
    url: baseUrl + '/portal/feedbackManage/del',
    method: 'delete',
    params: data
  })
}
export function getDictDataByType(query) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/getDictDataByType/' + query,
    method: 'get',
  })
}
