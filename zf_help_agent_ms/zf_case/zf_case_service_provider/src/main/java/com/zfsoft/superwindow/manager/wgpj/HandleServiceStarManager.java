package com.zfsoft.superwindow.manager.wgpj;

import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.dbaccess.dao.DbHandleServiceStarMapper;
import com.zfsoft.superwindow.dbaccess.data.DbEmotionRecognitionRecord;
import com.zfsoft.superwindow.dbaccess.data.DbHandleServiceStar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/8/16 9:04
 */
@Slf4j
@Service
public class HandleServiceStarManager {

    @Resource
    private DbHandleServiceStarMapper dbHandleServiceStarMapper;

    /**
     * 计算评价星值
     *
     * @param virtualBusinessOid    虚拟业务记录表主键, caseNumber 排队号
     */
    public void calculateAverageStar(String virtualBusinessOid,String caseNumber) {
        CurrentLoginUser currentLoginUser  = CurrentLoginUserHolder.getCurrentLoginUser();
        String userOid = "123";
        if(currentLoginUser!=null){
            userOid = currentLoginUser.getUserOid();
        }
        //List<DbEmotionRecognitionRecord> list = dbEmotionRecognitionRecordMapper.queryEmotionRecognitionListByVirtualBusinessOid(virtualBusinessOid);
        List<DbEmotionRecognitionRecord> list = new ArrayList<>();
        int totalStar = 0;
        int averageStar = 0;
        int toalStarNum = 0;//总共多少条记录
        if(list !=null&&list.size()>0){
            for(DbEmotionRecognitionRecord dbEmotionRecognitionRecord : list){
                totalStar = totalStar + Integer.parseInt(dbEmotionRecognitionRecord.getScore());
                toalStarNum++;
            }
        }
        if(toalStarNum>0){
            averageStar = totalStar/toalStarNum;
        }
        //DbHandleServiceStar serviceStar =  dbHandleServiceStarMapper.queryHandleServiceStarByCode(caseNumber);
        DbHandleServiceStar serviceStar =  dbHandleServiceStarMapper.queryHandleServiceStarByVirtualBusinessOid(virtualBusinessOid);
        if(serviceStar !=null){
            serviceStar.setStarValue(averageStar);
            dbHandleServiceStarMapper.update(serviceStar);
        }else{
            DbHandleServiceStar dbHandleServiceStar = new DbHandleServiceStar();
            dbHandleServiceStar.setStarValue(averageStar);
            dbHandleServiceStar.setCode(caseNumber);
            dbHandleServiceStar.setCreateDate(new Date());
            dbHandleServiceStar.setCreateUser(userOid);
            dbHandleServiceStar.setVirtualBusinessOid(virtualBusinessOid);
            dbHandleServiceStarMapper.insert(dbHandleServiceStar);
        }
    }


    public Integer getStarAndScoreByOid(String virtualBusinessOid) {
        Integer score = null;
        DbHandleServiceStar serviceStar =  dbHandleServiceStarMapper.queryHandleServiceStarByVirtualBusinessOid(virtualBusinessOid);
        if(serviceStar !=null){
            score = serviceStar.getStarValue();
        }
        return score;
    }
}
