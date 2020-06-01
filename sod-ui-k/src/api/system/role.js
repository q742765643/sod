import request from '@/utils/request'
const baseUrl = window.serverConfig.VUE_APP_UCENTER_API

// 查询角色列表
export function listRole(query) {
  return request({
    url: baseUrl + '/system/role/list',
    method: 'get',
    params: query
  })
}
export function optionselect() {
  return request({
    url: baseUrl + '/system/role/optionselect',
    method: 'get'
  })
}

// 查询角色详细
export function getRole(roleId) {
  return request({
    url: baseUrl + '/system/role/' + roleId,
    method: 'get'
  })
}

// 新增角色
export function addRole(data) {
  return request({
    url: baseUrl + '/system/role',
    method: 'post',
    data: data
  })
}

// 修改角色
export function updateRole(data) {
  return request({
    url: baseUrl + '/system/role',
    method: 'put',
    data: data
  })
}

// 角色数据权限
export function dataScope(data) {
  return request({
    url: baseUrl + '/system/role/dataScope',
    method: 'put',
    data: data
  })
}

// 角色状态修改
export function changeRoleStatus(id, status) {
  const data = {
    id,
    status
  }
  return request({
    url: baseUrl + '/system/role/changeStatus',
    method: 'put',
    data: data
  })
}

// 删除角色
export function delRole(roleId) {
  return request({
    url: baseUrl + '/system/role/' + roleId,
    method: 'delete'
  })
}

// 导出角色
export function exportRole(query) {
  return request({
    url: baseUrl + '/system/role/export',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}
