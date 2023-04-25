package com.zfsoft.single.service.yxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description: 规则验证接口
 * @author: liangss
 * @date: 2020-11-03 16:45:29
 */
@RequestMapping("/examinePointCardingByCombo")
public interface ExaminePointCardingByComboService {


    @RequestMapping( value = "/queryAhsSamplePicInfoByComboList",method = {RequestMethod.GET})
    ApiResultSet queryAhsSamplePicInfoByComboList(@RequestParam(value = "materiaOid", required = false) String materiaOid,
                                                  @RequestParam(value = "sampleInfoOid", required = false) String sampleInfoOid,
                                                  @RequestParam(value = "comboDirectoryOid", required = false) String comboDirectoryOid);



}
