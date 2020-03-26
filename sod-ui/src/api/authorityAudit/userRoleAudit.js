import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_UCENTER_API

// 查询列表
export function page(query) {
  return request({
    url: baseUrl + '/system/portalAuz/page',
    method: 'get',
    params: query
  })
}
// 删除业务角色
export function delByIds(id) {
  return request({
    url: baseUrl + '/system/portalAuz/delByIds',
    method: 'delete',
    params: {
      ids: id,
    }
  })
}
// 更改用户角色状态通过/不通过
export function update(id, status) {
  return request({
    url: baseUrl + '/system/portalAuz/update',
    method: 'put',
    params: {
      id: id,
      status: status
    }
  })
}
