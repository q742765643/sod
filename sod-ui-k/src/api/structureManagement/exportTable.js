import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_SYSTEM;

// 数据用途列表
export function logicDefineList(query) {
  return request({
    url: baseUrl + "/dm/tableExport/logicDefineList",
    method: "get",
    params: query
  });
}
// 数据库列表
export function databaseList(query) {
  return request({
    url: baseUrl + "/dm/tableExport/databaseList",
    method: "get",
    params: query
  });
}
// 资料分类树
export function dataTree(query) {
  return request({
    url: baseUrl + "/dm/tableExport/dataTree",
    method: "get",
    params: query
  });
}

// 表结构导出-完整版
export function exportTable(data) {
  return request({
    url: baseUrl + "/dm/tableExport/exportTable",
    method: "post",
    data: data
  });
}
// SQL导出
export function exportSQL(data) {
  return request({
    url: baseUrl + "/dm/tableExport/exportSQL",
    method: "post",
    data: data
  });
}
// 简版
export function exportTableSimple(data) {
  return request({
    url: baseUrl + "/dm/tableExport/exportTableSimple",
    method: "post",
    data: data
  });
}

export function downloadTable(query) {
  return request({
    url: baseUrl + '/api/com/downloadByPath',
    method: 'get',
    params: query,
    responseType: "arraybuffer"
  })
}
