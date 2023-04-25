package com.zfsoft.service.feign;

import com.zfsoft.platform.common.data.ApiResultSet;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: WangKe
 * @create: 2022-06-10
 * @description: 内蒙古自治区统一文件管理系统接口调用
 */
@FeignClient(value ="${zfsoft.feign.middle}", contextId = "middle-file", configuration = ZzqFastDfsFeignService.FeignMultipartSupportConfig.class)
public interface ZzqFastDfsFeignService {

    /**
     * 内蒙古自治区附件上传接口
     * @param file 文件
     * @return
     */
    @RequestMapping( value = "/web/fast/uploadFile",
            method = {RequestMethod.POST},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResultSet<String> uploadAtta(@RequestPart("file") MultipartFile file);

    /**
     * 内蒙古自治区附件上传接口
     * @param attaOid 附件oid
     * @param fileName 文件名称
     * @return
     */
    @RequestMapping(value = "/web/fast/downLoadFile", method = {RequestMethod.GET}, produces = "application/octet-stream")
    void downloadAtta(@RequestParam("attaOid") String attaOid, @RequestParam("fileName") String fileName);

    /**
     * 获取下载文件返回的输入流
     * @param attaOid 附件oid
     * @param fileName 文件名称
     * @return
     */
    @RequestMapping(value = "/web/fast/getFileIs", method = {RequestMethod.POST}, produces = "application/octet-stream")
    byte[] getFileIs(@RequestParam("attaOid") String attaOid, @RequestParam("fileName") String fileName);

    class FeignMultipartSupportConfig {
        @Bean
        public Encoder multipartFormEncoder() {
            return new SpringFormEncoder();
        }
    }
}
