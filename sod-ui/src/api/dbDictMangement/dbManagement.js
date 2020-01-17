import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_DM;
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
