import Cookies from 'js-cookie'
import md5 from 'js-md5'

const TokenKey = 'Admin-Token'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function createSign(param) {
  let pro = Object.keys(param).sort()
  let result = '';
  for (let i = 0; i < pro.length; i++) {
    if (typeof param[pro[i]] != 'undefined') {
      result = result + pro[i] + "=" + param[pro[i]] + "&";
    }

  }

  let sign = md5(result).toUpperCase();
  return sign

}
