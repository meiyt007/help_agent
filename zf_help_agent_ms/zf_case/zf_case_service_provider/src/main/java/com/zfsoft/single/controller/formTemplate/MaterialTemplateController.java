package com.zfsoft.single.controller.formTemplate;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.single.data.formTemplate.TermlateDataDto;
import com.zfsoft.single.manager.formTemplate.MaterialTemplateManager;
import com.zfsoft.single.service.formTemplate.MaterialTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
public class MaterialTemplateController implements MaterialTemplateService {

    @Resource
    private MaterialTemplateManager materialTemplateManager;

    /**
     * 获取盖章前模板
     *
     * @author : wangyg
     * @date : 2022/5/25
     */
    @Override
    public ApiResultSet<List<TermlateDataDto>> getTemplateList(String reportOid, String serviceOid) {
        ApiResultSet<List<TermlateDataDto>> apiResultSet = new ApiResultSet();
        List<TermlateDataDto> termlateDataDtos = materialTemplateManager.getTemplateList(reportOid, serviceOid);
        apiResultSet.setData(termlateDataDtos);
        return apiResultSet;
    }
}
