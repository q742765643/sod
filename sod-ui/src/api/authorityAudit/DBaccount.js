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

export function addBzi(data) {
  return request({
    url: baseUrl + '/dm/databaseUser/addBzi',
    method: 'post',
    data: data
  })
}
// 数据库列表
export function databaseList(query) {
  return request({
    url: baseUrl + '/dm/databaseDefine/getCanShowDatabaseDefineList',
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

// 导出
export function download(query) {
  return request({
    url: baseUrl + '/dm/databaseUser/exportData',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
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


// 模板下载
export function demoDownload() {
  return request({
    url: baseUrl + '/api/dbfile/downloadFile',
    method: 'get',
    params: {
      name: "databaseuser-application"
    },
    responseType: "arraybuffer"
  })
}


// 根据ID查询 从业务用户审核来的
export function getBizDatabaseUser(query) {
  return request({
    url: baseUrl + '/dm/databaseUser/getBizDatabaseUser',
    async: true,
    method: 'get',
    params: query
  })
}
