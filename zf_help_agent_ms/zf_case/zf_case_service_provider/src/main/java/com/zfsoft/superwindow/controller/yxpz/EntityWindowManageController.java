package com.zfsoft.superwindow.controller.yxpz;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.yxpz.EntityWindowManage;
import com.zfsoft.superwindow.dbaccess.data.DbEntityWindowManage;
import com.zfsoft.superwindow.manager.yxpz.EntityWindowManageManager;
import com.zfsoft.superwindow.service.yxpz.EntityWindowManageService;
import com.zfsoft.superwindow.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: kkfan
 * @create: 2020-10-26 10:33:00
 * @description: 实体窗口管理控制层
 */
@Slf4j
@RestController
public class EntityWindowManageController implements EntityWindowManageService {
    @Resource
    private EntityWindowManageManager entityWindowManageManager;


    @Override
    public ApiResultSet queryPageListWindow(EntityWindowManage entityWindowManage, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EntityWindowManage> sysAreaSiteList = this.entityWindowManageManager.queryList(entityWindowManage);
        PageResult<EntityWindowManage> pageResult = new PageResult<>(((Page) sysAreaSiteList).getPageNum(), ((Page) sysAreaSiteList).getPageSize(), ((Page) sysAreaSiteList).getTotal());
        pageResult.setData(sysAreaSiteList);
        log.info("获取受理辖区列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        return new ApiResultSet(pageResult);
    }

    @Override
    public ApiResultSet saveOrUpdateWindow( EntityWindowManage entityWindowManage) {
        this.entityWindowManageManager.saveOrUpdate(entityWindowManage);
        log.info("受理辖区信息新增/更新成功：{}", JSON.toJSONString(entityWindowManage));
        return new ApiResultSet(entityWindowManage);
    }

    @Override
    public ApiResultSet deleteWindow(String ids) {
        this.entityWindowManageManager.delete(ids);
        return new ApiResultSet(ids);
    }

    @Override
    public ApiResultSet getOneWindow(String id) {
        EntityWindowManage entityWindowManage = this.entityWindowManageManager.getOne(id);
        log.info("详情获取成功：{}", JSON.toJSONString(entityWindowManage));
        return new ApiResultSet(entityWindowManage);
    }

    @Override
    public ApiResultSet queryEntityWindowByWindoNumber(String windoNo) {
        EntityWindowManage windowNo= this.entityWindowManageManager.queryEntityWindowByWindoNo(windoNo);
        log.info("详情获取成功：{}", JSON.toJSONString(windoNo));
        return new ApiResultSet(windowNo);
    }

    @Override
    public ApiResultSet querAllConfigEntityWindow() {
        List<EntityWindowManage> list= this.entityWindowManageManager.querAllyConfigEntityWindow();
        return new ApiResultSet(list);
    }

    @Override
    public ApiResultSet queryWindowByUserOid(String userOid) {
        Boolean flag=this.entityWindowManageManager.queryEntityWindowByUserOid(userOid);
        return new ApiResultSet(flag);
    }

    @Override
    public ApiResultSet<EntityWindowManage> getntityWindowByUserOid(String userOid) {
        DbEntityWindowManage dbentity=this.entityWindowManageManager.getntityWindowByUserOid(userOid);
        if(dbentity!=null){
            EntityWindowManage entity=new EntityWindowManage();
            BeanUtils.copyProperties(dbentity,entity);
            return new ApiResultSet(entity);
        }
        return null;
    }

}
