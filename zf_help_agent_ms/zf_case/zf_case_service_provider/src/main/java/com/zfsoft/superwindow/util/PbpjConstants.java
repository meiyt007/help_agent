package com.zfsoft.superwindow.util;

public class PbpjConstants {

    /**
     * 平板评价服务接口方法名称
     */
    public static String METHOD_LOGIN = "login"; // 登录
    public static String METHOD_GETPBSYSLIST = "getPbSysList"; // 获取评价语音文件
    public static String METHOD_GETPJTYPE = "getPjType"; // 获取评价项
    public static String METHOD_GETPTUSERBYID = "getPtUserById"; // 根据主键查找人员信息
    public static String METHOD_GETPBINFORMATIONLIST = "getPbInformationList"; // 获取展示信息
    public static String METHOD_GETPTSERVICE = "getPtService"; // 获取事项基本信息
    public static String METHOD_SAVEPJMARK = "savePjMark"; // 添加评价信息
    public static String METHOD_GETSVBYCODE = "getSVByCode"; // 判断软件版本是否为最新
    public static String METHOD_LOGINOUT = "loginOut"; // 注销
    public static String METHOD_GETPBCASEINFO = "getPbCaseInfo"; // 获取办件信息
    public static String METHOD_GETLICENSEANDCASEINFO = "getLicenseAndCaseInfo"; // 获取证照出件的办件信息
    public static String METHOD_GETCASERETURNINFO = "getCaseReturnInfo"; // 获取证照出件的办件信息
    public static String METHOD_SIGNUPLOADIMG = "signUploadImg"; // 签名上传图片

    /**
     * 办事指南列表
     * @param organOid: 机构OID
     */
    public static String METHOD_GETSERVICELIST = "/service/base/getServiceList.do?";
    /**
     * 办事指南详细
     * @param serviceOid: 事项OID
     */
    public static String METHOD_GETSERVICEBASEINFO = "/service/base/getServiceBaseInfo.do?";
    /**
     * 办件查询
     * @param number: 办件编号
     */
    public static String METHOD_GETCASEBYCASENUMBER = "/service/base/getCaseByCaseNumber.do?";

}

