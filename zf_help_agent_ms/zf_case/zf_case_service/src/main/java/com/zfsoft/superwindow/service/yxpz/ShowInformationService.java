package com.zfsoft.superwindow.service.yxpz;

import com.zfsoft.microservice.platform.data.sys.SysAtta;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.yxpz.ShowInformation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName ShowInformationService
 * @Description 平板信息组件服务定义接口
 * @Author liangxm
 * @Date 2020-10-24 11:33
 * @Version V1.0
 **/
@RequestMapping("/manage/information")
public interface ShowInformationService {


    /**
     * @description:  平板信息的新增或者修改
     * @param ShowInformation 平板信息实体类
     * @author: liangxm
     * @Date: 2020/10/26 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
    ApiResultSet<ShowInformation> saveShowInformation(@RequestBody ShowInformation ShowInformation);


    /**
     * @description:  查询平板信息的信息列表

     * @author: liangxm
     * @Date: 2020/10/26 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
    ApiResultSet<PageResult<ShowInformation>> queryShowInformationWithPage(ShowInformation showInformation,
                                                                           @RequestParam(value = "pageNum") Integer pageNum,
                                                                           @RequestParam(value = "pageSize") Integer pageSize);

    /**
     * @description:  初始化平板信息的信息
     * @param id 平板信息实体类主键
     * @author: liangxm
     * @Date: 2020/10/26 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/init"},method = {RequestMethod.GET})
    ApiResultSet initShowInformation(String id);

    /**
     * 删除指定Id的平板信息信息
     * @param id 平板信息id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete/{id}",method = {RequestMethod.POST})
    Integer  deleteShowInformationById(String id);

    /**
     * 删除指定Id的平板信息信息
     * @param id 平板信息id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/able/{id}",method = {RequestMethod.POST})
    Integer  ableShowInformationById(String id);



    /**
     * @description:  获取平板信息的信息
     * @param id 平板信息实体类主键
     * @author: liangxm
     * @Date: 2020/10/26 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{id}",method = {RequestMethod.GET})
    ApiResultSet<ShowInformation>  getShowInformationById(String id);



    /**
     * @description:  获取平板信息附件信息
     * @param attaOid 附件主键
     * @author: chenjm
     * @Date: 2020/12/26 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOneFile/{attaOid}",method = {RequestMethod.GET})
    ApiResultSet<SysAtta>  getShowInformationAttaById(@RequestParam("attaOid") String attaOid);


    @ProcessFeignCalledResult
    @RequestMapping( value = "/getShowInformationByDistOid",method = {RequestMethod.POST})
    ApiResultSet<ShowInformation>  getShowInformationByDistOid(@RequestParam("districtOid") String districtOid);

}
