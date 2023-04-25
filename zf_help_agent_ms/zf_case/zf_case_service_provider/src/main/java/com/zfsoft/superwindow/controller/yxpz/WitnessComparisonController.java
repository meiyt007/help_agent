package com.zfsoft.superwindow.controller.yxpz;

import com.github.pagehelper.util.StringUtil;
import com.zfsoft.microservice.platform.data.sys.SysAtta;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.superwindow.data.yxpz.WitnessComparison;
import com.zfsoft.superwindow.feign.settings.SysAttaFeginService;
import com.zfsoft.superwindow.manager.yxpz.WitnessComparisonManager;
import com.zfsoft.superwindow.service.yxpz.WitnessComparisonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class WitnessComparisonController implements WitnessComparisonService {

    @Resource
    private WitnessComparisonManager witnessComparisonManager;

    @Resource
    private SysAttaFeginService sysAttaFeginService;

    @Override
    public ApiResultSet saveOrUpdate(WitnessComparison witnessComparison) {
       WitnessComparison witness= witnessComparisonManager.saveOrUpdate(witnessComparison);
        return new ApiResultSet(witness);
    }

    @Override
    public ApiResultSet<List<SysAtta>> uploadCompareFile(HttpServletRequest request, MultipartFile[] files) throws Exception {
        List<SysAtta> attaList=new ArrayList<SysAtta>();
        //登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        //判断file数组不能为空并且长度大于0
        if(files != null && files.length > 0) {
            //循环获取file数组中得文件
            MultipartFile file = null;
            UploadUtil uploadUtil = new UploadUtil(request);
            String filePath = null;
            //循环获取file数组中得文件
            for (int i = 0; i < files.length; i++) {
                file = files[i];
                //保存文件
                if (!file.isEmpty()) {
                    try {
                        // 上传并返回新文件名称
                        if(StringUtil.isNotEmpty(file.getOriginalFilename())){
                            file = new MockMultipartFile("File",file.getOriginalFilename(),"text/plain", file.getInputStream());
                        }
                        filePath = uploadUtil.uploadFile(file);
                        SysAttaTemp sysAttaFile = uploadUtil.getSysAttaFile(filePath, loginUser.getUserOid());
                        SysAtta sysAtta = new SysAtta();
                        BeanUtils.copyProperties(sysAttaFile,sysAtta);
                        //保存附件信息
                        ApiResultSet<SysAtta> atta = sysAttaFeginService.saveSysAtta(sysAtta);
                        attaList.add(atta.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        ApiResultSet<List<SysAtta>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(attaList);
        return apiResultSet;
    }

    @Override
    public ApiResultSet queryWitnessComparisonByCaseOid(String caseOid) {
        WitnessComparison wc= witnessComparisonManager.queryWitnessComparisonByCaseOid(caseOid);
        return new ApiResultSet(wc);
    }

    @Override
    public ApiResultSet queryBase64ImgWitnessComparisonByCaseOid(String caseOid) {
        Map<String, String> base64Img = witnessComparisonManager.queryBase64ImgWitnessComparisonByCaseOid(caseOid);
        return new ApiResultSet(base64Img);
    }
}
