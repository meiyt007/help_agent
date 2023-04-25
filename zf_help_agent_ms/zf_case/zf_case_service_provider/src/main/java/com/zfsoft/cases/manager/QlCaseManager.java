package com.zfsoft.cases.manager;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.data.QlCaseApplay;
import com.zfsoft.cases.data.vo.QlCaseVo;
import com.zfsoft.cases.dbaccess.dao.QlCaseApplayMapper;
import com.zfsoft.cases.dbaccess.dao.QlCaseMapper;
import com.zfsoft.cases.dbaccess.data.DbQlCase;
import com.zfsoft.cases.dbaccess.data.DbQlCaseApplay;
import com.zfsoft.cases.dbaccess.data.example.QlCaseExample;
import com.zfsoft.cases.dbaccess.data.vo.DbQlCaseVo;
import com.zfsoft.cases.feign.*;
import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.cases.util.LimitDateCalc;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.cases.util.StringUtils;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.sxDirectory.data.SxServiceType;
import com.zfsoft.service.sxDirectory.service.SxServiceTypeService;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.service.sxService.data.SxServiceExtend;
import com.zfsoft.service.sxService.service.SxServiceExtendService;
import com.zfsoft.service.sxService.service.SxServiceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @（#）: QlCaseManager
 * @description: 办件基本信息实现类
 * @author: wangwg
 * @date: 2020/10/23
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "case:qlcase")
public class QlCaseManager {

    private static final int DEFAULT_LENGTH = 4;


    @Resource
    private QlCaseMapper qlCaseMapper;

    @Resource
    private QlCaseApplayMapper qlCaseApplayMapper;

    @Resource
    RedisTemplate<String, Serializable> redisTemplate;

    @Resource
    private SxServiceService sxServiceFeginService;

    @Resource
    private SxServiceTypeService sxServiceTypeFeginService;

    @Resource
    private SxServiceExtendService sxServiceExtendFeginService;

    @Resource
    private SysOrganFeginService sysOrganFeginService;

    @Resource
    private SysUserFeginService sysUserFeginService;

    @Resource
    private ZzqPushCaseMsgFeignService zzqPushCaseMsgFeignService;

    @Value(value = "${zfsoft.zzq.obtainCaseNum.isAble}")
    private String obtainCaseNumIsAble;
    private static final String TRUE = "true";

    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public Map<String, String> saveQlCase(QlCase qlCase) {
        Map<String, String> map = new HashMap<String, String>();
        String serviceImplementCode = null;
        if (!"0".equals(qlCase.getCaseOid())) {
            //查询是否已有记录
            DbQlCase aCase = qlCaseMapper.queryQlCaseByCaseOid(qlCase.getCaseOid());
            if (StringUtil.isNotEmpty(qlCase.getServiceOid())) {
                ApiResultSet<SxService> resultSet = sxServiceFeginService.getSxServiceByOid(qlCase.getServiceOid());
                SxService sxService = resultSet.getData();
                qlCase.setServiceOid(sxService.getServiceOid());
                qlCase.setServiceName(sxService.getServiceName());
                qlCase.setOrganOid(sxService.getOrganOid());
                qlCase.setCaseType(String.valueOf(sxService.getCaseType()));
                qlCase.setServiceName(sxService.getServiceName());
                qlCase.setServiceType(sxService.getServiceTypeOid());
                ApiResultSet<SxServiceType> sxServiceTypes = sxServiceTypeFeginService.getSxServiceTypeByOid(sxService.getServiceTypeOid());
                SxServiceType serviceType = sxServiceTypes.getData();
                qlCase.setServiceTypeName(serviceType.getServiceTypeName());
                serviceImplementCode = sxService.getImplementCode();
            }
            if (aCase != null) {
                qlCase.setId(aCase.getId());
                if (!StringUtil.isEmpty(aCase.getCaseNumber())) {
                    String caseNumber = null;
                    DbQlCase dbQlCase = qlCaseMapper.queryQlCaseByCaseNumber(aCase.getCaseNumber());
                    if (!aCase.getCaseOid().equals(dbQlCase.getCaseOid())) {
                        caseNumber = this.creatCaseNumber(serviceImplementCode);
                    } else {
                        caseNumber = aCase.getCaseNumber();
                    }
                    qlCase.setCaseNumber(caseNumber);
                }
                qlCase.setCreateDate(aCase.getCreateDate());
            } else {
                if (null == qlCase.getId()) {
                    qlCase.setId(null);
                }
                if (StringUtil.isEmpty(qlCase.getCaseNumber())) {
                    String caseNumber = this.creatCaseNumber(serviceImplementCode);
                    qlCase.setCaseNumber(caseNumber);
                }
                qlCase.setCreateDate(new Date());
            }

            //TODO 网办，暂无工作人原登录信息，后续更改
            // 除窗口端外，创建用户应为空 update by qiaolei 2022-6-29
//            qlCase.setCreateUserOid("00000000000000000000000000000010");

            if (StringUtil.isEmpty(qlCase.getCaseOid())) {
                qlCase.setCaseOid(UUID.randomUUID().toString().replaceAll("-", ""));
            }
            qlCase.setModifyDate(new Date());
            qlCase.setDelFlag(0);
            DbQlCase dbQlCase = new DbQlCase();
            BeanUtils.copyProperties(qlCase, dbQlCase);
            int index = 0;
            if (null == qlCase.getId()) {
                index = qlCaseMapper.insert(dbQlCase);
            } else {
                dbQlCase.setModifyDate(new Date());
                index = qlCaseMapper.update(dbQlCase);
            }
            //保存成功
            if (index >= 0) {
                map.put("caseOid", dbQlCase.getCaseOid());
                if (dbQlCase.getCaseStatus() != null) {
                    map.put("caseStatus", dbQlCase.getCaseStatus().toString());
                }
                map.put("caseNumber", dbQlCase.getCaseNumber());
                map.put("serviceTypeName", dbQlCase.getServiceTypeName());
                map.put("createDate", new SimpleDateFormat("yyyy-MM-dd").format(dbQlCase.getCreateDate()));
            }
        }
        return map;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateQlcase(DbQlCase qlCase) {
        qlCaseMapper.update(qlCase);
    }

    /**
     * 生成办件编号接口，这里调用自治区办件编号生成接口
     *
     * @param serviceImplementCode 事项实施编码
     * @return String
     * @author WangKe
     * @date 2022-06-15
     **/
    protected String creatCaseNumber(String serviceImplementCode) {
        if (TRUE.equals(obtainCaseNumIsAble)) {
            ApiResultSet<String> caseData = zzqPushCaseMsgFeignService.getCaseNumByServiceImplementCode(serviceImplementCode);
            JSONObject jsonObj = JSON.parseObject(caseData.getData());
            String caseNumber = jsonObj.getString("caseNumber");
            return caseNumber;
        } else {
            /** 办件编号编码规则 31位实施编码+8位时间+4位流水号 */
            StringBuffer temp = new StringBuffer();
            temp.append(serviceImplementCode);
            temp.append(DateUtil.getDateAndTime("yyyyMMdd"));
            /** 获取今天的日期:yyyyMMdd */
            String date = DateUtil.getDateAndTime("yyyyMMdd");
            String key = "serial.number:" + date;
            /** 自增 一天时间，单位秒（过期时间）*/
            long liveTime = 86400;
            long sequence = this.incr(key, liveTime);
            String serialNumber = this.getSerialNumber(sequence);
            temp.append(serialNumber);
            return temp.toString();
        }
    }

    protected Long incr(String key, long liveTime) {
        //redis使用incr自增序号(如果key不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作)
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();
        //业务需求办件编号从1开始编号，为0时自增一次
        if (increment == 0) {
            RedisAtomicLong entityIdCounterIncr = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
            increment = entityIdCounterIncr.getAndIncrement();
        }
        if ((null == increment || increment.longValue() == 1) && liveTime > 0) {
            //初始设置过期时间  1天
            entityIdCounter.expire(liveTime, TimeUnit.SECONDS);
        }
        return increment;
    }

    protected String getSerialNumber(long seq) {
        String str = String.valueOf(seq);
        int len = str.length();
        // 取决于业务规模,应该不会超过4位（后四位流水号）
        if (len >= DEFAULT_LENGTH) {
            return str;
        }
        int rest = DEFAULT_LENGTH - len;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rest; i++) {
            sb.append('0');
        }
        sb.append(str);
        return sb.toString();
    }

    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public DbQlCase deleteQlCaseByCaseOid(String caseOid) {
        DbQlCase dbQlCase = qlCaseMapper.queryQlCaseByCaseOid(caseOid);
        dbQlCase.setDelFlag(1);
        qlCaseMapper.update(dbQlCase);
        return dbQlCase;
    }

    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public DbQlCase deleteQlCaseByCaseNumber(String caseNumber) {
        DbQlCase dbQlCase = qlCaseMapper.queryQlCaseByCaseNumber(caseNumber);
        dbQlCase.setDelFlag(1);
        qlCaseMapper.update(dbQlCase);
        return dbQlCase;
    }

    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public DbQlCase deleteZcQlCaseByCaseOid(String caseOid) {
        DbQlCase dbQlCase = qlCaseMapper.queryQlCaseByCaseOid(caseOid);
        if (null == dbQlCase) {
            throw new ResultInfoException("办件不存在！");
        }
        if (0 != dbQlCase.getCaseStatus()) {
            throw new ResultInfoException("该办件为非暂存件，不允许删除！");
        }
        dbQlCase.setDelFlag(1);
        qlCaseMapper.update(dbQlCase);
        return dbQlCase;
    }

    public PageResult<QlCase> queryQlCaseWithPage(QlCase qlCase, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);

        DbQlCase dbQlcase = new DbQlCase();
        BeanUtils.copyProperties(qlCase, dbQlcase);
        Page<DbQlCase> dbDbQlCases = (Page<DbQlCase>) qlCaseMapper.selectQlCaseList(dbQlcase);
        PageResult<QlCase> pageResult = new PageResult<>(dbDbQlCases.getPageNum(), dbDbQlCases.getPageSize(), dbDbQlCases.getTotal());
        List<QlCase> qlCaseList = dbDbQlCases.stream().map(dbQlCase -> {
            QlCase qlcase = new QlCase();
            BeanUtils.copyProperties(dbQlCase, qlcase);
            return qlcase;
        }).collect(Collectors.toList());
        pageResult.setData(qlCaseList);
        return pageResult;
    }

    //@Cacheable(key = "'queryQlCaseByCaseOid:caseOid=' + #caseOid", unless = "#result == null")
    public QlCase queryQlCaseByCaseOid(String caseOid) {
        if (StringUtil.isEmpty(caseOid)) {
            throw new ResultInfoException("办件业务编码为空！");
        }
        DbQlCase dbQlCase = qlCaseMapper.queryQlCaseByCaseOid(caseOid);
        if (dbQlCase != null) {
            QlCase qlCase = new QlCase();
            BeanUtils.copyProperties(dbQlCase, qlCase);
            return qlCase;
        }
        return null;
    }

    //@Cacheable(key = "'queryQlCaseByCaseNumber:caseNumber=' + #caseNumber", unless = "#result == null")
    public QlCase queryQlCaseByCaseNumber(String caseNumber) {
        if (StringUtil.isEmpty(caseNumber)) {
            throw new ResultInfoException("办件编号为空！");
        }
        DbQlCase dbQlCase = qlCaseMapper.queryQlCaseByCaseNumber(caseNumber);
        QlCase qlCase = new QlCase();
        if (dbQlCase != null) {
            BeanUtils.copyProperties(dbQlCase, qlCase);
        }
        return qlCase;
    }

    public PageResult<QlCase> queryDoneQlCasePage(QlCaseVo doneCaseVo, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        DbQlCaseVo dbQlCaseVo = new DbQlCaseVo();
        BeanUtils.copyProperties(doneCaseVo, dbQlCaseVo);
        Page<DbQlCase> dbDbQlCases = (Page<DbQlCase>) qlCaseMapper.selectDoneQlCaseList(dbQlCaseVo);
        PageResult<QlCase> pageResult = new PageResult<>(dbDbQlCases.getPageNum(), dbDbQlCases.getPageSize(), dbDbQlCases.getTotal());
        List<QlCase> qlCaseList = dbDbQlCases.stream().map(dbQlCase -> {
            QlCaseApplay applay = null;
            DbQlCaseApplay dbQlCaseApplay = qlCaseApplayMapper.queryQlCaseApplayByCaseOid(dbQlCase.getCaseOid());
            if (dbQlCaseApplay != null) {
                applay = new QlCaseApplay();
                BeanUtils.copyProperties(dbQlCaseApplay, applay);
            }
            QlCase qlcase = new QlCase();
            BeanUtils.copyProperties(dbQlCase, qlcase);
            qlcase.setApplay(applay);
            return qlcase;
        }).collect(Collectors.toList());
        pageResult.setData(qlCaseList);
        return pageResult;
    }

    public PageResult<QlCase> queryQlCasePage(DbQlCaseVo doneCaseVo, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        Page<DbQlCase> dbDbQlCases = (Page<DbQlCase>) qlCaseMapper.selectApplyQlCaseList(doneCaseVo);
        PageResult<QlCase> pageResult = new PageResult<>(dbDbQlCases.getPageNum(), dbDbQlCases.getPageSize(), dbDbQlCases.getTotal());
        List<QlCase> qlCaseList = dbDbQlCases.stream().map(dbQlCase -> {
            QlCase qlcase = new QlCase();
            BeanUtils.copyProperties(dbQlCase, qlcase);
            return qlcase;
        }).collect(Collectors.toList());
        pageResult.setData(qlCaseList);
        return pageResult;
    }

    public PageResult<QlCase> queryKstbQlCasePage(DbQlCaseVo doneCaseVo, String loginUserOid, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);

        List<String> authUserOidList = queryListUserOidByLoginUser(loginUserOid);
        doneCaseVo.setFilterUserOidList(authUserOidList);
        Page<DbQlCase> dbDbQlCases = (Page<DbQlCase>) qlCaseMapper.selectKstbApplyQlCaseList(doneCaseVo);
        PageResult<QlCase> pageResult = new PageResult<>(dbDbQlCases.getPageNum(), dbDbQlCases.getPageSize(), dbDbQlCases.getTotal());
        List<QlCase> qlCaseList = dbDbQlCases.stream().map(dbQlCase -> {
            QlCaseApplay applay = null;
            DbQlCaseApplay dbQlCaseApplay = qlCaseApplayMapper.queryQlCaseApplayByCaseOid(dbQlCase.getCaseOid());
            if (dbQlCaseApplay != null) {
                applay = new QlCaseApplay();
                BeanUtils.copyProperties(dbQlCaseApplay, applay);
            }
            QlCase qlcase = new QlCase();
            BeanUtils.copyProperties(dbQlCase, qlcase);
            qlcase.setApplay(applay);
            return qlcase;
        }).collect(Collectors.toList());
        pageResult.setData(qlCaseList);
        return pageResult;
    }


    public PageResult<QlCase> selectQlCaseSuperviseList(QlCaseVo doneCaseVo, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
       /* DbQlCaseVo dbQlCaseVo = new DbQlCaseVo();
        BeanUtils.copyProperties(doneCaseVo, dbQlCaseVo);
        Page<DbQlCase> dbDbQlCases = (Page<DbQlCase>) qlCaseMapper.selectQlCaseSuperviseList(dbQlCaseVo);
        PageResult<QlCase> pageResult = new PageResult<>(dbDbQlCases.getPageNum(), dbDbQlCases.getPageSize(), dbDbQlCases.getTotal());*/
        QlCaseExample qlCaseExample = new QlCaseExample();
        QlCaseExample.Criteria criteria = qlCaseExample.createCriteria();
        if (StringUtil.isNotEmpty(doneCaseVo.getCaseNumber())) {
            criteria.andCaseNumberLike("%" + doneCaseVo.getCaseNumber() + "%");
        }
        if (StrUtil.isNotBlank(doneCaseVo.getApplyUserName())) {
            criteria.andApplyUserNameLike("%" + doneCaseVo.getApplyUserName() + "%");
        }
        if (StringUtil.isNotEmpty(doneCaseVo.getProjectName())) {
            criteria.andProjectNameLike("%" + doneCaseVo.getProjectName() + "%");
        }
        if (StringUtil.isNotEmpty(doneCaseVo.getRegisterUser())) {
            criteria.andCreateUserOidEqualTo(doneCaseVo.getRegisterUser());
        }
        if (StringUtil.isNotEmpty(doneCaseVo.getServiceType())) {
            criteria.andServiceTypeEqualTo(doneCaseVo.getServiceType());
        }
        if (doneCaseVo.getSourceApp() != null) {
            criteria.andSourceAppEqualTo(doneCaseVo.getSourceApp());
        }
        if (doneCaseVo.getServiceOids() != null) {
            criteria.andServiceOidIn(doneCaseVo.getServiceOids());
        }
        // 过滤条件：办件编号、申请人名称、申请时间、是否删除、办件状态为已办结
        List<Integer> values = new ArrayList<Integer>();
        values.add(-1);
        values.add(4);
        values.add(0);
        values.add(1);
        criteria.andCaseStatusNotIn(values);
        //逻辑变化了 查询不到数据
        /*
         * if (StringUtil.isNotEmpty(doneCaseVo.getOverTime())){
         * if(doneCaseVo.getOverTime().equals("0")){ DateTime startDay =
         * cn.hutool.core.date.DateUtil.date(new Date());
         * criteria.andShouldConcludeDateGreaterThanOrEqualTo(startDay); }else{ DateTime
         * startDay = cn.hutool.core.date.DateUtil.date(new Date());
         * criteria.andShouldConcludeDateLessThan(startDay); } }
         */
        try {
            if (StringUtil.isNotEmpty(doneCaseVo.getStartDate())) {
                DateTime startDay = cn.hutool.core.date.DateUtil.parse(doneCaseVo.getStartDate());
                DateTime StartDate = cn.hutool.core.date.DateUtil.beginOfDay(startDay);
                criteria.andCreateDateGreaterThanOrEqualTo(StartDate);
            } else {
                criteria.andCreateDateGreaterThanOrEqualTo(DateUtil.addDate(new Date(), -7));
            }
            if (StringUtil.isNotEmpty(doneCaseVo.getEndDate())) {
                DateTime endDay = cn.hutool.core.date.DateUtil.parse(doneCaseVo.getEndDate());
                DateTime endDate = cn.hutool.core.date.DateUtil.endOfDay(endDay);
                criteria.andCreateDateLessThan(endDate);
            } else {
                criteria.andCreateDateLessThan(DateUtil.addDate(new Date(), 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        criteria.andDelFlagEqualTo(BaseStaticParameter.N);
        qlCaseExample.setOrderByClause(" CREATE_DATE DESC ");
       /* List<Integer> staus = new ArrayList<>();
        staus.add(3);
        staus.add(4);
        criteria.andCaseStatusIn(staus);*/
        Page<DbQlCase> dbQlCasePage = (Page<DbQlCase>) this.qlCaseMapper.selectByExample(qlCaseExample);
        PageResult<QlCase> pageResult = new PageResult<>(dbQlCasePage.getPageNum(), dbQlCasePage.getPageSize(), dbQlCasePage.getTotal());
        List<QlCase> qlCaseList = dbQlCasePage.stream().map(dbQlCase -> {
            QlCaseApplay applay = new QlCaseApplay();
            DbQlCaseApplay dbQlCaseApplay = qlCaseApplayMapper.queryQlCaseApplayByCaseOid(dbQlCase.getCaseOid());
            if (dbQlCaseApplay != null) {
                BeanUtils.copyProperties(dbQlCaseApplay, applay);
            }
            QlCase qlcase = new QlCase();
            if (dbQlCase != null) {
                BeanUtils.copyProperties(dbQlCase, qlcase);
            }
            if (qlcase != null && qlcase.getAcceptanceDate() != null) {
                Date addDate = null;
                ApiResultSet<SxServiceExtend> resultSet = sxServiceExtendFeginService.getSxServiceExtendByServiceOid(qlcase.getServiceOid());
                SxServiceExtend extend = resultSet.getData();
                if (null != extend.getPromiseLimitType() && !extend.getPromiseLimitType().equals("")) {
                    Integer limit = extend.getPromiseLimit().intValue();
                    if (extend.getPromiseLimitType().equals("H")) {
                        addDate = LimitDateCalc.dateCalc(qlcase.getAcceptanceDate(), 0, extend.getPromiseLimitType(), limit);
                    } else {
                        //应办结时间
                        addDate = DateUtil.getExpireDate(qlcase.getAcceptanceDate(), Integer.parseInt(extend.getPromiseLimit().toString()), doneCaseVo.getHolidays());
                    }
                }
                if (addDate != null) {
                    qlcase.setShouldConcludeDate(addDate);
                    if (addDate.before(new Date())) {
                        if (null != qlcase.getCaseStatus() && !qlcase.getCaseStatus().equals("3")) {//办理中
                            qlcase.setOverTime("1");
                        } else {
                            qlcase.setOverTime("0");
                        }
                    } else {
                        qlcase.setOverTime("0");
                    }
                } else {
                    qlcase.setOverTime("0");
                }
            }
            qlcase.setApplay(applay);
            return qlcase;
        }).collect(Collectors.toList());

        List<QlCase> alllist = null;
        if (doneCaseVo.getOverTime().equals("0")) {
            alllist = qlCaseList.stream().filter(qlcase -> qlcase.getOverTime() != null).sorted(Comparator.comparing(QlCase::getOverTime)).collect(Collectors.toList());
        } else {
            alllist = qlCaseList.stream().filter(qlcase -> qlcase.getOverTime() != null).sorted(Comparator.comparing(QlCase::getOverTime).reversed()).collect(Collectors.toList());
        }
        pageResult.setData(alllist);
        return pageResult;
    }

    public PageResult<QlCase> queryRqhbQlCasePage(QlCaseVo doneCaseVo, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        DbQlCaseVo dbQlCaseVo = new DbQlCaseVo();
        BeanUtils.copyProperties(doneCaseVo, dbQlCaseVo);
        Page<DbQlCase> dbDbQlCases = (Page<DbQlCase>) qlCaseMapper.selectRqhbQlCaseList(dbQlCaseVo);
        PageResult<QlCase> pageResult = new PageResult<>(dbDbQlCases.getPageNum(), dbDbQlCases.getPageSize(), dbDbQlCases.getTotal());
        List<QlCase> qlCaseList = dbDbQlCases.stream().map(dbQlCase -> {
            QlCase qlcase = new QlCase();
            if (dbQlCase.getCaseOid() != null) {
                BeanUtils.copyProperties(dbQlCase, qlcase);
            }
            return qlcase;
        }).collect(Collectors.toList());
        pageResult.setData(qlCaseList);
        return pageResult;
    }

    @Cacheable(key = "'createCaseNumber:serviceOid=' + #serviceOid", unless = "#result == null")
    public Map<String, String> createCaseNumber(String serviceOid) {
        if (StringUtil.isEmpty(serviceOid)) {
            throw new ResultInfoException("事项业务主键为空！");
        }
        Map<String, String> map = new HashMap<String, String>();
        ApiResultSet<SxService> resultSet = sxServiceFeginService.getSxServiceByOid(serviceOid);
        SxService sxService = resultSet.getData();
        if (sxService == null) {
            throw new ResultInfoException("找不到所属事项！");
        }
        if (StringUtil.isEmpty(sxService.getImplementCode())) {
            throw new ResultInfoException("事项实施编码为空！");
        }
        String caseNumber = this.creatCaseNumber(sxService.getImplementCode());
        map.put("caseNumber", caseNumber);
        return map;
    }

    @Cacheable(key = "'queryTemporaryQlCasePage:caseVo=' + #caseVo+',pageNum=' + #pageNum+',pageSize=' + #pageSize", unless = "#result == null")
    public PageResult<QlCase> queryTemporaryQlCasePage(QlCaseVo caseVo, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        DbQlCaseVo dbQlCaseVo = new DbQlCaseVo();
        if (StringUtils.isNotEmpty(caseVo.getCaseNumber())) {
            String caseNumber = "%" + caseVo.getCaseNumber() + "%";
            caseVo.setCaseNumber(caseNumber);
        }
        if (StringUtils.isNotEmpty(caseVo.getProjectName())) {
            String projectName = "%" + caseVo.getProjectName() + "%";
            caseVo.setProjectName(projectName);
        }
        if (StringUtils.isNotEmpty(caseVo.getApplyUserName())) {
            String applyUserName = "%" + caseVo.getApplyUserName() + "%";
            caseVo.setApplyUserName(applyUserName);
        }
        BeanUtils.copyProperties(caseVo, dbQlCaseVo);
        Page<DbQlCase> dbDbQlCases = (Page<DbQlCase>) qlCaseMapper.selectTemporaryQlCaseList(dbQlCaseVo);
        PageResult<QlCase> pageResult = new PageResult<>(dbDbQlCases.getPageNum(), dbDbQlCases.getPageSize(), dbDbQlCases.getTotal());
        List<QlCase> qlCaseList = dbDbQlCases.stream().map(dbQlCase -> {
            QlCaseApplay applay = new QlCaseApplay();
            DbQlCaseApplay dbQlCaseApplay = qlCaseApplayMapper.queryQlCaseApplayByCaseOid(dbQlCase.getCaseOid());
            if (dbQlCaseApplay != null) {
                BeanUtils.copyProperties(dbQlCaseApplay, applay);
            }
            QlCase qlcase = new QlCase();
            if (dbQlCase != null) {
                BeanUtils.copyProperties(dbQlCase, qlcase);
            }
            qlcase.setApplay(applay);
            return qlcase;
        }).collect(Collectors.toList());
        pageResult.setData(qlCaseList);
        return pageResult;
    }

    // @Cacheable(key = "'getQlCaseByPackageCaseOid:packageCaseOid=' + #packageCaseOid", unless = "#result == null")
    public List<QlCase> getQlCaseByPackageCaseOid(String packageCaseOid) {
        List<DbQlCase> dbDbQlCases = qlCaseMapper.getQlCaseByPackageCaseOid(packageCaseOid);
        List<QlCase> qlCaseList = dbDbQlCases.stream().map(dbQlCase -> {
            QlCase qlcase = null;
            if (dbQlCase != null) {
                qlcase = new QlCase();
                BeanUtils.copyProperties(dbQlCase, qlcase);
            }
            return qlcase;
        }).collect(Collectors.toList());
        return qlCaseList;
    }

    //@Cacheable(key = "'queryQlCaseWithPageForZcsl:doneCaseVo=' + #doneCaseVo+',pageNum=' + #pageNum+',pageSize=' + #pageSize", unless = "#result == null")
    public PageResult<QlCase> queryQlCaseWithPageForZcsl(QlCaseVo doneCaseVo, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        QlCaseExample qlCaseExample = new QlCaseExample();
        QlCaseExample.Criteria criteria = qlCaseExample.createCriteria();

        // 过滤条件：办件编号、申请人名称、申请时间、是否删除、办件状态为已办结
        if (StringUtil.isNotEmpty(doneCaseVo.getCaseNumber())) {
            criteria.andCaseNumberLike("%" + doneCaseVo.getCaseNumber() + "%");
        }
        if (StringUtil.isNotEmpty(doneCaseVo.getApplyUserName())) {
            criteria.andApplyUserNameLike("%" + doneCaseVo.getApplyUserName() + "%");
        }
        if (StringUtil.isNotEmpty(doneCaseVo.getProjectName())) {
            criteria.andProjectNameLike("%" + doneCaseVo.getProjectName() + "%");
        }
        if (StringUtil.isNotEmpty(doneCaseVo.getServiceType())) {
            criteria.andServiceTypeEqualTo(doneCaseVo.getServiceType());
        }
        try {
            if (StringUtil.isNotEmpty(doneCaseVo.getStartDate())) {
                DateTime startDay = cn.hutool.core.date.DateUtil.parse(doneCaseVo.getStartDate());
                DateTime StartDate = cn.hutool.core.date.DateUtil.beginOfDay(startDay);
                criteria.andCreateDateGreaterThanOrEqualTo(StartDate);
            }
            if (StringUtil.isNotEmpty(doneCaseVo.getEndDate())) {
                DateTime endDay = cn.hutool.core.date.DateUtil.parse(doneCaseVo.getEndDate());
                DateTime endDate = cn.hutool.core.date.DateUtil.endOfDay(endDay);
                criteria.andCreateDateLessThan(endDate);
            }
        } catch (Exception e) {
        }
        criteria.andDelFlagEqualTo(BaseStaticParameter.N);
        qlCaseExample.setOrderByClause(" CREATE_DATE DESC ");
       /* List<Integer> staus = new ArrayList<>();
        staus.add(3);
        staus.add(4);
        criteria.andCaseStatusIn(staus);*/
        Page<DbQlCase> dbQlCasePage = (Page<DbQlCase>) this.qlCaseMapper.selectByExample(qlCaseExample);
        PageResult<QlCase> pageResult = new PageResult<>(dbQlCasePage.getPageNum(), dbQlCasePage.getPageSize(), dbQlCasePage.getTotal());
        List<QlCase> qlCaseList = dbQlCasePage.stream().map(dbQlCase -> {
            QlCaseApplay applay = new QlCaseApplay();
            DbQlCaseApplay dbQlCaseApplay = qlCaseApplayMapper.queryQlCaseApplayByCaseOid(dbQlCase.getCaseOid());
            if (null != dbQlCaseApplay) {
                BeanUtils.copyProperties(dbQlCaseApplay, applay);
            }
            QlCase qlcase = new QlCase();
            BeanUtils.copyProperties(dbQlCase, qlcase);
            qlcase.setApplay(applay);
            return qlcase;
        }).collect(Collectors.toList());
        pageResult.setData(qlCaseList);
       /* DbQlCaseVo dbQlCaseVo = new DbQlCaseVo();
        BeanUtils.copyProperties(doneCaseVo, dbQlCaseVo);
        Page<DbQlCase> dbDbQlCases = (Page<DbQlCase>) qlCaseMapper.selectZcslQlCaseList(dbQlCaseVo);
        PageResult<QlCase> pageResult = new PageResult<>(dbDbQlCases.getPageNum(), dbDbQlCases.getPageSize(), dbDbQlCases.getTotal());
        List<QlCase> qlCaseList = dbDbQlCases.stream().map(dbQlCase -> {
            QlCaseApplay applay=new QlCaseApplay();
            DbQlCaseApplay dbQlCaseApplay = qlCaseApplayMapper.queryQlCaseApplayByCaseOid(dbQlCase.getCaseOid());
            BeanUtils.copyProperties(dbQlCaseApplay, applay);
            QlCase qlcase = new QlCase();
            BeanUtils.copyProperties(dbQlCase, qlcase);
            qlcase.setApplay(applay);
            return qlcase;
        }).collect(Collectors.toList());
        pageResult.setData(qlCaseList);*/
        return pageResult;
    }


    /**
     * 查询材料退件列表
     *
     * @param doneCaseVo
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResult<QlCase> querycltjQlCaseList(QlCaseVo doneCaseVo, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        DbQlCaseVo dbQlCaseVo = new DbQlCaseVo();

        if (StringUtil.isEmpty(doneCaseVo.getReturnStatus())) {
            doneCaseVo.setReturnStatus("1");
        }
        if (StringUtil.isEmpty(doneCaseVo.getInformStatus())) {
            doneCaseVo.setInformStatus("1");
        }
        BeanUtils.copyProperties(doneCaseVo, dbQlCaseVo);
        Page<DbQlCase> dbDbQlCases = (Page<DbQlCase>) qlCaseMapper.selectcltjQlCaseList(dbQlCaseVo);
        PageResult<QlCase> pageResult = new PageResult<>(dbDbQlCases.getPageNum(), dbDbQlCases.getPageSize(), dbDbQlCases.getTotal());
        List<QlCase> qlCaseList = dbDbQlCases.stream().map(dbQlCase -> {
            QlCase qlcase = new QlCase();
            BeanUtils.copyProperties(dbQlCase, qlcase);
            return qlcase;
        }).collect(Collectors.toList());
        pageResult.setData(qlCaseList);
        return pageResult;
    }

    /**
     * 根据办件编号查询退件信息
     *
     * @param doneCaseVo
     * @return
     */
    //@Cacheable(key = "'getOneRetuenCaseByCaseNumber:doneCaseVo=' + #doneCaseVo", unless = "#result == null")
    public QlCase getOneRetuenCaseByCaseNumber(QlCaseVo doneCaseVo) {
        if (StringUtil.isEmpty(doneCaseVo.getCaseNumber())) {
            throw new ResultInfoException("办件编号为空！");
        }
        DbQlCaseVo dbQlCaseVo = new DbQlCaseVo();
        BeanUtils.copyProperties(doneCaseVo, dbQlCaseVo);
        DbQlCase dbQlCase = qlCaseMapper.getOneRetuenCaseByCaseNumber(dbQlCaseVo);
        QlCase qlCase = new QlCase();
        if (dbQlCase != null) {
            DbQlCaseApplay dbQlCaseApplay = qlCaseApplayMapper.queryQlCaseApplayByCaseOid(dbQlCase.getCaseOid());
            QlCaseApplay applay = new QlCaseApplay();
            if (null != dbQlCaseApplay) {
                BeanUtils.copyProperties(dbQlCaseApplay, applay);
            }
            BeanUtils.copyProperties(dbQlCase, qlCase);
            qlCase.setApplay(applay);
        }
        return qlCase;
    }

    //@Cacheable(key = "'getOverDueAllCase'", unless = "#result == null")
    public List<QlCase> getOverDueAllCase() {
        QlCaseExample qlCaseExample = new QlCaseExample();
        QlCaseExample.Criteria criteria = qlCaseExample.createCriteria();
        criteria.andDelFlagEqualTo(BaseStaticParameter.N);
        criteria.andRqbzDueDateIsNotNull();
        criteria.andRqbzDueDateLessThanOrEqualTo(new Date());
        criteria.andCaseStatusEqualTo(2);//办理中的
        criteria.andPackageCaseOidIsNull();
        List<DbQlCase> qlcaseList = this.qlCaseMapper.selectByExample(qlCaseExample);
        List<QlCase> list = new ArrayList<>();
        if (qlcaseList != null && qlcaseList.size() > 0) {
            for (DbQlCase qlCase : qlcaseList) {
                QlCase qlcase = new QlCase();
                BeanUtils.copyProperties(qlCase, qlcase);
                list.add(qlcase);
            }
        }
        return list;
    }

    //@Cacheable(key = "'getQlCaseByOid:caseOid=' + #packageCaseOid+',serviceOid=' + #serviceOid+'", unless = "#result == null")
    public List<QlCase> getQlCaseByOid(String packageCaseOid, String serviceOid) {
        List<DbQlCase> dbDbQlCases = qlCaseMapper.getQlCaseByOid(packageCaseOid, serviceOid);
        List<QlCase> qlCaseList = dbDbQlCases.stream().map(dbQlCase -> {
            QlCase qlcase = new QlCase();
            if (dbQlCase != null) {
                BeanUtils.copyProperties(dbQlCase, qlcase);
            }
            return qlcase;
        }).collect(Collectors.toList());
        return qlCaseList;
    }

    //@Cacheable(key = "'queryQlCaseFinishListByCaseOid:caseOid=' + #caseOid", unless = "#result == null")
    public List<QlCase> queryQlCaseFinishListByCaseOid(String caseOid) {
        DbQlCase dbQlCase1 = new DbQlCase();
        dbQlCase1.setPackageCaseOid(caseOid);
        dbQlCase1.setCaseStatus(3);
        dbQlCase1.setDelFlag(0);
        List<DbQlCase> dbDbQlCases = qlCaseMapper.selectQlCaseList(dbQlCase1);
        List<QlCase> qlCaseList = dbDbQlCases.stream().map(dbQlCase -> {
            QlCase qlcase = new QlCase();
            if (dbQlCase != null) {
                BeanUtils.copyProperties(dbQlCase, qlcase);
            }
            return qlcase;
        }).collect(Collectors.toList());
        return qlCaseList;
    }


    public List<String> queryListUserOidByLoginUser(String userOid) {

        if (StringUtils.isEmpty(userOid)) {
            return Collections.emptyList();
        }
        ApiResultSet<SysUser> api = sysUserFeginService.getSysUserByUserOid(userOid);

        if (api == null || api.getCode() != ApiResultSet.SUCCESS || api.getData() == null || StringUtils.isEmpty(api.getData().getUserOid())) {
            return Collections.emptyList();
        }
        SysUser loginUser = api.getData();


        // 根据登录人的权限来判断 数据权限（1全部 2本人 3本部门 4本区划）
        Integer dataAuthority = loginUser.getDataAuthority();
        List<String> listUserOid = new ArrayList<>();
        if (dataAuthority != null) {
            if (dataAuthority == 2) {
                listUserOid.add(loginUser.getUserOid());
            } else if (dataAuthority == 3) {
                List<String> list = queryListUserOidByOrgOid(loginUser.getOrganOid());
                listUserOid.addAll(list);
            } else if (dataAuthority == 4) {
                List<String> listOrganOid = sysOrganFeginService.queryOrganOidByDistrictOid(loginUser.getDistrictOid());
                if (listOrganOid != null && !listOrganOid.isEmpty()) {
                    listOrganOid.forEach(orgOid -> {
                        List<String> list = queryListUserOidByOrgOid(orgOid);
                        listUserOid.addAll(list);
                    });
                }
            }
        }

        if (listUserOid == null || listUserOid.isEmpty()) {
            if (dataAuthority != 1) {
                listUserOid.add("-1");
            }
        }

        return listUserOid;
    }


    /**
     * @param orgOid
     * @return List<String>
     * @Title: queryListUserOidByOrgOid
     * @Description: 根据机构oid查询用户的userOid集合数据
     */
    private List<String> queryListUserOidByOrgOid(String orgOid) {
        List<String> listUserOid = new ArrayList<>();
        if (StringUtils.isNotEmpty(orgOid)) {
            List<String> list = sysUserFeginService.queryUserOidsByOrganOid(orgOid);
            if (list != null && !list.isEmpty()) {
                listUserOid.addAll(list);
            }
        }

        return listUserOid;
    }

    public List<QlCase> queryQlCaseListByPackageCaseOid(QlCaseVo qlCaseVo) {
        List<DbQlCase> dbDbQlCases = qlCaseMapper.getQlCaseByPackageCaseOid(qlCaseVo.getCaseOid());
        List<QlCase> qlCaseList = dbDbQlCases.stream().map(dbQlCase -> {
            QlCase qlcase = new QlCase();
            if (dbQlCase != null) {
                if (dbQlCase.getAcceptanceDate() != null) {
                    Date addDate = null;
                    ApiResultSet<SxServiceExtend> resultSet = sxServiceExtendFeginService.getSxServiceExtendByServiceOid(dbQlCase.getServiceOid());
                    SxServiceExtend extend = resultSet.getData();
                    if (null != extend.getPromiseLimitType() && !extend.getPromiseLimitType().equals("")) {
                        Integer limit = extend.getPromiseLimit().intValue();
                        if (extend.getPromiseLimitType().equals("H")) {
                            addDate = LimitDateCalc.dateCalc(dbQlCase.getAcceptanceDate(), 0, extend.getPromiseLimitType(), limit);
                        } else {
                            //应办结时间
                            addDate = DateUtil.getExpireDate(dbQlCase.getAcceptanceDate(), Integer.parseInt(extend.getPromiseLimit().toString()), qlCaseVo.getHolidays());
                        }
                    }
                    if (addDate != null) {
                        dbQlCase.setShouldConcludeDate(addDate);
                    }
                }
                BeanUtils.copyProperties(dbQlCase, qlcase);
            }
            return qlcase;
        }).collect(Collectors.toList());
        return qlCaseList;
    }

    public List<QlCase> getFzOnthingCase(String packageCaseOid) {
        List<DbQlCase> list = qlCaseMapper.getFzOnthingCase(packageCaseOid);
        if (list != null && list.size() > 0) {
            List<QlCase> qlCaseList = list.stream().map(dbQlCase -> {
                QlCase qlcase = new QlCase();
                BeanUtils.copyProperties(dbQlCase, qlcase);
                return qlcase;
            }).collect(Collectors.toList());
            return qlCaseList;
        }
        return null;
    }

    public PageResult<QlCase> queryQlCaseApplayByCredentialNumber(String credentialNumber, String applyUserType, String projectName, String caseNumber, Integer pageNum, Integer pageSize) {
        if (StringUtil.isEmpty(credentialNumber)) {
            throw new ResultInfoException("申请人证件号为空！");
        }
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        Map map = new HashMap();
        map.put("credentialNumber", credentialNumber);
        map.put("applyUserType", applyUserType);
        map.put("projectName", projectName);
        map.put("serviceName", caseNumber);
        PageHelper.startPage(pageNum, pageSize);
        Page<DbQlCase> dbDbQlCases = (Page<DbQlCase>) qlCaseMapper.queryCaseByZjh(map);
        PageResult<QlCase> pageResult = new PageResult<>(dbDbQlCases.getPageNum(), dbDbQlCases.getPageSize(), dbDbQlCases.getTotal());
        List<QlCase> qlCaseList = dbDbQlCases.stream().map(dbQlCase -> {
            QlCase qlcase = new QlCase();
            BeanUtils.copyProperties(dbQlCase, qlcase);
            return qlcase;
        }).collect(Collectors.toList());
        pageResult.setData(qlCaseList);
        return pageResult;
    }

    public ApiResultSet<Map<String, Integer>> getCaseTjInfo(String userOid) {
        Map<String, Integer> res = new HashMap<>();
        Map map = new HashMap();
        //当前登录人受理总量
        //CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        map.put("userOid", userOid);
        //Integer slCount= qlCaseMapper.countCase(map);
        //个人量
        map.put("applyUserType", 2);
        Integer grCount = qlCaseMapper.countCase(map);
        //法人量
        map.put("applyUserType", 1);
        Integer frCount = qlCaseMapper.countCase(map);

        res.put("slCount", grCount + frCount);
        res.put("grCount", grCount);
        res.put("frCount", frCount);
        return new ApiResultSet(res);
    }

    public PageResult<QlCase> taskYsCase(List<String> list, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        if (list == null || list.size() == 0) {
            return new PageResult<>(0, 0, 0);
        }
        Page<DbQlCase> dbDbQlCases = (Page<DbQlCase>) qlCaseMapper.taskYsCase(list);
        PageResult<QlCase> pageResult = new PageResult<>(dbDbQlCases.getPageNum(), dbDbQlCases.getPageSize(), dbDbQlCases.getTotal());
        List<QlCase> qlCaseList = dbDbQlCases.stream().map(dbQlCase -> {
            QlCase qlcase = new QlCase();
            BeanUtils.copyProperties(dbQlCase, qlcase);
            return qlcase;
        }).collect(Collectors.toList());
        pageResult.setData(qlCaseList);
        return pageResult;

       /*List<QlCase> qlList= caseList.stream().map(qlCase->{
           QlCase qlcaseNew=new QlCase();
           BeanUtils.copyProperties(qlCase,qlcaseNew);
           return qlcaseNew;
       }).collect(Collectors.toList());
	    return qlList;*/
    }

    public PageResult<QlCase> taskRqhbCasePage(List<String> list, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        if (list == null || list.size() == 0) {
            return new PageResult<>(0, 0, 0);
        }
        Page<DbQlCase> dbDbQlCases = (Page<DbQlCase>) qlCaseMapper.taskRqhbCaseList(list);
        PageResult<QlCase> pageResult = new PageResult<>(dbDbQlCases.getPageNum(), dbDbQlCases.getPageSize(), dbDbQlCases.getTotal());
        List<QlCase> qlCaseList = dbDbQlCases.stream().map(dbQlCase -> {
            QlCase qlcase = new QlCase();
            BeanUtils.copyProperties(dbQlCase, qlcase);
            return qlcase;
        }).collect(Collectors.toList());
        pageResult.setData(qlCaseList);
        return pageResult;
        /*List<QlCase> qlList= caseList.stream().map(qlCase->{
            QlCase qlcaseNew=new QlCase();
            BeanUtils.copyProperties(qlCase,qlcaseNew);
            return qlcaseNew;
        }).collect(Collectors.toList());
        return qlList;*/
    }

    public List<QlCase> taskRqhbCaseList(List<String> list) {

        if (list == null || list.size() == 0) {
            return new ArrayList<QlCase>();
        }
        List<DbQlCase> caseList = qlCaseMapper.taskRqhbCaseList(list);
        List<QlCase> qlList = caseList.stream().map(qlCase -> {
            QlCase qlcaseNew = new QlCase();
            BeanUtils.copyProperties(qlCase, qlcaseNew);
            return qlcaseNew;
        }).collect(Collectors.toList());
        return qlList;
    }

    public Map<String, Object> updateQlCaseFormInfo(QlCase qlCase) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (qlCase != null && qlCase.getCaseOid() != null) {
            DbQlCase aCase = qlCaseMapper.queryQlCaseByCaseOid(qlCase.getCaseOid());
            aCase.setFormOids(qlCase.getFormOids());
            int index = qlCaseMapper.update(aCase);
            if (index > 0) {
                map.put("code", 200);
                map.put("success", true);
            } else {
                map.put("code", 200);
                map.put("success", false);
            }
        }
        return map;
    }

    public QlCase queryQlCaseInfo(String queueNum, String date) {


        return null;
    }



}
