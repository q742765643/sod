import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SCHEDULE_CENTER_API

export function syncList(query) {
  return request({
    url: baseUrl + '/schedule/sync/list',
    method: 'get',
    params: query
  })
}

export function listLog(query) {
  return request({
    url: baseUrl + '/schedule/sync/listLog',
    method: 'get',
    params: query
  })
}

export function getDictDataByType(type) {
  return request({
    url: baseUrl + '/schedule/sync/getDictDataByType/' + type,
    method: 'get',
  })
}

export function syncDatabaseDetail(query) {
  return request({
    url: baseUrl + '/schedule/sync/syncDatabaseDetail',
    method: 'get',
    params: query
  })
}

export function syncTableByDatabaseId(query) {
  return request({
    url: baseUrl + '/schedule/sync/syncTableByDatabaseId',
    method: 'get',
    params: query
  })
}

export function syncColumnByTableId(query) {
  return request({
    url: baseUrl + '/schedule/sync/syncColumnByTableId',
    method: 'get',
    params: query
  })
}


export function syncSaveUpdate(data) {
  return request({
    url: baseUrl + '/schedule/sync/syncSaveUpdate',
    method: 'post',
    data: data
  })
}
export function delSync(id) {
  return request({
    url: baseUrl+'/schedule/sync/deleteSync/'+id,
    method: 'delete'
  })
}

export function getSyncInfo(id) {
  return request({
    url: baseUrl+'/schedule/sync/getSyncById/' + id,
    method: 'get'
  })
}
