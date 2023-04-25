import request from '@/utils/request';

// 办件所属事项信息查询
export function getServiceInfo (serviceOid) {
    return request({
        url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/viewSxService?serviceOid=' + serviceOid,
        method: 'get'
    })
}
// 办件信息查询
export function getOneCase (caseNumber) {
    return request({
        url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlCaseService/queryQlCaseByCaseNumber?caseNumber=' + caseNumber,
        method: 'get'
    })
}
// 办件申请人信息查询
export function getOneApplyPerson (caseOid) {
    return request({
        url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlCaseApplayService/queryQlCaseApplayByCaseOid?caseOid=' + caseOid,
        method: 'get'
    })
}
// 办件材料查询
export function getOneMaterialInfo (caseOid) {
    return request({
        url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlCaseMaterialService/queryQlCaseMaterialByCaseOid?caseOid=' + caseOid,
        method: 'get'
    })
}
// 办件环节信息查询
export function getOneLinkInfo (caseOid) {
    return request({
        url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlCaseLinkResultService/queryQlCaseLinkResultListByCaseOid?caseOid=' + caseOid,
        method: 'get'
    })
}
//送达方式
export function getQlCaseExt (caseOid) {
    return request({
        url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlCaseExtService/queryQlCaseExtByCaseOid?caseOid=' + caseOid,
        method: 'get'
    })
}

// 获取单个数字字典信息
export function getOneDict (oid) {
    return request({
        url: '/settings/security/dict/getSysDictByDictOid/' + oid,
        method: 'get'
    })
}

//获取补正信息
export function getQlCorrectByCaseOid (caseOid) {
    return request({
        url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/qlCaseCorrection/getListByCaseOid?caseOid=' + caseOid,
        method: 'post'
    })
}

// 获取出库信息
export function getOneByCaseNumber (caseNumber) {
    return request({
        url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseMaterialOut/getCaseMaterialOutByCaseNumber?caseNumber=' + caseNumber,
        method: 'post'
    })
}

//获取签收
export function getZzqsByCaseOid (caseOid) {
    return request({
        url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseLicenseManage/getOneByCaseOid?caseOid=' + caseOid,
        method: 'post'
    })
}

//获取签发
export function getZzqfByCaseOid (caseOid) {
    return request({
        url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/licenseIssued/getLicenseIssuedByCaseOid?caseOid=' + caseOid,
        method: 'post'
    })
}

// 材料下载
export function fileDownLoad (attaOid) {
    // const baseURL = process.env.VUE_APP_BJFW_API_PAGE;
    const baseURL = process.env.VUE_APP_BASE_CASE_API;
    //alert(baseURL)
    console.log("***" + baseURL)
    window.location.href = baseURL + '/sysAttaService/downloadAtta?attaOid=' + attaOid;
}

//ems发证
export function getDeliverRecordByCaseOid (caseOid) {
    return request({
        url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/caseLicenseManage/getDeliverRecordByCaseOid?caseOid=' + caseOid,
        method: 'post'
    })
}

// 办件表单信息查询
export function queryFormInfo (caseOid) {
    return request({
        url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlCaseService/queryFormInfoByCaseOid?caseOid=' + caseOid,
        method: 'get'
    })
}


