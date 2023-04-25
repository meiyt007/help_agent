package com.zfsoft.microservice.form.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.form.data.FormColumn;
import com.zfsoft.microservice.form.data.FormObjectExtand;
import com.zfsoft.microservice.form.dbaccess.dao.DbFormObjectExtandMapper;
import com.zfsoft.microservice.form.dbaccess.dao.DbFormObjectMapper;
import com.zfsoft.microservice.form.dbaccess.data.DbFormObject;
import com.zfsoft.microservice.form.dbaccess.data.DbFormObjectExtand;
import com.zfsoft.microservice.form.dbaccess.data.DbFormObjectExtandExample;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 存储对象扩展逻辑实现层
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "form:objectExtand")
public class FormObjectExtandManager {

    @Resource
    private DbFormObjectMapper dbFormObjectMapper;

    @Resource
    private DbFormObjectExtandMapper dbFormObjectExtandMapper;

    @Autowired
    private FormColumnManager formColumnManager;

    /**
     * 保存扩展对象
     *
     * @param formObjectExtand
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @CacheEvict(allEntries = true)
    public int saveFormObjectExtand(@ValidGroups(groups = {FormObjectExtand.INSERT_GROUP.class}) FormObjectExtand formObjectExtand) {
        if (null == formObjectExtand) {
            throw new ResultInfoException("存储对象不能为空！");
        }
        if (null == formObjectExtand.getId()) {
            formObjectExtand.setId(null);
            formObjectExtand.setExtandOid(IdUtil.simpleUUID());
        } else {
            // 对象不为空
            FormObjectExtand objectExtand = this.getFormObjectExtandById(formObjectExtand.getId());
            if (objectExtand == null) {
                throw new ResultInfoException("存储对象未查询到相应的信息!");
            }
        }
        if (StrUtil.isBlank(formObjectExtand.getMainObjectOid())) {
            throw new ResultInfoException("存储对象不能为空！");
        }
        DbFormObject object = dbFormObjectMapper.selectByForeignKey(formObjectExtand.getMainObjectOid());
        if (object == null) {
            throw new ResultInfoException("存储对象不能为空！");
        }
        // 设置信息的状态
        if (null == formObjectExtand.getIsDelete()) {
            formObjectExtand.setIsDelete(BaseStaticParameter.N);
        }
        if (null == formObjectExtand.getCreateDate()) {
            formObjectExtand.setCreateDate(new Date());
        }
        DbFormObjectExtand dbFormObjectExtand = new DbFormObjectExtand();
        BeanUtils.copyProperties(formObjectExtand, dbFormObjectExtand);
        int index = 0;
        if (null == formObjectExtand.getId()) {
            index = dbFormObjectExtandMapper.insert(dbFormObjectExtand);
        } else {
            index = dbFormObjectExtandMapper.updateByPrimaryKeySelective(dbFormObjectExtand);
        }
        //保存扩展对象列信息
        if (StrUtil.isBlank(object.getDatasourceOid()) && StrUtil.isBlank(dbFormObjectExtand.getSecondaryObjectOid())) {
            formColumnManager.deleteFormColumnByObjectOid(dbFormObjectExtand.getExtandOid());
            if (null != formObjectExtand.getColumnList()) {
                for (FormColumn formColumn : formObjectExtand.getColumnList()) {
                    formColumn.setId(null);
                    formColumn.setObjectOid(dbFormObjectExtand.getMainObjectOid());
                    formColumn.setExtandOid(dbFormObjectExtand.getExtandOid());
                    formColumnManager.saveFormColumn(formColumn);
                }
            }
        }

        return index;
    }

    /**
     * 根据主键获取
     *
     * @param id
     * @return
     */
    @Cacheable(key = "'getFormObjectExtandById:'+#id", unless = "#result == null")
    public FormObjectExtand getFormObjectExtandById(Long id) {
        DbFormObjectExtand dbFormObjectExtand = dbFormObjectExtandMapper.selectByPrimaryKey(id);
        if (dbFormObjectExtand == null) {
            return null;
        }
        FormObjectExtand formObjectExtand = new FormObjectExtand();
        BeanUtils.copyProperties(dbFormObjectExtand, formObjectExtand);
        return formObjectExtand;
    }

    /**
     * 删除扩展对象
     *
     * @param objectOid
     * @return
     */
    @CacheEvict(allEntries = true)
    public int deleteFormExtandByObjectOid(String objectOid) {
        if (StrUtil.isBlank(objectOid)) {
            return 0;
        }
        List<FormObjectExtand> formObjectExtands = queryFormObjectExtandList(objectOid,null);
        if (null != null) {
            for (FormObjectExtand formObjectExtand : formObjectExtands) {
                formColumnManager.deleteFormColumnByExtandOid(formObjectExtand.getExtandOid());
            }
        }
        int delete = dbFormObjectExtandMapper.deleteByMainObjectOid(objectOid);
//        DbFormObjectExtandExample dbFormObjectExtandExample = new DbFormObjectExtandExample();
//        DbFormObjectExtandExample.Criteria criteria = dbFormObjectExtandExample.createCriteria();
//        criteria.andMainObjectOidEqualTo(objectOid);
//        int delete = dbFormObjectExtandMapper.deleteByExample(dbFormObjectExtandExample);
        return delete;
    }

    /**
     * 根据业务主键获取对象信息
     *
     * @param extandOid
     * @return
     */
    @Cacheable(key = "'getFormObjectExtandByExtandOid:'+#extandOid", unless = "#result == null")
    public FormObjectExtand getFormObjectExtandByExtandOid(String extandOid) {
        if (StrUtil.isBlank(extandOid)) {
            return null;
        }
        DbFormObjectExtand dbFormObjectExtand = dbFormObjectExtandMapper.selectByExtandOid(extandOid);
        if (dbFormObjectExtand == null) {
            return null;
        }
        FormObjectExtand formObjectExtand = new FormObjectExtand();
        BeanUtils.copyProperties(dbFormObjectExtand, formObjectExtand);
        return formObjectExtand;
    }

    /**
     * 查询关联对象集合
     *
     * @param objectOid 对象主键
     * @param type      类型 1数组、2对象
     * @return
     */
    public List<FormObjectExtand> queryFormObjectExtandList(String objectOid, Integer type) {
        DbFormObjectExtandExample dbFormObjectExtandExample = new DbFormObjectExtandExample();
        DbFormObjectExtandExample.Criteria criteria = dbFormObjectExtandExample.createCriteria();
        dbFormObjectExtandExample.setOrderByClause(" ID DESC ");
        if (StrUtil.isNotEmpty(objectOid)) {
            criteria.andMainObjectOidEqualTo(objectOid);
        }
        if (null != type) {
            criteria.andTypeEqualTo(type);
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbFormObjectExtand> dbFormObjectExtands = dbFormObjectExtandMapper.selectByExample(dbFormObjectExtandExample);
        List<FormObjectExtand> list = dbFormObjectExtands.stream().map(dbFormObjectExtand -> {
            FormObjectExtand formObjectExtand = new FormObjectExtand();
            BeanUtils.copyProperties(dbFormObjectExtand, formObjectExtand);
            return formObjectExtand;
        }).collect(Collectors.toList());

        DbFormObject object = dbFormObjectMapper.selectByForeignKey(objectOid);
        if (null != object && StrUtil.isBlank(object.getDatasourceOid())) {
            FormColumn formColumn = new FormColumn();
            formColumn.setObjectOid(objectOid);
            List<FormColumn> datasourceColumn = formColumnManager.queryFormObjectExtandColumnList(formColumn);
            list = list.stream().map(extand -> {
                if (datasourceColumn != null) {
                    datasourceColumn.stream().forEach(column -> {
                        if (extand.getExtandOid().equals(column.getExtandOid())) {
                            List<FormColumn> columnList = extand.getColumnList();
                            if (columnList == null) {
                                columnList = new ArrayList<>();
                            }
                            columnList.add(column);
                            extand.setColumnList(columnList);
                        }
                    });
                }
                return extand;
            }).collect(Collectors.toList());
        } else {
            list = list.stream().map(extand -> {
                FormColumn formColumn = new FormColumn();
                formColumn.setObjectOid(extand.getSecondaryObjectOid());
                List<FormColumn> datasourceColumn = formColumnManager.queryFormColumnList(formColumn);
                extand.setColumnList(datasourceColumn);
                return extand;
            }).collect(Collectors.toList());
        }
        return list;
    }

    public List<FormObjectExtand> queryFormObjectExtandList(String mainObjectOid,String secondaryObjectOid,String variableName,String foreignKey,Integer type){
        DbFormObjectExtandExample dbFormObjectExtandExample = new DbFormObjectExtandExample();
        DbFormObjectExtandExample.Criteria criteria = dbFormObjectExtandExample.createCriteria();
        dbFormObjectExtandExample.setOrderByClause(" ID DESC ");
        if (StrUtil.isNotEmpty(mainObjectOid)) {
            criteria.andMainObjectOidEqualTo(mainObjectOid);
        }
        if (StrUtil.isNotEmpty(secondaryObjectOid)) {
            criteria.andSecondaryObjectOidEqualTo(secondaryObjectOid);
        }
        if (StrUtil.isNotEmpty(variableName)) {
            criteria.andVariableNameEqualTo(variableName);
        }
        if (StrUtil.isNotEmpty(foreignKey)) {
            criteria.andForeignKeyEqualTo(foreignKey);
        }
        if (null != type) {
            criteria.andTypeEqualTo(type);
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbFormObjectExtand> dbFormObjectExtands = dbFormObjectExtandMapper.selectByExample(dbFormObjectExtandExample);
        List<FormObjectExtand> list = dbFormObjectExtands.stream().map(dbFormObjectExtand -> {
            FormObjectExtand formObjectExtand = new FormObjectExtand();
            BeanUtils.copyProperties(dbFormObjectExtand, formObjectExtand);
            return formObjectExtand;
        }).collect(Collectors.toList());
        return list;
    }

}
