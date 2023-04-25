package com.zfsoft.ha.managers;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.ha.data.HaBanner;
import com.zfsoft.ha.data.responseData.HaBannerResponseData;
import com.zfsoft.ha.dbaccess.dao.DbHaBannerMapper;
import com.zfsoft.ha.dbaccess.data.DbHaBanner;
import com.zfsoft.ha.dbaccess.data.DbHaWorkUser;
import com.zfsoft.ha.dbaccess.data.example.DbHaBannerExample;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description //banner表实现
 * @Author: Wangyh
 * @Date: 2022年7月26日14:58:12
 */
@Service
@Slf4j
public class HaBannerServiceManager {
    /**
     * banner数据DB层接口
     */
    @Resource
    private DbHaBannerMapper dbHaBannerMapper;

    /**
     * 查询banner列表
     *
     * @return List<HaBannerResponseData> 获取banner列表详情
     * @author: wangyh
     * @Date: 2022年7月26日14:04:40
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaBannerResponseData> queryBannerServiceList() throws ServiceException {
        DbHaBannerExample dbHaBannerExample = new DbHaBannerExample();
        DbHaBannerExample.Criteria criteria = dbHaBannerExample.createCriteria();
        criteria.andDeleteStatusEqualTo("1");
        dbHaBannerExample.setOrderByClause("CREATE_DATE desc");
        List<DbHaBanner> dbHaBanners = dbHaBannerMapper.selectByExample(dbHaBannerExample);
        //循环遍历,将DbthaBanner对象复制到ThaBanner对象
        List<HaBannerResponseData> haBannerList = dbHaBanners.stream().map(dbBannerServic -> {
            HaBannerResponseData haBannerResponseData = new HaBannerResponseData();
            BeanUtils.copyProperties(dbBannerServic, haBannerResponseData);
            return haBannerResponseData;
        }).collect(Collectors.toList());
        return haBannerList;
    }

    /**
     * 查询banner分页信息列表
     *
     * @param haBanner
     * @param pageNumber
     * @param pageSize
     * @return PageResult<HaBanner>  获取查询banner分页信息列表详细信息
     * @author: wangyh
     * @Date: 2022年7月26日14:04:40
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public PageResult<HaBanner> queryBannerServicePage(HaBanner haBanner, Integer pageNumber, Integer pageSize) throws ServiceException {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber, pageSize);
        DbHaBannerExample dbHaBannerExample = new DbHaBannerExample();
        DbHaBannerExample.Criteria criteria = dbHaBannerExample.createCriteria();
        if (null != haBanner) {
            if (StrUtil.isNotEmpty(haBanner.getTitle())) {
                criteria.andTitleLike("%" + haBanner.getTitle() + "%");
            }
            if (StrUtil.isNotEmpty(haBanner.getContent())) {
                criteria.andContentLike(haBanner.getContent());
            }
            if (StrUtil.isNotEmpty(haBanner.getAbleStatus())) {
                criteria.andAbleStatusEqualTo(haBanner.getAbleStatus());
            }
            if (StrUtil.isNotEmpty(haBanner.getDeleteStatus())) {
                criteria.andDeleteStatusEqualTo(haBanner.getDeleteStatus());
            } else {
                criteria.andDeleteStatusEqualTo("1");
            }
            dbHaBannerExample.setOrderByClause("CREATE_DATE desc");
        } else {
            criteria.andDeleteStatusEqualTo("1");
        }
        Page<DbHaBanner> dbHaBanners = (Page<DbHaBanner>) dbHaBannerMapper.selectByExample(dbHaBannerExample);
        PageResult<HaBanner> pageResult = new PageResult<>(dbHaBanners.getPageNum(), dbHaBanners.getPageSize(), dbHaBanners.getTotal());
        //循环遍历,将DbthaBanner对象复制到ThaBanner对象
        List<HaBanner> haBannerList = dbHaBanners.stream().map(dbBannerServic -> {
            HaBanner haBanner1 = new HaBanner();
            BeanUtils.copyProperties(dbBannerServic, haBanner1);
            //判断根据字段类别的含义做返回对应中文
            if (StrUtil.isNotEmpty(haBanner1.getAbleStatus()) && haBanner1.getAbleStatus().equals("1")) {
                haBanner1.setAbleStatus("启用");
            } else {
                haBanner1.setAbleStatus("禁用");
            }
            if (StrUtil.isNotEmpty(haBanner1.getDeleteStatus()) && haBanner1.getDeleteStatus().equals("1")) {
                haBanner1.setDeleteStatus("未删除");
            } else {
                haBanner1.setDeleteStatus("已删除");
            }
            return haBanner1;
        }).collect(Collectors.toList());
        pageResult.setData(haBannerList);
        return pageResult;
    }


    /**
     * @param id 主键
     * @description: 删除banner信息
     * @author: wangyh
     * @Date: 2022年7月26日14:08:02
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public Integer deleteBannerid(Long id) throws ServiceException {
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        DbHaBanner dbHaBanner = dbHaBannerMapper.queryById(id);
        dbHaBanner.setDeleteStatus("2");
        dbHaBanner.setCreateBy(loginUser.getUserName());
        dbHaBanner.setUpdateDate(new Date());
        DbHaBannerExample dbHaBannerExample = new DbHaBannerExample();
        DbHaBannerExample.Criteria criteria = dbHaBannerExample.createCriteria();
        criteria.andIdEqualTo(id);
        int index = dbHaBannerMapper.updateByExampleSelective(dbHaBanner, dbHaBannerExample);
        return index;
    }

    /**
     * @param ids 主键
     * @return
     * @description: 批量删除用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public void batchBannerid(List<Long> ids) throws ServiceException {
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        for (Long Bannerid : ids) {
            DbHaBanner dbHaBanner = dbHaBannerMapper.queryById(Bannerid);
            if (dbHaBanner != null) {
                dbHaBanner.setDeleteStatus("2");//1-未删除，2-已删除
                dbHaBanner.setCreateBy(loginUser.getUserName());
                dbHaBanner.setUpdateDate(new Date());
                DbHaBannerExample dbHaBannerExample = new DbHaBannerExample();
                DbHaBannerExample.Criteria criteria = dbHaBannerExample.createCriteria();
                criteria.andIdEqualTo(Bannerid);
                dbHaBannerMapper.updateByExampleSelective(dbHaBanner, dbHaBannerExample);
            }
        }

    }

    /**
     * @param haBanner 参数配置实体类
     * @description: 参数配置的新增或者修改
     * @author: wangyh
     * @Date: 2022年7月26日14:43:41
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public Map<String, Object> saveOrUpdateThaWorkUser(HaBanner haBanner) throws ServiceException {
        Map<String, Object> map = new HashMap();
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        Date day = new Date();
        int index = 0;
        //判断是修改还是新增
        if (haBanner.getId() != null) {
            //修改
            DbHaBanner dbHaBanner = dbHaBannerMapper.queryById(haBanner.getId());
            BeanUtils.copyProperties(haBanner, dbHaBanner);
            dbHaBanner.setUpdateBy(loginUser.getUserName());
            dbHaBanner.setUpdateDate(day); //获取当前时间
            DbHaBannerExample dbHaBannerExample = new DbHaBannerExample();
            DbHaBannerExample.Criteria criteria = dbHaBannerExample.createCriteria();
            criteria.andIdEqualTo(haBanner.getId());
            int i = dbHaBannerMapper.updateByExampleSelective(dbHaBanner, dbHaBannerExample);
            map.put("index", index);
        } else {
            //新增
            DbHaBanner dbHaBanner = new DbHaBanner();
            BeanUtils.copyProperties(haBanner, dbHaBanner);
            dbHaBanner.setDeleteStatus("1");
            dbHaBanner.setAbleStatus("1"); //默认启用
            dbHaBanner.setCreateBy(loginUser.getUserName());
            dbHaBanner.setCreateDate(new Date());
            index = dbHaBannerMapper.insert(dbHaBanner);
            map.put("index", index);
        }
        return map;
    }


    /**
     * @param id 主键
     * @description: 根据id查询banner表信息信息
     * @author: wangyh
     * @Date: 2022年7月26日14:08:02
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public HaBanner selectByid(Long id) throws ServiceException {
        HaBanner haBanner = null;
        DbHaBanner dbHaBanner = dbHaBannerMapper.queryById(id);
        if (dbHaBanner != null) {
            haBanner = new HaBanner();
            BeanUtils.copyProperties(dbHaBanner, haBanner);
        }
        return haBanner;
    }
}
