package com.zfsoft.single.service.cpic;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.single.data.cpic.PicCompressRequest;
import com.zfsoft.single.data.cpic.QueryTDataSetZipRecSRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/pic")
public interface PicService {
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/uploadImage"}, method = {RequestMethod.POST} , consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ApiResultSet uploadImage(@RequestPart MultipartFile file,
                             @RequestParam(value = "serviceId", required = true) String serviceId,
                             @RequestParam(value = "refinedMaterialId", required = true) String refinedMaterialId,
                             @RequestParam(value = "isEmpty", required = true) Integer isEmpty,
                             @RequestParam(value = "materialId", required = true) String materialId,
                             @RequestParam(value = "picType", required = true) String picType,
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


    @ProcessFeignCalledResult
        @RequestMapping(value = {"/commpressPicToZip"}, method = {RequestMethod.POST})
    ApiResultSet commpressPicToZip(@RequestBody PicCompressRequest picCompressRequest);

    @ProcessFeignCalledResult
    @RequestMapping(value = {"/download"}, method = {RequestMethod.GET})
    ApiResultSet download(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "zipRecId", required = false) Long zipRecId);

    @ProcessFeignCalledResult
    @RequestMapping(value = {"/queryTDataSetMaterialImgRecsByRefinedMaterialId"}, method = {RequestMethod.GET})
    ApiResultSet queryTDataSetMaterialImgRecsByRefinedMaterialId(@RequestParam(value = "refinedMaterialId", required = false) String refinedMaterialId);

    @ProcessFeignCalledResult
    @RequestMapping(value = {"/queryTDataSetZipRecS"}, method = {RequestMethod.POST})
    ApiResultSet queryTDataSetZipRecS(@RequestBody QueryTDataSetZipRecSRequest queryTDataSetZipRecSRequest);

    @ProcessFeignCalledResult
    @RequestMapping(value = {"/deleteTDataSetZipRecById"}, method = {RequestMethod.GET})
    ApiResultSet deleteTDataSetZipRecById(@RequestParam(value = "id", required = false) Long id);

    @ProcessFeignCalledResult
    @RequestMapping(value = {"/updateIsPublish"}, method = {RequestMethod.GET})
    ApiResultSet updateIsPublish(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "isPublish", required = false) Integer isPublish);

    @ProcessFeignCalledResult
    @RequestMapping(value = {"/uploadImageTest"}, method = {RequestMethod.POST} , consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ApiResultSet uploadImageTest(@RequestPart MultipartFile file,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "attaOid", required = false) String attaOid);

    @ProcessFeignCalledResult
    @RequestMapping(value = {"/previewImage"}, method = {RequestMethod.GET})
    ApiResultSet previewImage(@RequestParam(value = "fastdfsNginxUrl") String fastdfsNginxUrl);

}
