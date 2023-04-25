package com.zfsoft.superwindow.controller.zxhd;


import com.alibaba.fastjson.JSON;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.zxhd.AdvisoryRegistration;
import com.zfsoft.superwindow.manager.zxhd.AdvisoryRegistrationManager;
import com.zfsoft.superwindow.service.zxhd.AdvisoryRegistrationService;
import com.zfsoft.superwindow.util.BASE64Utils;
import com.zfsoft.superwindow.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author: liangss
 * @create: 2020-10-23 13:22:56
 * @description: 咨询登记管理控制层
 */
@Slf4j
@RestController
@RequestMapping(value = "/advisoryRegistration")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdvisoryRegistrationController implements AdvisoryRegistrationService {

    private final AdvisoryRegistrationManager advisoryRegistrationManager;

    /**
     * 获取咨询登记列表
     * @param advisoryCode
     * @param name
     * @param advisoryStartDate
     * @param advisoryEndDate
     * @param districtStr
     * @param pageNum
     * @param pageSize
     * @return
     */
    @PostMapping(value = "/queryAdvisoryRegistrationWithPage")
    @Override
    public ApiResultSet<PageResult<AdvisoryRegistration>> queryAdvisoryRegistrationWithPage(String advisoryCode, String name, String advisoryStartDate, String advisoryEndDate,
                                                                                            String districtStr, Integer pageNum, Integer pageSize) {

        AdvisoryRegistration advisoryRegistration=new AdvisoryRegistration();
        advisoryRegistration.setAdvisoryCode(advisoryCode);
        advisoryRegistration.setName(name);
        PageResult<AdvisoryRegistration> pageResult =this.advisoryRegistrationManager.queryAdvisoryRegistrationWithPage(advisoryRegistration,advisoryStartDate,advisoryEndDate,districtStr,pageNum,pageSize);
        log.info("获取咨询登记列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        return new ApiResultSet<>(pageResult);
    }


    /**
     * 保存修改咨询登记信息
     * @param advisoryRegistration
     * @return
     */
    @Override
    @PostMapping(value = "/saveAdvisoryRegistration")
    public ApiResultSet<AdvisoryRegistration> saveAdvisoryRegistration(@RequestBody AdvisoryRegistration advisoryRegistration) {
        if(null!=advisoryRegistration){
            if(StringUtils.isNotEmpty(advisoryRegistration.getName())){
                advisoryRegistration.setName(BASE64Utils.base64Decode(advisoryRegistration.getName()));
            }
            if(StringUtils.isNotEmpty(advisoryRegistration.getTelePhone())){
                advisoryRegistration.setTelePhone(BASE64Utils.base64Decode(advisoryRegistration.getTelePhone()));
            }
            if(StringUtils.isNotEmpty(advisoryRegistration.getCardNumber())){
                advisoryRegistration.setCardNumber(BASE64Utils.base64Decode(advisoryRegistration.getCardNumber()));
            }
            if(StringUtils.isNotEmpty(advisoryRegistration.getAdvisoryContent())){
                advisoryRegistration.setAdvisoryContent(BASE64Utils.base64Decode(advisoryRegistration.getAdvisoryContent()));
            }
            if(StringUtils.isNotEmpty(advisoryRegistration.getResult())){
                advisoryRegistration.setResult(BASE64Utils.base64Decode(advisoryRegistration.getResult()));
            }
        }
        advisoryRegistrationManager.saveOrUpdate(advisoryRegistration);
        ApiResultSet<AdvisoryRegistration> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(advisoryRegistration);
        return apiResultSet;
    }


    /**
     * 根据主键查询咨询登记信息
     * @param id
     * @return
     */
    @Override
    @PostMapping(value = "/getAdvisoryRegistrationById")
    public ApiResultSet getAdvisoryRegistrationById(String id) {
        AdvisoryRegistration advisoryRegistration = this.advisoryRegistrationManager.getOne(id);
        log.info("详情获取成功：{}", JSON.toJSONString(advisoryRegistration));
        return new ApiResultSet(advisoryRegistration);
    }


    /**
     * 根据主键删除咨询登记信息
     * @param id
     * @return
     */
    @Override
    @PostMapping(value = "/deleteAdvisoryRegistrationById")
    public ApiResultSet<Integer> deleteAdvisoryRegistrationById(String id) {
        this.advisoryRegistrationManager.delAdvisoryRegistration(id);
        log.info("删除成功：{}", id);
        return null;
    }


}
