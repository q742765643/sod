import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_DM;

//查询注册数据
export function getDataTable(query) {
  return request({
    url: baseUrl + "/dm/dataclassapply/list",
    method: "get",
    params: query
  });
}
// 删除
export function deleteById(query) {
  return request({
    url: baseUrl + "/dm/newdataApply/deleteById",
    method: "delete",
    params: query
  });
}

// 删除
export function review(data) {
  return request({
    url: baseUrl + "/dm/dataclassapply/review",
    method: "post",
    data: data
  });
}
