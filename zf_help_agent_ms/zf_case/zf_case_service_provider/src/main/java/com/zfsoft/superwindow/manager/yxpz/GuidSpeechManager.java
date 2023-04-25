package com.zfsoft.superwindow.manager.yxpz;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.platform.data.sys.SysAtta;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.superwindow.data.yxpz.GuidSpeech;
import com.zfsoft.superwindow.dbaccess.dao.DbGuidSpeechMapper;
import com.zfsoft.superwindow.dbaccess.data.DbGuidSpeech;
import com.zfsoft.superwindow.dbaccess.data.DbGuidSpeechExample;
import com.zfsoft.superwindow.feign.settings.SysAttaFeginService;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.CommonUtil;
import com.zfsoft.superwindow.util.DateUtil;
import com.zfsoft.superwindow.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
引导语管理功能
 @Dongxl
 **/
@Service
@Slf4j
public class GuidSpeechManager {

    @Resource
    private DbGuidSpeechMapper guidSpeechMapper;
    @Resource
    private SysAttaFeginService sysAttaFeginService;

    /**
     * 分页查询引导语信息
     * @param guidSpeech
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResult queryGuidSpeechPage(GuidSpeech guidSpeech, Integer pageNum, Integer pageSize) {
        //分页参数
        if ( pageNum==null || pageNum <= 0) {
            pageNum = 0;
        }
        if (pageSize==null || pageSize <= 0) {
            pageSize = 10;
        }
        DbGuidSpeechExample example=new DbGuidSpeechExample();
        DbGuidSpeechExample.Criteria cria=example.createCriteria();
        if(guidSpeech!=null){
            if(StrUtil.isNotEmpty(guidSpeech.getGuideSpeechName())){
                cria.andGuideSpeechNameLike("%"+guidSpeech.getGuideSpeechName()+"%");
            }
            if(StrUtil.isNotEmpty(guidSpeech.getGuideSpeechCode())){
                cria.andGuideSpeechCodeEqualTo(guidSpeech.getGuideSpeechCode());
            }
        }
        cria.andDeleteStatusEqualTo(String.valueOf(0));
        example.setOrderByClause(" CREATE_DATE desc");
        PageHelper.startPage(pageNum,pageSize);
        Page<DbGuidSpeech> listGuidSpeech= (Page<DbGuidSpeech>) guidSpeechMapper.selectByExample(example);
        PageResult<GuidSpeech> result=new PageResult(listGuidSpeech.getPageNum(),listGuidSpeech.getPageSize(),listGuidSpeech.getTotal());
        List<GuidSpeech> list=listGuidSpeech.stream().map(guidspeech->{
            GuidSpeech speech=new GuidSpeech();
            BeanUtils.copyProperties(guidspeech,speech);
            return speech;
        }).collect(Collectors.toList());
        result.setData(list);
        return result;
    }

    /**
     * 根据主键查询详细信息
     * @param oid
     * @return
     */
    public GuidSpeech getOneGuidSpeech(String oid) {
        if(StrUtil.isNotEmpty(oid)){
          DbGuidSpeech guidSpeech=  guidSpeechMapper.selectByPrimaryKey(oid);
          if(guidSpeech!=null){
              GuidSpeech speech=new GuidSpeech();
              BeanUtils.copyProperties(guidSpeech,speech);
              return speech;
          }
        }
        return null;
    }

    /**
     * 根据主键删除信息
     * @param oid
     * @return
     */
    public String deleteOneGuidSpeech(String oid) {
        if(StrUtil.isNotEmpty(oid)) {
            DbGuidSpeech guidSpeech = guidSpeechMapper.selectByPrimaryKey(oid);
            if (guidSpeech != null) {
                guidSpeech.setDeleteStatus(String.valueOf(1));
                guidSpeechMapper.updateByPrimaryKey(guidSpeech);
            }
        }
        return null;
    }

    /**
     * 保存或修改信息
     * @param guidSpeech
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public String saveOrUpdate(@Validated GuidSpeech guidSpeech) {
//        if(guidSpeech!=null){
//           boolean flag= checkCodeExist(guidSpeech);
//           if(flag){
//               return "语音编码重复!";
//           }
//            if(guidSpeech.getBuildType().equals("1")){//附件上传
//                guidSpeech.setGuideSpeechContent(null);
//                guidSpeech.setConvertStatus(null);
//            }else{//文字转语音
//                SpeechBase64ByContentRequest content=new SpeechBase64ByContentRequest();
//                content.setContent(guidSpeech.getGuideSpeechContent());
//               SpeechBase64ByContentResponse respon= speechFeginService.getSpeechBase64ByContent(content);
//               if(respon!=null && StrUtil.isNotEmpty(respon.getBase64())){
//                   try {
//                     String str=  this.decoderBase64File(respon.getBase64());
//                     if(StrUtil.isNotEmpty(str)){
//                         guidSpeech.setConvertStatus("Y");
//                         guidSpeech.setAttaOid(str);
//                     }else{
//                         guidSpeech.setConvertStatus("N");
//                     }
//                   }catch (Exception e){e.printStackTrace();}
//
//               }else{
//                   guidSpeech.setConvertStatus("N");
//               }
//
//            }
            if(StrUtil.isNotEmpty(guidSpeech.getOid())){
                DbGuidSpeech guidSpeechold = guidSpeechMapper.selectByPrimaryKey(guidSpeech.getOid());
                if(guidSpeechold==null){
                    return "信息不存在！";
                }
                guidSpeech.setOid(guidSpeechold.getOid());
                guidSpeech.setDeleteStatus(String.valueOf(0));
                guidSpeech.setCreateDate(guidSpeechold.getCreateDate());
                guidSpeech.setModifyDate(new Date());
                BeanUtils.copyProperties(guidSpeech,guidSpeechold);
                guidSpeechMapper.updateByPrimaryKey(guidSpeechold);
            }else{
                guidSpeech.setOid(UUIDUtil.randomUUID().replaceAll("-",""));
                guidSpeech.setCreateDate(new Date());
                guidSpeech.setDeleteStatus(String.valueOf(0));
                guidSpeech.setModifyDate(new Date());
                DbGuidSpeech speech=new DbGuidSpeech();
                BeanUtils.copyProperties(guidSpeech,speech);
                guidSpeechMapper.insert(speech);
            }
        return null;
    }

    public GuidSpeech selectByGuidSpeechCode(String code){
        if(StrUtil.isNotEmpty(code)){
          DbGuidSpeech guid=  guidSpeechMapper.selectByGuidSpeechCode(code);
          if(guid!=null){
              GuidSpeech speech=new GuidSpeech();
              BeanUtils.copyProperties(guid,speech);
              if(StrUtil.isNotEmpty(guid.getAttaOid())){
                ApiResultSet<SysAtta> sysatta=  sysAttaFeginService.getSysAttaByAttaOid(guid.getAttaOid());
                if(sysatta!=null && sysatta.getData()!=null){
                    speech.setSpeechUrl(sysatta.getData().getFastdfsNginxUrl());
                }
              }
              return speech;
          }
        }
        return null;
    }

    /**
     * 验证编码是否存在
     * @param guidSpeech
     * @return
     */
    public boolean checkCodeExist(GuidSpeech guidSpeech) {
        DbGuidSpeechExample example=new DbGuidSpeechExample();
        DbGuidSpeechExample.Criteria cria=example.createCriteria();
        if(guidSpeech!=null){
            if(StrUtil.isNotEmpty(guidSpeech.getOid())){
                cria.andOidNotEqualTo(guidSpeech.getOid());
            }
            if(StrUtil.isNotEmpty(guidSpeech.getGuideSpeechCode())){
                cria.andGuideSpeechCodeEqualTo(guidSpeech.getGuideSpeechCode());
            }
        }
        cria.andDeleteStatusEqualTo(String.valueOf(0));
        int count=guidSpeechMapper.countByExample(example);
        if(count>0){
            return true;
        }
        return false;
    }



    /**
     * 将base64字符解码保存文件
     *
     * @param base64Code
     * @throws Exception
     */
    private String decoderBase64File(String base64Code)
            throws Exception {
        String targetPath=getTargetPath();
        String catalogue=System.currentTimeMillis()+".mp3";
        File file = new File(targetPath);
        if(file.exists()==false){
            file.mkdirs();
        }
        byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        FileOutputStream out = new FileOutputStream(targetPath+"/"+catalogue);
        out.write(buffer);
        out.close();
        //调用文件上传接口上传生成文件
       String oid= this.uploadFileByPath(targetPath+"/"+catalogue);
       return oid;
    }

    /**
     * 获取一个临时地址使用
     * @return
     */
    private String getTargetPath(){
        if(CommonUtil.isWindows()){
            //给一个固定的地址临时使用
            return "D://"+ "templates/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
        }else{
            //替换成功报存的文件路径
           return "/soft/usr/local/zfsoft/zc/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
        }
    }

    /**
     * 根据路径将文件上传到服务器
     * @param path
     * @return
     */
    private String uploadFileByPath(String path){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            File pdfFile = new File(path);
            FileInputStream fileInputStream = new FileInputStream(pdfFile);
            MultipartFile multipartFile = new MockMultipartFile(pdfFile.getName(), pdfFile.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
           SysAtta atta= this.uploadFile(request,multipartFile);
           if(atta!=null){
               return atta.getAttaOid();
           }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public SysAtta uploadFile(HttpServletRequest request, MultipartFile file) throws Exception {
        //登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        //判断file数组不能为空并且长度大于0
        UploadUtil uploadUtil = new UploadUtil(request);
        String filePath = null;
        if (!file.isEmpty()) {
            try {
                // 上传并返回新文件名称
                if(file.getOriginalFilename()!=null){
                    file = new MockMultipartFile("File",file.getOriginalFilename(),"text/plain", file.getInputStream());
                }
                filePath = uploadUtil.uploadFile(file);
                SysAttaTemp sysAttaFile = uploadUtil.getSysAttaFile(filePath, loginUser.getUserOid());
                SysAtta sysAtta = new SysAtta();
                org.springframework.beans.BeanUtils.copyProperties(sysAttaFile,sysAtta);
                //保存附件信息
                ApiResultSet<SysAtta> atta = sysAttaFeginService.saveSysAtta(sysAtta);
                return  atta.getData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
