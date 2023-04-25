import request from '@/utils/request';

//保存或者修改
export function saveOrUpdateCaseForm(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseForm/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}
//获取办件与表单关联信息
export function caseFormByCaseOid(caseOid,sxSerFormOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseForm/caseFormByCaseOid?caseOid=' + caseOid+"&sxSerFormOid="+sxSerFormOid,
    method: 'post',
  })
}

// 获取一件事的电子表单配置信息
export function selectBySxSerForm(comboDirectoryOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/onethingFormRunConfiguration/selectByComboSxSerForm?comboDirectoryOid='+comboDirectoryOid,
    method: 'get',
  })
}

// 获取一件事的电子表单配置信息
export function caseFormByCase(comboDireOid,regOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseForm/caseFormByCase?comboDireOid=' + comboDireOid+"&industryCaseOid="+regOid,
    method: 'post',
  })
}


