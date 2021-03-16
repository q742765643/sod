import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_PORTAL;

// 查询菜单列表
export function listMenu(query) {
  return request({
    url: baseUrl+'/portal/menuManage/list',
    method: 'get',
    params: query
  })
}

// 查询菜单下拉树结构
export function treeselect() {
  return request({
    url: baseUrl+'/portal/menuManage/treeselect',
    method: 'get'
  })
}

// 新增菜单
export function addMenu(data) {
  return request({
    url: baseUrl+'/portal/menuManage/addMenu',
    method: 'post',
    data: data
  })
}

// 修改菜单
export function updateMenu(data) {
  return request({
    url: baseUrl+'/portal/menuManage/updateMenu',
    method: 'put',
    data: data
  })
}

// 查询菜单详细
export function getMenu(menuId) {
  return request({
    url: baseUrl+'/portal/menuManage/' + menuId,
    method: 'get'
  })
}

// 删除菜单
export function delMenu(menuId) {
  return request({
    url: baseUrl+'/portal/menuManage/delMenu',
    method: 'delete',
    params: menuId
  })
}

// 根据角色ID查询菜单下拉树结构
export function roleMenuTreeselect(roleId) {
  return request({
    url: baseUrl+'/portal/menuManage/roleMenuTreeselect/' + roleId,
    method: 'get'
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
