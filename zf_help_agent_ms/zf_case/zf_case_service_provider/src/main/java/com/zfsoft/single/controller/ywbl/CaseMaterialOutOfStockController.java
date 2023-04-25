package com.zfsoft.single.controller.ywbl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.feign.SysUserFeginService;
import com.zfsoft.cases.service.QlCaseMaterialService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.single.data.ywbl.CaseMaterialOutOfStock;
import com.zfsoft.single.data.ywbl.CaseMaterialOutOfStockRecord;
import com.zfsoft.single.manager.ywbl.CaseMaterialOutOfStockManager;
import com.zfsoft.single.manager.ywbl.CaseMaterialOutOfStockRecordManager;
import com.zfsoft.single.service.ywbl.CaseMaterialOutOfStockService;
import com.zfsoft.single.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;


/**
 * @author: dongxl
 * @create: 2020-11-6
 * @description: 办件材料出库
 */
@Slf4j
@RestController
public class CaseMaterialOutOfStockController implements CaseMaterialOutOfStockService {

    @Resource
    private CaseMaterialOutOfStockManager caseMaterialOutOfStockManager;
    @Resource
    private QlCaseMaterialService qlCaseMaterialServiceFeginService;
    @Resource
    private CaseMaterialOutOfStockRecordManager caseMaterialOutOfStockRecordManager;
    @Autowired
    private SysUserFeginService sysUserFeginService;

    /**
     * 办件材料出库列表
     * @param caseMaterialOutOfStock
     * dongxl
     * @param pageNum
     * @param pageSize
     * @return
     */

    @Override
    public ApiResultSet queryPageList(CaseMaterialOutOfStock caseMaterialOutOfStock, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CaseMaterialOutOfStock> caseMaterialList = this.caseMaterialOutOfStockManager.queryPage(caseMaterialOutOfStock);
        if(caseMaterialList!=null && caseMaterialList.size()>0){
            PageResult<CaseMaterialOutOfStock> pageResult = new PageResult<>(
                    ((Page) caseMaterialList).getPageNum(),
                    ((Page) caseMaterialList).getPageSize(),
                    ((Page) caseMaterialList).getTotal()
            );
            pageResult.setData(caseMaterialList);
            log.info("获取好差评列表调用成功结果为：{}", JSON.toJSONString(pageResult));
            return new ApiResultSet(pageResult);

        }
        return new ApiResultSet();

    }

    /**
     * 保存或者更新出库信息 ( 出库操作的时候调用)
     * dongxl
     * @param caseMaterialOutOfStock
     * @return
     */
    @Override
    public ApiResultSet saveOrUpdate( CaseMaterialOutOfStock caseMaterialOutOfStock) {
        log.info("办件材料出库信息新增/更新成功：{}", JSON.toJSONString(caseMaterialOutOfStock));
        caseMaterialOutOfStock=this.caseMaterialOutOfStockManager.saveOrUpdate(caseMaterialOutOfStock);
       /* CaseMaterialOutOfStockRecord caseMaterialOutOfStockRecord=new CaseMaterialOutOfStockRecord();
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
        caseMaterialOutOfStockRecordManager.saveOrUpdate(caseMaterialOutOfStockRecord);*/
        return new ApiResultSet(caseMaterialOutOfStock);
    }

    /***
     * @Description: 更新出库信息
     * @Author:liangss
     * @Date:2021/9/7
     * @Param: [caseMaterialOutOfStock]
     */
    @Override
    public ApiResultSet updateCaseMaterialOutOfStock( CaseMaterialOutOfStock caseMaterialOutOfStock) {
        log.info("办件材料出库信息更新成功：{}", JSON.toJSONString(caseMaterialOutOfStock));
        this.caseMaterialOutOfStockManager.updateCaseMaterialOutOfStock(caseMaterialOutOfStock);
        return new ApiResultSet(caseMaterialOutOfStock);
    }


    /**
     * 根据办件编号获取出库信息
     * dongxl
     * @param caseNumber
     * @return
     */
    @Override
    public ApiResultSet getOneByCaseNumber(String caseNumber) {
        List<CaseMaterialOutOfStock> outOfStock = this.caseMaterialOutOfStockManager.getOneByCaseNumber(caseNumber);
        log.info("详情获取成功：{}", outOfStock);
        return new ApiResultSet(outOfStock);
    }

    /**
     * 批量保存出库信息
     * dongxl
     * @param ids
     * @return
     */
    @Override
    public ApiResultSet batchOutMaterial(String ids) {
        if(StrUtil.isNotEmpty(ids)){
            /*String[] caseIds=ids.split(";");
            CaseMaterialOutOfStock caseMaterialOutOfStock=new CaseMaterialOutOfStock();
            Arrays.asList(caseIds).stream().forEach(id->{
                caseMaterialOutOfStock.setId(Long.parseLong(id));
            this.caseMaterialOutOfStockManager.saveOrUpdate(caseMaterialOutOfStock);*/
                this.caseMaterialOutOfStockManager.batchOutMaterial(ids);
           // });
        }
        log.info("批量保存出库信息成功：{}", ids);
        return new ApiResultSet("");
    }

    @Override
    public ApiResultSet printOutMaterial(String ids) {
        List list=new ArrayList();
        try {
            if(StrUtil.isNotEmpty(ids)){
                String[] caseIds=ids.split(";");
                CurrentLoginUser user=CurrentLoginUserHolder.getCurrentLoginUser();
                Arrays.asList(caseIds).stream().forEach(id->{
                    Map<String, Object> map= this.caseMaterialOutOfStockManager.getReplaceMap(Long.parseLong(id),user);
                    if(map!=null&&map.size()>0){
                        list.add(map);
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("批量打印材料出库成功：{}", ids);
        return new ApiResultSet(list);
    }

    @Override
    public ApiResultSet getPrintOneMaterialInfo(Long id) {
        Map<String, Object> mapCase=new HashMap<>();
        CurrentLoginUser user=CurrentLoginUserHolder.getCurrentLoginUser();
        mapCase= this.caseMaterialOutOfStockManager.getReplaceMap(id,user);
        return new ApiResultSet(mapCase);
    }


    @Override
    public ApiResultSet getOneMaterialInfo(Long id) {
        CaseMaterialOutOfStock cm= this.caseMaterialOutOfStockManager.getOutMaterialById(id);
        List<QlCaseMaterial> qlcaseMaterial=new ArrayList<>();
     /*   if(cm!=null && cm.getMaterialOids() !=null){
           String[] materialArray= cm.getMaterialOids().split(";");
           for (String materialOid:materialArray){
               if(StrUtil.isNotEmpty(materialOid)){
                  ApiResultSet<QlCaseMaterial> material= qlCaseMaterialServiceFeginService.queryMaterialByCaseMaterialOid(materialOid);
                  if(material!=null){
                      qlcaseMaterial.add(material.getData());
                  }
               }
           }
        }
*/
        if(cm!=null && StringUtils.isNotEmpty(cm.getSelectMaterialOids())){
            String[] materialArray= cm.getSelectMaterialOids().split(";");
            Set<String> sets = new HashSet(Arrays.asList(materialArray));
            for (String materialOid:sets){
                if(StrUtil.isNotEmpty(materialOid)){
                    ApiResultSet<QlCaseMaterial> material= qlCaseMaterialServiceFeginService.queryMaterialByCaseMaterialOid(materialOid);
                    if(material!=null){
                        qlcaseMaterial.add(material.getData());
                    }
                }
            }
        }
        return new ApiResultSet(qlcaseMaterial);
    }

    @Override
    public ApiResultSet getCaseMaterialOutByCaseNumber(String caseNumber) {
       List<CaseMaterialOutOfStock> list= this.caseMaterialOutOfStockManager.getCaseMaterialOutByCaseNumber(caseNumber);
        return new ApiResultSet(list);
    }

    @Override
    public ApiResultSet saveCaseMaterialOutOfStockRecordByPrint(Long id) {

        CaseMaterialOutOfStock caseMaterialOutOfStock= this.caseMaterialOutOfStockManager.getOutMaterialById(id);
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
        CurrentLoginUser user=CurrentLoginUserHolder.getCurrentLoginUser();
        if(StringUtils.isEmpty(caseMaterialOutOfStockRecord.getReceiverName())){
            caseMaterialOutOfStockRecord.setReceiverName(user.getUserName());
            caseMaterialOutOfStockRecord.setSenderUserName(user.getUserName());
            caseMaterialOutOfStockRecord.setReceiverPhone(sysUserFeginService.getSysUserByUserOid(user.getUserOid()).getData().getMobile());
        }
        caseMaterialOutOfStockRecord.setMaterialsFlowType("1");//1 打印材料出库流转单 2 材料出库 3 材料签收
        caseMaterialOutOfStockRecordManager.saveOrUpdate(caseMaterialOutOfStockRecord);
        return new ApiResultSet(caseMaterialOutOfStock);
    }
}
