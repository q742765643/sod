import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SCHEDULE_CENTER_API

export function listSynctofile(query) {

  return request({
    url: baseUrl + '/schedule/synctofile/list',
    method: 'get',
    params: query
  })
}

export function getSynctofile(syncToFileId) {
  return request({
    url: baseUrl + '/schedule/synctofile/' + syncToFileId,
    method: 'get'
  })
}

export function addSynctofile(data) {
  return request({
    url: baseUrl + '/schedule/synctofile',
    method: 'post',
    data: data
  })
}

export function updateSynctofile(data) {
  return request({
    url: baseUrl + '/schedule/synctofile',
    method: 'put',
    data: data
  })
}

export function delSynctofile(syncToFileIds) {
  return request({
    url: baseUrl + '/schedule/synctofile/' + syncToFileIds,
    method: 'delete'
  })
}
export function findAllDataBase() {
  return request({
    url: baseUrl + '/schedule/synctofile/findDatabase',
    method: 'get'
  })
}
export function getByDatabaseId(databaseId, dataClassId) {
  return request({
    url: baseUrl + '/schedule/synctofile/findDataClassId',
    method: 'get',
    params: {
      "databaseId": databaseId,
      "dataClassId": dataClassId
    }
  })
}
export function getByDatabaseIdAndClassId(databaseId, dataClassId) {
  return request({
    url: baseUrl + '/schedule/job/getByDatabaseIdAndClassId',
    method: 'get',
    async: true,
    params: {
      "databaseId": databaseId,
      "dataClassId": dataClassId
    }
  })
}
export function startSynctofile(id) {
  return request({
    url: baseUrl + '/schedule/job/startById',
    method: 'get',
    params: {
      "id": id
    }
  })
}
export function stopSynctofile(id) {
  return request({
    url: baseUrl + '/schedule/job/stop',
    method: 'get',
    params: {
      "id": id
    }
  })
}
export function executeBackup(id) {
  return request({
    url: baseUrl + '/schedule/backup/execute',
    method: 'get',
    params: {
      "id": id
    }
  })
}

export function exportSynctofile(query) {
  return request({
    url: baseUrl + '/schedule/synctofile/export',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}

export function getNextTime(query) {
  return request({
    url: baseUrl + '/schedule/job/getNextTime',
    method: 'get',
    params: query,
  })
}
