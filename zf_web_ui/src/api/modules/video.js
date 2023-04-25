import request from "@/util/request";
const baseUrl = '/dev-api/case-service';
export function loginByAccount() {
    const data = {
        account:'wangyuhang',
        pcode:'123456',
        checkCode:'',
        haType:'1',
    };
    return request({
      url: baseUrl+ "/ha/login/loginByAccount",
      method: "post",
      params: data,
    });
}

export function helpInfo() {
    const data = {
        name:'wangyuhang',
        cardNo:'321897199709189114',
        phone:'17172190635',
        companyName:'',
        companyCode:'',
        workUserId:'234261805889966080'
    };
    return request({
      url: baseUrl+ "/ha/scan/helpInfo",
      method: "post",
      params: data,
    });
}

export function getSig() {
    const data = {
        identifier:'1000',
        room:'1000',
        expire:1000
    }
    return request({
      url: baseUrl+ "/ha/caSig/getSig",
      method: "get",
      params:data
    });
}
// 创建视频帮办房间
export function createRoom(data) {
  return request({
    url: baseUrl+ "/ha/video/createRoom",
    method: "post",
    data:data
  });
}

// 加入房间
export function joinRoom(data) {
  return request({
    url: baseUrl+ "/ha/video/joinRoom",
    method: "get",
    params:data
  });
}