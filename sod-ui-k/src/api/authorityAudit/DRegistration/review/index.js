import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_DM;
// 存储资料审核
export function getDotById(data) {
  return request({
    url: baseUrl + "/dm/newdataApply/getDotById",
    method: "get",
    params: data
  });
}
// 新增申请表
export function addApply(data) {
  return request({
    url: baseUrl + "/dm/dataTable/addApply",
    method: "post",
    params: data
  });
}
