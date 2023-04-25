import Util from './Util';
import {ELEMENTTYPE} from './enum'
var moment = require('moment');
/**
 * 表单提交工具类
 */
function saveFormUtil(fillArr) {
  this.util = new Util()
  const isTwodArray = this.util.judgeTwodArray(fillArr)
  if (isTwodArray) {
    this.fillArr = this.util.formatFillArr(fillArr)
  } else {
    this.fillArr = fillArr
  }
  //值为单个字符串的表单控件类型
  this.ResultOfSingleElements = [
    ELEMENTTYPE['INPUT'],
    ELEMENTTYPE['TEXTAREA'],
    ELEMENTTYPE['SELECT'],
    ELEMENTTYPE['RADIO'],
    ELEMENTTYPE['DATEPICKER']
  ]
  //值为数组,需要逗号隔开的表单控件类型
  this.ResultOfJoinElements = [
    ELEMENTTYPE['CHECKBOX'],
    ELEMENTTYPE['MORESELECT']
  ]
}

saveFormUtil.prototype.initDataJson = function() {
  const fillArr = this.fillArr
  let factorJson = {}
  fillArr.forEach(item => {
    if(this.ResultOfJoinElements.includes(item.field.elementType))
    {
      const result = item.result || []
      factorJson[item.field.code] = result.join(',')
    }else if(item.field.elementType === ELEMENTTYPE['DATEPICKER']) {
      if(item.result==='Invalid date') {
        item.result = ""
      }
      factorJson[item.field.code] =item.result?moment(item.result).format('YYYY-MM-DD HH:mm:ss'):""
    }else {
      factorJson[item.field.code] = item.result
    }
  })
  return factorJson
}

export default saveFormUtil
