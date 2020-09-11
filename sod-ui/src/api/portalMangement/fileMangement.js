import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_PORTAL;

// 查询表格
export function queryDataPage(query) {
  return request({
    url: baseUrl + '/portal/fileManage/list',
    method: 'get',
    params: query
  })
}

// 新增
export function saveFileManage(data) {
  return request({
    url: baseUrl + '/portal/fileManage/saveFileManage',
    method: 'post',
    data: data
  })
}

export function getDictDataByType(type) {
  return request({
    url: baseUrl + '/schedule/sync/getDictDataByType/' + type,
    method: 'get',
  })
}

// 删除
export function delFileManage(query) {
  return request({
    url: baseUrl + '/portal/fileManage/del',
    method: 'DELETE',
    params: query
  })
}

// 详情
export function getById(query) {
  return request({
    url: baseUrl + '/portal/fileManage/getById',
    method: 'get',
    params: query
  })
}

// 编辑
export function editFileManage(data) {
  return request({
    url: baseUrl + '/portal/fileManage/edit',
    method: 'put',
    data: data
  })
}
