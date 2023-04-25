package com.zfsoft.superwindow.service.pic;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/pic")
public interface PicService {
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/uploadImage"}, method = {RequestMethod.POST} , consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ApiResultSet uploadImage(@RequestPart MultipartFile file,
                             @RequestParam(value = "serviceId", required = false) String serviceId,
                             @RequestParam(value = "refinedMaterialId", required = false) String refinedMaterialId,
                             @RequestParam(value = "isEmpty", required = false) int isEmpty,
                             @RequestParam(value = "materialId", required = false) String materialId,
                             @RequestParam(value = "picType", required = false) String picType,
                             @RequestParam(value = "licenceOid", required = false) String licenceOid,
                             @RequestParam(value = "licenceName", required = false) String licenceName
    );

    @ProcessFeignCalledResult
    @RequestMapping(value = {"/deleteByPicRecordId"}, method = {RequestMethod.GET})
    ApiResultSet deleteById(@RequestParam(value = "picRecordId", required = false) Long picRecordId);

    @ProcessFeignCalledResult
    @RequestMapping(value = {"/updateByPicRecordId"}, method = {RequestMethod.GET})
    ApiResultSet updateByPicRecordId(@RequestParam(value = "picRecordId", required = false) Long picRecordId,
                                     @RequestParam(value = "isEmpty", required = false) int isEmpty);

}


