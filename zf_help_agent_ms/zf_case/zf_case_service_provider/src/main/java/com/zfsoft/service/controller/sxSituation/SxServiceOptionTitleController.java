package com.zfsoft.service.controller.sxSituation;

import com.alibaba.fastjson.JSON;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.manager.sxSituation.ServiceOptionRelManager;
import com.zfsoft.service.manager.sxSituation.SxServiceOptionTitleManager;
import com.zfsoft.service.manager.sxSituation.SxServiceOptionValManager;
import com.zfsoft.service.sxSituation.data.ServiceOptionRel;
import com.zfsoft.service.sxSituation.data.SxServiceOptionTitle;
import com.zfsoft.service.sxSituation.data.SxServiceOptionVal;
import com.zfsoft.service.sxSituation.service.SxServiceOptionTitleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author wangns
 * @description
 * @date 2020/11/3 11:16
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class SxServiceOptionTitleController implements SxServiceOptionTitleService {

    @Resource
    private SxServiceOptionTitleManager sxServiceOptionTitleManager;

    @Resource
    private SxServiceOptionValManager sxServiceOptionValManager;

    @Resource
    private ServiceOptionRelManager serviceOptionRelManager;

    /**
     * @param oid 业务主键
     * @description: 根据选项标题业务主健获取选项标题消息
     * @author: wangns
     * @Date: 2020/11/3 11:09
     **/
    @Override
    public ApiResultSet<SxServiceOptionTitle> getSxServiceOptionTitleByOid(String oid) {

        SxServiceOptionTitle sxServiceOptionTitle = sxServiceOptionTitleManager.getServiceOptionTitleByOid(oid);

        ApiResultSet<SxServiceOptionTitle> apiResultSet = new ApiResultSet<>();

        apiResultSet.setData(sxServiceOptionTitle);

        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<Map<String,Object>>> getSxServiceOptionAllTitleValRelation(String serviceOid,String titleOid) {
        List<Map<String,Object>> list =new LinkedList<Map<String,Object>>();
        //查询标题下所有选项
        List<SxServiceOptionVal> sxServiceOptionVals = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(titleOid);
        if (sxServiceOptionVals !=null){
            Map<String,Object> map =null;
            for (SxServiceOptionVal optionVal: sxServiceOptionVals) {
                map =new HashMap<String,Object>();
                map.put("valOid",optionVal.getOid());
                //查询跟选项有关联的标题的关联数据
                List<ServiceOptionRel> serviceOptionRels = serviceOptionRelManager.getServiceOptionRelListByOids(serviceOid,optionVal.getOid());
                if (serviceOptionRels !=null){
                    List<SxServiceOptionTitle> titleList=new LinkedList<>();
                    for (ServiceOptionRel optionRel: serviceOptionRels) {
                        //获取标题集合
                        String titleOids [] = optionRel.getTitleOids().split(",");
                        for (String oid: titleOids) {
                            //查询标题
                            SxServiceOptionTitle title = sxServiceOptionTitleManager.getServiceOptionTitleByOid(oid);
                            titleList.add(title);
                        }
                    }
                    map.put("titleList",titleList);
                }
                list.add(map);
            }
        }
        ApiResultSet<List<Map<String,Object>>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(list);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<SxServiceOptionTitle>> getListTitleAndOption(String serviceOid) {
        List<SxServiceOptionTitle> list=sxServiceOptionTitleManager.getListTitleAndOption(serviceOid);
        return new ApiResultSet<>(list);
    }

    @Override
    public ApiResultSet<List<SxServiceOptionTitle>> getListTitleAndOptionMaterial(String serviceOid) {
        List<SxServiceOptionTitle> list=sxServiceOptionTitleManager.getListTitleAndOptionMaterial(serviceOid);
        return new ApiResultSet<>(list);
    }

    @Override
    public ApiResultSet<Map<String,Object>> getRelationJsonForPicture(String serviceOid, String titleOid, String valOid) {
       String[] relationJson= sxServiceOptionTitleManager.getRelationJsonForPicture(serviceOid,titleOid,valOid);
        Map<String,Object> modelMap=new HashMap();
        modelMap.put("data1", JSON.parseArray(relationJson[0]));
        modelMap.put("data2", JSON.parseArray(relationJson[1]));
       return new ApiResultSet<>(modelMap);
    }


    @Override
    public ApiResultSet initSxServiceOptionTitle(String titleOid) {
        SxServiceOptionTitle optionTitle = sxServiceOptionTitleManager.getServiceOptionTitleByOid(titleOid);
        if(optionTitle != null){
            List<SxServiceOptionVal> optionVallist = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(titleOid);
            if(optionVallist != null &&  optionVallist.size() > 0){
                optionTitle.setSxServiceOptionVals(optionVallist);
            }
        }
        ApiResultSet<SxServiceOptionTitle> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(optionTitle);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<PageResult<SxServiceOptionTitle>> querySxServiceOptionTitlePag(String name, String serviceOid, Integer pageNum, Integer pageSize) {
        ApiResultSet<PageResult<SxServiceOptionTitle>> list=  sxServiceOptionTitleManager.querySxServiceOptionTitlePag(name,serviceOid,pageNum,pageSize);
        return new ApiResultSet(list);
    }

    @Override
    public ApiResultSet saveSxServiceOptionTitle(SxServiceOptionTitle sxServiceOptionTitle) {
        sxServiceOptionTitleManager.saveSxServiceOptionTitle(sxServiceOptionTitle);
        return new ApiResultSet("保存成功");
    }

    @Override
    public ApiResultSet delSxServiceOptionTitleByOid(String oid) {
        sxServiceOptionTitleManager.delSxServiceOptionTitleByOid(oid);
        return new ApiResultSet("删除成功");
    }
}
