import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SCHEDULE_CENTER_API

export function listMoveLog(query) {
  return request({
    url: baseUrl + '/schedule/moveLog/list',
    method: 'get',
    params: query
  })
}

export function getMoveLog(moveLogId) {
  return request({
    url: baseUrl + '/schedule/moveLog/' + moveLogId,
    method: 'get'
  })
}

export function delMoveLog(moveLogIds) {
  return request({
    url: baseUrl + '/schedule/moveLog/' + moveLogIds,
    method: 'delete'
  })
}

export function exportTable(query) {
  return request({
    url: baseUrl + '/schedule/moveLog/export',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}
