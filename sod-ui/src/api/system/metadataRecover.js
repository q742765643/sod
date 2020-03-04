import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_SCHEDULE_CENTER_API


// 数据库ip
export function findDataBase() {
  return request({
    url: baseUrl + '/schedule/metaBackup/findDataBase',
    method: 'get',
  })
}
/* 元数据恢复 */
// 查询列表
export function getFileList(query) {
  return request({
    url: baseUrl + '/metaRecoverLog/getFileList',
    method: 'get',
    params: query
  })
}
/* 元数据恢复日志 */
