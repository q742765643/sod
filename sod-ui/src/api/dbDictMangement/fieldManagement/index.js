import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_DB_API;
// 查询字典分组
export function findMenu(query) {
  return request({
    url: baseUrl + "/restApi/dicmgn/findMenu",
    method: "get",
    params: query
  });
}

//查询表格
export function getTableData(query) {
  return request({
    url: baseUrl + "/restApi/dicmgn/page",
    method: "get",
    params: query
  });
}
//查询表格
export function deleteByIds(query) {
  return request({
    url: baseUrl + "/restApi/dicmgn/deleteByIds",
    method: "get",
    params: query
  });
}
