package com.zfsoft.microservice.platform.controller.sys;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.platform.data.sys.SysApp;
import com.zfsoft.microservice.platform.manager.sys.SysAppManager;
import com.zfsoft.microservice.platform.service.sys.SysAppService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.excel.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SysAppController
 * @Description 应用管理的实现类
 * @Author wuxx
 * @Date 2020-09-12 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SysAppController implements SysAppService{

    @Resource
    private SysAppManager sysAppManager;

    /**
     * @param oid 应用主键
     * @description: 初始化区划的信息
     * @author: wuxx
     * @Date: 2020/9/10 10:14
     **/
    @RequestMapping(value = {"/init","/init/{oid}"}, method = {RequestMethod.GET})
    public ApiResultSet<Map<String, Object>> initSysApp(@PathVariable(value="oid",required=false) Long oid) {
        Map<String, Object> resultMap = new HashMap<>();
        if (null != oid) {
            SysApp app = sysAppManager.getSysAppById(oid);
            resultMap.put("app", app);
        }
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(resultMap);
        return apiResultSet;
    }

    /**
     * @param sysApp 应用实体类
     * @description: 应用的新增或者修改
     * @author: wuxx
     * @Date: 2020/9/10 10:14
     **/
    public ApiResultSet<SysApp> saveSysApp(SysApp sysApp) {
        sysAppManager.saveSysApp(sysApp);
        ApiResultSet<SysApp> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(sysApp);
        return apiResultSet;
    }

    /**
     * @param oid 应用实体类主键
     * @description: 应用的信息的删除
     * @author: wuxx
     * @Date: 2020/9/10 10:14
     **/
    @Override
    public ApiResultSet<Integer> deleteSysAppById(Long oid) {
        int rows = sysAppManager.deleteSysAppById(oid);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<Integer>();
        if (BaseStaticParameter.Y == rows){
            return apiResultSet;
        }else {
            apiResultSet.setCode(ApiResultSet.DIRTY_DATA_TITLE);
            apiResultSet.setMessage("删除失败，该应用下存在未删除的角色！");
            return apiResultSet;
        }
    }

    /**
     * @param oid 应用实体类主键
     * @description: 获取应用的信息
     * @author: wuxx
     * @Date: 2020/9/10 10:14
     **/
    public ApiResultSet<SysApp> getSysAppById(Long oid) {
        SysApp sysApp = sysAppManager.getSysAppById(oid);
        ApiResultSet<SysApp> apiResultSet = new ApiResultSet<SysApp>();
        apiResultSet.setData(sysApp);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<SysApp> getSysAppByAppOid(String appOid) {
        SysApp app = sysAppManager.getSysAppByAppOid(appOid);
        return new ApiResultSet<>(app);
    }

    /**
     * @param name 应用名称
     * @description: 查询应用的信息列表
     * @author: wuxx
     * @Date: 2020/9/10 10:14
     **/
    @RequestMapping(value = "/page", method = {RequestMethod.GET})
    public ApiResultSet querySysAppWithPage(String name, Integer pageNum,Integer pageSize) {
        SysApp sysApp = new SysApp();
        sysApp.setName(name);
        PageResult<SysApp> pageResult = sysAppManager.querySysAppWithPage(sysApp, pageNum, pageSize);
        return new ApiResultSet<>(pageResult);
    }

    /**
     * @param oid 应用实体类主键
     * @description: 应用的信息的启禁用
     * @author: wuxx
     * @Date: 2020/9/10 10:14
     **/
    @Override
    public ApiResultSet<Integer> ableSysAppById(@PathVariable("oid") Long oid) {
        int rows = sysAppManager.ableSysAppById(oid);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<Integer>();
        if (BaseStaticParameter.Y == rows){
            return apiResultSet;
        }else {
            apiResultSet.setCode(ApiResultSet.DIRTY_DATA_TITLE);
            apiResultSet.setMessage("禁用失败，该应用下存在启用状态的角色！");
            return apiResultSet;
        }
    }

    /**
     * @param name 应用名称
     * @description: 导出成excel
     * @author: wuxx
     * @Date: 2020/9/10 10:00
     **/
    public void listExp(HttpServletResponse response, String name, Integer pageNum,
                        Integer pageSize) {
        SysApp sysApp = new SysApp();
        sysApp.setName(name);
        PageResult<SysApp> pageResult = sysAppManager.querySysAppWithPage(sysApp, 1, 999);
        List<SysApp> list = pageResult.getData();
        String title = "应用管理列表";
        String[] rowsName = new String[] { "序号", "应用名称", "显示名称", "访问根路径", "创建时间", "启用状态" };
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < list.size(); i++) {
            SysApp sysAppN = list.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            if (!StrUtil.isEmpty(sysAppN.getName())) {
                objs[1] = sysAppN.getName();
            } else {
                objs[1] = "-";
            }
            if (!StrUtil.isEmpty(sysAppN.getDisplayName())) {
                objs[2] = sysAppN.getDisplayName();
            } else {
                objs[2] = "-";
            }
            if (!StrUtil.isEmpty(sysAppN.getAccessRootAddr())) {
                objs[3] = sysAppN.getAccessRootAddr();
            } else {
                objs[3] = "-";
            }
            /*if (!StrUtil.isEmpty(sysAppN.getIconName())) {
                objs[4] = sysAppN.getIconName();
            } else {
                objs[4] = "-";
            }*/
            if (sysAppN.getCreateDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                objs[4] = sdf.format(sysAppN.getCreateDate());
            } else {
                objs[4] = "-";
            }
            objs[5] = BaseStaticParameter.ABLE_MAP.get(sysAppN.getIsAble());
            dataList.add(objs);
        }
        ExportExcelUtil ex = new ExportExcelUtil();
        ex.export(response, title, rowsName, dataList);
    }

    /**
     * 查询应用列表
     * @author zxx
     * @date 2020/9/22 4:14 下午
     * @param name 名称
     * @return
     */
    public ApiResultSet querySysApp(String name) {
        SysApp sysApp = new SysApp();
        sysApp.setName(name);
        List<SysApp> sysApps = sysAppManager.querySysApp(sysApp);
        return new ApiResultSet<>(sysApps);
    }
}
