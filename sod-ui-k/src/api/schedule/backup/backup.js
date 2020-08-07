import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SCHEDULE_CENTER_API

export function listBackup(query) {

  return request({
    url: baseUrl + '/schedule/backup/list',
    method: 'get',
    params: query
  })
}

export function getBackup(backupId) {
  return request({
    url: baseUrl + '/schedule/backup/' + backupId,
    method: 'get'
  })
}

export function addBackup(data) {
  return request({
    url: baseUrl + '/schedule/backup',
    method: 'post',
    data: data
  })
}

export function updateBackup(data) {
  return request({
    url: baseUrl + '/schedule/backup',
    method: 'put',
    data: data
  })
}

export function delBackup(backupIds) {
  return request({
    url: baseUrl + '/schedule/backup/' + backupIds,
    method: 'delete'
  })
}
export function findAllDataBase() {
  return request({
    url: baseUrl + '/schedule/backup/findDatabase',
    method: 'get'
  })
}
export function getByDatabaseId(databaseId, dataClassId) {
  return request({
    url: baseUrl + '/schedule/backup/findDataClassId',
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
export function startBackup(id) {
  return request({
    url: baseUrl + '/schedule/job/startById',
    method: 'get',
    params: {
      "id": id
    }
  })
}
export function stopBackup(id) {
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
    url: baseUrl + '/schedule/job/execute',
    method: 'get',
    params: {
      "id": id
    }
  })
}

export function exportBackup(query) {
  return request({
    url: baseUrl + '/schedule/backup/export',
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
