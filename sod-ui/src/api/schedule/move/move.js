import request from '@/utils/request'
const baseUrl =process.env.VUE_APP_SCHEDULE_CENTER_API

export function listMove(query) {
  return request({
    url: baseUrl+'/schedule/move/list',
    method: 'get',
    params: query
  })
}

export function getMove(moveId) {
  return request({
    url: baseUrl+'/schedule/move/' + moveId,
    method: 'get'
  })
}

export function addMove(data) {
  return request({
    url: baseUrl+'/schedule/move',
    method: 'post',
    data: data
  })
}

export function updateMove(data) {
  return request({
    url: baseUrl+'/schedule/move',
    method: 'put',
    data: data
  })
}

export function delMove(moveIds) {
  return request({
    url: baseUrl+'/schedule/move/' + moveIds,
    method: 'delete'
  })
}

export function findAllDataBase() {
  return request({
    url: baseUrl+'/schedule/move/findDatabase',
    method: 'get'
  })
}
export function getByDatabaseId(databaseId,dataClassId) {
  return request({
    url: baseUrl+'/schedule/move/findDataClassId',
    method: 'get',
    params: {
      "databaseId":databaseId,
      "dataClassId":dataClassId
    }
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
