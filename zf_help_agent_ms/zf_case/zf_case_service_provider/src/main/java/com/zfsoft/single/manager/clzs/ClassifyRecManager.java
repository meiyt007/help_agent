package com.zfsoft.single.manager.clzs;

import cn.hutool.core.util.IdUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.single.data.clzs.ClassifyRec;
import com.zfsoft.single.dbaccess.dao.DbClassifyRecMapper;
import com.zfsoft.single.dbaccess.data.DbClassifyRec;
import com.zfsoft.single.dbaccess.data.DbClassifyRecExample;
import com.zfsoft.single.util.BeanUtils;
import com.zfsoft.single.util.SysCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;


/**
 * @ClassName OcrRecordManager
 * @Description: 材料分类列表
 * @Author liangss
 * @Date 2020-12-14 15:46:29
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClassifyRecManager {
    @Resource
    private DbClassifyRecMapper dbClassifyRecMapper;




    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public void saveOrUpdate(ClassifyRec industryCassifyRec) {
        DbClassifyRec dbClassifyRec = null;
        if (null != industryCassifyRec && null != industryCassifyRec.getId()) {
            dbClassifyRec=this.dbClassifyRecMapper.selectByPrimaryKey(industryCassifyRec.getId());
            org.springframework.util.Assert.notNull(dbClassifyRec, MessageFormat.format("更新对象不存在！对象id为{0}", dbClassifyRec.getId()));
            BeanUtils.copyProperties(industryCassifyRec, dbClassifyRec);
            dbClassifyRec.setModifyDate(new Date());
            this.dbClassifyRecMapper.updateByPrimaryKeySelective(dbClassifyRec);
        } else {
            dbClassifyRec=new DbClassifyRec();
            BeanUtils.copyProperties(industryCassifyRec, dbClassifyRec);
            dbClassifyRec.setOid(IdUtil.simpleUUID());
            dbClassifyRec.setModifyDate(new Date());
            //dbElement.setStatus(SysCode.DELETE_STATUS.NO);//暂存
            dbClassifyRec.setDelFlag(SysCode.DELETE_STATUS.NO);

            dbClassifyRec.setCreateDate(new Date());
            this.dbClassifyRecMapper.insert(dbClassifyRec);
            //dbIndustryElement.setCreateUser(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString());

        }
        industryCassifyRec.setId(dbClassifyRec.getId());
        industryCassifyRec.setOid(dbClassifyRec.getOid());
    }

    /**
     * 根据业务主键查询材料分类表
     * @param oid
     * @return
     */
    public ClassifyRec queryDbClassifyRecByOid(String oid){
            if (StringUtil.isEmpty(oid)) {
                throw new ResultInfoException("Oid为空！");
            }
            DbClassifyRec dbClassifyRec= dbClassifyRecMapper.queryDbClassifyRecByOid(oid);
            ClassifyRec classifyRec  = null;
            if(classifyRec !=null){
                classifyRec  = new ClassifyRec ();
                BeanUtils.copyProperties(dbClassifyRec, classifyRec);
            }
            return classifyRec;
    }

    /**
     * 根据id删除
     * @param id
     */
    public void del(String id) {
        org.springframework.util.Assert.hasLength(id, "主键不能为空！");
        DbClassifyRec dbClassifyRec=this.dbClassifyRecMapper.selectByPrimaryKey(Long.valueOf(id));
        org.springframework.util.Assert.notNull(dbClassifyRec, MessageFormat.format("操作对象不存在！对象id为{0}", id));
        dbClassifyRec.setDelFlag(SysCode.DELETE_STATUS.YES);
        this.dbClassifyRecMapper.updateByPrimaryKeySelective(dbClassifyRec);

    }

    /**
     * 根据条件查询列表
     * @param
     * @return
     */
    public List<ClassifyRec> queryClassifyRecList(ClassifyRec classifyRec) {
        DbClassifyRecExample dbClassifyRecExample=new DbClassifyRecExample();
        DbClassifyRecExample.Criteria criteria=dbClassifyRecExample.createCriteria();

        if(null!=classifyRec){
            if(null!=classifyRec.getClassifierStatus()){
                criteria.andClassifierStatusEqualTo(classifyRec.getClassifierStatus());
            }
            if(null!=classifyRec.getCaseMaterialOid()){
                criteria.andCaseMaterialOidEqualTo(classifyRec.getCaseMaterialOid());
            }
            if(null!=classifyRec.getCaseOid()){
                criteria.andCaseOidEqualTo(classifyRec.getCaseOid());
            }
            if(null!=classifyRec.getAttaOid()){
                criteria.andAttaOidEqualTo(classifyRec.getAttaOid());
            }
            if(null!=classifyRec.getClassifierId()){
                criteria.andClassifierIdEqualTo(classifyRec.getClassifierId());
            }

        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        dbClassifyRecExample.setOrderByClause(" MODIFY_DATE DESC ");
        List<DbClassifyRec>  dbClassifyRecList=this.dbClassifyRecMapper.selectByExample(dbClassifyRecExample);
        return BeanUtils.copyListProperties(dbClassifyRecList, ClassifyRec::new);

    }



    /*    public String saveClassifyRec(ReqSaveClassifyRecDto saveClassifyRecDto) {
        Assert.notNull(saveClassifyRecDto, "保存分类记录请求不能为空");
        Assert.notBlank(saveClassifyRecDto.getCaseOid(), "办件主键为空");
        Assert.notBlank(saveClassifyRecDto.getAttaOid(), "附件主键不能为空");
        Assert.notBlank(saveClassifyRecDto.getClassifierId(), "分类器id为空");

        return null;
    }
*/
}
