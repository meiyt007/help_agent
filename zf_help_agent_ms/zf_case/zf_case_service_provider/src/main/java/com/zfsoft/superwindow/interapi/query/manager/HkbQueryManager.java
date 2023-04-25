package com.zfsoft.superwindow.interapi.query.manager;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zfsoft.superwindow.interapi.query.manager.base.AbstractInterApiQueryStrategy;
import com.zfsoft.superwindow.service.easyquickcase.data.ResponseData;
import com.zfsoft.superwindow.service.interapi.data.ApiReqParams;
import com.zfsoft.superwindow.service.interapi.data.ApiResultElecQuery;
import com.zfsoft.superwindow.service.interapi.service.InterApiQueryStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class HkbQueryManager extends AbstractInterApiQueryStrategy implements InterApiQueryStrategy {

    @Value("${electronicLicense.hkbCxJkbm}")
    private String hkbCxJkbm;//户口本接口编码
    @Value(value = "${electronicLicense.accessToken}")
    private String accessToken;
    @Value(value = "${electronicLicense.appKey}")
    private String appKey;
    /**
     * 请求头 令牌 应用ID
     */
    private final Map<String,String> headers = new HashMap<String, String>(){
        {
            put("accessToken",accessToken);
            put("appKey",appKey);
        }
    };
    @Override
    public ResponseData query(ApiReqParams params) {
        log.info("调用户口本证照查询服务，参数："+params.toString());
        JSONObject obj = new JSONObject() ;
        obj.set("encryptedId",params.getEncryptedId());
        obj.set("workId",params.getWorkId());

        //调用浪潮接口 获取结果
        String result = HttpUtil.createPost(hkbCxJkbm).addHeaders(headers).form(obj).execute().body();
        //结果解析
        List<ApiResultElecQuery> ApiResultElecQueryList = new ArrayList<>();
        JSONObject object = new JSONObject(result);
        JSONObject dataObject = object.getJSONObject("data");
        JSONObject head = dataObject.getJSONObject("head");
        int status = head.getInt("status");
        if (status!=0){
            log.error("检索失败");
            return null;
        }
        JSONArray array = dataObject.getJSONObject("data").getJSONArray("dataList");
        if (array.size()!=0){
            for (Object value : array) {
                JSONObject retrievalShareObject = new JSONObject(value);
                ApiResultElecQuery apiResultElecQuery = JSONUtil.toBean(retrievalShareObject, ApiResultElecQuery.class);
                ApiResultElecQueryList.add(apiResultElecQuery);
            }
        }

        //return retrievalShareVoList;
        return null;
    }

    @Override
    public String getApiCode() {
        return hkbCxJkbm;
    }
}
