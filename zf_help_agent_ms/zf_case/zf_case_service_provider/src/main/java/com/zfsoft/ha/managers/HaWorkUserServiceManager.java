package com.zfsoft.ha.managers;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.util.BaseStaticParameter;
import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.ha.constant.Constants;
import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.ha.data.SysDistrict;
import com.zfsoft.ha.data.TripartiteVo.DoingBusinessVo;
import com.zfsoft.ha.data.TripartiteVo.HaWorkUserVo;
import com.zfsoft.ha.data.TripartiteVo.ZwWindowStatusVo;
import com.zfsoft.ha.data.requestData.HaWorkUserRequestData;
import com.zfsoft.ha.data.vo.HaWorkUserVo_Schedule;
import com.zfsoft.ha.data.vo.HelpPersonList;
import com.zfsoft.ha.dbaccess.dao.DbHaVideoAccessMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaVideoRoomMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkQueueMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkUserMapper;
import com.zfsoft.ha.dbaccess.data.DbHaVideoAccess;
import com.zfsoft.ha.dbaccess.data.DbHaVideoRoom;
import com.zfsoft.ha.dbaccess.data.DbHaWorkQueue;
import com.zfsoft.ha.dbaccess.data.DbHaWorkUser;
import com.zfsoft.ha.dbaccess.data.example.DbHaWorkUserExample;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkUserVo;
import com.zfsoft.ha.util.CalSig;
import com.zfsoft.microservice.platform.service.sys.SysDistrictService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import com.zfsoft.superwindow.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.zfsoft.ha.constant.Constants.DELETE_STATUS_NO;

/**
 * @Description //用户表实现
 * @Author: Wangyh
 * @Date: 2022/7/15 14:18
 */
@Service
@Slf4j
public class HaWorkUserServiceManager {

    /**
     * 用户db层接口
     */
    @Resource
    private DbHaWorkUserMapper dbHaWorkUserMapper;

    @Resource
    private SysDistrictService sysDistrictService;

    /**
     * 视频咨询房间
     **/
    @Resource
    private DbHaVideoRoomMapper dbHaVideoRoomMapper;
    @Resource
    private DbHaWorkQueueMapper dbHaWorkQueueMapper;
    @Resource
    private DbHaVideoAccessMapper dbHaVideoAccessMapper;
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public  List<ZwWindowStatusVo> queryNineUserList() throws ServiceException {
        DbHaWorkUserExample thaWorkUserExample = new DbHaWorkUserExample();
        DbHaWorkUserExample.Criteria criteria = thaWorkUserExample.createCriteria();
        criteria.andNineZwWindowShieldStatusEqualTo("2"); //1-屏蔽  2-未屏蔽
        criteria.andDeleteStatusEqualTo("1"); //1-未删除，2-已删除
        thaWorkUserExample.setOrderByClause("CREATE_DATE desc");
        List<DbHaWorkUser> dbUserServices =  dbHaWorkUserMapper.selectByExample(thaWorkUserExample);
        List<ZwWindowStatusVo>  doingBusinessVoList = dbUserServices.stream().map(dbUserServic -> {
            ZwWindowStatusVo zwWindowStatusVo = new ZwWindowStatusVo();
            zwWindowStatusVo.setName(dbUserServic.getName());
            if (dbUserServic.getDistrictOid() != null) {
                //区划
                ApiResultSet apiResultSet = sysDistrictService.selectByDistrictOid(dbUserServic.getDistrictOid());
                if (apiResultSet != null && apiResultSet.getCode() == 200) {
                    Object obj = apiResultSet.getData();
                    ObjectMapper objectMapper = new ObjectMapper();
                    SysDistrict sysDistrict = objectMapper.convertValue(obj, SysDistrict.class);//通过convertValue方法将object对象转换为相应实体对象
                    zwWindowStatusVo.setDistrictName(sysDistrict.getName());
                }
            }
            zwWindowStatusVo.setOnlineStatus(dbUserServic.getStatus());
            zwWindowStatusVo.setServicePostion(dbUserServic.getServicePostion());
            //查询视频是否在线
            DbHaVideoAccess dbHaVideoAccess = dbHaVideoAccessMapper.selectByWorkUserId(dbUserServic.getId(),DateUtil.getTodayStartTime(), DateUtil.getEndTime());
            if(dbHaVideoAccess!=null){
                DbHaVideoRoom dbHaVideoRoom = dbHaVideoRoomMapper.queryById(dbHaVideoAccess.getRoomOid());
                DbHaWorkQueue dbHaWorkQueue = dbHaWorkQueueMapper.selectByPrimaryKey(dbHaVideoRoom.getQueueOid());
                if(dbHaWorkQueue.getServiceStatus().equals("2")){ //判断服务是否在服务中
                    zwWindowStatusVo.setRoomId(dbHaVideoAccess.getRoomOid());
                    String videoNum = String.valueOf(dbHaVideoAccess.getVideoNum());
                    zwWindowStatusVo.setVideoNum(videoNum + "5");
                    try {
                        zwWindowStatusVo.setVideoSig(CalSig.genSign(videoNum + "5", dbHaVideoRoom.getRoomNumber(), 86400));
                    } catch (Exception e) {
                        log.info("生成sig值出错");
                        throw new RuntimeException(e);
                    }
                    zwWindowStatusVo.setRoomNumber(dbHaVideoRoom.getRoomNumber());
                    zwWindowStatusVo.setVideoAppId(CalSig.getAppid());
                    zwWindowStatusVo.setVideoOnlineStatus("1");
                }else{
                    zwWindowStatusVo.setVideoOnlineStatus("2");
                }
            }else{
                zwWindowStatusVo.setVideoOnlineStatus("2");
            }
            return zwWindowStatusVo;
        }).collect(Collectors.toList());
        return doingBusinessVoList;
    }
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public  List<DoingBusinessVo> queryTenUserList() throws ServiceException {
        DbHaWorkUserExample thaWorkUserExample = new DbHaWorkUserExample();
        DbHaWorkUserExample.Criteria criteria = thaWorkUserExample.createCriteria();
        criteria.andTenYsFwShieldStatusEqualTo("2"); //1-屏蔽  2-未屏蔽
        criteria.andDeleteStatusEqualTo("1"); //1-未删除，2-已删除
        thaWorkUserExample.setOrderByClause("CREATE_DATE desc");
        List<DbHaWorkUser> dbUserServices =  dbHaWorkUserMapper.selectByExample(thaWorkUserExample);
        List<DoingBusinessVo>  doingBusinessVoList = dbUserServices.stream().map(dbUserServic -> {
            DoingBusinessVo doingBusinessVo = new DoingBusinessVo();
            doingBusinessVo.setName(dbUserServic.getName());
            doingBusinessVo.setBusinessServicesSddress(dbUserServic.getServicePostion());
            doingBusinessVo.setOnlineStatus(dbUserServic.getStatus());
            //查询视频是否在线
            DbHaVideoAccess dbHaVideoAccess = dbHaVideoAccessMapper.selectByWorkUserId(dbUserServic.getId(),DateUtil.getTodayStartTime(), DateUtil.getEndTime());
            if(dbHaVideoAccess!=null){
                    DbHaVideoRoom dbHaVideoRoom = dbHaVideoRoomMapper.queryById(dbHaVideoAccess.getRoomOid());
                    DbHaWorkQueue dbHaWorkQueue = dbHaWorkQueueMapper.selectByPrimaryKey(dbHaVideoRoom.getQueueOid());
                    if(dbHaWorkQueue.getServiceStatus().equals("2")){ //判断服务是否在服务中
                        doingBusinessVo.setRoomId(dbHaVideoAccess.getRoomOid());
                        String videoNum = String.valueOf(dbHaVideoAccess.getVideoNum());
                        doingBusinessVo.setVideoNum(videoNum + "5");
                        try {
                            doingBusinessVo.setVideoSig(CalSig.genSign(videoNum + "5", dbHaVideoRoom.getRoomNumber(), 86400));
                        } catch (Exception e) {
                            log.info("生成sig值出错");
                            throw new RuntimeException(e);
                        }
                        doingBusinessVo.setVideoAppId(CalSig.getAppid());
                        doingBusinessVo.setRoomNumber(dbHaVideoRoom.getRoomNumber());
                        doingBusinessVo.setVideoOnlineStatus("1");
                    }else{
                        doingBusinessVo.setVideoOnlineStatus("2");
                    }
            }else{
                doingBusinessVo.setVideoOnlineStatus("2");
            }
            return doingBusinessVo;
        }).collect(Collectors.toList());
        return doingBusinessVoList;
    }
    /**
     * @return
     * @description: 获取每个帮办人员的排队情况
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public  List<HelpPersonList> queryHelpPersonList(Long groupId, String name, String status) throws ServiceException {
        List<DbHaWorkUser> dbHaWorkUsers = dbHaWorkUserMapper.queryHelpPersonList(groupId, name, status);
        List<HelpPersonList> workUserList = dbHaWorkUsers.stream().map(dbUserServic -> {
            HelpPersonList helpPersonList = new HelpPersonList();
            BeanUtils.copyProperties(dbUserServic, helpPersonList);
            //等待中的人数
            int waitNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatus(dbUserServic.getId(), Constants.WAIT, DateUtil.getBeginADay(), DateUtil.getEndADay());
            helpPersonList.setWaitingNum(waitNum);
            Integer estimatedWaitingTime = dbUserServic.getAvgServiceTime() * waitNum; //预计等待时长
            helpPersonList.setEsuimateTime(estimatedWaitingTime);
            int countCurrentServiceNumOnline = dbHaWorkQueueMapper.countCurrentServiceNumOnline(Constants.SERVICE_ING, Constants.TALE_NUM_TYPE_SPQH,helpPersonList.getId(),DateUtil.getBeginADay(), DateUtil.getEndADay());
            int countCurrentServiceNumOffline = dbHaWorkQueueMapper.countCurrentServiceNumOffline(Constants.SERVICE_ING,helpPersonList.getId(), DateUtil.getBeginADay(), DateUtil.getEndADay());
            helpPersonList.setCurrentServiceNumOnline(countCurrentServiceNumOnline);
            helpPersonList.setCurrentServiceNumOffline(countCurrentServiceNumOffline-countCurrentServiceNumOnline);
            return helpPersonList;
        }).collect(Collectors.toList());
        return workUserList;
    }


    /**
     * @param haWorkUserRequestData
     * @param pageNumber
     * @param pageSize
     * @return
     * @description: 查询用户分页信息列表
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public PageResult<HaWorkUser> queryUserServiceWithPage(HaWorkUserRequestData haWorkUserRequestData, Integer pageNumber, Integer pageSize) throws ServiceException {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber, pageSize);
        DbHaWorkUserExample thaWorkUserExample = new DbHaWorkUserExample();
        DbHaWorkUserExample.Criteria criteria = thaWorkUserExample.createCriteria();
        if (null != haWorkUserRequestData) {
            if (StrUtil.isNotEmpty(haWorkUserRequestData.getName())) {
                criteria.andNameLike("%" + haWorkUserRequestData.getName() + "%");
            }
            if (StrUtil.isNotEmpty(haWorkUserRequestData.getAccount())) {
                criteria.andAccountEqualTo(haWorkUserRequestData.getAccount());
            }
            if (StrUtil.isNotEmpty(haWorkUserRequestData.getPhone())) {
                criteria.andPhoneEqualTo(haWorkUserRequestData.getPhone());
            }
            if (StrUtil.isNotEmpty(haWorkUserRequestData.getDistrictOid())) {
                criteria.andDistrictOidEqualTo(haWorkUserRequestData.getDistrictOid());
            }
            if (StrUtil.isNotEmpty(haWorkUserRequestData.getUserType())) {
                criteria.andUserTypeEqualTo(haWorkUserRequestData.getUserType());
            }
            if (StrUtil.isNotEmpty(haWorkUserRequestData.getDeleteStatus())) {
                criteria.andUserTypeEqualTo(haWorkUserRequestData.getDeleteStatus());
            } else {
                criteria.andDeleteStatusEqualTo("1"); //1-未删除，2-已删除
            }
        } else {
            criteria.andDeleteStatusEqualTo("1"); //1-未删除，2-已删除
        }
        thaWorkUserExample.setOrderByClause("CREATE_DATE desc");
        Page<DbHaWorkUser> dbUserServices = (Page<DbHaWorkUser>) dbHaWorkUserMapper.selectByExample(thaWorkUserExample);
        PageResult<HaWorkUser> pageResult = new PageResult<>(dbUserServices.getPageNum(), dbUserServices.getPageSize(), dbUserServices.getTotal());
        List<HaWorkUser> workUserList = dbUserServices.stream().map(dbUserServic -> {
            HaWorkUser haWorkUser1 = new HaWorkUser();
            BeanUtils.copyProperties(dbUserServic, haWorkUser1);
            if (StrUtil.isNotEmpty(haWorkUser1.getUserType()) && haWorkUser1.getUserType().equals(BaseStaticParameter.USER_TYPE_DF)) {
                haWorkUser1.setUserType(BaseStaticParameter.USER_TYPE_DFZW);
            } else if (StrUtil.isNotEmpty(haWorkUser1.getUserType()) && haWorkUser1.getUserType().equals(BaseStaticParameter.USER_TYPE_BDB)) {
                haWorkUser1.setUserType(BaseStaticParameter.USER_TYPE_BDBZW);
            } else if (StrUtil.isNotEmpty(haWorkUser1.getUserType()) && haWorkUser1.getUserType().equals(BaseStaticParameter.USER_TYPE_WBJ)) {
                haWorkUser1.setUserType(BaseStaticParameter.USER_TYPE_WBJZW);
            }

            if (StrUtil.isNotEmpty(haWorkUser1.getHaType()) && haWorkUser1.getHaType().equals(BaseStaticParameter.HA_TYPE_KJ)) {
                haWorkUser1.setHaType(BaseStaticParameter.HA_TYPE_KJZW);
            } else if (StrUtil.isNotEmpty(haWorkUser1.getHaType()) && haWorkUser1.getHaType().equals(BaseStaticParameter.HA_TYPE_YZ)) {
                haWorkUser1.setHaType(BaseStaticParameter.HA_TYPE_YZZW);
            }

            if (StrUtil.isNotEmpty(haWorkUser1.getStatus()) && haWorkUser1.getStatus().equals(BaseStaticParameter.STATUS_LX)) {
                haWorkUser1.setStatus(BaseStaticParameter.STATUS_LXZW);
            } else if (StrUtil.isNotEmpty(haWorkUser1.getStatus()) && haWorkUser1.getStatus().equals(BaseStaticParameter.STATUS_ML)) {
                haWorkUser1.setStatus(BaseStaticParameter.STATUS_MLZW);
            } else if (StrUtil.isNotEmpty(haWorkUser1.getStatus()) && haWorkUser1.getStatus().equals(BaseStaticParameter.STATUS_KX)) {
                haWorkUser1.setStatus(BaseStaticParameter.STATUS_KXZW);
            } else if (StrUtil.isNotEmpty(haWorkUser1.getStatus()) && haWorkUser1.getStatus().equals(BaseStaticParameter.STATUS_FUWZ)) {
                haWorkUser1.setStatus(BaseStaticParameter.STATUS_FUWZZW);
            }
            if (haWorkUser1.getDistrictOid() != null) {
                //区划
                ApiResultSet apiResultSet = sysDistrictService.selectByDistrictOid(haWorkUser1.getDistrictOid());
                if (apiResultSet != null && apiResultSet.getCode() == 200) {
                    Object obj = apiResultSet.getData();
                    ObjectMapper objectMapper = new ObjectMapper();
                    SysDistrict sysDistrict = objectMapper.convertValue(obj, SysDistrict.class);//通过convertValue方法将object对象转换为相应实体对象
                    haWorkUser1.setDistrictOid(sysDistrict.getName());
                }
            }
            return haWorkUser1;
        }).collect(Collectors.toList());
        pageResult.setData(workUserList);
        return pageResult;
    }

    /**
     * @param id
     * @return
     * @description: 删除用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public HaWorkUser deleteUserid(Long id) throws ServiceException {
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        DbHaWorkUser DbHaWorkUser = dbHaWorkUserMapper.queryById(id);
        DbHaWorkUser.setDeleteStatus("2"); //1-未删除，2-已删除
        DbHaWorkUser.setUpdateBy(loginUser.getUserName());
        DbHaWorkUser.setUpdateDate(new Date());
        dbHaWorkUserMapper.update(DbHaWorkUser);
        HaWorkUser haWorkUser = new HaWorkUser();
        BeanUtils.copyProperties(DbHaWorkUser, haWorkUser);
        return haWorkUser;
    }

    /**
     * @param ids 主键
     * @return
     * @description: 批量删除用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public void batchUserid(List<Long> ids) throws ServiceException {
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        for (Long userid : ids) {
            DbHaWorkUser DbHaWorkUser = dbHaWorkUserMapper.queryById(userid);
            if (DbHaWorkUser != null) {
                DbHaWorkUser.setDeleteStatus("2");//1-未删除，2-已删除
                DbHaWorkUser.setUpdateBy(loginUser.getUserName());
                DbHaWorkUser.setUpdateDate(new Date());
                dbHaWorkUserMapper.update(DbHaWorkUser);
            }
        }

    }

    /**
     * @param id 用户主键
     * @description: 重置密码
     * @author: wangyh
     * @Date: 2022/7/15
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public HaWorkUser ResetPassword(Long id) throws ServiceException {
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        DbHaWorkUser DbHaWorkUser = dbHaWorkUserMapper.queryById(id);
        String pass = "123456";//重置密码,初始化密码
        String passwrod = CommonUtil.md5(DbHaWorkUser.getAccount() + pass + DbHaWorkUser.getSalt());
        DbHaWorkUser.setPassword(passwrod);
        DbHaWorkUser.setUpdateBy(loginUser.getUserName());
        DbHaWorkUser.setUpdateDate(new Date());
        dbHaWorkUserMapper.update(DbHaWorkUser);
        HaWorkUser haWorkUser = new HaWorkUser();
        BeanUtils.copyProperties(DbHaWorkUser, haWorkUser);
        return haWorkUser;
    }

    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public HaWorkUser getHelpWorkUserById(Long id) throws ServiceException {
        DbHaWorkUser DbHaWorkUser = dbHaWorkUserMapper.queryById(id);
        HaWorkUser haWorkUser = new HaWorkUser();
        if (DbHaWorkUser != null) {
            BeanUtils.copyProperties(DbHaWorkUser, haWorkUser);
        }
        return haWorkUser;
    }

    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaWorkUserVo>  selectByGroupId(Long groupId) throws ServiceException {
        List<DbHaWorkUser>  DbHaWorkUser = dbHaWorkUserMapper.selectByGroupId(groupId, Constants.DELETE_STATUS_NO,Constants.APPON_STATUS_YES);
        List<HaWorkUserVo> haWorkUserList = DbHaWorkUser.stream().map(e -> {
            HaWorkUserVo HaWorkUserVo = new HaWorkUserVo();
            BeanUtils.copyProperties(e, HaWorkUserVo);
            return HaWorkUserVo;
        }).collect(Collectors.toList());
        return haWorkUserList;
    }



    /**
     * @param haWorkUser 参数配置实体类
     * @return Map<String, Object> 获取修改或保存详细状态
     * @description: 参数配置的新增或者修改
     */
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public Map<String, Object> saveOrUpdateThaWorkUser(HaWorkUser haWorkUser) throws Exception {
        Map map = new HashMap();
        int index = 0;
        if (haWorkUser.getId() != null) {
            //修改
            DbHaWorkUser DbHaWorkUser = dbHaWorkUserMapper.queryById(haWorkUser.getId());
            BeanUtils.copyProperties(haWorkUser, DbHaWorkUser);
            DbHaWorkUser.setUpdateDate(new Date());
            index = dbHaWorkUserMapper.update(DbHaWorkUser);
            map.put("index", index);
            map.put("type", "1");//type 1代表修改
        } else {
            //新增
            DbHaWorkUserVo DbHaWorkUsers = dbHaWorkUserMapper.queryloginByAccountAndId(haWorkUser.getAccount(),null);//根据账号查询是否有同一用户信息
            if (DbHaWorkUsers == null) {
                DbHaWorkUser dbHaWorkUser = new DbHaWorkUser();
                // - 生成随机盐值 ,新增之后不可改变
                String salt = UUID.randomUUID().toString().toUpperCase();
                String passwrod = CommonUtil.md5(haWorkUser.getAccount() + haWorkUser.getPassword() + salt);
                haWorkUser.setPassword(passwrod);
                haWorkUser.setSalt(salt);
                haWorkUser.setCreateDate(new Date());
                haWorkUser.setDeleteStatus("1");//1-未删除，2-已删除
                haWorkUser.setStatus("1"); //所有新增默认状态都是离线
                haWorkUser.setCurrentServiceNum(0); //当前服务人数字段时，新增时给一个默认值0
                BeanUtils.copyProperties(haWorkUser, dbHaWorkUser);
                index = dbHaWorkUserMapper.insert(dbHaWorkUser);
                map.put("index", index);
                map.put("type", "2");//type 1代表新增
            }
        }
        return map;
    }

    /**
     * @return List<HaWorkUser> 获取查询员工状态列表详细信息
     * @description: 获取查询员工状态列表详细信息
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaWorkUser> getMinMaxServiceNumSM(String groupId,String haType) throws ServiceException {
        List<DbHaWorkUser> DbHaWorkUserList = dbHaWorkUserMapper.getMinMaxServiceNumSM(groupId,haType);
        List<HaWorkUser> haWorkUserList = DbHaWorkUserList.stream().map(thaWorkUserListService -> {
            HaWorkUser haWorkUser = new HaWorkUser();
            haWorkUser.setId(thaWorkUserListService.getId());
            haWorkUser.setStatus(thaWorkUserListService.getStatus());
            haWorkUser.setMaxServiceNum(thaWorkUserListService.getMaxServiceNum());
            return haWorkUser;
        }).collect(Collectors.toList());
        return haWorkUserList;
    }

    /**
     * @return List<HaWorkUser> 获取查询员工状态列表详细信息
     * @description: 获取查询员工状态列表详细信息
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaWorkUser> getMinMaxServiceNum() throws ServiceException {
        List<DbHaWorkUser> DbHaWorkUserList = dbHaWorkUserMapper.getMinMaxServiceNum();
        List<HaWorkUser> haWorkUserList = DbHaWorkUserList.stream().map(thaWorkUserListService -> {
            HaWorkUser haWorkUser = new HaWorkUser();
            haWorkUser.setId(thaWorkUserListService.getId());
            haWorkUser.setStatus(thaWorkUserListService.getStatus());
            haWorkUser.setMaxServiceNum(thaWorkUserListService.getMaxServiceNum());
            return haWorkUser;
        }).collect(Collectors.toList());
        return haWorkUserList;
    }

    /**
     * @return List<HaWorkUser> 获取查询员工状态列表详细信息
     * @description: 获取查询员工状态列表详细信息
     */
    public List<HaWorkUser> getMinMaxServiceNumByHaType(String haType) throws ServiceException {
        List<DbHaWorkUser> DbHaWorkUserList = dbHaWorkUserMapper.getMinMaxServiceNumByHaType(haType);
        List<HaWorkUser> haWorkUserList = DbHaWorkUserList.stream().map(thaWorkUserListService -> {
            HaWorkUser haWorkUser = new HaWorkUser();
            haWorkUser.setId(thaWorkUserListService.getId());
            haWorkUser.setStatus(thaWorkUserListService.getStatus());
            haWorkUser.setMaxServiceNum(thaWorkUserListService.getMaxServiceNum());
            return haWorkUser;
        }).collect(Collectors.toList());
        return haWorkUserList;
    }

    /**
     * @return List<HaWorkUser> 获取查询员工状态列表详细信息
     * @description: 获取查询员工状态列表详细信息
     */
    public List<HaWorkUserVo_Schedule> queryNameAndGroup(String workUserName, Long groupId) throws ServiceException {
        List<DbHaWorkUserVo> DbHaWorkUserList = dbHaWorkUserMapper.queryNameAndGroup(workUserName,groupId);
        List<HaWorkUserVo_Schedule> haWorkUserList = DbHaWorkUserList.stream().map(haWorkUserVo -> {
            HaWorkUserVo_Schedule haWorkUser = new HaWorkUserVo_Schedule();
            BeanUtils.copyProperties(haWorkUserVo, haWorkUser);
            return haWorkUser;
        }).collect(Collectors.toList());
        return haWorkUserList;
    }


    /**
     * @param id 主键
     * @description: 根据id查询User表信息信息
     * @author: wangyh
     * @Date: 2022年7月26日14:08:02
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public HaWorkUser selectByid(Long id) throws ServiceException {
        HaWorkUser haWorkUser = null;
        DbHaWorkUser DbHaWorkUser = dbHaWorkUserMapper.queryById(id);
        if (DbHaWorkUser != null) {
            haWorkUser = new HaWorkUser();
            BeanUtils.copyProperties(DbHaWorkUser, haWorkUser);
        }
        return haWorkUser;
    }


    /**
     * 查询所有的工作人员
     *
     * @return 工作人员能集合
     * @author yupeng
     * @date 2022年08月11 20:28:06
     */
    public List<HaWorkUser> queryAllWorkUser() {
        DbHaWorkUserExample thaWorkUserExample = new DbHaWorkUserExample();
        DbHaWorkUserExample.Criteria criteria = thaWorkUserExample.createCriteria();
        criteria.andDeleteStatusEqualTo("1");
        criteria.andUserTypeEqualTo("2");
        List<DbHaWorkUser> dbHaWorkUserList = dbHaWorkUserMapper.selectByExample(thaWorkUserExample);
        return dbHaWorkUserList.stream().map(dbHaWorkUser -> {
            HaWorkUser haWorkUser = new HaWorkUser();
            BeanUtils.copyProperties(dbHaWorkUser, haWorkUser);
            return haWorkUser;
        }).collect(Collectors.toList());
    }

    /**
     * 根据当前登录的帮办人查询对应的组长列表
     * @param haWorkUser
     * @return
     * @throws Exception
     */
    public List<HaWorkUser> queryAllWorkUserByHaWorkUser(HaWorkUser haWorkUser) throws Exception {
        List<DbHaWorkUser> dbHaWorkUsers = dbHaWorkUserMapper.selectByGroupIdAndPost(haWorkUser.getGroupId(),DELETE_STATUS_NO,null,haWorkUser.getGroupPost());
        List<HaWorkUser> result = dbHaWorkUsers.stream().map(dbHaWorkUser -> {
            HaWorkUser haWorkUser1 = new HaWorkUser();
            BeanUtils.copyProperties(dbHaWorkUser, haWorkUser1);
            return haWorkUser1;
        }).collect(Collectors.toList());
        return result;
    }


    @Transactional(rollbackFor = Exception.class)
    public void saveLoingLocationName(Long id, String loginLocationName){
        log.info("进入保存登录位置：id{},loginLocationName:{}", id, loginLocationName);
       try {
           dbHaWorkUserMapper.updateUserLoginLocationName(id,loginLocationName);
        } catch (Exception e) {
            log.error("保存登录位置出错，{}",e.getMessage());
            e.printStackTrace();
        }
    }
}
