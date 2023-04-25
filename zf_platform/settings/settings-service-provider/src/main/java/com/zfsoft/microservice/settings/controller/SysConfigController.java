package com.zfsoft.microservice.settings.controller;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.settings.data.SysConfig;
import com.zfsoft.microservice.settings.manager.SysConfigManager;
import com.zfsoft.microservice.settings.service.SysConfigService;
import com.zfsoft.microservice.settings.util.GenDataTreeUtil;
import com.zfsoft.microservice.settings.util.TreeSelect;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.excel.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SysConfigController
 * @Description 参数配置管理的实现类
 * @Author wuxx
 * @Date 2020-09-12 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SysConfigController implements SysConfigService{

    @Resource
    private SysConfigManager sysConfigManager;

    /**
     * @description:  初始化参数配置的信息
     * @param id 参数配置实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet initSysConfig(Long id,String parentOid){
        Map<String, Object> resultMap = new HashMap<>();
        if(null!=id){
            SysConfig  sysConfig = sysConfigManager.getSysConfigById(id);
            resultMap.put("sysConfig",sysConfig);
            if(StrUtil.isNotEmpty(sysConfig.getParentOid())){
                SysConfig parentSysConfig = sysConfigManager.getSysConfigByConfigOid(sysConfig.getParentOid());
                resultMap.put("parentSysConfig",parentSysConfig);
            }
        }
        if(StrUtil.isNotEmpty(parentOid)){
            SysConfig parentSysConfig = sysConfigManager.getSysConfigByConfigOid(parentOid);
            resultMap.put("parentSysConfig",parentSysConfig);
        }
        return new ApiResultSet<>(resultMap);
    }

    /**
     * @description:  参数配置的新增或者修改
     * @param sysConfig 参数配置实体类
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<SysConfig> saveSysConfig(@RequestBody SysConfig sysConfig){
        sysConfigManager.saveSysConfig(sysConfig);
        return  new ApiResultSet<>(sysConfig);
    }

    /**
     * @description:  参数配置的信息的删除
     * @param id 参数配置实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<Integer>  deleteSysConfigById(Long id){
        sysConfigManager.deleteSysConfigById(id);
        return new ApiResultSet<>();
    }

    /**
     * @description:  获取参数配置的信息
     * @param id 参数配置实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<SysConfig>  getSysConfigById(Long id){
        SysConfig sysConfig = sysConfigManager.getSysConfigById(id);
        return new ApiResultSet<>(sysConfig);
    }

    /**
     * @description:  获取参数配置的信息
     * @param configOid 参数配置实体类业务主键
     * @author: wanglei
     * @Date: 2020/10/29 11:40
     **/
    public ApiResultSet<SysConfig>  getSysConfigByConfigOid(@PathVariable("configOid")String configOid){
        SysConfig sysConfig = sysConfigManager.getSysConfigByConfigOid(configOid);
        ApiResultSet<SysConfig> apiResultSet = new ApiResultSet<SysConfig>();
        apiResultSet.setData(sysConfig);
        return apiResultSet;
    }

    /**
     * @description:  获取参数配置的信息
     * @param code 参数配置实体类code
     * @author: wuxx
     * @Date: 2020/9/14 10:14
     **/
    public ApiResultSet<SysConfig>  getSysConfigByCode(String code){
        SysConfig sysConfig = sysConfigManager.getSysConfigByCode(code);
        return new ApiResultSet<>(sysConfig);
    }

   /**
    * @description: 获取登录验码的标识 true 开启  false关闭
    * @author: wuxx
    * @Date: 2020/10/26 9:30
    **/
    @RequestMapping(value = {"/getLoginCodeFlag"},method = {RequestMethod.GET})
    public ApiResultSet getLoginCodeFlag(){
        SysConfig sysConfig = sysConfigManager.getSysConfigByCode("LOGIN_VALIDCODE_SWITCH");
        boolean codeFlag = false;
        if(null!=sysConfig){
            codeFlag = BaseStaticParameter.YES.equals(sysConfig.getValue());
        }
        return new ApiResultSet<>(codeFlag);
    }


    /**
     * @description:  查询参数配置的信息列表
     * @param name 参数配置名称
     * @param code 参数配置编码
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<PageResult<SysConfig>> querySysConfigWithPage(String name,String code,String parentOid,Integer pageNum,
                                                                          Integer pageSize){
        SysConfig sysConfig = new SysConfig();
        sysConfig.setName(name);
        sysConfig.setCode(code);
        sysConfig.setParentOid(parentOid);
        PageResult<SysConfig> pageResult = sysConfigManager.querySysConfigWithPage(sysConfig,pageNum,pageSize);
        ApiResultSet<PageResult<SysConfig>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    /**
     * @description:  参数配置的信息的启禁用
     * @param id 参数配置实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<Integer> ableSysConfigById(@PathVariable("id")Long id){
        int rows = sysConfigManager.ableSysConfigById(id);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<Integer>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }

    /**
     * @description:  根据父类code查询参数配置下级列表
     * @param parentCode 父类code
     * @author: wuxx
     * @Date: 2020/9/14 11:14
     **/
    public ApiResultSet<List<SysConfig>> querySysConfigListByParentCode(String parentCode){
        SysConfig sysConfig = sysConfigManager.getSysConfigByCode(parentCode);
        List<SysConfig> sysConfigList = sysConfigManager.querySysConfigListByParentOid(sysConfig.getConfigOid());
        return new ApiResultSet(sysConfigList);
    }

    /**
     * @description:  根据父类code查询参数配置下级列表
     * @param parentOid 父类parentOid
     * @author: wuxx
     * @Date: 2020/9/14 11:14
     **/
    public ApiResultSet<List<SysConfig>> querySysConfigListByParentOid(String parentOid){
        List<SysConfig> sysConfigList = sysConfigManager.querySysConfigListByParentOid(parentOid);
        return new ApiResultSet(sysConfigList);
    }

    /**
     * @description:  根据父类oid获取参数配置列表，用于生成参数配置树
     * @param parentOid 父类oid
     * @author: wuxx
     * @Date: 2020/9/9 18:14
     **/
    public ApiResultSet queryConfigTree(String parentOid){
        List<SysConfig> sysConfigList = sysConfigManager.querySysConfigChildrenListByParentOid(parentOid);
        List<TreeSelect> treeSelects = GenDataTreeUtil.buildSysConfigTreeSelect(sysConfigList);
        return new ApiResultSet(treeSelects);
    }

    /**
     * @description:  导出成excel
     * @param name 参数配置名称
     * @param code 参数配置编码
     * @author: wuxx
     * @Date: 2020/9/12 14:00
     **/
    @RequestMapping( value = "/listExp",method = {RequestMethod.GET})
    public void listExp(HttpServletResponse response, String name,String code,String parentOid){
        SysConfig sysConfig = new SysConfig();
        sysConfig.setName(name);
        sysConfig.setCode(code);
        sysConfig.setParentOid(parentOid);
        PageResult<SysConfig> pageResult = sysConfigManager.querySysConfigWithPage(sysConfig,1,Integer.MAX_VALUE);
        List<SysConfig> list = pageResult.getData();
        String title = "系统配置列表";
        String[] rowsName = new String[] { "序号", "配置项代码", " 配置项名称", "配置项值", "配置项描述", "  启用状态" };
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < list.size(); i++) {
            SysConfig sysConfigN = list.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            if (!StrUtil.isEmpty(sysConfigN.getCode())) {
                objs[1] = sysConfigN.getCode();
            } else {
                objs[1] = "--";
            }
            if (!StrUtil.isEmpty(sysConfigN.getName())) {
                objs[2] = sysConfigN.getName();
            } else {
                objs[2] = "--";
            }
            if (!StrUtil.isEmpty(sysConfigN.getValue())) {
                objs[3] = sysConfigN.getValue();
            } else {
                objs[3] = "--";
            }
            if (!StrUtil.isEmpty(sysConfigN.getMemo())) {
                objs[4] = sysConfigN.getMemo();
            } else {
                objs[4] = "--";
            }
            objs[5] = BaseStaticParameter.ABLE_MAP.get(sysConfigN.getIsAble());
            dataList.add(objs);
        }
        ExportExcelUtil ex = new ExportExcelUtil();
        ex.export(response, title, rowsName, dataList);
    }
}
