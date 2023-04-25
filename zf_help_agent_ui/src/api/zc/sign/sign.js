import request from '@/utils/request';

//签字初始化
export function getSignHWInitialize  (params) {
    return request({
        url: process.env.VUE_APP_SIGN_API + '/HWPenSign/HWInitialize',
        method: 'GET'
    })
}

//签字完成返回
export function getHWSignImage  () {
    return request({
        url: process.env.VUE_APP_SIGN_API + '/HWPenSign/HWGetSign',
        method: 'GET'
    })
}


export function getHWFinalize () {
    return request({
        url: process.env.VUE_APP_SIGN_API + '/HWPenSign/HWFinalize',
        method: 'GET'
    })
}

