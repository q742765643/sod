import request from '@/utils/request'
const baseUrl =process.env.VUE_APP_DB_API

// 查询sql模板
export function initSqlList() {
  return request({
    url: baseUrl+'/restApi/sqlTemplate/findAll',
    method: 'get'
  })
}

//获取数据库类型
export function getDBType() {
  return request({
    url: baseUrl+'/restApi/sqlTemplate/findAll',
    method: 'get'
  })
}

//删除sql模板
export function delSqlTem() {
  return request({
    url: baseUrl+'/restApi/sqlTemplate/del',
    method: 'delete'
  })
}

//新增sql模板
export function addSqlTem() {
  return request({
    url: baseUrl+'/restApi/sqlTemplate/add',
    method: 'post'
  })
}

//根据id查询模板
export function getSqlTemById() {
  return request({
    url: baseUrl+'/restApi/sqlTemplate/findByPk',
    method: 'get'
  })
}

//编辑sql模板
export function editSqlTem() {
  return request({
    url: baseUrl+'/restApi/sqlTemplate/edit',
    method: 'put'
  })
}
