import {
  ELEMENTTYPE
} from './enum';
import {
  REGTYPE
} from './enum';
import Util from './Util';

function validateUtil(fillArr = [], valiRegExpArr = []) {
  this.valiRegExpArr = valiRegExpArr
  this.util = new Util()
  const isTwodArray = this.util.judgeTwodArray(fillArr)
  if (isTwodArray) {
    this.fillArr = this.util.formatFillArr(fillArr)
  } else {
    const isArray = this.util.judgeIsArray(fillArr)
    if (isArray) {
      this.fillArr = fillArr
    } else {
      this.fieldItem = fillArr
    }

  }

}

/**
 * 将所有表单项添加  是否显示验证标识  和  验证信息
 */
validateUtil.prototype.addFieldValidInfo = function () {
  let new_arr = []
  this.fillArr.forEach(item => {
    //默认全部为不显示验证信息，验证信息为空
    if (!item.hasOwnProperty('showWarning') && !item.hasOwnProperty('warningInfo')) {
      item.showWarning = false;
      item.warningInfo = ''
    }
    new_arr.push(item)
  })
  return new_arr
}

/**表单项验证 返回验证后的fillArr */
validateUtil.prototype.validateForm = function () {
  const fillArr = this.fillArr
  let new_arr = []
  fillArr.forEach(item => {
    this.fieldItem = item
    item = this.validateFormItem()
    new_arr.push(item)
  })
  return this.util.judgeShowType(new_arr)
}

/**表格型表单验证 */
validateUtil.prototype.validateFormTable = function (tableFillArr) {
  let new_tableFillArr = []
  this.errorItem = {}
  tableFillArr.forEach(tableItem => {
    const keys = Object.keys(tableItem)
    let new_tableItem = {}
    keys.forEach(item => {
      this.fieldItem = tableItem[item]
      new_tableItem[item] = this.validateFormItem()
      if (new_tableItem[item].showWarning) {
        if (Object.keys(this.errorItem).length == 0) {
          this.errorItem = new_tableItem[item]
        }
      }
    })
    new_tableFillArr.push(new_tableItem)
  })
  return new_tableFillArr
}

/**表单项校验 */
validateUtil.prototype.validateFormItem = function () {
    let fieldItem = this.fieldItem;
    //验证文本域和文本框
    if (fieldItem.field.elementType === ELEMENTTYPE['INPUT'] || fieldItem.field.elementType === ELEMENTTYPE['TEXTAREA']) {
      fieldItem = this.validateInputTextarea()
    } else {
      //必填验证 非文本域和文本框只需验证必填即可
      fieldItem = this.validateMustFill()
    }
    return fieldItem
  },

  /**验证表单项 input 和 textarea */
  validateUtil.prototype.validateInputTextarea = function () {
    let fieldItem = this.fieldItem
    if (fieldItem.field.allowEmptyFlag === 0) {
      //必填验证
      fieldItem = this.validateMustFill()
      if (fieldItem.showWarning) {
        return fieldItem
      }
    } else if (fieldItem.field.allowEmptyFlag === 2) {
      //必须是数字
      fieldItem = this.validateIsNumber()
      if (fieldItem.showWarning) {
        return fieldItem
      }
    }
    if (fieldItem.field.regExp) {
      if (!isNaN(Number(fieldItem.field.regExp))) {
        /**验证特殊类型 */
        fieldItem = this.validateSpecialReg()
      } else {
        /**验证自定义正则 */
        fieldItem = this.validatMyReg()
      }

    }
    return fieldItem
  }

/**必填验证 */
validateUtil.prototype.validateMustFill = function () {
  let fieldItem = this.fieldItem;
  if (fieldItem.field.allowEmptyFlag === 0) {
    let bool = true;
    if (fieldItem.field.preFactorOid) {
      let util = new Util()
      const fillArr = util.formatFillArr(this.fillArr)
      bool = util.preFieldOidForShow(
        fillArr,
        fieldItem.field.preFactorOid,
        fieldItem.field.preFactorOptVal
      );
    }
    if ((fieldItem.result == '' || fieldItem.result == undefined) && bool) {
      fieldItem.showWarning = true;
      fieldItem.warningInfo = `${fieldItem.field.name}不能为空`
    } else {
      fieldItem.showWarning = false;
      fieldItem.warningInfo = ""
    }
  }
  return fieldItem
}

/**数字验证 */
validateUtil.prototype.validateIsNumber = function () {
  let fieldItem = this.fieldItem
  if (fieldItem.field.allowEmptyFlag !== 2) {
    return fieldItem
  }
  const util = new Util()
  if (fieldItem.result == '') {
    fieldItem.showWarning = true;
    fieldItem.warningInfo = `${fieldItem.field.name}必须是数字`
  } else {
    let isNUmber = util.isRealNum(fieldItem.result)
    if (isNUmber) {
      fieldItem.showWarning = false;
      fieldItem.warningInfo = ""
    } else {
      fieldItem.showWarning = true;
      fieldItem.warningInfo = `${fieldItem.field.name}必须是数字`
    }
  }
  return fieldItem
}

/**验证特殊类型 */
validateUtil.prototype.validateSpecialReg = function () {
  let fieldItem = this.fieldItem
  const regExp = fieldItem.field.regExp
  let isPass = true; //是否验证通过
  const fieldItemResult = fieldItem.result
  switch (regExp) {
    case REGTYPE['IDCARD']:
      isPass = this.util.validIdNumber(fieldItemResult)
      break;
    case REGTYPE['PHONE']:
      isPass = this.util.isPhone(fieldItemResult)
      break;
    case REGTYPE['EMAIL']:
      isPass = this.util.validEmail(fieldItemResult)
      break;
    case REGTYPE['LANDLINE']:
      isPass = this.util.validLandline(fieldItemResult)
      break;
    case REGTYPE['TELPHONE']:
      isPass = this.util.validTelphone(fieldItemResult)
      break;
    case REGTYPE['POSTCODE']:
      isPass = this.util.validPostCode(fieldItemResult)
      break;
  }
  if (isPass || fieldItem.result === '') {
    fieldItem.showWarning = false;
    fieldItem.warningInfo = ""
  } else {
    fieldItem.showWarning = true;
    fieldItem.warningInfo = this.getRegTypeDesc(regExp)
  }

  return fieldItem
}

/**验证自定义正则 */
validateUtil.prototype.validatMyReg = function () {
  let fieldItem = this.fieldItem
  let regExp = fieldItem.field.regExp
  if (regExp != null && regExp != '') {
    let arr = regExp.split(",");
    regExp = '';
    for (let i = 0; i < arr.length; i++) {
      regExp += String.fromCharCode(parseInt(arr[i], 16));
    }
  }
  if (fieldItem.result === "" || fieldItem.result.match(regExp) != null) {
    fieldItem.showWarning = false;
    fieldItem.warningInfo = "";
  } else {
    fieldItem.showWarning = true;
    fieldItem.warningInfo = fieldItem.field.regExpDesc;
  }
  return fieldItem
}

/**获取验证失败提示文字 */
validateUtil.prototype.getRegTypeDesc = function (regType) {
  let regDesc = ''
  this.valiRegExpArr.forEach(item => {
    if (item.regType === regType) {
      regDesc = item.regDesc
    }
  })
  return regDesc
}

export default validateUtil
