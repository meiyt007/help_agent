import {ELEMENTTYPE} from './enum'
import validateUtil from './validateUtil'
import Util from './Util';
var moment = require('moment');
function tableFormUtil(formData) {
  this.formData = formData;
  this.util = new Util()
  //有optionArr的控件数组
  this.haveOptionArrElement = [
    ELEMENTTYPE['RADIO'],
    ELEMENTTYPE['CHECKBOX'],
    ELEMENTTYPE['SELECT']
  ]
  //值为数组,需要逗号隔开的表单控件类型
  this.ResultOfJoinElements = [
    ELEMENTTYPE['CHECKBOX']
  ]
}

/**默认值初始化 */
tableFormUtil.prototype.initDefault = function(tableItem) {
    const keys = Object.keys(tableItem)
    let new_tableItem = {}
    keys.forEach(item => {
      new_tableItem[item] = this.initDefaultItem(tableItem[item])
    })
    return new_tableItem
}

/**表单项默认值初始化 */
tableFormUtil.prototype.initDefaultItem = function(tableItem) {
    let new_tableItem = tableItem
    let defaultValue = ''
    //获取单选框，下拉框，复选框的optionArr的默认值
    if(this.haveOptionArrElement.includes(tableItem.field.elementType)) {
      defaultValue = this.util.getOptionArrDefault(tableItem)
    }
    new_tableItem.result = defaultValue
    return this.util.DeepClone(new_tableItem)
}

/**构建表单提交时的json */
tableFormUtil.prototype.initDataJsonTable = function(tableFillArr) {
    if(tableFillArr.length === 0 || !tableFillArr) {
      throw new Error('initDataJsonTabletableFillArr 参数错误')
    }
    const codes = Object.keys(tableFillArr[0]);
    let factorJson = []
    // codes.forEach(code => {
    //   factorJson[code] = []
    // })

    tableFillArr.forEach(item => {
      let valueObj = {}
      codes.forEach(code => {
        const fieldItem = item[code]
        let result = ""
        if(this.ResultOfJoinElements.includes(fieldItem.field.elementType))
        {
          result = fieldItem.result || []
          result = result.join(',')
        }else if(fieldItem.field.elementType === ELEMENTTYPE['DATEPICKER']) {
          if(fieldItem.result==='Invalid date') {
            fieldItem.result = ""
          }
          result =fieldItem.result?moment(fieldItem.result).format('YYYY-MM-DD HH:mm:ss'):""
        }else if(fieldItem.field.elementType === ELEMENTTYPE['IMAGEUPLOADER']) {
          result = JSON.stringify(fieldItem.result)
        } else {
          result = fieldItem.result || '';
        }
        valueObj[code] = result
      })

      factorJson.push(valueObj)
    })

    //构建factorJson完成  result数组用 | 分割
    // codes.forEach(code => {
    //   factorJson[code] = factorJson[code].join('|')
    // })

    return factorJson


}



export default tableFormUtil
