import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_PORTAL;

// 查询表格
export function queryDataPage(query) {
  return request({
    url: baseUrl + '/portal/departManage/list',
    method: 'get',
    params: query
  })
}

// 详情
export function getById(query) {
  return request({
    url: baseUrl + '/portal/departManage/getById',
    method: 'get',
    params: query
  })
}

// 查询部门下拉树结构
export function treeselect(query) {
  return request({
    url: baseUrl+'/portal/departManage/treeselect',
    method: 'get',
    params: query
  })
}

// 新增
export function saveDepartManage(data) {
  return request({
    url: baseUrl + '/portal/departManage/save',
    method: 'post',
    data: data
  })
}

// 编辑
export function editById(data) {
  return request({
    url: baseUrl + '/portal/departManage/edit',
    method: 'put',
    data: data
  })
}

// 删除
export function delById(query) {
  return request({
    url: baseUrl + '/portal/departManage/del',
    method: 'DELETE',
    params: query
  })
}
