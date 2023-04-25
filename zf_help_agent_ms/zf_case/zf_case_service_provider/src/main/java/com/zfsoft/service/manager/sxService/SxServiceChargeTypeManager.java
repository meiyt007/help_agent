package com.zfsoft.service.manager.sxService;

import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceChargeTypeMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceChargeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @（#）: SxServiceChargeTypeManager
 * @description: 事项收费类型实现类
 * @author: wangwg
 * @date: 2021/06/10
 * @version: 1.1
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
public class SxServiceChargeTypeManager {

    @Resource
    private DbSxServiceChargeTypeMapper dbSxServiceChargeTypeMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param chargeTypeOid 主键
     * @return 实例对象
     */
    public DbSxServiceChargeType queryById(String chargeTypeOid) {
        return this.dbSxServiceChargeTypeMapper.queryById(chargeTypeOid);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    public List<DbSxServiceChargeType> queryAllByLimit(int offset, int limit) {
        return this.dbSxServiceChargeTypeMapper.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param tSxServiceChargeType 实例对象
     * @return 实例对象
     */
    public DbSxServiceChargeType insert(DbSxServiceChargeType tSxServiceChargeType) {
        this.dbSxServiceChargeTypeMapper.insert(tSxServiceChargeType);
        return tSxServiceChargeType;
    }

    /**
     * 修改数据
     *
     * @param tSxServiceChargeType 实例对象
     * @return 实例对象
     */
    public DbSxServiceChargeType update(DbSxServiceChargeType tSxServiceChargeType) {
        this.dbSxServiceChargeTypeMapper.update(tSxServiceChargeType);
        return this.queryById(tSxServiceChargeType.getChargeTypeOid());
    }

    /**
     * 通过主键删除数据
     *
     * @param chargeTypeOid 主键
     * @return 是否成功
     */
    public boolean deleteById(String chargeTypeOid) {
        return this.dbSxServiceChargeTypeMapper.deleteById(chargeTypeOid) > 0;
    }
}
