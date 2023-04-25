import request from '@/utils/request'

// 获取一件事告知书信息列表
export function page(queryParams) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/inform/pageInforom',
        method: 'get',
        params:queryParams
    })
}

// 保存一件事告知书信息
export function save(comboInform) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/inform/save',
        method: 'post',
        data: comboInform
    })
}

// 初始化一件事告知书信息
export function init(oid) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/inform/init?oid='+oid,
        method: 'get'
    })
}

// 删除一件事告知书信息
export function del(id) {
    return request({
        url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/inform/delete/'+id,
        method: 'post'
    })
}
