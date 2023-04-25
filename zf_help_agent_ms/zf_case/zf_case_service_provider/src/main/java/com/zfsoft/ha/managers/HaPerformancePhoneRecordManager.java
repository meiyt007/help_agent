package com.zfsoft.ha.managers;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaPerformancePhoneRecord;
import com.zfsoft.ha.data.requestData.HaPerPhoneRequestData;
import com.zfsoft.ha.data.vo.HaExcelPerformance;
import com.zfsoft.ha.dbaccess.dao.DbHaPerformancePhoneRecordMapper;
import com.zfsoft.ha.dbaccess.data.DbHaPerformancePhoneRecord;
import com.zfsoft.ha.dbaccess.data.example.DbHaPerformancePhoneRecordExample;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.ha.util.ReadExcel;
import com.zfsoft.ocr.data.pojo.exception.ServiceException;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.validate.ParamValid;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 绩效常规时长统计管理业务实现层
 * @author dingsn
 * @date 2022/10/31  11:21
 */
@Service
@Slf4j
public class HaPerformancePhoneRecordManager {


    @Resource
    private DbHaPerformancePhoneRecordMapper dbHaPerformancePhoneRecordMapper;

    /**
     * Description: 查询电话绩效分页信息列表
     * @author zhaobf
     * date: 2023/3/22 13:23
     * @copyright 版权由上海卓繁信息技术股份有限公司拥有
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public PageResult<HaPerformancePhoneRecord> queryPerPhoneWithPage(HaPerPhoneRequestData haPerPhoneRequestData) throws Exception {
        //分页参数
        if (null == haPerPhoneRequestData.getPageNumber() || haPerPhoneRequestData.getPageNumber() <= 0) {
            haPerPhoneRequestData.setPageNumber(0)  ;
        }
        if (null == haPerPhoneRequestData.getPageSize() || haPerPhoneRequestData.getPageSize() <= 0) {
            haPerPhoneRequestData.setPageSize(10)  ;
        }
        PageHelper.startPage(haPerPhoneRequestData.getPageNumber(), haPerPhoneRequestData.getPageSize());

        DbHaPerformancePhoneRecordExample splitExample = new DbHaPerformancePhoneRecordExample();
        DbHaPerformancePhoneRecordExample.Criteria criteria = splitExample.createCriteria();
//        if (StrUtil.isNotEmpty(name)) {
//            criteria.andNameLike("%" + name + "%");
//        }
        if (StrUtil.isNotEmpty(haPerPhoneRequestData.getPhone())) {
            criteria.andPhoneEqualTo(haPerPhoneRequestData.getPhone());
        }
        if (StrUtil.isNotEmpty(haPerPhoneRequestData.getCallResults())) {
            criteria.andCallResultsEqualTo(haPerPhoneRequestData.getCallResults());
        }
        if (StrUtil.isNotEmpty(haPerPhoneRequestData.getWorkNumber())) {
            criteria.andWorkNumberLike("%"+haPerPhoneRequestData.getWorkNumber()+"%");
        }
        if (haPerPhoneRequestData.getBeginTime()!=null && !haPerPhoneRequestData.getBeginTime().equals("")) {
            criteria.andCallTimeGreaterThanOrEqualTo(DateUtil.datetimeFormat.parse(haPerPhoneRequestData.getBeginTime()));
        }
        if (haPerPhoneRequestData.getEndTime()!=null && !haPerPhoneRequestData.getBeginTime().equals("")) {
            criteria.andCallTimeLessThanOrEqualTo(DateUtil.datetimeFormat.parse(haPerPhoneRequestData.getEndTime().replace("00:00:00","23:59:59")));
        }
        criteria.andDeleteStatusEqualTo("1");
        splitExample.setOrderByClause("CREATE_DATE desc");
        Page<DbHaPerformancePhoneRecord> splits = (Page<DbHaPerformancePhoneRecord>) dbHaPerformancePhoneRecordMapper.selectByExample(splitExample);
        PageResult<HaPerformancePhoneRecord> pageResult = new PageResult<>(splits.getPageNum(), splits.getPageSize(), splits.getTotal());
        List<HaPerformancePhoneRecord> workUserList = splits.stream().map(dbGroupServic -> {
            HaPerformancePhoneRecord haWorkGroup = new HaPerformancePhoneRecord();
            BeanUtils.copyProperties(dbGroupServic, haWorkGroup);
            return haWorkGroup;
        }).collect(Collectors.toList());
        pageResult.setData(workUserList);
        return pageResult;
    }


    /**
     * @param id 主键
     * @return ApiResultSet
     * @description: 删除电话绩效信息
     * @author: zhaobf
     * @Date: 2023/3/22
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public Integer deletePerPhoneById(Long id) throws Exception {
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        DbHaPerformancePhoneRecord dbHaPerformancePhoneRecord = dbHaPerformancePhoneRecordMapper.queryById(id);
        dbHaPerformancePhoneRecord.setDeleteStatus("2");
        dbHaPerformancePhoneRecord.setUpdateBy(loginUser.getUserName());
        dbHaPerformancePhoneRecord.setUpdateDate(new Date());
        DbHaPerformancePhoneRecordExample dbHaWorkGroupExample = new DbHaPerformancePhoneRecordExample();
        DbHaPerformancePhoneRecordExample.Criteria criteria = dbHaWorkGroupExample.createCriteria();
        criteria.andIdEqualTo(id);
        int index = dbHaPerformancePhoneRecordMapper.updateByExampleSelective(dbHaPerformancePhoneRecord, dbHaWorkGroupExample);
        return index;
    }

    /**
     * @param HaPerformancePhoneRecord 电话绩效实体类
     * @return ApiResultSet 获取新增或者修改电话绩效信息标识
     * @description: 新增或者修改电话绩效信息
     * @author: zhaobf
     * @Date: 2023/3/22
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public Map<String, Object> saveOrUpdatePerPhone(HaPerformancePhoneRecord HaPerformancePhoneRecord) {
        Map<String, Object> map = new HashMap();
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        HaLoginUserInfo haLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        String loginName = loginUser!=null?loginUser.getUserName():haLoginUserInfo!=null?haLoginUserInfo.getName():"系统管理员";
        int index = 0;
        //判断是修改还是新增
        if (HaPerformancePhoneRecord.getId() != null) {
            //修改
            DbHaPerformancePhoneRecord dbHaPerformancePhoneRecord = dbHaPerformancePhoneRecordMapper.queryById(HaPerformancePhoneRecord.getId());
            BeanUtils.copyProperties(HaPerformancePhoneRecord, dbHaPerformancePhoneRecord);
            dbHaPerformancePhoneRecord.setUpdateBy(loginName); //获取当前登录用户名称
            dbHaPerformancePhoneRecord.setUpdateDate(new Date()); //获取当前时间
            DbHaPerformancePhoneRecordExample dbHaPerformancePhoneRecordExample = new DbHaPerformancePhoneRecordExample();
            DbHaPerformancePhoneRecordExample.Criteria criteria = dbHaPerformancePhoneRecordExample.createCriteria();
            criteria.andIdEqualTo(HaPerformancePhoneRecord.getId());
            index = dbHaPerformancePhoneRecordMapper.updateByExampleSelective(dbHaPerformancePhoneRecord, dbHaPerformancePhoneRecordExample);
            map.put("index", index);
        } else {
            //新增
            DbHaPerformancePhoneRecord dbHaWorkGroup = new DbHaPerformancePhoneRecord();
            BeanUtils.copyProperties(HaPerformancePhoneRecord, dbHaWorkGroup);
            dbHaWorkGroup.setDeleteStatus("1");
            dbHaWorkGroup.setCreateBy(loginName);
            dbHaWorkGroup.setCreateDate(new Date());
            dbHaWorkGroup.setUpdateBy(loginName); //获取当前登录用户名称
            dbHaWorkGroup.setUpdateDate(new Date()); //获取当前时间
            index = dbHaPerformancePhoneRecordMapper.insertSelective(dbHaWorkGroup);
            map.put("index", index);
        }
        return map;
    }

    /**
     * 根据id查询电话绩效记录
     * @param id
     * @return
     */
    public HaPerformancePhoneRecord selectPerPhoneById(Long id){
        DbHaPerformancePhoneRecord dbHaPerformancePhoneRecord = dbHaPerformancePhoneRecordMapper.queryById(id);
        HaPerformancePhoneRecord haPerformancePhoneRecord = new HaPerformancePhoneRecord();
        BeanUtils.copyProperties(dbHaPerformancePhoneRecord, haPerformancePhoneRecord);
        return haPerformancePhoneRecord;
    }
   
    /**
    * Description: 导入excel，返回表单的信息
    * @param request 请求
    * @param files 文件excel
    * @author zhaobf
    * date: 2023/3/22 10:15
    * @copyright 版权由上海卓繁信息技术股份有限公司拥有
    */
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    public Map<String, String> ImportPhoneExcel(HttpServletRequest request, MultipartFile[] files)  {
        Map<String,String> result = new HashMap<>();
        MultipartFile file = files[0];
        List<HaExcelPerformance> excelResult = new ArrayList<>();
        //根据excel获取电话信息
        try {
            List<List<Object>> excel = ReadExcel.getListByExcel(file.getInputStream(),file.getOriginalFilename());
            //去除首行标题和总结
            for(int i=1;i<excel.size();i++){
                HaExcelPerformance haExcelPerformance = new HaExcelPerformance();
                if(((String)excel.get(i).get(0)).contains("合计")){
                    continue;
                }
                haExcelPerformance.setPhone((String) excel.get(i).get(0));
                haExcelPerformance.setName((String) excel.get(i).get(1));
                String time = (String) excel.get(i).get(2);
                haExcelPerformance.setCallTime(StringUtil.isNotEmpty(time)?DateUtil.time_sdf2.parse((String) excel.get(i).get(2)):null);
                String duration = extracted((String) excel.get(i).get(3));
                haExcelPerformance.setDuration(duration);
                haExcelPerformance.setAppraise((String) excel.get(i).get(4));
                haExcelPerformance.setCallNotes((String) excel.get(i).get(5));
                haExcelPerformance.setIntentResults((String) excel.get(i).get(6));
                haExcelPerformance.setCallResults((String) excel.get(i).get(7));
                haExcelPerformance.setWorkNumber((String) excel.get(i).get(8));
                excelResult.add(haExcelPerformance);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("分析excel异常。异常信息=" + e.getMessage());
        }
        //将excel数据插入电话绩效记录表，根据电话号码和呼叫时间进行唯一判定
        List<Integer> index = excelResult.stream().map(e -> {
            DbHaPerformancePhoneRecord dbPhoneRecord = dbHaPerformancePhoneRecordMapper.selectByPhoneAndCallTime(e.getPhone(), e.getCallTime());
            if (dbPhoneRecord != null) {
                return 0;
            } else {
                HaPerformancePhoneRecord phoneRecord = new HaPerformancePhoneRecord();
                BeanUtils.copyProperties(e, phoneRecord);
                Map<String, Object> stringObjectMap = this.saveOrUpdatePerPhone(phoneRecord);
                return (Integer) stringObjectMap.get("index");
            }
        }).collect(Collectors.toList());

        long notDo = index.stream().filter(s -> s == 0).count();
        long insert = index.stream().filter(s -> s == 1).count();
        result.put("insertCount", String.valueOf(insert));
        result.put("notDoCount", String.valueOf(notDo));
        result.put("totalCount", String.valueOf(notDo+insert));
        return result;

    }

    private String  extracted(String duration) {
        int hour = 0;
        int min = 0;
        int sec = 0;

        String[] date2;
        String[] date1 = duration.split("时");

        if(date1.length==2) {
            hour = Integer.parseInt(date1[0]);
            date2 = date1[1].split("分");
        }else if(date1.length==1) {
            hour = 0;
            date2 = date1[0].split("分");
        }else{
            return hour+"时"+min+"分"+sec+"秒";
        }

        if(date2.length==2) {
            min = Integer.parseInt(date2[0]);
            sec = Integer.parseInt(date2[1].split("秒")[0]);
        }else{
            min = 0;
            sec = Integer.parseInt(date2[0].split("秒")[0]);
        }

//        return hour+"时"+min+"分"+sec+"秒";
        return (hour*24*60+min*60+sec)+"秒";
    }
}
