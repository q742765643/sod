import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SCHEDULE_CENTER_API

export function listSyncudtopcLog(query) {
  return request({
    url: baseUrl + '/schedule/syncudtopcLog/list',
    method: 'get',
    params: query
  })
}

export function getSyncudtopcLog(syncudtopcLogId) {
  return request({
    url: baseUrl + '/schedule/syncudtopcLog/' + syncudtopcLogId,
    method: 'get'
  })
}

export function delSyncudtopcLog(syncudtopcLogIds) {
  return request({
    url: baseUrl + '/schedule/syncudtopcLog/' + syncudtopcLogIds,
    method: 'delete'
  })
}

export function exportTableLog(query) {
  return request({
    url: baseUrl + '/schedule/syncudtopcLog/export',
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
