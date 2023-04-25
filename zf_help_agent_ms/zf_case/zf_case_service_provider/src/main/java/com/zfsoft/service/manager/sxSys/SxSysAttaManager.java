package com.zfsoft.service.manager.sxSys;

import com.zfsoft.service.dbaccess.dao.sxSys.DbSxSysAttaMapper;
import com.zfsoft.service.dbaccess.data.sxSys.DbSxSysAtta;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.sxSys.data.SxSysAtta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * @ClassName SxSysAttaServiceImpl
 * @Description: 事项附件接口实现类
 * @Author wangxl
 * @Date 2020/10/22
 **/
@Service
@Slf4j
//@CacheConfig(cacheNames = "sxpt:sysAtta")
public class SxSysAttaManager {

        @Resource
        private DbSxSysAttaMapper dbSxSysAttaMapper;

       // @Cacheable(key = "'getSxSysAttaByOid:'+#oid", unless = "#result == null")
        public SxSysAtta getSxSysAttaByOid(String oid) {
            DbSxSysAtta dbSxSysAtta = dbSxSysAttaMapper.getDbSxSysAttaByOid(oid);
            SxSysAtta sxSysAtta = new SxSysAtta();
            if(dbSxSysAtta == null){
//                throw new ResultInfoException("事项附件信息为空！");
            }else{
                BeanUtils.copyProperties(dbSxSysAtta,sxSysAtta);
            }
            return sxSysAtta;
        }
        public SxSysAtta saveAtta(SxSysAtta sysAtta){
            if(sysAtta!=null){
                if(!StringUtil.isNotEmpty(sysAtta.getOid())){
                    sysAtta.setOid(UUID.randomUUID().toString().replaceAll("-",""));
                }
                sysAtta.setUploadDate(new Date());
                sysAtta.setIsDelete(0);
                DbSxSysAtta atta=new DbSxSysAtta();
                BeanUtils.copyProperties(sysAtta,atta);
                dbSxSysAttaMapper.insert(atta);
                return sysAtta;
            }
            return null;
        }



    public SxSysAtta uploadBase64PdfToSX(String imageBase64) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        SxSysAtta sxSysAttaNew;
        try {
            // log.info("获取附件imageBase64成功：****", imageBase64,"***");
            byte[] bytes = Base64.getDecoder().decode(imageBase64);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            String originaFiliname="image"+System.currentTimeMillis()+".png";
            //MultipartFile file = new MockMultipartFile("image"+new Date().getTime()+".png", bytes);
            // MultipartFile file = new MockMultipartFile("File","image"+new Date().getTime()+".png","text/plain",inputStream);
            UploadUtil uploadUtil = new UploadUtil(request);
            String filePath = null;
            MultipartFile file = new MockMultipartFile("File","image"+System.currentTimeMillis()+".pdf","text/plain",inputStream);
            filePath = uploadUtil.uploadFile(file);
            SysAttaTemp sysAttaFile = uploadUtil.getSysAttaFile(filePath, "");


            SxSysAtta sxSysAtta=new SxSysAtta();
            BeanUtils.copyProperties(sysAttaFile,sxSysAtta);
            sxSysAtta.setFilePath(sysAttaFile.getFastdfsNginxUrl());
            sxSysAtta.setOid(sysAttaFile.getAttaOid());
            //保存附件信息
            // apiResultSet=sysAttaFeginService.saveSysAtta(atta);

            sxSysAttaNew=this.saveAtta(sxSysAtta);
            /* .*/

            // log.info("获取附件apiResultSet成功：****", apiResultSet,"***");
        }catch (Exception e){
            e.printStackTrace();
            throw new ResultInfoException("图片信息异常！");
        }
        return  sxSysAttaNew;

    }


}
