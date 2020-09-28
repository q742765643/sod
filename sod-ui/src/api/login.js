import request from '@/utils/request'
import {
  Encrypt
} from '@/utils/htencrypt'
const baseUrl = process.env.VUE_APP_UCENTER_API
// 登录方法
export function login(username, password, code, uuid) {
  password = Encrypt(password)

  const data = {
    username,
    password,
    code,
    uuid
  }
  return request({
    url: baseUrl + '/login',
    method: 'post',
    params: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: baseUrl + '/getInfo',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    url: baseUrl + '/logout',
    method: 'post'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: baseUrl + '/captchaImage',
    method: 'get'
  })
}

// 第3方登录校验token
export function checkToken(token) {
  return request({
    url: baseUrl + '/checkToken/' + token,
    method: 'get'
  })
}


export function getTokenPortal(query) {
  return request({
    url: baseUrl + '/getToken' + query,
    method: 'get',
  })
}
