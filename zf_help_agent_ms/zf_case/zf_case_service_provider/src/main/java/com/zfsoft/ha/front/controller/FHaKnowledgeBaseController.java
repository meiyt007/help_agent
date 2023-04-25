package com.zfsoft.ha.front.controller;

import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.ha.data.vo.HaFileKnowledgeBase;
import com.zfsoft.ha.front.FHaKnowledgeBaseService;
import com.zfsoft.ha.managers.HaKnowledgeServiceManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description //haFileKnowledgeBase控制层
 * @Author: Wangyh
 * @Date: 2022/12/2 16:43
 */
@RestController
@Slf4j
public class FHaKnowledgeBaseController implements FHaKnowledgeBaseService {
    /**
     * banner实现业务接口层
     */
    @Resource
    private HaKnowledgeServiceManager haKnowledgeServiceManager;
    /**
     * @description:  查询文件知识库分页信息集合
     * @author: wangyh
     * @Date: 2022年7月26日14:04:40
     **/
    @Override
    public ApiResultSet<List<HaFileKnowledgeBase>> queryKnowledgeServiceList() {
        log.info("进入查询文件知识库分页信息集合方法");

        ApiResultSet<List<HaFileKnowledgeBase>> apiResultSet = null;
        try {
            List<HaFileKnowledgeBase> listResult = haKnowledgeServiceManager.queryKnowledgeServiceList();
            log.debug("listResult结果集,listResult:{}", listResult);
            apiResultSet = new ApiResultSet<>();
            apiResultSet.setData(listResult);
        } catch (Exception e) {
            log.error("查询文件知识库分页信息列表方法错误：", e);
            return new ApiResultSet(500, "查询文件知识库分页信息列表方法错误", e.getMessage());
        }
        return apiResultSet;
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
}
