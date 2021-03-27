import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_PORTAL;

// 查询角色列表
export function listRole(query) {
  return request({
    url: baseUrl + '/portal/roleManage/list',
    method: 'get',
    params: query
  })
}

export function findAllRole() {
  return request({
    url: baseUrl + '/portal/roleManage/findAllRole',
    method: 'get'
  })
}

export function addRole(data) {
  return request({
    url: baseUrl + '/portal/roleManage/save',
    method: 'post',
    data: data
  })
}

// 查询角色详细
export function getRole(query) {
  return request({
    url: baseUrl + '/portal/roleManage/getById',
    method: 'get',
    params: query
  })
}

// 删除角色
export function delRole(roleId) {
  return request({
    url: baseUrl + '/portal/roleManage/' + roleId,
    method: 'delete'
  })
}

// 修改角色
export function updateRole(data) {
  return request({
    url: baseUrl + '/portal/roleManage/edit',
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
    url: baseUrl + '/portal/roleManage/changeStatus',
    method: 'put',
    data: data
  })
}
