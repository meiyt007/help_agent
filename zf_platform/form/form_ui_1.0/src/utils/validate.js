/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUsername(str) {
  const valid_map = ['admin', 'editor']
  return valid_map.indexOf(str.trim()) >= 0
}

/**
 * @param {string} url
 * @returns {Boolean}
 */
export function validURL(url) {
  const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return reg.test(url)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validLowerCase(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/**
 * 验证url
 * @param {string} str
 * @returns {Boolean}
 */
export function validateURL(rule, value, callback) {
  setTimeout(() => {
    const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
    const rsCheck = reg.test(value);
    if (!rsCheck) {
      callback(new Error('输入正确的URL地址'));
    } else {
      callback();
    }
  }, 100);
}

/**
 * 验证中文
 * @param {string} str
 * @returns {Boolean}
 */
export function validChinese(rule, value, callback) {
  setTimeout(() => {
    const re = /^[\u4e00-\u9fa5]+$/
    const rsCheck = re.test(value);
    if (!rsCheck) {
      callback(new Error('只能输入中文'));
    } else {
      callback();
    }
  }, 100);
}

/**
 * 验证正整数不包括0,(1-9位数)
 * @param {int} str
 * @returns {Boolean}
 */
export function validIntNoZero(rule, value, callback) {
  setTimeout(() => {
    if (!Number(value)) {
      callback(new Error('请输入正整数'));
    } else {
      const re = /^[1-9]{1,}[\d]*$/;
      const rsCheck = re.test(value);
      if (!rsCheck) {
        callback(new Error('请输入正整数'));
      } else {
        callback();
      }
    }
  }, 100);
}

/**
 * 验证正整数包括0,(0-9位数)
 * @param {int} str
 * @returns {Boolean}
 */
export function validIntInCludeZero(rule, value, callback) {
  setTimeout(() => {
    if ('0'!=value && !Number(value)) {
      callback(new Error('请输入非负整数'));
    } else {
      const re = /^[0-9]{1,}[\d]*$/;
      const rsCheck = re.test(value);
      if (!rsCheck) {
        callback(new Error('请输入非负整数'));
      } else {
        callback();
      }
    }
  }, 100);
}


/**
 * 验证纯数字（包含小数点）
 * @param {int} str
 * @returns {Boolean}
 */
export function validateNumber(rule, value, callback){
  let numberReg = /^\d+$|^\d+[.]?\d+$/
  if (value !== '') {
    if (!numberReg.test(value)) {
      callback(new Error('请输入数字'))
    } else {
      callback()
    }
  } else {
    callback(new Error('请输入数字'))
  }
}

/**
 * 验证纯数字（不包含小数点）
 * @param {int} str
 * @returns {Boolean}
 */
export function validateNumberNoPonint(rule, value, callback){
  let numberReg = /^[\+\-]?[0-9]\d*$/
  if (value !== '') {
    if (!numberReg.test(value)) {
      callback(new Error('请输入数字'))
    } else {
      callback()
    }
  } else {
    callback(new Error('请输入数字'))
  }
}

/**
 * 是否手机号码或者固话
 * @param {Str} str
 * @returns {Boolean}
 */
export function validatePhoneTwo(rule, value, callback) {
  const reg = /^((0\d{2,3}-\d{7,8})|(1[34578]\d{9}))$/;
  if (value == '' || value == undefined || value == null) {
    callback();
  } else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error("请输入正确的手机号码或者固话"));
    } else {
      callback();
    }
  }
}

/**
 * 验证密码
 * @param {Str} str
 * @returns {Boolean}
 */
export function validatePassword(rule, value, callback) {
  const reg = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@#*_]{8,16}$/;
  if (value == '' || value == undefined || value == null) {
    callback();
  } else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error("密码以字母开头，必须含有大小写字母和数字，可有特殊字符@#*_ ,长度8-16"));
    } else {
      callback();
    }
  }
}


/**
 * 验证合法字符
 * @param{string} str
 * @returns {Boolean}
 */
export function validateLegalStr(rule, value, callback){
  let reg = /^[A-Za-z0-9_-]+$/
  if (value !== '') {
    if (!reg.test(value)) {
      callback(new Error('请输入由字母、数字、“_”或“-”组成的字符'))
    } else {
      callback()
    }
  } else {
    callback(new Error('请输入由字母、数字、“_”或“-”组成的字符'))
  }
}

/**
 * 验证合法字符--并且不能有数字开头
 * @param{string} str
 * @returns {Boolean}
 */
export function validateLegalStrNoNumber(rule, value, callback){
  let reg = /^[^0-9][A-Za-z0-9_]+$/
  if (value !== '') {
    if (!reg.test(value)) {
      callback(new Error('请输入由字母、数字或下划线组成的非数字开头字符'))
    } else {
      callback()
    }
  } else {
    callback(new Error('请输入由字母、数字或下划线组成的非数字开头字符'))
  }
}

/**
 * 验证统一社会信用代码
 * 统一社会信用代码由18位数字或者大写字母组成，但是字母不包括 I、O、Z、S、V
 一共由五部分组成
 第一部分：登记管理部门代码1位 (数字或大写英文字母)
 第二部分：机构类别代码1位 (数字或大写英文字母)
 第三部分：登记管理机关行政区划码6位 (数字)
 第四部分：主体标识码（组织机构代码）9位 (数字或大写英文字母)
 第五部分：校验码1位 (数字或大写英文字母)
 * @param{string} str
 * @returns {Boolean}
 */
export function validateUniteCode(rule, value, callback){
  let reg = /^([0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}|[1-9]\d{14})$/
  if (value != null && value != '') {
    if (!reg.test(value)) {
      callback(new Error('请输入正确的统一社会信用代码'))
    } else {
      callback()
    }
  } else {
    callback()
  }
}


/**
 * 验证邮箱
 * @param{string} str
 * @returns {Boolean}
 */
export function validateEmail(rule, value, callback){
  let reg = /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/
  if (value !== '') {
    if (!reg.test(value)) {
      callback(new Error('请输入正确的邮箱！'))
    } else {
      callback()
    }
  } else {
    callback(new Error('邮箱不能为空！'))
  }
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validAlphabets(str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

/**
 * @param {string} email
 * @returns {Boolean}
 */
export function validEmail(email) {
  const reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return reg.test(email)
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

export function validIDCard(rule, value, callback) {
  //非空不在验证范围内
  if (!value) callback();
  let len = value.length,
    reg;
  if (len == 15) {
    reg = new RegExp(/^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$/);
  } else if (len == 18) {
    reg = new RegExp(/^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/);
  } else {
    callback(new Error('请输入正确身份证号码'));
  }
  let r = value.match(reg);
  if (r == null) {
    callback(new Error('请输入正确身份证号码'));
  }else{
    callback()
  }
}
