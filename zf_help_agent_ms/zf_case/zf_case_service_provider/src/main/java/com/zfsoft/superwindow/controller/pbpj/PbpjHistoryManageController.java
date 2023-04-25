package com.zfsoft.superwindow.controller.pbpj;

import com.zfsoft.superwindow.data.yxpz.PbpjHistoryManage;
import com.zfsoft.superwindow.manager.pbpj.PbpjHistoryManageManager;
import com.zfsoft.superwindow.service.pbpj.PbpjHistoryManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName PbpjManageController
 * @Description 参数配置管理的实现类
 * @Author liangxm
 * @Date 2020-09-12 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class PbpjHistoryManageController implements PbpjHistoryManageService {
    @Resource
    private PbpjHistoryManageManager pbpjHistoryManageManager;

    @Override
    public int savePbpjHistoryManage(PbpjHistoryManage pbpjHistory) {
        int index=pbpjHistoryManageManager.savePbpjHistoryManage(pbpjHistory);
        return index;
    }
}
