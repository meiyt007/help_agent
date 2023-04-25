import request from '@/utils/request'
import { PUSH } from '@/utils/config';
import store from '@/store';
// const URL_PRE = () => store.state.config.deviceMap?.[PUSH];

/** 开发测试地址 */
const IPCONFIGS = ['http://172.168.252.182:8899'];
let URL_PRE = '';
// if (IPCONFIGS.includes(window.location.origin) && process.env.NODE_ENV === 'development') {
//     URL_PRE = () => 'http://172.168.252.137:9094';
// } else {
//     URL_PRE = () => store.state.config.deviceMap?.[PUSH];
// }
if (process.env.NODE_ENV === 'development') {
    URL_PRE = () => 'http://172.168.253.21:9094';
} else {
    URL_PRE = () => store.state.config.deviceMap?.[PUSH];
}

//打开摄像头
export function openCamera (params) {
    return request({
        url: URL_PRE() + '/camera/Open',
        method: 'post'
    })
}

//获取图像信息
export function getImage (params) {
    return request({
        url: URL_PRE() + '/camera/GetImage',
        method: 'post'
    })
}
//获取身份证信息
export function getIdCardInfo (params) {
    return request({
        url: URL_PRE() + '/idcard/GetData',
        method: 'get'
    })
}

//获取身份证信息
export function openIdCard (params) {
    return request({
        url: URL_PRE() + '/idcard/Open',
        method: 'get'
    })
}
//查找身份证设备
export function findIdCard () {
    return request({
        url: URL_PRE() + '/idcard/FindSave',
        method: 'get'
    })
}

//人证比对
export function comparsionFace (data) {
    return request({
        url: process.env.VUE_APP_ZC_ROUTE_PATH + '/hardwareScan/comparFace',
        method: 'post',
        /*headers:{
          "Content-Type":"application/json;charset=UTF-8",
        },*/
        data: data
    })
}

export function jxImage (data) {
    return request({
        url: process.env.VUE_APP_ZC_ROUTE_PATH + '/hardwareScan/getDataIdCardImage?imgdata=' + data,
        method: 'post',
    })
}
//保存人证比对信息
export function saveRzbdInfo (data) {
    return request({
        url: process.env.VUE_APP_ZC_ROUTE_PATH + '/witnessComparison/saveOrUpdate',
        method: 'post',
        data: data
    })
}

// 查询人证比对信息
export function queryWitnessComparisonByCaseOid (caseOid) {
    return request({
        url: process.env.VUE_APP_ZC_ROUTE_PATH + '/witnessComparison/queryWitnessComparisonByCaseOid',
        method: 'get',
        params: {
            caseOid
        }
    })
}

// 附件上传
export function uploadCompareFile (data) {
    return request({
        url: process.env.VUE_APP_ZC_ROUTE_PATH + '/witnessComparison/uploadCompareFile',
        method: 'POST',
        headers: {
            "Content-Type": "multipart/form-data",
        },
        data: data
    })
}


// 查询人证比对信息
export function getWitnessComparisonBase64Img (caseOid) {
    return request({
        url: process.env.VUE_APP_ZC_ROUTE_PATH + '/witnessComparison/queryBase64ImgWitnessComparisonByCaseOid',
        method: 'get',
        params: {
            caseOid
        }
    })
}


