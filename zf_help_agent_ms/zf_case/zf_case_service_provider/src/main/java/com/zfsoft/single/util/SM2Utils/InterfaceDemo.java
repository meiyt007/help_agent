package com.zfsoft.single.util.SM2Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * @Description: 接口示例测试
 * @Author: liyanq
 * @Date: 2019-09-18 13:48
 * @Version: 1.0
 * @Company: 版权所有©
 **/
public class InterfaceDemo {

    // 系统Key
    public static String apiKey = "C312E4CF2ADD090DFC7B50930E7C1812";
    // 系统私钥
    public static String privateKey = "1e2c8bce26418ce29ee5aa9fbc270832e8b7d9a516c03618823a19eb7ebc8964";
    // 系统公钥
    public static String publicKey = "04f4a5b3234510c72f4dcef0ee7a46830088f5e917dd6f56cfcd522adfcc4d490adb4440d53f396f1ad0fed98e8cf5940d8458537038912bd5d6593d89a651e856";
    // 密文
    public static String systemPwd = "044fcac1d7110d1b693e13bb1d0dfdfa1789961b7f7249955ee37658d3918c803e56bf0345f055cd517a5ff3b73cd34e17357e5a157a7195b3129cced9c21fd24e76141a8c5aca4900b506a8fc996917faf72225b053695fba273f4f824e40ed17e3aa6d2357cb74abaa52086c99793dafbee5d7c81233745e77b0a79be21ac661";

    // 系统Key
//    public static String apiKey = "CFE9107CAD11B6CC0B66A4B42DC150EC";
//    // 系统私钥
//    public static String privateKey = "6b234c41c5beb7ddb99272dad4169325ec4f356fde4e3910f4d0f5f6dbc5fbb6";
//    // 系统公钥
//    public static String publicKey = "046aa9aae3cbd5d27e4e3d0b2c896ee4028f79961e9a62cac3d3fa5b43dcac99896fbd96c32b72020c005e9cdaa6632dc62f2d9dbd4ad10c511416dcf83f1c9838";
//    // 密文
//    public static String systemPwd = "042ba44de65e47ec597ad263c7017af56016c7dedaaa46fdfe3a0a61cc9470faf7d2c58c49dffd40e48c4541a36dbc6d477c3dc387b1375ce30d14b1706996d36eeef6bdea3b89be012d23c85aa58a093fc34e9980f76e4d7d9dcef4c8f67b981f44c2079f6cdb3bb7a672b6b609eeebf01cc776b7c750b554c380dce5fea937e3";
    // 请求地址
//    public static String reqURL = "https://www.xzzwfw.gov.cn";
    public static String reqURL = "http://172.168.252.58:7777";
//    public static String reqURL = "http://113.62.176.70:8081/sfrz/iden/ids/naturalBelieveTrust.do?method=authorize&loginFlag=1&client_id=bb597aaa6cf6e89b016cfc73d5996733&redirect_uri=http://172.168.252.57:8880/xzpor&response_type=code&indexUrl=http://127.0.0.1:8880/xzpor&oauth2WebUrl=";
    
    
    /**
     * 调用【事项评价指标列表查询接口】
     * @return
     * @throws Exception
     * @author wangxl
     * @date 2019-10-15
     */
    public static String listEvaluationContent() throws Exception {
//        String requestData = "{'implementCode':'12152900K052060541249541700400003','cityCode':'LASASHI'}";
//        String requestData = "{\"caseNum\":\"20W000001X\",\"backUrl\":\"http://www.baidu.com\"}";
        String requestData = "{ \"content\":\"评价内容\"" +
                ",\"projectNo\":\"123456789\"" +
                ",\"appStatus\":\"2\"," +
                "\"rectification\":\"整体反馈\"" +
                ",\"effectivEvalua\":\"1\"}";
        Map<String, String> params = new HashMap<String, String>();
        params.put("apiKey", apiKey);
        params.put("systemPwd", systemPwd);
        String encData = SM2Util.sm2Encrypt(publicKey, requestData);
        params.put("requestData", encData);
        String dataSign = SM2Util.sm2Sign(privateKey, apiKey, encData);
        params.put("dataSign", dataSign);
        //事项评价指标列表查询接口
//        String result = sendPost(reqURL + "/pjInterface/myEvaluate.do", params);
//        String result = sendPost(reqURL + "/pjInterface/viewZwfwCaseEvaluate.do", params);
//        String result = sendPost(reqURL + "/pjInterface/listEvaluateByServiceOid.do", params);
//        String result = sendPost(reqURL + "/pjInterface/listEvaluateByImplementCode.do", params);
        String result = sendPost(reqURL + "/pjInterface/listEvaluationContent.do", params);
//        String result = sendPost(reqURL + "/pjInterface/serviceSatisConut.do", params);
//        String result = sendPost(reqURL + "/pjInterface/isEvaluate.do", params);
//        String result = sendPost(reqURL + "/pjInterface/myEvaluate.do", params);
//        String result = sendPost(reqURL + "/pjInterface/saveCaseEvaluate.do", params);
        // 根据业务处理返回的信息......
        return result;
    }


    /**
     * 调用【评价指标列表查询接口】
     * @return
     * @throws Exception
     * @author wangxl
     * @date 2019-10-15
     */
    public static String myEvaluate() throws Exception{
//         String requestData = "{'oid':'2c287cb66dd2d826016dd333ec71492a'}";
//         String requestData = "{'serviceOid':'bb597a8c6bf4fb6e016bf5dfaebf61bc','pageNumber':'1','pageSize':'10'}";
         String requestData = "{'netUserId':'bb597a9c6da61def016da666e2ae005d','pageNumber':'1','pageSize':'10'}";
         Map<String, String> params = new HashMap<String, String>();
         params.put("apiKey", apiKey);
         params.put("systemPwd", systemPwd);
         String encData = SM2Util.sm2Encrypt(publicKey, requestData);
         params.put("requestData", encData);
         String dataSign = SM2Util.sm2Sign(privateKey, apiKey, encData);
         params.put("dataSign", dataSign);
//         String result = sendPost(reqURL + "/pjInterface/viewZwfwCaseEvaluate.do", params);
//         String result = sendPost(reqURL + "/pjInterface/listEvaluateByServiceOid.do", params);
         String result = sendPost(reqURL + "/pjInterface/myEvaluate.do", params);
    	return result;
    }
    
    /**
     * 调用【线上修改办件评价信息】
     * @return
     * @throws Exception
     * @author wangxl
     * @date 2019-10-15
     */
    public static String updateCaseEvaluate() throws Exception{
        String requestData = "{'content':'测试修改办件评价','deptCode':'00001','projectNo':'20W000001N','appStatus':'1','rectification':'ddddddd','effectivEvalua':'1','dataSource':'2'}";
        Map<String, String> params = new HashMap<String, String>();
        params.put("apiKey", apiKey);
        params.put("systemPwd", systemPwd);
        String encData = SM2Util.sm2Encrypt(publicKey, requestData);
        params.put("requestData", encData);
        String dataSign = SM2Util.sm2Sign(privateKey, apiKey, encData);
        params.put("dataSign", dataSign);
        String result = sendPost(reqURL + "/pjInterface/updateCaseEvaluate.do", params);
        return result;
   }
    /**
     * 调用【线下办件评价保存】
     * @return
     * @throws Exception
     * @author wangxl
     * @date 2019-10-15
     */
    public static String saveCaseEvaluateOffLine() throws Exception{
        String requestData = "{'pjTerm':'4','content':'调用【线下办件评价保存】','serviceName':'测试事项','pf':'接口调用','evalDetail':'401,402,403'"
        		+ ",'deptCode':'0001','taskCode':'0000002','taskHandleItem':'0000025','subMatter':'测试主题','projectNo':'20W000001N'"
        		+ ",'proStatus':'2','proDepart':'测试部门','certKey':'542129197802160506','userName':'龙心一','userProp':'1'"
        		+ ",'assessNumber':'1','appStatus':'1','flag':'I','dataSource':'1','promisetime':'1','acceptDate':'2019-10-15'}";
        Map<String, String> params = new HashMap<String, String>();
        params.put("apiKey", apiKey);
        params.put("systemPwd", systemPwd);
        String encData = SM2Util.sm2Encrypt(publicKey, requestData);
        params.put("requestData", encData);
        String dataSign = SM2Util.sm2Sign(privateKey, apiKey, encData);
        params.put("dataSign", dataSign);
        String result = sendPost(reqURL + "/pjInterface/saveCaseEvaluateOffLine.do", params);
        return result;
   }
    
    /**
     * 调用【线下修改办件评价信息】
     * @return
     * @throws Exception
     * @author wangxl
     * @date 2019-10-15
     */
    public static String updateCaseEvaluateOffLine() throws Exception{
        String requestData = "{'content':'调用【线下修改办件评价信息】','deptCode':'0002','projectNo':'20W000001N'"
        		+ ",'appStatus':'1','rectification':'整体反馈','flag':'U','dataSource':'1','effectivEvalua':'1'}";
        Map<String, String> params = new HashMap<String, String>();
        params.put("apiKey", apiKey);
        params.put("systemPwd", systemPwd);
        String encData = SM2Util.sm2Encrypt(publicKey, requestData);
        params.put("requestData", encData);
        String dataSign = SM2Util.sm2Sign(privateKey, apiKey, encData);
        params.put("dataSign", dataSign);
        String result = sendPost(reqURL + "/pjInterface/updateCaseEvaluateOffLine.do", params);
        return result;
   }
    
    /**
     * 调用【线下超期系统默认评价提交接口】
     * @return
     * @throws Exception
     * @author wangxl
     * @date 2019-10-15
     */
    public static String saveCaseEvaluateOffLineCQ() throws Exception{
    	String requestData = "{'pjTerm':'3','content':'调用【线下超期系统默认评价提交接口】','serviceName':'测试事项','pf':'接口调用','evalDetail':'401,402,403'"
        		+ ",'deptCode':'0001','taskCode':'0000002','taskHandleItem':'0000025','subMatter':'测试主题','projectNo':'20W000001N'"
        		+ ",'proStatus':'2','proDepart':'测试部门','certKey':'542129197802160506','userName':'龙心一','userProp':'1'"
        		+ ",'assessNumber':'1','appStatus':'1','flag':'I','dataSource':'1','promisetime':'1','acceptDate':'2019-10-15'}";
        Map<String, String> params = new HashMap<String, String>();
        params.put("apiKey", apiKey);
        params.put("systemPwd", systemPwd);
        String encData = SM2Util.sm2Encrypt(publicKey, requestData);
        params.put("requestData", encData);
        String dataSign = SM2Util.sm2Sign(privateKey, apiKey, encData);
        params.put("dataSign", dataSign);
        String result = sendPost(reqURL + "/pjInterface/saveCaseEvaluateOffLineCQ.do", params);
        return result;
   }
    
    /**
     * 调用【线上超期评】
     * @return
     * @throws Exception
     * @author wangxl
     * @date 2019-10-15
     */
    public static String saveCaseEvaluateonLineCQ() throws Exception{
    	String requestData = "{'pjTerm':'3','content':'调用【线上超期系统默认评价提交接口】','serviceName':'测试事项','pf':'接口调用','evalDetail':'401,402,403'"
        		+ ",'deptCode':'0001','taskCode':'0000002','taskHandleItem':'0000025','subMatter':'测试主题','projectNo':'20W000001N'"
        		+ ",'proStatus':'2','proDepart':'测试部门','certKey':'542129197802160506','userName':'龙心一','userProp':'1'"
        		+ ",'assessNumber':'1','appStatus':'1','flag':'I','dataSource':'1','promisetime':'1','acceptDate':'2019-10-16'}";
        Map<String, String> params = new HashMap<String, String>();
        params.put("apiKey", apiKey);
        params.put("systemPwd", systemPwd);
        String encData = SM2Util.sm2Encrypt(publicKey, requestData);
        params.put("requestData", encData);
        String dataSign = SM2Util.sm2Sign(privateKey, apiKey, encData);
        params.put("dataSign", dataSign);
        String result = sendPost(reqURL + "/pjInterface/saveCaseEvaluateonLineCQ.do", params);
        return result;
   }
    
    public static void main(String[] args) {
        try {
        	//initZwfwCaseEvaluateInter();
//        	  String result = listEvaluationContent();
//        	String result = listEvaluationContent();
        	//String result = updateCaseEvaluate();
        	//String result = saveCaseEvaluateOffLine();
//        	String result = updateCaseEvaluateOffLine();
//        	String result = saveCaseEvaluateOffLineCQ();
//        	String result = saveCaseEvaluateonLineCQ();
//            System.out.print(result);
//        CASE_ID, CASE_NUM, PROJECT_NAME, SERVICE_TYPE, APPLY_NAME, APPLY_TIME, ORGAN_NAME, BL_STATUS, IS_DELETE, CARD_NO, SERVICE_ID, MODIFY_DATE, SERVICE_NAME, LXR_PHONE, SERVICE_CODE, ORGAN_CODE, DISTRICT_NAME, ORGAN_OID, DISTRICT_CODE, OBJECT_TYPE, CARD_NAME, BJ_TIME)
//        VALUES ('bb597a8b6c0065e1016c0090e454000e', '20W000001X', NULL, '0', '李艳青', '2019-07-17 23:33:11', '林芝市交通运输局', '3', 'N', '420104198609150443', 'bb597a9e6bdd418a016bdf08f9c62255', '2019-08-03 14:15:43', '“96169”投诉服务热线', NULL, '115426000099298368542018004000', '115426000099298368', '林芝市', 'bb597a8c6bb19fde016bb1bc59a80cf8', '540400000000', '1', NULL, NULL);
//                Map<String, String> m = new HashedMap();
//                m.put("backUrl", "www.baidu.com");
//                m.put("caseNum", "20W000001X");
//                m.put("serviceCode", "115426000099298368542018004000");
//                m.put("districtCode", "540400000000");
//                m.put("districtName", "林芝市");
//                m.put("organCode", "115426000099298368");
//                m.put("organName", "林芝市交通运输局");
//                m.put("applyName", "李艳青");
//                m.put("serviceName", "“96169”投诉服务热线");
//                m.put("pjSource", "pjSource");
//                m.put("applyTime", "2019-07-17 23:33:11");
//                System.out.println(JsonUtil.mapToJson(m));
            long a = 521201;
            System.out.println(a%10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @author liyanq
     * @date 2019年09月18日
     * @param url  发送请求的 URL
     * @param params  请求的参数集合
     * @return 远程资源的响应结果
     */
    private static String sendPost(String url, Map<String, String> params) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // POST方法
            conn.setRequestMethod("POST");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            if (params != null) {
                StringBuilder param = new StringBuilder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (param.length() > 0) {
                        param.append("&");
                    }
                    param.append(entry.getKey());
                    param.append("=");
                    param.append(entry.getValue());
                }
                out.write(param.toString());
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }
}
