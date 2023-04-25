package com.zfsoft.microservice.settings.controller;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.microservice.settings.manager.SysDictManager;
import com.zfsoft.microservice.settings.service.SysDictService;
import com.zfsoft.microservice.settings.util.GenDataTreeUtil;
import com.zfsoft.microservice.settings.util.TreeSelect;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.excel.ExportExcelUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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
 * @ClassName SysDictController
 * @Description 数据字典管理的实现类
 * @Author wuxx
 * @Date 2020-09-12 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SysDictController implements SysDictService{

    @Resource
    private SysDictManager sysDictManager;

    /**
     * @description:  初始化数据字典的信息
     * @param id 数据字典实体类
     * @param parentOid 父类oid
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    public ApiResultSet initSysDistrict(Long id,String parentOid){
        Map<String, Object> resultMap = new HashMap<>();
        if(null!=id){
            SysDict sysDict = sysDictManager.getSysDictById(id);
            resultMap.put("sysDict",sysDict);
            if(""!=sysDict.getParentOid()){
                SysDict parentSysDict = sysDictManager.getSysDictByDictOid(sysDict.getParentOid());
                resultMap.put("parentSysDict",parentSysDict);
            }
        }
        if(null!=parentOid){
            SysDict parentSysDict = sysDictManager.getSysDictByDictOid(parentOid);
            resultMap.put("parentSysDict",parentSysDict);
        }
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(resultMap);
        return apiResultSet;
    }
    /**
     * @description:  数字字 典的新增或者修改
     * @param sysDict 数据字典实体类
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<SysDict> saveSysDict(SysDict sysDict){
        if(StrUtil.isEmpty(sysDict.getCreateUserOid())){
            String userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
            sysDict.setCreateUserOid(userOid);
        }
        sysDictManager.saveSysDict(sysDict);
        ApiResultSet<SysDict> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(sysDict);
        return apiResultSet;
    }

    /**
     * @description:  新增或修改数据字典时，验证同一父节点下，是否存在相同的字典代码
     * @param id 实体类主键
     * @param code 字典代码
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<String> checkCode(Long id, String code){
        String message = sysDictManager.checkIsExistesDictCode(id, code);
        ApiResultSet<String> apiResultSet = new ApiResultSet<String>();
        apiResultSet.setData(message);
        return apiResultSet;
    }

    /**
     * @description:  数据字典的信息的删除
     * @param id 数据字典实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<Integer>  deleteSysDictById(Long id,String dictOid){
        int rows = sysDictManager.deleteSysDictById(id,dictOid);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<Integer>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }

    /**
     * @description:  获取数据字典的信息
     * @param id 数据字典实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<SysDict>  getSysDictById(Long id){
        SysDict sysDict = sysDictManager.getSysDictById(id);
        ApiResultSet<SysDict> apiResultSet = new ApiResultSet<SysDict>();
        apiResultSet.setData(sysDict);
        return apiResultSet;
    }

    /**
     * @description:  获取数据字典的信息
     * @param code 数据字典实体类code
     * @author: wuxx
     * @Date: 2020/9/14 10:14
     **/
//    @Idempotent(key = "#code",expireTime = 2,timeUnit = TimeUnit.SECONDS,info = "请勿重复请求！",delKey = false, idempotentReturn = false, limitSession = false)
    @Override
    public ApiResultSet<SysDict> getSysDictByCode(String code){
        SysDict sysDict = sysDictManager.getSysDictByCode(code);
        ApiResultSet<SysDict> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(sysDict);
        return apiResultSet;
    }

    /**
     * @description:  查询数据字典的信息列表
     * @param name 数据字典名称
     * @param code 数据字典编码
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<PageResult<SysDict>> querySysDictWithPage(String name,
                                                                  String code,
                                                                  String parentOid,
                                                                  Integer pageNum, Integer pageSize){
        SysDict sysDict = new SysDict();
        sysDict.setName(name);
        sysDict.setCode(code);
        sysDict.setParentOid(parentOid);
        PageResult<SysDict> pageResult = sysDictManager.querySysDictWithPage(sysDict,pageNum,pageSize);
        ApiResultSet<PageResult<SysDict>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    /**
     * @description:  数据字典的信息的启禁用
     * @param id 数据字典实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<Integer> ableSysDictById(Long id){
        int rows = sysDictManager.ableSysDictById(id);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<Integer>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<SysDict> getSysDictByDictOid(String dictOid) {
        SysDict sysDict = sysDictManager.getSysDictByDictOid(dictOid);
        ApiResultSet<SysDict> apiResultSet = new ApiResultSet<SysDict>();
        apiResultSet.setData(sysDict);
        return apiResultSet;
    }

    /**
     * @description:  根据父类code查询数据字典下级列表
     * @param parentCode 父类code
     * @author: wuxx
     * @Date: 2020/9/14 11:14
     **/
    public ApiResultSet<List<SysDict>> querySysDictListByParentCode(String parentCode){
        SysDict sysDictByCode = sysDictManager.getSysDictByCode(parentCode);
        if(null==sysDictByCode){
            return new ApiResultSet(null);
        }
        List<SysDict> sysDictList = sysDictManager.querySysDictListByParentOid(sysDictByCode.getDictOid());
        return new ApiResultSet(sysDictList);
    }

    /**
     * @description:  根据父类code查询数据字典下级列表
     * @param parentOid 父类oid
     * @author: wuxx
     * @Date: 2020/9/14 11:14
     **/
    public ApiResultSet<List<SysDict>> querySysDictListByParentOid(String parentOid){
        List<SysDict> sysDictList = sysDictManager.querySysDictListByParentOid(parentOid);
        return new ApiResultSet(sysDictList);
    }

    /**
     * @description:  根据dictOids集合查询数据字典列表
     * @param dictOids oid集合
     * @author: wanglei
     * @Date: 2020/10/28 17:14
     **/
    @RequestMapping(value = "/querySysDictListByDictOids")
    public ApiResultSet<List<SysDict>> querySysDictListByDictOids(List<String> dictOids){
        List<SysDict> sysDictList = sysDictManager.querySysDictListByDictOids(dictOids);
        return new ApiResultSet(sysDictList);
    }

    /**
     * @description:  根据父类oid获取数据字典列表，用于生成数据字典树
     * @param parentOid 父类oid
     * @author: wuxx
     * @Date: 2020/9/9 18:14
     **/
    @RequestMapping(value = "/queryDictTree")
    public ApiResultSet queryDictTree(String parentOid){
        List<SysDict> sysDictList = sysDictManager.querySysDictChildrenListByParentOid(parentOid);
        List<TreeSelect> treeSelects = GenDataTreeUtil.buildSysDictTreeSelect(sysDictList);
        return new ApiResultSet(treeSelects);
    }

    @GetMapping(value = "/queryDictListByParentDictText")
    public ApiResultSet queryDictListByParentDictText(String... parentDictText) {
        Map<String, Object> dictList = this.sysDictManager.queryDictListByParentDictText(parentDictText);
        return ApiResultSet.ok(dictList);
    }

    /**
     * @description:  导出成excel
     * @param name 数据字典名称
     * @param code 数据字典编码
     * @author: wuxx
     * @Date: 2020/9/12 14:00
     **/
    @RequestMapping( value = "/listExp",method = {RequestMethod.GET})
    public void listExp(HttpServletResponse response,String name,String code,String parentOid){
        SysDict sysDict = new SysDict();
        sysDict.setName(name);
        sysDict.setCode(code);
        sysDict.setParentOid(parentOid);
        PageResult<SysDict> pageResult = sysDictManager.querySysDictWithPage(sysDict,1,Integer.MAX_VALUE);
        List<SysDict> list = pageResult.getData();
        String title = "数据字典列表";
        String[] rowsName = new String[] { "序号", "字典代码", "字典名称", "备注", "创建时间", "启用状态" };
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < list.size(); i++) {
            SysDict curSysDict = list.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            if (!StrUtil.isEmpty(curSysDict.getCode())) {
                objs[1] = curSysDict.getCode();
            } else {
                objs[1] = "--";
            }
            if (!StrUtil.isEmpty(curSysDict.getName())) {
                objs[2] = curSysDict.getName();
            } else {
                objs[2] = "--";
            }
            if (!StrUtil.isEmpty(curSysDict.getMemo())) {
                objs[3] = curSysDict.getMemo();
            } else {
                objs[3] = "--";
            }
            if (curSysDict.getCreateDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                objs[4] = sdf.format(curSysDict.getCreateDate());
            } else {
                objs[4] = "--";
            }
            objs[5] = BaseStaticParameter.ABLE_MAP
                    .get(curSysDict.getIsAble());
            dataList.add(objs);
        }
        ExportExcelUtil ex = new ExportExcelUtil();
        ex.export(response, title, rowsName, dataList);
    }

}
