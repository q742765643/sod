import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;
const baseUrl1 = process.env.VUE_APP_UCENTER_API

// 查询表格
export function cldbApplicationAll(query) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/list',
    method: 'get',
    params: query
  })
}
// 查询反馈信息表格
export function feedbackAll(query) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/listFeedback',
    method: 'get',
    params: query
  })
}
//修改反馈信息状态
export function updateFeedbackStatus(query) {
  return request({
    url:baseUrl+'/dm/yunDatabaseApply/updateFeedbackStatus',
    method: 'get',
    params: query
  })
}
//修改反馈信息
export function updateFeedback(data) {
  return request({
    url:baseUrl+'/dm/yunDatabaseApply/editFeedback',
    method: 'put',
    data: data
  })
}
//根据反馈信息ID查询
export function getByFeedId(query) {
  return request({
    url:baseUrl+'/dm/yunDatabaseApply/getByFeedId',
    method: 'get',
    params: query
  })
}
//反馈信息删除
export function deleteFeedId(data) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/deleteByFeedId',
    method: 'delete',
    params: data
  })
}
export function deleteFeedbackId(data) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/deleteByFeedbackId',
    method: 'delete',
    params: data
  })
}
//根据用户名查询
export function getUserId(query) {
  return request({
    url:baseUrl+'/dm/yunDatabaseApply/getByUserId',
    method: 'get',
    params: query
  })
}
//
export function getDictDataByType(query) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/getDictDataByType/' + query,
    method: 'get',
  })
}
// 删除表格
export function deleteExamine(data) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/deleteById',
    method: 'delete',
    params: data
  })
}
export function deleteLogData(data) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/deleteByLogId',
    method: 'delete',
    params: data
  })
}
// 添加表格
export function saveCldb(data) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/save',
    method: 'post',
    data: data
  })
}
// 添加变更
export function saveLog(data) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/saveLog',
    method: 'post',
    data: data
  })
}
export function getLogId(query) {
  return request({
    url:baseUrl+'/dm/yunDatabaseApply/getByLogId',
    method: 'get',
    params: query
  })

}
// 编辑
export function editCldb(data) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/edit',
    method: 'put',
    data: data
  })
}

//中间件部署

export function deployNew(data) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/deployNew',
    method: 'post',
    data: data
  })
}
//中间件修改
export function editDeployNew(data) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/editDeployNew',
    method: 'post',
    data: data
  })
}
//中间件删除
export function deleteNew(data) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/deleteNew',
    method: 'post',
    data: data
  })
}
//节点信息
export function nodeDataNew(data) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/getNode',
    method: 'post',
    data: data
  })
}

//容器信息
export function containerDataNew(data) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/getContainers',
    method: 'post',
    data: data
  })
}
//运行日志

export function getJournalNew(data) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/getJournal',
    method: 'post',
    data: data
  })
}
//监控配置

export function getLinkInfo(data) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/getLink',
    method: 'post',
    data: data
  })
}
//监控配置

export function getConfigNew(data) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/getConfigNew',
    method: 'post',
    data: data
  })
}
//修改监控配置

export function editConfigNew(data) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/editConfigNew',
    method: 'post',
    data: data
  })
}
//监控数据
export function getMonitorDataNew(data) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/getMonitorDataNew',
    method: 'post',
    data: data
  })
}
//模板信息
export function getTemp(data) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/getTemp',
    method: 'post',
    data: data
  })
}
// 查详情
export function getById(query) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/getById',
    method: 'get',
    params: query
  })
}

// 下载
export function exprotFile(query) {
  return request({
    url: baseUrl + '/dm/yunDatabaseApply/download',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}

// 模板下载
export function exportDemo() {
  return request({
    url: baseUrl + '/api/dbfile/downloadFile',
    method: 'get',
    params: {
      name: "yundatabase-application"
    },
    responseType: "arraybuffer"
  })
}
//获取业务用户信息
export function gatBiz(query) {
  return request({
    url: baseUrl1 + '/system/user/getUserInfo',
    method: 'get',
    params: query
  })
}
// 获取所有用户
export function getUserByType(query) {
  return request({
    url: baseUrl + '/system/user/getUserByType',
    method: 'get',
    params: query
  })
}
