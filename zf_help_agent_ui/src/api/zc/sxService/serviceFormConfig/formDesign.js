import request from '@/utils/request'
import http from '@/utils/http'
//
export function list (oid) {
    return request({
        url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/formInfo/getSxFormInfoList',
        method: 'get',
        params: { serviceOid: oid }
    })
}

//
export function init (oid) {
    if (oid === undefined) {
        oid = '';
    }
    return request({
        url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/formInfo/getSxFormInfoByOid',
        method: 'get',
        params: { connectOid: oid }
    })
}

//
export function save (data) {
    return request({
        url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/formInfo/saveSxFormInfo',
        method: 'post',
        headers: {
            "Content-Type": "application/json;charset=UTF-8",
        },
        data: data
    })
}

export function saveOrUpdateField (data) {
    return request({
        url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/fillField/saveOrUpdateField',
        method: 'post',
        headers: {
            "Content-Type": "application/json;charset=UTF-8",
        },
        data: data
    })
}

//
export function del (oid) {
    return http({
        url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/formInfo/deleteSxFormInfo',
        method: 'post',
        data: { connectOid: oid }
    })
}

export function able (id) {
    return http({
        url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/formInfo/ableSxFormInfo',
        method: 'post',
        data: { id: id }
    })
}

export function updateDesignOid (oid, designOid, formMainOid, serviceOid) {
    return http({
        url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/formInfo/updateDesign',
        method: 'get',
        params: { oid: oid, designOid: designOid, formMainOid: formMainOid, serviceOid: serviceOid }
    })
}


// export function queryDesignFormList (oid) {
//     return request({
//         url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/formInfo/getDesignFormList',
//         method: 'get',
//         params: { serviceOid: oid }
//     })
// }

export function queryDesignFormList (oid, caseOid, valOids) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/formInfo/getZcFormInfoList',
    method: 'get',
    params: { serviceOid: oid, caseOid: caseOid, valOids: valOids }
  })
}


export function queryFormInfoExistList (serviceOid, fieldTypeOid) {
    return request({
        url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/formInfo/queryFormInfoExist',
        method: 'get',
        params: { serviceOid: serviceOid, fieldTypeOid: fieldTypeOid }
    })
}


export function updateChildName (oid, names) {
    return http({
        url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/formInfo/updateDesignChildName',
        method: 'get',
        params: { oid: oid, childFormName: names }
    })
}


