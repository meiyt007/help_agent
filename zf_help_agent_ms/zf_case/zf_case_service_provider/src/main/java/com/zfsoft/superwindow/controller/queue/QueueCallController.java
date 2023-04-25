package com.zfsoft.superwindow.controller.queue;

import com.alibaba.fastjson.JSONObject;
import com.zfsoft.microservice.settings.data.SysConfig;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.queue.QhjhWaitNumRequest;
import com.zfsoft.superwindow.data.queue.SendQhJhInfoRequest;
import com.zfsoft.superwindow.feign.settings.SysConfigFeignService;
import com.zfsoft.superwindow.service.queue.QueueCallService;
import com.zfsoft.superwindow.util.DateUtil;
import com.zfsoft.superwindow.util.HttpRequestUtil;
import com.zfsoft.superwindow.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/7/15 9:53
 */
@Slf4j
@RestController
public class QueueCallController implements QueueCallService {

    @Resource
    private RedisTemplate redisTemplate;

    private final static String COMPUTE_KEY = "compute_key_";

    private final static String PREFIX= "A";//号的开头

    private final static  String CALL_INIT_NUM = "001";//取号开始号，后面递增

    private final static  int CALL_MAX_NUM = 999;//取号开始号,最大的号

    private final static  int SMALL_INIT_NUM = 99;//小屏幕初始数量

    private final static  int SMALL_MIN_NUM = 1;//小屏幕最小数量

    private final static  int BIG_INIT_NUM = 200;//大屏幕初始数量

    private final static  int BIG_MIN_NUM = 1;//大屏幕最小数量

    private final static  int FINISH_NUM = 1;//结束数量，递增

    @Value("${zfsoft.queueSystem.url}")
    private String queueSystemUrl;

    @Resource
    private SysConfigFeignService sysConfigFeignService;

    @Override
    public ApiResultSet getTotalWaitNum(String userOid) {
        ApiResultSet apiResultSet = new ApiResultSet<>();
        JSONObject resultJson =  new JSONObject();
        ApiResultSet<SysConfig> config = sysConfigFeignService.getSysConfigByCode("QJMN");
        String  isQjmn = "";
        if(config!=null){
            //0,取叫号系统走模拟数据.1,取叫号系统走取叫号系统。
            isQjmn  =  config.getData().getValue();
        }
        if("0".equals(isQjmn)){
            CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
            String  key;
            if(currentLoginUser == null){
                key =  COMPUTE_KEY+ "6666662" ;
            }else{
                key =  COMPUTE_KEY+  currentLoginUser.getUserOid();
            }
            SimpleDateFormat date_sdf  = new SimpleDateFormat("yyyy-MM-dd");
            String  todayDateStr = DateUtil.date2Str(date_sdf);
            key = key + todayDateStr;
            if(redisTemplate.hasKey(key)) {
                ValueOperations<String, JSONObject> operation = redisTemplate.opsForValue();
                JSONObject params = operation.get(key);
                String callNum = params.getString("callNum");
                int smallScreenWaitNum = params.getInteger("smallScreenWaitNum");
                resultJson.put("waitNum",smallScreenWaitNum);
            }else {
                resultJson.put("waitNum",SMALL_INIT_NUM+1);
            }
            resultJson.put("success",true);
            apiResultSet.setData(resultJson);
            return apiResultSet;
        }
        //apiResultSet.setData(CALL_INIT_NUM);
        //apiResultSet.setData(60);
        //String  getTotalWaitNumUrl = "http://localhost:8080/jh/common/getWaitNum.do";
        //String  getTotalWaitNumUrl = "http://172.168.251.11:10015/qhjhxt/common/getWaitNum.do";
        //http://172.168.253.21:8086/pdjhxt
        try{
            CurrentLoginUser   currentLoginUser =  CurrentLoginUserHolder.getCurrentLoginUser();
            String oid  =  "";
            if(currentLoginUser==null){
                oid =  "";
            }else{
                oid  = currentLoginUser.getUserOid();
            }
            //获取当前排队叫号人数
            //  ApiResultSet<SysConfig>  sysCongig  =  sysConfigFeignService.getSysConfigByCode("TWN");
            JSONObject result = compute(oid);
            String  getTotalWaitNumUrl = queueSystemUrl+"/common/getWaitNum.do";
            Map<String, Object> map = new HashMap<String, Object>();
            //map.put("userId", "00000000000000000000000000000000");
            //40288582614b8eef01616a33e000771f
/*        map.put("userId", "40288582614b8eef01616a33e000771f");
        map.put("ipAddress", "00:00:32:10:00:01");*/
            map.put("userId", result.getString("userOidRelation"));
            map.put("ipAddress", result.getString("ipAddressRelation"));
            log.info("调用取号叫号系统-获取当前排队叫号人数 getTotalWaitNumUrl = " +getTotalWaitNumUrl + " 参数 = "+map.toString());
            String totalWaitNumUrlBody = HttpRequestUtil.sendPost(getTotalWaitNumUrl, map);
            log.info("totalWaitNumUrlBody  -----  "+ totalWaitNumUrlBody);
            JSONObject totalJson = JSONObject.parseObject(totalWaitNumUrlBody);
            resultJson.put("waitNum",totalJson.getInteger("waitNum"));
            resultJson.put("success",true);
            //apiResultSet.setData(totalJson.getInteger("waitNum"));
        }catch(Exception e) {
            log.error("等待人数查询失败  "+ e.getMessage());
            resultJson.put("success",false);
            resultJson.put("msg","等待人数查询失败");
        }
        apiResultSet.setData(resultJson);
        return apiResultSet;
    }

    //根据综窗的userOid，获取取叫号系统的 useroid  ，ipAddress
    private JSONObject compute(String  userOid){
        JSONObject  result  =  new JSONObject();
        Map<String,String>  userOidRelation  = new HashMap<>();
        //开发环境，映射关系
        //userOidRelation.put("00000000000000000000000000000010","402885816005a3b701607db4e9a52926");
        //userOidRelation.put("c5ccbbaec7c14c319b3ce030d392cbad","4028858279c1fe650179d5fb91fe15e0");
        //测试环境，银蛇关系。
        userOidRelation.put("9b42bf2577f64fd3acb8c6f9d54758bd","402885816005a3b701607db4e9a52926");
        userOidRelation.put("f424add4c839451e8bff35ad9fb61670","4028858279c1fe650179d5fb91fe15e0");
        //测试环境，张丁菱
        userOidRelation.put("c5ccbbaec7c14c319b3ce030d392cbad","402885816005a3b701607db637e0292e");
        Map<String,String>  ipAddressRelation  = new HashMap<>();
        //开发环境，映射关系
        //ipAddressRelation.put("00000000000000000000000000000010","00:00:32:10:00:08");
        //ipAddressRelation.put("c5ccbbaec7c14c319b3ce030d392cbad","00:00:32:10:00:04");
        //测试环境，银蛇关系。
        ipAddressRelation.put("9b42bf2577f64fd3acb8c6f9d54758bd","00:00:32:10:00:08");
        ipAddressRelation.put("f424add4c839451e8bff35ad9fb61670","00:00:32:10:00:04");
        //测试环境，张丁菱
        ipAddressRelation.put("c5ccbbaec7c14c319b3ce030d392cbad","00:00:32:10:00:11");
        String  qjhUserOid =  userOidRelation.get(userOid);
        result.put("userOidRelation", StringUtils.isEmpty(qjhUserOid)?"40288582614b8eef01616a33e000771f":qjhUserOid);
        String  qjhIpAddress  =  ipAddressRelation.get(userOid);
        result.put("ipAddressRelation",StringUtils.isEmpty(qjhIpAddress)?"00:00:32:10:00:01":qjhIpAddress);
        //测试覆盖
        //result.put("userOidRelation", "4028858279c1fe650179d5fb91fe15e0");
        //result.put("ipAddressRelation","00:00:32:10:00:05");
        return result;
    }

    @Override
    public ApiResultSet getQhjhWaitNum(QhjhWaitNumRequest qhjhWaitNumRequest) {
        ApiResultSet apiResultSet = new ApiResultSet<>();
        ApiResultSet<SysConfig> config = sysConfigFeignService.getSysConfigByCode("QJMN");
        String  isQjmn = "";
        if(config!=null){
            //0,取叫号系统走模拟数据.1,取叫号系统走取叫号系统。
            isQjmn  =  config.getData().getValue();
        }
        if("0".equals(isQjmn)){
            JSONObject result = compute();
            apiResultSet.setData(result);
            return  apiResultSet;
        }

/*        JSONObject  result = new JSONObject();
        result.put("callNum",11);//取号
        result.put("smallScreenWaitNum",11);//小屏幕数量
        result.put("bigScreenWaitNum",11);//大屏幕数量
        result.put("finishNum",11);//结束数量
        apiResultSet.setData(result);*/
        //apiResultSet.setData(compute());
/*        JSONObject callNumberJson = new JSONObject();
        callNumberJson.put("userOid","00000000000000000000000000000000");
        callNumberJson.put("callType","JH");*/

        //综窗，取叫号系统 用户表oid，映射关系集合。
        //Map<String,String>  userOid =  new HashMap<>();
        CurrentLoginUser   currentLoginUser =  CurrentLoginUserHolder.getCurrentLoginUser();
        String userOid  =  "";
        if(currentLoginUser==null){
            userOid =  "";
        }else{
            userOid  = currentLoginUser.getUserOid();
        }
        JSONObject result = compute(userOid);
        //放到系统参数表里面
        String  callNumberJsonUrl = queueSystemUrl+"/common/callNumber.do";
        Map<String, Object> map = new HashMap<String, Object>();
        //String url = pbpjUrl + getEvaluateContentUrl;
        //map.put("userOid", "00000000000000000000000000000000");
        /*map.put("userId", "40288582614b8eef01616a33e000771f");
        map.put("callType", "JH");
        map.put("ipAddress", "00:00:32:10:00:01");*/
        map.put("userId", result.getString("userOidRelation"));
        map.put("callType", "JH");
        map.put("ipAddress", result.getString("ipAddressRelation"));
        log.info("调用取号叫号系统-叫号接口参数 callNumberJsonUrl = " +callNumberJsonUrl + " callNumberJson = "+map.toString());

        String callNumberBody = HttpRequestUtil.sendPost(callNumberJsonUrl, map);

        log.info("callNumberBody  -----  "+ callNumberBody);
        JSONObject reJson = JSONObject.parseObject(callNumberBody);
        JSONObject callNumberResulrJson = new JSONObject();
        callNumberResulrJson.put("callNum",reJson.getString("numberCode"));
        callNumberResulrJson.put("smallScreenWaitNum",reJson.getString("waitNum"));
        callNumberResulrJson.put("numberOid",reJson.getString("numberOid"));
       // callNumberResulrJson.put("WindowName",reJson.getString("WindowName"));
        callNumberResulrJson.put("WindowName","001窗口");
        callNumberResulrJson.put("success",reJson.getBoolean("success"));
        if(reJson.getBoolean("success") == false){
            if(reJson.containsKey("data")){
                String  data = reJson.getString("data");
                callNumberResulrJson.put("data",data);
                if(data.contains("暂无业务")){
                    callNumberResulrJson.put("isNoWaitt",0);//0  暂无业务， 1  进行叫号操作异常
                }else{
                    callNumberResulrJson.put("isNoWaitt",1);//0  暂无业务， 1  进行叫号操作异常
                    ApiResultSet error  = new ApiResultSet<>();
                    error.setData(callNumberResulrJson);
                    error.setCode(ApiResultSet.UNKNOWN_ERROR);
                    return  error;
                }
            }
        }
        apiResultSet.setData(callNumberResulrJson);
        return apiResultSet;
    }

    private  JSONObject compute(){
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        String  key;
        if(currentLoginUser == null){
            key =  COMPUTE_KEY+ "6666662" ;
        }else{
            key =  COMPUTE_KEY+  currentLoginUser.getUserOid();
        }
        SimpleDateFormat date_sdf  = new SimpleDateFormat("yyyy-MM-dd");
        String  todayDateStr = DateUtil.date2Str(date_sdf);
        key = key + todayDateStr;
        JSONObject  result = new JSONObject();
        if(redisTemplate.hasKey(key)){
            ValueOperations<String, JSONObject> operation = redisTemplate.opsForValue();
            JSONObject params = operation.get(key);
            String callNum = params.getString("callNum");
            //int  callIntNum = Integer.parseInt(callNum.substring(1,callNum.length()));
            String  callStringNum =  callNum.substring(1,callNum.length());
            //callStringNum = callStringNum.replaceAll("0","");
            //StringBuilder traserResult  = new StringBuilder();
            for(int i=0;i<=callStringNum.length();i++){
                if("0".equalsIgnoreCase(callStringNum.charAt(i)+"")){
                    //callStringNum = callStringNum.replace("0","");
                }else{
                    //break;
                    callStringNum = callStringNum.substring(i);
                    break;
                }
            }
            //截取数字，把前面的多位 0. 去掉。 然后，累加叫号。A001  累加到 A002. 以此类推。
            int  callIntNum = Integer.parseInt(callStringNum);
            callIntNum++;
            //超过最大的号， 比如超过 999. 重新从 1 开始，计算。
            if(callIntNum >= CALL_MAX_NUM){
                callIntNum = 1;
            }
            String callNumRsult = "";
            //取号数字。 前面的0 ，需要拼接上去
            if(callIntNum<=9){
                callNumRsult += "00"+callIntNum;
            }else if(callIntNum<=99){
                callNumRsult += "0"+callIntNum;
            }else{
                callNumRsult += ""+ callIntNum;
            }
            //取号的前缀   比如  A001 就是A
            params.put("callNum",PREFIX+callNumRsult);//取号

            int  smallScreenWaitNum =  params.getInteger("smallScreenWaitNum");
            smallScreenWaitNum--;
            if(smallScreenWaitNum<=SMALL_MIN_NUM){
                smallScreenWaitNum = SMALL_INIT_NUM;
            }
            params.put("smallScreenWaitNum",smallScreenWaitNum);//取号

            int  bigScreenWaitNum =  params.getInteger("bigScreenWaitNum");
            bigScreenWaitNum--;
            if(bigScreenWaitNum<=BIG_MIN_NUM){
                bigScreenWaitNum = BIG_INIT_NUM;
            }
            params.put("bigScreenWaitNum",bigScreenWaitNum);//取号

            int  finishNum =  params.getInteger("finishNum");
            params.put("finishNum",++finishNum);//取号
            //模拟数据，成功。
            params.put("success",true);
            //模拟 id
            params.put("numberOid","-1");
            //模拟窗口名字
            params.put("WindowName","001窗口");
            redisTemplate.opsForValue().set(key, params, 24*60*60, TimeUnit.SECONDS);
            result.putAll(params);
        }else{
            JSONObject  params = new JSONObject();
            params.put("callNum",PREFIX+CALL_INIT_NUM);//取号
            params.put("smallScreenWaitNum",SMALL_INIT_NUM);//小屏幕数量
            params.put("bigScreenWaitNum",BIG_INIT_NUM);//大屏幕数量
            params.put("finishNum",FINISH_NUM);//结束数量
            //模拟数据，成功。
            params.put("success",true);
            //模拟 id
            params.put("numberOid","-1");
            //模拟窗口名字
            params.put("WindowName","001窗口");
            redisTemplate.opsForValue().set(key, params, 24*60*60, TimeUnit.SECONDS);
            result.putAll(params);
        }
        return result;
    }


    @Override
    public ApiResultSet sendQhJhInfo(SendQhJhInfoRequest sendQhJhInfoRequest) {
        ApiResultSet apiResultSet = new ApiResultSet<>();
        JSONObject  result = new JSONObject();
        result.put("msg","推送成功");
        apiResultSet.setData(result);
        return apiResultSet;
    }

    @Override
    public ApiResultSet handleCallNumber(String numberOid) {
        ApiResultSet apiResultSet = new ApiResultSet<>();
        ApiResultSet<SysConfig> config = sysConfigFeignService.getSysConfigByCode("QJMN");
        String  isQjmn = "";
        if(config!=null){
            //0,取叫号系统走模拟数据.1,取叫号系统走取叫号系统。
            isQjmn  =  config.getData().getValue();
        }
        if("0".equals(isQjmn)){
            JSONObject handleCallNumberResulrJson = new JSONObject();
            handleCallNumberResulrJson.put("isSuccess","0");//0 成功，1 失败
            apiResultSet.setData(handleCallNumberResulrJson);
            return apiResultSet;
        }
        //放到参数表，通过code，去取
        String  handleCallNumberJsonUrl = queueSystemUrl+"/common/handleCallNumber.do";
        Map<String, Object> map = new HashMap<String, Object>();
        //String url = pbpjUrl + getEvaluateContentUrl;
        map.put("numberOid", numberOid);
        log.info("调用取号叫号系统-根据取号OID修改状态为已叫号,正在办理对外接口 callNumberJsonUrl = " +handleCallNumberJsonUrl + " handleCallNumberJson = "+map.toString());
        String handlecallNumberBody = HttpRequestUtil.sendPost(handleCallNumberJsonUrl, map);
        log.info("handlecallNumberBody  -----  "+ handlecallNumberBody);
        JSONObject reJson = JSONObject.parseObject(handlecallNumberBody);
        boolean isSuccess =  reJson.getBoolean("success");
        String  msg  =  reJson.getString("data");
        JSONObject handleCallNumberResulrJson = new JSONObject();
         if(isSuccess){
             handleCallNumberResulrJson.put("isSuccess","0");//0 成功，1 失败
             handleCallNumberResulrJson.put("msg",msg);//接口返回信息
         }else{
             handleCallNumberResulrJson.put("isSuccess","1");//0 成功，1 失败
             handleCallNumberResulrJson.put("msg",msg);//接口返回信息
         };
        apiResultSet.setData(handleCallNumberResulrJson);
        return apiResultSet;
    }

    @Override
    public ApiResultSet completeCallNumber(String numberOid) {
        ApiResultSet apiResultSet = new ApiResultSet<>();
        ApiResultSet<SysConfig> config = sysConfigFeignService.getSysConfigByCode("QJMN");
        String  isQjmn = "";
        if(config!=null){
            //0,取叫号系统走模拟数据.1,取叫号系统走取叫号系统。
            isQjmn  =  config.getData().getValue();
        }
        if("0".equals(isQjmn)){
            JSONObject handleCallNumberResulrJson = new JSONObject();
            handleCallNumberResulrJson.put("isSuccess","0");//0 成功，1 失败
            apiResultSet.setData(handleCallNumberResulrJson);
            return apiResultSet;
        }
        CurrentLoginUser   currentLoginUser =  CurrentLoginUserHolder.getCurrentLoginUser();
        String userOid  =  "";
        if(currentLoginUser==null){
            userOid =  "";
        }else{
            userOid  = currentLoginUser.getUserOid();
        }
        JSONObject result = compute(userOid);
        String  completeCallNumberJsonUrl = queueSystemUrl+"/common/completetCallNumber.do";
        Map<String, Object> map = new HashMap<String, Object>();
        //String url = pbpjUrl + getEvaluateContentUrl;
        map.put("numberOid", numberOid);
        //map.put("userId", "40288582614b8eef01616a33e000771f");
        map.put("userId", result.getString("userOidRelation"));
        log.info("调用取号叫号系统-叫号 - 完成办理（状态4）对外接口 completeCallNumberJsonUrl = " +completeCallNumberJsonUrl + " completeCallNumberJson = "+map.toString());
        String completecallNumberBody = HttpRequestUtil.sendPost(completeCallNumberJsonUrl, map);
        log.info("completecallNumberBody  -----  "+ completecallNumberBody);
        JSONObject reJson = JSONObject.parseObject(completecallNumberBody);
        boolean isSuccess =  reJson.getBoolean("success");
        String  msg  =  reJson.getString("data");
        JSONObject handleCallNumberResulrJson = new JSONObject();
        if(isSuccess){
            handleCallNumberResulrJson.put("isSuccess","0");//0 成功，1 失败
            handleCallNumberResulrJson.put("msg",msg);//接口返回信息
        }else{
            handleCallNumberResulrJson.put("isSuccess","1");//0 成功，1 失败
            handleCallNumberResulrJson.put("msg",msg);//接口返回信息
        };
        apiResultSet.setData(handleCallNumberResulrJson);
        return apiResultSet;
    }

    @Override
    public ApiResultSet skipCallNumber(String numberOid) {
        ApiResultSet apiResultSet = new ApiResultSet<>();
        ApiResultSet<SysConfig> config = sysConfigFeignService.getSysConfigByCode("QJMN");
        String  isQjmn = "";
        if(config!=null){
            //0,取叫号系统走模拟数据.1,取叫号系统走取叫号系统。
            isQjmn  =  config.getData().getValue();
        }
        if("0".equals(isQjmn)){
            JSONObject handleCallNumberResulrJson = new JSONObject();
            handleCallNumberResulrJson.put("isSuccess","0");//0 成功，1 失败
            apiResultSet.setData(handleCallNumberResulrJson);
            return apiResultSet;
        }
        CurrentLoginUser   currentLoginUser =  CurrentLoginUserHolder.getCurrentLoginUser();
        String userOid  =  "";
        if(currentLoginUser==null){
            userOid =  "";
        }else{
            userOid  = currentLoginUser.getUserOid();
        }
        JSONObject result = compute(userOid);
        //String  skipCallNumberJsonUrl = "http://172.168.253.21:8086/pdjhxt/common/skipCallNumber.do";
        //http://172.168.250.82:10013/qhj
        String  skipCallNumberJsonUrl =queueSystemUrl+ "/common/skipCallNumber.do";
        Map<String, Object> map = new HashMap<String, Object>();
        //String url = pbpjUrl + getEvaluateContentUrl;
        map.put("numberOid", numberOid);
        //map.put("userId", "40288582614b8eef01616a33e000771f");
        map.put("userId", result.getString("userOidRelation"));
        log.info("调用取号叫号系统-跳过接口 对外接口 skipCallNumberJsonUrl = " +skipCallNumberJsonUrl + " skipCallNumberJsonUrl = "+map.toString());
        String completecallNumberBody = HttpRequestUtil.sendPost(skipCallNumberJsonUrl, map);
        log.info("completecallNumberBody  -----  "+ completecallNumberBody);
        JSONObject reJson = JSONObject.parseObject(completecallNumberBody);
        boolean isSuccess =  reJson.getBoolean("success");
        String  msg  =  reJson.getString("data");
        JSONObject handleCallNumberResulrJson = new JSONObject();
        if(isSuccess){
            handleCallNumberResulrJson.put("isSuccess","0");//0 成功，1 失败
            handleCallNumberResulrJson.put("msg",msg);//接口返回信息
        }else{
            handleCallNumberResulrJson.put("isSuccess","1");//0 成功，1 失败
            handleCallNumberResulrJson.put("msg",msg);//接口返回信息
        };
        apiResultSet.setData(handleCallNumberResulrJson);
        return apiResultSet;
    }
}
