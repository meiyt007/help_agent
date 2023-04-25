package com.zfsoft.superwindow.controller.front;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.settings.data.SysConfig;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.superwindow.data.front.VirtualBusinessRecord;
import com.zfsoft.superwindow.dbaccess.dao.DbVirtualBusinessRecordMapper;
import com.zfsoft.superwindow.dbaccess.data.DbVirtualBusinessRecord;
import com.zfsoft.superwindow.dbaccess.data.DbVirtualBusinessRecordManual;
import com.zfsoft.superwindow.feign.settings.SysConfigFeignService;
import com.zfsoft.superwindow.manager.front.VirtualBusinessRecordManager;
import com.zfsoft.superwindow.service.front.VirtualBusinessRecordService;
import com.zfsoft.superwindow.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class VirtualBusinessRecordController implements VirtualBusinessRecordService {

    @Resource
    private SysConfigFeignService sysConfigFeignService;

    @Resource
    private VirtualBusinessRecordManager virtualBusinessRecordManager;

    @Resource
    private DbVirtualBusinessRecordMapper dbVirtualBusinessRecordMapper;

    @Override
    public List getSatisfactionList() {
        List list = new ArrayList();
        ApiResultSet<List<SysConfig>> config = sysConfigFeignService.querySysConfigListByParentCode("MYCS");
        if (null != config.getData()) {
            List<SysConfig> SysConfigList = config.getData();
            if (SysConfigList.size() > 0) {
                for (SysConfig sysConfig : SysConfigList) {
                    Map<String, String> modelMap = new HashMap<>();
                    modelMap.put("id", sysConfig.getValue());
                    modelMap.put("label", sysConfig.getName());
                    list.add(modelMap);
                }
            }
        }
        return list;
    }

    @Override
    public ApiResultSet queryPageList(VirtualBusinessRecord virtualBusinessRecord, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<DbVirtualBusinessRecordManual> dbVirtualBusinessRecordManualList = this.virtualBusinessRecordManager.queryList(virtualBusinessRecord);
        PageResult<DbVirtualBusinessRecordManual> pageResult = new PageResult<>(
                ((Page) dbVirtualBusinessRecordManualList).getPageNum(),
                ((Page) dbVirtualBusinessRecordManualList).getPageSize(),
                ((Page) dbVirtualBusinessRecordManualList).getTotal()
        );
        pageResult.setData(dbVirtualBusinessRecordManualList);
        log.info("获取好差评列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        return new ApiResultSet(pageResult);
    }

    @Override
    public ApiResultSet saveVirtualBusinessRecord(VirtualBusinessRecord virtualBusinessRecord) {
        String oid  = this.virtualBusinessRecordManager.saveVirtualBusinessRecord(virtualBusinessRecord);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(oid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet getVirtualBusinessRecord(String virtualBusinessNum) {
        DbVirtualBusinessRecord dbVirtualBusinessRecord  = this.virtualBusinessRecordManager.getVirtualBusinessRecord(virtualBusinessNum);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(dbVirtualBusinessRecord);
        return apiResultSet;
    }

    @Override
    public ApiResultSet uploadAudio(MultipartFile file, String virtualBusinessNum) {
        log.info("进入上传录音文件的接口");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.info("上传录音文件开始");
        try {
            file = new MockMultipartFile("File", file.getOriginalFilename(), "multipart/form-data", file.getInputStream());
            UploadUtil uploadUtil = new UploadUtil(request);
            String filePath = uploadUtil.uploadFile(file);
            SysAttaTemp tempAtta = uploadUtil.getSysAttaFile(filePath, "");
            log.info("上传录音文件中间");
            if (virtualBusinessNum != null) {
                DbVirtualBusinessRecord dbVirtualBusinessRecord = dbVirtualBusinessRecordMapper.queryByOid(virtualBusinessNum);
                if (StringUtils.isNotEmpty(tempAtta.getFastdfsNginxUrl())) {
                    dbVirtualBusinessRecord.setAudioAddress(tempAtta.getFastdfsNginxUrl());
                }
                dbVirtualBusinessRecordMapper.update(dbVirtualBusinessRecord);
            }
            log.info("上传录音文件的路径为：{}", tempAtta.getFastdfsNginxUrl());
            return new ApiResultSet<>(tempAtta.getFastdfsNginxUrl());
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ApiResultSet();
    }
}
