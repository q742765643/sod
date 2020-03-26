import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_UCENTER_API

// 查询列表
export function getDictList() {
  return request({
    url: baseUrl + '/system/dict/data/dictType/sys_mmd_sync_table',
    method: 'get',
  })
}
