package com.zfsoft.superwindow.controller.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.data.clzs.FaModelTemplateBlock;
import com.zfsoft.superwindow.manager.clzs.FaModelTemplateBlockManager;
import com.zfsoft.superwindow.service.clzs.FaModelTemplateBlockService;
import com.zfsoft.superwindow.util.fa.PictureCutRuleNew;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: wangwg
 * @create: 2021-01-11
 * @description: 识别模板区块控制层
 */
@Slf4j
@RestController
public class FaModelTemplateBlockController implements FaModelTemplateBlockService {

    @Resource
    private FaModelTemplateBlockManager faModelTemplateBlockManager;

    @Override
    public ApiResultSet queryCataMetadataBlockByTemplateOid(String templateOid) throws Exception {
        List<PictureCutRuleNew> pictureCutRuleNews = faModelTemplateBlockManager.queryCataMetadataBlockByTemplateOid(templateOid);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pictureCutRuleNews);
        return apiResultSet;
    }

    @Override
    public ApiResultSet getFeatureFaModelTemplateBlockByTemplateOidNew(String faModelTemplateOid) throws Exception {
        FaModelTemplateBlock templateOidNew = faModelTemplateBlockManager.getFeatureFaModelTemplateBlockByTemplateOidNew(faModelTemplateOid);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(templateOidNew);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<FaModelTemplateBlock>> queryFaModelTemplateBlockList(FaModelTemplateBlock faModelTemplateBlock) {
        List<FaModelTemplateBlock> list=faModelTemplateBlockManager.queryFaModelTemplateBlockList(faModelTemplateBlock);
        return new ApiResultSet(list);
    }
}
