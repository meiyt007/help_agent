var VUE_APP_BASE_API = '/dev-api';
//var VUE_APP_ZC_ROUTE_PATH='/dzcpt-lss/zc';
var VUE_APP_ZC_ROUTE_PATH = "http://172.168.252.59:9090";
var commonUrl = {



  getZcUrl: () => {
    var code = "ZCPZDZ";
    var urlImg = VUE_APP_BASE_API + '/settings/security/config/getSysConfigByCode';
    var dataimg = {
      "code": code,
    }
    $.ajax({
      url: urlImg, //请求地址
      type: "get", //请求方式
      data: dataimg,
      dataType: 'json',
      success: (data) => { //成功回调
        VUE_APP_ZC_ROUTE_PATH = data.data.value;
        return VUE_APP_ZC_ROUTE_PATH;
      },
      fail: (data) => {
        console.log('fail')
      }
    });
  },


}
