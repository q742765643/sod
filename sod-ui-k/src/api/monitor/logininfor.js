import request from '@/utils/request'
const baseUrl =process.env.VUE_APP_UCENTER_API

// 查询登录日志列表
export function list(query) {
  return request({
    url: baseUrl+'/monitor/logininfor/list',
    method: 'get',
    params: query
  })
}

// 删除登录日志
export function delLogininfor(infoId) {
  return request({
    url: baseUrl+'/monitor/logininfor/' + infoId,
    method: 'delete'
  })
}

// 清空登录日志
export function cleanLogininfor() {
  return request({
    url: baseUrl+'/monitor/logininfor/clean',
    method: 'delete'
  })
}

// 导出登录日志
export function exportLogininfor(query) {
  return request({
    url: baseUrl+'/monitor/logininfor/export',
    method: 'get',
    params: query
  })
}
