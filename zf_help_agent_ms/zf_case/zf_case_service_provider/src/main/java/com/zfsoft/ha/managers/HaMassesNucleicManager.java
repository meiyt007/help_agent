package com.zfsoft.ha.managers;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.ha.data.HaMassesNucleic;
import com.zfsoft.ha.dbaccess.dao.DbHaMassesNucleicMapper;
import com.zfsoft.ha.dbaccess.data.DbHaMassesNucleic;
import com.zfsoft.ha.dbaccess.data.example.DbHaMassesNucleicExample;
import com.zfsoft.platform.utils.bean.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HaMassesNucleicManager {
    @Resource
    private DbHaMassesNucleicMapper dbHaMassesNucleicMapper;

    /**
     * 查询用户核酸消息
     *
     * @param haMassesNucleic
     * @return
     * @throws Exception
     */
    public List<HaMassesNucleic> selectByHaMassesNucleic(HaMassesNucleic haMassesNucleic, Date beginTime, Date endTime) {
        DbHaMassesNucleicExample example = new DbHaMassesNucleicExample();
        DbHaMassesNucleicExample.Criteria criteria = example.createCriteria();
        if (null != haMassesNucleic) {
            if (StrUtil.isNotEmpty(haMassesNucleic.getName())) {
                criteria.andNameLike("%" + haMassesNucleic.getName() + "%");
            }
            if (StrUtil.isNotEmpty(haMassesNucleic.getCardNo())) {
                criteria.andCardNoEqualTo(haMassesNucleic.getCardNo());
            }
            if (beginTime!=null) {
                criteria.andNucleicCollectionTimeGreaterThanOrEqualTo(beginTime);
            }
            if (endTime!=null) {
                criteria.andNucleicCollectionTimeLessThanOrEqualTo(endTime);
            }
            example.setOrderByClause("CREATE_DATE desc");
        }
        List<DbHaMassesNucleic> dbHaMassesNucleics = dbHaMassesNucleicMapper.selectByExample(example);
        List<HaMassesNucleic> haMassesNucleicList = dbHaMassesNucleics.stream().map(dbHaMassesNucleic1 -> {
            HaMassesNucleic haMassesNucleic1 = new HaMassesNucleic();
            BeanUtils.copyProperties(dbHaMassesNucleic1, haMassesNucleic1);
            return haMassesNucleic1;
        }).collect(Collectors.toList());
        return haMassesNucleicList;
    }

    public int getIsHaMassesNucleicByNucleicCode(String nucleicCode) throws Exception{
        return dbHaMassesNucleicMapper.getIsHaMassesNucleicByNucleicCode(nucleicCode);
    }

    @Transactional(rollbackFor = Exception.class)
    public HaMassesNucleic saveHaMassesNucleic(HaMassesNucleic haMassesNucleic) {
        if (null == haMassesNucleic.getId()) {
            haMassesNucleic.setId(null);
            haMassesNucleic.setCreateDate(new Date());
        }
        haMassesNucleic.setUpdateDate(new Date());
        DbHaMassesNucleic dbHaMassesNucleic = new DbHaMassesNucleic();
        BeanUtils.copyProperties(haMassesNucleic, dbHaMassesNucleic);
        int index = 0;
        if (null == haMassesNucleic.getId()) {
            index = dbHaMassesNucleicMapper.insert(dbHaMassesNucleic);
        } else {
            index = dbHaMassesNucleicMapper.update(dbHaMassesNucleic);
        }
        return haMassesNucleic;
    }
}
