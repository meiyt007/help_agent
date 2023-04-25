package com.zfsoft.ha.managers;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.feign.SysOrganFeginService;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.ha.util.ClientServer;
import com.zfsoft.ha.wandaResponse.*;
import com.zfsoft.microservice.platform.data.vo.SysOrganVo;
import com.zfsoft.microservice.platform.service.sys.SysOrganService;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceExtendMapper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceMapper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceMaterialMapper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceQuestionMapper;
import com.zfsoft.service.dbaccess.data.sxService.*;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.service.sxService.data.SxServiceExtend;
import com.zfsoft.service.sxService.data.SxServiceMaterial;
import com.zfsoft.service.sxService.data.SxServiceQuestion;
import com.zfsoft.single.util.HttpRequestUtil;
import com.zfsoft.single.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * @Description //事项对接实现
 * @Author: Wangyh
 * @Date: 2022/8/22 16:20
 */
@Service
@Slf4j
public class HaMatterDockManager {
    //登录接口ip
    private static String loginToken="http://172.21.179.149/syn/base/open/loginToken";

    //获取事项列表接口地址
    private static String monitorUrl ="http://172.21.179.149/syn/fwznSimpleData/queryItemListWithDkb";

    //获取事项情形列表接口地址
    private static String situationUrl ="http://172.21.179.149/syn/fwznSimpleData/queryStatusListWithDkb";

    //获取查询材料详情信息接口地址
    private static String commitUrl ="http://172.21.179.149/syn/dkbItemStuff/getCommit";

    //获取事项常见问题列表接口地址
    private static String issueUrl ="http://172.21.179.149:8081/ac-dkb/dkbIssue/queryIssue.do";

    //获取情形的办事指南接口地址
    private static String getShsxDetail ="http://172.21.179.149/syn/fwznSimpleData/getShsxDetail";
    //用户
    private static String account ="bangb";
    //密码
    private static String password ="1";

    @Resource
    private DbSxServiceMapper dbSxServiceMapper;

    @Resource
    private DbSxServiceExtendMapper dbSxServiceExtendMapper;

    @Resource
    private DbSxServiceMaterialMapper dbSxServiceMaterialMapper;

    @Resource
    private DbSxServiceQuestionMapper dbSxServiceQuestionMapper;

    @Resource
    private SysOrganFeginService sysOrganFeginService;
    /**
     * 同步数据到库中
     */
    public void MatterSynchronization() throws IOException {

            //1.调用万达登录接口，获取登录token
            String token =this.getToken();
            //2.调用万达事项列表接口(这个接口中的事项是种类，一个种类事项中可能包含多个事项)
            List<MatterListVo> matterListVoList = this.queryItemListWithDkb(token,null,null,null);
            //3.调用万达事项情形列表接口(他们的情形就相当于我们的事项)
            for(int i=0;i<matterListVoList.size();i++){
                MatterListVo matterListVo = matterListVoList.get(i);
                if(matterListVo.getItemID().equals("04673e8c-e1cb-4f2b-92c6-ee11cbdfe8f4")){
                    System.out.println("ccc");
                }
                List<SituationListVo> situationListVos = this.queryStatusListWithDkb(token,matterListVo.getItemID(),null);
                for(int j=0;j<situationListVos.size();j++){
                        //插入组织机构表数据
                        SysOrganVo sysOrganVo = new SysOrganVo();
//                        sysOrganVo.setOrganOid(matterListVo.getOrganCode()); //从万达导入业务主键
                        sysOrganVo.setOrganOid(matterListVo.getDefaultOrganCode()); //新增字段维护,万达推送接口时又需要新增这个字段，才可以调通接口，所以在数据导入时需要导入这个字段
                        sysOrganVo.setName(matterListVo.getOrganName()); //从万达导入的机构名称
                        sysOrganVo.setDistrictOid("4028545d665734290166b02711c20073");//所属区划主键，默认值
                        sysOrganVo.setFullName(matterListVo.getOrganName());//组织机构全名称
                        Map<String, Object>  apiResultSet = sysOrganFeginService.saveSysOrganSyn(sysOrganVo);
                        //获取事项基本信息
                        SituationListVo situationListVo = situationListVos.get(j);
                        Integer size = 0;
                        //根据事项id查询事项信息
                        DbSxService service=dbSxServiceMapper.getDbSxServiceByServiceOid(situationListVo.getStatusId());
                        if(service !=null && StringUtil.isNotEmpty(service.getIsShield()) && service.getIsShield().equals("1")){ //判断事项是否需要屏蔽导入 1-屏蔽 0 -不需要屏蔽
                            System.out.println("serviceoid size= "+size++);
                        }else{
                            DbSxServiceWithBLOBs record=new DbSxServiceWithBLOBs();
                            //基础数据塞入
                            SxService sxService = new SxService();
                            sxService.setExistChildItem("0");
                            sxService.setServiceStatus((short) 3);
                            sxService.setDelFlag((short) 0);
                            sxService.setOrganOid(matterListVo.getOrganCode()); //机构id
                            sxService.setOrganName(matterListVo.getOrganName()); //机构名称
    //                          service.setOrganName(matterListVo.getOrganName()); //机构名称
                            //判断更新保存
                            if(service !=null && !service.equals("")){//更新
                                service.setOrganOid(matterListVo.getOrganCode()); //机构id
                                this.updataMatter(service,situationListVo,record,token, matterListVo/*,guide*/);
                            }else {//新增
                                sxService.setOrganOid(matterListVo.getOrganCode()); //机构id
                                this.insertMatter(sxService,situationListVo,record,token,matterListVo/*,guide*/);
                            }
                        }

                }
            }
    }
    /**
     * 修改事项相关信息
     */
    public void updataMatter(DbSxService sxService,SituationListVo situationListVo,DbSxServiceWithBLOBs record,String token,MatterListVo matterListVo/*,Guide guide*/) throws IOException {
        //初始化接口新增标识
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        //基础数据塞入
        sxService.setExistChildItem("0");
        sxService.setServiceStatus((short) 3);
        sxService.setDelFlag((short) 0);
        //1.基础信息 将万达的事项id作为我们的业务主键 修改不需要
//        sxService.setServiceOid(situationListVo.getStatusId());
        //修改事项名称
        sxService.setServiceName(situationListVo.getItemName()+"-"+situationListVo.getStatusName());
        //endIMPLEMENT_CODE
        //办件类型
        sxService.setCaseType((short) 2);
        //是否支持网上办理，默认：支持 (0否、1是)
        sxService.setOnlinePayFlag((short) 1);
        //是否支持物流快递
        sxService.setExpressFlag((short)1);
        //行政管辖地
        sxService.setAdminJurisdiction("0");
        //是否联合办理
        sxService.setUnionOrganFlag((short)0);
        //是否有中介服务
        sxService.setIsZjfw((short)0);
        //是否允许特殊程序
        sxService.setIsSpecial((short)0);
        //移动端对接单点登录
        sxService.setAppIssingleLogin((short)0);
        //窗口端对接单点登录
        sxService.setIsSingleLogin("0");
        //是否支持自助终端办理
        sxService.setTerminalProcessing((short)0);
        //到现场次数
        sxService.setCountToScence("0");
        //公开方式
        sxService.setOpenWay((short)1);
        //公开渠道
        sxService.setOpenChannel((short)1);
        //是否支持预约
        sxService.setAppointmentFlag((short)1);
        //是否收费
        sxService.setChargeFlag((short)0);
        //是否收件及受理
        sxService.setSjStatus(1);
        sxService.setCreateDate(new Date());
        if(loginUser !=null){
            sxService.setCreateUser(loginUser.getUserOid());
            sxService.setDistrictOid(loginUser.getDistrictOid());
        }else{
            sxService.setCreateUser("00000000000000000000000000000010");
            sxService.setDistrictOid("4028545d665734290166b02711c20073");
        }
        //数据库后来新增字段 start
        sxService.setItemId(matterListVo.getItemID()); //事项唯一编码
        sxService.setWdOrder(situationListVo.getOrder());  //排序
        sxService.setItemNo(matterListVo.getItemNo()); //事项基本编码（12位，上海事项库catalog_code）
        sxService.setStatusNo(situationListVo.getStatusNo());//情形编码
        sxService.setIsScene("0");//返回的事项库名称是【一业一证库】表示是主题事项,其他的不是主题事项因为我们是黄埔的,所以默认不是
        //数据库后来新增字段 end
        BeanUtil.copyProperties(sxService,record);
        record.setModifyDate(new Date());
        dbSxServiceMapper.updateByPrimaryKeyWithBLOBs(record);
        //2.扩展信息
        DbSxServiceExtendWithBLOBs extend= new DbSxServiceExtendWithBLOBs();
        SxServiceExtend sxExtend= new SxServiceExtend();
        sxExtend.setExtendOid(UUID.randomUUID().toString().replace("-", ""));

        //新增导入字段
//        sxExtend.setLegalLimit(guide.getAnticipateDay()); //法定时限
//        sxExtend.setPromiseLimit(guide.getPromiseDay());//承诺时限
//        if(StringUtil.isNotEmpty(guide.getCkjssj())){
//            sxExtend.setHanleTimeRange(guide.getCkjssj()); //办理时间
//        }
        //修改逻辑不需要修改事项id
//        sxExtend.setServiceOid(situationListVo.getStatusId());
        sxExtend.setModifyDate(new Date());
        //结果样本类型
        sxExtend.setResultSampleType((short)0);
        //法定时限类型
        sxExtend.setLegalLimitType("W");
        //承诺时限类型
        sxExtend.setPromiseLimitType("W");
        //运行系统
        sxExtend.setRunSystem("0");
        //证照目录类型
        sxExtend.setDirectoryType((short)0);
        //是否长期有效
        sxExtend.setExpipyFlag((short)0);
        //是否权限划分
        sxExtend.setIsPermisionDivid((short)0);
        //是否有数量限制
        sxExtend.setNumberLimitType((short)0);
        //阶段性办理
        sxExtend.setIsStageHandle((short)1);
        //是否进驻政务大厅
        sxExtend.setIsEntryCenter((short)1);
        sxExtend.setModifyDate(new Date());
        BeanUtil.copyProperties(sxExtend,extend);
        dbSxServiceExtendMapper.insert(extend);
        //3.导入事项关联的材料信息
        List<CommitListVo> commitListVos = this.getCommit(token,situationListVo.getCommitID(),situationListVo.getStatusId());
        for(int k=0;k<commitListVos.size();k++){
            //获取材料对象信息
            CommitListVo commitListVo = commitListVos.get(k);
//            DbSxServiceMaterial dbSxServiceMaterial = dbSxServiceMaterialMapper.selectByMaterialOid(commitListVo.getRowguid());
            //声明对象材料对象
            SxServiceMaterial sxServiceMaterial = new SxServiceMaterial();
            //将万达的材料id赋值给我们的材料id  修改不需要业务id
            sxServiceMaterial.setMaterialOid(commitListVo.getRowguid());
            //将万达的情形id(他们的情形不是情形是事项)赋值给我们的所属事项id
            sxServiceMaterial.setServiceOid(situationListVo.getStatusId());
            //新增字段start
            sxServiceMaterial.setNmOriginal(commitListVo.getNmOriginal()); //原件数量,默认1
            sxServiceMaterial.setNmCopy(commitListVo.getNmCopy()); //复印件数量,默认1
            sxServiceMaterial.setIsScene("0");//返回的事项库名称是【一业一证库】表示是主题事项,其他的不是主题事项因为我们是黄埔的,所以默认不是
            sxServiceMaterial.setStuffStatus("0");//取值范围：0为首次提交、2为补充材料
            //新增字段end
            //材料名称
            sxServiceMaterial.setMaterialName(commitListVo.getMaterialname());
            //材料是否必选，万达的是字符串，且没有示例，暂时不传
            if(commitListVo.getMaterialnecessity().equals("1")){
                sxServiceMaterial.setMustFlag((short) 0);
            }else{
                sxServiceMaterial.setMustFlag((short) 1);
            }
            //备注
            sxServiceMaterial.setRemark(commitListVo.getBaknote());
            DbSxServiceMaterialWithBLOBs MaterialRecord  = new DbSxServiceMaterialWithBLOBs();
            BeanUtils.copyProperties(sxServiceMaterial, MaterialRecord);
            MaterialRecord.setDelFlag((short)0);
            if(loginUser !=null){
                String  userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
                MaterialRecord.setCreateUser(userOid);
            }else{
                MaterialRecord.setCreateUser("00000000000000000000000000000010");
            }
            MaterialRecord.setModifyDate(new Date());
            dbSxServiceMaterialMapper.updateByPrimaryKeySelective(MaterialRecord);
        }
        //4.导入事项关联的常见问题
        /*List<IssueVO> issueVOList = this.queryIssue(situationListVo.getStatusId(),null);
        for(int p=0;p<issueVOList.size();p++){
            IssueVO issueVO = issueVOList.get(p);
            SxServiceQuestion sxServiceQuestion = new SxServiceQuestion();
            //业务主键  修改业务，不需要业务主键
//            sxServiceQuestion.setQuestionOid(issueVO.getIssueID());
            //所属时间
            sxServiceQuestion.setServiceOid(situationListVo.getStatusId());
            //主题词，获取万达的问题类型
            sxServiceQuestion.setKeyWord(issueVO.getIssueType());
            //问题标题，获取万达的问题文本字段值
            sxServiceQuestion.setTitle(issueVO.getIssueInfo());
            //也取问题文本
            sxServiceQuestion.setDescription(issueVO.getIssueInfo());
            //问题解答
            sxServiceQuestion.setAnswer(issueVO.getFeedback());
            DbSxServiceQuestionWithBLOBs dbSxServiceQuestionWithBLOBs = new DbSxServiceQuestionWithBLOBs();
            BeanUtils.copyProperties(sxServiceQuestion, dbSxServiceQuestionWithBLOBs);
            dbSxServiceQuestionWithBLOBs.setCreateDate(new Date());
            dbSxServiceQuestionWithBLOBs.setModifyDate(new Date());
            dbSxServiceQuestionWithBLOBs.setDelFlag((short)0);
            this.dbSxServiceQuestionMapper.updateByPrimaryKeySelective(dbSxServiceQuestionWithBLOBs);
        }*/
    }
    /**
     * 新增事项相关信息
     */
    public void insertMatter(SxService sxService,SituationListVo situationListVo,DbSxServiceWithBLOBs record,String token,MatterListVo matterListVo/*,Guide guide*/) throws IOException {
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        //1.基础信息 将万达的事项id作为我们的业务主键
        sxService.setServiceOid(situationListVo.getStatusId());
        //新增事项名称
        sxService.setServiceName(situationListVo.getItemName()+"-"+situationListVo.getStatusName());
        //实施编码取的是情形编码
        sxService.setImplementCode(situationListVo.getStatusNo());
        //办件类型
        sxService.setCaseType((short) 2);
        //是否支持网上办理，默认：支持 (0否、1是)
        sxService.setOnlinePayFlag((short) 1);
        //是否支持物流快递
        sxService.setExpressFlag((short)1);
        //行政管辖地
        sxService.setAdminJurisdiction("0");
        //是否联合办理
        sxService.setUnionOrganFlag((short)0);
        //是否有中介服务
        sxService.setIsZjfw((short)0);
        //是否允许特殊程序
        sxService.setIsSpecial((short)0);
        //移动端对接单点登录
        sxService.setAppIssingleLogin((short)0);
        //窗口端对接单点登录
        sxService.setIsSingleLogin("0");
        //是否支持自助终端办理
        sxService.setTerminalProcessing((short)0);
        //到现场次数
        sxService.setCountToScence("0");
        //公开方式
        sxService.setOpenWay((short)1);
        //公开渠道
        sxService.setOpenChannel((short)1);
        //是否支持预约
        sxService.setAppointmentFlag((short)1);
        //是否收费
        sxService.setChargeFlag((short)0);
        //是否收件及受理
        sxService.setSjStatus(1);
        sxService.setCreateDate(new Date());
        if(loginUser !=null){
            sxService.setCreateUser(loginUser.getUserOid());
            sxService.setDistrictOid(loginUser.getDistrictOid());
        }else{
            sxService.setCreateUser("00000000000000000000000000000010");
            sxService.setDistrictOid("4028545d665734290166b02711c20073");
        }
        //数据库后来新增字段 start
        sxService.setItemId(matterListVo.getItemID()); //事项唯一编码
        sxService.setWdOrder(situationListVo.getOrder());  //排序
        sxService.setItemNo(matterListVo.getItemNo()); //事项基本编码（12位，上海事项库catalog_code）
        sxService.setStateNo(situationListVo.getStatusNo());//情形编码
        sxService.setIsScene("0");//返回的事项库名称是【一业一证库】表示是主题事项,其他的不是主题事项因为我们是黄埔的,所以默认不是
        //数据库后来新增字段 end
        BeanUtil.copyProperties(sxService,record);
        dbSxServiceMapper.insertSelective(record);
        //2.扩展信息
        DbSxServiceExtendWithBLOBs extend= new DbSxServiceExtendWithBLOBs();
        SxServiceExtend sxExtend= new SxServiceExtend();
        sxExtend.setExtendOid(UUID.randomUUID().toString().replace("-", ""));
        sxExtend.setServiceOid(situationListVo.getStatusId());
        //新增导入字段
//        sxExtend.setLegalLimit(guide.getAnticipateDay()); //法定时限
//        sxExtend.setPromiseLimit(guide.getPromiseDay());//承诺时限
//        if(StringUtil.isNotEmpty(guide.getCkjssj())){
//            sxExtend.setHanleTimeRange(guide.getCkjssj()); //办理时间
//        }

        sxExtend.setModifyDate(new Date());
        //结果样本类型
        sxExtend.setResultSampleType((short)0);
        //法定时限类型
        sxExtend.setLegalLimitType("W");
        //承诺时限类型
        sxExtend.setPromiseLimitType("W");
        //运行系统
        sxExtend.setRunSystem("0");
        //证照目录类型
        sxExtend.setDirectoryType((short)0);
        //是否长期有效
        sxExtend.setExpipyFlag((short)0);
        //是否权限划分
        sxExtend.setIsPermisionDivid((short)0);
        //是否有数量限制
        sxExtend.setNumberLimitType((short)0);
        //阶段性办理
        sxExtend.setIsStageHandle((short)1);
        //是否进驻政务大厅
        sxExtend.setIsEntryCenter((short)1);
        BeanUtil.copyProperties(sxExtend,extend);
        dbSxServiceExtendMapper.insert(extend);
        //3.导入事项关联的材料信息
        List<CommitListVo> commitListVos = this.getCommit(token,situationListVo.getCommitID(),situationListVo.getStatusId());
        for(int k=0;k<commitListVos.size();k++){
            //获取材料对象信息
            CommitListVo commitListVo = commitListVos.get(k);
            //声明对象材料对象
            SxServiceMaterial sxServiceMaterial = new SxServiceMaterial();
            //将万达的材料id赋值给我们的材料id
//            sxServiceMaterial.setMaterialOid(UUID.randomUUID().toString().replaceAll("-", ""));
            sxServiceMaterial.setMaterialOid(commitListVo.getRowguid());
            //将万达的情形id(他们的情形不是情形是事项)赋值给我们的所属事项id
            sxServiceMaterial.setServiceOid(situationListVo.getStatusId());
            //材料名称
            sxServiceMaterial.setMaterialName(commitListVo.getMaterialname());
            //新增字段start
            sxServiceMaterial.setNmOriginal(commitListVo.getNmOriginal()); //原件数量,默认1
            sxServiceMaterial.setNmCopy(commitListVo.getNmCopy()); //复印件数量,默认1
            sxServiceMaterial.setIsScene("0");//返回的事项库名称是【一业一证库】表示是主题事项,其他的不是主题事项因为我们是黄埔的,所以默认不是
            sxServiceMaterial.setStuffStatus("0");//取值范围：0为首次提交、2为补充材料
            //新增字段end

            //材料是否必选，万达的是字符串，且没有示例，暂时不传
            if(commitListVo.getMaterialnecessity().equals("1")){
                sxServiceMaterial.setMustFlag((short) 0);
            }else{
                sxServiceMaterial.setMustFlag((short) 1);
            }
            //备注
            sxServiceMaterial.setRemark(commitListVo.getBaknote());
            DbSxServiceMaterialWithBLOBs MaterialRecord  = new DbSxServiceMaterialWithBLOBs();
            BeanUtils.copyProperties(sxServiceMaterial, MaterialRecord);
            MaterialRecord.setDelFlag((short)0);
            MaterialRecord.setCreateDate(new Date());
            if(loginUser !=null){
                String  userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
                MaterialRecord.setCreateUser(userOid);
            }else{
                MaterialRecord.setCreateUser("00000000000000000000000000000010");
            }
            MaterialRecord.setModifyDate(new Date());
            DbSxServiceMaterial material =  dbSxServiceMaterialMapper.selectByMaterialOid(sxServiceMaterial.getMaterialOid());
            if(material ==null ){
                dbSxServiceMaterialMapper.insertSelective(MaterialRecord);
            }

        }
        //4.导入事项关联的常见问题
        /*List<IssueVO> issueVOList = this.queryIssue(situationListVo.getStatusId(),null);
        for(int p=0;p<issueVOList.size();p++){
            IssueVO issueVO = issueVOList.get(p);
            SxServiceQuestion sxServiceQuestion = new SxServiceQuestion();
            //业务主键
            sxServiceQuestion.setQuestionOid(issueVO.getIssueID());
            //所属时间
            sxServiceQuestion.setServiceOid(situationListVo.getStatusId());
            //主题词，获取万达的问题类型
            sxServiceQuestion.setKeyWord(issueVO.getIssueType());
            //问题标题，获取万达的问题文本字段值
            sxServiceQuestion.setTitle(issueVO.getIssueInfo());
            //也取问题文本
            sxServiceQuestion.setDescription(issueVO.getIssueInfo());
            //问题解答
            sxServiceQuestion.setAnswer(issueVO.getFeedback());
            DbSxServiceQuestionWithBLOBs dbSxServiceQuestionWithBLOBs = new DbSxServiceQuestionWithBLOBs();
            BeanUtils.copyProperties(sxServiceQuestion, dbSxServiceQuestionWithBLOBs);
            dbSxServiceQuestionWithBLOBs.setCreateDate(new Date());
            dbSxServiceQuestionWithBLOBs.setModifyDate(new Date());
            dbSxServiceQuestionWithBLOBs.setDelFlag((short)0);
            this.dbSxServiceQuestionMapper.insert(dbSxServiceQuestionWithBLOBs);
        }*/
    }


    /**
     * 登录获取token
     */
    public String getToken() throws IOException {
        String token ="";
        Map<String,Object> map = new HashMap<>();
        map.put("account",account);
        map.put("password",password);
        String json = JSON.toJSONString(map);
        String result = ClientServer.send(loginToken,json,"utf-8");
        JSONObject jsonObject = JSON.parseObject(result);
        Integer code = (Integer) jsonObject.get("code");
        if(1000 == code){
            JSONObject dataToken = JsonUtil.toJSONObject(jsonObject.get("dataToken"));
            token = (String) dataToken.get("token");
        }
        return token;
    }

    /**
     * @description:  获取事项库的事项列表
     * @param Authorization 登录token，放在headers中
     * @param page 分页页码，form-data
     * @param size 分页大小，form-data
     * @param databaseID 事项库id
     * @author: wangyh
     * @Date: 2022/8/22 15:19
     **/
    public List<MatterListVo> queryItemListWithDkb(String Authorization, Integer page, Integer size, String databaseID) {
        log.info("获取事项库的事项列表， Authorization{},page{},size{},databaseID:{}",Authorization,page,size,databaseID);
        if(StrUtil.isEmpty(Authorization)){
            return null;
        }
        if(StrUtil.isEmpty(databaseID)){
            databaseID = "262EA9AF-8D15-4CD3-8B20-18D614AB2453"; //默认值为黄埔的事项库
        }
        Map<String,Object> map = new HashMap<>();
        map.put("Authorization",Authorization);
        map.put("page",page);
        map.put("size",size);
        map.put("databaseID",databaseID);
        String result = HttpRequestUtil.sendPost(monitorUrl, map);
        List<MatterListVo> list = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(result);
        Integer code = (Integer) jsonObject.get("code");
        if(1000 == code){
            JSONArray dataArr = JsonUtil.toJSONArray(jsonObject.get("data"));
            for(int i=0;i<dataArr.size();i++) {
                String jsonKey = String.valueOf(dataArr.getJSONObject(i));
                MatterListVo matterListVo = (MatterListVo) JsonUtil.jsonToObject(jsonKey,MatterListVo.class);
                list.add(matterListVo);
            }
        }
        return list;
    }

    /**
     * @description:  获取情形列表
     * @param Authorization 登录token，放在headers中
     * @param itemID 事项Id，form-data
     * @param databaseID 事项库id
     * @author: wangyh
     * @Date: 2022/8/22 15:19
     **/
    public List<SituationListVo> queryStatusListWithDkb(String Authorization, String itemID, String databaseID) {
        log.info("获取情形列表， Authorization{},itemID{},databaseID:{}",Authorization,itemID,databaseID);
        if(StrUtil.isEmpty(Authorization)){
            return null;
        }
        if(StrUtil.isEmpty(databaseID)){
            databaseID = "262EA9AF-8D15-4CD3-8B20-18D614AB2453"; //默认值为黄埔的事项库
        }
        Map<String,Object> map = new HashMap<>();
        map.put("Authorization",Authorization);
        map.put("itemID",itemID);
        map.put("databaseID",databaseID);
        String result = HttpRequestUtil.sendPost(situationUrl, map);
        List<SituationListVo> list = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(result);
        Integer code = (Integer) jsonObject.get("code");
        if(1000 == code){
            JSONObject jsonData = (JSONObject) jsonObject.get("data");
            JSONArray dataArr = JsonUtil.toJSONArray(jsonData.get("status"));
            for(int i=0;i<dataArr.size();i++) {
                String jsonKey = String.valueOf(dataArr.getJSONObject(i));
                SituationListVo situationListVo = (SituationListVo) JsonUtil.jsonToObject(jsonKey,SituationListVo.class);
                list.add(situationListVo);
            }
        }
        return list;
    }

    /**
     * @description:  查询材料详情信息
     * @param Authorization 登录token，放在headers中
     * @param commitID
     * @param statusID 情形id
     * @author: wangyh
     * @Date: 2022/8/22 15:19
     **/
    public List<CommitListVo> getCommit(String Authorization, String commitID, String statusID) {
        log.info("查询材料详情信息， Authorization{},commitID{},statusID:{}",Authorization,commitID,statusID);
        if(StrUtil.isEmpty(Authorization)){
            return null;
        }
        if(StrUtil.isEmpty(commitID)){
            return null;
        }
        if(StrUtil.isEmpty(statusID)){
            statusID="";
        }
        Map<String,Object> map = new HashMap<>();
        map.put("Authorization",Authorization);
        map.put("commitID",commitID);
        map.put("statusID",statusID);
        String result = HttpRequestUtil.sendPost(commitUrl, map);
        List<CommitListVo> list = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(result);
        Integer code = (Integer) jsonObject.get("code");
        if(1000 == code){
            JSONArray dataArr = JsonUtil.toJSONArray(jsonObject.get("data"));
            for(int i=0;i<dataArr.size();i++) {
                String jsonKey = String.valueOf(dataArr.getJSONObject(i));
                CommitListVo commitListVo = (CommitListVo) JsonUtil.jsonToObject(jsonKey,CommitListVo.class);
                list.add(commitListVo);
            }
        }
        return list;
    }

    /**
     * @description:  问题issue查询
     * @param statusID 情形id
     * @param topNum 默认值：all
     * @author: wangyh
     * @Date: 2022/8/22 15:19
     **/
    public List<IssueVO> queryIssue(String statusID,String topNum) throws IOException {
        log.info("问题issue查询， statusID:{},topNum{}",statusID,topNum);
        if(StrUtil.isEmpty(statusID)){
            return null;
        }
        if(StrUtil.isEmpty(topNum)){
            topNum="all";
        }
        List<IssueVO> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("statusID",statusID);
        map.put("topNum",topNum);
        String json = JSON.toJSONString(map);
        String result = ClientServer.send(issueUrl,json,"utf-8");
        String jsonResult = "{"+"data"+":"+result+"}";
        JSONObject jsonObject = JSON.parseObject(jsonResult);
        JSONArray dataArr = JsonUtil.toJSONArray(jsonObject.get("data"));
        for(int i=0;i<dataArr.size();i++) {
            String jsonKey = String.valueOf(dataArr.getJSONObject(i));
            IssueVO issueVO = (IssueVO) JsonUtil.jsonToObject(jsonKey,IssueVO.class);
            list.add(issueVO);
        }
        return list;
    }

    /**
     * @description:  获取情形的办事指南
//     * @param token 登录token，放在headers中 其实可以不放
//     * @param rowguid 事项唯一编码（上海事项库rowguid）statusId，form-data
     * @author: wangyh
     * @Date: 2023/2/23 15:19
     **/
//    public Guide getShsxDetail(String token, String rowguid) throws IOException {
//        log.info("获取情形的办事指南， rowguid{}",rowguid);
//        Guide guide = new Guide();
//        if(StrUtil.isEmpty(rowguid)){
//            return null;
//        }
//        Map<String,Object> map = new HashMap<>();
//        map.put("Authorization",token);
//        map.put("rowguid",rowguid);
//        String result = HttpRequestUtil.sendPost(getShsxDetail, map);
//        String jsonResult = "{"+"data"+":"+result+"}";
//        JSONObject jsonObject = JSON.parseObject(jsonResult);
//        JSONObject data = (JSONObject) jsonObject.get("data");
//        String code = data.getString("code");
//        if(code.equals("1000")){
//            JSONObject dataObj = (JSONObject) data.get("data");
//            JSONObject customObj = (JSONObject) dataObj.get("custom");
//            JSONObject auditfwznextendObj = (JSONObject) customObj.get("auditfwznextend");
//            guide.setBljg(auditfwznextendObj.getString("bljg")); //实施主体
//            JSONObject auditfwznObj = (JSONObject) customObj.get("auditfwzn");
//            Short bjtype  =  Short.parseShort(auditfwznObj.getString("bjtype"));
//            guide.setBjtype(bjtype); //办件类型
//            guide.setAnticipateDay(Long.valueOf(auditfwznObj.getString("anticipate_day"))); //办件类型
//            guide.setPromiseDay(Long.valueOf(auditfwznObj.getString("promise_day"))); //承诺时限
//            guide.setCkjssj(auditfwznObj.getString("ckjssj")); //办理时间
//            guide.setLinkCk(auditfwznObj.getString("link_ck")); //窗口咨询
//            guide.setLinkWs(auditfwznObj.getString("link_ws")); //网上咨询
//            guide.setLinkXh(auditfwznObj.getString("link_xh")); //信函咨询
//            guide.setLinkTel(auditfwznObj.getString("link_tel")); //电话咨询(咨询方式)
//            guide.setLinkYj(auditfwznObj.getString("link_yj")); //电子邮件咨询
//        }
//        return guide;
//    }
    public static void main(String[] args) throws IOException {
        HaMatterDockManager haMatterDockManager = new HaMatterDockManager();
//        Guide g = haMatterDockManager.getShsxDetail("0c1ffceaf13070af437c34b08ac7d122","a7618de2-2759-4ef1-887b-11900bb74d68");
        String as = "";
//        String token = haMatterDockManager.getToken();
//        List<MatterListVo> as = haMatterDockManager.queryItemListWithDkb(token,null,null,null);
//        List<SituationListVo> assa = haMatterDockManager.queryStatusListWithDkb(token,"003a1d43-a53a-4aaf-9301-1ff8968e512f",null);
//        List<CommitListVo> assaa = haMatterDockManager.getCommit("1092807052105759b18f9c0ccc9b665f","053b25a8-a5ce-4b2f-83a6-c5006c95341c",null);
//        List<IssueVO> issueVOList = haMatterDockManager.queryIssue("003f0dca-393f-4537-a9b2-c3ab30bc4252","all");
//        String ass ="";
    }
}
