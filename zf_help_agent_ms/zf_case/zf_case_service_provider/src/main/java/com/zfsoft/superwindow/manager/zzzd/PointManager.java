package com.zfsoft.superwindow.manager.zzzd;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.feign.SysDistrictFeignService;
import com.zfsoft.cases.manager.EvictSettingManager;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.superwindow.data.zzzd.PointInfo;
import com.zfsoft.superwindow.dbaccess.dao.DbPointInfoMapper;
import com.zfsoft.superwindow.dbaccess.data.DbPointInfo;
import com.zfsoft.superwindow.dbaccess.data.DbPointInfoExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author ChangSheng
 * @Date 9:48 2022/5/28
 * @Description 点位信息操作service
 **/
@Service
@Slf4j
public class PointManager {

    @Resource
    private EvictSettingManager evictSettingManager;

    @Resource
    private DbPointInfoMapper dbPointInfoMapper;

    @Resource
    private SysDistrictFeignService sysDistrictFeginService;

    /**
     * 分页查询点位信息
     */
    public PageResult<PointInfo> queryPointInfoWithPage(PointInfo PointInfo, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbPointInfoExample dbPointInfoExample = new DbPointInfoExample();
        dbPointInfoExample.setOrderByClause(" CREATE_DATE DESC ");
        DbPointInfoExample.Criteria criteria = dbPointInfoExample.createCriteria();
        if(null!=PointInfo){
            //点位名称
            if(StrUtil.isNotEmpty(PointInfo.getName())){
                criteria.andNameLike("%"+PointInfo.getName().trim()+"%");
            }
            //所属区划
            if(StrUtil.isNotEmpty(PointInfo.getDistrictOid())){
                criteria.andDistrictOidEqualTo(PointInfo.getDistrictOid());
            }
        }
        criteria.andDeleteStatusEqualTo(BaseStaticParameter.NO);
        Page<DbPointInfo> dbPointInfos = (Page<DbPointInfo>)dbPointInfoMapper.selectByExample(dbPointInfoExample);
        PageResult<PointInfo> pageResult = new PageResult<>(dbPointInfos.getPageNum(),dbPointInfos.getPageSize(),dbPointInfos.getTotal());
        List<PointInfo> sysPointList = dbPointInfos.stream().map(dbPointInfo -> {
            PointInfo Point = new PointInfo();
            BeanUtils.copyProperties(dbPointInfo,Point);
            return Point;
        }).collect(Collectors.toList());
        //获取区划信息
        for(PointInfo temp:sysPointList){
            ApiResultSet<SysDistrict> data = sysDistrictFeginService.getSysDistrictByDistrictOid(temp.getDistrictOid().substring(temp.getDistrictOid().indexOf("-")+1));
            temp.setDistrictName(data.getData().getName());
        }
        pageResult.setData(sysPointList);
        return pageResult;
    }

    /**
     * 保存点位信息
     */
    @Transactional(rollbackFor=Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public int savePointInfo(PointInfo PointInfo) {
        DbPointInfo dbPointInfo = new DbPointInfo();
        BeanUtils.copyProperties(PointInfo,dbPointInfo);
        int index=0;
        if (null == PointInfo.getId()) {
            dbPointInfo.setCreateDate(new Date());
            dbPointInfo.setDeleteStatus("N");
            index = dbPointInfoMapper.insert(dbPointInfo);
        }else {
            index = dbPointInfoMapper.updateByPrimaryKeySelective(dbPointInfo);
        }
        PointInfo.setId(dbPointInfo.getId());
        //清除缓存
        evictSettingManager.evictpbpjManage(dbPointInfo.getId());
        return index;
    }

    /**
     * 根据id删除点位嘻嘻
     */
    @Transactional(rollbackFor=Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public int deletePointInfoById(String id) {
        DbPointInfo dbPointInfo = dbPointInfoMapper.selectByPrimaryKey(Long.valueOf(id));
        dbPointInfo.setDeleteStatus("Y");
        int index = dbPointInfoMapper.updateByPrimaryKeySelective(dbPointInfo);
        //清除缓存
        evictSettingManager.evictpbpjManage(dbPointInfo.getId());
        return index;
    }

    /**
     * 根据id查询点位信息
     */
    @Transactional(rollbackFor=Exception.class)
    public PointInfo getPointInfoById(String id) {
        DbPointInfo dbPointInfo = dbPointInfoMapper.selectByPrimaryKey(Long.valueOf(id));
        if(dbPointInfo == null) {
            throw new ResultInfoException("点位信息为空！");
        }
        PointInfo PointInfo = new PointInfo();
        BeanUtils.copyProperties(dbPointInfo,PointInfo);
        ApiResultSet<SysDistrict> data = sysDistrictFeginService.getSysDistrictByDistrictOid(PointInfo.getDistrictOid().substring(PointInfo.getDistrictOid().indexOf("-")+1));
        PointInfo.setDistrictName(data.getData().getName());
        return PointInfo;
    }

    /**
     * 查询所有点位信息
     */
    public List<PointInfo> getAllPointInfo(){
        DbPointInfoExample dbPointInfoExample = new DbPointInfoExample();
        dbPointInfoExample.setOrderByClause(" CREATE_DATE DESC ");
        DbPointInfoExample.Criteria criteria = dbPointInfoExample.createCriteria();
        criteria.andDeleteStatusEqualTo("N");
        List<DbPointInfo> list = dbPointInfoMapper.selectByExample(dbPointInfoExample);
        List<PointInfo> sysPointList = list.stream().map(dbPointInfo -> {
            PointInfo Point = new PointInfo();
            BeanUtils.copyProperties(dbPointInfo,Point);
            return Point;
        }).collect(Collectors.toList());
        return sysPointList;
    }
}
