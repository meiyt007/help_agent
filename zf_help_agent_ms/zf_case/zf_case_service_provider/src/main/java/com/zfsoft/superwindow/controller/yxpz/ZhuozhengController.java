package com.zfsoft.superwindow.controller.yxpz;

import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.feign.SysDictFeignService;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.data.yxpz.SxDocuTemplate;
import com.zfsoft.superwindow.manager.yxpz.SxDocuTemplateManager;
import com.zfsoft.superwindow.service.yxpz.ZhuozhengService;
import com.zfsoft.superwindow.util.*;
import com.zhuozhengsoft.pageoffice.FileSaver;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*import com.zfsoft.feign.settings.QlCaseServiceFeginService;*/

/**
 * @ClassName SxDocuTemplateController
 * @Description 参数配置管理的实现类
 * @Author liangxm
 * @Date 2020-09-12 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class ZhuozhengController implements ZhuozhengService {

  /* @Resource
   private QlCaseServiceFeginService qlCaseServiceFeginService;*/

    @Resource
    private SxDocuTemplateManager sxDocuTemplateManager;

    @Resource
    private SysDictFeignService sysDictFeignService;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public ModelAndView showWord(Map<String,Object> map,String oid){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        SxDocuTemplate template = sxDocuTemplateManager.getSxDocuTemplateById(Long.valueOf(oid));
        ApiResultSet<SysDict> applicationResult = sysDictFeignService.getSysDictByDictOid(template.getApplicationType());
        ApiResultSet<SysDict> docuTypeResult = sysDictFeignService.getSysDictByDictOid(template.getDocuTypeOid());
        String appCode = applicationResult.getData().getCode();//应用类型
        String docTypeCode =docuTypeResult.getData().getCode();//通知书类型
        PageOfficeCtrl poCtrl=new PageOfficeCtrl(request);
        String url = request.getRequestURI().substring(0,request.getRequestURI().indexOf("/manage/zhuozheng"));
        poCtrl.setServerPage(request.getContextPath()+"/poserver.zz");//设置服务页面
        System.out.println("----------"+request.getContextPath()+"/poserver.zz");
        //poCtrl.setServerPage(request.getContextPath() + "/poserver.zz");
        poCtrl.addCustomToolButton("保存","Save",1);//添加自定义保存按钮
        //poCtrl.addCustomToolButton("盖章","AddSeal",2);//添加自定义盖章按钮
        poCtrl.setSaveFilePage(url+"/manage/zhuozheng/save");//设置处理文件保存的请求方法
        poCtrl.addCustomToolButton("打印", "ShowPrintDlg()", 1);
        poCtrl.setTitlebar(false); // 隐藏标题栏
        poCtrl.setMenubar(false); // 隐藏菜单栏
        //打开word
        try {
            String fileName = "";
            if(CommonUtil.isWindows()){
                File path = new File(ResourceUtils.getURL("classpath:").getPath().replace("!",""));
                fileName =path.getPath()+ "/templates/"+ DocuTemplatEnum.getTemplateFileName(appCode,docTypeCode)+".doc";
            }else{
                fileName="file:///soft/usr/local/zfsoft/zc/"+DocuTemplatEnum.getTemplateFileName(appCode,docTypeCode)+".doc";
            }

            poCtrl.webOpen(fileName, OpenModeType.docAdmin,"张三");
            map.put("pageoffice",poCtrl.getHtmlCode("PageOfficeCtrl1"));
            poCtrl.setTagId("PageOfficeCtrl1");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ModelAndView mv = new ModelAndView("Word");
        return mv;
    }


    @Override
    public ApiResultSet<String> getDocuTemplateFilePath(String appCode, String docTypeCode) {
        String fileName=null;
        try {
            if(CommonUtil.isWindows()){
                File path = new File(ResourceUtils.getURL("classpath:").getPath().replace("!",""));
                fileName =path.getPath()+ "/templates/"+DocuTemplatEnum.getTemplateFileName(appCode,docTypeCode)+".doc";
            }else{
                fileName="file:///soft/usr/local/zfsoft/zc/"+DocuTemplatEnum.getTemplateFileName(appCode,docTypeCode)+".doc";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ApiResultSet<String> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(fileName);
        return apiResultSet;
    }

    @Override
    public void saveFile(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String fileName=null;
        try {
            if(CommonUtil.isWindows()){
                File path = new File(ResourceUtils.getURL("classpath:").getPath().replace("!",""));
                fileName =path.getPath()+ "/templates/caseAccept.doc";
            }else{
                fileName="///soft/usr/local/zfsoft/zc/caseAccept.doc";
            }
            FileSaver fs = new FileSaver(request, response);
            fs.saveToFile(fileName);
            fs.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * dongxl
     * 打印材料流转单
     * @param request
     * @param caseOid
     * @param id
     * @param map
     * @return
     */
    @Override
    public ModelAndView initTickertape(HttpServletRequest request,String caseOid,Long id,Map<String,Object> map) {
        // 定义pageOffice组件
        // 是PageOffice开发平台中的核心类，用来在线打开、显示、编辑Office文档。
        ModelAndView modelAndView = new ModelAndView("InitTickertape");
       /* CurrentLoginUser loginUserInfo= CurrentLoginUserHolder.getCurrentLoginUser();
        try {
            PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
            poCtrl.setServerPage(request.getContextPath() + "/poserver.zz");
            poCtrl.addCustomToolButton("打印", "Print()", 1);
            poCtrl.setTitlebar(false); // 隐藏标题栏
            poCtrl.setMenubar(false); // 隐藏菜单栏
            poCtrl.setJsFunction_AfterDocumentOpened("AfterDocumentOpened");
//			poCtrl.setSaveFilePage("saveAsPDF.do?regOid=" + regOid);
            WordDocument doc = new WordDocument();
            if (!StrUtil.isEmpty(caseOid)) {
                String url = request.getRequestURI().substring(0,request.getRequestURI().indexOf("/manage/zhuozheng"));
                File path = new File(ResourceUtils.getURL("classpath:").getPath());
                Map<String, String> map1 = caseMaterialOutOfStockManager.getReplaceMap(caseOid, id, loginUserInfo);
                String strPath=path.getPath()+"/templates/材料流转通知单.doc";
                // 获得word书签集合
                Bookmarks bookmarks = getBookmarks(strPath);
                String bookmarkName = "";
                for (int i = 0, j = bookmarks.getBookmarksCount(); i < j; i++) {
                    bookmarkName = bookmarks.getBookmark(i).getName();
                    DataRegion dataRegion = doc.openDataRegion(bookmarkName);
                    dataRegion.setValue(null==map1.get(bookmarkName)?"":map1.get(bookmarkName));
                }
                poCtrl.setWriter(doc);
                poCtrl.webOpen(path+"/templates/材料流转通知单.doc", OpenModeType.docNormalEdit, "张三");
                //poCtrl.setTagId("PageOfficeCtrl1");
                map.put("pageoffice",poCtrl.getHtmlCode("PageOfficeCtrl1"));
                modelAndView.addObject("caseOid", caseOid);
                //modelAndView.addObject("userOid", loginUserInfo.getUserOid());
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/
        return modelAndView;
    }

    @Override
    public ModelAndView notAcceptedNoticePrint(String caseNumber, String applyUserName, String sqTime, Map<String, Object> map) {
        // 定义pageOffice组件
        // 是PageOffice开发平台中的核心类，用来在线打开、显示、编辑Office文档。
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
            poCtrl.setServerPage(request.getContextPath() + "/poserver.zz");
            poCtrl.addCustomToolButton("打印", "ShowPrintDlg()", 1);
            poCtrl.setTitlebar(false); // 隐藏标题栏
            poCtrl.setMenubar(false); // 隐藏菜单栏

            Map<String, String> pamamMap = new HashMap<String, String>();
            pamamMap.put("casenumber", caseNumber);
            pamamMap.put("username",URLDecoder.decode(applyUserName, "utf-8"));
            pamamMap.put("sqtime",sqTime);
            String templatePath =null;
            String outPath =null;
            if(CommonUtil.isWindows()){
                //resource文件夹路径
                File path = new File(ResourceUtils.getURL("classpath:").getPath());
                //模板文件路径
                templatePath =path.getPath()+ "/templates/notAccepted.doc";
                //替换成功报存的文件路径
                outPath =path.getPath()+ "/templates/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
            }else{
                //模板文件路径
                templatePath = "/soft/usr/local/zfsoft/zc/notAccepted.doc";
                //替换成功报存的文件路径
                outPath = "/soft/usr/local/zfsoft/zc/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
            }
            //返回替换成功的文件路径
            String printPath = ReadWordUtils.readWriteWord(templatePath, outPath, pamamMap);
            poCtrl.webOpen("file://"+printPath, OpenModeType.docNormalEdit, "张三");
            poCtrl.setTagId("PageOfficeCtrl1");
            System.out.println(poCtrl.getHtmlCode("PageOfficeCtrl1"));
            map.put("pageoffice",poCtrl.getHtmlCode("PageOfficeCtrl1"));
        }catch (Exception e){
            e.printStackTrace();
        }
        ModelAndView mv = new ModelAndView("Word");
        return mv;
    }

    @Override
    public ModelAndView rqbzAcceptedNoticePrint(String caseName, String applyUserName,String sqTime,Map<String, Object> map) {
        // 定义pageOffice组件
        // 是PageOffice开发平台中的核心类，用来在线打开、显示、编辑Office文档。
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
            poCtrl.setServerPage(request.getContextPath() + "/poserver.zz");
            poCtrl.addCustomToolButton("打印", "ShowPrintDlg()", 1);
            poCtrl.setTitlebar(false); // 隐藏标题栏
            poCtrl.setMenubar(false); // 隐藏菜单栏
            Map<String, String> pamamMap = new HashMap<String, String>();
            pamamMap.put("casename", URLDecoder.decode(caseName, "utf-8"));
            pamamMap.put("username",URLDecoder.decode(applyUserName, "utf-8"));
            pamamMap.put("sqtime",sqTime);
            String templatePath =null;
            String outPath =null;
            if(CommonUtil.isWindows()){
                //resource文件夹路径
                File path = new File(ResourceUtils.getURL("classpath:").getPath());
                //模板文件路径
                templatePath =path.getPath()+ "/templates/rqbzAccept.doc";
                //替换成功报存的文件路径
                outPath =path.getPath()+ "/templates/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
            }else{
                //模板文件路径
                templatePath = "/soft/usr/local/zfsoft/zc/rqbzAccept.doc";
                //替换成功报存的文件路径
                outPath = "/soft/usr/local/zfsoft/zc/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
            }
            //返回替换成功的文件路径
            String printPath = ReadWordUtils.readWriteWord(templatePath, outPath, pamamMap);
            poCtrl.webOpen("file://"+printPath, OpenModeType.docNormalEdit, "张三");
            poCtrl.setTagId("PageOfficeCtrl1");
            System.out.println(poCtrl.getHtmlCode("PageOfficeCtrl1"));
            map.put("pageoffice",poCtrl.getHtmlCode("PageOfficeCtrl1"));
        }catch (Exception e){
            e.printStackTrace();
        }
        ModelAndView mv = new ModelAndView("Word");
        return mv;
    }


    @Override
    public ModelAndView cngzNoticePrint(String caseName, String applyUserName, String sqTime, Map<String, Object> map) {
        // 定义pageOffice组件
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            PageOfficeCtrl poCtrl=new PageOfficeCtrl(request);
            poCtrl.setServerPage(request.getContextPath() + "/poserver.zz");
            poCtrl.addCustomToolButton("打印", "ShowPrintDlg()", 1);
            poCtrl.setTitlebar(false); // 隐藏标题栏
            poCtrl.setMenubar(false); // 隐藏菜单栏
            Map<String, String> pamamMap = new HashMap<String, String>();
            pamamMap.put("casename", URLDecoder.decode(caseName, "utf-8"));
            pamamMap.put("username",URLDecoder.decode(applyUserName, "utf-8"));
            pamamMap.put("sqtime",sqTime);
            String templatePath =null;
            String outPath =null;
            if(CommonUtil.isWindows()){
                //resource文件夹路径
                File path = new File(ResourceUtils.getURL("classpath:").getPath());
                //模板文件路径
                templatePath =path.getPath()+ "/templates/gzcnsAccept.doc";
                //替换成功报存的文件路径
                outPath =path.getPath()+ "/templates/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
            }else{
                //模板文件路径
                templatePath = "/soft/usr/local/zfsoft/zc/gzcnsAccept.doc";
                //替换成功报存的文件路径
                outPath = "/soft/usr/local/zfsoft/zc/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
            }
            //返回替换成功的文件路径
            String printPath = ReadWordUtils.readWriteWord(templatePath, outPath, pamamMap);
            poCtrl.webOpen("file://"+printPath, OpenModeType.docNormalEdit, "张三");
            poCtrl.setTagId("PageOfficeCtrl1");
            System.out.println(poCtrl.getHtmlCode("PageOfficeCtrl1"));
            map.put("pageoffice",poCtrl.getHtmlCode("PageOfficeCtrl1"));
        }catch (Exception e){
            e.printStackTrace();
        }
        ModelAndView mv = new ModelAndView("Word");
        return mv;
    }

    @Override
    public ModelAndView printComboCaseRqhbNotice(Map<String, Object> map,String rqhbTime, String sqrName, String caseName) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        PageOfficeCtrl poCtrl=new PageOfficeCtrl(request);
        String url = request.getRequestURI().substring(0,request.getRequestURI().indexOf("/manage/zhuozheng"));
        poCtrl.setServerPage(request.getContextPath()+"/poserver.zz");//设置服务页面
        poCtrl.addCustomToolButton("打印", "ShowPrintDlg()", 1);
        poCtrl.setTitlebar(false); // 隐藏标题栏
        poCtrl.setMenubar(false); // 隐藏菜单栏

        //打开word9
        try {
            Map<String, String> pamamMap = new HashMap<String, String>();
            pamamMap.put("sqrname", URLDecoder.decode(sqrName, "utf-8"));
            pamamMap.put("casename",URLDecoder.decode(caseName, "utf-8"));
            pamamMap.put("rqhbtime",rqhbTime);
            String templatePath =null;
            String outPath =null;
            if(CommonUtil.isWindows()){
                //resource文件夹路径
                File path = new File(ResourceUtils.getURL("classpath:").getPath());
                //模板文件路径
                templatePath =path.getPath()+ "/templates/onethingRqbzAccept.doc";
                //替换成功报存的文件路径
                outPath =path.getPath()+ "/templates/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
            }else{
                //模板文件路径
                templatePath = "/soft/usr/local/zfsoft/zc/onethingRqbzAccept.doc";
                //替换成功报存的文件路径
                outPath = "/soft/usr/local/zfsoft/zc/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
            }
            //返回替换成功的文件路径
            String printPath = ReadWordUtils.readWriteWord(templatePath, outPath, pamamMap);
            poCtrl.webOpen("file://"+printPath, OpenModeType.docAdmin,"张三");
            poCtrl.setTagId("PageOfficeCtrl1");
            map.put("pageoffice",poCtrl.getHtmlCode("PageOfficeCtrl1"));
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ModelAndView mv = new ModelAndView("Word");
        return mv;
    }

    @Override
    public ModelAndView printComboCaseNotAcceptNotice(Map<String, Object> map, String caseNumber, String sqrName, String sqTime) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        PageOfficeCtrl poCtrl=new PageOfficeCtrl(request);
        String url = request.getRequestURI().substring(0,request.getRequestURI().indexOf("/manage/zhuozheng"));
        poCtrl.setServerPage(request.getContextPath()+"/poserver.zz");//设置服务页面
        poCtrl.addCustomToolButton("打印", "ShowPrintDlg()", 1);
        poCtrl.setTitlebar(false); // 隐藏标题栏
        poCtrl.setMenubar(false); // 隐藏菜单栏

        //打开word
        try {
            Map<String, String> pamamMap = new HashMap<String, String>();
            pamamMap.put("sqrname",URLDecoder.decode(sqrName, "utf-8"));
            pamamMap.put("casenumber",caseNumber);
            pamamMap.put("sqtime",sqTime);
            String templatePath =null;
            String outPath =null;
            if(CommonUtil.isWindows()){
                //resource文件夹路径
                File path = new File(ResourceUtils.getURL("classpath:").getPath());
                //模板文件路径
                templatePath =path.getPath()+ "/templates/onethingNotAccept.doc";
                //替换成功报存的文件路径
                outPath =path.getPath()+ "/templates/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
            }else{
                //模板文件路径
                templatePath = "/soft/usr/local/zfsoft/zc/onethingNotAccept.doc";
                //替换成功报存的文件路径
                outPath = "/soft/usr/local/zfsoft/zc/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
            }
            //返回替换成功的文件路径
            String printPath = ReadWordUtils.readWriteWord(templatePath, outPath, pamamMap);
            poCtrl.webOpen("file://"+printPath, OpenModeType.docAdmin,"张三");
            poCtrl.setTagId("PageOfficeCtrl1");
            map.put("pageoffice",poCtrl.getHtmlCode("PageOfficeCtrl1"));
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ModelAndView mv = new ModelAndView("Word");
        return mv;
    }

    @Override
    public ModelAndView printGrdzqybg(Map<String,Object> map,String caseOid) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        PageOfficeCtrl poCtrl=new PageOfficeCtrl(request);
        String url = request.getRequestURI().substring(0,request.getRequestURI().indexOf("/manage/zhuozheng"));
        poCtrl.setServerPage(request.getContextPath()+"/poserver.zz");//设置服务页面
        poCtrl.addCustomToolButton("打印", "ShowPrintDlg()", 1);
        poCtrl.setTitlebar(false); // 隐藏标题栏
        poCtrl.setMenubar(false); // 隐藏菜单栏
        //打开word
        try {
            Map<String, String> pamamMap = new HashMap<String, String>();
            if(StringUtils.isNotEmpty(caseOid)){
                Map<String, Object> mapres =(Map<String, Object>) redisTemplate.opsForValue().get(caseOid);
                pamamMap.put("companyName",URLDecoder.decode(mapres.get("companyName")==null?"":mapres.get("companyName").toString(), "utf-8"));
                pamamMap.put("socialCreditCode",mapres.get("socialCreditCode")==null?"":mapres.get("socialCreditCode").toString());
                pamamMap.put("mobile",mapres.get("mobile")==null?"":mapres.get("mobile").toString());
                pamamMap.put("code",mapres.get("code")==null?"":mapres.get("code").toString());
                pamamMap.put("contactUserName",mapres.get("contactUserName")==null?"":mapres.get("contactUserName").toString());
                pamamMap.put("contactCredentialType",mapres.get("contactCredentialType")==null?"":mapres.get("contactCredentialType").toString());
                pamamMap.put("contactCredentialNumber",mapres.get("contactCredentialNumber")==null?"":mapres.get("contactCredentialNumber").toString());
                pamamMap.put("contactMobile",mapres.get("contactMobile")==null?"":mapres.get("contactMobile").toString());
                pamamMap.put("contactPhone",mapres.get("contactPhone")==null?"":mapres.get("contactPhone").toString());
                if(mapres.get("checkAuthority")!=null){
                    if(mapres.get("checkAuthority").toString().equals("1")){
                        pamamMap.put("1checkAuthority","√");
                        pamamMap.put("0checkAuthority","");
                    }else{
                        pamamMap.put("1checkAuthority","");
                        pamamMap.put("0checkAuthority","√");
                    }
                }
                if(mapres.get("updateErrorFile")!=null){
                    if(mapres.get("updateErrorFile").toString().equals("1")){
                        pamamMap.put("1updateErrorFile","√");
                        pamamMap.put("0updateErrorFile","");
                    }else{
                        pamamMap.put("1updateErrorFile","");
                        pamamMap.put("0updateErrorFile","√");
                    }
                }
                if(mapres.get("updateTableFile")!=null){
                    if(mapres.get("updateTableFile").toString().equals("1")){
                        pamamMap.put("1updateTableFile","√");
                        pamamMap.put("0updateTableFile","");
                    }else{
                        pamamMap.put("1updateTableFile","");
                        pamamMap.put("0updateTableFile","√");
                    }
                }
                if(mapres.get("receiveAuthority")!=null){
                    if(mapres.get("receiveAuthority").toString().equals("1")){
                        pamamMap.put("1receiveAuthority","√");
                        pamamMap.put("0receiveAuthority","");
                    }else{
                        pamamMap.put("1receiveAuthority","");
                        pamamMap.put("0receiveAuthority","√");
                    }
                }
                if(mapres.get("bglist")!=null){
                    ArrayList list= (ArrayList) mapres.get("bglist");
                    if(list!=null && list.size()>0){
                        for(int i=0;i<list.size();i++){
                           JSONObject json= JSONObject.parseObject(list.get(i).toString());
                            pamamMap.put("$"+i+"serviceName$",json.get("serviceName")==null?"":json.get("serviceName").toString());
                            pamamMap.put("$"+i+"oldService$",json.get("oldService")==null?"":json.get("oldService").toString());
                            pamamMap.put("$"+i+"newService$",json.get("newService")==null?"":json.get("newService").toString());
                        }
                    }
                }
            }
            String templatePath =null;
            String outPath =null;
            if(CommonUtil.isWindows()){
                //resource文件夹路径
                File path = new File(ResourceUtils.getURL("classpath:").getPath());
                //模板文件路径
                templatePath =path.getPath()+ "/templates/grdzqybg.doc";
                //替换成功报存的文件路径
                outPath =path.getPath()+ "/templates/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
            }else{
                //模板文件路径
                templatePath = "/soft/usr/local/zfsoft/zc/grdzqybg.doc";
                //替换成功报存的文件路径
                outPath = "/soft/usr/local/zfsoft/zc/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
            }
            //返回替换成功的文件路径
            String printPath = ReadWordUtils.readWriteWord(templatePath, outPath, pamamMap);
            poCtrl.webOpen("file://"+printPath, OpenModeType.docAdmin,"张三");
            poCtrl.setTagId("PageOfficeCtrl1");
            map.put("pageoffice",poCtrl.getHtmlCode("PageOfficeCtrl1"));
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ModelAndView mv = new ModelAndView("Word");
        return mv;
    }

    @Override
    public ModelAndView acceptedNoticePrint(String caseNumber, String applyUserName, String sqTime, Map<String, Object> map) {
        // 定义pageOffice组件
        // 是PageOffice开发平台中的核心类，用来在线打开、显示、编辑Office文档。
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
            poCtrl.setServerPage(request.getContextPath() + "/poserver.zz");
            poCtrl.addCustomToolButton("打印", "ShowPrintDlg()", 1);
            poCtrl.setTitlebar(false); // 隐藏标题栏
            poCtrl.setMenubar(false); // 隐藏菜单栏

            Map<String, String> pamamMap = new HashMap<String, String>();
            pamamMap.put("casenumber", caseNumber);
            pamamMap.put("username",URLDecoder.decode(applyUserName, "utf-8"));
            pamamMap.put("sqtime",sqTime);
            String templatePath =null;
            String outPath =null;
            if(CommonUtil.isWindows()){
                //resource文件夹路径
                File path = new File(ResourceUtils.getURL("classpath:").getPath());
                //模板文件路径
                templatePath =path.getPath()+ "/templates/acceptNotice.doc";
                //替换成功报存的文件路径
                outPath =path.getPath()+ "/templates/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
            }else{
                //模板文件路径
                templatePath = "/soft/usr/local/zfsoft/zc/acceptNotice.doc";
                //替换成功报存的文件路径
                outPath = "/soft/usr/local/zfsoft/zc/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
            }
            //返回替换成功的文件路径
            String printPath = ReadWordUtils.readWriteWord(templatePath, outPath, pamamMap);
            poCtrl.webOpen("file://"+printPath, OpenModeType.docNormalEdit, "张三");
            poCtrl.setTagId("PageOfficeCtrl1");
            System.out.println(poCtrl.getHtmlCode("PageOfficeCtrl1"));
            map.put("pageoffice",poCtrl.getHtmlCode("PageOfficeCtrl1"));
        }catch (Exception e){
            e.printStackTrace();
        }
        ModelAndView mv = new ModelAndView("Word");
        return mv;
    }

    @Override
    public ModelAndView pageOfficePrint(String fileUrl, Map<String, Object> map) {
        // 定义pageOffice组件
        // 是PageOffice开发平台中的核心类，用来在线打开、显示、编辑Office文档。
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            //fileUrl="D:\\templates\\notice\\2022\\2\\21\\1645427758661.docx";
            PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
            poCtrl.setServerPage(request.getContextPath() + "/poserver.zz");
            poCtrl.addCustomToolButton("打印", "ShowPrintDlg()", 1);
            poCtrl.setTitlebar(false); // 隐藏标题栏
            poCtrl.setMenubar(false); // 隐藏菜单栏
            poCtrl.webOpen("file://"+fileUrl, OpenModeType.docNormalEdit, "张三");
            poCtrl.setTagId("PageOfficeCtrl1");
            System.out.println(poCtrl.getHtmlCode("PageOfficeCtrl1"));
            map.put("pageoffice",poCtrl.getHtmlCode("PageOfficeCtrl1"));
        }catch (Exception e){
            e.printStackTrace();
        }
        ModelAndView mv = new ModelAndView("Word");
        return mv;
    }


    /**
     * 添加PageOffice的服务器端授权程序Servlet（必须）
     * @return
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        com.zhuozhengsoft.pageoffice.poserver.Server poserver = new com.zhuozhengsoft.pageoffice.poserver.Server();
        ServletRegistrationBean srb = new ServletRegistrationBean(poserver);
        try {
//            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            File path = new File(System.getProperty("java.io.tmpdir"));
            poserver.setSysPath(path.getPath());//设置PageOffice注册成功后,license.lic文件存放的目录

            srb.addUrlMappings("/poserver.zz");
            srb.addUrlMappings("/posetup.exe");
            srb.addUrlMappings("/pageoffice.js");
            srb.addUrlMappings("/jquery.min.js");
            srb.addUrlMappings("/pobstyle.css");
            srb.addUrlMappings("/sealsetup.exe");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return srb;
    }

    /**
     * 添加印章管理程序Servlet（可选）
     * @return
     *//*
    @Bean
    public ServletRegistrationBean servletRegistrationBean2() {
        com.zhuozhengsoft.pageoffice.poserver.AdminSeal adminSeal = new com.zhuozhengsoft.pageoffice.poserver.AdminSeal();
        adminSeal.setAdminPassword(poPassWord);//设置印章管理员admin的登录密码
        ServletRegistrationBean srb = new ServletRegistrationBean(adminSeal);
        try {
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            adminSeal.setSysPath(path.getPath());//设置印章数据库文件poseal.db存放的目录
            srb = new ServletRegistrationBean(adminSeal);
            srb.addUrlMappings("/adminseal.zz");
            srb.addUrlMappings("/sealimage.zz");
            srb.addUrlMappings("/loginseal.zz");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return srb;
    }*/

}
