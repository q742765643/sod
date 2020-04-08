import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_DM;

/* 数据库管理 */
// 查询表格
export function defineList(query) {
  return request({
    url: baseUrl + "/dm/databaseDefine/page",
    method: "get",
    params: query
  });
}
// 删除
export function delList(query) {
  return request({
    url: baseUrl + "/dm/databaseDefine/del",
    method: "delete",
    params: query
  });
}
/* 弹窗 */
/* 存储信息*/
// 数据库节点 查询
export function databaseNodesAll() {
  return request({
    url: baseUrl + "/dm/databaseNodes/all",
    method: "get",
  });
}
// 删除
export function databaseNodesDelete(query) {
  return request({
    url: baseUrl + "/dm/databaseNodes/del",
    method: "delete",
    params: query
  });
}
// 查详情
export function databaseNodesGetById(query) {
  return request({
    url: baseUrl + "/dm/databaseNodes/get",
    method: "get",
    params: query
  });
}
// 新增
export function databaseNodesSave(data) {
  return request({
    url: baseUrl + "/dm/databaseNodes/save",
    method: "post",
    data: data
  });
}
