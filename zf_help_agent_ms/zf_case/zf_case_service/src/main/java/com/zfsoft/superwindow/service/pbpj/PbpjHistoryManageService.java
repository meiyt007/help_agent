package com.zfsoft.superwindow.service.pbpj;

import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.yxpz.PbpjHistoryManage;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName PbpjManageService
 * @Description 平板历史记录服务定义接口
 * @Author liangxm
 * @Date 2020-10-24 11:33
 * @Version V1.0
 **/
@RequestMapping("/manage/pbpjHistory")
public interface PbpjHistoryManageService {


    /**
     * 保存历史记录
     * @param pbpjHistory
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
    int savePbpjHistoryManage(@RequestBody PbpjHistoryManage pbpjHistory);
}
