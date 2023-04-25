package com.zfsoft.superwindow.service.zxhd;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.zxhd.AdvisoryRegistration;


/**
 * @ClassName advisoryRegistrationService
 * @Description 咨询登记定义接口
 * @Author liangss
 * @Date 2020-10-23 10:33
 * @Version V1.0
 **/
public interface AdvisoryRegistrationService {


    /**
     * 根据条件分页查询咨询登记
     * @param advisoryCode
     * @param name
     * @param advisoryStartDate
     * @param advisoryEndDate
     * @param districtStr
     * @param pageNum
     * @param pageSize
     * @return
     */
    ApiResultSet<PageResult<AdvisoryRegistration>> queryAdvisoryRegistrationWithPage(String advisoryCode, String name,
                                                                                     String advisoryStartDate, String advisoryEndDate, String districtStr,
                                                                                     Integer pageNum, Integer pageSize);

    /**
     * 保存/更新咨询登记
     * @param advisoryRegistration
     * @return
     */
    ApiResultSet<AdvisoryRegistration> saveAdvisoryRegistration(AdvisoryRegistration advisoryRegistration);

    /**
     * 根据主键查询咨询登记信息
     * @param id
     * @return
     */
    ApiResultSet  getAdvisoryRegistrationById(String id);

    /**
     * 根据主键删除咨询登记信息
     * @param id
     * @return
     */
    ApiResultSet<Integer>  deleteAdvisoryRegistrationById(String id);


}
