import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SCHEDULE_CENTER_API

export function listClear(query) {
  return request({
    url: baseUrl + '/schedule/clear/list',
    method: 'get',
    params: query
  })
}

export function getClear(clearId) {
  return request({
    url: baseUrl + '/schedule/clear/' + clearId,
    method: 'get'
  })
}

export function addClear(data) {
  return request({
    url: baseUrl + '/schedule/clear',
    method: 'post',
    data: data
  })
}

export function updateClear(data) {
  return request({
    url: baseUrl + '/schedule/clear',
    method: 'put',
    data: data
  })
}

export function delClear(clearIds) {
  return request({
    url: baseUrl + '/schedule/clear/' + clearIds,
    method: 'delete'
  })
}

export function findAllDataBase() {
  return request({
    url: baseUrl + '/schedule/clear/findDatabase',
    method: 'get'
  })
}
export function getByDatabaseId(databaseId, dataClassId) {
  return request({
    url: baseUrl + '/schedule/clear/findDataClassId',
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

export function startClear(id) {
  return request({
    url: baseUrl + '/schedule/job/startById',
    method: 'get',
    params: {
      "id": id
    }
  })
}
export function stopClear(id) {
  return request({
    url: baseUrl + '/schedule/job/stop',
    method: 'get',
    params: {
      "id": id
    }
  })
}
export function executeClear(id) {
  return request({
    url: baseUrl + '/schedule/job/execute',
    method: 'get',
    params: {
      "id": id
    }
  })
}

export function exportTable(query) {
  return request({
    url: baseUrl + '/schedule/clear/export',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}
