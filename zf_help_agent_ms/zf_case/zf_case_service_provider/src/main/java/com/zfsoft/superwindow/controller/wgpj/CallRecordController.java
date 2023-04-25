package com.zfsoft.superwindow.controller.wgpj;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.wgpj.SaveCallRecord;
import com.zfsoft.superwindow.dbaccess.data.DbSaveCallRecord;
import com.zfsoft.superwindow.manager.wgpj.CallRecordManager;
import com.zfsoft.superwindow.service.wgpj.CallRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/8/18 10:57
 */
@RestController
@Slf4j
public class CallRecordController implements CallRecordService {

    @Resource
    private CallRecordManager callRecordManager;

    @Override
    public ApiResultSet<String> SaveCallRecord(SaveCallRecord saveCallRecord) {
        String oid = callRecordManager.SaveCallRecord(saveCallRecord);
        return new ApiResultSet<>(oid);
    }

    @Override
    public ApiResultSet selectCallNums() {
        ApiResultSet  apiResultSet = new ApiResultSet();
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        List<DbSaveCallRecord>  lists =  callRecordManager.selectCallNums();
        JSONArray  result  =  new JSONArray();
        List<String>  callNums =  new ArrayList<>();
        for(DbSaveCallRecord dbSaveCallRecord : lists){
            /*if(!result.contains(dbSaveCallRecord.getCallNum())){
                result.add(dbSaveCallRecord.getCallNum());
            }*/
            if(!callNums.contains(dbSaveCallRecord.getCallNum())){
                JSONObject job = new JSONObject();
                job.put("name",dbSaveCallRecord.getCallNum());
                job.put("type",0);//0 之前的号，1 调用取叫号系统，获取的号
                job.put("windowName",dbSaveCallRecord.getCallInfo());
                result.add(job);
                callNums.add(dbSaveCallRecord.getCallNum());
            }
        }
        //后期修改成，调用接口获取，未叫号的号。
        apiResultSet.setData(result);
        return apiResultSet;
    }

    @Override
    public ApiResultSet querySaveCallRecordByOid(String oid) {
        SaveCallRecord saveCallRecord = callRecordManager.querySaveCallRecordByOid(oid);
        return  new ApiResultSet(saveCallRecord);
    }
}
