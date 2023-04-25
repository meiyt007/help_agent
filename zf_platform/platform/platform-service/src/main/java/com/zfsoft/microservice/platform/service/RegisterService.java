package com.zfsoft.microservice.platform.service;


import com.zfsoft.microservice.platform.data.vo.RegisterLicense;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


/**
 * @description:  注册文件逻辑层接口
 * @author: wuxx
 * @Date: 2020/11/3 10:42
 **/
@RequestMapping("/security/register")
public interface RegisterService {

	/**
	 *  验证上传注册文件是否正确 
	 * @param registerLicense 注册文件
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/checkRegisterLicense")
	@ResponseBody
	@ProcessFeignCalledResult
	ApiResultSet checkRegisterLicense(@RequestBody RegisterLicense registerLicense);
    /**
     *  生成机器特征码
     * @return
     * @throws Exception
     */
	@RequestMapping(value = "/initRegister")
	@ProcessFeignCalledResult
	ApiResultSet initRegister(@RequestParam(value="applyOid",required=false)String applyOid,
							  @RequestParam(value="tomcatOid",required=false)String tomcatOid);

	/**
	 *  上传注册授权文件的解析
	 * @param attaOid  附件oid
	 * @return
	 * @throws Exception
	 */
	@ProcessFeignCalledResult
	@RequestMapping(value = "/uploadRegister", method = { RequestMethod.GET })
	ApiResultSet uploadRegister(@RequestParam(value="attaOid",required=false) String attaOid);

	/**
	 *  上传注册授权文件
	 * @return
	 * @throws Exception
	 */
	@ProcessFeignCalledResult
	@PostMapping(value = "/uploadRegisterFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	ApiResultSet uploadRegisterFile();

	/**
	 * @description: 检查系统是否注册
	 * @author: wuxx
	 * @Date: 2020/11/4 10:46
	 **/
	@RequestMapping(value = "/checkRegister", method = { RequestMethod.GET })
	ApiResultSet checkRegister();

	/**
	 * @description: 检查用户所在区划是否注册
	 * @author: wuxx
	 * @Date: 2020/11/17 10:46
	 **/
	@RequestMapping(value = "/checkUserDistrictCodeRegister", method = { RequestMethod.GET })
	ApiResultSet checkUserDistrictCodeRegister(@RequestParam(value="districtOid",required=false) String districtOid);

}
