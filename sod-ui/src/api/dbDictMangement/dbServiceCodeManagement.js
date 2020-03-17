import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_SYSTEM;
// 查询表格
export function defineList(query) {
  return request({
    url: baseUrl + "/system/serviceCodeManagement/list",
    method: "get",
    params: query
  });
}
// 添加
export function addCode(data) {
  return request({
    url: baseUrl + "/system/serviceCodeManagement/save",
    method: "post",
    data: data
  });
}
// 编辑
export function editCode(data) {
  return request({
    url: baseUrl + "/system/serviceCodeManagement/edit",
    method: "put",
    data: data
  });
}
// 根据ID查详情
export function getById(query) {
  return request({
    url: baseUrl + "/system/serviceCodeManagement/getById",
    method: "get",
    params: query
  });
}
//单个删除服务代码数据
export function deleteById(query) {
  return request({
    url: baseUrl + "/system/serviceCodeManagement/deleteById",
    method: "delete",
    params: query
  });
}
//多个删除服务代码数据
export function deleteByIds(id) {
  return request({
    url: baseUrl + "/system/serviceCodeManagement/deleteByIds/" + id,
    method: "delete",
  });
}
