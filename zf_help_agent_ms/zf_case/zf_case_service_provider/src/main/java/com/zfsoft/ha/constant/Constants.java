package com.zfsoft.ha.constant;


import java.util.HashMap;
import java.util.Map;

/**
 * 通用常量信息
 *
 * @author wangwg
 */
public class Constants {

    /**
     * 删除状态1 -- 未删除
     */
    public static final String DELETE_STATUS_NO = "1";
    /**
     * 删除状态 2 已删除
     */
    public static final String DELETE_STATUS_YES = "2";

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * session保存用户登录信息的key值
     */
    public static final String CURRENT_USER = "CURRENT_LOGIN_USER";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = "sub";

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * 等待服务
     */
    public static final String WAIT = "1";

    /**
     * 服务中
     */
    public static final String SERVICE_ING = "2";

    /**
     * 结束服务
     */
    public static final String END_SERVICE = "3";
    /**
     * 转派中
     */
    public static final String TURN_SERVICE = "4";

    /**
     * 帮代办工作人员服务次数 redis key
     */
    public static final String WORK_USER_SERVICE_NUM = "workUserServiceNun:";


    /**
     * 转派状态 1.待接收  待审核
     */
    public static final String TURN_STATUS_WAit_ACCEPT = "1";

    /**
     * 转派状态 2.接收  审核通过
     */
    public static final String TURN_STATUS_ACCEPT = "2";

    /**
     * 转派状态 3.退回  审核未通过
     */
    public static final String TURN_STATUS_BACK = "3";

    /**
     * turnConfig： 1.手动接收
     */
    public static final String TURN_CONFIG_MANUAL_ACCEPT = "1";

    /**
     * turnConfig： 2.自动接收
     */
    public static final String TURN_CONFIG_AUTO_ACCEPT = "2";

    /**
     * workUserStatus： 1.离线
     */
    public static final String WORK_USER_STATUS_OFF_LINE = "1";

    /**
     * workUserStatus： 2.忙碌
     */
    public static final String WORK_USER_STATUS_BUSY= "2";

    /**
     * workUserStatus： 3.空闲
     */
    public static final String WORK_USER_STATUS_FREE = "3";

    /**
     * workUserStatus： 4.服务中
     */
    public static final String WORK_USER_IN_SERVICE = "4";

    /**
     * 帮办预约状态 1 可以预约
     */
    public static final String APPON_STATUS_YES = "1";
    /**
     * 帮办预约状态 2 不可以预约
     * */
    public static final String APPON_STATUS_NO = "2";
    /**
     * 办事人预约  状态：未处理
     */
    public static final String APPON_STATUS_NODO = "1";
    /**
     * 办事人预约  状态：已处理
     */
    public static final String APPON_STATUS_DO = "2";
    /**
     * 办事人预约  状态：取消预约
     */
    public static final String APPON_STATUS_NEXTDO = "3";

    /**
     * 排班 午别;1-上午，2-下午，3-全天，4休息
     */
    public static final String SCHEDULE_AM = "1";
    public static final String SCHEDULE_PM = "2";
    public static final String SCHEDULE_AM_AND_PM = "3";
    public static final String SCHEDULE_NICE = "4";

    /**
     * 分组职务,1-组长，2-副组长，3-组员
     */
    public static final String GROUP_LEADER = "1";
    public static final String GROUP_DEPUTY_LEADER = "2";
    public static final String GROUP_MEMBERS = "3";

    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String TWO = "2";

    public static final Map<String,String> shareResult = new HashMap<>();
    /**
     * 取号状态 1-可取号  2-不可取号
     */
    public static final String TAKE_NUM_YES = "1";
    public static final String TAKE_NUM_NO = "2";
    /**
     * 一桌联办状态 1-可  2-不可
     */
    public static final String DESK_YES = "1";
    public static final String DESK_NO = "2";

    /**
     * 一桌联办流程状态 0未预约，1预约中，2已预约，3已发起一桌联办
     */
    public static final String DESK_NO_APPO = "0";
    public static final String DESK_APPO_ING = "1";
    public static final String DESK_MADE_APPO = "2";
    public static final String DESK_INITIATED = "3";

    /**
     * 取号类型：1-扫码取号，2-预约取号，3-普通取号, 4手机取号 5 视频咨询取号
     */
    public static final String TALE_NUM_TYPE_SMQH = "1";
    public static final String TALE_NUM_TYPE_YYQH = "2";
    public static final String TALE_NUM_TYPE_PTQH = "3";
    public static final String TALE_NUM_TYPE_SJQH = "4";
    public static final String TALE_NUM_TYPE_SPQH = "5";
    /**
     * 视频咨询用户类型 1-帮办人员，2-帮办组长，3-委办局老师，4-办事人
     */
    public static final String VIDEO_USER_WORK = "1";
    public static final String VIDEO_USER_LEADER = "2";
    public static final String VIDEO_USER_OUT = "3";
    public static final String VIDEO_USER_PEOPLE = "4";

    /**
     * 人员在房间的状态 1-呼叫中、2-接通、3-拒绝、4-退出、5-超时
     */
    public static final String VIDEO_ROOM_CALL = "1";
    public static final String VIDEO_ROOM_THROUGH = "2";
    public static final String VIDEO_ROOM_REFUSE = "3";
    public static final String VIDEO_ROOM_QUIT = "4";
    public static final String VIDEO_ROOM_TIMEOUT = "5";
    /**
     * 房间类型 1-办里人发起，2-帮办人员发起的
     */
    public static final String ROOM_TYPE_JBR = "1";
    public static final String ROOM_TYPE_BBRY = "2";


    /**
     * 房间状态;1-开启状态、2-关闭状态 3-等待中（申请人在创建队列时是等待中)）
     */
    public static final String ROOM_STATUS_KQ = "1";
    public static final String ROOM_STATUS_GB = "2";
    public static final String ROOM_STATUS_DD = "3";

    static{
        shareResult.put("-100","只能分享文件");
        shareResult.put("-101","请传入分享的目标帮办人id");
        shareResult.put("-102","新建分享文件夹失败");
        shareResult.put("-103","新建原帮办人员名称文件夹失败");
        shareResult.put("-104","新建资源文件失败");
        shareResult.put("-110","分享的资源不存在");
    }
}
