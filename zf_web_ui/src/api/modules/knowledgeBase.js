/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-20 10:36:44
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-07 14:35:33
 * @FilePath: \zf_web_ui\src\api\modules\knowledgeBase.js
 * @Description: 知识库查询
 */
import request from "@/utils/request";
const baseUrl = process.env.VUE_APP_CASE_API;
// 知识库查询
export function knowledgeQuery(data) {
  return request({
    url: baseUrl + "/ha/knowledge/query",
    methods: "get",
    params: data,
  });
}
