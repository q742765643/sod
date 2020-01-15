import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_SERVER_API;
// 查询表格
export function defineList(query) {
  return request({
    url: baseUrl + "/queryAllDataEle",
    method: "get",
    params: query
  });
}
