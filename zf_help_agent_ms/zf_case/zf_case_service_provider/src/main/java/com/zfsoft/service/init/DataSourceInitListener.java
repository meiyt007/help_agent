package com.zfsoft.service.init;

import com.zfsoft.service.common.SxptBaseStaticParameter;
import com.zfsoft.service.manager.sxDirectory.SxServiceTypeManager;
import com.zfsoft.service.manager.sxSys.SxSysDictManager;
import com.zfsoft.service.sxSys.data.SxSysDict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangxialing
 * @Description:com.zfsoft.init
 * @date 2020/10/28
 */
@Component
public class DataSourceInitListener implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceInitListener.class);

    @Resource
    private SxServiceTypeManager sxServiceTypeManager;
    @Resource
    private SxSysDictManager sxSysDictManager;

    @Override
    public void run(ApplicationArguments args) {
        LOGGER.info("初始化数据开始..........");
        try {
            initBaseStaticParameter();//初始化数据
            LOGGER.info("系统初始化完成...........");
        } catch (Exception e) {
            LOGGER.error("系统初始化失败！settings-service-provider程序访问异常！");
        }
    }

    /**
     * 初始化数据
     */
    private void initBaseStaticParameter() {
        SxptBaseStaticParameter.serviceTypeList = sxServiceTypeManager.getSxServiceTypeList();
        SxptBaseStaticParameter.levelList = sxSysDictManager.querySxSysDictListByParentCode(SxptBaseStaticParameter.EXERCISE_LEVEL_CODE);

        // SxptBaseStaticParameter.organPropertys = null;
        List<SxSysDict> sxSysDicts = sxSysDictManager.querySxSysDictListByParentCode(SxptBaseStaticParameter.IMPLEMENT_ORGAN_PROPERTY);
        if (null != sxSysDicts && sxSysDicts.size() > 0) {
            SxptBaseStaticParameter.organPropertys = sxSysDicts;
            for (SxSysDict dict : sxSysDicts) {
                SxptBaseStaticParameter.organPropertyMaps.put(dict.getOid(), dict);
            }
        }
        List<SxSysDict> sxSysDictList = sxSysDictManager.querySxSysDictListByParentCode(SxptBaseStaticParameter.SERVICE_ORIGIN);
        if (null != sxSysDictList && sxSysDictList.size() > 0) {
            SxptBaseStaticParameter.originList = sxSysDictList;
            for (SxSysDict sysDict : sxSysDictList) {
                SxptBaseStaticParameter.originMaps.put(sysDict.getOid(), sysDict);
            }
        }
        //主题分类
        List<SxSysDict> ztflList = sxSysDictManager.querySxSysDictListByParentCode("ZTFL");
        if (null != ztflList && ztflList.size() > 0) {
            for (SxSysDict sysDict : ztflList) {
                List<SxSysDict> ztflChild = sxSysDictManager.querySxSysDictListByParentCode(sysDict.getCode());
                SxptBaseStaticParameter.SUB_CLASS.put(sysDict.getCode(), ztflChild);
            }
        }
        //生命周期
        List<SxSysDict> smzqList = sxSysDictManager.querySxSysDictListByParentCode("SMZQFL");
        if (null != smzqList && smzqList.size() > 0) {
            for (SxSysDict sysDict : smzqList) {//个人、法人
                List<SxSysDict> smzqChild = sxSysDictManager.querySxSysDictListByParentCode(sysDict.getCode());
                sysDict.setChildSysDict(smzqChild);
                if (smzqChild != null && smzqChild.size() > 0) {//子集
                    for (SxSysDict childSm : smzqChild) {
                        List<SxSysDict> smzqChilds = sxSysDictManager.querySxSysDictListByParentCode(childSm.getCode());
                        childSm.setChildSysDict(smzqChilds);
                    }
                }
                SxptBaseStaticParameter.LIFE_CYCLE.put(sysDict.getCode(), sysDict);//法人自然人
            }
        }
        //办事群体
        List<SxSysDict> bsqtList = sxSysDictManager.querySxSysDictListByParentCode("BSQTFL");
        if (null != bsqtList && bsqtList.size() > 0) {
            for (SxSysDict sysDict : bsqtList) {
                List<SxSysDict> bsqtChild = sxSysDictManager.querySxSysDictListByParentCode(sysDict.getCode());
                SxptBaseStaticParameter.SERVICE_GROUP.put(sysDict.getCode(), bsqtChild);
            }
        }

        SxptBaseStaticParameter.chargeLink = sxSysDictManager.querySxSysDictListByParentCode("XZSP");
        SxptBaseStaticParameter.handleDepths = sxSysDictManager.querySxSysDictListByParentCode(SxptBaseStaticParameter.HANDLE_DEPTH);
        SxptBaseStaticParameter.handleScopes = sxSysDictManager.querySxSysDictListByParentCode(SxptBaseStaticParameter.HANDLE_SCOPE);

    }


}
