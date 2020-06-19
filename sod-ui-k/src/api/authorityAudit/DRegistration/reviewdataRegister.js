import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_DM;
// 存储资料审核
export function updateStatus(data) {
  return request({
    url: baseUrl + "/dm/newdataApply/updateStatus",
    method: "post",
    data: data
  });
}
// 查询所有用途
export function logicDefineAll(query) {
  return request({
    url: baseUrl + "/dm/logicDefine/all",
    method: "get",
    params: query
  });
}
