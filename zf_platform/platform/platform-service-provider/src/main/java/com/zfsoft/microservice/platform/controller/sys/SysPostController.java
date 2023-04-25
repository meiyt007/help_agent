package com.zfsoft.microservice.platform.controller.sys;

import com.zfsoft.microservice.platform.data.sys.SysPost;
import com.zfsoft.microservice.platform.manager.sys.SysPostManager;
import com.zfsoft.microservice.platform.service.sys.SysPostService;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SysPostController
 * @Description 岗位管理的实现类
 * @Author wuxx
 * @Date 2020-09-12 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SysPostController implements SysPostService{

    @Autowired
    private SysPostManager sysPostManager;

    /**
     * @param oid 岗位主键
     * @description: 初始化区划的信息
     * @author: wuxx
     * @Date: 2020/01/21 16:14
     **/
    @RequestMapping(value = {"/init/{oid}"}, method = {RequestMethod.GET})
    public ApiResultSet<SysPost> initSysPost(@PathVariable(value="oid",required=false) Long oid) {
        SysPost sysPost = sysPostManager.getSysPostById(oid);
        return new ApiResultSet<>(sysPost);
    }

    /**
     * @param sysPost 岗位实体类
     * @description: 岗位的新增或者修改
     * @author: wuxx
     * @Date: 2020/01/21 16:14
     **/
    @Override
    public ApiResultSet<SysPost> saveSysPost(SysPost sysPost) {
        sysPostManager.saveSysPost(sysPost);
        ApiResultSet<SysPost> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(sysPost);
        return apiResultSet;
    }

    /**
     * @param oid 岗位实体类主键
     * @description: 岗位的信息的删除
     * @author: wuxx
     * @Date: 2020/01/21 16:14
     **/
    @Override
    public ApiResultSet<Integer> deleteSysPostById(Long oid) {
        int rows = sysPostManager.deleteSysPostById(oid);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<Integer>();
        if (BaseStaticParameter.Y == rows){
            return apiResultSet;
        }else {
            apiResultSet.setCode(ApiResultSet.DIRTY_DATA_TITLE);
            apiResultSet.setMessage("删除失败！");
            return apiResultSet;
        }
    }

    /**
     * @param id 岗位实体类主键
     * @description: 获取岗位的信息
     * @author: wuxx
     * @Date: 2020/01/21 16:14
     **/
    @Override
    public ApiResultSet<SysPost> getSysPostById(Long id) {
        SysPost sysPost = sysPostManager.getSysPostById(id);
        return new ApiResultSet<>(sysPost);
    }

    /**
     * @param name 岗位名称
     * @description: 查询岗位的信息列表
     * @author: wuxx
     * @Date: 2020/01/21 16:14
     **/
    @RequestMapping(value = "/page", method = {RequestMethod.GET})
    public ApiResultSet querySysPostWithPage(String name, String districtOid,String organOid, Integer pageNum,Integer pageSize) {
        SysPost sysPost = new SysPost();
        sysPost.setName(name);
        sysPost.setDistrictOid(districtOid);
        sysPost.setOrganOid(organOid);
        PageResult<SysPost> pageResult = sysPostManager.querySysPostWithPage(sysPost, pageNum, pageSize);
        return new ApiResultSet<>(pageResult);
    }
    /**
     * @description: 根据区划oid和组织机构oid查询岗位列表
     * @param districtOid 区划oid
     * @param organOid 组织机构oid
     * @author: wuxx
     * @Date: 2021/1/21 16:20
     **/
    @Override
    public ApiResultSet<List<SysPost>> querySysPostList(String districtOid, String organOid) {
        SysPost sysPost = new SysPost();
        sysPost.setDistrictOid(districtOid);
        sysPost.setOrganOid(organOid);
        List<SysPost> sysPosts = sysPostManager.querySysPostList(sysPost);
        return new ApiResultSet<>(sysPosts);
    }

    /**
     * @param oid 岗位实体类主键
     * @description: 岗位的信息的启禁用
     * @author: wuxx
     * @Date: 2020/01/21 16:14
     **/
    @Override
    public ApiResultSet<Integer> ableSysPostById(Long oid) {
        int rows = sysPostManager.ableSysPostById(oid);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<Integer>();
        if (BaseStaticParameter.Y == rows){
            return apiResultSet;
        }else {
            apiResultSet.setCode(ApiResultSet.DIRTY_DATA_TITLE);
            apiResultSet.setMessage("禁用失败！");
            return apiResultSet;
        }
    }

    /**
     * @param name 岗位名称
     * @description: 导出成excel
     * @author: wuxx
     * @Date: 2020/9/10 10:00
     **/
    public void listExp(HttpServletResponse response, String name,String districtOid,String organOid, Integer pageNum,
                        Integer pageSize) {
        SysPost sysPost = new SysPost();
        sysPost.setName(name);
        sysPost.setDistrictOid(districtOid);
        sysPost.setOrganOid(organOid);
        PageResult<SysPost> pageResult = sysPostManager.querySysPostWithPage(sysPost, pageNum, pageSize);
        List<SysPost> list = pageResult.getData();
        String title = "岗位管理列表";
        String[] rowsName = new String[] { "序号", "所属区划", "所属机构", "岗位名称",
                "排序号", "启用状态" };
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < list.size(); i++) {
            SysPost sysPostN = list.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = sysPostN.getDistrictName();
            objs[2] = sysPostN.getOrganName();
            objs[3] = sysPostN.getName();
            objs[4] = sysPostN.getSort();
            objs[5] = BaseStaticParameter.ABLE_MAP
                    .get(sysPostN.getIsAble());
            dataList.add(objs);
        }
        ExportExcelUtil ex = new ExportExcelUtil();
        ex.export(response, title, rowsName, dataList);
    }

}
