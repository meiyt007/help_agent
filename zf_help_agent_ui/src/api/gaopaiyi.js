import request from '@/utils/request';
import { GAOPAIYI } from '@/utils/config';
// 高拍仪硬件名称
const USB2CAMERA = 'USB 2.0 Camera';
/** 高拍仪前缀地址 */
import store from '@/store';
/** 开发测试地址 */
// let URL_PRE = () => 'http://172.168.252.162:6543';
let URL_PRE = '';
if (process.env.NODE_ENV === 'development') {
  URL_PRE = () => 'http://172.168.252.162:6543';
} else {
  URL_PRE = () => store.state.config.deviceMap?.[GAOPAIYI];
}

// 获取摄像头数量
export function getCamerasNum () {
  console.log('start getCamerasNum...');
  return request({
    url: URL_PRE() + '/GetDeviceCount',
    method: 'get',
    isGpy: true,
  })
}

// 获取分辨率数量
export function getResolution () {
  return request({
    url: URL_PRE() + '/GetResolution ',
    method: 'get',
    isGpy: true,
  })
}

/** 
 * 视频预览 打开视频
 * @param {string} dev_idx 摄像头索引
 * @param {string} res_id 分辨率索引
 * @param {string} pixfmt 出图模式(目前不用)
 */
export async function startPreview (params) {
  try {
    const { data, returnCode, returnMsg } = await getAllDisplayInfo();
    if (returnCode !== 0 || data.length === 0) return Error(returnMsg);

    // 先看是否能找到这个名字
    let dev_idx = data?.find?.(item => item.name === USB2CAMERA);
    if (!dev_idx) {
      dev_idx = data
        .map(item => ({ width: item?.resolution?.[0]?.width, dev_idx: item.dev_idx }))
        .sort((a, b) => b.width - a.width)?.[0]?.dev_idx ?? data[data.length - 1].dev_idx;
    } else {
      dev_idx = dev_idx.dev_idx;
    }

    return request({
      url: URL_PRE() + '/StartPreview?dev_idx=' + dev_idx + '&res_id=' + params.res_id + '&pixfmt=' + params.pixfmt,
      method: 'get',
      isGpy: true,
    })
  } catch (error) {
    return Error(error);
  }
}

// 停止预览
export function stopPreview () {
  return request({
    url: URL_PRE() + '/StopPreview?dev_idx=1',
    method: 'get',
    isGpy: true,
  })
}
// 拍照
export function takePhoto (params) {
  return request({
    url: URL_PRE() + '/getPic?savepath=' + params.savepath + '&quality=' + params.quality,
    method: 'get',
    isGpy: true,
  })
}
// 获取一帧图像 可设置定时器，通过轮询实现视频播放
export function getFrame () {
  return request({
    url: URL_PRE() + '/getFrame',
    method: 'get',
    isGpy: true,
  })
}
// 获取视频旋转角度
export function getRotate () {
  return request({
    url: URL_PRE() + '/getRotate',
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
// 开启纠偏
export function enalbeDeskImage () {
  return request({
    url: URL_PRE() + '/EnalbeDeskImage',
    method: 'get',
    isGpy: true,
  })
}
// 读取二代证
export function getIDCardInfo () {
  return request({
    url: URL_PRE() + '/getIDCardInfo',
    method: 'get',
    isGpy: true,
  })
}
// 身份证图片正反面合成
//先step = 1拍摄身份证正面照片，再 step = 2 拍摄身份证反面照片，服务后台会直接合成新图片并把图片返回
export function composeIDcardPic () {
  return request({
    url: URL_PRE() + '/composeIDcardPic',
    method: 'get',
    isGpy: true,
  })
}

// 一次性获取设备所有信息
export function getAllDisplayInfo () {
  console.log('start getAllDisplayInfo...');
  return request({
    url: URL_PRE() + '/GetAllDisplayInfo',
    method: 'get',
    isGpy: true,
  })
}