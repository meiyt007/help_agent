package com.zfsoft.superwindow.controller.pic;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.superwindow.dbaccess.data.TDataSetMaterialImgRec;
import com.zfsoft.superwindow.manager.pic.PicManager;
import com.zfsoft.superwindow.service.pic.PicService;
import com.zfsoft.superwindow.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/7/21 11:49
 */
@RestController
@Slf4j
public class PicController implements PicService {

    @Resource
    private PicManager picManager;

    @Override
    public ApiResultSet uploadImage(MultipartFile file, String serviceId, String refinedMaterialId, int isEmpty, String materialId,String picType,String licenceOid,
                                    String licenceName) {
        //MultipartFile file =null;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        String userOid  =  "c5ccbbaec7c14c319b3ce030d392cbad";
        if(currentLoginUser!=null){
            userOid  =  currentLoginUser.getUserOid(); //测试使用
        }
        try {
            if (StrUtil.isNotEmpty(picType)) {
                String formatName = "png";
                if (StrUtil.isNotEmpty(picType)) {
                    formatName = picType.substring(picType.lastIndexOf(".") + 1, picType.length());
                }
                if (!BaseStaticParameter.ATTA_IMAGE_EXT_SET.contains(formatName.toLowerCase())) {
                    throw new ResultInfoException("请上传gif、jpg、jpeg、png或bmp格式的文件！");
                }
                file = new MockMultipartFile("File", picType, "text/plain", file.getInputStream());
            }
            UploadUtil uploadUtil = new UploadUtil(request);
            String filePath = uploadUtil.uploadFile(file);
            SysAttaTemp tempAtta = uploadUtil.getSysAttaFile(filePath, userOid);
            //ComboCaseAtta atta = new ComboCaseAtta();
            //BeanUtils.copyProperties(tempAtta,atta);
            TDataSetMaterialImgRec tDataSetMaterialImgRec = new TDataSetMaterialImgRec();
            tDataSetMaterialImgRec.setAttachmentAddress(tempAtta.getFastdfsNginxUrl());
            tDataSetMaterialImgRec.setCreateBy(userOid);
            tDataSetMaterialImgRec.setIsEmpty(isEmpty);
            tDataSetMaterialImgRec.setLoadTime(new Date());
            tDataSetMaterialImgRec.setLicenceOid(StringUtils.isEmpty(licenceOid)?"null":licenceOid);
            tDataSetMaterialImgRec.setMaterialId(materialId);
            tDataSetMaterialImgRec.setServiceId(serviceId);
            tDataSetMaterialImgRec.setLicenceName(StringUtils.isEmpty(licenceName)?"null":licenceName);
            tDataSetMaterialImgRec.setRefinedMaterialId(refinedMaterialId);
            picManager.savePicRecord(tDataSetMaterialImgRec);
            Long  picRecordId  =  tDataSetMaterialImgRec.getId();
            //保存附件信息
            //comboCaseAttaManager.saveOrUpdate(atta);
            //JSONObject jsonObject = JSONUtil.createObj().set("oid", atta.getOid()).set("url", atta.getFastdfsNginxUrl()).set("name", atta.getOriginName()).set("size",atta.getFileSize());
            JSONObject jsonObject =  new JSONObject();
            jsonObject.putOpt("picRecordId",picRecordId);
            jsonObject.putOpt("originName",tempAtta.getOriginName());
            jsonObject.putOpt("size",tempAtta.getFileSize());
            return new ApiResultSet<>(jsonObject.toString());
        }catch (Exception e){
            throw new ResultInfoException(e.getMessage());
        }
    }

    @Override
    public ApiResultSet deleteById(Long picRecordId) {
        picManager.deletePicRecord(picRecordId);
        return new ApiResultSet<>("删除成功");
    }

    @Override
    public ApiResultSet updateByPicRecordId(Long picRecordId, int isEmpty) {
        picManager.updatePicRecord(picRecordId,isEmpty);
        return new ApiResultSet<>("修改成功");
    }


}
