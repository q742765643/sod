import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SCHEDULE_CENTER_API

export function listSynctofileLog(query) {
  return request({
    url: baseUrl + '/schedule/synctofileLog/list',
    method: 'get',
    params: query
  })
}

export function getSynctofileLog(synctofileLogId) {
  return request({
    url: baseUrl + '/schedule/synctofileLog/' + synctofileLogId,
    method: 'get'
  })
}

export function delSynctofileLog(synctofileLogIds) {
  return request({
    url: baseUrl + '/schedule/synctofileLog/' + synctofileLogIds,
    method: 'delete'
  })
}

export function exportTableLog(query) {
  return request({
    url: baseUrl + '/schedule/synctofileLog/export',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}

export function downFile(query) {
  return request({
    url: baseUrl + '/api/schedule/uploadDown/downFile',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}
