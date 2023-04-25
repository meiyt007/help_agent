/**
 * 窗口受理接口api
 * @author: wangxl
 * @date: 2020-12-1
 */
import request from '@/utils/request';

// 获取一件事办件目录情形信息
export function getCaseSituationList (caseOid) {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboCase/situation/queryDirectorySituationListByCaseOid?caseOid=' + caseOid,
        method: 'get'
    })
}

// 获取一件事情形选项信息
export function getSituationOpinionList (direOid, situationOid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/OptionTitle/queryComboOptionTitleListBySituationOid?direOid=' + direOid + "&situationOid=" + situationOid + '&optionType=1',
        method: 'get'
    })
}

/**
 * 获取一件事材料
 * @param direOid
 */
export function getComboDireMaterialList (direOid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directoryMaterial/queryMaterialListByDireOid/' + direOid,
        method: 'get'
    })
}

// 获取证件类型
export function getCertificateType (type) {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/common/getSelectCertificateType?type=' + type,
        method: 'get'
    })
}

// 一件事办件的新增或者修改
export function saveComboCase (data) {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/case/save',
        method: 'post',
        headers: {
            "Content-Type": "application/json;charset=utf-8",
        },
        data: data
    })
}


// 一件事办件情形选项关联
export function saveCaseSituOpt (data) {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboCase/situValRel/save',
        method: 'post',
        headers: {
            "Content-Type": "application/json;charset=utf-8",
        },
        data: data
    })
}

// 附件上传
export function uploadCaseMaterialFile (data) {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboCase/atta/uploadFile',
        method: 'POST',
        headers: {
            "Content-Type": "multipart/form-data",
        },
        data: data
    })
}

// 获取办件信息
export function getComboCaseByOid (caseOid) {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/case/getComboCaseByOid/' + caseOid,
        method: 'get'
    })
}


// 保存材料附件信息
export function saveCaseMaterialAtta (data) {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboCase/materialAtta/save',
        method: 'post',
        headers: {
            "Content-Type": "application/json;charset=utf-8",
        },
        data: data
    })
}

// 保存办件受理信息
export function saveCaseAccpet (data) {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/case/saveCaseAccpet',
        method: 'POST',
        headers: {
            "Content-Type": "application/json;charset=utf-8",
        },
        data: data
    })
}

// 检查平板评价用户启用信息
export function pushPbpjUser (userOid) {
    return request({
        url: process.env.VUE_APP_ZC_ROUTE_PATH + '/pbpj/user/getPbpjUserByUserOid?userOid=' + userOid,
        method: 'get'
    })
}


// 推送平板评价检查是否登录
export function pushPbpjCheckLogin () {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/oneThingPbpjService/checkUserLogin',
        method: 'get'
    })
}

// 推送平板评价信息确认
export function regComboCaseInfo (data, pbpjCaseOkUrl) {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/oneThingPbpjService/regComboCaseInfo?pbpjCaseOkUrl=' + pbpjCaseOkUrl,
        method: 'POST',
        headers: {
            "Content-Type": "application/json;charset=utf-8",
        },
        data: data
    })
}

// 获取平板评价点击确认信息
export function comboCaseCallBack () {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/oneThingPbpjService/comboCaseCallBack',
        method: 'get'
    })
}

// 获取证件类型
export function changeCredentialType (oid) {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/common/querySysDictByOid?dictOid=' + oid,
        method: 'get'
    })
}

export function getSituationResult (caseOid) {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/caseSituation/getCaseSituationResultByCaseOid?caseOid=' + caseOid,
        method: 'get'
    })
}

export function getComboFormInfo (caseOid) {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/case/queryComboFormInfoByCaseOid?caseOid=' + caseOid,
        method: 'get'
    })
}

export function getComboCaseValRel (caseOid, designOid) {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboCase/situValRel/getComboCaseFormViewByCaseOid?caseOid=' + caseOid + '&designOid=' + designOid,
        method: 'get'
    })
}

