package com.zfsoft.ha.managers;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.ha.constant.Constants;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaPerformancePlustimeRecord;
import com.zfsoft.ha.data.vo.HaPerformancePlustimeRecordVo;
import com.zfsoft.ha.dbaccess.dao.DbHaPerformancePlustimeRecordMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaPlusProjectMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkUserMapper;
import com.zfsoft.ha.dbaccess.data.DbHaPerformancePlustimeRecord;
import com.zfsoft.ha.dbaccess.data.DbHaPlusProject;
import com.zfsoft.ha.dbaccess.data.DbHaWorkUser;
import com.zfsoft.ha.dbaccess.data.example.DbHaPerformancePlustimeRecordExample;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.bean.BeanUtils;
import com.zfsoft.platform.utils.validate.ParamValid;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 绩效加分时长统计管理业务实现层
 * @author dingsn
 * @date 2022/11/01  15:47
 */
@Service
@Slf4j
public class HaPerformancePlustimeRecordManager {
    @Resource
    private DbHaPerformancePlustimeRecordMapper dbHaPerformancePlustimeRecordMapper;
    /**
     * 用户db层接口
     */
    @Resource
    private DbHaWorkUserMapper dbHaWorkUserMapper;
    /**
     * 加分项目后台维护
     */
    @Resource
    private DbHaPlusProjectMapper dbHaPlusProjectMapper;

    public PageResult<HaPerformancePlustimeRecordVo> queryHaPerformancePlustimeRecordPageResult(HaPerformancePlustimeRecordVo haPerformancePlustimeRecordVo, Integer pageNum, Integer pageSize) throws Exception {
        //获取当前登录用户信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        DbHaPerformancePlustimeRecord dbHaPerformancePlustimeRecord = new DbHaPerformancePlustimeRecord();
        BeanUtils.copyProperties(haPerformancePlustimeRecordVo, dbHaPerformancePlustimeRecord);
        String groupPost = currentHaLoginUserInfo.getGroupPost();//分组职务;1-组长，2-副组长，3-组员
        if (Constants.GROUP_LEADER.equals(groupPost)) {//组长时查包括他自己在内的所有他的组员的加分时长记录
            //当前帮代办人员对应帮办人员组长id作为查询条件
            dbHaPerformancePlustimeRecord.setGroupLeaderId(currentHaLoginUserInfo.getId());
            //当前帮代办人员对应帮办人员组长姓名作为查询条件
            dbHaPerformancePlustimeRecord.setGroupLeaderName(currentHaLoginUserInfo.getName());
            //当前帮代办人员id作为查询条件
            dbHaPerformancePlustimeRecord.setWorkUserOid(currentHaLoginUserInfo.getId());
            //当前帮代办人员姓名作为查询条件
//            dbHaPerformancePlustimeRecord.setWorkUserName(currentHaLoginUserInfo.getName());

        } else {//组员或者副组长只能看他自己的
            //当前帮代办人员id作为查询条件
            dbHaPerformancePlustimeRecord.setWorkUserOid(currentHaLoginUserInfo.getId());
            //当前帮代办人员姓名作为查询条件
//            dbHaPerformancePlustimeRecord.setWorkUserName(currentHaLoginUserInfo.getName());
        }
        //查询
        Page<DbHaPerformancePlustimeRecord> dbHaPerformancePlustimeRecordList = (Page<DbHaPerformancePlustimeRecord>) dbHaPerformancePlustimeRecordMapper.queryHaPerformancePlustimeRecordList(dbHaPerformancePlustimeRecord);
        PageResult<HaPerformancePlustimeRecordVo> pageResult = new PageResult<>(dbHaPerformancePlustimeRecordList.getPageNum(), dbHaPerformancePlustimeRecordList.getPageSize(), dbHaPerformancePlustimeRecordList.getTotal());
        List<HaPerformancePlustimeRecordVo> haPerformancePlustimeRecordVos = dbHaPerformancePlustimeRecordList.stream().map(dbHaPerformancePlustimeRecord1 -> {
            HaPerformancePlustimeRecordVo haPerformancePlustimeRecordVo1 = new HaPerformancePlustimeRecordVo();
            BeanUtils.copyProperties(dbHaPerformancePlustimeRecord1, haPerformancePlustimeRecordVo1);
            DbHaPlusProject dbHaPlusProject = dbHaPlusProjectMapper.selectByPrimaryKey(dbHaPerformancePlustimeRecord1.getPlusProjectOid());
            if (dbHaPlusProject != null) {
                haPerformancePlustimeRecordVo1.setPlusProjectName(dbHaPlusProject.getName());
            }
            haPerformancePlustimeRecordVo1.setGroupPost(groupPost);
            haPerformancePlustimeRecordVo1.setCurrentHaLoginUserId(currentHaLoginUserInfo.getId());
            return haPerformancePlustimeRecordVo1;
        }).collect(Collectors.toList());
        pageResult.setData(haPerformancePlustimeRecordVos);
        return pageResult;
    }

    /**
     * 新增或修改绩效加分时长记录信息
     * @param haPerformancePlustimeRecord
     * @return
     * @author dingsn
     */
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public JSONObject saveHaPerformancePlustimeRecord(HaPerformancePlustimeRecord haPerformancePlustimeRecord) throws Exception {
        JSONObject result = new JSONObject();
        //获取当前用户登录信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        DbHaWorkUser dbHaWorkUser = dbHaWorkUserMapper.queryById(haPerformancePlustimeRecord.getGroupLeaderId());
        if (dbHaWorkUser != null) {
            haPerformancePlustimeRecord.setGroupLeaderName(dbHaWorkUser.getName());
        }
        int index = 0;
        if (haPerformancePlustimeRecord.getId() != null) {//修改
            DbHaPerformancePlustimeRecord dbHaPerformancePlustimeRecord = new DbHaPerformancePlustimeRecord();
            BeanUtils.copyProperties(haPerformancePlustimeRecord,dbHaPerformancePlustimeRecord);
            DbHaPerformancePlustimeRecord haPerformancePlustimeRecord1 = dbHaPerformancePlustimeRecordMapper.selectByPrimaryKey(haPerformancePlustimeRecord.getId());
            if (haPerformancePlustimeRecord1 != null) {
                if (currentHaLoginUserInfo.getId().equals(haPerformancePlustimeRecord1.getWorkUserOid()) && "2".equals(haPerformancePlustimeRecord1.getAuditStatus())) {//当前登录得是帮办人员自己
                    dbHaPerformancePlustimeRecord.setAuditStatus(Constants.ZERO);//审核状态改为待审核
                }
                if (currentHaLoginUserInfo.getId().equals(haPerformancePlustimeRecord1.getGroupLeaderId())) {//当前登录的是组长角色的账号
                    dbHaPerformancePlustimeRecord.setAuditDate(new Date());//审核时间
                }
            }
            dbHaPerformancePlustimeRecord.setUpdateBy(currentHaLoginUserInfo.getName()); //获取当前登录用户名称
            dbHaPerformancePlustimeRecord.setUpdateDate(new Date()); //获取当前时间
            DbHaPerformancePlustimeRecordExample dbHaPerformancePlustimeRecordExample = new DbHaPerformancePlustimeRecordExample();
            DbHaPerformancePlustimeRecordExample.Criteria criteria = dbHaPerformancePlustimeRecordExample.createCriteria();
            criteria.andIdEqualTo(haPerformancePlustimeRecord.getId());
            index = dbHaPerformancePlustimeRecordMapper.updateByExampleSelective(dbHaPerformancePlustimeRecord,dbHaPerformancePlustimeRecordExample);
        } else {//新增
            DbHaPerformancePlustimeRecord dbHaPerformancePlustimeRecord = new DbHaPerformancePlustimeRecord();
            BeanUtils.copyProperties(haPerformancePlustimeRecord,dbHaPerformancePlustimeRecord);
            dbHaPerformancePlustimeRecord.setWorkUserOid(currentHaLoginUserInfo.getId());
            dbHaPerformancePlustimeRecord.setWorkUserName(currentHaLoginUserInfo.getName());
            dbHaPerformancePlustimeRecord.setAuditStatus(Constants.ZERO);
            dbHaPerformancePlustimeRecord.setDeleteStatus(Constants.DELETE_STATUS_NO);
            dbHaPerformancePlustimeRecord.setCreateBy(currentHaLoginUserInfo.getName());
            dbHaPerformancePlustimeRecord.setCreateDate(new Date());
            index = dbHaPerformancePlustimeRecordMapper.insert(dbHaPerformancePlustimeRecord);
        }
        result.put("index", index);
        return result;
    }

    /**
     * 根据id主键获取绩效加分时长记录信息
     * @param id
     * @return
     * @throws Exception
     * @author dingsn
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public HaPerformancePlustimeRecord selectHaPerformancePlustimeRecordByid(Long id) throws Exception {
        DbHaPerformancePlustimeRecord dbHaPerformancePlustimeRecord = dbHaPerformancePlustimeRecordMapper.selectByPrimaryKey(id);
        if (dbHaPerformancePlustimeRecord != null) {
            HaPerformancePlustimeRecord haPerformancePlustimeRecord = new HaPerformancePlustimeRecord();
            BeanUtils.copyProperties(dbHaPerformancePlustimeRecord,haPerformancePlustimeRecord);
            return haPerformancePlustimeRecord;
        }
        return null;
    }

    /**
     * 根据主键删除绩效加分时长记录信息
     * @param id
     * @return
     * @throws Exception
     * @author dingsn
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public Integer deleteHaPerformancePlustimeRecordById(Long id) throws Exception {
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        DbHaPerformancePlustimeRecord dbHaPerformancePlustimeRecord = dbHaPerformancePlustimeRecordMapper.selectByPrimaryKey(id);
        dbHaPerformancePlustimeRecord.setDeleteStatus(Constants.DELETE_STATUS_YES);
        dbHaPerformancePlustimeRecord.setUpdateBy(currentHaLoginUserInfo.getName());
        dbHaPerformancePlustimeRecord.setUpdateDate(new Date());
        DbHaPerformancePlustimeRecordExample dbHaPerformancePlustimeRecordExample = new DbHaPerformancePlustimeRecordExample();
        DbHaPerformancePlustimeRecordExample.Criteria criteria = dbHaPerformancePlustimeRecordExample.createCriteria();
        criteria.andIdEqualTo(id);
        return dbHaPerformancePlustimeRecordMapper.updateByExampleSelective(dbHaPerformancePlustimeRecord,dbHaPerformancePlustimeRecordExample);
    }
}
