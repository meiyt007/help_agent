package com.zfsoft.superwindow.manager.sxry;

import com.zfsoft.superwindow.data.sxry.DishonestRecord;
import com.zfsoft.superwindow.dbaccess.dao.DbDishonestRecordMapper;
import com.zfsoft.superwindow.dbaccess.data.DbDishonestRecord;
import com.zfsoft.superwindow.dbaccess.data.DbDishonestRecordExample;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.StringUtils;
import com.zfsoft.superwindow.util.SysCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * @Classname DishonestPersonmanager
 * @Description 失信人员管理
 * @Date 2021-01-11 11:04
 * @Created by liyanqing
 */
@Service
@Slf4j
public class DishonestRecordManager {

    @Resource
    private DbDishonestRecordMapper dbDishonestRecordMapper;

    /*
     * @Description: 获取失信记录列表
     * @Author: liyanqing
     * @Date: 2021-01-11 11:10
     * @param dishonestPerson:
     * @return: java.util.List<com.zfsoft.data.tcbl.DishonestPerson>
     **/
    public List<DishonestRecord> queryPageList(DishonestRecord dishonestRecord) {
        DbDishonestRecordExample example = new DbDishonestRecordExample();
        DbDishonestRecordExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(dishonestRecord.getDishonestOid())) {
            //失信人主键
            criteria.andDishonestOidEqualTo(dishonestRecord.getDishonestOid());
        }
        //未删除
        criteria.andDelFlagEqualTo(String.valueOf(SysCode.DELETE_STATUS.NO));
        List<DbDishonestRecord> dbDishonestRecords = dbDishonestRecordMapper.selectByExample(example);
        return BeanUtils.copyListProperties(dbDishonestRecords, DishonestRecord::new);
    }

    /*
     * @Description: 获取失信记录信息
     * @Author: liyanqing
     * @Date: 2021-01-11 13:38
     * @param id: 逻辑主键
     * @return: com.zfsoft.data.tcbl.DishonestPerson
     **/
    public DishonestRecord getOne(Long id) {
        Assert.hasLength(String.valueOf(id), "主键不能为空！");
        DbDishonestRecord record = dbDishonestRecordMapper.selectByPrimaryKey(id);
        DishonestRecord dishonestRecord = new DishonestRecord();
        BeanUtils.copyProperties(record, dishonestRecord);
        return dishonestRecord;
    }

    /**
     * 根据主键id查询信息
     * @param id
     * @return
     */
    private DbDishonestRecord selectByPrimaryKey(Long id) {
        Assert.notNull(String.valueOf(id), "查询id不能为空！");
        DbDishonestRecordExample example = new DbDishonestRecordExample();
        DbDishonestRecordExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        criteria.andDelFlagEqualTo(String.valueOf(SysCode.DELETE_STATUS.NO));
        List<DbDishonestRecord> records = dbDishonestRecordMapper.selectByExample(example);
        return CollectionUtils.isEmpty(records) ? null : records.get(0);
    }

    /*
     * @Description: 保存信息
     * @Author: liyanqing
     * @Date: 2021-01-11 14:29
     * @param dishonestPerson:
     * @return: void
     **/
    public void saveOrUpdate(DishonestRecord dishonestRecord) {
        if(null != dishonestRecord.getId()) {
            DbDishonestRecord one = this.selectByPrimaryKey(dishonestRecord.getId());
            Assert.notNull(one, MessageFormat.format("更新对象不存在！对象id为{0}", one.getId()));
            BeanUtils.copyProperties(dishonestRecord, one);
            dbDishonestRecordMapper.updateByPrimaryKeySelective(one);
            BeanUtils.copyProperties(one, dishonestRecord);
        } else {
            DbDishonestRecord record = new DbDishonestRecord();
            BeanUtils.copyProperties(dishonestRecord, record);
            record.setDelFlag(String.valueOf(SysCode.DELETE_STATUS.NO));
            record.setCreateDate(new Date());
            dbDishonestRecordMapper.insertSelective(record);
        }
    }
}
