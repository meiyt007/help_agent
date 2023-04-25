package com.zfsoft.service.controller.sxDirectory;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.manager.sxDirectory.SxDirectoryManager;
import com.zfsoft.service.sxDirectory.data.SxDirectory;
import com.zfsoft.service.sxDirectory.service.SxDirectoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @ClassName SxDirectoryController
 * @Description 目录清单控制类
 * @Author wangxl
 * @Date 2020-10-25
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SxDirectoryController implements SxDirectoryService {
    @Resource
    private SxDirectoryManager sxDirectoryManager;

    /**
     * @description:  初始化目录清单的信息
     * @param directoryOid 实体类业务主键
     * @author: wangxl
     * @Date: 2020/10/25
     **/
    @Override
    public ApiResultSet initSxDirectory(String directoryOid) {
        Map<String, Object> resultMap = new HashMap<>();
        if(null!=directoryOid){
            SxDirectory sxDirectory = sxDirectoryManager.getSxDirectoryByOid(directoryOid);
            resultMap.put("sxDirectory",sxDirectory);
        }
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(resultMap);
        return apiResultSet;
    }
    /**
     * @description:  获取目录清单的信息
     * @param directoryOid 实体类业务主键
     * @author: wangxl
     * @Date: 2020/10/25
     **/
    @Override
    public ApiResultSet<SxDirectory> getSxDirectoryByOid(String directoryOid) {
        SxDirectory sxDirectory = sxDirectoryManager.getSxDirectoryByOid(directoryOid);
        ApiResultSet<SxDirectory> apiResultSet = new ApiResultSet<SxDirectory>();
        apiResultSet.setData(sxDirectory);
        return apiResultSet;
    }

    /**
     * 根据业务主健获取对象json
     * @param directoryOid
     * @return
     */
    @Override
    public ApiResultSet<String> getSxDirectoryJsonByOid(String directoryOid) {
        String directJson = sxDirectoryManager.getSxDirectoryJsonByOid(directoryOid);
        return new ApiResultSet<>(directJson);
    }

    /**
     * 目录清单分页列表
     * @param directoryName
     * @param basicCode
     * @param serviceTypeOid
     * @param directoryStatus
     * @param levelDicts
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ApiResultSet<PageResult<SxDirectory>> querySxDirectoryWithPage(String directoryName, String basicCode
            , String serviceTypeOid, Integer directoryStatus, String levelDicts,Integer pageNum,Integer pageSize) {
        SxDirectory sxDirectory = new SxDirectory();
        if(StrUtil.isNotEmpty(directoryName)){
            sxDirectory.setDirectoryName(directoryName);
        }
        if(StrUtil.isNotEmpty(basicCode)) {
            sxDirectory.setBasicCode(basicCode);
        }
        if(StrUtil.isNotEmpty(serviceTypeOid)) {
            sxDirectory.setServiceTypeOid(serviceTypeOid);
        }
        if(null != directoryStatus){
            sxDirectory.setDirectoryStatus(directoryStatus.shortValue());
        }
        if(StrUtil.isNotEmpty(levelDicts)) {
            sxDirectory.setLevelDicts(levelDicts);
        }
        PageResult<SxDirectory> pageResult = sxDirectoryManager.querySxDirectoryWithPage(sxDirectory,pageNum,pageSize);
        ApiResultSet<PageResult<SxDirectory>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }
}
