import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_UCENTER_API

// 查询操作日志列表
export function list(query) {
  return request({
    url: baseUrl + '/monitor/operlog/list',
    method: 'get',
    params: query
  })
}

// 删除操作日志
export function delOperlog(operId) {
  return request({
    url: baseUrl + '/monitor/operlog/' + operId,
    method: 'delete'
  })
}

// 清空操作日志
export function cleanOperlog() {
  return request({
    url: baseUrl + '/monitor/operlog/clean',
    method: 'delete'
  })
}

// 导出操作日志
export function exportOperlog(query) {
  return request({
    url: baseUrl + '/monitor/operlog/export',
    method: 'get',
    params: query
  })
}
