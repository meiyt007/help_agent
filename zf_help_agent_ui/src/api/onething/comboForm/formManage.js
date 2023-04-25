import request from '@/utils/request'

//查询表单列表
export function list (thingOid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillForm/list?thingOid=' + thingOid,
        method: 'get',
    })
}

//查询表单列表
export function listForPreViewForm (thingOid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillForm/listForPreViewForm',
        method: 'get',
        params: { thingOid: thingOid }
    })
}

//查询表单历史列表
export function page (query) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillForm/page',
        method: 'get',
        params: query
    })
}


export function get (oid) {
    if (oid === undefined) {
        oid = '';
    }
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillForm/getFormInfoByOid',
        method: 'get',
        params: { formOid: oid }
    })
}

export function able (id) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillForm/ableFormInfo',
        method: 'post',
        params: { id: id }
    })
}

// 同步更新表单信息
export function syncComboFormInfo (thingOid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillForm/sync',
        method: 'post',
        params: { thingOid: thingOid }
    })
}

// 一件事主题的新增或者修改
export function connectFormCode (formOid, authorizeKey, formCode, formName) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillForm/connectFormCode',
        method: 'post',
        params: { formOid: formOid, authorizeKey: authorizeKey, formCode: formCode, formName: formName }
    })
}


//查询字段列表
export function queryComboFieldList (thingOid, typeOid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillField/queryComboFillFieldList?comboDirectoryOid='
            + thingOid + '&typeOid=' + typeOid,
        method: 'get'
    })
}


//查询字段列表
export function getFormCodes (thingOid, typeOid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillForm/getFormCodes',
        method: 'get',
        params: { thingOid: thingOid, typeOid: typeOid }
    })
}

//查询字段列表
export function getFormConfigJson (thingOid, typeOid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillForm/getFormConfigJson',
        method: 'get',
        params: { thingOid: thingOid, typeOid: typeOid }
    })
}

//查询字段列表
export function submitFormInfo (formOid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillForm/submitFormInfo',
        method: 'get',
        params: { formOid: formOid }
    })
}

export function queryDesignFormList (oid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillForm/getDesignFormList',
        method: 'get',
        params: { thingOid: oid }
    })
}

export function updateDesignFormOid (formOid, designOid, formMainOid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillForm/updateDesignForm',
        method: 'get',
        params: { formOid: formOid, designOid: designOid, formMainOid: formMainOid }
    })
}



export function queryFormInfoList (caseOid, comboDirectoryOid, valOids) {
    return request({
        url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/situationForm/queryPacFormInfoList',
        method: 'get',
        params: { caseOid: caseOid, comboDirectoryOid: comboDirectoryOid, valOids: valOids }
    })
}

export function updateChildName (formOid, childFormName) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillForm/updateChildName',
        method: 'get',
        params: { formOid: formOid, childFormName: childFormName }
    })
}

// 保存表单
export function saveOrUpdateComboFormInfo (params) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillForm/saveOrUpdateComboFormInfo',
        method: 'post',
        data: params
    })
}

// 查询列表中存在几个以启用的相同分类
export function queryFormInfoExist (thingOid, typeOid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/fillForm/queryFormInfoExist',
        method: 'get',
        params: { thingOid, typeOid }
    })
}
