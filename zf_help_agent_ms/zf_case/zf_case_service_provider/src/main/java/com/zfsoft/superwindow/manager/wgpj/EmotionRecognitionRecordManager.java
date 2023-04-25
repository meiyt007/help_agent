package com.zfsoft.superwindow.manager.wgpj;

import com.alibaba.fastjson.JSONObject;
import com.zfsoft.microservice.settings.data.SysConfig;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.wgpj.EmotionRecognitionRecord;
import com.zfsoft.superwindow.dbaccess.dao.DbEmotionRecognitionRecordMapper;
import com.zfsoft.superwindow.dbaccess.data.DbEmotionRecognitionRecord;
import com.zfsoft.superwindow.feign.settings.SysConfigFeignService;
import com.zfsoft.superwindow.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/8/13 9:16
 */
@Slf4j
@Service
public class EmotionRecognitionRecordManager {
    @Resource
    private DbEmotionRecognitionRecordMapper dbEmotionRecognitionRecordMapper;

    @Resource
    private SysConfigFeignService sysConfigFeignService;

    /**
     * 保存识别的结果
     *
     * @param responseBody
     * @param caseNumber
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveEmotionRecognition(String responseBody, String caseNumber) {
        String body = responseBody.replace("[[","").replace("]]","") ;
        String[] arrays  = body.split(",");
        if(arrays !=null){
            EmotionRecognitionRecord emotionRecognition = new EmotionRecognitionRecord();
            emotionRecognition.setCode(caseNumber);
            emotionRecognition.setPhotoNum(Integer.parseInt(arrays[0])+1);
            emotionRecognition.setEmotion(arrays[1].substring(1,arrays[1].length()-1));
            emotionRecognition.setEmotionDegree(arrays[2]);
            emotionRecognition.setEmotionTop(arrays[3]);
            emotionRecognition.setEmotionRight(arrays[4]);
            emotionRecognition.setEmotionBottom(arrays[5]);
            emotionRecognition.setEmotionLeft(arrays[6]);
            emotionRecognition.setCreateDate(new Date());
            emotionRecognition.setCreateUser("123");
            DbEmotionRecognitionRecord dbEmotionRecognitionRecord = new DbEmotionRecognitionRecord();
            BeanUtils.copyProperties(emotionRecognition,dbEmotionRecognitionRecord);
            dbEmotionRecognitionRecordMapper.insert(dbEmotionRecognitionRecord);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveEmotionRecognition(String responseBody, String caseNumber,String  virtualBusinessOid,int  type,String  picAddress) {
        //responseBody =  "{\"result\":\"sad\",\"confidence \":0.5703148245811462,\"location\":[168,47,268,143]}";
        //String body = responseBody.replace("[[","").replace("]]","") ;
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        String userOid = "00000000123";
        if(currentLoginUser!=null){
            userOid = currentLoginUser.getUserOid();
        }
        JSONObject result = JSONObject.parseObject(responseBody);
        EmotionRecognitionRecord emotionRecognition = new EmotionRecognitionRecord();
        emotionRecognition.setCode(caseNumber);
        emotionRecognition.setPhotoNum(0);
        emotionRecognition.setEmotion(result.getString("result"));
        emotionRecognition.setEmotionDegree(result.getString("confidence"));
        String locations =  result.getString("location");
        locations = locations.replace("[","").replace("]","");
        String[] arrays  = locations.split(",");
        emotionRecognition.setEmotionTop(arrays[0]);
        emotionRecognition.setEmotionRight(arrays[1]);
        emotionRecognition.setEmotionBottom(arrays[2]);
        emotionRecognition.setEmotionLeft(arrays[3]);
        emotionRecognition.setVirtualBusinessOid(virtualBusinessOid);
        //计算分数
        long score =  computeScore(result.getString("result"),Double.parseDouble(result.getString("confidence")));
        emotionRecognition.setScore(score+"");
        emotionRecognition.setLocation(locations);
        emotionRecognition.setCreateDate(new Date());
        emotionRecognition.setCreateUser(userOid);
        emotionRecognition.setTypes(type);
        emotionRecognition.setPicAddress(StringUtils.isEmpty(picAddress)?"null":picAddress);
        DbEmotionRecognitionRecord dbEmotionRecognitionRecord = new DbEmotionRecognitionRecord();
        BeanUtils.copyProperties(emotionRecognition,dbEmotionRecognitionRecord);
        dbEmotionRecognitionRecordMapper.insert(dbEmotionRecognitionRecord);
    }

    public  long  computeScore(String emtion,double confidence){
        ApiResultSet<List<SysConfig>> lists = sysConfigFeignService.querySysConfigListByParentCode("QXCS");
        long baseScore  = 0;
        String emtionType = "";
        for(SysConfig sysConfig : lists.getData()){
            String code = sysConfig.getCode();
            String[] codeArrays  = code.split("_");
            if(emtion.equalsIgnoreCase(codeArrays[1])){
                baseScore = Integer.parseInt(sysConfig.getValue());
                emtionType = codeArrays[0];
                break;
            }

        }
        return computeScoreByScoreAndConfidence(baseScore,confidence,emtionType);
    }

    private long  computeScoreByScoreAndConfidence(long baseScore ,double confidence,String emtionType){
        long score = 0;
        if("forward".equalsIgnoreCase(emtionType)){
            score =  baseScore + Math.round(10*confidence);
        }else if("middle".equalsIgnoreCase(emtionType)){
            score =  baseScore + new Random().nextInt(11) ;
        }else if("negative".equalsIgnoreCase(emtionType)){
            score =  baseScore - Math.round(10*confidence);
        }
        return score;
    }

    public List<DbEmotionRecognitionRecord> queryEmotionRecognitionList(EmotionRecognitionRecord emotionRecognitionRecord) {
        DbEmotionRecognitionRecord dbEmotionRecognitionRecord = new DbEmotionRecognitionRecord();
        BeanUtils.copyProperties(emotionRecognitionRecord, dbEmotionRecognitionRecord);
        List<DbEmotionRecognitionRecord> dbVirtualBusinessRecordManualList = this.dbEmotionRecognitionRecordMapper.queryAll(dbEmotionRecognitionRecord);
        return dbVirtualBusinessRecordManualList;
    }

    public EmotionRecognitionRecord getEmotionRecognitionRecordById(Long id) {
        Assert.hasLength(String.valueOf(id), "主键不能为空！");
        DbEmotionRecognitionRecord dbEmotionRecognitionRecord = this.dbEmotionRecognitionRecordMapper.queryById(id);
        EmotionRecognitionRecord emotionRecognitionRecord = new EmotionRecognitionRecord();
        org.springframework.beans.BeanUtils.copyProperties(dbEmotionRecognitionRecord,emotionRecognitionRecord);
        return emotionRecognitionRecord;
    }

    public List<DbEmotionRecognitionRecord> getEmotionListById(String virtualBusinessOid,String caseOid) {
        List<DbEmotionRecognitionRecord> list = dbEmotionRecognitionRecordMapper.queryEmotionRecognitionListByVirtualBusinessOidAndCaseId(virtualBusinessOid,caseOid);
        return list;
    }
}
