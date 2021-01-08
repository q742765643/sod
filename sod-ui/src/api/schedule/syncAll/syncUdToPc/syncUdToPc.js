import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SCHEDULE_CENTER_API

export function listSyncudtopc(query) {

  return request({
    url: baseUrl + '/schedule/syncudtopc/list',
    method: 'get',
    params: query
  })
}

export function getSyncudtopc(syncUdToPcId) {
  return request({
    url: baseUrl + '/schedule/syncudtopc/' + syncUdToPcId,
    method: 'get'
  })
}

export function addSyncudtopc(data) {
  return request({
    url: baseUrl + '/schedule/syncudtopc',
    method: 'post',
    data: data
  })
}

export function updateSyncudtopc(data) {
  return request({
    url: baseUrl + '/schedule/syncudtopc',
    method: 'put',
    data: data
  })
}

export function delSyncudtopc(syncUdToPcIds) {
  return request({
    url: baseUrl + '/schedule/syncudtopc/' + syncUdToPcIds,
    method: 'delete'
  })
}
export function findAllDataBase() {
  return request({
    url: baseUrl + '/schedule/syncudtopc/findDatabase',
    method: 'get'
  })
}
export function getByDatabaseId(databaseId, dataClassId) {
  return request({
    url: baseUrl + '/schedule/syncudtopc/findDataClassId',
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
export function startSyncudtopc(id) {
  return request({
    url: baseUrl + '/schedule/job/startById',
    method: 'get',
    params: {
      "id": id
    }
  })
}
export function stopSyncudtopc(id) {
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

export function exportSyncudtopc(query) {
  return request({
    url: baseUrl + '/schedule/syncudtopc/export',
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
