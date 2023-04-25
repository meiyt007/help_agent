package com.zfsoft.superwindow.interapi.validate.manager;

import com.alibaba.fastjson.JSONObject;
import com.zfsoft.superwindow.feign.settings.MiddleWebFeignService;
import com.zfsoft.superwindow.interapi.validate.manager.base.AbstractInterApiValidateStrategy;
import com.zfsoft.superwindow.service.easyquickcase.data.ResponseData;
import com.zfsoft.superwindow.service.interapi.data.ApiReqParams;
import com.zfsoft.superwindow.service.interapi.service.InterApiValidateStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author ChangSheng
 * @Date 14:10 2022/6/24
 * @Description
 **/
@Service
@Slf4j
public class ExtractFlagManager extends AbstractInterApiValidateStrategy implements InterApiValidateStrategy {

    @Autowired
    private MiddleWebFeignService middleWebFeignService;

    @Override
    public String getApiCode() {
        return "extractFlag";
    }

    public ExtractFlagManager() { register(); }

    @Override
    public ResponseData validate(ApiReqParams params) {

        ResponseData<Boolean> response = new ResponseData<>();

        if (StringUtils.isEmpty(params.getUniqueCode())) {
            response.error("身份证号不能为空");
            response.setData(false);
            return response;
        }
        if (StringUtils.isEmpty(params.getUniqueCode())) {
            response.error("身份证号不能为空");
            response.setData(false);
            return response;
        }
        //获取机构编码
        String jgbm = "01";
        //配合查询个人信息的接口 + 公积金是否可提取
        // TODO 记录调用日志传参
        ResponseData<JSONObject> data = middleWebFeignService.searchPersonInfo(jgbm, params.getUniqueCode(), "1", "");
        if (!(ResponseData.DEFAULT_SUCCESS_CODE.equals(data.getCode())
                && data.getData() != null)) {
            // TODO 记录日志
            response.error("查询公积金个人账户信息失败");
            response.setData(false);
            return response;
        }

        //获取公积金账号
        String grzh = data.getData().getString("grzh");

        //查询公积金是否可取
        ResponseData<JSONObject> dataJson = middleWebFeignService.extractFlag(grzh);
        if (!(ResponseData.DEFAULT_SUCCESS_CODE.equals(dataJson.getCode())
                && dataJson.getData() != null)) {
            // TODO 记录日志
            response.error("查询公积金是否可取失败");
            response.setData(false);
            return response;
        }
        if(Strings.isNotBlank(dataJson.getData().toJSONString())){
            //不为空，则不允许提取
            response.setCode(200);
            response.setData(false);
            response.setMessage(dataJson.getData().getString("msg"));
            response.setSuccess(true);
            return response;
        }
        response.success(true);
        return response;
    }
}
