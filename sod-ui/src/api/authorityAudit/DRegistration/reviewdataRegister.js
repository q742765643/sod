import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_DM;
// 存储资料审核
export function updateStatus(query) {
  return request({
    url: baseUrl + "/dm/newdataApply/updateStatus",
    method: "post",
    params: query
  });
}
