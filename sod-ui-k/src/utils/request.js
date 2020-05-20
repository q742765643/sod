import axios from 'axios'
import {
  Notification,
  MessageBox,
  Message
} from 'element-ui'
import store from '@/store'
import {
  getToken,
  createSign
} from '@/utils/auth'
import {
  Encrypt
} from '@/utils/htencrypt'
import {
  rsaencrypt
} from '@/utils/rsaencrypt'
import Cookies from 'js-cookie'
import router from '@/router'

const TokenKey = 'Admin-Token'
const uuid = require('uuid/v4')
axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  //baseURL: process.env.VUE_APP_BASE_API,
  // 超时
  // timeout: 30000
})
// request拦截器
service.interceptors.request.use(
  config => {
    // 判断是否从portal来的
    if (config.url.indexOf("interfaceId") > -1) {
      return config;
    }
    if (getToken()) {
      config.headers['Authorization'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    } else {
      config.headers['Authorization'] = Cookies.get(TokenKey)
    }

    let data = ""
    let ajax = config.headers['X-Requested-With'];
    let headers = config.headers['Content-Type'];
    if ("multipart/form-data" == headers) {
      return config;
    }
    if (typeof config.params != 'undefined') {
      data = Encrypt(JSON.stringify(config.params))
      const param = {
        "timestamp": new Date().getTime(),
        "nonce": uuid(),
        "data": JSON.stringify(config.params)
      }
      let sign = createSign(param)
      param.sign = sign
      config.params = param

    } else if (typeof config.data != 'undefined') {
      //data = Encrypt(JSON.stringify(config.data))
      /* const param = {
       "sign": "111111",
       "data": data
       }*/
      const param = {
        "timestamp": new Date().getTime(),
        "nonce": uuid(),
        "data": JSON.stringify(config.data)
      }
      let sign = createSign(param)
      param.sign = sign
      config.data = param
    } else {
      const param = {
        "timestamp": new Date().getTime(),
        "nonce": uuid(),
      }
      let sign = createSign(param)
      param.sign = sign
      config.params = param
    }
    return config
  },
  error => {
    console.log(error)
    Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(res => {
    // console.log(res)
    if (res.data instanceof ArrayBuffer) {
      return res;
    }
    const code = res.data.code;
    const responseURL = res.request.responseURL;
    if (code === 401) {
      MessageBox.confirm(
        '登录状态已过期，您可以继续留在该页面，或者重新登录',
        '系统提示', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        localStorage.clear();
        // window.location.href = "http://10.40.79.18:8080/dist/index.html";
        store.dispatch('LogOut').then(() => {
          location.reload() // 为了重新实例化vue-router对象 避免bug
        })
      })
      //console.log(this.$route.path)
    } else if (code == undefined) {
      return res.data
    } else if (code !== 200) {
      if (responseURL.indexOf('/comMetaData/syncDataNow') != -1 || responseURL.indexOf('/dm/dataAuthorityApply/updateRecordCheck') != -1 || responseURL.indexOf('/dm/dataAuthorityApply/updateRecordCheckCancel') != -1) {
        return res.data
      }
      console.log(res.data.msg)
      Notification.error({
        dangerouslyUseHTMLString: true,
        message: res.data.msg
      })
      return Promise.reject('error')
    } else {
      return res.data
    }
  },
  error => {
    console.log('err' + error)
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
