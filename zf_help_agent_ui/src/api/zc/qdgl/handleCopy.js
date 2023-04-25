import request from '@/utils/request';
 
//增加/修改
  export function save(data) {
    return request({
      url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceCopy/copySxService',
      method: 'post',
      data: data
    })
  }