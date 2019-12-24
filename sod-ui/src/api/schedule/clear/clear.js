import request from '@/utils/request'
const baseUrl =process.env.VUE_APP_SCHEDULE_CENTER_API

export function listClear(query) {
  return request({
    url: baseUrl+'/schedule/clear/list',
    method: 'get',
    params: query
  })
}

export function getClear(clearId) {
  return request({
    url: baseUrl+'/schedule/clear/' + clearId,
    method: 'get'
  })
}

export function addClear(data) {
  return request({
    url: baseUrl+'/schedule/clear',
    method: 'post',
    data: data
  })
}

export function updateClear(data) {
  return request({
    url: baseUrl+'/schedule/clear',
    method: 'put',
    data: data
  })
}

export function delClear(clearIds) {
  return request({
    url: baseUrl+'/schedule/clear/' + clearIds,
    method: 'delete'
  })
}


