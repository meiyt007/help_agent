package com.zfsoft.single.controller.ywbl;

import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.cases.service.SysAttaService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.single.data.ywbl.vo.SignImageVo;
import com.zfsoft.single.service.ywbl.CaseSignService;
import com.zfsoft.single.util.BeanUtils;
import com.zfsoft.single.util.CommonUtil;
import com.zfsoft.single.util.UUIDUtil;
import com.zfsoft.superwindow.data.wgpj.CaseSignRecord;
import com.zfsoft.superwindow.service.sign.CaseSignRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * @（#）: CaseSignController
 * @description: 办件签名记录
 * @author: wangwg
 * @date: 2021/8/17
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@Slf4j
@RestController
public class CaseSignController implements CaseSignService {

    @Resource
    private SysAttaService qlSysAttaFeignService;

    @Resource
    private CaseSignRecordService caseSignRecordFeginService;

    @Override
    public ApiResultSet updateSignImg(SignImageVo signImageVo) {
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        File base64ToFile= null;
        String url = "";
        if(signImageVo.getBase64Img() !=null){
             base64ToFile = base64ToFile(signImageVo.getBase64Img());
        }

        if (base64ToFile !=null) {
            UploadUtil uploadUtil = new UploadUtil(request);
            try {
                FileInputStream fileInputStream = new FileInputStream(base64ToFile);
                MultipartFile multipartFile = new MockMultipartFile(base64ToFile.getName(), base64ToFile.getName(),
                        ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
                MultipartFile  file = new MockMultipartFile("File",multipartFile.getOriginalFilename(),"text/plain", multipartFile.getInputStream());
                String filePath = uploadUtil.uploadFile(file);
                SysAttaTemp sysAttaFile = uploadUtil.getSysAttaFile(filePath, loginUser.getUserOid());
                QlSysAtta atta = new QlSysAtta();
                BeanUtils.copyProperties(sysAttaFile,atta);
                qlSysAttaFeignService.saveSysAtta(atta);
                CaseSignRecord caseSignRecord = new CaseSignRecord();
                caseSignRecord.setCaseOid(signImageVo.getCaseOid());
                caseSignRecord.setApplyCarno(signImageVo.getCardNo());
                caseSignRecord.setSignUrl(atta.getFastdfsNginxUrl());
                url = atta.getFastdfsNginxUrl();
                caseSignRecordFeginService.saveSignRecord(caseSignRecord);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ApiResultSet(url);
    }

    public static File base64ToFile(String base64)  {
        if(base64==null||"".equals(base64)) {
            return null;
        }
        //去除base64中无用的部分
        base64 =  base64.replace("data:image/png;base64,", "");
        //byte[] buff= Base64.decode(base64);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buff= new byte[0];
        try {
            buff = decoder.decodeBuffer(base64);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < buff.length; ++i) {
            if (buff[i] < 0) {// 调整异常数据
                buff[i] += 256;
            }
        }
        File file=null;
        FileOutputStream fout=null;
        try {
            String signImage ="";
            if(CommonUtil.isWindows()){
                signImage = "c://signImage/";
            }else{
                signImage = "/signImage/";
            }
            File mikFile = new File(signImage);
            if (!mikFile.exists()){
                mikFile.mkdirs();
            }
            file = new File(signImage+ UUIDUtil.randomUUID()+".png");
            fout=new FileOutputStream(file);
            fout.write(buff);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fout!=null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public static void main(String[] args) throws IOException {
        //String test = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAfQAAAEsCAYAAAA1u0HIAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAOxAAADsQBlSsOGwAACalJREFUeJzt3duOo0YYhVET5f1fmVyMUDyWsTEUddi1ljQXmUM3LQgff4HtZV3XBwAwtn9abwAAcJ2gA0AAQQeAAIIOAAEEHQACCDoABBB0AAgg6AAQQNABIICgA0AAQQeAAIIOAAEEHQACCDoABBB0AAgg6AAQQNABIICgA0AAQQeAAIIOAAEEHQACCDoABBB0AAgg6AAQQNABIICgA0AAQQeAAIIOAAEEHQACCDoABBB0AAgg6AAQQNABIICgA0AAQQeAAIIOAAEEHQACCDoABBB0AAgg6AAQQNABIICgA0AAQQeAAIIOAAEEHQACCDoABBB0AAgg6AAQQNABIICgA0AAQQeAAIIOAAEEHQACCDoABBB0AAgg6Jy2LMu6/Wq9LZuetgWgpmVdnf/43adwruu61NyWzfM2tdoGgFZM6BTX29QOMANB55QjE3DNsLuAAGZnyZ1ijkT1rqVwy+3A7ASd4mqH/fX7CTowI0vuFLeu6/Itqu6zA5RlQqeKb/G+MlVbbgcwoVPJt6ndxA5wjQmdZkq8lt39c4A/BJ3mroTdcjvAH5bcae7bUvyZPwOYjaDThU/32IUb4DtL7nRpL+LP0bfcDvA/Ezpd+jatm9oB/mZCp3st31IWYBQmdLr3LdZiDiDoDEK0AT4TdIbhKXiAfYJOBFEHZifoDONbtEUdmJmgM6x3S/CiDsxK0BnC3oewiDrAH4LO8PaiLuzATASd7h35iFQvawNmJ+jEMKkDM/PWr3TtyHR+5N/98m8BRmRCJ5KH5YDZCDrdOjudf/r7og6kEnQACOAeOl26Op3f/fUAemNCpzs1lsUtvQNpBJ3ulZ7Ov/0+wIgEna7UXhoXdSCFoDM9UQcSCDrdqDWdezkbkEjQ6ULtoIo6kEbQ6VKNl5WJOpBE0Gmu5WvEvR4dSCHoNNXDUvvr75nSgREJOl3pZWIWdWA0gk4zNZbaj4bZ/XRgdIJOEz3GUtSBkQk61b2LZC9L7aIOjErQqarnmG962x7GsSzLuv1qvS3MR9CpZoSYbzz5zq+ej5Fej2uyCTrNjHbSE3X2iDk9EHSqGDGG7qfzzevyupjTkqBzu5GW2l+Nsp3U1/IdDuEdQedWI8d84346r16n8tGOaTIJOrdJiPkeUZ/XqEvsnr7PJ+jcIi3m7qcz8v3ybbtH2mZ+J+gUlxbzTcLPQBkjHQsuPOch6BSVGvON++nzSZjMH4+xtptzBJ1i0mO+R9Rzjfwku+NyPoJOETPF3P30OXyK+Whv8Zr6/yJ/E3Qumynmm/Sfb3a/TOY9Rt1S+5yWde3uWGQQeyeynk4gd5/YRl6S5b2j+7TXC1kxn5cJnVNGiHkLPU5rHPfLBdq7N5Rpvf/FfG6CTjEznkBm/JlTnV1t6SXqYo6gc8rzCWP2t77s5YTOeVdvnbQ+BhxzPB7uoROs9v1t99PHVHK/tTgGHHdsTOhwE1NT/0p/yErtmIo5zwSdWLVPbk6mY7nrnvPr69VLfd1XYs4rQYeCWt9L5Xe1b8XcQcx5PAQdbifqfbrzYc6aFwlizkbQoTAnWB6P+5bexZw9gs40ak7Klt55VeIYcBzxiaDDTUSdkhO0h+D4RtABKjl7USfmHCHocCNTOlfjK+YcJehwM1Hn2ZX9L+Z8IugANzsbYk+08wtBhwpM6Tw7sv8dI/xK0KESUZ/bLxO2++acIegADexd0Ik5Zwk6VGRKn9vVz1mHTwQdGhN1No4FrhB0qMzUNbe993i31M5Vgg4NWHrnmZhTgqATTTjpnZhTiqBDIy425iXa3EHQAToh9Fwh6NCQKX1er/tezLlK0AEacPFGaYJOvN6n4N63j/Le7WP7nasEHS5wEgZ6IehwUYmom9Ln8Wnf2u9cIehMqfSJ04mYM9Z1XTwMRymCzhTuOmnuvY0nvONz0LmToENBV07Glt2zfXpHOFM6JQg607grmEIM9EDQoQBR55Nf36/d8cMZgs5U7gxvia9t6TXf3j6277lK0OFGVy8YTGrjO7sP7Xt+JehMp+aUXvrrM7ZvU7gpnSsEHQoTdTb2OzUJOlO6+yVDV6JuSst0dL96bwPO+rf1BkArd4dzXdfl9YS8/bdo5xspxr8+hU+fTOhwo70T40gne667EsgWx8qyLOv2q/b35jxBh5t9iroTZqar+7X2hPzp+zlOx7Gsq/0EtXw6MW4nVcuf4yuxD1sfB0eOVfoi6FDZr9OOk+dYSob4+Wu1PA72jlnHZl88FAeV7U3in/4utLR3zPZywcEfJnRozPSTo/Qyeetl90/eHbc9bd+MTOjQ2LvXHTsx8ni8f+ljLxyj/TGhAxRw1zTd85ROX7xsDaBjAs5Rgg5Q2J0R7nUJnvYEHeCiuyNrSucIQQe4wD1ueiHoAAPwKWx8I+gAJ5nO6YmgAxTQ03utMydBBzihRVCtAPCJoANc1Cq0pnSeCTrAj1qG1JTOHkEH+IEH4eiVoAMc1EvMvYSNdwQd4ADhpHeCDnBCT0vtLjZ4PAQd4KNlWdZeltp72wb6IugAO95NvkJKrwQd4I0RYu7hOJ4JOsABvcUcXgk6wBvPAR8l5qb0uS3rav8DjOw55KNcfFCeCR0AAgg6wOA8HMfjIegAEEHQAcKY0uck6AABPAyHoANAAEEHCOHhuLkJOgAEEHQACCDoAEEsu89L0AEggKADQABBBwhj2X1Ogg4AAQQdAAIIOkAgy+7zEXQACCDoABBA0AFCWXafi6ADQABBB4AAgg4QzLL7PAQdAAIIOgAEEHSAcM/L7uQSdICJuI+eS9ABIICgA0zAsns+QQeYiLDnWtbV7RQAGJ0JHQACCDoABBB0AAgg6AAQQNABIICgA0AAQQeAAIIOAAEEHQACCDoABBB0AAgg6AAQQNABIICgA0AAQQeAAIIOAAEEHQACCDoABBB0AAgg6AAQQNABIICgA0AAQQeAAIIOAAEEHQACCDoABBB0AAgg6AAQQNABIICgA0AAQQeAAIIOAAEEHQACCDoABBB0AAgg6AAQQNABIICgA0AAQQeAAIIOAAEEHQACCDoABBB0AAgg6AAQQNABIICgA0AAQQeAAIIOAAEEHQACCDoABBB0AAgg6AAQQNABIICgA0AAQQeAAIIOAAEEHQACCDoABBB0AAgg6AAQQNABIICgA0AAQQeAAIIOAAEEHQACCDoABBB0AAgg6AAQQNABIICgA0AAQQeAAIIOAAEEHQACCDoABBB0AAgg6AAQQNABIICgA0AAQQeAAIIOAAEEHQACCDoABBB0AAgg6AAQQNABIICgA0AAQQeAAP8BL46Pbzis4poAAAAASUVORK5CYII=";
        String test =  imageToBase64Str("E:\\pic\\2.jpg");
        base64ToFile(test);
    }

    public static String imageToBase64Str(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

}
