package com.zfsoft.platform.utils.fileUtil;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName FileUploadConfig
 * @Description:
 * @Author wuxx
 * @Date 2020/12/1
 **/
@Configuration
public class FileUploadConfig {
    @Bean
    public FileUploadParam feignCalledAdvice(){
        return new FileUploadParam();
    }

    @Bean
    public FastDFSUtil calledFastDFSUtil(){
        return new FastDFSUtil();
    }
}
