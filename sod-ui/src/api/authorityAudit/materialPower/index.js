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

// 审核拒绝
export function updateRecordCheck(data) {
  return request({
    url: baseUrl + '/dm/dataAuthorityApply/updateRecordCheck',
    method: 'PUT',
    params: data
  })
}
