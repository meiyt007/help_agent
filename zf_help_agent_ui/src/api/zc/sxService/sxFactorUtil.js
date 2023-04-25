import { ELEMENTTYPE } from "@/enum";

function sxFactorUtil () {
  if(arguments.length>0) {
    this.factorArr = arguments[0]
    this.factorOptionMap = arguments[1]
  }
}

sxFactorUtil.prototype.formatFactorArr = function() {
  let new_arr = [];
if(this.factorArr){
  this.factorArr.forEach(item => {
    if(item.elementType == ELEMENTTYPE['CHECKBOX']) {
      item.result = []
    }else {
      item.result = ""
    }
    item.optionArr = this.factorOptionMap[item.oid] || []
    new_arr.push(item)
  });
}
  return new_arr
}

sxFactorUtil.prototype.getResults = function() {
  const factorArr = this.factorArr
  let results = {}
  if(factorArr){
    factorArr.forEach(item => {
      if(item.elementType == ELEMENTTYPE['CHECKBOX']) {
        results[item.code] = item.result.join(',')
      }else {
        results[item.code] = item.result
      }
    });
  }
  return results
}

export default sxFactorUtil
