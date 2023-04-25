package com.zfsoft.superwindow.manager.clzs;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.BaseFormField;
import com.zfsoft.superwindow.dbaccess.dao.itfr.DbEntityBasicFormFieldMapper;
import com.zfsoft.superwindow.dbaccess.data.itfr.DbEntityBasicFormField;
import com.zfsoft.superwindow.dbaccess.data.itfr.DbEntityBasicFormFieldExample;
import com.zfsoft.superwindow.util.StringUtils;
import com.zfsoft.superwindow.util.SysCode;
import com.zfsoft.superwindow.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @ClassName MaterialCategoryManager
 * @Description: 材料智审实现类
 * @Author liangss
 * @Date 2020-11-03 10:46:29
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BasicFormFieldManager {

    //材料类别
    @Resource
    private DbEntityBasicFormFieldMapper dbEntityBasicFormFieldMapper;


    /**
     * 分页查询列表
     * @param fieldKey
     * @param fieldName
     * @param fieldType
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResult<DbEntityBasicFormField> queryInfoWithPage
            (String fieldKey, String fieldName, String fieldType, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbEntityBasicFormFieldExample dbEntityBasicFormFieldExample=new DbEntityBasicFormFieldExample();
        dbEntityBasicFormFieldExample.setOrderByClause(" CREATE_DATE DESC ");
        DbEntityBasicFormFieldExample.Criteria criteria=dbEntityBasicFormFieldExample.createCriteria();
        if (StringUtils.isNotEmpty(fieldKey)) {
            criteria.andFieldKeyLike("%"+fieldKey.trim()+"%");
        }

        if (StringUtils.isNotEmpty(fieldName)) {
            criteria.andFieldNameLike("%"+fieldName.trim()+"%");
        }

        if (StringUtils.isNotEmpty(fieldType)) {
            criteria.andFieldTypeEqualTo(fieldType);
        }
        criteria.andDeleteFalgEqualTo(String.valueOf(SysCode.DELETE_STATUS.NO));
        Page<DbEntityBasicFormField> dbEntityBasicFormFields = (Page<DbEntityBasicFormField>)dbEntityBasicFormFieldMapper.selectByExample(dbEntityBasicFormFieldExample);
        PageResult<DbEntityBasicFormField> pageResult = new PageResult<>(dbEntityBasicFormFields.getPageNum(),dbEntityBasicFormFields.getPageSize(),dbEntityBasicFormFields.getTotal());
        pageResult.setData(dbEntityBasicFormFields);
        return pageResult;
    }


    public void saveOrUpdate(BaseFormField baseFormField) {
        if (null!=baseFormField.getId()) {
            DbEntityBasicFormField dbEntityBasicFormField=this.dbEntityBasicFormFieldMapper.selectByPrimaryKey(baseFormField.getId());
            Assert.notNull(dbEntityBasicFormField, MessageFormat.format("更新对象不存在！对象id为{0}", dbEntityBasicFormField.getId()));
            BeanUtils.copyProperties(baseFormField, dbEntityBasicFormField);
            dbEntityBasicFormField.setModifyDate(new Date());
            this.dbEntityBasicFormFieldMapper.updateByPrimaryKeySelective(dbEntityBasicFormField);
        } else {
            DbEntityBasicFormField dbEntityBasicFormField=new DbEntityBasicFormField();
            BeanUtils.copyProperties(baseFormField, dbEntityBasicFormField);
            dbEntityBasicFormField.setDeleteFalg(String.valueOf(SysCode.DELETE_STATUS.NO));
            dbEntityBasicFormField.setCreateDate(new Date());
            dbEntityBasicFormField.setModifyDate(new Date());
            dbEntityBasicFormField.setOid(UUIDUtil.randomUUID());
            this.dbEntityBasicFormFieldMapper.insert(dbEntityBasicFormField);
        }
    }

    /**
     * 根据主键查询信息
     * @param id
     * @return
     */
    public DbEntityBasicFormField getOne(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbEntityBasicFormField dbMaterialCategory=this.dbEntityBasicFormFieldMapper.selectByPrimaryKey(Long.valueOf(id));
        return dbMaterialCategory;
    }


    /**
     * 根据主键删除登记信息
     * @param id
     */
    public void del(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbEntityBasicFormField dbEntityBasicFormField=this.dbEntityBasicFormFieldMapper.selectByPrimaryKey(Long.valueOf(id));
        Assert.notNull(dbEntityBasicFormField, MessageFormat.format("操作对象不存在！对象id为{0}", id));
        dbEntityBasicFormField.setDeleteFalg(String.valueOf(SysCode.DELETE_STATUS.YES));
        this.dbEntityBasicFormFieldMapper.updateByPrimaryKeySelective(dbEntityBasicFormField);
    }

    /**
     *  判断所属类型有没有同一个编码
     * @param fieldKey
     * @param fieldType
     * @return
     */
    public boolean checkTypeField(String fieldKey, String fieldType) {
        DbEntityBasicFormFieldExample dbEntityBasicFormFieldExample=new DbEntityBasicFormFieldExample();
        DbEntityBasicFormFieldExample.Criteria criteria=dbEntityBasicFormFieldExample.createCriteria();
        if (StringUtils.isNotEmpty(fieldKey)) {
            criteria.andFieldKeyEqualTo(fieldKey);
        }
        if (StringUtils.isNotEmpty(fieldType)) {
            criteria.andFieldTypeEqualTo(fieldType);
        }
        criteria.andDeleteFalgEqualTo(String.valueOf(SysCode.DELETE_STATUS.NO));
        List<DbEntityBasicFormField> dbEntityBasicFormFields = dbEntityBasicFormFieldMapper.selectByExample(dbEntityBasicFormFieldExample);
        if (dbEntityBasicFormFields !=null && dbEntityBasicFormFields.size()>0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据类型获取字段list
     * @param type
     * @return
     */
    public List<DbEntityBasicFormField> getBasicFieldListByType(String type) {
        DbEntityBasicFormFieldExample dbEntityBasicFormFieldExample = new DbEntityBasicFormFieldExample();
        DbEntityBasicFormFieldExample.Criteria criteria = dbEntityBasicFormFieldExample.createCriteria();
        if (StringUtils.isNotEmpty(type)) {
            criteria.andFieldTypeEqualTo(type);
            criteria.andDeleteFalgEqualTo(String.valueOf(SysCode.DELETE_STATUS.NO));
            return dbEntityBasicFormFieldMapper.selectByExample(dbEntityBasicFormFieldExample);
        } else {
            return null;
        }
    }

    /**
     * 根据条件查询配置的基础表单信息
     * @param baseFormField
     * @return
     */
    public List<BaseFormField> queryBaseFormFieldList(BaseFormField baseFormField) {
        DbEntityBasicFormFieldExample example=new DbEntityBasicFormFieldExample();
        DbEntityBasicFormFieldExample.Criteria criteria=example.createCriteria();
        if(baseFormField!=null){
            if(StringUtils.isNotEmpty(baseFormField.getFieldType())){
                criteria.andFieldTypeEqualTo(baseFormField.getFieldType());
            }
        }
        criteria.andDeleteFalgEqualTo(String.valueOf(SysCode.DELETE_STATUS.NO));
        example.setOrderByClause(" CREATE_DATE DESC ");
        List<DbEntityBasicFormField> dbEntityBasicFormFields = dbEntityBasicFormFieldMapper.selectByExample(example);
        if(dbEntityBasicFormFields!=null){
            List<BaseFormField> list= dbEntityBasicFormFields.stream().map(fields->{
                BaseFormField formField=new BaseFormField();
                BeanUtils.copyProperties(fields,formField);
                return formField;
            }).collect(Collectors.toList());
            return list;
        }
        return null;
    }

    public BaseFormField selectByOid(String oid){
      DbEntityBasicFormField formfield=  dbEntityBasicFormFieldMapper.selectByOid(oid);
      if(formfield!=null){
          BaseFormField baseField=new BaseFormField();
          BeanUtils.copyProperties(formfield,baseField);
          return baseField;
      }
      return null;
    }

    public String checkHasRepeat(String fieldType, String fieldKey, String oid) {
        String result = "false";
        //查询数据
        DbEntityBasicFormFieldExample example=new DbEntityBasicFormFieldExample();
        DbEntityBasicFormFieldExample.Criteria criteria=example.createCriteria();
        if (StringUtils.isNotEmpty(fieldType)) {
            criteria.andFieldTypeEqualTo(fieldType);
        }
        if (StringUtils.isNotEmpty(fieldKey)) {
            criteria.andFieldKeyEqualTo(fieldKey);
        }
        criteria.andDeleteFalgEqualTo(String.valueOf(SysCode.DELETE_STATUS.NO));
        List<DbEntityBasicFormField> dbEntityBasicFormFields = dbEntityBasicFormFieldMapper.selectByExample(example);
        if (StringUtils.isEmpty(oid)) {  //新增
            if (dbEntityBasicFormFields !=null && dbEntityBasicFormFields.size()>0) {
                result = "true";
            }
        } else {
            if (dbEntityBasicFormFields !=null && dbEntityBasicFormFields.size()>0) {
                for (DbEntityBasicFormField dbEntityBasicFormField: dbEntityBasicFormFields) {
                    if (!dbEntityBasicFormField.getOid().equals(oid)) {
                        result = "true";
                        break;
                    }
                }
            }
        }
        return result;
    }

}
