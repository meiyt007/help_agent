package com.zfsoft.superwindow.controller.easyquickcase;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.data.QlCaseApplay;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.feign.SysDistrictFeignService;
import com.zfsoft.cases.feign.SysOrganFeginService;
import com.zfsoft.cases.service.QlCaseApplayService;
import com.zfsoft.cases.service.QlCaseMaterialService;
import com.zfsoft.cases.service.QlCaseService;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.microservice.settings.data.SysConfig;
import com.zfsoft.microservice.settings.service.SysConfigService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.rest.pojo.face.FaceMatchRequest;
import com.zfsoft.rest.pojo.face.FaceMatchResponse;
import com.zfsoft.rest.service.face.IFaceRestService;
import com.zfsoft.service.sxService.service.SxServiceLocationService;
import com.zfsoft.service.sxService.service.SxServiceService;
import com.zfsoft.superwindow.controller.easyquickcase.data.ReqPersonDto;
import com.zfsoft.superwindow.data.sso.NatureUser;
import com.zfsoft.superwindow.data.yxpz.GuidSpeech;
import com.zfsoft.superwindow.data.zzzd.PointInfo;
import com.zfsoft.superwindow.data.zzzd.TerminalInfo;
import com.zfsoft.superwindow.dbaccess.data.DbPersonAuthInfo;
import com.zfsoft.superwindow.dbaccess.data.DbSsoNatureUser;
import com.zfsoft.superwindow.feign.settings.*;
import com.zfsoft.superwindow.feign.settings.data.TextInOcrFormResponse;
import com.zfsoft.superwindow.manager.sso.NatureUserManager;
import com.zfsoft.superwindow.manager.sso.PersonAuthInfoManager;
import com.zfsoft.superwindow.manager.yxpz.GuidSpeechManager;
import com.zfsoft.superwindow.manager.zzzd.PointManager;
import com.zfsoft.superwindow.manager.zzzd.TerminalManager;
import com.zfsoft.superwindow.service.easyquickcase.TerminalService;
import com.zfsoft.superwindow.service.easyquickcase.data.CasePrintCertificateDto;
import com.zfsoft.superwindow.service.easyquickcase.data.ReqPersonAuthInfoDto;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.JwtUtil;
import com.zfsoft.superwindow.util.UUIDUtil;
import com.zfsoft.superwindow.util.fa.AesUtil;
import com.zfsoft.superwindow.util.fa.AiTokenUtil;
import com.zfsoft.service.sxService.data.SxService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author wangyg
 * @description 自助端接口
 * @date 2022/6/16
 * @return
 */

@RestController
@Slf4j
@Api(tags = "自助端接口")
public class TerminalController implements TerminalService {

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired(required = false)
    private AiTokenUtil aiTokenUtil;

//    @Autowired(required = false)
//    private IFaceRestService faceRestService;

    @Resource
    private SysConfigService sysConfigService;

    @Resource
    private NatureUserManager natureUserManager;

    @Resource
    private PersonAuthInfoManager personAuthInfoManager;

    @Resource
    private GuidSpeechManager guidSpeechManager;

    @Resource
    private QlCaseService qlCaseServiceFeginService;

    @Resource
    private QlCaseApplayService qlCaseApplayServiceFeginService;

    @Resource
    private SysOrganFeginService sysOrganFeginService;

    @Resource
    private SxServiceService sxServiceFeginService;

    @Resource
    private QlCaseMaterialService qlCaseMaterialServiceFeginService;

    @Resource
    private SysDistrictFeignService sysDistrictFeginService;

    public final String TOKEN_KEY = "access_token";

    @Resource
    private TerminalManager terminalManager;

    @Resource
    private PointManager pointManager;

    @Resource
    private SxServiceLocationService sxServiceLocationFeignService;

    @Resource
    private TextInOcrFormFeignService textInOcrFormFeignService;

    @Override
    public ApiResultSet<String> login(ReqPersonAuthInfoDto reqPersonAuthInfoDto) {
        Assert.notNull(reqPersonAuthInfoDto, "用户信息不能为空");
        String cardInformation = reqPersonAuthInfoDto.getCardInformation();
        ApiResultSet<String> apiResultSet = new ApiResultSet<>();
        //如果密文有值，将数据解密后赋值
        if (StrUtil.isNotBlank(cardInformation)) {
            try {
                String info = AesUtil.decryptAES(cardInformation);
                if (info == null) {
                    apiResultSet.setCode(500);
                    apiResultSet.setMessage("用户信息为空");
                    return apiResultSet;
                }
                ReqPersonDto dto = JSON.parseObject(info, ReqPersonDto.class);
                reqPersonAuthInfoDto.setUserName(dto.getName());
                reqPersonAuthInfoDto.setUserSex(dto.getSex());
                reqPersonAuthInfoDto.setSignAndIssueOrg(dto.getDepartment());
                reqPersonAuthInfoDto.setUserPeoples(dto.getNation());
                reqPersonAuthInfoDto.setUserBirthDay(dto.getBirthday());
                reqPersonAuthInfoDto.setUserCardNumber(dto.getNumber());
                reqPersonAuthInfoDto.setUserAddr(dto.getAddress());
                reqPersonAuthInfoDto.setExpiryTime(dto.getTimeLimit());
                reqPersonAuthInfoDto.setHeadImgBase64(dto.getImage());
            } catch (Exception e) {
                apiResultSet.setCode(500);
                apiResultSet.setMessage(e.getMessage());
                return apiResultSet;
            }
        }
        if (StrUtil.isBlank(reqPersonAuthInfoDto.getCardInformation())) {
            Assert.notBlank(reqPersonAuthInfoDto.getExpiryTime(), "有效期不能为空！");
            Assert.notBlank(reqPersonAuthInfoDto.getHeadImgBase64(), "身份证头像不能为空！");
            Assert.notBlank(reqPersonAuthInfoDto.getSignAndIssueOrg(), "签发机关不能为空！");
            Assert.notBlank(reqPersonAuthInfoDto.getUserAddr(), "用户地址不能为空！");
            Assert.notBlank(reqPersonAuthInfoDto.getUserBirthDay(), "生日不能为空！");
            Assert.notBlank(reqPersonAuthInfoDto.getUserCardNumber(), "身份证号不能为空！");
            Assert.notBlank(reqPersonAuthInfoDto.getUserName(), "姓名不能为空！");
            Assert.notBlank(reqPersonAuthInfoDto.getUserPeoples(), "民族不能为空！");
            Assert.notBlank(reqPersonAuthInfoDto.getUserSex(), "性别不能为空！");
        }
        // 人脸核验--------begin
        // 获取图片base64
        String base64Str = reqPersonAuthInfoDto.getFaceImgBase64();

        FaceMatchResponse faceMatchResponse = new FaceMatchResponse();
//        if (StrUtil.isNotBlank(base64Str) && base64Str.equals(reqPersonAuthInfoDto.getHeadImgBase64())) {
//            faceMatchResponse.setCode(200);
//            faceMatchResponse.setScore(99.0);
//        } else {
//            FaceMatchRequest faceMatchRequest = aiTokenUtil.getTokenRequest(FaceMatchRequest.class);
//            faceMatchRequest.setBase64ImageOne(base64Str);
//            faceMatchRequest.setBase64ImageTwo(reqPersonAuthInfoDto.getHeadImgBase64());
//            faceMatchResponse = faceRestService.faceMatch(faceMatchRequest);
//        }
        Integer resultScore = -1;
        if (200 != faceMatchResponse.getCode()) {
            apiResultSet.setCode(500);
            apiResultSet.setMessage("人证核验失败");
            return apiResultSet;
        } else {
            resultScore = faceMatchResponse.getScore().intValue();
            //获取系统配置比对通过分数
            ApiResultSet<SysConfig> sysConfigData = sysConfigService.getSysConfigByCode("PASS_SCORE");
            if (200 != sysConfigData.getCode()) {
                apiResultSet.setCode(500);
                apiResultSet.setMessage("系统异常，请联系服务人员");
                return apiResultSet;
            }
            Integer passScore = Integer.valueOf(sysConfigData.getData().getValue());
            //调用结果验证
            if (resultScore < passScore) {
                apiResultSet.setCode(500);
                apiResultSet.setMessage("人证核验不通过");
                return apiResultSet;
            }
        }
        // 人脸核验--------end
        NatureUser natureUser = natureUserManager.getUserByCardNo(reqPersonAuthInfoDto.getUserCardNumber());
        DbSsoNatureUser user = new DbSsoNatureUser();
        if (null == natureUser) {
            user.setUserId(UUIDUtil.randomUUID());
            user.setUserName(reqPersonAuthInfoDto.getUserName());
            user.setCardNo(reqPersonAuthInfoDto.getUserCardNumber());
            if ("男".equals(reqPersonAuthInfoDto.getUserSex())) {
                user.setSex("1");
            } else {
                user.setSex("2");
            }
            user.setBirthday(reqPersonAuthInfoDto.getUserBirthDay());
            user.setCardType("SFZ");
            user.setCreateTime(new Date());
            user.setStatus("1");
            user.setSortOrder("1");
            user.setRegisterType("1");
        } else {
            // app端登陆可能没有性别等一些信息
            natureUser.setUserName(reqPersonAuthInfoDto.getUserName());
            if ("男".equals(reqPersonAuthInfoDto.getUserSex())) {
                natureUser.setSex("1");
            } else {
                natureUser.setSex("2");
            }
            natureUser.setBirthday(reqPersonAuthInfoDto.getUserBirthDay());
            BeanUtils.copyProperties(natureUser, user);
        }
        natureUserManager.saveOrUpdate(user);
        DbPersonAuthInfo dbPersonAuthInfo = BeanUtil.toBean(reqPersonAuthInfoDto, DbPersonAuthInfo.class);
        dbPersonAuthInfo.setFaceScore(String.valueOf(resultScore));
        dbPersonAuthInfo.setPersonOid(UUIDUtil.randomUUID());
        dbPersonAuthInfo.setFaceImageBase64(base64Str);
        dbPersonAuthInfo.setHeadImageBase64(reqPersonAuthInfoDto.getHeadImgBase64());
        dbPersonAuthInfo.setDeleteStatus("N");
        dbPersonAuthInfo.setCreateDate(new Date());
        dbPersonAuthInfo.setModifyDate(new Date());
        // 上传header头像
        personAuthInfoManager.saveOrUpdatePersonAuthInfo(dbPersonAuthInfo);
        // 将用户信息放入jwt
        String token = JwtUtil.sign(reqPersonAuthInfoDto.getUserCardNumber(), 0);
        redisTemplate.opsForValue().set("browser:jwt:" + token, token, 30, TimeUnit.MINUTES);
        apiResultSet.setData(token);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<String> loginOut(HttpServletRequest request) {
        ApiResultSet<String> apiResultSet = new ApiResultSet<>();
        String token = request.getHeader(TOKEN_KEY);
        if (StrUtil.isBlank(token)) {
            token = request.getParameter(TOKEN_KEY);
        }
        if (StrUtil.isBlank(token)) {
            apiResultSet.setCode(500);
            apiResultSet.setMessage("登录信息为空，无需注销");
            return apiResultSet;
        }
        redisTemplate.delete("browser:jwt:" + token);
        apiResultSet.setData("注销登录成功");
        return apiResultSet;
    }

    @Override
    public ApiResultSet<GuidSpeech> selectByGuidSpeechCode(String guidSpeechCode) {
        GuidSpeech guid = guidSpeechManager.selectByGuidSpeechCode(guidSpeechCode);
        return new ApiResultSet<>(guid);
    }

    @Override
    public ApiResultSet<CasePrintCertificateDto> initCasePrintCertificate(String caseNumber) {
        CasePrintCertificateDto dto = new CasePrintCertificateDto();
        ApiResultSet<CasePrintCertificateDto> apiResultSet = new ApiResultSet<>();

        ApiResultSet<QlCase> qlCaseApiResultSet = qlCaseServiceFeginService.queryQlCaseByCaseNumber(caseNumber);
        if (200 != qlCaseApiResultSet.getCode()) {
            apiResultSet.setCode(500);
            apiResultSet.setMessage("系统异常，请联系服务人员");
            return apiResultSet;
        }
        QlCase qlCase = qlCaseApiResultSet.getData();
        dto.setCaseOid(qlCase.getCaseOid());
        dto.setCaseNumber(qlCase.getCaseNumber());
        dto.setTerminalCode(qlCase.getTerminalCode());

        ApiResultSet<QlCaseApplay> qlCaseApplayApiResultSet = qlCaseApplayServiceFeginService.queryQlCaseApplayByCaseOid(qlCase.getCaseOid());
        if (200 != qlCaseApplayApiResultSet.getCode()) {
            apiResultSet.setCode(500);
            apiResultSet.setMessage("系统异常，请联系服务人员");
            return apiResultSet;
        }
        QlCaseApplay qlCaseApplay = qlCaseApplayApiResultSet.getData();
        dto.setApplyUserName(qlCaseApplay.getApplyUserName());
        dto.setApplyUserPhone(qlCaseApplay.getApplyUserPhone());

        ApiResultSet<SysOrgan> organApiResultSet = sysOrganFeginService.getSysOrganByOrganOid(qlCase.getOrganOid());
        if (200 != organApiResultSet.getCode()) {
            apiResultSet.setCode(500);
            apiResultSet.setMessage("系统异常，请联系服务人员");
            return apiResultSet;
        }
        SysOrgan sysOrgan = organApiResultSet.getData();
        dto.setOrganOid(sysOrgan.getOrganOid());
        dto.setOrganName(sysOrgan.getName());
        dto.setOrganTelphone(sysOrgan.getTelphone());

        ApiResultSet<SxService> sxServiceApiResultSet = sxServiceFeginService.getSxServiceByOid(qlCase.getServiceOid());
        if (200 != sxServiceApiResultSet.getCode()) {
            apiResultSet.setCode(500);
            apiResultSet.setMessage("系统异常，请联系服务人员");
            return apiResultSet;
        }
        SxService sxService = sxServiceApiResultSet.getData();
        dto.setServiceOid(sxService.getServiceOid());
        dto.setBasicCode(sxService.getBasicCode());
        dto.setServiceName(sxService.getServiceName());

        ApiResultSet<List<QlCaseMaterial>> listApiResultSet = qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(qlCase.getCaseOid());
        if (200 != listApiResultSet.getCode()) {
            apiResultSet.setCode(500);
            apiResultSet.setMessage("系统异常，请联系服务人员");
            return apiResultSet;
        }
        List<QlCaseMaterial> qlCaseMaterialList = listApiResultSet.getData();
        if (CollUtil.isNotEmpty(qlCaseMaterialList)) {
            List<String> materialNameList = qlCaseMaterialList.stream().map(QlCaseMaterial::getMaterialName).collect(Collectors.toList());
            dto.setMaterialNameList(materialNameList);
        }
        apiResultSet.setData(dto);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<SysDistrict>> getDistListByParentOid(String parentOid) {
        ApiResultSet<List<SysDistrict>> apiResultSet = new ApiResultSet<>();
        //拿父级区划信息
        ApiResultSet<SysDistrict> parent = sysDistrictFeginService.getSysDistrictByDistrictOid(parentOid);
        List<SysDistrict> data = new ArrayList<>();
        //拿到子集
        ApiResultSet<List<SysDistrict>> child = sysDistrictFeginService.querySysDistrictListByParentOid(parentOid);
        //降父级子级放入一个list
        data.add(parent.getData());
        data.addAll(child.getData());
        apiResultSet.setData(data);
        return apiResultSet;
    }

    @Override
    public ApiResultSet getLocationInfoByServiceOid(String serviceOid, String terminalCode) {
        ApiResultSet<String> apiResultSet = new ApiResultSet<>();
        TerminalInfo terminalInfo = terminalManager.getTerminalInfoByTerminalCode(terminalCode);
        if (null == terminalInfo || StrUtil.isEmpty(terminalInfo.getPointOid())) {
            apiResultSet.setCode(500);
            apiResultSet.setMessage("系统异常，请联系服务人员");
            return apiResultSet;
        }
        PointInfo pointInfo = pointManager.getPointInfoById(terminalInfo.getPointOid());
        if (null == pointInfo || StrUtil.isEmpty(pointInfo.getAddress())) {
            apiResultSet.setCode(500);
            apiResultSet.setMessage("系统异常，请联系服务人员");
            return apiResultSet;
        }
        ApiResultSet apiResultSetInfo = sxServiceLocationFeignService.getLocationInfo(serviceOid, pointInfo.getAddress());
        return apiResultSetInfo;
    }

    @Override
    public ApiResultSet discernBusiness(String picBase64) {
        ApiResultSet<String> apiResultSet = new ApiResultSet<>();
        if (StrUtil.isEmpty(picBase64)) {
            apiResultSet.setCode(500);
            apiResultSet.setMessage("图片不能为空");
            return apiResultSet;
        }
        List<String> list = new ArrayList<>();
        list.add(picBase64);
        TextInOcrFormResponse textInOcrFormResponse = textInOcrFormFeignService.textInOcrBusinessLicense(list);
        if(200!=textInOcrFormResponse.getCode()){
            apiResultSet.setCode(500);
            apiResultSet.setMessage(textInOcrFormResponse.getMessage());
            return apiResultSet;
        }
        String data = textInOcrFormResponse.getData();
        apiResultSet.setData(data);
        return apiResultSet;
    }
}
