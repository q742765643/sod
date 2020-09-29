import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_DM;

// 数据用途列表
export function consistencyCheckList(query) {
  return request({
    url: baseUrl + "/dm/consistencyCheck/list",
    method: "get",
    params: query
  });
}
export function deleteById(query) {
  return request({
    url: baseUrl + "/dm/consistencyCheck/deleteById",
    method: "delete",
    params: query
  });
}

export function consistencyCheckSave(data) {
  return request({
    url: baseUrl + "/dm/consistencyCheck/save",
    method: "post",
    data: data
  });
}

export function getDatabaseName(query) {
  return request({
    url: baseUrl + "/dm/database/getDatabaseName",
    method: "get",
    params: query
  });
}

export function historyList(query) {
  return request({
    url: baseUrl + "/dm/consistencyCheck/historyList",
    method: "get",
    params: query
  });
}

// 批量删除
export function deleteByIds(query) {
  return request({
    url: baseUrl + "/dm/consistencyCheck/deleteByIds/" + query,
    method: "delete",
  });
}

export function exportTable(query) {
  return request({
    url: baseUrl + '/dm/consistencyCheck/downloadDfcheckFile',
    method: 'post',
    data: query,
    responseType: "arraybuffer"
  })
}

export function downHistoryDfcheckFile(query) {
  return request({
    url: baseUrl + '/dm/consistencyCheck/downHistoryDfcheckFile',
    method: 'post',
    data: query,
    responseType: "arraybuffer"
  })
}


export function updateEleInfo(data) {
  return request({
    url: baseUrl + '/dm/consistencyCheck/updateEleInfo',
    method: 'post',
    data: data,
  })
}
