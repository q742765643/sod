import request from '@/utils/request'
const baseUrl = process.env.VUE_APP_DM;
// 新增表格
export function addTable(data) {
  return request({
    url: baseUrl + '/dm/databaseUser/add',
    method: 'post',
    data: data
  })
}

// 数据库列表
export function databaseList(query) {
  return request({
    url: baseUrl + '/dm/databaseDefine/all',
    method: 'get',
    params: query
  })
}

// 表格列表
export function databaseUserAll(query) {
  return request({
    url: baseUrl + '/dm/databaseUser/all',
    method: 'get',
    params: query
  })
}


// 申请上传文件接口
export function upload(data) {
  return request({
    url: baseUrl + '/dm/databaseUser/api/databaseUser/upload',
    method: 'post',
    data: data
  })
}

// 判断UP账户是否已存在
export function ifUPExist(query) {
  return request({
    url: baseUrl + '/dm/databaseUser/ifUPExist',
    async: true,
    method: 'get',
    params: query
  })
}

// 删除
export function deleteList(query) {
  return request({
    url: baseUrl + '/dm/databaseUser/delete',
    async: true,
    method: 'delete',
    params: query
  })
}

// 根据ID查询
export function getById(query) {
  return request({
    url: baseUrl + '/dm/databaseUser/getById',
    async: true,
    method: 'get',
    params: query
  })
}

// 下载
export function download(query) {
  return request({
    url: baseUrl + '/dm/databaseUser/download',
    async: true,
    method: 'get',
    params: query
  })
}

// 审核
export function update(data) {
  return request({
    url: baseUrl + '/dm/databaseUser/update',
    method: 'post',
    data: data
  })
}

// 导出
export function exportData(query) {
  return request({
    url: baseUrl + '/dm/databaseUser/exportData',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}
// 模板下载
export function demoDownload() {
  return request({
    url: baseUrl + '/dm/fileUpDown/download',
    method: 'get',
    params: {
      name: "databaseuser-application"
    },
    responseType: "arraybuffer"
  })
}
