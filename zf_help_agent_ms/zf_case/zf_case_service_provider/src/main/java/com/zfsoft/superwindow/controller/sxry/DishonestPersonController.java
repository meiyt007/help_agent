package com.zfsoft.superwindow.controller.sxry;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.sxry.DishonestPerson;
import com.zfsoft.superwindow.manager.sxry.DishonestPersonmanager;
import com.zfsoft.superwindow.service.sxry.DishonestPersonService;
import com.zfsoft.superwindow.util.SysCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname DishonestPersonController
 * @Description 失信人员管理控制层
 * @Date 2021-01-11 10:05
 * @Created by liyanqing
 */
@Slf4j
@RestController
public class DishonestPersonController implements DishonestPersonService {

    @Resource
    private DishonestPersonmanager dishonestPersonmanager;

    @Override
    public ApiResultSet queryPageList(DishonestPerson dishonestPerson, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<DishonestPerson> dishonestPersonList = dishonestPersonmanager.queryPageList(dishonestPerson);
        PageResult<DishonestPerson> pageResult = new PageResult<>(((Page) dishonestPersonList).getPageNum()
                , ((Page) dishonestPersonList).getPageSize(), ((Page) dishonestPersonList).getTotal());
        pageResult.setData(dishonestPersonList);
        log.info("获取失信人员列表");
        return new ApiResultSet(pageResult);
    }

    @Override
    public ApiResultSet<DishonestPerson> getOne(Long id) {
        DishonestPerson dishonestPerson = dishonestPersonmanager.getOne(id);
        log.info("获取失信人员信息");
        return new ApiResultSet(dishonestPerson);
    }

    @Override
    public ApiResultSet saveOrUpdate(DishonestPerson dishonestPerson) {
        if (null == dishonestPerson.getId()) {
            //新增、判断是否已存在
            DishonestPerson unRevoke = dishonestPersonmanager.getOneUnRevoke(dishonestPerson.getName(), dishonestPerson.getCardNumber());
            if (null != unRevoke) {
                //已存在、则更新
                unRevoke.setPhone(dishonestPerson.getPhone());
                String oid = this.dishonestPersonmanager.saveOrUpdate(unRevoke);
                log.info("调用失信人员执行信息新增/更新接口成功：{}", JSON.toJSONString(dishonestPerson));
                return new ApiResultSet(oid);
            } else {
                String oid = this.dishonestPersonmanager.saveOrUpdate(dishonestPerson);
                log.info("调用失信人员执行信息新增/更新接口成功：{}", JSON.toJSONString(dishonestPerson));
                return new ApiResultSet(oid);
            }
        } else {
            String oid = this.dishonestPersonmanager.saveOrUpdate(dishonestPerson);
            log.info("调用失信人员执行信息新增/更新接口成功：{}", JSON.toJSONString(dishonestPerson));
            return new ApiResultSet(oid);
        }
    }

    @Override
    public ApiResultSet dishonestCancel(DishonestPerson dishonestPerson) {
        dishonestPerson.setStatus(SysCode.DELETE_STATUS.YES);
        this.dishonestPersonmanager.saveOrUpdate(dishonestPerson);
        return new ApiResultSet(200);
    }

    @Override
    public ApiResultSet delete(Long id) {
        DishonestPerson dishonestPerson = dishonestPersonmanager.getOne(id);
        dishonestPerson.setDelFlag(SysCode.DELETE_STATUS.YES);
        this.dishonestPersonmanager.saveOrUpdate(dishonestPerson);
        return new ApiResultSet(200);
    }

    @Override
    public ApiResultSet<DishonestPerson> getDishonestPerson(String name, String cardNumber) {
        DishonestPerson list=this.dishonestPersonmanager.getOneUnRevoke(name,cardNumber);
        if (list != null) {
            return new ApiResultSet(list);
        }
        return new ApiResultSet();
    }
}
