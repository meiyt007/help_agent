import request from '@/utils/request'

// 获取一件事告知书信息列表
export function list(comboInformOid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/informLabel/list?comboInformOid=' + comboInformOid,
        method: 'get'
    })
}
// 获取一件事告知书信息列表
export function listComboInformLabelByComboDirectoryOid(comboDirectoryOid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/informLabel/listComboInformLabelByComboDirectoryOid?comboDirectoryOid=' + comboDirectoryOid,
        method: 'get'
    })
}

// 保存一件事告知书信息
export function save(comboInformLabelList, comboInformOid, comboDirectoryOid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/informLabel/save?comboInformOid=' + comboInformOid + "&comboDirectoryOid=" + comboDirectoryOid,
        method: 'post',
        data: comboInformLabelList
    })
}
