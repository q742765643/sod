import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_PORTAL;

// 查询表格
export function queryDataPage(query) {
  return request({
    url: baseUrl + '/portal/dynManage/list',
    method: 'get',
    params: query
  })
}

// 新增
export function saveDynManage(data) {
  return request({
    url: baseUrl + '/portal/dynManage/saveDynManage',
    method: 'post',
    data: data
  })
}

// 编辑
export function editDynManage(data) {
  return request({
    url: baseUrl + '/portal/dynManage/edit',
    method: 'put',
    data: data
  })
}

// 详情
export function getById(query) {
  return request({
    url: baseUrl + '/portal/dynManage/getById',
    method: 'get',
    params: query
  })
}

// 删除
export function delSyncManage(query) {
  return request({
    url: baseUrl + '/portal/dynManage/del',
    method: 'DELETE',
    params: query
  })
}
