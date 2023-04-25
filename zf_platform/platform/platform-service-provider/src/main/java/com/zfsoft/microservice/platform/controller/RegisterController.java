package com.zfsoft.microservice.platform.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.zfsoft.microservice.platform.data.sys.SysAtta;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.microservice.platform.data.vo.RegisterLicense;
import com.zfsoft.microservice.platform.data.vo.ZhuoFanRegister;
import com.zfsoft.microservice.platform.feign.settings.SysConfigFeignService;
import com.zfsoft.microservice.platform.manager.sys.RegisterManager;
import com.zfsoft.microservice.platform.manager.sys.SysAttaManager;
import com.zfsoft.microservice.platform.manager.sys.SysDistrictManager;
import com.zfsoft.microservice.platform.service.RegisterService;
import com.zfsoft.microservice.platform.util.ClientServer;
import com.zfsoft.microservice.platform.util.SecurityUtil;
import com.zfsoft.microservice.settings.data.SysConfig;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:   注册授权Controller
 * @author: wuxx
 * @Date: 2020/11/3 10:40
 **/
@RestController
public class RegisterController implements RegisterService {

	//推荐创建不可变静态类成员变量
	private static final Log log = LogFactory.get();

	@Autowired
	private SysConfigFeignService sysConfigFeignService;

	@Resource
	private SysDistrictManager sysDistrictManager;

	@Autowired
	private RegisterManager registerManager;

	@Autowired
	private SysAttaManager sysAttaManager;

	private String key = "zfsoft888";

	@Autowired
	private ZhuoFanRegister zhuoFanRegister;
	/**
	 * RegisterController.initRegister：初始化注册码输入页面
	 * RegisterController.initRegisterLicense：初始化注册码生成页面
	 * RegisterController.generateRegister：生成注册码
	 */
	//@RequestMapping(value = "/initRegister")
	public ApiResultSet initRegister(String applyOid, String tomcatOid) {
		log.info("初始化注册码输入页面");
		Map map = new HashMap();
		try {
			String machineId = registerManager.initRegister(key);
			SysConfig config = sysConfigFeignService.getSysConfigByCode("CUSTOMER_NAME").getData();
			map.put("customerName", config.getValue());
			map.put("machineId", machineId);
			map.put("applyOid", applyOid);
			map.put("tomcatOid", tomcatOid);
		} catch (Exception e) {
			log.error("初始化注册码输入页面异常", e);
			throw new ResultInfoException("初始化注册码输入页面异常！");
		}
		return new ApiResultSet<>(map);

	}

	/**
	 * @description: 验证上传注册文件是否正确
	 * @author: wuxx
	 * @Date: 2020/11/3 10:49
	 **/
	//@RequestMapping(value = "/checkRegisterLicense")
	public ApiResultSet checkRegisterLicense(RegisterLicense registerLicense) {
		log.info("验证上传注册文件是否正确");
		String message = null;
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			message = registerManager.checkRegisterLicense(registerLicense, key);
			if (StrUtil.isEmpty(message)) {
				if (!StrUtil.isEmpty(registerLicense.getApplyOid())) {
					message = ClientServer.sendPost(getJson(registerLicense, request));
				}
			}
			if (StrUtil.isEmpty(message)) {
				return new ApiResultSet<>("注册成功！");
			} else {
				throw new ResultInfoException(message);
			}
		} catch (Exception e) {
			log.error("验证上传授权文件异常！");
			SysConfig sysConfig = sysConfigFeignService.getSysConfigByCode("AUTHORIZATION_ERROR_MESSAGE").getData();
			String errMessage = null==sysConfig?"验证上传授权文件异常！":sysConfig.getValue();
			throw new ResultInfoException(errMessage,e);
		}
	}

	/**
	 * @description: 获取json对象
	 * @author: wuxx
	 * @Date: 2020/11/3 11:03
	 **/
	private String getJson(RegisterLicense registerLicense, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<>(7);
		map.put("applyOid", req.getParameter("applyOid"));
		map.put("tomcatOid", req.getParameter("tomcatOid"));
		map.put("productCode", req.getParameter("productCode"));
		map.put("customerName", req.getParameter("customerName"));
		map.put("authoriseBeginDate", registerLicense.getStartDate());
		map.put("authoriseWarnDate", registerLicense.getWarnDate());
		map.put("authoriseExpireDate", registerLicense.getExpireDate());
		return com.alibaba.fastjson.JSON.toJSONString(map);
	}


	/**
	 * @description:  上传注册授权文件
	 * @author: wuxx
	 * @Date: 2020/11/3 11:04
	 **/
	//@RequestMapping(value = "/uploadRegister", method = { RequestMethod.GET })
	public ApiResultSet uploadRegister(String attaOid) {
		Map<String, Object> map = new HashMap<String, Object>(6);
		StringBuffer sb = new StringBuffer();
		try {
			SysAtta sysAtta = sysAttaManager.getSysAttaByAttaOid(attaOid);
			InputStream inputStream = null;
			//上传成功
			if(null!=sysAtta && StrUtil.isNotEmpty(sysAtta.getFastdfsNginxUrl())){
				//解决可能nginx文件中无附件的问题，如果存在附件，直接再nginx中下载
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				HttpUtil.download(sysAtta.getFastdfsNginxUrl(), outputStream, true);
				inputStream = new ByteArrayInputStream(outputStream.toByteArray());
			}else{
				HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
				inputStream = sysAttaManager.getFileInputStream(request, attaOid);
			}
			if(null!=inputStream){
				InputStreamReader read = new InputStreamReader(inputStream, "UTF-8");
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					sb.append(lineTxt);
				}
				read.close();
				String mW = SecurityUtil.detrypt(sb.toString(), key);
				String[] mwArray = mW.split(",");
				for (String str : mwArray) {
					if (str.indexOf("ExpireDate") != -1) {
						map.put("expireDate", str.split(":")[1]);
						String configWarn = sysConfigFeignService.getSysConfigByCode("REGISTER_WARN").getData().getValue();
						map.put("warnDate", DateUtil.formatDate(DateUtil.offsetDay(DateUtil.parse(str.split(":")[1], DatePattern.NORM_DATE_PATTERN),
								-Integer.parseInt(configWarn))));
					}
					if (str.indexOf("RegistDate") != -1) {
						map.put("startDate", str.split(":")[1]);
					}
				}
				map.put("uploadUrl", sysAtta.getFastdfsNginxUrl());
			}

		} catch (Exception e) {
			log.error("文件上传失败,非法授权文件！");
			SysConfig sysConfig = sysConfigFeignService.getSysConfigByCode("AUTHORIZATION_ERROR_MESSAGE").getData();
			String errMessage = null==sysConfig?"文件上传失败,非法授权文件！":sysConfig.getValue();
			throw new ResultInfoException(errMessage);
		}
		try {
			if (StrUtil.isEmpty(sb.toString())) {
				throw new ResultInfoException("请上传正确的授权文件！");
			}
			return new ApiResultSet<>(map);
		} catch (Exception e) {
			log.error("请上传正确的授权文件！");
			throw new ResultInfoException("请上传正确的授权文件！");
		}
	}

	/**
	 * @description:  上传注册文件
	 * @author: wuxx
	 * @Date: 2020/11/3 16:33
	 **/
	public ApiResultSet uploadRegisterFile(){
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest) request;
			MultipartFile multipartFile = multipartRequest.getFile("file");//file是form-data中二进制字段对应的name
			System.out.println(multipartFile.getSize());
			String userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
			MockMultipartFile file =null;
			if(!file.getName().contains(".data")){
				throw new ResultInfoException("请上传正确的授权文件！");
			}
			//HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			UploadUtil uploadUtil = new UploadUtil(request);
			String filePath = uploadUtil.uploadFile(file);
			SysAttaTemp sysAttaTemp = uploadUtil.getSysAttaFile(filePath, userOid);
			SysAtta atta = new SysAtta();
			BeanUtils.copyProperties(sysAttaTemp, atta);
			//保存附件信息
			sysAttaManager.saveSysAtta(atta);
			JSONObject jsonObject = JSONUtil.createObj().set("oid", atta.getAttaOid()).set("url", atta.getFastdfsNginxUrl()).set("name", atta.getOriginName()).set("size",atta.getFileSize());
			return new ApiResultSet<>(jsonObject);
		}catch (Exception e){
			throw new ResultInfoException(e.getMessage());
		}
	}

	/**
	 * @description: 检查系统是否注册
	 * @author: wuxx
	 * @Date: 2020/11/4 10:46
	 **/
	@Override
	public ApiResultSet checkRegister() {
		ApiResultSet apiResultSet = new ApiResultSet<>();
		if(null==zhuoFanRegister.getIsAble() || zhuoFanRegister.getIsAble()){
			apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
			//验证是否注册授权
			if (BaseStaticParameter.NO.equals(BaseStaticParameter.AUTH_STATE)) {
				apiResultSet.setMessage("平台未注册！");
				return apiResultSet;
			}
			// 授权是否开始 发布时去掉下面注释
			if (BaseStaticParameter.STARTDATE_.after(new Date())) {
				apiResultSet.setMessage("未到授权开始时间！");
				return apiResultSet;
			}
			//  授权是否到期 发布时去掉下面注释
			if (BaseStaticParameter.DUEDATE_.before(new Date())) {
				apiResultSet.setMessage("授权已到期！");
				return apiResultSet;
			}
			//相差
			if(null!=BaseStaticParameter.DUEDATE_WARN && BaseStaticParameter.DUEDATE_WARN.getTime()<new Date().getTime()){
				long warnDay = DateUtil.between(BaseStaticParameter.DUEDATE_, new Date(), DateUnit.DAY)+1;
				apiResultSet.setMessage(warnDay+"");
			}
		}
		apiResultSet.setCode(ApiResultSet.SUCCESS);
		return apiResultSet;
	}

	/**
	 * @description: 检查用户所在区划是否注册
	 * @author: wuxx
	 * @Date: 2020/11/17 10:46
	 **/
	@Override
	public ApiResultSet checkUserDistrictCodeRegister(String districtOid) {
		ApiResultSet apiResultSet = new ApiResultSet<>();
		if(null==zhuoFanRegister.getIsAble() || zhuoFanRegister.getIsAble()){
			if (StrUtil.isEmpty(districtOid)) {
				apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
				apiResultSet.setMessage("用户区划未授权，登录失败！");
				return apiResultSet;
			}
			SysDistrict district = sysDistrictManager.getSysDistrictByDistrictOid(districtOid);
			if(null!=district && null!=BaseStaticParameter.REGISTER_CODE){
				if(BaseStaticParameter.REGISTER_CODE.contains(district.getCode())){
					apiResultSet.setCode(ApiResultSet.SUCCESS);
					return apiResultSet;
				}
			}
			apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
			apiResultSet.setMessage("用户区划未授权，登录失败！");
			return apiResultSet;
		}
		apiResultSet.setCode(ApiResultSet.SUCCESS);
		return apiResultSet;
	}
}
