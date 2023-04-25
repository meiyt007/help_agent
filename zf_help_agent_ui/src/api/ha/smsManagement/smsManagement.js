import request from "@/utils/request";

//查询所有的帮代办人员
export function getAppointmentWorkUser() {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/appointmentManger/getAllHelpWorkUser', method: 'get'
  })
}

//查询预约人员信息列表
export function queryPage(query) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/smsManager/queryPage', method: 'post', params: query
  })

}
