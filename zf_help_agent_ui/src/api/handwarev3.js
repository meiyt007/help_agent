import request from '@/utils/request';
import { GAOPAIYI } from '@/utils/config';
// 高拍仪硬件名称
const USB2CAMERA = 'CHENG_ZHE';
/** 高拍仪前缀地址 */
import store from '@/store';
/** 开发测试地址 */
// let URL_PRE = () => 'http://172.168.252.162:6543';
let URL_PRE = '';
if (process.env.NODE_ENV === 'development') {
  URL_PRE = () => 'http://172.168.253.207:9094';
} else {
  URL_PRE = () => store.state.config.deviceMap?.[GAOPAIYI];
}


//打开视频
export async function startnewPreview () {
  return request({
    url: URL_PRE() + '/StartPreview',
    method: 'get',
    isGpy: true,
  })
}

// 停止预览
export function stopnewPreview () {
  return request({
    url: URL_PRE() + '/StopPreview',
    method: 'get',
    isGpy: true,
  })
}
// 拍照
export function takenewPhoto (params) {
  return request({
    url: URL_PRE() + '/getPic',
    method: 'get',
    isGpy: true,
  })
}

// 拍照
export function takePic () {
  return request({
    url: URL_PRE() + '/SavePic',
    method: 'get',
    isGpy: true,
  })
}

// 视频旋转
export function rotateVideo (params) {
  return request({
    url: URL_PRE() + '/Rotate',
    method: 'get',
    isGpy: true,
    params
  })
}

// 打开身份证
export function openIdcardv3 () {
  return request({
    url: URL_PRE() + '/idcard/Open',
    method: 'get',
    isGpy: true,
  })
}
// 关闭身份证
export function closeIdcardv3 (params) {
  return request({
    url: URL_PRE() + '/idcard/Close',
    method: 'get',
    isGpy: true,
    params
  })
}
// 身份证获取数据
export function getdataIdcardv3 () {
  return request({
    url: URL_PRE() + '/idcard/GetData',
    method: 'get',
    isGpy: true,
  })
}