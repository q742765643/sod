import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SCHEDULE_CENTER_API

export function listClearLog(query) {
  return request({
    url: baseUrl + '/schedule/clearLog/list',
    method: 'get',
    params: query
  })
}

export function getClearLog(clearLogId) {
  return request({
    url: baseUrl + '/schedule/clearLog/' + clearLogId,
    method: 'get'
  })
}

export function delClearLog(clearLogIds) {
  return request({
    url: baseUrl + '/schedule/clearLog/' + clearLogIds,
    method: 'delete'
  })
}

export function exportTable(query) {
  return request({
    url: baseUrl + '/schedule/clearLog/export',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}
