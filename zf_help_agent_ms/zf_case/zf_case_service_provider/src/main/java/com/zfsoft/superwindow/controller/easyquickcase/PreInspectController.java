package com.zfsoft.superwindow.controller.easyquickcase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.service.QlCaseMaterialService;
import com.zfsoft.microservice.platform.data.sys.SysAtta;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.feign.settings.FormDataService;
import com.zfsoft.superwindow.feign.settings.MiddleWebFeignService;
import com.zfsoft.superwindow.feign.settings.SysAttaFeginService;
import com.zfsoft.superwindow.feign.settings.data.FormDataVo;
import com.zfsoft.superwindow.service.easyquickcase.PreInspectService;
import com.zfsoft.superwindow.service.easyquickcase.data.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
public class PreInspectController implements PreInspectService {

    @Resource
    private WebController webController;

    @Resource
    private FormDataService formDataService;

    @Resource
    private SysAttaFeginService sysAttaFeginService;

    @Resource
    private MiddleWebFeignService middleWebFeignService;

    @Resource
    private QlCaseMaterialService qlCaseMaterialServiceFeginService;


    @Override
    public ApiResultSet pushCase(String serviceOid, String caseOid) {
        //记录日志
        String callType = "1";
        String interId = "";
        ApiResultSet data = new ApiResultSet();
        try {
            //根据caseOid查询电子表单信息
            ApiResultSet formData = webController.getFormFillInfos(serviceOid, caseOid, "");
            //解析数据拿到  authorizeKey、designOid
            JSONObject json = JSON.parseObject(formData.getData().toString());
            JSONArray array = json.getJSONArray("data");
            String authorizeKey = array.getJSONObject(0).getString("authorizeKey");
            String designOid = array.getJSONObject(0).getString("designOid");
            //电子表单formData数据
            ApiResultSet<FormDataVo> formInfo = formDataService.getFormData(authorizeKey, designOid, caseOid);
            //获取formData数据
            JSONObject formJson = JSON.parseObject(formInfo.getData().getFormData());
        /*String example = "{\n" +
                "    \"zhengjianhaoma\":\"152727199510224826\",\n" +
                "    \"zhanghuyue\":\"18000\",\n" +
                "    \"zhanghuyuelixi\":\"215.32\",\n" +
                "    \"gongjijinzhanghao\":\"0100435817\",\n" +
                "    \"zongtique\":\"18215.32\",\n" +
                "    \"renxingming\":\"高虹雨\",\n" +
                "    \"tiqufangshi\":\"转账提取\",\n" +
                "    \"shoukuanrenleixing\":\"本人\",\n" +
                "    \"yiyouyinxingkaliebia\":\"中国建设银行股份有限公司乌审旗支行(7658392283)\",\n" +
                "    \"kaihuzhixing\":\"中国建设银行股份有限公司乌审旗支行\",\n" +
                "    \"yinxingkazhanghao\":\"7658392283\",\n" +
                "    \"kaihuyinxing\":\"105\",\n" +
                "    \"yinxingkasuoshudi\":\"asdasdasdasdad\",\n" +
                "    \"caseOid\":\"c72cc3b86c6e4735ae70788f9291e034\"\n" +
                "}";
        JSONObject formJson = JSON.parseObject(example);*/
            //个人姓名
            String name = formJson.getString("renxingming");
            //身份证号
            String cardNumber = formJson.getString("zhengjianhaoma");

            //调用公积金查询个人信息接口
            String jgbm = "01";//朱老板还没给出方法，这边先写死
            ResponseData<JSONObject> personJson = middleWebFeignService.searchPersonInfo(jgbm, cardNumber, callType, interId);
            //获取业务流水号
            String ywlsh = personJson.getData().getString("ywlsh");

            //先用caseOid查询到材料附件oid
            ApiResultSet<List<QlCaseMaterial>> materialInfo = qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
            List<QlCaseMaterial> materialList = materialInfo.getData();
            //调用公积金获取档案材料接口
            /**
             * 公积金材料名称
             *  现场影像
             *  银行卡
             *  身份证件（包括身份证、结婚证、户口本）
             */
            String ygdh = personJson.getData().getString("grbh");//客户编号
            String ywfl = "02";//业务分类
            String ywlb = "110101";//业务类别
            ResponseData<JSONObject> archives = middleWebFeignService.archivesSearch(ygdh, ywfl, ywlb, jgbm, callType, interId);
            //获取mc和bm
            JSONObject archivesJson = archives.getData();
            //[mc,bm]
            HashMap<String, String> archivesMap = new HashMap();
            for (int i = 0; i < archivesJson.getJSONArray("bm").size(); i++) {
                archivesMap.put(archivesJson.getJSONArray("mc").getString(i), archivesJson.getJSONArray("bm").getString(i));
            }
            //遍历对比材料，推送上去
            for (QlCaseMaterial material : materialList) {
                /**
                 * 这边的材料名称
                 *  现场影像（本人自拍照）
                 *  中国人民共和国居民身份证
                 *  银行卡
                 */
                Set<String> keys = archivesMap.keySet();
                //这边其实就是三个材料的对比
                for (String key : keys) {
                    /**
                     * 对比材料名称 -- 这边只能固定的对比三种材料
                     */
                    String attatOid = "";
                    String filecode = "";//材料编码
                    String filename = "";//材料名称
                    if (key.indexOf("现场影像") != -1 && material.getMaterialName().indexOf("现场影像") != -1) {//现场影像
                        attatOid = material.getMaterialAttaOid();
                        filecode = archivesMap.get(key);
                        filename = key;
                    } else if (key.indexOf("身份证") != -1 && material.getMaterialName().indexOf("身份证") != -1) {//身份证
                        attatOid = material.getMaterialAttaOid();
                        filecode = archivesMap.get(key);
                        filename = key;
                    } else if (key.indexOf("银") != -1 && material.getMaterialName().indexOf("银") != -1) {//银行卡
                        attatOid = material.getMaterialAttaOid();
                        filecode = archivesMap.get(key);
                        filename = key;
                    }
                    if (Strings.isNotBlank(attatOid) && Strings.isNotBlank(filecode) && Strings.isNotBlank(filename)) {
                        //文件参数
                        HashMap<String, Object> fileMap = Maps.newHashMapWithExpectedSize(2);
                        ApiResultSet<SysAtta> sysAtta = sysAttaFeginService.getSysAttaByAttaOid(attatOid);
                        fileMap.put("path", sysAtta.getData().getFastdfsNginxUrl());
                        fileMap.put("base64", getBase64(sysAtta.getData().getFastdfsNginxUrl()));
                        //调用公积金图片上传接口
                        String scanpages = "1";//文件数量
                        middleWebFeignService.archivesUpload(fileMap, filecode, scanpages, filename, ygdh, ywlsh, callType, interId);
                    }
                }
            }

            //最后调用离休、退休
            /*
             * tqyy  0201
             * tqfs  02
             * zrzxqc  提取人姓名 renxingming
             * tqyhzh  提取银行账号 yinxingkazhanghao
             * skyh    收款银行名称 kaihuzhixing
             * skrlx   本人
             * zjhm    提取人证件号码 zhengjianhaoma
             * ywlsh   业务流水号
             * */
            String tqfs = "02";
            String zrzxqc = name;
            String tqyhzh = personJson.getData().getString("yinxingkazhanghao");
            String skyh = personJson.getData().getString("kaihuzhixing");
            String beizhu = "";
            String zjhm = cardNumber;
            String skrlx = "本人";
            ResponseData<JSONObject> pushData = middleWebFeignService.retireExtract(tqfs, zrzxqc, tqyhzh, skyh, beizhu, zjhm, skrlx, ywlsh, callType, interId);
            //解析推送返回值，因为一直没有推送测试所以这边的解析就按照文档里来 todo
            String dataStr = pushData.getData().getString("data");
            log.info("推送办件结果：{}", dataStr);
        /*dataStr = "{" +
                "  \"success\": true," +
                "  \"msg\": \"已提交至中心版审核\" " +
                " }";*/
            JSONObject dataJson = JSON.parseObject(dataStr);
            if (dataJson.getBoolean("success")) {
                data.setCode(200);
                data.setMessage("推送成功");
            }else{
                data.setCode(201);
                data.setMessage("推送失败："+dataJson.getString("msg"));
            }
        }catch (Exception e){
            data.setCode(201);
            data.setMessage("推送失败："+e.getMessage());
        }
        return data;
    }

    /**
     * 根据url转成base64
     *
     * @param url
     * @return
     */
    private String getBase64(String url) {
        String result = "";
        //转成Base64
        InputStream httpsFile = null;
        try {
            httpsFile = getHttpsFile(url);
            byte[] bytes = IOUtils.toByteArray(httpsFile);
            result = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public InputStream getHttpsFile(String fileUrl) throws NoSuchProviderException, NoSuchAlgorithmException, IOException, KeyManagementException {
        // 创建SSLContext对象，使用指定的信任管理器初始化
        if (fileUrl.contains("https:")) {//https
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(fileUrl);
            HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
            httpsConn.setSSLSocketFactory(ssf);
            httpsConn.setDoInput(true);
            httpsConn.setDoOutput(true);
            return httpsConn.getInputStream();
        } else {//http
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10 * 1000);
            return connection.getInputStream();
        }
    }

}
