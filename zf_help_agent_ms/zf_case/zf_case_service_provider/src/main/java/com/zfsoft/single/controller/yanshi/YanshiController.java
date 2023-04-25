package com.zfsoft.single.controller.yanshi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.cases.service.QlCaseMaterialService;
import com.zfsoft.cases.service.QlCaseService;
import com.zfsoft.cases.service.SysAttaService;
import com.zfsoft.microservice.platform.data.sys.SysAtta;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.rest.pojo.robot.RobotDoorCloseRequest;
import com.zfsoft.rest.pojo.robot.RobotDoorCloseResponse;
import com.zfsoft.rest.pojo.robot.RobotDoorOpenRequest;
import com.zfsoft.rest.pojo.robot.RobotDoorOpenResponse;
import com.zfsoft.rest.service.robot.IRobotRestService;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.single.data.fzgl.CaseLicenseManage;
import com.zfsoft.single.data.ywbl.QlCaseCorrection;
import com.zfsoft.single.manager.fzgl.CaseLincenseManageManager;
import com.zfsoft.single.manager.ywbl.CaseMaterialOutOfStockManager;
import com.zfsoft.single.manager.ywbl.QlCaseCorrectionManager;
import com.zfsoft.single.manager.ywbl.WindowAcceptanceManager;
import com.zfsoft.single.manager.yxpz.SxServiceRegistrarManager;
import com.zfsoft.single.service.yanshi.YanshiService;
import com.zfsoft.single.util.WordPrintTemplate;
import com.zfsoft.single.util.poi.PoiTlUtils;
import com.zfsoft.superwindow.service.sign.CaseSignRecordService;
import com.zfsoft.superwindow.util.*;
import com.zfsoft.superwindow.util.fa.AiTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@Slf4j
public class YanshiController implements YanshiService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AiTokenUtil aiTokenUtil;

    @Resource
    private IRobotRestService robotRestService;

    @Resource
    private QlCaseService qlCaseServiceFeginService;

    @Resource
    private WindowAcceptanceManager windowAcceptanceManager;

    @Resource
    private QlCaseCorrectionManager qlCaseCorrectionManager;

    @Resource
    private CaseMaterialOutOfStockManager caseMaterialOutOfStockManager;

    @Resource
    private CaseLincenseManageManager caseLincenseManageManager;

    @Resource
    private SysAttaService qlSysAttaFeignService;

    @Resource
    private SxServiceRegistrarManager sxServiceRegistrarManager;

    @Resource
    private QlCaseMaterialService qlCaseMaterialServiceFeginService;

    @Resource
    private CaseSignRecordService caseSignRecordFeginService;

    @Resource
    private com.zfsoft.microservice.platform.service.sys.SysAttaService sysAttaFeginService;


    @Override
    public ApiResultSet<Map<String, Object>> saveOrUpdateInfo(Object object) {
        Map<String, Object> res=new HashMap<>();
        if(object!=null){
           JSONObject jsonObj= JSONObject.parseObject(JSONArray.toJSON(object).toString());
            Map<String, Object> map=jsonObj;
           String caseOid= map.get("caseOid").toString();
            Map<String, Object> message = (Map<String, Object>) redisTemplate.opsForValue().get(caseOid);
            if(message!=null){
                redisTemplate.delete(caseOid);
            }
            redisTemplate.opsForValue().set(caseOid,object);
            res.put("status","true");
        }else{
            res.put("status","false");
        }
        return new ApiResultSet<>(res);
    }

    @Override
    public ApiResultSet<Map<String, Object>> getCaseFormInfo(String caseOid) {
        Map<String, Object> message=new HashMap<>();
        if(StringUtils.isNotEmpty(caseOid)){
             message = (Map<String, Object>) redisTemplate.opsForValue().get(caseOid);
        }
        return new ApiResultSet<>(message);
    }

    @Override
    public ApiResultSet openRabot() {
        RobotDoorOpenRequest openRequest= aiTokenUtil.getTokenRequest(RobotDoorOpenRequest.class);
        openRequest.setProductId("HOTQY00SZ201802050905041");
        RobotDoorOpenResponse res=robotRestService.doorOpen(openRequest);
        return new ApiResultSet(res);
    }

    @Override
    public ApiResultSet colseRabot() {
        RobotDoorCloseRequest closeRequest= aiTokenUtil.getTokenRequest(RobotDoorCloseRequest.class);
        closeRequest.setProductId("HOTQY00SZ201802050905041");
        RobotDoorCloseResponse res=robotRestService.doorClose(closeRequest);
        return new ApiResultSet(res);
    }

    @Override
    public ApiResultSet acceptedPrint(Object object) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            JSONObject jsonobj=JSONObject.parseObject(JSONArray.toJSON(object).toString());
            Map<String, Object> map = jsonobj;
            Map<String, String> pamamMap=new HashMap<>();
            for(String key:map.keySet()){
                pamamMap.put(key, map.get(key)==null?"":map.get(key).toString());
            }
            String templatePath =null;
            String outPath =null;
            if(CommonUtil.isWindows()){
                //resource文件夹路径
                File path = new File(ResourceUtils.getURL("classpath:").getPath());
                //模板文件路径
                //templatePath =path.getPath()+ "/templates/"+pamamMap.get("fileName");//acceptNotice.doc
                //给一个固定的地址临时使用
                templatePath="D://"+"templates/"+pamamMap.get("fileName");//acceptNotice.doc
                //替换成功报存的文件路径
                //outPath =path.getPath()+ "/templates/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
                //给一个固定的地址临时使用
                outPath ="D://"+ "templates/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
                log.info("templatePath1="+templatePath);
            }else{
                //模板文件路径
                templatePath = "/soft/usr/local/zfsoft/zc/"+pamamMap.get("fileName");
                //替换成功报存的文件路径
                outPath = "/soft/usr/local/zfsoft/zc/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
            }
            log.info("templatePath2="+templatePath);
            //返回替换成功的文件路径
            String printPath = ReadWordUtils.readWriteWord(templatePath, outPath, pamamMap);
            File pdfFile = new File(printPath);
            FileInputStream fileInputStream = new FileInputStream(pdfFile);
            MultipartFile multipartFile = new MockMultipartFile(pdfFile.getName(), pdfFile.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
            QlSysAtta sys=uploadCaseMaterialFile(request,multipartFile);
            System.out.println(sys.toString());
            return  new ApiResultSet(sys.getFastdfsNginxUrl());
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    public QlSysAtta uploadCaseMaterialFile(HttpServletRequest request, MultipartFile file) throws Exception {
        List<QlSysAtta> attaList=new ArrayList<QlSysAtta>();
        //登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        //判断file数组不能为空并且长度大于0
            UploadUtil uploadUtil = new UploadUtil(request);
            String filePath = null;
         if (!file.isEmpty()) {
            try {
                // 上传并返回新文件名称
                if(file.getOriginalFilename()!=null){
                    file = new MockMultipartFile("File",file.getOriginalFilename(),"text/plain", file.getInputStream());
                }
                filePath = uploadUtil.uploadFile(file);
                SysAttaTemp sysAttaFile = uploadUtil.getSysAttaFile(filePath, loginUser.getUserOid());
                QlSysAtta sysAtta = new QlSysAtta();
                BeanUtils.copyProperties(sysAttaFile,sysAtta);
                //保存附件信息
                ApiResultSet<QlSysAtta> atta = qlSysAttaFeignService.saveSysAtta(sysAtta);
                return  atta.getData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void acceptedPrintDown(String filePath) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-type", "application/force-download;charset=UTF-8");
            String header = request.getHeader("User-Agent").toUpperCase();
            response.setHeader("Content-Disposition", "attachment;fileName=\"" + "filePrint.doc" + "\"");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/wps-office.doc");//multipart/form-data
            response.setHeader("Access-Control-Allow-Origin", "*");
            InputStream inputStream = new FileInputStream(new File(filePath ));
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[2048];

            int length;
            while((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            os.close();
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public ApiResultSet<Map<String, Integer>> getCaseTjInfo() {
        //当前登录人总的受理量
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
       ApiResultSet<Map<String, Integer>> res= qlCaseServiceFeginService.getCaseTjInfo(loginUser.getUserOid());
        return res;
    }

    @Override
    public ApiResultSet getWorkTaskCase(Integer type, Integer pageNum, Integer pageSize) {
        long start = System.currentTimeMillis();
        Map<String,Object> resMap=new HashMap<>();
        //获取当前用户所有授权的事项
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        List<SxService> sxList= windowAcceptanceManager.listWindowAcceptancePage(null,null,null,null,loginUser.getUserOid(),null,null,null);
        //if(sxList!=null && sxList.size()>0){
        List<String> list=new ArrayList();
        if(!CurrentLoginUserHolder.getIsAdminUser()){
            if(sxList!=null && sxList.size()>0){
                list= sxList.stream().map(service->service.getServiceOid()).collect(Collectors.toList());
            }else{
                return new ApiResultSet(resMap);
            }
        }

        if(type!=null){
            if(type==1){
                //查询待预审
                ApiResultSet<PageResult<QlCase>> ysCase=qlCaseServiceFeginService.taskYsCase(list,pageNum,pageSize);
                resMap.put("ysCase",ysCase);
            }else if(type==2){
                //查询所有的补正信息
                QlCaseCorrection corr=new QlCaseCorrection();
                corr.setCorrectionStatus("0");
                corr.setServiceOids(list);
                PageResult<QlCaseCorrection> corrList=qlCaseCorrectionManager.bqbzCasePage(corr,pageNum,pageSize);
                resMap.put("bqbzCase",corrList);
            }else if(type==3){
                //查询容缺补正
                ApiResultSet<PageResult<QlCase>> rqbzCase=qlCaseServiceFeginService.taskRqhbCasePage(list,pageNum,pageSize);
                resMap.put("rqbzCase",rqbzCase);
            }else if(type==4){
                //查询所有的待出库
                List listservice= sxServiceRegistrarManager.sxServiceOidsListByUserOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
                PageResult outOfstockList= caseMaterialOutOfStockManager.queryListOutOfStockPage(listservice,pageNum,pageSize);
                resMap.put("outOfstockCase",outOfstockList);
            }else if(type==5){
                //查询所有代发正
                CaseLicenseManage manage=new CaseLicenseManage();
                manage.setLicenseInStorage("0");
                String serviceOids="";
                if(list.size()>0){
                    serviceOids=  list.stream().map((String::valueOf)).collect(Collectors.joining(","));
                }
                manage.setServiceOids(serviceOids);
                PageResult<CaseLicenseManage> linceseList=caseLincenseManageManager.queryIssuedPage(manage,Integer.valueOf(1),Integer.valueOf(7));
                resMap.put("linceseCase", linceseList);
            }
        } else{
            //查询待预审
            ApiResultSet<PageResult<QlCase>> ysCase=qlCaseServiceFeginService.taskYsCase(list,pageNum,pageSize);
            resMap.put("ysCase",ysCase);
            resMap.put("ysCaseSize",ysCase.getData().getTotal());
            //查询容缺补正
            ApiResultSet<PageResult<QlCase>> rqbzCase=qlCaseServiceFeginService.taskRqhbCasePage(list,pageNum,pageSize);
            resMap.put("rqbzCaseSize",rqbzCase.getData().getTotal());
            //查询所有的补正信息
            QlCaseCorrection corr=new QlCaseCorrection();
            corr.setCorrectionStatus("0");
            corr.setServiceOids(list);
            PageResult<QlCaseCorrection> corrList=qlCaseCorrectionManager.bqbzCasePage(corr,pageNum,pageSize);
            resMap.put("bqbzCaseSize",corrList.getTotal());
            //查询所有的待出库
            PageResult outOfstockList= caseMaterialOutOfStockManager.queryListOutOfStockPage(list,pageNum,pageSize);
            resMap.put("outOfstockCaseSize",outOfstockList.getTotal());
            //查询所有代发正
            CaseLicenseManage manage=new CaseLicenseManage();
            manage.setLicenseInStorage("0");
            String serviceOids="";
            if(list.size()>0){
                serviceOids= list.stream().map(String::valueOf).collect(Collectors.joining(","));
            }
            manage.setServiceOids(serviceOids);
            PageResult<CaseLicenseManage> linceseList=caseLincenseManageManager.queryIssuedPage(manage,pageNum,pageSize);
            resMap.put("linceseCaseSize", linceseList.getTotal());
        }

        log.info("getWorkTaskCase系统耗时计算：{}", System.currentTimeMillis()-start + "ms");
        return new ApiResultSet(resMap);
    }


    @Override
    public ApiResultSet<SysAtta> uploadDocuTemplate(String oid){
        if((StringUtils.isNotEmpty(oid))&& (!oid.equals("null"))){
            //查询附件信息并返回
            ApiResultSet<SysAtta> sysatta= sysAttaFeginService.getSysAttaByAttaOid(oid);
            if(sysatta!=null){
                return sysatta;
            }
        }
        try {
            String templatePath =null;
            String outPath =null;
            //创建新的word并上传服务器返回
            if(CommonUtil.isWindows()){
                //给一个固定的地址临时使用
                templatePath="D://"+"templates";//docuBlank.doc
            }else{
                //模板文件路径
                templatePath = "/soft/usr/local/zfsoft/zc";
            }
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            UploadUtil uploadUtil = new UploadUtil(request);
            File pdfFile = new File(templatePath);
            if (!pdfFile.exists()) {
                pdfFile.mkdirs();// 能创建多级目录
            }
            String fileInfo=templatePath+"/docuBlank.doc";
            File fileNew = new File(fileInfo);
            if(!fileNew.exists()){
                fileNew.createNewFile();
            }
            FileInputStream fileInputStream = new FileInputStream(fileNew);
            MultipartFile multipartFile = new MockMultipartFile(fileNew.getName(), fileNew.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
            CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
            if (!multipartFile.isEmpty()) {
                    // 上传并返回新文件名称
                    if(multipartFile.getOriginalFilename()!=null){
                        multipartFile = new MockMultipartFile("File",multipartFile.getOriginalFilename(),"text/plain", multipartFile.getInputStream());
                    }
                    String filePath = uploadUtil.uploadFile(multipartFile);
                    SysAttaTemp sysAttaFile = uploadUtil.getSysAttaFile(filePath, loginUser.getUserOid());
                    SysAtta sysAtta = new SysAtta();
                    BeanUtils.copyProperties(sysAttaFile,sysAtta);
                    //保存附件信息
                    ApiResultSet<SysAtta> atta = sysAttaFeginService.saveSysAtta(sysAtta);
                    return  atta;
            }

        }catch (Exception e){

        }
     return null;
    }

    @Override
    public ApiResultSet<String> printGzWord(Object object) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String templatePath =null;
        String outPath =null;
        JSONObject jsonobj=JSONObject.parseObject(JSONArray.toJSON(object).toString());
        Map<String, Object> map = jsonobj;
        Map<String, Object> pamamMap=new HashMap<>();
        for(String key:map.keySet()){
            pamamMap.put(key, map.get(key)==null?"":map.get(key));
        }
        List<Map<String, Object>> tableDataList = new ArrayList<>();
        List<QlCaseMaterial> list=new ArrayList<>();
        if(pamamMap.get("rqbzDueDate")!=null && pamamMap.get("rqbzDueDate")!=""){
            String rqbzTime=pamamMap.get("rqbzDueDate").toString();
            String[] time=DateUtil.dateformat(rqbzTime,"yyyy-MM-dd").split("-");
            pamamMap.put("year",time[0]);
            pamamMap.put("month",time[1]);
            pamamMap.put("day",time[2]);

            //拼接循环补正材料
            String materials=pamamMap.get("bzMaterials").toString();
            List<String> listMaterial=new ArrayList<>();
            int i=1;
            for(String materilName:materials.split(",")){
                listMaterial.add("（"+i+"）"+materilName);
                i++;
            }
            pamamMap.put("materials",listMaterial);
        }
        //根据办件oid查询告知材料
        /*if(pamamMap.get("isGzSl")!=null && pamamMap.get("isGzSl")!="" && pamamMap.get("isGzSl").equals("true")){
            list= qlCaseMaterialServiceFeginService.queryCngzMaterialByCaseOid(pamamMap.get("caseOid").toString()).getData();
        }else if(pamamMap.get("isRqslFlag")!=null && pamamMap.get("isRqslFlag")!=""){
            list=qlCaseMaterialServiceFeginService.queryRqhbMaterialByCaseOid(pamamMap.get("caseOid").toString()).getData();
        }*/
        /*if((pamamMap.get("isGzSl")!=null && pamamMap.get("isGzSl")!="" && pamamMap.get("isGzSl").equals("true")) || (pamamMap.get("isRqslFlag")!=null && pamamMap.get("isRqslFlag")!="")){*/
            ApiResultSet<List<QlCaseMaterial>> caseRes= qlCaseMaterialServiceFeginService.queryMaterialByCaseOidNotAttaFile(pamamMap.get("caseOid").toString());
            if(caseRes!=null){
                list=caseRes.getData();
            }
            Map<String, Object> map1=new HashMap<>();
            map1.put("number","序号");
            map1.put("materialName","申请材料");
            map1.put("forma","形式");
            map1.put("paper","份数");
            tableDataList.add(map1);
        //}
       if(list!=null && list.size()>0){
           for(QlCaseMaterial caseMaterial:list){
               Map<String, Object> mapMat=new HashMap<>();
               mapMat.put("number",tableDataList.size());
               mapMat.put("materialName",caseMaterial.getMaterialName());
               if(caseMaterial.getCollectionType()!=null){
                   if(caseMaterial.getCollectionType().equals("1")){
                       mapMat.put("forma","纸质");
                   }else if (caseMaterial.getCollectionType().equals("2")){
                       mapMat.put("forma","附件");
                   }else if (caseMaterial.getCollectionType().equals("3")){
                       mapMat.put("forma","扫描");
                   }else if (caseMaterial.getCollectionType().equals("4")){
                       mapMat.put("forma","容缺");
                   }else if (caseMaterial.getCollectionType().equals("5")){
                       mapMat.put("forma","证照");
                   }else if (caseMaterial.getCollectionType().equals("7")){
                       mapMat.put("forma","告知承诺");
                   }
               }
               mapMat.put("paper",caseMaterial.getCollectionNumber().toString());
               tableDataList.add(mapMat);
           }
       }
        pamamMap.put("volist",tableDataList);
        String targetWordPath  = "";
        String noPstPath = "";
        if(CommonUtil.isWindows()){
            //给一个固定的地址临时使用
            templatePath="D://"+"templates/"+pamamMap.get("fileName");//acceptNotice.doc
            //替换成功报存的文件路径
            //给一个固定的地址临时使用
            outPath ="D://"+ "templates/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
            log.info("templatePath1="+templatePath);
            targetWordPath  =  "E://"+ "templates/targetWordPath/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay()+"/";
            noPstPath = "E://"+ "templates/noPstPath/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay()+"/";
        }else{
            //模板文件路径
            templatePath = "/soft/usr/local/zfsoft/zc/"+pamamMap.get("fileName");
            //替换成功报存的文件路径
            outPath = "/soft/usr/local/zfsoft/zc/notice/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay();
            targetWordPath  = "/soft/usr/local/zfsoft/zc/targetWordPath/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay()+"/";
            noPstPath = "/soft/usr/local/zfsoft/zc/noPstPath/"+ DateUtil.getYear()+"/"+ DateUtil.getMonth()+"/"+DateUtil.getDay()+"/";
        }
        log.info("templatePath2="+templatePath);
        ////生成二维码标识
        if(pamamMap.get("pushUrl").toString()!=null){
            pamamMap.put("caseNum",pamamMap.get("pushUrl").toString());
        }else{
            pamamMap.put("caseNum","暂无办件信息");
        }

        try {
            //送达信息
            if(pamamMap.get("delivery")!=null && pamamMap.get("delivery").toString().length()>0){
                if(pamamMap.get("delivery").toString().indexOf("1")>-1){
                    pamamMap.put("kdsd","快递送达");

                }
                if(pamamMap.get("delivery").toString().indexOf("2")>-1){

                    pamamMap.put("zxqj","自行取件");
                }
                if(pamamMap.get("delivery").toString().indexOf("3")>-1){
                    pamamMap.put("qita","其他");
                }
            }
            //签字信息
            ApiResultSet pathRes= caseSignRecordFeginService.querySignImgPath(pamamMap.get("caseOid").toString());
            if(pathRes!=null && pathRes.getData()!=null && pathRes.getData()!=""){
                String strbase64= getImgFromUrl(pathRes.getData().toString());
                pamamMap.put("sign:Base64",strbase64);
            }
            //返回替换成功的文件路径
            //替换里面的表格信息

            //本地测试写死路劲
            //templatePath = "E:\\\\poiImage\\\\resousce\\\\sltzs.docx";
            //outPath = "E:\\poiImage\\20211116\\outPath\\sltzs_2.docx";
            String fileTem= WordPrintTemplate.genWordByTemplate(outPath,pamamMap,templatePath);
            //String fileTem= ResolutionWord.DownloadWord(response,volist,twocode,pamamMap,templatePath,outPath);
            //读取word文档里面的pst即一些参数，将印章图片替换。并且删除pst数据。示例数据 {pst|22|44|123}， 第一位，是positionH 偏移量，第二位 是positionV偏移量，
            //第三位 是  印章图片高。
            String sourceWordPath  = "E:\\\\poiImage\\\\resousce\\\\sltzs.docx";
            //String targetWordPath  = "E:\\poiImage\\20211116\\finally\\sltzs_2.docx";
            //String noPstPath = "E:\\poiImage\\20211116\\noPstPath\\sltzs_2.docx";
            //String result = PoiTlUtils.poiTl(targetWordPath,sourceWordPath,"",noPstPath);
            //创建返回文件
            if(targetWordPath !=null) {
                File fileInfo = new File(targetWordPath);
                if (!fileInfo.exists()) {
                    fileInfo.mkdirs();// 能创建多级目录
                }
            }
            if(noPstPath !=null) {
                File fileInfo = new File(noPstPath);
                if (!fileInfo.exists()) {
                    fileInfo.mkdirs();// 能创建多级目录
                }
            }
            String fileName=System.currentTimeMillis()+".docx";
            //没有文件夹，则取创建，再去拼上  文件名
            String result = PoiTlUtils.poiTl(targetWordPath+fileName,fileTem,"",noPstPath+fileName);
            System.out.println("result ------ "+result);

            //File pdfFile = new File(fileTem);
            File pdfFile = new File(result);
            FileInputStream fileInputStream = new FileInputStream(pdfFile);
            MultipartFile multipartFile = new MockMultipartFile(pdfFile.getName(), pdfFile.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
            QlSysAtta sys=uploadCaseMaterialFile(request,multipartFile);
            System.out.println(sys.toString());
            return  new ApiResultSet(result);
        }catch (Exception e){
            log.info("printGzWord error "+ e.getMessage());
        }
       return null;

    }

    //图片转为base64
    public  String getImgFromUrl(String imgUrl) throws Exception {
        URL url = null;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;

        try
        {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection)url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            is = httpUrl.getInputStream();
            outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = is.read(buffer)) != -1)
            {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }

            // 对字节数组Base64编码
            return Base64Util.encode(outStream.toByteArray());

        }
        catch (Exception e)
        {
            throw new Exception(e);
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                }
            }

            if (outStream != null)
            {
                try
                {
                    outStream.close();
                }
                catch (IOException e)
                {
                }
            }
            if (httpUrl != null)
            {
                httpUrl.disconnect();
            }
        }

    }
}
