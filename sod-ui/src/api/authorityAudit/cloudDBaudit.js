import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;
// 查询表格
export function cldbApplicationAll(query) {
  return request({
    url: baseUrl + '/dm/cloudDatabaseApply/list',
    method: 'get',
    params: query
  })
}
//
export function getDictDataByType(query) {
  return request({
    url: baseUrl + '/dm/cloudDatabaseApply/getDictDataByType/' + query,
    method: 'get',
  })
}
// 删除表格
export function deleteExamine(data) {
  return request({
    url: baseUrl + '/dm/cloudDatabaseApply/deleteById',
    method: 'delete',
    params: data
  })
}
// 添加表格
export function saveCldb(data) {
  return request({
    url: baseUrl + '/dm/cloudDatabaseApply/save',
    method: 'post',
    data: data
  })
}

// 编辑
export function editCldb(data) {
  return request({
    url: baseUrl + '/dm/cloudDatabaseApply/edit',
    method: 'put',
    data: data
  })
}
// 查详情
export function getById(query) {
  return request({
    url: baseUrl + '/dm/cloudDatabaseApply/getById',
    method: 'get',
    params: query
  })
}

// 下载

export function exprotFile(query) {
  return request({
    url: baseUrl + '/dm/cloudDatabaseApply/download',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}

// 模板下载
export function exportDemo() {
  return request({
    url: baseUrl + '/api/dbfile/downloadFile',
    method: 'get',
    params: {
      name: "clouddatabase-application"
    },
    responseType: "arraybuffer"
  })
}

// 获取所有用户
export function getUserByType(query) {
  return request({
    url: baseUrl + '/system/user/getUserByType',
    method: 'get',
    params: query
  })
}
