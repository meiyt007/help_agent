package com.zfsoft.outer.inter.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.outer.inter.pojo.*;
import com.zfsoft.outer.inter.util.ClientServer;
import com.zfsoft.outer.inter.util.HttpRequestUtil;
import com.zfsoft.outer.inter.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
@Slf4j
@Controller
@RequestMapping(value = "/outer/inter")
public class OuterInterController {
    //用户
//    private static String account ="bangb";
//    //密码
//    private static String password ="1";
    private static String account ="scj016";
    //密码
    private static String password ="123456";
    @Value("${zfsoft.inter.url}")
    private  String loginTokenUrl;

    @Value("${zfsoft.inter.applicantConfirm}")
    private  String applicantConfirmUrl;

    @Value("${zfsoft.inter.addApplyStuffUrl}")
    private  String addApplyStuffUrl;

    @Value("${zfsoft.inter.submitApplyUrl}")
    private  String submitApplyUrl;

    @Value("${zfsoft.inter.addStuffAttachment}")
    private  String addStuffAttachmentUrl;

    @Value("${zfsoft.inter.pushsxUrl}")
    private  String pushsxUrl;

    @Value("${zfsoft.inter.stUrl}")
    private  String stUrl;

    @Value("${zfsoft.inter.attaPushUrl}")
    private  String attaPushUrl;

    @Value("${zfsoft.inter.policyPushUrl}")
    private  String policyPushUrl;

    @Value("${zfsoft.inter.evalPushUrl}")
    private  String evalPushUrl;

    @Value("${zfsoft.inter.searchWorkflowCataLog}")
    private  String searchWorkflowCataLogUrl;
    @Value("${zfsoft.inter.searchWorkflowNodeInfo}")
    private  String searchWorkflowNodeInfoUrl;
    @Value("${zfsoft.inter.handlePostNode}")
    private  String handlePostNodeUrl;

    @Value("${zfsoft.inter.saveDateUrl}")
    private  String saveDateUrl;
    @Resource
    private JdbcTemplate jdbcTemplate;



    /**
     * 6.6 领取方式 -万达
     * @param map    请求数据
     * @Author:wangyh
     * @Date: 2022-09-16 14:46
            */
    @ResponseBody
    @PostMapping("/saveDate")
    public String saveDate(@RequestParam Map<String,Object> map) throws IOException {
        HaInterRecord interRecord = new HaInterRecord();
        JSONObject jsonObjectResult = new JSONObject();
//        List<ResponseHandlePostNodeVo> applyVoList = new ArrayList<>();
        Long begin1 = System.currentTimeMillis();
        try {
            //测试数据
            /*JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("applyId","ac4644f1-28bc-4217-ad99-ab2613641051");
            jsonObject3.put("certWay","");
            jsonObject3.put("receiver","");
            jsonObject3.put("revPhone","");
            jsonObject3.put("revZipcode","");
            jsonObject3.put("revProvince","");
            jsonObject3.put("revCity","");
            jsonObject3.put("revArea","");
            jsonObject3.put("revAddress","");
            jsonObject3.put("sender","");
            jsonObject3.put("sndPhone","");
            jsonObject3.put("sndZipcode","");
            jsonObject3.put("sndProvince","");
            jsonObject3.put("sndCity","");
            jsonObject3.put("sndArea","");
            jsonObject3.put("sndAddress","");
            jsonObject3.put("expressCompany","");*/
//            String json = JSON.toJSONString(jsonObject3);
            String json = (String) map.get("json");
            log.info("json:{}",json);
            String result = ClientServer.sendPost(saveDateUrl, json,"utf-8",this.getToken());
            JSONObject jsonObject1 = JSON.parseObject(result);
            Integer code = (Integer) jsonObject1.get("code");
            if(1000 == code){
                jsonObjectResult.put("code","200");
                jsonObjectResult.put("message","接口成功");
            }else{
                jsonObjectResult.put("code","500");
                jsonObjectResult.put("message","接口失败");
            }
            interRecord.setName("6.6领取方式");
            interRecord.setUrl("/saveDate");
            interRecord.setMethod("post");
            interRecord.setSourceIp("172.21.179.149");
            interRecord.setParam(json);
            interRecord.setResult(result);
            interRecord.setResultStatus("1");
            /**
             * 封装企业信息
             */
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (!"1000".equals(jsonObject.get("code").toString())) {
                throw new Exception("第三方接口调用错误");
            }
        } catch (Exception ex) {
            jsonObjectResult.put("code","501");
            jsonObjectResult.put("message",null);
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;
            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return jsonObjectResult.toJSONString();
    }

    /**
     * 触发流程操作 -万达
     * @param map    请求数据
     * @Author:wangyh
     * @Date: 2022-09-16 14:46
     */
    @ResponseBody
    @PostMapping("/handlePostNode")
    public String handlePostNode(@RequestParam Map<String,Object> map) throws IOException {
        HaInterRecord interRecord = new HaInterRecord();
        JSONObject jsonObjectResult = new JSONObject();
        List<ResponseHandlePostNodeVo> applyVoList = new ArrayList<>();
        Long begin1 = System.currentTimeMillis();
        try {
            //测试数据
            /*JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("workflowId","");
            jsonObject3.put("applyID","5e1f3435-ecca-42f2-a778-8ae746c03f2a"); //必传
            jsonObject3.put("processCode","HELP_UPLOAD");
            jsonObject3.put("processName","帮办提交");
            jsonObject3.put("processDetail","");
            jsonObject3.put("autoProcess","");
            jsonObject3.put("startTime","");
            jsonObject3.put("endTime","");
            jsonObject3.put("nodeDetailId","");
            jsonObject3.put("zapplyID","");
            String json = JSON.toJSONString(jsonObject3);*/
            String json = (String) map.get("json");
            log.info("json:{}",json);
            String result = ClientServer.sendPost(handlePostNodeUrl, json,"utf-8",this.getToken());
            JSONObject jsonObject1 = JSON.parseObject(result);
            Integer code = (Integer) jsonObject1.get("code");
            if(1000 == code){
                JSONArray  dataArr = (JSONArray ) jsonObject1.get("data");
                //转数组
                for(int i=0;i<dataArr.size();i++){
                    JSONObject jsonObject = (JSONObject) dataArr.get(i);
                    String applyjson = jsonObject.toJSONString();
                    ResponseHandlePostNodeVo responseHandlePostNodeVo = (ResponseHandlePostNodeVo) JsonUtil.jsonToObject(applyjson,ResponseHandlePostNodeVo.class);
                    applyVoList.add(responseHandlePostNodeVo);
                }
                jsonObjectResult.put("code","200");
                jsonObjectResult.put("applyVoList",applyVoList);
            }else{
                jsonObjectResult.put("code","500");
                jsonObjectResult.put("applyVoList",null);
            }
            interRecord.setName("获取工作流列表");
            interRecord.setUrl("/handlePostNode");
            interRecord.setMethod("post");
            interRecord.setSourceIp("172.21.179.149");
            interRecord.setParam(json);
            interRecord.setResult(result);
            interRecord.setResultStatus("1");
            /**
             * 封装企业信息
             */
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (!"1000".equals(jsonObject.get("code").toString())) {
                throw new Exception("第三方接口调用错误");
            }
        } catch (Exception ex) {
            jsonObjectResult.put("code","501");
            jsonObjectResult.put("applyVoList",null);
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;
            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return jsonObjectResult.toJSONString();
    }

    /**
     * 18.5获取工作流列表 -万达
     * @param map    请求数据
     * @Author:wangyh
     * @Date: 2022-09-16 14:46
     */
    @ResponseBody
    @PostMapping("/searchWorkflowNodeInfo")
    public String searchWorkflowNodeInfo(@RequestParam Map<String,Object> map) throws IOException {
        HaInterRecord interRecord = new HaInterRecord();
        JSONObject jsonObjectResult = new JSONObject();
        Long begin1 = System.currentTimeMillis();
        try {
            //测试数据
//            JSONObject jsonObject3 = new JSONObject();
//            jsonObject3.put("workflowId","097bc422-b5cf-40a3-a87b-5a261e3dbdaa");
//            String json = JSON.toJSONString(jsonObject3);
            String json = (String) map.get("json");
            log.info("json:{}",json);
            String result = ClientServer.sendPost(searchWorkflowNodeInfoUrl, json,"utf-8",this.getToken());
            JSONObject jsonObject1 = JSON.parseObject(result);
            Integer code = (Integer) jsonObject1.get("code");
            if(1000 == code){
                JSONArray  dataArr = (JSONArray ) jsonObject1.get("data");
                ResponseWorkflowNodeVo responseWorkflowNodeVo = null;
                //转数组
                for(int i=0;i<dataArr.size();i++){
                    JSONObject jsonObject = (JSONObject) dataArr.get(i);
                    String applyjson = jsonObject.toJSONString();
                     responseWorkflowNodeVo = (ResponseWorkflowNodeVo) JsonUtil.jsonToObject(applyjson,ResponseWorkflowNodeVo.class);
                    if(responseWorkflowNodeVo.getProcessCode().equals("HELP_UPLOAD")){
                        break;
                    }
                }
                jsonObjectResult.put("code","200");
                jsonObjectResult.put("responseWorkflowNodeVo",responseWorkflowNodeVo);
            }else{
                jsonObjectResult.put("code","500");
                jsonObjectResult.put("responseWorkflowNodeVo",null);
            }
            interRecord.setName("获取工作流列表");
            interRecord.setUrl("/searchWorkflowNodeInfo");
            interRecord.setMethod("post");
            interRecord.setSourceIp("172.21.179.149");
            interRecord.setParam(json);
            interRecord.setResult("数据太长,无法放入");
            interRecord.setResultStatus("1");
            /**
             * 封装企业信息
             */
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (!"1000".equals(jsonObject.get("code").toString())) {
                throw new Exception("第三方接口调用错误");
            }
        } catch (Exception ex) {
            jsonObjectResult.put("code","501");
            jsonObjectResult.put("responseWorkflowNodeVo",null);
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;
            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return jsonObjectResult.toJSONString();
    }

    /**
     * 18.4获取工作流列表 -万达
     * @param map    请求数据
     * @Author:wangyh
     * @Date: 2022-09-16 14:46
     */
    @ResponseBody
    @PostMapping("/searchWorkflowCataLog")
    public String searchWorkflowCataLog(@RequestParam Map<String,Object> map) throws IOException {
        HaInterRecord interRecord = new HaInterRecord();
        JSONObject jsonObjectResult = new JSONObject();
        ResponseWorkflowCatVo responseWorkflowCatVo =null;
        Long begin1 = System.currentTimeMillis();
        try {
            //测试数据
            /*JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("page",1);
            jsonObject3.put("size",10);
            jsonObject3.put("organNodeId","");
            jsonObject3.put("organName","");
            jsonObject3.put("itemId","");
            jsonObject3.put("itemName","");
            jsonObject3.put("statusId","f5763c7a-b714-4589-860c-88d1d38be40a");
            jsonObject3.put("statusName","");
            jsonObject3.put("databaseID","262EA9AF-8D15-4CD3-8B20-18D614AB2453"); //写死
            jsonObject3.put("workflowId","");
            String json = JSON.toJSONString(jsonObject3);*/
            String json = (String) map.get("json");
            log.info("json:{}",json);
            String result = ClientServer.sendPost(searchWorkflowCataLogUrl, json,"utf-8",this.getToken());
            JSONObject jsonObject1 = JSON.parseObject(result);
            Integer code = (Integer) jsonObject1.get("code");
            if(1000 == code){
                JSONArray  dataArr = (JSONArray ) jsonObject1.get("data");
                //转数组
                for(int i=0;i<dataArr.size();i++){
                    JSONObject jsonObject = (JSONObject) dataArr.get(i);
                    String applyjson = jsonObject.toJSONString();
                    responseWorkflowCatVo = (ResponseWorkflowCatVo) JsonUtil.jsonToObject(applyjson,ResponseWorkflowCatVo.class);
                }
                jsonObjectResult.put("code","200");
                jsonObjectResult.put("responseWorkflowCatVo",responseWorkflowCatVo);
            }else{
                jsonObjectResult.put("code","500");
                jsonObjectResult.put("responseWorkflowCatVo",null);
            }
            interRecord.setName("获取工作流列表");
            interRecord.setUrl("/searchWorkflowCataLog");
            interRecord.setMethod("post");
            interRecord.setSourceIp("172.21.179.149");
            interRecord.setParam(json);
            interRecord.setResult(result);
            interRecord.setResultStatus("1");
            /**
             * 封装企业信息
             */
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (!"1000".equals(jsonObject.get("code").toString())) {
                throw new Exception("第三方接口调用错误");
            }
        } catch (Exception ex) {
            jsonObjectResult.put("code","501");
            jsonObjectResult.put("responseWorkflowCatVo",null);
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;
            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return jsonObjectResult.toJSONString();
    }
    /**
     * 获取核酸检测信息
     *
     * @param name   姓名
     * @param cardNo 身份证号码
     * @param phone  手机号
     * @return 返回核酸检测信息
     * @Author:zhaobf
     * @Date: 2022-08-18 14:46
     */
    @ResponseBody
    @GetMapping("/nucleicInfo")
    public ApiResultSet nucleicInfo(String name, String cardNo, String phone) {
        HaInterRecord interRecord = new HaInterRecord();
        List<Map<String, String>> maps = new ArrayList<>();

        String code = RandomUtil.randomNumbers(8);
        String date = DateUtil.format(new Date(), DatePattern.NORM_DATETIME_FORMAT);
        String result = "[{\"Code\":\"" + code + "\",\"checkAddress\":\"合肥市庐阳区金丰广场\",\"checkTime\":\"" + date + "\",\"testingTime\":\"" + date + "\",\"checkStatus\":\"1\"}]";
        Long begin1 = System.currentTimeMillis();

        try {
            interRecord.setName("核酸检测");
            interRecord.setUrl("/nucleicInfo");
            interRecord.setMethod("get");
            interRecord.setSourceIp("192.168.10.20");
            interRecord.setParam("{\"name\": \"" + name + "\", \"cardNo\": \"" + cardNo + "\", \"phone\": \"" + phone + "\"}");
            interRecord.setResult(result);
            interRecord.setResultStatus("1");
            //封装核酸检测
            JSONArray array = JSON.parseArray(result);

            for (Object obj : array) {
                Map<String, String> map = new HashMap<>();
                JSONObject object = (JSONObject) JSONObject.toJSON(obj);
                map.put("nucleicCode", (String) object.get("Code"));
                map.put("nucleicCollectionPoint", (String) object.get("checkAddress"));
                map.put("nucleicCollectionTime", (String) object.get("checkTime"));
                map.put("nucleicTestingTime", (String) object.get("testingTime"));
                map.put("nucleicResult", "1".equals(object.get("checkStatus")) ? "阴性" : "阳性");
                map.put("nucleicResultCode", (String) object.get("checkStatus"));
                maps.add(map);
            }

        } catch (Exception ex) {
//            ex.printStackTrace();
            interRecord.setResultStatus("2");
            interRecord.setResult("调用失败" + ex.getMessage());
            return new ApiResultSet<>(500, "获取核酸信息错误", ex.getMessage());
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;
            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return ApiResultSet.ok("获取核酸信息成功", maps);
    }

    /**
     * 获取企业信息
     *
     * @param companyName 企业名称
     * @param companyCode 企业统一信用编码
     * @Author:zhaobf
     * @Date: 2022-08-18 14:46
     */
    @ResponseBody
    @GetMapping("/getCompanyInfo")
    public ApiResultSet getCompanyInfo(String companyName, String companyCode) {
        HaInterRecord interRecord = new HaInterRecord();
        Map<String, String> map = new HashMap<>();
        String result = "{\n" +
                "    \"data\": {\n" +
                "        \"companyName\": \"湖北XXXX有限公司\",\n" +
                "        \"companyCode\": \"123123123\",\n" +
                "        \"bussinessAddress\": \"湖北省新竹县新宁县\",\n" +
                "        \"bussinessNature\": \"湖北\",\n" +
                "        \"legalPerson\": \"赵某某\",\n" +
                "        \"registeredCapital\": \"100万\",\n" +
                "        \"bussinessLimit\": \"2023\",\n" +
                "        \"legalPersonCardNo\": \"330411111111111111\",\n" +
                "        \"companyType\": \"1\"\n" +
                "    },\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"nulla\"\n" +
                "}";
        Long begin1 = System.currentTimeMillis();

        try {
            interRecord.setName("企业信息");
            interRecord.setUrl("/getCompanyInfo");
            interRecord.setMethod("get");
            interRecord.setSourceIp("192.168.10.20");
            interRecord.setParam("{'companyName': '" + companyName + "', 'companyCode': '" + companyCode + "'}");
            interRecord.setResult(result);
            interRecord.setResultStatus("1");
            /**
             * 封装企业信息
             */
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            if (!"200".equals(jsonObject.get("code").toString())) {
                throw new Exception("第三方接口调用错误");
            }
            map.put("companyName", (String) data.get("companyName"));
            map.put("companyCode", (String) data.get("companyCode"));
            map.put("bussinessAddress", (String) data.get("bussinessAddress"));
            map.put("bussinessNature", (String) data.get("bussinessNature"));
            map.put("legalPerson", (String) data.get("legalPerson"));
            map.put("registeredCapital", (String) data.get("registeredCapital"));
            map.put("bussinessLimit", (String) data.get("bussinessLimit"));
            map.put("legalPersonCardNo", (String) data.get("legalPersonCardNo"));
            map.put("companyType", (String) data.get("companyType"));

        } catch (Exception ex) {
            interRecord.setResultStatus("2");
            interRecord.setResult("调用失败" + ex.getMessage());
            return new ApiResultSet<>(500, "获取企业信息错误", ex.getMessage());
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;
            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return ApiResultSet.ok("获取企业信息成功", map);
    }

    /**
     * 取号
     *
     * @Author:zhaobf
     * @Date: 2022-08-18 14:46
     */
    @ResponseBody
    @GetMapping("/callNumber")
    public ApiResultSet callNumber() {
        HaInterRecord interRecord = new HaInterRecord();
        Map<String, String> map = new HashMap<>();
        String result = "{\n" +
                "    \"data\": {\n" +
                "        \"number\": \"123\"\n" +
                "    },\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"nulla\"\n" +
                "}";
        Long begin1 = System.currentTimeMillis();
        try {
            interRecord.setName("取号");
            interRecord.setUrl("/callNumber");
            interRecord.setMethod("get");
            interRecord.setSourceIp("192.168.10.20");
            interRecord.setParam("");
            interRecord.setResult(result);
            interRecord.setResultStatus("1");
            /**
             * 封装企业信息
             */
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            if (!"200".equals(jsonObject.get("code").toString())) {
                throw new Exception("第三方接口调用错误");
            }
            map.put("number", (String) data.get("number"));

        } catch (Exception ex) {
            interRecord.setResultStatus("2");
            interRecord.setResult("调用失败" + ex.getMessage());
            return new ApiResultSet<>(500, "取号错误", ex.getMessage());
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;
            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return ApiResultSet.ok("取号成功", map);
    }

    /**
     * 一键推送
     *
     * @param name    姓名
     * @param cardNo  身份证号
     * @param phone   手机号
     * @param title   标题
     * @param url     地址
     * @param context 内容
     * @Author:zhaobf
     * @Date: 2022-08-18 14:46
     */
    @ResponseBody
    @GetMapping("/onekeyPushGet")
    public ApiResultSet onekeyPushGet(String name, String cardNo, String phone, String title, String url, String context) {
        HaInterRecord interRecord = new HaInterRecord();
        Map<String, String> map = new HashMap<>();
        String result = "{\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"\"\n" +
                "}";
        Long begin1 = System.currentTimeMillis();
        try {

            interRecord.setName("一件推送");
            interRecord.setUrl("/onekeyPush");
            interRecord.setMethod("get");
            interRecord.setSourceIp("192.168.10.20");
            interRecord.setParam("{'name': '" + name + "', 'cardNo': '" + cardNo + "', 'phone': '" + phone + "', 'title': '" + title + "', 'url': '" + url + "', 'context': '" + context + "'}");
            interRecord.setResult(result);
            interRecord.setResultStatus("1");
            /**
             * 封装企业信息
             */
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (!"200".equals(jsonObject.get("code").toString())) {
                throw new Exception("第三方接口调用错误");
            }

        } catch (Exception ex) {
            interRecord.setResultStatus("2");
            interRecord.setResult("调用失败" + ex.getMessage());
            return new ApiResultSet<>(500, "一键推送错误", ex.getMessage());
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;
            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return ApiResultSet.ok("一键推送成功", map);
    }


    /**
     * 一键推送 万行
     *
     * @Date: 2022-10-17 14:46
     */
    @ResponseBody
    @PostMapping("/onekeyPush")
    public ApiResultSet onekeyPush(@RequestParam Map<String,Object> map){
        log.info("inter:进入一键推送map1={}",map);
        HaInterRecord interRecord = new HaInterRecord();
        Long begin1 = System.currentTimeMillis();
        /*WHOneKeyVo whOneKeyVo = new WHOneKeyVo();
        whOneKeyVo.setType(pushVo.getPushType());
        whOneKeyVo.setCaseOid(pushVo.getCaseOid());
        whOneKeyVo.setIdcard(pushVo.getCardNo());
        whOneKeyVo.setName(pushVo.getName());
        whOneKeyVo.setCompanyName(pushVo.getCompanyName());
        whOneKeyVo.setCompanyCode(pushVo.getCompanyCode());
        whOneKeyVo.setDateTime("2022-09-02T12:05:06");*/
        Map<String,Object>param = new HashMap<>();
        param.put("type",map.get("pushType"));
        param.put("caseOid",map.get("caseOid"));
        param.put("idcard",map.get("cardNo"));
        param.put("name",map.get("name"));
        param.put("companyName",map.get("companyName"));
        param.put("companyCode",map.get("companyCode"));
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM'T'HH:mm:ss");
        String dateTime = sdf.format(date);
        param.put("dateTime", dateTime);
        try {
            log.info("inter:进入一键推送调用第三方接口：pushsxUrl={}，param={}",pushsxUrl, param);
            String result = HttpRequestUtil.sendPost(pushsxUrl, param);
            log.info("inter:进入一键推送调用第三方接口结果：result={}",result);
            /**
             * 封装企业信息
             */
            JSONObject jsonObject1 = JSON.parseObject(result);
            if(!"200".equals( jsonObject1.get("code").toString())){
                throw new Exception("第三方接口调用错误"+jsonObject1.getString("message"));
            }
/*        String result = "{\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"\"\n" +
                "}";*/

            interRecord.setName("一键推送");
            interRecord.setUrl("/onekeyPush");
            interRecord.setMethod("post");
            interRecord.setSourceIp("sh-sbs.mayabot.com");
            interRecord.setParam(param.toString());
            interRecord.setResult(result);
            interRecord.setResultStatus("1");

        } catch (Exception ex) {
            interRecord.setResultStatus("2");
            interRecord.setResult("调用失败" + ex.getMessage());
            return new ApiResultSet<>(500, "一键推错误", ex.getMessage());
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;
            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return ApiResultSet.ok("一键推送成功", "");
    }

    /**
     * 一键推送 万行
     *
     * @Date: 2022-10-17 14:46applicationTaskExecutor
     */
    @ResponseBody
    @PostMapping("/attaPush")
    public ApiResultSet attaPush(@RequestBody @Valid HaAttaPushRequestData haAttaPushRequestData){
        log.info("inter:进入附件推送haAttaPushRequestData={}",haAttaPushRequestData);
        HaInterRecord interRecord = new HaInterRecord();
        Long begin1 = System.currentTimeMillis();
        /*WHOneKeyVo whOneKeyVo = new WHOneKeyVo();
        whOneKeyVo.setType(pushVo.getPushType());
        whOneKeyVo.setCaseOid(pushVo.getCaseOid());
        whOneKeyVo.setIdcard(pushVo.getCardNo());
        whOneKeyVo.setName(pushVo.getName());
        whOneKeyVo.setCompanyName(pushVo.getCompanyName());
        whOneKeyVo.setCompanyCode(pushVo.getCompanyCode());
        whOneKeyVo.setDateTime("2022-09-02T12:05:06");*/
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(haAttaPushRequestData);
        try {
            log.info("inter:进入附件推送调用第三方接口：attaPushUrl={}，map={}",attaPushUrl, stringObjectMap);
            String result = HttpRequestUtil.sendPost(attaPushUrl, stringObjectMap);
            log.info("inter:进入附件推送调用第三方接口结果：result={}",result);
            /**
             * 封装企业信息
             */
            JSONObject jsonObject1 = JSON.parseObject(result);
            if(!"200".equals( jsonObject1.get("code").toString())){
                throw new Exception("第三方接口调用错误"+jsonObject1.getString("message"));
            }
/*        String result = "{\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"\"\n" +
                "}";*/

            interRecord.setName("附件推送");
            interRecord.setUrl("/attaPush");
            interRecord.setMethod("post");
            interRecord.setSourceIp("sh-sbs.mayabot.com");
            interRecord.setParam(stringObjectMap.toString());
            interRecord.setResult(result);
            interRecord.setResultStatus("1");

        } catch (Exception ex) {
            interRecord.setResultStatus("2");
            interRecord.setResult("调用失败" + ex.getMessage());
            return new ApiResultSet<>(500, "附件推送错误", ex.getMessage());
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;
            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return ApiResultSet.ok("附件推送成功", "");
    }

    /**
     * 政策推送 万行
     *
     * @Date: 2023-3-30 14:46applicationTaskExecutor
     */
    @ResponseBody
    @PostMapping("/policyPush")
    public ApiResultSet policyPush(@RequestBody @Valid HaPolicyPushRequestData haPolicyPushRequestData){
        log.info("inter:进入政策推送policyPush RequestData={}",haPolicyPushRequestData);
        HaInterRecord interRecord = new HaInterRecord();
        Long begin1 = System.currentTimeMillis();

        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(haPolicyPushRequestData);
        try {
            log.info("inter:进入政策推送调用第三方接口：policyPushUrl={}，map={}",policyPushUrl, stringObjectMap);

            String result = HttpRequestUtil.sendPost(policyPushUrl, stringObjectMap);
            log.info("inter:进入政策推送调用第三方接口结果：result={}",result);

            JSONObject jsonObject1 = JSON.parseObject(result);
            if(!"200".equals( jsonObject1.get("code").toString())){
                throw new Exception("第三方接口调用错误"+jsonObject1.getString("message"));
            }
/*        String result = "{\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"\"\n" +
                "}";*/

            interRecord.setName("政策推送");
            interRecord.setUrl("/policyPushUrl");
            interRecord.setMethod("post");
            interRecord.setSourceIp("sh-sbs.mayabot.com");
            interRecord.setParam(stringObjectMap.toString());
            interRecord.setResult(result);
            interRecord.setResultStatus("1");

        } catch (Exception ex) {
            interRecord.setResultStatus("2");
            interRecord.setResult("调用失败" + ex.getMessage());
            return new ApiResultSet<>(500, "政策推送错误", ex.getMessage());
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;
            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return ApiResultSet.ok("政策推送成功", "");
    }

    /**
     * 评价推送 万行
     *
     * @Date: 2023-3-30 14:46applicationTaskExecutor
     */
    @ResponseBody
    @PostMapping("/evalPush")
    public ApiResultSet evalPush(@RequestBody @Valid HaEvalPushRequestData haEvalPushRequestData){
        log.info("inter:进入评价推送policyPush RequestData={}",haEvalPushRequestData);
        HaInterRecord interRecord = new HaInterRecord();
        Long begin1 = System.currentTimeMillis();

        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(haEvalPushRequestData);
        try {
            log.info("inter:进入评价推送调用第三方接口：evalPushUrl={}，map={}",evalPushUrl, stringObjectMap);

            String result = HttpRequestUtil.sendPost(evalPushUrl, stringObjectMap);
            log.info("inter:进入评价推送调用第三方接口结果：result={}",result);

            JSONObject jsonObject1 = JSON.parseObject(result);
            if(!"200".equals( jsonObject1.get("code").toString())){
                throw new Exception("第三方接口调用错误"+jsonObject1.getString("message"));
            }
/*        String result = "{\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"\"\n" +
                "}";*/

            interRecord.setName("评价推送");
            interRecord.setUrl("/evalPushUrl");
            interRecord.setMethod("post");
            interRecord.setSourceIp("sh-sbs.mayabot.com");
            interRecord.setParam(stringObjectMap.toString());
            interRecord.setResult(result);
            interRecord.setResultStatus("1");

        } catch (Exception ex) {
            interRecord.setResultStatus("2");
            interRecord.setResult("调用失败" + ex.getMessage());
            return new ApiResultSet<>(500, "政策推送错误", ex.getMessage());
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;
            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return ApiResultSet.ok("评价推送成功", "");
    }


    /**
     * 窗口取号推送、号票推送
     *
     * @Date: 2022-10-17 14:46
     */
    @ResponseBody
    @PostMapping("/winNumbPush")
    public ApiResultSet winNumbPush(WHWinNumbVO map){
        log.info("inter:进入窗口取号推送map1={}",map);
        HaInterRecord interRecord = new HaInterRecord();
        Long begin1 = System.currentTimeMillis();
        /*WHOneKeyVo whOneKeyVo = new WHOneKeyVo();
        whOneKeyVo.setType(pushVo.getPushType());
        whOneKeyVo.setCaseOid(pushVo.getCaseOid());
        whOneKeyVo.setIdcard(pushVo.getCardNo());
        whOneKeyVo.setName(pushVo.getName());
        whOneKeyVo.setCompanyName(pushVo.getCompanyName());
        whOneKeyVo.setCompanyCode(pushVo.getCompanyCode());
        whOneKeyVo.setDateTime("2022-09-02T12:05:06");*/
        Map<String,Object>param = new HashMap<>();
        param.put("stNumber",map.getStNumber());
        param.put("waitCount",map.getWaitCount());
        param.put("stDesc",map.getStDesc());
        param.put("stHallAddress",map.getStHallAddress());
        param.put("stHallFullName",map.getStHallFullName());
        param.put("stCategoryName",map.getStCategoryName());
        param.put("stTakeDate", map.getStTakeDate());
        param.put("cardNo", map.getCardNo());
        param.put("name", map.getName());
        param.put("companyName", map.getCompanyName());
        param.put("companyCode", map.getCompanyCode());

        try {
            log.info("inter:进入窗口取号推送调用第三方接口：stUrl={}，param={}",stUrl, param);
//            String result = HttpRequestUtil.sendPost(stUrl, param);
            String json = JSON.toJSONString(map);
            String result = ClientServer.send(stUrl,json,"utf-8");

            log.info("inter:进入窗口取号推送调用第三方接口结果：result={}",result);
            JSONObject jsonObject1 = JSON.parseObject(result);
            if(!"200".equals( jsonObject1.get("code").toString())){
                throw new Exception("第三方接口调用错误"+jsonObject1.getString("message"));
            }
/*        String result = "{\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"\"\n" +
                "}";*/

            interRecord.setName("窗口取号推送");
            interRecord.setUrl("/winNumbPush");
            interRecord.setMethod("post");
            interRecord.setSourceIp("192.168.10.20");
            interRecord.setParam(param.toString());
            interRecord.setResult(result);
            interRecord.setResultStatus("1");

        } catch (Exception ex) {
            interRecord.setResultStatus("2");
            interRecord.setResult("调用失败" + ex.getMessage());
            return new ApiResultSet<>(500, "一键推错误", ex.getMessage());
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;
            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return ApiResultSet.ok("一键推送成功", "");
    }

    /**
     * 获取办件编码 -万达
     * @param map    请求数据
     * @Author:wangyh
     * @Date: 2022-09-16 14:46
     */
    @ResponseBody
    @PostMapping("/applicantConfirm")
    public String applicantConfirm(@RequestParam Map<String,Object> map) throws IOException {
        HaInterRecord interRecord = new HaInterRecord();
        JSONObject jsonObjectResult = new JSONObject();
        List<ResponseApplyVo> applyVoList = new ArrayList<>();
        Long begin1 = System.currentTimeMillis();
        try {
        String json = (String) map.get("json");
        log.info("json:{}",json);
        String result = ClientServer.sendPost(applicantConfirmUrl, json,"utf-8",this.getToken());
        JSONObject jsonObject1 = JSON.parseObject(result);
        Integer code = (Integer) jsonObject1.get("code");
            if(1000 == code){
                JSONObject dataToken = JsonUtil.toJSONObject(jsonObject1.get("data"));
                JSONArray  applyArr = (JSONArray ) dataToken.get("apply");
                //转数组
                for(int i=0;i<applyArr.size();i++){
                    JSONObject jsonObject = (JSONObject) applyArr.get(i);
                    String applyjson = jsonObject.toJSONString();
                    ResponseApplyVo responseApplyVo = (ResponseApplyVo) JsonUtil.jsonToObject(applyjson,ResponseApplyVo.class);
                    applyVoList.add(responseApplyVo);
                }
                jsonObjectResult.put("code","200");
                jsonObjectResult.put("applyVoList",applyVoList);
            }else{
                jsonObjectResult.put("code","500");
                jsonObjectResult.put("applyVoList",null);
            }

            interRecord.setName("获取办件编码");
            interRecord.setUrl("/applicantConfirm");
            interRecord.setMethod("post");
            interRecord.setSourceIp("172.21.179.149");
            interRecord.setParam(json);
            interRecord.setResult(result);
            interRecord.setResultStatus("1");
            /**
             * 封装企业信息
             */
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (!"1000".equals(jsonObject.get("code").toString())) {
                throw new Exception("第三方接口调用错误");
            }
        } catch (Exception ex) {
            jsonObjectResult.put("code","501");
            jsonObjectResult.put("applyVoList",null);
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;
            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return jsonObjectResult.toJSONString();
    }

    /**
     * 增加材料 -万达
     * @param map<String,Object>    请求数据
     * @Author:wangyh
     * @Date: 2022-09-16 14:46
     */
    @ResponseBody
    @PostMapping("/addApplyStuff")
    public String addApplyStuff(@RequestParam Map<String,Object> map) throws IOException {
        log.info("进入增加材料接口");
        HaInterRecord interRecord = new HaInterRecord();
        JSONObject jsonObjectResult = new JSONObject();
        List<ResponseApplyStuffVo> applyVoList = new ArrayList<>();
        Long begin1 = System.currentTimeMillis();
        try {
            String json = (String) map.get("json");
            log.info("json:{}",json);
            String result = ClientServer.sendPost(addApplyStuffUrl, json,"utf-8",this.getToken());
            JSONObject jsonObject1 = JSON.parseObject(result);
            Integer code = (Integer) jsonObject1.get("code");
            if(1000 == code){
                JSONArray data = (JSONArray ) jsonObject1.get("data");
                //转数组
                for(int i=0;i<data.size();i++){
                    JSONObject jsonObject = (JSONObject) data.get(i);
                    String applyjson = jsonObject.toJSONString();
                    ResponseApplyStuffVo responseApplyVo = (ResponseApplyStuffVo) JsonUtil.jsonToObject(applyjson,ResponseApplyStuffVo.class);
                    applyVoList.add(responseApplyVo);
                }
                jsonObjectResult.put("code","200");
                jsonObjectResult.put("applyVoList",applyVoList);
            }else{
                jsonObjectResult.put("code","500");
                jsonObjectResult.put("applyVoList",null);
            }
            interRecord.setName("增加材料");
            interRecord.setUrl("/addApplyStuff");
            interRecord.setMethod("post");
            interRecord.setSourceIp("172.21.179.149");
            interRecord.setParam(json);
            interRecord.setResult(result);
            interRecord.setResultStatus("1");
            /**
             * 封装企业信息
             */
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (!"1000".equals(jsonObject.get("code").toString())) {
                throw new Exception("第三方接口调用错误");
            }
        } catch (Exception ex) {
            jsonObjectResult.put("code","501");
            jsonObjectResult.put("applyVoList",null);
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;
            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return jsonObjectResult.toJSONString();
    }

    /**
     *  材料添加附件并关联派生-万达
     * @param map<String,Object>    请求数据
     * @Author:wuqy
     * @Date: 2022-09-21 14:46
     */
    @ResponseBody
    @PostMapping("/addStuffAttachment")
    public String addStuffAttachment(@RequestParam Map<String,Object> map,MultipartFile file) throws IOException {
        log.info("进入材料添加附件并关联派生");
        HaInterRecord interRecord = new HaInterRecord();
        JSONObject jsonObjectResult = new JSONObject();
        List<ResponseaStuffAttaVo> applyVoList = new ArrayList<>();
        byte[] bytes = file.getBytes();
        InputStream in = new ByteArrayInputStream(bytes); //接收传递过来的文件流
        MultipartFile multipartFiles = new MockMultipartFile("file","", "multipart/form-data", in);
        /**
         * 测试传递过来的流能否输出图片（可以）
         */
/*        FileOutputStream outputStream = new FileOutputStream("C:\\Users\\Admin\\Desktop\\jar\\file.jpg");
        int read;
        byte[] bytess = new byte[1024];
        while ((read = in.read(bytess)) != -1) {
            outputStream.write(bytess, 0, read);
        }*/
        Long begin1 = System.currentTimeMillis();
        try {
            //封装token请求头
            Map<String,String> header = new HashMap<>();
            header.put("Authorization",this.getToken());
            //封装文件流请求参数
            Map<String,MultipartFile> files = new HashMap<>();
            files.put(file.getOriginalFilename(),multipartFiles);
            //请求万达接口
            String result = ClientServer.sendPostInter(addStuffAttachmentUrl, header,map,files,"","");
            JSONObject jsonObject1 = JSON.parseObject(result);
            Integer code = (Integer) jsonObject1.get("code");
            if(1000 == code){
                JSONArray data = (JSONArray ) jsonObject1.get("data");
                //转数组
                for(int i=0;i<data.size();i++){
                    JSONObject jsonObject = (JSONObject) data.get(i);
                    String applyjson = jsonObject.toJSONString();
                    ResponseaStuffAttaVo responseApplyVo = (ResponseaStuffAttaVo) JsonUtil.jsonToObject(applyjson,ResponseaStuffAttaVo.class);
                    applyVoList.add(responseApplyVo);
                }
                jsonObjectResult.put("code","200");
                jsonObjectResult.put("applyVoList",applyVoList);
            }else{
                jsonObjectResult.put("code","500");
                jsonObjectResult.put("applyVoList",null);
            }
            interRecord.setName("材料添加附件并关联派生");
            interRecord.setUrl("/addStuffAttachment");
            interRecord.setMethod("post");
            interRecord.setSourceIp("172.21.179.149");
            interRecord.setParam(String.valueOf(map));
            interRecord.setResult(result);
            interRecord.setResultStatus("1");
            /**
             * 封装企业信息
             */
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (!"1000".equals(jsonObject.get("code").toString())) {
                throw new Exception("第三方接口调用错误");
            }
        } catch (Exception ex) {
            jsonObjectResult.put("code","501");
            jsonObjectResult.put("applyVoList",null);
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;
            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return jsonObjectResult.toJSONString();
    }
    /**
     * 提交办件 -万达
     * @param map<String,Object>    请求数据
     * @Author:wangyh
     * @Date: 2022-09-16 14:46
     */
    @ResponseBody
    @PostMapping("/submitApply")
    public String submitApply(@RequestParam Map<String,Object> map) throws IOException {
        log.info("进入提交办件");
        HaInterRecord interRecord = new HaInterRecord();
        JSONObject jsonObjectResult = new JSONObject();
        List<ResponseSubmitVo> applyVoList = new ArrayList<>();
        Long begin1 = System.currentTimeMillis();;
        try {
            String json = (String) map.get("json");
            log.info("json:{}",json);
            String result = ClientServer.sendPost(submitApplyUrl, json,"utf-8",this.getToken());
            JSONObject jsonObject1 = JSON.parseObject(result);
            Integer code = (Integer) jsonObject1.get("code");
            if(1000 == code){
                JSONObject data = JsonUtil.toJSONObject(jsonObject1.get("data"));
                JSONArray  printArr = (JSONArray ) data.get("print");
                //转数组
                for(int i=0;i<printArr.size();i++){
                    JSONObject jsonObject = (JSONObject) printArr.get(i);
                    String applyjson = jsonObject.toJSONString();
                    ResponseSubmitVo responseSubmitVo = (ResponseSubmitVo) JsonUtil.jsonToObject(applyjson,ResponseSubmitVo.class);
                    applyVoList.add(responseSubmitVo);
                }
                jsonObjectResult.put("code","200");
                jsonObjectResult.put("applyVoList",applyVoList);
            }else{
                jsonObjectResult.put("code","500");
                jsonObjectResult.put("applyVoList",null);
            }
            interRecord.setName("提交办件");
            interRecord.setUrl("/submitApply");
            interRecord.setMethod("post");
            interRecord.setSourceIp("172.21.179.149");
            interRecord.setParam(json);
            interRecord.setResult(result);
            interRecord.setResultStatus("1");
            /**
             * 封装企业信息
             */
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (!"1000".equals(jsonObject.get("code").toString())) {
                throw new Exception("第三方接口调用错误");
            }
        } catch (Exception ex) {
            jsonObjectResult.put("code","501");
            jsonObjectResult.put("applyVoList",null);
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;
            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return jsonObjectResult.toJSONString();
    }
    /**
     * 雪花算法生成器
     */
    private Snowflake snowflake = new Snowflake(0, 0);

    /**
     * 日志记录
     *
     * @param interRecord
     */
    private void interRecord(HaInterRecord interRecord) {
        interRecord.setId(snowflake.nextId());
        String sql = "insert into t_ha_inter_record (ID, NAME, URL, SOURCE_IP, METHOD, PARAM, RESULT_STATUS, RESULT, " +
                "TOTAL_TIME, CREATE_DATE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, interRecord.getId(), interRecord.getName(), interRecord.getUrl(),
                interRecord.getSourceIp(), interRecord.getMethod(), interRecord.getParam(),
                interRecord.getResultStatus(), interRecord.getResult(), interRecord.getTotalTime(), new Date());
    }

    /**
     * 登录获取token
     */
    public String getToken() throws IOException {
        String token ="";
        Map<String,Object> map = new HashMap<>();
        map.put("account",account);
        map.put("password",password);
        String json = JSON.toJSONString(map);
        String result = ClientServer.send(loginTokenUrl,json,"utf-8");
        JSONObject jsonObject = JSON.parseObject(result);
        Integer code = (Integer) jsonObject.get("code");
        if(1000 == code){
            JSONObject dataToken = JsonUtil.toJSONObject(jsonObject.get("dataToken"));
            token = (String) dataToken.get("token");
        }
        return token;
    }


}
