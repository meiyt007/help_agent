package com.zfsoft.superwindow.controller.clzs;


import com.alibaba.fastjson.JSON;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.FaModelTemplatePic;
import com.zfsoft.superwindow.manager.clzs.FaModelTemplatePicManager;
import com.zfsoft.superwindow.service.clzs.FaModelTemplatePicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @author: liangss
 * @create: 2020-11-07 17:01:29
 * @description: 模板底图控制层
 */
@Slf4j
@RestController
@RequestMapping(value = "/faModelTemplatePic")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaModelTemplatePicController implements FaModelTemplatePicService {
    private final FaModelTemplatePicManager faModelTemplatePicManager;

    @PostMapping(value = "/queryFaModelTemplatePic")
    public ApiResultSet<PageResult<FaModelTemplatePic>> queryFaModelTemplatePic(FaModelTemplatePic faModelTemplatePic, Integer pageNum, Integer pageSize) {
        PageResult<FaModelTemplatePic> pageResult=this.faModelTemplatePicManager.queryFaModelTemplatePicWithPage(faModelTemplatePic,pageNum,pageSize);
        log.info("获取模板底图列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    /**
     * 获取列表
     * @param faModelTemplatePic
     * @return
     */
    @PostMapping(value = "/getFaModelTemplatePicList")
    public ApiResultSet getFaModelTemplatePicList(FaModelTemplatePic faModelTemplatePic , HttpServletResponse response) {
        List<FaModelTemplatePic> dbFaModelTemplatePicList =this.faModelTemplatePicManager.getFaModelTemplatePicList(faModelTemplatePic);
        log.info("获取模板底图列表调用成功结果为：{}", JSON.toJSONString(dbFaModelTemplatePicList));
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(dbFaModelTemplatePicList);
        return apiResultSet;
    }

    @PostMapping(value = "/saveOrUpdate")
    public ApiResultSet saveOrUpdate(FaModelTemplatePic faModelTemplatePic, HttpServletResponse response) throws Exception {
        this.faModelTemplatePicManager.saveOrUpdate(faModelTemplatePic);
        log.info("保存/更新材料目录信息成功：{}", JSON.toJSONString(faModelTemplatePic));
        return new ApiResultSet(faModelTemplatePic);
    }

}
