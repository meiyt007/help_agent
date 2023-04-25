import request from '@/utils/request';

// 获取电子表单信息
export function sxSerFormByServiceOid(serviceOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formRunConfiguration/selectBySxSerFormByServiceOid?serviceOid=' + serviceOid,
    method: 'get',
  })
}

// 获取电子表单信息
export function getSxSerFormBysxSerFormOid(sxSerFormOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formRunConfiguration/getSxSerFormByOid?sxSerFormOid=' + sxSerFormOid,
    method: 'get',
  })
}

//保存或者修改
export function saveOrUpdateCaseForm(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseForm/saveOrUpdate',
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
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseForm/caseFormByCaseOid?caseOid=' + caseOid+"&sxSerFormOid="+sxSerFormOid,
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
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseForm/caseFormByCase?comboDireOid=' + comboDireOid+"&regOid="+regOid,
    method: 'post',
  })
}

//保存或者修改
export function saveOrUpdateComboCaseForm(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseForm/saveOrUpdateComboCaseForm',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}


