/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-18 10:05:28
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-07 14:35:30
 * @FilePath: \hpNewHall\src\api\modules\home.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import request from "@/utils/request";

/**首页————获取用户信息**/
export function getLoginUser(data) {
  return request({
    url: "/web/getLoginUser",
    methods: "get",
    data: data,
  });
}

/**首页————根据区划及事项办理类型查询机构列表**/
export function listOrganByDistrictAndService(data) {
  return request({
    url: "/base-api/web/listOrganByDistrictAndService",
    methods: "get",
    data: data,
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
}
