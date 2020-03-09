import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_SYSTEM;
// 查询表格
export function defineList(query) {
  return request({
    url: baseUrl + "/system/serviceCodeManagement/list",
    method: "get",
    params: query
  });
}
//删除服务代码数据
export function deleteCode(query) {
  return request({
    url: baseUrl + "/system/api/apiDataEleDefine/delDataEleById",
    method: "get",
    params: query
  });
}
