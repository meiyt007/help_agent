package com.zfsoft.single.manager.yxpz;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.platform.data.sys.SysAtta;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.single.data.yxpz.SxSerForm;
import com.zfsoft.single.dbaccess.dao.DbSxSerFormMapper;
import com.zfsoft.single.dbaccess.data.DbSxSerForm;
import com.zfsoft.single.dbaccess.data.DbSxSerFormExample;
import com.zfsoft.single.util.BeanUtils;
import com.zfsoft.single.util.StringUtils;
import com.zfsoft.single.util.UUIDUtil;
import com.zfsoft.superwindow.feign.settings.SysAttaFeginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangns
 * @description 表单配置 实现类
 * @date 2020/11/5 11:52
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Slf4j
@Service
public class FormConfigurationManager {

    @Resource
    private DbSxSerFormMapper dbSxSerFormMapper;
    @Resource
    private SysAttaFeginService sysAttaFeginService;

    /**
     * 根据serviceOid查询关联的表单列表
     * @param sxSerForm
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResult<SxSerForm> getSerFormsByServiceOid(SxSerForm sxSerForm, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSxSerFormExample dbSxSerFormExample = new DbSxSerFormExample();
        DbSxSerFormExample.Criteria criteria = dbSxSerFormExample.createCriteria();
        criteria.andDelFlagEqualTo(0);
        if(!StringUtils.isEmpty(sxSerForm.getComboDirectoryOid())){
            criteria.andComboDirectoryOidEqualTo(sxSerForm.getComboDirectoryOid());
            criteria.andServiceOidIsNull();
        }
        if(!StringUtils.isEmpty(sxSerForm.getServiceOid())){
            criteria.andServiceOidEqualTo(sxSerForm.getServiceOid());
            criteria.andComboDirectoryOidIsNull();
        }
        Page<DbSxSerForm> dbSxSerForms = (Page<DbSxSerForm>)dbSxSerFormMapper.selectByExample(dbSxSerFormExample);
        PageResult<SxSerForm> pageResult = new PageResult<>(dbSxSerForms.getPageNum(),dbSxSerForms.getPageSize(),dbSxSerForms.getTotal());
        List<SxSerForm> sxSerFormList = dbSxSerForms.stream().map(dbSerForm -> {
            SxSerForm serForm = new SxSerForm();
            BeanUtils.copyProperties(dbSerForm,serForm);
            return serForm;
        }).collect(Collectors.toList());
        //表单类型、表单状态数值转换
        for(SxSerForm serForm:sxSerFormList){
            serForm.setFormTypeName(serForm.getFormType() == 1?"电子表单":"定制化页面");
            serForm.setFormUseStatusName(serForm.getFormUseStatus() == 1?"启用":"禁用");
        }
        pageResult.setData(sxSerFormList);
        return pageResult;
    }


    /**
     * 查询单条数据详情
     * @param sxSerFormOid
     * @return
     */
    public SxSerForm getSxSerFormByOid(String sxSerFormOid) {
        DbSxSerForm dbSxSerForm = dbSxSerFormMapper.selectBySxSerFormOid(sxSerFormOid);
        SxSerForm sxSerForm = null;
        if (null == dbSxSerForm) {

        }else{
            sxSerForm = new SxSerForm();
            BeanUtils.copyProperties(dbSxSerForm,sxSerForm);
            if(StringUtils.isNotEmpty(sxSerForm.getSimpleAtta())){
               ApiResultSet<SysAtta> sysAtta= sysAttaFeginService.getSysAttaByAttaOid(sxSerForm.getSimpleAtta());
               if(sysAtta!=null && sysAtta.getData()!=null){
                   sxSerForm.setAttaName(sysAtta.getData().getName());
               }
            }
        }
        return sxSerForm;

    }

    /**
     * 表单启用/禁用
     * @param oid
     * @return
     */
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    public int ableSxSerFormById(String oid) {

        //查询表里状态
        DbSxSerForm sxSerForm = dbSxSerFormMapper.selectBySxSerFormOid(oid);

        if(ObjectUtils.isEmpty(sxSerForm)){
            return 0;
        }
        short useStatus = (short) (Short.valueOf(sxSerForm.getFormUseStatus()) ==0 ? 1:0);
        sxSerForm.setFormUseStatus(useStatus);
        int index = dbSxSerFormMapper.updateByPrimaryKeySelective(sxSerForm);

        return index;

    }

    /**
     * 新增事项关联的表单
     * @param serForm
     */
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    public void save(SxSerForm serForm) {
        if (ObjectUtils.isEmpty(serForm)) {
            return;
        }
        DbSxSerForm dbSxSerForm = new DbSxSerForm();
        BeanUtils.copyProperties(serForm, dbSxSerForm);
        //先查询表里数据最大排序号
        DbSxSerFormExample dbSxSerFormExample = new DbSxSerFormExample();
        DbSxSerFormExample.Criteria criteria = dbSxSerFormExample.createCriteria();
        criteria.andDelFlagEqualTo( 0);
        criteria.andServiceOidEqualTo(serForm.getServiceOid());
        List<DbSxSerForm> dbSxSerForms = dbSxSerFormMapper.selectByExample(dbSxSerFormExample);
        if (!CollectionUtils.isEmpty(dbSxSerForms)) {
            dbSxSerForms.stream().sorted(Comparator.comparing(DbSxSerForm::getSort)).collect(Collectors.toList());
            //排序自增
            dbSxSerForm.setSort(dbSxSerForms.get(dbSxSerForms.size()-1).getSort() + 1);
        } else {
            dbSxSerForm.setSort(Long.valueOf("1"));
        }
        if(StringUtils.isEmpty(dbSxSerForm.getOid())){
            //设置业务主键
            dbSxSerForm.setOid(UUIDUtil.randomUUID());
        }
        //修改的，先判断当前修改后的表单类型，若之前是电子表单修改后为自定义表单则保留原数据新增一条默认禁用的表单配置，反之亦然
        if ("1".equals(serForm.getFormType().toString())) {
            dbSxSerForm.setFormCode(serForm.getFormCode());
            dbSxSerForm.setFormText(serForm.getFormText());
            dbSxSerForm.setIsFormFlag(serForm.getIsFormFlag());
            //清空定制化页面的数据
            dbSxSerForm.setFormAddr(null);
        } else {
            dbSxSerForm.setFormAddr(serForm.getFormAddr());
            //重置电子表单数据
            dbSxSerForm.setFormCode(null);
            dbSxSerForm.setFormText(null);
            dbSxSerForm.setIsFormFlag(Short.valueOf("1"));
        }
        //事项表单就将套餐目录主键置为空
        if (StringUtils.isEmpty(serForm.getComboDirectoryOid())) {
            dbSxSerForm.setComboDirectoryOid(null);
        }
        //表单来源
        dbSxSerForm.setFormSource(Short.valueOf("0"));
        dbSxSerForm.setDelFlag(Short.valueOf("0"));
        dbSxSerForm.setModifyDate(new Date());
        if(dbSxSerForm.getId() != null){
            this.dbSxSerFormMapper.updateByPrimaryKeySelective(dbSxSerForm);
        }else{
            //默认禁用
            dbSxSerForm.setFormUseStatus(Short.valueOf("0"));
            this.dbSxSerFormMapper.insert(dbSxSerForm);
        }
        //this.dbSxSerFormMapper.insert(dbSxSerForm);
    }



    /**
     * 新增 t_ser_form 数据
     *
     * t_ser_form：与事项关联的表单 ， 无 comboDirectoryOid 字段值，
     *              与套餐关联的表单，无 serviceOid 字段值
     *              若单条数据中 ，comboDirectoryOid 和 serviceOid 这俩字段都有值且 delFlag为0，则表示 此表单是某套餐下生效的事项关联的表单
     *
     * @param sxSerForms
     */
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    public void saveSxSerFormByList(List<SxSerForm> sxSerForms) {
        //判空
        if(CollectionUtils.isEmpty(sxSerForms)){
            return;
        }
        for(SxSerForm sxSerForm:sxSerForms){
            DbSxSerForm dbSxSerForm = new DbSxSerForm();
            BeanUtils.copyProperties(sxSerForm, dbSxSerForm);
            //设置业务主键
            dbSxSerForm.setOid(UUIDUtil.randomUUID());
            dbSxSerForm.setModifyDate(new Date());
            this.dbSxSerFormMapper.insert(dbSxSerForm);
        }
    }

    /**
     * 单条数据更新
     * @param sxSerForm
     */
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    public void updateSxSerForm(SxSerForm sxSerForm) {
        //判空
        if(null == sxSerForm){
            return;
        }
        DbSxSerForm dbSxSerForm = new DbSxSerForm();
        dbSxSerForm.setOid(sxSerForm.getOid());
        //delflag = 1 ,就将该实施清单关联的表单置为该套餐下失效的表单
        dbSxSerForm.setDelFlag((short)1);
        this.dbSxSerFormMapper.updateByPrimaryKeySelective(dbSxSerForm);
    }

    public List<SxSerForm> selectBySxSerFormByServiceOid(String serviceOid) {
        List<DbSxSerForm> dbSxSerFormList = dbSxSerFormMapper.selectBySxSerFormByServiceOid(serviceOid);
        if(dbSxSerFormList!=null&&dbSxSerFormList.size()>0){
            return BeanUtils.copyListProperties(dbSxSerFormList, SxSerForm::new);
        }else{
            return null;
        }
    }

    public List<SxSerForm> selectBySxSerForm(SxSerForm sxSerForm) {
        DbSxSerFormExample dbSxSerFormExample = new DbSxSerFormExample();
        DbSxSerFormExample.Criteria criteria = dbSxSerFormExample.createCriteria();
        if(null!=sxSerForm){
            if(StrUtil.isNotEmpty(sxSerForm.getComboDirectoryOid())){
                criteria.andComboDirectoryOidEqualTo(sxSerForm.getComboDirectoryOid());
            }
            if(StrUtil.isNotEmpty(sxSerForm.getServiceOid())){
                criteria.andServiceOidEqualTo(sxSerForm.getServiceOid());
            }
            criteria.andFormUseStatusEqualTo(1);
            criteria.andDelFlagEqualTo(0);
        }
        List<DbSxSerForm> dbCaseMaterial=dbSxSerFormMapper.selectByExample(dbSxSerFormExample);
        if(dbCaseMaterial!=null&&dbCaseMaterial.size()>0){
            return BeanUtils.copyListProperties(dbCaseMaterial, SxSerForm::new);
        }else{
            return null;
        }

    }

    /**
     * @description 根据事项oid主键获取分页数据-获取实施清单目录
     * @param oidStrs, pageNumber, pageSize
     * @return com.zfsoft.platform.common.data.PageResult<com.zfsoft.data.yxpz.SxSerForm>
     * @author chenjm
     * @date 2021/4/8 11:32
     **/
    public PageResult<SxSerForm> getSerFormsByServiceOidStrs(List<String> oidStrs,String comboDirectoryOid, Integer pageNumber, Integer pageSize) {
        List<SxSerForm> sxSerFormList =new ArrayList<>();
        PageResult<SxSerForm> pageResult=new PageResult<>();
        if(null!=oidStrs &&oidStrs.size()>=1){
            //分页参数
            if (null == pageNumber || pageNumber <= 0) {
                pageNumber = 0;
            }
            if (null == pageSize || pageSize <= 0) {
                pageSize = 10;
            }
            PageHelper.startPage(pageNumber,pageSize);
            DbSxSerFormExample dbSxSerFormExample = new DbSxSerFormExample();
            DbSxSerFormExample.Criteria criteria = dbSxSerFormExample.createCriteria();
            criteria.andDelFlagEqualTo(0);
            criteria.andFormUseStatusEqualTo(1);
            criteria.andServiceOidIn(oidStrs);
            criteria.andComboDirectoryOidIsNull();
            criteria.andOidNotInComboDirectoryOid(comboDirectoryOid);
            Page<DbSxSerForm> dbSxSerForms = (Page<DbSxSerForm>)dbSxSerFormMapper.selectByExample(dbSxSerFormExample);
             pageResult = new PageResult<>(dbSxSerForms.getPageNum(),dbSxSerForms.getPageSize(),dbSxSerForms.getTotal());
            sxSerFormList = dbSxSerForms.stream().map(dbSerForm -> {
                SxSerForm serForm = new SxSerForm();
                BeanUtils.copyProperties(dbSerForm,serForm);
                return serForm;
            }).collect(Collectors.toList());
            //表单类型、表单状态数值转换
            for(SxSerForm serForm:sxSerFormList){
                serForm.setFormTypeName(serForm.getFormType() == 1?"电子表单":"定制化页面");
                serForm.setFormUseStatusName(serForm.getFormUseStatus() == 1?"启用":"禁用");
            }
        }
        pageResult.setData(sxSerFormList);
        return pageResult;
    }


    /**
     * 删除
     * @param oid
     * @return
     */
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    public int delSxSerFormById(String oid) {

        //查询表里状态
        DbSxSerForm sxSerForm = dbSxSerFormMapper.selectBySxSerFormOid(oid);

        if(ObjectUtils.isEmpty(sxSerForm)){
            return 0;
        }
        sxSerForm.setDelFlag((short) 1);
        int index = dbSxSerFormMapper.updateByPrimaryKeySelective(sxSerForm);

        return index;

    }

}
