import request from "@/utils/request";


export function getOneForAuthorize(id) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/industryAuthorize/getOneForAuthorize?oid=' + id,
    method: 'get',
  })
}

// 保存或者修改
export function saveOrUpdateForRegister(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/industryAuthorize/saveOrUpdateForRegister',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 人员授权保存或者修改
export function saveOrUpdatePersonReg(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/industryAuthorize/saveOrUpdatePersonReg',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//区划机构一件事树
export function queryIndustryTree() {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/industryAuthorize/queryIndustryTree',
    method: 'post',
  })
}

// 查询一件事主题信息详情
export function getIndustryOne(comboDirectoryOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/industryAuthorize/getIndustryOne/'+comboDirectoryOid,
    method: 'get',
  })
}

// 查询当前人员下所有授权的一件事
export function getOneByUserOid(id) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/industryAuthorize/getOneByUserOid?oid=' + id,
    method: 'get',
  })
}


//一件事取消授权
export function delIndustryDireReg(oid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH +'/industry/industryAuthorize/delIndustryDireReg/' + oid,
    method: 'delete'
  })
}

//人员取消授权
export function delUserIndustryDire(oid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH +'/industry/industryAuthorize/delUserIndustryDire/' + oid,
    method: 'delete'
  })
}

