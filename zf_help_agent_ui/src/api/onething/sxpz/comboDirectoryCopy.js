import request from '@/utils/request';
 
//增加/修改
  export function save(data) {
    return request({
      url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directoryCopy/copyComboDirectory/',
      method: 'post',
      data: data
    })
  }