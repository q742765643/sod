import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SCHEDULE_CENTER_API

export function listBackupLog(query) {
  return request({
    url: baseUrl + '/schedule/backupLog/list',
    method: 'get',
    params: query
  })
}

export function getBackupLog(backupLogId) {
  return request({
    url: baseUrl + '/schedule/backupLog/' + backupLogId,
    method: 'get'
  })
}

export function delBackupLog(backupLogIds) {
  return request({
    url: baseUrl + '/schedule/backupLog/' + backupLogIds,
    method: 'delete'
  })
}

export function exportTable(query) {
  return request({
    url: baseUrl + '/schedule/backupLog/export',
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
