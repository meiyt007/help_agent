package com.zfsoft.service.feign;

import com.zfsoft.microservice.form.service.FormMainService;
import org.springframework.cloud.openfeign.FeignClient;

/**调基础支撑系统接口或其他应用模块接口
 * @ClassName SysDictFeignService
 * @Description feign
 * @Author chenyq
 * @Date 2022-4-16
 * @Version V2.0
 **/
@FeignClient(value = "${zfsoft.feign.form}",contextId = "formMain")
public interface FormMainFeignService extends FormMainService  {
}
