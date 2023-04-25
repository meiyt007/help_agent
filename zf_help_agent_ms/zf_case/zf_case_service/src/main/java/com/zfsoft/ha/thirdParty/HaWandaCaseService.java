package com.zfsoft.ha.thirdParty;

import com.zfsoft.ha.data.TripartiteVo.PushDataVo;
import com.zfsoft.ha.data.TripartiteVo.getWdCserIdVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Description //提交办件信息接口
 * @Author: Wangyh
 * @Date: 2022/9/14 10:40
 */
@RequestMapping("/ha/wanda")
public interface HaWandaCaseService {


//    /**
//     * @description:  申请人确认（获取办件编码）
//     * @author: wangyh
//     * @Date: 2022/9/14
//     **/
//    @RequestMapping(value = "/applicantConfirm", method = {RequestMethod.POST})
//    ApiResultSet applicantConfirm(@RequestBody getWdCserIdVo getWdCserIdVo) throws Exception;

    /**
     * @description:  数据推送（增加材料,提交办件,材料添加附件并关联派生）
     * @author: wangyh
     * @Date: 2022/9/14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/pushData", method = {RequestMethod.POST})
    ApiResultSet pushData(@RequestBody PushDataVo pushDataVo) throws Exception;

//    /**
//     * @description:  数据推送（增加材料,提交办件,材料添加附件并关联派生）
//     * @author: wangyh
//     * @Date: 2022/9/14
//     **/
//    @RequestMapping(value = "/addStuffAttachment", method = {RequestMethod.POST})
//    ApiResultSet addStuffAttachment() throws Exception;
}
