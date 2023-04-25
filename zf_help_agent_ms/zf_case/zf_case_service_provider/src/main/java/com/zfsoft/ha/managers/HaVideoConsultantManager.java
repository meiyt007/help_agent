package com.zfsoft.ha.managers;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.feign.SysOrganFeginService;
import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.ha.data.vo.VideoConsultant;
import com.zfsoft.ha.dbaccess.dao.DbVideoConsultantMapper;
import com.zfsoft.ha.dbaccess.data.DbHaWorkUser;
import com.zfsoft.ha.dbaccess.data.DbVideoConsultant;
import com.zfsoft.ha.dbaccess.data.example.DbVideoConsultantExample;
//import com.zfsoft.microservice.platform.manager.sys.SysOrganManager;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import com.zfsoft.superwindow.util.CommonUtil;
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
 * @Description //视频咨询实现层
 * @Author: Wangyh
 * @Date: 2023/1/6 14:23
 */
@Service
@Slf4j
public class HaVideoConsultantManager {
    @Resource
    private SysOrganFeginService sysOrganFeignService;
    @Resource
    private DbVideoConsultantMapper dbVideoConsultantMapper;


    /**
     * @param id 用户主键
     * @description: 重置密码
     * @author: wangyh
     * @Date: 2022/7/15
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public VideoConsultant ResetPassword(Long id) throws ServiceException {
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        DbVideoConsultant dbVideoConsultant = dbVideoConsultantMapper.selectByPrimaryKey(id);
        String pass = "123456";//重置密码,初始化密码
        String passwrod = CommonUtil.md5(dbVideoConsultant.getAccount() + pass + dbVideoConsultant.getSalt());
        dbVideoConsultant.setPassword(passwrod);
        dbVideoConsultant.setUpdateDate(new Date());
        dbVideoConsultantMapper.updateByPrimaryKeySelective(dbVideoConsultant);
        VideoConsultant videoConsultant = new VideoConsultant();
        BeanUtils.copyProperties(dbVideoConsultant, videoConsultant);
        return videoConsultant;
    }
    /**
     * @param videoConsultant
     * @param pageNumber
     * @param pageSize
     * @return
     * @description: 查询视频用户分页信息列表
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public PageResult<VideoConsultant> queryVideoPage(VideoConsultant videoConsultant, Integer pageNumber, Integer pageSize) throws ServiceException {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber, pageSize);
        DbVideoConsultantExample videoConsultantExample = new DbVideoConsultantExample();
        DbVideoConsultantExample.Criteria criteria = videoConsultantExample.createCriteria();
        if (null != videoConsultant) {
            if (StrUtil.isNotEmpty(videoConsultant.getName())) {
                criteria.andNameLike("%" + videoConsultant.getName() + "%");
            }
            if (StrUtil.isNotEmpty(videoConsultant.getAccount())) {
                criteria.andAccountEqualTo(videoConsultant.getAccount());
            }
            if (StrUtil.isNotEmpty(videoConsultant.getPhone())) {
                criteria.andPhoneEqualTo(videoConsultant.getPhone());
            }
            if (StrUtil.isNotEmpty(videoConsultant.getOrganOid())) {
                criteria.andOrganOidEqualTo(videoConsultant.getOrganOid());
            }
            if (StrUtil.isNotEmpty(videoConsultant.getDeleteStatus())) {
                criteria.andUserTypeEqualTo(videoConsultant.getDeleteStatus());
            } else {
                criteria.andDeleteStatusEqualTo("1"); //1-未删除，2-已删除
            }
        } else {
            criteria.andDeleteStatusEqualTo("1"); //1-未删除，2-已删除
        }
        videoConsultantExample.setOrderByClause("CREATE_DATE desc");
        Page<DbVideoConsultant> dbVideoConsultants = (Page<DbVideoConsultant>) dbVideoConsultantMapper.selectByExample(videoConsultantExample);
        PageResult<VideoConsultant> pageResult = new PageResult<>(dbVideoConsultants.getPageNum(), dbVideoConsultants.getPageSize(), dbVideoConsultants.getTotal());
        List<VideoConsultant> videoConsultantList = dbVideoConsultants.stream().map(DbvideoService -> {
            VideoConsultant videoConsultant1 = new VideoConsultant();
            BeanUtils.copyProperties(DbvideoService, videoConsultant1);
            //实施部门名称
            if(StrUtil.isNotEmpty(DbvideoService.getOrganOid())){
                SysOrgan sysOrgan = sysOrganFeignService.getSysOrganByOrganOid(DbvideoService.getOrganOid()).getData();
                if(null != sysOrgan){
                    videoConsultant1.setOrganOid(sysOrgan.getName());
                }
            }
            return videoConsultant1;
        }).collect(Collectors.toList());
        pageResult.setData(videoConsultantList);
        return pageResult;
    }

    /**
     * @param id
     * @return
     * @description: 删除视频咨询用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public VideoConsultant deleteVideoByid(Long id) throws ServiceException {
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        DbVideoConsultant dbVideoConsultant = dbVideoConsultantMapper.selectByPrimaryKey(id);
        dbVideoConsultant.setDeleteStatus("2"); //1-未删除，2-已删除
        dbVideoConsultant.setUpdateDate(new Date());
        dbVideoConsultantMapper.updateByPrimaryKeySelective(dbVideoConsultant);
        VideoConsultant videoConsultant = new VideoConsultant();
        BeanUtils.copyProperties(dbVideoConsultant, videoConsultant);
        return videoConsultant;
    }

    /**
     * @param videoConsultant 参数配置实体类
     * @return Map<String, Object> 获取修改或保存详细状态
     * @description: 参数配置的新增或者修改
     */
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public Map<String, Object> saveOrUpdateVideoConsultant(VideoConsultant videoConsultant) throws Exception {
        Map map = new HashMap();
        int index = 0;
        if (videoConsultant.getId() != null) {
            //修改
            DbVideoConsultant dbVideoConsultant = dbVideoConsultantMapper.selectByPrimaryKey(videoConsultant.getId());
            BeanUtils.copyProperties(videoConsultant, dbVideoConsultant);
            dbVideoConsultant.setUpdateDate(new Date());
            index = dbVideoConsultantMapper.updateByPrimaryKeySelective(dbVideoConsultant);
            map.put("index", index);
            map.put("type", "1");//type 1代表修改
        } else {
            //新增
            DbVideoConsultant dbVideoConsultant = dbVideoConsultantMapper.queryloginByAccountAndId(videoConsultant.getAccount());//根据账号查询是否有同一用户信息
            DbVideoConsultant roomNumber = dbVideoConsultantMapper.queryRoomNumber(videoConsultant.getRoomNumber());
            if (dbVideoConsultant == null && roomNumber ==null) {
                DbVideoConsultant dbVideoConsultant1 = new DbVideoConsultant();
                // - 生成随机盐值 ,新增之后不可改变
                String salt = UUID.randomUUID().toString().toUpperCase();
                String passwrod = CommonUtil.md5(videoConsultant.getAccount() + videoConsultant.getPassword() + salt);
                videoConsultant.setPassword(passwrod);
                videoConsultant.setSalt(salt);
                videoConsultant.setCreateDate(new Date());
                videoConsultant.setDeleteStatus("1");//1-未删除，2-已删除
                BeanUtils.copyProperties(videoConsultant, dbVideoConsultant1);
                index = dbVideoConsultantMapper.insert(dbVideoConsultant1);
                map.put("index", index);
                map.put("type", "2");//type 1代表新增
            }
        }
        return map;
    }

    /**
     * @param id 主键
     * @description: 根据id查询User表信息信息
     * @author: wangyh
     * @Date: 2023年1月6日14:08:02
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public VideoConsultant selectByid(Long id) throws ServiceException {
        VideoConsultant videoConsultant = null;
        DbVideoConsultant dbVideoConsultant = dbVideoConsultantMapper.selectByPrimaryKey(id);
        if (dbVideoConsultant != null) {
            videoConsultant = new VideoConsultant();
            BeanUtils.copyProperties(dbVideoConsultant, videoConsultant);
        }
        return videoConsultant;
    }
}
