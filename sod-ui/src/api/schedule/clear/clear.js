import request from '@/utils/request'
const baseUrl =process.env.VUE_APP_SCHEDULE_CENTER_API

export function listClear(query) {
  return request({
    url: baseUrl+'/schedule/clear/list',
    method: 'get',
    params: query
  })
}

export function getClear(clearId) {
  return request({
    url: baseUrl+'/schedule/clear/' + clearId,
    method: 'get'
  })
}

export function addClear(data) {
  return request({
    url: baseUrl+'/schedule/clear',
    method: 'post',
    data: data
  })
}

export function updateClear(data) {
  return request({
    url: baseUrl+'/schedule/clear',
    method: 'put',
    data: data
  })
}

export function delClear(clearIds) {
  return request({
    url: baseUrl+'/schedule/clear/' + clearIds,
    method: 'delete'
  })
}

export function findAllDataBase() {
  return request({
    url: baseUrl+'/schedule/job/findAllDataBase',
    method: 'get'
  })
}
export function getByDatabaseId(databaseId) {
  return request({
    url: baseUrl+'/schedule/job/getByDatabaseId/'+databaseId,
    method: 'get'
  })
}
export function getByDatabaseIdAndClassId(databaseId,dataClassId) {
  return request({
    url: baseUrl+'/schedule/job/getByDatabaseIdAndClassId',
    method: 'get',
    async: true,
    params: {
      "databaseId":databaseId,
      "dataClassId":dataClassId
    }
  })
}


