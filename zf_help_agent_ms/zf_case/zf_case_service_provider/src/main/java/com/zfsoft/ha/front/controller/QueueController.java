package com.zfsoft.ha.front.controller;

import com.zfsoft.ha.data.HaWorkQueue;
import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.ha.data.vo.HaWorkQueueVo;
import com.zfsoft.ha.dbaccess.data.DbHaWorkUser;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkQueueVo;
import com.zfsoft.ha.dbaccess.data.vo.HaWorkQueueResponseVo;
import com.zfsoft.ha.front.QueueService;
import com.zfsoft.ha.managers.HaWorkQueueManager;
import com.zfsoft.ha.managers.HaWorkUserServiceManager;
import com.zfsoft.ha.managers.QueueManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description //队列模块控制层
 * @Author: Wangyh
 * @Date: 2022/7/20 11:12
 */
@RestController
@Slf4j
public class QueueController implements QueueService {
    /**
     * 队列接口实现
     */
    @Resource
    private QueueManager queueManager;

    @Resource
    private HaWorkQueueManager haWorkQueueManager;

    /**
     * 用户表实现
     */
    @Resource
    private HaWorkUserServiceManager userServiceManager;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 是否有新的办事人员扫码
     * @return ApiResultSet 获取当前登录用户是否有新的办事人员扫码详细信息
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     */
    @Override
    public ApiResultSet isHaveNewMesses() {
        log.info("是否有新的办事人员扫码");
        Map<String, Object> map=null;
        try {
            map= queueManager.isHaveNewMesses();
        } catch (Exception e) {
            log.error("调用是否有新的办事人员扫码接口失败：", e);
            return new ApiResultSet<>(500, "调用是否有新的办事人员扫码接口失败", e.getMessage());
        }
        return ApiResultSet.ok("调用是否有新的办事人员扫码接口成功", map);
    }

    /**
     * 查询办事队列
     *
     * @param name          姓名
     * @param cardNo        身份证号码
     * @param companyName   企业名称
     * @param queueStatus   排队状态;1-扫码排队中，2-导服已分配
     * @param serviceStatus 服务状态;1-等待服务，2-服务中，3-服务完成
     * @return List<HaWorkQueueVo> 获取查询办事队列列表详情
     */
    @Override
    public ApiResultSet queryWorkQueueList(String name, String cardNo, String companyName,
                                           String queueStatus, String serviceStatus) {
        log.info("查询办事队列，name:{},cardNo:{},companyName:{},queueStatus:{},serviceStatus:{}", name, cardNo, companyName, queueStatus, serviceStatus);
        try {
            List<HaWorkQueueVo> workQueueList = queueManager.queryWorkQueueList(name, cardNo, companyName, queueStatus, serviceStatus);
            log.debug("workQueueList:{}", workQueueList);
            return ApiResultSet.ok("查询办事队列成功", workQueueList);
        } catch (Exception e) {
            log.error("查询办事队列方法错误：", e);
            return new ApiResultSet<>(500, "查询办事队列方法错误", e.getMessage());
        }
    }

    /**
     * @param //Token在header中
     * @description: 统计队列数据
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     * @return ApiResultSet 获取统计队列数据
     **/
    @Override
    public ApiResultSet getQueueNum() {
        log.info("统计队列数据");
        Map mapdata = new HashMap();
        try {
            DbHaWorkQueueVo dbHaWorkQueueVo = queueManager.getQueueNum();
            Integer totalServiceNum= queueManager.getTotalServiceNum();
            log.debug("dbHaWorkQueueVo:{}", dbHaWorkQueueVo);
            mapdata.put("quickWaitNum", dbHaWorkQueueVo.getQuickWaitNum());
            mapdata.put("tableWaitNum", dbHaWorkQueueVo.getTableWaitNum());
            mapdata.put("totalServiceNum",totalServiceNum);
            mapdata.put("windowWaitNum", 0);
            log.info("统计队列数据成功 mapdata={" + mapdata.toString() + "}");
        } catch (Exception e) {
            log.error("统计队列数据方法错误：", e);
            return new ApiResultSet(500, "统计队列数据方法错误", e.getMessage());
        }
        return ApiResultSet.ok("统计队列数据成功", mapdata);
    }

    /**
     * @param name   姓名
     * @param haType 帮代办类型，1-快捷帮办，2-圆桌帮办
     * @description: 获取帮代办人员列表
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     * @return ApiResultSet 获取帮代办人员列表详情
     **/
    @Override
    public ApiResultSet getWorkUserList(String name, String haType) {
        log.info("获取帮代办人员列表，name:{},haType:{}", name, haType);
        List<HaWorkQueueResponseVo> dbHaWorkQueueVos = null;
        try {
             dbHaWorkQueueVos = queueManager.getWorkUserList(name, haType);
        } catch (Exception e) {
            log.error("获取帮代办人员列表方法错误：", e);
            return new ApiResultSet(500, "获取帮代办人员列表方法错误", e.getMessage());
        }
        return ApiResultSet.ok("获取帮代办人员列表成功", dbHaWorkQueueVos);
    }

    /**
     * @param workUserId       说明:帮代办人员编号
     * @param queueId          队列编号
     * @param distributeStatus 分配状态;1-指定人员，2-随机分配，3-窗口办理
     * @param //Token在header中
     * @description: 分配帮代办人员
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @Override
    public ApiResultSet distributeWorkUser(Long workUserId, Long queueId, String distributeStatus,String windowsNumbe) {
        log.info("分配帮代办人员，workUserId:{},queueId:{},distributeStatus:{}", workUserId, queueId, distributeStatus);
        Map<String, Object> map = new HashMap<>();
        //判断分配状态;1-指定人员，2-随机分配，3-窗口办理是否为空，如果为空直接返回错误
        if (StringUtils.isNotEmpty(distributeStatus)) {
            switch (distributeStatus) {
                case "1":
                    if (workUserId != null && workUserId.longValue() != 0) {
                        try {
                            DbHaWorkUser dbThaWorkUser = queueManager.distributeWorkUser(workUserId, queueId, distributeStatus,windowsNumbe);
                            map.put("workUserId", workUserId);
                            map.put("workUserName", dbThaWorkUser.getName());
                            break;
                        } catch (Exception e) {
                            log.error("分配帮代办人员成功方法失败：", e);
                            return new ApiResultSet(500, "分配帮代办人员成功方法失败", e.getMessage());
                        }
                    } else {
                        return new ApiResultSet(500, "分配帮代办人员成功方法失败");
                    }
                case "2":
                    /**
                     * 优先级是1.空闲 2.服务中 3.忙碌 再根据每个状态中，服务人数最少的去分配
                     */
                    try {
                        Map<Long, Integer> mapStatus2 = new HashMap<>(); //状态为忙碌的集合
                        Map<Long, Integer> mapStatus3 = new HashMap<>();//状态为空闲的集合
                        Map<Long, Integer> mapStatus4 = new HashMap<>();//状态为服务中的集合
                        Map<String,Map<Long, Integer>> mapList = new HashMap<>();
                        List<HaWorkUser> haWorkUser = userServiceManager.getMinMaxServiceNum();
                        log.debug("thaWorkUser:{}", haWorkUser);

                        for (HaWorkUser haWorkUser1 : haWorkUser) {
                            switch (haWorkUser1.getStatus()) {
                                //"1-离线、2-忙碌、3-空闲、4-服务中")
                                case "2":
                                    mapStatus2.put(haWorkUser1.getId(), haWorkUser1.getMaxServiceNum());
                                    break;
                                case "3":
                                    mapStatus3.put(haWorkUser1.getId(), haWorkUser1.getMaxServiceNum());
                                    break;
                                case "4":
                                    mapStatus4.put(haWorkUser1.getId(), haWorkUser1.getMaxServiceNum());
                                    break;
                                default:
                                    break;
                            }
                        }
                        mapList.put("忙碌",mapStatus2);
                        mapList.put("空闲",mapStatus3);
                        mapList.put("服务中",mapStatus4);
                        if(mapList.get("空闲").size()>0){
                            map.putAll(this.getDistributeUser(mapList.get("空闲"), queueId, distributeStatus,windowsNumbe));
                        }else if (mapList.get("服务中").size()>0) {
                            map.putAll(this.getDistributeUser(mapList.get("服务中"), queueId, distributeStatus,windowsNumbe));
                        }else if (mapList.get("忙碌").size()>0) {
                            map.putAll(this.getDistributeUser(mapList.get("忙碌"), queueId, distributeStatus,windowsNumbe));
                        }
                    } catch (Exception e) {
                        log.error("分配帮代办人员成功方法失败：", e);
                        return new ApiResultSet(500, "分配帮代办人员成功方法失败", e.getMessage());
                    }
                case "3":
                    try {
                        DbHaWorkUser dbHaWorkUser = queueManager.distributeWorkUser(workUserId, queueId, distributeStatus,windowsNumbe);
                        map.put("windowsNumbe", windowsNumbe);
                        return ApiResultSet.ok("是为窗口办理",map);
                    } catch (Exception e) {
                        log.error("分配帮代办人员成功方法失败：", e);
                        return new ApiResultSet(500, "分配帮代办人员成功方法失败", e.getMessage());
                    }
                default:
                    return new ApiResultSet(500, "分配帮代办人员成功方法失败");
            }
        }
        return ApiResultSet.ok("分配帮代办人员成功", map);
    }

    public Map<String,Object> getDistributeUser(Map<Long, Integer> map, Long queueId, String distributeStatus,String windowsNumbe){
        Map<String, Object> mapReq = new HashMap<>();
        //取Map中Value最小值
        Integer minId = getMinValue(map);
        List<Long> listId = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : map.entrySet()) {
            if (entry.getValue() == minId) {
                listId.add(entry.getKey());
            }
        }
        //随机取出最小id集合中的一个
        if (listId.size() > 0) {
            Long userId = getRandomListId(listId);
            if (userId != null) {
                DbHaWorkUser dbThaWorkUser = queueManager.distributeWorkUser(userId, queueId, distributeStatus,windowsNumbe);
                mapReq.put("workUserId", userId);
                mapReq.put("workUserName", dbThaWorkUser.getName());
            }
        }
        return mapReq;
    }
    /**
     * 窗口叫号
     * @param queueId         队列编号
     * @param windowsNumbe    排队号
     * @param //Token在header中
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     * @return ApiResultSet 返回排队号
     **/
    @Override
    public ApiResultSet windowCall(Long queueId, String windowsNumbe) {
        log.info("窗口叫号，queueId:{},windowsNumbe:{}", queueId, windowsNumbe);
        Map<String, Object> mapdata = new HashMap();
        try {
            queueManager.windowCall(queueId, windowsNumbe);
            mapdata.put("windowsNumber", windowsNumbe);
        } catch (Exception e) {
            log.error("窗口叫号成功方法失败：", e);
            return new ApiResultSet(500, "窗口叫号成功方法失败", e.getMessage());
        }
        return ApiResultSet.ok("窗口叫号成功", mapdata);
    }

    @Override
    public ApiResultSet getQueueById(Long queueId) {
        log.info("根据id获取队列信息，queueId:{}", queueId);
        HaWorkQueue haWorkQueue;
        try {
            haWorkQueue =  queueManager.getQueueById(queueId);
        } catch (Exception e) {
            log.error("根据id获取队列信息失败：", e);
            return new ApiResultSet(500, "根据id获取队列信息失败", e.getMessage());
        }
        return ApiResultSet.ok("根据id获取队列信息成功", haWorkQueue);
    }

    @Override
    public ApiResultSet saveQueueAdviceMemo(Long queueId,String adviseMemo) {
        log.info("根据id获取队列信息，queueId:{}", queueId);
        HaWorkQueue haWorkQueue;
        String message;
        try {
            haWorkQueue =  queueManager.getQueueById(queueId);
            haWorkQueue.setAdviseMemo(adviseMemo);
            int index = haWorkQueueManager.saveHaWorkQueue(haWorkQueue);
            if (index != 0) {
                if (haWorkQueue.getId() != null) {
                    //说明有新增或修改
                    message = "修改成功";
                } else {
                    message = "新增成功";
                }
            } else {
                message = "已存在，没有更改";
            }
            log.info("办事队列：修改或保存办事队列信息成功" + message);
        } catch (Exception e) {
            log.error("根据id获取队列信息失败：", e);
            return new ApiResultSet(500, "根据id获取队列信息失败", e.getMessage());
        }
        return ApiResultSet.ok("根据id获取队列信息成功", haWorkQueue);
    }

    @Override
    public ApiResultSet saveNameAndCard(Long queueId, String name,String cardType,String cardNo) {
        log.info("办事队列：根据id保存队列基本信息，queueId:{}，name：{}，cardType：{}，cardNo：{}", queueId,name,cardType,cardNo);
        HaWorkQueue haWorkQueue;
        String message;
        try {
            haWorkQueue =  queueManager.getQueueById(queueId);
            haWorkQueue.setName(name);
            haWorkQueue.setCardType(cardType);
            haWorkQueue.setCardNo(cardNo);
            int index = haWorkQueueManager.saveHaWorkQueue(haWorkQueue);
            if (index != 0) {
                message = "修改成功";
            } else {
                message = "已存在，没有更改";
            }
            log.info("办事队列：根据id保存队列基本信息成功" + message);
        } catch (Exception e) {
            log.error("根据id获取队列信息失败：", e);
            return new ApiResultSet(500, "办事队列：根据id保存队列基本信息失败", e.getMessage());
        }
        return ApiResultSet.ok("办事队列：根据id保存队列基本信息成功", haWorkQueue);
    }


    //取Map中Value最小值
    public static Integer getMinValue(Map<Long, Integer> map) {
        Collection<Integer> c = map.values();
        Object[] obj = c.toArray();
        Arrays.sort(obj);
        //最小value
        Integer minValue = (Integer) obj[0];
        return minValue;
    }

    //随机取出同一个状态下，分配人数最小相同的id集合中的一个
    public static Long getRandomListId(List<Long> list) {
        Random random = new Random();
        int num = random.nextInt(list.size());
        return list.get(num);
    }

}
