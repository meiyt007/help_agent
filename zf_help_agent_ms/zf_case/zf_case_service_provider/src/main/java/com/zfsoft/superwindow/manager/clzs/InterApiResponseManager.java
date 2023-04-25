package com.zfsoft.superwindow.manager.clzs;

import com.zfsoft.superwindow.data.clzs.InterApiResponse;
import com.zfsoft.superwindow.dbaccess.dao.DbInterApiResponseMapper;
import com.zfsoft.superwindow.dbaccess.data.DbInterApiResponse;
import com.zfsoft.superwindow.dbaccess.data.DbInterApiResponseExample;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.SysCode;
import com.zfsoft.superwindow.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 接口响应值
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InterApiResponseManager {

    @Resource
    private DbInterApiResponseMapper dbInterApiResponseMapper;

   /***
   * @Description:更新目录元素
   * @Author:liangss
   * @Date:2021/11/4
   * @Param: [cardCatalogueElement]
   */
    public void saveOrUpdate(InterApiResponse interApiResponse) throws Exception {
        DbInterApiResponse dbInterApiResponse = null;
        if (null != interApiResponse.getId()) {
            dbInterApiResponse=this.dbInterApiResponseMapper.selectByPrimaryKey(interApiResponse.getId());
            Assert.notNull(dbInterApiResponse, MessageFormat.format("更新对象不存在！对象id为{0}", dbInterApiResponse.getId()));
            org.springframework.beans.BeanUtils.copyProperties(interApiResponse, dbInterApiResponse);
            dbInterApiResponse.setModifyDate(new Date());
            this.dbInterApiResponseMapper.updateByPrimaryKeySelective(dbInterApiResponse);
        } else {
//            if(interApiResponse.getDeleteFlag().equals(SysCode.DELETE_STATUS.NO)){
                dbInterApiResponse = new DbInterApiResponse();
                org.springframework.beans.BeanUtils.copyProperties(interApiResponse, dbInterApiResponse);
                dbInterApiResponse.setDeleteFlag(SysCode.DELETE_STATUS.NO);
                dbInterApiResponse.setCreateDate(new Date());
                dbInterApiResponse.setModifyDate(new Date());
                dbInterApiResponse.setOid(UUIDUtil.randomUUID());
                this.dbInterApiResponseMapper.insert(dbInterApiResponse);
//            }

        }
    }

    /***
    * @Description:根据目录oid查询目录元素编码列表
    * @Author:liangss
    * @Date:2021/11/4
    * @Param: [cardCatalogueOid]
    */
    public List<InterApiResponse> getInterApiResponseList(Long interAPiId) {
        DbInterApiResponseExample dbInterApiResponseExample = new DbInterApiResponseExample();
        DbInterApiResponseExample.Criteria criteria = dbInterApiResponseExample.createCriteria();
        if (interAPiId != null) {
            criteria.andInterApiIdEqualTo(interAPiId);
        }
        criteria.andDeleteFlagEqualTo(0);
        List<DbInterApiResponse> dbInterApiResponses = dbInterApiResponseMapper.selectByExample(dbInterApiResponseExample);
        List<InterApiResponse> interApiResponses= dbInterApiResponses.stream().map(dbInterApiResponse -> {
            InterApiResponse interApiResponse = new InterApiResponse();
            BeanUtils.copyProperties(dbInterApiResponse,interApiResponse);
            return interApiResponse;
        }).collect(Collectors.toList());
        return interApiResponses;
    }

    public InterApiResponse getInterApiResponseById(Long id){
        InterApiResponse interApiResponse = new InterApiResponse();
        DbInterApiResponse dbInterApiResponse = dbInterApiResponseMapper.selectByPrimaryKey(id);
        BeanUtils.copyProperties(dbInterApiResponse, interApiResponse);
        return interApiResponse;
    }
}
