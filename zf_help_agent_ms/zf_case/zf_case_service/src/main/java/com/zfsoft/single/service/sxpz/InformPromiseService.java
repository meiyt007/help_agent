package com.zfsoft.single.service.sxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.single.data.sxpz.InformPromise;
import com.zfsoft.single.data.sxpz.vo.InformPromiseVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 告知承诺清单管理服务接口
 * dongxiuli
 **/
@RequestMapping("/informPromise")
public interface InformPromiseService {

    /**
     * 查询告知清单列表
     * @param informPromise
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryPage",method = {RequestMethod.GET})
    ApiResultSet<PageResult<InformPromise>> queryPage(InformPromise informPromise,
                                                      @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                      @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * 保存告知清单
     * @param informPromise
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveOrUpdate",method = {RequestMethod.POST})
    ApiResultSet saveOrUpdate(@RequestBody InformPromiseVo informPromise);

    /**
     * 删除告知清单
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/deleteInformPromise",method = {RequestMethod.GET})
    ApiResultSet deleteInformPromise(Long id);

    /**
     * 查询所有的添加告知承诺清单serviceOId
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/allInformServiceOids",method = {RequestMethod.GET})
    ApiResultSet allInformServiceOids();


    /**
     * 根据事项主键查询信息
     * @param serviceOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getInformByServiceOid",method = {RequestMethod.GET})
    ApiResultSet getInformByServiceOid(String serviceOid);
}
