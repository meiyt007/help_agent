/**
 * 窗口受理接口api
 * @author: wangxl
 * @date: 2020-11-23
 */
import request from '@/utils/request';

// 获取一件事目录情形信息
export function getSituationList (comboDirectoryOid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/situation/queryAllSituationHavingVals?comboDirectoryOid=' + comboDirectoryOid + '&situationType=1',
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

// 根据选项值获取关联选项信息
export function queryComboOptionTitleByValOid (direOid, valOid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/OptionTitle/queryComboOptionTitleByValOid?direOid=' + direOid + "&valOid=" + valOid,
        method: 'get'
    })
}


/**
 * 获取一件事材料
 * @param direOid
 */
export function getComboDireMaterialList (direOid, valOidS) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directoryMaterial/queryMaterialListByDireOidAndValOids?direOid=' + direOid + '&valOidS=' + valOidS,
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


// 一件事办件表单更新
export function updateComboCase (data) {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/case/updateComboCaseFormInfo',
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
        method: 'post',
        headers: {
            "Content-Type": "application/json;charset=utf-8",
        },
        data: data
    })
}


// 获取一件事目录信息
export function getComboDireDetail (oid, valOidS) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directory/getComboDireDetail?direOid=' + oid + '&valOidS=' + valOidS,
        method: 'get'
    })
}

// 获取一件事目录详细信息
export function getComboDirectoryByDirectoryOid (oid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directory/getComboDirectoryByDirectoryOid/' + oid,
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

// 查询是否选择的选项为阻塞情形
export function blockSituationOptionsInfo (options, comboDireOid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/optionSituation/queryBlockOptions?options=' + options + '&comboDireOid=' + comboDireOid,
        method: 'get'
    })
}

// 获取特别程序类型名称
export function getSpecialName (oid) {
    return request({
        url: process.env.VUE_APP_SX_ROUTE_PATH + '/getOne/' + oid,
        method: 'get'
    })
}

// 查询一件事主题信息列表
export function pageZwbk (query) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/serviceManage/page',
        method: 'get',
        params: query
    })
}
// 查询一件事主题信息列表
export function queryOptionResultListChoose (oid, valOidS) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/optionResult/queryOptionResultListChoose?comboDirectoryOid=' + oid + '&valOidS=' + valOidS,
        method: 'get'
    })
}

// 材料出库信息保存
export function saveOut (data) {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboMaterialOut/saveOrUpdate',
        method: 'POST',
        headers: {
            "Content-Type": "application/json;charset=utf-8",
        },
        data: data
    })
}

// 获取用户信息
export function getloginUser () {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/case/getLoginUser',
        method: 'get'
    })
}

// 获取平板评价点击保存办件信息
export function pbpjSaveCaseInfo (caseOid) {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/oneThingPbpjService/pbpjSaveQlCase?caseOid=' + caseOid,
        method: 'get'
    })
}

// 获取平板评价推动评价信息
export function pushPbpjInfo (caseNumber) {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/oneThingPbpjService/pushPbpjInfo?caseNumber=' + caseNumber,
        method: 'get'
    })
}

// 默认自定义情形查询当前事项下标题没有跟任何其他选项值关联的数据
export const queryComboDireOptionTitlesNoRelation = (params) => {
    return request.get(process.env.VUE_APP_THING_ROUTE_PATH + '/combo/OptionTitle/queryComboDireOptionTitlesNoRelation', { params })
}

// 默认自定义情形查询点击标题下选项值，查询出相关联的标题信息
export const queryComboDireOptionTitlesRelation = (params) => {
    return request.get(process.env.VUE_APP_THING_ROUTE_PATH + '/combo/OptionTitle/queryComboDireOptionTitlesRelation', { params })
}

//
/**
 * 判断是否有阻塞情形
 * @param {number} situationType 1: 获取热门情形 2:获取阻塞情形 用于判断
 * @param {string} comboDirectoryOid
 */
export const getBlockOrHotSituationList = (params) => {
    return request.get(process.env.VUE_APP_THING_ROUTE_PATH + '/combo/situation/querySituationListBySituationType', { params })
}
