package com.zfsoft.ocr.manager.textInOcr;


import com.zfsoft.ocr.data.pojo.common.OcrCallRecord;
import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrRequest;
import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrResponse;
import com.zfsoft.ocr.manager.common.OcrCallRecordManager;
import com.zfsoft.ocr.util.FileManageUtil;
import com.zfsoft.ocr.util.textIn.CommonUtil;
import com.zfsoft.ocr.util.textIn.TextInOcr;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/***
* @Description:  通用识别接口
* @Author:liangss
* @Date:2021/12/31
* @Param:
*/
@Service
@Slf4j
public class TextInCommonUseRestManager {
    @Value("${zfsoft.textOcr.appId}")
    public  String appId;

    @Value("${zfsoft.textOcr.secretCode}")
    public  String secretCode;

    @Resource
    private OcrCallRecordManager ocrCallRecordManager;


    /***
    * @Description: 通用识别接口
    * @Author:liangss
    * @Date:2021/10/28
    * @Param: [ocrCustomTemplateRequest]  TextInOcrResponse
    */
    public String customCommonUseOcr(TextInOcrRequest textInOcrRequest) {
        log.info("进入通用文字识别方法");
        String imgUrl=textInOcrRequest.getImgUrl();
        String imgBase64=textInOcrRequest.getImgBase64();
        TextInOcrResponse textInOcrResponse = new TextInOcrResponse();
        String url=  CommonUtil.tywzsb_url;
         //接口地址
         String  interUrl="commonUseOcr/textInOcrCommonUseRestService/customCommonUseOcr";
        //接口code
         String interCode="customCommonUseOcr";
        //接口名称
         String interName="通用文字识别";
        BufferedReader in = null;
        DataOutputStream out = null;
        String result = "";
        byte[] imgData = new byte[0];
        //byte[] imgData = TextInOcrUtil.readfile(imgUrl);
        String md5="";
        try {
            if(StringUtils.isNotEmpty(imgUrl)){
                imgData= FileManageUtil.getImgFromUrl(imgUrl);
            }else{
                //base64转byte[]
                if(StringUtils.isNotEmpty(imgBase64)){
                    imgData= DatatypeConverter.parseBase64Binary(imgBase64);
                }
            }
            //获取md5的取值
            md5=TextInOcr.getMd5(imgData);
            OcrCallRecord ocrCallRecordNew=ocrCallRecordManager.queryDbOcrCallRecordByMd5AndInterUrl(md5,interUrl);
            //判断之前是否存储过ocr
            if(null!=ocrCallRecordNew&&StringUtils.isNotEmpty(ocrCallRecordNew.getInterResult())){
                result=ocrCallRecordNew.getInterResult();
            }else{

                URL realUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
                conn.setRequestProperty("connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "application/octet-stream");
                conn.setRequestProperty("x-ti-app-id", appId);
                conn.setRequestProperty("x-ti-secret-code", secretCode);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST"); // 设置请求方式
                out = new DataOutputStream(conn.getOutputStream());
                out.write(imgData);
                out.flush();
                out.close();
                in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
                if(StringUtils.isNotEmpty(interUrl)&&StringUtils.isNotEmpty(result)){
                    OcrCallRecord ocrCallRecord=new OcrCallRecord();
                    ocrCallRecord.setInterUrl(interUrl);
                    ocrCallRecord.setInterName(interName);
                    ocrCallRecord.setInterResult(result);
                    ocrCallRecord.setInterCode(interCode);
                    ocrCallRecord.setMd5(md5);
                    ocrCallRecordManager.saveOrUpdateOcrCallRecord(ocrCallRecord);
                }


            }


            /* byte[] imgData = TextInOcr.readfile(imgUrl);*/
            /*  byte[] imgData= FileManageUtil.getImgFromUrl(imgUrl);*/


        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally {
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





      /*  System.out.println(result);*/


        return result;
    }



}
