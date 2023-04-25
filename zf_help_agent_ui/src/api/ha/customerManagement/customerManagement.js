import request from "@/utils/request";

//查询所有的帮代办人员
export function getAppointmentWorkUser() {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/appointmentManger/getAllHelpWorkUser', method: 'get'
  })
}

//查询预约人员信息列表
export function getAppointmentList(query) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/appointmentManger/queryAppointmentPage', method: 'post', params: query
  })

}
