import request from '@/utils/request';

// 分页查询列表操作
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseCorrection/pageList',
    method: 'post',
    params: query
  })
}
//终止
export function saveStopCorrection(id) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseCorrection/saveStopCorrection?id='+id,
    method: 'post',
  })
}
//保存失信记录
export function saveOrUpdateRecord(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/dishonestRecord/saveOrUpdate',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=UTF-8",
    },
    data: data
  })
}
// 办件申请人信息查询
export function getOneApplyPerson(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseCorrection/getOneByCaseOid?caseOid='+caseOid,
    method: 'get'
  })
}

//保存或者修改
export function saveOrUpdateNotice(data) {

  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseCorrection/saveOrUpdateNotice',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data: data
  })
}

// 办件补正信息
export function getOneByCaseOid(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseCorrection/getOneByCaseOid?caseOid='+caseOid,
    method: 'post'
  })
}
export function getOneByid(id) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseCorrection/getOneById?id='+id,
    method: 'post'
  })
}

export function getCorrectMaterialInfo(correctionOid,caseOid) {

  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseCorrection/getOneMaterialInfo?correctionOid='+correctionOid+"&caseOid="+caseOid,
    method: 'post'
  })
}

//保存或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCaseCorrection/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 获取办件信息
export function getIndustryCaseByOid(caseOid,caseNumber) {
  if(caseNumber == undefined){
    caseNumber ='';
  }
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/case/getIndustryCaseByOidByCaseNumber?caseOid='+caseOid+'&caseNumber='+caseNumber,
    method: 'get'
  })
}


//一件事办件材料查询
export function getOneMaterialInfo(){
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/case/getIndustryCaseByOid/'+caseOid,
    method: 'get'
  })
}

//保存失信人员
export function saveOrUpdateOut(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/dishonestPerson/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}
