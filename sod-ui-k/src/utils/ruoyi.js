/**
 * 通用js方法封装处理
 * Copyright (c) 2019 ruoyi
 */

const baseURL = process.env.VUE_APP_DM
import fileDownload from 'js-file-download'
// 日期格式化
export function parseTime(time, pattern) {
  if (arguments.length === 0) {
    return null
  }
  const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
      time = parseInt(time)
    }
    if ((typeof time === 'number') && (time.toString().length === 10)) {
      time = time * 1000
    }
    date = new Date(time);
    // date.setHours(date.getHours() - 8);
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key]
    // Note: getDay() returns 0 on Sunday
    if (key === 'a') {
      return ['日', '一', '二', '三', '四', '五', '六'][value]
    }
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  })
  return time_str
}

// 表单重置
export function resetForm(refName) {
  if (this.$refs[refName] !== undefined) {
    this.$refs[refName].resetFields();
  }
}

// 添加日期范围
export function addDateRange(params, dateRange) {
  var search = params;
  var orderBy = {};
  if (search.params) {
    orderBy = search.params.orderBy;
  }
  if (null != dateRange) {
    search.params = {
      beginTime: dateRange[0],
      endTime: dateRange[1],
      orderBy: orderBy
    };
  }
  return search;
}

// 回显数据字典
export function selectDictLabel(datas, value) {
  var actions = [];
  Object.keys(datas).map((key) => {
    if (datas[key].dictValue == ('' + value)) {
      actions.push(datas[key].dictLabel);
      return false;
    }
  })
  return actions.join('');
}

// 通用下载方法
export function download(filePath) {
  window.location.href = baseURL + "/api/com/downloadByPath?filePath=" + filePath;
}
export function downloadfileCommon(res) {
  if (!res) {
    this.$message({
      type: "error",
      message: "没有文件，无需下载"
    });
    return;
  }
  // debugger

  console.log(res.headers['content-disposition']);
  if (res.headers['content-disposition']) {
    let filename = decodeURI(res.headers['content-disposition'].split(';')[1].split('=')[1]);
    console.log(filename);
    fileDownload(res.data, filename);
  } else {
    this.$message({
      type: "error",
      message: "没有文件，无需下载"
    });
  }

  /* console.log(222)
  let fileType = 'type: "application/octet-stream";charset=utf-8'
  if (Type == 'word') {
    fileType = 'type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document'
  }
  var blob = new Blob([res.data], {
    type: fileType
  }); //application/vnd.openxmlformats-officedocument.wordprocessingml.document这里表示doc类型
  var contentDisposition = res.headers['content-disposition']; //从response的headers中获取filename, 后端response.setHeader("Content-disposition", "attachment; filename=xxxx.docx") 设置的文件名;
  var patt = new RegExp("filename=([^;]+\\.[^\\.;]+);*");
  var result = patt.exec(contentDisposition);
  console.log(result)
  var filename = result[1];
  var downloadElement = document.createElement('a');
  var href = window.URL.createObjectURL(blob); //创建下载的链接
  var reg = /^["](.*)["]$/g;
  downloadElement.style.display = 'none';
  downloadElement.href = href;
  downloadElement.download = decodeURI(filename.replace(reg, "$1")); //下载后文件名
  document.body.appendChild(downloadElement);
  downloadElement.click(); //点击下载
  document.body.removeChild(downloadElement); //下载完成移除元素
  window.URL.revokeObjectURL(href); //释放掉blob对象 */

}
// 字符串格式化(%s )
export function sprintf(str) {
  var args = arguments,
    flag = true,
    i = 1;
  str = str.replace(/%s/g, function () {
    var arg = args[i++];
    if (typeof arg === 'undefined') {
      flag = false;
      return '';
    }
    return arg;
  });
  return flag ? str : '';
}
