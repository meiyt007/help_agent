import { ELEMENTTYPE } from './enum'
function Util () {

}

//格式化文件列表
Util.prototype.formatFileList = (fileList) => {
  let new_arr = []
  fileList.forEach(item => {
    new_arr.push({
      oid: item.oid,
      filename: item.filename
    })
  })
  return new_arr
}

//判断字段是否显示
Util.prototype.preFieldOidForShow = function (fillArr, preFactorOid, preFactorOptVal) {
  for (let i = 0; i < fillArr.length; i++) {
    if (fillArr[i].field.oid == preFactorOid) {
      if (fillArr[i].field.elementType == ELEMENTTYPE['CHECKBOX']) {
        if (fillArr[i].result.includes(preFactorOptVal)) {
          return true
        }
      }
      if (fillArr[i].result == preFactorOptVal) {
        return true
      }
    }
  }
  return false
}

/**将fillArr多维数组转一维数组 */
Util.prototype.formatFillArr = function (fillArr) {
  fillArr = Array.prototype.concat.apply([], fillArr)
  return fillArr
}

/**根据showType分组 */
Util.prototype.judgeShowType = function (fillArr) {
  let new_arr = [];
  let tr_arr = []
  fillArr.forEach(item => {
    if (item.field.showType == '0') {
      tr_arr.push(item)
      if (tr_arr.length == 2) {
        new_arr.push(tr_arr)
        tr_arr = []
      }
    } else {
      if (tr_arr.length > 0) {
        new_arr.push(tr_arr)
        tr_arr = []
      }
      tr_arr.push(item)
      new_arr.push(tr_arr)
      tr_arr = []
    }
  })

  if (tr_arr.length == 1) {
    new_arr.push(tr_arr)
  }

  return new_arr
}

/**判断数组是否为多维数组 */
Util.prototype.judgeTwodArray = function (arr) {
  const arrFirst = arr[0]
  if (arrFirst instanceof Array) {
    return true
  } else {
    return false
  }
}

/**判断是否数组 */
Util.prototype.judgeIsArray = function (arr) {
  if (arr instanceof Array) {
    return true
  } else {
    return false
  }
}

/**
 * 单选框复选框模块
 */
/**获取单选框，下拉框，复选框的optionArr的默认值 */
Util.prototype.getOptionArrDefault = (fieldItem) => {
  const optionArr = fieldItem.optionArr
  let defaultValue = ''
  if(fieldItem.field.elementType == ELEMENTTYPE['RADIO'] || fieldItem.field.elementType == ELEMENTTYPE['SELECT']) {
    optionArr.forEach(item => {
      if(item.defaultCheckedFlag === 1) {
        defaultValue = item.optionVal
      }
    })
  }else if(fieldItem.field.elementType == ELEMENTTYPE['CHECKBOX'] || fieldItem.field.elementType == ELEMENTTYPE['MORESELECT']) {
    defaultValue = []
    optionArr.forEach(item => {
      if(item.defaultCheckedFlag === 1) {
        defaultValue.push(item.optionVal)
      }
    })
  }
  return defaultValue
}

/**
 * 私有方法
 * 深度克隆
 */
Util.prototype.DeepClone = (data) => {
  return JSON.parse(JSON.stringify(data));
}

/**
 * 验证邮箱
 * @param {string} email
 * @returns {Boolean}
 */
Util.prototype.validEmail = function (email) {
  const reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return reg.test(email)
}

Util.prototype.isPhone = function (tel_num) {
  const tel_reg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
  return tel_reg.test(tel_num);
}

/**验证是否是数字 */
Util.prototype.isRealNum = function (val) {
  // isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除，
  if (val === "" || val == null) {
    return false;
  }
  if (!isNaN(val)) {
    //对于空数组和只有一个数值成员的数组或全是数字组成的字符串，isNaN返回false，例如：'123'、[]、[2]、['123'],isNaN返回false,
    //所以如果不需要val包含这些特殊情况，则这个判断改写为if(!isNaN(val) && typeof val === 'number' )
    return true;
  }
  else {
    return false;
  }
}

/**身份证号验证 */
Util.prototype.validIdNumber = function (idNumber) {
  const reg = /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
  return reg.test(idNumber)
}

/**座机号验证 */
Util.prototype.validLandline = function (Landline) {
  const reg = /0\d{2,3}-\d{7,8}/;
  return reg.test(Landline)
}

/**邮政编码验证 */
Util.prototype.validPostCode = function (postcode) {
  const reg = /^[0-9]{6}$/;
  return reg.test(postcode)
}

/**验证电话号 */
Util.prototype.validTelphone = function (telphone) {
  let isPhoneNumber = isPhone(telphone);
  let isLandline = validLandline(telphone);
  return isPhoneNumber | isLandline
}

/**验证社会信用代码 */
Util.prototype.validSocialCode = function (socialCode) {
  const reg = /^([0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}|[1-9]\d{14})$/
  return reg.test(socialCode)
}

export default Util
