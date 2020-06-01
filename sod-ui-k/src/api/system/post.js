import request from '@/utils/request'
const baseUrl =window.serverConfig.VUE_APP_UCENTER_API

// 查询岗位列表
export function listPost(query) {
  return request({
    url: baseUrl+'/system/post/list',
    method: 'get',
    params: query
  })
}

// 查询岗位详细
export function getPost(postId) {
  return request({
    url: baseUrl+'/system/post/' + postId,
    method: 'get'
  })
}

// 新增岗位
export function addPost(data) {
  return request({
    url: baseUrl+'/system/post',
    method: 'post',
    data: data
  })
}

// 修改岗位
export function updatePost(data) {
  return request({
    url: baseUrl+'/system/post',
    method: 'put',
    data: data
  })
}

// 删除岗位
export function delPost(postId) {
  return request({
    url: baseUrl+'/system/post/' + postId,
    method: 'delete'
  })
}

// 导出岗位
export function exportPost(query) {
  return request({
    url: baseUrl+'/system/post/export',
    method: 'get',
    params: query
  })
}
