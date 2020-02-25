/**
 * 
 * 10以内的中文
 */
export function chineseLengthTenValidation(str) {
  const reg = /^[\u4E00-\u9FA5]{1,10}$/
  return reg.test(str)
}
/**
 * 
 * 手机号码
 */
export function telphoneNumValidation(str) {
  const reg = /^1[34578]\d{9}$/
  return reg.test(str)
}
/**
 * 
 * 端口号
 */
export function portNumValidation(str) {
  const reg = /^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/
  return reg.test(str)
}
/**
 * 
 * ip地址
 */
export function ipUrlValidation(str) {
  const reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
  return reg.test(str)
}
/**
 * 
 * ip地址 IP段样例:192.168.1.%
 */
export function ipUrlValidation2(str) {
  const reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.%$/
  return reg.test(str)
}
/**
 * 
 * 数据库账号不能以数字开头
 */
export function unstartnumValidation(str) {
  const reg = /^([0-9][a-zA-Z0-9]*[\u4e00-\u9fa5]*)$/
  return reg.test(str)
}
/**
 * 
 * 密码 必须以英文数字组成
 */
export function englishAndNumValidation(str) {
  const reg = /[0-9]+[a-zA-Z]+[0-9a-zA-Z]*|[a-zA-Z]+[0-9]+[0-9a-zA-Z]*$/
  return reg.test(str)
}
/**
 * 
 * 不允许输入小写字母和中文，且需以大写字母开头
 */
export function codeVer(str) {
  const reg = /^[_A-Z][^a-z\u4e00-\u9fa5]*$/
  return reg.test(str)
}
/**
 * 
 * 验证由中文组成：
 */
export function chinese(str) {
  const reg = /^[()\u4e00-\u9fa5]+$/
  return reg.test(str)
}
/**
 * 
 * 验证由大于0正整数组成：
 */
export function num(info) {
  const reg = /^[0-9]+(\.\d+)?$/;;
  return reg.test(info);
}
/**
 * @param {string} str
 * @returns {Boolean}
 */
export function isString(str) {
  if (typeof str === 'string' || str instanceof String) {
    return true
  }
  return false
}

/**
 * @param {Array} arg
 * @returns {Boolean}
 */
export function isArray(arg) {
  if (typeof Array.isArray === 'undefined') {
    return Object.prototype.toString.call(arg) === '[object Array]'
  }
  return Array.isArray(arg)
}
//格式化时间
// 格式化日期，如月、日、时、分、秒保证为2位数
export function formatNumber(n) {
  n = n.toString();
  return n[1] ? n : "0" + n;
}
// 参数number为毫秒时间戳，format为需要转换成的日期格式
export function formatTime(number, format) {
  let time = new Date(number);
  let newArr = [];
  let formatArr = ["Y", "M", "D", "h", "m", "s"];
  newArr.push(time.getFullYear());
  newArr.push(formatNumber(time.getMonth() + 1));
  newArr.push(formatNumber(time.getDate()));

  newArr.push(formatNumber(time.getHours()));
  newArr.push(formatNumber(time.getMinutes()));
  newArr.push(formatNumber(time.getSeconds()));

  for (let i in newArr) {
    format = format.replace(formatArr[i], newArr[i]);
  }
  return format;
}
