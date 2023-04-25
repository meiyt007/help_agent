package com.zfsoft.cases.feign;

import com.zfsoft.microservice.form.service.FormManagerService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 调用表单中的防范
 *
 * @ClassName FormManagerService
 * @Description feign
 * @Author gaolh
 * @Date 2022-8-13
 * @Version V2.0
 **/
@FeignClient(value = "${zfsoft.feign.form}", contextId = "formManager")
public interface FormManagerFeignService extends FormManagerService {
}
