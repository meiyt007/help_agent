package com.zfsoft.ha.managers;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.ha.data.HaSms;
import com.zfsoft.ha.dbaccess.dao.DbHaSmsMapper;
import com.zfsoft.ha.dbaccess.data.DbHaSms;
import com.zfsoft.ha.dbaccess.data.example.DbHaSmsExample;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.bean.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2023/1/29 15:59
 */
@Service
@Slf4j
public class HaSmsManager{

    /**
     * 对接mapper
     */
    @Resource
    private DbHaSmsMapper dbHaSmsMapper;
    /**
    * Description: 查询短信发送情况
    * @param haSms 短信参数
    * @param beginTime 开始时间
    * @param endTime 结束时间
    * @author zhaobf
    * date: 2023/1/29 16:14
    * @copyright 版权由上海卓繁信息技术股份有限公司拥有
    */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaSms> selectSmsData(HaSms haSms, Date beginTime, Date endTime) {
        DbHaSmsExample example = new DbHaSmsExample();
        DbHaSmsExample.Criteria criteria = example.createCriteria();
        if (haSms != null) {
            if (StrUtil.isNotEmpty(haSms.getWorkUserName())) {
                criteria.andWorkUserNameLike("%" + haSms.getWorkUserName() + "%");
            }
            if (haSms.getWorkUserId()!=null) {
                criteria.andWorkUserIdEqualTo(haSms.getWorkUserId());
            }
            if (StrUtil.isNotEmpty(haSms.getPhone())) {
                criteria.andPhoneLike(haSms.getPhone() + "%");
            }
            if (StrUtil.isNotEmpty(haSms.getTitle())) {
                criteria.andTitleLike(haSms.getTitle() + "%");
            }
            if (StrUtil.isNotEmpty(haSms.getResult())) {
                criteria.andResultLike(haSms.getResult() + "%");
            }
            if (beginTime!=null) {
                criteria.andSendDateGreaterThanOrEqualTo(beginTime);
            }
            if (endTime!=null) {
                criteria.andSendDateLessThanOrEqualTo(endTime);
            }
            example.setOrderByClause("CREATE_DATE desc");

        }
        List<DbHaSms> dbHaSms = dbHaSmsMapper.selectByExample(example);
        List<HaSms> result = BeanUtils.copyListProperties(dbHaSms, HaSms::new);
        return result;
    }



    /**
     * 查询预约人员分页数据
     *
     * @param haSms 短信参数
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param pageNum    分页参数
     * @param pageSize   分页参数
     * @return 分页结果集
     * @author yupeng
     * *@date 2022年08月12 10:33:51
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public PageResult<HaSms> queryPage(HaSms haSms, Date beginTime, Date endTime, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);

        Page<HaSms> dbHaAppointmentPage = (Page<HaSms>) this.selectSmsData(haSms,beginTime,endTime);

        PageResult<HaSms> haAppointmentPage = new PageResult<>(dbHaAppointmentPage.getPageNum(), dbHaAppointmentPage.getPageSize(), dbHaAppointmentPage.getTotal());

        haAppointmentPage.setData(dbHaAppointmentPage);

        return haAppointmentPage;
    }
}
