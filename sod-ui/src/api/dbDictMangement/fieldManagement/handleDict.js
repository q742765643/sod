import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_DB_API;
//新增字典数据
export function addTableData(query) {
  return request({
    url: baseUrl + "/restApi/dicmgn/addType",
    method: "post",
    params: query
  });
}
