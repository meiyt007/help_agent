package com.zfsoft.superwindow.feign.settings;

import com.alibaba.fastjson.JSONObject;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.data.feign.Server;
import com.zfsoft.superwindow.data.feign.ServerInterface;
import com.zfsoft.superwindow.service.easyquickcase.data.ResponseData;
import com.zfsoft.superwindow.service.interapi.data.ApiReqParams;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: qiaol
 * @Description:
 * @Date: 2022/6/11 12:46
 */
@FeignClient(value = "${zfsoft.feign.middle}", contextId = "middleWeb")
public interface MiddleWebFeignService {

    /**
     *  通用接口定义查询接口
     * @param code
     * @param params
     * @return
     */
    @PostMapping("/web/commonApi/query/{code}")
    ApiResultSet queryApi(@PathVariable String code, @RequestBody ApiReqParams params);

    /**
     *  通用接口定义校验接口
     * @param code
     * @param params
     * @return
     */
    @PostMapping("/web/commonApi/validate/{code}")
    ApiResultSet<Boolean> validateApi(@PathVariable String code, @RequestBody ApiReqParams params);

    /**
     * 查询数据协同平台服务分类
     * @return
     */
    @GetMapping("/web/server/foreign/queryServerList")
    ApiResultSet<List<Server>> queryServerList();

    /**
     * 查询数据协同平台服务接口
     * @param serverOid
     * @return
     */
    @GetMapping("/web/serverInterface/foreign/queryServerInterfaceList")
    public ApiResultSet<List<ServerInterface>> queryForeignServerInterfaceList(@RequestParam(required = true, value = "serverOid") String serverOid);

    /**
     * 公积金查询个人信息
     */
    @GetMapping("/web/gold/searchPersonInfo")
    public ResponseData<JSONObject> searchPersonInfo(@RequestParam(required = true, value = "jgbm") String jgbm, @RequestParam(required = true, value = "zjhm") String zjhm,
                                                     @RequestParam("callType") String callType, @RequestParam(value = "interId", required = false) String interId);

    /**
     * 公积金退休、离休接口
     */
    @GetMapping("/web/gold/retireExtract")
    public ResponseData<JSONObject> retireExtract(@RequestParam(value = "tqfs") String tqfs, @RequestParam(value = "zrzxqc") String zrzxqc, @RequestParam(value = "tqyhzh") String tqyhzh, @RequestParam(value = "skyh") String skyh,
                                                  @RequestParam(value = "beizhu", required = false) String beizhu, @RequestParam(value = "zjhm") String zjhm, @RequestParam(value = "skrlx") String skrlx, @RequestParam(value = "ywlsh") String ywlsh,
                                                  @RequestParam("callType") String callType, @RequestParam(value = "interId", required = false) String interId);

    /**
     * 公积金获取档案材料接口
     */
    @GetMapping("/web/gold/archivesSearch")
    public ResponseData<JSONObject> archivesSearch(@RequestParam(value = "ygdh") String ygdh, @RequestParam(value = "ywfl") String ywfl, @RequestParam(value = "ywlb") String ywlb,
                                                   @RequestParam(value = "jgbm") String jgbm, @RequestParam("callType") String callType, @RequestParam(value = "interId", required = false) String interId);
    /**
     * 公积金上传图片接口
     */
    @PostMapping("/web/gold/archivesUpload")
    public ResponseData<String> archivesUpload(@RequestParam(value = "file") HashMap<String, Object> file, @RequestParam(value = "filecode") String filecode, @RequestParam(value = "scanpages") String scanpages,
                                               @RequestParam(value = "filename") String filename, @RequestParam(value = "ygdh") String ygdh, @RequestParam(value = "ywlsh") String ywlsh,
                                               @RequestParam("callType") String callType, @RequestParam(value = "interId", required = false) String interId);

    /**
     * 发送短信
     */
    @PostMapping("/web/sms/send/sendSms")
    public boolean sendAccessSms(@RequestParam(value = "phone") String phone, @RequestParam(value = "message") String message, @RequestParam(value = "templetid") String templetid);

    //查找余额和可提取余额
    @GetMapping("/web/gold/searchBalance")
    public ResponseData<JSONObject> searchBalance(@RequestParam(value = "grzh") String grzh);


    //获取利息
    @GetMapping("/web/gold/getInterest")
    public ResponseData<JSONObject> getInterest(@RequestParam(value = "grzh") String grzh);

    //判断否可以提取
    @GetMapping("/web/gold/extractFlag")
    public ResponseData<JSONObject> extractFlag(@RequestParam(value = "grzh") String grzh);

}
