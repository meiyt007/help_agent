import request from "@/utils/request";

//查询消息列表
export function getMessageList(query) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/messageManager/queryMessagePage', method: 'post', params: query
  })

}
