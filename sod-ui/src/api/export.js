import request from '@/utils/request'

// 获取资料分类统计数量
export function download(url, data) {
  return request({
    url: url,
    method: 'get',
    responseType: 'blob',
    data: data
  })
}
