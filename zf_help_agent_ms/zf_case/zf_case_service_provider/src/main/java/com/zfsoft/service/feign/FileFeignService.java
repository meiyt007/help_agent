package com.zfsoft.service.feign;

import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**调别的系统接口或其他应用模块接口
 * @ClassName SysDictFeignService
 * @Description feign
 * @Author wuxx
 * @Date 2020-10-20 14:23
 * @Version V1.0
 **/
@FeignClient(value = "platform-service-provider",contextId = "file")
@RequestMapping({"/security/atta"})
public interface FileFeignService {
    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResultSet uploadFile(HttpServletRequest request, @RequestPart("file") MultipartFile file, @RequestParam(value = "name", required = false) String name);

    @RequestMapping(value = {"/getAttaListByOids"},method = {RequestMethod.GET})
    ApiResultSet getAttaListByOids(@RequestParam("attaOids") String attaOids);

}

