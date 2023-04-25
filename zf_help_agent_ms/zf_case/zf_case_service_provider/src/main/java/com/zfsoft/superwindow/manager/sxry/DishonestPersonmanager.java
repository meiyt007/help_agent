package com.zfsoft.superwindow.manager.sxry;

import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.sxry.DishonestPerson;
import com.zfsoft.superwindow.dbaccess.dao.DbDishonestPersonMapper;
import com.zfsoft.superwindow.dbaccess.data.DbDishonestPerson;
import com.zfsoft.superwindow.dbaccess.data.DbDishonestPersonExample;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.StringUtils;
import com.zfsoft.superwindow.util.SysCode;
import com.zfsoft.superwindow.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Classname DishonestPersonmanager
 * @Description 失信人员管理
 * @Date 2021-01-11 11:04
 * @Created by liyanqing
 */
@Service
@Slf4j
public class DishonestPersonmanager {

    @Resource
    private DbDishonestPersonMapper dbDishonestPersonMapper;
    @Resource
    private DishonestRecordManager dishonestRecordManager;

    /*
     * @Description: 获取失信人员列表
     * @Author: liyanqing
     * @Date: 2021-01-11 11:10
     * @param dishonestPerson:
     * @return: java.util.List<com.zfsoft.data.tcbl.DishonestPerson>
     **/
    public List<DishonestPerson> queryPageList(DishonestPerson dishonestPerson) {
        DbDishonestPersonExample personExample = new DbDishonestPersonExample();
        DbDishonestPersonExample.Criteria criteria = personExample.createCriteria();
        if (StringUtils.isNotEmpty(dishonestPerson.getName())) {
            //失信人名称
            criteria.andNameLike("%" + dishonestPerson.getName().trim() + "%");
        }
        if (!Objects.isNull(dishonestPerson.getStatus())) {
            //失信人状态
            criteria.andStatusEqualTo(dishonestPerson.getStatus());
        } else {
            criteria.andStatusEqualTo(SysCode.DELETE_STATUS.NO);
        }
        //未删除
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        personExample.setOrderByClause(" CREATE_DATE DESC ");
        List<DbDishonestPerson> dbDishonestPeople = dbDishonestPersonMapper.selectByExample(personExample);
        return BeanUtils.copyListProperties(dbDishonestPeople, DishonestPerson::new);
    }

    /*
     * @Description: 获取失信人员信息
     * @Author: liyanqing
     * @Date: 2021-01-11 13:38
     * @param id: 逻辑主键
     * @return: com.zfsoft.data.tcbl.DishonestPerson
     **/
    public DishonestPerson getOne(Long id) {
        Assert.hasLength(String.valueOf(id), "主键不能为空！");
        DbDishonestPerson person = dbDishonestPersonMapper.selectByPrimaryKey(id);
        DishonestPerson dishonestPerson = new DishonestPerson();
        BeanUtils.copyProperties(person, dishonestPerson);
        return dishonestPerson;
    }

    /**
     * 根据主键id查询信息
     * @param id
     * @return
     */
    private DbDishonestPerson selectByPrimaryKey(Long id) {
        Assert.notNull(String.valueOf(id), "查询id不能为空！");
        DbDishonestPersonExample example = new DbDishonestPersonExample();
        DbDishonestPersonExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbDishonestPerson> dbDishonestPeople = dbDishonestPersonMapper.selectByExample(example);
        return CollectionUtils.isEmpty(dbDishonestPeople) ? null : dbDishonestPeople.get(0);
    }

    /*
     * @Description:
     * @Author: liyanqing
     * @Date: 2021-01-20 10:12
     * @param id: 获取执行失信人员
     * @return: com.zfsoft.data.sxry.DishonestPerson
     **/
    public DishonestPerson getOneUnRevoke(String name, String cardNumber) {
        DbDishonestPersonExample example = new DbDishonestPersonExample();
        DbDishonestPersonExample.Criteria criteria = example.createCriteria();
//        criteria.andIdEqualTo(id);
        criteria.andNameEqualTo(name);
        criteria.andCardNumberEqualTo(cardNumber);
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        criteria.andStatusEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbDishonestPerson> dbDishonestPeople = dbDishonestPersonMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(dbDishonestPeople)) {
            return null;
        } else {
            DbDishonestPerson dbDishonestPerson = dbDishonestPeople.get(0);
            DishonestPerson person = new DishonestPerson();
            BeanUtils.copyProperties(dbDishonestPerson, person);
            return person;
        }
    }

    /*
     * @Description: 保存信息
     * @Author: liyanqing
     * @Date: 2021-01-11 14:29
     * @param dishonestPerson:
     * @return: void
     **/
    public String saveOrUpdate(DishonestPerson dishonestPerson) {
        if(null != dishonestPerson.getId()) {
            DbDishonestPerson one = this.selectByPrimaryKey(dishonestPerson.getId());
            Assert.notNull(one, MessageFormat.format("更新对象不存在！对象id为{0}", one.getId()));
            BeanUtils.copyProperties(dishonestPerson, one);
            one.setUpdateDate(new Date());
            dbDishonestPersonMapper.updateByPrimaryKeySelective(one);
            //添加记录
            if (dishonestPerson.getDishonestRecord() != null) {
                dishonestPerson.getDishonestRecord().setDishonestOid(one.getDishonestOid());
                dishonestRecordManager.saveOrUpdate(dishonestPerson.getDishonestRecord());
            }
            return one.getDishonestOid();
        } else {
            DbDishonestPerson dbDishonestPerson = new DbDishonestPerson();
            BeanUtils.copyProperties(dishonestPerson, dbDishonestPerson);
            dbDishonestPerson.setDishonestOid(UUIDUtil.randomUUID());
            dbDishonestPerson.setDelFlag(SysCode.DELETE_STATUS.NO);
            dbDishonestPerson.setCreateDate(new Date());
            dbDishonestPerson.setStatus(SysCode.DELETE_STATUS.NO);
            if (CurrentLoginUserHolder.getCurrentLoginUser() != null) {
                dbDishonestPerson.setCreateUserOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
            }
            dbDishonestPerson.setUpdateDate(new Date());
            dbDishonestPersonMapper.insertSelective(dbDishonestPerson);
            //添加记录
            if (dishonestPerson.getDishonestRecord() != null) {
                dishonestPerson.getDishonestRecord().setDishonestOid(dbDishonestPerson.getDishonestOid());
                dishonestRecordManager.saveOrUpdate(dishonestPerson.getDishonestRecord());
            }
            return dbDishonestPerson.getDishonestOid();
        }
    }

    /*
     * @Description: 保存信息
     * @Author: liyanqing
     * @Date: 2021-01-11 14:29
     * @param dishonestPerson:
     * @return: void
     **/
    public String saveOrUpdateOut(DishonestPerson dishonestPerson) {
        if(null != dishonestPerson.getId()) {
            DbDishonestPerson one = this.selectByPrimaryKey(dishonestPerson.getId());
            Assert.notNull(one, MessageFormat.format("更新对象不存在！对象id为{0}", one.getId()));
            dishonestPerson.setUpdateDate(new Date());
            BeanUtils.copyProperties(dishonestPerson, one);
            dbDishonestPersonMapper.updateByPrimaryKeySelective(one);
        } else {
            DbDishonestPerson dbDishonestPerson = new DbDishonestPerson();
            dishonestPerson.setDishonestOid(UUIDUtil.randomUUID());
            dishonestPerson.setDelFlag(SysCode.DELETE_STATUS.NO);
            dishonestPerson.setCreateDate(new Date());
            dishonestPerson.setStatus(SysCode.DELETE_STATUS.NO);
            dishonestPerson.setCreateUserOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString());
            dishonestPerson.setUpdateDate(new Date());
            BeanUtils.copyProperties(dishonestPerson, dbDishonestPerson);
            dbDishonestPersonMapper.insertSelective(dbDishonestPerson);
        }
        return dishonestPerson.getDishonestOid();
    }

    public List<DishonestPerson> getDishonestPerson(String name, String cardNumber) {
        DbDishonestPersonExample personExample = new DbDishonestPersonExample();
        DbDishonestPersonExample.Criteria criteria = personExample.createCriteria();
        if (StringUtils.isNotEmpty(name)) {
            //失信人名称
            criteria.andNameLike("%" + name.trim() + "%");
        }
        if (StringUtils.isNotEmpty(cardNumber)) {
            //失信人证件号
            criteria.andCardNumberEqualTo(cardNumber);
        }
        //执行失信人
        criteria.andStatusEqualTo(SysCode.DELETE_STATUS.NO);
        //未删除
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbDishonestPerson> dbDishonestPeople = dbDishonestPersonMapper.selectByExample(personExample);
        return BeanUtils.copyListProperties(dbDishonestPeople, DishonestPerson::new);
    }
}
