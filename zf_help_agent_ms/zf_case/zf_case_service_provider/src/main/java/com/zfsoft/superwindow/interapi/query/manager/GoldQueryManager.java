package com.zfsoft.superwindow.interapi.query.manager;

import com.alibaba.fastjson.JSONObject;
import com.zfsoft.superwindow.feign.settings.MiddleWebFeignService;
import com.zfsoft.superwindow.feign.settings.data.GoldBalance;
import com.zfsoft.superwindow.interapi.query.manager.base.AbstractInterApiQueryStrategy;
import com.zfsoft.superwindow.service.easyquickcase.data.ResponseData;
import com.zfsoft.superwindow.service.interapi.data.ApiReqParams;
import com.zfsoft.superwindow.service.interapi.service.InterApiQueryStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author ChangSheng
 * @Date 10:04 2022/6/24
 * @Description 公积金余额查询
 **/
@Service
@Slf4j
public class GoldQueryManager extends AbstractInterApiQueryStrategy implements InterApiQueryStrategy {

    @Autowired
    private MiddleWebFeignService middleWebFeignService;

    @Override
    public String getApiCode() {
        return "goldQuery";
    }

    public GoldQueryManager() {
        register();
    }

    @Override
    public ResponseData query(ApiReqParams params) {

        ResponseData<GoldBalance> response = new ResponseData<>();
        GoldBalance goldBalance = new GoldBalance();

        if (StringUtils.isEmpty(params.getUniqueCode())) {
            response.error("身份证号不能为空");
            return response;
        }
        //获取机构编码
        String jgbm = "01";
        //配合查询个人信息的接口 + 查询公积金余额
        // TODO 记录调用日志传参
        ResponseData<JSONObject> data = middleWebFeignService.searchPersonInfo(jgbm, params.getUniqueCode(), "1", "");
        if (!(ResponseData.DEFAULT_SUCCESS_CODE.equals(data.getCode())
                && data.getData() != null)) {
            // TODO 记录日志
            response.error("查询公积金个人账户信息失败");
            return response;
        }
        goldBalance.setCardNumber(params.getUniqueCode());
        //获取公积金账号
        String grzh = data.getData().getString("grzh");
        String name = data.getData().getString("xingming");
        goldBalance.setName(name);
        goldBalance.setGrzh(grzh);
        if(Strings.isBlank(grzh)){
            response.error("查询公积金个人账户信息失败,公积金账号为空");
            return response;
        }
        //查询公积金余额
        ResponseData<JSONObject> balanceJson = middleWebFeignService.searchBalance(grzh);
        if (!(ResponseData.DEFAULT_SUCCESS_CODE.equals(balanceJson.getCode())
                && balanceJson.getData() != null)) {
            // TODO 记录日志
            response.error("查询公积金余额失败");
            return response;
        }
        goldBalance.setGrzhye(balanceJson.getData().getString("ktqe"));
        //查询利息
        ResponseData<JSONObject> InterestJson = middleWebFeignService.getInterest(grzh);
        if (!(ResponseData.DEFAULT_SUCCESS_CODE.equals(InterestJson.getCode())
                && InterestJson.getData() != null)) {
            // TODO 记录日志
            response.error("查询公积金利息失败");
            return response;
        }
        goldBalance.setInterest(InterestJson.getData().getString("zjlx"));

        float total = Float.valueOf(goldBalance.getGrzhye()) + Float.valueOf(goldBalance.getInterest());
        goldBalance.setTotal(String.valueOf(total));

        response.success(goldBalance);
        return response;
    }


}
