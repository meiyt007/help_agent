package com.zfsoft.ha.managers;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.ha.constant.Constants;
import com.zfsoft.ha.data.HaAppointment;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.requestData.HaAppointmentRequestData;
import com.zfsoft.ha.data.vo.HaAppointmentVo;
import com.zfsoft.ha.dbaccess.dao.DbHaAppointmentMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaServiceMapper;
import com.zfsoft.ha.dbaccess.data.DbHaAppointment;
import com.zfsoft.ha.dbaccess.data.example.DbHaAppointmentExample;
import com.zfsoft.ha.dbaccess.data.vo.DbHaAppointmentVo;
import com.zfsoft.ha.dbaccess.data.vo.DbHaServiceListVo;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.ApiResultSet;
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
import java.util.stream.Collectors;

/**
 * 预约信息service层
 *
 * @author zhaobf
 * @version 1.0
 * @date 2022/7/17 10:07
 */
@Service
@Slf4j
public class HaAppointmentManager {

    /**
     * 旗舰店预约mapper
     */
    @Resource
    DbHaAppointmentMapper dbHaAppointmentMapper;

    /**
     * 对接mapper
     */
    @Resource
    private DbHaServiceMapper dbHaServiceMapper;

    /**
     * 查询预约信息
     *
     * @param haAppointment
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaAppointment> selectByHaAppointment(HaAppointment haAppointment,Date beginTime,Date endTime) {
        DbHaAppointmentExample example = new DbHaAppointmentExample();
        DbHaAppointmentExample.Criteria criteria = example.createCriteria();
        if (haAppointment != null) {
            if (StrUtil.isNotEmpty(haAppointment.getName())) {
                criteria.andNameLike("%" + haAppointment.getName() + "%");
            }
            if (beginTime!=null) {
                criteria.andAppointmentTimeGreaterThanOrEqualTo(DateUtil.date2Str(beginTime,DateUtil.date_sdf));
            }
            if (endTime!=null) {
                criteria.andAppointmentTimeLessThanOrEqualTo(DateUtil.date2Str(endTime,DateUtil.date_sdf));
            }
            if (StrUtil.isNotEmpty(haAppointment.getCardNo())) {
                criteria.andCardNoEqualTo(haAppointment.getCardNo());
            }
            if (StrUtil.isNotEmpty(haAppointment.getCompanyCode())) {
                example.or().andCompanyCodeEqualTo(haAppointment.getCompanyCode());
            }
            example.setOrderByClause("CREATE_DATE desc");

        }
        List<DbHaAppointment> dbHaAppointments = dbHaAppointmentMapper.selectByExample(example);
        List<HaAppointment> haQuestionList = dbHaAppointments.stream().map(dbHaAppointment1 -> {
            HaAppointment HaAppointment1 = new HaAppointment();
            BeanUtils.copyProperties(dbHaAppointment1, HaAppointment1);
            return HaAppointment1;
        }).collect(Collectors.toList());
        return haQuestionList;
    }


    /**
     * 保存旗舰店的预约数据
     *
     * @param haAppointmentRequestData 旗舰店的预约数据
     * @return 结果信息
     * @author yupeng
     * @date 2022/7/28 15:30
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public String savePushAppointment(HaAppointmentRequestData haAppointmentRequestData) {

        DbHaAppointment dbHaAppointment = new DbHaAppointment();
        BeanUtils.copyProperties(haAppointmentRequestData, dbHaAppointment);
        dbHaAppointment.setCreateDate(new Date());
        dbHaAppointment.setAppointmentStatus(Constants.APPON_STATUS_NODO);
        int result = dbHaAppointmentMapper.insert(dbHaAppointment);
        //判断数据是否保存成功
        if (result < 0) {
            return "预约失败，请重试！";
        }
        return null;

    }

    /**
     * 保存预约信息
     *
     * @param haAppointment 预约数据
     * @return 接口请求信息
     * @author：zhaobf
     * @date：2022/8/5 15:30
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public HaAppointment save(HaAppointment haAppointment) {
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        if (null == haAppointment.getId()) {
            haAppointment.setCreateDate(new Date());
            haAppointment.setUpdateDate(new Date());
            haAppointment.setUpdateBy(currentHaLoginUserInfo.getName());
            haAppointment.setCreateBy(currentHaLoginUserInfo.getName());
        }
//        if(null == haUserResource.getAttaOid()){
//            haUserResource.setAttaOid(UUID.randomUUID().toString().replaceAll("-", ""));
//        }
        haAppointment.setUpdateDate(new Date());
        haAppointment.setUpdateBy(currentHaLoginUserInfo==null?"取号机":currentHaLoginUserInfo.getName());
        DbHaAppointment dbHaAppointment = new DbHaAppointment();
        BeanUtils.copyProperties(haAppointment, dbHaAppointment);
        int index = 0;
        if (null == haAppointment.getId()) {
            index = dbHaAppointmentMapper.insert(dbHaAppointment);
        } else {
            dbHaAppointment.setUpdateDate(new Date());
            index = dbHaAppointmentMapper.update(dbHaAppointment);
        }
        return haAppointment;
    }


    /**
     * 查询帮代办服务的列表
     *
     * @param cardNo      身份证号码
     * @param name        姓名
     * @param companyName 企业名称
     * @param companyCode 统一社会信用代码证
     * @return 查询结果
     * @author yupeng
     * @date 2022年08月09 15:26:57
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public ApiResultSet getHaServiceList(String cardNo, String name, String companyName, String companyCode) {

        /*if (StringUtil.isEmpty(cardNo) && StringUtil.isEmpty(companyCode)) {
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR, "身份证号码和统一社会信用代码不能全为空！");
        }*/
        if (StringUtil.isEmpty(cardNo)) {
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR, "身份证号码不能为空！");
        }
        List<DbHaServiceListVo> haServiceList = dbHaServiceMapper.getHaServiceList(cardNo, name, companyName, companyCode);
        return ApiResultSet.ok(haServiceList);
    }

    /**
     * 查询预约人员分页数据
     *
     * @param name       预约人姓名
     * @param workUserId 预约的工作人员
     * @param status     预约状态
     * @param pageNum    分页参数
     * @param pageSize   分页参数
     * @return 分页结果集
     * @author yupeng
     * *@date 2022年08月12 10:33:51
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public PageResult<HaAppointment> queryAppointmentPage(String name,String cardNo, String workUserId, String status, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);

        //查询分页数据
//     * @param name         预约人姓名
//     * @param workUserId 预约的工作人员id
//     * @param status       预约状态
        DbHaAppointment dbHaAppointment = new DbHaAppointment();
        dbHaAppointment.setName(name);
        dbHaAppointment.setCardNo(cardNo);
        dbHaAppointment.setAppointmentWorkUserId(workUserId);
        dbHaAppointment.setAppointmentStatus(status);
        Page<DbHaAppointmentVo> dbHaAppointmentPage = (Page<DbHaAppointmentVo>) dbHaAppointmentMapper.queryAppointmentList(dbHaAppointment);

        PageResult<HaAppointment> haAppointmentPage = new PageResult<>(dbHaAppointmentPage.getPageNum(), dbHaAppointmentPage.getPageSize(), dbHaAppointmentPage.getTotal());

        List<HaAppointment> haAppointmentList = dbHaAppointmentPage.stream().map(dbHaAppointmentVo -> {
            HaAppointment haAppointment = new HaAppointment();
            BeanUtils.copyProperties(dbHaAppointmentVo, haAppointment);
            return haAppointment;
        }).collect(Collectors.toList());

        haAppointmentPage.setData(haAppointmentList);

        return haAppointmentPage;
    }
    public List<HaAppointment> queryAppointmentList(HaAppointment HaAppointment){
        DbHaAppointment dbHaAppointment = new DbHaAppointment();
        BeanUtils.copyProperties(HaAppointment, dbHaAppointment);
        List<DbHaAppointmentVo> dbHaAppointmentVos = dbHaAppointmentMapper.queryAppointmentList(dbHaAppointment);
        List<HaAppointment> haAppointmentList = dbHaAppointmentVos.stream().map(dbHaAppointmentVo -> {
            HaAppointment haAppointment = new HaAppointment();
            BeanUtils.copyProperties(dbHaAppointmentVo, haAppointment);
            return haAppointment;
        }).collect(Collectors.toList());
        return haAppointmentList;
    }

    /**
     * 查询预约人员信息
     * @param cardNo
     * @param startTime
     * @param endTime
     * @return
     */
    public List<HaAppointmentVo> queryAppointmentByCardNo(String cardNo,Date  startTime,Date  endTime,String appointmentStatus){
//        String s = DateUtil.date2Str(startTime, DateUtil.date_sdf);
        List<DbHaAppointmentVo> dbHaAppointmentVos = dbHaAppointmentMapper.queryAppointmentByCardNo(cardNo, DateUtil.date2Str(startTime,DateUtil.date_sdf), DateUtil.date2Str(endTime,DateUtil.date_sdf),appointmentStatus);
        List<HaAppointmentVo> haAppointmentList = dbHaAppointmentVos.stream().map(dbHaAppointmentVo -> {
            HaAppointmentVo haAppointment = new HaAppointmentVo();
            BeanUtils.copyProperties(dbHaAppointmentVo, haAppointment);
            return haAppointment;
        }).collect(Collectors.toList());
        return haAppointmentList;
    }

    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public int remove(Long id) {
        DbHaAppointment dbHaAppointment = dbHaAppointmentMapper.selectByPrimaryKey(id);
        if(dbHaAppointment==null) return -1;
        dbHaAppointment.setAppointmentStatus(Constants.APPON_STATUS_NEXTDO);
        return dbHaAppointmentMapper.update(dbHaAppointment);
    }

    public HaAppointment getById(Long id) {
        DbHaAppointment dbHaAppointment = dbHaAppointmentMapper.selectByPrimaryKey(id);
        HaAppointment haAppointment = new HaAppointment();
        BeanUtils.copyProperties(dbHaAppointment,haAppointment);
        return haAppointment;
    }
}
