package com.zfsoft.ha.managers;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.cases.feign.SysOrganFeginService;
import com.zfsoft.cases.manager.SysAttaManager;
import com.zfsoft.ha.dbaccess.dao.DbXdServiceMapper;
import com.zfsoft.ha.dbaccess.data.DbXdService;
import com.zfsoft.ha.util.HttpComponentUtil;
import com.zfsoft.ha.util.SecurityUtil;
import com.zfsoft.ha.util.StringUtil;
import com.zfsoft.ha.xindianResponse.*;
import com.zfsoft.microservice.platform.data.vo.SysOrganVo;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceExtendMapper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceMapper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceMaterialMapper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceQuestionMapper;
import com.zfsoft.service.dbaccess.dao.sxSys.DbSxSysAttaMapper;
import com.zfsoft.service.dbaccess.data.sxService.*;
import com.zfsoft.service.manager.sxSys.SxSysAttaManager;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.service.sxService.data.SxServiceExtend;
import com.zfsoft.service.sxService.data.SxServiceMaterial;
import com.zfsoft.service.sxService.data.SxServiceMaterialEmptyAtta;
import com.zfsoft.service.sxSys.data.SxSysAtta;
import com.zfsoft.service.util.office.FileUtil;
import com.zfsoft.single.util.HttpRequestUtil;
import com.zfsoft.single.util.JsonUtil;
import com.zfsoft.superwindow.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description //新点事项库同步
 * @Author: Wangyh
 * @Date: 2023/1/28 13:30
 */
@Service
@Slf4j
public class HaSynServiceXdManager {
    private final static String app_id = "76d94237-0722-4354-88a3-8cbf128ca033";
    private final static String accesstoken = "36fe2574-ddb6-4911-9958-00d840ff47bc";
    private final static String app_key = "1d22f1e9-5c96-41ba-9546-6db2ff846ec2";
    //请求办事指南列表数据地址
    private static String getfwznList="http://10.81.69.239:80/clientgateway/";
    //请求办事指南列表数据地址
    private static String getFwznData="http://10.81.69.239:80/clientgateway/";
    //请求组织机构数据
    private static String getOuData="http://10.81.69.239:80/clientgateway/";
    //附件下载
    private static String getFile="http://10.81.69.239:80/clientgateway/";
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

    @Resource
    private SxSysAttaManager sxSysAttaManager;
    @Resource
    private DbXdServiceMapper dbXdServiceMapper;
    @Resource
    private DbSxSysAttaMapper dbSxSysAttaMapper;
    /**
     * redis
     */
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 上传附件信息实现类
     */
    @Resource
    private  SysAttaManager sysAttaManager;

    /**
     * 补缺万达无法导入的数据
     */
    public void repairServiceInfo(HttpServletRequest request) throws Exception {

        ServicerGuideVo servicerGuideVos = this.getfwznListTets("8a2bedee-3b50-4a50-8e8e-2a098941e978");
          //先查出新点事项库与万达事项库的交集事项、交集数据，取新点 事项主键，33位事项编码 本地库的事项主键
          List<DbXdService> serviceList = dbXdServiceMapper.queryXdZfService();
          //再根据新点事项主键去查，新点办事指.南。同时根据卓繁事项主键，set新点获取到的办事指南信息到库里
//          ServicerGuideVo servicerGuideVo = this.getfwznList("059d039b-687c-4474-ac54-53aad6c0689b");
//          ((HaSynServiceXdManager) AopContext.currentProxy()).completionServiceInfo(servicerGuideVo,"3ccb0c64-6505-47c1-a530-1eca4d146402",request);
          for(int i=0;i<serviceList.size();i++){
              DbXdService dbXdService = serviceList.get(i);
              //获取新点办事指南
              ServicerGuideVo servicerGuideVo = this.getfwznList(dbXdService.getRowguid());
             ((HaSynServiceXdManager) AopContext.currentProxy()).completionServiceInfo(servicerGuideVo,dbXdService.getServiceOid(),request);
             log.info("i:{}, rowguid:{}",i,dbXdService.getRowguid());
              System.out.println("rowguid"+dbXdService.getRowguid());
          }
    }
    /**
     * 新点事项库 补全之前万达无法提供的事项信息
     * @param servicerGuideVo
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public void  completionServiceInfo(ServicerGuideVo servicerGuideVo,String serviceOid ,HttpServletRequest request) throws Exception {
        log.info("补全单个事项方法");
        //根据事项id查询事项信息
        DbSxService service=dbSxServiceMapper.getDbSxServiceByServiceOid(serviceOid);
        if(service !=null && com.zfsoft.cases.util.StringUtil.isNotEmpty(service.getIsShield()) && service.getIsShield().equals("0")){ //判断事项是否需要屏蔽导入 是否屏蔽(0-未屏蔽、1-屏蔽)
            DbSxServiceWithBLOBs record=new DbSxServiceWithBLOBs();
            StringBuffer stringbuffer = new StringBuffer();
            /**
             * 新点主要导入的字段
             */
            service.setHostOffices(servicerGuideVo.getBljg()); //实施主体
            service.setCaseType(servicerGuideVo.getBjtype());//办件类型
            service.setZxCkText(servicerGuideVo.getLinkCk());//窗口咨询
            service.setZxDhText(servicerGuideVo.getLinkTel());//电话咨询
            if(!StringUtil.isBlank(servicerGuideVo.getLinkTel())){
                stringbuffer.append("4");
            }
            service.setZxWlText(servicerGuideVo.getLinkWs());//网络咨询
            if(!StringUtil.isBlank(servicerGuideVo.getLinkWs())){
                stringbuffer.append(",1");
            }
            if(!StringUtil.isBlank(servicerGuideVo.getLinkWs())){
                stringbuffer.append(",0");

            }
            service.setZxXjText(servicerGuideVo.getLinkXh());//信件咨询方式
            if(!StringUtil.isBlank(servicerGuideVo.getLinkXh())){
                stringbuffer.append(",3");
            }
            service.setZxYjText(servicerGuideVo.getLinkYj());//电子邮件咨询
            if(!StringUtil.isBlank(servicerGuideVo.getLinkYj())){
                stringbuffer.append(",2");
            }
            service.setZxType(stringbuffer.toString());
//            service.setServiceTypeOid(servicerGuideVo.getQlkind());//事项类型 这个字段是需要的,但是因为我原先系统只有行政许可这一个选项，所有在不做系统修改前。无法机芯导入
            //数据库后来新增字段 end
            BeanUtil.copyProperties(service,record);
            record.setModifyDate(new Date());
            dbSxServiceMapper.updateByPrimaryKeyWithBLOBs(record);
            //2.扩展信息
            DbSxServiceExtendWithBLOBs extend = dbSxServiceExtendMapper.getSxServiceExtendByServiceOid(service.getServiceOid());
            /**
             * 新点主要导入的字段
             */
            extend.setLegalLimit(servicerGuideVo.getAnticipateDay()); //法定时限
            extend.setPromiseLimit(servicerGuideVo.getPromiseDay());//承诺时限
            extend.setHanleTimeRange(servicerGuideVo.getCkjssj());//办理时间
            //办理流程图相关信息
            List<FlowChartVo> flow_chart = servicerGuideVo.getFlow_chart();
            String handleFlow =null;
            for(int i = 0;i<flow_chart.size();i++){
                FlowChartVo flowChartVo = flow_chart.get(i);
                cn.hutool.json.JSONObject jsonObject = this.getFile(request,flowChartVo.getAttachguid(),flowChartVo.getAttachfilename(),flowChartVo.getContenttype());
                String url = (String) jsonObject.get("url");
                handleFlow = url;
                log.info("流程图fastdfs地址, url:{}",url);
                SxSysAtta sxSysAtta = new SxSysAtta();
                sxSysAtta.setOid(flowChartVo.getAttachguid());
                sxSysAtta.setName(flowChartVo.getAttachfilename());
                sxSysAtta.setOriginName(flowChartVo.getAttachfilename());
                sxSysAtta.setFilePath(url);
                SxSysAtta sxSysAttas = sxSysAttaManager.saveAtta(sxSysAtta);
            }
            extend.setHandleFlow(handleFlow);
            dbSxServiceExtendMapper.updateByPrimaryKeySelective(extend);
            this.completionMatter(servicerGuideVo,service.getServiceOid(),request);
        }
    }

    /**
     * 新点事项库 补全之前万达无法提供的事项信息 具体业务方法
     */
    public void completionMatter(ServicerGuideVo servicerGuideVo,String serviceOid,HttpServletRequest request) throws Exception {
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        /**
         * 导入材料之前，先删掉材料
         */
        DbSxServiceMaterialExample dbSxServiceMaterialExample = new DbSxServiceMaterialExample();
        DbSxServiceMaterialExample.Criteria criteria = dbSxServiceMaterialExample.createCriteria();
        if (StrUtil.isNotEmpty(serviceOid)) {
            criteria.andServiceOidEqualTo(serviceOid);
        }
        List<DbSxServiceMaterial> wdMaterialList = dbSxServiceMaterialMapper.selectByExample(dbSxServiceMaterialExample);
        for(int i=0;i<wdMaterialList.size();i++){
            DbSxServiceMaterial dbSxServiceMaterial = wdMaterialList.get(i);

            if(!StringUtil.isBlank(dbSxServiceMaterial.getMaterialEmptyAddr())){
                //删除空表
                String[] empty = dbSxServiceMaterial.getMaterialEmptyAddr().split(",");
                for(String a : empty){
                    if(org.apache.commons.lang.StringUtils.isNotEmpty(a)){
//                        SxSysAtta sxSysAtta = sxSysAttaManager.getSxSysAttaByOid(a);
                        int index1 = dbSxSysAttaMapper.deleteByOid(a);
                    }
                }
            }
            if(!StringUtil.isBlank(dbSxServiceMaterial.getMaterialSampleAddr())){
                //删除样表
                String[] sample = dbSxServiceMaterial.getMaterialSampleAddr().split(",");
                for(String s : sample){
                    if(org.apache.commons.lang.StringUtils.isNotEmpty(s)){
                        int index1 = dbSxSysAttaMapper.deleteByOid(s);
                    }
                }
            }
            int index = dbSxServiceMaterialMapper.deleteByPrimaryKey(dbSxServiceMaterial.getId());

        }
        //导入事项关联的材料信息
        List<MatterVo> matterVoList = servicerGuideVo.getMatterVoList();
        for(int k=0;k<matterVoList.size();k++){
            //获取材料对象信息
            MatterVo matterVo = matterVoList.get(k);
            //声明对象材料对象
            SxServiceMaterial sxServiceMaterial = new SxServiceMaterial();
            sxServiceMaterial.setMaterialOid(matterVo.getMaterialid());
            sxServiceMaterial.setServiceOid(serviceOid);
            sxServiceMaterial.setMaterialName(matterVo.getMaterialname());//材料名称
            if(StrUtil.isNotEmpty(matterVo.getPage_num())){
                sxServiceMaterial.setPaperNumber(Long.valueOf(matterVo.getPage_num())); //纸质材料份数
            }
            sxServiceMaterial.setMaterialSource(matterVo.getFile_source()); //来源渠道 0申请人自备 1政府部门核发 2其它
            sxServiceMaterial.setOtherMaterialSource(matterVo.getFile_source_explain());//其他来源渠道
            sxServiceMaterial.setMaterialType(matterVo.getMaterialformat());//材料类型

            if(StrUtil.isNotEmpty(matterVo.getPage_num())){
                sxServiceMaterial.setNmOriginal(matterVo.getPage_num()); //原件数量,默认1
            }
            sxServiceMaterial.setNmCopy("1"); //复印件数量,默认1
            sxServiceMaterial.setIsScene("0");//返回的事项库名称是【一业一证库】表示是主题事项,其他的不是主题事项因为我们是黄埔的,所以默认不是
            sxServiceMaterial.setStuffStatus("0");//取值范围：0为首次提交、2为补充材料
            //材料是否必选，新点的是字符串，且没有示例，暂时不传
            if(matterVo.getMaterialnecessity().equals("1")){
                sxServiceMaterial.setMustFlag((short) 0);
            }else{
                sxServiceMaterial.setMustFlag((short) 1);
            }
            //备注
            sxServiceMaterial.setRemark(matterVo.getBaknote());
            //样表操作
            MaterialexampletableVo materialexampletable = matterVo.getMaterialexampletable();
            if(materialexampletable!=null){
                cn.hutool.json.JSONObject jsonObject = this.getFile(request,materialexampletable.getAttachguid(),materialexampletable.getAttachfilename(),materialexampletable.getContenttype());
                log.info("样表操作jsonObject, jsonObject:{}",jsonObject.toString());
                //获取fastdfs地址
                String url = (String) jsonObject.get("url");
                sxServiceMaterial.setMaterialSampleAddrYl(url);
                sxServiceMaterial.setMaterialSampleAddr(materialexampletable.getAttachguid());
                sxServiceMaterial.setMaterialSampleName(materialexampletable.getAttachfilename());
                SxSysAtta sxSysAtta = new SxSysAtta();
                sxSysAtta.setOid(materialexampletable.getAttachguid());
                sxSysAtta.setName(materialexampletable.getAttachfilename());
                sxSysAtta.setOriginName(materialexampletable.getAttachfilename());
                sxSysAtta.setFilePath(url);
                SxSysAtta sxSysAttas = sxSysAttaManager.saveAtta(sxSysAtta);
            }
            //空表操作
            MaterialemptytableVo materialemptytable = matterVo.getMaterialemptytable();
            if(materialemptytable !=null){
                cn.hutool.json.JSONObject jsonObject = this.getFile(request,materialemptytable.getAttachguid(),materialemptytable.getAttachfilename(),materialemptytable.getContenttype());
                log.info("空表jsonObject, jsonObject:{}",jsonObject.toString());
                sxServiceMaterial.setMaterialEmptyAddr(materialemptytable.getAttachguid());
                sxServiceMaterial.setMaterialEmptyOriginName(materialemptytable.getAttachfilename());
                //获取fastdfs地址
                SxSysAtta sxSysAtta = new SxSysAtta();
                sxSysAtta.setOid(materialemptytable.getAttachguid());
                sxSysAtta.setName(materialemptytable.getAttachfilename());
                String url = (String) jsonObject.get("url");
                sxSysAtta.setFilePath(url);
                sxSysAtta.setExtensionName(materialemptytable.getContenttype());
                sxSysAtta.setOriginName(materialemptytable.getAttachfilename());
                SxSysAtta sxSysAttas = sxSysAttaManager.saveAtta(sxSysAtta);
            }
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

            DbSxServiceMaterialWithBLOBs condition= dbSxServiceMaterialMapper.getSxServiceMaterialByOid(sxServiceMaterial.getMaterialOid());
            if(condition ==null){
                int index =  dbSxServiceMaterialMapper.insertSelective(MaterialRecord);
            }
        }
    }



















    /**
     * 新点事项库同步
     */
    public void ServiceInfoSyn(HttpServletRequest request) throws Exception {
       log.info("进入新点事项库同步方法");
        /**
         * 因为新点的事项库接口需要一次只能返回50条数据，共有一万四千多条，所以需要循环调用
         */
        Boolean fla = true;
        while (fla){
            //获取上一次的时间戳
            String fromtimestamp = (String) redisTemplate.opsForValue().get("fromtimestamp");
            //1获取这一次50个事项ID
            Map<String,Object> map = this.getFwznData(fromtimestamp);
            String time = (String) map.get("lastTimestamp");
            Integer sumNum = (Integer) map.get("sumNum");
            if(fromtimestamp!=null && sumNum ==0){
                fla =false;
                break;
            }
            //1.获取事项列表
            List<String> listRowguid = (List<String>) map.get("listRowguid");
            //通过AopContext获取当前代理类
            ((HaSynServiceXdManager) AopContext.currentProxy()).insertServiceInfo(listRowguid,request);
            //设置key为永不过时，只替代
            redisTemplate.opsForValue().set("fromtimestamp" , time);

        }


    }

    /**
     * 导入新点事项 50条导入一次
     * @param listRowguid
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public void insertServiceInfo (List<String>  listRowguid ,HttpServletRequest request ) throws Exception {
        for(int j =0;j<listRowguid.size();j++){
            // 测试事项主键
            String rowguid = listRowguid.get(j);
            //2.获取事项办事指南
            ServicerGuideVo servicerGuideVo = this.getfwznList(listRowguid.get(j));
            //以下是测试代码
            DbXdService dbXdService = new DbXdService();
            dbXdService.setRowguid(servicerGuideVo.getRowguid());
            dbXdService.setItemId(servicerGuideVo.getItemId());
            dbXdService.setQlName(servicerGuideVo.getQlName());
            dbXdService.setItemCode(servicerGuideVo.getItemCode());
            dbXdService.setOrgCode(servicerGuideVo.getOrgCode());
            int index = dbXdServiceMapper.insertSelective(dbXdService);
            /**
             * 先测试新点和万达事项是否对的上
             */
            //3.根据事项插入事项所属组织机构相关信息
            /*OrganizationVo organizationVo = this.getOuData(servicerGuideVo.getOuguid());
            servicerGuideVo.setOrganOid(organizationVo.getSh_code());
            servicerGuideVo.setOrganName(organizationVo.getOuname());
            Map<String, Object> map1 = this.updateSyOrgan(organizationVo);
            //4.同步新增事项
            Map<String, Object> map2 = this.SynDateServiceVo(servicerGuideVo,request);*/
        }
    }
    /**
     * 同步新点单个事项
     * @param servicerGuideVo
     * @return
     * @throws Exception
     */
    public Map<String, Object>  SynDateServiceVo(ServicerGuideVo servicerGuideVo ,HttpServletRequest request) throws Exception {
        log.info("同步新点单个事项");
        //1.事项表,获取事项基本信息
        Integer size = 0;
        //根据事项id查询事项信息
        DbSxService service=dbSxServiceMapper.getDbSxServiceByServiceOid(servicerGuideVo.getOuguid());
        if(service !=null && com.zfsoft.cases.util.StringUtil.isNotEmpty(service.getIsShield()) && service.getIsShield().equals("1")){ //判断事项是否需要屏蔽导入 1-屏蔽 0 -不需要屏蔽
            System.out.println("serviceoid size= "+size++);
        }else{
            DbSxServiceWithBLOBs record=new DbSxServiceWithBLOBs();
            //基础数据塞入
            SxService sxService = new SxService();
            sxService.setExistChildItem("0");
            sxService.setServiceStatus((short) 3);
            sxService.setDelFlag((short) 0);
            sxService.setOrganOid(servicerGuideVo.getOrganOid()); //机构id
            sxService.setOrganName(servicerGuideVo.getOrganName()); //机构名称
            /**
             * 新点主要导入的字段
             */
            sxService.setHostOffices(servicerGuideVo.getBljg()); //实施主体
            sxService.setCaseType(servicerGuideVo.getBjtype());//办件类型
            sxService.setZxCkText(servicerGuideVo.getLinkCk());//窗口咨询
            sxService.setZxWlText(servicerGuideVo.getLinkWs());//网络咨询
            sxService.setZxXjText(servicerGuideVo.getLinkXh());//信件咨询方式
            sxService.setZxDhText(servicerGuideVo.getLinkTel());//电话咨询
            sxService.setZxYjText(servicerGuideVo.getLinkYj());//电子邮件咨询
            sxService.setServiceTypeOid(servicerGuideVo.getQlkind());//事项类型
            //判断更新保存
            if(service !=null && !service.equals("")){//更新
                service.setOrganOid(servicerGuideVo.getOrganOid()); //机构id
            //this.updataMatter(service,situationListVo,record,token, matterListVo/*,guide*/);
            }else {//新增
                sxService.setOrganOid(servicerGuideVo.getOrganOid()); //机构id
                this.insertMatter(sxService,servicerGuideVo,record,request);
            }
        }
        return  null;
    }


    /**
     * 根据事项保存事项组织机构表
     * @param organizationVo
     * @return
     * @throws Exception
     */
    public Map<String, Object>  updateSyOrgan(OrganizationVo organizationVo) throws Exception {
        log.info("请求组织机构数据");
        //1.插入组织机构表数据
        SysOrganVo sysOrganVo = new SysOrganVo();
        sysOrganVo.setOrganOid(organizationVo.getSh_code()); //上海部门编码
        sysOrganVo.setName(organizationVo.getOushortname()); // 简称
        sysOrganVo.setDistrictOid("4028545d665734290166b02711c20073");//所属区划主键，默认值
        sysOrganVo.setFullName(organizationVo.getOuname());//组织机构全名称
        //新点有的万达没有的数据
        sysOrganVo.setCode(organizationVo.getAreacode()); //取的是新点areacode字段值 区划编码
        sysOrganVo.setDeptCode(organizationVo.getOucode());
        sysOrganVo.setParentOid(organizationVo.getParentouguid());//上级组织机构OID 取新点父部门标识
        sysOrganVo.setUniteCode(organizationVo.getOrg_code());
        Map<String, Object>  apiResultSet = sysOrganFeginService.saveSysOrganSyn(sysOrganVo);
        return  apiResultSet;
    }
    /**
     * 请求组织机构数据
     * @param ouguid
     * @return
     * @throws Exception
     */
    public OrganizationVo getOuData(String ouguid) throws Exception {
        log.info("请求组织机构数据");
        OrganizationVo organizationVo = new OrganizationVo();
        String apiname = "0e5d6379-8497-455c-8354-7711e496a2ce";//固定值 应用调用api的APIID 每个接口都有一个
        //构造请求头
        Map<String, Object> headersMap = getHeaders(apiname);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accesstoken", accesstoken);
        jsonObject.put("ouguid", ouguid);
        String result  = HttpComponentUtil.httpPostJsonStr(getOuData, headersMap,jsonObject.toJSONString());
        if (!StringUtil.isBlank(result)) {
            JSONObject resultObject = JSONObject.parseObject(result);
            JSONObject status = (JSONObject) resultObject.get("status");
            String code = status.getString("code");
            if(status.getString("code").equals("success")){
                JSONObject custom = (JSONObject) resultObject.get("custom");
                JSONObject auditorg = (JSONObject) custom.get("auditorg");
                organizationVo = JsonUtil.toBean(auditorg, OrganizationVo.class);
            }
        }
        return organizationVo;
    }

    /**
     * @description:  请求办事指南单条数据
     * @param rowguid "rowguid":"事项主键(必填)"
     * @author: wangyh
     * @Date: 2023/2/01 15:19
     **/
    public ServicerGuideVo getfwznListTets(String rowguid) throws Exception {
        log.info("请求办事指南单条数据getfwznListTets， rowguid{}",rowguid);
        String apiname = "d1f15aa3-6cf1-4ae2-8e94-03847b008ad6";//固定值 应用调用api的APIID 每个接口都有一个
        //构造请求头
        Map<String, Object> headersMap = new HashMap<>();
        headersMap.put("apiname","d1f15aa3-6cf1-4ae2-8e94-03847b008ad6");
        headersMap.put("signature","jjQcd9oJvrEKV3a3127BngSbo+sGiJdoRP0cHwPvNgrW0tH80fwapQ/hQeZhhxdhURpABlMresLSkeoi54XBvzu8XsS6xxwLoz2r68IJ6e6UAi9b4tcHn204N0QaZP7P");
        headersMap.put("appid","76d94237-0722-4354-88a3-8cbf128ca033");
        log.info("headersMap:{}",headersMap);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accesstoken", "36fe2574-ddb6-4911-9958-00d840ff47bc");
        jsonObject.put("rowguid", rowguid);
        log.info("jsonObject:{}",jsonObject.toString());
        log.info("getfwznList:{}",getfwznList);
        String result = HttpComponentUtil.httpPostJsonStr(getfwznList, headersMap, jsonObject.toJSONString());
        log.info("result=====getfwznListTets,result:{}",result);
        ServicerGuideVo servicerGuideVo = new ServicerGuideVo();
        if (!StringUtil.isBlank(result)) {
            JSONObject resultObject = JSONObject.parseObject(result);
            JSONObject status = (JSONObject) resultObject.get("status");
            String code = status.getString("code");
            log.info("code=====ssss,code:{}",code);
            log.info("code=====",code);
            if(status.getString("code").equals("success")){
                JSONObject custom = (JSONObject) resultObject.get("custom");
                JSONObject auditfwzn= (JSONObject) custom.get("auditfwzn");
                servicerGuideVo.setRowguid(auditfwzn.getString("rowguid"));//事项ID
                servicerGuideVo.setQlName(auditfwzn.getString("ql_name"));//事项名称
                if(!StringUtils.isEmpty(auditfwzn.getString("bjtype"))){
                    servicerGuideVo.setBjtype(Short.valueOf(auditfwzn.getString("bjtype")));//办件类型
                }
                if(StringUtils.isNumber(auditfwzn.getString("anticipate_day"))){
                    servicerGuideVo.setAnticipateDay(Long.valueOf(auditfwzn.getString("anticipate_day")));//法定时限
                }
                if(StringUtils.isNumber(auditfwzn.getString("promise_day"))){
                    servicerGuideVo.setPromiseDay(Long.valueOf(auditfwzn.getString("promise_day")));//承诺时限
                }
                servicerGuideVo.setCkjssj(auditfwzn.getString("ckjssj"));//办理时间
                servicerGuideVo.setLinkCk(auditfwzn.getString("link_ck"));//窗口咨询
                servicerGuideVo.setLinkWs(auditfwzn.getString("link_ws"));//网上咨询
                servicerGuideVo.setLinkXh(auditfwzn.getString("link_xh"));//信函咨询
                servicerGuideVo.setLinkTel(auditfwzn.getString("link_tel"));//电话咨询(咨询方式)
                servicerGuideVo.setLinkYj(auditfwzn.getString("link_yj"));//电子邮件咨询
                servicerGuideVo.setQlkind(auditfwzn.getString("ql_kind"));//事项类型
                servicerGuideVo.setOuguid(auditfwzn.getString("ouguid")); //部门唯一标识
                servicerGuideVo.setItemCode(auditfwzn.getString("item_code")); //实施编码
                servicerGuideVo.setCatalogCode(auditfwzn.getString("catalog_code")); //12位事项编码
                servicerGuideVo.setItemId(auditfwzn.getString("item_id")); //事项版本唯一值
                servicerGuideVo.setOrgCode(auditfwzn.getString("org_code")); //实施主体编码
                servicerGuideVo.setOrdernum(auditfwzn.getString("ordernumber")); //排序号
                JSONObject auditfwznextend= (JSONObject) custom.get("auditfwznextend");
                servicerGuideVo.setBljg(auditfwznextend.getString("bljg")); //实施主体
                //流程图相关信息
                JSONArray flow_chart= (JSONArray) auditfwzn.get("flow_chart");
                List<FlowChartVo> flowChartVoList = new ArrayList<>();
                FlowChartVo flowChartVo = null;
                for(int c=0;c <flow_chart.size();c++){
                    flowChartVo = JsonUtil.toBean(flow_chart.get(c), FlowChartVo.class);
                    flowChartVoList.add(flowChartVo);
                }
                servicerGuideVo.setFlow_chart(flowChartVoList);
                //材料数据，一个事项可能有多份材料
                JSONArray auditfwznmaterial= (JSONArray) custom.get("auditfwznmaterial");
                List<MatterVo> matterVoList = new ArrayList<>();
                for(int i=0;i<auditfwznmaterial.size();i++){
                    JSONObject jsonObject1 = (JSONObject) auditfwznmaterial.get(i);
                    MatterVo matterVo = new MatterVo();
                    matterVo.setRowguid(jsonObject1.getString("rowguid"));
                    matterVo.setItemguid (jsonObject1.getString("itemguid"));
                    matterVo.setMaterialid (jsonObject1.getString("materialid"));
                    matterVo.setMaterialname(jsonObject1.getString("materialname"));
                    matterVo.setMaterialnecessity(jsonObject1.getString("materialnecessity"));
                    if(StrUtil.isNotEmpty(jsonObject1.getString("materialnecessity"))&& jsonObject1.getString("materialnecessity").equals("1")){
                        matterVo.setMaterialformat((short) 0);
                    }else{
                        matterVo.setMaterialformat((short) 1);
                    }
                    if(StrUtil.isNotEmpty(jsonObject1.getString("file_source"))&& jsonObject1.getString("file_source").equals("1")){
                        matterVo.setFile_source((short) 0);
                    }else if(StrUtil.isNotEmpty(jsonObject1.getString("file_source"))&& jsonObject1.getString("file_source").equals("2")){
                        matterVo.setFile_source((short)1);
                    }else if(StrUtil.isNotEmpty(jsonObject1.getString("file_source"))&& jsonObject1.getString("file_source").equals("3")){
                        matterVo.setFile_source((short)2);
                    }
                    matterVo.setSubmittype(jsonObject1.getString("submittype"));
                    matterVo.setFile_source_explain(jsonObject1.getString("file_source_explain"));
                    matterVo.setPage_num(jsonObject1.getString("page_num"));
                    matterVo.setBaknote(jsonObject1.getString("baknote"));
                    matterVo.setExemption_form(jsonObject1.getString("exemption_form"));
                    matterVo.setExemption_form_explain(jsonObject1.getString("exemption_form_explain"));
                    matterVo.setIsconfirm(jsonObject1.getString("isconfirm"));
                    matterVo.setOrdernumber(jsonObject1.getString("ordernumber"));
                    matterVo.setBy_law(jsonObject1.getString("by_law"));
                    JSONArray auditfwznmaterialArr= (JSONArray)jsonObject1.get("materialemptytable");
                    MaterialemptytableVo materialemptytableVo =null;
                    for(int a=0;a<auditfwznmaterialArr.size();a++){
                        materialemptytableVo = JsonUtil.toBean(auditfwznmaterialArr.get(a), MaterialemptytableVo.class);
                    }
                    JSONArray materialexampletableArr= (JSONArray)jsonObject1.get("materialexampletable");
                    MaterialexampletableVo materialexampletable =null;
                    for (int b=0;b<materialexampletableArr.size();b++){
                        materialexampletable = JsonUtil.toBean(materialexampletableArr.get(b), MaterialexampletableVo.class);
                    }
                    matterVo.setMaterialemptytable(materialemptytableVo);
                    matterVo.setMaterialexampletable(materialexampletable);
                    matterVoList.add(matterVo);

                }
                servicerGuideVo.setMatterVoList(matterVoList);
            }
        }
        return servicerGuideVo;
    }
    /**
     * @description:  请求办事指南单条数据
     * @param rowguid "rowguid":"事项主键(必填)"
     * @author: wangyh
     * @Date: 2023/2/01 15:19
     **/
    public ServicerGuideVo getfwznList(String rowguid) throws Exception {
        log.info("请求办事指南单条数据， rowguid{}",rowguid);
        String apiname = "d1f15aa3-6cf1-4ae2-8e94-03847b008ad6";//固定值 应用调用api的APIID 每个接口都有一个
        //构造请求头
        Map<String, Object> headersMap = getHeaders(apiname);
        log.info("headersMap:{}",headersMap);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accesstoken", accesstoken);
        jsonObject.put("rowguid", rowguid);
        log.info("jsonObject:{}",jsonObject.toString());
        log.info("getfwznList:{}",getfwznList);
        String result = HttpComponentUtil.httpPostJsonStr(getfwznList, headersMap, jsonObject.toJSONString());
        log.info("result=====ssss,result:{}",result);
        log.info("result=====",result);
        ServicerGuideVo servicerGuideVo = new ServicerGuideVo();
        if (!StringUtil.isBlank(result)) {
            JSONObject resultObject = JSONObject.parseObject(result);
            JSONObject status = (JSONObject) resultObject.get("status");
            String code = status.getString("code");
            log.info("code=====ssss,code:{}",code);
            log.info("code=====",code);
            if(status.getString("code").equals("success")){
                JSONObject custom = (JSONObject) resultObject.get("custom");
                JSONObject auditfwzn= (JSONObject) custom.get("auditfwzn");
                servicerGuideVo.setRowguid(auditfwzn.getString("rowguid"));//事项ID
                servicerGuideVo.setQlName(auditfwzn.getString("ql_name"));//事项名称
                if(!StringUtils.isEmpty(auditfwzn.getString("bjtype"))){
                    servicerGuideVo.setBjtype(Short.valueOf(auditfwzn.getString("bjtype")));//办件类型
                }
                if(StringUtils.isNumber(auditfwzn.getString("anticipate_day"))){
                    servicerGuideVo.setAnticipateDay(Long.valueOf(auditfwzn.getString("anticipate_day")));//法定时限
                }
                if(StringUtils.isNumber(auditfwzn.getString("promise_day"))){
                    servicerGuideVo.setPromiseDay(Long.valueOf(auditfwzn.getString("promise_day")));//承诺时限
                }
                servicerGuideVo.setCkjssj(auditfwzn.getString("ckjssj"));//办理时间
                servicerGuideVo.setLinkCk(auditfwzn.getString("link_ck"));//窗口咨询
                servicerGuideVo.setLinkWs(auditfwzn.getString("link_ws"));//网上咨询
                servicerGuideVo.setLinkXh(auditfwzn.getString("link_xh"));//信函咨询
                servicerGuideVo.setLinkTel(auditfwzn.getString("link_tel"));//电话咨询(咨询方式)
                servicerGuideVo.setLinkYj(auditfwzn.getString("link_yj"));//电子邮件咨询
                servicerGuideVo.setQlkind(auditfwzn.getString("ql_kind"));//事项类型
                servicerGuideVo.setOuguid(auditfwzn.getString("ouguid")); //部门唯一标识
                servicerGuideVo.setItemCode(auditfwzn.getString("item_code")); //实施编码
                servicerGuideVo.setCatalogCode(auditfwzn.getString("catalog_code")); //12位事项编码
                servicerGuideVo.setItemId(auditfwzn.getString("item_id")); //事项版本唯一值
                servicerGuideVo.setOrgCode(auditfwzn.getString("org_code")); //实施主体编码
                servicerGuideVo.setOrdernum(auditfwzn.getString("ordernumber")); //排序号
                JSONObject auditfwznextend= (JSONObject) custom.get("auditfwznextend");
                servicerGuideVo.setBljg(auditfwznextend.getString("bljg")); //实施主体
                //流程图相关信息
                JSONArray flow_chart= (JSONArray) auditfwzn.get("flow_chart");
                List<FlowChartVo> flowChartVoList = new ArrayList<>();
                FlowChartVo flowChartVo = null;
                for(int c=0;c <flow_chart.size();c++){
                    flowChartVo = JsonUtil.toBean(flow_chart.get(c), FlowChartVo.class);
                    flowChartVoList.add(flowChartVo);
                }
                servicerGuideVo.setFlow_chart(flowChartVoList);
                //材料数据，一个事项可能有多份材料
                JSONArray auditfwznmaterial= (JSONArray) custom.get("auditfwznmaterial");
                List<MatterVo> matterVoList = new ArrayList<>();
                for(int i=0;i<auditfwznmaterial.size();i++){
                    JSONObject jsonObject1 = (JSONObject) auditfwznmaterial.get(i);
                    MatterVo matterVo = new MatterVo();
                    matterVo.setRowguid(jsonObject1.getString("rowguid"));
                    matterVo.setItemguid (jsonObject1.getString("itemguid"));
                    matterVo.setMaterialid (jsonObject1.getString("materialid"));
                    matterVo.setMaterialname(jsonObject1.getString("materialname"));
                    matterVo.setMaterialnecessity(jsonObject1.getString("materialnecessity"));
                    if(StrUtil.isNotEmpty(jsonObject1.getString("materialnecessity"))&& jsonObject1.getString("materialnecessity").equals("1")){
                        matterVo.setMaterialformat((short) 0);
                    }else{
                        matterVo.setMaterialformat((short) 1);
                    }
                    if(StrUtil.isNotEmpty(jsonObject1.getString("file_source"))&& jsonObject1.getString("file_source").equals("1")){
                        matterVo.setFile_source((short) 0);
                    }else if(StrUtil.isNotEmpty(jsonObject1.getString("file_source"))&& jsonObject1.getString("file_source").equals("2")){
                        matterVo.setFile_source((short)1);
                    }else if(StrUtil.isNotEmpty(jsonObject1.getString("file_source"))&& jsonObject1.getString("file_source").equals("3")){
                        matterVo.setFile_source((short)2);
                    }
                    matterVo.setSubmittype(jsonObject1.getString("submittype"));
                    if(StrUtil.isNotEmpty(jsonObject1.getString("file_source_explain"))){
                        matterVo.setFile_source_explain(jsonObject1.getString("file_source_explain"));
                    }
                    if(StrUtil.isNotEmpty(jsonObject1.getString("page_num"))){
                        matterVo.setPage_num(jsonObject1.getString("page_num"));
                    }
                    matterVo.setBaknote(jsonObject1.getString("baknote"));
                    matterVo.setExemption_form(jsonObject1.getString("exemption_form"));
                    matterVo.setExemption_form_explain(jsonObject1.getString("exemption_form_explain"));
                    matterVo.setIsconfirm(jsonObject1.getString("isconfirm"));
                    matterVo.setOrdernumber(jsonObject1.getString("ordernumber"));
                    matterVo.setBy_law(jsonObject1.getString("by_law"));
                    JSONArray auditfwznmaterialArr= (JSONArray)jsonObject1.get("materialemptytable");
                    MaterialemptytableVo materialemptytableVo =null;
                    for(int a=0;a<auditfwznmaterialArr.size();a++){
                         materialemptytableVo = JsonUtil.toBean(auditfwznmaterialArr.get(a), MaterialemptytableVo.class);
                    }
                    JSONArray materialexampletableArr= (JSONArray)jsonObject1.get("materialexampletable");
                    MaterialexampletableVo materialexampletable =null;
                    for (int b=0;b<materialexampletableArr.size();b++){
                         materialexampletable = JsonUtil.toBean(materialexampletableArr.get(b), MaterialexampletableVo.class);
                    }
                    matterVo.setMaterialemptytable(materialemptytableVo);
                    matterVo.setMaterialexampletable(materialexampletable);
                    matterVoList.add(matterVo);

                }
                servicerGuideVo.setMatterVoList(matterVoList);
            }
        }
        return servicerGuideVo;
    }

    /**
     * @description:  请求办事指南列表数据
     * @param "accesstoken":"访问令牌需要向审改办申请，验证成功后，返回数据(必填)",
     * @param "qlkind":"事项类型",
     * @param "fromtimestamp":"上次返回结果集最新一条记录的版本时间+“_”+记录序列号（yyyy-MM-dd HH:mm:ss + _ +数字），如果传值为空，默认返回所有已发布的最新版本记录，如果不为空，返回事项版本发布日期在传值时间及序列号之后的最新版本记录",
     * @param "areacode":"区划编码",
     * @param "orgcode":"统一社会信用代码",
     * @param "businesscode":"条线编码",
     * @param "limitnum":"返回的数据量。至多50，超过50则为50，不填"
     * @author: wangyh
     * @Date: 2023/2/01 15:19
     **/
    public Map<String,Object> getFwznData(String fromtimestamp) throws Exception {
        log.info("获取事项库的事项列表");
        Map<String,Object> map = new HashMap<>();
        String apiname = "3d45bc55-b6cd-4dad-8372-f4cf62869541";//固定值 应用调用api的APIID 每个接口都有一个
        //构造请求头
        Map<String, Object> headersMap = getHeaders(apiname);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accesstoken", accesstoken);
        jsonObject.put("fromtimestamp", fromtimestamp);
        String result = HttpComponentUtil.httpPostJsonStr(getfwznList, headersMap, jsonObject.toJSONString());
        List<String> list = new ArrayList<>();
        if (!StringUtil.isBlank(result)) {
            JSONObject resultObject = JSONObject.parseObject(result);
            JSONObject status = (JSONObject) resultObject.get("status");
            if(status.getString("code").equals("success")){
                JSONObject custom = (JSONObject) resultObject.get("custom");
                String lastTimestamp = custom.getString("lastTimestamp");
                Integer sumNum = (Integer) custom.get("sumNum");
                map.put("lastTimestamp",lastTimestamp);
                map.put("sumNum",sumNum);
                JSONArray fwznList = JsonUtil.toJSONArray(custom.get("fwznList"));
                for(int i=0;i<fwznList.size();i++) {
                    Map<String,Object> map1 = fwznList.getJSONObject(i);
                    String rowguid = (String) map1.get("rowguid");
                    list.add(rowguid);
                }
                map.put("listRowguid",list);
            }
        }
        return map;
    }

    /**
     * @description:  获取文件信息方法
     * @param "accesstoken":"访问令牌需要向审改办申请，验证成功后，返回数据(必填)",
     * @param "qlkind":"事项类型",
     * @param "fromtimestamp":"上次返回结果集最新一条记录的版本时间+“_”+记录序列号（yyyy-MM-dd HH:mm:ss + _ +数字），如果传值为空，默认返回所有已发布的最新版本记录，如果不为空，返回事项版本发布日期在传值时间及序列号之后的最新版本记录",
     * @param "areacode":"区划编码",
     * @param "orgcode":"统一社会信用代码",
     * @param "businesscode":"条线编码",
     * @param "limitnum":"返回的数据量。至多50，超过50则为50，不填"
     * @author: wangyh
     * @Date: 2023/2/01 15:19
     **/
    public cn.hutool.json.JSONObject getFile(HttpServletRequest request,String attachGuid,String fileName,String contenttype ) throws Exception {
        log.info("获取文件信息方法");
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        String apiname = "fb73be10-4f99-4aa0-9ef3-ea46de633ff0";//固定值 应用调用api的APIID 每个接口都有一个
        //构造请求头
        Map<String, Object> headersMap = getHeaders(apiname);
        Map<String, Object> params = new HashMap<>();
        params.put("isCommondto", "true");
        params.put("attachGuid", attachGuid);
        //1.截取掉文件的后缀名
        String fileNames =null;
        if(fileName.contains(".")){
            int str =fileName.indexOf(".");
            fileNames = fileName.substring(0,str);
        }
        //2.创建文件存储路径
        String filePath = this.getPath();
        String result = HttpComponentUtil.httpPostJsonFile(getFile, params,headersMap,filePath,fileNames,contenttype );
        cn.hutool.json.JSONObject jsonObject =  this.getFastdfs(request,fileNames+contenttype,result,loginUser);
        //删除创建的文件夹
//        FileUtil.deleteDir(filePath);
        return jsonObject;
    }

    /**
     * 获取文件存储地址
     * @return
     */
    public String getPath (){
        //获取当前项目目录
        String path = System.getProperty("user.dir");
        log.info("获取当前项目目录, path:{}",path);
      String pathZip = path +"/materialPage/";  //liunx环境
        //本地windows环境测试的时候释放下面的代码
        //   String pathZip = path + "\\zf_help_agent_ms\\zf_case\\zf_case_service_provider\\src\\main\\java\\com\\zfsoft\\materialPage\\";
        log.info("pathZip:{}",pathZip);
        FileUtil.createDir2(pathZip);
        return  pathZip;
    }
    /**
     * 新增事项相关信息
     */
    public void insertMatter(SxService sxService,ServicerGuideVo servicerGuideVo,DbSxServiceWithBLOBs record,HttpServletRequest request) throws IOException {
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        //1.基础信息 将万达的事项id作为我们的业务主键
        sxService.setServiceOid(servicerGuideVo.getRowguid());
        //新增事项名称
        sxService.setServiceName(servicerGuideVo.getQlName());
        //实施编码
        sxService.setImplementCode(servicerGuideVo.getItemCode());
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
        sxService.setItemId(servicerGuideVo.getItemId()); //事项唯一编码
        sxService.setWdOrder(servicerGuideVo.getOrdernum());  //排序
        sxService.setItemNo(servicerGuideVo.getCodeG()); //事项基本编码（12位，上海事项库catalog_code）
        sxService.setStateNo(servicerGuideVo.getItemcodeG());//情形编码
        sxService.setIsScene("0");//返回的事项库名称是【一业一证库】表示是主题事项,其他的不是主题事项因为我们是黄埔的,所以默认不是
        //数据库后来新增字段 end
        BeanUtil.copyProperties(sxService,record);
        dbSxServiceMapper.insertSelective(record);
        //2.扩展信息
        DbSxServiceExtendWithBLOBs extend= new DbSxServiceExtendWithBLOBs();
        SxServiceExtend sxExtend= new SxServiceExtend();
        sxExtend.setExtendOid(UUID.randomUUID().toString().replace("-", ""));
        sxExtend.setServiceOid(servicerGuideVo.getRowguid());
        /**
         * 新点主要导入的字段
         */
        sxExtend.setLegalLimit(servicerGuideVo.getAnticipateDay()); //法定时限
        sxExtend.setPromiseLimit(servicerGuideVo.getPromiseDay());//承诺时限
        sxExtend.setHanleTimeRange(servicerGuideVo.getCkjssj());//办理时间

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
        List<MatterVo> matterVoList = servicerGuideVo.getMatterVoList();
        for(int k=0;k<matterVoList.size();k++){
            //获取材料对象信息
            MatterVo matterVo = matterVoList.get(k);
            //声明对象材料对象
            SxServiceMaterial sxServiceMaterial = new SxServiceMaterial();
            sxServiceMaterial.setMaterialOid(matterVo.getMaterialid());
            sxServiceMaterial.setServiceOid(matterVo.getRowguid());
            //材料名称
            sxServiceMaterial.setMaterialName(matterVo.getMaterialname());
            //新增字段start
            sxServiceMaterial.setNmOriginal(matterVo.getPage_num()); //原件数量,默认1
            sxServiceMaterial.setNmCopy("1"); //复印件数量,默认1
            sxServiceMaterial.setIsScene("0");//返回的事项库名称是【一业一证库】表示是主题事项,其他的不是主题事项因为我们是黄埔的,所以默认不是
            sxServiceMaterial.setStuffStatus("0");//取值范围：0为首次提交、2为补充材料
            //新增字段end
            //材料是否必选，新点的是字符串，且没有示例，暂时不传
            if(matterVo.getMaterialnecessity().equals("1")){
                sxServiceMaterial.setMustFlag((short) 0);
            }else{
                sxServiceMaterial.setMustFlag((short) 1);
            }
            //备注
            sxServiceMaterial.setRemark(matterVo.getBaknote());
            //样表操作
            MaterialexampletableVo materialexampletable = matterVo.getMaterialexampletable();



            sxServiceMaterial.setMaterialSampleAddrYl(materialexampletable.getUrl());
            sxServiceMaterial.setMaterialSampleName(materialexampletable.getAttachfilename());
            //空表操作
            MaterialemptytableVo materialemptytable = matterVo.getMaterialemptytable();
            sxServiceMaterial.setMaterialEmptyAddr(materialemptytable.getAttachguid());




            SxSysAtta sxSysAtta = new SxSysAtta();
            sxSysAtta.setOid(materialemptytable.getAttachguid());
            sxSysAtta.setName(materialemptytable.getAttachfilename());
            sxSysAtta.setFilePath(materialemptytable.getUrl());
            SxSysAtta sxSysAttas = sxSysAttaManager.saveAtta(sxSysAtta);

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

    }


    /**
     * 上传文件并返回fastdfs地址
     * @param request
     * @param fileName 文件名称
     * @param filePath 文件存储路径
     * @return
     */
    public cn.hutool.json.JSONObject getFastdfs(HttpServletRequest request,String fileName ,String filePath,CurrentLoginUser loginUser){
        MultipartFile multipartFile = FileUtils.tranInputStream(filePath,fileName,fileName);
        //删除文件夹
        cn.hutool.json.JSONObject  jsonObject =  this.uploadFile(request,multipartFile,loginUser);
//        FileUtils.deleteFile();
        return  jsonObject;
    }

    /**
     * 上传fastdfs
     */
    public  cn.hutool.json.JSONObject uploadFile(HttpServletRequest request, MultipartFile file,CurrentLoginUser loginUser){
        cn.hutool.json.JSONObject jsonObject = null;
        if (!file.isEmpty()) {
            try {
                UploadUtil uploadUtil = new UploadUtil(request);
                String filePath = uploadUtil.uploadFile(file);
                if(filePath==null) {
                }
                String userOid ="";
                if(loginUser==null){
                }else{
                    userOid = String.valueOf(loginUser.getUserOid());
                }
                SysAttaTemp sysAttaFile = uploadUtil.getSysAttaFile(filePath,userOid);
                QlSysAtta sysAtta = new QlSysAtta();
                BeanUtils.copyProperties(sysAttaFile,sysAtta);
                QlSysAtta atta = sysAttaManager.saveSysAtta(sysAtta);
                jsonObject = JSONUtil.createObj().set("oid", atta.getAttaOid()).set("url", atta.getFastdfsNginxUrl()).set("name", atta.getOriginName());
            }catch (Exception e){
                log.error("文件上传：",e);
            }
        }
        return jsonObject;
    }

    /**
     * 获取支撑平台请求头数据
     * @param apiName 服务api_id
     */
    public static Map<String,Object> getHeaders(String apiName) throws Exception {
        Map<String,Object> headers = new HashMap<>();
        String signature = SecurityUtil.getSignature(app_id, app_key, apiName);
        String encryptSignature = SecurityUtil.encryptSignature(app_key, signature);
        System.out.println("encryptSignature======="+encryptSignature.replaceAll("\r|\n", ""));
        log.info("encryptSignature:{}",encryptSignature.replaceAll("\r|\n", ""));
        headers.put("signature",encryptSignature);
        headers.put("apiname", apiName);
        headers.put("appid", app_id);
        return headers;
    }
    public static void main(String[] args) throws Exception {
        HaSynServiceXdManager sa = new HaSynServiceXdManager();
//        ServicerGuideVo servicerGuideVos = sa.getfwznListTets("8a2bedee-3b50-4a50-8e8e-2a098941e978");
        ServicerGuideVo as =  sa.getfwznList("8a2bedee-3b50-4a50-8e8e-2a098941e978");
//        sa.getFile("授权委托书（通用）样表.docx",".docx");
        String ass = "";
    }

}
