import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_DMWLEI;

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
export function exportTable(query) {
  return request({
    url: baseUrl + "/dm/tableExport/exportTable",
    method: "get",
    params: query
  });
}
// SQL导出
export function exportSQL(query) {
  return request({
    url: baseUrl + "/dm/tableExport/exportSQL",
    method: "get",
    params: query
  });
}
