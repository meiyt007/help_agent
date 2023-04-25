import {ELEMENTTYPE} from './enum'
import validateUtil from './validateUtil'
import Util from './Util';
function initData(formData) {
  this.formData = formData
  this.conLableData = formData.fillInfo.conLableData
  this.util = new Util()
  //有optionArr的控件数组
  this.haveOptionArrElement = [
    ELEMENTTYPE['RADIO'],
    ELEMENTTYPE['CHECKBOX'],
    ELEMENTTYPE['SELECT']
  ]
}

//获取顶级层级
initData.prototype.getLabelArr = function() {
  //层级数据
  let new_arr = []
  this.conLableData.forEach(item => {
    if(item.type === '1') {
      new_arr.push(item)
    }
  })
  return new_arr
}

/**
 *
 * @param {当前选中的层级id} currentLevelOid
 */
initData.prototype.getFieldArr = function(currentLevelOid) {
  const lableFactorMap = this.formData.fillInfo.lableFactorMap;

  let fieldArr = lableFactorMap[currentLevelOid]
  //给数组添加上验证信息
  const validateutil = new validateUtil(fieldArr)
  fieldArr = validateutil.addFieldValidInfo()
  fieldArr.sort((a,b) => {
    return a.field.sort - b.field.sort
  })
  let new_fieldArr = []
  //添加默认值
  fieldArr.forEach(fieldItem => {
    if(!fieldItem.result) {
      fieldItem.result = this.getFieldDefault(currentLevelOid,fieldItem)
    }
    new_fieldArr.push(fieldItem)
  })

  //数据分组  按照一行一列 和一行两列分组
  const fieldArrByGroup = this.util.judgeShowType(new_fieldArr)

  return this.util.DeepClone(fieldArrByGroup)
}

/**表格类型的fillArr获取 */
initData.prototype.getTableFieldArr = function(currentLevelOid) {
  const lableFactorMap = this.formData.fillInfo.lableFactorMap;
  let tableFieldArr = []
  const results = this.initTableResults(currentLevelOid)
  const resultsKeys = Object.keys(results)
  if(resultsKeys.length===0) {
    let fillArr = this.getFieldArr(currentLevelOid)
    let tableFieldItem = {}
    fillArr = this.util.formatFillArr(fillArr)
    fillArr.forEach(item => {
      console.log(item)
      tableFieldItem[item.factorCode] = item
    })
    tableFieldArr.push(tableFieldItem)
    return this.util.DeepClone(tableFieldArr)
  }
  let fieldArr = lableFactorMap[currentLevelOid]
  //给数组添加上验证信息
  const validateutil = new validateUtil(fieldArr)
  fieldArr = validateutil.addFieldValidInfo()
  fieldArr.sort((a,b) => {
    return a.field.sort - b.field.sort
  })
  //表格行的数量
  const tableRows = results[resultsKeys[0]].length

  for(let i = 0; i < tableRows; i++) {
    let tableFieldItem = {}
    for(let j = 0;j<fieldArr.length;j++) {
      const item = this.util.DeepClone(fieldArr[j])
      if (item.factorCode != 'oid') {
        if(!item.result) {
          item.result = this.getFieldDefault(currentLevelOid,item,results[item.field.code][i])
        }
        tableFieldItem[item.factorCode] = item
      }
    }
    tableFieldArr.push(tableFieldItem)
  }
  return this.util.DeepClone(tableFieldArr)
}

/**根据currentLevelOid获取result数组 */
initData.prototype.initTableResults = function(currentLevelOid) {
  const currentLabelInfo  = this.getCurrentLabelInfo(currentLevelOid)
  const contentsArray = currentLabelInfo.contentsArray || []
  let new_result = {}
  contentsArray.forEach(item => {
    const keys = Object.keys(item)
    keys.forEach(code => {
      if(typeof new_result[code] === 'undefined') {
        new_result[code] = []
      }
      new_result[code].push(item[code])
    })
  })
  return new_result
}



/**
 * 初始化默认值result
 * @param {*} currentLevelOid 默认值数组， fieldItem 当前项
 */

 initData.prototype.getFieldDefault =  function(currentLevelOid,fieldItem,result) {
  let defaultValue = ""
  const currentLevelInfo = this.getCurrentLabelInfo(currentLevelOid)
  if(currentLevelInfo.tableStatus === '1') {
    defaultValue = result
  }else {
    const defaults = currentLevelInfo.contents || {};
    defaultValue = defaults[fieldItem.field.code] || ''
  }
  //如果修改的有默认值  直接返回
  if (defaultValue) {
    if(fieldItem.field.elementType === ELEMENTTYPE['CHECKBOX'] || fieldItem.field.elementType == ELEMENTTYPE['MORESELECT']) {
      defaultValue = defaultValue.split(',') || []
    }
    return defaultValue
  }
  //获取单选框，下拉框，复选框的optionArr的默认值
  if(this.haveOptionArrElement.includes(fieldItem.field.elementType)) {
    defaultValue = this.util.getOptionArrDefault(fieldItem)
  }
  return defaultValue
 }

 /**同步数据 */
 initData.prototype.syncFormData = function(fillArr,currentLevelOid) {
    fillArr = this.util.formatFillArr(fillArr,currentLevelOid)
    this.formData.fillInfo.lableFactorMap[currentLevelOid] = fillArr
    console.log(this.formData)
    return this.formData
 }

 /**获取当前label信息 */
 initData.prototype.getCurrentLabelInfo = function(currentLevelOid) {
    const conLableData = this.formData.fillInfo.conLableData
    let currentLevelInfo
    conLableData.forEach(item => {
      if(item.oid === currentLevelOid) {
        currentLevelInfo = item
      }
    })
    return currentLevelInfo
 }

 /**表格对应的字段名称 和code 转为数组*/
initData.prototype.getTableColumn = function(tableFillArr) {
  let tableColumn = []
  if(tableFillArr.length == 0) {
    return tableColumn
  }
  const tableFieldItem = tableFillArr[0]
  const codeArr = Object.keys(tableFieldItem)
  codeArr.forEach(item => {
    tableColumn.push({
      name: tableFieldItem[item].field.name,
      code: item
    })
  })
  return tableColumn
}

export default initData
