package com.zfsoft.superwindow.manager.sy;

import com.google.common.collect.Lists;
import com.zfsoft.cases.feign.SysUserFeginService;
import com.zfsoft.cases.util.GenDataTreeUtil;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.service.common.data.TreeSelect;
import com.zfsoft.superwindow.data.sy.AfterAdvisoryry;
import com.zfsoft.superwindow.dbaccess.dao.DbAfterAdvisoryMapper;
import com.zfsoft.superwindow.dbaccess.data.DbAfterAdvisory;
import com.zfsoft.superwindow.dbaccess.data.DbAfterAdvisoryExample;
import com.zfsoft.superwindow.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: kkfan
 * @create: 2020-10-31 11:57:51
 * @description: 后援业务逻辑层
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AfterAdvisoryManager {

    private final DbAfterAdvisoryMapper dbAfterAdvisoryMapper;

    private final SysUserFeginService sysUserFeginService;

    /**
     * 查询后援列表
     * @param afterAdvisoryry
     * @return
     */
    public List<AfterAdvisoryry> queryList(AfterAdvisoryry afterAdvisoryry) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String newDateStr = simpleDateFormat.format(date);
        Date startDate = null;
        try {
            startDate = simpleDateFormat.parse(newDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date endDate = DateUtils.addDays(date, 1);
        String userOid ="";
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        if(null!=loginUser){
            userOid=loginUser.getUserOid();
        }
        DbAfterAdvisoryExample example = new DbAfterAdvisoryExample();
        example.createCriteria()
                .andIsDeleteEqualTo(SysCode.DELETE_STATUS.NO)
                .andReplyUserOidEqualTo(userOid)
                .andAdvisoryTimeGreaterThanOrEqualTo(startDate)
                .andAdvisoryTimeLessThan(endDate);
        List<DbAfterAdvisory> dbAfterAdvisoryList = this.dbAfterAdvisoryMapper.selectByExample(example);
        return BeanUtils.copyListProperties(dbAfterAdvisoryList, AfterAdvisoryry::new);
    }

    /**
     * 查询忙碌状态   1 -- 忙碌   0 -- 空闲
     * @return
     */
    public Integer queryReplyState() {
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        // TODO 从currentLoginUser中获取
        Integer replyState = currentLoginUser.getAdviStatus();
        return replyState;
    }

    /**
     * 接受或拒绝请求
     * @param id    后援咨询表id
     * @param flag  空闲状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeReplyState(String id, Integer flag) {
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        Assert.hasLength(currentLoginUser.getUserOid(), "获取当前用户信息失败！");
        if(!SysCode.STATUS.STATUS_MAP.containsKey(flag)) {
            throw new IllegalArgumentException("flag 参数不合法，  1 -- YES   0 -- NO ");
        }
        DbAfterAdvisory dbAfterAdvisory = this.selectByPrimaryKey(Long.valueOf(id));
        Assert.notNull(dbAfterAdvisory, "后缓咨询对象异常!");
        if(SysCode.STATUS.YES .equals(flag) ) {
            dbAfterAdvisory.setReplyState(SysCode.REPLY_STATE.ACCEPT);
        } else if(SysCode.STATUS.NO.equals(flag) ) {
            dbAfterAdvisory.setReplyState(SysCode.REPLY_STATE.REFUSE);
        }
        this.dbAfterAdvisoryMapper.updateByPrimaryKeySelective(dbAfterAdvisory);
        SysUser sysUser = new SysUser();
        sysUser.setUserOid(currentLoginUser.getUserOid());
        sysUser.setAdviStatus(SysCode.ADVI_STATE.BUSY);
        this.sysUserFeginService.updateSysUser(sysUser);
    }

    /**
     * 校验是否已呼叫咨询
     * @param replyOrganOid 援助部门
     * @param replyUserOid  援助人
     * @return
     */
    public Boolean checkIsCall(String replyOrganOid, String replyUserOid) {
        Assert.hasLength(replyOrganOid, "援助部门不能为空！");
        Assert.hasLength(replyUserOid, "援助人不能为空！");
        String userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
        Assert.hasLength(userOid, "咨询人不能为空！");
        DbAfterAdvisory dbAfterAdvisory = this.getNewAfterAdvisory(replyOrganOid, replyUserOid, userOid);
        if(dbAfterAdvisory != null){
            //获取呼叫后援咨询的间隔时间
            int minute = DateUtil.dateDiff('m', new Date(), dbAfterAdvisory.getAdvisoryTime());
            if(minute < 5){
                throw new IllegalArgumentException("呼叫间隔时间不能小于5分钟");
            }
        }
        return true;
    }

    /**
     * 保存/更新
     * @param afterAdvisoryry
     */
   /* @Transactional(rollbackFor = Exception.class)*/
    public void saveOrUpdate(AfterAdvisoryry afterAdvisoryry) {
        if(null != afterAdvisoryry.getId()) {
            DbAfterAdvisory dbAfterAdvisory = this.selectByPrimaryKey(afterAdvisoryry.getId());
            Assert.notNull(dbAfterAdvisory, MessageFormat.format("更新对象不存在！对象id为{0}", afterAdvisoryry.getId()));
            BeanUtils.copyProperties(afterAdvisoryry, dbAfterAdvisory);
            dbAfterAdvisory.setModifyDate(new Date());
            this.dbAfterAdvisoryMapper.updateByPrimaryKeySelective(dbAfterAdvisory);
            BeanUtils.copyProperties(dbAfterAdvisory, afterAdvisoryry);
        } else {
            DbAfterAdvisory dbAfterAdvisory = new DbAfterAdvisory();
            BeanUtils.copyProperties(afterAdvisoryry, dbAfterAdvisory);
            dbAfterAdvisory.setAdvisoryTime(new Date());
            dbAfterAdvisory.setIsDelete(SysCode.DELETE_STATUS.NO);
            dbAfterAdvisory.setCreateDate(new Date());
            dbAfterAdvisory.setModifyDate(new Date());
            dbAfterAdvisory.setReplyState(SysCode.ADVI_STATE.FREE);
            CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
            if(null!=loginUser){
                dbAfterAdvisory.setAdvisoryUserOid(loginUser.getUserOid());
            }
            ApiResultSet<SysUser> replyUser = sysUserFeginService.getSysUserByUserOid(afterAdvisoryry.getReplyUserOid());
            if(null!=replyUser){
                dbAfterAdvisory.setReplyOrganOid(replyUser.getData().getOrganOid());
            }
            Long currentCount = this.queryAfterAdvisoryCount(dbAfterAdvisory.getReplyOrganOid(), null,dbAfterAdvisory.getReplyUserOid());
            int sort = currentCount.intValue();
            //排序号（当前人数+1）
            dbAfterAdvisory.setSort(++sort);
            dbAfterAdvisory.setAfterAdvisoryOid(UUIDUtil.randomUUID());
            this.dbAfterAdvisoryMapper.insertSelective(dbAfterAdvisory);
            BeanUtils.copyProperties(dbAfterAdvisory, afterAdvisoryry);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String ids) {
        Assert.hasLength(ids, "删除主键不能为空！");
        Optional.ofNullable(Arrays.asList(ids.split(",")))
                .orElseGet(Lists::newArrayList)
                .stream()
                .forEach(id -> {
                    DbAfterAdvisory dbAfterAdvisory = this.selectByPrimaryKey(Long.valueOf(id));
                    Assert.notNull(dbAfterAdvisory, MessageFormat.format("删除对象不存在！对象id为{0}", id));
                    dbAfterAdvisory.setIsDelete(SysCode.DELETE_STATUS.YES);
                    dbAfterAdvisory.setModifyDate(new Date());
                    this.dbAfterAdvisoryMapper.updateByPrimaryKeySelective(dbAfterAdvisory);
                });
    }

    public AfterAdvisoryry getOne(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbAfterAdvisory dbAfterAdvisory = this.selectByPrimaryKey(Long.valueOf(id));
        AfterAdvisoryry afterAdvisoryry = new AfterAdvisoryry();
        BeanUtils.copyProperties(dbAfterAdvisory, afterAdvisoryry);
        return afterAdvisoryry;
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    private DbAfterAdvisory selectByPrimaryKey(Long id) {
        DbAfterAdvisoryExample example = new DbAfterAdvisoryExample();
        example.createCriteria()
                .andIdEqualTo(id)
                .andIsDeleteEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbAfterAdvisory> dbHumanEvidenceDeviceMgtList = this.dbAfterAdvisoryMapper.selectByExample(example);
        return CollectionUtils.isEmpty(dbHumanEvidenceDeviceMgtList) ? null : dbHumanEvidenceDeviceMgtList.get(0);
    }


    /**
     * 根据援助部门oid，援助人oid查询后援咨询数
     * @param replyOrganOid 援助部门oid（回复部门）
     * @param replyUserOid 援助人oid(回复人)
     * @return
     */
    public Long queryAfterAdvisoryCount(String replyOrganOid, Integer replyState, String replyUserOid) {
        DbAfterAdvisoryExample example = new DbAfterAdvisoryExample();
        DbAfterAdvisoryExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo(SysCode.DELETE_STATUS.NO);
        if(StringUtils.isNotEmpty(replyOrganOid)) {
            criteria.andReplyOrganOidEqualTo(replyOrganOid);
        }
        if(null != replyState) {
            criteria.andReplyStateEqualTo(replyState);
        }
        if(StringUtils.isNotEmpty(replyUserOid)) {
            criteria.andReplyUserOidEqualTo(replyUserOid);
        }

        criteria.andCreateDateGreaterThanOrEqualTo(DateUtil.getBeginADay())
                .andCreateDateLessThan(DateUtil.addDate(DateUtil.getBeginADay(), 1));

        return Long.valueOf(this.dbAfterAdvisoryMapper.countByExample(example));
    }

    /**
     * 根据查询条件搜索后援咨询最新的数据
     * @date 2019年7月7日
     * @param replyOrganOid 援助部门oid（回复部门）
     * @param replyUserOid 援助人oid(回复人)
     * @param advisoryOid 咨询人oid
     * @return
     */
    public DbAfterAdvisory getNewAfterAdvisory(String replyOrganOid,
                                             String replyUserOid,String advisoryOid) {
        DbAfterAdvisoryExample example = new DbAfterAdvisoryExample();
        DbAfterAdvisoryExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo(SysCode.DELETE_STATUS.NO);
        if(StringUtils.isNotEmpty(replyOrganOid)){
            criteria.andReplyOrganOidEqualTo(replyOrganOid.trim());
        }
        if(StringUtils.isNotEmpty(replyUserOid)){
            criteria.andReplyUserOidEqualTo(replyUserOid.trim());
        }
        if(StringUtils.isNotEmpty(advisoryOid)){
            criteria.andAdvisoryUserOidEqualTo(advisoryOid.trim());
        }
        example.setOrderByClause(" CREATE_DATE DESC ");
        List<DbAfterAdvisory> dbAfterAdvisoryList = this.dbAfterAdvisoryMapper.selectByExample(example);
        return CollectionUtils.isEmpty(dbAfterAdvisoryList) ? null : dbAfterAdvisoryList.get(0);
    }

    public ApiResultSet getFreeUserTreeByOrgan(String organOid){
        ApiResultSet apiResultSet= new ApiResultSet();
        ApiResultSet<List<SysUser>> userList= sysUserFeginService.getSysUserListByOrganOid(organOid);
        if(userList!=null&&userList.getData()!=null&&userList.getData().size()>0){
            List<SysUser> user = userList.getData();
            List<SysUser> list=new ArrayList();
            for(SysUser ur:user){
                if(ur.getAdviStatus()==null||ur.getAdviStatus()!=1){
                    list.add(ur);
                }
            }
            List<TreeSelect> treeSelects = GenDataTreeUtil.buildUserTreeSelect(list);
            apiResultSet.setData(treeSelects);
        }
        return apiResultSet;
    }


}
