package com.zfsoft.superwindow.controller.wgpj;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.settings.data.SysConfig;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.superwindow.data.wgpj.EmotionRecognitionRecord;
import com.zfsoft.superwindow.data.wgpj.ImageVo;
import com.zfsoft.superwindow.dbaccess.data.DbEmotionRecognitionRecord;
import com.zfsoft.superwindow.feign.settings.SysConfigFeignService;
import com.zfsoft.superwindow.manager.wgpj.EmotionRecognitionRecordManager;
import com.zfsoft.superwindow.service.wgpj.IDoubleScreenInteractionService;
import com.zfsoft.superwindow.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.UUID;


/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/8/12 16:51
 */
@RestController
@Slf4j
public class DoubleScreenInteractionController implements IDoubleScreenInteractionService {

    @Value("${zfsoft.screenInteraction.emotionRecognitionUrl}")
    private String emotionUrl;

    @Resource
    private SysConfigFeignService sysConfigFeignService;

    @Resource
    private EmotionRecognitionRecordManager emotionRecognitionRecordManager;

    @Value("${zfsoft.upImage.windows}")
    private String upImageWindows;

    @Value("${zfsoft.upImage.linux}")
    private String upImageLinux;

    @Override
    public ApiResultSet<String> getPjServiceSystem() {
        ApiResultSet<SysConfig> config = sysConfigFeignService.getSysConfigByCode("PBPJ_SYSTEM");
        String type ="2";
        if(config.getData() !=null){
            type = config.getData().getValue();
        }
        ApiResultSet<String> apiResultSet = new ApiResultSet<String>();
        apiResultSet.setData(type);
        return apiResultSet;
    }


    @Override
    public ApiResultSet<String> getEmotionJudgment(ImageVo imageVo) {
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        String userOid = "00000000123";
        if(currentLoginUser!=null){
            userOid = currentLoginUser.getUserOid();
        }
        String responseBody = null;
        String caseNumber = imageVo.getCurrentNum();
        int  type = 0;//0 上传到服务器，1 不上传
        String  picAddress = "";//图片地址
        ApiResultSet<SysConfig> config = sysConfigFeignService.getSysConfigByCode("LXCS");
        if(config!=null){
            type = Integer.parseInt(config.getData().getValue());
        }
        File  tempFile =null ;
        FileSystemResource fileSystemResource = null;
        BASE64Decoder decoder = null;
        ByteArrayInputStream stream = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            //创建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
            //FileSystemResource fileSystemResource = null;
            //BASE64Decoder decoder = null;
            //ByteArrayInputStream stream = null;
            byte[] bytes = null;
            //File  tempFile;
            if(StringUtils.isEmpty(imageVo.getBase64Img())){
                //File newfile =new File("C:\\Users\\wangwg\\Desktop\\233.jpg");
                tempFile =new File("E:\\imagetest\\1.jpg");
                fileSystemResource = new FileSystemResource(tempFile);
            }else{
                decoder = new BASE64Decoder();
                bytes = decoder.decodeBuffer(URLDecoder.decode(imageVo.getBase64Img(), "utf-8"));
                stream = new ByteArrayInputStream(bytes);
                String imagePath ="";
                if(CommonUtil.isWindows()){
                    //imagePath = "/emotions/";
                    imagePath = upImageWindows;
                }else{
                    //imagePath = "/soft/usr/image/";
                    imagePath =  upImageLinux;
                }
                tempFile = new File(imagePath+ UUID.randomUUID().toString()+".jpg");
                FileUtils.copyInputStreamToFile(stream, tempFile);
                fileSystemResource = new FileSystemResource(tempFile);
            }
            map.add("image", fileSystemResource);

            //0 上传到服务器，1 不上传
            if(type==0){
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                UploadUtil uploadUtil = new UploadUtil(request);
                //MultipartFile file = new MockMultipartFile("File", tempFile.getOriginalFilename(),"text/plain", tempFile.getInputStream());
                MultipartFile file = new MockMultipartFile("File", tempFile.getName(),"text/plain",  new FileInputStream(tempFile));
                String filePath = null;
                try {
                    filePath = uploadUtil.uploadFile(file);
                    SysAttaTemp tempAtta = uploadUtil.getSysAttaFile(filePath, userOid);
                    picAddress =  tempAtta.getFastdfsNginxUrl();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("上传图片失败",e.getMessage());
                }
            }
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(emotionUrl, entity, String.class);
            responseBody = responseEntity.getBody();//[[0,"neutral",0.9946483969688416,94,36,197,206]]
            //System.out.println("情绪识别返回值(null代表没有识别到人脸)："+responseBody);
            //测试造数据
            //responseBody =  "{\"result\":\"sad\",\"confidence \":0.5703148245811462,\"location\":[168,47,268,143]}";
            responseBody =  responseBody.replace("confidence ","confidence");
            JSONObject result = JSONObject.parseObject(responseBody);
/*            if(result.containsKey("confidence ")&&result.containsKey("location")){
                if("null".equalsIgnoreCase(result.getString("confidence "))){
                    if("null".equalsIgnoreCase(result.getString("location"))){
                        return new ApiResultSet(responseBody);
                    }
                }
            }*/
            if(result.containsKey("location")){
                String location =  result.getString("location");
                    /*if("null".equalsIgnoreCase(location)){
                        return new ApiResultSet(responseBody);
                    }*/
                if(null == location){
                    ApiResultSet  r = new ApiResultSet(responseBody);
                    //r.setCode(ApiResultSet.UNKNOWN_ERROR);
                    //return new ApiResultSet(responseBody);
                    return r;
                }
            }
            if(result.containsKey("confidence")){
                String confidence =  result.getString("confidence");
                System.out.println(confidence);

            }
            System.out.println("情绪识别返回值："+responseBody);
/*            if(!responseBody.equals("null")){
                emotionRecognitionRecordManager.saveEmotionRecognition(responseBody,caseNumber);
                handleServiceStarManager.calculateAverageStar(caseNumber);
            }*/
            emotionRecognitionRecordManager.saveEmotionRecognition(responseBody,caseNumber,imageVo.getVirtualBusinessOid(),type,picAddress);
            //handleServiceStarManager.calculateAverageStar(caseNumber);
            //handleServiceStarManager.calculateAverageStar(imageVo.getVirtualBusinessOid(),imageVo.getCurrentNum());
        } catch (IOException e) {
            log.error("超级综窗柜台情绪识别接口出错  ",e.getMessage());
            e.printStackTrace();
        }finally {
            if(stream!=null){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(tempFile!=null && tempFile.exists()){
                tempFile.delete();
            }
        }
        return  new ApiResultSet(responseBody);
    }

    @Override
    public ApiResultSet getEmotionList(EmotionRecognitionRecord emotionRecognitionRecord, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<DbEmotionRecognitionRecord> list = emotionRecognitionRecordManager.queryEmotionRecognitionList(emotionRecognitionRecord);
        PageResult<DbEmotionRecognitionRecord> pageResult = new PageResult<>(
                ((Page) list).getPageNum(),
                ((Page) list).getPageSize(),
                ((Page) list).getTotal()
        );
        pageResult.setData(list);
        log.info("获取情绪列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        return new ApiResultSet(pageResult);
    }

    @Override
    public ApiResultSet<EmotionRecognitionRecord> getEmotionRecognitionRecordById(Long id) {
        EmotionRecognitionRecord emotionRecognitionRecord = emotionRecognitionRecordManager.getEmotionRecognitionRecordById(id);
        return new ApiResultSet(emotionRecognitionRecord);
    }

    @Override
    public ApiResultSet getEmotionListById(String virtualBusinessOid,String caseOid) {
        List<DbEmotionRecognitionRecord> dbEmotionRecognitionRecordList = emotionRecognitionRecordManager.getEmotionListById(virtualBusinessOid,caseOid);
        return new ApiResultSet(dbEmotionRecognitionRecordList);
    }

}
