package com.zfsoft.single.controller.fzgl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.util.SendSmsUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.single.data.fzgl.CaseLicenseManage;
import com.zfsoft.single.data.fzgl.EmsParamManage;
import com.zfsoft.single.dbaccess.data.DbCaseLicenseDeliverRecord;
import com.zfsoft.single.manager.fzgl.CaseLincenseManageManager;
import com.zfsoft.single.service.fzgl.CaseLicenseManageService;
import com.zfsoft.single.util.DateUtil;
import com.zfsoft.single.util.StringUtils;
import com.zfsoft.single.util.SysCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 证照签发
 * dongxl
 */
@Slf4j
@RestController
public class CaseLicenseManageController implements CaseLicenseManageService {

    @Resource
    private CaseLincenseManageManager caseLincenseManageManager;

    @Override
    public ApiResultSet queryPageList(CaseLicenseManage caseLicenseManage, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CaseLicenseManage> caseLicenseList = this.caseLincenseManageManager.queryPage(caseLicenseManage);
        if(caseLicenseList!=null && caseLicenseList.size()>0){
            PageResult<CaseLicenseManage> pageResult = new PageResult<>(((Page) caseLicenseList).getPageNum(), ((Page) caseLicenseList).getPageSize(), ((Page) caseLicenseList).getTotal());
            pageResult.setData(caseLicenseList);
            log.info("获取证照签收调用成功结果为：{}", JSON.toJSONString(pageResult));
            return new ApiResultSet(pageResult);
        }
        return new ApiResultSet();
    }

    @Override
    public ApiResultSet getOneByCaseOid(String caseOid) {
        CaseLicenseManage license=this.caseLincenseManageManager.getOneByCaseOid(caseOid);
        return new ApiResultSet(license);
    }

    @Override
    public ApiResultSet saveOrUpdateSign( CaseLicenseManage caseLicenseManage) {
        log.info("证照签收信息新增/更新成功：{}", JSON.toJSONString(caseLicenseManage));
        String res=this.caseLincenseManageManager.saveOrUpdateSign(caseLicenseManage);
        ApiResultSet ss=new ApiResultSet();
        if(res!=null){
            ss.setCode(500);
        }else {
            //发送短信
            CaseLicenseManage manage = caseLincenseManageManager.getOneByCaseOid(caseLicenseManage.getRegOid());
            if(manage!=null){
                if(String.valueOf(SysCode.STATUS.YES).equals(manage.getIsCms())){
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String dateOfIssue = DateUtil.getNowDate(df.format(new Date()));
                    if (StrUtil.isNotEmpty(manage.getApplyPhone())) {
                        String messageContent =  manage.getApplyUserName()+"您好，您的办件"+manage.getCaseNumber()+"于"+dateOfIssue+"已制证完成,请尽快来领取!";
                        SendSmsUtil.sendSms(manage.getApplyPhone(), messageContent);
                    }
                }
            }
        }
        return ss;
    }

    @Override
    public ApiResultSet<PageResult<CaseLicenseManage>> queryPageListIssued(CaseLicenseManage caseLicenseManage, Integer pageNum, Integer pageSize) {
        PageResult<CaseLicenseManage> pageResult = this.caseLincenseManageManager.queryPageListIssued(caseLicenseManage, pageNum, pageSize);
        ApiResultSet<PageResult<CaseLicenseManage>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        log.info("获取证照签发调用成功结果为：{}", JSON.toJSONString(pageResult));
        return new ApiResultSet(pageResult);
    }

    @Override
    public ApiResultSet<List<CaseLicenseManage>> queryIssuedList(CaseLicenseManage caseLicenseManage) {
        List<CaseLicenseManage> caseLicenseManageList = this.caseLincenseManageManager.queryIssuedList(caseLicenseManage);
        ApiResultSet<List<CaseLicenseManage>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(caseLicenseManageList);
        return apiResultSet;
    }

    //提交快递信息到快递平台
    @Override
    public ApiResultSet saveOrUpdateKd(EmsParamManage emsParamManage) {
        ApiResultSet apiResultSet=new ApiResultSet();
        //请求ems 正式放开
        //String result = caseLincenseManageManager.submitInfoToExpress(emsParamManage);
        //测试数据
        String result = "{\"responseOrders\":[{\"txLogisticID\":\"bd16bddd94804ac18c2902e4044c7c6f\",\"mailNo\":\"1166832240261\",\"success\":\"T\"}],\"success\":\"T\"}";
        log.info("get result：{}", result);
        if (StringUtils.isEmpty(result)) {
            log.error("请先获取授权码或程序错误！");
            apiResultSet.setCode(500);
        } else {
            DbCaseLicenseDeliverRecord dbCaseLicenseDeliverRecord = caseLincenseManageManager.handleEmsInfo(result, emsParamManage);
            //原逻辑不改变
            if (dbCaseLicenseDeliverRecord !=null) {
                String str= caseLincenseManageManager.saveOrUpdateKd(emsParamManage.getOid(), dbCaseLicenseDeliverRecord.getMailNo(),
                        emsParamManage.getAddresseeName(), emsParamManage.getAddresseePhone());

            }
            apiResultSet.setCode(200);
        }
        apiResultSet.setData(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return apiResultSet;
    }

    @Override
    public ApiResultSet selectByCaseNumber(String caseNumber) {
        CaseLicenseManage license=caseLincenseManageManager.selectByCaseNumber(caseNumber);
        return new  ApiResultSet(license);
    }

    @Override
    public ApiResultSet saveOrUpdateCaseLicenseManage(CaseLicenseManage caseLicenseManage) {
        caseLincenseManageManager.saveOrUpdateCaseLicenseManage(caseLicenseManage);
        return new ApiResultSet(caseLicenseManage);
    }

    @Override
    public ApiResultSet getDeliverRecordByCaseOid(String caseOid) {
        DbCaseLicenseDeliverRecord dbCaseLicenseDeliverRecord = caseLincenseManageManager.getDeliverRecordByCaseOid(caseOid);
        return new ApiResultSet(dbCaseLicenseDeliverRecord);
    }
}
