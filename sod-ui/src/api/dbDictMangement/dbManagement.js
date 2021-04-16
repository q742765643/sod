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
export function findBaseByPId(query) {
  return request({
    url: baseUrl + "/dm/database/findBaseByPId",
    method: "get",
    params: query
  });
}
// 新增
export function databaseDefineSave(data) {
  return request({
    url: baseUrl + "/dm/databaseDefine/save",
    method: "post",
    data: data
  });
}
// 查详情
export function databaseDefineGet(query) {
  return request({
    url: baseUrl + "/dm/databaseDefine/get",
    async: true,
    method: "get",
    params: query
  });
}

// 根据父id查询专题库列表
export function findByDatabaseDefineId(query) {
  return request({
    url: baseUrl + "/dm/database/findByDatabaseDefineId",
    method: "get",
    params: query
  });
}

// 多个删除
export function delByIds(query) {
  return request({
    url: baseUrl + "/dm/databaseDefine/delByIds",
    method: "delete",
    params: query
  });
}

export function conStatus(query) {
  return request({
    url: baseUrl + "/dm/databaseDefine/conStatus",
    method: "get",
    params: query
  });
}

export function connStatus(data) {
  return request({
    url: baseUrl + "/dm/databaseDefine/connStatus",
    method: "post",
    data: data
  });
}

// 导出
export function exportTable(query) {
  return request({
    url: baseUrl + "/dm/databaseDefine/export",
    method: "get",
    params: query,
    responseType: "arraybuffer"
  });
}
