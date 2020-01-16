import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_DB_API;
// 查询表格
export function getpage(query) {
  console.log(query);
  return request({
    url: baseUrl + "/api/dictionary/findById",
    method: "get",
    params: query
  });
}
