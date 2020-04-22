import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_DM;
// 存储资料审核
export function getDotById(data) {
  return request({
    url: baseUrl + "/dm/newdataApply/getDotById",
    method: "post",
    params: data
  });
}
