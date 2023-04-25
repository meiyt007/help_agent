package com.zfsoft.ha.constant;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/9/19 13:27
 */
public class RedisConstants {

    //登录认证  token
    public static final String LOGIN_SESSION_TOKEN = "login:token:";

    //登录的时效 2h
    public static final Long  LOGIN_SESSION_TTL = 2L;

    //登录认证  token
    public static final String LOGIN_DOCKING_SESSION_TOKEN = "login:docking:token:";

    //登录的时效 1D
    public static final Long  LOGIN_DOCKING_SESSION_TTL = 1L;

    //上一次查询办事队列的时间
    public static final String LAST_SELECT_QUEUE_TIME = "lastSelectQueueTime:";
    //上一次查询办事队列的时间时效  1d
    public static final Long LAST_SELECT_QUEUE_TTL = 1L;

    //取号key
    public static final String TASK_NUMBER="getTaskNumber:";
    //取号key时效 1d
    public static final Long TASK_NUMBER_TTL = 1L;

    //获取上一次记录的时间
    public static final String VIDEO_LATEST_TIME = "videoLatestTime:";
    //获取上一次记录的时间
    public static final String LATEST_TIME = "latestTime";
    //获取上一次记录的时间时效 2h
    public static final Long LATEST_TTL = 2L;

    //转派超时时间
    public static final Long  TURN_TIMEOUT= 300000L;

    //一桌联办  各部门确认预约信息 key
    public static final String DESK_GROUP = "desk:group:";
//    //获取上一次记录的时间时效 24h*7
//    public static final Long DESK_GROUP_TTL = 7L;
    //视频咨询用户编号
    public static final String VIDEO_USER_NUM="video:user:num";
    public static final String ChECK_CODE="checkCode:";
}
