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

/**
 * 
 * 导出方法
 */
export function testExport(fileurls) {
  if (fileurls == "" || typeof fileurls == "unedfined") {
    return;
  }

  var downiframe = document.querySelector("#downiframe");
  if (downiframe) {
    downiframe.parentNode.removeChild(downiframe);
  }
  try {
    var elemIF = document.createElement("iframe");
    elemIF.id = "downiframe";
    elemIF.src = fileurls;
    elemIF.style.display = "none";
    document.body.appendChild(elemIF);
  } catch (e) {
    console.dir("error");
  }
}
// 时间格式化
export function formatTime(selectTime, type) {
  let finalTime;
  selectTime = new Date(selectTime);
  const year = selectTime.getFullYear();
  let month = selectTime.getMonth() * 1 + 1;

  if (month < 10) {
    month = "0" + month;
  }
  let date = selectTime.getDate();
  if (date < 10) {
    date = "0" + date;
  }
  //返回时间格式为yyyy-mm-dd
  if (type === "time") {
    //时间
    let hour = selectTime.getHours();
    if (hour < 10) {
      hour = "0" + hour;
    }
    let minute = selectTime.getMinutes();
    if (minute < 10) {
      minute = "0" + minute;
    }
    let second = selectTime.getSeconds();
    if (second < 10) {
      second = "0" + second;
    }
    finalTime =
      year +
      "-" +
      month +
      "-" +
      date +
      " " +
      hour +
      ":" +
      minute +
      ":" +
      second;
    return finalTime;
  } else {
    return year + "-" + month + "-" + date;
  }
}
//获取url 参数
export function getUrlParamVal(param_name) {
      var reg = new RegExp("(^|&)" + param_name + "=([^&]*)(&|$)");
      var r = window.location.search.substr(1).match(reg);
      if (r != null) return unescape(r[2]);
      return null;
    }
