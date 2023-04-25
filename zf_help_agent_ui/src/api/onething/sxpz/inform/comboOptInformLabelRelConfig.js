import request from '@/utils/request'

// 一件事告知书标签配置信息列表
export function page(queryParams) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/optInformLabelRel/page',
        method: 'get',
        params:queryParams
    })
}

// 删除
export function del(id) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/optInformLabelRel/delete/'+id,
        method: 'post'
    })
}

// 初始化
export function init(comboDirectoryOid, optInformLabelRelOid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/optInformLabelRel/init?comboDirectoryOid=' + comboDirectoryOid +
                                                    "&optInformLabelRelOid=" + optInformLabelRelOid,
        method: 'get'
    })
}

// 保存
export function save(optionComboOptInformLabelRel) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/optInformLabelRel/save',
        method: 'post',
        data: optionComboOptInformLabelRel
    })
}

// 根据标签oid查询标签和情形配置关系
export function countComboOptInformLabelRelAmountByLabelOid(comboInformLabelOid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/optInformLabelRel/countComboOptInformLabelRelAmountByLabelOid' +
            '?comboInformLabelOid=' + comboInformLabelOid,
        method: 'get'
    })
}
