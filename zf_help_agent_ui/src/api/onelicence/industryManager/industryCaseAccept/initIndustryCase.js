/**
 * 窗口受理接口api
 * @author: wangxl
 * @date: 2020-11-23
 */
import request from '@/utils/request';

// 获取一业一证目录情形信息
export function getSituationList(industryDirectoryOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/situation/queryDirectorySituationListByDireOid?comboDirectoryOid='+industryDirectoryOid+'&situationType=1',
    method: 'get'
  })
}

// 获取一业一证情形选项信息
export function getSituationOpinionList(direOid,situationOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/OptionTitle/queryComboOptionTitleListBySituationOid?direOid='+direOid+"&situationOid="+situationOid+'&optionType=1',
    method: 'get'
  })
}

// 根据选项值获取关联选项信息
export function queryIndustryOptionTitleByValOid(direOid,valOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/OptionTitle/queryComboOptionTitleByValOid?direOid='+direOid+"&valOid="+valOid,
    method: 'get'
  })
}


/**
 * 获取一业一证材料
 * @param direOid
 */
export  function getIndustryDireMaterialList(direOid,valOidS) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directoryMaterial/queryMaterialListByDireOidAndValOids?direOid='+direOid+'&valOidS='+valOidS,
    method: 'get'
  })
}

// 一业一证办件的新增或者修改
export function saveIndustryCase(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH +'/industry/case/save',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data: data
  })
}

// 获取证件类型
export function getCertificateType(type) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/commonService/getSelectCertificateType?type='+type,
    method: 'get'
  })
}

// 一业一证办件情形选项关联
export function saveCaseSituOpt(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH +'/industryCase/situValRel/save',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data: data
  })
}

// 附件上传
export function uploadCaseMaterialFile(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCase/atta/uploadFile',
    method: 'POST',
    headers:{
      "Content-Type":"multipart/form-data",
    },
    data:data
  })
}

// 获取办件信息
export function getIndustryCaseByOid(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/case/getIndustryCaseByOid/'+caseOid,
    method: 'get'
  })
}


// 保存材料附件信息
export function saveCaseMaterialAtta(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryCase/materialAtta/save',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data: data
  })
}

// 保存办件受理信息
export function saveCaseAccpet(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/case/saveCaseAccpet',
    method: 'post',
    data:data
  })
}


// 获取一业一证目录信息
export function getIndustryDireDetail(oid,valOidS) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directory/getComboDireDetail?direOid='+oid+'&valOidS='+valOidS,
    method: 'get'
  })
}

// 获取证件类型
export function changeCredentialType(oid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/commonService/querySysDictByOid?dictOid='+oid,
    method: 'get'
  })
}



// 检查平板评价用户启用信息
export function pushPbpjUser(userOid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/pbpj/user/getPbpjUserByUserOid?userOid='+userOid,
    method: 'get'
  })
}


// 推送平板评价检查是否登录
export function pushPbpjCheckLogin() {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryPbpjService/checkUserLogin',
    method: 'get'
  })
}

// 获取平板评价点击保存办件信息
export function pbpjSaveCaseInfo(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryPbpjService/pbpjSaveQlCase?caseOid='+caseOid,
    method: 'get'
  })
}

// 获取平板评价推动评价信息
export function pushPbpjInfo(caseNumber) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryPbpjService/pushPbpjInfo?caseNumber='+caseNumber,
    method: 'get'
  })
}

// 推送平板评价信息确认
export function regIndustryCaseInfo(data,pbpjCaseOkUrl) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryPbpjService/regIndustryCaseInfo?pbpjCaseOkUrl='+pbpjCaseOkUrl,
    method: 'POST',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data: data
  })
}

// 获取平板评价点击确认信息
export function industryCaseCallBack() {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryPbpjService/industryCaseCallBack',
    method: 'get'
  })
}

// 查询是否选择的选项为阻塞情形
export function blockSituationOptions(options,comboDireOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/optionSituation/queryBlockOptions?options='+options+'&comboDireOid='+comboDireOid,
    method: 'get'
  })
}

// 获取特别程序类型名称
export function getSpecialName(oid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/getOne/'+ oid,
    method: 'get'
  })
}

// 查询一业一证主题信息列表
export function pageZwbk(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/serviceManage/page',
    method: 'get',
    params: query
  })
}
// 查询一业一证主题信息列表
export function queryOptionResultListChoose(oid,valOidS) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionResult/queryOptionResultListChoose?comboDirectoryOid='+oid+'&valOidS='+valOidS,
    method: 'get'
  })
}

// 材料出库信息保存
export function saveOut(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryMaterialOut/saveOrUpdate',
    method: 'POST',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data:data
  })
}

// 获取用户信息
export function getloginUser() {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/case/getLoginUser',
    method: 'get'
  })
}


// new一业一证办件的新增或者修改
export function saveOrupdateCase(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH +'/industry/case/saveOrupdateCase',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data: data
  })
}

// 获取办件信息
export function getIndustryCaseByCaseOid(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/case/getIndustryCaseByCaseOid/'+caseOid,
    method: 'get'
  })
}

// 保存办件受理信息
export function saveIndustryCaseAccpet(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industry/case/saveCaseAccpet',
    method: 'post',
    data:data
  })
}




