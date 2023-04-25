package com.zfsoft.ha.managers;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.ha.constant.RedisConstants;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaWorkGroup;
import com.zfsoft.ha.data.requestData.HaDeskPageRequestData;
import com.zfsoft.ha.data.requestData.HaDeskRequestData;
import com.zfsoft.ha.data.responseData.HaWorkUserGroupResponseData;
import com.zfsoft.ha.data.vo.HaDeskDepVo;
import com.zfsoft.ha.data.vo.HaDeskVo;
import com.zfsoft.ha.data.vo.HaWorkQueueVo;
import com.zfsoft.ha.dbaccess.dao.DbHaDeskDepMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaDeskMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkQueueMapper;
import com.zfsoft.ha.dbaccess.data.DbHaDesk;
import com.zfsoft.ha.dbaccess.data.DbHaDeskDep;
import com.zfsoft.ha.dbaccess.data.DbHaWorkQueue;
import com.zfsoft.ha.dbaccess.data.vo.DbHaDeskDepVo;
import com.zfsoft.ha.dbaccess.data.vo.DbHaDeskPageRequestData;
import com.zfsoft.ha.dbaccess.data.vo.DbHaDeskVo;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkQueueVo;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.ocr.data.pojo.exception.ServiceException;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.single.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.zfsoft.ha.constant.Constants.*;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/12/26 11:29
 */
@Service
@Slf4j
public class HaDeskManager {
    @Resource
    private DbHaDeskMapper dbHaDeskMapper;

    @Resource
    private DbHaDeskDepMapper dbHaDeskDepMapper;

    @Resource
    private DbHaWorkQueueMapper dbHaWorkQueueMapper;

    @Resource
    private HaWorkGroupServiceManager haWorkGroupServiceManager;


    @Resource
    private RedisTemplate redisTemplate;

    /**
    * Description: 保存一桌联办信息
    * @param haDeskRequestData 保存的信息
    * @author zhaobf
    * date: 2022/12/27 11:52
    */
    @Transactional(rollbackFor = ServiceException.class)
    public String saveDesk(HaDeskRequestData haDeskRequestData) {
        //获取当前登录用户
        HaLoginUserInfo userInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();

        DbHaDesk dbHaDesk = new DbHaDesk();
        BeanUtils.copyProperties(haDeskRequestData,dbHaDesk);
        dbHaDesk.setCreateBy(userInfo.getName());
        dbHaDesk.setCreateDate(new Date());
        dbHaDesk.setUpdateBy(userInfo.getName());
        dbHaDesk.setUpdateDate(new Date());
        dbHaDesk.setDeleteStatus(DELETE_STATUS_NO);
        return dbHaDeskMapper.insertSelective(dbHaDesk)+"";
    }

    /**
    * Description: 分页查询一桌联办记录
    * @param hadeskPage 分页数据
    * @param pageNum 页码
    * @param pageSize 页大小
    * @author zhaobf
    * date: 2022/12/27 11:53
    */
    public PageResult getListWithPage(HaDeskPageRequestData hadeskPage, Integer pageNum, Integer pageSize) throws Exception {
        HaLoginUserInfo userInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        hadeskPage.setWorkUserId(userInfo.getId());
        Long groupId = userInfo.getGroupId();
        HaWorkGroup haWorkGroup = haWorkGroupServiceManager.selectByid(groupId);
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        DbHaDeskPageRequestData dbHaDeskPage= new DbHaDeskPageRequestData();

        BeanUtils.copyProperties(hadeskPage, dbHaDeskPage);
        PageHelper.startPage(pageNum, pageSize);

        if(DESK_NO.equals(haWorkGroup.getDeskStatus())){
            Page<DbHaDeskVo> dbHaDeskVo = (Page<DbHaDeskVo>) dbHaDeskMapper.getListWithPage(dbHaDeskPage);
            PageResult<HaDeskVo> pageResult = new PageResult<>(dbHaDeskVo.getPageNum(), dbHaDeskVo.getPageSize(), dbHaDeskVo.getTotal());
            List<HaDeskVo> haDeskVos = dbHaDeskVo.stream().map(dbHaDeskVo1 -> {
                HaDeskVo haDeskVo = new HaDeskVo();
                BeanUtils.copyProperties(dbHaDeskVo1, haDeskVo);
                DbHaWorkQueueVo dbHaWorkQueue = dbHaDeskVo1.getDbHaWorkQueueVo();
                HaWorkQueueVo haWorkQueue = new HaWorkQueueVo();
                BeanUtils.copyProperties(dbHaWorkQueue, haWorkQueue);
                haDeskVo.setHaWorkQueueVo(haWorkQueue);
                return haDeskVo;
            }).collect(Collectors.toList());
            pageResult.setData(haDeskVos);
            return pageResult;
        }else{
            Page<DbHaDeskDepVo> dbHaDeskDepVo= (Page<DbHaDeskDepVo>) dbHaDeskDepMapper.getListWithPage(dbHaDeskPage);
            PageResult<HaDeskDepVo> pageResult = new PageResult<>(dbHaDeskDepVo.getPageNum(), dbHaDeskDepVo.getPageSize(), dbHaDeskDepVo.getTotal());
            List<HaDeskDepVo> haDeskVos = dbHaDeskDepVo.stream().map(dbHaDeskDepVo1 -> {
                DbHaDeskVo dbHaDeskVo = dbHaDeskDepVo1.getDbHaDeskVo();
                DbHaWorkQueueVo dbHaWorkQueue = dbHaDeskVo.getDbHaWorkQueueVo();
                HaDeskVo haDeskVo = new HaDeskVo();
                BeanUtils.copyProperties(dbHaDeskVo, haDeskVo);
                HaWorkQueueVo haWorkQueue = new HaWorkQueueVo();
                BeanUtils.copyProperties(dbHaWorkQueue, haWorkQueue);
                haDeskVo.setHaWorkQueueVo(haWorkQueue);
                HaDeskDepVo haDeskDepVo = new HaDeskDepVo();
                BeanUtils.copyProperties(dbHaDeskDepVo1, haDeskDepVo);
                haDeskDepVo.setHaDeskVo(haDeskVo);
                return haDeskDepVo;
            }).collect(Collectors.toList());
            pageResult.setData(haDeskVos);

            return pageResult;
        }


    }

    /**
    * Description: 发起一桌联办预约
    * @param deskId 一桌联办id
    * @param groupIds 需要预约的组别id，逗号隔开
    * @param appDate 预约时间
    * @author zhaobf
    * date: 2022/12/27 11:55
    */
    @Transactional(rollbackFor = ServiceException.class)
    public ApiResultSet<String> appDepartment(String deskId, String groupIds, Date appDate) {
        /**
         * 获取当前登录用户
         */
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();

        DbHaDesk dbHaDesk = dbHaDeskMapper.selectByPrimaryKey(Long.valueOf(deskId));
        if(!dbHaDesk.getDeskStatus().equals(DESK_NO_APPO)){
            return new ApiResultSet<>(ApiResultSet.DIRTY_DATA_TITLE,"当前的一桌联办已经预约过各个部门");
        }
        /**
         * 保存预约分组信息
         */
        String[] split = groupIds.split(",");
        if(split.length==0)
            return new ApiResultSet<>(ApiResultSet.DIRTY_DATA_TITLE,"当前的一桌联办没有预约部门");
        for (String s : split) {
            DbHaDeskDep haDeskDep = new DbHaDeskDep();
            haDeskDep.setDeskId(Long.valueOf(deskId));
            haDeskDep.setGroupId(Long.valueOf(s));
            haDeskDep.setConfirmFlag("0");
            haDeskDep.setDeleteStatus(DELETE_STATUS_NO);
            haDeskDep.setCreateBy(currentHaLoginUserInfo.getName());
            haDeskDep.setCreateDate(new Date());
            haDeskDep.setUpdateBy(currentHaLoginUserInfo.getName());
            haDeskDep.setUpdateDate(new Date());
            dbHaDeskDepMapper.insertSelective(haDeskDep);
        }
        /**
         * 保存预约时间和预约状态
         */
        dbHaDesk.setAppDate(appDate);
        dbHaDesk.setDeskStatus(DESK_APPO_ING);

        dbHaDeskMapper.updateByPrimaryKeySelective(dbHaDesk);

        /**
         * 保存redis信息，用于查询预约记录,每次发起一桌联办预约，都会同步相关部门的待确认数
         */
        for (String groupId : split) {
            List<DbHaDeskDep> haDeskDepList = dbHaDeskDepMapper.getDeskDepByGroupId(groupId);
            if(haDeskDepList==null){
                //记录当前查询时间存入redis
                redisTemplate.opsForValue().set(RedisConstants.DESK_GROUP+groupId, 0);
            }else{
                //记录当前查询时间存入redis
                redisTemplate.opsForValue().set(RedisConstants.DESK_GROUP+groupId, haDeskDepList.size());
            }
        }


        return ApiResultSet.ok("请求成功", "");
    }

    /**
    * Description: 一桌联办查看预约进度
    * @param deskId 一桌联办主键id
    * @author zhaobf
    * date: 2022/12/27 11:56
    */
    public List<HaDeskDepVo> getAppCondition(String deskId) {
        List<DbHaDeskDepVo> haDeskDepList = dbHaDeskDepMapper.getDeskDepByDeskId(deskId);
        changeDeskStatus(Long.valueOf(deskId));
        return BeanUtils.copyListProperties(haDeskDepList, HaDeskDepVo::new);
    }

    /**
    * Description: 查看当前帮办人有没有需要确认的一桌联办信息
    * @author zhaobf
    * date: 2022/12/27 11:56
    */
    public int isDeskAppointment() {
        //获取当前登录用户
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        Boolean aBoolean = redisTemplate.hasKey(RedisConstants.DESK_GROUP + currentHaLoginUserInfo.getGroupId());
        return aBoolean? (int) redisTemplate.opsForValue().get(RedisConstants.DESK_GROUP+currentHaLoginUserInfo.getGroupId()) :-1;
    }
    /**
    * Description: 查看一桌联办预约情况，展示预约信息
    * @author zhaobf
    * date: 2022/12/27 11:56
    */
    public Map<String, Object> getDeskDepMessage() {
        Map<String, Object> map = new HashMap<>();
        //获取当前登录用户
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        List<DbHaDeskDep> haDeskDepList = dbHaDeskDepMapper.getDeskDepByGroupId(String.valueOf(currentHaLoginUserInfo.getGroupId()));
        DbHaDeskDep dbHaDeskDep = haDeskDepList.stream().findFirst().orElse(null);
        map.put("deskDepMes",dbHaDeskDep);
        DbHaDesk dbHaDesk = dbHaDeskMapper.selectByPrimaryKey(dbHaDeskDep.getDeskId());
        map.put("deskMes",dbHaDesk);
        return map;
    }

    /**
    * Description: 确认预约信息
    * @param id 预约部门id deskDepMes中的id
    * @param confirmFlag 确认标识  0未确认1已确认
    * @param desc 备注
    * @author zhaobf
    * date: 2022/12/27 11:57
    */
    public ApiResultSet conAppointment(Long id, String confirmFlag, String desc) {
        /**
         * 获取当前登录用户
         */
        HaLoginUserInfo info = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        /**
         * 更新信息
         */
        DbHaDeskDep dbHaDeskDep = dbHaDeskDepMapper.selectByPrimaryKey(id);
        if(dbHaDeskDep==null){
            return new ApiResultSet<>(ApiResultSet.DIRTY_DATA_TITLE,"当前的一桌联办部门信息根据id找不到信息");
        }
        if(!dbHaDeskDep.getConfirmFlag().equals("0")){
            return new ApiResultSet<>(ApiResultSet.DIRTY_DATA_TITLE,"当前的一桌联办部门信息已经确认过了");
        }
        dbHaDeskDep.setConfirmFlag(confirmFlag);
        dbHaDeskDep.setConWorkUserId(info.getId());
        dbHaDeskDep.setConDate(new Date());
        dbHaDeskDep.setDesc(desc);
        dbHaDeskDep.setUpdateBy(info.getName());
        dbHaDeskDep.setUpdateDate(new Date());
        dbHaDeskDepMapper.updateByPrimaryKeySelective(dbHaDeskDep);
        /**
         * redis 更新
         */
        Long groupId=dbHaDeskDep.getGroupId();

        List<DbHaDeskDep> haDeskDepList = dbHaDeskDepMapper.getDeskDepByGroupId(String.valueOf(groupId));
        if(haDeskDepList==null){
            //记录当前查询时间存入redis
            redisTemplate.opsForValue().set(RedisConstants.DESK_GROUP+groupId, 0);
        }else{
            //记录当前查询时间存入redis
            redisTemplate.opsForValue().set(RedisConstants.DESK_GROUP+groupId, haDeskDepList.size());
        }
        /**
         * 判断该一桌联办是否全部确认，全部确认后更改联办状态
         */
        changeDeskStatus(dbHaDeskDep.getDeskId());
        return ApiResultSet.ok("请求成功", "");
    }
    /**
     * 判断该一桌联办是否全部确认，全部确认后更改联办状态
     */
    private void changeDeskStatus(Long deskId) {
        List<DbHaDeskDepVo> deskDepByDeskId = dbHaDeskDepMapper.getDeskDepByDeskId(deskId + "");
        boolean bool = deskDepByDeskId.stream().allMatch(e -> "1".equals(e.getConfirmFlag()));
        if(bool){
            DbHaDesk dbHaDesk = dbHaDeskMapper.selectByPrimaryKey(deskId);
            if(dbHaDesk.getDeskStatus().equals(DESK_APPO_ING)){
                dbHaDesk.setDeskStatus(DESK_MADE_APPO);
            }
            dbHaDeskMapper.updateByPrimaryKey(dbHaDesk);
        }
    }
    /**
    * Description: 发起一桌联办
    * @param deskQueueId 联办队列id
    * @param deskId 一桌联办id
    * @author zhaobf
    * date: 2022/12/27 14:24
    */
    @Transactional(rollbackFor = ServiceException.class)
    public ApiResultSet startDesk(Long deskQueueId, Long deskId) {
        DbHaWorkQueue dbHaWorkQueue = dbHaWorkQueueMapper.selectByPrimaryKey(deskQueueId);

        //获取当前登录用户
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        /**
         * 更新一桌联办,发起一桌联办
         */
        DbHaDesk dbHaDesk = dbHaDeskMapper.selectByPrimaryKey(deskId);
        dbHaDesk.setDeskQueueId(deskQueueId);
        if(dbHaDesk.getDeskStatus().equals(DESK_NO_APPO)){
            return new ApiResultSet<>(ApiResultSet.DIRTY_DATA_TITLE,"当前的一桌联办件还未预约");
        }else if(dbHaDesk.getDeskStatus().equals(DESK_APPO_ING)){
            return new ApiResultSet<>(ApiResultSet.DIRTY_DATA_TITLE,"当前的一桌联办件还未预约完成");
        }else if(dbHaDesk.getDeskStatus().equals(DESK_MADE_APPO)){
            dbHaDesk.setDeskStatus(DESK_INITIATED);
        }else if(dbHaDesk.getDeskStatus().equals(DESK_INITIATED)){
            return new ApiResultSet<>(ApiResultSet.DIRTY_DATA_TITLE,"当前的一桌联办件已发起一桌联办");
        }

        dbHaDeskMapper.updateByPrimaryKeySelective(dbHaDesk);
        /**
         * 给每个部门都发送一个办事队列
         */
        List<HaWorkUserGroupResponseData> resultSet = haWorkGroupServiceManager.getGroupList(GROUP_LEADER,"",DESK_YES);

        List<DbHaDeskDepVo> deskDepByDeskId = dbHaDeskDepMapper.getDeskDepByDeskId(String.valueOf(deskId));
        for (DbHaDeskDepVo item : deskDepByDeskId) {
            HaWorkUserGroupResponseData haData = resultSet.stream().filter(item2 -> item2.getGroupId().equals(item.getGroupId())).findFirst().orElse(null);
            if(haData==null){
                return new ApiResultSet<>(ApiResultSet.DIRTY_DATA_TITLE,"当前的一桌联办部门信息获取不到部门信息");
            }
            if(haData.getHaWorkUsers()==null){
                return new ApiResultSet<>(ApiResultSet.DIRTY_DATA_TITLE,"当前的一桌联办部门信息获取不到组长信息");
            }
            DbHaWorkQueue haWorkQueue = new DbHaWorkQueue();
            BeanUtils.copyProperties(dbHaWorkQueue,haWorkQueue);
            haWorkQueue.setId(null);
            haWorkQueue.setServiceStatus(WAIT);
            haWorkQueue.setServiceWorkUserId(haData.getHaWorkUsers().get(0).getId());
            haWorkQueue.setFirstServiceBeginTime(null);
            haWorkQueue.setWindowsNumber(null);
            haWorkQueue.setCreateBy(currentHaLoginUserInfo.getName());
            haWorkQueue.setCreateDate(new Date());
            haWorkQueue.setUpdateBy(currentHaLoginUserInfo.getName());
            haWorkQueue.setUpdateDate(new Date());
            dbHaWorkQueueMapper.insertSelective(haWorkQueue);

            item.setDeskQueueId(haWorkQueue.getId());
            DbHaDeskDep dbHaDeskDep = new DbHaDeskDep();
            BeanUtils.copyProperties(item,dbHaDeskDep);
            dbHaDeskDepMapper.updateByPrimaryKey(dbHaDeskDep);
        }
        return ApiResultSet.ok("请求成功", "");
    }


    /**
     * Description: 发起一桌联办
     * @param deskId 一桌联办id
     * @author zhaobf
     * date: 2022/12/27 14:24
     */
    @Transactional(rollbackFor = ServiceException.class)
    public ApiResultSet getDeskGroup(Long deskId) {

        /**
         * 获取所有一桌联办部门
         */
        List<HaWorkUserGroupResponseData> resultSet = haWorkGroupServiceManager.getGroupList(GROUP_LEADER,"",DESK_YES);

        /**
         * 获取需要确认的一桌联办部门
         */
        List<DbHaDeskDepVo> deskDepByDeskId = dbHaDeskDepMapper.getDeskDepByDeskId(String.valueOf(deskId));

        if(deskDepByDeskId==null||deskDepByDeskId.size() == 0){
            return new ApiResultSet<>(ApiResultSet.DIRTY_DATA_TITLE,"当前的一桌联办部门信息获取不到部门信息");
        }
        /**
         * anyMatch(Predicate p) 传入一个断言型函数，对流中所有的元素进行判断，
         * 只要有一个满足条件就返回true，都不满足返回false。
         */
        List<HaWorkUserGroupResponseData> collect = resultSet.stream().peek(e -> {
            if (deskDepByDeskId.stream().anyMatch(item2 -> item2.getGroupId().equals(e.getGroupId()))) {
                e.setStatus("1");
            } else {
                e.setStatus("2");
            }
        }).collect(Collectors.toList());


        return ApiResultSet.ok("请求成功", collect);
    }
}
