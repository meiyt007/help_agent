package com.zfsoft.superwindow.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.rest.pojo.face.FaceMatchRequest;
import com.zfsoft.rest.pojo.face.FaceMatchResponse;
import com.zfsoft.rest.service.face.IFaceRestService;
import com.zfsoft.superwindow.util.fa.AiTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 硬件扫描对接类
 * Dongxl
 */
@RestController
@Slf4j
@RequestMapping(value = "/hardwareScan")
public class HardwareScanController {

    @Value("${zfsoft.hardwareScan.url}")
    private String scanUrl;

//    @Resource
//    private IFaceRestService faceRestService;

    @Value("${zfsoft.hardwareScan.imgIcardUrl}")
    private String imgIcardUrl;

    @Resource
    private AiTokenUtil aiTokenUtil;

    @Value("${zfsoft.yjyzsScan.rzbdUrl}")
    private String rzbdUrl;

    /**
     * 打开摄像头接口
     * Dongxl
     * @return
     */
    @GetMapping(value ="openCamera")
    public ApiResultSet openCamera(){
        String res= HttpUtil.get(scanUrl + "/camera/Open");
        if(StrUtil.isNotEmpty(res)){
            JSONObject jsonObj = JSONObject.parseObject(res);
            Map<String,Object> map=jsonObj;
            if(!map.get("state").equals("sucess")){
                this.closeCamera();
            }else{
                return new ApiResultSet(map);
            }
        }
        return new ApiResultSet();
    }

    /**
     * 关闭摄像头
     * @return
     */
    @GetMapping(value ="closeCamera")
    public ApiResultSet closeCamera(){
        String res= HttpUtil.get(scanUrl + "/camera/Close");
        if(StrUtil.isNotEmpty(res)){
            JSONObject jsonObj = JSONObject.parseObject(res);
            Map<String,Object> map=jsonObj;
            if(!map.get("state").equals("sucess")){
                return new ApiResultSet(map.get("tips"));
            }
        }
        return new ApiResultSet();
    }

    /**
     * 获取人像信息
     * @return
     */
    @GetMapping(value ="getImage")
    public ApiResultSet getImage(){
        String res= HttpUtil.get(scanUrl + "/camera/GetImage");
        if(StrUtil.isNotEmpty(res)) {
            JSONObject jsonObj = JSONObject.parseObject(res);
            Map<String, Object> map = jsonObj;
            return new ApiResultSet(map);
        }
        return new ApiResultSet();
    }

    /**
     * 身份证打开
     * @return
     */
    @GetMapping(value ="/idcardOpen")
    public ApiResultSet idcardOpen(){
        String res= HttpUtil.get(scanUrl + "/idcard/Open");
        if(StrUtil.isNotEmpty(res)){
            JSONObject jsonObj = JSONObject.parseObject(res);
            Map<String,Object> map=jsonObj;
            //打开失败关闭识别
            if(!map.get("state").equals("sucess")){
                this.idcardClose();
            }
            return new ApiResultSet(map);
        }
        return new ApiResultSet();
    }

    /**
     * 身份证关闭摄像头
     * @return
     */
    @GetMapping(value ="idcardClose")
    public ApiResultSet idcardClose(){
        String res= HttpUtil.get(scanUrl + "/idcard/Close");
        if(StrUtil.isNotEmpty(res)){
            JSONObject jsonObj = JSONObject.parseObject(res);
            Map<String,Object> map=jsonObj;
            if(!map.get("state").equals("sucess")){
                return new ApiResultSet(map.get("tips"));
            }
        }
        return new ApiResultSet();
    }

    /**
     * 获取身份证信息
     * @return
     */
    @GetMapping(value ="getData")
    public ApiResultSet getData(){
        String res= HttpUtil.get(scanUrl + "/idcard/GetData");
        if(StrUtil.isNotEmpty(res)) {
            JSONObject jsonObj = JSONObject.parseObject(res);
            Map<String, Object> map = jsonObj;
            if(map.get("imgData")!=null){
                Map<String,Object> param=new HashMap<>();
                //十六进制图片解析为base64
                String str64= HttpUtil.post(imgIcardUrl+"?imgdata="+map.get("imgData").toString(),param);
                if(StrUtil.isNotEmpty(str64)){
                    JSONObject jsonObjimg = JSONObject.parseObject(str64);
                    Map<String,Object> map2=jsonObjimg;
                    if(map2.get("state").equals("sucess")){
                        map.put("imgData",map2.get("data"));
                    }
                }

            }
            return new ApiResultSet(map);
        }
        return new ApiResultSet();
    }
    ////十六进制图片解析为base64
    @PostMapping(value ="getDataIdCardImage")
    public ApiResultSet getDataIdCardImage(@RequestParam("imgdata")String imgdata){
        Map<String,Object> param=new HashMap<>();
        if(imgdata!=null){
            String str64= HttpUtil.post(imgIcardUrl+"?imgdata="+imgdata,param);
            if(StrUtil.isNotEmpty(str64)){
                JSONObject jsonObjimg = JSONObject.parseObject(str64);
                Map<String,Object> map2=jsonObjimg;
                if(map2.get("state").equals("sucess")){
                    param.put("imgData",map2.get("data"));
                }
            }
        }
        return new ApiResultSet(param);
    }


    /**
     * 百度接口人像比对
     * @return
     */
    @PostMapping(value ="comparsionFace")
    public ApiResultSet comparsionFace(@RequestBody FaceMatchRequest var1){
//        FaceMatchRequest faceMatchRequest = aiTokenUtil.getTokenRequest(FaceMatchRequest.class);
//        faceMatchRequest.setBase64ImageOne(var1.getBase64ImageOne());
//        faceMatchRequest.setBase64ImageTwo(var1.getBase64ImageTwo());
//        FaceMatchResponse res= faceRestService.faceMatch(faceMatchRequest);
        return new ApiResultSet();

    }

    /**
     * 人脸比对
     * @param var1
     * @return
     */
    @PostMapping(value ="comparFace")
    public ApiResultSet comparFace(@RequestBody FaceMatchRequest var1){
        Map map=new HashMap();
        map.put("data1",var1.getBase64ImageOne());
        map.put("data2",var1.getBase64ImageTwo());
        String result=HttpUtil.post(rzbdUrl+"face_verification_base64",map);
        if(StrUtil.isNotEmpty(result)){
            JSONObject jsonObjimg = JSONObject.parseObject(result);
            Map<String,Object> map2=jsonObjimg;
            return new ApiResultSet(map2);
        }
        return new ApiResultSet();

    }

}
