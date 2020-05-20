import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_DM;
// 获取数据分类
export function getDataClassify() {
  return request({
    url: baseUrl + "/dm/newdataApply/getDataTypeList",
    method: "get"
  });
}

//查询注册数据
export function getDataTable(query) {
  return request({
    url: baseUrl + "/dm/newdataApply/list",
    method: "get",
    params: query
  });
}


// 根据专题ID查数据库
export function databaseGet(query) {
  return request({
    url: baseUrl + "/dm/database/get",
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
