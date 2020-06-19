import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM

// 查询列表
export function queryList(query) {
  return request({
    url: baseUrl + '/dm/dataAuthorityApply/list',
    method: 'get',
    params: query
  })
}


// 根据申请id查询申请信息
export function getApplyInfoById(query) {
  return request({
    url: baseUrl + '/dm/dataAuthorityApply/getApplyInfoById',
    method: 'get',
    params: query
  })
}

// 根据申请id查询申请资料信息
export function getRecordByApplyId(query) {
  return request({
    url: baseUrl + '/dm/dataAuthorityApply/getRecordByApplyId',
    method: 'get',
    params: query
  })
}

// 审核授权
export function updateRecordCheck(data1) {
  return request({
    url: baseUrl + '/dm/dataAuthorityApply/updateRecordCheck',
    method: 'PUT',
    data: data1
  })
}

// 审核拒绝
export function updateRecordCheckCancel(data1) {
  return request({
    url: baseUrl + '/dm/dataAuthorityApply/updateRecordCheckCancel',
    method: 'PUT',
    data: data1
  })
}
// 查询
export function getReadAuthority(query) {
  return request({
    url: baseUrl + '/dm/dataAuthorityApply/getReadAuthority',
    method: 'get',
    params: query
  })
}

// 修改
export function updateReadAuthority(data) {
  return request({
    url: baseUrl + '/dm/dataAuthorityApply/updateReadAuthority',
    method: 'post',
    data: data
  })
}
