package com.zfsoft.service.controller.sxService;


import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.service.manager.sxService.SxFieldTypeManager;
import com.zfsoft.service.sxService.data.SxFieldType;
import com.zfsoft.service.sxService.service.SxFieldTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName SxFieldTypeController
 * @Description 字段类别管理的实现类
 * @Author wuxx
 * @Date 2021-03-11 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SxFieldTypeController implements SxFieldTypeService {

    @Resource
    private SxFieldTypeManager sxFieldTypeManager;

    /**
     * @description:  查询字段类别管理的信息列表
     * @param fieldTypeName 类别名称
     * @author: wuxx
     * @Date: 2021/7/14 14:14
     **/
    @Override
    public ApiResultSet<PageResult<SxFieldType>> querySxFieldTypeWithPage(String fieldTypeName, Integer isAble, Integer pageNum,
                                                                          Integer pageSize){
        SxFieldType sxFieldType = new SxFieldType();
        sxFieldType.setFieldTypeName(fieldTypeName);
        sxFieldType.setIsAble(isAble);
        PageResult<SxFieldType> pageResult = sxFieldTypeManager.querySxFieldTypeWithPage(sxFieldType,pageNum,pageSize);
        return new ApiResultSet<>(pageResult);
    }

    /**
     * @description:  初始化字段类别管理的信息
     * @param id 字段类别管理实体类主键
     * @author: wuxx
     * @Date: 2021/7/14 14:14
     **/
    @Override
    public ApiResultSet initSxFieldType(Long id){
        SxFieldType  sxFieldType = sxFieldTypeManager.getSxFieldTypeById(id);
        return new ApiResultSet<>(sxFieldType);
    }

    /**
     * @description:  字段类别管理的新增或者修改
     * @param sxFieldType 字段类别管理实体类
     * @author: wuxx
     * @Date: 2021/7/14 14:14
     **/
    @Override
    public ApiResultSet<SxFieldType> saveSxFieldType(@RequestBody SxFieldType sxFieldType){
        if(null==sxFieldType.getCreateUserOid()){
            sxFieldType.setCreateUserOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
        }
        sxFieldTypeManager.saveSxFieldType(sxFieldType);
        return  new ApiResultSet<>(sxFieldType);
    }

    /**
     * @description:  获取字段类别管理的信息
     * @param id 字段类别管理实体类主键
     * @author: wuxx
     * @Date: 2021/7/14 14:14
     **/
    @Override
    public ApiResultSet<SxFieldType> getSxFieldTypeById(Long id){
        SxFieldType sxFieldType = sxFieldTypeManager.getSxFieldTypeById(id);
        return new ApiResultSet<>(sxFieldType);
    }

    /**
     * @description:  获取字段类别管理的信息
     * @param fieldTypeOid 授权key
     * @author: wuxx
     * @Date: 2021/7/14 14:14
     **/
    @Override
    public ApiResultSet<SxFieldType> getSxFieldTypeByFieldTypeOid(String fieldTypeOid){
        SxFieldType sxFieldType = sxFieldTypeManager.getSxFieldTypeByFieldTypeOid(fieldTypeOid);
        return new ApiResultSet<>(sxFieldType);
    }

    /**
     * @description:  字段类别管理的信息的删除
     * @param id 字段类别管理实体类主键
     * @author: wuxx
     * @Date: 2021/7/14 14:14
     **/
    @Override
    public ApiResultSet<Integer> deleteSxFieldTypeById(Long id){
        sxFieldTypeManager.deleteSxFieldTypeById(id);
        return new ApiResultSet<>();
    }
    /**
     * @description:  字段类别管理的信息的启禁用
     * @param id 字段类别管理实体类主键
     * @author: wuxx
     * @Date: 2021/7/14 14:14
     **/
    @Override
    public ApiResultSet<Integer> ableSxFieldTypeById(Long id){
        sxFieldTypeManager.ableSxFieldTypeById(id);
        return new ApiResultSet<>();
    }

    /**
     * @description:  根据类别主键查询字段类别下级列表
     * @param parentOid 父类类别主键
     * @author: wuxx
     * @Date: 2021/03/10 16:14
     **/
    @Override
    public ApiResultSet<List<SxFieldType>> querySxFieldTypeList(String parentOid,Integer isAble) {
        SxFieldType sxFieldType = new SxFieldType();
        sxFieldType.setParentOid(parentOid);
        sxFieldType.setIsAble(isAble);
        List<SxFieldType> sxFieldTypeList = sxFieldTypeManager.querySxFieldTypeList(sxFieldType);
        return  new ApiResultSet<>(sxFieldTypeList);
    }


    /**
     * @description: 根据类别主键查询一级字段类别
     * @author: wuxx
     * @Date: 2021/7/14 16:14
     **/
    @Override
    public ApiResultSet<List<SxFieldType>> queryTopSxFieldTypeList(String fieldTypeName,Integer isAble) {
        SxFieldType sxFieldType = new SxFieldType();
        sxFieldType.setFieldTypeName(fieldTypeName);
        sxFieldType.setIsAble(isAble);
        List<SxFieldType> sxFieldTypeList = sxFieldTypeManager.queryTopSxFieldTypeList(sxFieldType);
        return  new ApiResultSet<>(sxFieldTypeList);
    }

    @Override
    public ApiResultSet<List<SxFieldType>> querySxFieldTypeListByServiceOid(String serviceOid) {
        List<SxFieldType> sxFieldTypeList = sxFieldTypeManager.querySxFieldTypeListByServiceOid(serviceOid);
        return  new ApiResultSet<>(sxFieldTypeList);
    }

    @Override
    public ApiResultSet<List<SxFieldType>> queryComboFieldTypeListByThingOid(String thingOid) {
        List<SxFieldType> sxFieldTypeList = sxFieldTypeManager.queryComboFieldTypeListByThingOid(thingOid);
        return  new ApiResultSet<>(sxFieldTypeList);
    }
}
