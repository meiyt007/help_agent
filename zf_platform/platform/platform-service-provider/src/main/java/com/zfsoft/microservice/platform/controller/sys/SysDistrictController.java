package com.zfsoft.microservice.platform.controller.sys;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.microservice.platform.data.vo.SysDistrictHpVo;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysDistrictMapper;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysDistrict;
import com.zfsoft.microservice.platform.manager.sys.SysDistrictManager;
import com.zfsoft.microservice.platform.service.sys.SysDistrictService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.excel.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName SysDistrictController
 * @Description 区划管理的实现类
 * @Author wuxx
 * @Date2020-08-31 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SysDistrictController implements SysDistrictService{
    @Resource
    private SysDistrictManager sysDistrictManager;
    @Resource
    private DbSysDistrictMapper dbSysDistrictMapper;
    /**
     * @description:  初始化区划的信息
     * @param  oid 区划实体类主键
     * @param  parentOid 区划实体父类主键
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    public ApiResultSet initSysDistrict(Long oid,String parentOid){
        Map<String, Object> resultMap = new HashMap<>();
        if(null!=oid){
            SysDistrict sysDistrict = sysDistrictManager.getSysDistrictById(oid);
            resultMap.put("sysDistrict",sysDistrict);
        }
        if(StrUtil.isNotEmpty(parentOid)){
            SysDistrict parentSysDistrict = sysDistrictManager.getSysDistrictByDistrictOid(parentOid);
            resultMap.put("parentSysDistrict",parentSysDistrict);
        }
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(resultMap);
        return apiResultSet;
    }

    /**
     * @description:  区划的新增或者修改
     * @param sysDistrict 区划实体类
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    public ApiResultSet<SysDistrict> saveSysDistrict(SysDistrict sysDistrict){
        sysDistrictManager.saveSysDistrict(sysDistrict);
        ApiResultSet<SysDistrict> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(sysDistrict);
        return apiResultSet;
    }

    /**
     * @description:  区划的信息的删除
     * @param oid 区划实体类主键
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    public ApiResultSet<Integer>  deleteSysDistrictById(Long oid){
        int rows = sysDistrictManager.deleteSysDistrictById(oid);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<Integer>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }


    /**
     * @description:  获取区划的信息
     * @param oid 区划实体类主键
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    public ApiResultSet<SysDistrict>  getSysDistrictById(Long oid){
        SysDistrict sysDistrict = sysDistrictManager.getSysDistrictById(oid);
        ApiResultSet<SysDistrict> apiResultSet = new ApiResultSet<SysDistrict>();
        apiResultSet.setData(sysDistrict);
        return apiResultSet;
    }

    /**
     * @description:  获取区划的信息
     * @param districtOid 区划实体类业务主键
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    public ApiResultSet<SysDistrict>  getSysDistrictByDistrictOid(String districtOid){
        SysDistrict sysDistrict = sysDistrictManager.getSysDistrictByDistrictOid(districtOid);
        ApiResultSet<SysDistrict> apiResultSet = new ApiResultSet<SysDistrict>();
        apiResultSet.setData(sysDistrict);
        return apiResultSet;
    }

    /**
     * @description:  获取区划的信息
     * @param code 区划实体类业务编码
     * @author: wuxx
     * @Date: 2021/2/3 10:14
     **/
    @Override
    public ApiResultSet<SysDistrict> getSysDistrictByDistrictCode(String code) {
        SysDistrict sysDistrict = sysDistrictManager.getSysDistrictByDistrictCode(code);
        return  new ApiResultSet<>(sysDistrict);
    }

    /**
     * @description:  根据父类oid查询区划
     * @param parentOid 父类oid
     * @author: wuxx
     * @Date: 2020/10/31 17:08
     **/
    @Override
    public ApiResultSet<List<SysDistrict>> querySysDistrictListByParentOid(String parentOid) {
        List<SysDistrict> districtList = sysDistrictManager.querySysDistrictListByParentOid(parentOid);
        return new ApiResultSet<>(districtList);
    }

    /**
     * @description:  查询区划的信息列表
     * @param name 区划名称
     * @param code 区划代码
     * @param parentOid 区划父类oid
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    public ApiResultSet querySysDistrictWithPage(String name,String code,String parentOid,Integer pageNum,
                                                                     Integer pageSize){
        SysDistrict sysDistrict = new SysDistrict();
        sysDistrict.setName(name);
        sysDistrict.setParentOid(parentOid);
        sysDistrict.setCode(code);
        PageResult<SysDistrict> pageResult = sysDistrictManager.querySysDistrictWithPage(sysDistrict,pageNum,pageSize);
        return new ApiResultSet<>(pageResult);
    }

    /**
     * @description:  区划的信息的启禁用
     * @param oid 区划实体类主键
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    public ApiResultSet<Integer> ableSysDistrictById(@PathVariable("oid")Long oid){
        int rows = sysDistrictManager.ableSysDistrictById(oid);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<Integer>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }

    /**
     * @description:  查询区划的信息列表
     * @param name 区划名称
     * @param code 区划代码
     * @param parentOid 区划父类oid
     * @author: wuxx
     * @Date: 2020/9/18 10:14
     **/
    @RequestMapping( value = "/listExp",method = {RequestMethod.GET})
    public void listExp(HttpServletResponse response,String name,String code,String parentOid){
        SysDistrict sysDistrict = new SysDistrict();
        sysDistrict.setName(name);
        sysDistrict.setParentOid(parentOid);
        sysDistrict.setCode(code);
        PageResult<SysDistrict> pageResult = sysDistrictManager.querySysDistrictWithPage(sysDistrict,1,Integer.MAX_VALUE);
        List<SysDistrict> list = pageResult.getData();
        String title = "区划列表";
        String[] rowsName = new String[] { "序号", "区划代码", "区划名称", "上级区划名称", "级别", "启用状态", "排序号" };
        List<Object[]> dataList = new ArrayList<Object[]>();
        /**
         * 使用ExportExcelUtil注意以下两点： rowsName.length=objs.lenth;
         * 数据不能为空objs[i].value!=null&&objs[i].value.trim()!=''
         */
        Object[] objs = null;
        for (int i = 0; i < list.size(); i++) {
            SysDistrict sysdistrict = list.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            if (!StrUtil.isEmpty(sysdistrict.getCode())) {
                objs[1] = sysdistrict.getCode();
            } else {
                objs[1] = "--";
            }
            if (!StrUtil.isEmpty(sysdistrict.getCode())) {
                objs[2] = sysdistrict.getName();
            } else {
                objs[2] = "-";
            }
            if (sysdistrict.getParentName() != null
                    && !StrUtil.isEmpty(sysdistrict.getParentName())) {
                objs[3] = sysdistrict.getParentName();
            } else {
                objs[3] = "-";
            }
            if (sysdistrict.getLevelDictName() != null) {
                objs[4] = sysdistrict.getLevelDictName();
            } else {
                objs[4] = "-";
            }
            if (null!=sysdistrict.getIsAble()) {
                objs[5] = BaseStaticParameter.ABLE_MAP.get(sysdistrict.getIsAble());
            } else {
                objs[5] = "-";
            }
            if (!StrUtil.isEmpty(String.valueOf(sysdistrict.getSort()))) {
                objs[6] = sysdistrict.getSort();
            } else {
                objs[6] = "-";
            }
            dataList.add(objs);
        }
        ExportExcelUtil ex = new ExportExcelUtil();
        ex.export(response, title, rowsName, dataList);
    }

    @Override
    public ApiResultSet<List<SysDistrict>> getSysDistrictListByPath(String path) {
        List<SysDistrict> list = sysDistrictManager.getSysDistrictListByPath(path);
        return new ApiResultSet<>(list);
    }

    @Override
    public ApiResultSet<SysDistrict> getTopSysDistrict() {
        SysDistrict topSysDistrict = sysDistrictManager.getTopSysDistrict();
        return new ApiResultSet<>(topSysDistrict);
    }

    @Override
    public ApiResultSet<List<SysDistrict>> querySysDistrictListByLevelDictOid(String levelDictOid) {
        List<SysDistrict> districtList = sysDistrictManager.querySysDistrictListByLevelDictOid(levelDictOid);
        return new ApiResultSet<>(districtList);
    }

    @Override
    public ApiResultSet<List<SysDistrict>> queryDistrictSimpleTreeList(String districtOid) {
        List<SysDistrict> sysDistrictList = sysDistrictManager.queryDistrictSimpleTreeList(districtOid);
        return new ApiResultSet<>(sysDistrictList);
    }

    @Override
    public ApiResultSet<List<String>> queryPathListByDistrictOid(String districtOid) {
        List<SysDistrict> list = sysDistrictManager.getSysDistrictListByPath(districtOid);
        List<String> districtOidList = list.stream()
                .map(SysDistrict::getDistrictOid)
                .collect(Collectors.toList());
        return new ApiResultSet<>(districtOidList);
    }

    @Override
    public ApiResultSet selectByDistrictOid(String districtOid) {
        SysDistrictHpVo sysDistrictHpVo = new SysDistrictHpVo();
        DbSysDistrict dbSysDistrict = dbSysDistrictMapper.selectByDistrictOid(districtOid);
        if(dbSysDistrict == null)
            return null;
        BeanUtils.copyProperties(dbSysDistrict, sysDistrictHpVo);
        return ApiResultSet.ok("区划接口调用成功",sysDistrictHpVo);
    }



}
