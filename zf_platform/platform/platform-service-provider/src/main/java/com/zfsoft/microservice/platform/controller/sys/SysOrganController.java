package com.zfsoft.microservice.platform.controller.sys;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.microservice.platform.data.vo.SysOrganVo;
import com.zfsoft.microservice.platform.manager.sys.SysOrganManager;
import com.zfsoft.microservice.platform.manager.sys.SysUserManager;
import com.zfsoft.microservice.platform.service.sys.SysOrganService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.excel.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @ClassName SysOrganController
 * @Description 组织机构管理的实现类
 * @Author wuxx
 * @Date2020-08-31 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SysOrganController implements SysOrganService{

    @Resource
    private SysOrganManager sysOrganManager;

    @Autowired
    private SysUserManager sysUserService;

    /**
     * @description:  初始化区划的信息
     * @param oid 组织机构主键
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    public ApiResultSet  initSysDistrict(Long oid){
        SysOrgan organ = null;
        if(null!=oid){
            organ = sysOrganManager.getSysOrganById(oid);
        }
        return  new ApiResultSet<>(organ);
    }

    /**
     * @description:  组织机构的新增或者修改
     * @param sysOrgan 组织机构实体类
     * @author: wuxx
     * @Date: 2020/9/2 10:14
     **/
    public ApiResultSet<SysOrgan> saveSysOrgan( SysOrgan sysOrgan){
        sysOrganManager.saveSysOrgan(sysOrgan);
        ApiResultSet<SysOrgan> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(sysOrgan);
        return apiResultSet;
    }

    /**
     * @description:  组织机构的信息的删除
     * @param oid 组织机构实体类主键
     * @author: wuxx
     * @Date: 2020/9/2 10:14
     **/
    public ApiResultSet<Integer>  deleteSysOrganById(Long oid){
        int rows = sysOrganManager.deleteSysOrganById(oid);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<Integer>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<SysOrgan> getSysOrganByOrganOid(String organOid) {
        if(StrUtil.isEmpty(organOid)){
            return  new ApiResultSet<>();
        }
        SysOrgan organ = sysOrganManager.getSysOrganByOrganOid(organOid);
        return new ApiResultSet<>(organ);
    }
    /**
     * @description:  根据父类oid查询机构
     * @param parentOid 父类oid
     * @author: wuxx
     * @Date: 2020/10/31 17:08
     **/
    @Override
    public ApiResultSet<List<SysOrgan>> querySysOrganListByParentOid(String parentOid) {
        List<SysOrgan> organList = sysOrganManager.querySysOrganListByParentOid(parentOid);
        return new ApiResultSet<>(organList);
    }

    @Override
    public ApiResultSet<SysOrgan> querySysOrganByCode(String code) {
        SysOrgan sysOrgan = sysOrganManager.querySysOrganByCode(code);
        return new ApiResultSet<>(sysOrgan);
    }

    /**
     * @description:  获取组织机构的信息
     * @param oid 组织机构实体类主键
     * @author: wuxx
     * @Date: 2020/9/2 10:14
     **/
    @RequestMapping( value = "/getOne/{oid}",method = {RequestMethod.GET})
    public ApiResultSet<SysOrgan>  getSysOrganById(@PathVariable("oid")Long oid){
        SysOrgan sysOrgan = sysOrganManager.getSysOrganById(oid);
        ApiResultSet<SysOrgan> apiResultSet = new ApiResultSet<SysOrgan>();
        apiResultSet.setData(sysOrgan);
        return apiResultSet;
    }
    /**
     * @description:  查询组织机构的信息列表
     * @param name 组织机构名称
     * @param districtOid 区划id
     * @param parentOid 组织机构父类oid
     * @author: wuxx
     * @Date: 2020/9/2 10:14
     **/
    public ApiResultSet querySysOrganShieldWithPage(String name, String districtOid,String parentOid,String shield,Integer pageNum,
                                              Integer pageSize){
        SysOrgan sysOrgan = new SysOrgan();
        sysOrgan.setParentOid(parentOid);
        sysOrgan.setName(name);
        sysOrgan.setDistrictOid(districtOid);
        sysOrgan.setShield(shield);
        PageResult<SysOrgan> pageResult = sysOrganManager.querySysOrganShieldWithPage(sysOrgan,pageNum,pageSize);
        return new ApiResultSet<>(pageResult);
    }

    /**
     * @description:  查询组织机构的信息列表
     * @param name 组织机构名称
     * @param districtOid 区划id
     * @param parentOid 组织机构父类oid
     * @author: wuxx
     * @Date: 2020/9/2 10:14
     **/
    public ApiResultSet querySysOrganWithPage(String name, String districtOid,String parentOid,Integer pageNum,
                                                                 Integer pageSize){
        SysOrgan sysOrgan = new SysOrgan();
        sysOrgan.setParentOid(parentOid);
        sysOrgan.setName(name);
        sysOrgan.setDistrictOid(districtOid);
        PageResult<SysOrgan> pageResult = sysOrganManager.querySysOrganWithPage(sysOrgan,pageNum,pageSize);
        return new ApiResultSet<>(pageResult);
    }

    /**
     * @description:  组织机构的信息的启禁用
     * @param oid 组织机构实体类主键
     * @author: wuxx
     * @Date: 2020/9/2 10:14
     **/
    public ApiResultSet<Integer> ableSysOrganById(Long oid){
        int rows = sysOrganManager.ableSysOrganById(oid);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<Integer>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }

    /**
     * @description:  查看组织机构管理用户
     * @param organOid 组织机构oid
     * @author: wuxx
     * @Date: 2020/9/10 10:00
     **/
    public ApiResultSet userListByOrgan(String organOid, Integer pageNum,Integer pageSize){
        SysUser sysUser = new SysUser();
        sysUser.setOrganOid(organOid);
        PageResult<SysUser> sysUserPageResult = sysUserService.querySysUserWithPage(sysUser, pageNum, pageSize);
        return new ApiResultSet<>(sysUserPageResult);
    }

    /**
     * @description:  导出成excel
     * @param name 组织机构名称
     * @param districtOid 区划id
     * @param parentOid 组织机构父类oid
     * @author: wuxx
     * @Date: 2020/9/10 10:00
     **/
    public void listExp(String name, String districtOid,String parentOid){
        SysOrgan sysOrgan = new SysOrgan();
        sysOrgan.setParentOid(parentOid);
        sysOrgan.setName(name);
        sysOrgan.setDistrictOid(districtOid);
        PageResult<SysOrgan> pageResult = sysOrganManager.querySysOrganWithPage(sysOrgan,1,Integer.MAX_VALUE);
        List<SysOrgan> list = pageResult.getData();
        String title = "组织机构列表";
        String[] rowsName = new String[] { "序号", "所属区划", "组织机构名称","组织机构全称", "组织机构代码",
                "组织机构电话", "组织机构类别", "启用状态", "排序号" };
        List<Object[]> dataList = new ArrayList<Object[]>();
        /**
         * 使用ExportExcelUtil注意以下两点： rowsName.length=objs.lenth;
         * 数据不能为空objs[i].value!=null&&objs[i].value.trim()!=''
         */
        Object[] objs = null;
        for (int i = 0; i < list.size(); i++) {
            SysOrgan organN = list.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            String distictName = organN.getDistrictName();
            if (!StrUtil.isEmpty(distictName)) {
                objs[1] = distictName;
            } else {
                objs[1] = "-";
            }
            objs[2] = organN.getName();
            if (!StrUtil.isEmpty(organN.getFullName())) {
                objs[3] = organN.getFullName();
            } else {
                objs[3] = "-";
            }
            objs[4] = organN.getCode();

            if (!StrUtil.isEmpty(organN.getTelphone())) {
                objs[5] = organN.getTelphone();
            } else {
                objs[5] = "-";
            }
            String typeName = "";
            if (StrUtil.isNotEmpty(organN.getTypeDictName())) {
                typeName = organN.getTypeDictName();
            }
            if (!StrUtil.isEmpty(typeName)) {
                objs[6] = typeName;
            } else {
                objs[6] = "-";
            }

            if (null!=organN.getIsAble()) {
                objs[7] = BaseStaticParameter.ABLE_MAP
                        .get(organN.getIsAble());
            } else {
                objs[7] = "-";
            }
            objs[8] = organN.getSort();

            /*
             * SimpleDateFormat df = new SimpleDateFormat(
             * "yyyy-MM-dd HH:mm:ss"); String date = df.format(new Date());
             * objs[8] = date;
             */
            dataList.add(objs);
        }
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        ExportExcelUtil ex = new ExportExcelUtil();
        ex.export(response, title, rowsName, dataList);
    }

    @Override
    public ApiResultSet<List<String>> getOrganNameListByOids(List<String> oids) {
        List<String> list = sysOrganManager.getOrganNameListByOids(oids);
        return new ApiResultSet<>(list);
    }

    @Override
    public ApiResultSet<List<SysOrgan>> querySysOrganListByDistrictOid(String districtOid) {
        List<SysOrgan> organList = sysOrganManager.querySysOrganListByDistrictOid(districtOid);
        return new ApiResultSet<>(organList);
    }

    @Override
    public ApiResultSet synSaveSysOrgan(SysOrganVo sysOrganVo) {
        Map<String,Object> map = new HashMap<>();
        try {
            Long id = sysOrganManager.synSaveSysOrgan(sysOrganVo);
            map.put("organId",id);
        }catch (Exception e){
            return new ApiResultSet<>(500,"接口调用失败");
        }
        return ApiResultSet.ok("接口调用成",map);
    }

    /**
     * 获取机构分组列表
     * @author wangyh
     * @Date: 2020/9/09 14:14
     */
    @Override
    public ApiResultSet<List<SysOrgan>> queryOrganSelectOptions() {
        log.info("获取机构分组列表");
        List<SysOrgan> organList = sysOrganManager.querySysOrganList();
        return ApiResultSet.ok("获取机构分组列表成功",organList);
    }
    /**
     * @description:  批量删除用户信息
     * @param ids 主键
     * @return
     */
    @Override
    public ApiResultSet shieldOrganListid(List<Long> ids,String isshield) throws Exception {
        log.info("批量删除用户信息，ids:{}",ids);
        ApiResultSet<SysOrgan> apiResultSet = new ApiResultSet<>();
        sysOrganManager.shieldid( ids,isshield);
        apiResultSet.setMessage("批量屏蔽成功");
        return apiResultSet;
    }

    /**
     * @description:  屏蔽部门
     * @param id 账号
     * @author: wangyh
     * @Date: 2022/7/15
     **/
    @Override
    public ApiResultSet shieldById(Long id,String isshield) throws Exception{
        log.info("屏蔽部门，id:{}",id);
        SysOrgan sysOrgan = sysOrganManager.shieldId(id,isshield);
        log.debug("sysOrgan结果集：sysOrgan:{}",sysOrgan);
        ApiResultSet<SysOrgan> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(sysOrgan);
        apiResultSet.setMessage("屏蔽部门成功");
        apiResultSet.setTime(String.valueOf(new Date()));
        return apiResultSet;
    }
}
