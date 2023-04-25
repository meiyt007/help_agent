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
import com.zfsoft.superwindow.data.zzzd.TerminalInfo;
import com.zfsoft.superwindow.dbaccess.dao.DbTerminalInfoMapper;
import com.zfsoft.superwindow.dbaccess.data.DbTerminalInfo;
import com.zfsoft.superwindow.dbaccess.data.DbTerminalInfoExample;
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
 * @Date 9:47 2022/5/26
 * @Description 这个才是service层
 **/
@Service
@Slf4j
public class TerminalManager {

    @Resource
    private DbTerminalInfoMapper dbTerminalInfoMapper;

    @Resource
    private EvictSettingManager evictSettingManager;

    @Resource
    private SysDistrictFeignService sysDistrictFeginService;

    /**
     * 查询自助终端分页数据
     */
    public PageResult<TerminalInfo> queryTerminalInfoWithPage(TerminalInfo terminalInfo, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbTerminalInfoExample dbTerminalInfoExample = new DbTerminalInfoExample();
        dbTerminalInfoExample.setOrderByClause(" CREATE_DATE DESC ");
        DbTerminalInfoExample.Criteria criteria = dbTerminalInfoExample.createCriteria();
        if(null!=terminalInfo){
            //终端编号
            if(StrUtil.isNotEmpty(terminalInfo.getTerminalCode())){
                criteria.andTerminalCodeLike("%"+terminalInfo.getTerminalCode().trim()+"%");
            }
            //所属区划
            if(StrUtil.isNotEmpty(terminalInfo.getDistrictOid())){
                criteria.andDistrictOidEqualTo(terminalInfo.getDistrictOid());
            }
            //所属点位
            if(StrUtil.isNotEmpty(terminalInfo.getPointOid())){
                criteria.andPointOidEqualTo(terminalInfo.getPointOid());
            }
        }
        criteria.andDeleteStatusEqualTo(BaseStaticParameter.NO);
        Page<DbTerminalInfo> dbTerminalInfos = (Page<DbTerminalInfo>)dbTerminalInfoMapper.selectByExample(dbTerminalInfoExample);
        PageResult<TerminalInfo> pageResult = new PageResult<>(dbTerminalInfos.getPageNum(),dbTerminalInfos.getPageSize(),dbTerminalInfos.getTotal());
        List<TerminalInfo> sysTerminalList = dbTerminalInfos.stream().map(dbTerminalInfo -> {
            TerminalInfo terminal = new TerminalInfo();
            BeanUtils.copyProperties(dbTerminalInfo,terminal);
            return terminal;
        }).collect(Collectors.toList());
        //获取区划信息
        for(TerminalInfo temp:sysTerminalList){
            ApiResultSet<SysDistrict> data = sysDistrictFeginService.getSysDistrictByDistrictOid(temp.getDistrictOid().substring(temp.getDistrictOid().indexOf("-")+1));
            if(null != data.getData()) {
                temp.setDistrictName(data.getData().getName());
            }
        }
        pageResult.setData(sysTerminalList);
        return pageResult;
    }

    /**
     * 保存自助终端数据
     */
    @Transactional(rollbackFor=Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public int saveTerminalInfo(TerminalInfo terminalInfo) {
        DbTerminalInfo dbTerminalInfo = new DbTerminalInfo();
        BeanUtils.copyProperties(terminalInfo,dbTerminalInfo);
        int index=0;
        if (null == terminalInfo.getId()) {
            dbTerminalInfo.setCreateDate(new Date());
            dbTerminalInfo.setDeleteStatus("N");
            index = dbTerminalInfoMapper.insert(dbTerminalInfo);
        }else {
            index = dbTerminalInfoMapper.updateByPrimaryKeySelective(dbTerminalInfo);
        }
        terminalInfo.setId(dbTerminalInfo.getId());
        //清除缓存
        evictSettingManager.evictpbpjManage(dbTerminalInfo.getId());
        return index;
    }

    /**
     * 根据id删除终端数据
     */
    @Transactional(rollbackFor=Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public int deleteTerminalInfoById(String id) {
        DbTerminalInfo dbTerminalInfo = dbTerminalInfoMapper.selectByPrimaryKey(Long.valueOf(id));
        dbTerminalInfo.setDeleteStatus("Y");
        int index = dbTerminalInfoMapper.updateByPrimaryKeySelective(dbTerminalInfo);
        //清除缓存
        evictSettingManager.evictpbpjManage(dbTerminalInfo.getId());
        return index;
    }

    /**
     * 根据id查询终端数据
     */
    @Transactional(rollbackFor=Exception.class)
    public TerminalInfo getTerminalInfoById(String id) {
        DbTerminalInfo dbTerminalInfo = dbTerminalInfoMapper.selectByPrimaryKey(Long.valueOf(id));
        if(dbTerminalInfo == null) {
            throw new ResultInfoException("终端信息为空！");
        }
        TerminalInfo terminalInfo = new TerminalInfo();
        BeanUtils.copyProperties(dbTerminalInfo,terminalInfo);
        ApiResultSet<SysDistrict> data = sysDistrictFeginService.getSysDistrictByDistrictOid(terminalInfo.getDistrictOid().substring(terminalInfo.getDistrictOid().indexOf("-")+1));
        terminalInfo.setDistrictName(data.getData().getName());
        return terminalInfo;
    }

    /**
     * @description 根据终端编号获取终端信息
     * @param terminalCode
     * @author wangyg
     * @date 2022/6/24
     * @return
     */
    public TerminalInfo getTerminalInfoByTerminalCode(String terminalCode) {
        DbTerminalInfoExample dbTerminalInfoExample = new DbTerminalInfoExample();
        DbTerminalInfoExample.Criteria criteria = dbTerminalInfoExample.createCriteria();
        criteria.andTerminalCodeEqualTo(terminalCode);
        List<DbTerminalInfo> terminalInfoList = dbTerminalInfoMapper.selectByExample(dbTerminalInfoExample);
        TerminalInfo terminalInfo = new TerminalInfo();
        BeanUtils.copyProperties(terminalInfoList.get(0),terminalInfo);
        return terminalInfo;
    }
}
