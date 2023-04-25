import request from '@/utils/request';

//查询列表
export function sxServicePage (query) {
    return request({
        url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/sxServicePage',
        method: 'post',
        params: query
    })
}

//获取事项详情
export function getSxServiceOne (id) {
    return request({
        url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/viewSxService?serviceOid=' + id,
        method: 'get'
    })
}

// 事项表单关联保存或者修改
export function save (data) {
    return request({
        url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formRunConfiguration/save',
        method: 'post',
        headers: {
            "Content-Type": "application/json;charset=UTF-8",
        },
        data: data
    })
}

//查询列表 - serviceOid
export function getSerFormList (serviceOid, pageNumber, pageSize) {
    return request({
        url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formRunConfiguration/getSerFormsByServiceOid/' + serviceOid + '/' + pageNumber + '/' + pageSize,
        method: 'get',
    })
}

export function getAbleSerFormList (serviceOid) {
    return request({
        url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formRunConfiguration/selectBySxSerFormByServiceOid?serviceOid=' + serviceOid,
        method: 'get',
    })
}

//查询列表 - comboDirectoryOid
export function getComboFormList (comboDirectoryOid, pageNumber, pageSize) {
    return request({
        url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formRunConfiguration/getSerFormsByComboDirectoryOid/' + comboDirectoryOid + '/' + pageNumber + '/' + pageSize,
        method: 'get',

    })
}

// 获取事项表单详情
export function getSxSerFormByOid (sxSerFormOid) {
    return request({
        url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formRunConfiguration/getSxSerFormByOid?sxSerFormOid=' + sxSerFormOid,
        method: 'get',
    })
}

// 启禁用表单信息
export function sxSerFormAble (oid) {
    return request({
        url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formRunConfiguration/sxSerFormAble',
        params: {
            oid: oid
        },
        method: 'post'
    })
}

//获取一件事下所有和一件事关联的表单
export function getSerFormsOfCombo (comboDirectoryOid, queryIneffectiveFlag, pageNumber, pageSize) {
    return request({
        url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formRunConfiguration/getSerFormsOfCombo/' + comboDirectoryOid + '/' + pageNumber + '/' + pageSize + '/' + queryIneffectiveFlag,
        method: 'get',
    })
}

//批量更新事项表单，
export function saveSxSerFormByList (data) {
    return request({
        url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formRunConfiguration/saveSxSerFormByList',
        data: data,
        headers: {
            "Content-Type": "application/json;charset=UTF-8",
        },
        method: 'post'
    })
}

//单条更新
export function updateSxSerForm (oid) {
    return request({
        url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formRunConfiguration/updateSxSerForm',
        method: 'post',
        headers: {
            "Content-Type": "application/json;charset=UTF-8",
        },
        params: {
            oid: oid
        },
    })
}


// 删除表单信息
export function sxSerFormDel (oid) {
    return request({
        url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formRunConfiguration/sxSerFormDel',
        params: {
            oid: oid
        },
        method: 'post'
    })
}


// 获取发布表单信息
export function queryFormList () {
    return request({
        url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formRunConfiguration/queryPublishFormList',
        method: 'get'
    })
}

// 获取发布表单分页信息
export function queryFormPageList (queryName, pageNumber, pageSize) {
    return request({
        url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formRunConfiguration/queryElectronicsFormPageList?queryName=' + queryName
            + "&pageNumber=" + pageNumber + "&pageSize=" + pageSize,
        method: 'get'
    })
}



