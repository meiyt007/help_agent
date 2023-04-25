package com.zfsoft.single.controller.insideapi;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.service.QlCaseMaterialService;
import com.zfsoft.microservice.platform.data.sys.SysAtta;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.single.data.ywbl.CaseMaterialOutOfStock;
import com.zfsoft.single.data.ywbl.CaseMaterialOutOfStockRecord;
import com.zfsoft.single.manager.ywbl.CaseMaterialOutOfStockManager;
import com.zfsoft.single.manager.ywbl.CaseMaterialOutOfStockRecordManager;
import com.zfsoft.single.service.insideapi.IAppService;
import com.zfsoft.single.util.BeanUtils;
import com.zfsoft.single.util.StringUtils;
import com.zfsoft.superwindow.feign.settings.SysAttaFeginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
* @Description:  App调用接口
* @Author:liangss
* @Date:2021/9/8
* @Param:
*/
@RestController
@Slf4j
public class AppController implements IAppService {

    @Resource
    private CaseMaterialOutOfStockManager caseMaterialOutOfStockManager;
    @Resource
    private QlCaseMaterialService qlCaseMaterialServiceFeginService;
    @Resource
    private CaseMaterialOutOfStockRecordManager caseMaterialOutOfStockRecordManager;
    @Resource
    private SysAttaFeginService sysAttaFeginService;

    /***
     * @Description: 根据id获取出库数据
     * @Author:liangss
     * @Date:2021/9/8
     * @Param: [id]
     */
    @Override
    public ApiResultSet getOneMaterialTypeInfo(String id) {
        Map<String, Object> resultMap = new HashMap<>();
        List<QlCaseMaterial> qlcaseMaterial=new ArrayList<>();
        CaseMaterialOutOfStock cm=new CaseMaterialOutOfStock();
        if(StringUtils.isNotEmpty(id)){
            cm= this.caseMaterialOutOfStockManager.getOutMaterialById(Long.parseLong(id));
        }
        resultMap.put("caseMaterialOutOfStock",cm);
        ApiResultSet resultData = new ApiResultSet();
        resultData.setData(resultMap);
        return resultData;
    }







    /***
    * @Description: 根据id获取出库数据
    * @Author:liangss
    * @Date:2021/9/8
    * @Param: [id]
    */
    @Override
    public ApiResultSet getOneMaterialInfo(String id) {
        Map<String, Object> resultMap = new HashMap<>();
        List<QlCaseMaterial> qlcaseMaterial=new ArrayList<>();
        CaseMaterialOutOfStock cm=new CaseMaterialOutOfStock();
        if(StringUtils.isNotEmpty(id)){
             cm= this.caseMaterialOutOfStockManager.getOutMaterialById(Long.parseLong(id));
            if(cm!=null && StringUtils.isNotEmpty(cm.getSelectMaterialOids())){
                String[] materialArray= cm.getSelectMaterialOids().split(";");
                for (String materialOid:materialArray){
                    if(StrUtil.isNotEmpty(materialOid)){
                        ApiResultSet<QlCaseMaterial> material= qlCaseMaterialServiceFeginService.queryMaterialByCaseMaterialOid(materialOid);
                        if(material!=null){
                            qlcaseMaterial.add(material.getData());
                        }
                    }
                }
            }
        }

        //获取流转记录
        String outOid=cm.getOutOid();
        String regOid=cm.getRegOid();
        CaseMaterialOutOfStockRecord caseMaterialOutOfStockRecord=new CaseMaterialOutOfStockRecord();
        caseMaterialOutOfStockRecord.setOutOid(outOid);
        caseMaterialOutOfStockRecord.setRegOid(regOid);
        List<CaseMaterialOutOfStockRecord> caseMaterialOutOfStockRecordList=caseMaterialOutOfStockRecordManager.queryCaseMaterialOutOfStockRecordList(caseMaterialOutOfStockRecord);
        resultMap.put("caseMaterialOutOfStockRecordList",caseMaterialOutOfStockRecordList);
        resultMap.put("caseMaterialOutOfStock",cm);
        resultMap.put("qlcaseMaterial",qlcaseMaterial);
        ApiResultSet resultData = new ApiResultSet();
        resultData.setData(resultMap);
        return resultData;
    }

    @Override
    public ApiResultSet getCaseMaterialOutOfStockRecordById(String id) {
        Map<String, Object> resultMap = new HashMap<>();
        CaseMaterialOutOfStockRecord cm=caseMaterialOutOfStockRecordManager.getCaseMaterialOutOfStockRecordById(Long.parseLong(id));

        String outAttaOid=cm.getOutAttaOid();
        String sdAttaOid=cm.getSdAttaOid();
        SysAtta sysAtta=new SysAtta();
        if(StringUtils.isNotEmpty(outAttaOid)){
            ApiResultSet<SysAtta> atta  = sysAttaFeginService.getSysAttaByAttaOid(outAttaOid);
            sysAtta=atta.getData();
        }
        resultMap.put("cm",cm);
        resultMap.put("sysAtta",sysAtta);
        ApiResultSet resultData = new ApiResultSet();
        resultData.setData(resultMap);
        return resultData;

    }

    @Override
    public ApiResultSet saveMaterialOutInfo(CaseMaterialOutOfStock caseMaterialOutOfStock) {
        Map<String, Object> resultMap = new HashMap<>();
        log.info("办件材料出库信息新增/更新成功：{}", JSON.toJSONString(caseMaterialOutOfStock));
        this.caseMaterialOutOfStockManager.updateCaseMaterialOutOfStock(caseMaterialOutOfStock);
        CaseMaterialOutOfStockRecord caseMaterialOutOfStockRecord=new CaseMaterialOutOfStockRecord();
        caseMaterialOutOfStockRecord.setOutOid(caseMaterialOutOfStock.getOutOid());
        caseMaterialOutOfStockRecord.setRegOid(caseMaterialOutOfStock.getRegOid());
        caseMaterialOutOfStockRecord.setOutType(caseMaterialOutOfStock.getOutType());
        caseMaterialOutOfStockRecord.setReceiverName(caseMaterialOutOfStock.getReceiverName());
        caseMaterialOutOfStockRecord.setReceiverPhone(caseMaterialOutOfStock.getReceiverPhone());
        caseMaterialOutOfStockRecord.setKdCode(caseMaterialOutOfStock.getKdCode());
        caseMaterialOutOfStockRecord.setKdCompany(caseMaterialOutOfStock.getKdCompany());
        caseMaterialOutOfStockRecord.setSenderUserName(caseMaterialOutOfStock.getSenderUserName());
        caseMaterialOutOfStockRecord.setSenderUserPhone(caseMaterialOutOfStock.getSenderUserPhone());
        caseMaterialOutOfStockRecord.setIdCard(caseMaterialOutOfStock.getIdCard());
        caseMaterialOutOfStockRecord.setOutAttaOid(caseMaterialOutOfStock.getOutAttaOid());
        caseMaterialOutOfStockRecord.setSdAttaOid(caseMaterialOutOfStock.getSdAttaOid());
        caseMaterialOutOfStockRecord.setMaterialsFlowType("2");//1 打印材料出库流转单 2 材料出库 3 材料签收
        caseMaterialOutOfStockRecordManager.saveOrUpdate(caseMaterialOutOfStockRecord);
        resultMap.put("message","success");
        ApiResultSet resultData = new ApiResultSet();
        resultData.setData(resultMap);
        return resultData;
    }

    @Override
    public ApiResultSet signMaterialOutInfo(CaseMaterialOutOfStock caseMaterialOutOfStock) {
        Map<String, Object> resultMap = new HashMap<>();
        log.info("办件材料出库信息新增/更新成功：{}", JSON.toJSONString(caseMaterialOutOfStock));
        this.caseMaterialOutOfStockManager.updateCaseMaterialOutOfStock(caseMaterialOutOfStock);
        CaseMaterialOutOfStockRecord caseMaterialOutOfStockRecord=new CaseMaterialOutOfStockRecord();
        caseMaterialOutOfStockRecord.setOutOid(caseMaterialOutOfStock.getOutOid());
        caseMaterialOutOfStockRecord.setRegOid(caseMaterialOutOfStock.getRegOid());
        caseMaterialOutOfStockRecord.setOutType(caseMaterialOutOfStock.getOutType());
        caseMaterialOutOfStockRecord.setReceiverName(caseMaterialOutOfStock.getReceiverName());
        caseMaterialOutOfStockRecord.setReceiverPhone(caseMaterialOutOfStock.getReceiverPhone());
        caseMaterialOutOfStockRecord.setKdCode(caseMaterialOutOfStock.getKdCode());
        caseMaterialOutOfStockRecord.setKdCompany(caseMaterialOutOfStock.getKdCompany());
        caseMaterialOutOfStockRecord.setSenderUserName(caseMaterialOutOfStock.getSenderUserName());
        caseMaterialOutOfStockRecord.setSenderUserPhone(caseMaterialOutOfStock.getSenderUserPhone());
        caseMaterialOutOfStockRecord.setIdCard(caseMaterialOutOfStock.getIdCard());
        caseMaterialOutOfStockRecord.setOutAttaOid(caseMaterialOutOfStock.getOutAttaOid());
        caseMaterialOutOfStockRecord.setSdAttaOid(caseMaterialOutOfStock.getSdAttaOid());
        caseMaterialOutOfStockRecord.setMaterialsFlowType("3");//1 打印材料出库流转单 2 材料出库 3 材料签收
        caseMaterialOutOfStockRecordManager.saveOrUpdate(caseMaterialOutOfStockRecord);
        resultMap.put("message","success");
        ApiResultSet resultData = new ApiResultSet();
        resultData.setData(resultMap);
        return resultData;
    }

    @Override
    public ApiResultSet uploadAppFile(MultipartFile[] files){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            List<SysAtta> attaList = new ArrayList<SysAtta>();
            //判断file数组不能为空并且长度大于0
            if(files !=null&&files.length >0){
                MultipartFile file = null;
                UploadUtil uploadUtil = new UploadUtil(request);
                String filePath = null;
                //循环获取file数组中得文件
                for (int i = 0; i < files.length; i++) {
                    file = files[i];
                    //保存文件
                    if (!file.isEmpty()) {
                        try {
                            // 上传并返回新文件名称
                            if(StrUtil.isNotEmpty(file.getOriginalFilename())){
                                file = new MockMultipartFile("File",file.getOriginalFilename(),"text/plain", file.getInputStream());
                            }

                            filePath = uploadUtil.uploadFile(file);
                            SysAttaTemp sysAttaFile = uploadUtil.getSysAttaFile(filePath, "");
                            SysAtta sysAtta = new SysAtta();
                            BeanUtils.copyProperties(sysAttaFile,sysAtta);
                            //保存附件信息
                            ApiResultSet<SysAtta> atta = sysAttaFeginService.saveSysAtta(sysAtta);
                            return  atta;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return new ApiResultSet<>(attaList);
        }catch (Exception e){
            throw new ResultInfoException(e.getMessage());
        }
    }
   /* public ApiResultSet uploadAppFile(MultipartFile[] files) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        com.zfsoft.platform.utils.fileUtil.UploadUtil uploadUtil = new com.zfsoft.platform.utils.fileUtil.UploadUtil(request);
        try {
            List<SysAtta> attaList = new ArrayList<SysAtta>();
            //判断file数组不能为空并且长度大于0
            if(files !=null&&files.length >0){
                MultipartFile file = null;
                String filePath = null;
                //循环获取file数组中得文件
                for (int i = 0; i < files.length; i++) {
                    file = files[i];
                    //保存文件
                    if (!file.isEmpty()) {
                        try {
                            // 上传并返回新文件名称
                            if(StrUtil.isNotEmpty(file.getOriginalFilename())){
                                file = new MockMultipartFile("File",file.getOriginalFilename(),"text/plain", file.getInputStream());
                            }
                             filePath = uploadUtil.uploadFile(file);
                            SysAttaTemp sysAttaFile = uploadUtil.getSysAttaFile(filePath, "");
                            SysAtta sysAtta = new SysAtta();
                            BeanUtils.copyProperties(sysAttaFile,sysAtta);
                            //保存附件信息
                            ApiResultSet<SysAtta> atta = sysAttaFeginService.saveSysAtta(sysAtta);
                            return  atta;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return new ApiResultSet<>(attaList);
        }catch (Exception e){
            throw new ResultInfoException(e.getMessage());
        }
    }*/


}
