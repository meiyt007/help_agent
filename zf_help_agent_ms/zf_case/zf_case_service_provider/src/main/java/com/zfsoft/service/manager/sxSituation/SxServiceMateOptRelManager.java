package com.zfsoft.service.manager.sxSituation;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.service.dbaccess.dao.sxSituation.DbSxServiceMateOptRelMapper;
import com.zfsoft.service.dbaccess.data.sxSituation.DbSxServiceMateOptRel;
import com.zfsoft.service.dbaccess.data.sxSituation.DbSxServiceMateOptRelExample;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.sxService.data.SxServiceMaterial;
import com.zfsoft.service.sxSituation.data.SxServiceMateOptRel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author wangns
 * @description 细化材料选项值关系 实现类
 * @date 2020/11/2 17:20
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
public class SxServiceMateOptRelManager {

    @Resource
    private DbSxServiceMateOptRelMapper dbSxServiceMateOptRelMapper;

    /**
     * 查询单条记录
     * @param oid 业务主键
     * @return
     */
    public SxServiceMateOptRel getSxServiceMateOptRelByService (String oid){
        DbSxServiceMateOptRel dbSxServiceMateOptRel = dbSxServiceMateOptRelMapper.selectByOid(oid);
        SxServiceMateOptRel sxServiceMateOptRel = null ;
        if (null == dbSxServiceMateOptRel) {
            //没查出数据，不抛异常
//            throw  new ResultInfoException("根据业务主键OID=【"+oid+"】，查询细化材料选项值关系表单为空！");
        }else{
            sxServiceMateOptRel = new SxServiceMateOptRel();
            BeanUtils.copyProperties(dbSxServiceMateOptRel,sxServiceMateOptRel);
        }
        return sxServiceMateOptRel;
    }

    /**
     * 查询集合
     * @param optionOid 选项值业务主键
     * @param materialOid 细化材料业务主键
     * @param sxMaterialOid 事项材料业务主键
     * @return
     */
    public List<SxServiceMateOptRel> getSxServiceMateOptRelsByOids(String optionOid, String materialOid, String sxMaterialOid) {
        DbSxServiceMateOptRelExample dbSxServiceMateOptRelExample = new DbSxServiceMateOptRelExample();
        DbSxServiceMateOptRelExample.Criteria criteria = dbSxServiceMateOptRelExample.createCriteria();
        if(StrUtil.isNotEmpty(optionOid)){
            criteria.andOptionOidEqualTo(optionOid);
        }
        if(StrUtil.isNotEmpty(materialOid)){
            criteria.andMaterialOidEqualTo(materialOid);
        }
        if(StrUtil.isNotEmpty(sxMaterialOid)){
            criteria.andSxMaterialOidEqualTo(sxMaterialOid);
        }
        criteria.andDeleteStatusEqualTo("0");
        List<DbSxServiceMateOptRel> dbSxServiceMateOptRels = dbSxServiceMateOptRelMapper.selectByExample(dbSxServiceMateOptRelExample);
        List<SxServiceMateOptRel> sxServiceMateOptRels = dbSxServiceMateOptRels.stream().map(sxServiceMateOptRel -> {
            SxServiceMateOptRel serviceMateOptRel = new SxServiceMateOptRel();
            BeanUtils.copyProperties(sxServiceMateOptRel,serviceMateOptRel);
            return serviceMateOptRel;
        }).collect(Collectors.toList());
        return sxServiceMateOptRels;
    }

    public List<SxServiceMateOptRel> getSxServiceMateOptRelsByOptionValOid(String optionValOid) {
        if(StrUtil.isEmpty(optionValOid)) {
            throw new ResultInfoException("选项值主键为空！");
        }
        List<DbSxServiceMateOptRel> dbSxServiceMateOptRels = dbSxServiceMateOptRelMapper.selectMateOptRelsByOptionValOid(optionValOid);
        List<SxServiceMateOptRel> sxServiceMateOptRels = dbSxServiceMateOptRels.stream().map(sxServiceMateOptRel -> {
            SxServiceMateOptRel serviceMateOptRel = new SxServiceMateOptRel();
            BeanUtils.copyProperties(sxServiceMateOptRel,serviceMateOptRel);
            return serviceMateOptRel;
        }).collect(Collectors.toList());
        return sxServiceMateOptRels;
    }

    public void saveOrUpdateMaterOptRel(String valOid, String materialOids){
        if(StringUtil.isNotEmpty(valOid)){
            //查询所有关联信息删除
            List<DbSxServiceMateOptRel> list= dbSxServiceMateOptRelMapper.selectMateOptRelsByOptionValOid(valOid);
            if(list!=null &&list.size()>0){
                for (DbSxServiceMateOptRel rel:list){
                    dbSxServiceMateOptRelMapper.deleteByPrimaryKey(rel.getOid());
                }
            }
            //重新保存所有的关联信息
            if(StringUtil.isNotEmpty(materialOids)){
                String[] materialArr=materialOids.split(",");
                for(String materialoid:materialArr){
                    DbSxServiceMateOptRel optRel=new DbSxServiceMateOptRel();
                    optRel.setCreateDate(new Date());
                    optRel.setDeleteStatus("0");
                    optRel.setModifyDate(new Date());
                    optRel.setOptionOid(valOid);
                    optRel.setSxMaterialOid(materialoid);
                    optRel.setOid(UUID.randomUUID().toString().replaceAll("-",""));
                    dbSxServiceMateOptRelMapper.insert(optRel);
                }
            }
        }
    }
}
