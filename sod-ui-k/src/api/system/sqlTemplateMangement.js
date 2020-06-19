import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_SYSTEM;
// 获取数据分类
export function getAllSqlList() {
  return request({
    url: baseUrl + "/sqlTemplate/findAll",
    method: "get"
  });
}

//新增sql模板
export function saveSqlTemplate(query) {
  return request({
    url: baseUrl + "/sqlTemplate/add",
    method: "post",
    params: query
  });
}
//查看是否已建立数据库
export function isAlreadyTem(query) {
  return request({
    url: baseUrl + "/sqlTemplate/checkSqlTemplate",
    method: "get",
    params: query
  });
}
//删除sql模板
export function delTemplate(query) {
  return request({
    url: baseUrl + "/sqlTemplate/del",
    method: "delete",
    params: query
  });
}
//编辑sql模板
export function editTemplate(query) {
  return request({
    url: baseUrl + "/sqlTemplate/edit",
    method: "put",
    params: query
  });
}
