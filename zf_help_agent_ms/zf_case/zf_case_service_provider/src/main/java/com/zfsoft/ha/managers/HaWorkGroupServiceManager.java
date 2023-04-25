package com.zfsoft.ha.managers;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaWorkGroup;
import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.ha.data.responseData.HaWorkUserGroupResponseData;
import com.zfsoft.ha.data.responseData.HaWorkUserResponseData;
import com.zfsoft.ha.data.vo.HaWorkUserScheduleVo;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkGroupMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkUserMapper;
import com.zfsoft.ha.dbaccess.data.DbHaWorkGroup;
import com.zfsoft.ha.dbaccess.data.DbHaWorkUser;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkGroupExample;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.zfsoft.ha.constant.Constants.*;

/**
 * @Description //帮代办人员分组表实现层
 * @Author: Wangyh
 * @Date: 2022/8/4 14:06
 */
@Service
@Slf4j
public class HaWorkGroupServiceManager {
    /**
     * 帮代办人员分组db层接口
     */
    @Resource
    private DbHaWorkGroupMapper dbHaWorkGroupMapper;

    /**
     * 帮代办人员db层接口
     */
    @Resource
    private DbHaWorkUserMapper dbHaWorkUserMapper;

    @Resource
    private HaWorkUserScheduleManager haWorkUserScheduleManager;

    @Resource
    private HaWorkQueueManager haWorkQueueManager;

    /**
     * @param name         组名
     * @param deleteStatus 是否删除
     * @param pageNumber
     * @param pageSize
     * @return
     * @description: 查询用户分页信息列表
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public PageResult<HaWorkGroup> queryGroupServiceWithPage(String name, String deleteStatus, Integer pageNumber, Integer pageSize) throws Exception {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber, pageSize);

        DbHaWorkGroupExample dbHaWorkGroupExample = new DbHaWorkGroupExample();
        DbHaWorkGroupExample.Criteria criteria = dbHaWorkGroupExample.createCriteria();
        if (StrUtil.isNotEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (StrUtil.isNotEmpty(deleteStatus)) {
            criteria.andDeleteStatusEqualTo(deleteStatus);
        } else {
            criteria.andDeleteStatusEqualTo("1");
        }
        dbHaWorkGroupExample.setOrderByClause("CREATE_DATE desc");
        Page<DbHaWorkGroup> dbHaWorkGroups = (Page<DbHaWorkGroup>) dbHaWorkGroupMapper.selectByExample(dbHaWorkGroupExample);
        PageResult<HaWorkGroup> pageResult = new PageResult<>(dbHaWorkGroups.getPageNum(), dbHaWorkGroups.getPageSize(), dbHaWorkGroups.getTotal());
        List<HaWorkGroup> workUserList = dbHaWorkGroups.stream().map(dbGroupServic -> {
            HaWorkGroup haWorkGroup = new HaWorkGroup();
            BeanUtils.copyProperties(dbGroupServic, haWorkGroup);
            return haWorkGroup;
        }).collect(Collectors.toList());
        pageResult.setData(workUserList);
        return pageResult;
    }


    /**
     * @param id 主键
     * @return ApiResultSet 获取删除帮代办人员分组信息标识
     * @description: 删除帮代办人员分组信息
     * @author: wangyh
     * @Date: 2022/8/4
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public Integer deleteGroupid(Long id) throws Exception {
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        DbHaWorkGroup dbHaWorkGroup = dbHaWorkGroupMapper.queryById(id);
        dbHaWorkGroup.setDeleteStatus("2");
        dbHaWorkGroup.setUpdateBy(loginUser.getUserName());
        dbHaWorkGroup.setUpdateDate(new Date());
        DbHaWorkGroupExample dbHaWorkGroupExample = new DbHaWorkGroupExample();
        DbHaWorkGroupExample.Criteria criteria = dbHaWorkGroupExample.createCriteria();
        criteria.andIdEqualTo(id);
        int index = dbHaWorkGroupMapper.updateByExampleSelective(dbHaWorkGroup, dbHaWorkGroupExample);
        return index;
    }

    /**
     * @param haWorkGroup 帮代办人员分组实体类
     * @return ApiResultSet 获取新增或者修改帮代办人员分组信息标识
     * @description: 新增或者修改帮代办人员分组信息
     * @author: wangyh
     * @Date: 2022/8/4
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public Map<String, Object> saveOrUpdateHaWorkGroup(HaWorkGroup haWorkGroup) throws Exception {
        Map<String, Object> map = new HashMap();
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        int index = 0;
        //判断是修改还是新增
        if (haWorkGroup.getId() != null) {
            //修改
            DbHaWorkGroup dbHaWorkGroup = dbHaWorkGroupMapper.queryById(haWorkGroup.getId());
            BeanUtils.copyProperties(haWorkGroup, dbHaWorkGroup);
            dbHaWorkGroup.setUpdateBy(loginUser.getUserName()); //获取当前登录用户名称
            dbHaWorkGroup.setUpdateDate(new Date()); //获取当前时间
            DbHaWorkGroupExample dbHaWorkGroupExample = new DbHaWorkGroupExample();
            DbHaWorkGroupExample.Criteria criteria = dbHaWorkGroupExample.createCriteria();
            criteria.andIdEqualTo(haWorkGroup.getId());
            int i = dbHaWorkGroupMapper.updateByExampleSelective(dbHaWorkGroup, dbHaWorkGroupExample);
            map.put("index", index);
        } else {
            //新增
            DbHaWorkGroup dbHaWorkGroup = new DbHaWorkGroup();
            BeanUtils.copyProperties(haWorkGroup, dbHaWorkGroup);
            dbHaWorkGroup.setDeleteStatus("1");
            dbHaWorkGroup.setCreateBy(loginUser.getUserName());
            dbHaWorkGroup.setCreateDate(new Date());
            index = dbHaWorkGroupMapper.insert(dbHaWorkGroup);
            map.put("index", index);
        }
        return map;
    }

    /**
     * @param id
     * @return ApiResultSet 获取根据id查询帮代办人员分组信息
     * @description: 根据id查询帮代办人员分组信息
     * @author: wangyh
     * @Date: 2022/8/4
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public HaWorkGroup selectByid(Long id) throws Exception {
        HaWorkGroup haWorkGroup = null;
        DbHaWorkGroup dbHaWorkGroup = dbHaWorkGroupMapper.queryById(id);
        if (dbHaWorkGroup != null) {
            haWorkGroup = new HaWorkGroup();
            BeanUtils.copyProperties(dbHaWorkGroup, haWorkGroup);
        }
        return haWorkGroup;
    }

    /**
     * @description: 根据区划oid查询区划列表
     * @author: wangyh
     * @Date: 2022/8/2 14:32
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaWorkGroup> haWorkGroupList() {
        DbHaWorkGroupExample dbHaWorkGroupExample = new DbHaWorkGroupExample();
        DbHaWorkGroupExample.Criteria criteria = dbHaWorkGroupExample.createCriteria();
        criteria.andDeleteStatusEqualTo("1");
        dbHaWorkGroupExample.setOrderByClause("CREATE_DATE desc");
        List<DbHaWorkGroup> dbGroupsList = dbHaWorkGroupMapper.selectByExample(dbHaWorkGroupExample);
        List<HaWorkGroup> haWorkGroups = dbGroupsList.stream().map(groupService -> {
            HaWorkGroup haWorkGroup = new HaWorkGroup();
            BeanUtils.copyProperties(groupService, haWorkGroup);
            return haWorkGroup;
        }).collect(Collectors.toList());
        return haWorkGroups;
    }

    /**
     * @description: 获取分组列表信息
     * @author: kangax
     * @date: 2022-08-11 14:18
     */
    public List<HaWorkUserGroupResponseData> getGroupList(String post) {
        //默认查询只取号，不一桌联办的分组
        return this.getGroupList(post,TAKE_NUM_YES,DESK_NO);
    }

    public List<HaWorkUserGroupResponseData> getGroupList(String post,String takeNumStatus,String deskStatus) {
        DbHaWorkGroupExample dbHaWorkGroupExample = new DbHaWorkGroupExample();
        DbHaWorkGroupExample.Criteria criteria = dbHaWorkGroupExample.createCriteria();
        criteria.andDeleteStatusEqualTo("1");
        if(!takeNumStatus.isEmpty()){
            criteria.andTakeNumStatusEqualTo(takeNumStatus);
        }
        if(!deskStatus.isEmpty()){
            criteria.andDeskStatusEqualTo(deskStatus);
        }

        dbHaWorkGroupExample.setOrderByClause("CREATE_DATE desc");
        List<DbHaWorkGroup> dbGroupsList = dbHaWorkGroupMapper.selectByExample(dbHaWorkGroupExample);
        List<HaWorkUserGroupResponseData> haWorkUserGroupResponseDataList = dbGroupsList.stream().map(dbHaWorkGroup -> {
            HaWorkUserGroupResponseData haWorkUserGroupResponseData = new HaWorkUserGroupResponseData();
            //封装返回参数
            haWorkUserGroupResponseData.setGroupId(dbHaWorkGroup.getId());
            haWorkUserGroupResponseData.setGroupName(dbHaWorkGroup.getName());
            List<DbHaWorkUser> dbHaWorkUsers = dbHaWorkUserMapper.selectByGroupIdAndPost(dbHaWorkGroup.getId(),DELETE_STATUS_NO,null,post);
            haWorkUserGroupResponseData.setHaWorkUsers(dbHaWorkUsers.stream().map(s->{
                HaWorkUser haWorkUser = new HaWorkUser();
                BeanUtils.copyProperties(s,haWorkUser);
                return haWorkUser;
            }).collect(Collectors.toList()));

            return haWorkUserGroupResponseData;
        }).collect(Collectors.toList());
        return haWorkUserGroupResponseDataList;
    }


    public List<HaWorkUserResponseData> getCurrentGroupLeaderList() throws ServiceException {
        //获取当前用户登录信息
        HaLoginUserInfo workUser = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        DbHaWorkGroup dbHaWorkGroup = dbHaWorkGroupMapper.queryById(workUser.getGroupId());
        // 用户类型;1-导服人员，2-帮代办人员，3委办局老师
        String userType = "2";
        // 分组职务,1-组长，2-副组长，3-组员
        String groupPost = "1,2";
        List<DbHaWorkUser> dbHaWorkUsers = dbHaWorkUserMapper.selectByGroupIdAndType(dbHaWorkGroup.getId(), userType, groupPost);
        List<HaWorkUserResponseData> haWorkUserResponseDataList = dbHaWorkUsers.stream().map(s -> {
            HaWorkUserResponseData haWorkUserResponseData = new HaWorkUserResponseData();
            BeanUtils.copyProperties(s, haWorkUserResponseData);
            haWorkUserResponseData.setGroupName(dbHaWorkGroup.getName());
            return haWorkUserResponseData;
        }).collect(Collectors.toList());
        return haWorkUserResponseDataList;
    }


    /**
     * 获取当前用户组长副组长人员信息
     * @return
     */
    public List<HaWorkUserGroupResponseData> getBureauGroupList() throws ServiceException {
        DbHaWorkGroupExample dbHaWorkGroupExample = new DbHaWorkGroupExample();
        DbHaWorkGroupExample.Criteria criteria = dbHaWorkGroupExample.createCriteria();
        criteria.andDeleteStatusEqualTo("1");

        dbHaWorkGroupExample.setOrderByClause("CREATE_DATE desc");
        List<DbHaWorkGroup> dbGroupsList = dbHaWorkGroupMapper.selectByExample(dbHaWorkGroupExample);
        List<HaWorkUserGroupResponseData> haWorkUserGroupResponseDataList = new ArrayList<>();
        // 用户类型;1-导服人员，2-帮代办人员，3委办局老师
        String userType = "3";
        dbGroupsList.forEach(dbHaWorkGroup -> {
            List<DbHaWorkUser> dbHaWorkUsers = dbHaWorkUserMapper.selectByGroupIdAndType(dbHaWorkGroup.getId(), userType, null);
            if (dbHaWorkUsers.size() > 0) {
                HaWorkUserGroupResponseData haWorkUserGroupResponseData = new HaWorkUserGroupResponseData();
                //封装返回参数
                haWorkUserGroupResponseData.setGroupId(dbHaWorkGroup.getId());
                haWorkUserGroupResponseData.setGroupName(dbHaWorkGroup.getName());
                haWorkUserGroupResponseData.setHaWorkUsers(dbHaWorkUsers.stream().map(s->{
                    HaWorkUser haWorkUser = new HaWorkUser();
                    BeanUtils.copyProperties(s,haWorkUser);
                    return haWorkUser;
                }).collect(Collectors.toList()));
                haWorkUserGroupResponseDataList.add(haWorkUserGroupResponseData);
            }
        });
        return haWorkUserGroupResponseDataList;
    }
    /**
     * 获取当前用户组长副组长人员信息
     * @return
     */
    public List<HaWorkUser> getStreetist() throws ServiceException {
        List<HaWorkUser> result = new ArrayList<>();
        DbHaWorkGroupExample dbHaWorkGroupExample = new DbHaWorkGroupExample();
        DbHaWorkGroupExample.Criteria criteria = dbHaWorkGroupExample.createCriteria();
        criteria.andDeleteStatusEqualTo("1");
        criteria.andNameLike("%街道%");
        dbHaWorkGroupExample.setOrderByClause("CREATE_DATE desc");
        List<DbHaWorkGroup> dbGroupsList = dbHaWorkGroupMapper.selectByExample(dbHaWorkGroupExample);
        List<HaWorkUserGroupResponseData> haWorkUserGroupResponseDataList = new ArrayList<>();
        // 用户类型;1-导服人员，2-帮代办人员，3委办局老师
        String userType = "2";
        dbGroupsList.forEach(dbHaWorkGroup -> {
            List<DbHaWorkUser> dbHaWorkUsers = dbHaWorkUserMapper.selectByGroupIdAndType(dbHaWorkGroup.getId(), userType, null);
            if (dbHaWorkUsers.size() > 0) {

                result.addAll(dbHaWorkUsers.stream().map(s->{
                    HaWorkUser haWorkUser = new HaWorkUser();
                    BeanUtils.copyProperties(s,haWorkUser);
                    return haWorkUser;
                }).collect(Collectors.toList()));
            }
        });
        return result;
    }

    /**
     * @description: 获取分组列表信息
     * @author: kangax
     * @date: 2022-08-11 14:18
     * @Modify: 2022-11-01 17:24 zhaobf
     */
    public Map<String,List<Map<String,Object>>> getGroupListSub(){
        return this.getGroupListSub(TAKE_NUM_YES,DESK_NO);
    }

    public Map<String,List<Map<String,Object>>> getGroupListSub(String takeNumStatus,String deskStatus) {
        Map<String,List<Map<String,Object>>>subGroup = new HashMap<>();

//        List<Map<String,Object>> assistantTypeList = new ArrayList<>();
        List<Map<String,Object>> businessGrouping = new ArrayList<>();
        //获取所有用户分组
        DbHaWorkGroupExample dbHaWorkGroupExample = new DbHaWorkGroupExample();
        DbHaWorkGroupExample.Criteria criteria = dbHaWorkGroupExample.createCriteria();
        criteria.andDeleteStatusEqualTo("1");
        if(!takeNumStatus.isEmpty()){
            criteria.andTakeNumStatusEqualTo(takeNumStatus);
        }
        if(!deskStatus.isEmpty()){
            criteria.andDeskStatusEqualTo(deskStatus);
        }
        dbHaWorkGroupExample.setOrderByClause("CREATE_DATE desc");

        List<DbHaWorkGroup> dbGroupsList = dbHaWorkGroupMapper.selectByExample(dbHaWorkGroupExample);
        for (DbHaWorkGroup dbHaWorkGroup : dbGroupsList) {
            Date beginADay = DateUtil.getNYR();
            //结果为“0”是上午 结果为“1”是下午
            int index = new GregorianCalendar().get(GregorianCalendar.AM_PM);
            //获取当天，当前午别，当前组别的所有用户id
            List<HaWorkUserScheduleVo> haWorkUserScheduleVos = haWorkUserScheduleManager.queryUserScheAndQueueListByDay(beginADay,dbHaWorkGroup.getId().toString(),null, String.valueOf(index),DateUtil.getBeginADay(), DateUtil.getEndADay());
            //封装所有的帮办人员等待，服务人数到vo
//            List<HaWorkUserVo> haWorkUserVos = haWorkQueueManager.getWaitAndserviceIngNum(haWorkUserScheduleVos);
            List<HaWorkUserScheduleVo> kjbb = haWorkUserScheduleVos.stream().filter(e -> ("1".equals(e.getHaType()))).collect(Collectors.toList());
            List<HaWorkUserScheduleVo> yzbb = haWorkUserScheduleVos.stream().filter(e -> ("2".equals(e.getHaType()))).collect(Collectors.toList());

            Map<String,Object> one = new HashMap<>();
            one.put("name",dbHaWorkGroup.getName());
            one.put("groupId",dbHaWorkGroup.getId());
            one.put("kjbb",kjbb);
            one.put("yzbb",yzbb);
            businessGrouping.add(one);
//            }
        }
//        subGroup.put("assistantTypeList",assistantTypeList);
        subGroup.put("businessGrouping",businessGrouping);
        return subGroup;
    }

}
