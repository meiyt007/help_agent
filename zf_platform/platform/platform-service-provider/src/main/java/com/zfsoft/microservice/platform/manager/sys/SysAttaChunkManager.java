package com.zfsoft.microservice.platform.manager.sys;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * @Description: 附件分段上传历史表接口实现类
 * @Author wangyg
 * @Date 2022/5/26
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "sys:attaChunk")
public class SysAttaChunkManager {
}
