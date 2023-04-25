package com.zfsoft.ha.manager.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.cases.manager.SysAttaManager;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.ha.data.HaBanner;
import com.zfsoft.ha.data.HaWorkGroup;
import com.zfsoft.ha.data.vo.HaFileKnowledgeBase;
import com.zfsoft.ha.manager.HaKnowledgeBaseService;
import com.zfsoft.ha.managers.HaKnowledgeServiceManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.service.sxSys.data.SxSysAtta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description //haFileKnowledgeBase控制层
 * @Author: Wangyh
 * @Date: 2022/12/2 16:43
 */
@RestController
@Slf4j
public class HaKnowledgeBaseController implements HaKnowledgeBaseService {
    /**
     * banner实现业务接口层
     */
    @Resource
    private HaKnowledgeServiceManager haKnowledgeServiceManager;
    /**
     * 上传附件信息实现类
     */
    @Resource
    private SysAttaManager sysAttaManager;

    @Override
    public ApiResultSet<JSONObject> uploadFile(HttpServletRequest request, String name, MultipartFile file) {
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        JSONObject jsonObject = null;
        if (!file.isEmpty()) {
            try {
                UploadUtil uploadUtil = new UploadUtil(request);
                String filePath = uploadUtil.uploadFile(file);
                if(filePath==null) {
                    return  new ApiResultSet<>(500,"文件上传失败",null);
                }
                String userOid ="";
                if(loginUser==null){
                    return  new ApiResultSet<>(500,"文件上传失败","没有获取到登录信息");
                }else{
                    userOid = String.valueOf(loginUser.getUserOid());
                }
                SysAttaTemp sysAttaFile = uploadUtil.getSysAttaFile(filePath,userOid);
                QlSysAtta sysAtta = new QlSysAtta();
                BeanUtils.copyProperties(sysAttaFile,sysAtta);
                QlSysAtta atta = sysAttaManager.saveSysAtta(sysAtta);
                jsonObject = JSONUtil.createObj().set("oid", atta.getAttaOid()).set("fastdfsNginxUrl", atta.getFastdfsNginxUrl()).set("fileName", atta.getOriginName());
            }catch (Exception e){
                log.error("文件上传失败：",e);
                return  new ApiResultSet<>(500,"文件上传失败",e.getMessage());
            }
        }
        return new ApiResultSet<>(jsonObject);
    }
    /**
     * @description:  查询文件知识库分页信息列表
     * @param fileName 文件名称
     * @param serviceOid 所属事项
     * @param isDelete 删除状态
     * @author: wangyh
     * @Date: 2022年7月26日14:04:40
     **/
    @Override
    public ApiResultSet<PageResult<HaFileKnowledgeBase>> queryKnowledgeServicePage(String fileName, String serviceOid, String isDelete, Integer pageNumber, Integer pageSize) {
        log.info("查询文件知识库分页信息列表，fileName:{},serviceOid:{},isDelete:{},pageNumber:{},pageSize:{}", fileName, serviceOid, isDelete, pageNumber, pageSize);
        HaFileKnowledgeBase haFileKnowledgeBase = new HaFileKnowledgeBase();
        ApiResultSet<PageResult<HaFileKnowledgeBase>> apiResultSet = null;
        haFileKnowledgeBase.setFileName(fileName);
        haFileKnowledgeBase.setServiceOid(serviceOid);
        if(StringUtil.isNotEmpty(isDelete)){
            haFileKnowledgeBase.setIsDelete(Short.valueOf(isDelete));
        }else{
            haFileKnowledgeBase.setIsDelete((short) 0);
        }
        try {
            PageResult<HaFileKnowledgeBase> pageResult = haKnowledgeServiceManager.queryKnowledgeServiceList(haFileKnowledgeBase, pageNumber, pageSize);
            log.debug("pageResult结果集,pageResult:{}", pageResult);
            apiResultSet = new ApiResultSet<>();
            apiResultSet.setData(pageResult);
        } catch (Exception e) {
            log.error("查询文件知识库分页信息列表方法错误：", e);
            return new ApiResultSet(500, "查询文件知识库分页信息列表方法错误", e.getMessage());
        }
        return apiResultSet;

    }

    /**
     * @description:  参数配置的新增或者修改
     * @param haFileKnowledgeBase 参数配置实体类
     * @author: wangyh
     * @Date: 2022年7月26日14:43:41
     **/
    @Override
    public ApiResultSet saveknowledgeorUpdata(HaFileKnowledgeBase haFileKnowledgeBase) throws Exception {
        log.info("参数配置的新增或者修改，haFileKnowledgeBase:{}", haFileKnowledgeBase);
        Map<String, Object> map = haKnowledgeServiceManager.saveOrUpdateFileKnowledgeBases(haFileKnowledgeBase);
        int index = (int) map.get("index");
        log.debug("index结果集，index:{}", index);
        if (index == 1) {
            return ApiResultSet.ok("新增成功");
        } else {
            return ApiResultSet.ok("修改成功");
        }
    }

    /**
     * @description:  删除haFileKnowledgeBase信息
     * @param id 主键
     * @author: wangyh
     * @Date: 2022年7月26日14:08:02
     **/
    @Override
    public ApiResultSet<HaFileKnowledgeBase> deleteKnowledgeId(Long id) {
        log.info("删除haFileKnowledgeBase信息，id:{}", id);
        try {
            Integer index = haKnowledgeServiceManager.deleteZskBaseId(id);
            if (index == 1) {
                return ApiResultSet.ok("用户删除成功");
            } else {
                return new ApiResultSet(500, "用户删除失败");
            }
        } catch (Exception e) {
            log.error("删除haFileKnowledgeBase信息方法错误：", e);
            return new ApiResultSet(500, "删除haFileKnowledgeBase信息方法错误", e.getMessage());
        }
    }

    /**
     * @description:  根据id查询haFileKnowledgeBase表信息
     * @param id
     * @return
     */
    @Override
    public ApiResultSet getZskBaseById(Long id) throws Exception {
        log.info("根据id查询banner表信息，id:{}", id);
        HaFileKnowledgeBase haFileKnowledgeBase = haKnowledgeServiceManager.selectByid(id);
        return ApiResultSet.ok("接口调用成功", haFileKnowledgeBase);
    }

    /**
     * @description:  根据id查询事项对象集合
     * @author: wangyh
     * @Date: 2022/8/4 11:23
     **/
    @Override
    public ApiResultSet<List<SxService>> queryServiceList(String id) {
        log.info("根据id查询事项对象集合， id:{}",id);
        List<SxService> sxServiceList = haKnowledgeServiceManager.queryServiceList();
        return ApiResultSet.ok("获取事项表成功",sxServiceList);
    }
    /**
     * excel导入
     * @param
     * @return
     */
    @Override
    public ApiResultSet leading(HttpServletRequest request, String fileName, MultipartFile file) throws Exception {
        log.info("excel导入");
        haKnowledgeServiceManager.excelLeading(fileName,file);
        return ApiResultSet.ok("成功");
    }
}
