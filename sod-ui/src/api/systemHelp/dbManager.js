import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;
// 下载
export function downloadFile(query) {
  return request({
    url: baseUrl + '/api/dbfile/downloadFile',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}
